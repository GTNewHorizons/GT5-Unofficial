package gtPlusPlus.xmod.gregtech.common.covers;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.LangUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class GTPP_Cover_Overflow_Item extends GT_CoverBehavior {

    public final int mInitialCapacity;
    public final int mMaxItemCapacity;

    public static final Class sQuantumChest;
    public static final Class sSuperChestGTPP;
    public static final Class sSuperChestGTNH;
    public static HashMap<Integer, Field> mItemAmountFields = new HashMap<Integer, Field>();
    public static HashMap<Integer, Field> mItemTypeFields = new HashMap<Integer, Field>();

    static {
        sQuantumChest = ReflectionUtils.getClass("gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumChest");
        sSuperChestGTPP = ReflectionUtils
                .getClass("gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest");
        sSuperChestGTNH = ReflectionUtils.getClass("gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperChest");
        if (sQuantumChest != null) {
            mItemAmountFields.put(0, ReflectionUtils.getField(sQuantumChest, "mItemCount"));
            mItemTypeFields.put(0, ReflectionUtils.getField(sQuantumChest, "mItemStack"));
        }
        if (sSuperChestGTPP != null) {
            mItemAmountFields.put(1, ReflectionUtils.getField(sSuperChestGTPP, "mItemCount"));
            mItemTypeFields.put(1, ReflectionUtils.getField(sSuperChestGTPP, "mItemStack"));
        }
        if (sSuperChestGTNH != null) {
            mItemAmountFields.put(2, ReflectionUtils.getField(sSuperChestGTNH, "mItemCount"));
            mItemTypeFields.put(2, ReflectionUtils.getField(sSuperChestGTNH, "mItemStack"));
        }
    }

    public GTPP_Cover_Overflow_Item(int aCapacity) {
        this.mInitialCapacity = aCapacity;
        this.mMaxItemCapacity = aCapacity * 1000;
    }

    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
            ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == 0) {
            return aCoverVariable;
        }

        // Get the IGTTile
        IGregTechTileEntity aGtTileEntity = aTileEntity
                .getIGregTechTileEntity(aTileEntity.getXCoord(), aTileEntity.getYCoord(), aTileEntity.getZCoord());
        if (aGtTileEntity == null) {
            return aCoverVariable;
        }

        // Get the MetaTile
        final IMetaTileEntity aMetaTileEntity = aGtTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return aCoverVariable;
        }
        boolean didHandle = false;
        // Special Case for everything I want to support. /facepalm
        if (sQuantumChest != null && sQuantumChest.isInstance(aMetaTileEntity)) {
            didHandle = handleDigitalChest(aMetaTileEntity, 0);
        } else if (sSuperChestGTPP.isInstance(aMetaTileEntity)) {
            didHandle = handleDigitalChest(aMetaTileEntity, 1);

        } else if (sSuperChestGTNH != null && sSuperChestGTNH.isInstance(aMetaTileEntity)) {
            didHandle = handleDigitalChest(aMetaTileEntity, 2);
        }

        return aCoverVariable;
    }

    private boolean handleDigitalChest(IMetaTileEntity aTile, int aType) {
        int aItemAmount = (int) ReflectionUtils.getFieldValue(mItemAmountFields.get(aType), aTile);
        ItemStack aItemType = (ItemStack) ReflectionUtils.getFieldValue(mItemTypeFields.get(aType), aTile);

        if (aItemType == null || aItemAmount <= 0) {
            return false;
        } else {
            if (aItemAmount > mInitialCapacity) {
                int aNewItemAmount = mInitialCapacity;
                ReflectionUtils.setField(aTile, mItemAmountFields.get(aType), aNewItemAmount);
            }
        }
        return true;
    }

    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GT_Utility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            aCoverVariable += (mMaxItemCapacity * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            aCoverVariable -= (mMaxItemCapacity * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        }
        if (aCoverVariable > mMaxItemCapacity) {
            aCoverVariable = mInitialCapacity;
        }
        if (aCoverVariable <= 0) {
            aCoverVariable = mMaxItemCapacity;
        }
        GT_Utility.sendChatToPlayer(
                aPlayer,
                LangUtils.trans("322", "Overflow point: ") + aCoverVariable + trans("323", "L"));
        return aCoverVariable;
    }

    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
            EntityPlayer aPlayer, float aX, float aY, float aZ) {
        boolean aShift = aPlayer.isSneaking();
        int aAmount = aShift ? 128 : 8;
        if (GT_Utility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            aCoverVariable += aAmount;
        } else {
            aCoverVariable -= aAmount;
        }
        if (aCoverVariable > mMaxItemCapacity) {
            aCoverVariable = mInitialCapacity;
        }
        if (aCoverVariable <= 0) {
            aCoverVariable = mMaxItemCapacity;
        }
        GT_Utility.sendChatToPlayer(
                aPlayer,
                LangUtils.trans("322", "Overflow point: ") + aCoverVariable + trans("323", "L"));
        aTileEntity.setCoverDataAtSide(side, aCoverVariable);
        return true;
    }

    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
            ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
            ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
            ICoverable aTileEntity) {
        return false;
    }

    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
            ICoverable aTileEntity) {
        return true;
    }

    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }
}
