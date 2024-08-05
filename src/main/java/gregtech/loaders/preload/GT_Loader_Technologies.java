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

        TechList.DebugTech2 = new TechnologyBuilder("DebugTech2").unlocalizedName("gt.tech.debug2.name")
            .prerequisites(TechList.DebugTech)
            .buildAndRegister();

        TechList.DebugTech3 = new TechnologyBuilder("DebugTech3").unlocalizedName("gt.tech.debug3.name")
            .prerequisites(TechList.DebugTech)
            .buildAndRegister();
    }
}
