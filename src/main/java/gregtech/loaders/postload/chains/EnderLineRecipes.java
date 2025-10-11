package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtnhintergalactic.recipe.IGRecipeMaps;

public class EnderLineRecipes {

    static ItemStack missing = new ItemStack(Blocks.fire);

    public static void run() {

        EnderLineRecipes.addEncasedTeleportatiumParts();
    }

    private static void addEncasedTeleportatiumParts() {
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.ingot, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plate, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plateDouble, 1, 2);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.plateDense, 1, 9);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.stick, 2, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.round, 9, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.bolt, 8, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.screw, 8, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.ring, 4, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.foil, 4, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.itemCasing, 2, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.gearGtSmall, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.rotor, 1, 5);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.stickLong, 1, 1);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.gearGt, 1, 4);
        addEncasedTeleportatiumPartsRecipe(OrePrefixes.block, 1, 9);

    }

    private static void addEncasedTeleportatiumPartsRecipe(OrePrefixes prefix, int multiplier, int inverseMultiplier) {

        // Space Assembler

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.Enderium, multiplier),
                ItemList.Field_Generator_IV.get(1),
                ItemList.Field_Generator_MV.get(1),
                getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 2, 1),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier5", 1))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.TeleportatiumEncased, multiplier))
            .fluidInputs(
                // new FluidStack(solderUEV, 144),
                Materials.TeleportatiumStable.getFluid(144L * inverseMultiplier),
                Materials.EnderAirFortified.getFluid(144L * inverseMultiplier),
                Materials.EnderAirBalanced.getFluid(144L * inverseMultiplier))
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(IGRecipeMaps.spaceAssemblerRecipes);

        // Electric compressor

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier7", 1),
                getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 2, 4),
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4L * inverseMultiplier, 6, missing))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.TeleportatiumEncased, multiplier))
            .fluidInputs(Materials.TeleportatiumStable.getMolten(144L * inverseMultiplier))
            .fluidOutputs(Materials.EnderAir.getGas(14L * inverseMultiplier))
            .metadata(IGRecipeMaps.MODULE_TIER, 1)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

    }

}
