package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CauldronFrontend extends RecipeMapFrontend {

    private final static List<Pos2d> positions = UIHelper.getGridPositions(2, 30, 24, 2, 1);
    private final static Pos2d pos1 = positions.get(0);
    private final static Pos2d pos2 = positions.get(1);

    public CauldronFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> position = new ArrayList<>();
        position.add(new Pos2d(pos2.x + 8, pos2.y));
        return position;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        ArrayList<Pos2d> position = new ArrayList<>();
        position.add(new Pos2d(pos1.x, pos1.y));
        return position;
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        ArrayList<Pos2d> position = new ArrayList<>();
        position.add(new Pos2d(pos2.x + 66, pos2.y));
        return position;
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        final ItemStack water = GTUtility.getFluidDisplayStack(Materials.Water.getFluid(1), false);
        if (stack.isItemEqual(water)) {
            currentTip.add(EnumChatFormatting.RED + "Consumed per operation");
        }

        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }
}
