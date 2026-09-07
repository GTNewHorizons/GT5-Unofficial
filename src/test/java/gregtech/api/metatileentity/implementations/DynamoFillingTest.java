package gregtech.api.metatileentity.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTELargeRocketEngine;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;

class DynamoFillingTest {

    static class ExternalBase extends BaseMetaTileEntity {

        final List<Long> offers = new ArrayList<>();

        @Override
        public boolean increaseStoredEnergyUnits(long energy, boolean ignoreCapacity) {
            offers.add(energy);
            boolean accepted = super.increaseStoredEnergyUnits(energy, ignoreCapacity);
            if (!accepted && offers.size() == 1) setStoredEU(0);
            return accepted;
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void externalBaseCanReopenFullStorageDuringRejectedCall(boolean rocket) {
        MTEHatchDynamoMulti hatch = hatch(4);
        ExternalBase base = new ExternalBase();
        hatch.setBaseMetaTileEntity(base);
        hatch.setEUVar(hatch.maxEUStore());
        assertTrue(machine(rocket, hatch).addEnergyOutputMultipleDynamos(128, true));
        assertEquals(List.of(32L, 32L, 32L, 32L), base.offers);
        assertEquals(96, hatch.getEUVar());
    }

    static class OverflowDynamo extends MTEHatchDynamoMulti {

        OverflowDynamo() {
            super("overflow-test", 1, 4, new String[0], null);
        }

        @Override
        public long maxEUStore() {
            return Long.MAX_VALUE;
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void overflowedStorageStillClampsBetweenPackets(boolean rocket) {
        OverflowDynamo hatch = new OverflowDynamo();
        List<Long> dirty = new ArrayList<>();
        BaseMetaTileEntity base = attach(hatch, Long.MAX_VALUE - 1, dirty);
        assertTrue(machine(rocket, hatch).addEnergyOutputMultipleDynamos(64, true));
        assertEquals(32, hatch.getEUVar());
        assertEquals(List.of(Long.MAX_VALUE - 1, 0L), dirty);
        verify(base, times(2)).increaseStoredEnergyUnits(32, false);
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void offeredCounterNarrowingAndRemainderStayUnchanged(boolean rocket) {
        MTEHatchDynamoMulti hatch = new MTEHatchDynamoMulti("counter-test", 9, 1025, new String[0], null);
        attach(hatch, hatch.maxEUStore(), new ArrayList<>());
        long offer = hatch.maxEUOutput() * 1024 + 1;
        assertTrue(offer > Integer.MAX_VALUE);
        // Current int accounting wraps, even though every packet is rejected. Correctness fix is separate.
        assertFalse(machine(rocket, hatch).addEnergyOutputMultipleDynamos(offer, true));
        assertEquals(hatch.maxEUStore(), hatch.getEUVar());
    }

    @Test
    void rocketAmpsPlusOneOverflowRetainsLegacyNoTransfer() {
        MTEHatchDynamoMulti hatch = hatch(Integer.MAX_VALUE);
        BaseMetaTileEntity base = attach(hatch, hatch.maxEUStore(), new ArrayList<>());
        assertFalse(machine(true, hatch).addEnergyOutputMultipleDynamos(32L * Integer.MAX_VALUE, true));
        verify(base, never()).increaseStoredEnergyUnits(anyLong(), anyBoolean());
    }

    @Test
    void genericExoticHatchesKeepOfferedAccounting() {
        MTEHatchDynamoMulti full = hatch(4);
        MTEHatchDynamoMulti empty = hatch(4);
        BaseMetaTileEntity fullBase = attach(full, full.maxEUStore(), new ArrayList<>());
        attach(empty, 0, new ArrayList<>());
        MTEMultiBlockBase machine = machine(false);
        machine.mExoticDynamoHatches.add(full);
        machine.mExoticDynamoHatches.add(null);
        machine.mExoticDynamoHatches.add(empty);
        assertTrue(machine.addEnergyOutputMultipleDynamos(160, true));
        assertEquals(32, empty.getEUVar());
        assertEquals(List.of(full, empty), machine.mExoticDynamoHatches);
        verify(fullBase, never()).increaseStoredEnergyUnits(32, false);
    }

    @BeforeAll
    static void initializeWithoutForgeLauncher() throws Exception {
        try (var utility = mockStatic(GTUtility.class, CALLS_REAL_METHODS)) {
            utility.when(GTUtility::isClient)
                .thenReturn(false);
            Class.forName(MTELargeRocketEngine.class.getName());
        }
    }

    private static MTEMultiBlockBase machine(boolean rocket, MTEHatchDynamo... hatches) {
        MTEMultiBlockBase machine = rocket ? mock(MTELargeRocketEngine.class, CALLS_REAL_METHODS)
            : mock(MTEMultiBlockBase.class, CALLS_REAL_METHODS);
        machine.mDynamoHatches = new ArrayList<>(List.of(hatches));
        machine.mExoticDynamoHatches = new ArrayList<>();
        if (rocket) ((MTELargeRocketEngine) machine).mAllDynamoHatches = new ArrayList<>(List.of(hatches));
        doNothing().when(machine)
            .explodeMultiblock();
        return machine;
    }

    private static BaseMetaTileEntity attach(MTEHatchDynamo hatch, long stored, List<Long> dirtyBalances) {
        BaseMetaTileEntity base = spy(new BaseMetaTileEntity());
        hatch.setBaseMetaTileEntity(base);
        hatch.setEUVar(stored);
        World world = mock(World.class);
        Chunk chunk = mock(Chunk.class);
        when(world.getChunkFromBlockCoords(0, 0)).thenReturn(chunk);
        doAnswer(call -> {
            dirtyBalances.add(hatch.getEUVar());
            return null;
        }).when(chunk)
            .setChunkModified();
        base.setWorldObj(world);
        clearInvocations(base);
        return base;
    }

    private static MTEHatchDynamoMulti hatch(int amps) {
        return new MTEHatchDynamoMulti("dynamo-test", 1, amps, new String[0], null);
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void packetBoundariesMatchProductionStorageLoop(boolean rocket) {
        for (long initial : new long[] { -1, 0, 991, 992, 993, 1023, 1024, 1025 }) {
            for (long offer : new long[] { 0, 1, 32, 65, 128 }) {
                MTEHatchDynamoMulti actual = hatch(4);
                MTEHatchDynamoMulti reference = hatch(4);
                List<Long> actualDirty = new ArrayList<>();
                List<Long> referenceDirty = new ArrayList<>();
                BaseMetaTileEntity actualBase = attach(actual, initial, actualDirty);
                BaseMetaTileEntity referenceBase = attach(reference, initial, referenceDirty);
                int whole = (int) (offer / 32);
                int remainder = (int) (offer % 32);
                for (int i = 0; i < whole; i++) referenceBase.increaseStoredEnergyUnits(32, false);
                if (whole < 4 && (rocket || remainder > 0)) referenceBase.increaseStoredEnergyUnits(remainder, false);
                assertEquals(offer > 0, machine(rocket, actual).addEnergyOutputMultipleDynamos(offer, true));
                assertEquals(reference.getEUVar(), actual.getEUVar(), "initial=" + initial + ", offer=" + offer);
                assertEquals(referenceDirty, actualDirty);
                if (initial < actual.maxEUStore()) {
                    int calls = whole + (whole < 4 && (rocket || remainder > 0) ? 1 : 0);
                    verify(actualBase, times(calls)).increaseStoredEnergyUnits(anyLong(), eq(false));
                }
            }
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void fullHighAmpHatchWorkCount(boolean rocket) {
        for (int amps : new int[] { 4, 16, 64, 4096 }) {
            MTEHatchDynamoMulti hatch = hatch(amps);
            List<Long> dirty = new ArrayList<>();
            BaseMetaTileEntity base = attach(hatch, hatch.maxEUStore(), dirty);
            assertTrue(machine(rocket, hatch).addEnergyOutputMultipleDynamos(32L * amps, true));
            assertEquals(hatch.maxEUStore(), hatch.getEUVar());
            assertTrue(dirty.isEmpty());
            verify(base, never()).increaseStoredEnergyUnits(32, false);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void acceptedDirtyCallbacksSeeEachPriorBalanceAndCanChangeAmps(boolean rocket) {
        MTEHatchDynamoMulti hatch = hatch(4);
        List<Long> dirty = new ArrayList<>();
        BaseMetaTileEntity base = attach(hatch, 0, dirty);
        World world = mock(World.class);
        Chunk chunk = mock(Chunk.class);
        when(world.getChunkFromBlockCoords(0, 0)).thenReturn(chunk);
        doAnswer(call -> {
            dirty.add(hatch.getEUVar());
            hatch.Amperes = 2;
            return null;
        }).when(chunk)
            .setChunkModified();
        base.setWorldObj(world);
        assertTrue(machine(rocket, hatch).addEnergyOutputMultipleDynamos(128, true));
        assertEquals(rocket ? List.of(0L, 32L) : List.of(0L, 32L, 64L, 96L), dirty);
        assertEquals(rocket ? 64 : 128, hatch.getEUVar());
    }

    static class ExternalDynamo extends MTEHatchDynamoMulti {

        int reads;

        ExternalDynamo() {
            super("external-test", 1, 4, new String[0], null);
        }

        @Override
        public long getEUVar() {
            reads++;
            return super.getEUVar();
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void externalHatchKeepsRejectedCallsAndReads(boolean rocket) {
        ExternalDynamo hatch = new ExternalDynamo();
        BaseMetaTileEntity base = attach(hatch, hatch.maxEUStore(), new ArrayList<>());
        hatch.reads = 0;
        assertTrue(machine(rocket, hatch).addEnergyOutputMultipleDynamos(128, true));
        verify(base, times(4)).increaseStoredEnergyUnits(32, false);
        assertEquals(4, hatch.reads);
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void mixedHatchesRetainOfferedAccountingAndInvalidRemoval(boolean rocket) {
        MTEHatchDynamoMulti full = hatch(4);
        MTEHatchDynamoMulti empty = hatch(4);
        MTEHatchDynamoMulti invalid = hatch(4);
        attach(full, full.maxEUStore(), new ArrayList<>());
        attach(empty, 0, new ArrayList<>());
        MTEMultiBlockBase machine = machine(rocket, full, invalid, empty);
        assertTrue(machine.addEnergyOutputMultipleDynamos(160, true));
        assertEquals(full.maxEUStore(), full.getEUVar());
        assertEquals(32, empty.getEUVar());
        List<?> hatches = rocket ? ((MTELargeRocketEngine) machine).mAllDynamoHatches : machine.mDynamoHatches;
        assertEquals(List.of(full, empty), hatches);
        MTEHatchDynamo otherVoltage = new MTEHatchDynamo("other", 2, new String[0], null);
        attach(otherVoltage, 0, new ArrayList<>());
        machine = machine(rocket, full, otherVoltage);
        assertFalse(machine.addEnergyOutputMultipleDynamos(32, false));
        verify(machine).explodeMultiblock();
    }
}
