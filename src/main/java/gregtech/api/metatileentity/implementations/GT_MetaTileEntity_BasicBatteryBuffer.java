package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import ic2.api.item.IElectricItem;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public class GT_MetaTileEntity_BasicBatteryBuffer extends GT_MetaTileEntity_TieredMachineBlock
    implements IAddUIWidgets {

    public boolean mCharge = false, mDecharge = false;
    public int mBatteryCount = 0, mChargeableCount = 0;
    private long count = 0;
    private long mStored = 0;
    private long mMax = 0;

    public GT_MetaTileEntity_BasicBatteryBuffer(int aID, String aName, String aNameRegional, int aTier,
        String aDescription, int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
    }

    public GT_MetaTileEntity_BasicBatteryBuffer(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicBatteryBuffer(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures, int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        desc[mDescriptionArray.length] = mInventory.length + " Slots";
        return desc;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1] };
            rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                mInventory.length == 16 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier]
                    : mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]
                        : Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return mTextures[side == aFacing ? 1 : 0][colorIndex + 1];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BasicBatteryBuffer(mName, mTier, mDescriptionArray, mTextures, mInventory.length);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16L * mInventory.length;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 64L * mInventory.length;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return mChargeableCount * 2L;
    }

    @Override
    public long maxAmperesOut() {
        return mBatteryCount;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return mCharge ? mInventory.length : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return mDecharge ? mInventory.length : 0;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        //
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        //
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;
            mBatteryCount = 0;
            mChargeableCount = 0;
            for (ItemStack tStack : mInventory) if (GT_ModHandler.isElectricItem(tStack, mTier)) {
                if (GT_ModHandler.isChargerItem(tStack)) mBatteryCount++;
                mChargeableCount++;
            }
        }
        count++;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (GT_ModHandler.isElectricItem(aStack) && aStack.getUnlocalizedName()
            .startsWith("gt.metaitem.01.")) {
            String name = aStack.getUnlocalizedName();
            if (name.equals("gt.metaitem.01.32510") || name.equals("gt.metaitem.01.32511")
                || name.equals("gt.metaitem.01.32520")
                || name.equals("gt.metaitem.01.32521")
                || name.equals("gt.metaitem.01.32530")
                || name.equals("gt.metaitem.01.32531")) {
                return ic2.api.item.ElectricItem.manager.getCharge(aStack) == 0;
            }
        }
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!GT_Utility.isStackValid(aStack)) {
            return false;
        }
        return mInventory[aIndex] == null && GT_ModHandler.isElectricItem(aStack, this.mTier);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public long[] getStoredEnergy() {
        boolean scaleOverflow = false;
        boolean storedOverflow = false;
        long tScale = getBaseMetaTileEntity().getEUCapacity();
        long tStored = getBaseMetaTileEntity().getStoredEU();
        long tStep = 0;
        if (mInventory != null) {
            for (ItemStack aStack : mInventory) {
                if (GT_ModHandler.isElectricItem(aStack)) {

                    if (aStack.getItem() instanceof GT_MetaBase_Item) {
                        Long[] stats = ((GT_MetaBase_Item) aStack.getItem()).getElectricStats(aStack);
                        if (stats != null) {
                            if (stats[0] > Long.MAX_VALUE / 2) {
                                scaleOverflow = true;
                            }
                            tScale = tScale + stats[0];
                            tStep = ((GT_MetaBase_Item) aStack.getItem()).getRealCharge(aStack);
                            if (tStep > Long.MAX_VALUE / 2) {
                                storedOverflow = true;
                            }
                            tStored = tStored + tStep;
                        }
                    } else if (aStack.getItem() instanceof IElectricItem) {
                        tStored = tStored + (long) ic2.api.item.ElectricItem.manager.getCharge(aStack);
                        tScale = tScale + (long) ((IElectricItem) aStack.getItem()).getMaxCharge(aStack);
                    }
                }
            }
        }
        if (scaleOverflow) {
            tScale = Long.MAX_VALUE;
        }
        if (storedOverflow) {
            tStored = Long.MAX_VALUE;
        }
        return new long[] { tStored, tScale };
    }

    @Override
    public String[] getInfoData() {
        updateStorageInfo();

        return new String[] { EnumChatFormatting.BLUE + getLocalName() + EnumChatFormatting.RESET, "Stored Items:",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mStored)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMax)
                + EnumChatFormatting.RESET
                + " EU",
            "Average input:", GT_Utility.formatNumbers(getBaseMetaTileEntity().getAverageElectricInput()) + " EU/t",
            "Average output:", GT_Utility.formatNumbers(getBaseMetaTileEntity().getAverageElectricOutput()) + " EU/t" };
    }

    private void updateStorageInfo() {
        if (mMax == 0 || (count > 20)) {
            long[] tmp = getStoredEnergy();
            mStored = tmp[0];
            mMax = tmp[1];
            count = 0;
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.stored",
                GT_Utility.formatNumbers(tag.getLong("mStored")),
                GT_Utility.formatNumbers(tag.getLong("mMax"))));
        long avgIn = tag.getLong("AvgIn");
        long avgOut = tag.getLong("AvgOut");
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.avg_in_with_amperage",
                GT_Utility.formatNumbers(avgIn),
                GT_Utility.getAmperageForTier(avgIn, (byte) getInputTier()),
                GT_Utility.getColoredTierNameFromTier((byte) getInputTier())));
        currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.avg_out_with_amperage",
                GT_Utility.formatNumbers(avgOut),
                GT_Utility.getAmperageForTier(avgOut, (byte) getOutputTier()),
                GT_Utility.getColoredTierNameFromTier((byte) getOutputTier())));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        updateStorageInfo();
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("mStored", mStored);
        tag.setLong("mMax", mMax);
        tag.setLong("AvgIn", getBaseMetaTileEntity().getAverageElectricInput());
        tag.setLong("AvgOut", getBaseMetaTileEntity().getAverageElectricOutput());
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mInventory.length) {
            case 4 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 2)
                    .startFromSlot(0)
                    .endAtSlot(3)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(70, 25));
            case 9 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .startFromSlot(0)
                    .endAtSlot(8)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(61, 16));
            case 16 -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 4)
                    .startFromSlot(0)
                    .endAtSlot(15)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(52, 7));
            default -> builder.widget(
                SlotGroup.ofItemHandler(inventoryHandler, 1)
                    .startFromSlot(0)
                    .endAtSlot(0)
                    .slotCreator(index -> new BaseSlot(inventoryHandler, index) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }
                    })
                    .background(getGUITextureSet().getItemSlot())
                    .build()
                    .setPos(79, 34));
        }
    }
}
