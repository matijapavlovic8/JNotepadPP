package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 * {@code SingleDocumentListener} observes changes within a {@link SingleDocumentModel} instance.
 * @author MatijaPav
 */
public interface SingleDocumentListener {

    /**
     * Notifies that a status update has occurred.
     * @param model updated instance.
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Notifies that a file path update has occurred.
     * @param model updated instance.
     */
    void documentFilePathUpdated(SingleDocumentModel model);

}
