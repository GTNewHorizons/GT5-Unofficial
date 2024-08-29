package gregtech.api.util;

import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.ACCEPT;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.ACCEPT_STOP;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.REJECT;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.SKIP;
import static com.gtnewhorizon.structurelib.util.ItemStackPredicate.NBTMode.EXACT;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.common.blocks.GT_Block_Casings5;
import gregtech.common.blocks.GT_Block_FrameBox;
import gregtech.common.blocks.GT_Cyclotron_Coils;
import gregtech.common.blocks.GT_Item_Machines;

public class GT_StructureUtility {

    // private static final Map<Class<?>, String> customNames = new HashMap<>();
    private GT_StructureUtility() {
        throw new AssertionError("Not instantiable");
    }

    public static boolean hasMTE(IGregTechTileEntity aTile, Class<? extends IMetaTileEntity> clazz) {
        return aTile != null && clazz.isInstance(aTile.getMetaTileEntity());
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex,
        int aDots) {
        return ofHatchAdder(aHatchAdder, aTextureIndex, StructureLibAPI.getBlockHint(), aDots - 1);
    }

    public static <T> IStructureElement<T> ofFrame(Materials aFrameMaterial) {
        if (aFrameMaterial == null) throw new IllegalArgumentException();
        return new IStructureElement<>() {

            private IIcon[] mIcons;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (block instanceof GT_Block_FrameBox frameBox) {
                    int meta = world.getBlockMetadata(x, y, z);
                    Materials material = frameBox.getMaterial(meta);
                    return aFrameMaterial == material;
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
                ItemStack tFrameStack = getFrameStack();
                if (!GT_Utility.isStackValid(tFrameStack)
                    || !(tFrameStack.getItem() instanceof ItemBlock tFrameStackItem)) return false;
                return tFrameStackItem
                    .placeBlockAt(tFrameStack, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrameStack));
            }

            private ItemStack getFrameStack() {
                return GT_OreDictUnificator.get(OrePrefixes.frameGt, aFrameMaterial, 1);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                ItemStack tFrameStack = getFrameStack();
                if (!GT_Utility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
                    return BlocksToPlace.errored;
                return BlocksToPlace.create(tFrameStack);
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (check(t, world, x, y, z)) return SKIP;
                ItemStack tFrameStack = getFrameStack();
                if (!GT_Utility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
                    return REJECT; // honestly, this is more like a programming error or pack issue
                return StructureUtility.survivalPlaceBlock(
                    tFrameStack,
                    ItemStackPredicate.NBTMode.IGNORE_KNOWN_INSIGNIFICANT_TAGS,
                    null,
                    false,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    public static <T> GT_HatchElementBuilder<T> buildHatchAdder() {
        return GT_HatchElementBuilder.builder();
    }

    /**
     * Completely equivalent to {@link #buildHatchAdder()}, except it plays nicer with type inference when statically
     * imported
     */
    public static <T> GT_HatchElementBuilder<T> buildHatchAdder(Class<T> typeToken) {
        return GT_HatchElementBuilder.builder();
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex,
        Block aHintBlock, int aHintMeta) {
        if (aHatchAdder == null || aHintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity
                    && aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aHintBlock, aHintMeta);
                return true;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex,
        Block aHintBlock, int aHintMeta, BiPredicate<T, IGregTechTileEntity> shouldSkip,
        Function<T, Class<? extends IMetaTileEntity>> aMetaId, final IStructureElement.PlaceResult acceptType) {
        if (aHatchAdder == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity
                    && aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex);
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
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                Class<? extends IMetaTileEntity> clazz = aMetaId.apply(t);
                if (clazz == null) return BlocksToPlace.createEmpty();
                return BlocksToPlace.create(is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (shouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity
                        && shouldSkip.test(t, (IGregTechTileEntity) tileEntity)) return SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) return REJECT;
                Class<? extends IMetaTileEntity> clazz = aMetaId.apply(t);
                if (clazz == null) return REJECT;
                ItemStack taken = env.getSource()
                    .takeOne(is -> clazz.isInstance(GT_Item_Machines.getMetaTileEntity(is)), true);
                if (GT_Utility.isStackInvalid(taken)) {
                    env.getChatter()
                        .accept(
                            new ChatComponentTranslation(
                                "GT5U.autoplace.error.no_mte.class_name",
                                clazz.getSimpleName()));
                    return REJECT;
                }
                if (StructureUtility
                    .survivalPlaceBlock(taken, EXACT, null, true, world, x, y, z, env.getSource(), env.getActor())
                    == ACCEPT) return acceptType;
                return REJECT;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdder(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex,
        Block aHintBlock, int aHintMeta, BiPredicate<T, IGregTechTileEntity> shouldSkip, ToIntFunction<T> aMetaId) {
        if (aHatchAdder == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity
                    && aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex);
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
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                GT_Item_Machines item = (GT_Item_Machines) Item.getItemFromBlock(GregTech_API.sBlockMachines);
                int meta = aMetaId.applyAsInt(t);
                if (meta < 0) return BlocksToPlace.createEmpty();
                return BlocksToPlace.create(
                    ItemStackPredicate.from(item)
                        .setMeta(meta));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                if (shouldSkip != null) {
                    TileEntity tileEntity = world.getTileEntity(x, y, z);
                    if (tileEntity instanceof IGregTechTileEntity
                        && shouldSkip.test(t, (IGregTechTileEntity) tileEntity)) return SKIP;
                }
                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) return REJECT;
                GT_Item_Machines item = (GT_Item_Machines) Item.getItemFromBlock(GregTech_API.sBlockMachines);
                int meta = aMetaId.applyAsInt(t);
                if (meta < 0) return REJECT;
                ItemStack taken = env.getSource()
                    .takeOne(
                        ItemStackPredicate.from(item)
                            .setMeta(meta),
                        true);
                if (GT_Utility.isStackInvalid(taken)) {
                    env.getChatter()
                        .accept(new ChatComponentTranslation("GT5U.autoplace.error.no_mte.id", meta));
                    return REJECT;
                }
                return StructureUtility
                    .survivalPlaceBlock(taken, EXACT, null, true, world, x, y, z, env.getSource(), env.getActor())
                    == ACCEPT ? ACCEPT_STOP : REJECT;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> aHatchAdder, int textureIndex,
        int dots, Block placeCasing, int placeCasingMeta) {
        return ofHatchAdderOptional(
            aHatchAdder,
            textureIndex,
            StructureLibAPI.getBlockHint(),
            dots - 1,
            placeCasing,
            placeCasingMeta);
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> aHatchAdder, int aTextureIndex,
        Block aHintBlock, int hintMeta, Block placeCasing, int placeCasingMeta) {
        if (aHatchAdder == null || aHintBlock == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                Block worldBlock = world.getBlock(x, y, z);
                return (tileEntity instanceof IGregTechTileEntity
                    && aHatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) aTextureIndex))
                    || (worldBlock == placeCasing && worldBlock.getDamageValue(world, x, y, z) == placeCasingMeta);
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

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                if (check(t, world, x, y, z)) return SKIP;
                return StructureUtility
                    .survivalPlaceBlock(placeCasing, placeCasingMeta, world, x, y, z, s, actor, chatter);
            }
        };
    }

    /**
     * Assume all coils accepted.
     *
     * @see #ofCoil(BiPredicate, Function)
     */
    public static <T> IStructureElement<T> ofCoil(BiConsumer<T, HeatingCoilLevel> aHeatingCoilSetter,
        Function<T, HeatingCoilLevel> aHeatingCoilGetter) {
        return ofCoil((t, l) -> {
            aHeatingCoilSetter.accept(t, l);
            return true;
        }, aHeatingCoilGetter);
    }

    /**
     * Heating coil structure element.
     *
     * @param aHeatingCoilSetter Notify the controller of this new coil. Got called exactly once per coil. Might be
     *                           called less times if structure test fails. If the setter returns false then it assumes
     *                           the coil is rejected.
     * @param aHeatingCoilGetter Get the current heating level. Null means no coil recorded yet.
     */
    public static <T> IStructureElement<T> ofCoil(BiPredicate<T, HeatingCoilLevel> aHeatingCoilSetter,
        Function<T, HeatingCoilLevel> aHeatingCoilGetter) {
        if (aHeatingCoilSetter == null || aHeatingCoilGetter == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (!(block instanceof IHeatingCoil)) return false;
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
                return HeatingCoilLevel
                    .getFromTier((byte) Math.min(HeatingCoilLevel.getMaxTier(), Math.max(0, trigger.stackSize - 1)));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTech_API.sBlockCasings5, getMetaFromHint(trigger), 3);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(GregTech_API.sBlockCasings5, getMetaFromHint(trigger));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                Block block = world.getBlock(x, y, z);
                boolean isCoil = block instanceof IHeatingCoil
                    && ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z)) == getHeatFromHint(trigger);
                if (isCoil) return SKIP;
                return StructureUtility.survivalPlaceBlock(
                    GregTech_API.sBlockCasings5,
                    getMetaFromHint(trigger),
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    /**
     * Assumes all solenoids are accepted.
     *
     * @see #ofSolenoidCoil(BiPredicate, Function)
     */
    public static <T> IStructureElement<T> ofSolenoidCoil(BiConsumer<T, Byte> aSolenoidTierSetter,
        Function<T, Byte> aSolenoidTierGetter) {
        return ofSolenoidCoil((t, l) -> {
            aSolenoidTierSetter.accept(t, l);
            return true;
        }, aSolenoidTierGetter);
    }

    /**
     * Solenoid coil structure element.
     *
     * @param aSolenoidTierSetter Notify the controller of this new solenoid. Got called exactly once per solenoid.
     *                            Might be
     *                            called less times if structure test fails. If the setter returns false then it assumes
     *                            the solenoid is rejected.
     * @param aSolenoidTierGetter Get the solenoid voltage tier. Null means no tier recorded yet.
     */
    public static <T> IStructureElement<T> ofSolenoidCoil(BiPredicate<T, Byte> aSolenoidTierSetter,
        Function<T, Byte> aSolenoidTierGetter) {
        if (aSolenoidTierSetter == null || aSolenoidTierGetter == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);

                if (block != GregTech_API.sSolenoidCoilCasings) return false;

                var coils = ((GT_Cyclotron_Coils) GregTech_API.sSolenoidCoilCasings);

                Byte existingLevel = aSolenoidTierGetter.apply(t);
                byte newLevel = (byte) (coils.getVoltageTier(world.getBlockMetadata(x, y, z)));

                if (existingLevel == null) {
                    return aSolenoidTierSetter.test(t, newLevel);
                } else {
                    return newLevel == existingLevel;
                }
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI
                    .hintParticle(world, x, y, z, GregTech_API.sSolenoidCoilCasings, getMetaFromHint(trigger));
                return true;
            }

            private int getMetaFromHint(ItemStack trigger) {
                return Math.min(Math.max(trigger.stackSize - 1, 0), 10);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTech_API.sSolenoidCoilCasings, getMetaFromHint(trigger), 3);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(GregTech_API.sSolenoidCoilCasings, getMetaFromHint(trigger));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return survivalPlaceBlock(
                    t,
                    world,
                    x,
                    y,
                    z,
                    trigger,
                    AutoPlaceEnvironment.fromLegacy(s, actor, chatter));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                Block block = world.getBlock(x, y, z);

                boolean isCoil = block == GregTech_API.sSolenoidCoilCasings
                    && world.getBlockMetadata(x, y, z) == getMetaFromHint(trigger);

                if (isCoil) return SKIP;

                return StructureUtility.survivalPlaceBlock(
                    GregTech_API.sSolenoidCoilCasings,
                    getMetaFromHint(trigger),
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    @Nonnull
    public static Predicate<ItemStack> filterByMTEClass(List<? extends Class<? extends IMetaTileEntity>> list) {
        return is -> {
            IMetaTileEntity tile = GT_Item_Machines.getMetaTileEntity(is);
            return tile != null && list.stream()
                .anyMatch(c -> c.isInstance(tile));
        };
    }

    @Nonnull
    public static Predicate<ItemStack> filterByMTETier(int aMinTier, int aMaxTier) {
        return is -> {
            IMetaTileEntity tile = GT_Item_Machines.getMetaTileEntity(is);
            return tile instanceof GT_MetaTileEntity_TieredMachineBlock
                && ((GT_MetaTileEntity_TieredMachineBlock) tile).mTier <= aMaxTier
                && ((GT_MetaTileEntity_TieredMachineBlock) tile).mTier >= aMinTier;
        };
    }
}
