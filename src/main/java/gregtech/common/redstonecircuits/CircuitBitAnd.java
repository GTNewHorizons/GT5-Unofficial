package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitBitAnd extends CircuitryBehavior {

    public CircuitBitAnd(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 0;
        aCircuitData[1] = 0;
        aCircuitData[2] = 0;
        aCircuitData[3] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 0) {
            aCircuitData[0] = 0;
        }
        if (aCircuitData[1] < 0) {
            aCircuitData[1] = 0;
        }
        if (aCircuitData[2] < 0) {
            aCircuitData[2] = 0;
        }
        if (aCircuitData[3] < 0) {
            aCircuitData[3] = 0;
        }
        if (aCircuitData[0] > 1) {
            aCircuitData[0] = 1;
        }
        if (aCircuitData[1] > 1) {
            aCircuitData[1] = 1;
        }
        if (aCircuitData[2] > 1) {
            aCircuitData[2] = 1;
        }
        if (aCircuitData[3] > 1) {
            aCircuitData[3] = 1;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aRedstoneCircuitBlock.setRedstone(
            (byte) ((getStrongestRedstone(aRedstoneCircuitBlock)
                & (aCircuitData[0] | aCircuitData[1] << 1 | aCircuitData[2] << 2 | aCircuitData[3] << 3)) != 0 ? 15
                    : 0),
            aRedstoneCircuitBlock.getOutputFacing());
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.bit_and.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.bit_and.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return StatCollector.translateToLocalFormatted("GT5U.gui.circuit.bit_and.data_description", aCircuitDataIndex);
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        return aCircuitData[aCircuitDataIndex] == 0
            ? StatCollector.translateToLocal("GT5U.gui.circuit.generic.data_display.off")
            : StatCollector.translateToLocal("GT5U.gui.circuit.generic.data_display.on");
    }
}
