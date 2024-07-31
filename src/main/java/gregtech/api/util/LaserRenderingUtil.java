package gregtech.api.util;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.ICustomBlockSetting;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

public class LaserRenderingUtil {
    // This code is shamelessly ripped from GTNH-Intergalactic

    public interface IBlockAdder<T> {

        /**
         * Callback on block added, needs to check if block is valid (and add it)
         *
         * @param block block attempted to add
         * @param meta  meta of block attempted to add
         * @param world World of the block
         * @param x     X coordinate of the block
         * @param y     Y coordinate of the block
         * @param z     Z coordinate of the block
         * @return is structure still valid
         */
        boolean apply(T t, Block block, int meta, World world, int x, int y, int z);
    }

    public static <T> IStructureElement<T> ofBlockAdder(IBlockAdder<T> iBlockAdder, Block defaultBlock,
        int defaultMeta) {
        if (iBlockAdder == null || defaultBlock == null) {
            throw new IllegalArgumentException();
        }
        if (defaultBlock instanceof ICustomBlockSetting) {
            return new IStructureElement<T>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdder.apply(t, worldBlock, worldBlock.getDamageValue(world, x, y, z), world, x, y, z);
                }

                @Override
                public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                    ((ICustomBlockSetting) defaultBlock).setBlock(world, x, y, z, defaultMeta);
                    return true;
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }

                @Override
                public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                    if (check(t, world, x, y, z)) return PlaceResult.SKIP;
                    return StructureUtility
                        .survivalPlaceBlock(defaultBlock, defaultMeta, world, x, y, z, s, actor, chatter);
                }
            };
        } else {
            return new IStructureElement<T>() {

                @Override
                public boolean check(T t, World world, int x, int y, int z) {
                    Block worldBlock = world.getBlock(x, y, z);
                    return iBlockAdder
                        .apply(t, worldBlock, ((Block) worldBlock).getDamageValue(world, x, y, z), world, x, y, z);
                }

                @Override
                public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                    world.setBlock(x, y, z, defaultBlock, defaultMeta, 2);
                    return true;
                }

                @Override
                public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                    StructureLibAPI.hintParticle(world, x, y, z, defaultBlock, defaultMeta);
                    return true;
                }

                @Override
                public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                    if (check(t, world, x, y, z)) return IStructureElement.PlaceResult.SKIP;
                    return StructureUtility
                        .survivalPlaceBlock(defaultBlock, defaultMeta, world, x, y, z, s, actor, chatter);
                }
            };
        }
    }
}
