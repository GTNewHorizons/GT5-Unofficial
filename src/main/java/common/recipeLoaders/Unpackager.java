package common.recipeLoaders;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import common.Blocks;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class Unpackager implements Runnable {

    @Override
    public void run() {

        // TFFT Recycling
        {
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

        // Capacitor recycling
        {
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
    }
}
