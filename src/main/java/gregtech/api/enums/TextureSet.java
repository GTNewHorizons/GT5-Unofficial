package gregtech.api.enums;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;

public class TextureSet {

    private static final String aTextMatIconDir = "materialicons/";
    private static final String aTextVoidDir = "/void";

    // spotless:off
    private static final List<Pair<TextureType, String>> SUFFIXES = ImmutableList.<Pair<TextureType, String>>builder()
        .add(Pair.of(TextureType.ITEM, "/dustTiny"))
        .add(Pair.of(TextureType.ITEM, "/dustSmall"))
        .add(Pair.of(TextureType.ITEM, "/dust"))
        .add(Pair.of(TextureType.ITEM, "/dustImpure"))
        .add(Pair.of(TextureType.ITEM, "/dustPure"))
        .add(Pair.of(TextureType.ITEM, "/crushed"))
        .add(Pair.of(TextureType.ITEM, "/crushedPurified"))
        .add(Pair.of(TextureType.ITEM, "/crushedCentrifuged"))
        .add(Pair.of(TextureType.ITEM, "/gem"))
        .add(Pair.of(TextureType.ITEM, "/nugget"))
        .add(Pair.of(TextureType.ITEM, "/casingSmall"))
        .add(Pair.of(TextureType.ITEM, "/ingot"))
        .add(Pair.of(TextureType.ITEM, "/ingotHot"))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, "/plate"))
        .add(Pair.of(TextureType.ITEM, "/plateDouble"))
        .add(Pair.of(TextureType.ITEM, "/plateTriple"))
        .add(Pair.of(TextureType.ITEM, "/plateQuadruple"))
        .add(Pair.of(TextureType.ITEM, "/plateQuintuple"))
        .add(Pair.of(TextureType.ITEM, "/plateDense"))
        .add(Pair.of(TextureType.ITEM, "/stick"))
        .add(Pair.of(TextureType.ITEM, "/lens"))
        .add(Pair.of(TextureType.ITEM, "/round"))
        .add(Pair.of(TextureType.ITEM, "/bolt"))
        .add(Pair.of(TextureType.ITEM, "/screw"))
        .add(Pair.of(TextureType.ITEM, "/ring"))
        .add(Pair.of(TextureType.ITEM, "/foil"))
        .add(Pair.of(TextureType.ITEM, "/cell"))
        .add(Pair.of(TextureType.ITEM, "/cellPlasma"))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, "/toolHeadHammer"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadFile"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadSaw"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadDrill"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadChainsaw"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadWrench"))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, "/toolHeadScrewdriver"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadBuzzSaw"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadSoldering"))
        .add(Pair.of(TextureType.ITEM, "/nanites"))
        .add(Pair.of(TextureType.ITEM, "/wireFine"))
        .add(Pair.of(TextureType.ITEM, "/gearGtSmall"))
        .add(Pair.of(TextureType.ITEM, "/rotor"))
        .add(Pair.of(TextureType.ITEM, "/stickLong"))
        .add(Pair.of(TextureType.ITEM, "/springSmall"))
        .add(Pair.of(TextureType.ITEM, "/spring"))
        .add(Pair.of(TextureType.ITEM, "/arrowGtWood"))
        .add(Pair.of(TextureType.ITEM, "/arrowGtPlastic"))
        .add(Pair.of(TextureType.ITEM, "/gemChipped"))
        .add(Pair.of(TextureType.ITEM, "/gemFlawed"))
        .add(Pair.of(TextureType.ITEM, "/gemFlawless"))
        .add(Pair.of(TextureType.ITEM, "/gemExquisite"))
        .add(Pair.of(TextureType.ITEM, "/gearGt"))
        .add(Pair.of(TextureType.ITEM, "/oreRaw"))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, "/oreSmall"))
        .add(Pair.of(TextureType.BLOCK, "/ore"))
        .add(Pair.of(TextureType.BLOCK, "/wire"))
        .add(Pair.of(TextureType.BLOCK, "/foil"))
        .add(Pair.of(TextureType.BLOCK, "/block1"))
        .add(Pair.of(TextureType.BLOCK, "/block2"))
        .add(Pair.of(TextureType.BLOCK, "/block3"))
        .add(Pair.of(TextureType.BLOCK, "/block4"))
        .add(Pair.of(TextureType.BLOCK, "/block5"))
        .add(Pair.of(TextureType.BLOCK, "/block6"))
        .add(Pair.of(TextureType.BLOCK, "/pipeSide"))
        .add(Pair.of(TextureType.BLOCK, "/pipeTiny"))
        .add(Pair.of(TextureType.BLOCK, "/pipeSmall"))
        .add(Pair.of(TextureType.BLOCK, "/pipeMedium"))
        .add(Pair.of(TextureType.BLOCK, "/pipeLarge"))
        .add(Pair.of(TextureType.BLOCK, "/pipeHuge"))
        .add(Pair.of(TextureType.BLOCK, "/frameGt"))
        .add(Pair.of(TextureType.BLOCK, "/pipeQuadruple"))
        .add(Pair.of(TextureType.BLOCK, "/pipeNonuple"))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.BLOCK, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, "/crateGtDust"))
        .add(Pair.of(TextureType.ITEM, "/crateGtIngot"))
        .add(Pair.of(TextureType.ITEM, "/crateGtGem"))
        .add(Pair.of(TextureType.ITEM, "/crateGtPlate"))
        .add(Pair.of(TextureType.ITEM, "/turbineBlade"))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, aTextVoidDir))
        .add(Pair.of(TextureType.ITEM, "/plateSuperdense"))
        .add(Pair.of(TextureType.ITEM, "/handleMallet"))
        .add(Pair.of(TextureType.ITEM, "/toolHeadMallet"))
        .build();

    private final boolean hasCustomFluid;
    private final boolean isCustomTextureSet;

    public String aTextCustomAutogenerated = "autogenerated";

    /**
     * Standard Texture Sets used by multiple materials
     */
    public static final TextureSet SET_NONE = new TextureSet("NONE"),
        SET_DULL = new TextureSet("DULL"),
        SET_RUBY = new TextureSet("RUBY"),
        SET_OPAL = new TextureSet("OPAL"),
        SET_LEAF = new TextureSet("LEAF"),
        SET_WOOD = new TextureSet("WOOD"),
        SET_SAND = new TextureSet("SAND"),
        SET_FINE = new TextureSet("FINE"),
        SET_FIERY = new TextureSet("FIERY"),
        SET_FLUID = new TextureSet("FLUID"),
        SET_ROUGH = new TextureSet("ROUGH"),
        SET_PAPER = new TextureSet("PAPER"),
        SET_GLASS = new TextureSet("GLASS"),
        SET_FLINT = new TextureSet("FLINT"),
        SET_LAPIS = new TextureSet("LAPIS"),
        SET_SHINY = new TextureSet("SHINY"),
        SET_SHARDS = new TextureSet("SHARDS"),
        SET_POWDER = new TextureSet("POWDER"),
        SET_QUARTZ = new TextureSet("QUARTZ"),
        SET_EMERALD = new TextureSet("EMERALD"),
        SET_DIAMOND = new TextureSet("DIAMOND"),
        SET_LIGNITE = new TextureSet("LIGNITE"),
        SET_MAGNETIC = new TextureSet("MAGNETIC"),
        SET_METALLIC = new TextureSet("METALLIC"),
        SET_NETHERSTAR = new TextureSet("NETHERSTAR"),
        SET_GEM_VERTICAL = new TextureSet("GEM_VERTICAL"),
        SET_GEM_HORIZONTAL = new TextureSet("GEM_HORIZONTAL"),
        SET_REFINED = new TextureSet("REFINED"),
        SET_GEM_A = new TextureSet("GEM_A"),
        SET_NUCLEAR = new TextureSet("NUCLEAR");

    /**
     * Custom Texture Sets used by multiple materials
     */
    public static final TextureSet SET_DARKSTEEL = new TextureSet("darksteel", true),
        SET_CRYSTALLINE = new TextureSet("crystalline", true),
        SET_VIVID = new TextureSet("vivid", true);
    // spotless:on

    /**
     * For the Indices of OrePrefixes you need to look into the OrePrefix Enum.
     */
    public static final short INDEX_wire = 69, INDEX_block1 = 71, INDEX_block4 = 74;

    public final IIconContainer[] mTextures = new IIconContainer[128];
    public final String mSetName;

    /**
     * Construct a new TextureSet for a Material.
     *
     * @param setName The name of the texture set in the resources directory
     */
    public TextureSet(String setName) {
        this(setName, false, false, false, null, false, false);
    }

    /**
     * Construct a new TextureSet for a Material.
     *
     * @param setName        The name of the texture set in the resources directory
     * @param hasCustomFluid Whether materials of this texture set use the set's fluid or a separate fluid
     */
    public TextureSet(String setName, boolean hasCustomFluid) {
        this(setName, true, true, hasCustomFluid, null, false, false);
    }

    /**
     * Construct a new TextureSet for a Material.
     *
     * @param setName         The name of the texture set in the resources directory
     * @param hasCustomFluid  Whether materials of this texture set use the set's fluid or a separate fluid
     * @param hasCustomNanite Whether materials of this texture have their own nanite texture
     */
    public TextureSet(String setName, boolean hasCustomFluid, boolean hasCustomNanite) {
        this(setName, true, hasCustomNanite, hasCustomFluid, null, false, false);
    }

    /**
     * Construct a new TextureSet for a Material.
     *
     * @param setName         The name of the texture set in the resources directory
     * @param isCustom        Whether the texture set is a standard or custom set
     * @param hasCustomFluid  Whether materials of this texture set use the set's fluid or a separate fluid
     * @param hasCustomNanite Whether materials of this texture have their own nanite texture
     * @param origin          Texture set to copy some textures from, optionally
     * @param overrideBlock   Whether to use custom block textures or ones from the origin set
     * @param overrideItem    Whether to use custom item textures or ones from the origin set
     */
    public TextureSet(String setName, boolean isCustom, boolean hasCustomFluid, boolean hasCustomNanite,
        TextureSet origin, boolean overrideBlock, boolean overrideItem) {
        if (isCustom) {
            this.mSetName = "CUSTOM/" + setName;
            this.aTextCustomAutogenerated = setName;
        } else {
            this.mSetName = setName;
        }

        this.isCustomTextureSet = isCustom;
        this.hasCustomFluid = hasCustomFluid;

        for (int i = 0; i < 128; i++) {
            var suffix = SUFFIXES.get(i);
            String name = aTextMatIconDir + mSetName + suffix.getRight();
            if (suffix.getLeft() == TextureType.BLOCK) {
                if (overrideBlock || origin == null) {
                    mTextures[i] = switch (suffix.getRight()) {
                        case "/ore", "/oreSmall" -> new BlockIcons.CustomAlphaIcon(name);
                        default -> new BlockIcons.CustomIcon(name);
                    };
                } else {
                    mTextures[i] = origin.mTextures[i];
                }
            } else {
                // Avoid copying nanites texture if it can be reused (non-custom)
                if (suffix.getRight()
                    .equals("/nanites") && !hasCustomNanite) {
                    mTextures[i] = new ItemIcons.CustomIcon(aTextMatIconDir + "NANITES" + suffix.getRight());
                } else {
                    if (overrideItem || origin == null) {
                        mTextures[i] = new ItemIcons.CustomIcon(name);
                    } else {
                        mTextures[i] = origin.mTextures[i];
                    }
                }
            }
        }
    }

    public boolean isCustomTextureSet() {
        return this.isCustomTextureSet;
    }

    public boolean hasCustomFluid() {
        return this.hasCustomFluid;
    }

    private enum TextureType {
        BLOCK,
        ITEM,
    }
}
