package gregtech.common.tileentities.machines.multi;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.misc.GTStructureChannels;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.GregTechAPI.sBlockCoilECCF;
import static gregtech.api.GregTechAPI.sBlockTintedGlass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTRecipeConstants.ECCF_PRESSURE;
import static gregtech.api.util.GTRecipeConstants.ECCF_TEMPERATURE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

public class MTEEnvironmentallyControlledChemicalFacility extends
    MTEExtendedPowerMultiBlockBase<MTEEnvironmentallyControlledChemicalFacility> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String HEAT_MODULE_L = "tempHeatL";
    private static final String COOL_MODULE_L = "tempCoolL";
    private static final String VACUUM_MODULE_R = "VacuumR";
    private static final String COMPRESSION_MODULE_R = "PressureR";
    private static final String PARALLEL_MODULE_R = "ParallelR";
    private static final String PARALLEL_MODULE_L = "ParallelL";

    double currentTemp = 0;
    double currentPressure = 0;

    private int coolCoilTier = 0;
    private int vacuumCoilTier = 0;
    private int heatCoilTier = 0;
    private int compressCoilTier = 0;

    private boolean isHeatModule;
    private boolean isCoolModule;
    private boolean isVacuumModule;
    private boolean isCompressModule;

    private static final double COEFF_TEMP = 0.98;
    private static final double COEFF_PRESSURE = 0.95;

    private double initialPressure = 0;
    private double initialTemp = 0;
    private double coolantTemp = 0;

    private int requiredTemp = 0;
    private int requiredPressure = 0;

    private MTEHatchInput mLubricantInputHatch;
    private MTEHatchInput mCoolantInputHatch;
    private MTEHatchEnergy mPressureEnergyHatch;

    private int tempThreshold = 10;
    private int pressureThreshold = 10;
    private long drainAmountEU = 0;
    private int coolantInputHatchAmount = 0;

    static final int MODULE_OFFSET_V = 2;
    static final int MODULE_OFFSET_LEFT = 4;
    static final int MODULE_OFFSET_RIGHT = -3;
    static final int MODULE_OFFSET_DEPTH = -1;

    private static final String ECCFPressureNBTTag = "ECCFPressure";
    private static final String ECCFTempNBTTag = "ECCFTemperature";

    private static final IStructureDefinition<MTEEnvironmentallyControlledChemicalFacility> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEnvironmentallyControlledChemicalFacility>builder()
        // spotless:off
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][]{
                {"     "," AAA "," AAA "," AAA "," A~A "," AAA "},
                {" AAA ","AJJJA","PJJJP","AJJJA","AJJJA","AAAAA"},
                {" AAA ","AJJJA","AJ JA","AJ JA","AJJJA","AAAAA"},
                {" AAA ","AJJJA","PJJJP","AJJJA","AJJJA","AAAAA"},
                {"     "," AAA "," AAA "," AAA "," AAA "," AAA "}}
        )
        .addShape(
            HEAT_MODULE_L,
            new String[][]{
                {" P", "QP", "PF", "QY"},
                {"  ", "QQ", "FH", "QQ"},
                {" P", "QP", "PF", "QQ"}}
        )
        .addShape(
            COOL_MODULE_L,
            new String[][]{
                {" P", "QP", "PF", "QY"},
                {"  ", "QQ", "FC", "QQ"},
                {" P", "QP", "PF", "QQ"}}
        )
        .addShape(
            VACUUM_MODULE_R,
            new String[][]{
                {"P ", "PQ", "FQ", "QU"},
                {"  ", "FQ", "VP", "QQ"},
                {"P ", "PQ", "FQ", "PE"}}
        )
        .addShape(
            COMPRESSION_MODULE_R,
            new String[][]{
                {"P ", "P ", "PF", "QU"},
                {"  ", "Q ", "KF", "QQ"},
                {"P ", "P ", "PF", "QE"}}
        )
        .addShape(
            PARALLEL_MODULE_R,
            new String[][]{
                {"P ", "PF", "PF", "QQ"},
                {"Q ", "CG", "CG", "QQ"},
                {"P ", "PF", "PF", "QQ"}}
        )
        .addShape(
            PARALLEL_MODULE_L,
            new String[][]{
                {" P", "FP", "FP", "QQ"},
                {" Q", "GC", "GC", "QQ"},
                {" P", "FP", "FP", "QQ"}}
        )
        // spotless:on
        .addElement('Q', ofBlock(GregTechAPI.sBlockCasings8, 0))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement('J', ofBlock(GregTechAPI.sBlockCasings2, 0))
        .addElement(
            'D',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).atLeast(InputHatch, OutputHatch)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'C',
            GTStructureChannels.ECCF_COOLER.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getCoolCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 0),
                        Pair.of(sBlockCoilECCF, 1),
                        Pair.of(sBlockCoilECCF, 2),
                        Pair.of(sBlockCoilECCF, 3)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.coolCoilTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.coolCoilTier)))
        .addElement(
            'H',
            GTStructureChannels.ECCF_HEATER.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getHeatCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 4),
                        Pair.of(sBlockCoilECCF, 5),
                        Pair.of(sBlockCoilECCF, 6),
                        Pair.of(sBlockCoilECCF, 7)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.heatCoilTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.heatCoilTier)))
        .addElement(
            'K',
            GTStructureChannels.ECCF_COMPRESSOR.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getCompressCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 8),
                        Pair.of(sBlockCoilECCF, 9),
                        Pair.of(sBlockCoilECCF, 10),
                        Pair.of(sBlockCoilECCF, 11)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.compressCoilTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.compressCoilTier)))
        .addElement(
            'V',
            GTStructureChannels.ECCF_VACUUM.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getVacuumCoilMeta,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 12),
                        Pair.of(sBlockCoilECCF, 13),
                        Pair.of(sBlockCoilECCF, 14),
                        Pair.of(sBlockCoilECCF, 15)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.vacuumCoilTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.vacuumCoilTier)))
        .addElement(
            'A',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class)
                .atLeast(OutputHatch, InputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 0)))
        .addElement(
            'Y',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addCoolantInputToMachineList)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'U',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addLubricantInputToMachineList)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement(
            'E',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchEnergy.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addPressureEnergyToMachineList)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement('F', ofFrame(Materials.Polytetrafluoroethylene))
        .addElement('G', ofBlockAnyMeta(sBlockTintedGlass, 1))
        .build();

    public MTEEnvironmentallyControlledChemicalFacility(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEnvironmentallyControlledChemicalFacility(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEEnvironmentallyControlledChemicalFacility> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEnvironmentallyControlledChemicalFacility(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Environmentally Controlled Chemical Facility, ECCF")
            .addInfo(
                EnumChatFormatting.GRAY + "Allows to produce some chemicals in a "
                    + EnumChatFormatting.BLUE
                    + "single step"
                    + EnumChatFormatting.GRAY
                    + ", but requires special conditions")
            .addInfo(
                EnumChatFormatting.GRAY + "Doesn't overclock, instead increases parallels by "
                    + EnumChatFormatting.GOLD
                    + "4 ^ Energy Tier")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.GRAY + "Conditions are shown in NEI and can be achieved by placing ECCF on another "
                    + EnumChatFormatting.BLUE
                    + "planet ")
            .addInfo(
                EnumChatFormatting.GRAY + "with required conditions or maintaining them using "
                    + EnumChatFormatting.BLUE
                    + "Modules")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Module interaction")
            .addInfo(
                EnumChatFormatting.GRAY + "Every second, ECCF temperature drifts towards ambient, to: "
                    + EnumChatFormatting.GOLD
                    + "(current - ambient) * 0.98 + ambient")
            .addInfo(
                EnumChatFormatting.GRAY + "It drains "
                    + EnumChatFormatting.YELLOW
                    + "all coolants"
                    + EnumChatFormatting.GRAY
                    + " from the input hatch in the temperature module")
            .addInfo(
                EnumChatFormatting.GRAY + "And changes temperature to: "
                    + EnumChatFormatting.GOLD
                    + "current + (fluid amount / 10000) * coolant value")
            .addInfo(
                EnumChatFormatting.GRAY + "ECCF drains energy hatch buffer of pressure module "
                    + EnumChatFormatting.YELLOW
                    + "every tick")
            .addInfo(
                EnumChatFormatting.GRAY + "Every second, ECCF pressure drifts towards ambient, to: "
                    + EnumChatFormatting.GOLD
                    + "(current - ambient) * 0.95 + ambient")
            .addInfo(
                EnumChatFormatting.GRAY + "And applies pressure changes depending on the module type: "
                    + EnumChatFormatting.GOLD
                    + "current + (buffer EU ^ 0.7)")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Temperature")
            .addInfo("" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Heating Module")
            .addInfo(
                EnumChatFormatting.GOLD + "Lava"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + "1,300K")
            .addInfo(
                EnumChatFormatting.GOLD + "Blazing Pyrotheum"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + "4,000K")
            .addInfo(
                EnumChatFormatting.GOLD + "Helium Plasma"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + "10,000K")
            .addInfo(
                EnumChatFormatting.GOLD + "Raw Stellar Plasma"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.YELLOW
                    + "10,000,000K")
            .addInfo("" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD + "Cooling Module")
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "IC2 Coolant"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.AQUA
                    + "250K")
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "Gelid Cryotheum"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.AQUA
                    + "25K")
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "Super Coolant"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.AQUA
                    + "5K")
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "Molten Spacetime"
                    + EnumChatFormatting.GRAY
                    + ": "
                    + EnumChatFormatting.AQUA
                    + "0K")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Pressure")
            .addInfo(
                EnumChatFormatting.GRAY + "2 modules: "
                    + EnumChatFormatting.DARK_AQUA
                    + "Vacuum"
                    + EnumChatFormatting.GRAY
                    + " to decrease pressure and "
                    + EnumChatFormatting.GOLD
                    + "Compressor"
                    + EnumChatFormatting.GRAY
                    + " to increase pressure")
            .addInfo(
                EnumChatFormatting.GRAY + "Pressure module requires energy and "
                    + EnumChatFormatting.YELLOW
                    + "5L/s"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.YELLOW
                    + "VO lubricants "
                    + EnumChatFormatting.GRAY
                    + "to operate")
            .addInfo(
                EnumChatFormatting.GRAY + "While the module is running, "
                    + EnumChatFormatting.YELLOW
                    + "0-80% "
                    + EnumChatFormatting.GRAY
                    + "of the non-ambient pressure is"
                    + EnumChatFormatting.RED
                    + " lost"
                    + EnumChatFormatting.GRAY
                    + " depending on the "
                    + EnumChatFormatting.YELLOW
                    + "VO lubricant "
                    + EnumChatFormatting.GRAY
                    + "used")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "VO Lubricants Values")
            .addInfo(EnumChatFormatting.GOLD + "No lubricant " + EnumChatFormatting.GRAY + "- 80% loss")
            .addInfo(EnumChatFormatting.GOLD + "VO-17 " + EnumChatFormatting.GRAY + "- 30% loss")
            .addInfo(EnumChatFormatting.GOLD + "VO-43 " + EnumChatFormatting.GRAY + "- 10% loss")
            .addInfo(EnumChatFormatting.GOLD + "VO-75 " + EnumChatFormatting.GRAY + "- 0% loss")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Information")
            .addInfo(
                EnumChatFormatting.GRAY + "Use "
                    + EnumChatFormatting.GOLD
                    + "portable scanner "
                    + EnumChatFormatting.GRAY
                    + "to get information about "
                    + EnumChatFormatting.GOLD
                    + "pressure"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.GOLD
                    + "temperature")
            .addInfo(
                EnumChatFormatting.GRAY + "Check"
                    + EnumChatFormatting.GOLD
                    + " quest about ECCF"
                    + EnumChatFormatting.GRAY
                    + " to get information about "
                    + EnumChatFormatting.GOLD
                    + "formulas "
                    + EnumChatFormatting.GRAY
                    + "and "
                    + EnumChatFormatting.GOLD
                    + "dimension conditions")
            .beginStructureBlock(5, 6, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Chemically Inert Casing", 0, false)
            .addInputBus("Any Chemically Inert Casing", 1)
            .addOutputBus("Any Chemically Inert Casing", 1)
            .addInputHatch("Any Chemically Inert Casing", 1)
            .addOutputHatch("Any Chemically Inert Casing", 1)
            .addEnergyHatch("Any Chemically Inert Casing", 1)
            .addMaintenanceHatch("Any Chemically Inert Casing", 1)
            .toolTipFinisher(
                "" + EnumChatFormatting.BLUE
                    + EnumChatFormatting.BOLD
                    + "VorTex"
                    + EnumChatFormatting.GRAY
                    + " & "
                    + EnumChatFormatting.YELLOW
                    + EnumChatFormatting.BOLD
                    + "Rait_GamerGR");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 4, 0);
        Pair<Integer, Integer> modules = create_modules(stackSize);
        int MODULE_LEFT = modules.getLeft();
        int MODULE_RIGHT = modules.getRight();

        switch (MODULE_LEFT) {
            case 1 -> buildPiece(
                HEAT_MODULE_L,
                stackSize,
                hintsOnly,
                MODULE_OFFSET_LEFT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH);
            case 2 -> buildPiece(
                COOL_MODULE_L,
                stackSize,
                hintsOnly,
                MODULE_OFFSET_LEFT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH);
        }
        switch (MODULE_RIGHT) {
            case 3 -> buildPiece(
                COMPRESSION_MODULE_R,
                stackSize,
                hintsOnly,
                MODULE_OFFSET_RIGHT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH);
            case 4 -> buildPiece(
                VACUUM_MODULE_R,
                stackSize,
                hintsOnly,
                MODULE_OFFSET_RIGHT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine && (create_modules(stackSize).getLeft() == 0) && (create_modules(stackSize).getRight() == 0))
            return -1;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 4, 0, elementBudget, env, false, true);
        Pair<Integer, Integer> modules = create_modules(stackSize);
        switch (modules.getLeft()) {
            case 1 -> built += survivialBuildPiece(
                HEAT_MODULE_L,
                stackSize,
                MODULE_OFFSET_LEFT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                COOL_MODULE_L,
                stackSize,
                MODULE_OFFSET_LEFT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH,
                elementBudget,
                env,
                false,
                true);
        }
        switch (modules.getRight()) {
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_R,
                stackSize,
                MODULE_OFFSET_RIGHT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_R,
                stackSize,
                MODULE_OFFSET_RIGHT,
                MODULE_OFFSET_V,
                MODULE_OFFSET_DEPTH,
                elementBudget,
                env,
                false,
                true);
        }
        return built;
    }

    public Pair<Integer, Integer> create_modules(ItemStack stackSize) {
        /*
         * 0 - none
         * 1 - heat
         * 2 - freeze
         * 3 - compress
         * 4 - pump
         */
        int MODULE_LEFT = 0;
        int MODULE_RIGHT = 0;
        boolean COOLER = false;
        boolean HEATER = false;
        boolean VACUUM = false;
        boolean COMPRESSOR = false;
        if (stackSize.getTagCompound() != null) {
            NBTTagCompound channels = stackSize.getTagCompound()
                .getCompoundTag("channels");
            if (channels != null) {
                HEATER = channels.getInteger("eccf_heater") > 0;
                COOLER = channels.getInteger("eccf_cooler") > 0;
                COMPRESSOR = channels.getInteger("eccf_compress") > 0;
                VACUUM = channels.getInteger("eccf_vacuum") > 0;
            }
        }
        // right
        if (HEATER) MODULE_LEFT = 1;
        if (COOLER) MODULE_LEFT = 2;
        // left
        if (COMPRESSOR) MODULE_RIGHT = 3;
        if (VACUUM) MODULE_RIGHT = 4;
        return Pair.of(MODULE_LEFT, MODULE_RIGHT);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        coolCoilTier = -1;
        heatCoilTier = -1;
        vacuumCoilTier = -1;
        compressCoilTier = -1;
        isHeatModule = false;
        isCoolModule = false;
        isVacuumModule = false;
        isCompressModule = false;
        getDimConditions();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0)) {
            return false;
        }
        coolantInputHatchAmount = 0;
        isHeatModule = checkPiece(HEAT_MODULE_L, MODULE_OFFSET_LEFT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        if (coolantInputHatchAmount > 1) return false;
        coolantInputHatchAmount = 0;
        isCoolModule = checkPiece(COOL_MODULE_L, MODULE_OFFSET_LEFT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        if (coolantInputHatchAmount > 1) return false;
        isVacuumModule = checkPiece(VACUUM_MODULE_R, MODULE_OFFSET_RIGHT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        isCompressModule = checkPiece(COMPRESSION_MODULE_R, MODULE_OFFSET_RIGHT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        return (!isHeatModule || !isCoolModule) && (!isVacuumModule || !isCompressModule);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                requiredTemp = recipe.getMetadataOrDefault(ECCF_TEMPERATURE, 0);
                requiredPressure = recipe.getMetadataOrDefault(ECCF_PRESSURE, 0);
                tempThreshold = (int) (1.5 * Math.pow(requiredTemp, 0.55));
                pressureThreshold = (int) (1.5 * Math.pow(requiredPressure, 0.55));
                if (Math.abs(currentTemp - requiredTemp) <= tempThreshold
                    && Math.abs(currentPressure - requiredPressure) <= pressureThreshold) {
                    return super.validateRecipe(recipe);
                }
                stopMachine(SimpleShutDownReason.ofCritical("conditions_range"));
                return CheckRecipeResultRegistry.RECIPE_CONDITIONS;
            }
        }.setOverclock(1, 4);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) Math.pow(4, GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.ECCFRecipes, RecipeMaps.planetConditions);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.ECCFRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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

    @Nullable
    public static Integer getCoolCoilMeta(Block block, int meta) {
        if (block == sBlockCoilECCF) {
            return switch (meta) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 3 -> 3;
                default -> null;
            };
        }
        return null;
    }

    @Nullable
    public static Integer getHeatCoilMeta(Block block, int meta) {
        if (block == sBlockCoilECCF) {
            return switch (meta) {
                case 4 -> 0;
                case 5 -> 1;
                case 6 -> 2;
                case 7 -> 3;
                default -> null;
            };
        }
        return null;
    }

    @Nullable
    public static Integer getCompressCoilMeta(Block block, int meta) {
        if (block == sBlockCoilECCF) {
            return switch (meta) {
                case 8 -> 0;
                case 9 -> 1;
                case 10 -> 2;
                case 11 -> 3;
                default -> null;
            };
        }
        return null;
    }

    @Nullable
    public static Integer getVacuumCoilMeta(Block block, int meta) {
        if (block == sBlockCoilECCF) {
            return switch (meta) {
                case 12 -> 0;
                case 13 -> 1;
                case 14 -> 2;
                case 15 -> 3;
                default -> null;
            };
        }
        return null;
    }

    public String[] getInfoData() {
        return new String[] {

            StatCollector.translateToLocal("GT5U.ECCF_pressure") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(currentPressure)
                + EnumChatFormatting.RESET
                + " Pa",
            StatCollector.translateToLocal("GT5U.ECCF.pressure_required") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(requiredPressure)
                + EnumChatFormatting.RESET
                + " Pa",
            StatCollector.translateToLocal("GT5U.ECCF.pressure_threshold") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(pressureThreshold)
                + EnumChatFormatting.RESET
                + " Pa",
            StatCollector.translateToLocal("GT5U.ECCF.temperature") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(currentTemp)
                + EnumChatFormatting.RESET
                + " K",
            StatCollector.translateToLocal("GT5U.ECCF.temperature_required") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(requiredTemp)
                + EnumChatFormatting.RESET
                + " K",
            StatCollector.translateToLocal("GT5U.ECCF.temperature_threshold") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(tempThreshold)
                + EnumChatFormatting.RESET
                + " K" };
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble(ECCFPressureNBTTag, currentPressure);
        aNBT.setDouble(ECCFTempNBTTag, currentTemp);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        currentPressure = aNBT.getDouble(ECCFPressureNBTTag);
        currentTemp = aNBT.getDouble(ECCFTempNBTTag);
        super.loadNBTData(aNBT);
    }

    public enum DimensionConditions {

        // T0
        OVERWORLD("Overworld", 290, 101000),
        NETHER("Nether", 900, 200000),
        TWILIGHT("Twilight", 270, 101000),
        END("End", 220, 0),
        // T1
        MOON("Moon", 250, 0),
        // T2
        DEIMOS("Deimos", 233, 0),
        MARS("Mars", 215, 600),
        PHOBOS("Phobos", 233, 0),
        // T3
        ASTEROIDS("Asteroids", 200, 0),
        CALLISTO("Callisto", 134, 0),
        CERES("Ceres", 168, 0),
        EUROPA("Europa", 102, 0),
        GANYMEDE("Ganymede", 132, 0),
        ROSS128B("Ross128b", 295, 101000),
        // T4
        IO("Io", 130, 0),
        MERCURY("Mercury", 440, 0),
        VENUS("Venus", 740, 9200000),
        // T5
        ENCELADUS("Enceladus", 70, 1),
        MIRANDA("Miranda", 60, 0),
        OBERON("Oberon", 70, 0),
        TITAN("Titan", 94, 146700),
        ROSS128BA("Ross128ba", 285, 101000),
        // T6
        PROTEUS("Proteus", 50, 0),
        TRITON("Triton", 38, 1),
        // T7
        HAUMEA("Haumea", 32, 0),
        KUIPERBELT("Kuiperbelt", 50, 0),
        MAKEMAKE("Makemake", 30, 0),
        PLUTO("Pluto", 44, 1),
        // T8
        BARNARD_C("BarnardC", 300, 10000), // temporary
        BARNARD_E("BarnardE", 300, 10000), // temporary
        BARNARD_F("BarnardF", 300, 10000), // temporary
        CENTAURI_A("CentauriA", 1500, 0),
        TCETI_E("TCetiE", 300, 10000), // temporary
        VEGA_B("VegaB", 300, 10000), // temporary
        // T9
        ANUBIS("Anubis", 300, 10000), // temporary
        HORUS("Horus", 300, 10000), // temporary
        MAAHES("Maahes", 300, 10000), // temporary
        NEPER("Neper", 300, 10000), // temporary
        SETH("Seth", 300, 10000), // temporary
        // T10
        UNDERDARK("Underdark", 270, 131000);

        private final String dimensionName;
        private final int initialTemp;
        private final int initialPressure;

        DimensionConditions(String dimensionName, int initialTemp, int initialPressure) {
            this.dimensionName = dimensionName;
            this.initialTemp = initialTemp;
            this.initialPressure = initialPressure;
        }

        public int getInitialTemp() {
            return initialTemp;
        }

        public int getInitialPressure() {
            return initialPressure;
        }

        public static DimensionConditions fromDimensionName(String dimensionName) {
            for (DimensionConditions condition : values()) {
                if (condition.dimensionName.equalsIgnoreCase(dimensionName)) {
                    return condition;
                }
            }
            return OVERWORLD;
        }
    }

    public void getDimConditions() {
        WorldProvider provider = this.getBaseMetaTileEntity()
            .getWorld().provider;
        String dimName = provider.getDimensionName();
        DimensionConditions condition = DimensionConditions.fromDimensionName(dimName);
        initialTemp = condition.getInitialTemp();
        initialPressure = condition.getInitialPressure();
    }

    public void setTempFromCoolant(String name) {
        switch (name) {
            // cooling
            case "ic2coolant":
                coolantTemp = 250;
                break;
            case "cryotheum":
                coolantTemp = 25;
                break;
            case "supercoolant":
                coolantTemp = 5;
                break;
            case "molten.spacetime":
                coolantTemp = 0;
                break;
            // heating
            case "lava":
                coolantTemp = 1300;
                break;
            case "pyrotheum":
                coolantTemp = 4000;
                break;
            case "plasma.helium":
                coolantTemp = 10000;
                break;
            case "rawstarmatter":
                coolantTemp = 10000000;
                break;
        }
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mCoolantInputHatch = (MTEHatchInput) aMetaTileEntity;
            coolantInputHatchAmount++;
            return true;
        }
        return false;
    }

    public boolean addLubricantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mLubricantInputHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addPressureEnergyToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchEnergy) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mPressureEnergyHatch = (MTEHatchEnergy) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (mPressureEnergyHatch != null) {
            drainAmountEU += mPressureEnergyHatch.getBaseMetaTileEntity()
                .getStoredEU();
            mPressureEnergyHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(drainAmountEU, false);
        }
        if (mMachine && (aTick % 20 == 0)) {
            if ((currentTemp == 0) && (currentPressure == 0)) {
                currentPressure = initialPressure;
                currentTemp = initialTemp;
            }
            // Temperature calculation
            if (isCoolModule || isHeatModule) {
                // returns values to atmosphere conditions
                currentTemp = (currentTemp - initialTemp) * COEFF_TEMP + initialTemp;
                // drain all coolant from hatches and change temperature
                if (mCoolantInputHatch != null) {
                    if (mCoolantInputHatch.mFluid != null) {
                        String coolantName = mCoolantInputHatch.mFluid.getFluid()
                            .getName();
                        setTempFromCoolant(coolantName);
                        boolean isCoolant = ((coolantName.equals("ic2coolant")) || (coolantName.equals("cryotheum"))
                            || (coolantName.equals("supercoolant"))
                            || (coolantName.equals("molten.spacetime")));
                        boolean isHot = ((coolantName.equals("pyrotheum")) || (coolantName.equals("plasma.helium"))
                            || (coolantName.equals("rawstarmatter"))
                            || (coolantName.equals("lava")));
                        // Apply coolant (linear function)
                        double coolantTempImpact = mCoolantInputHatch.mFluid.amount / 10000f * coolantTemp;
                        if ((isCoolModule || isCoolant) || (isHeatModule || isHot)) {
                            if (coolantTemp < initialTemp)
                                currentTemp = Math.max(currentTemp - coolantTempImpact, coolantTemp);
                            if (coolantTemp > initialTemp)
                                currentTemp = Math.min(currentTemp + coolantTempImpact, coolantTemp);
                            drain(mCoolantInputHatch, mCoolantInputHatch.mFluid, true);
                        }
                    }
                }
            } else currentTemp = initialTemp;

            // Pressure calculation
            if (isCompressModule || isVacuumModule) {
                // returns values to atmosphere conditions
                currentPressure = (currentPressure - initialPressure) * COEFF_PRESSURE + initialPressure;
                // Apply pressure changes
                if (mPressureEnergyHatch != null) {
                    drainAmountEU /= 20;
                    currentPressure += Math.pow(drainAmountEU, 0.7) * (isCompressModule ? 1 : -1);
                    currentPressure = Math.max(currentPressure, 0);
                    drainAmountEU = 0;
                }
                // Apply lubricant depressurization (leakCoeff is loss percentage)
                double leakCoeff = 0.8;
                // check if there is a hatch for lubricant and apply depressurization if there is too few lubricant
                if (mLubricantInputHatch != null) {
                    if (mLubricantInputHatch.mFluid != null) {
                        // if more than 5mb of fluid is in hatch:
                        if (mLubricantInputHatch.mFluid.amount >= 5) {
                            String lubricantType = mLubricantInputHatch.mFluid.getUnlocalizedName();
                            leakCoeff = switch (lubricantType) {
                                case "vo17" -> 0.3;
                                case "vo43" -> 0.1;
                                case "vo75" -> 0;
                                default -> 0.8;
                            };
                            mLubricantInputHatch.drain(5, true);
                        }
                    }
                    currentPressure = currentPressure * leakCoeff + initialPressure * (1 - leakCoeff);
                }
            } else currentPressure = initialPressure;

            // Range check
            if (mMaxProgresstime != 0) {
                if ((Math.abs(currentTemp - requiredTemp) > tempThreshold)
                    || (Math.abs(currentPressure - requiredPressure) > pressureThreshold)) {
                    stopMachine(SimpleShutDownReason.ofCritical("conditions_range"));
                }
            }
        }
    }
}
