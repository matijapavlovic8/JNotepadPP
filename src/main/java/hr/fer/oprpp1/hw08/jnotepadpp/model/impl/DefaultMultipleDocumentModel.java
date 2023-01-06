package hr.fer.oprpp1.hw08.jnotepadpp.model.impl;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.DefaultCaret;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    /**
     * List of documents.
     */
    private final List<SingleDocumentModel> documents;

    /**
     * Currently active
     */
    private SingleDocumentModel currentDocument;

    /**
     * List of listeners.
     */
    private final List<MultipleDocumentListener> listeners;

    public DefaultMultipleDocumentModel(){
        super();
        this.currentDocument = null;
        this.documents = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.addChangeListener(e -> {
            SingleDocumentModel prev = currentDocument;
            this.currentDocument = this.getSelectedIndex() != -1 ? documents.get(this.getSelectedIndex()) : null;
            this.listeners.forEach(l -> l.currentDocumentChanged(prev, currentDocument));
        });
    }

    /**
     * Gets the visual component  responsible for displaying the entire
     * {@link MultipleDocumentModel} user interface.
     * @return {@link JComponent}
     */
    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    /**
     * Creates a new document.
     * @return {@link SingleDocumentModel} instance.
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        return addDocument(null, "");
    }

    /**
     * Gets the current document.
     * @return {@link SingleDocumentModel} instance.
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        return this.currentDocument;
    }

    /**
     * Loads a document.
     * @param path {@link Path} instance of the desired document.
     * @return {@link SingleDocumentModel} instance.
     * @throws NullPointerException if given {@link Path} is null.
     */
    @Override
    public SingleDocumentModel loadDocument(Path path) {
        Objects.requireNonNull(path);
        if(!Files.isReadable(path))
            throw new IllegalArgumentException("Given path is not readable!");
        for(SingleDocumentModel doc: documents){
            if(doc.getFilePath() == null)
                continue;
            if(doc.getFilePath().equals(path)){
                SingleDocumentModel prev = currentDocument;
                currentDocument = doc;
                listeners.forEach(l -> l.currentDocumentChanged(prev, currentDocument));
                return doc;
            }
        }
        try{
            byte[] bytes = Files.readAllBytes(path);
            return addDocument(path, new String(bytes));
        } catch (IOException e){
            throw new RuntimeException("File can't be loaded!");
        }
    }

    /**
     * Saves the document.
     * @param model   {@link SingleDocumentModel} instance to be saved.
     * @param newPath {@link Path} to which the document will be saved.
     */
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        Objects.requireNonNull(model, "Model can't be null!");
        newPath = newPath == null ? model.getFilePath() : newPath;
        currentDocument.setFilePath(newPath);
        for(SingleDocumentModel doc: documents){
            if(doc.getFilePath() == null)
                continue;
            if(doc.getFilePath().equals(newPath) && !doc.equals(model)) {
                throw new IllegalArgumentException("File on this path is already opened!");
            }
        }

        try{
            Objects.requireNonNull(newPath, "Path can't be null!");
            Files.writeString(newPath, model.getTextComponent().getText());
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        this.listeners.forEach(l -> l.currentDocumentChanged(model, currentDocument));
        model.setModified(false);
        updateIcon(model);
    }

    /**
     * Closes the given document.
     * @param model {@link SingleDocumentModel} instance that is to be closed.
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = Math.max(this.documents.indexOf(model) - 1, 0);
        this.removeTabAt(index);
        this.documents.remove(model);
        if(this.documents.isEmpty())
            createNewDocument();
        this.currentDocument = this.documents.get(index);
        this.listeners.forEach(l -> l.documentRemoved(model));
        this.listeners.forEach(l -> l.currentDocumentChanged(model, currentDocument));
    }

    /**
     * Adds a {@link MultipleDocumentListener}
     * @param l added listener
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        this.listeners.add(l);
    }

    /**
     * Removes a {@link MultipleDocumentListener}
     * @param l removed listener.
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        this.listeners.remove(l);
    }

    /**
     * Gets the number of documents in the {@link MultipleDocumentModel}.
     * @return {@code int} nuber of documents.
     */
    @Override
    public int getNumberOfDocuments() {
        return this.documents.size();
    }

    /**
     * Gets {@link SingleDocumentModel} instance on the given index.
     * @param index of desired document.
     * @return {@link SingleDocumentModel} found on given index.
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        return this.documents.get(index);
    }

    /**
     * Gets the {@link SingleDocumentModel} instance with the given path.
     * @param path {@link Path} of the desired document.
     * @return {@link SingleDocumentModel} found on given path.
     * @throws NullPointerException if no such model exists.
     */
    @Override
    public SingleDocumentModel findForPath(Path path) {
        for(SingleDocumentModel doc: documents){
            if(doc.getFilePath().equals(path))
                return doc;
        }
        throw new NullPointerException("No such model exists!");
    }

    /**
     * Gets the index of the given document.
     * @param doc {@link SingleDocumentModel} whose index is being requested.
     * @return {@code int} index of the document or -1 if no such document is present.
     */
    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        int index = -1;
        for(SingleDocumentModel d: documents){
            index++;
            if(d.equals(doc))
                return index;
        }
        return -1;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     * @return an Iterator.
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    /**
     * Adds a document to current model.
     * @param path {@link Path} on which the document is found.
     * @param text {@link String} containing the text content of added document.
     * @return {@link SingleDocumentModel}
     */
    private SingleDocumentModel addDocument(Path path, String text){
        SingleDocumentModel prev = currentDocument;
        currentDocument = new DefaultSingleDocumentModel(path, text);
        documents.add(currentDocument);
        this.addTab(currentDocument.getFilePath() != null ? currentDocument.getFilePath().getFileName().toString() : "unnamed", getIcon("../../icons/green.png"), new JPanel().add(new JScrollPane(this.currentDocument.getTextComponent())));
        currentDocument.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                updateIcon(model);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                updatePath(model);
            }
        });
        currentDocument.getTextComponent().setCaret(new DefaultCaret() {
            @Override
            public void setSelectionVisible(boolean visible) {
                super.setSelectionVisible(true);
            }
            @Override
            public void setVisible(boolean visible) {
                super.setVisible(true);
            }
        });
        listeners.forEach(l -> l.documentAdded(currentDocument));
        listeners.forEach(l -> l.currentDocumentChanged(prev, currentDocument));
        return currentDocument;
    }

    private ImageIcon getIcon(String path){
        Objects.requireNonNull(path, "Path to icon can't be null!");
        try(InputStream is = this.getClass().getResourceAsStream(path)){
            if(is == null)
                throw new NullPointerException("Icon doesn't exist!" + path);

            Image icon = new ImageIcon(is.readAllBytes()).getImage();
            return new ImageIcon(icon.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (IOException e){
            throw new RuntimeException("Can't fetch icon!");
        }
    }

    /**
     * Updates the models path.
     * @param model whose path is updated.
     */
    private void updatePath(SingleDocumentModel model) {
        int index = documents.indexOf(model);
        Path path = model.getFilePath();
        setTitleAt(index, path.getFileName().toString());
        setToolTipTextAt(index, path.toString());
    }

    /**
     * Updates the icon from modified to unmodified and vice-versa.
     * @param model model whose icon is updated.
     */
    private void updateIcon(SingleDocumentModel model) {
        int index = documents.indexOf(model);
        if(!model.isModified()){
            setIconAt(index, getIcon("../../icons/green.png"));
        } else {
            setIconAt(index, getIcon("../../icons/red.png"));
        }
    }
}
