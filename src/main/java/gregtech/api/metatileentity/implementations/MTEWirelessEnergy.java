package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.AuthorColen;
import static gregtech.api.enums.GTValues.V;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;
import static gregtech.common.misc.WirelessNetworkManager.totalStorage;
import static java.lang.Long.min;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class MTEWirelessEnergy extends MTEHatchEnergy {

    private final BigInteger eu_transferred_per_operation = BigInteger
        .valueOf(2 * V[mTier] * ticks_between_energy_addition);
    private final long eu_transferred_per_operation_long = eu_transferred_per_operation.longValue();

    private UUID owner_uuid;

    public MTEWirelessEnergy(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEWirelessEnergy(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
            EnumChatFormatting.GRAY + "Does not connect to wires. This block withdraws EU from the network.",
            AuthorColen };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS[mTier + 1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS[mTier + 1] };
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 2 * V[mTier];
    }

    @Override
    public long maxEUStore() {
        return totalStorage(V[mTier]);
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIRELESS;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWirelessEnergy(mName, mTier, new String[] { "" }, mTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        if (!aBaseMetaTileEntity.isServerSide()) return;

        // UUID of the owner.
        owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

        SpaceProjectManager.checkOrCreateTeam(owner_uuid);

        tryFetchingEnergy();
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
