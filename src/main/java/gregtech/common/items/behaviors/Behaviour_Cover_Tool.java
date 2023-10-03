package gregtech.common.items.behaviors;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;

public class Behaviour_Cover_Tool extends Behaviour_None {

    public static final IItemBehaviour<GT_MetaBase_Item> INSTANCE = new Behaviour_Cover_Tool();
    private final String mTooltip = GT_LanguageManager
        .addStringLocalization("gt.behaviour.cover_copy_paste", "Can copy/paste cover data.");

    private ISerializableObject mStoredData = GregTech_API.sNoBehavior.createDataObject();
    private int mCoverType;
    private int mTickRateAddition = 0;

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        final NBTTagCompound tNBT = aStack.getTagCompound();
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        final boolean isCopyMode = aPlayer.isSneaking();
        initDataFromNBT(tNBT);
        if (((aPlayer instanceof EntityPlayerMP)) && (aItem.canUse(aStack, 100.0D))) {
            if (isCopyMode) {
                ArrayList<String> tList = new ArrayList<>();
                doCopy(tTileEntity, aWorld, aX, aY, aZ, side, hitX, hitY, hitZ, tList);
                aItem.discharge(aStack, 100.0D, Integer.MAX_VALUE, true, false, false);
                writeListToNBT(tList, tNBT, aPlayer);
                saveDataToNBT(tNBT);
            } else {
                doPaste(tTileEntity, side, hitX, hitY, hitZ, aPlayer);
                aItem.discharge(aStack, 25.0D, Integer.MAX_VALUE, true, false, false);
            }
        }
        GT_Utility.doSoundAtClient(SoundResource.IC2_TOOLS_OD_SCANNER, 1, 1.0F, aX, aY, aZ);
        return aPlayer instanceof EntityPlayerMP;
    }

    private void initDataFromNBT(NBTTagCompound aNBT) {
        if (aNBT != null) {
            mCoverType = aNBT.getInteger("mCoverType");
            GT_CoverBehaviorBase<?> tBehavior = GregTech_API.getCoverBehaviorNew(mCoverType);
            NBTBase tData = aNBT.getTag("mCoverData");
            if (tData != null) mStoredData = tBehavior.createDataObject(tData);
            else mStoredData = GregTech_API.sNoBehavior.createDataObject();
            mTickRateAddition = aNBT.hasKey("mTickRateAddition") ? aNBT.getInteger("mTickRateAddition") : 1;
        }
    }

    private void saveDataToNBT(NBTTagCompound aNBT) {
        aNBT.setInteger("mCoverType", mCoverType);
        if (mStoredData == null) mStoredData = GregTech_API.sNoBehavior.createDataObject();
        aNBT.setTag("mCoverData", mStoredData.saveDataToNBT());
        aNBT.setInteger("mTickRateAddition", mTickRateAddition);
    }

    @SuppressWarnings("rawtypes")
    private void writeListToNBT(List aList, NBTTagCompound aNBT, EntityPlayer aPlayer) {
        if (aList != null && aNBT != null) {
            int tSize = aList.size();
            aNBT.setInteger("dataLinesCount", tSize);
            for (int i = 0; i < tSize; i++) {
                aNBT.setString("dataLines" + i, (String) aList.get(i));
                GT_Utility.sendChatToPlayer(aPlayer, (String) aList.get(i));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void doCopy(TileEntity aTileEntity, World aWorld, int aX, int aY, int aZ, ForgeDirection side, float hitX,
        float hitY, float hitZ, List aList) {
        aList.add(
            "----- X: " + EnumChatFormatting.AQUA
                + GT_Utility.formatNumbers(aX)
                + EnumChatFormatting.RESET
                + " Y: "
                + EnumChatFormatting.AQUA
                + GT_Utility.formatNumbers(aY)
                + EnumChatFormatting.RESET
                + " Z: "
                + EnumChatFormatting.AQUA
                + GT_Utility.formatNumbers(aZ)
                + EnumChatFormatting.RESET
                + " D: "
                + EnumChatFormatting.AQUA
                + aWorld.provider.dimensionId
                + EnumChatFormatting.RESET
                + " -----");
        if (aTileEntity instanceof ICoverable tCoverable) {
            final ForgeDirection tSide = tCoverable.getCoverItemAtSide(side) != null ? side
                : tCoverable.getCoverItemAtSide(GT_Utility.determineWrenchingSide(side, hitX, hitY, hitZ)) != null
                    ? GT_Utility.determineWrenchingSide(side, hitX, hitY, hitZ)
                    : ForgeDirection.UNKNOWN;
            if (tSide != ForgeDirection.UNKNOWN) {
                if (tCoverable.getCoverBehaviorAtSideNew(tSide)
                    .allowsCopyPasteTool()) {
                    mStoredData = tCoverable.getComplexCoverDataAtSide(tSide);
                    mCoverType = tCoverable.getCoverIDAtSide(tSide);
                    mTickRateAddition = tCoverable.getCoverInfoAtSide(tSide)
                        .getTickRateAddition();

                    aList.add("Block Side: " + EnumChatFormatting.AQUA + tSide.name() + EnumChatFormatting.RESET);
                    aList.add(
                        "Cover Type: " + EnumChatFormatting.GREEN
                            + tCoverable.getCoverItemAtSide(tSide)
                                .getDisplayName()
                            + EnumChatFormatting.RESET);
                } else {
                    mStoredData = GregTech_API.sNoBehavior.createDataObject();
                    mCoverType = 0;
                    mTickRateAddition = 0;
                    aList.add("Copy unavailable for this cover type");
                }
            } else {
                mStoredData = GregTech_API.sNoBehavior.createDataObject();
                mCoverType = 0;
                mTickRateAddition = 0;
                aList.add("No Cover Found");
            }
        } else {
            mStoredData = GregTech_API.sNoBehavior.createDataObject();
            mCoverType = 0;
            mTickRateAddition = 0;
            aList.add("No Cover Found");
        }
    }

    private void doPaste(TileEntity aTileEntity, ForgeDirection side, float hitX, float hitY, float hitZ,
        EntityPlayer aPlayer) {
        if (aTileEntity instanceof ICoverable tCoverable) {
            if (mCoverType == 0 || mStoredData == null) {
                GT_Utility.sendChatToPlayer(aPlayer, "Please Copy a Valid Cover First.");
                return;
            }
            final ForgeDirection tSide = tCoverable.getCoverItemAtSide(side) != null ? side
                : tCoverable.getCoverItemAtSide(GT_Utility.determineWrenchingSide(side, hitX, hitY, hitZ)) != null
                    ? GT_Utility.determineWrenchingSide(side, hitX, hitY, hitZ)
                    : ForgeDirection.UNKNOWN;
            if (tSide != ForgeDirection.UNKNOWN) {
                int tCoverID = tCoverable.getCoverIDAtSide(tSide);
                if (tCoverID == mCoverType) {
                    tCoverable.setCoverDataAtSide(tSide, mStoredData);
                    tCoverable.getCoverInfoAtSide(tSide)
                        .setTickRateAddition(mTickRateAddition);
                    GT_Utility.sendChatToPlayer(aPlayer, "Cover Data Pasted.");
                } else {
                    GT_Utility.sendChatToPlayer(aPlayer, "Not Matched Cover.");
                }
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "No Cover Found.");
            }
        }
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        try {
            final NBTTagCompound tNBT = aStack.getTagCompound();
            final int tSize = tNBT.getInteger("dataLinesCount");
            if (tSize < 1) throw new Exception();
            aList.add(EnumChatFormatting.BLUE + "Stored Cover Data:");
            for (int i = 0; i < tSize; i++) {
                aList.add(EnumChatFormatting.RESET + tNBT.getString("dataLines" + i));
            }
        } catch (Exception e) {
            aList.add(this.mTooltip);
        }
        return aList;
    }
}
