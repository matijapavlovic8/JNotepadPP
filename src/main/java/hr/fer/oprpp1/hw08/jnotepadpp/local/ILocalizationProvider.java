package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Localization provider.
 * @author MatijaPav
 */
public interface ILocalizationProvider {

    /**
     * Adds the given {@link ILocalizationListener}.
     * @param l listener that is to be added.
     */
    void addLocalizationListener(ILocalizationListener l);

    /**
     * Removes the given {@link ILocalizationListener}.
     * @param l listener that is to be removed.
     */
    void removeLocalizationListener(ILocalizationListener l);

    /**
     * Returns the {@code String} for which the parameter acts as a key.
     * @param key {@code String} key.
     * @return {@code String}
     */
    String getString(String key);

    /**
     * Gets current language.
     * @return currently cached language.
     */
    String getCurrentLanguage();
}
