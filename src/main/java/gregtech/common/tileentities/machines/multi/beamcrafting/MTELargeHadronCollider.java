package gregtech.common.tileentities.machines.multi.beamcrafting;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_ACCELERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_ACCELERATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_COLLIDER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_COLLIDER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static java.lang.Math.max;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
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
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.gui.modularui.multiblock.MTELargeHadronColliderGui;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;

public class MTELargeHadronCollider extends MTEExtendedPowerMultiBlockBase<MTELargeHadronCollider>
    implements ISurvivalConstructable {

    private static final int MACHINEMODE_ACCELERATOR = 0;
    private static final int MACHINEMODE_COLLIDER = 1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_EM = "EM";
    private static final String STRUCTURE_PIECE_WEAK = "Weak";
    private static final String STRUCTURE_PIECE_STRONG = "Strong";
    private static final String STRUCTURE_PIECE_GRAV = "Grav";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private ArrayList<MTEHatchAdvancedOutputBeamline> mOutputBeamline = new ArrayList<>();
    private float outputEnergy;
    private int outputRate;
    private int outputParticleID;
    private float outputFocus;
    public double playerTargetBeamEnergyeV = 1_000_000_000;
    public int playerTargetAccelerationCycles = 10;

    BeamInformation initialParticleInfo = null;
    BeamInformation cachedOutputParticle = null;
    public int accelerationCycleCounter = 0;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("machineMode", machineMode);
        if (cachedOutputParticle != null) {
            aNBT.setFloat("energy", cachedOutputParticle.getEnergy());
            aNBT.setInteger("rate", cachedOutputParticle.getRate());
            aNBT.setInteger("particleId", cachedOutputParticle.getParticleId());
            aNBT.setFloat("focus", cachedOutputParticle.getFocus());
        }
        if (initialParticleInfo != null) {
            aNBT.setFloat("iniParticleEnergy", initialParticleInfo.getEnergy());
            aNBT.setInteger("iniParticleRate", initialParticleInfo.getRate());
            aNBT.setInteger("iniParticleId", initialParticleInfo.getParticleId());
            aNBT.setFloat("iniParticleFocus", initialParticleInfo.getFocus());
        }
        aNBT.setDouble("playerBeamEnergy", playerTargetBeamEnergyeV);
        aNBT.setInteger("playerAccelCycles", playerTargetAccelerationCycles);
        aNBT.setInteger("accelerationCycleCounter", accelerationCycleCounter);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getInteger("machineMode");
        if (aNBT.hasKey("energy") && aNBT.hasKey("rate") && aNBT.hasKey("particleId") && aNBT.hasKey("focus")) {
            cachedOutputParticle = new BeamInformation(
                aNBT.getFloat("energy"),
                aNBT.getInteger("rate"),
                aNBT.getInteger("particleId"),
                aNBT.getFloat("focus"));
        }
        if (aNBT.hasKey("iniParticleEnergy") && aNBT.hasKey("iniParticleRate")
            && aNBT.hasKey("iniParticleId")
            && aNBT.hasKey("iniParticleFocus")) {
            initialParticleInfo = new BeamInformation(
                aNBT.getFloat("iniParticleEnergy"),
                aNBT.getInteger("iniParticleRate"),
                aNBT.getInteger("iniParticleId"),
                aNBT.getFloat("iniParticleFocus"));
        }
        playerTargetBeamEnergyeV = aNBT.getDouble("playerBeamEnergy");
        playerTargetAccelerationCycles = aNBT.getInteger("playerAccelCycles");
        accelerationCycleCounter = aNBT.getInteger("accelerationCycleCounter");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.MULTI_LHC.mode." + machineMode);
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_ACCELERATOR) return MACHINEMODE_COLLIDER;
        else return MACHINEMODE_ACCELERATOR;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    private static final IStructureDefinition<MTELargeHadronCollider> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeHadronCollider>builder()

        // <editor-fold desc="main structure">
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
                "                                         C     CCC      CC CCC     C    D                                    ",
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
                "                                C   C                                   C  C                                 ",
                "                               CC   C                                   C  C                                 ",
                "                              C     C                                   C  C                                 ",
                "                              C    C                                     CC                                  ",
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
                "                              CC    C                                   C   C                                ",
                "                             C      C                                   C   C                                ",
                "                             C     C                                     CCC                                 ",
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
                "                              C    C                                     C  C                                ",
                "                             C     C                                     C  C                                ",
                "                            C     C                                       CC                                 ",
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
                "                               C   C                                     C  C                                ",
                "                              C    C                                     C  C                                ",
                "                             C     C                                     C  C                                ",
                "                           CC    CC                                       CC                                 ",
                "                           C    CC                                                                           ",
                "                           C    CCCCCCCCCC                         CCCCCCCCC                                 ",
                "                           C    C         CCCCC               CCCCC         CC                               ",
                "                            CCCCC         CCCCC               CCCCC         CC                               ",
                "                               CC         CCCCC               CCCCC         CC                               ",
                "                                 CCCCCCCCC                         CCCCCCCCC                                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                CCC                                       CC                                 ",
                "                               C   C                                     C  C                                ",
                "                              CC   C                                     C  C                                ",
                "                             C C   C                                     C  C                                ",
                "                           CC  CCCC                                       CC                                 ",
                "                          C    C                                                                             ",
                "                          C     CCCCCCC                               CCCCCCCC                               ",
                "                          C            CCC                         CCC        CC                             ",
                "                           CC          CCC                         CCC        CC                             ",
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
                "                         C    CC                                                                             ",
                "                         C     CCCCC                                     CCCCCCC                             ",
                "                         C          CCC                               CCC       CC                           ",
                "                          C         CCC                               CCC       CC                           ",
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
                "                         C   CCC                                                                             ",
                "                        CC    CCCC                                         CCCCCCC                           ",
                "                        CC        CC                                     CC       CC                         ",
                "                        CC        CC                                     CC       CC                         ",
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
                "                        CCC  C                                                                               ",
                "                       CC    CCC                                             CCCCCCC                         ",
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
                "                       CC   CC                                                 CCCCCC                        ",
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
                "                      CCC  C                                                     CCCCCC                      ",
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
                "                    CCCCC                                                         CCC  CCC                   ",
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
                "                                CC                                         CC   CC  CCC                      ",
                "                                                                                CC     CC                    ",
                "                   CCCCC                                                          C     CCC                  ",
                "                 CC     C                                                          C      CC                 ",
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
                "                C     CC                                                           CC       C                ",
                "                C     CC                                                             C      C                ",
                "                C     CC                                                             CC     C                ",
                "                 CCCCC                                                                 CCCCC                 "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CCCC                               ",
                "                                C  C                                     C    CC                             ",
                "                                C  C                                     C      CCC                          ",
                "                                C  C                                     C         CC                        ",
                "                                 CC                                       CCC        CC                      ",
                "                                                                             CCC       CCC                   ",
                "                CCCCC                                                          CCCC    CCCCCC                ",
                "               C     C                                                             CC  C     C               ",
                "               C     C                                                               CCC     C               ",
                "               C     C                                                                 C     C               ",
                "                CCCCC                                                                   CCCCC                "
            },{
                "                                                                                                             ",
                "                                                                                                             ",
                "                                 CC                                       CCCC                               ",
                "                                C  C                                     C    CC                             ",
                "                                C  C                                     C      CC                           ",
                "                                C  C                                     C       CC                          ",
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
                "                                C  C                                     C    C                              ",
                "                                C  C                                     C     CC                            ",
                "                                C  C                                     C     CCC                           ",
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

        //</editor-fold>

        //<editor-fold desc="electromagnetism module">
        .addShape(
            STRUCTURE_PIECE_EM,
            new String[][]{{
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "CCCDDDCDDDCCC",
                "CCBCCCCCCCBCC",
                "C1BCCCCCCCB1C",
                "CCBCCCCCCCBCC",
                "CCCDDDCDDDCCC",
                "CCCCCCCCCCCCC",
                "             ",
                "             "
            }, {
                "             ",
                "CCCBBBBBBBCCC",
                "CCDB  B  BDCC",
                "CCCCCCCCCCCCC",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC",
                "CCDB  B  BDCC",
                "CCCBBBBBBBCCC",
                "             "
            }, {
                "CCCCCCCCCCCCC",
                "CCDB  B  BDCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "  D       D  ",
                "  DB  B  BD  ",
                "  D       D  ",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCDB  B  BDCC",
                "CCCCCCCCCCCCC"
            }, {
                "CCBBCEEECBBCC",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             "
            }, {
                "CCBCCECCCCBCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCBCCEEECCBCC",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCBCCECCCCBCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCBBCEEECBBCC",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             "
            }, {
                "CCCCCCCCCCCCC",
                "CCDB  B  BDCC",
                "  DB  B  BD  ",
                "  D       D  ",
                "  D       D  ",
                "  DB  B  BD  ",
                "  D       D  ",
                "  D       D  ",
                "  DB  B  BD  ",
                "CCDB  B  BDCC",
                "CCCCCCCCCCCCC"
            }, {
                "             ",
                "CCBBBBBBBBBCC",
                "CCDB  B  BDCC",
                "CCCCCCCCCCCCC",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "  DB  B  BD  ",
                "CCCCCCCCCCCCC",
                "CCDB  B  BDCC",
                "CCBBBBBBBBBCC",
                "             "
            }, {
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "CCDDDDDDDDDCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCDDDDDDDDDCC",
                "CCCCCCCCCCCCC",
                "             ",
                "             "
            }}
        )
        //</editor-fold>

        //<editor-fold desc="weak module">
        .addShape(
            STRUCTURE_PIECE_WEAK,
            new String[][]{{
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "CCDDDDDDDDDCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCDDDDDDDDDCC",
                "CCCCCCCCCCCCC",
                "             ",
                "             "
            }, {
                "             ",
                "CCKKKKKKKKKCC",
                "CCD       DCC",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  D       D  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "CCD       DCC",
                "CCKKKKKKKKKCC",
                "             "
            }, {
                "CCCCCCCCCCCCC",
                "CCD       DCC",
                "  D       D  ",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "  DKKKKKKKD  ",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "  D       D  ",
                "CCD       DCC",
                "CCCCCCCCCCCCC"
            }, {
                "CCKCECCCECKCC",
                "CCCC     CCCC",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             "
            }, {
                "CCKCECECECKCC",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCKCECECECKCC",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCKCECECECKCC",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "CCCCCCCCCCCCC",
                "             ",
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC"
            }, {
                "CCKCCECECCKCC",
                "CCCC     CCCC",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "CCCCCCCCCCCCC",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "             "
            }, {
                "CCCCCCCCCCCCC",
                "CCD       DCC",
                "  D       D  ",
                "  D       D  ",
                "  DKKKKKKKD  ",
                "  DKKKKKKKD  ",
                "  DKKKKKKKD  ",
                "  D       D  ",
                "  D       D  ",
                "CCD       DCC",
                "CCCCCCCCCCCCC"
            }, {
                "             ",
                "CCCKKKKKKKCCC",
                "CCD       DCC",
                "CCCCCCCCCCCCC",
                "  D       D  ",
                "  D       D  ",
                "  D       D  ",
                "CCCCCCCCCCCCC",
                "CCD       DCC",
                "CCCKKKKKKKCCC",
                "             "
            }, {
                "             ",
                "             ",
                "CCCCCCCCCCCCC",
                "CCCDDDCDDDCCC",
                "CCKCCCCCCCKCC",
                "C2KCCCCCCCK2C",
                "CCKCCCCCCCKCC",
                "CCCDDDCDDDCCC",
                "CCCCCCCCCCCCC",
                "             ",
                "             "
            }}
        )
        //</editor-fold>

        //<editor-fold desc="strong module">
        .addShape(
            STRUCTURE_PIECE_STRONG,
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
            },{
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "C  C   C  3",
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
                " IICIIICID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " IIIIIIIID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CEEECEC  ",
                " IIIIIIIID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " IIIIIIIID ",
                "CIIIIIIIIIC",
                "DCIICCCIICC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICC",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CECEEEC  ",
                " IIIIIIIID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " IIIIIIIID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " IICIIICID ",
                "CIIIIIIIIIC",
                "DCIICCCIICD",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "CIIC   CIIC",
                "DCIICCCIICD",
                "CIIIIIIIIIC",
                " IICIIICID ",
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
                "C  C   C  3",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
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
            STRUCTURE_PIECE_GRAV,
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
            },{
                "  CCCCCCC  ",
                " CCC   CCC ",
                "CC       CC",
                "CC  CCC  CC",
                "C  C   C  C",
                "4  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
                "  C CCC C  "
            }, {
                "  COOOOOC  ",
                " CDCDDDCDO ",
                "CDDDDDDDDDC",
                "CCDDCCCDDCD",
                "ODDC   CDDC",
                "ODDC   CDDC",
                "ODDC   CDDC",
                "CCDDCCCDDCD",
                "CDDDDDDDDDC",
                " CDCDDDCDO ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " OOCOOOCOO ",
                "COOOOOOOOOC",
                "DCOOCCCOOCD",
                "COOC   COOC",
                "COOC   COOC",
                "COOC   COOC",
                "DCOOCCCOOCD",
                "COOOOOOOOOC",
                " OOCOOOCOO ",
                "  C CCC C  "
            }, {
                "  CEEECEC  ",
                " O       O ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " O       O ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CECECEC  ",
                " O       O ",
                "C         C",
                "CC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "CC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CECCCEC  ",
                " O       O ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CEEEEEC  ",
                " O       O ",
                "C         C",
                "DC  CCC  CD",
                "C  C   C  C",
                "C  C   C  C",
                "C  C   C  C",
                "DC  CCC  CD",
                "C         C",
                " O C   C O ",
                "  C CCC C  "
            }, {
                "  CCCCCCC  ",
                " OOCOOOCOO ",
                "COOOOOOOOOC",
                "DCOOCCCOOCD",
                "COOC   COOC",
                "COOC   COOC",
                "COOC   COOC",
                "DCOOCCCOOCD",
                "COOOOOOOOOC",
                " OOCOOOCOO ",
                "  C CCC C  "
            }, {
                "  COOOOOC  ",
                " CDCDDDCDO ",
                "CDDDDDDDDDC",
                "CCDDCCCDDCD",
                "ODDC   CDDC",
                "ODDC   CDDC",
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
                "4  C   C  C",
                "C  C   C  C",
                "CC  CCC  CC",
                "CC       CC",
                " CCC   CCC ",
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
        .addElement(
            'C', // collider casing
            buildHatchAdder(MTELargeHadronCollider.class).atLeast(Energy, ExoticEnergy)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(10))
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings13, 10))
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
        .addElement(
            'F',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTELargeHadronCollider::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            '1',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(3)
                .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchEM)
                .build()) // EM beam output hatch
        .addElement(
            '2',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(4)
                .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchWeak)
                .build()) // Weak beam output hatch
        .addElement(
            '3',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(5)
                .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchStrong)
                .build()) // Strong beam output hatch
        .addElement(
            '4',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(6)
                .adder(MTELargeHadronCollider::addAdvancedBeamlineOutputHatchGrav)
                .build()) // Grav beam output hatch

        .addElement('B', ofBlock(GregTechAPI.sBlockCasings13, 11)) // CMS Casing (EM)
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings13, 12)) // ATLAS Casing (Weak)
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings13, 13)) // ALICE Casing (Strong)
        .addElement('O', ofBlock(GregTechAPI.sBlockCasings13, 14)) // LHCB Casing (Grav)
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

    private boolean addAdvancedBeamlineOutputHatchEM(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            hatch.setInitialParticleList(LHCModule.EM.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }

    private boolean addAdvancedBeamlineOutputHatchWeak(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            hatch.setInitialParticleList(LHCModule.Weak.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }

    private boolean addAdvancedBeamlineOutputHatchStrong(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            hatch.setInitialParticleList(LHCModule.Strong.acceptedParticles);
            this.mOutputBeamline.add(hatch);
            return true;
        }
        return false;

    }

    private boolean addAdvancedBeamlineOutputHatchGrav(IGregTechTileEntity te, int casingIndex) {

        if (te == null) return false;
        IMetaTileEntity aMetaTileEntity = te.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchAdvancedOutputBeamline hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(casingIndex);
            hatch.setInitialParticleList(LHCModule.Grav.acceptedParticles);
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
            if ((aActive) && (this.machineMode == 0)) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_ACCELERATOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_ACCELERATOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else if ((aActive) && (this.machineMode == 1)) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_COLLIDER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_COLLIDER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC)
                        .extFacing()
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
        tt.addMachineType("Particle Accelerator, Particle Collider, LHC")
            .addInfo("Accelerates particles to high energies and then collides them to generate new particles")
            .addInfo("Electrically neutral particles are unaffected")
            .addInfo(
                "Set the " + EnumChatFormatting.YELLOW
                    + "target beam energy "
                    + EnumChatFormatting.GRAY
                    + "(up to 2TeV) "
                    + "and the "
                    + EnumChatFormatting.BLUE
                    + "maximum number of cycles "
                    + EnumChatFormatting.GRAY)
            .addInfo("Cycles every second")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.UNDERLINE + "Accelerator mode")
            .addInfo("Accelerates the beam in the smaller LHC ring")
            .addInfo(
                "Accelerator mode increases the " + EnumChatFormatting.GOLD
                    + "Beam Energy "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.WHITE
                    + "10eV"
                    + EnumChatFormatting.GRAY
                    + " for every EU consumed, "
                    + "until the")
            .addInfo(
                EnumChatFormatting.YELLOW + "Target Beam Energy "
                    + EnumChatFormatting.GRAY
                    + "is reached, or the "
                    + EnumChatFormatting.BLUE
                    + "maximum number of cycles "
                    + EnumChatFormatting.GRAY
                    + "is reached")
            .addInfo(
                "If the " + EnumChatFormatting.YELLOW
                    + "Target Beam Energy "
                    + EnumChatFormatting.GRAY
                    + "is reached, multiply the current "
                    + EnumChatFormatting.RED
                    + "Beam Rate "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.WHITE
                    + "1.1")
            .addInfo(
                EnumChatFormatting.GREEN + "Power "
                    + EnumChatFormatting.GRAY
                    + "cost starts at "
                    + EnumChatFormatting.WHITE
                    + "1A "
                    + EnumChatFormatting.GRAY
                    + "of "
                    + EnumChatFormatting.AQUA
                    + "Energy Hatch Voltage "
                    + EnumChatFormatting.GRAY
                    + "per "
                    + EnumChatFormatting.RED
                    + "Beam Rate "
                    + EnumChatFormatting.GRAY
                    + "and increases quadratically")
            .addInfo("with the " + EnumChatFormatting.LIGHT_PURPLE + "number of completed cycles")
            .addInfo(
                EnumChatFormatting.GREEN + "P "
                    + EnumChatFormatting.GRAY
                    + "= "
                    + EnumChatFormatting.AQUA
                    + "V "
                    + EnumChatFormatting.GRAY
                    + "* "
                    + EnumChatFormatting.RED
                    + "R "
                    + EnumChatFormatting.GRAY
                    + "* "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "N"
                    + EnumChatFormatting.GRAY
                    + "^2")
            .addInfo(
                "Automatically switches to Collider mode when the " + EnumChatFormatting.BLUE
                    + "maximum number of cycles "
                    + EnumChatFormatting.GRAY
                    + "is reached")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.UNDERLINE + "Collider mode")
            .addInfo(
                "Splits the beam into " + EnumChatFormatting.WHITE
                    + "two "
                    + EnumChatFormatting.GRAY
                    + "beams that go in opposite directions in the larger LHC ring, which")
            .addInfo("then collide to generate new particles")
            .addInfo(
                "The " + EnumChatFormatting.DARK_AQUA
                    + "Collision Energy "
                    + EnumChatFormatting.GRAY
                    + "is two times the "
                    + EnumChatFormatting.GOLD
                    + "Beam Energy")
            .addInfo(
                "If the " + EnumChatFormatting.DARK_AQUA
                    + "Collision Energy "
                    + EnumChatFormatting.GRAY
                    + "exceeds the rest mass of a particle, that particle will start")
            .addInfo("appearing in the outputs")
            .addInfo("A particle output can only appear in the modules that correspond to the forces that")
            .addInfo("interact with that particle")
            .addInfo(
                "For example, Neutrinos cannot appear in the Strong force module, " + EnumChatFormatting.WHITE
                    + "ALICE"
                    + EnumChatFormatting.GRAY
                    + ", since they do")
            .addInfo("not interact via the strong force")
            .addInfo(
                EnumChatFormatting.WHITE + "Filtered Beamline Output Hatches "
                    + EnumChatFormatting.GRAY
                    + "will allow you to see the list of possible outputs, and")
            .addInfo("filter them")
            .addSeparator()
            .addInfo("There are four LHC modules, marked by the letter on top of the module:")
            .addInfo(
                EnumChatFormatting.AQUA + "E - Electromagnetism "
                    + EnumChatFormatting.GRAY
                    + "- Charged Matter Sensor ("
                    + EnumChatFormatting.AQUA
                    + "CMS"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                EnumChatFormatting.DARK_GREEN + "W - Weak Interaction "
                    + EnumChatFormatting.GRAY
                    + "- Advanced Total Lepton Assimilation Snare ("
                    + EnumChatFormatting.DARK_GREEN
                    + "ATLAS"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                EnumChatFormatting.WHITE + "S - Strong Force "
                    + EnumChatFormatting.GRAY
                    + "- Absolute Lattice Integrated Chromodynamic Encapsulator ("
                    + EnumChatFormatting.WHITE
                    + "ALICE"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                EnumChatFormatting.DARK_PURPLE + "G - Gravity "
                    + EnumChatFormatting.GRAY
                    + "- Localized Horizon Curvature Binder ("
                    + EnumChatFormatting.DARK_PURPLE
                    + "LHCb"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addSeparator()
            .beginStructureBlock(109, 13, 122, false)
            .addController("Front Center")
            .addCasingInfoExactly("Collider Casing", 6034, false)
            .addCasingInfoExactly("Energy Hatch", 1, false)
            .addCasingInfoExactly("Shielded Accelerator Casing", 16, false)
            .addCasingInfoExactly("Shielded Accelerator Glass", 20, false)
            .addCasingInfoExactly("Beamline Input Hatch", 1, false)
            .addCasingInfoExactly("Neonite (Any, optional)", 73, false)
            .addTecTechHatchInfo()
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 54, 4, 1);
        buildPiece(STRUCTURE_PIECE_EM, stackSize, hintsOnly, 5, -1, -113);
        if (stackSize.stackSize > 1) {
            buildPiece(STRUCTURE_PIECE_WEAK, stackSize, hintsOnly, 5, -1, -9);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(STRUCTURE_PIECE_STRONG, stackSize, hintsOnly, 57, -1, -61);
        }
        if (stackSize.stackSize > 3) {
            buildPiece(STRUCTURE_PIECE_GRAV, stackSize, hintsOnly, -47, -1, -61);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;

        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);

        built += survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 54, 4, 1, realBudget, env, false, true);
        built += survivalBuildPiece(STRUCTURE_PIECE_EM, stackSize, 5, -1, -113, realBudget, env, false, true);
        if (stackSize.stackSize > 1) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_WEAK,
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
                STRUCTURE_PIECE_STRONG,
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
                STRUCTURE_PIECE_GRAV,
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

        emEnabled = checkPiece(STRUCTURE_PIECE_EM, 5, -1, -113);
        weakEnabled = checkPiece(STRUCTURE_PIECE_WEAK, 5, -1, -9);
        strongEnabled = checkPiece(STRUCTURE_PIECE_STRONG, 57, -1, -61);
        gravEnabled = checkPiece(STRUCTURE_PIECE_GRAV, -47, -1, -61);

        return checkPiece(STRUCTURE_PIECE_MAIN, 54, 4, 1)
            && ((mExoticEnergyHatches.size() == 1) ^ (mEnergyHatches.size() == 1));

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

    private String clientLocale = "en_US";

    @Override
    public String[] getInfoData() {

        BeamLinePacket dataPacket = new BeamLinePacket(cachedOutputParticle);

        return new String[] {
            translateToLocalFormatted("tt.keyword.Content", this.clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + (dataPacket != null ? dataPacket.getContentString() : 0),
            translateToLocalFormatted("tt.keyword.PacketHistory", this.clientLocale) + ": "
                + EnumChatFormatting.RED
                + (dataPacket != null ? dataPacket.getTraceSize() : 0), };
    }

    public double getCachedBeamEnergy() {
        return cachedOutputParticle != null ? cachedOutputParticle.getEnergy() : 0;
    }

    public int getCachedBeamRate() {
        return cachedOutputParticle != null ? cachedOutputParticle.getRate() : 0;
    }

    public final float MAXIMUM_PARTICLE_ENERGY_keV = 2_000_000_000; // 2TeV max
    public final double keVEURatio = 0.1 / 1000; // 1 EU = 0.1 eV, so 1 EU = 0.1/1000 keV
    public final float rateScaleFactor = 1.1F;

    public BeamInformation accelerateParticle(BeamInformation particle) {

        float inputEnergy = particle.getEnergy();
        int inputRate = particle.getRate();

        float outEnergy = inputEnergy;
        int outRate = inputRate;

        long machineVoltage = getAverageInputVoltage();
        // inputEnergy is in keV, playerTargetBeamEnergyeV is in eV did this so player can type '2G eV' instead of '2M keV'
        if (inputEnergy <= playerTargetBeamEnergyeV / 1000) {
            outEnergy += (float) (Math.pow(accelerationCycleCounter + 1, 2) * this.mMaxProgresstime
                * machineVoltage
                * keVEURatio);
            if (outEnergy >= MAXIMUM_PARTICLE_ENERGY_keV) {
                return new BeamInformation(
                    MAXIMUM_PARTICLE_ENERGY_keV,
                    outRate,
                    particle.getParticleId(),
                    particle.getFocus());
            }

        } else {
            outRate = (int) Math.ceil(outRate * rateScaleFactor);
        }

        return new BeamInformation(outEnergy, outRate, particle.getParticleId(), particle.getFocus());
    }

    public long calculateEnergyCostAccelerator(BeamInformation particle) {
        long machineVoltage = getAverageInputVoltage();
        // counter starts at 0, so +1
        return (long) (machineVoltage * Math.pow(accelerationCycleCounter + 1, 2) * particle.getRate());


    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        initialParticleInfo = null;
        cachedOutputParticle = null;
        accelerationCycleCounter = 0;
        machineMode = MACHINEMODE_ACCELERATOR;
        super.stopMachine(reason);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        int oldMachineMode = machineMode;
        if (aValue == 0){
            machineMode = 0;
        }
        else{
            machineMode = 1;
        }
        if (oldMachineMode != machineMode) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((machineMode == 1) ? 1 : 0);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        BeamInformation inputInfo = this.getInputInformation();

        if (inputInfo == null || inputInfo.getRate() == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        if (machineMode == MACHINEMODE_ACCELERATOR) {

            this.mEfficiency = 10000;
            this.mEfficiencyIncrease = 10000;
            this.mMaxProgresstime = TickTime.SECOND;

            if (cachedOutputParticle == null) {
                // assign cachedOutputParticle, which will be accelerated in consequent processing cycles
                // also assign initialParticleInfo, which inputInfo is compared against every cycle
                cachedOutputParticle = inputInfo.copy();
                initialParticleInfo = inputInfo.copy();
                accelerationCycleCounter = 0;
            } else {
                // if cachedOutputParticle exists, then apply acceleration cycle logic
                if (!cachedOutputParticle.getParticle()
                    .canAccelerate()) {
                    // if the input beam is not charged particles, crash
                    stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                if (!initialParticleInfo.isEqual(inputInfo)) {
                    // if the input beam is ever modified or interrupted, crash the LHC
                    stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inputinterrupt"));
                    return CheckRecipeResultRegistry.NO_RECIPE;
                } else {

                    lEUt = calculateEnergyCostAccelerator(cachedOutputParticle);
                    // todo fix waila
                    if (!drainEnergyInput(20 * lEUt)) { // *20 because CheckRecipeResult is every second
                        stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                        endRecipeProcessing();
                        return CheckRecipeResultRegistry.insufficientPower(lEUt);
                    }

                    if (accelerationCycleCounter < playerTargetAccelerationCycles) {
                        cachedOutputParticle = accelerateParticle(cachedOutputParticle);
                        accelerationCycleCounter += 1;
                    } else {
                        machineMode = MACHINEMODE_COLLIDER;
                    }
                }
            }
        } else {

            float inputEnergy = cachedOutputParticle.getEnergy();
            Particle inputParticle = Particle.getParticleFromId(cachedOutputParticle.getParticleId());
            int inputRate = cachedOutputParticle.getRate();

            if (inputEnergy == 0) return CheckRecipeResultRegistry.NO_RECIPE;
            float inputFocus = cachedOutputParticle.getFocus();

            if (!inputParticle.canAccelerate()) {
                stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            this.mEfficiency = 10000;
            this.mEfficiencyIncrease = 10000;
            this.mMaxProgresstime = TickTime.SECOND;

            // Generate output particle:

            // 1. Determine output energy (= collision energy)
            this.outputEnergy = (inputEnergy) * 2; // output energy = collision energy = input energy * 2

            // 2. Use collision energy to generate particle ID
            this.outputParticleID = GenerateOutputParticleID(this.outputEnergy);

            // 3. Use input rate and output particle rest mass to determine output rate
            Particle outputParticle = Particle.getParticleFromId(this.outputParticleID);
            float outputMass = outputParticle.getMass();
            this.outputRate = (int) max(0, (1 - (outputMass / this.outputEnergy)) * (inputRate));

            // 4. Focus is unused
            this.outputFocus = (int) (inputFocus);

            if (this.outputRate == 0) {
                stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.low_input_eut"));
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            outputPacketAfterRecipe();
            // todo fix waila
            if (!drainEnergyInput(20 * lEUt)) { // *20 because CheckRecipeResult is every second
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                endRecipeProcessing();
                return CheckRecipeResultRegistry.insufficientPower(lEUt);
            }
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int GenerateOutputParticleID(float collisionEnergy) {

        // restMass is in MeV
        // beamline energies are in keV
        // :(

        double collisionEnergyMeV = collisionEnergy / 1000;

        int n = Particle.VALUES.length;

        double[] weights = new double[n];
        double totalWeight = 0.0;

        for (int i = 0; i < n; i++) {
            Particle p = Particle.getParticleFromId(i);
            double thresholdMeV = max(p.getMass(), 0.5); // massless particles have a threshold of 0.5 (arbitrary).
            // massive particles have a threshold equal to their rest mass.
            double w = (collisionEnergyMeV < thresholdMeV) ? 0.0 : p.getLHCWeight();

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
        if (idx < 0) idx = -idx - 1; // insertion point
        if (idx >= n) idx = n - 1; // safety clamp

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
                if (o.acceptedInputMap.getOrDefault(Particle.getParticleFromId(this.outputParticleID), false)
                    == false) {
                    o.dataPacket = null;
                    continue;
                }
                o.dataPacket = packet;

            }
        }
    }

    @Override
    public void onDisableWorking() {
        initialParticleInfo = null;
        cachedOutputParticle = null;
        accelerationCycleCounter = 0;
        machineMode = MACHINEMODE_ACCELERATOR;
        super.onDisableWorking();
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected @NotNull MTELargeHadronColliderGui getGui() {
        return new MTELargeHadronColliderGui(this);
    }

    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_LHC_SPIN_UP;
    }

}
