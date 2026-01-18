package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.RemoteIO;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class RecipesMachinesCustom {

    public static void loadRecipes() {
        xlTurbines();
        solarTower();
        chemPlant();
        algaeFarm();
        alloyBlastSmelter();
        quantumForceTransformer();
        treeGrowthSimulator();
        lftr();
        cyclotron();
        powerSubstation();
        zhuhai();
        milling();
        sparging();
        molecularTransformer();
        thermalBoiler();
    }

    private static void xlTurbines() {
        // Turbine Shaft
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Turbine_Shaft.get(1),
            new Object[] { "PMP", "LCL", "PMP", 'P', MaterialsAlloy.INCOLOY_DS.getPlateDouble(1), 'M',
                ItemList.Electric_Motor_HV.get(1), 'L', OrePrefixes.cell.get(Materials.Lubricant), 'C',
                ItemList.Casing_Gearbox_Titanium.get(1) });

        // Rotor Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(4),
                MaterialsAlloy.INCOLOY_DS.getScrew(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                MaterialsAlloy.TANTALUM_CARBIDE.getGear(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Hatch_Turbine_Rotor.get(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reinforced Steam Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                MaterialsAlloy.INCONEL_625.getScrew(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_LP.get(1))
            .fluidInputs(Materials.Aluminium.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // XL Turbo Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeSteamTurbine.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                MaterialsAlloy.INCOLOY_DS.getScrew(16),
                MaterialsAlloy.INCOLOY_DS.getGear(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Steam_Turbine.get(1))
            .fluidInputs(Materials.Titanium.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reinforced Gas Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine1.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                MaterialsAlloy.INCONEL_625.getScrew(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Gas.get(1))
            .fluidInputs(Materials.Titanium.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // XL Turbo Gas Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeGasTurbine.get(1),
                MaterialsAlloy.ZERON_100.getPlate(8),
                MaterialsAlloy.ZERON_100.getScrew(16),
                MaterialsAlloy.ZERON_100.getGear(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Gas_Turbine.get(1))
            .fluidInputs(Materials.Chrome.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Reinforced HP Steam Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine2.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(4),
                MaterialsAlloy.INCOLOY_DS.getScrew(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_HP.get(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // XL Turbo HP Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeHPSteamTurbine.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(8),
                MaterialsAlloy.INCONEL_625.getScrew(16),
                MaterialsAlloy.INCONEL_625.getGear(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_HPSteam_Turbine.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Reinforced Plasma Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine3.get(1),
                MaterialsAlloy.ZERON_100.getPlate(4),
                MaterialsAlloy.ZERON_100.getScrew(8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Plasma.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // XL Turbo Plasma Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargePlasmaTurbine.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                MaterialsAlloy.PIKYONIUM.getScrew(16),
                MaterialsAlloy.PIKYONIUM.getGear(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Plasma_Turbine.get(1))
            .fluidInputs(Materials.Iridium.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Reinforced SC Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.SC_Turbine_Casing.get(1),
                GGMaterial.lumiium.get(OrePrefixes.plate, 4),
                GGMaterial.lumiium.get(OrePrefixes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_SC.get(1))
            .fluidInputs(GGMaterial.adamantiumAlloy.getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // XL Turbo SC Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.SC_Fluid_Turbine.get(1),
                GGMaterial.dalisenite.get(OrePrefixes.plate, 8),
                GGMaterial.dalisenite.get(OrePrefixes.screw, 16),
                GGMaterial.dalisenite.get(OrePrefixes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_SCSteam_Turbine.get(1))
            .fluidInputs(GGMaterial.hikarium.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
    }

    private static void solarTower() {
        // Solar Tower
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_HV.get(4),
                MaterialsAlloy.MARAGING250.getPlate(8),
                MaterialsAlloy.MARAGING250.getBolt(8),
                MaterialsAlloy.MARAGING250.getScrew(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Industrial_Solar_Tower.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(16 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Structural Solar Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING350.getFrameBox(1),
                MaterialsAlloy.STAINLESS_STEEL.getPlate(4),
                MaterialsAlloy.MARAGING350.getScrew(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_Structural.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Salt Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING250.getFrameBox(1),
                MaterialsAlloy.STAINLESS_STEEL.getPlate(4),
                MaterialsAlloy.MARAGING250.getBolt(16),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Aluminium, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_SaltContainment.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Thermally Insulated Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING250.getFrameBox(1),
                MaterialsAlloy.STEEL_BLACK.getPlate(4),
                MaterialsAlloy.MARAGING250.getScrew(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_HeatContainment.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Solar Reflector
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_MV.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(2),
                MaterialsAlloy.INCONEL_625.getGear(4),
                ItemList.Electric_Motor_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4))
            .circuit(17)
            .itemOutputs(GregtechItemList.Solar_Tower_Reflector.get(1))
            .fluidInputs(Materials.Titanium.getMolten(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void chemPlant() {
        // Strong Bronze Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_1.get(2),
            BITS,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze) });

        // Sturdy Aluminium Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_2.get(2),
            BITS,
            new Object[] { "PPP", "hFw", "PPP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1))
            .circuit(2)
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_2.get(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        // ExxonMobil Chemical Plant
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_MV.get(4),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Aluminium, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 16),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Polyethylene, 4),
                MaterialsElements.STANDALONE.BLACK_METAL.getFrameBox(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.ChemicalPlant_Controller.get(1))
            .fluidInputs(MaterialsAlloy.STEEL_BLACK.getFluidStack(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Catalyst Housing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_LV.get(2),
                ItemList.Hatch_Input_Bus_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 48),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SolderingAlloy, 16))
            .circuit(15)
            .itemOutputs(GregtechItemList.Bus_Catalysts.get(1))
            .fluidInputs(Materials.Bronze.getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void algaeFarm() {
        // Algae Farm
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_ULV.get(4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 12),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 32),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 32))
            .circuit(21)
            .itemOutputs(GregtechItemList.AlgaeFarm_Controller.get(1))
            .fluidInputs(MaterialsAlloy.POTIN.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void alloyBlastSmelter() {
        // Alloy Blast Smelter
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_AlloyBlastSmelter.get(1),
            new Object[] { "PCP", "WMW", "PCP", 'P', MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(1), 'C', "circuitElite",
                'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'M', ItemList.Machine_IV_AlloySmelter });

        // Blast Smelter Casing Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_BlastSmelter.get(1),
            new Object[] { "PhP", "PFP", "PwP", 'P', MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(1), 'F',
                MaterialsAlloy.ZIRCONIUM_CARBIDE.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(6), MaterialsAlloy.ZIRCONIUM_CARBIDE.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_BlastSmelter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        // Blast Smelter Heat Containment Coil
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Coil_BlastSmelter.get(1),
            new Object[] { "PPP", "FCF", "PPP", 'P', MaterialsAlloy.STABALLOY.getPlate(1), 'F',
                MaterialsAlloy.STABALLOY.getFrameBox(1), 'C', ItemList.Casing_Gearbox_Titanium });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.STABALLOY.getPlate(6),
                MaterialsAlloy.STABALLOY.getFrameBox(2),
                ItemList.Casing_Gearbox_Titanium.get(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Coil_BlastSmelter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
    }

    private static void quantumForceTransformer() {
        // QFT Coil Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Infinity.get(1),
                ItemList.Reactor_Coolant_Sp_6.get(4),
                MaterialsAlloy.LAURENIUM.getPlateDouble(2),
                CustomItemList.eM_Coil.get(1))
            .itemOutputs(GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1))
            .fluidInputs(MaterialsAlloy.QUANTUM.getFluidStack(4 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Quantum Force Transformer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1),
            2048 * 120 * 20,
            2048,
            (int) TierEU.RECIPE_UIV,
            16,
            new Object[] { GregtechItemList.Controller_MolecularTransformer.get(1),
                GTModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 1),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8 }, ItemList.Electric_Pump_UEV.get(4),
                ItemList.Field_Generator_UEV.get(4), GregtechItemList.Laser_Lens_Special.get(1) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS),
                MaterialsAlloy.PIKYONIUM.getFluidStack(32 * INGOTS) },
            GregtechItemList.QuantumForceTransformer.get(1),
            3 * MINUTES,
            (int) TierEU.RECIPE_UIV);
    }

    private static void treeGrowthSimulator() {
        // Tree Growth Simulator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_TreeFarm.get(1),
            new Object[] { "FRF", "PHP", "FXF", 'F', ItemList.Field_Generator_IV, 'R',
                MaterialsAlloy.INCOLOY_MA956.getRotor(1), 'P', MaterialsAlloy.NITINOL_60.getPlate(1), 'H',
                GregtechItemList.GTPP_Casing_IV.get(1), 'X',
                MaterialsAlloy.INCONEL_792.getComponentByPrefix(OrePrefixes.pipeMedium, 1) });

        // Sterile Farm Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TUMBAGA.getFrameBox(1),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1),
                ItemList.MV_Coil.get(1),
                ItemList.IC2_Plantball.get(4),
                GTOreDictUnificator.get(OrePrefixes.plank, Materials.Wood, 8))
            .circuit(2)
            .itemOutputs(GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1))
            .fluidInputs(GTModHandler.getDistilledWater(2_000))
            .duration(10 * SECONDS)
            .eut(64)
            .addTo(assemblerRecipes);
    }

    private static void lftr() {
        // Thorium Reactor [LFTR]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ThoriumReactor.get(1),
            new Object[] { "ABA", "CDC", "EFE", 'A', GregtechItemList.LFTRControlCircuit, 'B',
                OrePrefixes.cableGt12.get(Materials.Naquadah), 'C', MaterialsAlloy.HASTELLOY_N.getPlateDouble(1), 'D',
                GregtechItemList.Gregtech_Computer_Cube, 'E', MaterialsElements.getInstance().THORIUM232.getPlate(1),
                'F', ItemList.Hull_IV });

        // Reactor Shield Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Reactor_II.get(1),
            new Object[] { "PdP", "GFG", "PhP", 'P', MaterialsAlloy.HASTELLOY_C276.getPlateDouble(1), 'G',
                MaterialsAlloy.TALONITE.getGear(1), 'F', ItemList.Field_Generator_LV });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.HASTELLOY_C276.getPlateDouble(4),
                MaterialsAlloy.TALONITE.getGear(2),
                ItemList.Field_Generator_LV.get(1))
            .itemOutputs(GregtechItemList.Casing_Reactor_II.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hastelloy-N Reactor Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Reactor_I.get(1),
            new Object[] { "PIP", "IFI", "PIP", 'P', MaterialsAlloy.HASTELLOY_N.getPlateDouble(1), 'I',
                getModItem(Mods.IndustrialCraft2.ID, "reactorPlatingHeat", 1), 'F',
                MaterialsAlloy.HASTELLOY_C276.getFrameBox(1) });

        // LFTR Control Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                ItemList.Field_Generator_HV.get(1))
            .itemOutputs(GregtechItemList.LFTRControlCircuit.get(1))
            .duration(4 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Reactor Fuel Processing Plant
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_FuelRefinery.get(1),
            new Object[] { "CiC", "PXP", "GHG", 'C', "circuitElite", 'P',
                OrePrefixes.plateDense.get(Materials.TungstenSteel), 'X', GregtechItemList.Gregtech_Computer_Cube, 'G',
                MaterialsAlloy.STELLITE.getGear(1), 'H', ItemList.Hull_IV });

        // Incoloy-DS Fluid Containment Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_Internal.get(1),
            new Object[] { "PHP", "GTG", "PHP", 'P', MaterialsAlloy.INCOLOY_DS.getPlate(1), 'H',
                MaterialsAlloy.STABALLOY.getComponentByPrefix(OrePrefixes.pipeHuge, 1), 'G',
                MaterialsAlloy.INCOLOY_DS.getGear(1), 'T', ItemList.Super_Tank_IV });

        // Hastelloy-N Sealant Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_External.get(1),
            new Object[] { "ABA", "BFB", "ABA", 'A', MaterialsAlloy.INCOLOY_MA956.getPlate(1), 'B',
                MaterialsAlloy.HASTELLOY_N.getPlate(1), 'F', MaterialsAlloy.HASTELLOY_C276.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.HASTELLOY_N.getPlate(4),
                MaterialsAlloy.INCOLOY_MA956.getPlate(4),
                MaterialsAlloy.HASTELLOY_C276.getFrameBox(1))
            .itemOutputs(GregtechItemList.Casing_Refinery_External.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hastelloy-X Structural Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_Structural.get(1),
            new Object[] { "RGP", "hFw", "PHR", 'R', MaterialsAlloy.INCONEL_792.getRing(1), 'G',
                MaterialsAlloy.HASTELLOY_X.getGear(1), 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                MaterialsAlloy.HASTELLOY_C276.getFrameBox(1), 'H', ItemList.Casing_EV });

        // Cold Trap I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ColdTrap_IV.get(1),
            new Object[] { "PDP", "PCP", "RFR", 'P', MaterialsAlloy.INCONEL_625.getPlate(1), 'D',
                MaterialsAlloy.HASTELLOY_X.getPlateDouble(1), 'C', ItemList.Casing_IV, 'R', ItemList.Robot_Arm_IV, 'F',
                ItemList.Casing_FrostProof });

        // Cold Trap II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ColdTrap_ZPM.get(1),
            new Object[] { "PDP", "PCP", "RFR", 'P', MaterialsAlloy.PIKYONIUM.getPlate(1), 'D',
                MaterialsAlloy.HS188A.getPlateDouble(1), 'C', GregtechItemList.ColdTrap_IV, 'R', ItemList.Robot_Arm_ZPM,
                'F', ItemList.Casing_FrostProof });

        // Reactor Processing Unit I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ReactorProcessingUnit_IV.get(1),
            new Object[] { "FRP", "DCD", "PDF", 'F', ItemList.Field_Generator_HV, 'R', ItemList.Robot_Arm_IV, 'P',
                MaterialsAlloy.INCONEL_625.getPlate(1), 'D', MaterialsAlloy.HASTELLOY_N.getPlateDouble(1), 'C',
                ItemList.Machine_IV_ChemicalReactor });

        // Reactor Processing Unit II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ReactorProcessingUnit_ZPM.get(1),
            new Object[] { "FRP", "DCD", "PDF", 'F', ItemList.Field_Generator_IV, 'R', ItemList.Robot_Arm_ZPM, 'P',
                MaterialsAlloy.PIKYONIUM.getPlate(1), 'D', MaterialsAlloy.HS188A.getPlateDouble(1), 'C',
                GregtechItemList.ReactorProcessingUnit_IV });

        // Nuclear Salt Processing Plant
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Nuclear_Salt_Processing_Plant.get(1),
            new Object[] { "ABA", "CDC", "AEA", 'A', OrePrefixes.plate.get(Materials.Osmiridium), 'B',
                GregtechItemList.ReactorProcessingUnit_IV, 'C', WerkstoffLoader.Ruridit.get(OrePrefixes.plate), 'D',
                "circuitUltimate", 'E', GregtechItemList.ColdTrap_IV });
    }

    private static void cyclotron() {
        // Cyclotron Outer Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_FrostProof.get(1),
                GregtechItemList.DehydratorCoilWireEV.get(4),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                MaterialsAlloy.INCONEL_690.getScrew(16),
                MaterialsAlloy.EGLIN_STEEL.getLongRod(4),
                ItemList.Electric_Piston_HV.get(2))
            .itemOutputs(GregtechItemList.Casing_Cyclotron_External.get(1))
            .fluidInputs(MaterialsAlloy.ZIRCONIUM_CARBIDE.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Cyclotron Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Nichrome.get(1),
                GregtechItemList.DehydratorCoilWireIV.get(8),
                MaterialsAlloy.INCOLOY_MA956.getPlate(8),
                MaterialsAlloy.TANTALLOY_61.getBolt(16),
                MaterialsAlloy.INCOLOY_020.getScrew(32),
                ItemList.Field_Generator_EV.get(1))
            .itemOutputs(GregtechItemList.Casing_Cyclotron_Coil.get(1))
            .fluidInputs(MaterialsAlloy.HG1223.getFluidStack(5 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // COMET - Compact Cyclotron
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                GregtechItemList.Casing_Cyclotron_Coil.get(2),
                MaterialsAlloy.INCOLOY_020.getPlate(8),
                MaterialsAlloy.TANTALLOY_61.getGear(2),
                MaterialsAlloy.INCOLOY_MA956.getScrew(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 16))
            .itemOutputs(GregtechItemList.COMET_Cyclotron.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_020.getFluidStack(9 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void powerSubstation() {
        // Sub-Station External Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Power_SubStation.get(1),
            new Object[] { "SPS", "PFP", "SPS", 'S', OrePrefixes.screw.get(Materials.Titanium), 'P',
                MaterialsAlloy.INCOLOY_020.getPlate(1), 'F', MaterialsAlloy.INCOLOY_MA956.getFrameBox(1) });

        // Power Station Control Node
        GTModHandler.addCraftingRecipe(
            GregtechItemList.PowerSubStation.get(1),
            new Object[] { "ABA", "CDC", "EAE", 'A', MaterialsAlloy.INCOLOY_MA956.getPlate(1), 'B',
                GregtechItemList.LFTRControlCircuit, 'C', GregtechItemList.Casing_Power_SubStation, 'D',
                GregtechItemList.Casing_Vanadium_Redox, 'E', MaterialsAlloy.INCOLOY_020.getPlate(1) });

        // Vanadium Redox Power Cell (EV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.HalfCompleteCasing_II.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox.get(1))
            .fluidInputs(Materials.Oxygen.getGas(16_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (IV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_IV.get(1))
            .fluidInputs(Materials.Nitrogen.getGas(16_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (LuV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_LuV.get(1))
            .fluidInputs(Materials.Helium.getGas(8_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (ZPM)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_LuV.get(1),
                ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1))
            .fluidInputs(Materials.Argon.getGas(4_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (UV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_UV.get(1))
            .fluidInputs(Materials.Radon.getGas(4_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (UHV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_MAX.get(1))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(500))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void zhuhai() {
        // Aquatic Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_FishPond.get(1),
            new Object[] { "PhP", "EFE", "PwP", 'P', MaterialsAlloy.AQUATIC_STEEL.getPlate(1), 'E',
                MaterialsAlloy.EGLIN_STEEL.getPlate(1), 'F', MaterialsAlloy.EGLIN_STEEL.getFrameBox(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.AQUATIC_STEEL.getPlate(4),
                MaterialsAlloy.EGLIN_STEEL.getPlate(2),
                MaterialsAlloy.EGLIN_STEEL.getFrameBox(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_FishPond.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        // Zhuhai - Fishing Port
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_FishingPond.get(1),
            new Object[] { "PCP", "WFW", "PCP", 'P', MaterialsAlloy.AQUATIC_STEEL.getPlate(1), 'C', "circuitElite", 'W',
                OrePrefixes.wireFine.get(Materials.Electrum), 'F', GregtechItemList.FishTrap });
    }

    private static void milling() {
        // IsaMill Grinding Machine
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_Macerator.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Casing_IsaMill_Casing.get(4),
                GregtechItemList.Casing_IsaMill_Gearbox.get(4),
                ItemList.Component_Grinder_Tungsten.get(16),
                new Object[] { "circuitMaster", 16 },
                MaterialsAlloy.INCONEL_625.getGear(8),
                MaterialsAlloy.INCONEL_625.getPlate(32),
                MaterialsAlloy.ZERON_100.getPlateDouble(16),
                MaterialsAlloy.ZERON_100.getScrew(64))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(16 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_IsaMill.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // IsaMill Gearbox
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Gearbox_Titanium.get(2),
                MaterialsAlloy.INCONEL_625.getGear(4),
                MaterialsAlloy.INCONEL_625.getPlate(16))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Gearbox.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // IsaMill Exterior Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_MacerationStack.get(1),
                MaterialsAlloy.ZERON_100.getPlateDouble(2),
                MaterialsAlloy.ZERON_100.getRod(4),
                MaterialsAlloy.ZERON_100.getScrew(8))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Casing.get(1))
            .fluidInputs(Materials.Titanium.getMolten(4 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // IsaMill Piping
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_IsaMill_Casing.get(1),
                ItemList.Casing_Item_Pipe_Quantium.get(1),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.HSSE, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSE, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.HSSE, 8))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Pipe.get(1))
            .fluidInputs(Materials.Aluminium.getMolten(8 * INGOTS))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Flotation Cell Regulator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Distillation_Tower.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Machine_Adv_DistillationTower.get(2),
                GregtechItemList.Casing_Extruder.get(4L),
                GregtechItemList.Casing_Flotation_Cell.get(4),
                ItemList.Electric_Pump_LuV.get(4),
                MaterialsAlloy.STELLITE.getGear(8),
                MaterialsAlloy.STELLITE.getPlate(32),
                MaterialsAlloy.HASTELLOY_N.getPlateDouble(16),
                MaterialsAlloy.HASTELLOY_N.getScrew(64))
            .fluidInputs(
                MaterialsAlloy.INCONEL_625.getFluidStack(16 * INGOTS),
                MaterialsAlloy.INCONEL_792.getFluidStack(32 * INGOTS),
                MaterialsAlloy.HASTELLOY_N.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_Flotation_Cell.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Flotation Cell Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Extruder.get(4L),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.HSSG, 4),
                MaterialsAlloy.AQUATIC_STEEL.getPlate(8),
                MaterialsAlloy.AQUATIC_STEEL.getRing(8),
                MaterialsAlloy.AQUATIC_STEEL.getRotor(4))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_Flotation_Cell.get(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Ball Housing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_IV.get(1),
                ItemList.Hatch_Input_Bus_EV.get(1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 32),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SolderingAlloy, 16))
            .circuit(7)
            .itemOutputs(GregtechItemList.Bus_Milling_Balls.get(1))
            .fluidInputs(Materials.Tungsten.getMolten(8 * INGOTS))
            .duration(4 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void sparging() {
        // Research on Gas Sparging
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Helium.getCells(8),
                Materials.Fluorine.getCells(8),
                MaterialsAlloy.HS188A.getIngot(8),
                ItemList.Distillation_Tower.get(1))
            .circuit(8)
            .itemOutputs(
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Sparge Tower Controller
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Casing_Sparge_Tower_Exterior.get(4),
                GregtechItemList.GTPP_Casing_EV.get(4),
                ItemList.Machine_IV_Distillery.get(1),
                new Object[] { "circuitElite", 8 },
                MaterialsAlloy.HS188A.getGear(8),
                MaterialsAlloy.HS188A.getPlate(32),
                MaterialsAlloy.HASTELLOY_N.getPlateDouble(16),
                MaterialsAlloy.HASTELLOY_N.getScrew(64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64))
            .fluidInputs(
                MaterialsAlloy.INCOLOY_DS.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(32 * INGOTS),
                Materials.Titanium.getMolten(32 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_Sparge_Tower.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Sparge Tower Exterior Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_HV.get(1),
                MaterialsAlloy.HS188A.getPlate(2),
                MaterialsAlloy.HASTELLOY_N.getRing(4),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.TungstenSteel, 4),
                MaterialsAlloy.HASTELLOY_N.getScrew(4))
            .circuit(8)
            .itemOutputs(GregtechItemList.Casing_Sparge_Tower_Exterior.get(1))
            .fluidInputs(Materials.StainlessSteel.getMolten(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void molecularTransformer() {
        // Research on Molecular Transformation
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_LuV.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(16),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(32),
                MaterialsAlloy.HG1223.getFineWire(64),
                ItemList.Emitter_EV.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 10))
            .itemOutputs(
                ItemDummyResearch
                    .getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(16 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Molecular Transformer
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch
                    .getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                MaterialsAlloy.HG1223.getFineWire(64),
                MaterialsAlloy.HG1223.getFineWire(64),
                ItemList.Electric_Motor_IV.get(16),
                ItemList.Energy_LapotronicOrb.get(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Nichrome, 32),
                MaterialsAlloy.ZERON_100.getFrameBox(4),
                MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlateDouble(32),
                MaterialsAlloy.BABBIT_ALLOY.getPlate(64),
                MaterialsAlloy.LEAGRISIUM.getGear(8),
                new Object[] { "circuitData", 64 },
                new Object[] { "circuitElite", 32 },
                new Object[] { "circuitMaster", 16 },
                GregtechItemList.Laser_Lens_WoodsGlass.get(1))
            .fluidInputs(
                MaterialsAlloy.NITINOL_60.getFluidStack(18 * INGOTS),
                MaterialsAlloy.INCOLOY_MA956.getFluidStack(72 * INGOTS),
                MaterialsAlloy.KANTHAL.getFluidStack(4 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_MolecularTransformer.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Molecular Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.ZERON_100.getPlate(4),
                MaterialsAlloy.ZERON_100.getScrew(8),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Palladium, 16),
                ItemList.Sensor_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_1.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // High Voltage Current Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.INCONEL_625.getPlate(4),
                MaterialsAlloy.INCONEL_625.getScrew(8),
                ItemList.Casing_Coil_Nichrome.get(2),
                ItemList.Field_Generator_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_2.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Particle Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.glowstone, 16),
                MaterialsAlloy.INCONEL_625.getGear(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Titanium, 4),
                ItemList.Field_Generator_EV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_3.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void thermalBoiler() {
        // Thermal Boiler
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Thermal_Boiler.get(1),
            new Object[] { "LCL", "GIG", "LCL", 'L', getModItem(RemoteIO.ID, "tile.machine", 1, 1), 'C',
                ItemList.Machine_HV_Centrifuge, 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel), 'I',
                "circuitElite" });

        // Thermal Containment Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_ThermalContainment.get(2),
            new Object[] { "PSP", "CHC", "PPP", 'P', MaterialsAlloy.MARAGING350.getPlate(1), 'S',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'C', "circuitAdvanced", 'H', ItemList.Casing_HV });

        // Lava Filter
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 32),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 32),
                MaterialsAlloy.TUMBAGA.getRing(16),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 4),
                getModItem(Mods.IndustrialCraft2.ID, "itemPartCarbonMesh", 64, 0))
            .circuit(18)
            .itemOutputs(GregtechItemList.LavaFilter.get(16))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(1 * INGOTS))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(240)
            .addTo(assemblerRecipes);
    }
}
