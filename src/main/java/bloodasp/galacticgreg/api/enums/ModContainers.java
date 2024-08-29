package bloodasp.galacticgreg.api.enums;

import bloodasp.galacticgreg.api.ModContainer;
import gregtech.api.enums.Mods;

public enum ModContainers {

    GalactiCraftCore(new ModContainer(Mods.GalacticraftCore.ID)),
    GalacticraftMars(new ModContainer(Mods.GalacticraftMars.ID)),
    GalaxySpace(new ModContainer(Mods.GalaxySpace.ID)),
    AmunRa(new ModContainer("GalacticraftAmunRa")),
    Vanilla(new ModContainer("Vanilla"));

    public final ModContainer modContainer;

    private ModContainers(ModContainer modContainer) {
        this.modContainer = modContainer;
    }
}
