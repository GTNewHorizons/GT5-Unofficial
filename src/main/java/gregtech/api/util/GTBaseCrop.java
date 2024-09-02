package gregtech.api.util;

import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.Mods.IC2CropPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.crops.ICropTile;
import speiger.src.crops.api.ICropCardInfo;

public class GTBaseCrop extends CropCard implements ICropCardInfo {

    public static ArrayList<GTBaseCrop> sCropList = new ArrayList<>();
    private String mName = E;
    private String mDiscoveredBy = "Gregorius Techneticies";
    private String[] mAttributes;
    private int mTier = 0;
    private int mMaxSize = 0;
    private int mAfterHarvestSize = 0;
    private int mHarvestSize = 0;
    private final int[] mStats = new int[5];
    private final int mGrowthSpeed = 0;
    private ItemStack mDrop = null;
    private ItemStack[] mSpecialDrops = null;
    private Materials mBlock = null;
    private static boolean bIc2NeiLoaded = IC2CropPlugin.isModLoaded();

    /**
     * To create new Crops
     *
     * @param aID           Default ID
     * @param aCropName     Name of the Crop
     * @param aDiscoveredBy The one who discovered the Crop
     * @param aDrop         The Item which is dropped by the Crop. must be != null
     * @param aBaseSeed     Baseseed to plant this Crop. null == crossbreed only
     * @param aTier         tier of the Crop. forced to be >= 1
     * @param aMaxSize      maximum Size of the Crop. forced to be >= 3
     * @param aGrowthSpeed  how fast the Crop grows. if < 0 then its set to Tier*300
     * @param aHarvestSize  the size the Crop needs to be harvested. forced to be between 2 and max size
     */
    public GTBaseCrop(int aID, String aCropName, String aDiscoveredBy, ItemStack aBaseSeed, int aTier, int aMaxSize,
        int aGrowthSpeed, int aAfterHarvestSize, int aHarvestSize, int aStatChemical, int aStatFood, int aStatDefensive,
        int aStatColor, int aStatWeed, String[] aAttributes, ItemStack aDrop, ItemStack[] aSpecialDrops) {
        new GTBaseCrop(
            aID,
            aCropName,
            aDiscoveredBy,
            aBaseSeed,
            aTier,
            aMaxSize,
            aGrowthSpeed,
            aAfterHarvestSize,
            aHarvestSize,
            aStatChemical,
            aStatFood,
            aStatDefensive,
            aStatColor,
            aStatWeed,
            aAttributes,
            null,
            aDrop,
            aSpecialDrops);
    }

    /**
     * To create new Crops
     *
     * @param aID           Default ID
     * @param aCropName     Name of the Crop
     * @param aDiscoveredBy The one who discovered the Crop
     * @param aDrop         The Item which is dropped by the Crop. must be != null
     * @param aBaseSeed     Baseseed to plant this Crop. null == crossbreed only
     * @param aTier         tier of the Crop. forced to be >= 1
     * @param aMaxSize      maximum Size of the Crop. forced to be >= 3
     * @param aGrowthSpeed  how fast the Crop grows. if < 0 then its set to Tier*300
     * @param aHarvestSize  the size the Crop needs to be harvested. forced to be between 2 and max size
     * @param aBlock        the block below needed for crop to grow. If null no block needed
     */
    public GTBaseCrop(int aID, String aCropName, String aDiscoveredBy, ItemStack aBaseSeed, int aTier, int aMaxSize,
        int aGrowthSpeed, int aAfterHarvestSize, int aHarvestSize, int aStatChemical, int aStatFood, int aStatDefensive,
        int aStatColor, int aStatWeed, String[] aAttributes, Materials aBlock, ItemStack aDrop,
        ItemStack[] aSpecialDrops) {
        mName = aCropName;
        aID = GTConfig.addIDConfig(ConfigCategories.IDs.crops, mName.replaceAll(" ", "_"), aID);
        if (aDiscoveredBy != null && !aDiscoveredBy.equals(E)) mDiscoveredBy = aDiscoveredBy;
        if (aDrop != null && aID > 0 && aID < 256) {
            mDrop = GTUtility.copyOrNull(aDrop);
            mSpecialDrops = aSpecialDrops;
            mTier = Math.max(1, aTier);
            mMaxSize = Math.max(3, aMaxSize);
            mHarvestSize = Math.min(Math.max(aHarvestSize, 2), mMaxSize);
            mAfterHarvestSize = Math.min(Math.max(aAfterHarvestSize, 1), mMaxSize - 1);
            mStats[0] = aStatChemical;
            mStats[1] = aStatFood;
            mStats[2] = aStatDefensive;
            mStats[3] = aStatColor;
            mStats[4] = aStatWeed;
            mAttributes = aAttributes;
            mBlock = aBlock;
            if (!Crops.instance.registerCrop(this, aID))
                throw new GTItsNotMyFaultException("Make sure the Crop ID is valid!");
            if (aBaseSeed != null) Crops.instance.registerBaseSeed(aBaseSeed, this, 1, 1, 1, 1);
            sCropList.add(this);
        }
        if (bIc2NeiLoaded) {
            try {
                Class.forName("speiger.src.crops.api.CropPluginAPI")
                    .getMethod("registerCropInfo", Class.forName("speiger.src.crops.api.ICropCardInfo"))
                    .invoke(
                        Class.forName("speiger.src.crops.api.CropPluginAPI")
                            .getField("instance"),
                        this);
            } catch (IllegalAccessException | ClassNotFoundException | SecurityException | NoSuchMethodException
                | NoSuchFieldException | InvocationTargetException | IllegalArgumentException ex) {
                bIc2NeiLoaded = false;
            }
        }
    }

