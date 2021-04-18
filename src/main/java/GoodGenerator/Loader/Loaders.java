package GoodGenerator.Loader;

import GoodGenerator.Blocks.RegularBlock.Casing;
import GoodGenerator.Blocks.TEs.MultiNqGenerator;
import GoodGenerator.Items.MyItemBlocks;
import GoodGenerator.Items.MyItems;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Loaders {

    public static final Item radiationProtectionPlate = new MyItems("radiationProtectionPlate",GoodGenerator.GG);

    public static final Block MAR_Casing = new Casing("MAR_Casing",new String[]{
            GoodGenerator.MOD_ID+":MAR_Casing",
    });
    public static ItemStack MAR;

    public static void Register(){
        GameRegistry.registerBlock(MAR_Casing, MyItemBlocks.class,"MAR_Casing");
        GameRegistry.registerItem(radiationProtectionPlate,"radiationProtectionPlate",GoodGenerator.MOD_ID);
        Loaders.MAR = new MultiNqGenerator(12732,"NaG","Large Naquadah Reactor").getStackForm(1L);
    }
}
