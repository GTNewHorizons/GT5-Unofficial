package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_ANOMALY;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_NONE;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_PARTICLES;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.MAX_COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.STRUCTURE_TIER;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHeatProducer;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.common.gui.modularui.multiblock.MTEWaferDopingFacilityGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHeatSensor;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEWaferDopingFacility extends MTEExtendedPowerMultiBlockBase<MTEWaferDopingFacility>
    implements ISurvivalConstructable, IHeatProducer {

    private static final String TIER_1 = "tier1";
    private static final String TIER_2 = "tier2";
    private static final String TIER_3 = "tier3";
    private static final String TIER_4 = "tier4";
    private static final int OFFSET_X_1 = 3;
    private static final int OFFSET_Y_1 = 7;
    private static final int OFFSET_Z_1 = 0;
    private static final int OFFSET_X_2 = 11;
    private static final int OFFSET_Y_2 = 10;
    private static final int OFFSET_Z_2 = 0;
    private static final int OFFSET_X_3 = 11;
    private static final int OFFSET_Y_3 = 14;
    private static final int OFFSET_Z_3 = 6;
    private static final int OFFSET_X_4 = 26;
    private static final int OFFSET_Y_4 = 14;
    private static final int OFFSET_Z_4 = 6;
    private static final IStructureDefinition<MTEWaferDopingFacility> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEWaferDopingFacility>builder()
        .addShape(
            TIER_1,
            transpose(
                new String[][] { { "       ", "       ", "   Q   ", "  QQQ  ", "   Q   ", "       ", "       " },
                    { "       ", "   Q   ", "   I   ", " QIHIQ ", "   I   ", "   Q   ", "       " },
                    { "       ", "   I   ", "   Q   ", " IQKQI ", "   Q   ", "   I   ", "       " },
                    { "       ", "   I   ", "       ", " I K I ", "       ", "   I   ", "       " },
                    { "       ", "       ", "       ", "   K   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   K   ", "       ", "       ", "       " },
                    { "  111  ", " 1 F 1 ", "1     1", "1F K F1", "1     1", " 1 F 1 ", "  111  " },
                    { "  1~1  ", " 1TTT1 ", "1TTTTT1", "1TTHTT1", "1TTTTT1", " 1TTT1 ", "  111  " } }))
        .addShape(
            TIER_2,
            transpose(
                new String[][] {
                    { "               ", "               ", "               ", "    MKMKMKM    ", "               ",
                        "               ", "               " },
                    { "               ", "               ", "    MKMKMKM    ", "   MMMMMMMMM   ", "    MKMKMKM    ",
                        "               ", "               " },
                    { "               ", "               ", "   MK     KM   ", "  MMMKMKMKMMM  ", "   MK     KM   ",
                        "               ", "               " },
                    { "               ", "               ", "  CMC     MQM  ", "  MMM     QQQ  ", "  CMC     MQM  ",
                        "               ", "               " },
                    { "               ", "  CCC      Q   ", " C M C    MIM  ", " CMMMC   QIHIQ ", " C M C    MIM  ",
                        "  CCC      Q   ", "               " },
                    { "  CCC          ", " CKKKC     I   ", "CK M KC   MQM  ", "CKMMMKC  IQKQI ", "CK M KC   MQM  ",
                        " CKKKC     I   ", "  CCC          " },
                    { "               ", "  KKK      I   ", " K M K         ", " KMMMK   I K I ", " K M K         ",
                        "  KKK      I   ", "               " },
                    { "  CCC          ", " CIIIC         ", "CI M IC        ", "CIMMMIC    K   ", "CI M IC        ",
                        " CIIIC         ", "  CCC          " },
                    { "               ", "  KKK          ", " K M K         ", " KMMMK     K   ", " K M K         ",
                        "  KKK          ", "               " },
                    { "  CXC     111  ", " CKKKC   1 F 1 ", "CK M KC 1     1", "CKMMMKC 1F K F1", "CK M KC 1     1",
                        " CKKKC   1 F 1 ", "  CCC     111  " },
                    { "  CCC     1~1  ", " CCCCC   1TTT1 ", "CCCCCCC 1TTTTT1", "CCCCCCC 1TTHTT1", "CCCCCCC 1TTTTT1",
                        " CCCCC   1TTT1 ", "  CCC     111  " } }))
        .addShape(
            TIER_3,
            transpose(
                new String[][] {
                    { "                                   ", "                                   ",
                        "                                   ", "                                   ",
                        "                     S             ", "                                   ",
                        "                                   ", "                                   ",
                        "                                   ", "                              S    ",
                        "                                   ", "                                   ",
                        "                                   ", "                                   ",
                        "                     S             ", "                                   ",
                        "                                   ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                                   ", "                    SSS            ",
                        "                    S S            ", "                    SSS            ",
                        "                                   ", "                                   ",
                        "                             SSS   ", "                             S S   ",
                        "                             SSS   ", "                                   ",
                        "                                   ", "                    SSS            ",
                        "                    S S            ", "                    SSS            ",
                        "                                   ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                    SSS            ", "                   S   S           ",
                        "                   S   S           ", "                   S   S           ",
                        "                    SSS            ", "                             SSS   ",
                        "                            S   S  ", "                            S   S  ",
                        "                            S   S  ", "                             SSS   ",
                        "                    SSS            ", "                   S   S           ",
                        "                   S   S           ", "                   S   S           ",
                        "                    SSS            ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   CSSSC           ", "                   S   S           ",
                        "                   S   S           ", "                   S   S           ",
                        "                   CSSSC           ", "                            JSSSJ  ",
                        "                            S   S  ", "                            S   S  ",
                        "                            S   S  ", "                            JSSSJ  ",
                        "                   GSSSG           ", "                   S   S           ",
                        "                   S   S           ", "                   S   S           ",
                        "                   GSSSG           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   PCCCP           ", "                   CC CC           ",
                        "                   C   C           ", "                   CC CC           ",
                        "                   PCCCP           ", "                            RJJJR  ",
                        "                            JJ JJ  ", "    MKMKMKM                 J   J  ",
                        "                            JJ JJ  ", "                            RJJJR  ",
                        "                   OGGGO           ", "                   GG GG           ",
                        "                   G   G           ", "                   GG GG           ",
                        "                   OGGGO           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   P   P           ", "                    VNV            ",
                        "                    N N            ", "                    VNV            ",
                        "                   P   P           ", "                            R   R  ",
                        "    MKMKMKM                  DED   ", "   MMMMMMMMM                 E E   ",
                        "    MKMKMKM                  DED   ", "                            R   R  ",
                        "                   O   O           ", "                    UNU            ",
                        "                    N N            ", "                    UNU            ",
                        "                   O   O           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   PCCCP           ", "                   CVNVC           ",
                        "                   CN NC           ", "                   CVNVC           ",
                        "                   PCCCP           ", "                            RJJJR  ",
                        "   MK     KM                JDEDJ  ", "  MMMKMKMKMMM               JE EJ  ",
                        "   MK     KM                JDEDJ  ", "                            RJJJR  ",
                        "                   OGGGO           ", "                   GUNUG           ",
                        "                   GN NG           ", "                   GUNUG           ",
                        "                   OGGGO           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   P   P           ", "                    VNV            ",
                        "                    N N            ", "                    VNV            ",
                        "                   P C P           ", "                     V      R   R  ",
                        "  CMC     MQM        LL      DED   ", "  MMM     QQQ       LSZLJDJDJE E   ",
                        "  CMC     MQM        LL      DED   ", "                     U      R   R  ",
                        "                   O G O           ", "                    UNU            ",
                        "                    N N            ", "                    UNU            ",
                        "                   O   O           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   PCCCP           ", "                   CVNVC           ",
                        "                   CN NC           ", "                   CVNVC           ",
                        "                   PCNCP           ", "  CCC     NQN       VNV     RJJJR  ",
                        " C M C   NMIMN      LNLLJDJDJDEDJ  ", " CMMMC   QIHIQ      SNEEEEEEEE EJ  ",
                        " C M C   NMIMN      LNLLJDJDJDEDJ  ", "  CCC     NQN       UNU     RJJJR  ",
                        "                   OGNGO           ", "                   GUNUG           ",
                        "                   GN NG           ", "                   GUNUG           ",
                        "                   OGGGO           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   P   P           ", "                    VNV            ",
                        "                    N N            ", "                    VNV            ",
                        "  CCC              P C P           ", " CKKKC   N I N       V      R   R  ",
                        "CK M KC   MQM        LL      DED   ", "CKMMMKC  IQKQI      0NLLJDJDJE E   ",
                        "CK M KC   MQM        LL      DED   ", " CKKKC   N I N       U      R   R  ",
                        "  CCC              O G O           ", "                    UNU            ",
                        "                    N N            ", "                    UNU            ",
                        "                   O   O           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   PCCCP           ", "                   CVNVC           ",
                        "                   CN NC           ", "                   CVNVC           ",
                        "                   PCCCP           ", "  KKK    N I N              RJJJR  ",
                        " K M K               L      JDEDJ  ", " KMMMK   I K I      LNL     JE EJ  ",
                        " K M K               L      JDEDJ  ", "  KKK    N I N              RJJJR  ",
                        "                   OGGGO           ", "                   GUNUG           ",
                        "                   GN NG           ", "                   GUNUG           ",
                        "                   OGGGO           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   P   P           ", "                    VNV            ",
                        "                    N N            ", "                    VNV            ",
                        "  CCC              P   P           ", " CIIIC   N   N              R   R  ",
                        "CI M IC              L       DED   ", "CIMMMIC    K        LNL      E E   ",
                        "CI M IC              L       DED   ", " CIIIC   N   N              R   R  ",
                        "  CCC              O   O           ", "                    UNU            ",
                        "                    N N            ", "                    UNU            ",
                        "                   O   O           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                                   ",
                        "                   PCCCP           ", "                   CVVVC           ",
                        "                   CVVVC           ", "                   CVVVC           ",
                        "                   PCCCP           ", "  KKK    N   N              RJJJR  ",
                        " K M K              S       JDDDJ  ", " KMMMK     K   LSLSLNL      JDDDJ  ",
                        " K M K              S       JDDDJ  ", "  KKK    N   N              RJJJR  ",
                        "                   OGGGO           ", "                   GUUUG           ",
                        "                   GUUUG           ", "                   GUUUG           ",
                        "                   OGGGO           ", "                                   ",
                        "                                   " },
                    { "                                   ", "                   CCCCC           ",
                        "                  CC   CC          ", "                  C     C          ",
                        "                  C     C          ", "                  C     C          ",
                        "  CXC     111     CC   CC   JJJJJ  ", " CKKKC   1 F 1     CCCCC   JJ   JJ ",
                        "CK M KC 1     1LSLSL       J     J ", "CKMMMKC 1F K F1NNNNNS      J     J ",
                        "CK M KC 1     1LSLSL       J     J ", " CKKKC   1 F 1     GGGGG   JJ   JJ ",
                        "  CCC     111     GG   GG   JJJJJ  ", "                  G     G          ",
                        "                  G     G          ", "                  G     G          ",
                        "                  GG   GG          ", "                   GGGGG           ",
                        "                                   " },
                    { "                   CCCCC           ", "                  C     C          ",
                        "                 C       C         ", "                 C       C         ",
                        "                 C       C         ", "                 C       C  JJJJJ  ",
                        "  CCC     1~1    C       C J     J ", " CCCCC   1TTT1    C     C J       J",
                        "CCCCCCC 1TTTTT1    CCCCC  J       J", "CCCCCCC 1TTHTT1LSLSL      J       J",
                        "CCCCCCC 1TTTTT1    GGGGG  J       J", " CCCCC   1TTT1    G     G J       J",
                        "  CCC     111    G       G J     J ", "                 G       G  JJJJJ  ",
                        "                 G       G         ", "                 G       G         ",
                        "                 G       G         ", "                  G     G          ",
                        "                   GGGGG           " } }))
        .addShape(
            TIER_4,
            transpose(
                new String[][] {
                    { "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                    S             ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                             S    ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                    S             ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                   SSS            ",
                        "                                   S S            ",
                        "                                   SSS            ",
                        "                                                  ",
                        "                                                  ",
                        "                                            SSS   ",
                        "                                            S S   ",
                        "                                            SSS   ",
                        "                                                  ",
                        "                                                  ",
                        "                                   SSS            ",
                        "                                   S S            ",
                        "                                   SSS            ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                   SSS            ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                   SSS            ",
                        "                                            SSS   ",
                        "                                           S   S  ",
                        "                                           S   S  ",
                        "                                           S   S  ",
                        "                                            SSS   ",
                        "                                   SSS            ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                   SSS            ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  CSSSC           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  CSSSC           ",
                        "                                           JSSSJ  ",
                        "                                           S   S  ",
                        "                                           S   S  ",
                        "                                           S   S  ",
                        "                                           JSSSJ  ",
                        "                                  GSSSG           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  S   S           ",
                        "                                  GSSSG           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  PCCCP           ",
                        "                                  CC CC           ",
                        "                                  C   C           ",
                        "                                  CC CC           ",
                        "                                  PCCCP           ",
                        "                                           RJJJR  ",
                        "                                           JJ JJ  ",
                        "                   MKMKMKM                 J   J  ",
                        "                                           JJ JJ  ",
                        "                                           RJJJR  ",
                        "                                  OGGGO           ",
                        "                                  GG GG           ",
                        "                                  G   G           ",
                        "                                  GG GG           ",
                        "                                  OGGGO           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  P   P           ",
                        "                                   VNV            ",
                        "                                   N N            ",
                        "                                   VNV            ",
                        "                                  P   P           ",
                        "                                           R   R  ",
                        "                   MKMKMKM                  DED   ",
                        "                  MMMMMMMMM                 E E   ",
                        "                   MKMKMKM                  DED   ",
                        "                                           R   R  ",
                        "                                  O   O           ",
                        "                                   UNU            ",
                        "                                   N N            ",
                        "                                   UNU            ",
                        "                                  O   O           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  PCCCP           ",
                        "                                  CVNVC           ",
                        "                                  CN NC           ",
                        "                                  CVNVC           ",
                        "                                  PCCCP           ",
                        "                                           RJJJR  ",
                        "                  MK     KM                JDEDJ  ",
                        "                 MMMKMKMKMMM               JE EJ  ",
                        "                  MK     KM                JDEDJ  ",
                        "                                           RJJJR  ",
                        "                                  OGGGO           ",
                        "                                  GUNUG           ",
                        "                                  GN NG           ",
                        "                                  GUNUG           ",
                        "                                  OGGGO           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  P   P           ",
                        "                                   VNV            ",
                        "                                   N N            ",
                        "                                   VNV            ",
                        "                                  P C P           ",
                        "                                    V      R   R  ",
                        "                 CMC     MQM        LL      DED   ",
                        "                 MMM     QQQ       LSZLJDJDJE E   ",
                        "                 CMC     MQM        LL      DED   ",
                        "                                    U      R   R  ",
                        "                                  O G O           ",
                        "                                   UNU            ",
                        "                                   N N            ",
                        "                                   UNU            ",
                        "                                  O   O           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  PCCCP           ",
                        "                                  CVNVC           ",
                        "                                  CN NC           ",
                        "                                  CVNVC           ",
                        "                                  PCNCP           ",
                        "                 CCC     NQN       VNV     RJJJR  ",
                        "                C M C   NMIMN      LNLLJDJDJDEDJ  ",
                        "                CMMMC   QIHIQ      SNEEEEEEEE EJ  ",
                        "                C M C   NMIMN      LNLLJDJDJDEDJ  ",
                        "                 CCC     NQN       UNU     RJJJR  ",
                        "                                  OGNGO           ",
                        "                                  GUNUG           ",
                        "                                  GN NG           ",
                        "                                  GUNUG           ",
                        "                                  OGGGO           ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  P   P           ",
                        "                                   VNV            ",
                        "                                   N N            ",
                        "                                   VNV            ",
                        "                 CCC     WWW      P C P           ",
                        "                CKKKC   N I N       V      R   R  ",
                        "               CK M KC W MQM W      LL      DED   ",
                        "               CKMMMKC WIQKQIW     0NLLJDJDJE E   ",
                        "               CK M KC W MQM W      LL      DED   ",
                        "                CKKKC   N I N       U      R   R  ",
                        "                 CCC     BBB      O G O           ",
                        "                                   UNU            ",
                        "                          B        N N            ",
                        "                         RBR       UNU            ",
                        "                         RBR      O   O           ",
                        "            R    R      RBBBR                     ",
                        "            RRRRRR       BWB                      ",
                        "            R    R       BWB                      ",
                        "                         BWB                      ",
                        "                         BWB                      ",
                        "                         BWB                      ",
                        "                         BWB                      ",
                        "                          BB                      ",
                        "                           B                      ",
                        "                           B                      ",
                        "  RRR                    RRR                      ",
                        "   R                      R                       ",
                        "   R                      R                       ",
                        "   R                      R                       ",
                        "   R                      R                       ",
                        "  RRR                    RRR                      ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "            R    R                                ",
                        "            RRRRRR                                ",
                        "            R    R                                ",
                        "                                                  ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  PCCCP           ",
                        "                                  CVNVC           ",
                        "                                  CN NC           ",
                        "                                  CVNVC           ",
                        "                         WWW      PCCCP           ",
                        "                 KKK    N I N              RJJJR  ",
                        "                K M K  W     W      L      JDEDJ  ",
                        "                KMMMK  WI K IW     LNL     JE EJ  ",
                        "                K M K  W     W      L      JDEDJ  ",
                        "                 KKK    N I N              RJJJR  ",
                        "                         BWB      OGGGO           ",
                        "                                  GUNUG           ",
                        "                          W       GN NG           ",
                        "                        RBABR     GUNUG           ",
                        "            R BB R      RBABR     OGGGO           ",
                        "            BBBBBB     RBBABBR                    ",
                        "         BBBBBBBBBBBB   BBABB                     ",
                        "        BBBBBBBBBBBBBB  BBABB                     ",
                        "       BBB  R BB R  BBB BBABB                     ",
                        "      BBB            BBBBBABB                     ",
                        "     BBB              BBB ABB                     ",
                        "    BBB                BBBABB                     ",
                        "   BBB                  BBBBB                     ",
                        "   BB                    BBBB                     ",
                        "   BB                    BBBB                     ",
                        " RBBBR                  RBBBR                     ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        " RBBBR                  RBBBR                     ",
                        "   BB                    BB                       ",
                        "   BB                    BB                       ",
                        "   BBB                  BBB                       ",
                        "    BBB                BBB                        ",
                        "     BBB              BBB                         ",
                        "      BBB            BBB                          ",
                        "       BBB  R    R  BBB                           ",
                        "        BBBBB    BBBBB                            ",
                        "         BBBBBBBBBBBB                             ",
                        "            BBBBBB                                ",
                        "            R    R                                ",
                        "                                                  " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  P   P           ",
                        "                                   VNV            ",
                        "                                   N N            ",
                        "                                   VNV            ",
                        "                 CCC     WWW      P   P           ",
                        "                CIIIC   N   N              R   R  ",
                        "               CI M IC W     W      L       DED   ",
                        "               CIMMMIC W  K  W     LNL      E E   ",
                        "               CI M IC W     W      L       DED   ",
                        "                CIIIC   N   N              R   R  ",
                        "                 CCC     WWW      O   O           ",
                        "                                   UNU            ",
                        "                        BWWWB      N N            ",
                        "            R    R     RBA ABR     UNU            ",
                        "            BBBBBB     RBA ABR    O   O           ",
                        "         BBBA    ABBB  RBA ABR                    ",
                        "        B            B  WA AW                     ",
                        "       B    A    A    B WA AW                     ",
                        "      B   BBBBYBBBBB   BBA AW                     ",
                        "     B   B  R    R  B   BA AW                     ",
                        "    B   B            B     AW                     ",
                        "   B   B              B    AW                     ",
                        "  B   B                B   AW                     ",
                        "  B  B                  B  AW                     ",
                        "  B  B                  B  AW                     ",
                        "RBA ABR                RBA ABR                    ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        "RBA ABR                RBA ABR                    ",
                        "  B  B                  B  B                      ",
                        "  B  B                  B  B                      ",
                        "  B   B                B   B                      ",
                        "   B   B              B   B                       ",
                        "    B   B            B   B                        ",
                        "     B   B  R    R  B   B                         ",
                        "      B   BBB    BBB   B                          ",
                        "       B    ABBBBA    B                           ",
                        "        B            B                            ",
                        "         BBBA    ABBB                             ",
                        "            BBBBBB                                ",
                        "            R    R                                " },
                    { "                                                  ",
                        "                                                  ",
                        "                                  PCCCP           ",
                        "                                  CVVVC           ",
                        "                                  CVVVC           ",
                        "                                  CVVVC           ",
                        "                         WWW      PCCCP           ",
                        "                 KKK    N   N              RJJJR  ",
                        "                K M K  W     W     S       JDDDJ  ",
                        "                KMMMK  W  K  WLSLSLNL      JDDDJ  ",
                        "                K M K  W     W     S       JDDDJ  ",
                        "                 KKK    N   N              RJJJR  ",
                        "                         BWB      OGGGO           ",
                        "                                  GUUUG           ",
                        "                          W       GUUUG           ",
                        "            R    R     R BAB R    GUUUG           ",
                        "            BBBBBB     R BAB R    OGGGO           ",
                        "         BBBA    ABBB  RBBABBR                    ",
                        "        B            B  BBABB                     ",
                        "       B    A    A    B BBABB                     ",
                        "      B   BBBBBBBBBB   BBBABB                     ",
                        "     B   B  R    R  B   BBABB                     ",
                        "    B   B            B    ABB                     ",
                        "   B   B              B   ABB                     ",
                        "  B   B                B   BB                     ",
                        "  B  B                  B  BB                     ",
                        "  B  B                  B  BB                     ",
                        "RBA ABR                RBA ABR                    ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        " B  B                    B  B                     ",
                        "RBA ABR                RBA ABR                    ",
                        "  B  B                  B  B                      ",
                        "  B  B                  B  B                      ",
                        "  B   B                B   B                      ",
                        "   B   B              B   B                       ",
                        "    B   B            B   B                        ",
                        "     B   B  R    R  B   B                         ",
                        "      B   BBB    BBB   B                          ",
                        "       B    ABBBBA    B                           ",
                        "        B            B                            ",
                        "         BBBA    ABBB                             ",
                        "            BBBBBB                                ",
                        "            R    R                                " },
                    { "                                                  ",
                        "                                  CCCCC           ",
                        "                                 CC   CC          ",
                        "                                 C     C          ",
                        "                                 C     C          ",
                        "                                 C     C          ",
                        "                 CXC     111     CC   CC   JJJJJ  ",
                        "                CKKKC   1 F 1     CCCCC   JJ   JJ ",
                        "               CK M KC 1     1LSLSL       J     J ",
                        "               CKMMMKC 1F K F1NNNNNS      J     J ",
                        "               CK M KC 1     1LSLSL       J     J ",
                        "                CKKKC   1 F 1     GGGGG   JJ   JJ ",
                        "                 CCC     111     GG   GG   JJJJJ  ",
                        "                                 G     G          ",
                        "                          B      G     G          ",
                        "            R    R     R  B  R   G     G          ",
                        "              BB       R  B  R   GG   GG          ",
                        "            BBBBBB     R BBB R    GGGGG           ",
                        "         BBBBBBBBBBBB    BWB                      ",
                        "        BBBBBBBBBBBBBB   BWB                      ",
                        "       BBB    BB    BBB  BWB                      ",
                        "      BBB   R    R   BBB BWB                      ",
                        "     BBB              BBBBWB                      ",
                        "    BBB                BBBWB                      ",
                        "   BBB                  BBBB                      ",
                        "   BB                    BBB                      ",
                        "   BB                    BBB                      ",
                        "R BBB R                R BBB R                    ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        "  BB                      BB                      ",
                        "R BBB R                R BBB R                    ",
                        "   BB                    BB                       ",
                        "   BB                    BB                       ",
                        "   BBB                  BBB                       ",
                        "    BBB                BBB                        ",
                        "     BBB              BBB                         ",
                        "      BBB   R    R   BBB                          ",
                        "       BBB          BBB                           ",
                        "        BBBBB    BBBBB                            ",
                        "         BBBBBBBBBBBB                             ",
                        "            BBBBBB                                ",
                        "                                                  ",
                        "            R    R                                " },
                    { "                                  CCCCC           ",
                        "                                 C     C          ",
                        "                                C       C         ",
                        "                                C       C         ",
                        "                                C       C         ",
                        "                                C       C  JJJJJ  ",
                        "                 CCC     1~1    C       C J     J ",
                        "                CCCCC   1TTT1    C     C J       J",
                        "               CCCCCCC 1TTTTT1    CCCCC  J       J",
                        "               CCCCCCC 1TTHTT1LSLSL      J       J",
                        "               CCCCCCC 1TTTTT1    GGGGG  J       J",
                        "                CCCCC   1TTT1    G     G J       J",
                        "                 CCC     111    G       G J     J ",
                        "                                G       G  JJJJJ  ",
                        "                                G       G         ",
                        "            R    R     R     R  G       G         ",
                        "                       R     R  G       G         ",
                        "                       R     R   G     G          ",
                        "                                  GGGGG           ",
                        "                                                  ",
                        "                                                  ",
                        "            R    R                                ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "R     R                R     R                    ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "R     R                R     R                    ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "            R    R                                ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "                                                  ",
                        "            R    R                                " } }))
        .addElement('A', Casings.ElectrodeCasing.asElement())
        .addElement('B', Casings.ShieldedAcceleratorCasing.asElement())
        .addElement('C', Casings.HeatProofMachineCasing.asElement())
        .addElement('D', Casings.SuperconductingCoilBlock.asElement())
        .addElement('E', Casings.FluxedElectrumItemPipeCasing.asElement())
        .addElement('F', Casings.SolidSteelMachineCasing.asElement())
        .addElement('G', Casings.FrostProofMachineCasing.asElement())
        .addElement('H', Casings.SteelGearBoxCasing.asElement())
        .addElement('I', Casings.SteelPipeCasing.asElement())
        .addElement('J', Casings.FusionMachineCasing.asElement())
        .addElement('K', Casings.CupronickelCoilBlock.asElement())
        .addElement('L', Casings.ChemicallyInertMachineCasing.asElement())
        .addElement('M', Casings.PTFEPipeCasing.asElement())
        .addElement('N', Casings.PBIPipeCasing.asElement())
        .addElement('O', ofFrame(Materials.Aluminium))
        .addElement('P', ofFrame(Materials.Invar))
        .addElement('Q', ofFrame(Materials.Steel))
        .addElement('R', ofFrame(Materials.Duranium))
        .addElement('S', Casings.ChemicalGradeGlass.asElement())
        .addElement('T', Casings.PlascreteBlock.asElement())
        .addElement('U', Casings.GrateMachineCasing.asElement())
        .addElement('V', Casings.FilterMachineCasing.asElement())
        .addElement('W', Casings.ShieldedAcceleratorGlass.asElement())
        .addElement(
            'X',
            buildHatchAdder(MTEWaferDopingFacility.class).atLeast(MTEHeatSensor.HeatSensorHatchElement.HeatSensor)
                .casingIndex(Casings.HeatProofMachineCasing.textureId)
                .hint(2)
                .buildAndChain(Casings.HeatProofMachineCasing.asElement()))
        .addElement(
            'Y',
            buildHatchAdder(MTEWaferDopingFacility.class).hatchClass(MTEHatchInputBus.class)
                .adder(MTEWaferDopingFacility::addParticleInputBus)
                .casingIndex(Casings.ShieldedAcceleratorCasing.textureId)
                .hint(5)
                .buildAndChain(Casings.ShieldedAcceleratorCasing.asElement()))
        .addElement(
            'Z',
            buildHatchAdder(MTEWaferDopingFacility.class).atLeast(InputHatch)
                .casingIndex(Casings.ChemicallyInertMachineCasing.textureId)
                .hint(3)
                .buildAndChain(Casings.ChemicallyInertMachineCasing.asElement()))
        .addElement(
            '0',
            buildHatchAdder(MTEWaferDopingFacility.class).hatchClass(MTEHatchDopingControl.class)
                .adder(MTEWaferDopingFacility::addDopingControlHatch)
                .casingIndex(Casings.ChemicallyInertMachineCasing.textureId)
                .hint(4)
                .buildAndChain(Casings.ChemicallyInertMachineCasing.asElement()))
        .addElement(
            '1',
            buildHatchAdder(MTEWaferDopingFacility.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy.or(MultiAmpEnergy))
                .casingIndex(Casings.HeatProofMachineCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.HeatProofMachineCasing.asElement()))
        .build();

    private int structureTier;
    private int requestedParallel = 1;
    private int activeParticleMultiplier;
    private int pendingRecipeTier = 1;
    private int pendingParticleMode;
    private int pendingParticleMultiplier;
    private boolean pendingConsumeAnomaly;
    private HeatingCoilLevel coilLevel;
    private MTEHeatSensor heatSensor;
    private MTEHatchDopingControl dopingControlHatch;
    private MTEHatchInputBus particleInputBus;
    private int currentTemperatureTenths = 2_930;
    private int activeMinimumTemperature;
    private int activeMaximumTemperature;
    private boolean temperatureCycleFailed;
    private int[] signalSequence = new int[0];
    private int signalIndex;
    private long signalDeadline;
    private long signalPulseUntil;
    private boolean signalWaiting;
    private boolean signalCycleFailed;
    private static final String TAG_PARALLEL = "waferDopingFacilityParallel";
    private static final String TAG_PARTICLE_MULTIPLIER = "waferDopingFacilityParticleMultiplier";
    private static final String TAG_TEMP_TENTHS = "waferDopingFacilityTemperatureTenths";
    private static final String TAG_MIN_TEMP = "waferDopingFacilityMinTemperature";
    private static final String TAG_MAX_TEMP = "waferDopingFacilityMaxTemperature";
    private static final String TAG_TEMP_FAILED = "waferDopingFacilityTemperatureFailed";
    private static final String TAG_SIGNAL_SEQUENCE = "waferDopingFacilitySignalSequence";
    private static final String TAG_SIGNAL_INDEX = "waferDopingFacilitySignalIndex";
    private static final String TAG_SIGNAL_DEADLINE = "waferDopingFacilitySignalDeadline";
    private static final String TAG_SIGNAL_PULSE_UNTIL = "waferDopingFacilitySignalPulseUntil";
    private static final String TAG_SIGNAL_WAITING = "waferDopingFacilitySignalWaiting";
    private static final String TAG_SIGNAL_FAILED = "waferDopingFacilitySignalFailed";

    public MTEWaferDopingFacility(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEWaferDopingFacility(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEWaferDopingFacility(mName);
    }

    @Override
    public IStructureDefinition<MTEWaferDopingFacility> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.waferDopingFacilityRecipes;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        structureTier = 0;
        coilLevel = null;
        heatSensor = null;
        if (dopingControlHatch != null) dopingControlHatch.updateOutputSignal((byte) 0);
        dopingControlHatch = null;
        particleInputBus = null;

        List<StructureError> tierFourErrors = new ArrayList<>();
        if (checkPiece(TIER_4, OFFSET_X_4, OFFSET_Y_4, OFFSET_Z_4, tierFourErrors)) {
            validateTierFour(errors);
            return;
        }

        clearHatches();
        structureTier = 0;
        coilLevel = null;
        heatSensor = null;
        if (dopingControlHatch != null) dopingControlHatch.updateOutputSignal((byte) 0);
        dopingControlHatch = null;
        particleInputBus = null;

        List<StructureError> tierThreeErrors = new ArrayList<>();
        if (checkPiece(TIER_3, OFFSET_X_3, OFFSET_Y_3, OFFSET_Z_3, tierThreeErrors)) {
            validateTierThree(errors);
            return;
        }

        clearHatches();
        structureTier = 0;
        coilLevel = null;
        heatSensor = null;
        if (dopingControlHatch != null) dopingControlHatch.updateOutputSignal((byte) 0);
        dopingControlHatch = null;
        particleInputBus = null;
        List<StructureError> tierTwoErrors = new ArrayList<>();
        if (checkPiece(TIER_2, OFFSET_X_2, OFFSET_Y_2, OFFSET_Z_2, tierTwoErrors)) {
            validateTierTwo(errors);
            return;
        }

        clearHatches();
        structureTier = 0;
        coilLevel = null;
        heatSensor = null;
        if (dopingControlHatch != null) dopingControlHatch.updateOutputSignal((byte) 0);
        dopingControlHatch = null;
        particleInputBus = null;
        if (!checkPiece(TIER_1, OFFSET_X_1, OFFSET_Y_1, OFFSET_Z_1, errors)) return;

        coilLevel = HeatingCoilLevel.LV;
        long multiAmpCount = mExoticEnergyHatches.stream()
            .filter(MTEHatchEnergyMulti.class::isInstance)
            .count();
        if (multiAmpCount != 0) errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        validateCommonHatches(errors);
        if (errors.isEmpty()) structureTier = 1;
    }

    private void validateTierThree(List<StructureError> errors) {
        coilLevel = HeatingCoilLevel.LV;
        validateCommonHatches(errors);
        checkHasInputHatch(errors);
        if (heatSensor == null) errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        if (dopingControlHatch == null) errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        if (errors.isEmpty()) structureTier = 3;
    }

    private void validateTierFour(List<StructureError> errors) {
        coilLevel = HeatingCoilLevel.LV;
        validateCommonHatches(errors);
        checkHasInputHatch(errors);
        if (particleInputBus == null || heatSensor == null || dopingControlHatch == null) {
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
        if (errors.isEmpty()) structureTier = 4;
    }

    private void validateTierTwo(List<StructureError> errors) {
        long multiAmpCount = mExoticEnergyHatches.stream()
            .filter(MTEHatchEnergyMulti.class::isInstance)
            .count();
        if (multiAmpCount != 1 || mExoticEnergyHatches.size() != 1 || !mEnergyHatches.isEmpty() || heatSensor == null) {
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
        if (coilLevel == null || coilLevel == HeatingCoilLevel.None) {
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        }
        validateCommonHatches(errors);
        if (errors.isEmpty()) structureTier = 2;
    }

    private void validateCommonHatches(List<StructureError> errors) {
        checkHasAnyEnergy(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public List<MTEHeatSensor> getHeatSensorHatches() {
        return heatSensor == null ? Collections.emptyList() : Collections.singletonList(heatSensor);
    }

    @Override
    public boolean addHeatSensorHatchToMachineList(IGregTechTileEntity tile, int casingTexture) {
        if (tile == null || heatSensor != null || !(tile.getMetaTileEntity() instanceof MTEHeatSensor sensor))
            return false;
        sensor.updateTexture(casingTexture);
        heatSensor = sensor;
        return true;
    }

    private boolean addDopingControlHatch(IGregTechTileEntity tile, int casingTexture) {
        if (tile == null || dopingControlHatch != null
            || !(tile.getMetaTileEntity() instanceof MTEHatchDopingControl hatch)) return false;
        hatch.updateTexture(casingTexture);
        dopingControlHatch = hatch;
        return true;
    }

    private boolean addParticleInputBus(IGregTechTileEntity tile, int casingTexture) {
        if (tile == null || particleInputBus != null || !(tile.getMetaTileEntity() instanceof MTEHatchInputBus bus))
            return false;
        if (!addInputBusToMachineList(tile, casingTexture)) return false;
        particleInputBus = bus;
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                int recipeTier = recipe.getMetadataOrDefault(STRUCTURE_TIER, 1);
                pendingRecipeTier = recipeTier;
                if (recipeTier < 1 || recipeTier > structureTier) return CheckRecipeResultRegistry.NO_RECIPE;
                if (recipeTier >= 2) {
                    activeMinimumTemperature = recipe.getMetadataOrDefault(COIL_HEAT, 0);
                    activeMaximumTemperature = recipe.getMetadataOrDefault(MAX_COIL_HEAT, 0);
                    if (activeMinimumTemperature <= 0 || activeMaximumTemperature <= activeMinimumTemperature
                        || getCurrentTemperature() < activeMinimumTemperature)
                        return CheckRecipeResultRegistry.NO_RECIPE;
                } else {
                    activeMinimumTemperature = 0;
                    activeMaximumTemperature = 0;
                }
                if (recipeTier >= 3 && (!hasThreeDistinctSignalFluids())) return CheckRecipeResultRegistry.NO_RECIPE;
                pendingParticleMode = recipe
                    .getMetadataOrDefault(WAFER_DOPING_PARTICLE_MODE, WAFER_DOPING_PARTICLE_MODE_NONE);
                pendingParticleMultiplier = 0;
                if (recipeTier >= 4) {
                    if (pendingParticleMode == WAFER_DOPING_PARTICLE_MODE_PARTICLES) {
                        pendingParticleMultiplier = calculateParticleMultiplier(
                            countItem(Particle.getBaseParticle(Particle.UNKNOWN)));
                    } else if (pendingParticleMode == WAFER_DOPING_PARTICLE_MODE_ANOMALY) {
                        int available = calculateParticleMultiplier(
                            countItem(GregtechItemList.Laser_Lens_Special.get(0)));
                        if (available > 0) {
                            pendingConsumeAnomaly = getBaseMetaTileEntity().getWorld().rand.nextInt(100) == 0;
                            if (pendingConsumeAnomaly) pendingParticleMultiplier = available;
                        }
                    }
                    if (pendingParticleMultiplier <= 0 && pendingParticleMode != WAFER_DOPING_PARTICLE_MODE_ANOMALY) {
                        return CheckRecipeResultRegistry.NO_RECIPE;
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                int recipeTier = Math.clamp(recipe.getMetadataOrDefault(STRUCTURE_TIER, 1), 1, 4);
                int tickDuration = calculateProcessingTicks(structureTier, recipeTier);
                return super.createOverclockCalculator(recipe).setDuration(tickDuration)
                    .setHeatOC(false)
                    .setMaxOverclocks(0);
            }

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                int maxParallel = structureTier >= 2 ? requestedParallel : 1;
                if (recipe.getMetadataOrDefault(STRUCTURE_TIER, 1) >= 4) {
                    maxParallel *= Math.max(1, pendingParticleMultiplier);
                }
                return super.createParallelHelper(recipe).setMaxParallel(Math.min(1_024, maxParallel));
            }
        }.setMaxParallelSupplier(() -> {
            int userParallel = structureTier >= 2 ? requestedParallel : 1;
            return userParallel;
        });
    }

    private static int calculateParticleMultiplier(int particleCount) {
        return Math.clamp(particleCount, 0, 16);
    }

    private static int calculateProcessingTicks(int structureTier, int recipeTier) {
        if (recipeTier < 1 || recipeTier > 4 || structureTier < recipeTier || structureTier > 4) return 0;
        return (60 * 20) >> (structureTier - recipeTier);
    }

    public int getStructureTier() {
        return structureTier;
    }

    public int getProgressTicks() {
        return mProgresstime;
    }

    public int getRequestedParallel() {
        return requestedParallel;
    }

    public void setRequestedParallel(int value) {
        requestedParallel = Math.clamp(value, 0, 64);
    }

    public int getActiveParticleMultiplier() {
        return activeParticleMultiplier;
    }

    public int getCurrentTemperature() {
        return currentTemperatureTenths / 10;
    }

    public int getMinimumTemperature() {
        return activeMinimumTemperature;
    }

    public int getMaximumTemperature() {
        return activeMaximumTemperature;
    }

    public int getActiveSignalPower() {
        return signalWaiting && signalIndex < signalSequence.length ? signalSequence[signalIndex] : 0;
    }

    public int getSignalTimeRemainingSeconds() {
        if (!signalWaiting || getBaseMetaTileEntity() == null) return 0;
        return (int) Math.max(
            0,
            (signalDeadline - getBaseMetaTileEntity().getWorld()
                .getTotalWorldTime() + 19) / 20);
    }

    public boolean hasTemperatureFailure() {
        return temperatureCycleFailed;
    }

    public boolean hasSignalFailure() {
        return signalCycleFailed;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        pendingRecipeTier = 1;
        pendingParticleMode = 0;
        pendingParticleMultiplier = 0;
        pendingConsumeAnomaly = false;
        activeParticleMultiplier = 0;
        activeMinimumTemperature = 0;
        activeMaximumTemperature = 0;
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful() && pendingRecipeTier >= 2) temperatureCycleFailed = false;
        if (result.wasSuccessful() && pendingRecipeTier >= 3) startSignalCycle();
        if (!result.wasSuccessful() || mMaxProgresstime <= 0 || structureTier < 4 || pendingRecipeTier < 4)
            return result;

        activeParticleMultiplier = pendingParticleMultiplier;
        if (pendingParticleMode == WAFER_DOPING_PARTICLE_MODE_PARTICLES) {
            consumeItem(Particle.getBaseParticle(Particle.UNKNOWN), activeParticleMultiplier);
        } else if (pendingParticleMode == WAFER_DOPING_PARTICLE_MODE_ANOMALY && pendingConsumeAnomaly) {
            consumeItem(GregtechItemList.Laser_Lens_Special.get(0), activeParticleMultiplier);
        }
        return result;
    }

    private int countItem(ItemStack sought) {
        if (sought == null || particleInputBus == null) return 0;
        int count = 0;
        for (ItemStack stack : particleInputBus.mInventory) {
            if (stack != null && stack.isItemEqual(sought)) count += stack.stackSize;
        }
        return count;
    }

    private void consumeItem(ItemStack sought, int amount) {
        if (sought == null || amount <= 0 || particleInputBus == null) return;
        int remaining = amount;
        for (int slot = 0; slot < particleInputBus.mInventory.length && remaining > 0; slot++) {
            ItemStack stack = particleInputBus.mInventory[slot];
            if (stack == null || !stack.isItemEqual(sought)) continue;
            int used = Math.min(remaining, stack.stackSize);
            stack.stackSize -= used;
            remaining -= used;
            if (stack.stackSize == 0) particleInputBus.mInventory[slot] = null;
        }
        particleInputBus.updateSlots();
    }

    private void startSignalCycle() {
        signalSequence = new int[] { 3, 7, 11 };
        net.minecraft.world.World world = getBaseMetaTileEntity().getWorld();
        for (int i = signalSequence.length - 1; i > 0; i--) {
            int j = world.rand.nextInt(i + 1);
            int temporary = signalSequence[i];
            signalSequence[i] = signalSequence[j];
            signalSequence[j] = temporary;
        }
        signalIndex = 0;
        signalCycleFailed = false;
        issueNextSignal();
    }

    private void issueNextSignal() {
        if (signalIndex >= signalSequence.length) {
            signalWaiting = false;
            setSignalOutput((byte) 0);
            return;
        }
        signalWaiting = true;
        long now = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
        signalDeadline = now + 100;
        signalPulseUntil = now + 1;
        setSignalOutput((byte) signalSequence[signalIndex]);
    }

    private void setSignalOutput(byte power) {
        if (dopingControlHatch != null) dopingControlHatch.updateOutputSignal(power);
    }

    private void tickSignalCycle() {
        if (!signalWaiting || signalCycleFailed) return;
        long now = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime();
        if (now >= signalPulseUntil) setSignalOutput((byte) 0);
        if (now > signalDeadline) {
            failSignalCycle();
            return;
        }

        Fluid expectedFluid = getSignalFluid(signalSequence[signalIndex]);
        if (expectedFluid == null) return;
        Fluid[] signalFluids = { getSignalFluid(3), getSignalFluid(7), getSignalFluid(11) };
        for (FluidStack offered : getStoredFluids()) {
            if (offered == null || offered.amount <= 0) continue;
            boolean isSignalFluid = false;
            for (Fluid candidate : signalFluids) {
                if (candidate != null && candidate == offered.getFluid()) isSignalFluid = true;
            }
            if (!isSignalFluid || offered.amount < 10) continue;
            if (offered.getFluid() != expectedFluid || !depleteInput(new FluidStack(expectedFluid, 10))) {
                failSignalCycle();
                return;
            }
            signalIndex++;
            issueNextSignal();
            return;
        }
    }

    private Fluid getSignalFluid(int power) {
        return switch (power) {
            case 3 -> Materials.Cryotheum.getFluid(1)
                .getFluid();
            case 7 -> Materials.Pyrotheum.getFluid(1)
                .getFluid();
            case 11 -> Materials.Oxygen.getPlasma(1)
                .getFluid();
            default -> null;
        };
    }

    private boolean hasThreeDistinctSignalFluids() {
        Fluid first = getSignalFluid(3);
        Fluid second = getSignalFluid(7);
        Fluid third = getSignalFluid(11);
        return first != null && second != null && third != null && first != second && first != third && second != third;
    }

    private void failSignalCycle() {
        signalCycleFailed = true;
        signalWaiting = false;
        mOutputItems = null;
        mOutputFluids = null;
        setSignalOutput((byte) 0);
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        tickSignalCycle();
        if (structureTier >= 2 && activeMaximumTemperature > 0
            && !temperatureCycleFailed
            && getBaseMetaTileEntity().getWorld()
                .getTotalWorldTime() % 20 == 0) {
            currentTemperatureTenths += 100;
            if (getCurrentTemperature() > activeMaximumTemperature) {
                temperatureCycleFailed = true;
                mOutputItems = null;
                mOutputFluids = null;
            }
        }
        if (signalWaiting && mProgresstime > 0) mProgresstime--;
        return super.onRunningTick(stack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        super.onPostTick(base, tick);
        if (base.isServerSide() && heatSensor != null) heatSensor.setHeatValue(getCurrentTemperature());
        if (base.isServerSide() && dopingControlHatch != null) {
            long now = base.getWorld()
                .getTotalWorldTime();
            byte requestedSignal = signalWaiting && !signalCycleFailed
                && signalIndex < signalSequence.length
                && now < signalPulseUntil ? (byte) signalSequence[signalIndex] : 0;
            dopingControlHatch.updateOutputSignal(requestedSignal);
        }
        if (!base.isServerSide() || structureTier < 2 || tick % 20 != 0) return;
        if (mMaxProgresstime > 0) return;
        if (base.isAllowedToWork() && activeMinimumTemperature > getCurrentTemperature()
            && activeMinimumTemperature > 0) {
            if (drainEnergyInput(1)) {
                currentTemperatureTenths = Math
                    .min(activeMinimumTemperature * 10, currentTemperatureTenths + coilHeatingRateTenths());
            }
        } else {
            currentTemperatureTenths = Math.max(2_930, currentTemperatureTenths - 2_500);
        }
    }

    private int coilHeatingRateTenths() {
        int coilTier = coilLevel == null ? 0 : coilLevel.getTier() + 1;
        return (int) Math.round((75.0 + Math.pow(1.8, coilTier)) * 10.0);
    }

    private HeatingCoilLevel getCoil() {
        return coilLevel;
    }

    private void setCoil(HeatingCoilLevel level) {
        coilLevel = level;
    }

    @Override
    protected void outputAfterRecipe() {
        super.outputAfterRecipe();
        activeMinimumTemperature = 0;
        activeMaximumTemperature = 0;
        signalWaiting = false;
        signalSequence = new int[0];
        signalCycleFailed = false;
        temperatureCycleFailed = false;
        setSignalOutput((byte) 0);
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);
        tag.setInteger(TAG_PARALLEL, requestedParallel);
        tag.setInteger(TAG_PARTICLE_MULTIPLIER, activeParticleMultiplier);
        tag.setInteger(TAG_TEMP_TENTHS, currentTemperatureTenths);
        tag.setInteger(TAG_MIN_TEMP, activeMinimumTemperature);
        tag.setInteger(TAG_MAX_TEMP, activeMaximumTemperature);
        tag.setBoolean(TAG_TEMP_FAILED, temperatureCycleFailed);
        tag.setIntArray(TAG_SIGNAL_SEQUENCE, signalSequence);
        tag.setInteger(TAG_SIGNAL_INDEX, signalIndex);
        tag.setLong(TAG_SIGNAL_DEADLINE, signalDeadline);
        tag.setLong(TAG_SIGNAL_PULSE_UNTIL, signalPulseUntil);
        tag.setBoolean(TAG_SIGNAL_WAITING, signalWaiting);
        tag.setBoolean(TAG_SIGNAL_FAILED, signalCycleFailed);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);
        setRequestedParallel(tag.getInteger(TAG_PARALLEL));
        activeParticleMultiplier = Math.clamp(tag.getInteger(TAG_PARTICLE_MULTIPLIER), 0, 16);
        currentTemperatureTenths = Math.max(2_930, tag.getInteger(TAG_TEMP_TENTHS));
        activeMinimumTemperature = Math.max(0, tag.getInteger(TAG_MIN_TEMP));
        activeMaximumTemperature = Math.max(0, tag.getInteger(TAG_MAX_TEMP));
        temperatureCycleFailed = tag.getBoolean(TAG_TEMP_FAILED);
        signalSequence = tag.getIntArray(TAG_SIGNAL_SEQUENCE);
        if (signalSequence.length != 3) signalSequence = new int[0];
        signalIndex = Math.clamp(signalSequence.length, 0, tag.getInteger(TAG_SIGNAL_INDEX));
        signalDeadline = tag.getLong(TAG_SIGNAL_DEADLINE);
        signalPulseUntil = tag.getLong(TAG_SIGNAL_PULSE_UNTIL);
        signalWaiting = tag.getBoolean(TAG_SIGNAL_WAITING);
        signalCycleFailed = tag.getBoolean(TAG_SIGNAL_FAILED);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEWaferDopingFacilityGui(this);
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        int tier = stack == null ? 1 : Math.clamp(stack.stackSize, 1, 4);
        if (tier == 1) buildPiece(TIER_1, stack, hintsOnly, OFFSET_X_1, OFFSET_Y_1, OFFSET_Z_1);
        else if (tier == 2) buildPiece(TIER_2, stack, hintsOnly, OFFSET_X_2, OFFSET_Y_2, OFFSET_Z_2);
        else if (tier == 3) buildPiece(TIER_3, stack, hintsOnly, OFFSET_X_3, OFFSET_Y_3, OFFSET_Z_3);
        else buildPiece(TIER_4, stack, hintsOnly, OFFSET_X_4, OFFSET_Y_4, OFFSET_Z_4);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int budget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int tier = stack == null ? 1 : Math.clamp(stack.stackSize, 1, 4);
        String piece = tier == 1 ? TIER_1 : tier == 2 ? TIER_2 : tier == 3 ? TIER_3 : TIER_4;
        int offsetX = tier == 1 ? OFFSET_X_1 : tier == 2 ? OFFSET_X_2 : tier == 3 ? OFFSET_X_3 : OFFSET_X_4;
        int offsetY = tier == 1 ? OFFSET_Y_1 : tier == 2 ? OFFSET_Y_2 : tier == 3 ? OFFSET_Y_3 : OFFSET_Y_4;
        int offsetZ = tier == 1 ? OFFSET_Z_1 : tier == 2 ? OFFSET_Z_2 : tier == 3 ? OFFSET_Z_3 : OFFSET_Z_4;
        return survivalBuildPiece(piece, stack, offsetX, offsetY, offsetZ, budget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(StatCollector.translateToLocal("GT5U.wafer_doping.name"))
            .beginStructureBlock(50, 15, 45, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMiscHatch("1", "Heat Sensor Hatch", "Specified Position", 2)
            .addInputHatch("1", "Input Hatch for Signal Fluid", 3)
            .addMiscHatch("1", "Doping Control Hatch", "Specified Position", 4)
            .addInputBus("1", "Input Bus for Particle input", 1)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.One"))
            .addCasing("20+", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addCasing("20", Casings.PlascreteBlock.getLocalizedName(), false)
            .addCasing("13", "Steel Frame Box", false)
            .addCasing("12", "Steel Pipe Casing", false)
            .addCasing("5", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("4", Casings.SolidSteelMachineCasing.getLocalizedName(), false)
            .addCasing("2", Casings.SteelGearBoxCasing.getLocalizedName(), false)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.Two"))
            .addCasing("120+", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addCasing("80", Casings.PTFEPipeCasing.getLocalizedName(), false)
            .addCasing("69", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("24", "Steel Pipe Casing", false)
            .addCasing("20", Casings.PlascreteBlock.getLocalizedName(), false)
            .addCasing("13", "Steel Frame Box", false)
            .addCasing("4", Casings.SolidSteelMachineCasing.getLocalizedName(), false)
            .addCasing("2", Casings.SteelGearBoxCasing.getLocalizedName(), false)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.Three"))
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.Four"))
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        ITexture casing = Textures.BlockIcons.getCasingTextureForId(Casings.HeatProofMachineCasing.textureId);
        if (side != facing) return new ITexture[] { casing };
        return new ITexture[] { casing, TextureFactory.builder()
            .addIcon(
                active ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE
                    : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)
            .extFacing()
            .build() };
    }
}
