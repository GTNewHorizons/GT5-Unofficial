package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.core.CreativeTab;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.util.GT_Utility;

public class MultiTileEntityRegistry {

    private static final HashMap<String, MultiTileEntityRegistry> NAMED_REGISTRIES = new HashMap<>();

    // TODO: NBT sensitive or not? Starting with not for now
    private static final ItemStackMap<MultiTileEntityRegistry> REGISTRIES = new ItemStackMap<>(false);
    private static final HashSet<Class<?>> registeredTileEntities = new HashSet<>();

    public HashMap<Short, CreativeTab> mCreativeTabs = new HashMap<>();
    public Map<Integer, MultiTileEntityClassContainer> registry = new HashMap<>();
    public List<MultiTileEntityClassContainer> registrations = new ArrayList<>();

    public final String internalName;
    public final MultiTileEntityBlock block;

    private static MultiTileEntityBlock regblock(String internalName, MultiTileEntityBlock block,
        Class<? extends ItemBlock> itemClass) {
        GameRegistry.registerBlock(block, itemClass == null ? ItemBlock.class : itemClass, internalName);
        return block;
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName) {
        this(internalName, new MultiTileEntityBlock(), MultiTileEntityItem.class);
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName, MultiTileEntityBlock block,
        Class<? extends ItemBlock> itemClass) {
        this(internalName, regblock(internalName, block, itemClass));
    }

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName, MultiTileEntityBlock block) {
        if (!Loader.instance()
            .isInState(LoaderState.PREINITIALIZATION)) {
            throw new IllegalStateException(
                "The MultiTileEntity Registry must be initialized during Preload Phase and not before");
        }
        this.internalName = internalName;
        this.block = block;
        GT_FML_LOGGER.info(internalName + " " + Block.getIdFromBlock(block) + "This is the answer");
        this.block.setRegistry(this);
        REGISTRIES.put(new ItemStack(Item.getItemById(Block.getIdFromBlock(block)), 1, GT_Values.W), this);
        NAMED_REGISTRIES.put(internalName, this);
    }

    public static TileEntity getCachedTileEntity(int registryId, int metaId) {
        final MultiTileEntityRegistry registry = getRegistry(registryId);
        if (registry == null) return null;
        return registry.getCachedTileEntity(metaId);
    }

    public static MultiTileEntityRegistry getRegistry(int aRegistryID) {
        return REGISTRIES.get(new ItemStack(Item.getItemById(aRegistryID), 1, GT_Values.W));
    }

    public static MultiTileEntityRegistry getRegistry(String aRegistryName) {
        return NAMED_REGISTRIES.get(aRegistryName);
    }

    public MultiTileEntityClassContainer create(int metaId, Class<? extends MultiTileEntity> clazz) {
        return new MultiTileEntityClassContainer(this, block, metaId, clazz);
    }

    /**
     * Adds a new MultiTileEntity. It is highly recommended to do this in either the PreInit or the Init Phase. PostInit
     * might not work well.
     */
    public ItemStack add(String categoryName, MultiTileEntityClassContainer classContainer) {
        boolean tFailed = false;
        if (classContainer == null) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container is null!");
            tFailed = true;
        } else {
            if (classContainer.getClazz() == null) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class inside Class Container is null!");
                tFailed = true;
            }
            if (classContainer.getMetaId() == GT_Values.W) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses Wildcard MetaData!");
                tFailed = true;
            }
            if (classContainer.getMetaId() < 0) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses negative MetaData!");
                tFailed = true;
            }
            if (registry.containsKey(classContainer.getMetaId())) {
                GT_FML_LOGGER.error(
                    "MULTI-TILE REGISTRY ERROR: Class Container uses occupied MetaData! (" + classContainer.getMetaId()
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

        registry.put(classContainer.getMetaId(), classContainer);
        registrations.add(classContainer);
        lastRegisteredId = classContainer.getMetaId();

        if (registeredTileEntities.add(classContainer.getClazz())) {
            GameRegistry.registerTileEntity(
                classContainer.getClazz(),
                classContainer.getOriginalTileEntity()
                    .getTileEntityName());
        }
        return getItem(classContainer.getMetaId());
    }

    public int lastRegisteredId = GT_Values.W;

    public ItemStack getItem(int aID) {
        return getItem(aID, 1, null);
    }

    public ItemStack getItem(int aID, NBTTagCompound aNBT) {
        return getItem(aID, 1, aNBT);
    }

    public ItemStack getItem(int aID, long aAmount) {
        return getItem(aID, aAmount, null);
    }

    public ItemStack getItem(int metaId, long amount, NBTTagCompound nbt) {
        final ItemStack stack = new ItemStack(block, (int) amount, metaId);
        if (nbt == null || nbt.hasNoTags()) {
            nbt = new NBTTagCompound();

        }
        stack.setTagCompound(nbt);
        return stack;
    }

    public String getLocal(int metaId) {
        return StatCollector.translateToLocal(internalName + "." + metaId + ".name");
    }

    public MultiTileEntity getCachedTileEntity(ItemStack stack) {
        return getCachedTileEntity(Items.feather.getDamage(stack));
    }

    public MultiTileEntity getCachedTileEntity(int metaId) {
        MultiTileEntityClassContainer clazz = registry.get(metaId);
        if (clazz == null) return null;
        return clazz.getOriginalTileEntity();
    }

    public MultiTileEntity getNewTileEntity(@Nonnull final ItemStack stack) {
        return getNewTileEntity(Items.feather.getDamage(stack));
    }

    public MultiTileEntity getNewTileEntity(final int metaId) {
        MultiTileEntityClassContainer clazz = registry.get(metaId);
        if (clazz == null) return null;
        MultiTileEntity te = (MultiTileEntity) GT_Utility.callConstructor(clazz.getClazz(), 0, null, true);
        te.initFromNBT(clazz.getParameters(), Block.getIdFromBlock(block), metaId);
        return te;
    }

    public MultiTileEntityClassContainer getClassContainer(int metaId) {
        return registry.get(metaId);
    }

    public MultiTileEntityBlock getBlock() {
        return block;
    }
}
