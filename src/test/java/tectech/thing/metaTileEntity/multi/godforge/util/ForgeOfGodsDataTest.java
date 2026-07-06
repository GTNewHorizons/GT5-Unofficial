package tectech.thing.metaTileEntity.multi.godforge.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

public class ForgeOfGodsDataTest {

    @Test
    public void batteryChargeSupportsLongNBT() {
        long internalBattery = Integer.MAX_VALUE + 42L;
        long maxBatteryCharge = Integer.MAX_VALUE + 1_000L;

        ForgeOfGodsData data = new ForgeOfGodsData();
        data.setInternalBattery(internalBattery);
        data.setMaxBatteryCharge(maxBatteryCharge);

        NBTTagCompound tag = new NBTTagCompound();
        data.serializeNBT(tag, true);

        assertEquals(internalBattery, tag.getLong("internalBattery"));
        assertEquals(maxBatteryCharge, tag.getLong("batterySize"));

        ForgeOfGodsData loaded = new ForgeOfGodsData();
        loaded.deserializeNBT(tag);

        assertEquals(internalBattery, loaded.getInternalBattery());
        assertEquals(maxBatteryCharge, loaded.getMaxBatteryCharge());
    }

    @Test
    public void batteryChargeLoadsLegacyIntegerNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("internalBattery", 123);
        tag.setInteger("batterySize", 456);

        ForgeOfGodsData data = new ForgeOfGodsData();
        data.deserializeNBT(tag);

        assertEquals(123L, data.getInternalBattery());
        assertEquals(456L, data.getMaxBatteryCharge());
    }
}
