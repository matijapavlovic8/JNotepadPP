package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Localized instance of {@link JMenu}
 * @author MatijaPav
 */
public class LocalizableJMenu extends JMenu {

    /**
     * String used as a key to retrieve localization info.
     */
    private final String key;

    /**
     * Localization provider.
     */
    private final ILocalizationProvider provider;

    public LocalizableJMenu(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;
        this.provider.addLocalizationListener(this::setLocalization);
        setLocalization();
    }

    /**
     * Sets text of {@link JMenu} to given localization.
     */
    private void setLocalization(){
        setText(provider.getString(this.key));
    }
}
