package gregtech.api.multitileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.base.MultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

public class MultiTileEntityRegistry {

    private static final HashMap<String, MultiTileEntityRegistry> NAMED_REGISTRIES = new HashMap<>();

    // TODO: NBT sensitive or not? Starting with not for now
    private static final ItemStackMap<MultiTileEntityRegistry> REGISTRIES = new ItemStackMap<>(false);
    private static final HashSet<Class<?>> sRegisteredTileEntities = new HashSet<>();

    public HashMap<Short, CreativeTab> mCreativeTabs = new HashMap<>();
    public Map<Short, MultiTileEntityClassContainer> mRegistry = new HashMap<>();
    public List<MultiTileEntityClassContainer> mRegistrations = new ArrayList<>();

    public final String mNameInternal;
    public final MultiTileEntityBlockInternal mBlock;

    private static MultiTileEntityBlockInternal regblock(String aNameInternal, MultiTileEntityBlockInternal aBlock,
        Class<? extends ItemBlock> aItemClass) {
        GameRegistry.registerBlock(aBlock, aItemClass == null ? ItemBlock.class : aItemClass, aNameInternal);
        return aBlock;
    }

    /**
     * @param aNameInternal the internal Name of the Item
     */
    public MultiTileEntityRegistry(String aNameInternal) {
        this(aNameInternal, new MultiTileEntityBlockInternal(), MultiTileEntityItemInternal.class);
    }

    /**
     * @param aNameInternal the internal Name of the Item
     */
    public MultiTileEntityRegistry(String aNameInternal, MultiTileEntityBlockInternal aBlock,
        Class<? extends ItemBlock> aItemClass) {
        this(aNameInternal, regblock(aNameInternal, aBlock, aItemClass));
    }

    /**
     * @param aNameInternal the internal Name of the Item
     */
    public MultiTileEntityRegistry(String aNameInternal, MultiTileEntityBlockInternal aBlock) {
        if (!GregTech_API.sPreloadStarted || GregTech_API.sPreloadFinished) throw new IllegalStateException(
            "The MultiTileEntity Registry must be initialised during Preload Phase and not before");
        mNameInternal = aNameInternal;
        mBlock = aBlock;
        GT_FML_LOGGER.info(aNameInternal + " " + Block.getIdFromBlock(aBlock) + "This is the answer");
        mBlock.mMultiTileEntityRegistry = this;
        REGISTRIES.put(new ItemStack(Item.getItemFromBlock(aBlock), 1, GT_Values.W), this);
        NAMED_REGISTRIES.put(mNameInternal, this);
    }

    public static TileEntity getCanonicalTileEntity(int aRegistryID, int aMultiTileEntityID) {
        final MultiTileEntityRegistry tRegistry = getRegistry(aRegistryID);
        if (tRegistry == null) return null;
        final MultiTileEntityClassContainer tClassContainer = tRegistry.getClassContainer(aMultiTileEntityID);
        if (tClassContainer == null) return null;
        return tClassContainer.mCanonicalTileEntity;
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
    public ItemStack add(String aLocalised, String aCategoricalName, MultiTileEntityClassContainer aClassContainer) {
        boolean tFailed = false;
        if (GT_Utility.isStringInvalid(aLocalised)) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Localisation Missing!");
            tFailed = true;
        }
        if (aClassContainer == null) {
            GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container is null!");
            tFailed = true;
        } else {
            if (aClassContainer.mClass == null) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class inside Class Container is null!");
                tFailed = true;
            }
            if (aClassContainer.mID == GT_Values.W) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses Wildcard MetaData!");
                tFailed = true;
            }
            if (aClassContainer.mID < 0) {
                GT_FML_LOGGER.error("MULTI-TILE REGISTRY ERROR: Class Container uses negative MetaData!");
                tFailed = true;
            }
            if (mRegistry.containsKey(aClassContainer.mID)) {
                GT_FML_LOGGER.error(
                    "MULTI-TILE REGISTRY ERROR: Class Container uses occupied MetaData! (" + aClassContainer.mID + ")");
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

        GT_LanguageManager
            .addStringLocalization(mNameInternal + "." + aClassContainer.mID + ".name", aLocalised, false);
        mRegistry.put(aClassContainer.mID, aClassContainer);
        mLastRegisteredID = aClassContainer.mID;
        mRegistrations.add(aClassContainer);

        if (sRegisteredTileEntities.add(aClassContainer.mCanonicalTileEntity.getClass())) {
            aClassContainer.mCanonicalTileEntity.onRegistrationFirst(this, aClassContainer.mID);
        }
        // // TODO: Recipe
        // if (aRecipe != null && aRecipe.length > 1) {
        // if (aRecipe[0] instanceof Object[]) aRecipe = (Object[])aRecipe[0];
        // if (aRecipe.length > 2) CR.shaped(getItem(aClassContainer.mID), CR.DEF_REV_NCC, aRecipe);
        // }
        // // A simple special case to make it easier to add a Machine to Recipe Lists without having to worry
        // about anything.
        // String tRecipeMapName = aClassContainer.mParameters.getString(NBT_RECIPEMAP);
        // if (GT_Utility.isStringValid(tRecipeMapName)) {RecipeMap tMap =
        // RecipeMap.RECIPE_MAPS.get(tRecipeMapName); if (tMap != null)
        // tMap.mRecipeMachineList.add(getItem(aClassContainer.mID));}
        // tRecipeMapName = aClassContainer.mParameters.getString(NBT_FUELMAP);
        // if (GT_Utility.isStringValid(tRecipeMapName)) {RecipeMap tMap =
        // RecipeMap.RECIPE_MAPS.get(tRecipeMapName); if (tMap != null)
        // tMap.mRecipeMachineList.add(getItem(aClassContainer.mID));}
        //
        return getItem(aClassContainer.mID);
    }

