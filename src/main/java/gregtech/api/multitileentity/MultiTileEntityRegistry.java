package gregtech.api.multitileentity;

import static gregtech.GTMod.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.core.CreativeTab;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import gregtech.api.enums.GTValues;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class MultiTileEntityRegistry {

    private static final HashMap<String, MultiTileEntityRegistry> NAMED_REGISTRIES = new HashMap<>();

    // TODO: NBT sensitive or not? Starting with not for now
    private static final ItemStackMap<MultiTileEntityRegistry> REGISTRIES = new ItemStackMap<>(false);
    private static final HashSet<Class<?>> sRegisteredTileEntities = new HashSet<>();

    public HashMap<Short, CreativeTab> creativeTabs = new HashMap<>();
    public final Short2ObjectMap<MultiTileEntityClassContainer> registry = new Short2ObjectOpenHashMap<>();
    public List<MultiTileEntityClassContainer> registrations = new ArrayList<>();

    private final String internalName;
    private final MultiTileEntityBlock block;

    /**
     * @param internalName the internal Name of the Item
     */
    public MultiTileEntityRegistry(String internalName, MultiTileEntityBlock block) {
        if (!Loader.instance()
            .isInState(LoaderState.PREINITIALIZATION)) {
            throw new IllegalStateException(
                "The MultiTileEntity Registry must be initialized during Preload Phase and not before");
        }
        if (!block.isRegistered()) {
            throw new IllegalStateException("Block not registered");
        }
        this.internalName = internalName;
        this.block = block;
        GT_FML_LOGGER.info(internalName + " " + Block.getIdFromBlock(block) + " This is the answer");
        this.block.setRegistry(this);
        REGISTRIES.put(new ItemStack(Item.getItemById(Block.getIdFromBlock(block)), 1, GTValues.W), this);
        NAMED_REGISTRIES.put(internalName, this);
    }

    public static TileEntity getReferenceTileEntity(int aRegistryID, int aMultiTileEntityID) {
        final MultiTileEntityRegistry tRegistry = getRegistry(aRegistryID);
        if (tRegistry == null) return null;
        final MultiTileEntityClassContainer tClassContainer = tRegistry.getClassContainer(aMultiTileEntityID);
        if (tClassContainer == null) return null;
        return tClassContainer.getReferenceTileEntity();
    }

    public MultiTileEntity getReferenceTileEntity(ItemStack stack) {
        return getReferenceTileEntity(Items.feather.getDamage(stack));
    }

    public MultiTileEntity getReferenceTileEntity(int metaId) {
        final MultiTileEntityClassContainer muteClass = registry.get((short) metaId);
        if (muteClass == null) return null;
        return muteClass.getReferenceTileEntity();
    }

    public static MultiTileEntityRegistry getRegistry(int aRegistryID) {
        return REGISTRIES.get(new ItemStack(Item.getItemById(aRegistryID), 1, GTValues.W));
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
        if (GTUtility.isStringInvalid(aLocalised)) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Localisation Missing!");
            tFailed = true;
        }
        if (aClassContainer == null) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container is null!");
            tFailed = true;
        } else {
            if (aClassContainer.getMuteClass() == null) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class inside Class Container is null!");
                tFailed = true;
            }
            if (aClassContainer.getMuteID() == GTValues.W) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses Wildcard MetaData!");
                tFailed = true;
            }
            if (aClassContainer.getMuteID() < 0) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses negative MetaData!");
                tFailed = true;
            }
            if (registry.containsKey(aClassContainer.getMuteID())) {
                GT_FML_LOGGER.error(
                    "MULTI-TILE REGISTRY ERROR: Class Container uses occupied MetaData! (" + aClassContainer.getMuteID()
                        + ")");
                tFailed = true;
            }
        }
        if (tFailed) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR " + aLocalised + " : STACKTRACE START");
            int i = 0;
            for (StackTraceElement tElement : new Exception().getStackTrace()) if (i++ < 5 && !tElement.getClassName()
                .startsWith("sun")) GT_FML_LOGGER.error("\tat " + tElement);
            else break;
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: STACKTRACE END");
            return null;
        }

        GTLanguageManager.addStringLocalization(internalName + "." + aClassContainer.getMuteID() + ".name", aLocalised);
        registry.put(aClassContainer.getMuteID(), aClassContainer);
        mLastRegisteredID = aClassContainer.getMuteID();
        registrations.add(aClassContainer);

        if (sRegisteredTileEntities.add(
            aClassContainer.getReferenceTileEntity()
                .getClass())) {
            aClassContainer.getReferenceTileEntity()
                .onRegistrationFirst(this, aClassContainer.getMuteID());
        }

        return getItem(aClassContainer.getMuteID());
    }

    public short mLastRegisteredID = GTValues.W;

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

    public ItemStack getItem(int metaID, long amount, NBTTagCompound nbt) {
        final ItemStack stack = new ItemStack(block, (int) amount, metaID);
        if (nbt == null || nbt.hasNoTags()) {
            nbt = new NBTTagCompound();
            final TileEntity tileEntity = getNewTileEntity(metaID, nbt);
            ((IMultiTileEntity) tileEntity).writeItemNBT(nbt);
        }
        stack.setTagCompound(nbt);
        return stack;
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
        return getNewTileEntity(null, 0, 0, 0, aID);
    }

    public TileEntity getNewTileEntity(World aWorld, int x, int y, int z, int metaID) {
        return getNewTileEntity(aWorld, x, y, z, metaID, null);
    }

    public TileEntity getNewTileEntity(World aWorld, int x, int y, int z, int metaID, NBTTagCompound nbt) {
        final MultiTileEntityClassContainer container = registry.get((short) metaID);
        if (container == null) return null;
        final MultiTileEntity te = (MultiTileEntity) GTUtility
            .callConstructor(container.getMuteClass(), -1, null, true);
        te.setWorldObj(aWorld);
        te.xCoord = x;
        te.yCoord = y;
        te.zCoord = z;
        nbt = (nbt == null || nbt.hasNoTags()) ? container.getParameters()
            : GTUtil.fuseNBT(nbt, container.getParameters());
        te.initFromNBT(nbt, (short) metaID, (short) Block.getIdFromBlock(block));
        return te;
    }

    public TileEntity getNewTileEntity(int meta, NBTTagCompound nbt) {
        return getNewTileEntity(null, 0, 0, 0, meta, nbt);
    }

    public String getInternalName() {
        return internalName;
    }

    public MultiTileEntityBlock getBlock() {
        return block;
    }
}
