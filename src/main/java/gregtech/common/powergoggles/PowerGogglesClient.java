package gregtech.common.powergoggles;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import appeng.api.util.DimensionalCoord;
import gregtech.api.net.GTPacketOverwritePowerGogglesMeasurements;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.misc.WirelessNetworkManager;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesClient {

    private DimensionalCoord lscLink;
    private LinkedList<PowerGogglesMeasurement> measurements = new LinkedList<>();

    public PowerGogglesClient() {}

    public PowerGogglesClient(DimensionalCoord lscLink) {
        this.lscLink = lscLink;
    }

    public void updateLscLink(ItemStack itemstack, EntityPlayerMP player) {
        NBTTagCompound tag = itemstack.getTagCompound();
        DimensionalCoord newLink = null;
        if (tag != null && !tag.hasNoTags()) {
            newLink = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
        }

        if (!Objects.equals(lscLink, newLink)) {
            setLscLink(newLink);
            measurements.clear();
            overwriteMeasurements(player);
            // Persist data
            PowerGogglesWorldSavedData.INSTANCE.markDirty();
        }
    }

    public void setLscLink(DimensionalCoord lscLink) {
        this.lscLink = lscLink;
    }

    public void updatePlayer(EntityPlayerMP playerMP) {
        // Protection against calling with 0 measurements
        if (measurements.isEmpty()) {
            return;
        }

        PowerGogglesMeasurement lastMeasurement = measurements.getLast();
        if (lastMeasurement.isWireless()) {
            NW.sendToPlayer(
                new GTPacketUpdatePowerGoggles(WirelessNetworkManager.getUserEU((playerMP).getUniqueID())),
                playerMP);
        } else {
            NW.sendToPlayer(
                new GTPacketUpdatePowerGoggles(lastMeasurement.getMeasurement(), lastMeasurement.getCapacity()),
                playerMP);
        }
    }

    public NBTTagCompound getNBT(UUID uuid) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("uuid", uuid.toString());

        if (lscLink != null) {
            tag.setInteger("x", lscLink.x);
            tag.setInteger("y", lscLink.y);
            tag.setInteger("z", lscLink.z);
            tag.setInteger("dim", lscLink.getDimension());
        }

        NBTTagList measurementsTag = new NBTTagList();
        for (PowerGogglesMeasurement measurement : measurements) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            addMeasurementToTag(tagCompound, measurement);

            measurementsTag.appendTag(tagCompound);
        }
        tag.setTag("measurements", measurementsTag);
        return tag;
    }

    private void addMeasurementToTag(NBTTagCompound tagCompound, PowerGogglesMeasurement measurement) {
        tagCompound.setBoolean("isWireless", measurement.isWireless());
        tagCompound.setByteArray(
            "measurement",
            measurement.getMeasurement()
                .toByteArray());

        if (!measurement.isWireless()) {
            tagCompound.setLong("capacity", measurement.getCapacity());
        }

    }

    public void measure(EntityPlayerMP playerMP) {
        MTELapotronicSuperCapacitor lsc = PowerGogglesUtil.getLsc(lscLink);

        // LSC got destroyed, or something else happened to it
        if (lsc == null && lscLink != null) {
            return;
        }
        if (lsc == null) {
            measurements
                .addLast(new PowerGogglesMeasurement(true, WirelessNetworkManager.getUserEU(playerMP.getUniqueID())));
        } else {
            measurements
                .addLast(new PowerGogglesMeasurement(false, BigInteger.valueOf(lsc.getEUVar()), lsc.maxEUStore()));
        }
        if (measurements.size() > PowerGogglesConstants.STORED_MEASUREMENTS) {
            measurements.removeFirst();
        }
        PowerGogglesWorldSavedData.INSTANCE.markDirty();

    }

    public void setMeasurements(LinkedList<PowerGogglesMeasurement> measurements) {
        this.measurements = measurements;
    }

    public void overwriteMeasurements(EntityPlayerMP player) {
        NW.sendToPlayer(new GTPacketOverwritePowerGogglesMeasurements(measurements), player);
    }

}
