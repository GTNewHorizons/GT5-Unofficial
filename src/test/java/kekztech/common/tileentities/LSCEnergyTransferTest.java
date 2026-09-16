package kekztech.common.tileentities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyTunnel;

class LSCEnergyTransferTest {

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
    void wirelessRebalanceAndMaintenanceStillPrecedeFinalNetDelta() throws Exception {
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
                () -> gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap(null, BigInteger.valueOf(50)));
        }
        assertTrue(lsc.getPassiveDischargeAmount() > 0);
        assertEquals(target.add(BigInteger.valueOf(10 - lsc.getPassiveDischargeAmount())), lsc.getStored());
        verify(base).injectEnergyUnits(ForgeDirection.UNKNOWN, 10, 1);
        verify(base).drainEnergyUnits(ForgeDirection.UNKNOWN, 50, 1);
        assertEquals(
            10,
            lsc.getEnergyInputValues()
                .avgLong());
        assertEquals(
            50,
            lsc.getEnergyOutputValues()
                .avgLong());
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
