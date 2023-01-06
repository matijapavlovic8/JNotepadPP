package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * {@code LocalizationProviderBridge} extends {@link AbstractLocalizationProvider}
 * and acts as a decorator for {@link ILocalizationProvider} instances.
 * @author MatijaPav
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /**
     * Describes if the bridge is currently connected or not.
     */
    private boolean connected;

    /**
     * Listener
     */
    private final ILocalizationListener listener;

    /**
     * Parent to which the bridge connects to.
     */
    private final ILocalizationProvider parent;

    /**
     * Cached language.
     */
    private String cachedLang;

    public LocalizationProviderBridge(ILocalizationProvider parent){
        this.parent = parent;
        listener = this::fire;
        cachedLang = null;
    }

    /**
     * Connects the bridge with the parent.
     */
    public void connect(){
        if(connected)
            return;
        connected = true;
        parent.addLocalizationListener(listener);
        if(cachedLang != null && !getCurrentLanguage().equals(cachedLang)){
            fire();
            this.cachedLang = getCurrentLanguage();
        }
    }

    /**
     * Disconnects the bridge to the parent.
     */
    public void disconnect(){
        if(!connected)
            return;
        this.connected = false;
        parent.removeLocalizationListener(this.listener);
        this.cachedLang = getCurrentLanguage();
    }

    /**
     * Returns the {@code String} for which the parameter acts as a key.
     * @param key {@code String} key.
     * @return {@code String}
     */
    @Override
    public String getString(String key) {
        return parent.getString(key);
    }

    /**
     * Gets current language.
     * @return currently cached language.
     */
    @Override
    public String getCurrentLanguage() {
        return parent.getCurrentLanguage();
    }
}
