package gregtech.loaders.preload;

import gregtech.api.util.GTLog;
import gregtech.common.redstonecircuits.CircuitBasicLogic;
import gregtech.common.redstonecircuits.CircuitBitAnd;
import gregtech.common.redstonecircuits.CircuitCombinationLock;
import gregtech.common.redstonecircuits.CircuitEquals;
import gregtech.common.redstonecircuits.CircuitPulser;
import gregtech.common.redstonecircuits.CircuitRandomizer;
import gregtech.common.redstonecircuits.CircuitRedstoneMeter;
import gregtech.common.redstonecircuits.CircuitRepeater;
import gregtech.common.redstonecircuits.CircuitTimer;

public class LoaderCircuitBehaviors implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Register Redstone Circuit behaviours.");
        new CircuitTimer(0);
        new CircuitBasicLogic(1);
        new CircuitRepeater(2);
        new CircuitPulser(3);
        new CircuitRedstoneMeter(4);

        new CircuitRandomizer(8);

        new CircuitCombinationLock(16);
        new CircuitBitAnd(17);
        new CircuitEquals(18);
    }
}
