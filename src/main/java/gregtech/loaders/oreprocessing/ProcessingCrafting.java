package gregtech.loaders.oreprocessing;

import com.spartakcore.gthandler.CustomItemList;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ProcessingCrafting implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingCrafting() {
        OrePrefixes.crafting.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        switch (aOreDictName) {
            case "craftingQuartz":
                GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.redstone_torch, 3, 32767), GT_Utility.copyAmount(1L, new Object[]{aStack}), Materials.Concrete.getMolten(144L), new ItemStack(net.minecraft.init.Items.comparator, 1, 0), 800, 1);
                break;
            case "craftingWireCopper":
                GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.getFirstOre(OrePrefixes.circuit.get(Materials.Basic), 1), GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_ModHandler.getIC2Item("frequencyTransmitter", 1L), 800, 1);
                break;
            case "craftingWireTin":
                GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.getFirstOre(OrePrefixes.circuit.get(Materials.Basic), 1), GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_ModHandler.getIC2Item("frequencyTransmitter", 1L), 800, 1);
                break;
            case "craftingLensBlue":
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 13), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 13), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.IC2_LapotronCrystal.getWildcard(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L, new Object[0]), 256, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lapotron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Parts_Crystal_Chip_Master.get(1L, new Object[0]), 300, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(1, new Object[0]), 500, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(4, new Object[0]), 200, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Chip_CrystalCPU.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Chip_CrystalSoC.get(1, new Object[0]), 100, 40000,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(2, new Object[0]), 350, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(8, new Object[0]), 100, 7680,true);             
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(24, new Object[0]), 50, 30720,true);             
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer8.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_QPIC.get(1, new Object[0]), 2400, 500000,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_PIC.get(2, new Object[0]), 500, 1024,true);
                break;
            case "craftingLensYellow":
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 14), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 14), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(4, new Object[0]), 200, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(2, new Object[0]), 550, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(8, new Object[0]), 50, 7680,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(24, new Object[0]), 10, 30720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_LPIC.get(1, new Object[0]), 800, 120,false);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_LPIC.get(4, new Object[0]), 600, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(2, new Object[0]), 800, 1024,true);
                break;
            case "craftingLensOrange":
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC3.get(1, new Object[0]), 2700, 7680,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC3.get(4, new Object[0]), 3200, 30720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC3.get(16, new Object[0]), 5600, 65536,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer7.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_NPIC.get(1, new Object[0]), 1800, 30720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer8.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_NPIC.get(4, new Object[0]), 1800, 122880,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC.get(1, new Object[0]), 1600, 480,true);
                break;
            case "craftingLensLime":
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Simple_SoC.get(1, new Object[0]), 300, 64,false);
            	break;
            case "craftingLensCyan":
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 15), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 15), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(1, new Object[0]), 900, 120,false);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(4, new Object[0]), 500, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(8, new Object[0]), 200, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(6, new Object[0]), 350, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(16, new Object[0]), 50, 7680,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(48, new Object[0]), 10, 30720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Parts_MCrystal_Chip_Elite.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Chip_MCrystalCPU.get(1, new Object[0]), 120, 169720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_Ram.get(6, new Object[0]), 700, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer10.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_FPIC.get(1, new Object[0]), 200, 500000,true);
				break;
            case "craftingLensRed":
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Redstone, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), 50, 120);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(1, new Object[0]), 900, 120,false);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(4, new Object[0]), 500, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(8, new Object[0]), 200, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(6, new Object[0]), 350, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(16, new Object[0]), 50, 7680,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(48, new Object[0]), 10, 30720,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ILC.get(6, new Object[0]), 700, 1024,true);
                break;
            case "craftingLensGreen":
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Chip_CrystalCPU.get(1, new Object[0]), 100, 10000,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC4.get(1, new Object[0]), 2400, 65536,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC2.get(1, new Object[0]), 1800, 1920,true);
                //GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC4.get(1, new Object[0]), 3600, 30640,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer6.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC4.get(4, new Object[0]), 1200, 196608,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Chip_CrystalSoC.get(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Chip_CrystalSoC2.get(1, new Object[0]), 1200, 80000,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ULPIC.get(2, new Object[0]), 600, 30,false);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_ULPIC.get(8, new Object[0]), 600, 120,false);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_SoC2.get(1, new Object[0]), 3000, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.IC2_LapotronCrystal.getWildcard(1L, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), CustomItemList.QuantumCrystal.get(1L), 256, 480,true);
                break;
            case "craftingLensWhite":
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 19), 2000, 1920);
                GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.WroughtIron, 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 19), 2000, 1920);

                GT_Values.RA.addLaserEngraverRecipe(new ItemStack(Blocks.sandstone, 1, 2), GT_Utility.copyAmount(0L, new Object[]{aStack}), new ItemStack(Blocks.sandstone, 1, 1), 50, 16);
                GT_Values.RA.addLaserEngraverRecipe(new ItemStack(Blocks.stone, 1, 0), GT_Utility.copyAmount(0L, new Object[]{aStack}), new ItemStack(Blocks.stonebrick, 1, 3), 50, 16);
                GT_Values.RA.addLaserEngraverRecipe(new ItemStack(Blocks.quartz_block, 1, 0), GT_Utility.copyAmount(0L, new Object[]{aStack}), new ItemStack(Blocks.quartz_block, 1, 1), 50, 16);
                GT_Values.RA.addLaserEngraverRecipe(GT_ModHandler.getModItem("appliedenergistics2", "tile.BlockQuartz", 1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("appliedenergistics2", "tile.BlockQuartzChiseled", 1L), 50, 16);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(1, new Object[0]), 900, 120,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(4, new Object[0]), 500, 480,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(8, new Object[0]), 200, 1920,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer4.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(6, new Object[0]), 350, 1024,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer5.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(16, new Object[0]), 50, 7680,true);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer9.get(1, new Object[0]), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemList.Circuit_Wafer_CPU.get(6, new Object[0]), 700, 1024,true);
                break;
            case "craftingLensGray":
            	if(Loader.isModLoaded("GalacticraftCore")){
            	GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Wafer_ILC.get(1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("GalacticraftCore", "item.basicItem", 1L, 13), 200, 256);
                GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Wafer_NAND.get(1L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GT_ModHandler.getModItem("GalacticraftCore", "item.basicItem", 1L, 14), 200, 480);
            	}
                break;
        }
    }
}
