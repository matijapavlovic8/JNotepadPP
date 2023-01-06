package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * {@code FormLocalizationProvider} acts as a proxy to {@link LocalizationProvider} instance.
 * @author MatijaPav
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
    /**
     * Instantiates a {@link FormLocalizationProvider} object.
     * @param parent {@link ILocalizationProvider} parent
     * @param frame {@link JFrame} frame which requests localization.
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);
        frame.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window has been opened.
             * @param e
             */
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                connect();
            }

            /**
             * Invoked when a window has been closed.
             * @param e
             */
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                disconnect();
            }
        });
    }
}
