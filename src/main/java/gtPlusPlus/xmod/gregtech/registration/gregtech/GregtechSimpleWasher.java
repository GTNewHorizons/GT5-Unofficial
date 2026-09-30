package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_ZPM;
import static gregtech.api.recipe.RecipeMaps.simpleWasherRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertDecomposition;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipeBuilder;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechSimpleWasher {

    public static void run() {
        generateDirtyDustRecipes();
        generateDirtyCrushedRecipes();
        // Register the Simple Washer Entity.

        // The unlocalized names here have inconsistent numbering because there only used to be a simple washer
        // every other tier, and they were numbered numerically. In order to maintain backwards compatibility,
        // the tier numbers need to stay as they were.
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_LV,
            SimpleDustWasher_LV.ID,
            "simplewasher.01.tier.06",
            "Simple Washer I",
            1);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_MV,
            SimpleDustWasher_MV.ID,
            "simplewasher.01.tier.02",
            "Simple Washer II",
            2);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_HV,
            SimpleDustWasher_HV.ID,
            "simplewasher.01.tier.07",
            "Simple Washer III",
            3);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_EV,
            SimpleDustWasher_EV.ID,
            "simplewasher.01.tier.03",
            "Simple Washer IV",
            4);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_IV,
            SimpleDustWasher_IV.ID,
            "simplewasher.01.tier.08",
            "Simple Washer V",
            5);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_LuV,
            SimpleDustWasher_LuV.ID,
            "simplewasher.01.tier.04",
            "Simple Washer VI",
            6);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_ZPM,
            SimpleDustWasher_ZPM.ID,
            "simplewasher.01.tier.09",
            "Simple Washer VII",
            7);
        registerSimpleWasher(
            GregtechItemList.SimpleDustWasher_UV,
            SimpleDustWasher_UV.ID,
            "simplewasher.01.tier.05",
            "Simple Washer VIII",
            8);
    }

    private static void registerSimpleWasher(GregtechItemList washer, int id, String unloc, String loc, int tier) {
        washer.set(
            MTEBasicMachineWithRecipeBuilder.builder(id)
                .setName(unloc, loc)
                .setTier(tier)
                .setDescription(
                    new String[] { "It's like an automatic Cauldron for washing dusts.", GTPPCore.GT_Tooltip.get() })
                .setRecipes(simpleWasherRecipes)
                .setSlotsCount(1, 1)
                .setSound(SoundResource.GTCEU_LOOP_BATH)
                .setOverlays("SIMPLE_WASHER")
                .setFluidSlots(true, false)
                .build()
                .setRecipeCatalystPriority(-tier)
                .getStackForm(1L));
    }

    /// Washes each impure and purified dust back to its clean dust, for every material with ore processing.
    private static void generateDirtyDustRecipes() {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (MaterialUtils.hasFlag(ml, GTMaterialFlag.NO_ORE_PROCESSING)) continue;
            ItemStack dustClean = convertDecomposition(MaterialParts.stack(OrePrefixes.dust, ml, 1L))[0];
            addSimpleWashRecipe(MaterialParts.stack(OrePrefixes.dustImpure, ml, 1L), dustClean);
            addSimpleWashRecipe(MaterialParts.stack(OrePrefixes.dustPure, ml, 1L), dustClean);
        }
    }

    /// Washes each crushed ore to its purified form, for every material with ore processing.
    private static void generateDirtyCrushedRecipes() {
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (MaterialUtils.hasFlag(ml, GTMaterialFlag.NO_ORE_PROCESSING)) continue;
            addSimpleWashRecipe(
                MaterialParts.stack(OrePrefixes.crushed, ml, 1L),
                MaterialParts.stack(OrePrefixes.crushedPurified, ml, 1L));
        }
    }

    private static void addSimpleWashRecipe(ItemStack aInput, ItemStack aOutput) {
        if (aInput != null && aOutput != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(aInput)
                .itemOutputs(aOutput)
                .fluidInputs(GTUtility.getWater(100))
                .duration(5 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(simpleWasherRecipes);
        }
    }
}
