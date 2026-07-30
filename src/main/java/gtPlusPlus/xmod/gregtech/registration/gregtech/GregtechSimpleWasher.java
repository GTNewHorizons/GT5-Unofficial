package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_EV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_HV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_IV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_LV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_MV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_UV;
import static gregtech.api.enums.MetaTileEntityIDs.SimpleDustWasher_ZPM;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
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

    /// Washes each impure and purified dust back to its clean dust, across the three material domains the
    /// legacy loop covered: the gregtech-named materials through the ore dictionary, the werkstoff part set
    /// through [#werkstoffStack], and the gtPlusPlus materials through their MaterialLib shapes. The gtpp loop
    /// tests the ore shapes itself because [GTMaterialProperties#GTPP_STATE] marks a gtPlusPlus-family
    /// material, not an ore material -- most of them carry no dust at all.
    private static boolean generateDirtyDustRecipes() {
        int mRecipeCount = 0;
        // Generate Recipe Map for the Dust Washer.
        ItemStack dustClean;
        ItemStack dustDirty;
        ItemStack dustPure;
        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (!LegacyNameDomain.contains(ml)) {
                continue;
            }
            if (MaterialUtils.hasFlag(ml, GTMaterialFlag.NO_ORE_PROCESSING)) {
                continue;
            }
            if (ml == Materials.Platinum || ml == Materials.Osmium
                || ml == Materials.Iridium
                || ml == Materials.Palladium
                || ml == MaterialFacades.AnyCopper
                || ml == MaterialFacades.AnyIron) {
                continue;
            }

            dustClean = GTOreDictUnificator.get(OrePrefixes.dust, ml, 1L);
            dustDirty = GTOreDictUnificator.get(OrePrefixes.dustImpure, ml, 1L);
            dustPure = GTOreDictUnificator.get(OrePrefixes.dustPure, ml, 1L);
            addSimpleWashRecipe(dustDirty, dustClean);
            addSimpleWashRecipe(dustPure, dustClean);
        }

        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) {
                continue;
            }
            dustClean = werkstoffStack(ml, OrePrefixes.dust);
            dustDirty = werkstoffStack(ml, OrePrefixes.dustImpure);
            dustPure = werkstoffStack(ml, OrePrefixes.dustPure);
            addSimpleWashRecipe(dustDirty, dustClean);
            addSimpleWashRecipe(dustPure, dustClean);
        }

        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.GTPP_STATE) == null) {
                continue;
            }
            if (!ml.hasShape(Shapes.dust)) {
                continue;
            }
            dustClean = MaterialLibAPI.getStack(ml, Shapes.dust, 1);
            if (ml.hasShape(Shapes.dustImpure)) {
                addSimpleWashRecipe(MaterialLibAPI.getStack(ml, Shapes.dustImpure, 1), dustClean);
            }
            if (ml.hasShape(Shapes.dustPure)) {
                addSimpleWashRecipe(MaterialLibAPI.getStack(ml, Shapes.dustPure, 1), dustClean);
            }
        }

        return simpleWasherRecipes.getAllRecipes()
            .size() > mRecipeCount;
    }

    /// Resolves a part the legacy werkstoff system generated, or null when it did not generate that prefix --
    /// gregtech's part autogen covers shapes the werkstoff system never had, so a plain stack lookup would
    /// wash materials this loop never covered.
    private static ItemStack werkstoffStack(com.ruling_0.materiallib.api.Material material, OrePrefixes prefix) {
        return LegacyWerkstoffIndex.generatesPrefix(material, prefix) ? MaterialParts.stack(prefix, material, 1L)
            : null;
    }

    /// Washes each crushed ore to its purified form, across the same three material domains
    /// [#generateDirtyDustRecipes] covers and with the same gtpp shape test.
    private static boolean generateDirtyCrushedRecipes() {
        int mRecipeCount = simpleWasherRecipes.getAllRecipes()
            .size();
        // Generate Recipe Map for the Dust Washer.
        ItemStack crushedClean;
        ItemStack crushedDirty;
        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (!LegacyNameDomain.contains(ml)) {
                continue;
            }
            if (MaterialUtils.hasFlag(ml, GTMaterialFlag.NO_ORE_PROCESSING)) {
                continue;
            }
            crushedClean = GTOreDictUnificator.get(OrePrefixes.crushedPurified, ml, 1L);
            crushedDirty = GTOreDictUnificator.get(OrePrefixes.crushed, ml, 1L);
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) {
                continue;
            }
            crushedClean = werkstoffStack(ml, OrePrefixes.crushedPurified);
            crushedDirty = werkstoffStack(ml, OrePrefixes.crushed);
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        for (com.ruling_0.materiallib.api.Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.GTPP_STATE) == null) {
                continue;
            }
            if (!ml.hasShape(Shapes.crushed) || !ml.hasShape(Shapes.crushedPurified)) {
                continue;
            }
            addSimpleWashRecipe(
                MaterialLibAPI.getStack(ml, Shapes.crushed, 1),
                MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 1));
        }

        return simpleWasherRecipes.getAllRecipes()
            .size() > mRecipeCount;
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
