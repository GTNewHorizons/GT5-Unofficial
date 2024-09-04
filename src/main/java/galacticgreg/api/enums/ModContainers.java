package galacticgreg.api.enums;

import galacticgreg.api.ModContainer;
import gregtech.api.enums.Mods;

public enum ModContainers {

    GalactiCraftCore(new ModContainer(Mods.GalacticraftCore)),
    GalacticraftMars(new ModContainer(Mods.GalacticraftMars)),
    GalaxySpace(new ModContainer(Mods.GalaxySpace)),
    AmunRa(new ModContainer(Mods.GalacticraftAmunRa)),
    Vanilla(new ModContainer(Mods.Minecraft));

    public final ModContainer modContainer;

    private ModContainers(ModContainer modContainer) {
        this.modContainer = modContainer;
    }
}
