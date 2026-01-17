package kubatech.loaders;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.ElectroMagicTools;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.Witchery;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.DEFC_CASING_TIER;
import static kubatech.api.gui.KubaTechUITextures.SLOT_FUSION_CRAFTER;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;

public class DEFCRecipes {

    public static final RecipeMap<RecipeMapBackend> fusionCraftingRecipes = RecipeMapBuilder
        .of("kubatech.defusioncrafter")
        .maxIO(9, 1, 1, 1)
        .minInputs(1, 0)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("kubatech.defusioncrafter.tier"))
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? SLOT_FUSION_CRAFTER : null)
        .build();

    public static void addRecipes() {

        // Dragonblood recipe for magics haters
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dragon_egg, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64L))
            .fluidInputs(Materials.Radon.getPlasma(1 * INGOTS))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 8L))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 288))
            .eut(TierEU.RECIPE_UHV)
            .duration(14_000)
            .addTo(mixerRecipes);

        // Casings

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 6L),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 6L))
            .fluidInputs(Materials.Void.getMolten(8 * INGOTS))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingBase.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.BloodyIchorium", 1, 0),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6L))
            .fluidInputs(Materials.Void.getMolten(8 * INGOTS))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT1.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT1.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 6L),
                GTModHandler.getModItem(DraconicEvolution.ID, "draconicCore", 1, 0))
            .fluidInputs(Materials.Void.getMolten(16 * INGOTS))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT2.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT2.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 6L),
                GTModHandler.getModItem(DraconicEvolution.ID, "wyvernCore", 2, 0))
            .fluidInputs(Materials.Void.getMolten(32 * INGOTS))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT3.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT3.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 6L),
                GTModHandler.getModItem(DraconicEvolution.ID, "awakenedCore", 3, 0))
            .fluidInputs(Materials.Void.getMolten(1 * STACKS))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT4.get(1))
            .eut(TierEU.RECIPE_UEV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT4.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 6L),
                GTModHandler.getModItem(DraconicEvolution.ID, "chaoticCore", 4, 0))
            .fluidInputs(Materials.Void.getMolten(18432L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT5.get(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);

        fusionRecipes();
        conversionRecipes();
    }

    private static final Item EMTItems = GameRegistry.findItem("EMT", "EMTItems");

    private static void addOldHiddenRecipe(GTRecipe recipe) {
        if (!ElectroMagicTools.isModLoaded()) return;
        recipe = recipe.copy();
        recipe.mInputs = Arrays.stream(recipe.mInputs)
            .map(i -> {
                if (i != null && i.getItem() == ItemLoader.kubaitems) {
                    return new ItemStack(
                        EMTItems,
                        0,
                        16 + (i.getItemDamage() - kubatech.api.enums.ItemList.DEFCDraconicSchematic.get(1)
                            .getItemDamage()));
                } else return i;
            })
            .toArray(ItemStack[]::new);
        recipe.mHidden = true;
        fusionCraftingRecipes.add(recipe);
    }

    private static void conversionRecipes() {
        if (!ElectroMagicTools.isModLoaded()) return;
        GameRegistry.addShapelessRecipe(
            kubatech.api.enums.ItemList.DEFCDraconicSchematic.get(1),
            new ItemStack(EMTItems, 1, 16));
        GameRegistry
            .addShapelessRecipe(kubatech.api.enums.ItemList.DEFCWyvernSchematic.get(1), new ItemStack(EMTItems, 1, 17));
        GameRegistry.addShapelessRecipe(
            kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(1),
            new ItemStack(EMTItems, 1, 18));
        GameRegistry.addShapelessRecipe(
            kubatech.api.enums.ItemList.DEFCChaoticSchematic.get(1),
            new ItemStack(EMTItems, 1, 19));
    }

    private static void fusionRecipes() {
        // CORES

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1),
                ItemList.QuantumEye.get(1L),
                kubatech.api.enums.ItemList.DEFCDraconicSchematic.get(0L))
            .fluidInputs(Materials.Sunnarium.getMolten(10 * INGOTS))
            .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "draconicCore", 1, 0))
            .eut(TierEU.RECIPE_UV)
            .duration(400)
            .metadata(DEFC_CASING_TIER, 1)
            .addTo(fusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4),
                GTModHandler.getModItem(DraconicEvolution.ID, "draconicCore", 4, 0),
                ItemList.QuantumStar.get(1L),
                kubatech.api.enums.ItemList.DEFCWyvernSchematic.get(0L))
            .fluidInputs(Materials.Neutronium.getMolten(10 * INGOTS))
            .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "wyvernCore", 1, 0))
            .eut(TierEU.RECIPE_UHV)
            .duration(800)
            .metadata(DEFC_CASING_TIER, 2)
            .addTo(fusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        if (Mods.SuperSolarPanels.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 4),
                    GTModHandler.getModItem(DraconicEvolution.ID, "wyvernCore", 4, 0),
                    GTModHandler.getModItem(SuperSolarPanels.ID, "enderquantumcomponent", 1, 0),
                    kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
                .fluidInputs(Materials.Infinity.getMolten(10 * INGOTS))
                .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "awakenedCore", 1, 0))
                .eut(TierEU.RECIPE_UEV)
                .duration(1600)
                .metadata(DEFC_CASING_TIER, 3)
                .addTo(fusionCraftingRecipes)
                .forEach(DEFCRecipes::addOldHiddenRecipe);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 4),
                    GTModHandler.getModItem(DraconicEvolution.ID, "wyvernCore", 4, 0),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "ManyullynCrystal", 1, 0),
                    kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
                .fluidInputs(Materials.Infinity.getMolten(10 * INGOTS))
                .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "awakenedCore", 1, 0))
                .eut(TierEU.RECIPE_UEV)
                .duration(1600)
                .metadata(DEFC_CASING_TIER, 3)
                .addTo(fusionCraftingRecipes)
                .forEach(DEFCRecipes::addOldHiddenRecipe);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4),
                GTModHandler.getModItem(DraconicEvolution.ID, "awakenedCore", 4, 0),
                GTModHandler.getModItem(DraconicEvolution.ID, "chaosFragment", 2, 2),
                kubatech.api.enums.ItemList.DEFCChaoticSchematic.get(0L))
            .fluidInputs(Materials.SpaceTime.getMolten(10 * INGOTS))
            .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "chaoticCore", 1, 0))
            .eut(24_000_000)
            .duration(3200)
            .metadata(DEFC_CASING_TIER, 4)
            .addTo(fusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        // ENERGY CORES

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StellarAlloy, 4),
                GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 4, 1),
                GTModHandler.getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                kubatech.api.enums.ItemList.DEFCWyvernSchematic.get(0L))
            .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "draconiumEnergyCore", 1, 0))
            .eut(TierEU.RECIPE_UV)
            .duration(1000)
            .metadata(DEFC_CASING_TIER, 2)
            .addTo(fusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 8),
                GTModHandler.getModItem(DraconicEvolution.ID, "draconiumEnergyCore", 4, 0),
                GTModHandler.getModItem(AdvancedSolarPanel.ID, "asp_crafting_items", 4, 4),
                GTModHandler.getModItem(DraconicEvolution.ID, "wyvernCore", 1, 0),
                kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
            .itemOutputs(GTModHandler.getModItem(DraconicEvolution.ID, "draconiumEnergyCore", 1, 1))
            .eut(TierEU.RECIPE_UHV)
            .duration(2000)
            .metadata(DEFC_CASING_TIER, 3)
            .addTo(fusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        // Dragon Blood
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dragon_egg, 0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64))
            .circuit(1)
            .fluidInputs(Materials.Radon.getPlasma(1 * INGOTS))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 288))
            .eut(TierEU.RECIPE_UHV)
            .duration(4200)
            .metadata(DEFC_CASING_TIER, 3)
            .addTo(fusionCraftingRecipes);

        if (Witchery.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Witchery.ID, "infinityegg", 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64))
                .circuit(1)
                .fluidInputs(Materials.Radon.getPlasma(72))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 432))
                .eut(TierEU.RECIPE_UHV)
                .duration(3600)
                .metadata(DEFC_CASING_TIER, 3)
                .addTo(fusionCraftingRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dragon_egg, 0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                GTPPCombType.DRAGONBLOOD.getStackForType(1))
            .fluidInputs(Materials.Radon.getPlasma(216))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 432))
            .eut(TierEU.RECIPE_UHV)
            .duration(2800)
            .metadata(DEFC_CASING_TIER, 3)
            .addTo(fusionCraftingRecipes);

        if (Witchery.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Witchery.ID, "infinityegg", 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                    GTPPCombType.DRAGONBLOOD.getStackForType(1))
                .fluidInputs(Materials.Radon.getPlasma(108))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 648))
                .eut(TierEU.RECIPE_UHV)
                .duration(2400)
                .metadata(DEFC_CASING_TIER, 3)
                .addTo(fusionCraftingRecipes);
        }
    }
}
