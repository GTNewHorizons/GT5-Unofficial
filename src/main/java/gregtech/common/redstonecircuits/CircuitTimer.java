package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitTimer extends CircuitryBehavior {

    public CircuitTimer(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 2;
        aCircuitData[1] = 1;
        aCircuitData[2] = 2;
        aCircuitData[4] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 2) {
            aCircuitData[0] = 2;
        }
        if (aCircuitData[1] < 1) {
            aCircuitData[1] = 1;
        }
        if (aCircuitData[2] < 2) {
            aCircuitData[2] = 2;
        }
        if (aCircuitData[3] < 0) {
            aCircuitData[3] = 0;
        }
        if (aCircuitData[3] > 1) {
            aCircuitData[3] = 1;
        }
        if (aCircuitData[4] < 0) {
            aCircuitData[4] = 0;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[3] == 1) {
            if (getAnyRedstone(aRedstoneCircuitBlock)) {
                aCircuitData[4] += 1;
            } else {
                aCircuitData[4] = 0;
            }
        } else if (getAnyRedstone(aRedstoneCircuitBlock)) {
            aCircuitData[4] = 0;
        } else {
            aCircuitData[4] += 1;
        }
        if (aCircuitData[4] >= aCircuitData[0]) {
            if (aCircuitData[1] > 1) {
                if (aCircuitData[4] >= aCircuitData[0] + (aCircuitData[1] - 1) * aCircuitData[2]) {
                    aRedstoneCircuitBlock.setRedstone((byte) 15, aRedstoneCircuitBlock.getOutputFacing());
                    aCircuitData[4] = 0;
                } else {
                    aRedstoneCircuitBlock.setRedstone(
                        (byte) ((aCircuitData[4] - aCircuitData[0]) % aCircuitData[2] == 0 ? 15 : 0),
                        aRedstoneCircuitBlock.getOutputFacing());
                }
            } else {
                aRedstoneCircuitBlock.setRedstone((byte) 15, aRedstoneCircuitBlock.getOutputFacing());
                aCircuitData[4] = 0;
            }
        } else {
            aRedstoneCircuitBlock.setRedstone((byte) 0, aRedstoneCircuitBlock.getOutputFacing());
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.timer.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.timer.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> StatCollector.translateToLocal("GT5U.gui.circuit.data_description.delay");
            case 1 -> StatCollector.translateToLocal("GT5U.gui.circuit.timer.data_description.pulses");
            case 2 -> StatCollector.translateToLocal("GT5U.gui.circuit.data_description.length");
            case 3 -> StatCollector.translateToLocal(
                aCircuitData[aCircuitDataIndex] == 1 ? "GT5U.gui.circuit.data_description.rs_on"
                    : "GT5U.gui.circuit.data_description.rs_off");
            case 4 -> StatCollector.translateToLocal("GT5U.gui.circuit.timer.data_description.time");
            default -> "";
        };
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 3) {
            return "";
        }
        return null;
    }
}
