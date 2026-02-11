package tectech.loader.recipe;

import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FOG_EXOTIC_TIER;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_MULTISTEP;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_TIER;
import static gregtech.api.util.GTRecipeConstants.FOG_UPGRADE_NAME_SHORT;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.recipe.TecTechRecipeMaps.godforgeExoticMatterRecipes;
import static tectech.recipe.TecTechRecipeMaps.godforgePlasmaRecipes;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.getRandomIntInRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;

public class Godforge implements Runnable {

    public static final ArrayList<Materials> plasmaGTMaterialList = new ArrayList<>();
    public static final ArrayList<Integer> plasmaGTWeightList = new ArrayList<>();
    public static final HashMap<ItemStack, Integer> exoticModulePlasmaItemMap = new HashMap<>();
    public static final HashMap<FluidStack, Integer> exoticModulePlasmaFluidMap = new HashMap<>();
    public static final HashMap<ItemStack, Integer> exoticModuleMagmatterItemMap = new HashMap<>();
    public static final List<ItemStack> quarkGluonFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> quarkGluonItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterTimeFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterSpaceFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterItemsForNEI = new ArrayList<>();

    private FluidStack[] convertToFluid(ItemStack[] items) {
        List<FluidStack> molten = new ArrayList<>();

        for (ItemStack itemStack : items) {
            String dict = OreDictionary.getOreName(OreDictionary.getOreIDs(itemStack)[0]);
            // substring 4 because dust is 4 characters long and there is no other possible oreDict
            String strippedOreDict = dict.substring(4);
            molten.add(FluidRegistry.getFluidStack("molten." + strippedOreDict.toLowerCase(), 1 * INGOTS));

        }

        return molten.toArray(new FluidStack[0]);
    }

