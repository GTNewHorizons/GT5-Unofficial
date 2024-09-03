package gregtech.common.redstonecircuits;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitRedstoneMeter extends CircuitryBehavior {

    public CircuitRedstoneMeter(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 1;
        aCircuitData[1] = 15;
        aCircuitData[2] = 0;
        aCircuitData[3] = 15;
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
            aCircuitData[1] = 0;
        }
        if (aCircuitData[1] > 15) {
            aCircuitData[1] = 15;
        }
        if (aCircuitData[1] < aCircuitData[0]) {
            aCircuitData[1] = aCircuitData[0];
        }
        if (aCircuitData[2] < 0) {
            aCircuitData[2] = 0;
        }
        if (aCircuitData[2] > 1) {
            aCircuitData[2] = 1;
        }
        if (aCircuitData[3] < 0) {
            aCircuitData[3] = 0;
        }
        if (aCircuitData[3] > 15) {
            aCircuitData[3] = 15;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        byte tRedstone = getStrongestRedstone(aRedstoneCircuitBlock);
        aRedstoneCircuitBlock
            .setRedstone(
                ((tRedstone >= aCircuitData[0]) && (tRedstone <= aCircuitData[1]) ? 1 : 0)
                    != (aCircuitData[2] != 0 ? 1 : 0) ? (byte) aCircuitData[3] : 0,
                aRedstoneCircuitBlock.getOutputFacing());
    }

    @Override
    public String getName() {
        return "Redstone Meter";
    }

    @Override
    public String getDescription() {
        return "Checks Boundaries";
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> "Lower";
            case 1 -> "Upper";
            case 2 -> "Invert:";
            case 3 -> "RS Out:";
            default -> "";
        };
    }

    @Override
    public boolean displayItemStack(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock, int aIndex) {
        return false;
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 2) {
            return aCircuitData[2] == 0 ? "OFF" : "ON";
        }
        return null;
    }
}
