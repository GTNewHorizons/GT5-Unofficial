package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Element;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Replicator;

public class ReplicatorFakeMap extends GT_Recipe.GT_Recipe_Map {

    public ReplicatorFakeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
    }

    @Override
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        AtomicInteger ai = new AtomicInteger();
        Optional.ofNullable(GT_OreDictUnificator.getAssociation(aOutputs[0]))
            .map(itemData -> itemData.mMaterial)
            .map(materialsStack -> materialsStack.mMaterial)
            .map(materials -> materials.mElement)
            .map(Element::getMass)
            .ifPresent(e -> {
                aFluidInputs[0].amount = (int) GT_MetaTileEntity_Replicator.cubicFluidMultiplier(e);
                ai.set(GT_Utility.safeInt(aFluidInputs[0].amount * 512L, 1));
            });
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                ai.get(),
                aEUt,
                aSpecialValue));
    }
}
