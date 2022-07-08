package gregtech.api.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.common.blocks.GT_Block_Casings5;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class GT_StructureUtility {
    private GT_StructureUtility() {
        throw new AssertionError("Not instantiable");
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, int aDots) {
        return ofHatchAdder(aHatchAdder, aTextureIndex, StructureLibAPI.getBlockHint(), aDots - 1);
    }

    public static <T> IStructureElement<T> ofFrame(Materials aFrameMaterial) {
        if (aFrameMaterial == null) throw new IllegalArgumentException();
        return new IStructureElement<T>() {

            private IIcon[] mIcons;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tBase = world.getTileEntity(x, y, z);
                if (tBase instanceof BaseMetaPipeEntity) {
                    BaseMetaPipeEntity tPipeBase = (BaseMetaPipeEntity) tBase;
                    if (tPipeBase.isInvalidTileEntity()) return false;
                    if (tPipeBase.getMetaTileEntity() instanceof GT_MetaPipeEntity_Frame)
                        return aFrameMaterial == ((GT_MetaPipeEntity_Frame) tPipeBase.getMetaTileEntity()).mMaterial;
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (mIcons == null) {
                    mIcons = new IIcon[6];
                    Arrays.fill(mIcons, aFrameMaterial.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex].getIcon());
                }
                StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, aFrameMaterial.mRGBa);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                ItemStack tFrameStack = GT_OreDictUnificator.get(OrePrefixes.frameGt, aFrameMaterial, 1);
                if (tFrameStack.getItem() instanceof ItemBlock) {
                    ItemBlock tFrameStackItem = (ItemBlock) tFrameStack.getItem();
                    return tFrameStackItem.placeBlockAt(tFrameStack, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrameStack));
                }
                return false;
            }
        };
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, Block aHintBlock, int aHintMeta) {
        if (aHatchAdder == null || aHintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity && aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aHintBlock, aHintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> aHatchAdder, int textureIndex, int dots, Block placeCasing, int placeCasingMeta) {
        return ofHatchAdderOptional(aHatchAdder, textureIndex, StructureLibAPI.getBlockHint(), dots - 1, placeCasing, placeCasingMeta);
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, Block aHintBlock, int hintMeta, Block placeCasing, int placeCasingMeta) {
        if (aHatchAdder == null || aHintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                Block worldBlock = world.getBlock(x, y, z);
                return (tileEntity instanceof IGregTechTileEntity &&
                    aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex)) ||
                    (worldBlock == placeCasing && worldBlock.getDamageValue(world, x, y, z) == placeCasingMeta);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aHintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, placeCasing, placeCasingMeta, 2);
                return true;
            }
        };
    }

    /**
     * Assume all coils accepted.
     *
     * @see #ofCoil(BiPredicate, Function)
     */
    public static <T> IStructureElement<T> ofCoil(BiConsumer<T, HeatingCoilLevel> aHeatingCoilSetter, Function<T, HeatingCoilLevel> aHeatingCoilGetter) {
        return ofCoil((t, l) -> {
            aHeatingCoilSetter.accept(t, l);
            return true;
        }, aHeatingCoilGetter);
    }

    /**
     * Heating coil structure element.
     *
     * @param aHeatingCoilSetter Notify the controller of this new coil.
     *                           Got called exactly once per coil.
     *                           Might be called less times if structure test fails.
     *                           If the setter returns false then it assumes the coil is rejected.
     * @param aHeatingCoilGetter Get the current heating level. Null means no coil recorded yet.
     */
    public static <T> IStructureElement<T> ofCoil(BiPredicate<T, HeatingCoilLevel> aHeatingCoilSetter, Function<T, HeatingCoilLevel> aHeatingCoilGetter) {
        if (aHeatingCoilSetter == null || aHeatingCoilGetter == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (!(block instanceof IHeatingCoil))
                    return false;
                HeatingCoilLevel existingLevel = aHeatingCoilGetter.apply(t),
                    newLevel = ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z));
                if (existingLevel == null || existingLevel == HeatingCoilLevel.None) {
                    return aHeatingCoilSetter.test(t, newLevel);
                } else {
                    return newLevel == existingLevel;
                }
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockCasings5, getMeta(trigger));
                return true;
            }

            private int getMeta(ItemStack trigger) {
                return GT_Block_Casings5.getMetaFromCoilHeat(HeatingCoilLevel.getFromTier((byte) Math.min(HeatingCoilLevel.getMaxTier() , Math.max(0, trigger.stackSize - 1))));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTech_API.sBlockCasings5, getMeta(trigger), 3);
            }
        };
    }
}
