package hr.fer.oprpp1.hw08.jnotepadpp.model;

import java.nio.file.Path;
import javax.swing.JComponent;

/**
 * Container model for {@link SingleDocumentModel} instances.
 * @author MatijaPav
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

    /**
     * Gets the visual component  responsible for displaying the entire
     * {@link MultipleDocumentModel} user interface.
     * @return {@link JComponent}
     */
    JComponent getVisualComponent();

    /**
     * Creates a new document.
     * @return {@link SingleDocumentModel} instance.
     */
    SingleDocumentModel createNewDocument();
    /**
     * Gets the current document.
     * @return {@link SingleDocumentModel} instance.
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document.
     * @param path {@link Path} instance of the desired document.
     * @return {@link SingleDocumentModel} instance.
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves the document.
     * @param model {@link SingleDocumentModel} instance to be saved.
     * @param newPath {@link Path} to which the document will be saved.
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes the given document.
     * @param model {@link SingleDocumentModel} instance that is to be closed.
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds a {@link MultipleDocumentListener}
     * @param l added listener
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes a {@link MultipleDocumentListener}
     * @param l removed listener.
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Gets the number of documents in the {@link MultipleDocumentModel}.
     * @return {@code int} nuber of documents.
     */
    int getNumberOfDocuments();

    /**
     * Gets {@link SingleDocumentModel} instance on the given index.
     * @param index of desired document.
     * @return {@link SingleDocumentModel} found on given index.
     */
    SingleDocumentModel getDocument(int index);

    /**
     * Gets the {@link SingleDocumentModel} instance with the given path.
     * @param path {@link Path} of the desired document.
     * @return {@link SingleDocumentModel} found on given path.
     * @throws NullPointerException if no such model exists.
     */
    SingleDocumentModel findForPath(Path path);

    /**
     * Gets the index of the given document.
     * @param doc {@link SingleDocumentModel} whose index is being requested.
     * @return {@code int} index of the document or -1 if no such document is present.
     */
    int getIndexOfDocument(SingleDocumentModel doc);
}
