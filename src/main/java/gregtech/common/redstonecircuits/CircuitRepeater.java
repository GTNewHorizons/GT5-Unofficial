package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitRepeater extends CircuitryBehavior {

    public CircuitRepeater(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 1;
        aCircuitData[4] = 0;
        aCircuitData[5] = -1;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 1) {
            aCircuitData[0] = 1;
        }
        if (aCircuitData[4] < 0) {
            aCircuitData[4] = 0;
        }
        if (aCircuitData[5] < -1) {
            aCircuitData[5] = -1;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (getAnyRedstone(aRedstoneCircuitBlock)) {
            aCircuitData[4] += 1;
            if (aCircuitData[5] < 0) {
                aCircuitData[5] = 0;
            }
        }
        if ((aCircuitData[5] >= 0) && (aCircuitData[5] < aCircuitData[0])) {
            aCircuitData[5] += 1;
        }
        if (aCircuitData[4] > 0) {
            if (aCircuitData[5] >= aCircuitData[0]) {
                aCircuitData[4] -= 1;
                aRedstoneCircuitBlock.setRedstone((byte) 15, aRedstoneCircuitBlock.getOutputFacing());
            } else {
                aRedstoneCircuitBlock.setRedstone((byte) 0, aRedstoneCircuitBlock.getOutputFacing());
            }
        } else {
            aRedstoneCircuitBlock.setRedstone((byte) 0, aRedstoneCircuitBlock.getOutputFacing());
            aCircuitData[5] = -1;
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.repeater.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.repeater.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 0) {
            return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.delay");
        }
        return "";
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex > 0) {
            return "";
        }
        return null;
    }
}
