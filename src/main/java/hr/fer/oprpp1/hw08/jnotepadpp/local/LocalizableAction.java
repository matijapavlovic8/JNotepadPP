package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * {@link LocalizableAction} is a localized {@link AbstractAction}
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * String used as a key to retrieve localization info.
     */
    private final String key;

    /**
     * Localization provider.
     */
    private final ILocalizationProvider provider;

    public LocalizableAction(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;
        putValues();
        this.provider.addLocalizationListener(this::putValues);
    }

    /**
     * Maps localized names and descriptions of actions with original.
     */
    private void putValues(){
        this.putValue(Action.NAME, this.provider.getString(key));
    }
}
