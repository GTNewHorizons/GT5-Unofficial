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
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

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
            STRUCTURE_PIECE_TIER_2,
            new String[][] {
                // spotless:off
                {" BBB             ", " EEE             ", " BEE             ", " DBE    BBB      ", " DDB  EBDDDBE    ", " DDB  EDDDDDE    ", " DBE  EDDDDDE    ", " BEE  EDDDDDE    ", " EEE  EBDDDBE    ", " BBB  EBB~BBE    "},
                {"BBEBB            ", "B A E            ", "D   E            ", "D   E EBBBBBE BBB", "D   E F A A F EFE", "D   E F     F EDE", "D   E F     F EDE", "D   E F     F EDE", "B A E F A A F EFE", "BBEBB EEBBBEE BBB"},
                {"BEEEB            ", "BAAAGGGGGGGGGGGG ", "D C E  G G G   G ", "D C E EGEGEGE BGB", "D C GGGAAAAAGGGAF", "D C E E C C E FCD", "D C GGG C C GGGCD", "D C E E C C E FCD", "BAAAGGGAAAAAGGGAF", "BEEEB EEEEEEE BEB"},
                {"BBEBB            ", "B A E            ", "D   E            ", "D   E EBBBBBE BBB", "D   E F A A F EFE", "D   E F     F EDE", "D   E F     F EDE", "D   E F     F EDE", "B A E F A A F EFE", "BBEBB EEBBBEE BBB"},
                {" BBB             ", " EEE             ", " BEE             ", " DBE    BBB      ", " DDB  EBDDDBE    ", " DDB  EDDDDDE    ", " DBE  EDDDDDE    ", " BEE  EDDDDDE    ", " EEE  EBDDDBE    ", " BBB  EBBBBBE    "}
                // spotless:on
            })
        .addShape(
            STRUCTURE_PIECE_TIER_3,
            new String[][] {
                // spotless:off
                {"                                 ", "                                 ", "                                 ", "                                 ", "               DDD               ", "               DDD               ", "               DDD               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               IHI               ", "              IDDDI              ", "             ID   DI             ", "             HD   DH             ", "         I   ID   DI   I         ", "              IDDDI              ", "               IHI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               HIH               ", "               IKI               ", "              I   I              ", "            HI     IH            ", "          IIIK     KIII          ", "         I  HI     IH  I         ", "              I   I              ", "              IIKII              ", "               HIH               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             IIIJIII             ", "            I DHAHD I            ", "            IDH   HDI            ", "            II     II            ", "            JA  C  AJ            ", "         I  II     II  I         ", "              H   H              ", "              DHAHD              ", "              IIJII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH   HD             ", "            II     II            ", "            JA  C  AJ            ", "            II     II            ", "             IH   HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH C HD             ", "            II  C  II            ", "            JACCCCCAJ            ", "            II  C  II            ", "             IH C HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH   HD             ", "            II     II            ", "            JA  C  AJ            ", "            II     II            ", "             IH   HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             IIIJIII             ", "            I DHAHD I            ", "            IDH   HDI            ", "            II     II            ", "            JA  C  AJ            ", "         I  II     II  I         ", "              H   H              ", "              DHAHD              ", "              IIJII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               HIH               ", "               IKI               ", "              I   I              ", "            HI     IH            ", "          IIIK     KIII          ", "         I  HI     IH  I         ", "              I   I              ", "              IIKII              ", "               HIH               ", "                                 ", "                                 ", "                                 ", "                                 ", "              IIKII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               HIH               ", "              H   H              ", "             I     I             ", "            D   C   D            ", " III   III   I     I   III   III ", "              H   H              ", "               HIH               ", "                                 ", "             I     I             ", "             I     I             ", "             I     I             ", "                                 ", "                I                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                D                ", "               HIH               ", "             IH   HI             ", "            D       D            ", "  I     I  D    C    D  I     I  ", "            D       D            ", "             IH   HI             ", "             I HIH I             ", "             I     I             ", "                                 ", "             I     I             ", "                                 ", "             I     I             ", "             I  I  I             ", "             I     I             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                D                ", "             I HIH I             ", "            D H   H D            ", "           D         D           ", "  I     I D     C     D I     I  ", "           D         D           ", "            D H   H D            ", "             I HIH I             ", "               DDD               ", "               DDD               ", "             I DDD I             ", "               DDD               ", "               DDD               ", "             I HIH I             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DDD               ", "   I   I     IHIKIHI     I   I   ", "   I   I   DD       DD   I   I   ", "  HIIIIIH D           D HIIIIIH  ", "  IJJJJJID             DIJJJJJI  ", "  HIIIIIH D           D HIIIIIH  ", "           DD       DD           ", "             IHIKIHI             ", "              D   D              ", "              D   D              ", "             ID   DI             ", "              D   D              ", "              D   D              ", "             IHIKIHI             ", "               DDD               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                D                ", "   I   I       D D       I   I   ", "           IIII   IIII           ", "   DDDDD  I           I  DDDDD   ", " IIIIIIIII             IIIIIIIII ", " HKAAAAAK               KAAAAAKH ", " IIIIIIIII             IIIIIIIII ", "    III   I           I   III    ", "          IIIII   IIIII          ", "          I  H     H  I          ", "         I   H     H   I         ", "         IIIII     IIIII         ", "         I   H     H   I         ", "          I  H     H  I          ", "          IIIII   IIIII          ", "          I  ID   DI  I          ", "             IDD DDI             ", "             K DDD K             ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                D                ", "   I   I      DD DD      I   I   ", "   DDDDD    HI     IH    DDDDD   ", " IIHHHHHIHHH         HHHIHHHHHII ", " D                             D ", " D   C                     C   D ", " D              C              D ", " IIHHHHHIHHH    C    HHHIHHHHHII ", "  IDDDDDI   HI     IH   IDDDDDI  ", "   I   I    D       D    I   I   ", "            D       D            ", "            D CC CC D            ", "            D       D            ", "            D       D            ", "        I   HI     IH   I        ", "             D  C  D             ", "             D  C  D             ", "              D   D              ", "              ID DI              ", "              HDDDH              ", "               HDH               ", "                ~                ", "                                 "},
                {"               DDD               ", "  HIIIIIH   DDD   DDD   HIIIIIH  ", " IIHHHHHIHHHI       IHHHIHHHHHII ", " D                             D ", "D                               D", "D    C                     C    D", "D                               D", " D                             D ", " IIHHHHHIHHHI       IHHHIHHHHHII ", "  HIIIIIH  D    C    D  HIIIIIH  ", "           D    C    D           ", "           D  C C C  D           ", "           D    C    D           ", "           D    C    D           ", "        I  HI       IH  I        ", "            D       D            ", "             D     D             ", "             D     D             ", "              D   D              ", "              D   D              ", "              HD DH              ", "                D                ", "                                 "},
                {"             DDDDDDD             ", "  IJJJJJI DDD       DDD IJJJJJI  ", " HKAAAAAKIIIK       KIIIKAAAAAKH ", " D   C                     C   D ", "D    C                     C    D", "D  CCCCC CCC         CCC CCCCC  D", "D    C        C C C        C    D", " D   C        C C C        C   D ", " HKAAAAAKIIIK   C   KIIIKAAAAAKH ", "  IJJJJJI  D   CCC   D  IJJJJJI  ", "           D   CCC   D           ", "           D   CCC   D           ", "           D   CCC   D           ", "           D   CCC   D           ", "        KIIIK   C   KIIIK        ", "            D C C C D            ", "            D C C C D            ", "             D     D             ", "             D     D             ", "              D   D              ", "              D   D              ", "              HD DH              ", "                D                "},
                {"               DDD               ", "  HIIIIIH   DDD   DDD   HIIIIIH  ", " IIHHHHHIHHHI       IHHHIHHHHHII ", " D                             D ", "D                               D", "D    C                     C    D", "D                               D", " D                             D ", " IIHHHHHIHHHI       IHHHIHHHHHII ", "  HIIIIIH  D    C    D  HIIIIIH  ", "           D    C    D           ", "           D  C C C  D           ", "           D    C    D           ", "           D    C    D           ", "        I  HI       IH  I        ", "            D       D            ", "             D     D             ", "             D     D             ", "              D   D              ", "              D   D              ", "              HD DH              ", "                D                ", "                                 "},
                {"                D                ", "   I   I      DD DD      I   I   ", "   DDDDD    HI     IH    DDDDD   ", " IIHHHHHIHHH         HHHIHHHHHII ", " D                             D ", " D   C                     C   D ", " D              C              D ", " IIHHHHHIHHH    C    HHHIHHHHHII ", "  IDDDDDI   HI     IH   IDDDDDI  ", "   I   I    D       D    I   I   ", "            D       D            ", "            D CC CC D            ", "            D       D            ", "            D       D            ", "        I   HI     IH   I        ", "             D  C  D             ", "             D  C  D             ", "              D   D              ", "              ID DI              ", "              HDDDH              ", "               HDH               ", "                H                ", "                                 "},
                {"                D                ", "   I   I       D D       I   I   ", "           IIII   IIII           ", "   DDDDD  I           I  DDDDD   ", " IIIIIIIII             IIIIIIIII ", " HKAAAAAK               KAAAAAKH ", " IIIIIIIII             IIIIIIIII ", "    III   I           I   III    ", "          IIIII   IIIII          ", "          I  H     H  I          ", "         I   H     H   I         ", "         IIIII     IIIII         ", "         I   H     H   I         ", "          I  H     H  I          ", "          IIIII   IIIII          ", "          I  ID   DI  I          ", "             IDD DDI             ", "             K DDD K             ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               DDD               ", "   I   I     IHIKIHI     I   I   ", "   I   I   DD       DD   I   I   ", "  HIIIIIH D           D HIIIIIH  ", "  IJJJJJID             DIJJJJJI  ", "  HIIIIIH D           D HIIIIIH  ", "           DD       DD           ", "             IHIKIHI             ", "              D   D              ", "              D   D              ", "             ID   DI             ", "              D   D              ", "              D   D              ", "             IHIKIHI             ", "               DDD               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                D                ", "             I HIH I             ", "            D H   H D            ", "           D         D           ", "  I     I D     C     D I     I  ", "           D         D           ", "            D H   H D            ", "             I HIH I             ", "               DDD               ", "               DDD               ", "             I DDD I             ", "               DDD               ", "               DDD               ", "             I HIH I             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                D                ", "               HIH               ", "             IH   HI             ", "            D       D            ", "  I     I  D    C    D  I     I  ", "            D       D            ", "             IH   HI             ", "             I HIH I             ", "             I     I             ", "                                 ", "             I     I             ", "                                 ", "             I     I             ", "             I  I  I             ", "             I     I             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               HIH               ", "              H   H              ", "             I     I             ", "            D   C   D            ", " III   III   I     I   III   III ", "              H   H              ", "               HIH               ", "                                 ", "             I     I             ", "             I     I             ", "             I     I             ", "                                 ", "                I                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               HIH               ", "               IKI               ", "              I   I              ", "            HI     IH            ", "          IIIK     KIII          ", "         I  HI     IH  I         ", "              I   I              ", "              IIKII              ", "               HIH               ", "                                 ", "                                 ", "                                 ", "                                 ", "              IIKII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             IIIJIII             ", "            I DHAHD I            ", "            IDH   HDI            ", "            II     II            ", "            JA  C  AJ            ", "         I  II     II  I         ", "              H   H              ", "              DHAHD              ", "              IIJII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH   HD             ", "            II     II            ", "            JA  C  AJ            ", "            II     II            ", "             IH   HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH C HD             ", "            II  C  II            ", "            JACCCCCAJ            ", "            II  C  II            ", "             IH C HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               IJI               ", "              DHAHD              ", "             DH   HD             ", "            II     II            ", "            JA  C  AJ            ", "            II     II            ", "             IH   HI             ", "              DHAHD              ", "               IJI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "             IIIJIII             ", "            I DHAHD I            ", "            IDH   HDI            ", "            II     II            ", "            JA  C  AJ            ", "         I  II     II  I         ", "              H   H              ", "              DHAHD              ", "              IIJII              ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "               HIH               ", "               IKI               ", "              I   I              ", "            HI     IH            ", "          IIIK     KIII          ", "         I  HI     IH  I         ", "              I   I              ", "              IIKII              ", "               HIH               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "               IHI               ", "              IDDDI              ", "             ID   DI             ", "             HD   DH             ", "         I   ID   DI   I         ", "              IDDDI              ", "               IHI               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                {"                                 ", "                                 ", "                                 ", "                                 ", "               DDD               ", "               DDD               ", "               DDD               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "}
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
        .addElement(
            'E',
            buildHatchAdder(MTECrystalGrowthChamber.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(14))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTECrystalGrowthChamber::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 14))))
        .addElement('F', ofSheetMetal(Materials.Adamantium))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings9, 0)) // PBI Pipe
        .addElement(
            'H',
            buildHatchAdder(MTECrystalGrowthChamber.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(15))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTECrystalGrowthChamber::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 15))))
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings1,12)) //DTPF Transcendent Casing
        .addElement('J', ofBlock(GregTechAPI.sBlockCasings1, 13)) //DTPF Injection Casing
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings1, 14)) //DTPF Bridge Casing
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
            buildPiece(STRUCTURE_PIECE_TIER_2, stackSize, hintsOnly, 9, 9, 0);
        } else {
            buildPiece(STRUCTURE_PIECE_TIER_3, stackSize, hintsOnly, 16, 21, 14);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (stackSize.stackSize == 1) {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_1, stackSize, 2, 5, 0, elementBudget, env, false, true);
        } else if (stackSize.stackSize == 2) {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_2, stackSize, 9, 9, 0, elementBudget, env, false, true);
        } else {
            return survivalBuildPiece(STRUCTURE_PIECE_TIER_3, stackSize, 16, 21, 14, elementBudget, env, false, true);
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
        } else if (checkPiece(STRUCTURE_PIECE_TIER_2, 9, 9, 0)) {
            newTier = 2;
        } else if (checkPiece(STRUCTURE_PIECE_TIER_3, 16, 21, 14)) {
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
