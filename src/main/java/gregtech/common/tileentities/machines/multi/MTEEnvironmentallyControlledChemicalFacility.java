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

import javax.annotation.Nullable;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.GregTechAPI.sBlockCoilECCF;
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

/*
 * this code might be very buggy, the biggest bug is that for some reason it can't
 * detect tier of the module blocks, which will be very important later on
 */

public class MTEEnvironmentallyControlledChemicalFacility extends
    MTEExtendedPowerMultiBlockBase<MTEEnvironmentallyControlledChemicalFacility> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String HEAT_MODULE_L = "tempHeatL";
    private static final String COOL_MODULE_L = "tempCoolL";
    private static final String VACUUM_MODULE_R = "VacuumR";
    private static final String COMPRESSION_MODULE_R = "PressureR";

    double currentTemp = 0;
    double currentPressure = 0;

    private int CoolCoilTier = 0;
    private int VacuumCoilTier = 0;
    private int HeatCoilTier = 0;
    private int CompressCoilTier = 0;

    private boolean isHeatModule;
    private boolean isCoolModule;
    private boolean isVacuumModule;
    private boolean isCompressModule;

    private static final double COEFF_TEMP = 0.98;
    private double coeffPressure = 0.95;

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
    private String lubricantType;
    private double leakCoeff = 0;

    private static final IStructureDefinition<MTEEnvironmentallyControlledChemicalFacility> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEnvironmentallyControlledChemicalFacility>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
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
                {"HH","H ","H ","YQ"},
                {"  ","  ","  ","QQ"},
                {"HH","H ","H ","QQ"}}
        )
        .addShape(
            COOL_MODULE_L,
            new String[][]{
                {"CC","C ","C ","YQ"},
                {"  ","  ","  ","QQ"},
                {"CC","C ","C ","QQ"}}
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
        )// spotless:on
        .addElement('Q', ofBlock(GregTechAPI.sBlockCasings8, 0))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement('J', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement(
            'D',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).atLeast(InputHatch, OutputHatch)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
            .addElement(
                    'C',
                    GTStructureChannels.ACR_COOL_PIPE.use(
                                    ofBlocksTiered(
                                            MTEEnvironmentallyControlledChemicalFacility::getCoolCoilMeta,
                                            ImmutableList.of(
                                                    Pair.of(sBlockCoilECCF, 0),
                                                    Pair.of(sBlockCoilECCF, 1),
                                                    Pair.of(sBlockCoilECCF, 2),
                                                    Pair.of(sBlockCoilECCF, 3)),
                                            -1,
                                            (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.CoolCoilTier = m,
                                            (MTEEnvironmentallyControlledChemicalFacility t) -> t.CoolCoilTier)))
            .addElement(
                    'H',
                    GTStructureChannels.ACR_HEAT_PIPE.use(
                                    ofBlocksTiered(
                                            MTEEnvironmentallyControlledChemicalFacility::getHeatCoilMeta,
                                            ImmutableList.of(
                                                    Pair.of(sBlockCoilECCF, 4),
                                                    Pair.of(sBlockCoilECCF, 5),
                                                    Pair.of(sBlockCoilECCF, 6),
                                                    Pair.of(sBlockCoilECCF, 7)),
                                            -1,
                                            (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.HeatCoilTier = m,
                                            (MTEEnvironmentallyControlledChemicalFacility t) -> t.HeatCoilTier)))
            .addElement(
                    'K',
                    GTStructureChannels.ACR_COMPRESS_PIPE.use(
                                    ofBlocksTiered(
                                            MTEEnvironmentallyControlledChemicalFacility::getCompressCoilMeta,
                                            ImmutableList.of(
                                                    Pair.of(sBlockCoilECCF, 8),
                                                    Pair.of(sBlockCoilECCF, 9),
                                                    Pair.of(sBlockCoilECCF, 10),
                                                    Pair.of(sBlockCoilECCF, 11)),
                                            -1,
                                            (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.CompressCoilTier = m,
                                            (MTEEnvironmentallyControlledChemicalFacility t) -> t.CompressCoilTier)))
            .addElement(
                    'V',
                    GTStructureChannels.ACR_VACUUM_PIPE.use(
                                    ofBlocksTiered(
                                            MTEEnvironmentallyControlledChemicalFacility::getVacuumCoilMeta,
                                            ImmutableList.of(
                                                    Pair.of(sBlockCoilECCF, 12),
                                                    Pair.of(sBlockCoilECCF, 13),
                                                    Pair.of(sBlockCoilECCF, 14),
                                                    Pair.of(sBlockCoilECCF, 15)),
                                            -1,
                                            (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.VacuumCoilTier = m,
                                            (MTEEnvironmentallyControlledChemicalFacility t) -> t.VacuumCoilTier)))
        .addElement(
            'A',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class)
                .atLeast(OutputHatch, InputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings8, 0)))
        .addElement('S', ofBlock(GregTechAPI.sBlockCasings2, 0))
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
            .addSeparator()
            .addInfo(
                EnumChatFormatting.GRAY + "Conditions are shown in "
                    + EnumChatFormatting.BLUE
                    + "NEI"
                    + EnumChatFormatting.GRAY
                    + " and can be achieved by placing ECCF on a "
                    + EnumChatFormatting.BLUE
                    + "planet "
            )
            .addInfo(
                    EnumChatFormatting.GRAY
                    + "with required conditions or maintaining them using "
                    + EnumChatFormatting.BLUE
                    + "modules")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Modules:")
            .addInfo(EnumChatFormatting.GRAY
                + "2 exclusive modules for temperature: "
                + EnumChatFormatting.BLUE
                + "Cooling"
                + EnumChatFormatting.GRAY
                + " and "
                + EnumChatFormatting.RED + "Heating")
            .addInfo(EnumChatFormatting.GRAY + "2 exclusive modules for pressure: "
                + EnumChatFormatting.DARK_AQUA
                + "Vacuum Pump"
                + EnumChatFormatting.GRAY
                + " and "
                + EnumChatFormatting.BLUE + "Compressor")
            .addInfo(EnumChatFormatting.GRAY + "Internal structure changes "
                + EnumChatFormatting.GREEN
                + "Radioactivity"
                + EnumChatFormatting.GRAY
                + " and "
                + EnumChatFormatting.WHITE
                + "Inertness "
                + EnumChatFormatting.GRAY
                + "of the environment"
                + EnumChatFormatting.WHITE
                + EnumChatFormatting.BOLD
                + " [COMING SOON]")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE
                + EnumChatFormatting.BOLD
                + "Temperature Values:")
            .addInfo("" + EnumChatFormatting.RED
                + EnumChatFormatting.BOLD
                + "Heating")
            .addInfo(EnumChatFormatting.RED + "Lava"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.YELLOW
                + "1 300K")
            .addInfo(EnumChatFormatting.RED + "Blazing Pyrotheum"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.YELLOW
                + "4 000K")
            .addInfo(EnumChatFormatting.RED + "Helium Plasma"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.YELLOW
                + "10 000K")
            .addInfo(EnumChatFormatting.RED + "Raw Stellar Plasma Mixture"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.YELLOW
                + "10 000 000K")
            .addInfo("" + EnumChatFormatting.DARK_AQUA
                + EnumChatFormatting.BOLD
                + "Cooling")
            .addInfo(EnumChatFormatting.DARK_AQUA + "IC2 Coolant"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.BLUE
                + "250K")
            .addInfo(EnumChatFormatting.DARK_AQUA + "Gelid Cryotheum"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.BLUE
                + "25K")
            .addInfo(EnumChatFormatting.DARK_AQUA + "Super Coolant"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.BLUE
                + "5K")
            .addInfo(EnumChatFormatting.DARK_AQUA + "Molten Spacetime"
                + EnumChatFormatting.GRAY + ": "
                + EnumChatFormatting.BLUE
                + "0K")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Pressure")
            .addInfo(EnumChatFormatting.GRAY + "Pressure module requires energy and "
                + EnumChatFormatting.YELLOW + "5L of VC lubricant per second "
                + EnumChatFormatting.GRAY + "to operate")
            .addInfo(EnumChatFormatting.GRAY + "If less than"
                + EnumChatFormatting.YELLOW
                + " 1000L "
                + EnumChatFormatting.GRAY
                + "lubricant is supplied, "
                + EnumChatFormatting.BLUE
                + "depressurization "
                + EnumChatFormatting.GRAY
                + "can happen")
            .addInfo(EnumChatFormatting.GRAY + "Causing loosing a lot of pressure")
            .beginStructureBlock(5, 6, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Chemically Inert Casing", 0, false)
            .addInputBus("Any Chemically Inert Casing", 1)
            .addOutputBus("Any Chemically Inert Casing", 1)
            .addInputHatch("Any Chemically Inert Casing", 1)
            .addOutputHatch("Any Chemically Inert Casing", 1)
            .addEnergyHatch("Any Chemically Inert Casing", 1)
            .addMaintenanceHatch("Any Chemically Inert Casing", 1)
            .toolTipFinisher(EnumChatFormatting.BLUE + "VorTex"
                    + EnumChatFormatting.GRAY
                    + " & "
                    + EnumChatFormatting.YELLOW
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
            case 1 -> buildPiece(HEAT_MODULE_L, stackSize, hintsOnly, moduleOffsetL, moduleOffsetV, depthOffset);
            case 2 -> buildPiece(COOL_MODULE_L, stackSize, hintsOnly, moduleOffsetL, moduleOffsetV, depthOffset);
        }
        switch (MODULE_RIGHT) {
            case 3 -> buildPiece(COMPRESSION_MODULE_R, stackSize, hintsOnly, moduleOffsetR, moduleOffsetV, depthOffset);
            case 4 -> buildPiece(VACUUM_MODULE_R, stackSize, hintsOnly, moduleOffsetR, moduleOffsetV, depthOffset);
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
                moduleOffsetL,
                moduleOffsetV,
                    depthOffset,
                elementBudget,
                env,
                false,
                true);
            case 2 -> built += survivialBuildPiece(
                COOL_MODULE_L,
                stackSize,
                moduleOffsetL,
                moduleOffsetV,
                    depthOffset,
                elementBudget,
                env,
                false,
                true);
        }
        switch (modules.getRight()) {
            case 3 -> built += survivialBuildPiece(
                COMPRESSION_MODULE_R,
                stackSize,
                moduleOffsetR,
                moduleOffsetV,
                    depthOffset,
                elementBudget,
                env,
                false,
                true);
            case 4 -> built += survivialBuildPiece(
                VACUUM_MODULE_R,
                stackSize,
                moduleOffsetR,
                moduleOffsetV,
                depthOffset,
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
        boolean COOL_PIPE = false;
        boolean HEAT_PIPE = false;
        boolean VACUUM_PIPE = false;
        boolean COMPRESS_PIPE = false;
        if (stackSize.getTagCompound() != null) {
            if (stackSize.getTagCompound()
                .getCompoundTag("channels") != null) {
                HEAT_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("heat_pipe") > 0;
                COOL_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("cool_pipe") > 0;
                COMPRESS_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("compress_pipe") > 0;
                VACUUM_PIPE = stackSize.getTagCompound()
                    .getCompoundTag("channels")
                    .getInteger("vacuum_pipe") > 0;
            }
        }
        // right
        if (HEAT_PIPE) MODULE_LEFT = 1;
        if (COOL_PIPE) MODULE_LEFT = 2;
        // left
        if (COMPRESS_PIPE) MODULE_RIGHT = 3;
        if (VACUUM_PIPE) MODULE_RIGHT = 4;
        return Pair.of(MODULE_LEFT, MODULE_RIGHT);
    }

    int moduleOffsetV = 2;
    int moduleOffsetL = 4;
    int moduleOffsetR = -3;
    int depthOffset = -1;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        CoolCoilTier = -1;
        HeatCoilTier = -1;
        VacuumCoilTier = -1;
        CompressCoilTier = -1;
        isHeatModule = false;
        isCoolModule = false;
        isVacuumModule = false;
        isCompressModule = false;
        getDimConditions();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0)) {
            return false;
        }
        isHeatModule = checkPiece(HEAT_MODULE_L, moduleOffsetL, moduleOffsetV, depthOffset);
        isCoolModule = checkPiece(COOL_MODULE_L, moduleOffsetL, moduleOffsetV, depthOffset);
        isVacuumModule = checkPiece(VACUUM_MODULE_R, moduleOffsetR, moduleOffsetV, depthOffset);
        isCompressModule = checkPiece(COMPRESSION_MODULE_R, moduleOffsetR, moduleOffsetV, depthOffset);
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
                this.maxParallel = Math.toIntExact(availableVoltage / recipe.mEUt);
                if (Math.abs(currentTemp - requiredTemp) <= tempThreshold || Math.abs(currentPressure - requiredPressure) <= pressureThreshold) {
                    return super.validateRecipe(recipe);
                }
                stopMachine(SimpleShutDownReason.ofCritical("conditions.out.of.range"));
                return CheckRecipeResultRegistry.insufficientHeat(requiredTemp);
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
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockECCFRecipes;
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

            StatCollector.translateToLocal("GT5U.ECCF.pressure") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(currentPressure)
                + EnumChatFormatting.RESET
                + " Pa",
            StatCollector.translateToLocal("GT5U.ECCF.pressure.required") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(requiredPressure)
                + EnumChatFormatting.RESET
                + " Pa",
            StatCollector.translateToLocal("GT5U.ECCF.pressure.threshold") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(pressureThreshold)
                + EnumChatFormatting.RESET
                + " Pa" ,
            StatCollector.translateToLocal("GT5U.ECCF.temperature") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(currentTemp)
                + EnumChatFormatting.RESET
                + " K" ,
            StatCollector.translateToLocal("GT5U.ECCF.temperature.required") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(requiredTemp)
                + EnumChatFormatting.RESET
                + " K",
            StatCollector.translateToLocal("GT5U.ECCF.temperature.threshold") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(tempThreshold)
                + EnumChatFormatting.RESET
                + " K"
        };
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

    private static final String ECCFPressureNBTTag = "ECCFPressure";
    private static final String ECCFTempNBTtag = "ECCFTemperature";

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble(ECCFPressureNBTTag, currentPressure);
        aNBT.setDouble(ECCFTempNBTtag, currentTemp);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        currentPressure = aNBT.getDouble(ECCFPressureNBTTag);
        currentTemp = aNBT.getDouble(ECCFTempNBTtag);
        super.loadNBTData(aNBT);
    }

    public void getDimConditions() {
        WorldProvider provider = this.getBaseMetaTileEntity()
            .getWorld().provider;
        String DimName = provider.getDimensionName();
        switch (DimName) {
            case "End":
                initialTemp = 220;
                initialPressure = 0;
                break;
            case "Nether":
                initialTemp = 900;
                initialPressure = 200000;
                break;
            case "Mercury":
                initialTemp = 440;
                initialPressure = 0;
                break;
            case "Venus":
                initialTemp = 740;
                initialPressure = 9200000;
                break;
            case "Overworld":
                initialTemp = 290;
                initialPressure = 101000;
                break;
            case "Moon":
                initialTemp = 250;
                initialPressure = 0;
                break;
            case "Mars":
                initialTemp = 215;
                initialPressure = 600;
                break;
            case "Phobos":
                initialTemp = 233;
                initialPressure = 0;
                break;
            case "Deimos":
                initialTemp = 233;
                initialPressure = 0;
                break;
            case "Asteroids":
                initialTemp = 200;
                initialPressure = 0;
                break;
            case "Io":
                initialTemp = 130;
                initialPressure = 0;
                break;
            case "Europa":
                initialTemp = 102;
                initialPressure = 0;
                break;
            case "Callisto":
                initialTemp = 134;
                initialPressure = 0;
                break;
            case "Ganymede":
                initialTemp = 132;
                initialPressure = 0;
                break;
            case "Miranda":
                initialTemp = 60;
                initialPressure = 0;
                break;
            case "Titan":
                initialTemp = 94;
                initialPressure = 146700;
                break;
            case "Oberon":
                initialTemp = 70;
                initialPressure = 0;
                break;
            case "Proteus":
                initialTemp = 50;
                initialPressure = 0;
                break;
            case "Pluto":
                initialTemp = 44;
                initialPressure = 1;
                break;
            case "Triton":
                initialTemp = 38;
                initialPressure = 1;
                break;
            case "Makemake":
                initialTemp = 30;
                initialPressure = 0;
                break;
            case "Ceres":
                initialTemp = 168;
                initialPressure = 0;
                break;
            case "Haumea":
                initialTemp = 32;
                initialPressure = 0;
                break;
            case "CentauriA":
                initialTemp = 1500;
                initialPressure = 0;
                break;
            case "VegaB":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "BarnardC":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "BarnardE":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "BarnardF":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "TCetiE":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Ross128b":
                initialTemp = 295;
                initialPressure = 101000;
                break;
            case "Ross128ba":
                initialTemp = 285;
                initialPressure = 101000;
                break;
            case "Kuiperbelt":
                initialTemp = 50;
                initialPressure = 0;
                break;
            case "Neper":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Maahes":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Anubis":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Horus":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Seth":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "MehenBelt":
                initialTemp = 300;
                initialPressure = 10000;
                break;
                // temporary
            case "Underdark":
                initialTemp = 270;
                initialPressure = 131000;
                break;
            default:
                initialTemp = 270;
                initialPressure = 101000;
                break;
        }
    }

    public void getCoolantTemperature(String name) {
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
        if (mMachine && (aTick % 1 == 0)) { // TODO change back to 20
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
                        getCoolantTemperature(coolantName);
                        boolean isCold = ((coolantName.equals("ic2coolant")) || (coolantName.equals("cryotheum"))
                            || (coolantName.equals("supercoolant")) || (coolantName.equals("molten.spacetime")));
                        boolean isHot = ((coolantName.equals("pyrotheum")) || (coolantName.equals("plasma.helium"))
                            || (coolantName.equals("rawstarmatter")) || (coolantName.equals("lava")));
                        // Apply coolant (linear function)
                        if (isCoolModule && isCold) {
                            currentTemp = Math.max(currentTemp - (double) mCoolantInputHatch.mFluid.amount / 10000 * coolantTemp, coolantTemp);
                            drain(mCoolantInputHatch, mCoolantInputHatch.mFluid, true);
                        }
                        if (isHeatModule && isHot) {
                            currentTemp = Math.min(currentTemp + (double) mCoolantInputHatch.mFluid.amount / 10000 * coolantTemp, coolantTemp);
                            drain(mCoolantInputHatch, mCoolantInputHatch.mFluid, true);
                        }
                    }
                }
            } else currentTemp = initialTemp;

            // Pressure calculation
            if (this.isCompressModule || this.isVacuumModule) {
                // returns values to atmosphere conditions
                currentPressure = (currentPressure - initialPressure) * coeffPressure + initialPressure;
                // Apply pressure changes
                if (mPressureEnergyHatch != null) {
                    drainAmountEU = mPressureEnergyHatch.getBaseMetaTileEntity().getStoredEU();
                    currentPressure += Math.pow(drainAmountEU, 0.7) * (isCompressModule ? 1 : 0);
                    currentPressure -= Math.pow(drainAmountEU, 0.7) * (isVacuumModule ? 1 : 0);
                    currentPressure = Math.max(currentPressure, 0);
                    mPressureEnergyHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(drainAmountEU, false);
                }
                // Apply lubricant depressurization
                leakCoeff = 0.3;
                // check if there is a hatch for lubricant and apply depressurization if there is too few lubricant
                if (mLubricantInputHatch != null) {
                    if (mLubricantInputHatch.mFluid != null) {
                        lubricantType = mLubricantInputHatch.mFluid.getUnlocalizedName();
                        mLubricantInputHatch.drain(5, true);
                    }
                    else currentPressure = currentPressure * (1-leakCoeff) + initialPressure * leakCoeff;
                }
            } else currentPressure = initialPressure;

            // Range check
            if (mMaxProgresstime != 0) {
                if ((Math.abs(currentTemp - requiredTemp) <= tempThreshold) || (Math.abs(currentPressure - requiredPressure) <= pressureThreshold)) {
                    stopMachine(SimpleShutDownReason.ofCritical("conditions.out.of.range"));
                }
            }
        }
    }
}
