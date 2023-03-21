package common;

import java.util.HashMap;

import kekztech.Items;
import kekztech.KekzCore;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import util.Util;

import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.LuVTierEnhancer;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class Recipes {

    public static final HashMap<String, InfusionRecipe> infusionRecipes = new HashMap<>();
    static Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");
    static Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

    static Fluid radoxPoly = FluidRegistry.getFluid("molten.radoxpoly") != null
            ? FluidRegistry.getFluid("molten.radoxpoly")
            : FluidRegistry.getFluid("molten.polybenzimidazole");

    public static void postInit() {
        KekzCore.LOGGER.info("Registering recipes...");

        registerRecipes_TFFT();
        registerRecipes_SOFC();
        // registerRecipes_Nuclear();
        registerRecipes_Jars();
        registerRecipes_LSC();
        // registerRecipes_SpaceElevator();
        registerRecipes_Cosmetics();

        KekzCore.LOGGER.info("Finished registering recipes");
    }

    private static void registerRecipes_TFFT() {

        // TFFT Controller
        GT_ModHandler.addCraftingRecipe(
                TileEntities.tfft.getStackForm(1),
                new Object[] { "ESE", "FTF", "CVC", 'E',
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.EnderEye, 1), 'S',
                        ItemList.Cover_Screen.get(1), 'F', ItemList.Field_Generator_LV.get(1), 'T',
                        new ItemStack(Blocks.tfftStorageField, 1), 'C', "circuitData", 'V',
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 1), });

        // TFFT Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.DarkSteel, 3),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderPearl, 3), },
                Materials.Polytetrafluoroethylene.getMolten(144),
                new ItemStack(Blocks.tfftStorageField, 1),
                100,
                BW_Util.getMachineVoltageFromTier(3));

        // TFFT Multi Hatch
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_HV.get(1), ItemList.Cover_FluidStorageMonitor.get(1),
                        ItemList.Field_Generator_LV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polytetrafluoroethylene, 25) },
                Materials.Plastic.getMolten(432),
                TileEntities.tfftHatch.getStackForm(1),
                400,
                BW_Util.getMachineVoltageFromTier(3));

        // TFFTStorageField1
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Steel, 3),
                        ItemList.FluidRegulator_LV.get(1) },
                Materials.Glass.getMolten(144),
                new ItemStack(Blocks.tfftStorageField, 1, 1),
                100,
                BW_Util.getMachineVoltageFromTier(3));

        // TFFTStorageField2
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6), ItemList.Casing_Tank_1.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Plastic, 3),
                        ItemList.FluidRegulator_MV.get(1) },
                Materials.Plastic.getMolten(288),
                new ItemStack(Blocks.tfftStorageField, 1, 2),
                200,
                BW_Util.getMachineVoltageFromTier(3));

        // TFFTStorageField3
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6), ItemList.Casing_Tank_3.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.StainlessSteel, 3),
                        ItemList.Field_Generator_LV.get(1), ItemList.FluidRegulator_HV.get(1) },
                Materials.Plastic.getMolten(432),
                new ItemStack(Blocks.tfftStorageField, 1, 3),
                400,
                BW_Util.getMachineVoltageFromTier(3));

        // TFFTStorageField4
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6), ItemList.Casing_Tank_5.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Polytetrafluoroethylene, 3),
                        ItemList.Field_Generator_MV.get(2), ItemList.FluidRegulator_EV.get(1) },
                Materials.Epoxid.getMolten(864),
                new ItemStack(Blocks.tfftStorageField, 1, 4),
                400,
                BW_Util.getMachineVoltageFromTier(4));

        // TFFTStorageField5
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(6), ItemList.Casing_Tank_7.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Enderium, 3),
                        ItemList.Field_Generator_HV.get(4), ItemList.FluidRegulator_IV.get(1) },
                Materials.Epoxid.getMolten(1152),
                new ItemStack(Blocks.tfftStorageField, 1, 5),
                400,
                BW_Util.getMachineVoltageFromTier(5));

        // TFFTStorageField6
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 5),
                40000,
                new ItemStack[] { ItemList.Casing_Tank_7.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Naquadah, 3),
                        GT_ModHandler.getModItem("dreamcraft", "item.ChromeBars", 6),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Quantium, 6),
                        ItemList.Field_Generator_EV.get(8), ItemList.FluidRegulator_LuV.get(1),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngineeringProcessorFluidEmeraldCore", 4) },
                new FluidStack[] { Materials.Enderium.getMolten(1440), Materials.Polybenzimidazole.getMolten(1584) },
                new ItemStack(Blocks.tfftStorageField, 1, 6),
                600,
                BW_Util.getMachineVoltageFromTier(6));

        // TFFTStorageField7
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 6),
                80000,
                new ItemStack[] { ItemList.Casing_Tank_10.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.NetherStar, 3),
                        GT_ModHandler.getModItem("dreamcraft", "item.OsmiumBars", 6),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.MysteriousCrystal, 6),
                        ItemList.Field_Generator_IV.get(16), ItemList.Field_Generator_LuV.get(4),
                        ItemList.FluidRegulator_UV.get(1),
                        GT_ModHandler.getModItem("dreamcraft", "item.EngineeringProcessorFluidEmeraldCore", 16) },
                new FluidStack[] { Materials.CrystallineAlloy.getMolten(2880),
                        Materials.Polybenzimidazole.getMolten(2016) },
                new ItemStack(Blocks.tfftStorageField, 1, 7),
                600,
                BW_Util.getMachineVoltageFromTier(8));

        // TFFTStorageField8
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 7),
                120000,
                new ItemStack[] { ItemList.Quantum_Tank_IV.get(1),
                        GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.DraconiumAwakened, 3),
                        GT_ModHandler.getModItem("dreamcraft", "item.NeutroniumBars", 6),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.InfinityCatalyst, 6),
                        ItemList.Field_Generator_ZPM.get(16), ItemList.Field_Generator_UV.get(4),
                        GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 2),
                        GT_ModHandler
                                .getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4) },
                new FluidStack[] { Materials.CrystallinePinkSlime.getMolten(4320), new FluidStack(radoxPoly, 2880) },
                new ItemStack(Blocks.tfftStorageField, 1, 8),
                600,
                BW_Util.getMachineVoltageFromTier(10));

        // TFFTStorageField9
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 8),
                160000,
                new ItemStack[] { ItemList.Quantum_Tank_IV.get(4),
                        GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 6),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Infinity, 3),
                        ItemList.EnergisedTesseract.get(1),
                        GT_ModHandler.getModItem("miscutils", "itemRotorHypogen", 6),
                        ItemList.Field_Generator_UHV.get(16), ItemList.Field_Generator_UEV.get(4),
                        GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 3),
                        GT_ModHandler
                                .getModItem("universalsingularities", "universal.tinkersConstruct.singularity", 1, 4) },
                new FluidStack[] { Materials.MelodicAlloy.getMolten(5760), new FluidStack(radoxPoly, 3456) },
                new ItemStack(Blocks.tfftStorageField, 1, 9),
                600,
                BW_Util.getMachineVoltageFromTier(12));

        // TFFTStorageField10
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 9),
                200000,
                new ItemStack[] { ItemList.Quantum_Tank_IV.get(16),
                        GT_ModHandler.getModItem("Avaritia", "Neutronium_Compressor", 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 6),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 6),
                        GT_OreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.SpaceTime, 3),
                        ItemList.EnergisedTesseract.get(6),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.SpaceTime, 6),
                        ItemList.Field_Generator_UEV.get(16), ItemList.Field_Generator_UIV.get(4),
                        GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 4),
                        GT_ModHandler.getModItem("GoodGenerator", "huiCircuit", 4, 4),
                        GT_ModHandler.getModItem("EnderIO", "itemBasicCapacitor", 64, 6),
                        GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1) },
                new FluidStack[] { Materials.StellarAlloy.getMolten(7200), new FluidStack(radoxPoly, 4608) },
                new ItemStack(Blocks.tfftStorageField, 1, 10),
                600,
                BW_Util.getMachineVoltageFromTier(13));

        // Recycling
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 2),
                ItemList.Casing_Tank_1.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 3),
                ItemList.Casing_Tank_3.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 4),
                ItemList.Casing_Tank_5.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 5),
                ItemList.Casing_Tank_7.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 6),
                ItemList.Casing_Tank_7.get(4),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 7),
                ItemList.Casing_Tank_10.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 8),
                ItemList.Quantum_Tank_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 12),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 9),
                ItemList.Quantum_Tank_IV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 12),
                100,
                BW_Util.getMachineVoltageFromTier(3));
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.tfftStorageField, 1, 10),
                ItemList.Quantum_Tank_IV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 12),
                100,
                BW_Util.getMachineVoltageFromTier(3));
    }

    private static void registerRecipes_SOFC() {

        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // Controller
        final Object[] mk1_recipe = { "CCC", "PHP", "FBL", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                ItemList.Electric_Pump_HV.get(1L), 'H', ItemList.Hull_HV.get(1L), 'F',
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 1), 'B',
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 1), 'L',
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1) };
        GT_ModHandler.addCraftingRecipe(TileEntities.sofc1.getStackForm(1), mk1_recipe);
        final Object[] mk2_recipe = { "CCC", "PHP", "FBL", 'C', OrePrefixes.circuit.get(Materials.Master), 'P',
                ItemList.Electric_Pump_IV.get(1L), 'H', ItemList.Hull_IV.get(1L), 'F',
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1), 'B',
                Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1), 'L',
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1) };
        GT_ModHandler.addCraftingRecipe(TileEntities.sofc2.getStackForm(1), mk2_recipe);

        // Blocks
        final ItemStack[] yszUnit = { GT_Utility.getIntegratedCircuit(6),
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 4),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Yttrium, 1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_HV.get(1L), };
        GT_Values.RA.addAssemblerRecipe(
                yszUnit,
                Materials.Hydrogen.getGas(4000),
                new ItemStack(Blocks.yszUnit, 1),
                1200,
                480);
        final ItemStack[] gdcUnit = { GT_Utility.getIntegratedCircuit(6),
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 8),
                GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
                GT_OreDictUnificator
                        .get(OrePrefixes.rotor, Materials.Desh, new ItemStack(ErrorItem.getInstance(), 1), 1),
                ItemList.Electric_Motor_IV.get(1L), };
        GT_Values.RA.addAssemblerRecipe(
                gdcUnit,
                Materials.Hydrogen.getGas(16000),
                new ItemStack(Blocks.gdcUnit, 1),
                2400,
                1920);

        // Items
        GT_Values.RA.addAlloySmelterRecipe(
                craftingItem.getStackOfAmountFromDamage(
                        Items.YSZCeramicDust.getMetaID(),
                        Loader.isModLoaded("bartworks") ? 3 : 10),
                ItemList.Shape_Mold_Plate.get(0),
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 1),
                400,
                480);
        GT_Values.RA.addFormingPressRecipe(
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
                ItemList.Shape_Mold_Plate.get(0),
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 1),
                800,
                480);

        if (!Loader.isModLoaded("bartworks")) {
            GT_Values.RA.addChemicalRecipe(
                    Materials.Yttrium.getDust(1),
                    GT_Utility.getIntegratedCircuit(6),
                    Materials.Oxygen.getGas(3000),
                    null,
                    craftingItem.getStackOfAmountFromDamage(Items.YttriaDust.getMetaID(), 1),
                    null,
                    400,
                    30);
            GT_Values.RA.addChemicalRecipe(
                    Util.getStackofAmountFromOreDict("dustZirconium", 1),
                    GT_Utility.getIntegratedCircuit(6),
                    Materials.Oxygen.getGas(2000),
                    null,
                    craftingItem.getStackOfAmountFromDamage(Items.ZirconiaDust.getMetaID(), 1),
                    null,
                    400,
                    30);
        }

        GT_Values.RA.addChemicalRecipe(
                Materials.Cerium.getDust(2),
                GT_Utility.getIntegratedCircuit(6),
                Materials.Oxygen.getGas(3000),
                null,
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 2),
                null,
                400,
                30);
        GT_Values.RA.addMixerRecipe(
                Items.YttriaDust.getOreDictedItemStack(1),
                Items.ZirconiaDust.getOreDictedItemStack(5),
                GT_Utility.getIntegratedCircuit(6),
                null,
                null,
                null,
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 6),
                400,
                96);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator
                        .get(OrePrefixes.dust, Materials.Gadolinium, new ItemStack(ErrorItem.getInstance(), 1), 1),
                craftingItem.getStackOfAmountFromDamage(Items.CeriaDust.getMetaID(), 9),
                GT_Utility.getIntegratedCircuit(6),
                null,
                null,
                null,
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicDust.getMetaID(), 10),
                400,
                1920);
    }

    /*
     * private static void registerRecipes_Nuclear() { final MetaItem_CraftingComponent craftingItem =
     * MetaItem_CraftingComponent.getInstance(); // Controller // Blocks final ItemStack[] controlrod = {
     * GT_Utility.getIntegratedCircuit(6), GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Lead, 1),
     * GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 4), GT_OreDictUnificator.get(OrePrefixes.dust,
     * Materials.Carbon, 64) }; GT_Values.RA.addAssemblerRecipe( controlrod, null, new
     * ItemStack(Blocks.reactorControlRod, 1), 800, 480); final ItemStack[] reactorchamber = {
     * GT_Utility.getIntegratedCircuit(6), GT_OreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Lead, 1),
     * GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Lead, 9), GT_OreDictUnificator.get(OrePrefixes.ring,
     * Materials.TungstenSteel, 18), GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 2), };
     * GT_Values.RA.addAssemblerRecipe( reactorchamber, FluidRegistry.getFluidStack("wet.concrete", 144), new
     * ItemStack(Blocks.reactorChamberOFF, 1), 1600, 480); // Items
     * GT_Values.RA.addMixerRecipe(Materials.Boron.getDust(1), Materials.Arsenic.getDust(1),
     * GT_Utility.getIntegratedCircuit(6), null, null, null,
     * craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideDust.getMetaID(), 2), 100, 1920);
     * GT_Values.RA.addChemicalRecipe( Materials.Ammonia.getCells(2), Materials.CarbonDioxide.getCells(1), null, null,
     * craftingItem.getStackOfAmountFromDamage(Items.AmineCarbamiteDust.getMetaID(), 1),
     * Util.getStackofAmountFromOreDict("cellEmpty", 3), 400, 30); GT_Values.RA.addChemicalRecipe(
     * craftingItem.getStackOfAmountFromDamage(Items.AmineCarbamiteDust.getMetaID(), 1), Materials.Diamond.getDust(16),
     * Materials.CarbonDioxide.getGas(1000), null,
     * craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondDust.getMetaID(), 1), null, 1200, 480);
     * GT_Values.RA.addAutoclaveRecipe(
     * craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondDust.getMetaID(), 4),
     * Materials.CarbonDioxide.getGas(16000),
     * craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondCrystal.getMetaID(), 1), 10000, 2400, 7680);
     * GT_Values.RA.addAutoclaveRecipe( craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideDust.getMetaID(), 4),
     * Materials.Nitrogen.getGas(4000), craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideCrystal.getMetaID(),
     * 1), 10000, 2400, 1920); GT_Values.RA.addLatheRecipe( GT_OreDictUnificator.get(OrePrefixes.stick,
     * Materials.AnnealedCopper, 1), craftingItem.getStackFromDamage(Items.CopperHeatPipe.getMetaID()), null, 120, 120);
     * GT_Values.RA.addLatheRecipe( GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Silver, 1),
     * craftingItem.getStackFromDamage(Items.SilverHeatPipe.getMetaID()), null, 120, 480); GT_Values.RA.addLatheRecipe(
     * craftingItem.getStackOfAmountFromDamage(Items.BoronArsenideCrystal.getMetaID(), 4),
     * craftingItem.getStackFromDamage(Items.BoronArsenideHeatPipe.getMetaID()), null, 1200, 1920);
     * GT_Values.RA.addLatheRecipe(
     * craftingItem.getStackOfAmountFromDamage(Items.IsotopicallyPureDiamondCrystal.getMetaID(), 4),
     * craftingItem.getStackFromDamage(Items.DiamondHeatPipe.getMetaID()), null, 1200, 7680); }
     */

    private static void registerRecipes_Jars() {

        // Thaumium Reinforced Jar
        final ItemStack[] recipe_jarthaumiumreinforced = {
                GameRegistry.makeItemStack("Thaumcraft:ItemResource", 15, 1, null),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane), };
        final AspectList aspects_jarthaumiumreinforced = new AspectList().add(Aspect.ARMOR, 64).add(Aspect.ORDER, 32)
                .add(Aspect.WATER, 32).add(Aspect.GREED, 16).add(Aspect.VOID, 16).add(Aspect.AIR, 8);
        infusionRecipes.put(
                "THAUMIUMREINFORCEDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "THAUMIUMREINFORCEDJAR",
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                        5,
                        aspects_jarthaumiumreinforced,
                        ItemApi.getBlock("blockJar", 0),
                        recipe_jarthaumiumreinforced));
        // Thaumium Reinforced Void Jar
        final ItemStack[] recipe_voidjarupgrade = {
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderEye, 1), ItemApi.getItem("itemNugget", 5) };
        final AspectList aspects_voidjarupgrade = new AspectList().add(Aspect.VOID, 14).add(Aspect.MAGIC, 14)
                .add(Aspect.ENTROPY, 14).add(Aspect.WATER, 14);
        infusionRecipes.put(
                "THAUMIUMREINFORCEDVOIDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "THAUMIUMREINFORCEDJAR",
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 3),
                        2,
                        aspects_voidjarupgrade,
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                        recipe_voidjarupgrade));

        final ItemStack[] recipe_jarichor = { GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane), };
        final AspectList aspects_jarichor = new AspectList().add(Aspect.ARMOR, 256).add(Aspect.ELDRITCH, 128)
                .add(Aspect.ORDER, 128).add(Aspect.WATER, 128).add(Aspect.GREED, 64).add(Aspect.VOID, 64)
                .add(Aspect.AIR, 32);
        infusionRecipes.put(
                "ICHORJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "ICHORJAR",
                        new ItemStack(Blocks.jarIchor, 1, 0),
                        15,
                        aspects_jarichor,
                        ItemApi.getBlock("blockJar", 0),
                        recipe_jarichor));
        // Ichor Void Jar
        infusionRecipes.put(
                "ICHORVOIDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "ICHORJAR",
                        new ItemStack(Blocks.jarIchor, 1, 3),
                        5,
                        aspects_voidjarupgrade,
                        new ItemStack(Blocks.jarIchor, 1, 0),
                        recipe_voidjarupgrade));
    }

    private static void registerRecipes_LSC() {

        // Controller
        final Object[] lsc_recipe = { "LPL", "CBC", "LPL", 'L', ItemList.IC2_LapotronCrystal.getWildcard(1L), 'P',
                ItemList.Circuit_Chip_PIC.get(1L), 'C', OrePrefixes.circuit.get(Materials.Master), 'B',
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), };
        GT_ModHandler.addCraftingRecipe(TileEntities.lsc.getStackForm(1), lsc_recipe);

        // Blocks
        final Object[] lcBase_recipe = { "WBW", "RLR", "WBW", 'W', OrePrefixes.plate.get(Materials.Tantalum), 'B',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'R',
                OrePrefixes.stickLong.get(Materials.TungstenSteel), 'L', OrePrefixes.block.get(Materials.Lapis) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), lcBase_recipe);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tantalum, 4),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 1) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0),
                100,
                480);

        // Empty Capacitor
        final Object[] lcEmpty_recipe = { "SLS", "L L", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6), lcEmpty_recipe);

        // EV Capacitor
        final Object[] lcEV_recipe = { "SLS", "LCL", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis), 'C',
                GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7), lcEV_recipe);

        // EV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                        GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7),
                200,
                BW_Util.getMachineVoltageFromTier(3));

        // IV Capacitor
        final Object[] lcIV_recipe = { "SLS", "LOL", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis), 'O', ItemList.Energy_LapotronicOrb.get(1L) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), lcIV_recipe);

        // IV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                        ItemList.Energy_LapotronicOrb.get(1L), GT_Utility.getIntegratedCircuit(1) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
                400,
                BW_Util.getMachineVoltageFromTier(4));

        // LuV Capacitor
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                        ItemList.Circuit_Board_Elite.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 64),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(36),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(36), ItemList.Circuit_Chip_HPIC.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(8), ItemList.Circuit_Parts_CapacitorASMD.get(8),
                        ItemList.Circuit_Parts_ResistorASMD.get(8), ItemList.Circuit_Parts_TransistorASMD.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64) },
                new FluidStack[] { new FluidStack(solderIndalloy, 720) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
                1000,
                80000);

        // LuV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Energy_LapotronicOrb2.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
                800,
                BW_Util.getMachineVoltageFromTier(5));
        LuVTierEnhancer.addToBlackListForOsmiridiumReplacement(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2));

        // ZPM Capacitor
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1 },
                        ItemList.Energy_LapotronicOrb2.get(8L), ItemList.Field_Generator_LuV.get(2),
                        ItemList.Circuit_Wafer_SoC2.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32) },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
                2000,
                100000);

        // ZPM Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Energy_Module.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
                1600,
                BW_Util.getMachineVoltageFromTier(6));

        // UV Capacitor
        GT_Values.RA.addAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
                288000,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        ItemList.Energy_Module.get(8L), ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Wafer_HPIC.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32) },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
                2000,
                200000);

        // UV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Energy_Cluster.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
                3200,
                BW_Util.getMachineVoltageFromTier(7));

        // Ultimate Capacitor (UHV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
                12000,
                16,
                300000,
                3,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 }, ItemList.ZPM2.get(8L),
                        ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                        ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                        ItemList.Circuit_Parts_DiodeASMD.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 4608), Materials.Naquadria.getMolten(9216),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
                4000,
                1600000);

        // UHV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ZPM3.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
                6400,
                BW_Util.getMachineVoltageFromTier(8));

        // Extremely Ultimate Capacitor (UEV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
                1200000,
                128,
                8000000,
                16,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 }, ItemList.ZPM3.get(8L),
                        ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                        ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18432),
                        Materials.Naquadria.getMolten(18432), Materials.SuperCoolant.getFluid(64000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                250 * 20,
                (int) TierEU.RECIPE_UEV);

        // UEV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ZPM4.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                640 * 20,
                (int) TierEU.RECIPE_UHV);

        // Insanely Ultimate Capacitor (UIV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                24_000_000,
                1_280,
                32_000_000,
                32,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24),
                        GT_ModHandler.getModItem("miscutils", "itemPlateDoubleHypogen", 32),
                        GT_ModHandler.getModItem("miscutils", "itemPlateDoubleHypogen", 32),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, ItemList.ZPM4.get(8L),
                        ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                        ItemList.Circuit_Wafer_QPIC.get(64),
                        GT_ModHandler.getModItem("dreamcraft", "item.RawPicoWafer", 64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 18_432),
                        new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 18432),
                        Materials.Quantium.getMolten(18_432), Materials.SuperCoolant.getFluid(128_000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
                300 * 20,
                (int) TierEU.RECIPE_UIV);

        // UIV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ZPM5.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
                640 * 20,
                (int) TierEU.RECIPE_UEV);

        // Mega Ultimate Capacitor (UMV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
                480_000_000,
                12_288,
                128_000_000,
                64,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24),
                        GT_ModHandler.getModItem("miscutils", "itemPlateDoubleDragonblood", 32),
                        GT_ModHandler.getModItem("miscutils", "itemPlateDoubleDragonblood", 32),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 }, ItemList.ZPM5.get(8L),
                        ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                        ItemList.Circuit_Wafer_QPIC.get(64),
                        GT_ModHandler.getModItem("dreamcraft", "item.PicoWafer", 64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 36_864),
                        new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 36_864),
                        new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 36_864),
                        Materials.SuperCoolant.getFluid(256_000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
                350 * 20,
                (int) TierEU.RECIPE_UMV);

        // UMV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.ZPM6.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
                640 * 20,
                (int) TierEU.RECIPE_UIV);

        // Capacitor recycling
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7),
                GT_ModHandler.getIC2Item("lapotronCrystal", 1L, 26),
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
                ItemList.Energy_LapotronicOrb.get(1L),
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
                ItemList.Energy_LapotronicOrb2.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
                ItemList.Energy_Module.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
                ItemList.Energy_Cluster.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
                ItemList.ZPM3.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                ItemList.ZPM4.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
                ItemList.ZPM5.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24),
                1200,
                (int) TierEU.RECIPE_LV);
        GT_Values.RA.addUnboxingRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
                ItemList.ZPM6.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24),
                1200,
                (int) TierEU.RECIPE_LV);
    }

    /*
     * private static void registerRecipes_SpaceElevator() { // Controller final Object[] se_recipe = { "BCB", "CPC",
     * "BCB", 'B', new ItemStack(Blocks.spaceElevatorStructure, 1, 0), 'C',
     * OrePrefixes.cableGt16.get(Materials.Aluminium), 'P', OrePrefixes.circuit.get(Materials.Master) };
     * GT_ModHandler.addCraftingRecipe(TileEntities.se.getStackForm(1), se_recipe); // Blocks final Object[]
     * seBase_recipe = { "DRD", "RCR", "DRD", 'D', OrePrefixes.plate.get(Materials.DarkSteel), 'R',
     * OrePrefixes.stick.get(Materials.Steel), 'C', OrePrefixes.block.get(Materials.Concrete), };
     * GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.spaceElevatorStructure, 1, 0), seBase_recipe); final
     * Object[] seCoilHolder_recipe = { "DRD", "RCR", "DRD", 'D', OrePrefixes.plate.get(Materials.DarkSteel), 'R',
     * OrePrefixes.ring.get(Materials.Steel), 'C', OrePrefixes.cableGt01.get(Materials.Aluminium) };
     * GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.spaceElevatorStructure, 1, 1), seCoilHolder_recipe); }
     */

    private static void registerRecipes_Cosmetics() {
        // Hex Tiles
        final ItemStack[] hexTiles = { GT_Utility.getIntegratedCircuit(6),
                GT_OreDictUnificator.get(OrePrefixes.stone, Materials.Concrete, 1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.DarkSteel, 2) };
        GT_Values.RA.addAssemblerRecipe(
                hexTiles,
                FluidRegistry.getFluidStack("molten.plastic", 36),
                new ItemStack(Blocks.largeHexPlate, 2),
                600,
                120);
    }
}
