package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CGCT1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CGCT1_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CGCT1_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_CGCT1_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.misc.GTStructureChannels;

public class MTECrystalGrowthChamber extends MTEExtendedPowerMultiBlockBase<MTECrystalGrowthChamber>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final String STRUCTURE_PIECE_TIER_1 = "tier1";
    private static final String STRUCTURE_PIECE_TIER_2 = "tier2";
    private static final String STRUCTURE_PIECE_TIER_3 = "tier3";

    private final IStructureDefinition<MTECrystalGrowthChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTECrystalGrowthChamber>builder()
        .addShape(
            STRUCTURE_PIECE_TIER_1,
            new String[][] {
                // spotless:off
                {"     ", "  D  ", " DDD ", " DDD ", " DDD ", " B~B ", " BBB "},
                {"  D  ", " D D ", "D   D", "D   D", "B   B", "B A B", "BBBBB"},
                {" DDD ", "D   D", "D C D", "B C B", "B C B", "BAAAB", "BBBBB"},
                {"  D  ", " D D ", "D   D", "B   B", "B   B", "B A B", "BBBBB"},
                {"     ", "  D  ", " DDD ", " DDD ", " BBB ", " BBB ", " BBB "}
                // spotless:on
            })
        .addShape(
            // todo fix block mappings
            STRUCTURE_PIECE_TIER_2,
            new String[][] {
                // spotless:off
                {" AAA             ", " BBB             ", " ABB             ", " FAB    AAA      ", " FFA  BAFFFAB    ", " FFA  BFFFFFB    ", " FAB  BFFFFFB    ", " ABB  BFFFFFB    ", " BBB  BAFFFAB    ", " AAA  BAAHAAB    "},
                {"AABAA            ", "A D B            ", "F   B            ", "F   B BAAAAAB AAA", "F   B G D D G BGB", "F   B G     G BFB", "F   B G     G BFB", "F   B G     G BFB", "A D B G D D G BGB", "AABAA BBAAABB AAA"},
                {"ABBBA            ", "ADDDCCCCCCCCCCCC ", "F E B  C C C   C ", "F E B BCBCBCB ACA", "F E CCCDDDDDCCCDG", "F E B B E E B GEF", "F E CCC E E CCCEF", "F E B B E E B GEF", "ADDDCCCDDDDDCCCDG", "ABBBA BBBBBBB ABA"},
                {"AABAA            ", "A D B            ", "F   B            ", "F   B BAAAAAB AAA", "F   B G D D G BGB", "F   B G     G BFB", "F   B G     G BFB", "F   B G     G BFB", "A D B G D D G BGB", "AABAA BBAAABB AAA"},
                {" AAA             ", " BBB             ", " ABB             ", " FAB    AAA      ", " FFA  BAFFFAB    ", " FFA  BFFFFFB    ", " FAB  BFFFFFB    ", " ABB  BFFFFFB    ", " BBB  BAFFFAB    ", " AAA  BAAAAAB    "}
                // spotless:on
            })
        .addShape(
            // todo fix block mappings
            STRUCTURE_PIECE_TIER_3,
            new String[][] {
                // spotless:off
                {"                                 ", "                                 ", "                                 ", "                                 ", "               GGG               ", "               GGG               ", "               GGG               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               ADA               ", "              AGGGA              ", "             AG   GA             ", "             DG   GD             ", "         A   AG   GA   A         ", "              AGGGA              ", "               ADA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DAD               ", "               ACA               ", "              A   A              ", "            DA     AD            ", "          AAAC     CAAA          ", "         A  DA     AD  A         ", "              A   A              ", "              AACAA              ", "               DAD               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             AAABAAA             ", "            A GDEDG A            ", "            AGD   DGA            ", "            AA     AA            ", "            BE  F  EB            ", "         A  AA     AA  A         ", "              D   D              ", "              GDEDG              ", "              AABAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD   DG             ", "            AA     AA            ", "            BE  F  EB            ", "            AA     AA            ", "             AD   DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD F DG             ", "            AA  F  AA            ", "            BEFFFFFEB            ", "            AA  F  AA            ", "             AD F DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD   DG             ", "            AA     AA            ", "            BE  F  EB            ", "            AA     AA            ", "             AD   DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             AAABAAA             ", "            A GDEDG A            ", "            AGD   DGA            ", "            AA     AA            ", "            BE  F  EB            ", "         A  AA     AA  A         ", "              D   D              ", "              GDEDG              ", "              AABAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DAD               ", "               ACA               ", "              A   A              ", "            DA     AD            ", "          AAAC     CAAA          ", "         A  DA     AD  A         ", "              A   A              ", "              AACAA              ", "               DAD               ", "                                 ", "                                 ", "                                 ", "                                 ", "              AACAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               DAD               ", "              D   D              ", "             A     A             ", "            G   F   G            ", " AAA   AAA   A     A   AAA   AAA ", "              D   D              ", "               DAD               ", "                                 ", "             A     A             ", "             A     A             ", "             A     A             ", "                                 ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                G                ", "               DAD               ", "             AD   DA             ", "            G       G            ", "  A     A  G    F    G  A     A  ", "            G       G            ", "             AD   DA             ", "             A DAD A             ", "             A     A             ", "                                 ", "             A     A             ", "                                 ", "             A     A             ", "             A  A  A             ", "             A     A             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                G                ", "             A DAD A             ", "            G D   D G            ", "           G         G           ", "  A     A G     F     G A     A  ", "           G         G           ", "            G D   D G            ", "             A DAD A             ", "               GGG               ", "               GGG               ", "             A GGG A             ", "               GGG               ", "               GGG               ", "             A DAD A             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               GGG               ", "   A   A     ADACADA     A   A   ", "   A   A   GG       GG   A   A   ", "  DAAAAAD G           G DAAAAAD  ", "  ABBBBBAG             GABBBBBA  ", "  DAAAAAD G           G DAAAAAD  ", "           GG       GG           ", "             ADACADA             ", "              G   G              ", "              G   G              ", "             AG   GA             ", "              G   G              ", "              G   G              ", "             ADACADA             ", "               GGG               ", "                G                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                G                ", "   A   A       G G       A   A   ", "           AAAA   AAAA           ", "   GGGGG  A           A  GGGGG   ", " AAAAAAAAA             AAAAAAAAA ", " DCEEEEEC               CEEEEECD ", " AAAAAAAAA             AAAAAAAAA ", "    AAA   A           A   AAA    ", "          AAAAA   AAAAA          ", "          A  D     D  A          ", "         A   D     D   A         ", "         AAAAA     AAAAA         ", "         A   D     D   A         ", "          A  D     D  A          ", "          AAAAA   AAAAA          ", "          A  AG   GA  A          ", "             AGG GGA             ", "             C GGG C             ", "                G                ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                G                ", "   A   A      GG GG      A   A   ", "   GGGGG    DA     AD    GGGGG   ", " AADDDDDADDD         DDDADDDDDAA ", " G                             G ", " G   F                     F   G ", " G              F              G ", " AADDDDDADDD    F    DDDADDDDDAA ", "  AGGGGGA   DA     AD   AGGGGGA  ", "   A   A    G       G    A   A   ", "            G       G            ", "            G FF FF G            ", "            G       G            ", "            G       G            ", "        A   DA     AD   A        ", "             G  F  G             ", "             G  F  G             ", "              G   G              ", "              AG GA              ", "              DGGGD              ", "               DGD               ", "                H                ", "                                 "},
                {"               GGG               ", "  DAAAAAD   GGG   GGG   DAAAAAD  ", " AADDDDDADDDA       ADDDADDDDDAA ", " G                             G ", "G                               G", "G    F                     F    G", "G                               G", " G                             G ", " AADDDDDADDDA       ADDDADDDDDAA ", "  DAAAAAD  G    F    G  DAAAAAD  ", "           G    F    G           ", "           G  F F F  G           ", "           G    F    G           ", "           G    F    G           ", "        A  DA       AD  A        ", "            G       G            ", "             G     G             ", "             G     G             ", "              G   G              ", "              G   G              ", "              DG GD              ", "                G                ", "                                 "},
                {"             GGGGGGG             ", "  ABBBBBA GGG       GGG ABBBBBA  ", " DCEEEEECAAAC       CAAACEEEEECD ", " G   F                     F   G ", "G    F                     F    G", "G  FFFFF FFF         FFF FFFFF  G", "G    F        F F F        F    G", " G   F        F F F        F   G ", " DCEEEEECAAAC   F   CAAACEEEEECD ", "  ABBBBBA  G   FFF   G  ABBBBBA  ", "           G   FFF   G           ", "           G   FFF   G           ", "           G   FFF   G           ", "           G   FFF   G           ", "        CAAAC   F   CAAAC        ", "            G F F F G            ", "            G F F F G            ", "             G     G             ", "             G     G             ", "              G   G              ", "              G   G              ", "              DG GD              ", "                G                "},
                {"               GGG               ", "  DAAAAAD   GGG   GGG   DAAAAAD  ", " AADDDDDADDDA       ADDDADDDDDAA ", " G                             G ", "G                               G", "G    F                     F    G", "G                               G", " G                             G ", " AADDDDDADDDA       ADDDADDDDDAA ", "  DAAAAAD  G    F    G  DAAAAAD  ", "           G    F    G           ", "           G  F F F  G           ", "           G    F    G           ", "           G    F    G           ", "        A  DA       AD  A        ", "            G       G            ", "             G     G             ", "             G     G             ", "              G   G              ", "              G   G              ", "              DG GD              ", "                G                ", "                                 "},
                {"                G                ", "   A   A      GG GG      A   A   ", "   GGGGG    DA     AD    GGGGG   ", " AADDDDDADDD         DDDADDDDDAA ", " G                             G ", " G   F                     F   G ", " G              F              G ", " AADDDDDADDD    F    DDDADDDDDAA ", "  AGGGGGA   DA     AD   AGGGGGA  ", "   A   A    G       G    A   A   ", "            G       G            ", "            G FF FF G            ", "            G       G            ", "            G       G            ", "        A   DA     AD   A        ", "             G  F  G             ", "             G  F  G             ", "              G   G              ", "              AG GA              ", "              DGGGD              ", "               DGD               ", "                D                ", "                                 "},
                {"                G                ", "   A   A       G G       A   A   ", "           AAAA   AAAA           ", "   GGGGG  A           A  GGGGG   ", " AAAAAAAAA             AAAAAAAAA ", " DCEEEEEC               CEEEEECD ", " AAAAAAAAA             AAAAAAAAA ", "    AAA   A           A   AAA    ", "          AAAAA   AAAAA          ", "          A  D     D  A          ", "         A   D     D   A         ", "         AAAAA     AAAAA         ", "         A   D     D   A         ", "          A  D     D  A          ", "          AAAAA   AAAAA          ", "          A  AG   GA  A          ", "             AGG GGA             ", "             C GGG C             ", "                G                ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               GGG               ", "   A   A     ADACADA     A   A   ", "   A   A   GG       GG   A   A   ", "  DAAAAAD G           G DAAAAAD  ", "  ABBBBBAG             GABBBBBA  ", "  DAAAAAD G           G DAAAAAD  ", "           GG       GG           ", "             ADACADA             ", "              G   G              ", "              G   G              ", "             AG   GA             ", "              G   G              ", "              G   G              ", "             ADACADA             ", "               GGG               ", "                G                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                G                ", "             A DAD A             ", "            G D   D G            ", "           G         G           ", "  A     A G     F     G A     A  ", "           G         G           ", "            G D   D G            ", "             A DAD A             ", "               GGG               ", "               GGG               ", "             A GGG A             ", "               GGG               ", "               GGG               ", "             A DAD A             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                G                ", "               DAD               ", "             AD   DA             ", "            G       G            ", "  A     A  G    F    G  A     A  ", "            G       G            ", "             AD   DA             ", "             A DAD A             ", "             A     A             ", "                                 ", "             A     A             ", "                                 ", "             A     A             ", "             A  A  A             ", "             A     A             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               DAD               ", "              D   D              ", "             A     A             ", "            G   F   G            ", " AAA   AAA   A     A   AAA   AAA ", "              D   D              ", "               DAD               ", "                                 ", "             A     A             ", "             A     A             ", "             A     A             ", "                                 ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DAD               ", "               ACA               ", "              A   A              ", "            DA     AD            ", "          AAAC     CAAA          ", "         A  DA     AD  A         ", "              A   A              ", "              AACAA              ", "               DAD               ", "                                 ", "                                 ", "                                 ", "                                 ", "              AACAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             AAABAAA             ", "            A GDEDG A            ", "            AGD   DGA            ", "            AA     AA            ", "            BE  F  EB            ", "         A  AA     AA  A         ", "              D   D              ", "              GDEDG              ", "              AABAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD   DG             ", "            AA     AA            ", "            BE  F  EB            ", "            AA     AA            ", "             AD   DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD F DG             ", "            AA  F  AA            ", "            BEFFFFFEB            ", "            AA  F  AA            ", "             AD F DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               ABA               ", "              GDEDG              ", "             GD   DG             ", "            AA     AA            ", "            BE  F  EB            ", "            AA     AA            ", "             AD   DA             ", "              GDEDG              ", "               ABA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             AAABAAA             ", "            A GDEDG A            ", "            AGD   DGA            ", "            AA     AA            ", "            BE  F  EB            ", "         A  AA     AA  A         ", "              D   D              ", "              GDEDG              ", "              AABAA              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DAD               ", "               ACA               ", "              A   A              ", "            DA     AD            ", "          AAAC     CAAA          ", "         A  DA     AD  A         ", "              A   A              ", "              AACAA              ", "               DAD               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               ADA               ", "              AGGGA              ", "             AG   GA             ", "             DG   GD             ", "         A   AG   GA   A         ", "              AGGGA              ", "               ADA               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "                                 ", "                                 ", "               GGG               ", "               GGG               ", "               GGG               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "}
                // spotless:on
            })
        .addElement('A', chainEmitterCasings())
        .addElement(
            'B',
            buildHatchAdder(MTECrystalGrowthChamber.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(13))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTECrystalGrowthChamber::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 13))))
        .addElement('C', chainGemClusters())
        .addElement('D', ofBlock(GregTechAPI.sBlockGlass1, 9)) // Sapphire Glass
        .addElement('E', ofFrame(Materials.Steel))
        .addElement('F', ofFrame(Materials.Iron))
        .addElement('G', ofFrame(Materials.TungstenSteel))
        .addElement('H', ofFrame(Materials.Titanium))
        .build();

    private int structureTier = -1;
    private int emitterTier = -1;
    private int gemClusterTier = -1;

    public MTECrystalGrowthChamber(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECrystalGrowthChamber(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTECrystalGrowthChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private <T> IStructureElement<T> chainEmitterCasings() {
        return GTStructureChannels.TIER_EMITTER_CASING.use(lazy(t -> ofBlocksTiered((block, meta) -> {
            if (block != GregTechAPI.sBlockCasingsEmitter) return null;
            if (meta < 0 || meta > 13) return null;
            return meta + 1;
        },
            IntStream.range(0, 14)
                .mapToObj(i -> Pair.of(GregTechAPI.sBlockCasingsEmitter, i))
                .collect(Collectors.toList()),
            -1,
            (v1, v2) -> this.emitterTier = v2,
            v -> this.emitterTier)));
    }

    private <T> IStructureElement<T> chainGemClusters() {
        return GTStructureChannels.TIER_GEM_CLUSTER.use(lazy(t -> ofBlocksTiered((block, meta) -> {
            if (block != GregTechAPI.sBlockGemCluster) return null;
            if (meta < 0 || meta > 13) return null;
            return meta + 1;
        },
            IntStream.range(0, 14)
                .mapToObj(i -> Pair.of(GregTechAPI.sBlockGemCluster, i))
                .collect(Collectors.toList()),
            -1,
            (v1, v2) -> this.gemClusterTier = v2,
            v -> this.gemClusterTier)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECrystalGrowthChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 13)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_CGCT1_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_CGCT1_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 13)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_CGCT1)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_CGCT1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 13)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Crystal Growth Chamber, CGC")
            .addBulkMachineInfo(4, 1.5F, 1F)
            .beginStructureBlock(5, 7, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize == 1) {
            buildPiece(STRUCTURE_PIECE_TIER_1, stackSize, hintsOnly, 2, 5, 0);
        } else if (stackSize.stackSize == 2) {
            buildPiece(STRUCTURE_PIECE_TIER_2, stackSize, hintsOnly, 0, -1, 0);
        }
        buildPiece(STRUCTURE_PIECE_TIER_3, stackSize, hintsOnly, 0, -1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (stackSize.stackSize == 1) {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_1, stackSize, 2, 5, 0, elementBudget, env, false, true);
        } else if (stackSize.stackSize == 2) {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_2, stackSize, 0, -1, 0, elementBudget, env, false, true);
        } else {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_3, stackSize, 0, -1, 0, elementBudget, env, false, true);
        }
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int newTier = 0;
        if (checkPiece(STRUCTURE_PIECE_TIER_1, 2, 5, 0)) {
            newTier = 1;
        } else if (checkPiece(STRUCTURE_PIECE_TIER_2, 0, -1, 0)) {
            newTier = 2;
        } else if (checkPiece(STRUCTURE_PIECE_TIER_3, 0, -1, 0)) {
            newTier = 3;
        }

        if (newTier == 0) return false;
        if (newTier != structureTier) {
            structureTier = newTier;
            // todo sync if needed
        }

        // todo check any hatches
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.autoclaveRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
