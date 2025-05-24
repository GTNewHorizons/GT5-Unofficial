package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitCombinationLock extends CircuitryBehavior {

    public CircuitCombinationLock(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 1;
        aCircuitData[1] = 0;
        aCircuitData[2] = 0;
        aCircuitData[3] = 0;
        aCircuitData[4] = 0;
        aCircuitData[5] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 1) {
            aCircuitData[0] = 1;
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
        if (aCircuitData[0] > 15) {
            aCircuitData[0] = 15;
        }
        if (aCircuitData[1] > 15) {
            aCircuitData[1] = 15;
        }
        if (aCircuitData[2] > 15) {
            aCircuitData[2] = 15;
        }
        if (aCircuitData[3] > 15) {
            aCircuitData[3] = 15;
        }
        if (aCircuitData[4] < 0) {
            aCircuitData[4] = 0;
        }
        if (aCircuitData[4] > 3) {
            aCircuitData[4] = 3;
        }
        if (aCircuitData[5] < 0) {
            aCircuitData[5] = 0;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        while ((aCircuitData[aCircuitData[4]] == 0) && (aCircuitData[4] < 4)) {
            aCircuitData[4] += 1;
        }
        if (aCircuitData[4] < 4) {
            int tRedstone = getStrongestRedstone(aRedstoneCircuitBlock);
            if (tRedstone > 0) {
                if (aCircuitData[5] == 0) {
                    if (tRedstone == aCircuitData[aCircuitData[4]]) {
                        aCircuitData[4] += 1;
                    } else {
                        aCircuitData[4] = 0;
                    }
                }
                aCircuitData[5] = 1;
            } else {
                aCircuitData[5] = 0;
            }
            aRedstoneCircuitBlock.setRedstone((byte) 0, aRedstoneCircuitBlock.getOutputFacing());
        } else {
            aRedstoneCircuitBlock.setRedstone((byte) 15, aRedstoneCircuitBlock.getOutputFacing());
            aCircuitData[4] = 0;
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.combination_lock.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.combination_lock.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return StatCollector
            .translateToLocalFormatted("GT5U.gui.circuit.combination_lock.data_description", aCircuitDataIndex);
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        return null;
    }
}
