package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.core.CreativeTab;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class MultiTileEntityRegistry {

    private static final HashMap<String, MultiTileEntityRegistry> NAMED_REGISTRIES = new HashMap<>();

    // TODO: NBT sensitive or not? Starting with not for now
    private static final ItemStackMap<MultiTileEntityRegistry> REGISTRIES = new ItemStackMap<>(false);
    private static final HashSet<Class<?>> sRegisteredTileEntities = new HashSet<>();
    private final HashMap<Integer, MultiTileEntityContainer> cachedTileEntityContainers = new HashMap<>();

    public HashMap<Short, CreativeTab> creativeTabs = new HashMap<>();
    public final Short2ObjectMap<MultiTileEntityClassContainer> registry = new Short2ObjectOpenHashMap<>();
    public List<MultiTileEntityClassContainer> registrations = new ArrayList<>();

    private final String internalName;
    private final MultiTileEntityBlockRegistryInternal block;

    private static MultiTileEntityBlockRegistryInternal regblock(String internalName,
        MultiTileEntityBlockRegistryInternal block, Class<? extends ItemBlock> itemClass) {
        GameRegistry.registerBlock(block, itemClass == null ? ItemBlock.class : itemClass, internalName);
        return block;
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName) {
        this(internalName, new MultiTileEntityBlockRegistryInternal(), MultiTileEntityItem.class);
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName, MultiTileEntityBlockRegistryInternal block,
        Class<? extends ItemBlock> itemClass) {
        this(internalName, regblock(internalName, block, itemClass));
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName, MultiTileEntityBlockRegistryInternal block) {
        if (!Loader.instance()
            .isInState(LoaderState.PREINITIALIZATION)) {
            throw new IllegalStateException(
                "The MultiTileEntity Registry must be initialized during Preload Phase and not before");
        }
        this.internalName = internalName;
        this.block = block;
        GT_FML_LOGGER.info(internalName + " " + Block.getIdFromBlock(block) + "This is the answer");
        this.block.registry = this;
        REGISTRIES.put(new ItemStack(Item.getItemById(Block.getIdFromBlock(block)), 1, GT_Values.W), this);
        NAMED_REGISTRIES.put(internalName, this);
    }

    public static TileEntity getCanonicalTileEntity(int aRegistryID, int aMultiTileEntityID) {
        final MultiTileEntityRegistry tRegistry = getRegistry(aRegistryID);
        if (tRegistry == null) return null;
        final MultiTileEntityClassContainer tClassContainer = tRegistry.getClassContainer(aMultiTileEntityID);
        if (tClassContainer == null) return null;
        return tClassContainer.referenceTileEntity;
    }

    public static MultiTileEntityRegistry getRegistry(int aRegistryID) {
        return REGISTRIES.get(new ItemStack(Item.getItemById(aRegistryID), 1, GT_Values.W));
    }

    public static MultiTileEntityRegistry getRegistry(String aRegistryName) {
        return NAMED_REGISTRIES.get(aRegistryName);
    }

    public MultiTileEntityClassContainer create(int aID, Class<? extends MultiTileEntity> aClass) {
        return new MultiTileEntityClassContainer(this, aID, aClass);
    }

    /**
     * Adds a new MultiTileEntity. It is highly recommended to do this in either the PreInit or the Init Phase. PostInit
     * might not work well.
     */
    public ItemStack add(String aLocalised, MultiTileEntityClassContainer aClassContainer) {
        boolean tFailed = false;
        if (GT_Utility.isStringInvalid(aLocalised)) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Localisation Missing!");
            tFailed = true;
        }
        if (aClassContainer == null) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container is null!");
            tFailed = true;
        } else {
            if (aClassContainer.muteClass == null) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class inside Class Container is null!");
                tFailed = true;
            }
            if (aClassContainer.muteID == GT_Values.W) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses Wildcard MetaData!");
                tFailed = true;
            }
            if (aClassContainer.muteID < 0) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses negative MetaData!");
                tFailed = true;
            }
            if (registry.containsKey(aClassContainer.muteID)) {
                GT_FML_LOGGER.error(
                    "MULTI-TILE REGISTRY ERROR: Class Container uses occupied MetaData! (" + aClassContainer.muteID
                        + ")");
                tFailed = true;
            }
        }
        if (tFailed) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: STACKTRACE START");
            int i = 0;
            for (StackTraceElement tElement : new Exception().getStackTrace()) if (i++ < 5 && !tElement.getClassName()
                .startsWith("sun")) GT_FML_LOGGER.error("\tat " + tElement);
            else break;
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: STACKTRACE END");
            return null;
        }

        GT_LanguageManager.addStringLocalization(internalName + "." + aClassContainer.muteID + ".name", aLocalised);
        registry.put(aClassContainer.muteID, aClassContainer);
        mLastRegisteredID = aClassContainer.muteID;
        registrations.add(aClassContainer);

        if (sRegisteredTileEntities.add(aClassContainer.referenceTileEntity.getClass())) {
            aClassContainer.referenceTileEntity.onRegistrationFirst(this, aClassContainer.muteID);
        }

        return getItem(aClassContainer.muteID);
    }

    public short mLastRegisteredID = GT_Values.W;

    public ItemStack getItem() {
        return getItem(mLastRegisteredID, 1, null);
    }

    public ItemStack getItem(int aID) {
        return getItem(aID, 1, null);
    }

    public ItemStack getItem(int aID, NBTTagCompound nbt) {
        return getItem(aID, 1, nbt);
    }

    public ItemStack getItem(int aID, long aAmount) {
        return getItem(aID, aAmount, null);
    }

    public ItemStack getItem(int aID, long aAmount, NBTTagCompound nbt) {
        final ItemStack rStack = new ItemStack(block, (int) aAmount, aID);
        if (nbt == null || nbt.hasNoTags()) {
            nbt = new NBTTagCompound();
            final MultiTileEntityContainer tTileEntityContainer = getNewTileEntityContainer(aID, nbt);
            if (tTileEntityContainer != null) ((IMultiTileEntity) tTileEntityContainer.tileEntity).writeItemNBT(nbt);
        }
        rStack.setTagCompound(nbt);
        return rStack;
    }

    public String getLocal(int aID) {
        return StatCollector.translateToLocal(internalName + "." + aID + ".name");
    }

    public MultiTileEntityClassContainer getClassContainer(int aID) {
        return registry.get((short) aID);
    }

    public MultiTileEntityClassContainer getClassContainer(ItemStack stack) {
        return registry.get((short) Items.feather.getDamage(stack));
    }

    public TileEntity getNewTileEntity(int aID) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(null, 0, 0, 0, aID, null);
        return tContainer == null ? null : tContainer.tileEntity;
    }

    public MultiTileEntityContainer getNewTileEntityContainer(World world, int x, int y, int z, int aID,
        NBTTagCompound nbt) {
        final MultiTileEntityClassContainer tClass = registry.get((short) aID);
        if (tClass == null || tClass.block == null) return null;
        final MultiTileEntityContainer container = new MultiTileEntityContainer(
            (MultiTileEntity) GT_Utility.callConstructor(tClass.muteClass, -1, null, true),
            tClass.block,
            tClass.blockMetaData);
        if (container.tileEntity == null) return null;
        final MultiTileEntity tileEntity = container.tileEntity;
        tileEntity.setWorldObj(world);
        tileEntity.xCoord = x;
        tileEntity.yCoord = y;
        tileEntity.zCoord = z;
        tileEntity.initFromNBT(
            nbt == null || nbt.hasNoTags() ? tClass.parameters : GT_Util.fuseNBT(nbt, tClass.parameters),
            (short) aID,
            (short) Block.getIdFromBlock(block));
        return container;
    }

    public TileEntity getNewTileEntity(World world, int x, int y, int z, int aID) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(world, x, y, z, aID, null);
        return tContainer == null ? null : tContainer.tileEntity;
    }

    public TileEntity getNewTileEntity(ItemStack stack) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(
            null,
            0,
            0,
            0,
            Items.feather.getDamage(stack),
            stack.getTagCompound());
        return tContainer == null ? null : tContainer.tileEntity;
    }

    public TileEntity getNewTileEntity(World world, int x, int y, int z, ItemStack stack) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(
            world,
            x,
            y,
            z,
            Items.feather.getDamage(stack),
            stack.getTagCompound());
        return tContainer == null ? null : tContainer.tileEntity;
    }

    public MultiTileEntityContainer getCachedTileEntityContainer(ItemStack stack) {
        MultiTileEntityContainer container = cachedTileEntityContainers.get(Items.feather.getDamage(stack));
        if (container == null) {
            container = getNewTileEntityContainer(stack);
            cachedTileEntityContainers.put(Items.feather.getDamage(stack), container);
        }
        return container;
    }

    public MultiTileEntityContainer getNewTileEntityContainer(ItemStack stack) {
        return getNewTileEntityContainer(null, 0, 0, 0, Items.feather.getDamage(stack), stack.getTagCompound());
    }

    public MultiTileEntityContainer getNewTileEntityContainer(World world, int x, int y, int z, ItemStack stack) {
        return getNewTileEntityContainer(world, x, y, z, Items.feather.getDamage(stack), stack.getTagCompound());
    }

    public MultiTileEntityContainer getNewTileEntityContainer(int aID, NBTTagCompound nbt) {
        return getNewTileEntityContainer(null, 0, 0, 0, aID, nbt);
    }

    public String getInternalName() {
        return internalName;
    }

    public MultiTileEntityBlockRegistryInternal getBlock() {
        return block;
    }
}
