package gregtech.common.ores;

import java.util.List;

import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IMaterial;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IOreAdapter<TMat extends IMaterial> {

    public boolean supports(Block block, int meta);
    public boolean supports(OreInfo<?> info);

    public OreInfo<TMat> getOreInfo(Block block, int meta);
    public default OreInfo<TMat> getOreInfo(World world, int x, int y, int z) {
        return getOreInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }
    public default OreInfo<TMat> getOreInfo(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getOreInfo(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    public ObjectIntPair<Block> getBlock(OreInfo<?> info);
    
    public default ItemStack getStack(OreInfo<?> info, int amount) {
        ObjectIntPair<Block> p = getBlock(info);

        if (p != null) {
            return new ItemStack(p.left(), amount, p.rightInt());
        }

        if (info.stoneType != StoneType.Stone) {
            try(OreInfo<?> info2 = info.clone()) {
                info2.stoneType = StoneType.Stone;

                p = getBlock(info);

                if (p != null) {
                    return new ItemStack(p.left(), amount, p.rightInt());
                }
            }
        }

        return null;
    }

    public List<ItemStack> getOreDrops(OreInfo<?> info, boolean silktouch, int fortune);
    public List<ItemStack> getPotentialDrops(OreInfo<?> info);
}
