package gregtech.common.redstonecircuits;

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
        return "Timer";
    }

    @Override
    public String getDescription() {
        return "Pulses Redstone";
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> "Delay";
            case 1 -> "Pulses";
            case 2 -> "Length";
            case 3 -> aCircuitData[aCircuitDataIndex] == 1 ? "RS => ON" : "RS => OFF";
            case 4 -> "Time";
            default -> "";
        };
    }

    @Override
    public boolean displayItemStack(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock, int aIndex) {
        return false;
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 3) {
            return "";
        }
        return null;
    }
}
