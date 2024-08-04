package gregtech.loaders.preload;

import gregtech.api.util.GT_Log;
import gregtech.common.misc.techtree.TechList;
import gregtech.common.misc.techtree.base.TechnologyBuilder;

public class GT_Loader_Technologies implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Loading researchable technologies");

        TechList.DebugTech = new TechnologyBuilder("DebugTech").unlocalizedName("gt.tech.debug.name")
            .buildAndRegister();
    }
}
