package gregtech.api.enums;

import gregtech.api.interfaces.IIconContainer;

public class TextureSet {

    private static final String aTextMatIconDir = "materialicons/";
    private static final String aTextVoidDir = "/void";

    private static final TextureType[] IS_BLOCK_TEXTURE = new TextureType[] { TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK,
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM };
    private static final String[] SUFFIXES = new String[] { "/dustTiny", "/dustSmall", "/dust", "/dustImpure",
        "/dustPure", "/crushed", "/crushedPurified", "/crushedCentrifuged", "/gem", "/nugget", "/casingSmall", "/ingot",
        "/ingotHot", aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, "/plate", "/plateDouble", "/plateTriple",
        "/plateQuadruple", "/plateQuintuple", "/plateDense", "/stick", "/lens", "/round", "/bolt", "/screw", "/ring",
        "/foil", "/cell", "/cellPlasma", aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        "/toolHeadHammer", "/toolHeadFile", "/toolHeadSaw", "/toolHeadDrill", "/toolHeadChainsaw", "/toolHeadWrench",
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, "/toolHeadScrewdriver", "/toolHeadBuzzSaw",
        "/toolHeadSoldering", "/nanites", "/wireFine", "/gearGtSmall", "/rotor", "/stickLong", "/springSmall",
        "/spring", "/arrowGtWood", "/arrowGtPlastic", "/gemChipped", "/gemFlawed", "/gemFlawless", "/gemExquisite",
        "/gearGt", "/oreRaw", aTextVoidDir, aTextVoidDir, "/oreSmall", "/ore", "/wire", "/foil", "/block1", "/block2",
        "/block3", "/block4", "/block5", "/block6", "/pipeSide", "/pipeTiny", "/pipeSmall", "/pipeMedium", "/pipeLarge",
        "/pipeHuge", "/frameGt", "/pipeQuadruple", "/pipeNonuple", "/sheetmetal", aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        "/crateGtDust", "/crateGtIngot", "/crateGtGem", "/crateGtPlate", "/turbineBlade", aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, "/plateSuperdense",
        "/handleMallet", "/toolHeadMallet", "/toolTurbine", "/toolWrench", "/toolCrowbar", "/toolWireCutter",
        "/toolScoop", "/toolBranchCutter", "/toolKnife", "/toolKnifeButchery", "/toolPlunger", "/toolJackHammer",
        "/pocketMultiToolClosed", "/pocketMultiToolKnife", "/pocketMultiToolSaw", "/pocketMultiToolFile",
        "/pocketMultiToolScrewdriver", "/pocketMultiToolWireCutter", "/pocketMultiToolBranchCutter", "/toolTrowel",
        "/toolHeadAngleGrinder", "/toolHeadElectricSnips", "/handleFile", "/handleTrowel", "/handleSaw",
        "/handleScrewdriver", "/toolHeadMallet", "/toolProspector", "/toolProspectorElectricLuV",
        "/toolProspectorElectricZPM", "/toolProspectorElectricUV", "/toolProspectorElectricUHV", aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir };

    public boolean is_custom = false;

    public String aTextCustomAutogenerated = "autogenerated";

    public static final TextureSet SET_NONE = new TextureSet("NONE"), SET_DULL = new TextureSet("DULL"),
        SET_RUBY = new TextureSet("RUBY"), SET_OPAL = new TextureSet("OPAL"), SET_LEAF = new TextureSet("LEAF"),
        SET_WOOD = new TextureSet("WOOD"), SET_SAND = new TextureSet("SAND"), SET_FINE = new TextureSet("FINE"),
        SET_FIERY = new TextureSet("FIERY"), SET_FLUID = new TextureSet("FLUID"), SET_ROUGH = new TextureSet("ROUGH"),
        SET_PAPER = new TextureSet("PAPER"), SET_GLASS = new TextureSet("GLASS"), SET_FLINT = new TextureSet("FLINT"),
        SET_LAPIS = new TextureSet("LAPIS"), SET_SHINY = new TextureSet("SHINY"), SET_SHARDS = new TextureSet("SHARDS"),
        SET_POWDER = new TextureSet("POWDER"), SET_QUARTZ = new TextureSet("QUARTZ"),
        SET_EMERALD = new TextureSet("EMERALD"), SET_DIAMOND = new TextureSet("DIAMOND"),
        SET_LIGNITE = new TextureSet("LIGNITE"), SET_MAGNETIC = new TextureSet("MAGNETIC"),
        SET_METALLIC = new TextureSet("METALLIC"), SET_NETHERSTAR = new TextureSet("NETHERSTAR"),
        SET_GEM_VERTICAL = new TextureSet("GEM_VERTICAL"), SET_GEM_HORIZONTAL = new TextureSet("GEM_HORIZONTAL"),
        SET_REFINED = new TextureSet("REFINED"), SET_GEM_A = new TextureSet("GEM_A"),
        SET_NUCLEAR = new TextureSet("NUCLEAR");

