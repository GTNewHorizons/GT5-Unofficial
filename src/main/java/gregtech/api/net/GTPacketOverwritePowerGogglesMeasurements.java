package gregtech.api.net;

import java.math.BigInteger;
import java.util.LinkedList;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.powergoggles.PowerGogglesMeasurement;
import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;
import io.netty.buffer.ByteBuf;

public class GTPacketOverwritePowerGogglesMeasurements extends GTPacket {

    LinkedList<PowerGogglesMeasurement> measurements;

    public GTPacketOverwritePowerGogglesMeasurements() {}

    public GTPacketOverwritePowerGogglesMeasurements(LinkedList<PowerGogglesMeasurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.OVERWRITE_POWER_GOGGLES_MEASUREMENTS.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(measurements.size());

        for (PowerGogglesMeasurement measurement : measurements) {
            buffer.writeBoolean(measurement.isWireless());

            byte[] measurementBytes = measurement.getMeasurement()
                .toByteArray();
            buffer.writeInt(measurementBytes.length);
            buffer.writeBytes(measurementBytes);

            if (!measurement.isWireless()) {
                buffer.writeLong(measurement.getCapacity());
            }
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        LinkedList<PowerGogglesMeasurement> measurements = new LinkedList<>();
        int measurementCount = buffer.readInt();

        for (int i = 0; i < measurementCount; i++) {
            boolean isWireless = buffer.readBoolean();

            BigInteger measurement = decodeMeasurement(buffer);

            if (isWireless) {
                measurements.add(new PowerGogglesMeasurement(true, measurement));
            } else {
                long capacity = buffer.readLong();
                measurements.add(new PowerGogglesMeasurement(false, measurement, capacity));
            }
        }

        return new GTPacketOverwritePowerGogglesMeasurements(measurements);
    }

    private BigInteger decodeMeasurement(ByteArrayDataInput buffer) {
        int byteCount = buffer.readInt();
        byte[] result = new byte[byteCount];

        for (int i = 0; i < byteCount; i++) {
            result[i] = buffer.readByte();
        }

        return new BigInteger(result);
    }

    @Override
    public void process(IBlockAccess world) {
        PowerGogglesHudHandler.getInstance()
            .getRenderer()
            .setMeasurements(measurements);
    }
}
