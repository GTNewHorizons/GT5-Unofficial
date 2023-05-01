package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power.GTPP_MTE_TieredMachineBlock;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class GT_MetaTileEntity_BasicBreaker extends GTPP_MTE_TieredMachineBlock implements IAddUIWidgets {

    public boolean mCharge = false;
    public boolean mDecharge = false;
    public int mBatteryCount = 0;
    public int mChargeableCount = 0;
    private long count = 0L;
    private long mStored = 0L;
    private long mMax = 0L;

    public GT_MetaTileEntity_BasicBreaker(int aID, String aName, String aNameRegional, int aTier, String aDescription,
            int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription, new ITexture[0]);
    }

    public GT_MetaTileEntity_BasicBreaker(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
            int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicBreaker(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
            int aSlotCount) {
        super(aName, aTier, aSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        final String[] desc = new String[6];
        int tTier = this.mTier;
        desc[0] = "" + EnumChatFormatting.BOLD + "16 Fuse Slots";
        desc[1] = "Per each fuse, you may insert " + EnumChatFormatting.YELLOW
                + (GT_Values.V[tTier])
                + EnumChatFormatting.GRAY
                + " EU/t";
        desc[2] = "However this " + EnumChatFormatting.ITALIC
                + EnumChatFormatting.RED
                + "MUST"
                + EnumChatFormatting.GRAY
                + " be in a single Amp";
        desc[3] = "This machine can accept upto a single amp of " + GT_Values.VN[Math.min(tTier + 2, 15)]
                + " as a result";
        desc[4] = "Breaker Loss: " + EnumChatFormatting.RED
                + ""
                + (GT_Values.V[tTier] / 16)
                + EnumChatFormatting.GRAY
                + " EU/t";
        desc[5] = CORE.GT_Tooltip.get();
        return desc;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[2][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                    this.mInventory.length > 4 ? BlockIcons.OVERLAYS_ENERGY_IN_MULTI[Math.min(12, mTier)]
                            : BlockIcons.OVERLAYS_ENERGY_IN[Math.min(12, mTier)] };

            rTextures[1][i + 1] = new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][i + 1],
                    this.mInventory.length > 4 ? BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
                            : BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
        }

        return rTextures;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        return this.mTextures[side == facing ? 1 : 0][aColorIndex + 1];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BasicBreaker(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures,
                this.mInventory.length);
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

    public boolean isInputFacing(ForgeDirection side) {
        return side != this.getBaseMetaTileEntity().getFrontFacing();
    }

    public boolean isOutputFacing(ForgeDirection side) {
        return side == this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return GT_Values.V[this.mTier] * 16L * 16;
    }

    @Override
    public long maxEUStore() {
        return GT_Values.V[this.mTier] * 64L * 16;
    }

    @Override
    public long maxEUInput() {
        return GT_Values.V[this.mTier] * 16;
    }

    @Override
    public long maxEUOutput() {
        return GT_Values.V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 16;
    }

    @Override
    public long maxAmperesOut() {
        return 16;
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
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

    @Override
    public int getProgresstime() {
        return (int) this.getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) this.getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            /*
             * this.mCharge = aBaseMetaTileEntity.getStoredEU() / 2L > aBaseMetaTileEntity.getEUCapacity() / 3L;
             * this.mDecharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3L;
             * this.mBatteryCount = 0; this.mChargeableCount = 0; ItemStack[] arg3 = this.mInventory; int arg4 =
             * arg3.length; for (int arg5 = 0; arg5 < arg4; ++arg5) { ItemStack tStack = arg3[arg5]; if
             * (GT_ModHandler.isElectricItem(tStack, this.mTier)) { if (GT_ModHandler.isChargerItem(tStack)) {
             * ++this.mBatteryCount; } ++this.mChargeableCount; } }
             */
        }
    }

    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public long[] getStoredEnergy() {
        boolean scaleOverflow = false;
        boolean storedOverflow = false;
        long tScale = this.getBaseMetaTileEntity().getEUCapacity();
        long tStored = this.getBaseMetaTileEntity().getStoredEU();
        long tStep = 0L;
        if (this.mInventory != null) {
            ItemStack[] arg8 = this.mInventory;
            int arg9 = arg8.length;

            for (int arg10 = 0; arg10 < arg9; ++arg10) {
                ItemStack aStack = arg8[arg10];
                if (GT_ModHandler.isElectricItem(aStack)) {
                    if (aStack.getItem() instanceof GT_MetaBase_Item) {
                        Long[] stats = ((GT_MetaBase_Item) aStack.getItem()).getElectricStats(aStack);
                        if (stats != null) {
                            if (stats[0].longValue() > 4611686018427387903L) {
                                scaleOverflow = true;
                            }

                            tScale += stats[0].longValue();
                            tStep = ((GT_MetaBase_Item) aStack.getItem()).getRealCharge(aStack);
                            if (tStep > 4611686018427387903L) {
                                storedOverflow = true;
                            }

                            tStored += tStep;
                        }
                    } else if (aStack.getItem() instanceof IElectricItem) {
                        tStored += (long) ElectricItem.manager.getCharge(aStack);
                        tScale += (long) ((IElectricItem) aStack.getItem()).getMaxCharge(aStack);
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
        return new String[] { "Tile Type: " + this.getTileEntityBaseType() };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean doesExplode() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add4by4Slots(builder);
    }
}
