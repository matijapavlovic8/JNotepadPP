package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ILocalizationProvider}
 * @author MatijaPav
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider{
    /**
     * List of listeners.
     */
    private List<ILocalizationListener> listeners;

    public AbstractLocalizationProvider(){
        this.listeners = new ArrayList<>();
    }

    /**
     * Adds the given {@link ILocalizationListener}.
     * @param l listener that is to be added.
     */
    @Override
    public void addLocalizationListener(ILocalizationListener l) {
        this.listeners.add(l);
    }

    /**
     * Removes the given {@link ILocalizationListener}.
     * @param l listener that is to be removed.
     */
    @Override
    public void removeLocalizationListener(ILocalizationListener l) {
        this.listeners.remove(l);
    }

    /**
     * Notifies listeners about a localization change.
     */
    public void fire(){
        listeners.forEach(ILocalizationListener::localizationChanged);
    }
}
