package gregtech.common.redstonecircuits;

import net.minecraft.util.StatCollector;

import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.util.CircuitryBehavior;

public class CircuitBasicLogic extends CircuitryBehavior {

    public CircuitBasicLogic(int aIndex) {
        super(aIndex);
    }

    @Override
    public void initParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        aCircuitData[0] = 0;
    }

    @Override
    public void validateParameters(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 0) {
            aCircuitData[0] = 0;
        }
        if (aCircuitData[0] > 13) {
            aCircuitData[0] = 13;
        }
    }

    @Override
    public void onTick(int[] aCircuitData, IRedstoneCircuitBlock aRedstoneCircuitBlock) {
        if (aCircuitData[0] < 2) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) (aCircuitData[0] % 2 == (getAnyRedstone(aRedstoneCircuitBlock) ? 0 : 1) ? 15 : 0),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 4) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) (aCircuitData[0] % 2 == (getOneRedstone(aRedstoneCircuitBlock) ? 0 : 1) ? 15 : 0),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 6) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) (aCircuitData[0] % 2 == (getAllRedstone(aRedstoneCircuitBlock) ? 0 : 1) ? 15 : 0),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 7) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) (15 - getStrongestRedstone(aRedstoneCircuitBlock)),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 9) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) ((aCircuitData[0] % 2 == 0 ? 15 : 0)
                    ^ (getStrongestRedstone(aRedstoneCircuitBlock) | getWeakestRedstone(aRedstoneCircuitBlock))),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 11) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) ((aCircuitData[0] % 2 == 0 ? 15 : 0) ^ getStrongestRedstone(aRedstoneCircuitBlock)
                    ^ getWeakestRedstone(aRedstoneCircuitBlock)),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 13) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) ((aCircuitData[0] % 2 == 0 ? 15 : 0)
                    ^ getStrongestRedstone(aRedstoneCircuitBlock) & getWeakestRedstone(aRedstoneCircuitBlock)),
                aRedstoneCircuitBlock.getOutputFacing());
        } else if (aCircuitData[0] < 14) {
            aRedstoneCircuitBlock.setRedstone(
                (byte) (getStrongestRedstone(aRedstoneCircuitBlock) ^ 0xF),
                aRedstoneCircuitBlock.getOutputFacing());
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.basic.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("GT5U.gui.circuit.basic.description");
    }

    @Override
    public String getDataDescription(int[] aCircuitData, int aCircuitDataIndex) {
        if (aCircuitDataIndex == 0) {
            switch (aCircuitData[0]) {
                case 0 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.or");
                }
                case 1 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.nor");
                }
                case 2 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.xor");
                }
                case 3 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.xnor");
                }
                case 4 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.and");
                }
                case 5 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.nand");
                }
                case 6 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.invert");
                }
                case 7 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_or");
                }
                case 8 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_nor");
                }
                case 9 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_xor");
                }
                case 10 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_xnor");
                }
                case 11 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_and");
                }
                case 12 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_nand");
                }
                case 13 -> {
                    return StatCollector.translateToLocal("GT5U.gui.circuit.data_description.bit_invert");
                }
            }
        }
        return "";
    }

    @Override
    public String getDataDisplay(int[] aCircuitData, int aCircuitDataIndex) {
        return "";
    }
}
