package kubatech.loaders;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMixerRecipes;

import java.util.Arrays;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import kubatech.Tags;
import kubatech.api.LoaderReference;

public class DEFCRecipes {

    public static final GT_Recipe.GT_Recipe_Map sFusionCraftingRecipes = new GT_Recipe.GT_Recipe_Map(
        new HashSet<>(16),
        "kubatech.defusioncrafter",
        "Draconic Evolution Fusion Crafter",
        null,
        GregTech.getResourcePath("textures/gui/basicmachines", "FusionCrafter"),
        9,
        1,
        1,
        0,
        1,
        "Tier Casing: ",
        1,
        E,
        false,
        true).setSlotOverlay(false, false, UITexture.fullImage(Tags.MODID, "gui/slot/fusion_crafter"));

    public static void addRecipes() {

        // Dragonblood recipe for magics haters
        if (LoaderReference.GTPlusPlus) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.dragon_egg, 1),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64L))
                .fluidInputs(Materials.Radon.getPlasma(144))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 8L))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 288))
                .eut(1_966_080)
                .duration(14_000)
                .addTo(sMixerRecipes);
        }

        // Casings

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 6L),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 6L))
            .fluidInputs(Materials.Void.getMolten(1152L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingBase.get(1))
            .eut(491_520)
            .duration(24000)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "tile.BloodyIchorium", 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6L))
            .fluidInputs(Materials.Void.getMolten(1152L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT1.get(1))
            .eut(491_520)
            .duration(24000)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT1.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 6L),
                GT_ModHandler.getModItem("DraconicEvolution", "draconicCore", 1, 0))
            .fluidInputs(Materials.Void.getMolten(2304L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT2.get(1))
            .eut(491_520)
            .duration(24000)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT2.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 6L),
                GT_ModHandler.getModItem("DraconicEvolution", "wyvernCore", 2, 0))
            .fluidInputs(Materials.Void.getMolten(4608L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT3.get(1))
            .eut(1_996_080)
            .duration(12000)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT3.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 6L),
                GT_ModHandler.getModItem("DraconicEvolution", "awakenedCore", 3, 0))
            .fluidInputs(Materials.Void.getMolten(9216L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT4.get(1))
            .eut(7_864_320)
            .duration(12000)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                kubatech.api.enums.ItemList.DEFCCasingT4.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 6L),
                GT_ModHandler.getModItem("DraconicEvolution", "chaoticCore", 4, 0))
            .fluidInputs(Materials.Void.getMolten(18432L))
            .itemOutputs(kubatech.api.enums.ItemList.DEFCCasingT5.get(1))
            .eut(31_457_280)
            .duration(12000)
            .addTo(sAssemblerRecipes);

        fusionRecipes();
        conversionRecipes();
    }

    private static final Item EMTItems = GameRegistry.findItem("EMT", "EMTItems");

    private static void addOldHiddenRecipe(GT_Recipe recipe) {
        if (!LoaderReference.ElectroMagicTools) return;
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
        sFusionCraftingRecipes.add(recipe);
    }

    private static void conversionRecipes() {
        if (!LoaderReference.ElectroMagicTools) return;
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

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1),
                ItemList.QuantumEye.get(1L),
                kubatech.api.enums.ItemList.DEFCDraconicSchematic.get(0L))
            .fluidInputs(Materials.Sunnarium.getMolten(1440))
            .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "draconicCore", 1, 0))
            .eut(500_000)
            .duration(400)
            .specialValue(1)
            .addTo(sFusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4),
                GT_ModHandler.getModItem("DraconicEvolution", "draconicCore", 4, 0),
                ItemList.QuantumStar.get(1L),
                kubatech.api.enums.ItemList.DEFCWyvernSchematic.get(0L))
            .fluidInputs(Materials.Neutronium.getMolten(1440))
            .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "wyvernCore", 1, 0))
            .eut(2_000_000)
            .duration(800)
            .specialValue(2)
            .addTo(sFusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        if (Loader.isModLoaded("supersolarpanel")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 4),
                    GT_ModHandler.getModItem("DraconicEvolution", "wyvernCore", 4, 0),
                    GT_ModHandler.getModItem("supersolarpanel", "enderquantumcomponent", 1, 0),
                    kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
                .fluidInputs(Materials.Infinity.getMolten(1440))
                .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "awakenedCore", 1, 0))
                .eut(8_000_000)
                .duration(1600)
                .specialValue(3)
                .addTo(sFusionCraftingRecipes)
                .forEach(DEFCRecipes::addOldHiddenRecipe);
        } else {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 12),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 4),
                    GT_ModHandler.getModItem("DraconicEvolution", "wyvernCore", 4, 0),
                    GT_ModHandler.getModItem("dreamcraft", "item.ManyullynCrystal", 1, 0),
                    kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
                .fluidInputs(Materials.Infinity.getMolten(1440))
                .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "awakenedCore", 1, 0))
                .eut(8_000_000)
                .duration(1600)
                .specialValue(3)
                .addTo(sFusionCraftingRecipes)
                .forEach(DEFCRecipes::addOldHiddenRecipe);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4),
                GT_ModHandler.getModItem("DraconicEvolution", "awakenedCore", 4, 0),
                GT_ModHandler.getModItem("DraconicEvolution", "chaosFragment", 2, 2),
                kubatech.api.enums.ItemList.DEFCChaoticSchematic.get(0L))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(1440))
            .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "chaoticCore", 1, 0))
            .eut(24_000_000)
            .duration(3200)
            .specialValue(4)
            .addTo(sFusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        // ENERGY CORES

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Draconium, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StellarAlloy, 4),
                GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 4, 1),
                GT_ModHandler.getModItem("DraconicEvolution", "draconicCore", 1, 0),
                kubatech.api.enums.ItemList.DEFCWyvernSchematic.get(0L))
            .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "draconiumEnergyCore", 1, 0))
            .eut(500_000)
            .duration(1000)
            .specialValue(2)
            .addTo(sFusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 8),
                GT_ModHandler.getModItem("DraconicEvolution", "draconiumEnergyCore", 4, 0),
                GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 4, 4),
                GT_ModHandler.getModItem("DraconicEvolution", "wyvernCore", 1, 0),
                kubatech.api.enums.ItemList.DEFCAwakenedSchematic.get(0L))
            .itemOutputs(GT_ModHandler.getModItem("DraconicEvolution", "draconiumEnergyCore", 1, 1))
            .eut(2_000_000)
            .duration(2000)
            .specialValue(3)
            .addTo(sFusionCraftingRecipes)
            .forEach(DEFCRecipes::addOldHiddenRecipe);

        // Dragon Blood
        if (LoaderReference.GTPlusPlus) {

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.dragon_egg, 0),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64))
                .fluidInputs(Materials.Radon.getPlasma(144))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 288))
                .eut(1_966_080)
                .duration(4200)
                .specialValue(3)
                .noOptimize()
                .addTo(sFusionCraftingRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_ModHandler.getModItem("witchery", "infinityegg", 0),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64))
                .fluidInputs(Materials.Radon.getPlasma(72))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.dragonblood"), 432))
                .eut(1_966_080)
                .duration(3600)
                .specialValue(3)
                .noOptimize()
                .addTo(sFusionCraftingRecipes);
        }
    }
}
