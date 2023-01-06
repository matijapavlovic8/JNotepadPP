package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 * {@code MultipleDocumentListener} observes changes within a {@link MultipleDocumentModel} instance.
 * @author MatijaPav
 */
public interface MultipleDocumentListener {
    /**
     * Notifies entities that a new document is currently active
     * @param previousModel previously active document.
     * @param currentModel currently active document.
     * @throws NullPointerException if both parameters are null.
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Notifies that a new {@link SingleDocumentModel} instance was added.
     * @param model added instance.
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Notifies that a {@link SingleDocumentModel} instance was removed.
     * @param model removed instance.
     */
    void documentRemoved(SingleDocumentModel model);

}
