package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import crazypants.enderio.machine.capbank.TileCapBank;
import crazypants.enderio.machine.capbank.network.ICapBankNetwork;
import crazypants.enderio.power.IPowerContainer;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public class GT_MetaTileEntity_RfConvertor extends GregtechMetaEnergyBuffer implements IEnergyReceiver {

    public GT_MetaTileEntity_RfConvertor(final String aName, final int aTier, final String aDescription,
            final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
    }

    public GT_MetaTileEntity_RfConvertor(final int aID, final String aName, final String aNameRegional, final int aTier,
            final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Use Screwdriver to change voltage",
                "Hold Shift while using Screwdriver to change amperage",
                EnumChatFormatting.DARK_AQUA + "Variable Output Voltage", CORE.GT_Tooltip.get() };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        GT_RenderedTexture aTex = new GT_RenderedTexture(TexturesGtBlock.Casing_Material_ZirconiumCarbide);
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[1][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[2][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[3][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier] };
            rTextures[4][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier] };
            rTextures[5][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier] };
            rTextures[6][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[7][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[8][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[9][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
            rTextures[10][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
            rTextures[11][i + 1] = new ITexture[] { aTex, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        return mTextures[Math.min(2, side.ordinal()) + (side == facing ? 3 : 0) + (aActive ? 0 : 6)][aColorIndex + 1];
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RfConvertor(
                this.mName,
                this.mTier,
                this.mDescription,
                this.mTextures,
                this.mInventory.length);
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxEUStore() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long maxEUInput() {
        return 0;
    }

    @Override
    public long maxEUOutput() {
        return V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return !isOutputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (!aBaseMetaTileEntity.isActive()) {
                aBaseMetaTileEntity.setActive(true);
            }
            if (this.getEUVar() < this.maxEUStore()) {
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    if (aBaseMetaTileEntity.getStoredEU() >= aBaseMetaTileEntity.getEUCapacity()) break;
                    if (isInputFacing(side)) {
                        receiveEnergy(side, Integer.MAX_VALUE, false);
                    }
                }
            }
            return;
        }
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = super.getInfoData();
        return new String[] { infoData[0], "Converts RF -> GTEU | Tier: " + this.mTier, infoData[1], infoData[2] };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mTier", this.mTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTier = aNBT.getByte("mTier");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            byte aTest = (byte) (aCurrentOutputAmperage + 1);
            if (aTest > 16 || aTest <= 0) {
                aTest = 1;
            }
            aCurrentOutputAmperage = aTest;
            PlayerUtils.messagePlayer(aPlayer, "Now handling " + maxAmperesOut() + " Amps.");
        } else {
            if (this.mTier < GT_Values.V.length) {
                this.mTier++;
            } else {
                this.mTier = 0;
            }
            PlayerUtils.messagePlayer(aPlayer, "Now running at " + GT_Values.VOLTAGE_NAMES[this.mTier] + ".");
        }
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if (isOutputFacing(from)) {
            return false;
        }
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

        // Cannot accept power on the output face.
        if (!canConnectEnergy(from) || isOutputFacing(from)) {
            return 0;
        }

        TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(from);
        if (tTileEntity == null) {
            return 0;
        }

        Logger.WARNING("Someone is trying to inject RF from " + from + ". Type:" + tTileEntity.getClass().getName());

        // Calculate maximum RF we need to consume
        int aInputRF = MathUtils.safeInt(this.maxEUOutput() * GregTech_API.mEUtoRF / 100);

        // Make sure we only consume the correct amount of RF that is pushed into this Tile.
        if (aInputRF > maxReceive) {
            aInputRF = maxReceive;
        }

        int aInjectedRF = 0;
        boolean aVal = false;

        long aStoredEU = this.getEUVar();
        long aMaxEU = this.maxEUStore();
        Logger.WARNING("Stored: " + aStoredEU + ", Capacity: " + aMaxEU + "");
        if (aStoredEU < aMaxEU) {
            Logger.WARNING("StoredEU < MaxEU");
            long aRemainingSpace = aMaxEU - aStoredEU;
            if (aRemainingSpace > 0) {
                long tEU = 0;
                final ForgeDirection toSide = from.getOpposite();
                byte aSide = (byte) from.ordinal();
                Logger.WARNING("Free: " + aRemainingSpace + "EU");
                if (tTileEntity instanceof IEnergyProvider
                        && ((IEnergyProvider) tTileEntity).extractEnergy(toSide, 1, true) == 1) {
                    tEU = (long) ((IEnergyProvider) tTileEntity)
                            .extractEnergy(toSide, (int) maxEUOutput() * 100 / GregTech_API.mRFtoEU, false);
                    Logger.WARNING("Drained from IEnergyProvider Tile: " + (tEU * 100 / GregTech_API.mRFtoEU) + "");
                    tEU = tEU * GregTech_API.mRFtoEU / 100;
                } else if (tTileEntity instanceof IEnergyStorage
                        && ((IEnergyStorage) tTileEntity).extractEnergy(1, true) == 1) {
                            tEU = (long) ((IEnergyStorage) tTileEntity)
                                    .extractEnergy((int) maxEUOutput() * 100 / GregTech_API.mRFtoEU, false);
                            Logger.WARNING(
                                    "Drained from IEnergyStorage Tile: " + (tEU * 100 / GregTech_API.mRFtoEU) + "");
                            tEU = tEU * GregTech_API.mRFtoEU / 100;
                        } else
                    if (GregTech_API.meIOLoaded && tTileEntity instanceof IPowerContainer
                            && ((IPowerContainer) tTileEntity).getEnergyStored() > 0) {
                                int storedRF = ((IPowerContainer) tTileEntity).getEnergyStored();
                                int extractRF = (int) maxEUOutput() * 100 / GregTech_API.mRFtoEU;
                                tEU = 0;
                                if (tTileEntity instanceof TileCapBank) {
                                    ICapBankNetwork network = ((TileCapBank) tTileEntity).getNetwork();
                                    if (network != null && network.getEnergyStoredL() > 0) {
                                        tEU = Math.min(
                                                (Math.min(
                                                        Math.min(network.getEnergyStoredL(), storedRF - extractRF),
                                                        network.getMaxOutput())) * GregTech_API.mRFtoEU / 100,
                                                maxEUOutput());
                                        Logger.WARNING(
                                                "Drained from EIO CapBank Tile: " + (tEU * 100 / GregTech_API.mRFtoEU)
                                                        + "");
                                        network.addEnergy((int) -(tEU * 100 / GregTech_API.mRFtoEU));
                                    }
                                } else {
                                    if (storedRF > extractRF) {
                                        ((IPowerContainer) tTileEntity).setEnergyStored(storedRF - extractRF);
                                        tEU = maxEUOutput();
                                        Logger.WARNING(
                                                "Drained from EIO Tile: " + (tEU * 100 / GregTech_API.mRFtoEU) + "");
                                    } else {
                                        ((IPowerContainer) tTileEntity).setEnergyStored(0);
                                        tEU = storedRF * GregTech_API.mRFtoEU / 100;
                                        Logger.WARNING(
                                                "Drained from EIO Tile: " + (tEU * 100 / GregTech_API.mRFtoEU) + "");
                                    }
                                }
                            }
                Logger.WARNING("EU to inject: " + tEU + "EU");
                if (!simulate) {
                    aVal = this.getBaseMetaTileEntity().increaseStoredEnergyUnits(tEU, true);
                }
                if (tEU > 0) {
                    Logger.WARNING("Tried injecting " + tEU + " eu into self. Success? " + aVal);
                }
            }
        }
        return aInjectedRF;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        long aStoredEU = this.getEUVar();
        long aMaxEU = this.maxEUStore();
        if (aStoredEU == 0) {
            return 0;
        }
        if (aStoredEU < aMaxEU) {
            long aRemainingSpace = aMaxEU - aStoredEU;
            if (aRemainingSpace > 0) {
                if (aRemainingSpace > (this.maxEUOutput() / (GregTech_API.mEUtoRF / 100))) {
                    int aCalculatedFreeSpace = 0;
                    int aRfPer10Eu = GregTech_API.mEUtoRF / 10;
                    // Calculate how many lots of '10
                    aCalculatedFreeSpace = (int) Math.floor(aRemainingSpace / 10);
                    // Return value equal to how many lots of '10eu' packets we can fit in.
                    return Integer.MAX_VALUE
                            - MathUtils.balance(aCalculatedFreeSpace * aRfPer10Eu, 0, Integer.MAX_VALUE);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return Integer.MAX_VALUE;
    }
}
