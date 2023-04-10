package common.recipeLoaders;

import kekztech.Items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.LuVTierEnhancer;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import common.Blocks;
import common.TileEntities;
import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class Assembler implements Runnable {

    @Override
    public void run() {
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

        // LSC Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tantalum, 4),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lapis, 1) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0),
                100,
                480);

        // EV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                        GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7),
                200,
                BW_Util.getMachineVoltageFromTier(3));

        // IV Capacitor alt recipe
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                        ItemList.Energy_LapotronicOrb.get(1L), GT_Utility.getIntegratedCircuit(1) },
                null,
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
                400,
                BW_Util.getMachineVoltageFromTier(4));

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

        final MetaItem_CraftingComponent craftingItem = MetaItem_CraftingComponent.getInstance();

        // YSZ Unit
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

        // GDC Unit
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
