package gregtech.common.tileentities.machines.multi.Solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.getTier;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import goodgenerator.loader.Loaders;
import gregtech.api.casing.Casings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.casing.TTCasingsContainer;

public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable {

    private static final List<CoolingFluid> COOLING_FLUIDS = ImmutableList.of(
        new CoolingFluid(Materials.SuperCoolant, 1, 100),
        new CoolingFluid(MaterialsUEVplus.SpaceTime, 2, 50),
        new CoolingFluid(MaterialsUEVplus.Eternity, 3, 25));

    private CoolingFluid currentCoolingFluid = null;
    private static int horizontalOffset = 7;
    private static int verticalOffset = 43;
    private static int depthOffset = 0;

    public boolean terminalSwitch = false;
    private int mTier = 3; // 1 - base , 2 - ~UEV, 3 - ~UMV
    private final float speedModifierBase = 2.5F;
    private final float euEffBase = 1F;
    private final int parallelScaleBase = 16;
    private final float ocFactorBase = 2.0F;
    private int additionaloverclocks = 0;
    private boolean uevRecipesEnabled = false;
    private boolean hypercoolerPresent = false;

    // modified values for display and calculations
    private float ocFactorAdditive = 0.0F;
    private float speedAdditive = 0.0F;
    private float speedMultiplier = 1.0F;
    private float speedModifierAdj = speedModifierBase;

    private float euEffAdditive = 0.0F;
    private float euEffMultiplier = 1.0F;
    private float euEffAdj = euEffBase;

    private int parallelScaleAdditive = 0;
    private float parallelScaleMultiplier = 1.0F;
    private float parallelScaleAdj = parallelScaleBase;

    // array of ordinals for nbt saving purposes
    public SolidifierModules[] modules = { SolidifierModules.UNSET, SolidifierModules.UNSET, SolidifierModules.UNSET,
        SolidifierModules.UNSET };
    private int[] moduleHorizontalOffsets = { 7, 7, 7, 7 };
    private int[] moduleVerticalOffsets = { 12, 20, 28, 36 };
    private int[] moduleDepthOffsets = { 0, 0, 0, 0 };
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTEModularSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEModularSolidifier>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(
                new String[][]{
                    {"     HHHHH     ","   DDHHHHHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHHHH  HHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHH  HHHHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHHHHHDD   ","     HHHHH     "},
                    {"      HHH      ","       D       ","       D       ","       D       ","       D       ","               ","H             H","HDDDD     DDDDH","H             H","               ","       D       ","       D       ","       D       ","       D       ","      HHH      "},
                    {"     HHHHH     ","   DDHHHHHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHHHH  HHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHH  HHHHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHHHHHDD   ","     HHHHH     "},
                    {"               ","               ","     H   H     ","     HDDDH     ","               ","  HH   B   HH  ","   D   C   D   ","   D BC CB D   ","   D   C   D   ","  HH   B   HH  ","               ","     HDDDH     ","     H   H     ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    H     H    ","   H   D   H   ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","   H   D   H   ","    H     H    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    F     F    ","   H  EFE  H   ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","   H  EFE  H   ","    F     F    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EGE      ","   A E C E A   ","   A GC CG A   ","   A E C E A   ","      EGE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AGA      ","    F  D  F    ","      EGE      ","   A E C E A   ","   GDGC CGDG   ","   A E C E A   ","      EGE      ","    F  D  F    ","      AGA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EGE      ","   A E C E A   ","   A GC CG A   ","   A E C E A   ","      EGE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AGA      ","    F  D  F    ","      EGE      ","   A E C E A   ","   GDGC CGDG   ","   A E C E A   ","      EGE      ","    F  D  F    ","      AGA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EGE      ","   A E C E A   ","   A GC CG A   ","   A E C E A   ","      EGE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AGA      ","    F  D  F    ","      EGE      ","   A E C E A   ","   GDGC CGDG   ","   A E C E A   ","      EGE      ","    F  D  F    ","      AGA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EGE      ","   A E C E A   ","   A GC CG A   ","   A E C E A   ","      EGE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AGA      ","    F  D  F    ","      EGE      ","   A E C E A   ","   GDGC CGDG   ","   A E C E A   ","      EGE      ","    F  D  F    ","      AGA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","       D       ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","       D       ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EFE      ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","      EFE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","      AAA      ","    F     F    ","      EGE      ","   A E C E A   ","   A GC CG A   ","   A E C E A   ","      EGE      ","    F     F    ","      AAA      ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    F     F    ","   H  EFE  H   ","   A E C E A   ","   A FC CF A   ","   A E C E A   ","   H  EFE  H   ","    F     F    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","               ","     HAAAH     ","    H     H    ","   H   D   H   ","   A   C   A   ","   A DC CD A   ","   A   C   A   ","   H   D   H   ","    H     H    ","     HAAAH     ","               ","               ","               "},
                    {"               ","               ","     H   H     ","     HDDDH     ","               ","  HH   B   HH  ","   D   C   D   ","   D BC CB D   ","   D   C   D   ","  HH   B   HH  ","               ","     HDDDH     ","     H   H     ","               ","               "},
                    {"     HHHHH     ","   DDHHHHHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHHHH  HHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHH  HHHHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHHHHHDD   ","     HHHHH     "},
                    {"      H~H      ","       D       ","       D       ","       D       ","       D       ","               ","H             H","HDDDD     DDDDH","H             H","               ","       D       ","       D       ","       D       ","       D       ","      HHH      "},
                    {"     HHHHH     ","   DDHHHHHDD   ","  D  HHHHH  D  "," D    HHH    D "," D    HHH    D ","HHH  HHHHH  HHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHHHHHHHHHHHHHH","HHH  HHHHH  HHH"," D    HHH    D "," D    HHH    D ","  D  HHHHH  D  ","   DDHHHHHDD   ","     HHHHH     "}
                }))
        .addShape(SolidifierModules.EFFICIENT_OC.structureID, transpose(new String[][]{
                {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                {"     onnno     ","   ooo   ooo   ","  oo       oo  "," oo         oo "," o           o ","oo           oo","n             n","n             n","n             n","oo           oo"," o           o "," oo         oo ","  oo       oo  ","   ooo   ooo   ","     onnno     "},
                {"     pmmmp     ","   ppp d ppp   ","  pp   d   pp  "," pp         pp "," p           p ","pp           pp","m             m","mdd         ddm","m             m","pp           pp"," p           p "," pp         pp ","  pp   d   pp  ","   ppp d ppp   ","     pmmmp     "},
                {"     onnno     ","   ooo   ooo   ","  oo       oo  "," oo         oo "," o           o ","oo           oo","n             n","n             n","n             n","oo           oo"," o           o "," oo         oo ","  oo       oo  ","   ooo   ooo   ","     onnno     "},
                {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
                {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
            }
        ))
        .addShape(SolidifierModules.TRANSCENDENT_REINFORCEMENT.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"      fff      ","    DD   DD    ","   D       D   ","  D         D  "," D           D "," D           D ","f             f","f             f","f             f"," D           D "," D           D ","  D         D  ","   D       D   ","    DD   DD    ","      fff      "},
            {"     gghgg     ","   ggg d ggg   ","  gg   d   gg  "," gg         gg "," g           g ","gg           gg","g             g","hdd         ddh","g             g","gg           gg"," g           g "," gg         gg ","  gg   d   gg  ","   ggg d ggg   ","     gghgg     "},
            {"      fff      ","    DD   DD    ","   D       D   ","  D         D  "," D           D "," D           D ","f             f","f             f","f             f"," D           D "," D           D ","  D         D  ","   D       D   ","    DD   DD    ","      fff      "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }
        ))
        .addShape(SolidifierModules.HYPERCOOLER.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"     bbbbb     ","   bcb   bcb   ","  bb       bb  "," bb         bb "," c           c ","bb           bb","b             b","b             b","b             b","bb           bb"," c           c "," bb         bb ","  bb       bb  ","   bcb   bcb   ","     bbbbb     "},
            {"     AbcbA     ","   AAA d AAA   ","  AA   d   AA  "," AA         AA "," A           A ","AA           AA","b             b","cdd         ddc","b             b","AA           AA"," A           A "," AA         AA ","  AA   d   AA  ","   AAA d AAA   ","     AbcbA     "},
            {"     bbbbb     ","   bcb   bcb   ","  bb       bb  "," bb         bb "," c           c ","bb           bb","b             b","b             b","b             b","bb           bb"," c           c "," bb         bb ","  bb       bb  ","   bcb   bcb   ","     bbbbb     "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.ACTIVE_TIME_DILATION_SYSTEM.structureID, transpose(new String[][]{
            {"               ","      K K      ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","      K K      ","               "},
            {"      j j      ","      j j      ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","      j j      ","      j j      "},
            {"     ljijl     ","   lll   lll   ","  ll       ll  "," ll         ll "," l           l ","ll           ll","l             l","l             l","l             l","ll           ll"," l           l "," ll         ll ","  ll       ll  ","   lll   lll   ","     ljijl     "},
            {"      jkj      ","       d       ","       d       ","               ","               ","               ","l             l","kdd         ddk","l             l","               ","               ","               ","       d       ","       d       ","      jkj      "},
            {"     ljijl     ","   lll   lll   ","  ll       ll  "," ll         ll "," l           l ","ll           ll","l             l","l             l","l             l","ll           ll"," l           l "," ll         ll ","  ll       ll  ","   lll   lll   ","     ljijl     "},
            {"      j j      ","      j j      ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","      j j      ","      j j      "},
            {"               ","      K K      ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","      K K      ","               "}
        }))
        .addShape(SolidifierModules.UNSET.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings11, 7))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings5, 12))
        // .addElement('D' ,ofBlock(GregTechAPI.sBlockFrames,81))
        .addElement('D', ofFrame(MaterialsUEVplus.TranscendentMetal))
        .addElement('E', lazy(() -> ofBlock(GodforgeCasings, 0)))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings9, 0)) // temp
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings11, 7)) // temp
        .addElement(
            'H',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .buildAndChain(
                    onElementPass(MTEModularSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 10)))) // placeholder
        .addElement('b', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings12,7 ))
        .addElement('d', lazy(() -> ofBlock(ModBlocks.blockCasingsMisc, 14)))
        .addElement('e', ofBlock(GregTechAPI.sBlockFrames, 581))
        .addElement('f', lazy(() -> ofBlock(GodforgeCasings, 3)))
        .addElement('g', lazy(() -> ofBlock(GodforgeCasings, 5)))
        .addElement('h', lazy(() -> ofBlock(BlockGodforgeGlass.INSTANCE, 0)))
        .addElement('i', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10)))
        .addElement('j', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11)))
        .addElement('k', lazy(() -> ofBlock(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings12,2))
        .addElement('l', ofFrame(Materials.Longasssuperconductornameforuhvwire))// this cant be real
        .addElement('m', lazy(() -> ofBlock(ModBlocks.blockCasings5Misc,13)))
        .addElement('n', lazy(() -> ofBlock(ModBlocks.blockCasings5Misc,9)))
        .addElement('o',lazy(() -> ofBlock(ModBlocks.blockCasings6Misc,0)))
        .addElement('p',lazy(() -> ofBlock(Loaders.compactFusionCoil,4)))
        .build();

    public MTEModularSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEModularSolidifier(String aName) {
        super(aName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mTier = aNBT.getInteger("multiTier");
        modules[0] = SolidifierModules.getModule(aNBT.getInteger("module1OR"));
        modules[1] = SolidifierModules.getModule(aNBT.getInteger("module2OR"));
        modules[2] = SolidifierModules.getModule(aNBT.getInteger("module3OR"));
        modules[3] = SolidifierModules.getModule(aNBT.getInteger("module4OR"));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("multiTier", mTier);
        aNBT.setInteger("module1OR", modules[0].ordinal());
        aNBT.setInteger("module2OR", modules[1].ordinal());
        aNBT.setInteger("module3OR", modules[2].ordinal());
        aNBT.setInteger("module4OR", modules[3].ordinal());
    }

    @Override
    public IStructureDefinition<MTEModularSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModularSolidifier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)) };
        }
        return rTexture;
    }

    /*Colors for stats

    Speed bonus - White
    Parallels - Aqua
    Eu Eff - Red
    OC Factor - Dark Purple
    OC Total - Light Purple
    mTier - Gold
    spaces for module info - 4
     */
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier")
            .addInfo(EnumChatFormatting.WHITE+"150%"+EnumChatFormatting.GRAY+" faster than singleblock machines of the same voltage")
            .addInfo("Gains "+EnumChatFormatting.AQUA+"16"+EnumChatFormatting.GRAY+" parallels per voltage tier")
            .addInfo("Will "+EnumChatFormatting.BOLD+"not"+EnumChatFormatting.GRAY+" perform "+EnumChatFormatting.LIGHT_PURPLE+"overclocks"+EnumChatFormatting.GRAY+" over the hatch tier without modules.")
            .addInfo("Has "+EnumChatFormatting.GOLD+"3 Tiers"+EnumChatFormatting.GRAY+", depending on Central Chassis Casing")
            .addInfo("Has "+EnumChatFormatting.GOLD+"2 - 4"+EnumChatFormatting.GRAY+" Module Slots, based on machine tier")
            .addInfo("Each Module Slot has "+EnumChatFormatting.GOLD+"7"+EnumChatFormatting.GRAY+" different options.")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo("" + EnumChatFormatting.BOLD + EnumChatFormatting.RED + "Glorious Evolution!")

            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoRangeColored("Module-Enabled Foundry Casing",EnumChatFormatting.GRAY,300,500,EnumChatFormatting.GOLD,false)
            .addCasingInfoExactlyColored("Central Magnetic Foundry Chassis", EnumChatFormatting.GRAY, 120, EnumChatFormatting.GOLD,true)
            .addCasingInfoRangeColored("Any Tiered Glass",EnumChatFormatting.GRAY,100,140,EnumChatFormatting.GOLD,true)
            .addCasingInfoExactlyColored("Hypogen Coil", EnumChatFormatting.GRAY, 120, EnumChatFormatting.GOLD,false)
            .addInputBus("Any Foundry Casing", 1)
            .addOutputBus("Any Foundry Casing", 1)
            .addInputHatch("Any Foundry Casing", 1)
            .addEnergyHatch("Any Foundry Casing", 1)
            .addMaintenanceHatch("Any Foundry Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.AQUA+"Hypercooler Module")
            .addStructureInfo("Consumes "+EnumChatFormatting.AQUA+"Cooling Fluid"+EnumChatFormatting.GRAY+" for "+EnumChatFormatting.LIGHT_PURPLE+"Extra Overclocks"+EnumChatFormatting.GRAY+". Limit of "+EnumChatFormatting.WHITE+"1"+EnumChatFormatting.GRAY+" Per "+EnumChatFormatting.GOLD+"Solidifier")
            .addStructureInfo("Drains "+coolingStrOrder("100","50","25")+" L/s of "+coolingStrOrder("Super Coolant","Spacetime","Eternity")+" to gain "+coolingStrOrder("1","2","3") +" Maximum Overclocks")
            // .addStructureInfo("    Fluids: "+coolingStrOrder("Super Coolant","Spacetime","Eternity"))
          //  .addStructureInfo("    L/s: "+coolingStrOrder("100","50","25"))
           // .addStructureInfo("    Overclocks: "+coolingStrOrder("1","2","3"))
            .addStructureInfo(EnumChatFormatting.AQUA+"20x"+EnumChatFormatting.GRAY+" Hypercooler Casing")
            .addStructureInfo(EnumChatFormatting.GOLD+"44x"+EnumChatFormatting.GRAY+" Any Tiered Glass")
            .addStructureInfo(EnumChatFormatting.GOLD+"104x"+EnumChatFormatting.GRAY+" Infinity Cooled Casing")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.LIGHT_PURPLE+"Transcendent Reinforcement")
            .addStructureInfo("Allows for "+EnumChatFormatting.LIGHT_PURPLE+"UEV+ Recipes"+EnumChatFormatting.GRAY+" to be processed")
            .addStructureInfo(EnumChatFormatting.GOLD+"4x"+EnumChatFormatting.GRAY+" Spatially Transcendent Gravitational Lens Block")
            .addStructureInfo(EnumChatFormatting.GOLD+"24x"+EnumChatFormatting.GRAY+" Transcendentally Amplified Magnetic Confinement Casing")
            .addStructureInfo(EnumChatFormatting.GOLD+"52x"+EnumChatFormatting.GRAY+" Remote Graviton Flow Modulator")
            .addStructureInfo(EnumChatFormatting.GOLD+"48x"+EnumChatFormatting.GRAY+" Transcendent Metal Frame Box")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.YELLOW+"Time Dilation System")
            .addStructureInfo("Multiplies "+EnumChatFormatting.WHITE+"Speed"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.WHITE+"6x"+EnumChatFormatting.GRAY+". Multiplies "+EnumChatFormatting.RED+"EU Consumption"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.RED+"6x")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.RED+"Efficient Overclocking Module")
            .addStructureInfo("Adds "+EnumChatFormatting.DARK_PURPLE+"0.2"+EnumChatFormatting.GRAY+" to the "+EnumChatFormatting.DARK_PURPLE+"Overclock Factor")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.DARK_AQUA+"Power Efficient Subsystems")
            .addStructureInfo("Subtracts initial "+EnumChatFormatting.RED+"EU Consumption"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.RED+"25%"+EnumChatFormatting.GRAY+". Multiplies "+EnumChatFormatting.WHITE+"Speed Bonus"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.WHITE+"0.75x")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.GREEN+"Streamlined Casters")
            .addStructureInfo("Multiplies "+EnumChatFormatting.WHITE+"Speed Bonus"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.WHITE+"1.25x"+EnumChatFormatting.GRAY+". Ramps up "+EnumChatFormatting.WHITE+"speed"+EnumChatFormatting.GRAY+" over time to a maximum of "+EnumChatFormatting.WHITE+"2.5x"+EnumChatFormatting.GRAY+", cools down at double that rate.")
            .addStructureInfo("Multiplies "+EnumChatFormatting.AQUA+"Parallels"+EnumChatFormatting.GRAY+" by "+EnumChatFormatting.AQUA+"0.5x")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.WHITE+"Extra Casting Basins")
            .addStructureInfo("Adds "+EnumChatFormatting.AQUA+"12 Parallels"+EnumChatFormatting.GRAY+" per tier. Casings are valid for extra hatch space.")

            .toolTipFinisher();

        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules m = modules[i];
            if (m != SolidifierModules.UNSET) {
                buildPiece(
                    m.structureID,
                    stackSize,
                    hintsOnly,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i]);
            }
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            elementBudget,
            env,
            false,
            true);
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules m = modules[i];
            if (m != SolidifierModules.UNSET) {
                built += survivalBuildPiece(
                    m.structureID,
                    stackSize,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i],
                    elementBudget,
                    env,
                    false,
                    true);
            }
        }

        return built;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mTier = 0;
        // todo: tiered structure 1 - 3
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, depthOffset) && mCasingAmount >= 14) {
            mTier = 3;
            return checkModules();
        }
        /*
         * if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
         * {
         * mTier = 2;
         * return checkModules();
         * }
         * if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
         * {
         * mTier = 1;
         * return checkModules();
         * }
         */
        return false;
    }

    private boolean checkModules() {
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules m = modules[i];
            if (!checkPiece(m.structureID, moduleHorizontalOffsets[i], moduleVerticalOffsets[i], moduleDepthOffsets[i]))
                return false;
        }
        return true;

    }

    public CoolingFluid findCoolingFluid() {
        for (MTEHatchInput hatch : mInputHatches) {
            Optional<CoolingFluid> fluid = COOLING_FLUIDS.stream()
                .filter(candidate -> drain(hatch, candidate.getStack(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
        }
        return null;
    }

    private void resetParameters() {
        ocFactorAdditive = 0.0F;

        speedAdditive = 0.0F;
        speedMultiplier = 1.0F;

        euEffAdditive = 0.0F;
        euEffMultiplier = 1.0F;

        parallelScaleAdditive = 0;
        parallelScaleMultiplier = 1.0F;

        hypercoolerPresent = false;
        uevRecipesEnabled = false;
    }

    public void checkSolidifierModules() {
        resetParameters();
        // loop through each module. based on tier. 2 - 4 modules.
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules checkedModule = modules[i];
            switch (checkedModule) {
                case UNSET:
                    break;
                case HYPERCOOLER:
                    hypercoolerPresent = true;
                    break;
                case TRANSCENDENT_REINFORCEMENT:
                    uevRecipesEnabled = true;
                    break;
                case EFFICIENT_OC:
                    ocFactorAdditive += 0.2F;
                    break;
                case ACTIVE_TIME_DILATION_SYSTEM:
                    euEffMultiplier *= 6;
                    speedMultiplier *= 6;
                    break;
                case STREAMLINED_CASTERS:
                    parallelScaleMultiplier *= 0.5F;
                    speedMultiplier *= 1.5F;
                    break;
                case EXTRA_CASTING_BASINS:
                    parallelScaleAdditive += 12;
                    break;
                case POWER_EFFICIENT_SUBSYSTEMS:
                    euEffAdditive -= 0.2F;
                    speedMultiplier *= (2F / 3F);
                    break;
            }
        }
        calculateNewStats();
    }

    private void calculateNewStats() {
        parallelScaleAdj = (parallelScaleBase + parallelScaleAdditive) * parallelScaleMultiplier;
        speedModifierAdj = (speedModifierBase + speedAdditive) * speedMultiplier;
        euEffAdj = (euEffBase + euEffAdditive) * euEffMultiplier;

    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        checkSolidifierModules();
        logic.setSpeedBonus(1F / speedMultiplier);
        logic.setMaxParallel((int) (Math.floor(parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage())));
        logic.setEuModifier(euEffAdj);
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setMaxTierSkips(3); // capped at 3 for now (current solidifier can do the same)
    }

    @Nonnull
    @Override
    protected CheckRecipeResult checkRecipeForCustomHatches(CheckRecipeResult lastResult) {
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                List<ItemStack> items = hatch.getNonConsumableItems();
                FluidStack fluid = solidifierHatch.getFluid();

                if (items != null && fluid != null) {
                    processingLogic.setInputItems(items);
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        lastResult = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        return lastResult;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                additionaloverclocks = 0;

                if (hypercoolerPresent) {
                    currentCoolingFluid = findCoolingFluid();
                    if (currentCoolingFluid == null) {
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                    additionaloverclocks = currentCoolingFluid.grantedOC;
                }

                if (GTUtility.getTier(recipe.mEUt) >= VoltageIndex.UEV && !uevRecipesEnabled) {
                    return CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxRegularOverclocks(
                        additionaloverclocks + (getTier(getAverageInputVoltage()) - getTier(recipe.mEUt)))
                    .setDurationDecreasePerOC(ocFactorBase + ocFactorAdditive);

            }

            // for cribuffers/proxies?
            @Override
            public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
                if (dualInvWithPatternToRecipeCache.containsKey(inv)) {
                    activeDualInv = inv;
                    return true;
                }

                GTDualInputPattern inputs = inv.getPatternInputs();
                setInputItems(inputs.inputItems);
                setInputFluids(inputs.inputFluid);
                Set<GTRecipe> recipes = findRecipeMatches(RecipeMaps.fluidSolidifierRecipes)
                    .collect(Collectors.toSet());
                if (!recipes.isEmpty()) {
                    dualInvWithPatternToRecipeCache.put(inv, recipes);
                    activeDualInv = inv;
                    return true;
                }
                return false;
            }

        };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    /*
     * things to consider with processing math
     * Things get added and multiplied(parallel,eu/t, speed bonus)
     * Order of operations: ADD/SUB First, MUL/DIV After
     * OC Factor changes (overclock calculator can deal with this)
     * Hypercooler adds OC's based on fluid supplied
     */
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

    // mui2 stuff
    @Override
    public int getMaxParallelRecipes() {
        checkModules();
        return (int) (Math.floor(parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage()));
    }
    @Override
    protected @NotNull MTEModularSolidifierGui getGui() {
        return new MTEModularSolidifierGui(this);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    // getters/setters for mui syncing
    public int getModuleSynced(int index) {
        if (index > SolidifierModules.values().length - 1) index = 0;

        return modules[index].ordinal();
    }

    public void setModule(int index, int ordinal) {
        // just in case, shouldn't be possible
        if (index > modules.length - 1) return;
        SolidifierModules moduleToAdd = SolidifierModules.getModule(ordinal);

        if (moduleToAdd == SolidifierModules.HYPERCOOLER) {
            checkSolidifierModules();
            if (hypercoolerPresent) return;
        }
        modules[index] = moduleToAdd;
    }

    // its not good to run the same method 5 times over per tick when displaying stats, but its fine i guess
    public String getSpeedStr() {
        checkSolidifierModules();
        return (speedModifierAdj - 1) * 100 + "%";
    }

    public String getParallelsString() {
        checkSolidifierModules();
        return (int) parallelScaleAdj + "";
    }

    public String getEuEFFString() {
        checkSolidifierModules();
        return ((int) (euEffAdj * 100)) + "%";
    }

    public String getOCFactorString() {
        checkSolidifierModules();
        return 2 + ocFactorAdditive + " : 4";
    }

    public String coolingStrOrder(String val1, String val2, String val3)
    {
        return EnumChatFormatting.BLUE+val1+"/"+EnumChatFormatting.LIGHT_PURPLE+val2+"/"+EnumChatFormatting.GREEN+val3+EnumChatFormatting.GRAY;
    }

    private static class CoolingFluid {

        public Materials material;
        public int grantedOC;
        public int amount;

        public CoolingFluid(Materials material, int grantedOC, int amount) {
            this.material = material;
            this.grantedOC = grantedOC;
            this.amount = amount;
        }

        public FluidStack getStack() {
            FluidStack stack = material.getFluid(amount);
            // shoutout penguin, i think i get why you were upset with this code here :^)
            if (stack == null) {
                return material.getMolten(amount);
            }
            return stack;
        }
    }
}
