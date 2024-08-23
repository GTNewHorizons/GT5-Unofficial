package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_WIRELESS_LASER;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static java.lang.Long.min;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.MetaTileEntity;

public class GT_MetaTileEntity_Hatch_WirelessMulti extends GT_MetaTileEntity_Hatch_EnergyMulti
    implements IWirelessEnergyHatchInformation {

    private final BigInteger eu_transferred_per_operation = BigInteger
        .valueOf(Amperes * V[mTier] * ticks_between_energy_addition);
    private final long eu_transferred_per_operation_long = eu_transferred_per_operation.longValue();

    private UUID owner_uuid;

    public GT_MetaTileEntity_Hatch_WirelessMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
                GRAY + "Does not connect to wires. This block withdraws EU from the network.",
                AuthorColen + GRAY + BOLD + " & " + BLUE + BOLD + "Cloud" },
            aAmp);
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_WirelessMulti(String aName, int aTier, int aAmp, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    private ITexture[] TEXTURE_OVERLAY;

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        switch (Amperes) {
            case 4:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A;
                break;
            case 16:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A;
                break;
            case 64:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A;
                break;
            default:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_LASER;
                break;
        }
        return new ITexture[] { aBaseTexture, TEXTURE_OVERLAY[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        switch (Amperes) {
            case 4:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_4A;
                break;
            case 16:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_16A;
                break;
            case 64:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_MULTI_64A;
                break;
            default:
                TEXTURE_OVERLAY = OVERLAYS_ENERGY_IN_WIRELESS_LASER;
                break;
        }
        return new ITexture[] { aBaseTexture, TEXTURE_OVERLAY[mTier] };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return Amperes * V[mTier];
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return totalStorage(V[mTier]) * Amperes / 2;
    }

    @Override
    public long maxAmperesIn() {
        return Amperes;
    }

    @Override
    public long maxWorkingAmperesIn() {
        return Amperes;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_WirelessMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIRELESS;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // On first tick find the player name and attempt to add them to the map.

            // UUID and username of the owner.
            owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

            strongCheckOrAddUser(owner_uuid);

            tryFetchingEnergy();
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // This is set up in a way to be as optimised as possible. If a user has a relatively plentiful energy
            // network
            // it should make no difference to them. Minimising the number of operations on BigInteger is essential.

            // Every ticks_between_energy_addition add eu_transferred_per_operation to internal EU storage from network.
            if (aTick % ticks_between_energy_addition == 0L) {
                tryFetchingEnergy();
            }
        }
    }

    private void tryFetchingEnergy() {
        long currentEU = getBaseMetaTileEntity().getStoredEU();
        long maxEU = maxEUStore();
        long euToTransfer = min(maxEU - currentEU, eu_transferred_per_operation_long);
        if (euToTransfer <= 0) return; // nothing to transfer
        if (!addEUToGlobalEnergyMap(owner_uuid, -euToTransfer)) return;
        setEUVar(currentEU + euToTransfer);
    }
}
