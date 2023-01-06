package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Implementation of {@link AbstractLocalizationProvider}
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Path to the translations resource.
     */
    private static final String TRANSLATIONS = "hr.fer.oprpp1.hw08.jnotepadpp.local.translations";

    /**
     * Current language.
     */
    private String language;

    /**
     * Bundle of resources providing translations.
     */
    private ResourceBundle bundle;

    /**
     * Provider instance.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    private LocalizationProvider(){
        this.language = "en";
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS, Locale.forLanguageTag(this.language));
        this.fire();
    }

    /**
     * Gets the instance of provider.
     * @return {@link LocalizationProvider} instance.
     */
    public static LocalizationProvider getInstance(){
        return instance;
    }

    public void setLanguage(String lang){
        this.language = lang;
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS, Locale.forLanguageTag(this.language));
        this.fire();
    }

    /**
     * Returns the {@code String} for which the parameter acts as a key.
     * @param key {@code String} key.
     * @return {@code String}
     */
    @Override
    public String getString(String key) {
        return this.bundle.getString(key);
    }

    /**
     * Gets current language.
     * @return currently cached language.
     */
    @Override
    public String getCurrentLanguage() {
        return this.language;
    }
}
