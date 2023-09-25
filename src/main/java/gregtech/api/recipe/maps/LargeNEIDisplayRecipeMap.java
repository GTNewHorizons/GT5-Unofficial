package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.modularui.UIHelper;

/**
 * Nicely display NEI with many items and fluids. Remember to call
 * {@link RecipeMap#setUsualFluidInputCount} and
 * {@link RecipeMap#setUsualFluidOutputCount}. If row count >= 6, it doesn't fit in 2 recipes per page, so
 * change it via IMC.
 */
public class LargeNEIDisplayRecipeMap extends RecipeMap {

    private static final int xDirMaxCount = 3;
    private static final int yOrigin = 8;

    public LargeNEIDisplayRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
        String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
        setLogoPos(80, 62);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 106, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 16, yOrigin + getItemRowCount() * 18, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 106, yOrigin + getItemRowCount() * 18, xDirMaxCount);
    }

    @Override
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        // Delay setter so that calls to #setUsualFluidInputCount and #setUsualFluidOutputCount are considered
        setNEIBackgroundSize(172, 82 + (Math.max(getItemRowCount() + getFluidRowCount() - 4, 0)) * 18);
        return super.createNEITemplate(
            itemInputsInventory,
            itemOutputsInventory,
            specialSlotInventory,
            fluidInputsInventory,
            fluidOutputsInventory,
            progressSupplier,
            windowOffset);
    }

    private int getItemRowCount() {
        return (Math.max(mUsualInputCount, mUsualOutputCount) - 1) / xDirMaxCount + 1;
    }

    private int getFluidRowCount() {
        return (Math.max(getUsualFluidInputCount(), getUsualFluidOutputCount()) - 1) / xDirMaxCount + 1;
    }
}