    /**
     * Custom Texture Sets for more clean code
     */
    public static final TextureSet SET_DARKSTEEL = new TextureSet("darksteel", true),
        SET_ENERGETIC = new TextureSet("energetic", true), SET_VIBRANT = new TextureSet("vibrant", true),
        SET_CRYSTALLINE = new TextureSet("crystalline", true), SET_MELODIC = new TextureSet("melodic", true),
        SET_STELLAR = new TextureSet("stellar", true), SET_VIVID = new TextureSet("vivid", true),
        SET_CRUDE_STEEL = new TextureSet("crudesteel", false),
        SET_CRYSTALLINE_PINK_SLIME = new TextureSet("crystallinepinkslime", true),
        SET_ENERGETIC_SILVER = new TextureSet("energeticsilver", false),
        SET_END_STEEL = new TextureSet("endsteel", false), SET_DRACONIUM = new TextureSet("draconium", false),
        SET_AWOKEN_DRACONIUM = new TextureSet("awakeneddraconium", false),
        SET_ELECTROTINE = new TextureSet("electrotine", false), SET_REDSTONE = new TextureSet("redstone", false),
        SET_GLOWSTONE = new TextureSet("glowstone", false), SET_PYROTHEUM = new TextureSet("pyrotheum", false),
        SET_CRYOTHEUM = new TextureSet("cryotheum", false), SET_BLAZE = new TextureSet("blaze", false),
        SET_BLIZZ = new TextureSet("blizz", false), SET_MANASTEEL = new TextureSet("Manasteel", false),
        SET_LIVINGROCK = new TextureSet("Livingrock", true), SET_GAIA_SPIRIT = new TextureSet("GaiaSpirit", true),
        SET_LIVINGWOOD = new TextureSet("Livingwood", true), SET_DREAMWOOD = new TextureSet("Dreamwood", true),
        SET_MANA_DIAMOND = new TextureSet("ManaDiamond", true), SET_DRAGONSTONE = new TextureSet("Dragonstone", true),
        SET_INFINITY = new TextureSet("infinity", true), SET_BEDROCKIUM = new TextureSet("bedrockium", true),
        SET_ICHORIUM = new TextureSet("ichorium", true),
        SET_COSMIC_NEUTRONIUM = new TextureSet("cosmicneutronium", true),
        SET_SBM = new TextureSet("stablebaryonicmatter", true), SET_NETHERITE = new TextureSet("netherite", true),
        SET_PRISMATIC_ACID = new TextureSet("prismaticacid", true), SET_SPACETIME = new TextureSet("spacetime", true),
        SET_MHDCSM = new TextureSet("MagnetohydrodynamicallyConstrainedStarMatter", true),
        SET_WHITE_DWARF_MATTER = new TextureSet("WhiteDwarfMatter", true),
        SET_UNIVERSIUM = new TextureSet("universium", true), SET_ETERNITY = new TextureSet("eternity", true),
        SET_MAGMATTER = new TextureSet("magmatter", true), SET_GRAVITON_SHARD = new TextureSet("GravitonShard", true),
        SET_DIMENSIONALLY_SHIFTED_SUPER_FLUID = new TextureSet("dimensionallyshiftedsuperfluid", true),
        SET_PROTOHALKONITE_BASE = new TextureSet("protohalkonitebase", true),
        SET_HOT_PROTOHALKONITE = new TextureSet("hotprotohalkonite", true),
        SET_PROTOHALKONITE = new TextureSet("protohalkonite", true),
        SET_HOT_EXOHALKONITE = new TextureSet("hotexohalkonite", true),
        SET_EXOHALKONITE = new TextureSet("exohalkonite", true),
        SET_SG_CRYSTAL_SLURRY = new TextureSet("sgcrystalfluid", true), SET_RHUGNOR = new TextureSet("rhugnor", true),
        SET_HYPOGEN = new TextureSet("hypogen", false);

