package gregtech.common.tileentities.machines.multi.Solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bartworks.common.tileentities.multis.mega.MTEMegaVacuumFreezer;
import com.google.common.collect.ImmutableList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.tileentities.machines.multi.MTENanoForge;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import ggfab.api.GGFabRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import tectech.thing.casing.TTCasingsContainer;

import javax.annotation.Nonnull;





public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable {


    private static final List<CoolingFluid> COOLING_FLUIDS = ImmutableList.of(
        new CoolingFluid(MaterialsUEVplus.SpaceTime,1,100),
        new CoolingFluid(MaterialsUEVplus.Space, 2, 50),
        new CoolingFluid(MaterialsUEVplus.Eternity, 3, 25));

    private CoolingFluid currentCoolingFluid = null;
    private static int horizontalOffset = 7;
    private static int verticalOffset = 43;
    private static int depthOffset = 0;

    private int mTier = 3; // 1 - base , 2 - ~UEV, 3 - ~UMV
    private final float speedModifierBase = 2F;
    private final float euEffBase = 0.9F;
    private final int parallelScaleBase = 8;
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

    // offsets, for building the structure, redirect to build the bottom left corner of the structure piece at
    // Controller pos + offsets.
    private SolidifierModules[] modules = {SolidifierModules.getModule(4),SolidifierModules.getModule(4),SolidifierModules.getModule(4),SolidifierModules.getModule(4)};

    //these are all the same as of right now, but MAY change with diff structure
    private int[] moduleHorizontalOffsets = {15,15,15,15};
    private int[] moduleVerticalOffsets = {5,10,15,20};
    private int[] moduleDepthOffsets = {-2,-2,-2,-2};
    private static final String STRUCTURE_PIECE_MAIN = "main";
    // Hypercooler is limited to 1, either dont read the second one or strucure check fail

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
        .addShape(SolidifierModules.TRANSCENDENT_REINFORCEMENT.structureID, transpose(new String[][]{
            {"cccdccc","cccdccc","cccdccc"}}
        ))
        .addShape(SolidifierModules.HYPERCOOLER.structureID, transpose(new String[][]{
            {"eeefeee","eefgfee","eeefeee"}
        }))
        .addShape(SolidifierModules.UNSET.structureID, transpose(new String[][]{
            {"       ","       ","       "}
        }))
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings11,7))
        .addElement('C',ofBlock(GregTechAPI.sBlockCasings5,12))
        // .addElement('D' ,ofBlock(GregTechAPI.sBlockFrames,81))
        .addElement('D', ofFrame(MaterialsUEVplus.TranscendentMetal))
        .addElement('E', lazy(() -> ofBlock(GodforgeCasings,0)))
        .addElement('F', ofBlock( GregTechAPI.sBlockCasings2,3)) //temp
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings2,14)) //temp
        .addElement(
            'H',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .buildAndChain(onElementPass(MTEModularSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 10)))) //placeholder nano forge casing
        .addElement('c', ofBlock(GregTechAPI.sBlockMetal9, 4))
        .addElement('d', ofBlock(GregTechAPI.sBlockCasings13, 11))
        .addElement('e', ofBlock(GregTechAPI.sBlockCasings5, 11))
        .addElement('f', ofBlock(GregTechAPI.sBlockGlass1, 3))
        .addElement('g', ofBlock(GregTechAPI.sBlockCasings13, 13))
        .build();

    public MTEModularSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEModularSolidifier(String aName) {
        super(aName);
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

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier")
            .addInfo("100% faster than singleblock machines of the same voltage")
            .addInfo("Gains 12 parallels per voltage tier")
            .beginStructureBlock(3, 5, 3, true)
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
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules m = modules[i];
            if (m != SolidifierModules.UNSET)
            {
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
            if (m != SolidifierModules.UNSET)
            {
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
        //todo: tiered structure 1 - 3
        if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, depthOffset) && mCasingAmount >= 14)
        {
            mTier = 3;
            return checkModules();
        }
       /* if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
        {
            mTier = 2;
            return checkModules();
        }
        if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
        {
            mTier = 1;
            return checkModules();
        }*/
        return false;


    }

    private boolean checkModules() {
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            SolidifierModules m = modules[i];
            if(!checkPiece(m.structureID,moduleHorizontalOffsets[i],moduleVerticalOffsets[i],moduleDepthOffsets[i])) return false;
        }
        return true;

    }

    public CoolingFluid findCoolingFluid()
    {
        for (MTEHatchInput hatch : mInputHatches)
        {
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
        logic.setMaxTierSkips(3); //capped at 3 for now (current solidifier can do the same)
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
            protected  CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                additionaloverclocks = 0;

                if (hypercoolerPresent)
                {
                    currentCoolingFluid = findCoolingFluid();
                    if(currentCoolingFluid == null)
                    {
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                    additionaloverclocks = currentCoolingFluid.grantedOC ;
                }

                if (GTUtility.getTier(recipe.mEUt) >= VoltageIndex.UEV && !uevRecipesEnabled)
                {
                    return CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxRegularOverclocks(additionaloverclocks + (getTier(getAverageInputVoltage()) - getTier(recipe.mEUt)))
                    .setDurationDecreasePerOC(ocFactorBase+ocFactorAdditive);

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
    public boolean supportsPowerPanel() {
        return false;
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
    public String[] getModuleNames(int index) {
        // just in case
        if (index > SolidifierModules.values().length - 1) index = 0;

        SolidifierModules modulegiven = modules[index];
        return new String[] { modulegiven.displayName, modulegiven.shorthand, modulegiven.structureID };
    }

    public void setModule(int index, int ordinal) {
        // just in case, shouldn't be possible
        if (index > modules.length - 1 || ordinal > SolidifierModules.size()) return;
        SolidifierModules moduleToAdd = SolidifierModules.getModule(ordinal);
        if (moduleToAdd == SolidifierModules.HYPERCOOLER) {
            checkSolidifierModules();
            if (hypercoolerPresent) return;
        }
        modules[index] = moduleToAdd;
    }

    public String getSpeedStr() {
        return (speedModifierAdj - 1) * 100 + "%";
    }



    private static class CoolingFluid{
        public Materials material;
        public int grantedOC;
        public int amount;

        public CoolingFluid(Materials material, int grantedOC, int amount)
        {
            this.material = material;
            this.grantedOC = grantedOC;
            this.amount = amount;
        }
        public FluidStack getStack()
        {
            FluidStack stack = material.getFluid(amount);
            //shoutout penguin, i think i get why you were upset with this code here :^)
            if (stack == null){
                return material.getMolten(amount);
            }
            return stack;
        }
    }
}
