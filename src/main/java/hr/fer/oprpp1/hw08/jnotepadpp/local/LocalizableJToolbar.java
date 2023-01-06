package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.JMenu;
import javax.swing.JToolBar;

/**
 * Localized instance of {@link JToolBar}
 * @author MatijaPav
 */
public class LocalizableJToolbar extends JToolBar{
    /**
     * String used as a key to retrieve localization info.
     */
    private final String key;

    /**
     * Localization provider.
     */
    private final ILocalizationProvider provider;

    public LocalizableJToolbar(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;
        this.provider.addLocalizationListener(this::setLocalization);
        setLocalization();
    }

    /**
     * Sets text of {@link JToolBar} to given localization.
     */
    private void setLocalization(){
        setName(provider.getString(this.key));
    }
}
