package gregtech.common.tileentities.machines.multi.LHC;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;
import static java.lang.Math.max;
import static java.lang.Math.min;


public class MTELargeHadronCollider extends MTEExtendedPowerMultiBlockBase<MTELargeHadronCollider>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private ArrayList<MTEHatchAdvancedOutputBeamline> mOutputBeamline = new ArrayList<>();
    @Nullable
    private HeatingCoilLevel mCoilLevel = null;
    @Nullable
    private Byte mSolenoidLevel = null;

    private float outputEnergy;
    private int outputRate;
    private int outputParticleID;
    private float outputFocus;

    private static final IStructureDefinition<MTELargeHadronCollider> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeHadronCollider>builder()

        //<editor-fold desc="main structure">
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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
            }, {
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

        //</editor-fold>

        //<editor-fold desc="electromagnetism module">
        .addShape(
            LHCModules.EM.structurePiece,
            new String[][]{{
                "           ",
                "           ",
                "CCCCCCCCCCC",
                "CCDDDCDDDCC",
                "CBCCCCCCCBC",
                "CBC1CBC1CBC",
                "CBCCCCCCCBC",
                "CCDDDCDDDCC",
                "CCCCCCCCCCC",
                "           ",
                "           "
            }, {
                "           ",
                "CCBBBBBBBCC",
                "CDI  J  IDC",
                "CCCCCCCCCCC",
                " DI  J  ID ",
                " DI  J  ID ",
                " DI  J  ID ",
                "CCCCCCCCCCC",
                "CDI  J  IDC",
                "CCBBBBBBBCC",
                "           "
            }, {
                "CCCCCCCCCCC",
                "CDI  J  IDC",
                " DI  J  ID ",
                " D       D ",
                " D       D ",
                " DI  J  ID ",
                " D       D ",
                " D       D ",
                " DI  J  ID ",
                "CDI  J  IDC",
                "CCCCCCCCCCC"
            }, {
                "CBBCEEECBBC",
                "CCCCCCCCCCC",
                " D       D ",
                " DI  J  ID ",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                " DI  J  ID ",
                " D       D ",
                "CCCCCCCCCCC",
                "           "
            }, {
                "CBCCECCCCBC",
                " DI  J  ID ",
                " D       D ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " D       D ",
                " DI  J  ID ",
                "CCCCCCCCCCC"
            }, {
                "CBCCEEECCBC",
                " DI  J  ID ",
                " DI  J  ID ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " DI  J  ID ",
                " DI  J  ID ",
                "CCCCCCCCCCC"
            }, {
                "CBCCECCCCBC",
                " DI  J  ID ",
                " D       D ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " D       D ",
                " DI  J  ID ",
                "CCCCCCCCCCC"
            }, {
                "CBBCEEECBBC",
                "CCCCCCCCCCC",
                " D       D ",
                " DI  J  ID ",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                " DI  J  ID ",
                " D       D ",
                "CCCCCCCCCCC",
                "           "
            }, {
                "CCCCCCCCCCC",
                "CDI  J  IDC",
                " DI  J  ID ",
                " D       D ",
                " D       D ",
                " DI  J  ID ",
                " D       D ",
                " D       D ",
                " DI  J  ID ",
                "CDI  J  IDC",
                "CCCCCCCCCCC"
            }, {
                "           ",
                "CBBBBBBBBBC",
                "CDI  J  IDC",
                "CCCCCCCCCCC",
                " DI  J  ID ",
                " DI  J  ID ",
                " DI  J  ID ",
                "CCCCCCCCCCC",
                "CDI  J  IDC",
                "CBBBBBBBBBC",
                "           "
            }, {
                "           ",
                "           ",
                "CCCCCCCCCCC",
                "CDDDDDDDDDC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CDDDDDDDDDC",
                "CCCCCCCCCCC",
                "           ",
                "           "
            }}
        )
        //</editor-fold>

        //<editor-fold desc="weak module">
        .addShape(
            LHCModules.Weak.structurePiece,
            new String[][]{{
                "           ",
                "           ",
                "CCCCCCCCCCC",
                "CDDDDDDDDDC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CDDDDDDDDDC",
                "CCCCCCCCCCC",
                "           ",
                "           "
            }, {
                "           ",
                "CKKKKKKKKKC",
                "CD       DC",
                "CCCCCCCCCCC",
                " D       D ",
                " D       D ",
                " D       D ",
                "CCCCCCCCCCC",
                "CD       DC",
                "CKKKKKKKKKC",
                "           "
            }, {
                "CCCCCCCCCCC",
                "CD       DC",
                " D       D ",
                " D       D ",
                " DKKKKKKKD ",
                " DKKKKKKKD ",
                " DKKKKKKKD ",
                " D       D ",
                " D       D ",
                "CD       DC",
                "CCCCCCCCCCC"
            }, {
                "CKCECCCECKC",
                "CCC     CCC",
                " D       D ",
                " DKKKKKKKD ",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                " DKKKKKKKD ",
                " D       D ",
                "CCCCCCCCCCC",
                "           "
            }, {
                "CKCECECECKC",
                " D       D ",
                " DKKKKKKKD ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " DKKKKKKKD ",
                " D       D ",
                "CCCCCCCCCCC"
            }, {
                "CKCECECECKC",
                " D       D ",
                " DKKKKKKKD ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " DKKKKKKKD ",
                " D       D ",
                "CCCCCCCCCCC"
            }, {
                "CKCECECECKC",
                " D       D ",
                " DKKKKKKKD ",
                "CCCCCCCCCCC",
                "           ",
                "           ",
                "           ",
                "CCCCCCCCCCC",
                " DKKKKKKKD ",
                " D       D ",
                "CCCCCCCCCCC"
            }, {
                "CKCCECECCKC",
                "CCC     CCC",
                " D       D ",
                " DKKKKKKKD ",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                "CCCCCCCCCCC",
                " DKKKKKKKD ",
                " D       D ",
                "CCCCCCCCCCC",
                "           "
            }, {
                "CCCCCCCCCCC",
                "CD       DC",
                " D       D ",
                " D       D ",
                " DKKKKKKKD ",
                " DKKKKKKKD ",
                " DKKKKKKKD ",
                " D       D ",
                " D       D ",
                "CD       DC",
                "CCCCCCCCCCC"
            }, {
                "           ",
                "CCKKKKKKKCC",
                "CD       DC",
                "CCCCCCCCCCC",
                " D       D ",
                " D       D ",
                " D       D ",
                "CCCCCCCCCCC",
                "CD       DC",
                "CCKKKKKKKCC",
                "           "
            }, {
                "           ",
                "           ",
                "CCCCCCCCCCC",
                "CCDDDCDDDCC",
                "CKCCCCCCCKC",
                "CKC2C C2CKC",
                "CKCCCCCCCKC",
                "CCDDDCDDDCC",
                "CCCCCCCCCCC",
                "           ",
                "           "
            }}
        )
        //</editor-fold>

        //<editor-fold desc="strong module">
        .addShape(
            LHCModules.Strong.structurePiece,
            new String[][]{{
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
                "  C CCC C  "
            }, {
                "  CIIIIIC  ",
                " IDCDDDCDC ",
                "CDDDDDDDDDC",
                "DCDDCCCDDCC",
                "CDDC   CDDI",
                "CDDC   CDDI",
                "CDDC   CDDI",
                "DCDDCCCDDCC",
                "CDDDDDDDDDC",
                " IDCDDDCDC ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " ILCLLLCLD ",
                "CLLLLLLLLLC",
                "DCLLCCCLLCD",
                "CLLC   CLLC",
                "CLLC   CLLC",
                "CLLC   CLLC",
                "DCLLCCCLLCD",
                "CLLLLLLLLLC",
                " ILCLLLCLD ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " IMMMMMMMD ",
                "CMMMMMMMMMC",
                "DCMMCCCMMCD",
                "CMMC   CMMC",
                "CMMC   CMM3",
                "CMMC   CMMC",
                "DCMMCCCMMCD",
                "CMMMMMMMMMC",
                " IMCMMMCMD ",
                "  C CCC C  "
            }, {
                "  CEEECEC  ",
                " IMMMMMMMD ",
                "CMMMMMMMMMC",
                "DCMMCCCMMCD",
                "CMMC   CMMC",
                "CMMC   CMMC",
                "CMMC   CMMC",
                "DCMMCCCMMCD",
                "CMMMMMMMMMC",
                " IMCMMMCMD ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " IMMMMMMMD ",
                "CMMMMMMMMMC",
                "DCMMCCCMMCC",
                "CMMC   CMMC",
                "CMMC   CMM ",
                "CMMC   CMMC",
                "DCMMCCCMMCC",
                "CMMMMMMMMMC",
                " IMCMMMCMD ",
                "  C CCC C  "
            }, {
                "  CECEEEC  ",
                " IMMMMMMMD ",
                "CMMMMMMMMMC",
                "DCMMCCCMMCD",
                "CMMC   CMMC",
                "CMMC   CMMC",
                "CMMC   CMMC",
                "DCMMCCCMMCD",
                "CMMMMMMMMMC",
                " IMCMMMCMD ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " IMMMMMMMD ",
                "CMMMMMMMMMC",
                "DCMMCCCMMCD",
                "CMMC   CMMC",
                "CMMC   CMM3",
                "CMMC   CMMC",
                "DCMMCCCMMCD",
                "CMMMMMMMMMC",
                " IMCMMMCMD ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " INCNNNCND ",
                "CNNNNNNNNNC",
                "DCNNCCCNNCD",
                "CNNC   CNNC",
                "CNNC   CNNC",
                "CNNC   CNNC",
                "DCNNCCCNNCD",
                "CNNNNNNNNNC",
                " INCNNNCND ",
                "  C CCC C  "
            }, {
                "  CIIIIIC  ",
                " IDCDDDCDC ",
                "CDDDDDDDDDC",
                "DCDDCCCDDCC",
                "CDDC   CDDI",
                "CDDC   CDDI",
                "CDDC   CDDI",
                "DCDDCCCDDCC",
                "CDDDDDDDDDC",
                " IDCDDDCDC ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
                "  C CCC C  "
            }}
        )
        //</editor-fold>

        //<editor-fold desc="gravity module">
        .addShape(
            LHCModules.Grav.structurePiece,
            new String[][]{{
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
                "  C CCC C  "
            }, {
                "  COPOPOC  ",
                " CDCDDDCDO ",
                "CDDDDDDDDDC",
                "CCDDCCCDDCD",
                "ODDC   CDDC",
                "PDDC   CDDC",
                "ODDC   CDDC",
                "CCDDCCCDDCD",
                "CDDDDDDDDDC",
                " CDCDDDCDO ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " OPCPPPCPP ",
                "CPOOOOOOOPC",
                "DCOPCCCPOCD",
                "CPOC   COPC",
                "CPOC   COPC",
                "CPOC   COPC",
                "DCOPCCCPOCD",
                "CPOOOOOOOPC",
                " OPCPPPCPP ",
                "  C CCC C  "
            }, {
                "  CEEECEC  ",
                " P       P ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "4  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " P C   C P ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " P       P ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " P C   C P ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " O       O ",
                "C         C",
                "CC  CCC  CD",
                "C  C   C  C",
                "   C   C  C",
                "C  C   C  C",
                "CC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CECCCEC  ",
                " P       P ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " P C   C P ",
                "  C CCC C  "
            }, {
                "  CEEEEEC  ",
                " P       P ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "4  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " P C   C P ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " OOCOOOCOP ",
                "COPPPPPPPOC",
                "DCPOCCCOPCD",
                "COPC   CPOC",
                "COPC   CPOC",
                "COPC   CPOC",
                "DCPOCCCOPCD",
                "COPPPPPPPOC",
                " OOCOOOCOP ",
                "  C CCC C  "
            }, {
                "  COPOPOC  ",
                " CDCDDDCDO ",
                "CDDDDDDDDDC",
                "CCDDCCCDDCD",
                "ODDC   CDDC",
                "PDDC   CDDC",
                "ODDC   CDDC",
                "CCDDCCCDDCD",
                "CDDDDDDDDDC",
                " CDCDDDCDO ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
                "  C CCC C  "
            }}
        )
        //</editor-fold>

        //spotless:on
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings13, 10)) // collider casing
        .addElement('A', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
        .addElement('D', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
        .addElement('E', lazy(t -> { // neonite saffron mango or whatever
            if (Mods.Chisel.isModLoaded()) {
                Block neonite = GameRegistry.findBlock(Mods.Chisel.ID, "neonite");
                return ofBlockAnyMeta(neonite, 1);
            } else {
                return ofBlockAnyMeta(Blocks.air);
            }
        }))
        .addElement('F', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchInputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(2)
            .adder(MTELargeHadronCollider::addBeamLineInputHatch).build()) // beamline input hatch
        .addElement('1', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(3)
            .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchEM).build()) // EM beam output hatch
        .addElement('2', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(4)
            .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchWeak).build()) // Weak beam output hatch
        .addElement('3', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(5)
            .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchStrong).build()) // Strong beam output hatch
        .addElement('4', buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
            .casingIndex(CASING_INDEX_CENTRE).dot(6)
            .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchGrav).build()) // Grav beam output hatch

        .addElement('B', ofBlock(GregTechAPI.sBlockMetal6, 5)) // block of samarium
        .addElement(
            'I',
            GTStructureChannels.HEATING_COIL.use(
                activeCoils(
                    ofCoil(
                        MTELargeHadronCollider::setCoilLevel,
                        MTELargeHadronCollider::getCoilLevel)))
        )
        .addElement(
            'J',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTELargeHadronCollider::setSolenoidLevel,
                    MTELargeHadronCollider::getSolenoidLevel))
        )
        .addElement('K', ofBlock(GregTechAPI.sBlockMetal3, 6)) // block of gallium
        .addElement('L', ofBlock(GregTechAPI.sBlockMetal5, 2)) // block of neutronium
        .addElement('M', ofBlock(GregTechAPI.sBlockCasings13, 10)) // block of infinity // todo
        .addElement('N', ofBlock(GregTechAPI.sBlockCasings13, 10)) // block of cosmic neutronium // todo
        .addElement('O', ofBlock(GregTechAPI.sBlockMetal9, 4)) // block of transcendent metal
        .addElement('P', ofBlock(GregTechAPI.sBlockMetal9, 3)) // block of spacetime
        .build();

    public MTELargeHadronCollider(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeHadronCollider(String aName) {
        super(aName);
    }
    private void setCoilLevel(HeatingCoilLevel level) {
        mCoilLevel = level;
    }
    private HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    private Byte getSolenoidLevel() {
        return mSolenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        mSolenoidLevel = level;
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

    private boolean addAdvancedBeamlineOutputHatchEM(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            MTEHatchAdvancedOutputBeamline hatch = (MTEHatchAdvancedOutputBeamline) aMetaTileEntity;
            hatch.setInitialParticleList(LHCModules.EM.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }
    private boolean addAdvancedBeamlineOutputHatchWeak(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            MTEHatchAdvancedOutputBeamline hatch = (MTEHatchAdvancedOutputBeamline) aMetaTileEntity;
            hatch.setInitialParticleList(LHCModules.Weak.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }    private boolean addAdvancedBeamlineOutputHatchStrong(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            MTEHatchAdvancedOutputBeamline hatch = (MTEHatchAdvancedOutputBeamline) aMetaTileEntity;
            hatch.setInitialParticleList(LHCModules.Strong.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }    private boolean addAdvancedBeamlineOutputHatchGrav(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            MTEHatchAdvancedOutputBeamline hatch = (MTEHatchAdvancedOutputBeamline) aMetaTileEntity;
            hatch.setInitialParticleList(LHCModules.Grav.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
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
                rTexture = new ITexture[]{
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
                        .build()};
            } else {
                rTexture = new ITexture[]{
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
                        .build()};
            }
        } else {
            rTexture = new ITexture[]{Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10))};
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
        buildPiece(LHCModules.EM.structurePiece, stackSize, hintsOnly, 5, -1, -113);
        if (stackSize.stackSize > 1) {
            buildPiece(LHCModules.Weak.structurePiece, stackSize, hintsOnly, 5, -1, -9);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(LHCModules.Strong.structurePiece, stackSize, hintsOnly, 57, -1, -61);
        }
        if (stackSize.stackSize > 3) {
            buildPiece(LHCModules.Grav.structurePiece, stackSize, hintsOnly, -47, -1, -61);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;

        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);

        built += survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 54, 4, 1, realBudget, env, false, true);
        built += survivalBuildPiece(LHCModules.EM.structurePiece, stackSize, 5, -1, -113, realBudget, env, false, true);
        if (stackSize.stackSize > 1) {
            built += survivalBuildPiece(
                LHCModules.Weak.structurePiece,
                stackSize,
                5,
                -1,
                -9,
                realBudget,
                env,
                false,
                true);
        }
        if (stackSize.stackSize > 2) {
            built += survivalBuildPiece(
                LHCModules.Strong.structurePiece,
                stackSize,
                57,
                -1,
                -61,
                realBudget,
                env,
                false,
                true);
        }
        if (stackSize.stackSize > 3) {
            built += survivalBuildPiece(
                LHCModules.Grav.structurePiece,
                stackSize,
                -47,
                -1,
                -61,
                realBudget,
                env,
                false,
                true);
        }
        return built;
    }

    public boolean emEnabled;
    public boolean weakEnabled;
    public boolean strongEnabled;
    public boolean gravEnabled;


    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        mInputBeamline.clear();
        mOutputBeamline.clear();

        emEnabled = checkPiece(LHCModules.EM.structurePiece,5, -1, -113);
        weakEnabled = checkPiece(LHCModules.Weak.structurePiece,5, -1, -9);
        strongEnabled = checkPiece(LHCModules.Strong.structurePiece,57, -1, -61);
        gravEnabled = checkPiece(LHCModules.Grav.structurePiece,-47, -1, -61);

        return checkPiece(STRUCTURE_PIECE_MAIN, 54, 4, 1);

    }


    @Override
    public boolean doRandomMaintenanceDamage() {
        // Cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;

        float inputEnergy = inputInfo.getEnergy();
        Particle inputParticle = Particle.getParticleFromId(inputInfo.getParticleId());
        int inputRate = inputInfo.getRate();

        if (inputEnergy == 0) return CheckRecipeResultRegistry.NO_RECIPE;
        float inputFocus = inputInfo.getFocus();

        if (!inputParticle.canAccelerate()) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.mEfficiency = 10000 ;
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = TickTime.SECOND;

        //todo: // energy this.lEUt = -GTValues.VP[GTUtility.getTier(this.getAverageInputVoltage())] * this.getMaxInputAmps();

        //todo: same^ // long voltage = this.getMaxInputEu();
        //todo: same^ // float voltageFactor = getVoltageFactor(voltage);
        //todo: same^ // this.outputEnergy = (float) calculateOutputParticleEnergy(voltage, inputEnergy, this.antennaeTier);


        // Generate output particle:

        // 1. Determine output energy (= collision energy)
        this.outputEnergy = (float) (inputEnergy)/2; // output energy = collision energy = input energy * 0.5

        // 2. Use collision energy to generate particle ID
        this.outputParticleID = GenerateOutputParticleID(this.outputEnergy);

        // 3. Use input rate and output particle rest mass to determine output rate
        Particle outputParticle = Particle.getParticleFromId(this.outputParticleID);
        float outputMass = outputParticle.getMass();
        this.outputRate = (int) max(0,(1 - (outputMass/this.outputEnergy))*(inputRate));

        // 4. Focus is unused
        this.outputFocus = (int) (inputFocus);

        if (this.outputRate == 0) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.low_input_eut"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        outputPacketAfterRecipe();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int GenerateOutputParticleID(float collisionEnergy){

        // restMass is in MeV
        // beamline energies are in keV
        // :(

        double collisionEnergyMeV = collisionEnergy/1000;

        int n = Particle.VALUES.length;

        double[] weights = new double[n];
        double totalWeight = 0.0;

        for (int i=0; i < n; i++) {
            Particle p = Particle.getParticleFromId(i);
            double thresholdMeV = max(p.getMass(),0.5); // massless particles have a threshold of 0.5 (arbitrary).
                                                        // massive particles have a threshold equal to their rest mass.
            double w = (collisionEnergyMeV < thresholdMeV) ? 0.0
                : p.getLHCWeight();

            if (w < 0 || Double.isNaN(w) || Double.isInfinite(w)) w = 0.0;

            weights[i] = w;
            totalWeight += w;
        }
        if (totalWeight <= 0.0) {
            return 0; // default to photons
        }

        double[] cumulative = new double[n];
        double run = 0.0;
        for (int i = 0; i < n; i++) {
            run += weights[i];
            cumulative[i] = run;
        }
        double r = Math.random() * totalWeight;
        int idx = java.util.Arrays.binarySearch(cumulative, r);
        if (idx < 0) idx = -idx - 1;            // insertion point
        if (idx >= n) idx = n - 1;              // safety clamp

        return idx;

    }

    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }
    private void outputPacketAfterRecipe() {
        if (!this.mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(this.outputEnergy, this.outputRate, this.outputParticleID, this.outputFocus));
            for (MTEHatchAdvancedOutputBeamline o : this.mOutputBeamline) {
                if (!o.acceptedInputs.contains(Particle.getParticleFromId(this.outputParticleID)) || o.getBlacklist().contains(this.outputParticleID)) {
                    continue;
                }
                o.dataPacket = packet;

            }
        }
    }

}
