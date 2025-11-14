package gregtech.common.gui.modularui.multiblock.godforge.util;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.widgets.Expandable;

public class ForgeOfGodsExpandable extends Expandable {

    private UITexture background;

    public ForgeOfGodsExpandable() {
        // To manually handle the background
        background(IDrawable.EMPTY);
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        // To make the panel resize on a specific action rather
        // than clicking anywhere in the expandable widget's area
        return Result.ACCEPT;
    }

    public void updateBackground(UITexture newBackground) {

    }
}
