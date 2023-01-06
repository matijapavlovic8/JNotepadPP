package hr.fer.oprpp1.hw08.jnotepadpp.model;

import java.nio.file.Path;
import javax.swing.JTextArea;

/**
 * {@code SingleDocumentModel} models a single document.
 * @author MatijaPav
 */
public interface SingleDocumentModel {
    /**
     * Gets the {@link JTextArea} component of the document model.
     * @return {@link JTextArea}
     */
    JTextArea getTextComponent();

    /**
     * Gets the path of the current document.
     * @return {@link Path} of the document.
     */
    Path getFilePath();

    /**
     * Sets the path of the document.
     * @param path {@link Path} instance representing updated file path.
     */
    void setFilePath(Path path);

    /**
     * Indicates if the document was altered after the last save.
     * @return {@code true} if document was modified, {@code false} otherwise.
     */
    boolean isModified();

    /**
     * Marks the document as modified / unmodified.
     * @param modified desired value of modified flag.
     */
    void setModified(boolean modified);

    /**
     * Adds a {@link SingleDocumentListener} instance.
     * @param l listener that is to be added.
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes {@link SingleDocumentListener} instance.
     * @param l l listener that is to be removed.
     */
    void removeSingleDocumentListener(SingleDocumentListener l);

}
