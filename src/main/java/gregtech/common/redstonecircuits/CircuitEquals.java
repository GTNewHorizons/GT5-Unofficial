package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitEquals extends CircuitryBehavior {

    public CircuitEquals(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 0;
        aCircuitData[1] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 0) {
            aCircuitData[0] = 0;
        }
        if (aCircuitData[0] > 15) {
            aCircuitData[0] = 15;
        }
        if (aCircuitData[1] < 0) {
            aCircuitData[3] = 0;
        }
        if (aCircuitData[1] > 1) {
            aCircuitData[3] = 1;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aRedstoneCircuitBlock.setRedstone(
            ((byte) (((aCircuitData[1] != 0) == (getStrongestRedstone(aRedstoneCircuitBlock) == aCircuitData[0])) ? 0
                : 15)),
            aRedstoneCircuitBlock.getOutputFacing());
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.equals.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.equals.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> StatCollector.translateToLocal("GT5U.gui.circuit.equals.data_description.signal");
            case 1 -> StatCollector.translateToLocal(
                aCircuitData[1] == 0 ? "GT5U.gui.circuit.equals.data_description.equal"
                    : "GT5U.gui.circuit.equals.data_description.unequal");
            default -> "";
        };
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex > 0) {
            return "";
        }
        return null;
    }
}
