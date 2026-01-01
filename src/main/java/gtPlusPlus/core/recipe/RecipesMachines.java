package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.RemoteIO;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import toxiceverglades.dimension.DimensionEverglades;

public class RecipesMachines {

    public static void loadRecipes() {
        run();
        Logger.INFO("Loading Recipes for the Various machine blocks.");
    }

    private static void run() {

        initModItems();
        tieredMachineHulls();
        energyCores();
        wirelessChargers();
        largeArcFurnace();
        industrialVacuumFurnace();
        fakeMachineCasingCovers();
        overflowValveCovers();
        // superBuses();
        distillus();
        algaeFarm();
        chemPlant();
        zyngen();
        milling();
        sparging();
        chisels();
        rockBreaker();
        thermicFluidHeater();
        advHeatExchanger();
        chiselBuses();
        solidifierHatches();
        extrusionHatches();

        gt4FarmManager();
        gt4Inventory();

        multiForgeHammer();
        multiMolecularTransformer();
        multiXlTurbines();
        multiSolarTower();
        multiElementalDuplicator();

        resonanceChambers();
        modulators();
    }

    private static void thermicFluidHeater() {

        RecipeUtils.addShapedGregtechRecipe(
            CI.getPlate(5, 1),
            "circuitElite",
            CI.getPlate(5, 1),
            pipeTier7,
            ItemList.Machine_IV_FluidHeater.get(1),
            pipeTier7,
            CI.getPlate(5, 1),
            "circuitData",
            CI.getPlate(5, 1),
            GregtechItemList.Controller_IndustrialFluidHeater.get(1));
    }

    private static void advHeatExchanger() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_Multi_HeatExchanger.get(1),
                CI.getDoublePlate(6, 8),
                CI.getScrew(6, 16),
                CI.getCircuit(5, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.XL_HeatExchanger.get(1))
            .fluidInputs(CI.tieredMaterials[5].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_StableTitanium.get(1), CI.getPlate(5, 4), CI.getScrew(5, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_XL_HeatExchanger.get(1))
            .fluidInputs(CI.tieredMaterials[5].getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

    }

    private static void gt4FarmManager() {

        ItemList[] aInputHatches = new ItemList[] { ItemList.Hatch_Input_LV, ItemList.Hatch_Input_MV,
            ItemList.Hatch_Input_HV, ItemList.Hatch_Input_EV, ItemList.Hatch_Input_IV, ItemList.Hatch_Input_LuV,
            ItemList.Hatch_Input_ZPM, ItemList.Hatch_Input_UV };
        GregtechItemList[] aOutputMachines = new GregtechItemList[] { GregtechItemList.GT4_Crop_Harvester_LV,
            GregtechItemList.GT4_Crop_Harvester_MV, GregtechItemList.GT4_Crop_Harvester_HV,
            GregtechItemList.GT4_Crop_Harvester_EV, GregtechItemList.GT4_Crop_Harvester_IV,
            GregtechItemList.GT4_Crop_Harvester_LuV, GregtechItemList.GT4_Crop_Harvester_ZPM,
            GregtechItemList.GT4_Crop_Harvester_UV };

        int aTier = 1;
        for (int i = 0; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                CI.getRobotArm(aTier, 1),
                CI.getSensor(aTier, 1),
                CI.getRobotArm(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.getTieredMachineHull(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.circuits[aTier],
                aInputHatches[i].get(1),
                CI.circuits[aTier],
                aOutputMachines[i].get(1));
            aTier++;
        }
    }

    private static void gt4Inventory() {

        GregtechItemList[] aOutputElectricCraftingTable = new GregtechItemList[] {
            GregtechItemList.GT4_Electric_Auto_Workbench_LV, GregtechItemList.GT4_Electric_Auto_Workbench_MV,
            GregtechItemList.GT4_Electric_Auto_Workbench_HV, GregtechItemList.GT4_Electric_Auto_Workbench_EV,
            GregtechItemList.GT4_Electric_Auto_Workbench_IV, GregtechItemList.GT4_Electric_Auto_Workbench_LuV,
            GregtechItemList.GT4_Electric_Auto_Workbench_ZPM, GregtechItemList.GT4_Electric_Auto_Workbench_UV };

        int aTier = 1;
        for (int i = 0; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                new ItemStack(Blocks.crafting_table),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.circuits[aTier],
                CI.getTieredMachineHull(aTier),
                CI.circuits[aTier],
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                CI.getRobotArm(aTier, 1),
                ItemUtils.getOrePrefixStack(OrePrefixes.plate, CI.tieredMaterials[aTier], 1),
                aOutputElectricCraftingTable[i].get(1));
            aTier++;
        }
    }

    private static void multiForgeHammer() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(2),
                ItemList.Machine_IV_Hammer.get(1),
                CI.getPlate(4, 8),
                CI.getBolt(5, 32),
                MaterialsElements.getInstance().ZIRCONIUM.getFineWire(32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4L))
            .itemOutputs(GregtechItemList.Controller_IndustrialForgeHammer.get(1))
            .fluidInputs(CI.getTieredFluid(4, 12 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_IndustrialForgeHammer.get(1),
            CI.bitsd,
            new Object[] { "IBI", "HCH", "IHI", 'I', CI.getPlate(4, 1), 'B', MaterialsAlloy.BABBIT_ALLOY.getPlate(1),
                'C', ItemList.Casing_HeatProof.get(1), 'H', MaterialsAlloy.HASTELLOY_X.getRod(1) });
    }

