package gregtech.common.tileentities.machines.multi.LHC;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
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
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.MTETargetChamber;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;


public class MTELargeHadronCollider extends MTEExtendedPowerMultiBlockBase<MTELargeHadronCollider>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();


    private static final IStructureDefinition<MTELargeHadronCollider> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeHadronCollider>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                           CCCCCCCCCCCCCCC                                   ",
                "                                                           CCCEEECEEECEEEC                                   ",
                "                                                           CCCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                           CCCCCCCCCCCCCCC                                   ",
                "                                                  CCCCCCCCCCCCCCCCCCCCCCA                                    ",
                "                                                  CEEE~EEECCCCCCCCCCCCCCA                                    ",
                "                                                  CCCCCCCCCCCCCCCCCCCCCCA                                    ",
                "                                                           CCCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                                  CCCCCCCCCC            A                                    ",
                "                                              CCCC         CCCC         D                                    ",
                "                                              CCCC         CCCC         D                                    ",
                "                                              CCCC         CCCC         D                                    ",
                "                                                  CCCCCCCCCC            A                                    ",
                "                                                           CCCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                             CCCCCCCCCCCCC                                   ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                              CCCCCCCCCCCCCCCCC         D                                    ",
                "                                            CC                 CC       D                                    ",
                "                                            CC                 CC       D                                    ",
                "                                            CC                 CC       D                                    ",
                "                                              CCCCCCCCCCCCCCCCC         D                                    ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                             CEEECEEECEEEC                                   ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                            CCCCCC       CCCCCCCC       D                                    ",
                "                                          CC      CCCCCCCCC      CC     D                                    ",
                "                                          CC      CEEEECCCC      CC     F                                    ",
                "                                          CC      CCCCCCCCC      CC     D                                    ",
                "                                            CCCCCC       CCCCCCCC       D                                    ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                             CCCCCCCCCCCCC                                   ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                          CCCCC           CC  CCCCC     D                                    ",
                "                                         C     CCC      CC CCC     C    D                                    ",
                "                                         C     CCC     CCC CCC     C    D                                    ",
                "                                         C     CCC      CC CCC     C    D                                    ",
                "                                          CCCCC           CC  CCCCC     D                                    ",
                "                                                           CCCCCCCCCCCCCA                                    ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                         CCCC              C    CCCC    A                                    ",
                "                                       CC    CC           CC  CC    CCC D                                    ",
                "                                       CC    CC           CC  CC    CCC D                                    ",
                "                                       CC    CC           CC  CC    CCC D                                    ",
                "                                         CCCC              C    CCCC    A                                    ",
                "                                                            CCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                       CCCC                 CCCCCCCCCCCCCC                                   ",
                "                                      C    CC               CCCCCC    CCA                                    ",
                "                                      C    CC               CCEEEC    CCA                                    ",
                "                                      C    CC               CCCCCC    CCA                                    ",
                "                                       CCCC                 CCCCCCCCCCCCCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                      CCCC                         CCCC                                      ",
                "                                     C    C                       C    CCC                                   ",
                "                                     C    C                       C    CCC                                   ",
                "                                     C    C                       C    CCC                                   ",
                "                                      CCCC                         CCCC                                      ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                     CCCC                           CCCC                                     ",
                "                                    C    C                         C    C                                    ",
                "                                    C    C                         C    C                                    ",
                "                                    C    C                         C    C                                    ",
                "                                     CCCC                           CCCC                                     ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                     CCC                             CCC                                     ",
                "                                   CC   C                           C   C                                    ",
                "                                   CC   C                           C   C                                    ",
                "                                   CC   C                           C   C                                    ",
                "                                     CCC                             CCC                                     ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                    CCC                               CCC                                    ",
                "                                  CC   C                             C   C                                   ",
                "                                  CC   C                             C   C                                   ",
                "                                  CC   C                             C   C                                   ",
                "                                    CCC                               CCC                                    ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                   CCC                                 CCC                                   ",
                "                                 CC   C                               C   C                                  ",
                "                                 CC   C                               C   C                                  ",
                "                                 CC   C                               C   C                                  ",
                "                                   CCC                                 CCC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                   CC                                   CC                                   ",
                "                                 CC  C                                 C  C                                  ",
                "                                C C  C                                 C  C                                  ",
                "                                C C  C                                 C  C                                  ",
                "                                CCCCC                                   CC                                   ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                                                                                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                  CCC                                   CCC                                  ",
                "                                CC   C                                 C   C                                 ",
                "                               CCC   C                                 C   C                                 ",
                "                               C C   C                                 C   C                                 ",
                "                               C  CCC                                   CCC                                  ",
                "                               CCCC                                                                          ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                          CCCCC               CCCCC                                          ",
                "                                          CCCCC               CCCCC                                          ",
                "                                          CCCCC               CCCCC                                          ",
                "                                               CCCCCCCCCCCCCCC                                               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CCC                                     CC                                  ",
                "                                CC  C                                   C  C                                 ",
                "                               CCC  C                                   C  C                                 ",
                "                              C  C  C                                   C  C                                 ",
                "                              C   CC                                     CC                                  ",
                "                              C   C                                                                          ",
                "                              CCCC        CCCCCCCCCCCCCCCCCCCCCCCCC                                          ",
                "                                      CCCC                         CCCC                                      ",
                "                                      CCCC                         CCCC                                      ",
                "                                      CCCC                         CCCC                                      ",
                "                                          CCCCCCCCCCCCCCCCCCCCCCCCC                                          "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CCC                                     CCC                                 ",
                "                                C   C                                   C   C                                ",
                "                              CCC   C                                   C   C                                ",
                "                             C  C   C                                   C   C                                ",
                "                             C   CCC                                     CCC                                 ",
                "                             C    C                                                                          ",
                "                             C    C   CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC                                      ",
                "                              CCCC CCC                                 CCC                                   ",
                "                                   CCC                                 CCC                                   ",
                "                                   CCC                                 CCC                                   ",
                "                                      CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC                                      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CC                                 ",
                "                               CC  C                                     C  C                                ",
                "                              CCC  C                                     C  C                                ",
                "                             C  C  C                                     C  C                                ",
                "                            C    CC                                       CC                                 ",
                "                            C     C                                                                          ",
                "                            C     CCCCCCCCCCCCC               CCCCCCCCCCCC                                   ",
                "                            CCCCCCC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                 CC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                 CC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                   CCCCCCCCCCCC               CCCCCCCCCCCC                                   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CCC                                       CC                                 ",
                "                               CC  C                                     C  C                                ",
                "                              CCC  C                                     C  C                                ",
                "                             C CC  C                                     C  C                                ",
                "                           CC   CCC                                       CC                                 ",
                "                           C    CC                                                                           ",
                "                           C    CCCCCCCCCC                         CCCCCCCCC                                 ",
                "                           C   CC         CCCCC               CCCCC         CC                               ",
                "                            CCCCC         CCCCC               CCCCC         CC                               ",
                "                               CC         CCCCC               CCCCC         CC                               ",
                "                                 CCCCCCCCC                         CCCCCCCCC                                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CCC                                       CC                                 ",
                "                               CC  C                                     C  C                                ",
                "                              CCC  C                                     C  C                                ",
                "                             C CC  C                                     C  C                                ",
                "                           CC  C CC                                       CC                                 ",
                "                          C    C                                                                             ",
                "                          C    CCCCCCCC                               CCCCCCCC                               ",
                "                          C  CC        CCC                         CCC        CC                             ",
                "                           CCCC        CCC                         CCC        CC                             ",
                "                             CC        CCC                         CCC        CC                             ",
                "                               CCCCCCCC                               CCCCCCCC                               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               C  C                                       C  C                               ",
                "                             CCC  C                                       C  C                               ",
                "                          CCCCCCCC                                         CC                                ",
                "                         CC   CC                                                                             ",
                "                         C   CCCCCCC                                     CCCCCCC                             ",
                "                         C CC       CCC                               CCC       CC                           ",
                "                          CCC       CCC                               CCC       CC                           ",
                "                           CC       CCC                               CCC       CC                           ",
                "                             CCCCCCC                                     CCCCCCC                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                          CCCCCCCC                                         CC                                ",
                "                         CC  CCC                                                                             ",
                "                        CCCCCCCCCC                                         CCCCCCC                           ",
                "                        CCC       CC                                     CC       CC                         ",
                "                        CCC       CC                                     CC       CC                         ",
                "                        CCC       CC                                     CC       CC                         ",
                "                           CCCCCCC                                         CCCCCCC                           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                           CC   CC                                         CC                                ",
                "                        CCCCCC                                                                               ",
                "                       CCCCCCCCC                                             CCCCCCC                         ",
                "                       CC       CC                                         CC       C                        ",
                "                       CC       CC                                         CC       C                        ",
                "                       CC       CC                                         CC       C                        ",
                "                         CCCCCCC                                             CCCCCCC                         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                                CC                                         CC                                ",
                "                        CCCCC                                                                                ",
                "                       CCCCCCC                                                 CCCCCC                        ",
                "                      CC      CC                                             CC      CC                      ",
                "                      CC      CC                                             CC      CC                      ",
                "                      CC      CC                                             CC      CC                      ",
                "                        CCCCCC                                                 CCCCCC                        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                                CC                                         CC                                ",
                "                         CC                                                                                  ",
                "                      CCCCCC                                                     CCCCCC                      ",
                "                     C      CC                                                 CC      C                     ",
                "                     C      CC                                                 CC      C                     ",
                "                     C      CC                                                 CC      C                     ",
                "                      CCCCCC                                                     CCCCCC                      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                                CC                                         CC                                ",
                "                                                                                                             ",
                "                     CCCCCC                                                        CCCCC                     ",
                "                    C     CC                                                     CC     C                    ",
                "                    C     CC                                                     CC     C                    ",
                "                    C     CC                                                     CC     C                    ",
                "                     CCCCC                                                         CCCCC                     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C                               ",
                "                                CC                                         CC     CCC                        ",
                "                                                                                  CCCCC                      ",
                "                    CCCCC                                                         CCCCCCCC                   ",
                "                   C     C                                                         C     C                   ",
                "                   C     C                                                         C     C                   ",
                "                   C     C                                                         C     C                   ",
                "                    CCCCC                                                           CCCCC                    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               E  E                                       E  E                               ",
                "                               C  C                                       C  C  CCCC                         ",
                "                                CC                                         CC   CCC CCC                      ",
                "                                                                                CCC    CC                    ",
                "                   CCCCC                                                          C CCCCCCC                  ",
                "                 CC     C                                                          CC     CC                 ",
                "                 CC     C                                                           C     CC                 ",
                "                 CC     C                                                           C     CC                 ",
                "                   CCCCC                                                             CCCCC                   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CC                                         CC                                ",
                "                               C  C                                       C  C                               ",
                "                               C  C                                       C  CCCCCC                          ",
                "                               C  C                                       C  CCC   CC                        ",
                "                                CC                                         CCCCC     CC                      ",
                "                                                                              CC       CCC                   ",
                "                 CCCCC                                                          CCC    CCCCC                 ",
                "                C     CC                                                           CCCC     C                ",
                "                C     CC                                                             CC     C                ",
                "                C     CC                                                             CC     C                ",
                "                 CCCCC                                                                 CCCCC                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CCCC                               ",
                "                                C  C                                     C  CCCC                             ",
                "                                C  C                                     C  CC  CCC                          ",
                "                                C  C                                     C  CC     CC                        ",
                "                                 CC                                       CCC        CC                      ",
                "                                                                             CCCC      CCC                   ",
                "                CCCCC                                                          CCCC    CCCCCC                ",
                "               C     C                                                             CC  C     C               ",
                "               C     C                                                               CCC     C               ",
                "               C     C                                                                 C     C               ",
                "                CCCCC                                                                   CCCCC                "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CCCC                               ",
                "                                C  C                                     C  C CC                             ",
                "                                C  C                                     C  C   CC                           ",
                "                                C  C                                     C  C    CC                          ",
                "                                 CC                                       CCC    CCCC                        ",
                "                                                                             CCCCCCCCCCCC                    ",
                "               CCCCC                                                           CC  CCCCCCCCCCC               ",
                "              C     C                                                                CCCC     C              ",
                "              C     C                                                                   C     C              ",
                "              C     C                                                                   C     C              ",
                "               CCCCC                                                                     CCCCC               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CCCC                               ",
                "                                C  C                                     C  C C                              ",
                "                                C  C                                     C  C  CC                            ",
                "                                C  C                                     C  C  CCC                           ",
                "                                 CC                                       CCCCCCCC                           ",
                "                                                                              CCC                            ",
                "               CCCC                                                                     CCCCCC               ",
                "              C    C                                                                     C    C              ",
                "              C    C                                                                     C    C              ",
                "              C    C                                                                     C    C              ",
                "               CCCC                                                                       CCCC               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CCC                                     CCCC                                ",
                "                                C   C                                   C   CC                               ",
                "                                C   C                                   C   CCC                              ",
                "                                C   C                                   C   CCC                              ",
                "                                 CCC                                     CCC                                 ",
                "                                                                                                             ",
                "              CCCC                                                                        CCCCC              ",
                "             C    C                                                                       C    C             ",
                "             C    C                                                                       C    C             ",
                "             C    C                                                                       C    C             ",
                "              CCCC                                                                         CCCC              "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                  CC                                     CC                                  ",
                "                                 C  C                                   C  CC                                ",
                "                                 C  C                                   C  CC                                ",
                "                                 C  C                                   C  CC                                ",
                "                                  CC                                     CC                                  ",
                "                                                                                                             ",
                "             CCCC                                                                           CCCC             ",
                "            C    C                                                                        CC    C            ",
                "            C    C                                                                        CC    C            ",
                "            C    C                                                                        CC    C            ",
                "             CCCC                                                                           CCCC             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                  CCC                                   CCC                                  ",
                "                                 C   C                                 C   C                                 ",
                "                                 C   C                                 C   C                                 ",
                "                                 C   C                                 C   C                                 ",
                "                                  CCC                                   CCC                                  ",
                "                                                                                                             ",
                "            CCCC                                                                             CCCC            ",
                "           C    C                                                                           C    C           ",
                "           C    C                                                                           C    C           ",
                "           C    C                                                                           C    C           ",
                "            CCCC                                                                             CCCC            "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                   CC                                   CC                                   ",
                "                                  C  C                                 C  C                                  ",
                "                                  C  C                                 C  C                                  ",
                "                                  C  C                                 C  C                                  ",
                "                                   CC                                   CC                                   ",
                "                                                                                                             ",
                "           CCCC                                                                               CCCC           ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "           CCCC                                                                               CCCC           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                   CCC                                 CCC                                   ",
                "                                  C   C                               C   C                                  ",
                "                                  C   C                               C   C                                  ",
                "                                  C   C                               C   C                                  ",
                "                                   CCC                                 CCC                                   ",
                "                                                                                                             ",
                "           CCCC                                                                               CCCC           ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "           CCCC                                                                               CCCC           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                    CCC                               CCC                                    ",
                "                                   C   C                             C   C                                   ",
                "                                   C   C                             C   C                                   ",
                "                                   C   C                             C   C                                   ",
                "                                    CCC                               CCC                                    ",
                "                                                                                                             ",
                "          CCCC                                                                                 CCCC          ",
                "         C    C                                                                               C    C         ",
                "         C    C                                                                               C    C         ",
                "         C    C                                                                               C    C         ",
                "          CCCC                                                                                 CCCC          "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                     CCC                             CCC                                     ",
                "                                    C   C                           C   C                                    ",
                "                                    C   C                           C   C                                    ",
                "                                    C   C                           C   C                                    ",
                "                                     CCC                             CCC                                     ",
                "                                                                                                             ",
                "         CCCC                                                                                   CCCC         ",
                "        C    C                                                                                 C    C        ",
                "        C    C                                                                                 C    C        ",
                "        C    C                                                                                 C    C        ",
                "         CCCC                                                                                   CCCC         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                     CCCC                           CCCC                                     ",
                "                                    C    C                         C    C                                    ",
                "                                    C    C                         C    C                                    ",
                "                                    C    C                         C    C                                    ",
                "                                     CCCC                           CCCC                                     ",
                "                                                                                                             ",
                "         CCC                                                                                     CCC         ",
                "        C   C                                                                                   C   C        ",
                "        C   C                                                                                   C   C        ",
                "        C   C                                                                                   C   C        ",
                "         CCC                                                                                     CCC         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                      CCCC                         CCCC                                      ",
                "                                     C    C                       C    C                                     ",
                "                                     C    C                       C    C                                     ",
                "                                     C    C                       C    C                                     ",
                "                                      CCCC                         CCCC                                      ",
                "                                                                                                             ",
                "        CCCC                                                                                     CCCC        ",
                "       C    C                                                                                   C    C       ",
                "       C    C                                                                                   C    C       ",
                "       C    C                                                                                   C    C       ",
                "        CCCC                                                                                     CCCC        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                       CCCC                       CCCC                                       ",
                "                                      C    CC                   CC    C                                      ",
                "                                      C    CC                   CC    C                                      ",
                "                                      C    CC                   CC    C                                      ",
                "                                       CCCC                       CCCC                                       ",
                "                                                                                                             ",
                "        CCC                                                                                       CCC        ",
                "       C   C                                                                                     C   C       ",
                "       C   C                                                                                     C   C       ",
                "       C   C                                                                                     C   C       ",
                "        CCC                                                                                       CCC        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                         CCCC                   CCCC                                         ",
                "                                       CC    CC               CC    CC                                       ",
                "                                       CC    CC               CC    CC                                       ",
                "                                       CC    CC               CC    CC                                       ",
                "                                         CCCC                   CCCC                                         ",
                "                                                                                                             ",
                "       CCCC                                                                                       CCCC       ",
                "      C    C                                                                                     C    C      ",
                "      C    C                                                                                     C    C      ",
                "      C    C                                                                                     C    C      ",
                "       CCCC                                                                                       CCCC       "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                          CCCCC               CCCCC                                          ",
                "                                         C     CCC         CCC     C                                         ",
                "                                         C     CCC         CCC     C                                         ",
                "                                         C     CCC         CCC     C                                         ",
                "                                          CCCCC               CCCCC                                          ",
                "                                                                                                             ",
                "       CCC                                                                                         CCC       ",
                "      C   C                                                                                       C   C      ",
                "      C   C                                                                                       C   C      ",
                "      C   C                                                                                       C   C      ",
                "       CCC                                                                                         CCC       "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                            CCCCCC         CCCCCC                                            ",
                "                                          CC      CCCCCCCCC      CC                                          ",
                "                                          CC      CEEEEEEEC      CC                                          ",
                "                                          CC      CCCCCCCCC      CC                                          ",
                "                                            CCCCCC         CCCCCC                                            ",
                "                                                                                                             ",
                "      CCCC                                                                                         CCCC      ",
                "     C    C                                                                                       C    C     ",
                "     C    C                                                                                       C    C     ",
                "     C    C                                                                                       C    C     ",
                "      CCCC                                                                                         CCCC      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                              CCCCCCCCCCCCCCCCC                                              ",
                "                                            CC                 CC                                            ",
                "                                            CC                 CC                                            ",
                "                                            CC                 CC                                            ",
                "                                              CCCCCCCCCCCCCCCCC                                              ",
                "                                                                                                             ",
                "      CCC                                                                                           CCC      ",
                "     C   C                                                                                         C   C     ",
                "     C   C                                                                                         C   C     ",
                "     C   C                                                                                         C   C     ",
                "      CCC                                                                                           CCC      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                  CCCCCCCCC                                                  ",
                "                                              CCCC         CCCC                                              ",
                "                                              CCCC         CCCC                                              ",
                "                                              CCCC         CCCC                                              ",
                "                                                  CCCCCCCCC                                                  ",
                "                                                                                                             ",
                "     CCCC                                                                                           CCCC     ",
                "    C    C                                                                                         C    C    ",
                "    C    C                                                                                         C    C    ",
                "    C    C                                                                                         C    C    ",
                "     CCCC                                                                                           CCCC     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                  CCCCCCCCC                                                  ",
                "                                                  CEEEEEEEC                                                  ",
                "                                                  CCCCCCCCC                                                  ",
                "                                                                                                             ",
                "                                                                                                             ",
                "     CCC                                                                                             CCC     ",
                "    C   C                                                                                           C   C    ",
                "    C   C                                                                                           C   C    ",
                "    C   C                                                                                           C   C    ",
                "     CCC                                                                                             CCC     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCCC                                                                                             CCCC    ",
                "   C    C                                                                                           C    C   ",
                "   C    C                                                                                           C    C   ",
                "   C    C                                                                                           C    C   ",
                "    CCCC                                                                                             CCCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCC                                                                                               CCC    ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "    CCC                                                                                               CCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCC                                                                                               CCC    ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "    CCC                                                                                               CCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCCC                                                                                               CCCC   ",
                "  C    C                                                                                             C    C  ",
                "  C    C                                                                                             C    C  ",
                "  C    C                                                                                             C    C  ",
                "   CCCC                                                                                               CCCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                " CCC                                                                                                     CCC ",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                "C   C                                                                                                   C   C",
                " CCC                                                                                                     CCC "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "  CCC                                                                                                   CCC  ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                " C   C                                                                                                 C   C ",
                "  CCC                                                                                                   CCC  "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCC                                                                                                 CCC   ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "  C   C                                                                                               C   C  ",
                "   CCC                                                                                                 CCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "   CCCC                                                                                               CCCC   ",
                "  C    C                                                                                             C    C  ",
                "  C    C                                                                                             C    C  ",
                "  C    C                                                                                             C    C  ",
                "   CCCC                                                                                               CCCC   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCC                                                                                               CCC    ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "    CCC                                                                                               CCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCC                                                                                               CCC    ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "   C   C                                                                                             C   C   ",
                "    CCC                                                                                               CCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "    CCCC                                                                                             CCCC    ",
                "   C    C                                                                                           C    C   ",
                "   C    C                                                                                           C    C   ",
                "   C    C                                                                                           C    C   ",
                "    CCCC                                                                                             CCCC    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "     CCC                                                                                             CCC     ",
                "    C   C                                                                                           C   C    ",
                "    C   C                                                                                           C   C    ",
                "    C   C                                                                                           C   C    ",
                "     CCC                                                                                             CCC     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "     CCCC                                                                                           CCCC     ",
                "    C    C                                                                                         C    C    ",
                "    C    C                                                                                         C    C    ",
                "    C    C                                                                                         C    C    ",
                "     CCCC                                                                                           CCCC     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "      CCC                                                                                           CCC      ",
                "     C   C                                                                                         C   C     ",
                "     C   C                                                                                         C   C     ",
                "     C   C                                                                                         C   C     ",
                "      CCC                                                                                           CCC      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "      CCCC                                                                                         CCCC      ",
                "     C    C                                                                                       C    C     ",
                "     C    C                                                                                       C    C     ",
                "     C    C                                                                                       C    C     ",
                "      CCCC                                                                                         CCCC      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "       CCC                                                                                         CCC       ",
                "      C   C                                                                                       C   C      ",
                "      C   C                                                                                       C   C      ",
                "      C   C                                                                                       C   C      ",
                "       CCC                                                                                         CCC       "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "       CCCC                                                                                       CCCC       ",
                "      C    C                                                                                     C    C      ",
                "      C    C                                                                                     C    C      ",
                "      C    C                                                                                     C    C      ",
                "       CCCC                                                                                       CCCC       "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "        CCC                                                                                       CCC        ",
                "       C   C                                                                                     C   C       ",
                "       C   C                                                                                     C   C       ",
                "       C   C                                                                                     C   C       ",
                "        CCC                                                                                       CCC        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "        CCCC                                                                                     CCCC        ",
                "       C    C                                                                                   C    C       ",
                "       C    C                                                                                   C    C       ",
                "       C    C                                                                                   C    C       ",
                "        CCCC                                                                                     CCCC        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "         CCC                                                                                     CCC         ",
                "        C   C                                                                                   C   C        ",
                "        C   C                                                                                   C   C        ",
                "        C   C                                                                                   C   C        ",
                "         CCC                                                                                     CCC         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "         CCCC                                                                                   CCCC         ",
                "        C    C                                                                                 C    C        ",
                "        C    C                                                                                 C    C        ",
                "        C    C                                                                                 C    C        ",
                "         CCCC                                                                                   CCCC         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "          CCCC                                                                                 CCCC          ",
                "         C    C                                                                               C    C         ",
                "         C    C                                                                               C    C         ",
                "         C    C                                                                               C    C         ",
                "          CCCC                                                                                 CCCC          "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "           CCCC                                                                               CCCC           ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "           CCCC                                                                               CCCC           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "           CCCC                                                                               CCCC           ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "          C    C                                                                             C    C          ",
                "           CCCC                                                                               CCCC           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "            CCCC                                                                             CCCC            ",
                "           C    C                                                                           C    C           ",
                "           C    C                                                                           C    C           ",
                "           C    C                                                                           C    C           ",
                "            CCCC                                                                             CCCC            "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "             CCCC                                                                           CCCC             ",
                "            C    C                                                                         C    C            ",
                "            C    C                                                                         C    C            ",
                "            C    C                                                                         C    C            ",
                "             CCCC                                                                           CCCC             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "              CCCC                                                                         CCCC              ",
                "             C    C                                                                       C    C             ",
                "             C    C                                                                       C    C             ",
                "             C    C                                                                       C    C             ",
                "              CCCC                                                                         CCCC              "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "               CCCC                                                                       CCCC               ",
                "              C    C                                                                     C    C              ",
                "              C    C                                                                     C    C              ",
                "              C    C                                                                     C    C              ",
                "               CCCC                                                                       CCCC               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "               CCCCC                                                                     CCCCC               ",
                "              C     C                                                                   C     C              ",
                "              C     C                                                                   C     C              ",
                "              C     C                                                                   C     C              ",
                "               CCCCC                                                                     CCCCC               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                CCCCC                                                                   CCCCC                ",
                "               C     C                                                                 C     C               ",
                "               C     C                                                                 C     C               ",
                "               C     C                                                                 C     C               ",
                "                CCCCC                                                                   CCCCC                "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                 CCCCC                                                                 CCCCC                 ",
                "                C     CC                                                             CC     C                ",
                "                C     CC                                                             CC     C                ",
                "                C     CC                                                             CC     C                ",
                "                 CCCCC                                                                 CCCCC                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                   CCCCC                                                             CCCCC                   ",
                "                 CC     C                                                           C     CC                 ",
                "                 CC     C                                                           C     CC                 ",
                "                 CC     C                                                           C     CC                 ",
                "                   CCCCC                                                             CCCCC                   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                    CCCCC                                                           CCCCC                    ",
                "                   C     C                                                         C     C                   ",
                "                   C     C                                                         C     C                   ",
                "                   C     C                                                         C     C                   ",
                "                    CCCCC                                                           CCCCC                    "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                     CCCCC                                                         CCCCC                     ",
                "                    C     CC                                                     CC     C                    ",
                "                    C     CC                                                     CC     C                    ",
                "                    C     CC                                                     CC     C                    ",
                "                     CCCCC                                                         CCCCC                     "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                      CCCCCC                                                     CCCCCC                      ",
                "                     C      CC                                                 CC      C                     ",
                "                     C      CC                                                 CC      C                     ",
                "                     C      CC                                                 CC      C                     ",
                "                      CCCCCC                                                     CCCCCC                      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                        CCCCCC                                                 CCCCCC                        ",
                "                      CC      CC                                             CC      CC                      ",
                "                      CC      CC                                             CC      CC                      ",
                "                      CC      CC                                             CC      CC                      ",
                "                        CCCCCC                                                 CCCCCC                        "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                         CCCCCCC                                             CCCCCCC                         ",
                "                        C       CC                                         CC       C                        ",
                "                        C       CC                                         CC       C                        ",
                "                        C       CC                                         CC       C                        ",
                "                         CCCCCCC                                             CCCCCCC                         "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                           CCCCCCC                                         CCCCCCC                           ",
                "                         CC       CC                                     CC       CC                         ",
                "                         CC       CC                                     CC       CC                         ",
                "                         CC       CC                                     CC       CC                         ",
                "                           CCCCCCC                                         CCCCCCC                           "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                             CCCCCCC                                     CCCCCCC                             ",
                "                           CC       CCC                               CCC       CC                           ",
                "                           CC       CCC                               CCC       CC                           ",
                "                           CC       CCC                               CCC       CC                           ",
                "                             CCCCCCC                                     CCCCCCC                             "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                               CCCCCCCC                               CCCCCCCC                               ",
                "                             CC        CCC                         CCC        CC                             ",
                "                             CC        CCC                         CCC        CC                             ",
                "                             CC        CCC                         CCC        CC                             ",
                "                               CCCCCCCC                               CCCCCCCC                               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CCCCCCCCC                         CCCCCCCCC                                 ",
                "                               CC         CCCCC               CCCCC         CC                               ",
                "                               CC         CCCCC               CCCCC         CC                               ",
                "                               CC         CCCCC               CCCCC         CC                               ",
                "                                 CCCCCCCCC                         CCCCCCCCC                                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                   CCCCCCCCCCCC               CCCCCCCCCCCC                                   ",
                "                                 CC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                 CC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                 CC            CCCCCCCCCCCCCCC            CC                                 ",
                "                                   CCCCCCCCCCCC               CCCCCCCCCCCC                                   "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                      CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC                                      ",
                "                                   CCC                                 CCC                                   ",
                "                                   CCC                                 CCC                                   ",
                "                                   CCC                                 CCC                                   ",
                "                                      CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC                                      "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                          CCCCCCCCCCCCCCCCCCCCCCCCC                                          ",
                "                                      CCCC                         CCCC                                      ",
                "                                      CCCC                         CCCC                                      ",
                "                                      CCCC                         CCCC                                      ",
                "                                          CCCCCCCCCCCCCCCCCCCCCCCCC                                          "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                          CCCCC               CCCCC                                          ",
                "                                          CCCCC               CCCCC                                          ",
                "                                          CCCCC               CCCCC                                          ",
                "                                               CCCCCCCCCCCCCCC                                               "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                                                                                             ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                               CCCCCCCCCCCCCCC                                               ",
                "                                                                                                             "
            }})
        //spotless:on
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings13, 10) ) // collider casing
        .addElement('A', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
        .addElement('D', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
        .addElement('E', lazy(t -> { // neonite saffron mango or whatever
            if (Mods.Chisel.isModLoaded()) {
                Block neonite = GameRegistry.findBlock(Mods.Chisel.ID, "neonite");
                return ofBlockAnyMeta(neonite, 1);
            } else {
                return ofBlockAnyMeta(Blocks.air);
            }}))
        .addElement('F', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchInputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(2)
            .adder(MTELargeHadronCollider::addBeamLineInputHatch).build()) // beamline input hatch

        .build();

    public MTELargeHadronCollider(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeHadronCollider(String aName) {
        super(aName);
    }

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }




    @Override
    public IStructureDefinition<MTELargeHadronCollider> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeHadronCollider(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
                                 int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE) // todo: new texture
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW) // todo: new texture
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY) // todo: new texture
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW) // todo: new texture
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Particle Collider")
            .addBulkMachineInfo(0, 0.0F, 0F)
            .beginStructureBlock(109, 13, 122, false)
            .addController("Front Center")
            .addCasingInfoExactly("Collider Casing", 5817, false)
            .addCasingInfoExactly("Shielded Accelerator Casing", 16, false)
            .addCasingInfoExactly("Shielded Accelerator Glass", 20, false)
            .addCasingInfoExactly("Beamline Input Hatch", 1, false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 54, 4, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0) && mCasingAmount >= 14;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() { // todo: new
        return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage())); // todo: new
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.brewingRecipes; // todo: new
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
