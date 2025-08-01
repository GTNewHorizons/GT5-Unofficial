package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

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
        return StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        return switch (aCircuitDataIndex) {
            case 0 -> StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.data_description.lower");
            case 1 -> StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.data_description.upper");
            case 2 -> StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.data_description.invert");
            case 3 -> StatCollector.translateToLocal("GT5U.gui.circuit.redstone_meter.data_description.rs_out");
            default -> "";
        };
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 2) {
            return StatCollector.translateToLocal(
                aCircuitData[2] == 0 ? "GT5U.gui.circuit.generic.data_display.off"
                    : "GT5U.gui.circuit.generic.data_display.on");
        }
        return null;
    }
}
