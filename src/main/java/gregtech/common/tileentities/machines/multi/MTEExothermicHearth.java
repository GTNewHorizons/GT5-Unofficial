package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEARTH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEARTH_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEARTH_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEARTH_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.gui.modularui.multiblock.MTEExothermicHearthGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEExothermicHearth extends MTEExtendedPowerMultiBlockBase<MTEExothermicHearth>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int VERTICAL_OFFSET = 39;
    private static final int HORIZONTAL_OFFSET = 11;
    private static final int DEPTH_OFFSET = 1;
    private static final IStructureDefinition<MTEExothermicHearth> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEExothermicHearth>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"                       ","                       ","                       ","                       ","                       ","         G   G         ","         G   G         ","         G   G         ","         G   G         ","     GGGGGGGGGGGGG     ","         G   G         ","         G   G         ","         G   G         ","     GGGGGGGGGGGGG     ","         G   G         ","         G   G         ","         G   G         ","         G   G         ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","         HHHHH         ","      HHH H H HHH      ","     H   GH HG   H     ","    H   FFFFFFF   H    ","    H  FFFFFFFFF  H    ","    H FFFFFFFFFFF H    ","   H GFFFFFFFFFFFG H   ","   HHHFFFFFHFFFFFHHH   ","   H  FFFFHKHFFFF  H   ","   HHHFFFFFHFFFFFHHH   ","   H GFFFFFFFFFFFG H   ","    H FFFFFFFFFFF H    ","    H  FFFFFFFFF  H    ","    H   FFFFFFF   H    ","     H   GH HG   H     ","      HHH H H HHH      ","         HHHHH         ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","         HHHHH         ","      HHHHHHHHHHH      ","     HHHHHHHHHHHHH     ","    HHHHHHHHHHHHHHH    ","    HHHHHDDDDDHHHHH    ","    HHHHD  D  DHHHH    ","   HHHHD   D   DHHHH   ","   HHHHD  DDD  DHHHH   ","   HHHHDDDDDDDDDHHHH   ","   HHHHD  DDD  DHHHH   ","   HHHHD   D   DHHHH   ","    HHHHD  D  DHHHH    ","    HHHHHDDDDDHHHHH    ","    HHHHHHHHHHHHHHH    ","     HHHHHHHHHHHHH     ","      HHHHHHHHHHH      ","         HHHHH         ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","         HHHHH         ","      HHHHHHHHHHH      ","     HHHHHHHHHHHHH     ","    HHHHHHHHHHHHHHH    ","    HHHHH     HHHHH    ","    HHHH       HHHH    ","   HHHH         HHHH   ","   HHHH         HHHH   ","   HHHH    D    HHHH   ","   HHHH         HHHH   ","   HHHH         HHHH   ","    HHHH       HHHH    ","    HHHHH     HHHHH    ","    HHHHHHHHHHHHHHH    ","     HHHHHHHHHHHHH     ","      HHHHHHHHHHH      ","         HHHHH         ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE       EHH     ","    HHE         EHH    ","    HE           EH    ","    HE     D     EH    ","    HE           EH    ","    HHE         EHH    ","     HHE       EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          EEE          ","      F  E   E  F      ","        E     E        ","       E   C   E       ","      E    C    E      ","     E     C     E     ","     E  CCCDCCC  E     ","     E     C     E     ","      E    C    E      ","       E   C   E       ","        E     E        ","      F  E   E  F      ","          EEE          ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE   C   EHH     ","    HHE         EHH    ","    HE           EH    ","    HE  C  D  C  EH    ","    HE           EH    ","    HHE         EHH    ","     HHE   C   EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          EEE          ","      F  E   E  F      ","        E     E        ","       E   C   E       ","      E         E      ","     E           E     ","     E  C  D  C  E     ","     E           E     ","      E         E      ","       E   C   E       ","        E     E        ","      F  E   E  F      ","          EEE          ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE   C   EHH     ","    HHE         EHH    ","    HE           EH    ","    HE  C  D  C  EH    ","    HE           EH    ","    HHE         EHH    ","     HHE   C   EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          EEE          ","      F  E   E  F      ","        E     E        ","       E   C   E       ","      E         E      ","     E           E     ","     E  C  D  C  E     ","     E           E     ","      E         E      ","       E   C   E       ","        E     E        ","      F  E   E  F      ","          EEE          ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE   C   EHH     ","    HHE         EHH    ","    HE           EH    ","    HE  C  D  C  EH    ","    HE           EH    ","    HHE         EHH    ","     HHE   C   EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          EEE          ","      F  E   E  F      ","        E     E        ","       E   C   E       ","      E         E      ","     E           E     ","     E  C  D  C  E     ","     E           E     ","      E         E      ","       E   C   E       ","        E     E        ","      F  E   E  F      ","          EEE          ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE       EHH     ","    HHE    C    EHH    ","    HE     C     EH    ","    HE   CCDCC   EH    ","    HE     C     EH    ","    HHE    C    EHH    ","     HHE       EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","         HHHHH         ","        HHEEEHH        ","      FHHE   EHHF      ","      HHE     EHH      ","     HHE       EHH     ","    HHE         EHH    ","    HE           EH    ","    HE     D     EH    ","    HE           EH    ","    HHE         EHH    ","     HHE       EHH     ","      HHE     EHH      ","      FHHE   EHHF      ","        HHEEEHH        ","         HHHHH         ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","         HHHHH         ","      F HH   HH F      ","       HH     HH       ","      HH       HH      ","     HH         HH     ","     H           H     ","     H     D     H     ","     H           H     ","     HH         HH     ","      HH       HH      ","       HH     HH       ","      F HH   HH F      ","         HHHHH         ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        F     F        ","        FFFFFFF        ","      FFFIIIIIFFF      ","      FII     IIF      ","    FFFI       IFFF    ","     FI         IF     ","     FI         IF     ","     FI    D    IF     ","     FI         IF     ","     FI         IF     ","    FFFI       IFFF    ","      FII     IIF      ","      FFFIIIIIFFF      ","        FFFFFFF        ","        F     F        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        FIIIIIF        ","      IIIIIIIIIII      ","     IIEEEEEEEEEII     ","     IEI       IEI     ","    FIE         EIF    ","    IIE         EII    ","    IIE    C    EII    ","    IIE   CDC   EII    ","    IIE    C    EII    ","    IIE         EII    ","    FIE         EIF    ","     IEI       IEI     ","     IIEEEEEEEEEII     ","      IIIIIIIIIII      ","        FIIIIIF        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        F     F        ","      I  EEEEE  I      ","     IIEEEEEEEEEII     ","      EI       IE      ","    F E         E F    ","     EE         EE     ","     EE    C    EE     ","     EE   CDC   EE     ","     EE    C    EE     ","     EE         EE     ","    F E         E F    ","      EI       IE      ","     IIEEEEEEEEEII     ","      I  EEEEE  I      ","        F     F        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        FIIIIIF        ","      IIIIIIIIIII      ","     IIEEEEEEEEEII     ","     IEI       IEI     ","    FIE         EIF    ","    IIE         EII    ","    IIE    C    EII    ","    IIE   CDC   EII    ","    IIE    C    EII    ","    IIE         EII    ","    FIE         EIF    ","     IEI       IEI     ","     IIEEEEEEEEEII     ","      IIIIIIIIIII      ","        FIIIIIF        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        F     F        ","      I  EEEEE  I      ","     IIEEEEEEEEEII     ","      EI       IE      ","    F E         E F    ","     EE         EE     ","     EE    C    EE     ","     EE   CDC   EE     ","     EE    C    EE     ","     EE         EE     ","    F E         E F    ","      EI       IE      ","     IIEEEEEEEEEII     ","      I  EEEEE  I      ","        F     F        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        FIIIIIF        ","      IIIIIIIIIII      ","     IIEEEEEEEEEII     ","     IEI       IEI     ","    FIE         EIF    ","    IIE         EII    ","    IIE    C    EII    ","    IIE   CDC   EII    ","    IIE    C    EII    ","    IIE         EII    ","    FIE         EIF    ","     IEI       IEI     ","     IIEEEEEEEEEII     ","      IIIIIIIIIII      ","        FIIIIIF        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        F     F        ","      I  EEEEE  I      ","     IIEEEEEEEEEII     ","      EI       IE      ","    F E         E F    ","     EE         EE     ","     EE    C    EE     ","     EE   CDC   EE     ","     EE    C    EE     ","     EE         EE     ","    F E         E F    ","      EI       IE      ","     IIEEEEEEEEEII     ","      I  EEEEE  I      ","        F     F        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        FIIIIIF        ","      IIIIIIIIIII      ","     IIEEEEEEEEEII     ","     IEI       IEI     ","    FIE         EIF    ","    IIE         EII    ","    IIE    C    EII    ","    IIE   CDC   EII    ","    IIE    C    EII    ","    IIE         EII    ","    FIE         EIF    ","     IEI       IEI     ","     IIEEEEEEEEEII     ","      IIIIIIIIIII      ","        FIIIIIF        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        F     F        ","      I  EEEEE  I      ","     IIEEEEEEEEEII     ","      EI       IE      ","    F E         E F    ","     EE         EE     ","     EE    C    EE     ","     EE   CDC   EE     ","     EE    C    EE     ","     EE         EE     ","    F E         E F    ","      EI       IE      ","     IIEEEEEEEEEII     ","      I  EEEEE  I      ","        F     F        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","        FIIIIIF        ","      IIIIIIIIIII      ","     IIEEEEEEEEEII     ","     IEI       IEI     ","    FIE         EIF    ","    IIE         EII    ","    IIE    C    EII    ","    IIE   CDC   EII    ","    IIE    C    EII    ","    IIE         EII    ","    FIE         EIF    ","     IEI       IEI     ","     IIEEEEEEEEEII     ","      IIIIIIIIIII      ","        FIIIIIF        ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","      FFFFFFFFFFF      ","      IIIIIIIIIII      ","    FIIIIIIIIIIIIIF    ","    FII         IIF    ","    FII         IIF    ","    FII    C    IIF    ","    FII         IIF    ","    FII  C D C  IIF    ","    FII         IIF    ","    FII    C    IIF    ","    FII         IIF    ","    FII         IIF    ","    FIIIIIIIIIIIIIF    ","      IIIIIIIIIII      ","      FFFFFFFFFFF      ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","      II       II      ","    FFIIIIIIIIIIFFF    ","    FIIIIIIIIIIIIIF    ","   IIII         IIFI   ","   III           III   ","    II           II    ","    II    CCC    II    ","    II   C   C   II    ","    II   C D C   II    ","    II   C   C   II    ","    II    CCC    II    ","    II           II    ","   III           III   ","   IFII         IIFI   ","    FIIIIIIIIIIIIIF    ","    FFFIIIIIIIIIFFF    ","      II       II      ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","      II       II      ","    FAIIIAAAAAIIIAF    ","    AJEEEEEEEEEEEJA    ","   IIEI         IEII   ","   IIE           EII   ","    IE           EI    ","    AE     C     EA    ","    AE           EA    ","    AE   C D C   EA    ","    AE           EA    ","    AE     C     EA    ","    IE           EI    ","   IIE           EII   ","   IIEI         IEII   ","    AJEEEEEEEEEEEJA    ","    FAIIIAAAAAIIIAF    ","      II       II      ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","      II       II      ","    FABBAAAAAAABBAF    ","    ACEEEEEEEEEEECA    ","   IBEI         IEBI   ","   IBE           EBI   ","    AE     C     EA    ","    AE           EA    ","    AE           EA    ","    AE  C  D  C  EA    ","    AE           EA    ","    AE           EA    ","    AE     C     EA    ","   IBE           EBI   ","   IBEI         IEBI   ","    ACEEEEEEEEEEECA    ","    FABBAAAAAAABBAF    ","      II       II      ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","      II       II      ","    FABBAAAAAAABBAF    ","    AJJJJJJJJJJJJJA    ","   IBJI         IJBI   ","   IBJ           JBI   ","    AJ     C     JA    ","    AJ           JA    ","    AJ           JA    ","    AJ  C  D  C  JA    ","    AJ           JA    ","    AJ           JA    ","    AJ     C     JA    ","   IBJ           JBI   ","   IBJI         IJBI   ","    AJJJJJJJJJJJJJA    ","    FABBAAAAAAABBAF    ","      II       II      ","                       ","                       ","                       "},
                {"                       ","                       ","      II       II      ","      BB       BB      ","    FABBAAAAAAABBAF    ","    ACEEEEEEEEEEECA    ","  IBBEI         IEBBI  ","  IBBE           EBBI  ","    AE   CCCCC   EA    ","    AE  C     C  EA    ","    AE  C     C  EA    ","    AE  C  D  C  EA    ","    AE  C     C  EA    ","    AE  C     C  EA    ","    AE   CCCCC   EA    ","  IBBE           EBBI  ","  IBBEI         IEBBI  ","    ACEEEEEEEEEEECA    ","    FABBAAAAAAABBAF    ","      BB       BB      ","      II       II      ","                       ","                       "},
                {"                       ","                       ","      II       II      ","      BB       BB      ","    FABIIAAAAAIIBAF    ","    AJEEEEEEEEEEEJA    ","  IBBEI         IEBBI  ","  IBIE           EIBI  ","    IE     C     EI    ","    AE           EA    ","    AE           EA    ","    AE  C  D  C  EA    ","    AE           EA    ","    AE           EA    ","    IE     C     EI    ","  IBIE           EIBI  ","  IBBEI         IEBBI  ","    AJEEEEEEEEEEEJA    ","    FABIIAAAAAIIBAF    ","      BB       BB      ","      II       II      ","                       ","                       "},
                {"                       ","                       ","      II       II      ","      BB       BB      ","    FIBIIIIIIIIIBIF    ","    IIIIIIIIIIIIIII    ","  IBBI           IBBI  ","  IBII           IIBI  ","    II     C     II    ","    II           II    ","    II           II    ","    II  C  D  C  II    ","    II           II    ","    II           II    ","    II     C     II    ","  IBII           IIBI  ","  IBBI           IBBI  ","    IIIIIIIIIIIIIII    ","    FIBIIIIIIIIIBIF    ","      BB       BB      ","      II       II      ","                       ","                       "},
                {"                       ","      II       II      ","      BB  III  BB      ","    FFIIIIIIIIIIIFF    ","   FFIIIIIIIIIIIIIFF   ","   FII           IIF   "," IBII             IIBI "," IBII      C      IIBI ","   II             II   ","   II             II   ","  III             III  ","  III  C   D   C  III  ","  III             III  ","   II             II   ","   II             II   "," IBII      C      IIBI "," IBII             IIBI ","   FII           IIF   ","   FFIIIIIIIIIIIIIFF   ","    FFIIIIIIIIIIIFF    ","      BB  III  BB      ","      II       II      ","                       "},
                {"                       ","      II       II      ","      BB  III  BB      ","    IABBAABBBAABIAI    ","   IFJJJJJJJJJJJJJFI   ","   AJI           IJA   "," IBIJ             JBBI "," IBBJ      C      JBBI ","   AJ             JA   ","   AJ             JA   ","  IBJ             JBI  ","  IBJ  C   D   C  JBI  ","  IBJ             JBI  ","   AJ             JA   ","   AJ             JA   "," IBBJ      C      JBBI "," IBBJ             JIBI ","   AJI           IJA   ","   IFJJJJJJJJJJJJJFI   ","    IAIBAABBBAABBAI    ","      BB  III  BB      ","      II       II      ","                       "},
                {"                       ","      II       II      ","      BB  III  BB      ","    IABBAABBBAABIAI    ","   IFCCCCCCCCCCCCCFI   ","   ACI           ICA   "," IBIC             CBBI "," IBBC    CCCCC    CBBI ","   AC   C     C   CA   ","   AC  C       C  CA   ","  IBC  C       C  CBI  ","  IBC  C   D   C  CBI  ","  IBC  C       C  CBI  ","   AC  C       C  CA   ","   AC   C     C   CA   "," IBBC    CCCCC    CBBI "," IBBC             CIBI ","   ACI           ICA   ","   IFCCCCCCCCCCCCCFI   ","    IAIBAABBBAABBAI    ","      BB  III  BB      ","      II       II      ","                       "},
                {"                       ","      II       II      ","      BB  III  BB      ","    IABBAABBBAABIAI    ","   IFJJJJJJJJJJJJJFI   ","   AJI           IJA   "," IBIJ             JBBI "," IBBJ      C      JBBI ","   AJ             JA   ","   AJ             JA   ","  IBJ             JBI  ","  IBJ  C   D   C  JBI  ","  IBJ             JBI  ","   AJ             JA   ","   AJ             JA   "," IBBJ      C      JBBI "," IBBJ             JIBI ","   AJI           IJA   ","   IFJJJJJJJJJJJJJFI   ","    IAIBAABBBAABBAI    ","      BB  III  BB      ","      II       II      ","                       "},
                {"      II       II      ","      BB  III  BB      ","   FFFBBIIBBBIIBBFFF   ","  FFIIIIIIIIIIIIIIIFF  ","  FIFIIIIIIIIIIIIIFIF  ","  FIII           IIIF  ","IBBII             IIBBI","IBBII      C      IIBBI","  III             III  ","  III             III  "," IBII             IIBI "," IBII  C   D   C  IIBI "," IBII             IIBI ","  III             III  ","  III             III  ","IBBII      C      IIBBI","IBBII             IIBBI","  FIII           IIIF  ","  FIFIIIIIIIIIIIIIFIF  ","  FFIIIIIIIIIIIIIIIFF  ","   FFFBBIIBBBIIBBFFF   ","      BB  III  BB      ","      II       II      "},
                {"      II       II      ","      BB  III  BB      ","   IAABBAABBBAABBAAI   ","  IIJJJJJJJJJJJJJJJII  ","  AJI             IJA  ","  AJ               JA  ","IBBJ       C       JBBI","IBBJ               JBBI","  AJ               JA  ","  AJ               JA  "," IBJ               JBI "," IBJ  C    D    C  JBI "," IBJ               JBI ","  AJ               JA  ","  AJ               JA  ","IBBJ               JBBI","IBBJ       C       JBBI","  AJ               JA  ","  AJI             IJA  ","  IIJJJJJJJJJJJJJJJII  ","   IAABBAABBBAABBAAI   ","      BB  III  BB      ","      II       II      "},
                {"      II       II      ","      BB  I~I  BB      ","   IAABBAABBBAABBAAI   ","  IICCCCCCCCCCCCCCCII  ","  ACI             ICA  ","  AC               CA  ","IBBC       C       CBBI","IBBC               CBBI","  AC               CA  ","  AC               CA  "," IBC               CBI "," IBC  C    D    C  CBI "," IBC               CBI ","  AC               CA  ","  AC               CA  ","IBBC               CBBI","IBBC       C       CBBI","  AC               CA  ","  ACI             ICA  ","  IICCCCCCCCCCCCCCCII  ","   IAABBAABBBAABBAAI   ","      BB  III  BB      ","      II       II      "},
                {"      II       II      ","      BB  III  BB      ","   IAABBAABBBAABBAAI   ","  IIJJJJJJJJJJJJJJJII  ","  AJI             IJA  ","  AJ               JA  ","IBBJ     DDCDD     JBBI","IBBJ   DD  D  DD   JBBI","  AJ   D   D   D   JA  ","  AJ  D   DDD   D  JA  "," IBJ  D  D D D  D  JBI "," IBJ  CDDDDDDDDDC  JBI "," IBJ  D  D D D  D  JBI ","  AJ  D   DDD   D  JA  ","  AJ   D   D   D   JA  ","IBBJ   DD  D  DD   JBBI","IBBJ     DDCDD     JBBI","  AJ               JA  ","  AJI             IJA  ","  IIJJJJJJJJJJJJJJJII  ","   IAABBAABBBAABBAAI   ","      BB  III  BB      ","      II       II      "},
                {"    IIIIIIIIIIIIIII    ","   IBBBBBBIIIBBBBBBI   ","  IBBBBBBBBBBBBBBBBBI  "," IBBBBBBBBBBBBBBBBBBBI ","IBBBIIIIBBIIIBBIIIIBBBI","IBBBIIBBBBBBBBBBBIIBBBI","IBBBIBBBBBBBBBBBBBIBBBI","IBBBIBBBBBBBBBBBBBIBBBI","IBBBBBBBBIBBBIBBBBBBBBI","IBBBBBBBIBBBBBIBBBBBBBI","IIBBIBBBBBBBBBBBBBIBBII","IIBBIBBBBBBBBBBBBBIBBII","IIBBIBBBBBBBBBBBBBIBBII","IBBBBBBBIBBBBBIBBBBBBBI","IBBBBBBBBIBBBIBBBBBBBBI","IBBBIBBBBBBBBBBBBBIBBBI","IBBBIBBBBBBBBBBBBBIBBBI","IBBBIIBBBBBBBBBBBIIBBBI","IBBBIIIIBBIIIBBIIIIBBBI"," IBBBBBBBBBBBBBBBBBBBI ","  IBBBBBBBBBBBBBBBBBI  ","   IBBBBBBIIIBBBBBBI   ","    IIIIIIIIIIIIIII    "},
                {"    IIIIIIIIIIIIIII    ","   IIIIIIIIIIIIIIIII   ","  IIIIIIIIIIIIIIIIIII  "," IIIIIIIIIBBBIIIIIIIII ","IIIIBBBIIBBBBBIIBBBIIII","IIIIBBBBBBBBBBBBBBBIIII","IIIIBBBBBBIIIBBBBBBIIII","IIIIIBBBIIIIIIIBBBIIIII","IIIIIBBIIIIIIIIIBBIIIII","IIIIBBBIIIIIIIIIBBBIIII","IIIBBBIIIIBBBBBBBBBBIII","IIIBBBIIIIBBBBBBBBBBIII","IIIBBBIIIIBBBBBBBBBBIII","IIIIBBBIIIIIIIIIIIIIIII","IIIIIBBIIIIIIIIIIIIIIII","IIIIIBBBIIIIIIIBBBIIIII","IIIIBBBBBBIIIBBBBBBIIII","IIIIBBBBBBBBBBBBBBBIIII","IIIIBBBIIBBBBBIIBBBIIII"," IIIIIIIIIBBBIIIIIIIII ","  IIIIIIIIIIIIIIIIIII  ","   IIIIIIIIIIIIIIIII   ","    IIIIIIIIIIIIIII    "}
            }))
        //spotless:on
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('B', Casings.HeatProofMachineCasing.asElement())
        .addElement('C', Casings.BlackPlutoniumItemPipeCasing.asElement())
        .addElement('D', Casings.TungstensteelPipeCasing.asElement())
        .addElement(
            'E',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEExothermicHearth::setCoilLevel, MTEExothermicHearth::getCoilLevel))))
        .addElement('F', Casings.RadiantNaquadahAlloyCasing.asElement())
        .addElement('G', ofFrame(Materials.PrismaticNaquadah))
        .addElement('H', Casings.ThermalContainmentCasing.asElement())
        .addElement(
            'I',
            buildHatchAdder(MTEExothermicHearth.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .hint(1)
                .casingIndex(Casings.HearthCasing.getTextureId())
                .buildAndChain(onElementPass(MTEExothermicHearth::onCasingAdded, Casings.HearthCasing.asElement())))
        .addElement('J', Casings.BlastSmelterHeatContainmentCoil.asElement())
        .addElement(
            'K',
            buildHatchAdder(MTEExothermicHearth.class).anyOf(Muffler)
                .hint(2)
                .casingIndex(Casings.StructuralCokeOvenCasing.getTextureId())
                // this casing references the same texture as the alloy blast smelter,
                // and it has the perk of actually being registered in the atlas!
                // if this ever gets split out for texture pack reasons, it should be updated here as well.
                .build())
        .build();

    public MTEExothermicHearth(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExothermicHearth(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEExothermicHearth> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExothermicHearth(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Furnace, HeaRTH, MEBF, MBF")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addInfo(
                TooltipHelper.effText("-5%") + " EU Usage per "
                    + TooltipHelper.coloredText("900K", EnumChatFormatting.RED)
                    + " above the recipe requirement")
            .addSeparator()
            .addInfo(
                "Increases Heat by " + EnumChatFormatting.RED
                    + "100K"
                    + EnumChatFormatting.GRAY
                    + " for every "
                    + TooltipHelper.tierText("Voltage")
                    + " tier past "
                    + EnumChatFormatting.AQUA
                    + "MV")
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "1800K"
                    + EnumChatFormatting.GRAY
                    + " over the recipe requirement grants 1 "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Perfect Overclock")
            .addSeparator()
            .addInfo(
                "While active, the machine will heat up and multiply its parallels up to " + EnumChatFormatting.GOLD
                    + "2x")
            .addInfo(
                "Takes " + EnumChatFormatting.LIGHT_PURPLE
                    + "30 minutes"
                    + EnumChatFormatting.GRAY
                    + " of constant running to reach maximum multiplier")
            .addInfo("While not running, the machine will rapidly cooldown")
            .addInfo(
                "Optionally supply " + EnumChatFormatting.RED
                    + formatFluid(PYROTHEUM_DRAIN_BASE)
                    + EnumChatFormatting.GRAY
                    + "/s of "
                    + EnumChatFormatting.GOLD
                    + "Pyrotheum"
                    + EnumChatFormatting.GRAY
                    + " to speed up heating by "
                    + EnumChatFormatting.RED
                    + "6x")
            .addInfo(
                "The drain rate of " + EnumChatFormatting.GOLD
                    + "Pyrotheum"
                    + EnumChatFormatting.GRAY
                    + " will increase "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "linearly"
                    + EnumChatFormatting.GRAY
                    + " with the parallel multiplier")
            .addSeparator()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo(VoltageIndex.UMV)
            .addUnlimitedTierSkips()
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .addInfo(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.DARK_RED + "Heating up!")
            .beginStructureBlock(23, 43, 23, true)
            .addController("Front center, 4th layer")
            .addCasingInfoRange("Heat Absorbent Casing", 1800, 1915, false)
            .addCasingInfoExactly("Heat Proof Machine Casing", 925, false)
            .addCasingInfoExactly("Heating Coils", 864, true)
            .addCasingInfoExactly("Any Tiered Glass", 332, true)
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addEnergyHatch("Any Heat Absorbent Casing")
            .addMaintenanceHatch("Any Heat Absorbent Casing")
            .addInputBus("Any Heat Absorbent Casing")
            .addInputHatch("Any Heat Absorbent Casing")
            .addMufflerHatch("Top middle")
            .addOutputBus("Any Heat Absorbent Casing")
            .addOutputHatch("Any Heat Absorbent Casing")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addStructureAuthors("GregTech Odyssey")
            .toolTipFinisher();
        return tt;
    }

    private HeatingCoilLevel coilLevel;
    private int heatingCapacity;
    private int glassTier = -1;

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.glassTier = aNBT.getInteger("glass");
        this.parallelModifier = aNBT.getFloat("parallelModifier");
        this.isPyroSupplied = aNBT.getBoolean("pyro");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("glass", this.glassTier);
        aNBT.setFloat("parallelModifier", this.parallelModifier);
        aNBT.setBoolean("pyro", this.isPyroSupplied);
    }

    private boolean checkFluid(int amount) {
        final FluidStack fluidToDrain = new FluidStack(TFFluids.fluidPyrotheum, amount);
        return this.depleteInput(fluidToDrain, true);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(MTEExothermicHearth.this.heatingCapacity)
                    .setHeatOC(true)
                    .setHeatDiscount(true);
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (isPyroSupplied) {
                    if (!checkFluid((int) Math.floor(PYROTHEUM_DRAIN_BASE * parallelModifier)))
                        return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                }
                return recipe.mSpecialValue <= MTEExothermicHearth.this.heatingCapacity
                    ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    private int runningTickCounter = 0;
    private float parallelModifier = 1;
    public boolean isPyroSupplied = false;
    private static final int PYROTHEUM_DRAIN_BASE = 250;
    // without pyrotheum, it should take 30 minutes to reach max multiplier (2x)
    // with pyrotheum, itll take 5 minutes.
    private static final float INCREMENT_BASE = 1f / 360;
    private static final float INCREMENT_PYRO = INCREMENT_BASE * 6;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        runningTickCounter++;
        // every 5 seconds, increment the parallel modifier.
        if (runningTickCounter % 20 == 0) { // drain pyrotheum and crash machine if enough isnt supplied
            if (isPyroSupplied) {
                final FluidStack pyrotheum = new FluidStack(
                    TFFluids.fluidPyrotheum,
                    (int) Math.floor(PYROTHEUM_DRAIN_BASE * parallelModifier));
                if (!this.depleteInput(pyrotheum, false)) stopMachine(ShutDownReasonRegistry.outOfFluid(pyrotheum));
            }
        }
        if (runningTickCounter % 100 == 0 && parallelModifier < 2) {
            float increment = isPyroSupplied ? INCREMENT_PYRO : INCREMENT_BASE;
            parallelModifier = Math.min(2, parallelModifier + increment);
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMTE, long tick) {
        super.onPostTick(baseMTE, tick);
        if (!baseMTE.isServerSide()) return;
        if (mMaxProgresstime == 0 && parallelModifier > 1) { // machine is not running
            // every second while not on, decrement parallel modifier
            if (tick % 20 == 0) parallelModifier = Math.max(1, parallelModifier - INCREMENT_BASE);
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("pyrotheum", isPyroSupplied);
        tag.setInteger("drain", (int) Math.floor(parallelModifier * PYROTHEUM_DRAIN_BASE));
        tag.setFloat("parallelModifier", parallelModifier);

        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        boolean pyrotheumActive = tag.getBoolean("pyrotheum");
        float parallelModifier = tag.getFloat("parallelModifier");
        currentTip.add(translateToLocalFormatted("GT5U.waila.mebf.parallel", formatNumber(parallelModifier)));
        if (pyrotheumActive) {
            currentTip
                .add(translateToLocalFormatted("GT5U.waila.mebf.pyrotheum", formatFluid(tag.getInteger("drain"))));
        }

    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) Math.floor(parallelModifier * Configuration.Multiblocks.megaMachinesMax);
    }

    private static final int pollutionPerSecond = 400 * 256;

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return pollutionPerSecond;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEExothermicHearthGui(this);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(Casings.HearthCasing.getTextureId()),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEARTH_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEARTH_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(Casings.HearthCasing.getTextureId()),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEARTH)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_HEARTH_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(Casings.HearthCasing.getTextureId()) };
        }
        return rTexture;
    }

    int casingAmount = 0;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        this.glassTier = -1;
        this.setCoilLevel(HeatingCoilLevel.None);
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            realBudget,
            env,
            false,
            true);
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        this.coilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.heatingCapacity = 0;
        this.glassTier = -1;

        this.setCoilLevel(HeatingCoilLevel.None);

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)) return false;

        if (this.getCoilLevel() == HeatingCoilLevel.None) return false;

        if (this.mMaintenanceHatches.size() != 1) return false;

        if (this.glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
            }
        }
        if (this.glassTier < VoltageIndex.UMV) {
            for (MTEHatch mEnergyHatch : this.mExoticEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    return false;
                }
            }
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    return false;
                }
            }
        }

        this.heatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GTUtility.getTier(getMaxInputVoltage()) - 2);
        return this.casingAmount > 1800;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -2;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MEGA_BLAST_FURNACE_LOOP;
    }
}
