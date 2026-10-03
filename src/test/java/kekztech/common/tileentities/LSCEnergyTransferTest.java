package kekztech.common.tileentities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

class LSCEnergyTransferTest {

    @Test
    void rejectedHatchesLeaveBudgetsForLaterHatches() {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        doReturn(mock(IGregTechTileEntity.class)).when(lsc)
            .getBaseMetaTileEntity();
        lsc.setStored(BigInteger.valueOf(50));
        lsc.setCapacity(BigInteger.valueOf(60));
        MTEHatchEnergy empty = mock(MTEHatchEnergy.class);
        MTEHatchEnergy ready = mock(MTEHatchEnergy.class);
        for (MTEHatchEnergy input : java.util.List.of(empty, ready)) {
            when(input.isValid()).thenReturn(true);
            when(input.maxEUInput()).thenReturn(40L);
            when(input.maxAmperesIn()).thenReturn(1L);
            lsc.mEnergyHatches.add(input);
        }
        when(ready.getEUVar()).thenReturn(100L);
        MTEHatchDynamo full = mock(MTEHatchDynamo.class);
        MTEHatchDynamo available = mock(MTEHatchDynamo.class);
        for (MTEHatchDynamo output : java.util.List.of(full, available)) {
            when(output.isValid()).thenReturn(true);
            when(output.maxEUOutput()).thenReturn(40L);
            when(output.maxAmperesOut()).thenReturn(1L);
            when(output.maxEUStore()).thenReturn(100L);
            lsc.mDynamoHatches.add(output);
        }
        when(full.getEUVar()).thenReturn(100L);
        lsc.onRunningTick(null);
        verify(empty, never()).setEUVar(anyLong());
        verify(full, never()).setEUVar(anyLong());
        verify(ready).setEUVar(90);
        verify(available).setEUVar(40);
        assertEquals(BigInteger.valueOf(20), lsc.getStored());
    }

    @Test
    @SuppressWarnings("unchecked")
    void allSixHatchLoopsShareRemainingBudgets() throws Exception {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        IGregTechTileEntity base = mock(IGregTechTileEntity.class);
        doReturn(base).when(lsc)
            .getBaseMetaTileEntity();
        BigInteger capacity = spy(BigInteger.valueOf(60));
        lsc.setStored(BigInteger.valueOf(50));
        lsc.setCapacity(capacity);
        MTEHatchEnergy input = mock(MTEHatchEnergy.class);
        MTEHatchEnergyMulti multi = mock(MTEHatchEnergyMulti.class);
        MTEHatchEnergyTunnel tunnel = mock(MTEHatchEnergyTunnel.class);
        when(tunnel.getAmperes()).thenReturn(1);
        for (var hatch : java.util.List.of(input, multi, tunnel)) {
            when(hatch.isValid()).thenReturn(true);
            when(hatch.maxEUInput()).thenReturn(40L);
            when(hatch.maxAmperesIn()).thenReturn(1L);
            when(hatch.getEUVar()).thenReturn(100L);
        }
        MTEHatchDynamo output = mock(MTEHatchDynamo.class);
        MTEHatchDynamoMulti multiOutput = mock(MTEHatchDynamoMulti.class);
        MTEHatchDynamoTunnel tunnelOutput = mock(MTEHatchDynamoTunnel.class);
        tunnelOutput.Amperes = 1;
        for (var hatch : java.util.List.of(output, multiOutput, tunnelOutput)) {
            when(hatch.isValid()).thenReturn(true);
            when(hatch.maxEUOutput()).thenReturn(40L);
            when(hatch.maxAmperesOut()).thenReturn(1L);
            when(hatch.maxEUStore()).thenReturn(100L);
        }
        lsc.mEnergyHatches.add(input);
        lsc.mDynamoHatches.add(output);
        String[] fields = { "mEnergyHatchesTT", "mEnergyTunnelsTT", "mDynamoHatchesTT", "mDynamoTunnelsTT" };
        Object[] hatches = { multi, tunnel, multiOutput, tunnelOutput };
        for (int i = 0; i < fields.length; i++) {
            var field = MTELapotronicSuperCapacitor.class.getDeclaredField(fields[i]);
            field.setAccessible(true);
            ((java.util.Set<Object>) field.get(lsc)).add(hatches[i]);
        }
        lsc.onRunningTick(null);
        verify(input).setEUVar(90);
        verify(multi).setEUVar(100);
        verify(tunnel).setEUVar(100);
        verify(output).setEUVar(40);
        verify(multiOutput).setEUVar(10);
        verify(tunnelOutput).setEUVar(0);
        verify(base).injectEnergyUnits(ForgeDirection.UNKNOWN, 10, 1);
        verify(base).drainEnergyUnits(ForgeDirection.UNKNOWN, 50, 1);
        verify(capacity, times(1)).subtract(BigInteger.valueOf(50));
        assertEquals(BigInteger.TEN, lsc.getStored());
    }

