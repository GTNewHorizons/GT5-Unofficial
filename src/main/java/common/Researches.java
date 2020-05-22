package common;

import kekztech.KekzCore;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumic.tinkerer.common.research.KamiResearchItem;

public class Researches {

    public static final String THAUMIUMREINFORCEDJAR = "THAUMIUMREINFORCEDJAR";
    public static final String ICHORJAR = "ICHORJAR";

    public static void preInit() {
        // Blacklist these researches from being a requirement to unlock TTKami
        KekzCore.LOGGER.info("Blacklisting research " + THAUMIUMREINFORCEDJAR + " from /iskamiunlocked");
        KamiResearchItem.Blacklist.add(ICHORJAR);
        KekzCore.LOGGER.info("Blacklisting research" +ICHORJAR+ "from /iskamiunlocked");
        KamiResearchItem.Blacklist.add(ICHORJAR);
    }

    public static void postInit() {
        final AspectList aspects_jarthaumiumreinforced = new AspectList()
                .add(Aspect.ARMOR, 3)
                .add(Aspect.WATER, 3)
                .add(Aspect.GREED, 3)
                .add(Aspect.VOID, 3);
        @SuppressWarnings("unused")
        final ResearchItem jar_thaumiumreinforced = new ResearchItem("THAUMIUMREINFORCEDJAR", "ALCHEMY", aspects_jarthaumiumreinforced, 3, -4, 2, new ItemStack(Blocks.jarThaumiumReinforced, 1))
                .setPages(
                        new ResearchPage("kekztech.research_page.THAUMIUMREINFORCEDJAR.0"),
                        new ResearchPage(Recipes.infusionRecipes.get("THAUMIUMREINFORCEDJAR")),
                        new ResearchPage(Recipes.infusionRecipes.get("THAUMIUMREINFORCEDVOIDJAR")),
                        new ResearchPage("kekztech.research_page.THAUMIUMREINFORCEDJAR.1")
                )
                .setConcealed()
                .setParents("JARLABEL")
                .registerResearchItem();

        final AspectList aspects_jarichor = new AspectList()
                .add(Aspect.ARMOR, 3)
                .add(Aspect.ELDRITCH, 3)
                .add(Aspect.WATER, 3)
                .add(Aspect.GREED, 5)
                .add(Aspect.VOID, 5);
        @SuppressWarnings("unused")
        final ResearchItem jar_ichor = new ResearchItem("ICHORJAR", "ALCHEMY", aspects_jarichor, 2, -5, 3, new ItemStack(Blocks.jarIchor, 1))
                .setPages(
                        new ResearchPage("kekztech.research_page.ICHORJAR.0"),
                        new ResearchPage(Recipes.infusionRecipes.get("ICHORJAR")),
                        new ResearchPage(Recipes.infusionRecipes.get("ICHORVOIDJAR"))
                )
                .setConcealed()
                .setParents("THAUMIUMREINFORCEDJAR")
                .setParentsHidden("ICHOR")
                .registerResearchItem();
    }
}
