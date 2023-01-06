package hr.fer.oprpp1.hw08.jnotepadpp.model.impl;

import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import java.awt.Dimension;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

    /**
     * Document path.
     */
    private Path filePath;

    /**
     * Indicates if the document has been modified.
     */
    private boolean modified;

    /**
     * List of listeners.
     */
    private final List<SingleDocumentListener> listeners;

    /**
     * Text component.
     */
    private final JTextArea textComponent;

    public DefaultSingleDocumentModel(Path filePath, String text){
        this.filePath = filePath;
        this.listeners = new ArrayList<>();
        this.textComponent = new JTextArea(text);
        this.textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });
    }


    /**
     * Gets the {@link JTextArea} component of the document model.
     * @return {@link JTextArea}
     */
    @Override
    public JTextArea getTextComponent() {
        return this.textComponent;
    }

    /**
     * Gets the path of the current document.
     * @return {@link Path} of the document.
     */
    @Override
    public Path getFilePath() {
        return this.filePath;
    }

    /**
     * Sets the path of the document.
     * @param path {@link Path} instance representing updated file path.
     */
    @Override
    public void setFilePath(Path path) {
        this.filePath = Objects.requireNonNull(path);
        notifyPathListeners();
    }

    /**
     * Indicates if the document was altered after the last save.
     * @return {@code true} if document was modified, {@code false} otherwise.
     */
    @Override
    public boolean isModified() {
        return this.modified;
    }

    /**
     * Marks the document as modified / unmodified.
     * @param modified desired value of modified flag.
     */
    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        notifyStatusListeners();
    }

    /**
     * Adds a {@link SingleDocumentListener} instance.
     * @param l listener that is to be added.
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    /**
     * Removes {@link SingleDocumentListener} instance.
     * @param l l listener that is to be removed.
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies listeners that a status change has occurred.
     */
    private void notifyStatusListeners(){
        listeners.forEach(l -> l.documentModifyStatusUpdated(this));
    }

    /**
     * Notifies the listeners that a path change has occured.
     */
    private void notifyPathListeners(){
        listeners.forEach(l -> l.documentFilePathUpdated(this));
    }
}
