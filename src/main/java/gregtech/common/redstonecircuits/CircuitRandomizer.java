package gregtech.common.redstonecircuits;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitRandomizer extends CircuitryBehavior {

    public CircuitRandomizer(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 1;
        aCircuitData[4] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 1) {
            aCircuitData[0] = 1;
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
            aCircuitData[4] = 0;
            aRedstoneCircuitBlock
                .setRedstone((byte) aRedstoneCircuitBlock.getRandom(16), aRedstoneCircuitBlock.getOutputFacing());
        }
    }

    @Override
    public String getName() {
        return "Randomizer";
    }

    @Override
    public String getDescription() {
        return "Randomizes Redstone";
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> "Delay";
            case 3 -> aCircuitData[aCircuitDataIndex] == 1 ? "RS => ON" : "RS => OFF";
            case 4 -> "Status";
            default -> "";
        };
    }

    @Override
    public boolean displayItemStack(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock, int aIndex) {
        return false;
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex != 0) {
            return "";
        }
        return null;
    }
}
