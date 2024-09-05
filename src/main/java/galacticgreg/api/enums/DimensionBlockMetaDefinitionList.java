package galacticgreg.api.enums;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;

import galacticgreg.api.ModDBMDef;

public enum DimensionBlockMetaDefinitionList {

    Moon(new ModDBMDef("tile.moonBlock", 4)),
    Mars(new ModDBMDef("tile.mars", 9)),
    Phobos(new ModDBMDef("phobosblocks", 2)),
    Deimos(new ModDBMDef("deimosblocks", 1)),
    Ceres(new ModDBMDef("ceresblocks", 1)),
    Io(new ModDBMDef("ioblocks", 2)),
    Ganymede(new ModDBMDef("ganymedeblocks", 1)),
    Callisto(new ModDBMDef("callistoblocks", 1)),
    Venus(new ModDBMDef("venusblocks", 1)),
    Mercury(new ModDBMDef("mercuryblocks", 2)),
    Enceladus(new ModDBMDef("enceladusblocks", 1)),
    Titan(new ModDBMDef("titanblocks", 2)),
    Oberon(new ModDBMDef("oberonblocks", 2)),
    Proteus(new ModDBMDef("proteusblocks", 2)),
    Triton(new ModDBMDef("tritonblocks", 2)),
    Pluto(new ModDBMDef("plutoblocks", 5)),
    MakeMake(new ModDBMDef("makemakegrunt", 1)),
    Haumea(new ModDBMDef("haumeablocks")),
    CentauriAlpha(new ModDBMDef("acentauribbsubgrunt")),
    VegaB(new ModDBMDef("vegabsubgrunt")),
    BarnardaC(new ModDBMDef("barnardaCdirt"), new ModDBMDef(Blocks.stone)),
    BarnardaE(new ModDBMDef("barnardaEsubgrunt")),
    BarnardaF(new ModDBMDef("barnardaFsubgrunt")),
    TcetiE(new ModDBMDef("tcetieblocks", 2)),
    Miranda(new ModDBMDef("mirandablocks", 2)),
    Europa(
        // Europa top layer turned off bc ores are too easy to spot
        new ModDBMDef("europagrunt", 1), // Europa Ice Layer ~55-65 without top layer
        new ModDBMDef(Blocks.water), new ModDBMDef(Blocks.flowing_water), new ModDBMDef(Blocks.ice), // Generates
                                                                                                     // directly over
                                                                                                     // bedrock
        new ModDBMDef(Blocks.packed_ice), // Generates directly over bedrock
        new ModDBMDef("europaunderwatergeyser") // Generates directly over bedrock
    ),
    Neper(new ModDBMDef(Blocks.stone), new ModDBMDef("tile.baseBlockRock", 10)),
    Maahes(new ModDBMDef("tile.baseBlockRock", 1)),
    Anubis(new ModDBMDef("tile.baseBlockRock", 1)),
    Horus(new ModDBMDef(Blocks.obsidian)),
    Seth(new ModDBMDef(Blocks.hardened_clay));

    public final List<ModDBMDef> DBMDefList;

    private DimensionBlockMetaDefinitionList(ModDBMDef... DBMDefArray) {
        DBMDefList = Arrays.asList(DBMDefArray);
    }
}
