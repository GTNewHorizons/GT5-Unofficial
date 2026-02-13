package gregtech.api.util;

import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.ACCEPT;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.ACCEPT_STOP;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.REJECT;
import static com.gtnewhorizon.structurelib.structure.IStructureElement.PlaceResult.SKIP;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.util.ItemStackPredicate.NBTMode.EXACT;
import static gregtech.api.GregTechAPI.sBlockSheetmetalBW;
import static gregtech.api.GregTechAPI.sBlockSheetmetalGT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizon.structurelib.util.ItemStackPredicate;

import bartworks.system.material.Werkstoff;
import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.common.blocks.BlockCasings5;
import gregtech.common.blocks.BlockCyclotronCoils;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.core.material.Material;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class GTStructureUtility {

    // private static final Map<Class<?>, String> customNames = new HashMap<>();
    private GTStructureUtility() {
        throw new AssertionError("Not instantiable");
    }

    public static boolean hasMTE(IGregTechTileEntity aTile, Class<? extends IMetaTileEntity> clazz) {
        return aTile != null && clazz.isInstance(aTile.getMetaTileEntity());
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGTHatchAdder<T> aHatchAdder, int aTextureIndex,
        int aDots) {
        return ofHatchAdder(aHatchAdder, aTextureIndex, StructureLibAPI.getBlockHint(), aDots - 1);
    }

    public static <T> IStructureElement<T> ofAnyWater() {
        return ofAnyWater(false);
    }

    public static <T> IStructureElement<T> ofAnyWater(boolean allowFlowing) {
        return new IStructureElement<>() {

            final Block distilledWater = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (block == Blocks.water || block == distilledWater) return true;
                if (allowFlowing && block == Blocks.flowing_water) return true;
                if (Mods.COFHCore.isModLoaded()) {
                    return block instanceof BlockWater || block instanceof BlockTickingWater;
                }
                return false;
            }

            @Override
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                return check(t, world, x, y, z);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, Blocks.water, 0, 2);
                return true;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, Blocks.water, 0);
                return true;
            }

            @Override
            public IStructureElement.BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                return IStructureElement.BlocksToPlace.create(Blocks.water, 0);
            }
        };
    }

    public static <T> IStructureElement<T> ofSheetMetal(Materials material) {
        if (material == null) throw new IllegalArgumentException("material for sheet metal can not be null!");
        return ofBlock(sBlockSheetmetalGT, material.mMetaItemSubID);
    }

    public static <T> IStructureElement<T> ofSheetMetal(Werkstoff werkstoff) {
        if (werkstoff == null) throw new IllegalArgumentException("werkstoff for sheet metal can not be null!");
        return ofBlock(sBlockSheetmetalBW, werkstoff.getmID());
    }

    public static <T> IStructureElement<T> ofFrame(Materials aFrameMaterial) {
        if (aFrameMaterial == null) throw new IllegalArgumentException();
        return new IStructureElement<>() {

            private IIcon[] mIcons;

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                if (block instanceof BlockFrameBox frameBox) {
                    int meta = world.getBlockMetadata(x, y, z);
                    Materials material = BlockFrameBox.getMaterial(meta);
                    return aFrameMaterial == material;
                }
                return false;
            }

            @Override
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                return check(t, world, x, y, z);
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                if (mIcons == null && FMLLaunchHandler.side()
                    .isClient()) {
                    mIcons = new IIcon[6];
                    Arrays.fill(
                        mIcons,
                        aFrameMaterial.mIconSet.mTextures[OrePrefixes.frameGt.getTextureIndex()].getIcon());
                }
                StructureLibAPI.hintParticleTinted(world, x, y, z, mIcons, aFrameMaterial.mRGBa);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                ItemStack tFrameStack = getFrameStack();
                if (!GTUtility.isStackValid(tFrameStack)
                    || !(tFrameStack.getItem() instanceof ItemBlock tFrameStackItem)) return false;
                return tFrameStackItem
                    .placeBlockAt(tFrameStack, null, world, x, y, z, 6, 0, 0, 0, Items.feather.getDamage(tFrameStack));
            }

            private ItemStack getFrameStack() {
                return GTOreDictUnificator.get(OrePrefixes.frameGt, aFrameMaterial, 1);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                ItemStack tFrameStack = getFrameStack();
                if (!GTUtility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
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
                if (!GTUtility.isStackValid(tFrameStack) || !(tFrameStack.getItem() instanceof ItemBlock))
                    return REJECT; // honestly, this is more like a programming error or pack issue
                return com.gtnewhorizon.structurelib.structure.StructureUtility.survivalPlaceBlock(
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

    public static <T> IStructureElement<T> ofFrame(Supplier<ItemStack> frameSupplier) {
        return lazy(t -> {
            ItemStack stack = frameSupplier.get();
            Block block = Block.getBlockFromItem(stack.getItem());
            return ofBlock(block, stack.getItemDamage());
        });
    }

    public static <T> IStructureElement<T> ofFrame(Material material) {
        return ofFrame(() -> material.getFrameBox(1));
    }

    public static <T> HatchElementBuilder<T> buildHatchAdder() {
        return HatchElementBuilder.builder();
    }

    /**
     * Completely equivalent to {@link #buildHatchAdder()}, except it plays nicer with type inference when statically
     * imported
     */
    public static <T> HatchElementBuilder<T> buildHatchAdder(Class<T> typeToken) {
        return HatchElementBuilder.builder();
    }

    public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGTHatchAdder<T> aHatchAdder, int aTextureIndex,
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

    public static <T> IStructureElement<T> ofHatchAdder(IGTHatchAdder<T> aHatchAdder, int aTextureIndex,
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
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity;
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
                return BlocksToPlace.create(is -> clazz.isInstance(ItemMachines.getMetaTileEntity(is)));
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
                    .takeOne(is -> clazz.isInstance(ItemMachines.getMetaTileEntity(is)), true);
                if (GTUtility.isStackInvalid(taken)) {
                    env.getChatter()
                        .accept(
                            new ChatComponentTranslation(
                                "GT5U.autoplace.error.no_mte.class_name",
                                clazz.getSimpleName()));
                    return REJECT;
                }
                if (com.gtnewhorizon.structurelib.structure.StructureUtility
                    .survivalPlaceBlock(taken, EXACT, null, true, world, x, y, z, env.getSource(), env.getActor())
                    == ACCEPT) return acceptType;
                return REJECT;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdder(IGTHatchAdder<T> aHatchAdder, int aTextureIndex,
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
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileEntity instanceof IGregTechTileEntity;
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
                ItemMachines item = (ItemMachines) Item.getItemFromBlock(GregTechAPI.sBlockMachines);
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
                ItemMachines item = (ItemMachines) Item.getItemFromBlock(GregTechAPI.sBlockMachines);
                int meta = aMetaId.applyAsInt(t);
                if (meta < 0) return REJECT;
                ItemStack taken = env.getSource()
                    .takeOne(
                        ItemStackPredicate.from(item)
                            .setMeta(meta),
                        true);
                if (GTUtility.isStackInvalid(taken)) {
                    env.getChatter()
                        .accept(new ChatComponentTranslation("GT5U.autoplace.error.no_mte.id", meta));
                    return REJECT;
                }
                return com.gtnewhorizon.structurelib.structure.StructureUtility
                    .survivalPlaceBlock(taken, EXACT, null, true, world, x, y, z, env.getSource(), env.getActor())
                    == ACCEPT ? ACCEPT_STOP : REJECT;
            }
        };
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGTHatchAdder<T> aHatchAdder, int textureIndex,
        int dots, Block placeCasing, int placeCasingMeta) {
        return ofHatchAdderOptional(
            aHatchAdder,
            textureIndex,
            StructureLibAPI.getBlockHint(),
            dots - 1,
            placeCasing,
            placeCasingMeta);
    }

    public static <T> IStructureElement<T> ofHatchAdderOptional(IGTHatchAdder<T> aHatchAdder, int aTextureIndex,
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
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                Block worldBlock = world.getBlock(x, y, z);
                return (tileEntity instanceof IGregTechTileEntity)
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
                return com.gtnewhorizon.structurelib.structure.StructureUtility
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

    public static <T extends MTEMultiBlockBase> IStructureElement<T> activeCoils(IStructureElement<T> element) {
        return new ProxyStructureElement<>(element) {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                if (!element.check(t, world, x, y, z)) return false;

                t.mCoils.add(CoordinatePacker.pack(x, y, z));

                return true;
            }
        };
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

                if (!(block instanceof IHeatingCoil coil)) return false;

                HeatingCoilLevel existingLevel = aHeatingCoilGetter.apply(t);
                HeatingCoilLevel newLevel = coil.getCoilHeat(world.getBlockMetadata(x, y, z));

                if (existingLevel == null || existingLevel == HeatingCoilLevel.None) {
                    return aHeatingCoilSetter.test(t, newLevel);
                } else {
                    return newLevel == existingLevel;
                }
            }

            @Override
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                Block block = world.getBlock(x, y, z);
                if (!(block instanceof IHeatingCoil)) return false;
                HeatingCoilLevel blockLevel = ((IHeatingCoil) block).getCoilHeat(world.getBlockMetadata(x, y, z));
                HeatingCoilLevel expectedLevel = getHeatFromHint(trigger);
                return blockLevel == expectedLevel;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, GregTechAPI.sBlockCasings5, getMetaFromHint(trigger));
                return true;
            }

            private int getMetaFromHint(ItemStack trigger) {
                return BlockCasings5.getMetaFromCoilHeat(getHeatFromHint(trigger));
            }

            private HeatingCoilLevel getHeatFromHint(ItemStack trigger) {
                return HeatingCoilLevel
                    .getFromTier((byte) Math.min(HeatingCoilLevel.getMaxTier(), Math.max(0, trigger.stackSize - 1)));
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTechAPI.sBlockCasings5, getMetaFromHint(trigger), 3);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(GregTechAPI.sBlockCasings5, getMetaFromHint(trigger));
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
                return com.gtnewhorizon.structurelib.structure.StructureUtility.survivalPlaceBlock(
                    GregTechAPI.sBlockCasings5,
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
     *                            Might be called less times if structure test fails. If the setter returns false then
     *                            it assumes the solenoid is rejected.
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

                if (block != GregTechAPI.sSolenoidCoilCasings) return false;

                var coils = ((BlockCyclotronCoils) GregTechAPI.sSolenoidCoilCasings);

                Byte existingLevel = aSolenoidTierGetter.apply(t);
                byte newLevel = (byte) (coils.getVoltageTier(world.getBlockMetadata(x, y, z)));

                if (existingLevel == null) {
                    return aSolenoidTierSetter.test(t, newLevel);
                } else {
                    return newLevel == existingLevel;
                }
            }

            @Override
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                Block block = world.getBlock(x, y, z);
                if (block != GregTechAPI.sSolenoidCoilCasings) return false;

                int expectedMeta = getMetaFromHint(trigger);
                int blockMeta = world.getBlockMetadata(x, y, z);

                return expectedMeta == blockMeta;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI
                    .hintParticle(world, x, y, z, GregTechAPI.sSolenoidCoilCasings, getMetaFromHint(trigger));
                return true;
            }

            private int getMetaFromHint(ItemStack trigger) {
                return Math.min(Math.max(trigger.stackSize - 1, 0), 10);
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, GregTechAPI.sSolenoidCoilCasings, getMetaFromHint(trigger), 3);
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return BlocksToPlace.create(GregTechAPI.sSolenoidCoilCasings, getMetaFromHint(trigger));
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

                boolean isCoil = block == GregTechAPI.sSolenoidCoilCasings
                    && world.getBlockMetadata(x, y, z) == getMetaFromHint(trigger);

                if (isCoil) return SKIP;

                return com.gtnewhorizon.structurelib.structure.StructureUtility.survivalPlaceBlock(
                    GregTechAPI.sSolenoidCoilCasings,
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
            IMetaTileEntity tile = ItemMachines.getMetaTileEntity(is);
            return tile != null && list.stream()
                .anyMatch(c -> c.isInstance(tile));
        };
    }

    /**
     * like {@link #filterByMTEClass(java.util.List)}, but adds a blacklist check to the predicate
     *
     * @param list
     * @param blacklist
     * @return predicate of all multis of same type as hatchelement, with blacklist omitted
     */
    @Nonnull
    public static Predicate<ItemStack> filterByMTEClassWithBlacklist(
        List<? extends Class<? extends IMetaTileEntity>> list, List<Class<? extends IMetaTileEntity>> blacklist) {
        return is -> {
            IMetaTileEntity tile = ItemMachines.getMetaTileEntity(is);
            return tile != null && list.stream()
                .anyMatch(c -> c.isInstance(tile) && !blacklist.contains(tile.getClass()));
        };
    }

    @Nonnull
    public static Predicate<ItemStack> filterByMTETier(int aMinTier, int aMaxTier) {
        return is -> {
            IMetaTileEntity tile = ItemMachines.getMetaTileEntity(is);

            if (tile instanceof MTEHatch hatch) {
                if (hatch.getTierForStructure() <= aMaxTier && hatch.getTierForStructure() >= aMinTier) return true;
            }

            return tile instanceof MTETieredMachineBlock && ((MTETieredMachineBlock) tile).mTier <= aMaxTier
                && ((MTETieredMachineBlock) tile).mTier >= aMinTier;
        };
    }

    public static <T> IStructureElement<T> chainAllGlasses() {
        return chainAllGlasses(-1, (te, t) -> {}, te -> -1);
    }

    /** support all Bart, Botania, Ic2, Thaumcraft glasses for multiblock structure **/
    public static <T> IStructureElement<T> chainAllGlasses(int notSet, BiConsumer<T, Integer> setter,
        Function<T, Integer> getter) {
        return GTStructureChannels.BOROGLASS.use(
            lazy(t -> ofBlocksTiered(GlassTier::getGlassBlockTier, GlassTier.getGlassList(), notSet, setter, getter)));
    }

    private static Integer getItemPipeCasingTier(Block block, int meta) {
        if (block != GregTechAPI.sBlockCasings11) return null;
        if (meta < 0 || meta > 7) return null;
        return meta + 1;
    }

    public static <T> IStructureElement<T> chainItemPipeCasings() {
        return chainItemPipeCasings(-1, (t, tier) -> {}, t -> -1);
    }

    public static <T> IStructureElement<T> chainItemPipeCasings(int notSet, BiConsumer<T, Integer> setter,
        Function<T, Integer> getter) {
        return GTStructureChannels.ITEM_PIPE_CASING.use(
            lazy(
                t -> ofBlocksTiered(
                    GTStructureUtility::getItemPipeCasingTier,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasings11, 0),
                        Pair.of(GregTechAPI.sBlockCasings11, 1),
                        Pair.of(GregTechAPI.sBlockCasings11, 2),
                        Pair.of(GregTechAPI.sBlockCasings11, 3),
                        Pair.of(GregTechAPI.sBlockCasings11, 4),
                        Pair.of(GregTechAPI.sBlockCasings11, 5),
                        Pair.of(GregTechAPI.sBlockCasings11, 6),
                        Pair.of(GregTechAPI.sBlockCasings11, 7)),
                    notSet,
                    setter,
                    getter)));
    }

    public static <T> IStructureElement<T> chainAllCasings() {
        return chainAllCasings(-1, (te, t) -> {}, te -> -1);
    }

    public static <T> IStructureElement<T> chainAllCasings(int notSet, BiConsumer<T, Integer> setter,
        Function<T, Integer> getter) {
        return GTStructureChannels.TIER_CASING.use(
            lazy(
                t -> ofBlocksTiered(
                    CasingTier::getCasingBlockTier,
                    CasingTier.getCasingList(),
                    notSet,
                    setter,
                    getter)));
    }

    public static <T> IStructureElement<T> noSurvivalAutoplace(IStructureElement<T> element) {
        return new ProxyStructureElement<>(element) {

            @Override
            public PlaceResult survivalPlaceBlock(T multi, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {
                return PlaceResult.SKIP;
            }

            @Override
            public PlaceResult survivalPlaceBlock(T multi, World world, int x, int y, int z, ItemStack trigger,
                IItemSource s, EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
                return PlaceResult.SKIP;
            }
        };
    }

    public static <TMTE extends IMetaTileEntity> List<TMTE> extractMTEs(Class<TMTE> mteClass, ItemStack... stacks) {
        List<TMTE> mtes = new ArrayList<>();

        for (ItemStack stack : stacks) {
            IMetaTileEntity mte = ItemMachines.getMetaTileEntity(stack);

            assert mte != null;
            if (!mteClass.isAssignableFrom(mte.getClass()))
                throw new IllegalArgumentException(stack.getDisplayName() + " is not a " + mteClass);

            mtes.add(mteClass.cast(mte));
        }

        return mtes;
    }

    public interface MTEAdder<T, TMTE extends IMetaTileEntity> {

        boolean check(T t, TMTE mte, int tier);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static <T, TMTE extends IMetaTileEntity> IStructureElement<T> ofGenericMTETiered(Class<TMTE> mteClass,
        MTEAdder<T, TMTE> adder, List<TMTE> tiers) {
        Int2IntOpenHashMap tierMap = new Int2IntOpenHashMap();

        int i = 1;
        for (TMTE mte : tiers) {
            tierMap.put(
                mte.getBaseMetaTileEntity()
                    .getMetaTileID(),
                i++);
        }

        return new IStructureElement<>() {

            private TMTE getMTE(World world, int x, int y, int z) {
                if (!(world.getTileEntity(x, y, z) instanceof IGregTechTileEntity igte)) return null;

                IMetaTileEntity imte = igte.getMetaTileEntity();

                if (!(mteClass.isAssignableFrom(imte.getClass()))) return null;

                return mteClass.cast(imte);
            }

            private TMTE getPlaceable(ItemStack trigger) {
                int index = GTUtility.clamp(trigger.stackSize, 1, tiers.size());

                return GTDataUtils.getIndexSafe(tiers, index - 1);
            }

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TMTE mte = getMTE(world, x, y, z);

                if (mte == null) return false;

                int tier = tierMap.getOrDefault(
                    mte.getBaseMetaTileEntity()
                        .getMetaTileID(),
                    -1);

                if (tier == -1) return false;

                return adder.check(t, mte, tier);
            }

            @Override
            public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
                TMTE wanted = getPlaceable(trigger);
                TMTE actual = getMTE(world, x, y, z);

                if (actual == null) return false;

                int tier = tierMap.getOrDefault(
                    actual.getBaseMetaTileEntity()
                        .getMetaTileID(),
                    -1);

                if (tier == -1) return false;

                return tiers.get(tier) != wanted;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, GregTechAPI.sBlockMachines, 0);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                TMTE mte = getPlaceable(trigger);

                ItemStack stack = mte.getStackForm(1);

                if (!(stack.getItem() instanceof ItemMachines itemMachines)) return false;

                boolean success = itemMachines
                    .placeBlockAt(stack, null, world, x, y, z, ForgeDirection.UP.ordinal(), 0.5f, 0.5f, 0.5f, 0);

                if (!success) return false;

                if (world.getTileEntity(x, y, z) instanceof ITurnable turnable) {
                    turnable.setFrontFacing(ForgeDirection.SOUTH);
                }

                return true;
            }

            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {

                int index = GTUtility.clamp(trigger.stackSize, 0, tiers.size() - 1);
                TMTE mte = GTDataUtils.getIndexSafe(tiers, index);

                return BlocksToPlace.create(mte.getStackForm(1));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                AutoPlaceEnvironment env) {

                TMTE wanted = getPlaceable(trigger);
                TMTE actual = getMTE(world, x, y, z);

                if (actual == wanted) return PlaceResult.SKIP;

                if (!StructureLibAPI.isBlockTriviallyReplaceable(world, x, y, z, env.getActor())) {
                    return PlaceResult.REJECT;
                }

                ItemStack stack = wanted.getStackForm(1);

                PlaceResult result = StructureUtility.survivalPlaceBlock(
                    stack,
                    EXACT,
                    null,
                    false,
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());

                if (result != ACCEPT) return result;

                if (world.getTileEntity(x, y, z) instanceof ITurnable turnable) {
                    turnable.setFrontFacing(ForgeDirection.SOUTH);
                }

                return result;
            }
        };
    }

    /**
     * Just a structure element that proxies its operations to another one. Useful for overriding or hooking into
     * specific operations while keeping the rest unchanged.
     */
    public static class ProxyStructureElement<T, E extends IStructureElement<T>> implements IStructureElement<T> {

        public final E proxiedElement;

        public ProxyStructureElement(E proxiedElement) {
            this.proxiedElement = proxiedElement;
        }

        @Override
        public boolean check(T t, World world, int x, int y, int z) {
            return proxiedElement.check(t, world, x, y, z);
        }

        @Override
        public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
            return proxiedElement.couldBeValid(t, world, x, y, z, trigger);
        }

        @Override
        public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
            return proxiedElement.spawnHint(t, world, x, y, z, trigger);
        }

        @Override
        public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
            return proxiedElement.placeBlock(t, world, x, y, z, trigger);
        }

        @Override
        public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s,
            EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
            return proxiedElement.survivalPlaceBlock(t, world, x, y, z, trigger, s, actor, chatter);
        }

        @Override
        public @Nullable BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            return proxiedElement.getBlocksToPlace(t, world, x, y, z, trigger, env);
        }

        @Override
        public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
            return proxiedElement.survivalPlaceBlock(t, world, x, y, z, trigger, env);
        }

        @Override
        public IStructureElementNoPlacement<T> noPlacement() {
            return proxiedElement.noPlacement();
        }

        @Override
        public int getStepA() {
            return proxiedElement.getStepA();
        }

        @Override
        public int getStepB() {
            return proxiedElement.getStepB();
        }

        @Override
        public int getStepC() {
            return proxiedElement.getStepC();
        }

        @Override
        public boolean resetA() {
            return proxiedElement.resetA();
        }

        @Override
        public boolean resetB() {
            return proxiedElement.resetB();
        }

        @Override
        public boolean resetC() {
            return proxiedElement.resetC();
        }

        @Override
        public boolean isNavigating() {
            return proxiedElement.isNavigating();
        }
    }

    /**
     * Just a hatch element that proxies its operations to another one. Useful for overriding or hooking into specific
     * operations while keeping the rest unchanged.
     */
    public static class ProxyHatchElement<T> implements IHatchElement<T> {

        public final IHatchElement<? super T> proxiedHatch;

        public ProxyHatchElement(IHatchElement<? super T> proxiedHatch) {
            this.proxiedHatch = proxiedHatch;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return proxiedHatch.mteClasses();
        }

        @Override
        public IGTHatchAdder<? super T> adder() {
            return proxiedHatch.adder();
        }

        @Override
        public String name() {
            return proxiedHatch.name();
        }

        @Override
        public String getDisplayName() {
            return proxiedHatch.getDisplayName();
        }

        @Override
        public long count(T t) {
            return proxiedHatch.count(t);
        }
    }
}
