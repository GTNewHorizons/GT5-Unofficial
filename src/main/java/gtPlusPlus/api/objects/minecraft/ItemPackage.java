package gtPlusPlus.api.objects.minecraft;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.handler.CompatHandler;

public abstract class ItemPackage implements RunnableWithInfo<String> {

    public ItemPackage() {
        this(false);
    }

    public ItemPackage(boolean hasExtraLateRun) {
        // Register for late run
        CompatHandler.mObjectsToRunInPostInit.put(this);
        if (hasExtraLateRun) {
            CompatHandler.mObjectsToRunInOnLoadComplete.put(this);
        }
        init();
    }

    @Override
    public final void run() {
        generateRecipes();
    }

    @Override
    public final String getInfoData() {
        return errorMessage();
    }

    public abstract String errorMessage();

    public abstract boolean generateRecipes();

    private void init() {
        items();
        blocks();
        fluids();
    }

    public abstract void items();

    public abstract void blocks();

    public abstract void fluids();

    /**
     * Override this to handle GT Recipe map manipulation after they're Baked.
     *
     * @param event - the {@link FMLLoadCompleteEvent}.
     * @return - Did we do anything?
     */
    public boolean onLoadComplete(FMLLoadCompleteEvent event) {
        return false;
    };
}
