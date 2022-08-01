package gregtech.api.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.common.blocks.GT_Block_Casings5;
import gregtech.common.blocks.GT_Item_Machines;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

public class GT_StructureUtility {
    //    private static final Map<Class<?>, String> customNames = new HashMap<>();
    private GT_StructureUtility() {
        throw new AssertionError("Not instantiable");
    }

    public static boolean hasMTE(IGregTechTileEntity aTile, Class<? extends IMetaTileEntity> clazz) {
        return aTile != null && clazz.isInstance(aTile.getMetaTileEntity());
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, int aDots) {
        return ofHatchAdder(aHatchAdder, aTextureIndex, StructureLibAPI.getBlockHint(), aDots - 1);
    }

    public static <T extends GT_MetaTileEntity_EnhancedMultiBlockBase<T>> IStructureElement<T> ofMuffler(int casing, int aDots, IStructureElement.PlaceResult acceptType) {
        return ofHatchAdder(T::addMufflerToMachineList, casing, StructureLibAPI.getBlockHint(), aDots, (o, t) -> hasMTE(t, GT_MetaTileEntity_Hatch_Muffler.class), t -> GT_MetaTileEntity_Hatch_Muffler.class, acceptType);
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
                if (!GT_Utility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
                    return false;
                ItemBlock tFrameStackItem = (ItemBlock) tFrameStack.getItem();
                return tFrameStackItem.placeBlockAt(tFrameStack, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrameStack));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (check(t, world, x, y, z)) return PlaceResult.SKIP;
                ItemStack tFrameStack = GT_OreDictUnificator.get(OrePrefixes.frameGt, aFrameMaterial, 1);
                if (!GT_Utility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
                    return PlaceResult.REJECT; // honestly, this is more like a programming error or pack issue
                return StructureUtility.survivalPlaceBlock(tFrameStack, ItemStackPredicate.NBTMode.IGNORE_KNOWN_INSIGNIFICANT_TAGS, null, false, world, x, y, z, s, actor);
            }
        };
    }

    public static <T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> GT_HatchElementBuilder<T> buildHatchAdder() {
        return GT_HatchElementBuilder.builder();
    }

    /**
     * Completely equivalent to {@link #buildHatchAdder()}, except it plays nicer with type inference when statically imported
     */
    public static <T extends GT_MetaTileEntity_EnhancedMultiBlockBase<?>> GT_HatchElementBuilder<T> buildHatchAdder(Class<T> typeToken) {
        return GT_HatchElementBuilder.builder();
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

    public static <T> IStructureElement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, Block aHintBlock, int aHintMeta, BiPredicate<T, IGregTechTileEntity> shouldSkip, Function<T, Class<? extends IMetaTileEntity>> aMetaId, final IStructureElement.PlaceResult acceptType) {
        if (aHatchAdder == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
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

            @Override
            public boolean placeBlock(T t, World world, int i, int i1, int i2, ItemStack itemStack) {
                // TODO
                return false;
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (shouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity && shouldSkip.test(t, (IGregTechTileEntity) tileEntity))
                        return PlaceResult.SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor))
                    return PlaceResult.REJECT;
                Class<? extends IMetaTileEntity> clazz = aMetaId.apply(t);
                if (clazz == null) return PlaceResult.REJECT;
                ItemStack taken = s.takeOne(is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)), true);
                if (GT_Utility.isStackInvalid(taken)) {
                    chatter.accept(new ChatComponentText("Suggested to have MTE of type " + clazz.getSimpleName() + " but none was found"));
                    return PlaceResult.REJECT;
                }
                if (StructureUtility.survivalPlaceBlock(taken, ItemStackPredicate.NBTMode.IGNORE, null, true, world, x, y, z, s, actor) == PlaceResult.ACCEPT) {

                    return acceptType;
                }
                return PlaceResult.REJECT;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex, Block aHintBlock, int aHintMeta, BiPredicate<T, IGregTechTileEntity> shouldSkip, ToIntFunction<T> aMetaId) {
        if (aHatchAdder == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<T>() {
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

            @Override
            public boolean placeBlock(T t, World world, int i, int i1, int i2, ItemStack itemStack) {
                // TODO
                return false;
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (shouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity && shouldSkip.test(t, (IGregTechTileEntity) tileEntity))
                        return PlaceResult.SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, actor))
                    return PlaceResult.REJECT;
                GT_Item_Machines item = (GT_Item_Machines) Item.getItemFromBlock(GregTech_API.sBlockMachines);
                int meta = aMetaId.applyAsInt(t);
                if (meta < 0) return PlaceResult.REJECT;
                ItemStack taken = s.takeOne(ItemStackPredicate.from(item).setMeta(meta), true);
                if (GT_Utility.isStackInvalid(taken)) {
                    chatter.accept(new ChatComponentText("Suggested to have MTE of id " + meta + " but none was found"));
                    return PlaceResult.REJECT;
                }
                return StructureUtility.survivalPlaceBlock(taken, ItemStackPredicate.NBTMode.IGNORE, null, true, world, x, y, z, s, actor) == PlaceResult.ACCEPT ? PlaceResult.ACCEPT_STOP : PlaceResult.REJECT;
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
                StructureLibAPI.hintParticle(world, x, y, z, GregTech_API.sBlockCasings5, getMetaFromHint(trigger));
                return true;
            }

            private int getMetaFromHint(ItemStack trigger) {
                return GT_Block_Casings5.getMetaFromCoilHeat(getHeatFromHint(trigger));
            }

            private HeatingCoilLevel getHeatFromHint(ItemStack trigger) {
                return HeatingCoilLevel.getFromTier((byte) Math.min(HeatingCoilLevel.getMaxTier() , Math.max(0, trigger.stackSize - 1)));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTech_API.sBlockCasings5, getMetaFromHint(trigger), 3);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                Block block = world.getBlock(x, y, z);
                boolean isCoil = block instanceof IHeatingCoil && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) == getHeatFromHint(trigger);
                if (isCoil) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(GregTech_API.sBlockCasings5, getMetaFromHint(trigger), world, x, y, z, s, actor);
            }
        };
    }

    @Nonnull
    public static Predicate<ItemStack> filterByMTEClass(List<? extends Class<? extends IMetaTileEntity>> list) {
        return is -> {
            IMetaTileEntity tile = GT_Item_Machines.getMetaTileEntity(is);
            return tile != null && list.stream().anyMatch(c -> c.isInstance(tile));
        };
    }
}
