package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_EV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_HV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_IV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_LV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_LuV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_MV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_ULV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_UV;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.SimpleDustWasher_ZPM;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.data.Quad;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechSimpleWasher {

    public static void run() {
        if (CORE.ConfigSwitches.enableMachine_SimpleWasher) {
            generateDirtyDustRecipes();
            generateDirtyCrushedRecipes();
            // Register the Simple Washer Entity.

            // The unlocalized names here have inconsistent numbering because there only used to be a simple washer
            // every other tier, and they were numbered numerically. In order to maintain backwards compatibility,
            // the tier numbers need to stay as they were.
            List<Quad<GregtechItemList, Integer, String, String>> washers = ImmutableList.of(
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_LV,
                    SimpleDustWasher_LV.ID,
                    "simplewasher.01.tier.06",
                    "Simple Washer I"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_MV,
                    SimpleDustWasher_MV.ID,
                    "simplewasher.01.tier.02",
                    "Simple Washer II"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_HV,
                    SimpleDustWasher_HV.ID,
                    "simplewasher.01.tier.07",
                    "Simple Washer III"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_EV,
                    SimpleDustWasher_EV.ID,
                    "simplewasher.01.tier.03",
                    "Simple Washer IV"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_IV,
                    SimpleDustWasher_IV.ID,
                    "simplewasher.01.tier.08",
                    "Simple Washer V"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_LuV,
                    SimpleDustWasher_LuV.ID,
                    "simplewasher.01.tier.04",
                    "Simple Washer VI"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_ZPM,
                    SimpleDustWasher_ZPM.ID,
                    "simplewasher.01.tier.09",
                    "Simple Washer VII"),
                new Quad<>(
                    GregtechItemList.SimpleDustWasher_UV,
                    SimpleDustWasher_UV.ID,
                    "simplewasher.01.tier.05",
                    "Simple Washer VIII"));

            GregtechItemList.SimpleDustWasher_ULV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                    SimpleDustWasher_ULV.ID,
                    "simplewasher.01.tier.01",
                    "Deprecated ULV Simple Washer",
                    0,
                    new String[] { "It's like an automatic Cauldron for washing dusts.",
                        "§cDEPRECATED: No recipe.§r Make a Simple Washer I.", CORE.GT_Tooltip.get() },
                    simpleWasherRecipes,
                    1,
                    1,
                    true,
                    SoundResource.NONE,
                    SpecialEffects.NONE,
                    "SIMPLE_WASHER",
                    null).setRecipeCatalystPriority(-11)
                        .getStackForm(1L));

            for (int i = 0; i < washers.size(); i++) {
                Quad<GregtechItemList, Integer, String, String> washer = washers.get(i);
                int tier = i + 1;
                washer.getKey()
                    .set(
                        new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                            washer.getValue_1(),
                            washer.getValue_2(),
                            washer.getValue_3(),
                            tier,
                            new String[] { "It's like an automatic Cauldron for washing dusts.",
                                CORE.GT_Tooltip.get() },
                            simpleWasherRecipes,
                            1,
                            1,
                            true,
                            SoundResource.NONE,
                            SpecialEffects.NONE,
                            "SIMPLE_WASHER",
                            null).setRecipeCatalystPriority(-tier)
                                .getStackForm(1L));
            }
        }
    }

    private static boolean generateDirtyDustRecipes() {
        int mRecipeCount = 0;
        // Generate Recipe Map for the Dust Washer.
        ItemStack dustClean;
        ItemStack dustDirty;
        ItemStack dustPure;
        for (Materials v : Materials.values()) {
            if (v == Materials.Platinum || v == Materials.Osmium
                || v == Materials.Iridium
                || v == Materials.Palladium) {
                continue;
            }

            dustClean = GT_OreDictUnificator.get(OrePrefixes.dust, v, 1L);
            dustDirty = GT_OreDictUnificator.get(OrePrefixes.dustImpure, v, 1L);
            dustPure = GT_OreDictUnificator.get(OrePrefixes.dustPure, v, 1L);
            addSimpleWashRecipe(dustDirty, dustClean);
            addSimpleWashRecipe(dustPure, dustClean);
        }

        for (Werkstoff v : Werkstoff.werkstoffHashSet) {
            dustClean = v.hasItemType(OrePrefixes.dust) ? v.get(OrePrefixes.dust) : null;
            dustDirty = v.hasItemType(OrePrefixes.dustImpure) ? v.get(OrePrefixes.dustImpure) : null;
            dustPure = v.hasItemType(OrePrefixes.dustPure) ? v.get(OrePrefixes.dustPure) : null;
            addSimpleWashRecipe(dustDirty, dustClean);
            addSimpleWashRecipe(dustPure, dustClean);
        }

        return simpleWasherRecipes.getAllRecipes()
            .size() > mRecipeCount;
    }

    private static boolean generateDirtyCrushedRecipes() {
        int mRecipeCount = simpleWasherRecipes.getAllRecipes()
            .size();
        // Generate Recipe Map for the Dust Washer.
        ItemStack crushedClean;
        ItemStack crushedDirty;
        for (Materials v : Materials.values()) {
            crushedClean = GT_OreDictUnificator.get(OrePrefixes.crushedPurified, v, 1L);
            crushedDirty = GT_OreDictUnificator.get(OrePrefixes.crushed, v, 1L);
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        for (Werkstoff v : Werkstoff.werkstoffHashSet) {
            crushedClean = v.hasItemType(OrePrefixes.crushedPurified) ? v.get(OrePrefixes.crushedPurified) : null;
            crushedDirty = v.hasItemType(OrePrefixes.crushed) ? v.get(OrePrefixes.crushed) : null;
            addSimpleWashRecipe(crushedDirty, crushedClean);
        }

        return simpleWasherRecipes.getAllRecipes()
            .size() > mRecipeCount;
    }

    private static void addSimpleWashRecipe(ItemStack aInput, ItemStack aOutput) {
        if (aInput != null && aOutput != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(aInput)
                .itemOutputs(aOutput)
                .fluidInputs(FluidUtils.getFluidStack("water", 100))
                .duration(5 * TICKS)
                .eut(8)
                .addTo(simpleWasherRecipes);
        }
    }
}
