package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BuildCraftSilicon;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingCrafting implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrafting() {
        OrePrefixes.crafting.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        switch (aOreDictName) {
            case "craftingQuartz" -> GT_Values.RA.addAssemblerRecipe(
                new ItemStack(Blocks.redstone_torch, 3, 32767),
                GT_Utility.copyAmount(1L, aStack),
                Materials.Concrete.getMolten(144L),
                new ItemStack(net.minecraft.init.Items.comparator, 1, 0),
                80,
                20);
            case "craftingWireCopper", "craftingWireTin" -> GT_Values.RA.addAssemblerRecipe(
                ItemList.Circuit_Basic.get(1L),
                GT_Utility.copyAmount(1L, aStack),
                GT_ModHandler.getIC2Item("frequencyTransmitter", 1L),
                80,
                20);
            case "craftingLensBlue" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 13),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.IC2_LapotronCrystal.getWildcard(1L),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L),
                    900,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Chip_CrystalCPU.get(1L),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Chip_CrystalSoC.get(1),
                    600,
                    40000,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_PIC.get(1),
                    1200,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_PIC.get(4),
                    800,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_QPIC.get(1),
                    2400,
                    500000,
                    true);
            }
            case "craftingLensYellow" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 14),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_LPIC.get(1),
                    800,
                    120,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_LPIC.get(4),
                    600,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC.get(1),
                    900,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC.get(4),
                    600,
                    7680,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC.get(8),
                    300,
                    30720,
                    true);
            }
            case "craftingLensOrange" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Simple_SoC.get(1),
                    300,
                    64,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Simple_SoC.get(4),
                    300,
                    256,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Simple_SoC.get(16),
                    300,
                    1024,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Simple_SoC.get(64),
                    300,
                    4096,
                    false);
            }
            case "craftingLensCyan" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 15),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Ram.get(1),
                    1200,
                    120,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Ram.get(4),
                    900,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Ram.get(8),
                    600,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Ram.get(16),
                    300,
                    7680,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_Ram.get(32),
                    150,
                    30720,
                    true);
            }
            case "craftingLensRed" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Redstone, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(BuildCraftSilicon.ID, "redstoneChipset", 1L, 0),
                    50,
                    120);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.RedAlloy, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.EtchedLowVoltageWiring", 1L, 0),
                    200,
                    16);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_ILC.get(1),
                    1200,
                    120,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_ILC.get(4),
                    900,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_ILC.get(8),
                    600,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_NPIC.get(1),
                    1800,
                    30720,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_NPIC.get(4),
                    1500,
                    122880,
                    true);
            }
            case "craftingLensGreen" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1L),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Chip_CrystalCPU.get(1),
                    600,
                    10000,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Chip_CrystalSoC.get(1L),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Chip_CrystalSoC2.get(1),
                    1200,
                    80000,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_ULPIC.get(2),
                    600,
                    30,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_ULPIC.get(8),
                    600,
                    120,
                    false);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC2.get(1),
                    1800,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC2.get(2),
                    1500,
                    7680,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_SoC2.get(4),
                    1200,
                    30720,
                    true);
            }
            case "craftingLensWhite" -> {
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 19),
                    2000,
                    1920);
                GT_Values.RA.addLaserEngraverRecipe(
                    new ItemStack(Blocks.sandstone, 1, 2),
                    GT_Utility.copyAmount(0L, aStack),
                    new ItemStack(Blocks.sandstone, 1, 1),
                    50,
                    16);
                GT_Values.RA.addLaserEngraverRecipe(
                    new ItemStack(Blocks.stone, 1, 0),
                    GT_Utility.copyAmount(0L, aStack),
                    new ItemStack(Blocks.stonebrick, 1, 3),
                    50,
                    16);
                GT_Values.RA.addLaserEngraverRecipe(
                    new ItemStack(Blocks.quartz_block, 1, 0),
                    GT_Utility.copyAmount(0L, aStack),
                    new ItemStack(Blocks.quartz_block, 1, 1),
                    50,
                    16);
                GT_Values.RA.addLaserEngraverRecipe(
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartz", 1L),
                    GT_Utility.copyAmount(0L, aStack),
                    GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockQuartzChiseled", 1L),
                    50,
                    16);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_CPU.get(1),
                    1200,
                    120,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer2.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_CPU.get(4),
                    900,
                    480,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer3.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_CPU.get(8),
                    600,
                    1920,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer4.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_CPU.get(16),
                    300,
                    7680,
                    true);
                GT_Values.RA.addLaserEngraverRecipe(
                    ItemList.Circuit_Silicon_Wafer5.get(1),
                    GT_Utility.copyAmount(0L, aStack),
                    ItemList.Circuit_Wafer_CPU.get(32),
                    150,
                    30720,
                    true);
            }
        }
    }
}
