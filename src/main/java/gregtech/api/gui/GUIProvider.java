package gregtech.api.gui;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;

public abstract class GUIProvider<T extends GUIHost<T>> {
    
    protected final T host;

    public GUIProvider(T host) {
        this.host = host;
    }

    @Nonnull
    public ModularWindow openGUI(@Nonnull UIBuildContext uiContext) {
        Builder builder = Objects.requireNonNull(ModularWindow.builder(host.getWidth(), host.getHeight()));
        if (shouldBindPlayerInventory()) {
            builder.bindPlayerInventory(uiContext.getPlayer());
        }
        attachSynchHandlers(builder, uiContext);
        addWidgets(builder, uiContext);
        return Objects.requireNonNull(builder.build());
    }

    protected abstract void attachSynchHandlers(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext);

    protected abstract void addWidgets(@Nonnull Builder builder, @Nonnull UIBuildContext uiContext);

    protected boolean shouldBindPlayerInventory() {
        return true;
    }
}
