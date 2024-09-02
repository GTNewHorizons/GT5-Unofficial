package gtPlusPlus.xmod.gregtech.common.helpers;

import static gregtech.api.GregTechAPI.mEUtoRF;
import static gregtech.api.enums.Mods.Baubles;
import static gregtech.api.enums.Mods.COFHCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.items.MetaGeneratedItem02;
import gregtech.common.items.MetaGeneratedItem03;
import gregtech.common.items.MetaGeneratedTool01;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEWirelessCharger;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ChargingHelper {

    private static final Map<String, Pair<MTEWirelessCharger, Byte>> mValidPlayers = new HashMap<>();
    protected static Map<BlockPos, MTEWirelessCharger> mChargerMap = new HashMap<>();
    private int mTickTimer = 0;
    private static final int mTickMultiplier = 20;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerTick(ServerTickEvent event) {
        if (++mTickTimer % mTickMultiplier == 0) {
            if (Utils.isServer()) {
                for (EntityPlayer mPlayerMan : PlayerUtils.getOnlinePlayers()) {
                    doPlayerChargeTick(mPlayerMan);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void doPlayerChargeTick(EntityPlayer mPlayerMan) {
        try {
            long mVoltage;
            long mEuStored;

            if (!mChargerMap.isEmpty() && mValidPlayers.containsKey(mPlayerMan.getDisplayName())) {
                InventoryPlayer mPlayerInventory = mPlayerMan.inventory;
                ItemStack[] mArmourContents = mPlayerInventory.armorInventory.clone();
                ItemStack[] mInventoryContents = mPlayerInventory.mainInventory.clone();
                ItemStack[] baubleSlots = null;
                if (Baubles.isModLoaded()) {
                    IInventory baubleInv = BaublesApi.getBaubles(mPlayerMan);
                    if (baubleInv != null) {
                        baubleSlots = new ItemStack[baubleInv.getSizeInventory()];
                        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
                            baubleSlots[i] = baubleInv.getStackInSlot(i);
                        }
                    }
                }

                for (MTEWirelessCharger mEntityTemp : mChargerMap.values()) {
                    if (mEntityTemp != null) {
                        if (mEntityTemp.getBaseMetaTileEntity() == null || !mEntityTemp.getBaseMetaTileEntity()
                            .isAllowedToWork()) continue;
                        if (mPlayerMan.getEntityWorld().provider.dimensionId == mEntityTemp.getDimensionID()) {
                            mVoltage = mEntityTemp.maxEUInput();
                            mEuStored = mEntityTemp.getEUVar();
                            if (mVoltage > 0 && mEuStored >= mVoltage) {
                                Map<String, UUID> LR = mEntityTemp.getLongRangeMap();
                                Map<String, UUID> LO = mEntityTemp.getLocalMap();

                                long mStartingEu = mEntityTemp.getEUVar();
                                if (canCharge(mEntityTemp, mPlayerMan, LR, LO)) {
                                    chargeItems(mEntityTemp, mArmourContents);
                                    chargeItems(mEntityTemp, mInventoryContents);
                                    chargeItems(mEntityTemp, baubleSlots);
                                }

                                if (mStartingEu - mEntityTemp.getEUVar() <= 0) {
                                    long mMaxDistance;
                                    if (mEntityTemp.getMode() == 0) {
                                        mMaxDistance = (4 * GTValues.V[mEntityTemp.getTier()]);
                                    } else if (mEntityTemp.getMode() == 1) {
                                        mMaxDistance = (mEntityTemp.getTier() * 10L);
                                    } else {
                                        mMaxDistance = (4 * GTValues.V[mEntityTemp.getTier()] / 2);
                                    }
                                    double mDistance = calculateDistance(mEntityTemp, mPlayerMan);
                                    long mVoltageCost = MathUtils.findPercentageOfInt(mMaxDistance, (float) mDistance);

                                    if (mVoltageCost > 0) {
                                        if (mVoltageCost > mEntityTemp.maxEUInput()) {
                                            mEntityTemp.setEUVar((mEntityTemp.getEUVar() - mEntityTemp.maxEUInput()));
                                        } else {
                                            mEntityTemp.setEUVar((mEntityTemp.getEUVar() - mVoltageCost));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            if (!mChargerMap.isEmpty()) {
                for (BlockPos aPos : mChargerMap.keySet()) {
                    MTEWirelessCharger r = mChargerMap.get(aPos);
                    if (r == null || r.getBaseMetaTileEntity()
                        .isInvalidTileEntity()) {
                        mChargerMap.remove(aPos);
                    }
                }
            }
        }
    }

    public static MTEWirelessCharger getEntry(BlockPos mPos) {
        return mChargerMap.get(mPos);
    }

    public static boolean addEntry(BlockPos mPos, MTEWirelessCharger mEntity) {
        if (mEntity == null) {
            return false;
        }
        mChargerMap.put(mPos, mEntity);
        return true;
    }

    public static boolean removeEntry(BlockPos mPos, MTEWirelessCharger mEntity) {
        if (mEntity == null) {
            return false;
        }
        if (mChargerMap.containsKey(mPos)) {
            return mChargerMap.remove(mPos, mEntity);
        } else {
            return false;
        }
    }

    public static boolean addValidPlayer(EntityPlayer mPlayer, MTEWirelessCharger mEntity) {
        if (mEntity == null) {
            return false;
        }
        if (mValidPlayers.containsKey(mPlayer.getDisplayName())) {
            return false;
        } else {
            Pair<MTEWirelessCharger, Byte> mEntry = new Pair<>(mEntity, (byte) mEntity.getMode());
            return mValidPlayers.put(mPlayer.getDisplayName(), mEntry) == null;
        }
    }

    public static boolean removeValidPlayer(EntityPlayer mPlayer, MTEWirelessCharger mEntity) {
        if (mEntity == null) {
            return false;
        }
        if (mValidPlayers.containsKey(mPlayer.getDisplayName())) {
            Pair<MTEWirelessCharger, Byte> mEntry = new Pair<>(mEntity, (byte) mEntity.getMode());
            return mValidPlayers.remove(mPlayer.getDisplayName(), mEntry);
        } else {
            return false;
        }
    }

    private boolean canCharge(MTEWirelessCharger charger, EntityPlayer chargeablePlayer,
        Map<String, UUID> longRangeChargers, Map<String, UUID> shortRangeChargers) {
        if (charger.getMode() == 0) {
            return !longRangeChargers.isEmpty() && longRangeChargers.containsKey(chargeablePlayer.getDisplayName());
        } else if (charger.getMode() == 1) {
            return !shortRangeChargers.isEmpty() && shortRangeChargers.containsKey(chargeablePlayer.getDisplayName());
        } else {
            if (!longRangeChargers.isEmpty() && longRangeChargers.containsKey(chargeablePlayer.getDisplayName())) {
                return true;
            }
            return !shortRangeChargers.isEmpty() && shortRangeChargers.containsKey(chargeablePlayer.getDisplayName());
        }
    }

    private double calculateDistance(MTEWirelessCharger mEntityTemp, EntityPlayer mPlayerMan) {
        if (mEntityTemp == null || mPlayerMan == null) {
            return 0;
        }
        return mEntityTemp.getDistanceBetweenTwoPositions(
            mEntityTemp.getTileEntityPosition(),
            mEntityTemp.getPositionOfEntity(mPlayerMan));
    }

    private void chargeItems(@Nonnull MTEWirelessCharger mEntity, ItemStack[] mItems) {
        if (mItems == null || mItems.length == 0) {
            return;
        }
        chargeItemsEx(mEntity, mItems);
    }

    private void chargeItemsEx(@Nonnull MTEWirelessCharger mEntity, ItemStack[] mItems) {
        // Bad Inventory
        if (mItems == null || mItems.length == 0) {
            return;
        }
        // Set Variables to Charge
        final long mVoltage = mEntity.maxEUInput();
        long mEuStored = mEntity.getEUVar();
        // For Inventory Contents

        for (ItemStack mTemp : mItems) {
            // Is item Electrical
            if (isItemValid(mTemp)) {
                // Transfer Limit
                double mItemEuTLimit = ((IElectricItem) mTemp.getItem()).getTransferLimit(mTemp);
                // Check if Tile has more or equal EU to what can be transferred into the item.
                if (mEuStored >= mItemEuTLimit) {

                    double mItemMaxCharge = ((IElectricItem) mTemp.getItem()).getMaxCharge(mTemp);
                    double mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);

                    if (mitemCurrentCharge >= mItemMaxCharge) {
                        continue;
                    }

                    // Try to get charge direct from NBT for GT and IC2 stacks
                    if (mTemp.getItem() instanceof MetaGeneratedTool01 || mTemp.getItem() instanceof MetaGeneratedItem01
                        || mTemp.getItem() instanceof MetaGeneratedItem02
                        || mTemp.getItem() instanceof MetaGeneratedItem03
                        || mTemp.getItem()
                            .getClass()
                            .getName()
                            .equalsIgnoreCase(MetaGeneratedTool01.class.getName())) {
                        if (!NBTUtils.hasKey(mTemp, "GT.ItemCharge")) {
                            if (!mTemp.getDisplayName()
                                .toLowerCase()
                                .contains("battery")) {
                                if (!GTModHandler.isElectricItem(mTemp)) {
                                    continue;
                                }
                            } else {
                                mitemCurrentCharge = 0;
                            }
                        } else {
                            mitemCurrentCharge = NBTUtils.getLong(mTemp, "GT.ItemCharge");
                        }
                    } else if (mTemp.getItem() instanceof IElectricItem) {
                        mitemCurrentCharge = NBTUtils.getLong(mTemp, "charge");
                    }

                    double mVoltageIncrease;
                    if (mItemEuTLimit >= mVoltage) {
                        mVoltageIncrease = mVoltage;
                    } else if (mItemEuTLimit < mVoltage) {
                        mVoltageIncrease = mItemEuTLimit;
                    } else {
                        mVoltageIncrease = mItemEuTLimit;
                    }

                    int mMulti;
                    if ((mitemCurrentCharge + (mVoltageIncrease * 20)) <= (mItemMaxCharge - (mVoltageIncrease * 20))) {
                        mMulti = 20;
                    } else if ((mitemCurrentCharge + (mVoltageIncrease * 10))
                        <= (mItemMaxCharge - (mVoltageIncrease * 10))) {
                            mMulti = 10;
                        } else if ((mitemCurrentCharge + (mVoltageIncrease * 5))
                            <= (mItemMaxCharge - (mVoltageIncrease * 5))) {
                                mMulti = 5;
                            } else {
                                mMulti = 1;
                            }

                    int mMultiVoltage = (int) (mMulti * mVoltageIncrease);

                    if ((mitemCurrentCharge + mMultiVoltage) <= mItemMaxCharge) {
                        if (GTModHandler.chargeElectricItem(mTemp, mMultiVoltage, Integer.MAX_VALUE, true, false) > 0) {
                            for (int i = 0; i < mMulti; i++) {
                                ElectricItem.manager.charge(mTemp, mVoltageIncrease, Integer.MAX_VALUE, false, false);
                            }
                        }
                        if (ElectricItem.manager.getCharge(mTemp) > mitemCurrentCharge) {
                            mEntity.setEUVar(mEuStored - (mVoltage * mMulti));
                            mEuStored = mEntity.getEUVar();
                        }
                    }

                    // Try top up Item Chrage
                    mitemCurrentCharge = ElectricItem.manager.getCharge(mTemp);
                    if (mitemCurrentCharge < mItemMaxCharge && mitemCurrentCharge >= (mItemMaxCharge - mVoltage)) {
                        int xDif = (int) (mItemMaxCharge - mitemCurrentCharge);
                        if (GTModHandler.chargeElectricItem(mTemp, xDif, Integer.MAX_VALUE, true, false) >= 0) {
                            if (ElectricItem.manager.getCharge(mTemp) >= mItemMaxCharge) {
                                mEntity.setEUVar(mEntity.getEUVar() - (xDif));
                                mEuStored = mEntity.getEUVar();
                            }
                        }
                    }
                }
            } else if (isItemValidRF(mTemp)) {
                try {
                    IEnergyContainerItem rfItem = (IEnergyContainerItem) mTemp.getItem();
                    if (rfItem != null) {
                        long chargedPower = Math.min(
                            rfItem.getMaxEnergyStored(mTemp) - rfItem.getEnergyStored(mTemp),
                            mEntity.getEUVar() * mEUtoRF / 100L);
                        chargedPower = rfItem.receiveEnergy(
                            mTemp,
                            chargedPower > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) chargedPower,
                            false);
                        chargedPower = chargedPower * 100L / mEUtoRF;
                        mEntity.setEUVar(Math.max(mEntity.getEUVar() - chargedPower, 0));
                        mEuStored = mEntity.getEUVar();
                    }
                } catch (Exception ignored) {

                }
            }
        }
    }

    public static boolean isItemValid(final ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        if (GTModHandler.isElectricItem(itemstack)) {
            return true;
        }
        return itemstack.getItem() instanceof IElectricItem;
    }

    private static boolean isItemValidRF(final ItemStack itemStack) {
        return itemStack != null && COFHCore.isModLoaded() && itemStack.getItem() instanceof IEnergyContainerItem;
    }
}