    @Test
    void wirelessRebalanceIncludesHatchInputBeforeMaintenanceLoss() throws Exception {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        IGregTechTileEntity base = mock(IGregTechTileEntity.class);
        doReturn(base).when(lsc)
            .getBaseMetaTileEntity();
        var capacitors = MTELapotronicSuperCapacitor.class.getDeclaredField("capacitors");
        capacitors.setAccessible(true);
        ((int[]) capacitors.get(lsc))[4] = 1;
        BigInteger target = kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit.LSC_wireless_eu_cap;
        lsc.setCapacity(target.add(BigInteger.valueOf(100)));
        lsc.setStored(target.add(BigInteger.valueOf(50)));
        lsc.setWireless_mode(true);
        lsc.setCounter(Integer.MAX_VALUE - 1);
        lsc.mWrench = true;
        MTEHatchEnergy input = mock(MTEHatchEnergy.class);
        when(input.isValid()).thenReturn(true);
        when(input.maxEUInput()).thenReturn(10L);
        when(input.maxAmperesIn()).thenReturn(1L);
        when(input.getEUVar()).thenReturn(100L);
        lsc.mEnergyHatches.add(input);
        try (var wireless = mockStatic(gregtech.common.misc.WirelessNetworkManager.class)) {
            wireless.when(
                () -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(any(), any(BigInteger.class)))
                .thenReturn(true);
            lsc.onRunningTick(null);
            wireless.verify(
                () -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(null, BigInteger.valueOf(60)));
        }
        assertTrue(lsc.getPassiveDischargeAmount() > 0);
        assertEquals(target.subtract(BigInteger.valueOf(lsc.getPassiveDischargeAmount())), lsc.getStored());
        verify(base).injectEnergyUnits(ForgeDirection.UNKNOWN, 10, 1);
        verify(base).drainEnergyUnits(ForgeDirection.UNKNOWN, 60, 1);
        assertEquals(
            10,
            lsc.getEnergyInputValues()
                .avgLong());
        assertEquals(
            60,
            lsc.getEnergyOutputValues()
                .avgLong());
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    void wirelessRebalanceConservesEnergyAfterHatchesEmptyStorage(boolean accepted) throws Exception {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        doReturn(mock(IGregTechTileEntity.class)).when(lsc)
            .getBaseMetaTileEntity();
        var capacitors = MTELapotronicSuperCapacitor.class.getDeclaredField("capacitors");
        capacitors.setAccessible(true);
        ((int[]) capacitors.get(lsc))[4] = 1;
        BigInteger target = kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit.LSC_wireless_eu_cap;
        BigInteger starting = target.add(BigInteger.valueOf(1_000_000_000_000L));
        lsc.setCapacity(starting);
        lsc.setStored(starting);
        lsc.setWireless_mode(true);
        lsc.setCounter(Integer.MAX_VALUE - 1);
        MTEHatchDynamo output = mock(MTEHatchDynamo.class);
        when(output.isValid()).thenReturn(true);
        when(output.maxEUOutput()).thenReturn(starting.longValueExact());
        when(output.maxAmperesOut()).thenReturn(1L);
        when(output.maxEUStore()).thenReturn(Long.MAX_VALUE);
        AtomicLong hatch = new AtomicLong();
        when(output.getEUVar()).thenAnswer(call -> hatch.get());
        doAnswer(call -> {
            hatch.set(call.getArgument(0));
            return null;
        }).when(output)
            .setEUVar(anyLong());
        lsc.mDynamoHatches.add(output);
        BigInteger initialWireless = target.multiply(BigInteger.TWO);
        AtomicReference<BigInteger> wirelessBalance = new AtomicReference<>(initialWireless);
        try (var wireless = mockStatic(gregtech.common.misc.WirelessNetworkManager.class)) {
            wireless.when(
                () -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(any(), any(BigInteger.class)))
                .thenAnswer(call -> {
                    if (accepted) wirelessBalance.updateAndGet(balance -> balance.add(call.getArgument(1)));
                    return accepted;
                });
            lsc.onRunningTick(null);
            wireless.verify(
                () -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(null, target.negate()));
        }
        long lost = accepted ? lsc.getPassiveDischargeAmount() : 0;
        assertEquals(
            accepted ? target.longValueExact() : 0,
            lsc.getEnergyInputValues()
                .avgLong());
        assertEquals(
            starting.longValueExact(),
            lsc.getEnergyOutputValues()
                .avgLong());
        assertEquals(accepted ? target.subtract(BigInteger.valueOf(lost)) : BigInteger.ZERO, lsc.getStored());
        assertEquals(starting.longValueExact(), hatch.get());
        assertEquals(
            starting.add(initialWireless),
            lsc.getStored()
                .add(wirelessBalance.get())
                .add(BigInteger.valueOf(hatch.get()))
                .add(BigInteger.valueOf(lost)));
    }

    @ParameterizedTest
    @CsvSource({ "10000000000000000000, 9223372036854775807", "-10000000000000000000, 9223372036854775807",
        "9223372036854775802, 9223372036854775807", "-9223372036854775802, 9223372036854775807", "100, 110",
        "-100, 110" })
    void wirelessStatisticsSaturateWithoutChangingTransferredEnergy(String amount, long expectedStat) throws Exception {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        IGregTechTileEntity base = mock(IGregTechTileEntity.class);
        doReturn(base).when(lsc)
            .getBaseMetaTileEntity();
        var capacitors = MTELapotronicSuperCapacitor.class.getDeclaredField("capacitors");
        capacitors.setAccessible(true);
        ((int[]) capacitors.get(lsc))[9] = 1;
        BigInteger target = kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit.UMV_wireless_eu_cap;
        BigInteger transferred = new BigInteger(amount);
        lsc.setCapacity(kekztech.common.itemBlocks.ItemBlockLapotronicEnergyUnit.UMV_cap_storage);
        lsc.setStored(target.add(transferred));
        lsc.setWireless_mode(true);
        lsc.setCounter(Integer.MAX_VALUE - 1);

        // Equal hatch transfers keep storage unchanged while both statistics already contain energy.
        MTEHatchEnergy input = mock(MTEHatchEnergy.class);
        when(input.isValid()).thenReturn(true);
        when(input.maxEUInput()).thenReturn(10L);
        when(input.maxAmperesIn()).thenReturn(1L);
        when(input.getEUVar()).thenReturn(100L);
        lsc.mEnergyHatches.add(input);
        MTEHatchDynamo output = mock(MTEHatchDynamo.class);
        when(output.isValid()).thenReturn(true);
        when(output.maxEUOutput()).thenReturn(10L);
        when(output.maxAmperesOut()).thenReturn(1L);
        when(output.maxEUStore()).thenReturn(100L);
        lsc.mDynamoHatches.add(output);
        try (var wireless = mockStatic(gregtech.common.misc.WirelessNetworkManager.class)) {
            wireless.when(() -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(null, transferred))
                .thenReturn(true);
            assertTrue(lsc.onRunningTick(null));
            wireless
                .verify(() -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(null, transferred));
        }
        verify(input).setEUVar(90);
        verify(output).setEUVar(10);
        assertEquals(target.subtract(BigInteger.valueOf(lsc.getPassiveDischargeAmount())), lsc.getStored());
        long expectedInput = transferred.signum() < 0 ? expectedStat : 10;
        long expectedOutput = transferred.signum() > 0 ? expectedStat : 10;
        assertEquals(
            expectedInput,
            lsc.getEnergyInputValues()
                .avgLong());
        assertEquals(
            expectedOutput,
            lsc.getEnergyOutputValues()
                .avgLong());
        verify(base).injectEnergyUnits(ForgeDirection.UNKNOWN, expectedInput, 1);
        verify(base).drainEnergyUnits(ForgeDirection.UNKNOWN, expectedOutput, 1);
    }

    @org.junit.jupiter.api.BeforeAll
    static void initializeWithoutForgeLauncher() throws Exception {
        net.minecraft.block.Block previous = kekztech.common.Blocks.lscLapotronicEnergyUnit;
        try (var utility = mockStatic(gregtech.api.util.GTUtility.class)) {
            utility.when(() -> gregtech.api.util.GTUtility.powInt(anyInt(), anyInt()))
                .thenCallRealMethod();
            kekztech.common.Blocks.lscLapotronicEnergyUnit = mock(net.minecraft.block.Block.class);
            Class.forName("kekztech.common.tileentities.MTELapotronicSuperCapacitor");
        } finally {
            kekztech.common.Blocks.lscLapotronicEnergyUnit = previous;
        }
    }

    @Test
    void remainingBudgetsPreserveLargeBalances() {
        for (BigInteger stored : new BigInteger[] { BigInteger.ZERO, BigInteger.valueOf(50), BigInteger.valueOf(100),
            BigInteger.valueOf(110), BigInteger.valueOf(Long.MAX_VALUE)
                .multiply(BigInteger.TEN) }) {
            BigInteger capacity = stored.bitLength() > 63 ? stored.add(BigInteger.TEN) : BigInteger.valueOf(100);
            MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
            IGregTechTileEntity base = mock(IGregTechTileEntity.class);
            doReturn(base).when(lsc)
                .getBaseMetaTileEntity();
            lsc.setStored(stored);
            lsc.setCapacity(capacity);
            long drawBudget = stored.compareTo(capacity) >= 0 ? 0
                : capacity.subtract(stored)
                    .min(BigInteger.valueOf(80))
                    .longValue();
            long pushBudget = stored.min(BigInteger.valueOf(80))
                .longValue();
            for (int i = 0; i < 2; i++) {
                MTEHatchEnergy input = mock(MTEHatchEnergy.class);
                when(input.isValid()).thenReturn(true);
                when(input.maxEUInput()).thenReturn(40L);
                when(input.maxAmperesIn()).thenReturn(1L);
                when(input.getEUVar()).thenReturn(100L);
                lsc.mEnergyHatches.add(input);
                MTEHatchDynamo output = mock(MTEHatchDynamo.class);
                when(output.isValid()).thenReturn(true);
                when(output.maxEUOutput()).thenReturn(40L);
                when(output.maxAmperesOut()).thenReturn(1L);
                when(output.maxEUStore()).thenReturn(100L);
                lsc.mDynamoHatches.add(output);
            }
            assertTrue(lsc.onRunningTick(null));
            for (int i = 0; i < 2; i++) {
                long draw = Math.min(40, drawBudget - 40L * i);
                long push = Math.min(40, pushBudget - 40L * i);
                verify(lsc.mEnergyHatches.get(i)).setEUVar(100 - Math.max(0, draw));
                verify(lsc.mDynamoHatches.get(i)).setEUVar(Math.max(0, push));
            }
            assertEquals(
                stored.add(BigInteger.valueOf(drawBudget - pushBudget - lsc.getPassiveDischargeAmount()))
                    .max(BigInteger.ZERO),
                lsc.getStored());
            verify(base).injectEnergyUnits(ForgeDirection.UNKNOWN, drawBudget, 1);
            verify(base).drainEnergyUnits(ForgeDirection.UNKNOWN, pushBudget, 1);
        }
    }

    @Test
    void fullInputStillReturnsZeroForNegativeOverflowedWattage() {
        MTELapotronicSuperCapacitor lsc = spy(new MTELapotronicSuperCapacitor("energy-test"));
        doReturn(mock(IGregTechTileEntity.class)).when(lsc)
            .getBaseMetaTileEntity();
        lsc.setStored(BigInteger.TEN);
        lsc.setCapacity(BigInteger.TEN);
        MTEHatchEnergy input = mock(MTEHatchEnergy.class);
        when(input.isValid()).thenReturn(true);
        when(input.maxEUInput()).thenReturn(Long.MAX_VALUE);
        when(input.maxAmperesIn()).thenReturn(2L);
        AtomicLong energy = new AtomicLong(100);
        when(input.getEUVar()).thenAnswer(call -> energy.get());
        doAnswer(call -> {
            energy.set(call.getArgument(0));
            return null;
        }).when(input)
            .setEUVar(anyLong());
        lsc.mEnergyHatches.add(input);
        lsc.onRunningTick(null);
        assertEquals(100, energy.get());
        assertEquals(BigInteger.TEN, lsc.getStored());
    }
}
