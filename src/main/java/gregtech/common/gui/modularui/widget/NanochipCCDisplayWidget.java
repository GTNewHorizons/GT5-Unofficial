package gregtech.common.gui.modularui.widget;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.integration.recipeviewer.RecipeViewerIngredientProvider;
import com.cleanroommc.modularui.widget.Widget;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class NanochipCCDisplayWidget extends Widget<NanochipCCDisplayWidget> implements RecipeViewerIngredientProvider {

    ItemStack item;

    public NanochipCCDisplayWidget(ItemStack ccItem) {
        super();
        this.item = ccItem;
    }

    // just a plain override for the widget that allows it to supply a certain stack to NEI based on if its a PC or not.
    @Override
    public @Nullable ItemStack getStackForRecipeViewer() {
        ItemStack realStack = CircuitComponent.tryGetRealStack(item);
        return realStack == null ? item : realStack;
    }

}
