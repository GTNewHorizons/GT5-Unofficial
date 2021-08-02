package reactor.itemBehaviour;

import java.util.HashMap;

public abstract class IReactorItemBehaviour {

    protected final HashMap<String, String> properties;

    protected IReactorItemBehaviour(String propertiesKey) {
        // load

        properties = new HashMap<>();
    }

    public abstract void apply();

    public abstract boolean hasDurabilityBar();

    public abstract double getDurabilityForDisplay();
}
