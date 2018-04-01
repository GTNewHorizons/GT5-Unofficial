package gtPlusPlus.xmod.sc2.modules;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.ICropModule;
import vswe.stevescarts.Modules.Addons.ModuleAddon;

public class ModuleExoticSeeds extends ModuleAddon implements ICropModule {

    public ModuleExoticSeeds(MinecartModular cart) {
        super(cart);
    }

    private synchronized Block getBlockFromItemSeeds(ItemStack seed) {
        try {

            Item seedItem = seed.getItem();
            if (!(seedItem instanceof ItemSeeds)) return null;

            Block cropBlock = (Block) ReflectionUtils.getField(ItemSeeds.class, "field_150925_a").get(seedItem);

            return cropBlock;
        } catch (Throwable t) {

        }
        return null;
    }

    @Override
    public boolean isSeedValid(ItemStack seed) {
        return getBlockFromItemSeeds(seed) != null;
    }

    @Override
    public Block getCropFromSeed(ItemStack seed) {
        return getBlockFromItemSeeds(seed);
    }

    @Override
    public boolean isReadyToHarvest(int x, int y, int z) {
        Block b = getCart().worldObj.getBlock(x, y, z);
        int m = getCart().worldObj.getBlockMetadata(x, y, z);

        return b instanceof BlockCrops && m == 7;
    }

}