package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_STEAMHOLE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_STEAMHOLE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_STEAMHOLE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MULTI_STEAMHOLE_GLOW;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.tileentities.render.TileEntityBlackhole;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamMegaCompressor extends MTEBetterSteamMultiBase<MTESteamMegaCompressor>
    implements ISurvivalConstructable {

    public MTESteamMegaCompressor(String aName) {
        super(aName);
    }

    public MTESteamMegaCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "Supercompressor";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTESteamMegaCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESteamMegaCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","            CCC     CCC            ","         CCCCC       CCCCC         ","         CCCC         CCCC         ","                                   ","                                   ","                                   ","         CCCC         CCCC         ","         CCCCC       CCCCC         ","            CCC     CCC            ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","             CCC   CCC             ","            CCCC   CCCC            ","           CCCCC   CCCCC           ","         CCCCCC     CCCCCC         ","       CCCCCCC       CCCCCCC       ","       CCCCCC         CCCCCC       ","                                   ","                                   ","                                   ","       CCCCCC         CCCCCC       ","       CCCCCCC       CCCCCCC       ","         CCCCCC     CCCCCC         ","           CCCCC   CCCCC           ","            CCCC   CCCC            ","             CCC   CCC             ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","      CCCCCCCB       BCCCCCCC      ","      CCCCCCB   BBB   BCCCCCC      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      CCCCCCB   BBB   BCCCCCC      ","      CCCCCCCB       BCCCCCCC      ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCBCCCCCBCCCCCCB       ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","       BCCCCCCBCCCCCBCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","    CCCCCCCCCB       BCCCCCCCCC    ","    CCCCCCCCB         BCCCCCCCC    ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","    CCCCCCCCB         BCCCCCCCC    ","    CCCCCCCCCB       BCCCCCCCCC    ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","          BB           BB          ","         BBBB  CCCCC  BBBB         ","         BBBBCC     CCBBBB         ","          BBC         CBB          ","           C           C           ","   CCC     C           C     CCC   ","   CCC    C             C    CCC   ","          C             C          ","          C             C          ","          C             C          ","   CCC    C             C    CCC   ","   CCC     C           C     CCC   ","           C           C           ","          BBC         CBB          ","         BBBBCC     CCBBBB         ","         BBBB  CCCCC  BBBB         ","          BB           BB          ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","           BBB CCCCC BBB           ","           BBCC     CCBB           ","           BC         CB           ","   CC       C         C       CC   ","   CC      C           C      CC   ","    D      C           C      D    ","    D      C           C      D    ","    D      C           C      D    ","   CC      C           C      CC   ","   CC       C         C       CC   ","           BC         CB           ","           BBCC     CCBB           ","           BBB CCCCC BBB           ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBBCCCCCBBB            ","            BBC     CBB            ","  CCC       BC       CB       CCC  ","  CCC       C         C       CCC  ","            C         C            ","            C         C            ","            C         C            ","  CCC       C         C       CCC  ","  CCC       BC       CB       CCC  ","            BBC     CBB            ","            BBBCCCCCBBB            ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","             BBCCCCCBB             ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","   D         C       C         D   ","   D         C       C         D   ","   D         C       C         D   ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","             BBCCCCCBB             ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              BCCCCCB              ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","             C       C             ","             C       C             ","             C       C             ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","              BCCCCCB              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CCC          BBCCCBB          CCC "," CCC          BC   CB          CCC ","  D           C     C           D  ","  D           C     C           D  ","  D           C     C           D  "," CCC          BC   CB          CCC "," CCC          BBCCCBB          CCC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   "},
                {"                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC            BCCCB            CC "," CC           BC   CB           CC ","              C     C              ","              C     C              ","              C     C              "," CC           BC   CB           CC "," CC            BCCCB            CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   "},
                {"                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC             BBB             CC "," CC            BCCCB            CC "," D            BCCCCCB            D "," D            BCCCCCB            D "," D            BCCCCCB            D "," CC            BCCCB            CC "," CC             BBB             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   "},
                {"                                   ","              CC   CC              ","              CCBBBCC              ","                CCC                ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC                             CC "," CC             BBB             CC ","  BC           B   B           CB  ","  BC           B   B           CB  ","  BC           B   B           CB  "," CC             BBB             CC "," CC                             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                CCC                ","              CCBBBCC              ","              CC   CC              ","                                   "},
                {"              CCDDDCC              ","              CC   CC              ","              CBBBBBC              ","               C   C               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCC                             CCC","CCBC                           CBCC","D B                             B D","D B                             B D","D B                             B D","CCBC                           CBCC","CCC                             CCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               C   C               ","              CBBBBBC              ","              CC   CC              ","              CCDDDCC              "},
                {"              CC   CC              ","              CCCCCCC              ","              BBBBBBB              ","              C ABA C              ","                ABA                ","                 A                 ","                 A                 ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBAA                         AABC "," CBBBAA                     AABBBC "," CBAA                         AABC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                 A                 ","                 A                 ","                ABA                ","              C ABA C              ","              BBBBBBB              ","              CCCCCCC              ","              CC   CC              "},
                {"              CC   CC              ","              CCCCCCC              ","              BBBBBBB              ","              C BBB C              ","                BBB                ","                ABA                ","                ABA                ","                 B                 ","                 B                 ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBBBAA                     AABBBC "," CBBBBBBB                 BBBBBBBC "," CBBBAA                     AABBBC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                 B                 ","                 B                 ","                ABA                ","                ABA                ","                BBB                ","              C BBB C              ","              BBBBBBB              ","              CCCCCCC              ","              CC   CC              "},
                {"              CC   CC              ","              CCCCCCC              ","              BBBBBBB              ","              C ABA C              ","                ABA                ","                 A                 ","                 A                 ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCBC                           CBCC","CCB                             BCC"," CBAA                         AABC "," CBBBAA                     AABBBC "," CBAA                         AABC ","CCB                             BCC","CCBC                           CBCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                 A                 ","                 A                 ","                ABA                ","              C ABA C              ","              BBBBBBB              ","              CCCCCCC              ","              CC   CC              "},
                {"              CCDDDCC              ","              CC   CC              ","              CBBBBBC              ","               C   C               ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","CCC                             CCC","CCBC                           CBCC","D B                             B D","D B                             B D","D B                             B D","CCBC                           CBCC","CCC                             CCC","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","               C   C               ","              CBBBBBC              ","              CC   CC              ","              CCDDDCC              "},
                {"                                   ","              CC   CC              ","              CCBBBCC              ","                CCC                ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC                             CC "," CC             BBB             CC ","  BC           B   B           CB  ","  BC           B   B           CB  ","  BC           B   B           CB  "," CC             BBB             CC "," CC                             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                CCC                ","              CCBBBCC              ","              CC   CC              ","                                   "},
                {"                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC             BBB             CC "," CC            BCCCB            CC "," D            BCCCCCB            D "," D            BCCCCCB            D "," D            BCCCCCB            D "," CC            BCCCB            CC "," CC             BBB             CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   "},
                {"                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CC            BCCCB            CC "," CC           BC   CB           CC ","              C     C              ","              C     C              ","              C     C              "," CC           BC   CB           CC "," CC            BCCCB            CC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   "},
                {"                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "," CCC          BBCCCBB          CCC "," CCC          BC   CB          CCC ","  D           C     C           D  ","  D           C     C           D  ","  D           C     C           D  "," CCC          BC   CB          CCC "," CCC          BBCCCBB          CCC ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CC   CC              ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              BCCCCCB              ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","             C       C             ","             C       C             ","             C       C             ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","              BCCCCCB              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","             BBCCCCCBB             ","  CC         BC     CB         CC  ","  CC         C       C         CC  ","   D         C       C         D   ","   D         C       C         D   ","   D         C       C         D   ","  CC         C       C         CC  ","  CC         BC     CB         CC  ","             BBCCCCCBB             ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","            BBBCCCCCBBB            ","            BBC     CBB            ","  CCC       BC       CB       CCC  ","  CCC       C         C       CCC  ","            C         C            ","            C         C            ","            C         C            ","  CCC       C         C       CCC  ","  CCC       BC       CB       CCC  ","            BBC     CBB            ","            BBBCCCCCBBB            ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","           BBB CCCCC BBB           ","           BBCC     CCBB           ","           BC         CB           ","   CC       C         C       CC   ","   CC      C           C      CC   ","    D      C           C      D    ","    D      C           C      D    ","    D      C           C      D    ","   CC      C           C      CC   ","   CC       C         C       CC   ","           BC         CB           ","           BBCC     CCBB           ","           BBB CCCCC BBB           ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","          BB           BB          ","         BBBB  CC~CC  BBBB         ","         BBBBCC     CCBBBB         ","          BBC         CBB          ","           C           C           ","   CCC     C           C     CCC   ","   CCC    C             C    CCC   ","          C             C          ","          C             C          ","          C             C          ","   CCC    C             C    CCC   ","   CCC     C           C     CCC   ","           C           C           ","          BBC         CBB          ","         BBBBCC     CCBBBB         ","         BBBB  CCCCC  BBBB         ","          BB           BB          ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCDDDCC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","    CCCCCCCCCB       BCCCCCCCCC    ","    CCCCCCCCB         BCCCCCCCC    ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","     DBCCCCCB         BCCCCCBD     ","    CCCCCCCCB         BCCCCCCCC    ","    CCCCCCCCCB       BCCCCCCCCC    ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CCDDDCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCBCCCCCBCCCCCCB       ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","      BCCCCCBCCCCCCCCCBCCCCCB      ","     CCCCCCCBCCCCCCCCCBCCCCCCC     ","     CCCCCCCCBCCCCCCCBCCCCCCCC     ","       BCCCCCCBCCCCCBCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CCBBBCC              ","            BBCCCCCCCBB            ","           BCCCCCCCCCCCB           ","          BCCCCCCCCCCCCCB          ","         BCCCCCCCCCCCCCCCB         ","        BCCCCCCCCCCCCCCCCCB        ","       BCCCCCCCBBBBBCCCCCCCB       ","       BCCCCCCB     BCCCCCCB       ","      CCCCCCCB       BCCCCCCC      ","      CCCCCCB   BBB   BCCCCCC      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      BCCCCCB  B   B  BCCCCCB      ","      CCCCCCB   BBB   BCCCCCC      ","      CCCCCCCB       BCCCCCCC      ","       BCCCCCCB     BCCCCCCB       ","       BCCCCCCCBBBBBCCCCCCCB       ","        BCCCCCCCCCCCCCCCCCB        ","         BCCCCCCCCCCCCCCCB         ","          BCCCCCCCCCCCCCB          ","           BCCCCCCCCCCCB           ","            BBCCCCCCCBB            ","              CCBBBCC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","             CCC   CCC             ","            CCCC   CCCC            ","           CCCCC   CCCCC           ","         CCCCCC     CCCCCC         ","       CCCCCCC       CCCCCCC       ","       CCCCCC         CCCCCC       ","                                   ","                                   ","                                   ","       CCCCCC         CCCCCC       ","       CCCCCCC       CCCCCCC       ","         CCCCCC     CCCCCC         ","           CCCCC   CCCCC           ","            CCCC   CCCC            ","             CCC   CCC             ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "},
                {"                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","              CC   CC              ","              CC   CC              ","              CC   CC              ","             CCC   CCC             ","            CCC     CCC            ","         CCCCC       CCCCC         ","         CCCC         CCCC         ","                                   ","                                   ","                                   ","         CCCC         CCCC         ","         CCCCC       CCCCC         ","            CCC     CCC            ","             CCC   CCC             ","              CC   CC              ","              CC   CC              ","              CC   CC              ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   ","                                   "}
            }))
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            ofChain(
                buildSteamInput(MTESteamMegaCompressor.class)
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(1)
                    .build(),
                buildHatchAdder(MTESteamMegaCompressor.class)
                    .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(),
                ofBlock(GregTechAPI.sBlockCasings2, 0)))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings1, 10))
        .addElement('D', ofFrame(Materials.Steel))
        .build();

    @Override
    public IStructureDefinition<MTESteamMegaCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 17, 27, 10);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 17, 27, 10, realBudget, env, false, true);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return compressorRecipes;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Has 256 parallels")
            .addInfo("Only uses supercritical steam")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Shoots impossibly pressurized jets of steam at material to compress it.")
            .addInfo(
                EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Capable of creating superdense plates...")
            .addInfo("Created by: " + AuthorSteamIsTheNumber)
            .toolTipFinisher();
        return tt;
    }

    FluidStack scSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);

    @Override
    public ArrayList<FluidStack> getAllSteamStacks() {
        ArrayList<FluidStack> aFluids = new ArrayList<>();
        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid.isFluidEqual(scSteam)) {
                aFluids.add(aFluid);
            }
        }
        return aFluids;
    }

    @Override
    public boolean tryConsumeSteam(int aAmount) {
        if (getTotalSteamStored() <= 0) {
            return false;
        } else {
            return this.depleteInput(FluidRegistry.getFluidStack("supercriticalsteam", aAmount));
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_MULTI_STEAMHOLE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_MULTI_STEAMHOLE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_MULTI_STEAMHOLE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_MULTI_STEAMHOLE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)) };
        }
        return rTexture;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 17, 27, 10);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).limitOverclockCount(0);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                createRenderBlock();
                return super.onRecipeStart(recipe);
            }

            @Override
            public ProcessingLogic clear() {
                destroyRenderBlock();
                return super.clear();
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 256;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[3]);
        // We need to trick the GT_ParallelHelper we have enough amps for all recipe parallels.
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamMegaCompressor(this.mName);
    }

    @Override
    public void onBlockDestroyed() {
        destroyRenderBlock();
        super.onBlockDestroyed();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d != ForgeDirection.UP && d != ForgeDirection.DOWN;
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    private TileEntityBlackhole rendererTileEntity = null;

    // Returns true if render was actually created
    private boolean createRenderBlock() {
        if (!mMachine) return false;
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        ForgeDirection opposite = getDirection().getOpposite();
        int x = 7 * opposite.offsetX;
        int z = 7 * opposite.offsetZ;
        int y = 11;

        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, Blocks.air);
        base.getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, GregTechAPI.sBlackholeRender);
        rendererTileEntity = (TileEntityBlackhole) base.getWorld()
            .getTileEntity(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z);

        rendererTileEntity.startScaleChange(true);
        rendererTileEntity.toggleLaser(true);
        rendererTileEntity.setLaserColor(6, 6, 6);
        rendererTileEntity.setStability(0);
        return true;
    }

    private void destroyRenderBlock() {
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        ForgeDirection opposite = getDirection().getOpposite();
        int x = 7 * opposite.offsetX;
        int z = 7 * opposite.offsetZ;
        int y = 11;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(base.getXCoord() + x, base.getYCoord() + y, base.getZCoord() + z, Blocks.air);
    }
}
