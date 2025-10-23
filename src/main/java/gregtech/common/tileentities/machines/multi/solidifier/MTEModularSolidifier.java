package gregtech.common.tileentities.machines.multi.solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.getTier;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vbo.IModelCustomExt;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasingsFoundry;
import gregtech.common.gui.modularui.multiblock.MTEModularSolidifierGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.casing.TTCasingsContainer;

public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable, IMTERenderer {

    private static final List<CoolingFluid> COOLING_FLUIDS = ImmutableList.of(
        new CoolingFluid(Materials.SuperCoolant, 1, 100),
        new CoolingFluid(Materials.SpaceTime, 2, 50),
        new CoolingFluid(Materials.Eternity, 3, 25));

    private final ArrayList<MTEHatchInput> mCoolantInputHatches = new ArrayList<>();
    private CoolingFluid currentCoolingFluid = null;
    private static final int horizontalOffset = 7;
    private static final int verticalOffset = 43;
    private static final int depthOffset = 0;

    public boolean terminalSwitch = false;
    private int tier = 0; // 1 - UEV , 2 - ~UIV, 3 - ~UXV
    private final float speedModifierBase = 1.5F;
    private final float euEffBase = 1.0F;
    private final int parallelScaleBase = 16;
    private final float ocFactorBase = 2.0F;
    private int additionaloverclocks = 0;
    private boolean uevRecipesEnabled = false;
    private boolean hypercoolerPresent = false;
    private boolean tdsPresent = false;
    private boolean effOCPresent = false;

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
    private final int[] moduleHorizontalOffsets = { 7, 7, 7, 7 };
    private final int[] moduleVerticalOffsets = { 12, 20, 28, 36 };
    private final int[] moduleDepthOffsets = { 0, 0, 0, 0 };
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
        .addShape(SolidifierModules.STREAMLINED_CASTERS.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb","b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb ","  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
            {"     cFcFc     ","   ccc   ccc   ","  cc       cc  "," cc         cc "," c           c ","cc           cc","F             F","c             c","F             F","cc           cc"," c           c "," cc         cc ","  cc       cc  ","   ccc   ccc   ","     cFcFc     "},
            {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb","b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb ","  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.POWER_EFFICIENT_SUBSYSTEMS.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"     gfffg     ","   ddd   ddd   ","  gd       dg  "," dd         dd "," d           d ","gd           dg","f             f","f             f","f             f","gd           dg"," d           d "," dd         dd ","  gd       dg  ","   ddd   ddd   ","     gfffg     "},
            {"     eddde     ","   fee   eef   ","               "," f           f "," e           e ","ee           ee","d             d","d             d","d             d","ee           ee"," e           e "," f           f ","               ","   fee   eef   ","     eddde     "},
            {"     gfffg     ","   ddd   ddd   ","  gd       dg  "," dd         dd "," d           d ","gd           dg","f             f","f             f","f             f","gd           dg"," d           d "," dd         dd ","  gd       dg  ","   ddd   ddd   ","     gfffg     "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.EXTRA_CASTING_BASINS.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"      jjj      ","      hhh      ","               ","               ","               ","               ","jh           hj","jh           hj","jh           hj","               ","               ","               ","               ","      hhh      ","      jjj      "},
            {"     jhmhj     ","   jjhHHHhjj   ","  k         k  "," j           j "," j           j ","jh           hj","hH           Hh","mH           Hm","hH           Hh","jh           hj"," j           j "," j           j ","  k         k  ","   jjhHHHhjj   ","     jhmhj     "},
            {"     mmlmm     ","   iimHHHmii   ","  h         h  "," i           i "," i           i ","mm           mm","mH           Hm","lH           Hl","mH           Hm","mm           mm"," i           i "," i           i ","  h         h  ","   iimHHHmii   ","     mmlmm     "},
            {"     jhmhj     ","   jjhHHHhjj   ","  k         k  "," j           j "," j           j ","jh           hj","hH           Hh","mH           Hm","hH           Hh","jh           hj"," j           j "," j           j ","  k         k  ","   jjhHHHhjj   ","     jhmhj     "},
            {"      jjj      ","      hhh      ","               ","               ","               ","               ","jh           hj","jh           hj","jh           hj","               ","               ","               ","               ","      hhh      ","      jjj      "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.HYPERCOOLER.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"      qoq      ","    qq   qq    ","               ","               "," q           q "," q           q ","q             q","o             o","q             q"," q           q "," q           q ","               ","               ","    qq   qq    ","      qoq      "},
            {"     npspn     ","   pnn   nnp   ","               "," p           p "," n           n ","nn           nn","p             p","s             s","p             p","nn           nn"," n           n "," p           p ","               ","   pnn   nnp   ","     npspn     "},
            {"     ossso     ","   rrr   rrr   ","  o         o  "," r           r "," r           r ","or           ro","s             s","s             s","s             s","or           ro"," r           r "," r           r ","  o         o  ","   rrr   rrr   ","     ossso     "},
            {"     npspn     ","   pnn   nnp   ","               "," p           p "," n           n ","nn           nn","p             p","s             s","p             p","nn           nn"," n           n "," p           p ","               ","   pnn   nnp   ","     npspn     "},
            {"      qoq      ","    qq   qq    ","               ","               "," q           q "," q           q ","q             q","o             o","q             q"," q           q "," q           q ","               ","               ","    qq   qq    ","      qoq      "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.EFFICIENT_OC.structureID, transpose(new String[][]{
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "},
            {"      ttt      ","    tt   tt    ","               ","               "," t           t "," t           t ","t             t","t             t","t             t"," t           t "," t           t ","               ","               ","    tt   tt    ","      ttt      "},
            {"     vFtFv     ","   tvv   vvt   ","  tF       Ft  "," tF         Ft "," v           v ","vv           vv","F             F","t             t","F             F","vv           vv"," v           v "," tF         Ft ","  tF       Ft  ","   tvv   vvt   ","     vFtFv     "},
            {"     utttu     ","   uuu   uuu   ","  uu       uu  "," uu         uu "," u           u ","uu           uu","t             t","t             t","t             t","uu           uu"," u           u "," uu         uu ","  uu       uu  ","   uuu   uuu   ","     utttu     "},
            {"     vFtFv     ","   tvv   vvt   ","  tF       Ft  "," tF         Ft "," v           v ","vv           vv","F             F","t             t","F             F","vv           vv"," v           v "," tF         Ft ","  tF       Ft  ","   tvv   vvt   ","     vFtFv     "},
            {"      ttt      ","    tt   tt    ","               ","               "," t           t "," t           t ","t             t","t             t","t             t"," t           t "," t           t ","               ","               ","    tt   tt    ","      ttt      "},
            {"               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               ","               "}
        }))
        .addShape(SolidifierModules.TRANSCENDENT_REINFORCEMENT.structureID, transpose(new String[][]{
            {"     24442     ","               ","               ","               ","               ","3             3","4             4","4             4","4             4","3             3","               ","               ","               ","               ","     24442     "},
            {"       8       ","   22     22   ","               "," 3           3 "," 3           3 ","               ","               ","8             8","               ","               "," 3           3 "," 3           3 ","               ","   22     22   ","       8       "},
            {"      777      ","    88   88    ","  5         5  ","               "," 8           8 "," 8           8 ","7             7","7             7","7             7"," 8           8 "," 8           8 ","               ","  5         5  ","    88   88    ","      777      "},
            {"     16861     ","   111   111   ","  11       11  "," 11         11 "," 1           1 ","11           11","6             6","8             8","6             6","11           11"," 1           1 "," 11         11 ","  11       11  ","   111   111   ","     16861     "},
            {"      777      ","    88   88    ","  5         5  ","               "," 8           8 "," 8           8 ","7             7","7             7","7             7"," 8           8 "," 8           8 ","               ","  5         5  ","    88   88    ","      777      "},
            {"       8       ","   33     33   ","               "," 2           2 "," 2           2 ","               ","               ","8             8","               ","               "," 2           2 "," 2           2 ","               ","   33     33   ","       8       "},
            {"     34443     ","               ","               ","               ","               ","2             2","4             4","4             4","4             4","2             2","               ","               ","               ","               ","     34443     "}
        }))
        .addShape(SolidifierModules.ACTIVE_TIME_DILATION_SYSTEM.structureID, transpose(new String[][]{
            {"               ","       #       ","               ","               ","               ","               ","               "," #           # ","               ","               ","               ","               ","               ","       #       ","               "},
            {"     &@ @^     ","    && ! ^^    ","               ","               "," ^           & ","^^           &&","@             @"," !           ! ","@             @","&&           ^^"," &           ^ ","               ","               ","    ^^ ! &&    ","     ^@ @&     "},
            {"      @!@      ","   &**   XX^   ","   &       ^   "," ^^         && "," X           * "," X           * ","@             @","!             !","@             @"," *           X "," *           X "," &&         ^^ ","   ^       &   ","   ^XX   **&   ","      @!@      "},
            {"      !Z!      ","    $$   $$    ","  %         %  ","               "," $           $ "," $           $ ","!             !","Z             Z","!             !"," $           $ "," $           $ ","               ","  %         %  ","    $$   $$    ","      !Z!      "},
            {"      @!@      ","   ^XX   **&   ","   ^       &   "," &&         ^^ "," *           X "," *           X ","@             @","!             !","@             @"," X           * "," X           * "," ^^         && ","   &       ^   ","   &**   XX^   ","      @!@      "},
            {"     ^@ @&     ","    ^^ ! &&    ","               ","               "," &           ^ ","&&           ^^","@             @"," !           ! ","@             @","^^           &&"," ^           & ","               ","               ","    && ! ^^    ","     &@ @^     "},
            {"               ","       #       ","               ","               ","               ","               ","               "," #           # ","               ","               ","               ","               ","               ","       #       ","               "}
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
        .addElement('D', ofFrame(Materials.TranscendentMetal))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasingsFoundry, 11))
        .addElement(
            'F',
            GTStructureChannels.MAGNETIC_CHASSIS.use(
                ofBlocksTiered(
                    MTEModularSolidifier::getTierFromMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 1),
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 2),
                        Pair.of(GregTechAPI.sBlockCasingsFoundry, 3)),
                    -1,
                    MTEModularSolidifier::setMachineTier,
                    MTEModularSolidifier::getMachineTier)))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings11, 7)) // item pipe casing
        .addElement(
            'H',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputHatch, OutputBus, InputBus, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasingsFoundry) GregTechAPI.sBlockCasingsFoundry).getTextureIndex(0))
                .buildAndChain(
                    onElementPass(MTEModularSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasingsFoundry, 0)))) // placeholder
        .addElement('a', ofBlock(GregTechAPI.sBlockCasingsFoundry, 0)) // base casing, no adder
        // streamlined casters
        .addElement('b', ofBlock(GregTechAPI.sBlockCasingsFoundry, 10)) // Streamlined Caster
        .addElement('c', ofFrame(Materials.Enderium))
        // power efficient subsystems
        .addElement('d', ofBlock(GregTechAPI.sBlockCasingsFoundry, 6)) // power eff subsystems
        .addElement('e', ofFrame(Materials.SamariumMagnetic))
        .addElement('f', ofFrame(Materials.TengamAttuned))
        .addElement('g', lazy(() -> ofBlock(ModBlocks.blockCustomMachineCasings, 3))) // TODO: replace with MEBF/green
                                                                                      // casing after rework
        // casting basins
        .addElement('h', ofBlock(GregTechAPI.sBlockCasings10, 13)) // shaper casing
        .addElement('i', ofBlock(GregTechAPI.sBlockCasings10, 14)) // shaper radiator
        .addElement('j', ofBlock(GregTechAPI.sBlockCasingsFoundry, 8)) // extra casting basings
        .addElement('k', ofFrame(Materials.Erbium))
        .addElement(
            'l',
            lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, GGMaterial.preciousMetalAlloy.getmID())))
        .addElement('m', lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasings, GGMaterial.preciousMetalAlloy.getmID())))
        // hypercooler
        .addElement('n', ofBlock(GregTechAPI.sBlockCasings2, 1))
        .addElement('o', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .addElement(
            'p',
            buildHatchAdder(MTEModularSolidifier.class).hatchClass(MTEHatchInput.class)
                .adder(MTEModularSolidifier::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 9))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasingsFoundry, 9))
        .addElement('q', ofFrame(Materials.Netherite))
        .addElement('r', ofBlock(GregTechAPI.sBlockGlass1, 3))
        .addElement('s', lazy(() -> ofBlock(ModBlocks.blockCasings3Misc, 10))) // TODO: replace with MVF casing after
                                                                               // rework
        // efficient overclock
        .addElement('t', ofBlock(GregTechAPI.sBlockCasingsFoundry, 5))
        .addElement('u', ofBlock(GregTechAPI.sBlockGlass1, 4))
        .addElement('v', lazy(() -> ofBlock(ModBlocks.blockCasings6Misc, 0)))
        // transcendent reinforcement
        .addElement('1', ofBlock(GregTechAPI.sBlockCasingsFoundry, 7))
        .addElement('2', ofFrame(Materials.Creon))
        .addElement('3', ofFrame(Materials.Mellion))
        .addElement('4', ofFrame(Materials.TranscendentMetal))
        .addElement('5', ofFrame(Materials.SpaceTime))
        .addElement('6', ofBlock(GregTechAPI.sBlockMetal9, 3))
        .addElement('7', lazy(() -> ofBlock(GodforgeCasings, 3)))
        .addElement('8', lazy(() -> ofBlock(BlockGodforgeGlass.INSTANCE, 0)))
        // Time Dilation System
        .addElement('!', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10)))
        .addElement('@', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11)))
        .addElement('#', ofBlock(GregTechAPI.sBlockCasingsFoundry, 4))
        .addElement('$', ofFrame(Materials.Universium))
        .addElement('%', ofFrame(Materials.Eternity))
        .addElement('^', ofFrame(Materials.WhiteDwarfMatter))
        .addElement('&', ofFrame(Materials.BlackDwarfMatter))
        .addElement('*', ofBlock(GregTechAPI.sBlockMetal9, 6))
        .addElement('X', ofBlock(GregTechAPI.sBlockMetal9, 7))
        .addElement('Z', lazy(() -> ofBlock(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)))
        .build();

    /*
     * private static final IStructureDefinition<MTEModularSolidifier> STRUCTURE_DEFINITION_temp = StructureDefinition
     * .<MTEModularSolidifier>builder()
     * .addShape(SolidifierModules.STREAMLINED_CASTERS.structureID, transpose(new String[][]{
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb"
     * ,"b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb "
     * ,"  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
     * {"     cFcFc     ","   ccc   ccc   ","  cc       cc  "," cc         cc "," c           c ","cc           cc"
     * ,"F             F","c             c","F             F","cc           cc"," c           c "," cc         cc "
     * ,"  cc       cc  ","   ccc   ccc   ","     cFcFc     "},
     * {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb"
     * ,"b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb "
     * ,"  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "}
     * }))
     * .addShape(SolidifierModules.POWER_EFFICIENT_SUBSYSTEMS.structureID, transpose(new String[][]{
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"     gfffg     ","   ddd   ddd   ","  gd       dg  "," dd         dd "," d           d ","gd           dg"
     * ,"f             f","f             f","f             f","gd           dg"," d           d "," dd         dd "
     * ,"  gd       dg  ","   ddd   ddd   ","     gfffg     "},
     * {"     eddde     ","   fee   eef   ","               "," f           f "," e           e ","ee           ee"
     * ,"d             d","d             d","d             d","ee           ee"," e           e "," f           f "
     * ,"               ","   fee   eef   ","     eddde     "},
     * {"     gfffg     ","   ddd   ddd   ","  gd       dg  "," dd         dd "," d           d ","gd           dg"
     * ,"f             f","f             f","f             f","gd           dg"," d           d "," dd         dd "
     * ,"  gd       dg  ","   ddd   ddd   ","     gfffg     "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "}
     * }))
     * .addShape(SolidifierModules.EXTRA_CASTING_BASINS.structureID, transpose(new String[][]{
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"      jjj      ","      hhh      ","               ","               ","               ","               "
     * ,"jh           hj","jh           hj","jh           hj","               ","               ","               "
     * ,"               ","      hhh      ","      jjj      "},
     * {"     jhmhj     ","   jjhHHHhjj   ","  k         k  "," j           j "," j           j ","jh           hj"
     * ,"hH           Hh","mH           Hm","hH           Hh","jh           hj"," j           j "," j           j "
     * ,"  k         k  ","   jjhHHHhjj   ","     jhmhj     "},
     * {"     mmlmm     ","   iimHHHmii   ","  h         h  "," i           i "," i           i ","mm           mm"
     * ,"mH           Hm","lH           Hl","mH           Hm","mm           mm"," i           i "," i           i "
     * ,"  h         h  ","   iimHHHmii   ","     mmlmm     "},
     * {"     jhmhj     ","   jjhHHHhjj   ","  k         k  "," j           j "," j           j ","jh           hj"
     * ,"hH           Hh","mH           Hm","hH           Hh","jh           hj"," j           j "," j           j "
     * ,"  k         k  ","   jjhHHHhjj   ","     jhmhj     "},
     * {"      jjj      ","      hhh      ","               ","               ","               ","               "
     * ,"jh           hj","jh           hj","jh           hj","               ","               ","               "
     * ,"               ","      hhh      ","      jjj      "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "}
     * }))
     * .addShape(SolidifierModules.HYPERCOOLER.structureID, transpose(new String[][]{
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb"
     * ,"b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb "
     * ,"  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
     * {"     cFcFc     ","   ccc   ccc   ","  cc       cc  "," cc         cc "," c           c ","cc           cc"
     * ,"F             F","c             c","F             F","cc           cc"," c           c "," cc         cc "
     * ,"  cc       cc  ","   ccc   ccc   ","     cFcFc     "},
     * {"     bbFbb     ","   bbb   bbb   ","  bF       Fb  "," bF         Fb "," b           b ","bb           bb"
     * ,"b             b","F             F","b             b","bb           bb"," b           b "," bF         Fb "
     * ,"  bF       Fb  ","   bbb   bbb   ","     bbFbb     "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "}
     * }))
     * .addShape(SolidifierModules.EFFICIENT_OC.structureID, transpose(new String[][]{
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "},
     * {"      ttt      ","    tt   tt    ","               ","               "," t           t "," t           t "
     * ,"t             t","t             t","t             t"," t           t "," t           t ","               "
     * ,"               ","    tt   tt    ","      ttt      "},
     * {"     vFtFv     ","   tvv   vvt   ","  tF       Ft  "," tF         Ft "," v           v ","vv           vv"
     * ,"F             F","t             t","F             F","vv           vv"," v           v "," tF         Ft "
     * ,"  tF       Ft  ","   tvv   vvt   ","     vFtFv     "},
     * {"     utttu     ","   uuu   uuu   ","  uu       uu  "," uu         uu "," u           u ","uu           uu"
     * ,"t             t","t             t","t             t","uu           uu"," u           u "," uu         uu "
     * ,"  uu       uu  ","   uuu   uuu   ","     utttu     "},
     * {"     vFtFv     ","   tvv   vvt   ","  tF       Ft  "," tF         Ft "," v           v ","vv           vv"
     * ,"F             F","t             t","F             F","vv           vv"," v           v "," tF         Ft "
     * ,"  tF       Ft  ","   tvv   vvt   ","     vFtFv     "},
     * {"      ttt      ","    tt   tt    ","               ","               "," t           t "," t           t "
     * ,"t             t","t             t","t             t"," t           t "," t           t ","               "
     * ,"               ","    tt   tt    ","      ttt      "},
     * {"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               ","               ","               ","               "
     * ,"               ","               ","               "}
     * }))
     * .addShape(SolidifierModules.TRANSCENDENT_REINFORCEMENT.structureID, transpose(new String[][]{
     * {"     24442     ","               ","               ","               ","               ","3             3"
     * ,"3             4","3             4","3             4","3             3","               ","               "
     * ,"               ","               ","     24442     "},
     * {"       8       ","   22     22   ","               "," 3           3 "," 3           3 ","               "
     * ,"               ","8             8","               ","               "," 3           3 "," 3           3 "
     * ,"               ","   22     22   ","       8       "},
     * {"      777      ","    88   88    ","  5         5  ","               "," 8           8 "," 8           8 "
     * ,"7             7","7             7","7             7"," 8           8 "," 8           8 ","               "
     * ,"  5         5  ","    88   88    ","      777      "},
     * {"     16861     ","   111   111   ","  11       11  "," 11         11 "," 1           1 ","11           11"
     * ,"6             6","8             8","6             6","11           11"," 1           1 "," 11         11 "
     * ,"  11       11  ","   111   111   ","     16861     "},
     * {"      777      ","    88   88    ","  5         5  ","               "," 8           8 "," 8           8 "
     * ,"7             7","7             7","7             7"," 8           8 "," 8           8 ","               "
     * ,"  5         5  ","    88   88    ","      777      "},
     * {"       8       ","   33     33   ","               "," 2           2 "," 2           2 ","               "
     * ,"               ","8             8","               ","               "," 2           2 "," 2           2 "
     * ,"               ","   33     33   ","       8       "},
     * {"     34443     ","               ","               ","               ","               ","2             2"
     * ,"4             4","4             4","4             4","2             2","               ","               "
     * ,"               ","               ","     34443     "}
     * }))
     * .addShape(SolidifierModules.ACTIVE_TIME_DILATION_SYSTEM.structureID, transpose(new String[][]{
     * {"     @$$$@     ","               ","               ","               ","               ","#             #"
     * ,"#             $","#             $","#             $","#             #","               ","               "
     * ,"               ","               ","     @$$$@     "},
     * {"       *       ","   @@     @@   ","               "," #           # "," #           # ","               "
     * ,"               ","*             *","               ","               "," #           # "," #           # "
     * ,"               ","   @@     @@   ","       *       "},
     * {"      &&&      ","    **   **    ","  %         %  ","               "," *           * "," *           * "
     * ,"&             &","&             &","&             &"," *           * "," *           * ","               "
     * ,"  %         %  ","    **   **    ","      &&&      "},
     * {"     !^*^!     ","   !!!   !!!   ","  !!       !!  "," !!         !! "," !           ! ","!!           !!"
     * ,"^             ^","*             *","^             ^","!!           !!"," !           ! "," !!         !! "
     * ,"  !!       !!  ","   !!!   !!!   ","     !^*^!     "},
     * {"      &&&      ","    **   **    ","  %         %  ","               "," *           * "," *           * "
     * ,"&             &","&             &","&             &"," *           * "," *           * ","               "
     * ,"  %         %  ","    **   **    ","      &&&      "},
     * {"       *       ","   ##     ##   ","               "," @           @ "," @           @ ","               "
     * ,"               ","*             *","               ","               "," @           @ "," @           @ "
     * ,"               ","   ##     ##   ","       *       "},
     * {"     #$$$#     ","               ","               ","               ","               ","@             @"
     * ,"$             $","$             $","$             $","@             @","               ","               "
     * ,"               ","               ","     #$$$#     "}
     * }))
     * .addElement('a', ofBlock(GregTechAPI.sBlockCasingsFoundry,0)) // base casing, no adder
     * // streamlined casters
     * .addElement('b', ofBlock(GregTechAPI.sBlockCasingsFoundry,10)) // Streamlined Caster
     * .addElement('c', ofFrame(Materials.Enderium))
     * // power efficient subsystems
     * .addElement('d', ofBlock(GregTechAPI.sBlockCasingsFoundry,6)) // power eff subsystems
     * .addElement('e', ofFrame(Materials.SamariumMagnetic))
     * .addElement('f', ofFrame(Materials.TengamAttuned))
     * .addElement('g',lazy(()-> ofBlock( ModBlocks.blockCustomMachineCasings,3))) //TODO: replace with MEBF/green
     * casing after rework
     * // casting basins
     * .addElement('h',ofBlock(GregTechAPI.sBlockCasings10,13)) // shaper casing
     * .addElement('i', ofBlock(GregTechAPI.sBlockCasings10,14)) // shaper radiator
     * .addElement('j', ofBlock(GregTechAPI.sBlockCasingsFoundry, 8 )) // extra casting basings
     * .addElement('k', ofFrame(Materials.Erbium))
     * .addElement(
     * 'l', lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, GGMaterial.preciousMetalAlloy.getmID())))
     * .addElement(
     * 'm', lazy(() -> ofBlock(WerkstoffLoader.BWBlockCasings, GGMaterial.preciousMetalAlloy.getmID())))
     * // hypercooler
     * .addElement('n', ofBlock(GregTechAPI.sBlockCasings2,1))
     * .addElement('o', ofBlock(GregTechAPI.sBlockCasings8, 14))
     * .addElement(
     * 'p',
     * buildHatchAdder(MTEModularSolidifier.class).hatchClass(MTEHatchInput.class)
     * .adder(MTEModularSolidifier::addCoolantInputToMachineList)
     * .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 9))
     * .dot(2)
     * .buildAndChain(GregTechAPI.sBlockCasingsFoundry, 9))
     * .addElement('q', ofFrame(Materials.Netherite))
     * .addElement('r', ofBlock(GregTechAPI.sBlockGlass1,3))
     * .addElement('s', lazy(() ->ofBlock(ModBlocks.blockCasings3Misc, 10))) //TODO: replace with MVF casing after
     * rework
     * // efficient overclock
     * .addElement('t', ofBlock(GregTechAPI.sBlockCasingsFoundry,5))
     * .addElement('u', ofBlock(GregTechAPI.sBlockGlass1,4))
     * .addElement('v',lazy(() -> ofBlock(ModBlocks.blockCasings6Misc,0)))
     * // transcendent reinforcement
     * .addElement('1', ofBlock(GregTechAPI.sBlockCasingsFoundry,7))
     * .addElement('2', ofFrame(Materials.Creon))
     * .addElement('3', ofFrame(Materials.Mellion))
     * .addElement('4', ofFrame(Materials.TranscendentMetal))
     * .addElement('5', ofFrame(Materials.SpaceTime))
     * .addElement('6', ofBlock(GregTechAPI.sBlockMetal9,3))
     * .addElement('7', lazy(() -> ofBlock(GodforgeCasings,3)))
     * .addElement('8', lazy(()-> ofBlock(BlockGodforgeGlass.INSTANCE, 0)))
     * // Time Dilation System
     * .addElement('!', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0,10)))
     * .addElement('@', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsBA0,11)))
     * .addElement('#', ofBlock(GregTechAPI.sBlockCasingsFoundry,4))
     * .addElement('$', ofFrame(Materials.WhiteDwarfMatter))
     * .addElement('%', ofFrame(Materials.BlackDwarfMatter))
     * .addElement('^', ofFrame(Materials.Universium))
     * .addElement('&', ofFrame(Materials.Eternity))
     * .addElement('*', ofBlock(GregTechAPI.sBlockMetal9,6))
     * .addElement('_', ofBlock(GregTechAPI.sBlockMetal9,7))
     * .addElement('+', lazy(() -> ofBlock(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)))
     * .build();
     */

    public MTEModularSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEModularSolidifier(String aName) {
        super(aName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tier = aNBT.getInteger("multiTier");
        modules[0] = SolidifierModules.getModule(aNBT.getInteger("module1OR"));
        modules[1] = SolidifierModules.getModule(aNBT.getInteger("module2OR"));
        modules[2] = SolidifierModules.getModule(aNBT.getInteger("module3OR"));
        modules[3] = SolidifierModules.getModule(aNBT.getInteger("module4OR"));
        shouldRender = aNBT.getBoolean("shouldRender");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("multiTier", tier);
        aNBT.setInteger("module1OR", modules[0].ordinal());
        aNBT.setInteger("module2OR", modules[1].ordinal());
        aNBT.setInteger("module3OR", modules[2].ordinal());
        aNBT.setInteger("module4OR", modules[3].ordinal());
        aNBT.setBoolean("shouldRender", shouldRender);
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)),
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)),
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
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsFoundry, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier")
            .addBulkMachineInfo(parallelScaleBase, speedModifierBase, euEffBase)
            .addInfo(
                "Will " + EnumChatFormatting.BOLD
                    + "not"
                    + EnumChatFormatting.GRAY
                    + " perform "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "overclocks"
                    + EnumChatFormatting.GRAY
                    + " over the hatch tier without modules")
            .addInfo(
                "Has " + EnumChatFormatting.GOLD
                    + "3 Tiers"
                    + EnumChatFormatting.GRAY
                    + ", depending on Central Chassis Casing")
            .addInfo(
                "Has " + EnumChatFormatting.GOLD
                    + "2 - 4"
                    + EnumChatFormatting.GRAY
                    + " Module Slots, based on machine tier")
            .addInfo(
                "Each Module Slot has " + EnumChatFormatting.GOLD
                    + "7"
                    + EnumChatFormatting.GRAY
                    + " different options")
            .addInfo("Toggle Render with Screwdriver")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo("" + EnumChatFormatting.BOLD + EnumChatFormatting.RED + "Glorious Evolution!")

            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoRangeColored(
                "Module-Enabled Foundry Casing",
                EnumChatFormatting.GRAY,
                300,
                500,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Central Magnetic Foundry Chassis",
                EnumChatFormatting.GRAY,
                120,
                EnumChatFormatting.GOLD,
                true)
            .addCasingInfoRangeColored(
                "Any Tiered Glass",
                EnumChatFormatting.GRAY,
                100,
                140,
                EnumChatFormatting.GOLD,
                true)
            .addCasingInfoExactlyColored("Hypogen Coil", EnumChatFormatting.GRAY, 120, EnumChatFormatting.GOLD, false)
            .addInputBus("Any Foundry Casing", 1)
            .addOutputBus("Any Foundry Casing", 1)
            .addInputHatch("Any Foundry Casing", 1)
            .addEnergyHatch("Any Foundry Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.AQUA + "Hypercooler Module")
            .addStructureInfo(
                "Consumes " + EnumChatFormatting.AQUA
                    + "Cooling Fluid"
                    + EnumChatFormatting.GRAY
                    + " for "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Extra Overclocks"
                    + EnumChatFormatting.GRAY
                    + ". Limit of "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Per "
                    + EnumChatFormatting.GOLD
                    + "Solidifier")
            .addStructureInfo(
                "Drains " + coolingStrOrder("100", "50", "25")
                    + " L/s of "
                    + coolingStrOrder("Super Coolant", "Spacetime", "Eternity")
                    + " to gain "
                    + coolingStrOrder("1", "2", "3")
                    + " Maximum Overclocks")
            .addStructureInfo(EnumChatFormatting.AQUA + "20x" + EnumChatFormatting.GRAY + " Hypercooler Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "44x" + EnumChatFormatting.GRAY + " Any Tiered Glass")
            .addStructureInfo(EnumChatFormatting.GOLD + "104x" + EnumChatFormatting.GRAY + " Infinity Cooled Casing")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.LIGHT_PURPLE + "Transcendent Reinforcement")
            .addStructureInfo(
                "Allows for " + EnumChatFormatting.LIGHT_PURPLE
                    + "UEV+ Recipes"
                    + EnumChatFormatting.GRAY
                    + " to be processed")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "4x"
                    + EnumChatFormatting.GRAY
                    + " Spatially Transcendent Gravitational Lens Block")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "24x"
                    + EnumChatFormatting.GRAY
                    + " Transcendentally Amplified Magnetic Confinement Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "52x" + EnumChatFormatting.GRAY + " Remote Graviton Flow Modulator")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "48x" + EnumChatFormatting.GRAY + " Transcendent Metal Frame Box")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.YELLOW + "Time Dilation System")
            .addStructureInfo(
                "Multiplies Speed and EU Consumption by " + EnumChatFormatting.GREEN
                    + "6x"
                    + EnumChatFormatting.GRAY
                    + ". Limit of "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Per "
                    + EnumChatFormatting.GOLD
                    + "Solidifier")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.RED + "Efficient Overclocking Module")
            .addStructureInfo(
                "Adds " + EnumChatFormatting.DARK_PURPLE
                    + "0.2"
                    + EnumChatFormatting.GRAY
                    + " to the "
                    + EnumChatFormatting.DARK_PURPLE
                    + "Overclock Factor"
                    + EnumChatFormatting.GRAY
                    + ". Limit of "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Per "
                    + EnumChatFormatting.GOLD
                    + "Solidifier")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.DARK_AQUA + "Power Efficient Subsystems")
            .addStructureInfo(
                "Subtracts initial EU Consumption by " + EnumChatFormatting.AQUA
                    + "20%"
                    + EnumChatFormatting.GRAY
                    + ", multiplies "
                    + EnumChatFormatting.GREEN
                    + "Speed"
                    + EnumChatFormatting.GRAY
                    + " by "
                    + EnumChatFormatting.WHITE
                    + "0.75x")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.GREEN + "Streamlined Casters")
            .addStructureInfo(
                "Multiplies Speed" + EnumChatFormatting.GRAY
                    + " by "
                    + EnumChatFormatting.GREEN
                    + "1.25x"
                    + EnumChatFormatting.GRAY
                    + ". Ramps up speed over time to a maximum of "
                    + EnumChatFormatting.GREEN
                    + "2.5x"
                    + EnumChatFormatting.GRAY
                    + ", cools down at double that rate")
            .addStructureInfo("Multiplies Parallels by " + EnumChatFormatting.GOLD + "0.5x")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.BLUE + "Extra Casting Basins")
            .addStructureInfo(
                "Adds " + EnumChatFormatting.GOLD
                    + "12"
                    + EnumChatFormatting.GRAY
                    + " Parallels per "
                    + TooltipTier.VOLTAGE.getValue()
                    + " tier. Casings are valid for extra hatch space")

            .toolTipFinisher();

        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        for (int i = 0; i < 2 + (tier - 1); i++) {
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
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        int built = 0;
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffset,
            verticalOffset,
            depthOffset,
            realBudget,
            env,
            false,
            true);
        for (int i = 0; i < 2 + (tier - 1); i++) {
            SolidifierModules m = modules[i];
            if (m != SolidifierModules.UNSET) {
                built += survivalBuildPiece(
                    m.structureID,
                    stackSize,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i],
                    realBudget,
                    env,
                    false,
                    true);
            }
        }

        return built;
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    private void setMachineTier(int tier) {
        this.tier = tier;
    }

    private int getMachineTier() {
        return tier;
    }

    @Nullable
    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasingsFoundry) return null;
        if (metaID < 1 || metaID > 3) return null;
        return metaID;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        tier = -1;
        mCoolantInputHatches.clear();
        // limit hatch space to about 25 hatches without modules. T.D.S removes 12 for balance, and casters adds 36.
        if (checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, depthOffset)
            && casingAmount >= 500 + (tdsPresent ? 12 : 0)) {
            return checkModules();
        }
        return false;
    }

    private boolean checkModules() {
        for (int i = 0; i < 2 + (tier - 1); i++) {
            SolidifierModules m = modules[i];
            if (!checkPiece(
                m.structureID,
                moduleHorizontalOffsets[i],
                moduleVerticalOffsets[i],
                moduleDepthOffsets[i])) {
                return false;
            }
            if (m == SolidifierModules.HYPERCOOLER && mCoolantInputHatches.size() != 1) return false;
        }
        return true;

    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mCoolantInputHatches.add((MTEHatchInput) aMetaTileEntity);
            return true;
        }
        return false;
    }

    public CoolingFluid findCoolingFluid() {
        for (MTEHatchInput hatch : mCoolantInputHatches) {
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
        tdsPresent = false;
        effOCPresent = false;
    }

    public void checkSolidifierModules() {
        resetParameters();
        // loop through each module. based on tier. 2 - 4 modules.
        for (int i = 0; i < 2 + (tier - 1); i++) {
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
                    effOCPresent = true;
                    ocFactorAdditive += 0.35F;
                    break;
                case ACTIVE_TIME_DILATION_SYSTEM:
                    if (tdsPresent) break;
                    tdsPresent = true;
                    euEffMultiplier *= 8;
                    speedMultiplier *= 6;
                    break;
                case STREAMLINED_CASTERS:
                    speedAdditive += 1.5F;
                    parallelScaleMultiplier *= 0.9f;
                    break;
                case EXTRA_CASTING_BASINS:
                    parallelScaleAdditive += 12;
                    break;
                case POWER_EFFICIENT_SUBSYSTEMS:
                    euEffAdditive -= 0.1f;
                    euEffMultiplier *= 0.8f;
                    speedMultiplier *= 0.95f;
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
        logic.setSpeedBonus(1F / speedModifierAdj);
        logic.setMaxParallel((int) (Math.floor(parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage())));
        logic.setEuModifier(euEffAdj);
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
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
                    .setMaxOverclocks(additionaloverclocks + (getTier(getAverageInputVoltage()) - getTier(recipe.mEUt)))
                    .setDurationDecreasePerOC(ocFactorBase + ocFactorAdditive);

            }

        };
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        if (mMaxProgresstime > 0 && aTick % 20 == 0 && hypercoolerPresent) {
            if (this.currentCoolingFluid != null) {
                FluidStack fluid = this.currentCoolingFluid.getStack();
                for (MTEHatchInput hatch : mCoolantInputHatches) {
                    if (drain(hatch, fluid, false)) {
                        drain(hatch, fluid, true);
                        return;
                    }
                }
                stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
            }
        }
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
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    // GUI code
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
    protected boolean forceUseMui2() {
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
        if (moduleToAdd == SolidifierModules.ACTIVE_TIME_DILATION_SYSTEM) {
            checkSolidifierModules();
            if (tdsPresent) return;
        }
        if (moduleToAdd == SolidifierModules.EFFICIENT_OC) {
            checkSolidifierModules();
            if (effOCPresent) return;
        }
        if (modules[index] == moduleToAdd) return;

        modules[index] = moduleToAdd;
    }

    public String getSpeedStr() {
        checkSolidifierModules();
        return (speedModifierAdj) * 100 + "%";
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

    public String coolingStrOrder(String val1, String val2, String val3) {
        return EnumChatFormatting.BLUE + val1
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + val2
            + "/"
            + EnumChatFormatting.GREEN
            + val3
            + EnumChatFormatting.GRAY;
    }

    // Render code
    private boolean shouldRender = true;
    private boolean renderInitialized;
    private static ResourceLocation ringTexture;
    private static IModelCustomExt ring;
    private static ShaderProgram ringProgram;
    private int uGlowColor;

    // TODO: figure out why isActive doesnt send to client by default???
    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        /*
         * if (!shouldRender || !getBaseMetaTileEntity().isActive()) return;
         * if (!renderInitialized) {
         * initializeRender();
         * if (!renderInitialized) return;
         * }
         * final TextureManager textureManager = Minecraft.getMinecraft()
         * .getTextureManager();
         * textureManager.bindTexture(ringTexture);
         * renderRingOne(x, y, z, timeSinceLastTick, modules[0].rgbArr);
         * renderRingTwo(x, y, z, timeSinceLastTick, modules[1].rgbArr);
         * renderRingThree(x, y, z, timeSinceLastTick, modules[2].rgbArr);
         * renderRingFour(x, y, z, timeSinceLastTick, modules[3].rgbArr);
         */
    }

    private void initializeRender() {
        // spotless:off
        ring = (IModelCustomExt) AdvancedModelLoader.loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-one.obj"));
        ringTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/RING.png");

        try{
            ringProgram = new ShaderProgram(GregTech.resourceDomain, "shaders/foundry.vert.glsl","shaders/foundry.frag.glsl");
            uGlowColor = ringProgram.getUniformLocation("u_Color");
        } catch (Exception e) {
            GTMod.GT_FML_LOGGER.error(e.getMessage());
            return;
        }
        renderInitialized = true;
        // spotless:on
    }

    private void renderRingOne(double x, double y, double z, float timer, float[] rgba) {
        ringProgram.use();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 9.5f, z + 7.5F);
        GL11.glScalef(1.2f, 0.6f, 1.2f);
        GL20.glUniform4f(uGlowColor, rgba[0], rgba[1], rgba[2], rgba[3]);
        ring.renderAllVBO();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();
    }

    private void renderRingTwo(double x, double y, double z, float timer, float[] rgba) {
        ringProgram.use();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 17.5f, z + 7.5F);
        GL11.glScalef(1.2f, 0.6f, 1.2f);
        GL20.glUniform4f(uGlowColor, rgba[0], rgba[1], rgba[2], rgba[3]);
        ring.renderAllVBO();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();

    }

    private void renderRingThree(double x, double y, double z, float timer, float[] rgba) {
        ringProgram.use();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 25.5f, z + 7.5F);
        GL11.glScalef(1.2f, 0.6f, 1.2f);
        GL20.glUniform4f(uGlowColor, rgba[0], rgba[1], rgba[2], rgba[3]);
        ring.renderAllVBO();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();

    }

    private void renderRingFour(double x, double y, double z, float timer, float[] rgba) {
        ringProgram.use();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5f, y + 33.5f, z + 7.5F);
        GL11.glScalef(1.2f, 0.6f, 1.2f);
        GL20.glUniform4f(uGlowColor, rgba[0], rgba[1], rgba[2], rgba[3]);
        ring.renderAllVBO();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        ShaderProgram.clear();
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        shouldRender = !shouldRender;
        getBaseMetaTileEntity().issueTileUpdate();
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.animations." + (shouldRender ? "enabled" : "disabled")));

    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("shouldRender", shouldRender);
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        if (data.hasKey("shouldRender")) shouldRender = data.getBoolean("shouldRender");
    }

    // data class
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
            if (stack == null) {
                return material.getMolten(amount);
            }
            return stack;
        }
    }
}
