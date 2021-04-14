package GoodGenerator.Loader;

import GoodGenerator.Blocks.RegularBlock.Casing;
import GoodGenerator.Blocks.TEs.MultiNqGenerator;
import GoodGenerator.Items.MyItemBlocks;
import GoodGenerator.Items.MyItems;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Loaders {

    public static final Block MAR_Casing = new Casing("MAR_Casing",new String[]{
            GoodGenerator.MOD_ID+":MAR_Casing",
    });
    public static ItemStack MAR;

    public static void Register(){
        GameRegistry.registerBlock(MAR_Casing, MyItemBlocks.class,"MAR_Casing");

        Loaders.MAR = new MultiNqGenerator(12600+ (GT_Values.VN.length+5) * 8 + 1,"NaG","great naquadah reactor ").getStackForm(1L);

    }
}
