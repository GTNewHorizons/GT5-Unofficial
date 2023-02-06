package gregtech.loaders.postload;

import net.minecraft.init.Blocks;

import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;

public class GT_MinableRegistrator implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding Blocks to the Miners Valuable List.");
        GT_ModHandler.addValuableOre(Blocks.glowstone, 0, 1);
        GT_ModHandler.addValuableOre(Blocks.soul_sand, 0, 1);
    }
}
