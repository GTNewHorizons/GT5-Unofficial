package goodgenerator.util;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.util.GTOreDictUnificator;

public class StructureHelper {

    public static <T> IStructureElement<T> addFrame(Materials aMaterials) {
        return new IStructureElement<T>() {

            private IIcon[] mIcons;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tBlock = world.getTileEntity(x, y, z);
                if (tBlock instanceof BaseMetaPipeEntity) {
                    BaseMetaPipeEntity tFrame = (BaseMetaPipeEntity) tBlock;
                    if (tFrame.isInvalidTileEntity()) return false;
                    if (tFrame.getMetaTileEntity() instanceof MTEFrame) {
                        return ((MTEFrame) tFrame.getMetaTileEntity()).mMaterial == aMaterials;
                    }
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (mIcons == null) {
                    mIcons = new IIcon[6];
                    Arrays.fill(mIcons, aMaterials.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex].getIcon());
                }
                StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, aMaterials.mRGBa);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                ItemStack tFrame = GTOreDictUnificator.get(OrePrefixes.frameGt, aMaterials, 1);
                if (tFrame.getItem() instanceof ItemBlock) {
                    ItemBlock tFrameStackItem = (ItemBlock) tFrame.getItem();
                    return tFrameStackItem
                        .placeBlockAt(tFrame, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrame));
                }
                return false;
            }
        };
    }

    // Only support to use meta to tier
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiConsumer<T, Integer> aSetTheFuckingMeta,
        Function<T, Integer> aGetTheFuckingMeta, int maxMeta) {
        return addTieredBlock(aBlock, (t, i) -> {
            aSetTheFuckingMeta.accept(t, i);
            return true;
        }, aGetTheFuckingMeta, maxMeta);
    }

    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiPredicate<T, Integer> aSetTheFuckingMeta,
        Function<T, Integer> aGetTheFuckingMeta, int maxMeta) {

        return new IStructureElement<T>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block tBlock = world.getBlock(x, y, z);
                if (aBlock == tBlock) {
                    Integer currentMeta = aGetTheFuckingMeta.apply(t);
                    int newMeta = tBlock.getDamageValue(world, x, y, z) + 1;
                    if (newMeta > maxMeta) return false;
                    if (currentMeta == 0) {
                        return aSetTheFuckingMeta.test(t, newMeta);
                    } else {
                        return currentMeta == newMeta;
                    }
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aBlock, getMeta(trigger));
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, aBlock, getMeta(trigger), 3);
            }

            private int getMeta(ItemStack trigger) {
                int meta = trigger.stackSize - 1;
                if (meta <= 0) meta = 0;
                if (meta >= maxMeta) meta = maxMeta - 1;
                return meta;
            }
        };
    }
}
