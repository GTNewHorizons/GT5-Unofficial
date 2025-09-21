package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitPulser extends CircuitryBehavior {

    public CircuitPulser(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 1;
        aCircuitData[1] = 16;
        aCircuitData[4] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 1) {
            aCircuitData[0] = 1;
        }
        if (aCircuitData[1] < 0) {
            aCircuitData[1] = 0;
        }
        if (aCircuitData[1] > 16) {
            aCircuitData[1] = 16;
        }
        if (aCircuitData[4] < 0) {
            aCircuitData[4] = 0;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        byte redstoneSignal = aCircuitData[1] == 0 ? getWeakestNonZeroRedstone(aRedstoneCircuitBlock)
            : getStrongestRedstone(aRedstoneCircuitBlock);
        if (aCircuitData[4] == 0) {
            aCircuitData[5] = redstoneSignal;
        }
        if ((redstoneSignal > 0) || (aCircuitData[4] > 0)) {
            int index = 4;
            int tmp42_41 = aCircuitData[index];
            aCircuitData[index] = (tmp42_41 + 1);
            if ((tmp42_41 >= aCircuitData[0]) && (redstoneSignal <= 0)) {
                aCircuitData[4] = 0;
            }
        }
        aRedstoneCircuitBlock.setRedstone(
            (aCircuitData[4] > 0) && (aCircuitData[4] <= aCircuitData[0]) ? (byte) aCircuitData[1]
                : (aCircuitData[1] <= 0) || (aCircuitData[1] > 15) ? (byte) aCircuitData[5] : 0,
            aRedstoneCircuitBlock.getOutputFacing());
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.pulser.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.pulser.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> StatCollector.translateToLocal("GT5U.gui.circuit.data_description.length");
            case 1 -> StatCollector.translateToLocal("GT5U.gui.circuit.pulser.data_description.rs_out");
            default -> "";
        };
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 1) {
            if (aCircuitData[aCircuitDataIndex] == 16) {
                return StatCollector.translateToLocal("GT5U.gui.circuit.pulser.data_display.highest");
            }
            if (aCircuitData[aCircuitDataIndex] == 0) {
                return StatCollector.translateToLocal("GT5U.gui.circuit.pulser.data_display.lowest");
            }
        }
        return aCircuitDataIndex > 1 ? "" : null;
    }
}
