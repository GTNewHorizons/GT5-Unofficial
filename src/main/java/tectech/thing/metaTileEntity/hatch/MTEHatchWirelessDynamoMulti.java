package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.YELLOW;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.AuthorColen;
import static gregtech.api.enums.GTValues.V;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEHatchWirelessDynamoMulti extends MTEHatchDynamoMulti {

    private UUID owner_uuid;

    /***
     * As opposed to the Energy Input equivalent, there is only one wireless dynamo multiamp. It has a maximum capacity
     * of Long.MAX, meant to consolidate an LSC for power gen options. Takes in UMV amps 65k.
     */
    public MTEHatchWirelessDynamoMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
                GRAY + "Does not connect to wires. This block accepts EU into the network.",
                AuthorColen + GRAY + BOLD + " & " + GREEN + BOLD + "Chrom",
                translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1") + ": "
                    + YELLOW
                    + formatNumber(aAmp * V[aTier])
                    + GRAY
                    + " EU/t" },
            aAmp);
    }

    public MTEHatchWirelessDynamoMulti(String aName, int aTier, int aAmp, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (Amperes != maxAmperes) {
            aNBT.setInteger("amperes", Amperes);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        int savedAmperes = aNBT.getInteger("amperes");
        if (savedAmperes != 0) {
            Amperes = savedAmperes;
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_LASER[mTier + 1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_LASER[mTier + 1] };
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return Amperes * V[mTier];
    }

    @Override
    public long maxEUStore() {
        return Long.MAX_VALUE;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.WIRELESS;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessDynamoMulti(mName, mTier, Amperes, new String[] { "" }, mTextures);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {

            // On first tick find the player name and attempt to add them to the map.
            if (aTick == 1) {

                // UUID and username of the owner.
                owner_uuid = aBaseMetaTileEntity.getOwnerUuid();

                strongCheckOrAddUser(owner_uuid);
            }

            // Every ticks_between_energy_addition ticks change the energy content of the machine.
            if (aTick % ticks_between_energy_addition == 0L) {
                addEUToGlobalEnergyMap(owner_uuid, getEUVar());
                setEUVar(0L);
            }
        }
    }

}
