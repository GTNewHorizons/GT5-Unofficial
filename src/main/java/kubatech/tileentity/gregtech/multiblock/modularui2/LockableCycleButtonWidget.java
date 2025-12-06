package kubatech.tileentity.gregtech.multiblock.modularui2;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;

public abstract class LockableCycleButtonWidget extends CycleButtonWidget {

    public abstract boolean isLocked();

    public LockableCycleButtonWidget baseOverlay(IDrawable baseOverlay) {
        this.overlay(new DynamicDrawable(() -> {
            if (isLocked()) return new DrawableStack(baseOverlay, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
            return baseOverlay;
        }));
        return this;
    }

    public LockableCycleButtonWidget baseDynamicOverlay(Supplier<IDrawable> supplier) {
        IDrawable baseOverlay = new DynamicDrawable(supplier);
        return this.baseOverlay(baseOverlay);
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (isLocked()) return Result.IGNORE;
        return super.onMousePressed(mouseButton);
    }
}