    @Override
    public byte getSizeAfterHarvest(ICropTile crop) {
        return (byte) mAfterHarvestSize;
    }

    @Override
    public int growthDuration(ICropTile aCrop) {
        if (mGrowthSpeed < 200) return super.growthDuration(aCrop);
        return tier() * mGrowthSpeed;
    }

    @Override
    public int getrootslength(ICropTile crop) {
        return 5;
    }

    @Override
    public String[] attributes() {
        return mAttributes;
    }

    @Override
    public String discoveredBy() {
        return mDiscoveredBy;
    }

    @Override
    public final boolean canGrow(ICropTile aCrop) {
        // block check is only performed at the last stage of growth
        if (this.needsBlockBelow() && aCrop.getSize() == mMaxSize - 1) {
            return isBlockBelow(aCrop);
        }
        return aCrop.getSize() < maxSize();
    }

    @Override
    public final boolean canBeHarvested(ICropTile aCrop) {
        return aCrop.getSize() >= mHarvestSize;
    }

    @Override
    public boolean canCross(ICropTile aCrop) {
        return aCrop.getSize() + 2 > maxSize();
    }

    @Override
    public int stat(int n) {
        if (n < 0 || n >= mStats.length) return 0;
        return mStats[n];
    }

    @Override
    public String name() {
        return mName;
    }

    @Override
    public int tier() {
        return mTier;
    }

    @Override
    public int maxSize() {
        return mMaxSize;
    }

    @Override
    public ItemStack getGain(ICropTile aCrop) {
        int tDrop = 0;
        if (mSpecialDrops != null && (tDrop = java.util.concurrent.ThreadLocalRandom.current()
            .nextInt(0, (mSpecialDrops.length * 2) + 2)) < mSpecialDrops.length && mSpecialDrops[tDrop] != null) {
            return GTUtility.copyOrNull(mSpecialDrops[tDrop]);
        }
        return GTUtility.copyOrNull(mDrop);
    }

    @Override
    public boolean rightclick(ICropTile aCrop, EntityPlayer aPlayer) {
        if (!canBeHarvested(aCrop)) return false;
        return aCrop.harvest(aPlayer instanceof EntityPlayerMP);
    }

    @Override
    public int getOptimalHavestSize(ICropTile crop) {
        return maxSize();
    }

    /**
     * Checks if the crop needs a block below it
     *
     * @return True if the crop needs a block below it to grow to its max size
     */
    public boolean needsBlockBelow() {
        return GTMod.gregtechproxy.mCropNeedBlock && this.mBlock != null;
    }

    public boolean isBlockBelow(ICropTile aCrop) {
        if (aCrop == null) {
            return false;
        }
        for (int i = 1; i < this.getrootslength(aCrop); i++) {
            Block tBlock = aCrop.getWorld()
                .getBlock(aCrop.getLocation().posX, aCrop.getLocation().posY - i, aCrop.getLocation().posZ);
            if ((tBlock instanceof BlockOresAbstract)) {
                TileEntity tTileEntity = aCrop.getWorld()
                    .getTileEntity(aCrop.getLocation().posX, aCrop.getLocation().posY - i, aCrop.getLocation().posZ);
                if ((tTileEntity instanceof TileEntityOres)) {
                    Materials tMaterial = GregTechAPI.sGeneratedMaterials[(((TileEntityOres) tTileEntity).mMetaData
                        % 1000)];
                    if ((tMaterial != null) && (tMaterial != Materials._NULL)) {
                        return tMaterial == mBlock;
                    }
                }
            } else {
                int tMetaID = aCrop.getWorld()
                    .getBlockMetadata(aCrop.getLocation().posX, aCrop.getLocation().posY - i, aCrop.getLocation().posZ);
                if (isBlockBelow(new ItemStack(tBlock, 1, tMetaID))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * An isolated function to check if an item stack is a block that should be below this crop
     *
     * @param aItem a stack of the block placed under the crop
     * @return The result of the check
     */
    public boolean isBlockBelow(ItemStack aItem) {
        // safety in case someone calls this without checking if we have a block
        if (!this.needsBlockBelow()) return true;

        // get material from stack
        ItemData tAssociation = GTOreDictUnificator.getAssociation(aItem);
        if (tAssociation == null) return false;

        // return true if it's an ore of the material
        // note: some ores don't appear to have associations in testing, naq ore is an example of that
        if (tAssociation.mPrefix.toString()
            .startsWith("ore") && tAssociation.mMaterial.mMaterial == mBlock) {
            return true;
        }

        // return true if it's a block of the material
        if (tAssociation.mPrefix == OrePrefixes.block && tAssociation.mMaterial.mMaterial == mBlock) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCropInformation() {
        if (mBlock != null) {
            ArrayList<String> result = new ArrayList<>(1);
            result.add(
                String.format(
                    "Requires %s Ore or Block of %s as soil block to reach full growth.",
                    mBlock.mName,
                    mBlock.mName));
            return result;
        }
        return null;
    }

    @Override
    public ItemStack getDisplayItem() {
        if (mSpecialDrops != null && mSpecialDrops[mSpecialDrops.length - 1] != null) {
            return GTUtility.copyOrNull(mSpecialDrops[mSpecialDrops.length - 1]);
        }
        return GTUtility.copyOrNull(mDrop);
    }
}
