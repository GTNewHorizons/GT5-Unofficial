package gregtech.loaders.postload;

import net.minecraft.init.Blocks;

import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;

public class MinableRegistrator implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Adding Blocks to the Miners Valuable List.");
        GTModHandler.addValuableOre(Blocks.glowstone, 0, 1);
        GTModHandler.addValuableOre(Blocks.soul_sand, 0, 1);
    }
}
