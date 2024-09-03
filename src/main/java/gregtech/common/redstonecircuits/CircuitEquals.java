package gregtech.common.redstonecircuits;

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
        return "Equals";
    }

    @Override
    public String getDescription() {
        return "signal == this";
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> "Signal";
            case 1 -> aCircuitData[1] == 0 ? "Equal" : "Unequal";
            default -> "";
        };
    }

    @Override
    public boolean displayItemStack(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock, int aIndex) {
        return false;
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex > 0) {
            return "";
        }
        return null;
    }
}
