package gregtech.common.covers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;

import gregtech.api.covers.CoverContext;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverBase.GateMode;

class CoverWirelessControllerTest {

    @Test
    void loadsLegacyIntegerData() {
        CoverWirelessController cover = createCover();

        cover.readFromNbt(wrapData(new NBTTagInt(0xABCD002A)));

        assertMigratedLegacyData(cover, "42", true);
        assertCurrentDataFormat(cover, "42", true);
    }

    @Test
    void loadsLegacyPublicIntegerData() {
        CoverWirelessController cover = createCover();

        cover.readFromNbt(wrapData(new NBTTagInt(0xFFFF)));

        assertMigratedLegacyData(cover, "65535", false);
        assertCurrentDataFormat(cover, "65535", false);
    }

    @Test
    void loadsLegacyCompoundData() {
        CoverWirelessController cover = createCover();
        NBTTagCompound legacyData = new NBTTagCompound();
        legacyData.setInteger("frequency", 1234);
        legacyData.setBoolean("privateChannel", true);

        cover.readFromNbt(wrapData(legacyData));

        assertMigratedLegacyData(cover, "1234", true);
        assertCurrentDataFormat(cover, "1234", true);
    }

    @Test
    void loadsCurrentData() {
        CoverWirelessController cover = createCover();
        UUID uuid = UUID.fromString("a5f1d453-3e1c-4a5c-9f1d-b3f6f78f042a");
        NBTTagCompound currentData = new NBTTagCompound();
        currentData.setString("frequency", "channel-name");
        currentData.setString("uuid", uuid.toString());
        currentData.setByte("mode", (byte) GateMode.OR.ordinal());
        currentData.setByte("state", (byte) 4);

        cover.readFromNbt(wrapData(currentData));

        assertEquals("channel-name", cover.getFrequency());
        assertEquals(uuid, cover.getUuid());
        assertEquals(GateMode.OR, cover.getGateMode());
        assertEquals(RedstoneCondition.DISABLE_WITH_REDSTONE, cover.getRedstoneCondition());
        assertTrue(cover.isSafeMode());
    }

    @Test
    void loadsDataFromBeforeGateModesWereAdded() {
        CoverWirelessController cover = createCover();
        NBTTagCompound oldControllerData = new NBTTagCompound();
        oldControllerData.setString("frequency", "pre-gate-mode");
        oldControllerData.setByte("state", (byte) 1);

        cover.readFromNbt(wrapData(oldControllerData));

        assertEquals("pre-gate-mode", cover.getFrequency());
        assertNull(cover.getUuid());
        assertEquals(GateMode.SINGLE_SOURCE, cover.getGateMode());
        assertEquals(RedstoneCondition.DISABLE_WITH_REDSTONE, cover.getRedstoneCondition());
        assertFalse(cover.isSafeMode());
    }

    private static CoverWirelessController createCover() {
        return new CoverWirelessController(new CoverContext(null, ForgeDirection.NORTH, null), null);
    }

    private static NBTTagCompound wrapData(NBTBase data) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("d", data);
        return tag;
    }

    private static void assertMigratedLegacyData(CoverWirelessController cover, String frequency,
        boolean privateChannel) {
        assertEquals(frequency, cover.getFrequency());
        assertEquals(privateChannel, cover.getPrivacyState());
        assertEquals(GateMode.SINGLE_SOURCE, cover.getGateMode());
        assertEquals(RedstoneCondition.ENABLE_WITH_REDSTONE, cover.getRedstoneCondition());
        assertFalse(cover.isSafeMode());
    }

    private static void assertCurrentDataFormat(CoverWirelessController cover, String frequency,
        boolean privateChannel) {
        NBTTagCompound data = cover.writeToNBT(new NBTTagCompound())
            .getCompoundTag("d");
        assertEquals(frequency, data.getString("frequency"));
        assertEquals(privateChannel, data.hasKey("uuid"));
        assertEquals(GateMode.SINGLE_SOURCE.ordinal(), data.getByte("mode"));
        assertEquals(0, data.getByte("state"));
    }
}
