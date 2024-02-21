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
        TextureType.BLOCK, TextureType.BLOCK, TextureType.BLOCK, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM,
        TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, TextureType.ITEM, };
    private static final String[] SUFFIXES = new String[] { "/dustTiny", "/dustSmall", "/dust", "/dustImpure",
        "/dustPure", "/crushed", "/crushedPurified", "/crushedCentrifuged", "/gem", "/nugget", "/casingSmall", "/ingot",
        "/ingotHot", "/ingotDouble", "/ingotTriple", "/ingotQuadruple", "/ingotQuintuple", "/plate", "/plateDouble",
        "/plateTriple", "/plateQuadruple", "/plateQuintuple", "/plateDense", "/stick", "/lens", "/round", "/bolt",
        "/screw", "/ring", "/foil", "/cell", "/cellPlasma", "/toolHeadSword", "/toolHeadPickaxe", "/toolHeadShovel",
        "/toolHeadAxe", "/toolHeadHoe", "/toolHeadHammer", "/toolHeadFile", "/toolHeadSaw", "/toolHeadDrill",
        "/toolHeadChainsaw", "/toolHeadWrench", "/toolHeadUniversalSpade", "/toolHeadSense", "/toolHeadPlow",
        "/toolHeadArrow", "/toolHeadScrewdriver", "/toolHeadBuzzSaw", "/toolHeadSoldering", "/nanites", "/wireFine",
        "/gearGtSmall", "/rotor", "/stickLong", "/springSmall", "/spring", "/arrowGtWood", "/arrowGtPlastic",
        "/gemChipped", "/gemFlawed", "/gemFlawless", "/gemExquisite", "/gearGt", "/oreRaw", aTextVoidDir, aTextVoidDir,
        "/oreSmall", "/ore", "/wire", "/foil", "/block1", "/block2", "/block3", "/block4", "/block5", "/block6",
        "/pipeSide", "/pipeTiny", "/pipeSmall", "/pipeMedium", "/pipeLarge", "/pipeHuge", "/frameGt", "/pipeQuadruple",
        "/pipeNonuple", aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, "/crateGtDust", "/crateGtIngot", "/crateGtGem",
        "/crateGtPlate", "/turbineBlade", aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir,
        aTextVoidDir, aTextVoidDir, aTextVoidDir, aTextVoidDir, "/handleMallet", "/toolHeadMallet", };

    public boolean is_custom = false;

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
        SET_GEM_VERTICAL = new TextureSet("GEM_VERTICAL"), SET_GEM_HORIZONTAL = new TextureSet("GEM_HORIZONTAL");

    /**
     * For the Indices of OrePrefixes you need to look into the OrePrefix Enum.
     */
    public static final short INDEX_wire = 69, INDEX_foil = 70, INDEX_block1 = 71, INDEX_block2 = 72, INDEX_block3 = 73,
        INDEX_block4 = 74, INDEX_block5 = 75, INDEX_block6 = 76;

    public final IIconContainer[] mTextures = new IIconContainer[128];
    public final String mSetName;

    public TextureSet(String aSetName) {
        mSetName = aSetName;
        for (int i = 0; i < 128; i++) {
            if (IS_BLOCK_TEXTURE[i] == TextureType.BLOCK) {
                mTextures[i] = new Textures.BlockIcons.CustomIcon(aTextMatIconDir + aSetName + SUFFIXES[i]);
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
    }

    /**
     * Construct a TextureSet that will delegate some of its textures to the origin TextureSet.
     * <p>
     * This assumes you want to construct a custom texture set.
     */
    private TextureSet(String aSetName, TextureSet origin, boolean overrideBlock, boolean overrideItem) {
        mSetName = "CUSTOM/" + aSetName;
        this.is_custom = true;

        for (int i = 0; i < 128; i++) {
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