    public short mLastRegisteredID = GT_Values.W;

    public ItemStack getItem() {
        return getItem(mLastRegisteredID, 1, null);
    }

    public ItemStack getItem(int aID) {
        return getItem(aID, 1, null);
    }

    public ItemStack getItem(int aID, NBTTagCompound aNBT) {
        return getItem(aID, 1, aNBT);
    }

    public ItemStack getItem(int aID, long aAmount) {
        return getItem(aID, aAmount, null);
    }

    public ItemStack getItem(int aID, long aAmount, NBTTagCompound aNBT) {
        final ItemStack rStack = new ItemStack(mBlock, (int) aAmount, aID);
        if (aNBT == null || aNBT.hasNoTags()) {
            aNBT = new NBTTagCompound();
            final MultiTileEntityContainer tTileEntityContainer = getNewTileEntityContainer(aID, aNBT);
            if (tTileEntityContainer != null) ((IMultiTileEntity) tTileEntityContainer.mTileEntity).writeItemNBT(aNBT);
        }
        rStack.setTagCompound(aNBT);
        return rStack;
    }

    public String getLocal(int aID) {
        return StatCollector.translateToLocal(mNameInternal + "." + aID + ".name");
    }

    public MultiTileEntityClassContainer getClassContainer(int aID) {
        return mRegistry.get((short) aID);
    }

    public MultiTileEntityClassContainer getClassContainer(ItemStack aStack) {
        return mRegistry.get((short) Items.feather.getDamage(aStack));
    }

    public TileEntity getNewTileEntity(int aID) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(null, 0, 0, 0, aID, null);
        return tContainer == null ? null : tContainer.mTileEntity;
    }

    public MultiTileEntityContainer getNewTileEntityContainer(World aWorld, int aX, int aY, int aZ, int aID,
        NBTTagCompound aNBT) {
        final MultiTileEntityClassContainer tClass = mRegistry.get((short) aID);
        if (tClass == null || tClass.mBlock == null) return null;
        final MultiTileEntityContainer rContainer = new MultiTileEntityContainer(
            (TileEntity) GT_Utility.callConstructor(tClass.mClass, -1, null, true),
            tClass.mBlock,
            tClass.mBlockMetaData);
        if (rContainer.mTileEntity == null) return null;
        rContainer.mTileEntity.setWorldObj(aWorld);
        rContainer.mTileEntity.xCoord = aX;
        rContainer.mTileEntity.yCoord = aY;
        rContainer.mTileEntity.zCoord = aZ;
        ((IMultiTileEntity) rContainer.mTileEntity).initFromNBT(
            aNBT == null || aNBT.hasNoTags() ? tClass.mParameters : GT_Util.fuseNBT(aNBT, tClass.mParameters),
            (short) aID,
            (short) Block.getIdFromBlock(mBlock));
        return rContainer;
    }

    public TileEntity getNewTileEntity(World aWorld, int aX, int aY, int aZ, int aID) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(aWorld, aX, aY, aZ, aID, null);
        return tContainer == null ? null : tContainer.mTileEntity;
    }

    public TileEntity getNewTileEntity(ItemStack aStack) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(
            null,
            0,
            0,
            0,
            Items.feather.getDamage(aStack),
            aStack.getTagCompound());
        return tContainer == null ? null : tContainer.mTileEntity;
    }

    public TileEntity getNewTileEntity(World aWorld, int aX, int aY, int aZ, ItemStack aStack) {
        final MultiTileEntityContainer tContainer = getNewTileEntityContainer(
            aWorld,
            aX,
            aY,
            aZ,
            Items.feather.getDamage(aStack),
            aStack.getTagCompound());
        return tContainer == null ? null : tContainer.mTileEntity;
    }

    public MultiTileEntityContainer getNewTileEntityContainer(ItemStack aStack) {
        return getNewTileEntityContainer(null, 0, 0, 0, Items.feather.getDamage(aStack), aStack.getTagCompound());
    }

    public MultiTileEntityContainer getNewTileEntityContainer(World aWorld, int aX, int aY, int aZ, ItemStack aStack) {
        return getNewTileEntityContainer(aWorld, aX, aY, aZ, Items.feather.getDamage(aStack), aStack.getTagCompound());
    }

    public MultiTileEntityContainer getNewTileEntityContainer(int aID, NBTTagCompound aNBT) {
        return getNewTileEntityContainer(null, 0, 0, 0, aID, aNBT);
    }
}
