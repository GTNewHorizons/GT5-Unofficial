package gregtech.loaders.preload;

import gregtech.GT_Mod;
import gregtech.common.misc.techtree.TechList;
import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.base.Technology;
import gregtech.common.misc.techtree.base.TechnologyBuilder;
import gregtech.common.misc.techtree.gui.TechTreeLayout;
import gregtech.common.misc.techtree.interfaces.IPrerequisite;

public class GT_Loader_Technologies implements Runnable {

    @Override
    public void run() {
        GT_Mod.GT_FML_LOGGER.info("Loading researchable technologies");

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
        /*
         * TechList.TechF = new TechnologyBuilder("TechF").unlocalizedName("gt.tech.f.name")
         * .prerequisites(TechList.TechA, TechList.TechD)
         * .buildAndRegister();
         */
        TechList.TechG = new TechnologyBuilder("TechG").unlocalizedName("gt.tech.g.name")
            .prerequisites(TechList.TechC)
            .buildAndRegister();

        int numTechnologies = TechnologyRegistry.numTechnologies();
        GT_Mod.GT_FML_LOGGER.info("Constructing tech tree layout");
        long start = System.currentTimeMillis();
        TechTreeLayout.constructOrGet();
        long end = System.currentTimeMillis();
        GT_Mod.GT_FML_LOGGER.info(String.format("Loaded %d technologies.", numTechnologies));
        GT_Mod.GT_FML_LOGGER.info(String.format("Constructing tech tree layout took %d ms.", end - start));
    }
}
