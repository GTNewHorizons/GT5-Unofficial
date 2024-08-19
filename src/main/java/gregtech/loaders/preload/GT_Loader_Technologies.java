package gregtech.loaders.preload;

import gregtech.api.util.GT_Log;
import gregtech.common.misc.techtree.TechList;
import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.base.Technology;
import gregtech.common.misc.techtree.base.TechnologyBuilder;
import gregtech.common.misc.techtree.interfaces.IPrerequisite;

public class GT_Loader_Technologies implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Loading researchable technologies");

        // Construct with constructor manually because we don't want to assign itself as a prerequisite and mess
        // everything up
        TechList.HiddenRoot = new Technology("Root", "gt.tech.root.name", new IPrerequisite[] {});
        TechnologyRegistry.register(TechList.HiddenRoot);

        TechList.TechA = new TechnologyBuilder("TechA").unlocalizedName("gt.tech.a.name")
            .buildAndRegister();

        TechList.TechB = new TechnologyBuilder("TechB").unlocalizedName("gt.tech.b.name")
            .buildAndRegister();

        TechList.TechC = new TechnologyBuilder("TechC").unlocalizedName("gt.tech.c.name")
            .buildAndRegister();
        TechList.TechD = new TechnologyBuilder("TechD").unlocalizedName("gt.tech.d.name")
            .buildAndRegister();
        TechList.TechE = new TechnologyBuilder("TechE").unlocalizedName("gt.tech.e.name")
            .prerequisites(TechList.TechB, TechList.TechC)
            .buildAndRegister();
        TechList.TechF = new TechnologyBuilder("TechF").unlocalizedName("gt.tech.f.name")
            .prerequisites(TechList.TechA, TechList.TechD)
            .buildAndRegister();
        TechList.TechG = new TechnologyBuilder("TechG").unlocalizedName("gt.tech.g.name")
            .prerequisites(TechList.TechF, TechList.TechC)
            .buildAndRegister();
    }
}