    /**
     * For the Indices of OrePrefixes you need to look into the OrePrefix Enum.
     */
    public static final short INDEX_wire = 69, INDEX_foil = 70, INDEX_block1 = 71, INDEX_block2 = 72, INDEX_block3 = 73,
        INDEX_block4 = 74, INDEX_block5 = 75, INDEX_block6 = 76;

    /**
     * For Tools that don't have a prefix associated with them
     */
    public static final short INDEX_turbine = 128, INDEX_wrench = 129, INDEX_crowbar = 130, INDEX_wireCutter = 131,
        INDEX_scoop = 132, INDEX_branchCutter = 133, INDEX_knife = 134, INDEX_butcheryKnife = 135, INDEX_plunger = 136,
        INDEX_jackHammer = 137, INDEX_pocketMultiToolClosed = 138, INDEX_pocketMultiToolKnife = 139,
        INDEX_pocketMultiToolSaw = 140, INDEX_pocketMultiToolFile = 141, INDEX_pocketMultiToolScrewdriver = 142,
        INDEX_pocketMultiToolWireCutter = 143, INDEX_pocketMultiToolBranchCutter = 144, INDEX_trowel = 145,
        INDEX_angleGrinder = 146, INDEX_electricSnips = 147, INDEX_handleFile = 148, INDEX_handleTrowel = 149,
        INDEX_handleSaw = 150, INDEX_handleScrewdriver = 151, INDEX_prospector = 153, INDEX_prospectorElectricLuV = 154,
        INDEX_prospectorElectricZPM = 155, INDEX_prospectorElectricUV = 156, INDEX_prospectorElectricUHV = 157;

    public final IIconContainer[] mTextures = new IIconContainer[192];
    public final String mSetName;

    public TextureSet(String aSetName) {
        mSetName = aSetName;
        for (int i = 0; i < 192; i++) {
            if (IS_BLOCK_TEXTURE[i] == TextureType.BLOCK) {
                switch (SUFFIXES[i]) {
                    case "/ore", "/oreSmall" -> mTextures[i] = new Textures.BlockIcons.CustomAlphaIcon(
                        aTextMatIconDir + aSetName + SUFFIXES[i]);
                    default -> mTextures[i] = new Textures.BlockIcons.CustomIcon(
                        aTextMatIconDir + aSetName + SUFFIXES[i], true);
                }
            } else {
                // Check nanites folder for nanites texture to avoid copy pasting large file multiple times.
                // Exemption for CUSTOM textures so they can be overriden as normal by placing nanite image in
                // their respective folder.
                if (SUFFIXES[i].equals("/nanites") && (!aSetName.contains("CUSTOM"))) {
                    mTextures[i] = new Textures.ItemIcons.CustomIcon(aTextMatIconDir + "NANITES" + SUFFIXES[i]);
                } else {
                    mTextures[i] = new Textures.ItemIcons.CustomIcon(aTextMatIconDir + aSetName + SUFFIXES[i]);
                }
            }
        }
    }

    public TextureSet(String aSetName, boolean isCustom) {
        this("CUSTOM/" + aSetName);
        this.is_custom = isCustom;
        this.aTextCustomAutogenerated = aSetName;
    }

    /**
     * Construct a TextureSet that will delegate some of its textures to the origin TextureSet.
     * <p>
     * This assumes you want to construct a custom texture set.
     */
    private TextureSet(String aSetName, TextureSet origin, boolean overrideBlock, boolean overrideItem) {
        mSetName = "CUSTOM/" + aSetName;
        this.is_custom = true;

        for (int i = 0; i < 192; i++) {
            if (IS_BLOCK_TEXTURE[i] == TextureType.BLOCK) {
                if (overrideBlock) {
                    mTextures[i] = new Textures.BlockIcons.CustomIcon(aTextMatIconDir + mSetName + SUFFIXES[i]);
                } else {
                    mTextures[i] = origin.mTextures[i];
                }
            } else {
                if (overrideItem) {
                    mTextures[i] = new Textures.ItemIcons.CustomIcon(aTextMatIconDir + aSetName + SUFFIXES[i]);
                } else {
                    mTextures[i] = origin.mTextures[i];
                }
            }
        }
    }

    public TextureSet withBlockTextures(String aNewSetName) {
        return new TextureSet(aNewSetName, this, true, false);
    }

    private enum TextureType {
        BLOCK,
        ITEM,
    }
}
