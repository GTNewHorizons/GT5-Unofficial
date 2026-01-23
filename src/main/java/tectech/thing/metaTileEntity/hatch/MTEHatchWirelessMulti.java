package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BLUE;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.YELLOW;
import static gregtech.api.enums.GTValues.AuthorColen;
import static gregtech.api.enums.GTValues.V;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;
import static gregtech.common.misc.WirelessNetworkManager.totalStorage;
import static java.lang.Long.min;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.math.LongMath;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

public class MTEHatchWirelessMulti extends MTEHatchEnergyMulti {

    public final long precisionMultiplier = LongMath.pow(10, 15);
    public final BigInteger eu_transferred_per_operation = BigInteger.valueOf(Amperes * V[mTier])
        .multiply(BigInteger.valueOf(ticks_between_energy_addition));

    public final double overflowDivisor = getOverflowDivisor(eu_transferred_per_operation);

    public final long actualTicksBetweenEnergyAddition = overflowDivisor > 1
        ? (long) (ticks_between_energy_addition / (overflowDivisor * 2))
        : ticks_between_energy_addition;

    public final long eu_transferred_per_operation_long = overflowDivisor > 1
        ? eu_transferred_per_operation.divide(BigInteger.valueOf((long) (overflowDivisor * precisionMultiplier * 2)))
            .multiply(BigInteger.valueOf(precisionMultiplier))
            .longValue()
        : eu_transferred_per_operation.longValue();

    public UUID owner_uuid;

    public MTEHatchWirelessMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { GRAY + "Stores energy globally in a network, up to 2^(2^31) EU.",
                GRAY + "Does not connect to wires. This block withdraws EU from the network.",
                translateToLocal("gt.blockmachines.hatch.screwdrivertooltip"),
                AuthorColen + GRAY + BOLD + " & " + BLUE + BOLD + "Cloud",
                translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1") + ": "
                    + YELLOW
                    + formatNumber(aAmp * V[aTier])
                    + GRAY
                    + " EU/t" },
            aAmp);
    }

    @Override
    public int getHatchType() {
        // If amperage is > 64, this is a "wireless laser" and should not be usable on multi-amp only machines
        return maxAmperes <= 64 ? 1 : 2;
    }

    public MTEHatchWirelessMulti(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aAmp, aDescription, aTextures);
    }

    public double getOverflowDivisor(BigInteger euTransferredPerOperation) {
        if (euTransferredPerOperation.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            return euTransferredPerOperation.doubleValue() / Long.MAX_VALUE;
        }
        return 1d;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_LASER[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS[mTier + 1] };
        }
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_LASER[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_ON_WIRELESS[mTier + 1] };
        }
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return Amperes * V[mTier];
    }

    @Override
    public long maxEUStore() {
        return (long) (totalStorage(V[mTier]) / (2 * overflowDivisor) * Amperes);
    }

    @Override
    public long maxAmperesIn() {
        return Amperes;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
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

            // Every actualTicksBetweenEnergyAddition add eu_transferred_per_operation to internal EU storage from
            // network.
            if (aTick % actualTicksBetweenEnergyAddition == 0L) {
                tryFetchingEnergy();
            }
        }
    }

    public void tryFetchingEnergy() {
        long currentEU = getBaseMetaTileEntity().getStoredEU();
        long maxEU = maxEUStore();
        long euToTransfer = min(maxEU - currentEU, eu_transferred_per_operation_long);
        if (euToTransfer <= 0) return; // nothing to transfer
        if (!addEUToGlobalEnergyMap(owner_uuid, -euToTransfer)) return;
        setEUVar(currentEU + euToTransfer);
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        openGui(aPlayer);
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        final int x = getGUIWidth() / 2 - 37;
        final int y = getGUIHeight() / 5 - 7;
        builder.widget(
            TextWidget.localised("GT5U.machines.laser_hatch.amperage")
                .setPos(x, y)
                .setSize(74, 14))
            .widget(
                new NumericWidget().setSetter(val -> Amperes = (int) val)
                    .setGetter(() -> Amperes)
                    .setBounds(1, maxAmperes)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(x, y + 16)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
    }
}