    @Override
    public void run() {
        // Solid to plasma recipes
        {
            // Fusion tier 1-3
            {
                // Single step
                ItemStack[] solids_t0_1step = { Materials.Aluminium.getDust(1), Materials.Iron.getDust(1),
                    Materials.Calcium.getDust(1), Materials.Sulfur.getDust(1), Materials.Zinc.getDust(1),
                    Materials.Niobium.getDust(1), Materials.Tin.getDust(1), Materials.Titanium.getDust(1),
                    Materials.Nickel.getDust(1), Materials.Silver.getDust(1), Materials.Americium.getDust(1),
                    Materials.Antimony.getDust(1), Materials.Ardite.getDust(1), Materials.Arsenic.getDust(1),
                    Materials.Barium.getDust(1), Materials.Beryllium.getDust(1), Materials.Caesium.getDust(1),
                    Materials.Cadmium.getDust(1), Materials.Carbon.getDust(1), Materials.Cerium.getDust(1),
                    Materials.Cobalt.getDust(1), Materials.Copper.getDust(1), Materials.Desh.getDust(1),
                    Materials.Dysprosium.getDust(1), Materials.Erbium.getDust(1), Materials.Europium.getDust(1),
                    Materials.Gadolinium.getDust(1), Materials.Gallium.getDust(1), Materials.Gold.getDust(1),
                    Materials.Holmium.getDust(1), Materials.Indium.getDust(1), Materials.Lanthanum.getDust(1),
                    Materials.Lithium.getDust(1), Materials.Lutetium.getDust(1), Materials.Magnesium.getDust(1),
                    Materials.Manganese.getDust(1), Materials.MeteoricIron.getDust(1), Materials.Molybdenum.getDust(1),
                    Materials.Neodymium.getDust(1), Materials.Oriharukon.getDust(1), Materials.Palladium.getDust(1),
                    Materials.Phosphorus.getDust(1), Materials.Potassium.getDust(1), Materials.Praseodymium.getDust(1),
                    Materials.Promethium.getDust(1), Materials.Rubidium.getDust(1), Materials.Samarium.getDust(1),
                    Materials.Silicon.getDust(1), Materials.Sodium.getDust(1), Materials.Strontium.getDust(1),
                    Materials.Tantalum.getDust(1), Materials.Tellurium.getDust(1), Materials.Terbium.getDust(1),
                    Materials.Thulium.getDust(1), Materials.Tungsten.getDust(1), Materials.Uranium.getDust(1),
                    Materials.Uranium235.getDust(1), Materials.Vanadium.getDust(1), Materials.Ytterbium.getDust(1),
                    Materials.Yttrium.getDust(1), Materials.Chrome.getDust(1),
                    MaterialsElements.getInstance().ZIRCONIUM.getDust(1),
                    MaterialsElements.getInstance().THORIUM232.getDust(1),
                    MaterialsElements.getInstance().GERMANIUM.getDust(1),
                    MaterialsElements.getInstance().THALLIUM.getDust(1),
                    MaterialsElements.getInstance().RUTHENIUM.getDust(1),
                    MaterialsElements.getInstance().RHENIUM.getDust(1),
                    MaterialsElements.getInstance().RHODIUM.getDust(1),
                    MaterialsElements.getInstance().IODINE.getDust(1),
                    MaterialsElements.getInstance().HAFNIUM.getDust(1),
                    MaterialsElements.getInstance().CURIUM.getDust(1) };

                FluidStack[] molten_t0_1step = convertToFluid(solids_t0_1step);

                FluidStack[] solid_plasmas_t0_1step = { Materials.Aluminium.getPlasma(1 * INGOTS),
                    Materials.Iron.getPlasma(1 * INGOTS), Materials.Calcium.getPlasma(1 * INGOTS),
                    Materials.Sulfur.getPlasma(1 * INGOTS), Materials.Zinc.getPlasma(1 * INGOTS),
                    Materials.Niobium.getPlasma(1 * INGOTS), Materials.Tin.getPlasma(1 * INGOTS),
                    Materials.Titanium.getPlasma(1 * INGOTS), Materials.Nickel.getPlasma(1 * INGOTS),
                    Materials.Silver.getPlasma(1 * INGOTS), Materials.Americium.getPlasma(1 * INGOTS),
                    Materials.Antimony.getPlasma(1 * INGOTS), Materials.Ardite.getPlasma(1 * INGOTS),
                    Materials.Arsenic.getPlasma(1 * INGOTS), Materials.Barium.getPlasma(1 * INGOTS),
                    Materials.Beryllium.getPlasma(1 * INGOTS), Materials.Caesium.getPlasma(1 * INGOTS),
                    Materials.Cadmium.getPlasma(1 * INGOTS), Materials.Carbon.getPlasma(1 * INGOTS),
                    Materials.Cerium.getPlasma(1 * INGOTS), Materials.Cobalt.getPlasma(1 * INGOTS),
                    Materials.Copper.getPlasma(1 * INGOTS), Materials.Desh.getPlasma(1 * INGOTS),
                    Materials.Dysprosium.getPlasma(1 * INGOTS), Materials.Erbium.getPlasma(1 * INGOTS),
                    Materials.Europium.getPlasma(1 * INGOTS), Materials.Gadolinium.getPlasma(1 * INGOTS),
                    Materials.Gallium.getPlasma(1 * INGOTS), Materials.Gold.getPlasma(1 * INGOTS),
                    Materials.Holmium.getPlasma(1 * INGOTS), Materials.Indium.getPlasma(1 * INGOTS),
                    Materials.Lanthanum.getPlasma(1 * INGOTS), Materials.Lithium.getPlasma(1 * INGOTS),
                    Materials.Lutetium.getPlasma(1 * INGOTS), Materials.Magnesium.getPlasma(1 * INGOTS),
                    Materials.Manganese.getPlasma(1 * INGOTS), Materials.MeteoricIron.getPlasma(1 * INGOTS),
                    Materials.Molybdenum.getPlasma(1 * INGOTS), Materials.Neodymium.getPlasma(1 * INGOTS),
                    Materials.Oriharukon.getPlasma(1 * INGOTS), Materials.Palladium.getPlasma(1 * INGOTS),
                    Materials.Phosphorus.getPlasma(1 * INGOTS), Materials.Potassium.getPlasma(1 * INGOTS),
                    Materials.Praseodymium.getPlasma(1 * INGOTS), Materials.Promethium.getPlasma(1 * INGOTS),
                    Materials.Rubidium.getPlasma(1 * INGOTS), Materials.Samarium.getPlasma(1 * INGOTS),
                    Materials.Silicon.getPlasma(1 * INGOTS), Materials.Sodium.getPlasma(1 * INGOTS),
                    Materials.Strontium.getPlasma(1 * INGOTS), Materials.Tantalum.getPlasma(1 * INGOTS),
                    Materials.Tellurium.getPlasma(1 * INGOTS), Materials.Terbium.getPlasma(1 * INGOTS),
                    Materials.Thulium.getPlasma(1 * INGOTS), Materials.Tungsten.getPlasma(1 * INGOTS),
                    Materials.Uranium.getPlasma(1 * INGOTS), Materials.Uranium235.getPlasma(1 * INGOTS),
                    Materials.Vanadium.getPlasma(1 * INGOTS), Materials.Ytterbium.getPlasma(1 * INGOTS),
                    Materials.Yttrium.getPlasma(1 * INGOTS), Materials.Chrome.getPlasma(1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().ZIRCONIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().THORIUM232.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().GERMANIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().THALLIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().RUTHENIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().RHENIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().RHODIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().IODINE.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().HAFNIUM.getPlasma(), 1 * INGOTS),
                    new FluidStack(MaterialsElements.getInstance().CURIUM.getPlasma(), 1 * INGOTS) };

                for (int i = 0; i < solids_t0_1step.length; i++) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(solids_t0_1step[i])
                        .fluidOutputs(solid_plasmas_t0_1step[i])
                        .duration(10 * TICKS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);

                    if (molten_t0_1step[i] != null) {
                        GTValues.RA.stdBuilder()
                            .fluidInputs(molten_t0_1step[i])
                            .fluidOutputs(solid_plasmas_t0_1step[i])
                            .duration(10 * TICKS)
                            .eut(TierEU.RECIPE_MAX)
                            .metadata(FOG_PLASMA_MULTISTEP, false)
                            .metadata(FOG_PLASMA_TIER, 0)
                            .addTo(godforgePlasmaRecipes);
                    }
                }
            }

            // Multi-step
            ItemStack[] solids_t0_xstep = { Materials.Force.getDust(1), Materials.Bismuth.getDust(1),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getDust(1), Materials.Boron.getDust(1),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1), MaterialsElements.STANDALONE.RUNITE.getDust(1),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1), Materials.Iridium.getDust(1),
                Materials.Naquadah.getDust(1), Materials.Osmium.getDust(1), Materials.Platinum.getDust(1),
                Materials.Plutonium.getDust(1), MaterialsElements.getInstance().CALIFORNIUM.getDust(1) };

            FluidStack[] molten_t0_xstep = convertToFluid(solids_t0_xstep);

            FluidStack[] solid_plasmas_t0_xstep = {
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 1 * INGOTS),
                Materials.Bismuth.getPlasma(1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(), 1 * INGOTS),
                Materials.Boron.getPlasma(1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 1 * INGOTS),
                Materials.Iridium.getPlasma(1 * INGOTS), Materials.Naquadah.getPlasma(1 * INGOTS),
                Materials.Osmium.getPlasma(1 * INGOTS), Materials.Platinum.getPlasma(1 * INGOTS),
                Materials.Plutonium.getPlasma(1 * INGOTS),
                new FluidStack(MaterialsElements.getInstance().CALIFORNIUM.getPlasma(), 1 * INGOTS) };

            for (int i = 0; i < solids_t0_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t0_xstep[i])
                    .fluidOutputs(solid_plasmas_t0_xstep[i])
                    .duration(2 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 0)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t0_xstep[i] != null) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t0_xstep[i])
                        .fluidOutputs(solid_plasmas_t0_xstep[i])
                        .duration(2 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }
            }
        }
        // Fusion tier 4-5
        {
            // Single step
            ItemStack[] solids_t1_1step = { Materials.Lead.getDust(1), Materials.Plutonium241.getDust(1),
                Materials.Thorium.getDust(1), Materials.Naquadria.getDust(1) };

            FluidStack[] molten_t1_1step = convertToFluid(solids_t1_1step);

            FluidStack[] solid_plasmas_t1_1step = { Materials.Lead.getPlasma(1 * INGOTS),
                Materials.Plutonium241.getPlasma(1 * INGOTS), Materials.Thorium.getPlasma(1 * INGOTS),
                Materials.Naquadria.getPlasma(1 * INGOTS) };

            for (int i = 0; i < solids_t1_1step.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t1_1step[i])
                    .fluidOutputs(solid_plasmas_t1_1step[i])
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, false)
                    .metadata(FOG_PLASMA_TIER, 1)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t1_1step[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t1_1step[i])
                        .fluidOutputs(solid_plasmas_t1_1step[i])
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 1)
                        .addTo(godforgePlasmaRecipes);
                }
            }

            // Multi-step
            ItemStack[] solids_t1_xstep = { MaterialsElements.getInstance().NEPTUNIUM.getDust(1),
                MaterialsElements.getInstance().FERMIUM.getDust(1) };

            FluidStack[] molten_t1_xstep = convertToFluid(solids_t1_xstep);

            FluidStack[] solid_plasmas_t1_xstep = {
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 1 * INGOTS) };

            for (int i = 0; i < solids_t1_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t1_xstep[i])
                    .fluidOutputs(solid_plasmas_t1_xstep[i])
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 1)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t1_xstep[i] != null) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t1_xstep[i])
                        .fluidOutputs(solid_plasmas_t1_xstep[i])
                        .duration(7 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 1)
                        .addTo(godforgePlasmaRecipes);
                }
            }
        }
        // Exotic Plasmas
        {
            // Single step
            ItemStack[] solids_t2_1step = { MaterialsElements.STANDALONE.RHUGNOR.getDust(1),
                MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(1), Materials.Bedrockium.getDust(1),
                Materials.CosmicNeutronium.getDust(1), Materials.Draconium.getDust(1),
                Materials.DraconiumAwakened.getDust(1), Materials.Ichorium.getDust(1), };

            FluidStack[] molten_t2_1step = convertToFluid(solids_t2_1step);

            FluidStack[] solid_plasmas_t2_1step = {
                new FluidStack(MaterialsElements.STANDALONE.RHUGNOR.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.DRAGON_METAL.getPlasma(), 1 * INGOTS),
                new FluidStack(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlasma(), 1 * INGOTS),
                Materials.Bedrockium.getPlasma(1 * INGOTS), Materials.CosmicNeutronium.getPlasma(1 * INGOTS),
                Materials.Draconium.getPlasma(1 * INGOTS), Materials.DraconiumAwakened.getPlasma(1 * INGOTS),
                Materials.Ichorium.getPlasma(1 * INGOTS), };

            for (int i = 0; i < solids_t2_1step.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t2_1step[i])
                    .fluidOutputs(solid_plasmas_t2_1step[i])
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, false)
                    .metadata(FOG_PLASMA_TIER, 2)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t2_1step[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t2_1step[i])
                        .fluidOutputs(solid_plasmas_t2_1step[i])
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 2)
                        .addTo(godforgePlasmaRecipes);
                }
            }

            // Multi-step
            ItemStack[] solids_t2_xstep = { MaterialsElements.STANDALONE.HYPOGEN.getDust(1),
                Materials.Tritanium.getDust(1), Materials.Flerovium.getDust(1), Materials.Neutronium.getDust(1),
                Materials.Infinity.getDust(1), Materials.SixPhasedCopper.getDust(1), };

            FluidStack[] molten_t2_xstep = convertToFluid(solids_t2_xstep);

            FluidStack[] solid_plasmas_t2_xstep = {
                new FluidStack(MaterialsElements.STANDALONE.HYPOGEN.getPlasma(), 144),
                Materials.Tritanium.getPlasma(1 * INGOTS), Materials.Flerovium.getPlasma(1 * INGOTS),
                Materials.Neutronium.getPlasma(1 * INGOTS), Materials.Infinity.getPlasma(1 * INGOTS),
                Materials.SixPhasedCopper.getPlasma(1 * INGOTS), };

            for (int i = 0; i < solids_t2_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t2_xstep[i])
                    .fluidOutputs(solid_plasmas_t2_xstep[i])
                    .duration(25 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 2)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t2_xstep[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t2_xstep[i])
                        .fluidOutputs(solid_plasmas_t2_xstep[i])
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 2)
                        .addTo(godforgePlasmaRecipes);
                }

            }

        }

        // Fluid to plasma recipes
        {
            // Fusion tier 1-3
            {
                // Single step
                FluidStack[] fluids_t0_1step = { Materials.Helium.getGas(500), Materials.Nitrogen.getGas(500),
                    Materials.Argon.getGas(500), Materials.Chlorine.getGas(500), Materials.Deuterium.getGas(500),
                    Materials.Fluorine.getGas(500), Materials.Hydrogen.getGas(500), Materials.Radon.getGas(500),
                    Materials.Tritium.getGas(500), Materials.Mercury.getFluid(500) };
                FluidStack[] fluid_plasmas_t0_1step = { Materials.Helium.getPlasma(500),
                    Materials.Nitrogen.getPlasma(500), Materials.Argon.getPlasma(500),
                    Materials.Chlorine.getPlasma(500), Materials.Deuterium.getPlasma(500),
                    Materials.Fluorine.getPlasma(500), Materials.Hydrogen.getPlasma(500),
                    Materials.Radon.getPlasma(500), Materials.Tritium.getPlasma(500),
                    Materials.Mercury.getPlasma(500) };

                for (int i = 0; i < fluids_t0_1step.length; i++) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(fluids_t0_1step[i])
                        .fluidOutputs(fluid_plasmas_t0_1step[i])
                        .duration(1 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }

                // Multi-step
                FluidStack[] fluids_t0_xstep = { MaterialsElements.getInstance().NEON.getFluidStack(500),
                    Materials.Oxygen.getGas(500), MaterialsElements.getInstance().KRYPTON.getFluidStack(500),
                    MaterialsElements.getInstance().XENON.getFluidStack(500) };
                FluidStack[] fluid_plasmas_t0_xstep = {
                    new FluidStack(MaterialsElements.getInstance().NEON.getPlasma(), 500),
                    Materials.Oxygen.getPlasma(500),
                    new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 500),
                    new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 500) };

                for (int i = 0; i < fluids_t0_xstep.length; i++) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(fluids_t0_xstep[i])
                        .fluidOutputs(fluid_plasmas_t0_xstep[i])
                        .duration(3 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }
            }
            // Fusion tier 4-5
            {
                // Single step
                // None yet

                // Multi-step
                // None yet
            }
            // Exotic
            {
                // None yet
            }
        }

        // Exotic module fake recipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Iron.getDust(1))
                .fluidInputs(Materials.Iron.getMolten(1))
                .fluidOutputs(Materials.QuarkGluonPlasma.getFluid(1_000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MAX)
                .metadata(FOG_EXOTIC_TIER, 1)
                .ignoreCollision()
                .fake()
                .addTo(godforgeExoticMatterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(Materials.Iron.getDust(1))
                .fluidInputs(Materials.Iron.getMolten(1), Materials.Bismuth.getMolten(1))
                .fluidOutputs(Materials.MagMatter.getMolten(4 * INGOTS))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MAX)
                .metadata(FOG_EXOTIC_TIER, 1)
                .ignoreCollision()
                .fake()
                .addTo(godforgeExoticMatterRecipes);
        }

        // Exotic module plasma material maps
        // GT materials
        plasmaGTMaterialList.addAll(
            Arrays.asList(
                Materials.Aluminium,
                Materials.Americium,
                Materials.Antimony,
                Materials.Ardite,
                Materials.Argon,
                Materials.Arsenic,
                Materials.Barium,
                Materials.Beryllium,
                Materials.Caesium,
                Materials.Calcium,
                Materials.Cadmium,
                Materials.Carbon,
                Materials.Cerium,
                Materials.Chlorine,
                Materials.Cobalt,
                Materials.Copper,
                Materials.Desh,
                Materials.Deuterium,
                Materials.Dysprosium,
                Materials.Erbium,
                Materials.Europium,
                Materials.Fluorine,
                Materials.Gadolinium,
                Materials.Gallium,
                Materials.Gold,
                Materials.Helium,
                Materials.Holmium,
                Materials.Hydrogen,
                Materials.Indium,
                Materials.Iron,
                Materials.Lanthanum,
                Materials.Lithium,
                Materials.Lutetium,
                Materials.Magnesium,
                Materials.Manganese,
                Materials.MeteoricIron,
                Materials.Molybdenum,
                Materials.Neodymium,
                Materials.Nickel,
                Materials.Niobium,
                Materials.Nitrogen,
                Materials.Oriharukon,
                Materials.Palladium,
                Materials.Phosphorus,
                Materials.Potassium,
                Materials.Praseodymium,
                Materials.Promethium,
                Materials.Radon,
                Materials.Rubidium,
                Materials.Samarium,
                Materials.Silicon,
                Materials.Silver,
                Materials.Sodium,
                Materials.Strontium,
                Materials.Sulfur,
                Materials.Tantalum,
                Materials.Tellurium,
                Materials.Terbium,
                Materials.Thulium,
                Materials.Tin,
                Materials.Titanium,
                Materials.Tritium,
                Materials.Tungsten,
                Materials.Uranium235,
                Materials.Uranium,
                Materials.Vanadium,
                Materials.Ytterbium,
                Materials.Yttrium,
                Materials.Zinc));

        plasmaGTWeightList.addAll(
            Arrays.asList(
                6000 /* Aluminium */,
                10000 /* Americium */,
                6000 /* Antimony */,
                6000 /* Ardite */,
                6000 /* Argon */,
                6000 /* Arsenic */,
                6000 /* Barium */,
                6000 /* Beryllium */,
                6000 /* Caesium */,
                10000 /* Calcium */,
                6000 /* Cadmium */,
                6000 /* Carbon */,
                6000 /* Cerium */,
                6000 /* Chlorine */,
                6000 /* Cobalt */,
                6000 /* Copper */,
                6000 /* Desh */,
                6000 /* Deuterium */,
                2000 /* Dysprosium */,
                2000 /* Erbium */,
                6000 /* Europium */,
                6000 /* Fluorine */,
                2000 /* Gadolinium */,
                6000 /* Gallium */,
                6000 /* Gold */,
                10000 /* Helium */,
                6000 /* Holmium */,
                10000 /* Hydrogen */,
                6000 /* Indium */,
                10000 /* Iron */,
                6000 /* Lanthanum */,
                6000 /* Lithium */,
                6000 /* Lutetium */,
                6000 /* Magnesium */,
                6000 /* Manganese */,
                6000 /* Meteoric Iron */,
                6000 /* Molybdenum */,
                6000 /* Neodymium */,
                10000 /* Nickel */,
                10000 /* Niobium */,
                10000 /* Nitrogen */,
                6000 /* Oriharukon */,
                6000 /* Palladium */,
                6000 /* Phosphorus */,
                6000 /* Potassium */,
                6000 /* Praseodymium */,
                2000 /* Promethium */,
                10000 /* Radon */,
                2000 /* Rubidium */,
                6000 /* Samarium */,
                6000 /* Raw Silicon */,
                10000 /* Silver */,
                6000 /* Sodium */,
                2000 /* Strontium */,
                10000 /* Sulfur */,
                6000 /* Tantalum */,
                2000 /* Tellurium */,
                1000 /* Terbium */,
                6000 /* Thulium */,
                10000 /* Tin */,
                10000 /* Titanium */,
                6000 /* Tritium */,
                6000 /* Tungsten */,
                6000 /* Uranium 235 */,
                6000 /* Uranium 238 */,
                6000 /* Vanadium */,
                2000 /* Ytterbium */,
                6000 /* Yttrium */,
                6000 /* Zinc */));

        // GT++ materials
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().ZIRCONIUM.getDust(1), 6000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().THORIUM232.getDust(1), 6000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().GERMANIUM.getDust(1), 2000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().THALLIUM.getDust(1), 2000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().RUTHENIUM.getDust(1), 6000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().RHENIUM.getDust(1), 2000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().RHODIUM.getDust(1), 6000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().HAFNIUM.getDust(1), 6000);
        exoticModulePlasmaItemMap.put(MaterialsElements.getInstance().CURIUM.getDust(1), 10000);

        // Lanthanides Materials
        exoticModulePlasmaItemMap.put(WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 1), 6000);

        // Mercury is weird, it has neither dust nor gas, so it needs to be added separately
        exoticModulePlasmaFluidMap.put(Materials.Mercury.getFluid(1), 6000);

        // Loop for adding all GT plasma materials
        for (int i = 0; i < plasmaGTMaterialList.size(); i++) {
            if (plasmaGTMaterialList.get(i)
                .getDustTiny(1) != null) {
                exoticModulePlasmaItemMap.put(
                    plasmaGTMaterialList.get(i)
                        .getDust(1),
                    plasmaGTWeightList.get(i));
            } else {
                exoticModulePlasmaFluidMap.put(
                    plasmaGTMaterialList.get(i)
                        .getGas(1),
                    plasmaGTWeightList.get(i));
            }
        }

        // Magmatter map
        // GT materials
        exoticModuleMagmatterItemMap.put(Materials.CosmicNeutronium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Draconium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.DraconiumAwakened.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Ichorium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Neutronium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Flerovium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Bedrockium.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Infinity.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(Materials.Tritanium.getDust(1), 100000);

        // GT++ materials
        exoticModuleMagmatterItemMap.put(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialsElements.STANDALONE.HYPOGEN.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialsElements.STANDALONE.RHUGNOR.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialsElements.STANDALONE.DRAGON_METAL.getDust(1), 100000);

        // For NEI
        for (FluidStack fluid : exoticModulePlasmaFluidMap.keySet()) {
            fluid.amount = getRandomIntInRange(1, 64);
            quarkGluonFluidItemsForNEI.add(GTUtility.getFluidDisplayStack(fluid, true));
        }
        for (ItemStack item : exoticModulePlasmaItemMap.keySet()) {
            item.stackSize = getRandomIntInRange(1, 7);
            quarkGluonItemsForNEI.add(item);
        }
        for (int i = 0; i < 21; i++) {
            magmatterTimeFluidItemsForNEI
                .add(GTUtility.getFluidDisplayStack(Materials.Time.getMolten(getRandomIntInRange(1, 50)), true));
            magmatterSpaceFluidItemsForNEI
                .add(GTUtility.getFluidDisplayStack(Materials.Space.getMolten(getRandomIntInRange(51, 100)), true));
        }
        magmatterItemsForNEI.addAll(exoticModuleMagmatterItemMap.keySet());

        // Godforge upgrade materials
        if (EternalSingularity.isModLoaded()) {
            ForgeOfGodsUpgrade.START.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 64),
                ItemList.SuperconductorComposite.get(32),
                GGMaterial.metastableOganesson.get(OrePrefixes.gearGt, 16),
                getModItem(EternalSingularity.ID, "eternal_singularity", 8L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.FDIM.addExtraCost(
                GregtechItemList.Mega_AlloyBlastSmelter.get(16L),
                ItemList.Casing_Coil_Hypogen.get(64L),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(32L),
                getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                ItemRefer.Field_Restriction_Coil_T3.get(48),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.GPCI.addExtraCost(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(8),
                GregtechItemList.FusionComputer_UV3.get(8),
                GregtechItemList.Casing_Fusion_Internal2.get(64),
                ItemList.UHTResistantMesh.get(64),
                MaterialsAlloy.QUANTUM.getPlateDense(48),
                MaterialsElements.STANDALONE.RHUGNOR.getGear(32),
                getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.QGPIU.addExtraCost(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(16),
                ItemRefer.Compact_Fusion_MK5.get(2),
                ItemRefer.Compact_Fusion_Coil_T4.get(64),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(16),
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(4),
                MaterialsElements.STANDALONE.RHUGNOR.getGear(64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Ichorium, 64),
                getModItem(EternalSingularity.ID, "eternal_singularity", 32L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.CD.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 64),
                MaterialsElements.STANDALONE.HYPOGEN.getFrameBox(64),
                MaterialsElements.STANDALONE.DRAGON_METAL.getFrameBox(64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                CustomItemList.EOH_Infinite_Energy_Casing.get(8),
                ItemList.ZPM6.get(2),
                ItemList.Field_Generator_UMV.get(32));

            ForgeOfGodsUpgrade.EE.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Eternity, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Universium, 2),
                CustomItemList.EOH_Infinite_Energy_Casing.get(64),
                CustomItemList.StabilisationFieldGeneratorTier6.get(48),
                ItemList.ZPM6.get(16),
                ItemList.Transdimensional_Alignment_Matrix.get(8),
                CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(32),
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(64),
                ItemList.Field_Generator_UMV.get(64),
                ItemList.Robot_Arm_UMV.get(64));

            ForgeOfGodsUpgrade.END.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Eternity, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MagMatter, 64),
                CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(64),
                CustomItemList.astralArrayFabricator.get(4),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.MagMatter, 4),
                ItemList.ZPM6.get(32),
                ItemList.Field_Generator_UXV.get(64),
                ItemList.Robot_Arm_UXV.get(64));
        }

    }

    public static void runDevEnvironmentRecipes() {
        // put something in here to not crash the game in dev environment when opening the manual insertion window
        ForgeOfGodsUpgrade.START.addExtraCost(
            new ItemStack(Blocks.cobblestone, 4),
            new ItemStack(Blocks.dirt, 12),
            new ItemStack(Blocks.diamond_block, 8),
            new ItemStack(Blocks.gold_block, 32));
        ForgeOfGodsUpgrade.FDIM.addExtraCost(new ItemStack(Blocks.cobblestone, 8));
        ForgeOfGodsUpgrade.GPCI.addExtraCost(new ItemStack(Blocks.cobblestone, 12));
        ForgeOfGodsUpgrade.QGPIU.addExtraCost(new ItemStack(Blocks.cobblestone, 16));
        ForgeOfGodsUpgrade.CD.addExtraCost(new ItemStack(Blocks.cobblestone, 32));
        ForgeOfGodsUpgrade.EE.addExtraCost(new ItemStack(Blocks.cobblestone, 48));
        ForgeOfGodsUpgrade.END.addExtraCost(new ItemStack(Blocks.cobblestone, 64));
    }

    public static void addFakeUpgradeCostRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.START.getExtraCostNoNulls())
            .itemOutputs(
                CustomItemList.Godforge_GravitonFlowModulatorTier1.get(1),
                CustomItemList.Machine_Multi_SmeltingModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.START.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.FDIM.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_MoltenModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.FDIM.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.GPCI.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_PlasmaModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.GPCI.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.QGPIU.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(1))
            .fluidOutputs(Materials.QuarkGluonPlasma.getFluid(1_000))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.QGPIU.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.CD.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.CD.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.EE.getExtraCostNoNulls())
            .fluidOutputs(Materials.MagMatter.getMolten(4 * INGOTS))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.EE.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.END.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1), Materials.GravitonShard.getGems(1))
            .fluidOutputs(Materials.Neutronium.getPlasma(1_000))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.END.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
    }

    public static void initMoltenModuleRecipes() {
        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            List<ItemStack> itemOutputs = new ArrayList<>(1);
            List<FluidStack> fluidOutputs = new ArrayList<>(2);

            int[] originalChances = recipe.mOutputChances;
            IntList newChances = new IntArrayList();
            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack stack = recipe.getOutput(i);
                if (stack == null) continue;
                FluidStack potentialFluid = convertToMolten(stack);
                if (potentialFluid != null) {
                    potentialFluid.amount *= stack.stackSize;
                    fluidOutputs.add(potentialFluid);
                } else {
                    itemOutputs.add(stack);
                    if (originalChances != null) {
                        int chance = 10000;
                        if (originalChances.length > i) {
                            chance = originalChances[i];
                        }
                        newChances.add(chance);
                    }
                }
            }

            fluidOutputs.addAll(Arrays.asList(recipe.mFluidOutputs));
            Integer heat = recipe.getMetadata(COIL_HEAT);

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemOutputs(itemOutputs.toArray(new ItemStack[0]))
                .fluidOutputs(fluidOutputs.toArray(new FluidStack[0]))
                .duration(recipe.mDuration)
                .eut(recipe.mEUt)
                .specialValue(recipe.mSpecialValue);

            if (recipe.mInputs != null) builder.itemInputs(recipe.mInputs);
            if (recipe.mFluidInputs != null) builder.fluidInputs(recipe.mFluidInputs);
            if (!newChances.isEmpty()) builder.outputChances(newChances.toIntArray());
            if (heat != null) builder.metadata(COIL_HEAT, heat);

            builder.addTo(TecTechRecipeMaps.godforgeMoltenRecipes);
        }
    }

    private static FluidStack convertToMolten(ItemStack stack) {
        // if this is null it has to be a gt++ material
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        Materials mat = data == null ? null : data.mMaterial.mMaterial;
        if (mat != null) {
            if (mat.mStandardMoltenFluid != null) {
                return mat.getMolten(INGOTS * data.mMaterial.mAmount / GTValues.M);
            } else if (mat.mFluid != null) {
                return mat.getFluid(1_000);
            }
        }
        int[] oreIDs = OreDictionary.getOreIDs(stack);
        if (oreIDs.length == 0) {
            return null;
        }
        String dict = OreDictionary.getOreName(oreIDs[0]);

        // Check various oredicts
        String strippedOreDict = null;
        if (dict.startsWith("ingotHot")) {
            strippedOreDict = dict.substring(8);
        } else if (dict.startsWith("dustRoasted") && !dict.contains("Cobalt")) {
            strippedOreDict = dict.substring(11);
        }
        if (strippedOreDict != null) {
            return FluidRegistry.getFluidStack("molten." + strippedOreDict.toLowerCase(), 1 * INGOTS);
        }
        return null;
    }
}
