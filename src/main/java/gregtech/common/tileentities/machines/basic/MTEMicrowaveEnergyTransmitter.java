package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.config.MachineStats;
import gregtech.common.gui.modularui.singleblock.MTEMicrowaveEnergyTransmitterGui;

public class MTEMicrowaveEnergyTransmitter extends MTEBasicTank {

    private static boolean sInterDimensionalTeleportAllowed = true;
    private static int mMaxLoss = 50;
    private static int mMaxLossDistance = 10000;
    private static boolean mPassiveEnergyUse = true;
    public int mTargetX = 0;
    public int mTargetY = 0;
    public int mTargetZ = 0;
    public int mTargetD = 0;
    public boolean mDebug = false;
    public boolean hasBlock = false;
    public int tTargetX = 0;
    public int tTargetY = 0;
    public int tTargetZ = 0;
    public int tTargetD = 0;
    public TileEntity tTile = null;

    public MTEMicrowaveEnergyTransmitter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            new String[] { "Transmits Energy Wirelessly", "Use Nitrogen Plasma", "for Inter-dimensional transmission",
                "0.004EU Loss per 100 Blocks" });
    }

    public MTEMicrowaveEnergyTransmitter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        this.hasBlock = checkForBlock();
        openGui(aPlayer);
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMicrowaveEnergyTransmitter(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getInfoData() {
        return new String[] { StatCollector.translateToLocal("GT5U.infodata.coordinates"),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.x",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetX) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.y",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetY) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.coordinates.z",
                EnumChatFormatting.GREEN + formatNumber(this.mTargetZ) + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension",
                "" + EnumChatFormatting.GREEN + this.mTargetD + EnumChatFormatting.RESET),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension.valid",
                (GTUtility.isRealDimension(this.mTargetD)
                    ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.infodata.yes")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.infodata.no")
                        + EnumChatFormatting.RESET)),
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.dimension.registered",
                (DimensionManager.isDimensionRegistered(this.mTargetD)
                    ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.infodata.yes")
                        + EnumChatFormatting.RESET
                    : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.infodata.no")
                        + EnumChatFormatting.RESET)) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == ForgeDirection.DOWN) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        if (aActive) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_TELEPORTER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (mFluid != null) aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
        aNBT.setInteger("mTargetX", this.mTargetX);
        aNBT.setInteger("mTargetY", this.mTargetY);
        aNBT.setInteger("mTargetZ", this.mTargetZ);
        aNBT.setInteger("mTargetD", this.mTargetD);
        aNBT.setBoolean("mDebug", this.mDebug);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
        this.mTargetX = aNBT.getInteger("mTargetX");
        this.mTargetY = aNBT.getInteger("mTargetY");
        this.mTargetZ = aNBT.getInteger("mTargetZ");
        this.mTargetD = aNBT.getInteger("mTargetD");
        this.mDebug = aNBT.getBoolean("mDebug");
    }

    @Override
    public void onConfigLoad() {
        sInterDimensionalTeleportAllowed = MachineStats.teleporter.interDimensionalTPAllowed;
        mMaxLoss = Math.max(MachineStats.microwaveEnergyTransmitter.maxLoss, 11);
        mMaxLossDistance = MachineStats.microwaveEnergyTransmitter.maxLossDistance;
        mPassiveEnergyUse = MachineStats.microwaveEnergyTransmitter.passiveEnergyUse;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((this.mTargetX == 0) && (this.mTargetY == 0) && (this.mTargetZ == 0) && (this.mTargetD == 0)) {
                this.mTargetX = aBaseMetaTileEntity.getXCoord();
                this.mTargetY = aBaseMetaTileEntity.getYCoord();
                this.mTargetZ = aBaseMetaTileEntity.getZCoord();
                this.mTargetD = aBaseMetaTileEntity.getWorld().provider.dimensionId;
            }
            this.hasBlock = checkForBlock();
        }
    }

    public boolean checkForBlock() {
        for (byte i = -5; i <= 5; i = (byte) (i + 1)) {
            for (byte j = -5; j <= 5; j = (byte) (j + 1)) {
                for (byte k = -5; k <= 5; k = (byte) (k + 1)) {
                    if (getBaseMetaTileEntity().getBlockOffset(i, j, k) == GregTechAPI.sBlockMetal5
                        && getBaseMetaTileEntity().getMetaIDOffset(i, j, k) == 8) { // require osmiridium block
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasDimensionalTeleportCapability() {
        return this.mDebug || (sInterDimensionalTeleportAllowed && (this.hasBlock
            || mFluid != null && mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1)) && mFluid.amount >= 1000));
    }

    public boolean isDimensionalTeleportAvailable() {
        return this.mDebug || (hasDimensionalTeleportCapability() && GTUtility.isRealDimension(this.mTargetD)
            && GTUtility.isRealDimension(getBaseMetaTileEntity().getWorld().provider.dimensionId));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mFluid == null) {
            mFluid = Materials.Nitrogen.getPlasma(0);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isServerSide()) {
            if (getBaseMetaTileEntity().getTimer() % 100L == 50L) {
                this.hasBlock = checkForBlock();
            }
            if ((getBaseMetaTileEntity().isAllowedToWork()) && (getBaseMetaTileEntity().getRedstone())) {
                if (getBaseMetaTileEntity().getStoredEU() > (V[mTier] * 16)) {
                    if (mPassiveEnergyUse) {
                        getBaseMetaTileEntity().decreaseStoredEnergyUnits(2L << (mTier - 1), false);
                    }
                    if (hasDimensionalTeleportCapability()
                        && this.mTargetD != getBaseMetaTileEntity().getWorld().provider.dimensionId
                        && mFluid.isFluidEqual(Materials.Nitrogen.getPlasma(1))) {
                        mFluid.amount--;
                        if (mFluid.amount < 1) {
                            mFluid = null;
                        }
                    }
                    if (tTargetD != mTargetD || tTargetX != mTargetX || tTargetY != mTargetY || tTargetZ != mTargetZ) {
                        tTargetD = mTargetD;
                        tTargetX = mTargetX;
                        tTargetY = mTargetY;
                        tTargetZ = mTargetZ;
                        if (this.mTargetD == getBaseMetaTileEntity().getWorld().provider.dimensionId) {
                            tTile = getBaseMetaTileEntity().getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                        } else {
                            World tWorld = DimensionManager.getWorld(this.mTargetD);
                            if (tWorld != null) {
                                tTile = tWorld.getTileEntity(this.mTargetX, this.mTargetY, this.mTargetZ);
                            }
                        }
                    }
                    int tDistance = distanceCalculation();
                    if (tTile != null) {
                        if (tTile instanceof IEnergyConnected) {
                            long packetSize = V[mTier];
                            if (tTile instanceof IGregTechTileEntity) {
                                IMetaTileEntity mte = ((IGregTechTileEntity) tTile).getMetaTileEntity();
                                if (mte instanceof BaseMetaTileEntity) {
                                    packetSize = ((BaseMetaTileEntity) mte).getMaxSafeInput();
                                }
                            }
                            long energyUse = 10;
                            if (mMaxLossDistance != 0) {
                                energyUse = GTUtility
                                    .safeInt(10L + (tDistance * Math.max(mMaxLoss - 10L, 0) / mMaxLossDistance));
                            }
                            energyUse = packetSize + ((V[mTier] * energyUse) / 100);
                            if (getBaseMetaTileEntity().isUniversalEnergyStored(energyUse)) {
                                if (((IEnergyConnected) tTile).injectEnergyUnits(ForgeDirection.UNKNOWN, packetSize, 1)
                                    > 0) {
                                    getBaseMetaTileEntity().decreaseStoredEnergyUnits(energyUse, false);
                                }
                            }
                        }
                    }
                }
                getBaseMetaTileEntity().setActive(true);
            } else {
                getBaseMetaTileEntity().setActive(false);
            }
        }
    }

    private int distanceCalculation() {
        double dx = getBaseMetaTileEntity().getXCoord() - this.mTargetX;
        double dy = getBaseMetaTileEntity().getYCoord() - this.mTargetY;
        double dz = getBaseMetaTileEntity().getZCoord() - this.mTargetZ;
        return Math.abs(
            ((this.mTargetD != getBaseMetaTileEntity().getWorld().provider.dimensionId)
                && (isDimensionalTeleportAvailable()) ? 100 : 1) * (int) Math.sqrt(dx * dx + dy * dy + dz * dz));
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
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 256;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 3;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 64000;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEMicrowaveEnergyTransmitterGui(this).build(data, syncManager, uiSettings);
    }
}