    private static void multiMolecularTransformer() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(6, 1),
                CI.getPlate(5, 16),
                CI.getBolt(5, 32),
                MaterialsAlloy.HG1223.getFineWire(64),
                CI.getEmitter(4, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 10))
            .itemOutputs(
                ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .fluidInputs(CI.getTieredFluid(5, 16 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Molecular Transformer
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                MaterialsAlloy.HG1223.getFineWire(64),
                MaterialsAlloy.HG1223.getFineWire(64),
                ItemList.Electric_Motor_IV.get(16),
                ItemList.Energy_LapotronicOrb.get(16),
                CI.getTieredComponent(OrePrefixes.cableGt12, 6, 16),
                CI.getTieredComponent(OrePrefixes.wireGt16, 5, 32),
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
                MaterialsAlloy.INCOLOY_MA956.getFluidStack(1 * STACKS + 8 * INGOTS),
                MaterialsAlloy.KANTHAL.getFluidStack(4 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_MolecularTransformer.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getPlate(6, 4),
                CI.getScrew(6, 8),
                MaterialsElements.getInstance().PALLADIUM.getFineWire(16),
                CI.getSensor(5, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_1.get(1))
            .fluidInputs(CI.getTieredFluid(5, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getPlate(5, 4),
                CI.getScrew(5, 8),
                ItemList.Casing_Coil_Nichrome.get(2),
                CI.getFieldGenerator(3, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_2.get(1))
            .fluidInputs(CI.getTieredFluid(5, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.glowstone, 16),
                CI.getGear(5, 8),
                MaterialsElements.getInstance().TITANIUM.getWire04(4),
                CI.getFieldGenerator(4, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_3.get(1))
            .fluidInputs(CI.getTieredFluid(5, 4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

    }

    private static void multiXlTurbines() {

        RecipeUtils.addShapedRecipe(
            CI.getDoublePlate(4, 1),
            CI.getElectricMotor(3, 1),
            CI.getDoublePlate(4, 1),
            ItemUtils.getItemStackOfAmountFromOreDict("cellLubricant", 1),
            ItemList.Casing_Gearbox_Titanium.get(1),
            ItemUtils.getItemStackOfAmountFromOreDict("cellLubricant", 1),
            CI.getDoublePlate(4, 1),
            CI.getElectricMotor(3, 1),
            CI.getDoublePlate(4, 1),
            GregtechItemList.Casing_Turbine_Shaft.get(1));

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine.get(1),
                CI.getPlate(4, 4),
                CI.getScrew(4, 8),
                CI.getCircuit(4, 4),
                CI.getGear(3, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Hatch_Turbine_Rotor.get(1))
            .fluidInputs(CI.tieredMaterials[3].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Steam
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
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeSteamTurbine.get(1),
                CI.getPlate(4, 8),
                CI.getScrew(4, 16),
                CI.getGear(4, 4),
                CI.getCircuit(4, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Steam_Turbine.get(1))
            .fluidInputs(CI.tieredMaterials[4].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Gas
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Turbine1.get(1), CI.getPlate(5, 4), CI.getScrew(5, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Gas.get(1))
            .fluidInputs(CI.tieredMaterials[4].getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeGasTurbine.get(1),
                CI.getPlate(6, 8),
                CI.getScrew(6, 16),
                CI.getGear(6, 4),
                CI.getCircuit(6, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Gas_Turbine.get(1))
            .fluidInputs(CI.tieredMaterials[6].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // HP Steam
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Turbine2.get(1), CI.getPlate(4, 4), CI.getScrew(4, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_HP.get(1))
            .fluidInputs(CI.tieredMaterials[3].getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargeHPSteamTurbine.get(1),
                CI.getPlate(5, 8),
                CI.getScrew(5, 16),
                CI.getGear(5, 4),
                CI.getCircuit(5, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_HPSteam_Turbine.get(1))
            .fluidInputs(CI.tieredMaterials[5].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Plasma
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Turbine3.get(1), CI.getPlate(6, 4), CI.getScrew(6, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Plasma.get(1))
            .fluidInputs(CI.tieredMaterials[5].getMolten(2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.LargePlasmaTurbine.get(1),
                CI.getPlate(7, 8),
                CI.getScrew(7, 16),
                CI.getGear(7, 4),
                CI.getCircuit(7, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_Plasma_Turbine.get(1))
            .fluidInputs(CI.tieredMaterials[7].getMolten(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(supercriticalFluidTurbineCasing, 1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.plate), 4, 10101),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.screw), 8, 10101))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_SC.get(1))
            .fluidInputs(FluidRegistry.getFluidStack("molten.adamantium alloy", 2 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(1, Loaders.SCTurbine),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.plate), 8, 10104),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.screw), 16, 10104),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.gearGt), 4, 10104),
                CI.getCircuit(7, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Large_SCSteam_Turbine.get(1))
            .fluidInputs(FluidRegistry.getFluidStack("molten.hikarium", 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

    }

    private static void multiSolarTower() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(3, 4),
                MaterialsAlloy.MARAGING250.getPlate(8),
                MaterialsAlloy.MARAGING250.getBolt(8),
                MaterialsAlloy.MARAGING250.getScrew(8),
                CI.getCircuit(5, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Industrial_Solar_Tower.get(1))
            .fluidInputs(CI.getTieredFluid(3, 16 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING350.getFrameBox(1),
                MaterialsAlloy.STAINLESS_STEEL.getPlate(4),
                MaterialsAlloy.MARAGING350.getScrew(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_Structural.get(1))
            .fluidInputs(CI.getTieredFluid(3, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING250.getFrameBox(1),
                MaterialsAlloy.STAINLESS_STEEL.getPlate(4),
                MaterialsAlloy.MARAGING250.getBolt(16),
                MaterialsElements.getInstance().ALUMINIUM.getScrew(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_SaltContainment.get(1))
            .fluidInputs(CI.getTieredFluid(3, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.MARAGING250.getFrameBox(1),
                MaterialsAlloy.STEEL_BLACK.getPlate(4),
                MaterialsAlloy.MARAGING250.getScrew(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_HeatContainment.get(1))
            .fluidInputs(CI.getAlternativeTieredFluid(3, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(2, 1),
                MaterialsAlloy.INCONEL_625.getPlate(2),
                MaterialsAlloy.INCONEL_625.getGear(4),
                CI.getElectricMotor(3, 2),
                CI.getCircuit(3, 4))
            .circuit(17)
            .itemOutputs(GregtechItemList.Solar_Tower_Reflector.get(1))
            .fluidInputs(Materials.Titanium.getMolten(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

    }

    private static void multiElementalDuplicator() {

        // Elemental Duplicator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_Replicator.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CI.getTieredMachineHull(7, 4),
                CI.getFieldGenerator(5, 16),
                CI.getElectricMotor(7, 16),
                CI.getElectricPiston(7, 4),
                CI.getEnergyCore(6, 2),
                CI.getPlate(7, 16),
                CI.getScrew(7, 32),
                CI.getBolt(6, 32),
                CI.getTieredComponent(OrePrefixes.rod, 6, 10),
                new Object[] { "circuitUltimate", 20 },
                ItemList.Tool_DataOrb.get(32),
                GregtechItemList.Laser_Lens_Special.get(1))
            .fluidInputs(
                CI.getTieredFluid(7, 32 * INGOTS),
                CI.getAlternativeTieredFluid(6, 16 * INGOTS),
                CI.getTertiaryTieredFluid(6, 16 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(2 * STACKS))
            .itemOutputs(GregtechItemList.Controller_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Data Orb Repository
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Modulator_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CI.getTieredGTPPMachineCasing(7, 2),
                CI.getFieldGenerator(4, 4),
                CI.getEnergyCore(4, 2),
                CI.getPlate(7, 8),
                CI.getScrew(6, 16),
                CI.getBolt(6, 16),
                CI.getTieredComponent(OrePrefixes.rod, 5, 16),
                new Object[] { "circuitMaster", 32 },
                ItemList.Tool_DataOrb.get(32))
            .fluidInputs(
                CI.getTieredFluid(6, 16 * INGOTS),
                CI.getAlternativeTieredFluid(5, 8 * INGOTS),
                CI.getTertiaryTieredFluid(5, 8 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(1 * STACKS))
            .itemOutputs(GregtechItemList.Hatch_Input_Elemental_Duplicator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Elemental Confinement Shell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.ResonanceChamber_III.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                CI.getTieredMachineHull(6, 5),
                CI.getFieldGenerator(3, 16),
                CI.getEnergyCore(2, 2),
                CI.getPlate(7, 4),
                CI.getScrew(7, 4),
                CI.getBolt(6, 8),
                CI.getTieredComponent(OrePrefixes.rod, 5, 4),
                new Object[] { "circuitElite", 4 },
                ItemList.Tool_DataStick.get(4))
            .fluidInputs(
                CI.getTieredFluid(5, 16 * INGOTS),
                CI.getAlternativeTieredFluid(4, 8 * INGOTS),
                CI.getTertiaryTieredFluid(4, 8 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Casing_ElementalDuplicator.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    private static void resonanceChambers() {
        int aFieldTier = 1;
        int aCasingTier = 4;
        for (int i = 0; i < 4; i++) {
            RecipeUtils.addShapedRecipe(
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getTieredMachineCasing(aCasingTier),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                CI.getFieldGenerator(aFieldTier, 1),
                CI.getDoublePlate(aCasingTier, 1),
                new ItemStack(ModBlocks.blockSpecialMultiCasings2, 1, i));
            aCasingTier++;
            aFieldTier++;
        }
    }

    private static void modulators() {
        int aCasingTier = 4;
        for (int i = 4; i < 8; i++) {
            RecipeUtils.addShapedRecipe(
                circuits[aCasingTier],
                CI.getPlate(aCasingTier, 1),
                circuits[aCasingTier],
                CI.getPlate(aCasingTier, 1),
                CI.getTieredMachineCasing(aCasingTier),
                CI.getPlate(aCasingTier, 1),
                circuits[aCasingTier],
                CI.getPlate(aCasingTier, 1),
                circuits[aCasingTier],
                new ItemStack(ModBlocks.blockSpecialMultiCasings2, 1, i));
            aCasingTier++;
        }
    }

    private static void zyngen() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredMachineHull(4),
                ItemList.Machine_IV_AlloySmelter.get(1),
                CI.getGear(3, 16),
                CI.getBolt(3, 64),
                CI.getPlate(4, 16))
            .circuit(6)
            .itemOutputs(GregtechItemList.Industrial_AlloySmelter.get(1))
            .fluidInputs(CI.getAlternativeTieredFluid(4, 8 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

    }

    private static void chemPlant() {

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_1.get(2L),
            CI.bits,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                OrePrefixes.frameGt.get(Materials.Bronze) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_2.get(2L),
            CI.bits,
            new Object[] { "PPP", "hFw", "PPP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.get(Materials.Aluminium) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(2, 4),
                CI.getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.gearGt, 4),
                CI.getTieredComponentOfMaterial(Materials.AnnealedCopper, OrePrefixes.plate, 16),
                CI.getTieredComponentOfMaterial(Materials.Polyethylene, OrePrefixes.pipeLarge, 4),
                CI.getTieredComponent(OrePrefixes.frameGt, 2, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.ChemicalPlant_Controller.get(1))
            .fluidInputs(MaterialsAlloy.STEEL_BLACK.getFluidStack(8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(1, 2),
                ItemList.Hatch_Input_Bus_MV.get(1),
                CI.getTieredComponentOfMaterial(Materials.Bronze, OrePrefixes.gearGt, 8),
                CI.getTieredComponentOfMaterial(Materials.Lead, OrePrefixes.plate, 48),
                CI.getTieredComponentOfMaterial(Materials.SolderingAlloy, OrePrefixes.wireFine, 16))
            .circuit(15)
            .itemOutputs(GregtechItemList.Bus_Catalysts.get(1))
            .fluidInputs(MaterialsAlloy.BRONZE.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

    }

    private static void algaeFarm() {

        // Give the bad algae a use.
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.AlgaeBiomass.get(32))
            .itemOutputs(GregtechItemList.GreenAlgaeBiomass.get(4))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CI.getTieredGTPPMachineCasing(0, 4),
                CI.getTieredComponentOfMaterial(Materials.Aluminium, OrePrefixes.rod, 12),
                CI.getTieredComponentOfMaterial(Materials.Wood, OrePrefixes.plate, 32),
                CI.getTieredComponentOfMaterial(Materials.Steel, OrePrefixes.bolt, 16),
                CI.getTieredComponentOfMaterial(Materials.Redstone, OrePrefixes.dust, 32))
            .circuit(21)
            .itemOutputs(GregtechItemList.AlgaeFarm_Controller.get(1))
            .fluidInputs(MaterialsAlloy.POTIN.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        RecipesMachinesCustom.loadRecipes();
        RecipesMachinesMulti.loadRecipes();
        RecipesMachinesTiered.loadRecipes();

        // Computer Cube
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_DataOrb.get(4),
                ItemList.Cover_Screen.get(4),
                ItemList.Hull_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2))
            .itemOutputs(GregtechItemList.Gregtech_Computer_Cube.get(1))
            .fluidInputs(Materials.Tantalum.getMolten(16 * INGOTS))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Circuit programmer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Robot_Arm_LV.get(4),
                ItemList.Cover_Controller.get(1),
                ItemList.Hull_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2))
            .itemOutputs(new ItemStack(ModBlocks.blockCircuitProgrammer))
            .fluidInputs(Materials.Iron.getMolten(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Lead Lined Chest
        for (ItemStack plateRubber : OreDictionary.getOres("plateAnyRubber")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    GTUtility.copyAmount(32, plateRubber),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 9),
                    new ItemStack(Blocks.chest))
                .itemOutputs(new ItemStack(ModBlocks.blockDecayablesChest))
                .fluidInputs(Materials.Lead.getMolten(16 * INGOTS))
                .duration(1 * MINUTES + 30 * SECONDS)
                .eut(60)
                .addTo(assemblerRecipes);
        }

        // RTG
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(Mods.IndustrialCraft2.ID, "blockGenerator", 1, 6),
                MaterialsAlloy.NITINOL_60.getPlate(8),
                MaterialsAlloy.MARAGING350.getGear(4),
                ItemList.Field_Generator_EV.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4))
            .itemOutputs(GregtechItemList.RTG.get(1))
            .fluidInputs(MaterialsAlloy.NIOBIUM_CARBIDE.getFluidStack(16 * INGOTS))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Agricultural Sewer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                ItemList.FluidRegulator_MV.get(2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 2),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                MaterialsAlloy.POTIN.getScrew(6))
            .itemOutputs(new ItemStack(ModBlocks.blockPooCollector))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Biocomposite Collector
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                new ItemStack(ModBlocks.blockPooCollector),
                ItemList.FluidRegulator_IV.get(2),
                GTOreDictUnificator.get("pipeHugeStaballoy", 4),
                MaterialsAlloy.ZERON_100.getScrew(16))
            .itemOutputs(new ItemStack(ModBlocks.blockPooCollector, 1, 8))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(9 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Flask Configurator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                new ItemStack(ModBlocks.blockCircuitProgrammer),
                ItemList.VOLUMETRIC_FLASK.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 2),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4))
            .circuit(17)
            .itemOutputs(new ItemStack(ModBlocks.blockVolumetricFlaskSetter, 1))
            .fluidInputs(MaterialsAlloy.SILICON_CARBIDE.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Tesseract Generator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Generator.get(1),
            new Object[] { "PCP", "CEC", "PXP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'C', "circuitMaster",
                'E', new ItemStack(Blocks.ender_chest), 'X', GregtechItemList.Gregtech_Computer_Cube });

        // Tesseract Terminal
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Terminal.get(1),
            new Object[] { "PCP", "CEC", "PHP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'C', "circuitElite",
                'E', new ItemStack(Blocks.ender_chest), 'X', ItemList.Hull_EV });

        // Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'I', OrePrefixes.plate.get(Materials.Redstone), 'C',
                ItemList.Casing_Grate, 'R', ItemList.FluidRegulator_IV, 'I', "circuitElite", 'H',
                ItemList.Hatch_Input_IV });

        // Extreme Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Extreme.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'I', MaterialsAlloy.PIKYONIUM.getPlate(1), 'C',
                GregtechItemList.Hatch_Air_Intake, 'R', ItemList.FluidRegulator_ZPM, 'I', "circuitUltimate", 'H',
                ItemList.Hatch_Input_ZPM });

        // Atmospheric Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Atmospheric.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'I', MaterialsAlloy.OCTIRON.getPlate(1), 'C',
                GregtechItemList.Hatch_Air_Intake_Extreme, 'R', ItemList.FluidRegulator_UHV, 'I', "circuitInfinite",
                'H', ItemList.Hatch_Input_UHV });

        // todo
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_LargeSemifluidGenerator.get(1L),
            BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Pump_EV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt08.get(Materials.Electrum), 'G', MaterialsAlloy.INCONEL_792.getGear(1) });

        // Project Table
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Emitter_EV.get(2),
                ItemList.Robot_Arm_EV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 8),
                MaterialsAlloy.TANTALUM_CARBIDE.getScrew(8),
                MaterialsAlloy.INCONEL_625.getPlate(4))
            .itemOutputs(new ItemStack(ModBlocks.blockProjectTable))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(4 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reservoir Hatch
        if (RemoteIO.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hatch_Input_EV.get(1),
                    getModItem(RemoteIO.ID, "tile.machine", 1),
                    ItemList.Electric_Pump_EV.get(1))
                .itemOutputs(GregtechItemList.Hatch_Reservoir.get(1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // Containment Frame (Everglades Portal)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Field_Generator_MV.get(1),
                ItemList.Field_Generator_HV.get(1),
                ItemList.Emitter_HV.get(1),
                ItemList.Sensor_HV.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 4))
            .itemOutputs(new ItemStack(DimensionEverglades.blockPortalFrame, 2))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(8 * INGOTS))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Industrial Multi Tank Casing (unused but craftable)
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MultitankExterior.get(1),
            new Object[] { "RPR", "PFP", "PPP", 'R', MaterialsAlloy.LEAGRISIUM.getRod(1), 'P',
                MaterialsAlloy.LEAGRISIUM.getPlate(1), 'F', MaterialsAlloy.LEAGRISIUM.getFrameBox(1) });

        // Trinium Plated Casing (unused but craftable)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFrameBox(4),
                MaterialsAlloy.TRINIUM_TITANIUM.getPlateDouble(1),
                MaterialsAlloy.PIKYONIUM.getGear(2),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getPlateDouble(4),
                ItemList.Hull_LuV.get(1))
            .itemOutputs(GregtechItemList.Casing_BedrockMiner.get(1))
            .fluidInputs(MaterialsAlloy.MARAGING350.getFluidStack(16 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void extrusionHatches() {
        ItemStack[] mSuperBusesInput = new ItemStack[] { ItemList.Hatch_Input_Bus_IV.get(1),
            ItemList.Hatch_Input_Bus_LuV.get(1), ItemList.Hatch_Input_Bus_ZPM.get(1),
            ItemList.Hatch_Input_Bus_UV.get(1) };

        ItemStack[] mSolidifierHatches = new ItemStack[] { GregtechItemList.Hatch_Extrusion_I.get(1),
            GregtechItemList.Hatch_Extrusion_II.get(1), GregtechItemList.Hatch_Extrusion_III.get(1),
            GregtechItemList.Hatch_Extrusion_IV.get(1) };

        for (int i = 0; i < 4; i++) {
            int componentTier = i + 5;
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(17),
                    mSuperBusesInput[i],
                    CI.getSensor(componentTier, 1),
                    CI.getConveyor(componentTier, 1),
                    CI.getTieredComponent(OrePrefixes.circuit, componentTier + 1, 4),
                    new ItemStack(Blocks.chest))
                .itemOutputs(mSolidifierHatches[i])
                .fluidInputs(CI.getTieredFluid(componentTier, 2 * INGOTS))
                .duration(30 * SECONDS)
                .eut(GTValues.VP[componentTier])
                .addTo(assemblerRecipes);
        }
    }
}
