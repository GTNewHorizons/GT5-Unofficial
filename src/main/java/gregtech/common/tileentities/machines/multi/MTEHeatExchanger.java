package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.registries.LHECoolantRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;

public class MTEHeatExchanger extends MTEEnhancedMultiBlockBase<MTEHeatExchanger> implements ISurvivalConstructable {

    private int dryHeatCounter = 0; // Counts up to dryHeatMaximum to check for explosion conditions
    private static final int dryHeatMaximum = 2000; // 2000 ticks = 100 seconds
    private static final int CASING_INDEX = 50;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEHeatExchanger> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEHeatExchanger>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "cCc", "ccc" }, { "ccc", "cPc", "ccc" }, { "ccc", "cPc", "ccc" },
                    { "c~c", "cHc", "ccc" }, }))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings2, 14))
        .addElement(
            'C',
            OutputHatch.withAdder(MTEHeatExchanger::addColdFluidOutputToMachineList)
                .withCount(t -> t.mOutputColdFluidHatch.isValid() ? 1 : 0)
                .newAny(CASING_INDEX, 3))
        .addElement(
            'H',
            InputHatch.withAdder(MTEHeatExchanger::addHotFluidInputToMachineList)
                .withCount(t -> t.mInputHotFluidHatch.isValid() ? 1 : 0)
                .newAny(CASING_INDEX, 2))
        .addElement(
            'c',
            buildHatchAdder(MTEHeatExchanger.class).atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEHeatExchanger::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, (byte) 2))))
        .build();
    public static float penalty_per_config = 0.015f; // penalize 1.5% efficiency per circuitry level (1-25)

    private MTEHatchInput mInputHotFluidHatch;
    private MTEHatchOutput mOutputColdFluidHatch;
    private boolean superheated = false;
    private int superheated_threshold = 0;
    /**
     * How much more steam we can make without draining real water. Unit is (1L/GT_Values.STEAM_PER_WATER)
     */
    private int steamBudget;

    private int mCasingAmount;

    public MTEHeatExchanger(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEHeatExchanger(String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Heat Exchanger, LHE")
            .addInfo(
                "Inputs are" + EnumChatFormatting.RED
                    + " Lava"
                    + EnumChatFormatting.GRAY
                    + ","
                    + EnumChatFormatting.RED
                    + " Hot Coolant"
                    + EnumChatFormatting.GRAY
                    + ", or"
                    + EnumChatFormatting.RED
                    + " Hot Solar Salt")
            .addInfo(
                "Outputs are" + EnumChatFormatting.BLUE
                    + " Pahoehoe Lava"
                    + EnumChatFormatting.GRAY
                    + ","
                    + EnumChatFormatting.BLUE
                    + " IC2 Coolant"
                    + EnumChatFormatting.GRAY
                    + ", or"
                    + EnumChatFormatting.BLUE
                    + " Cold Solar Salt")
            .addInfo(
                "Converts Distilled Water into" + EnumChatFormatting.WHITE
                    + " Steam"
                    + EnumChatFormatting.GRAY
                    + " or"
                    + EnumChatFormatting.WHITE
                    + " SH Steam"
                    + EnumChatFormatting.GRAY
                    + " in the process")
            .addInfo(
                "Outputs" + EnumChatFormatting.WHITE
                    + " SH Steam"
                    + EnumChatFormatting.GRAY
                    + " if the input rate of hot fluid is above a certain"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " threshold")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.RED + "Lava"
                    + EnumChatFormatting.GRAY
                    + " : SH Threshold"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " 1,000 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 2,000 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 160,000 SH Steam/s")
            .addInfo(
                EnumChatFormatting.RED + "Hot Coolant"
                    + EnumChatFormatting.GRAY
                    + " : SH Threshold"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " 800 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 1,600 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 320,000 SH Steam/s")
            .addInfo(
                EnumChatFormatting.RED + "Hot Solar Salt"
                    + EnumChatFormatting.GRAY
                    + " : SH Threshold"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " 160 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 320 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 320,000 SH Steam/s")
            .addSeparator()
            .addInfo("A circuit in the controller lowers the SH threshold at the cost of steam")
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + "3.75%"
                    + EnumChatFormatting.GRAY
                    + " reduced SH threshold and"
                    + EnumChatFormatting.WHITE
                    + " 1.5%"
                    + EnumChatFormatting.GRAY
                    + " reduced steam per circuit over 1")
            .beginStructureBlock(3, 4, 3, false)
            .addController("Front bottom")
            .addCasingInfoRange("Stable Titanium Machine Casing", 20, 28, false)
            .addOtherStructurePart("Titanium Pipe Casing", "Center 2 blocks")
            .addMaintenanceHatch("Any casing", 1)
            .addInputHatch("Hot Fluid, bottom center casing", 2)
            .addInputHatch("Distilled water, any casing", 1)
            .addOutputHatch("Cold Fluid, top center casing", 3)
            .addOutputHatch("Steam/SH Steam, any casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        superheated = aNBT.getBoolean("superheated");
        steamBudget = aNBT.getInteger("steamBudget");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("superheated", superheated);
        aNBT.setInteger("steamBudget", steamBudget);
        super.saveNBTData(aNBT);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !r.isUpsideDown() && !f.isVerticallyFliped();
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        FluidStack hotFluid = null;
        if (mInputHotFluidHatch instanceof MTEHatchInputME inputME) {
            FluidStack[] fluids = inputME.getStoredFluids();
            if (fluids.length > 0) {
                hotFluid = fluids[0];
            }
        } else {
            hotFluid = mInputHotFluidHatch.getFluid();
        }

        if (hotFluid == null) return CheckRecipeResultRegistry.NO_RECIPE;

        int fluidAmountToConsume = hotFluid.amount; // how much fluid is in hatch

        superheated_threshold = 4000; // default: must have 4000L per second to generate superheated steam
        float efficiency = 1f; // default: operate at 100% efficiency with no integrated circuitry
        int shs_reduction_per_config = 150; // reduce threshold 150L/s per circuitry level (1-25)
        float steam_output_multiplier = 20f; // default: multiply output by 4 * 10 (boosted x5)
        float penalty = 0.0f; // penalty to apply to output based on circuitry level (1-25).

        // Do we have an integrated circuit with a valid configuration?
        if (mInventory[1] != null && mInventory[1].getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, apply the penalty and reduce the threshold.
                penalty = (circuit_config - 1) * penalty_per_config;
                superheated_threshold -= shs_reduction_per_config * (circuit_config - 1);
            }
        }

        efficiency -= penalty;

        var coolant = LHECoolantRegistry.getCoolant(hotFluid.getFluid());

        if (coolant == null) {
            superheated_threshold = 0;
            return CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            steam_output_multiplier *= coolant.steamMultiplier;
            superheated_threshold *= coolant.superheatedThreshold;
        }

        // set the internal superheated flag if we have
        // enough hot fluid. Used in the onRunningTick method.
        superheated = fluidAmountToConsume >= superheated_threshold;

        // Don't consume too much hot fluid per second
        fluidAmountToConsume = Math.min(fluidAmountToConsume, superheated_threshold * 2);
        // the 3-arg drain will work on both normal hatch and ME hatch
        mInputHotFluidHatch
            .drain(ForgeDirection.UNKNOWN, new FluidStack(hotFluid.getFluid(), fluidAmountToConsume), true);
        mOutputColdFluidHatch.fill(coolant.getColdFluid(fluidAmountToConsume), true);

        this.mMaxProgresstime = 20;
        this.mEUt = (int) (fluidAmountToConsume * steam_output_multiplier * efficiency);
        this.mEfficiencyIncrease = 80;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int useWater(int steam) {
        steamBudget -= steam;
        int usage = -Math.min(0, Math.floorDiv(steamBudget, GTValues.STEAM_PER_WATER));
        // still subtract, because usage will be a negative number
        steamBudget += usage * GTValues.STEAM_PER_WATER;
        return usage;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0) {
            int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 10000L); // APPROXIMATELY how much steam to
                                                                                   // generate.
            if (tGeneratedEU > 0) {

                if (superheated) tGeneratedEU /= 2; // We produce half as much superheated steam if necessary

                int distilledConsumed = useWater(tGeneratedEU); // how much distilled water to consume
                // tGeneratedEU = distilledConsumed * 160; // EXACTLY how much steam to generate, producing a perfect
                // 1:160 ratio with distilled water consumption

                FluidStack distilledStack = GTModHandler.getDistilledWater(distilledConsumed);
                startRecipeProcessing();
                if (depleteInput(distilledStack)) // Consume the distilled water
                {
                    if (superheated) {
                        addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", tGeneratedEU)); // Generate
                                                                                                     // superheated
                                                                                                     // steam
                    } else {
                        addOutput(Materials.Steam.getGas(tGeneratedEU)); // Generate regular steam
                    }
                    dryHeatCounter = 0;
                } else {
                    if (dryHeatCounter < dryHeatMaximum) {
                        dryHeatCounter += 1;
                    } else {
                        GTLog.exp.println(this.mName + " was too hot and had no more Distilled Water!");
                        explodeMultiblock(); // Generate crater
                    }
                }
                endRecipeProcessing();
            }
            return true;
        }
        return true;
    }

    @Override
    public IStructureDefinition<MTEHeatExchanger> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mOutputColdFluidHatch = null;
        mInputHotFluidHatch = null;
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 3, 0) && mCasingAmount >= 20 && mMaintenanceHatches.size() == 1;
    }

    public boolean addColdFluidOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mOutputColdFluidHatch = (MTEHatchOutput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addHotFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = getRecipeMap();
            mInputHotFluidHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHeatExchanger(this.mName);
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + " "
                + StatCollector.translateToLocal("GT5U.LHE.steam")
                + ": "
                + (superheated ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW)
                + formatNumber(superheated ? -2L * mEUt : -mEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            StatCollector.translateToLocal("GT5U.LHE.superheated") + ": "
                + (superheated ? EnumChatFormatting.RED : EnumChatFormatting.BLUE)
                + superheated
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.LHE.superheated") + " "
                + StatCollector.translateToLocal("GT5U.LHE.threshold")
                + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(superheated_threshold)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + recipesDone
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void startRecipeProcessing() {
        super.startRecipeProcessing();
        if (mInputHotFluidHatch instanceof IRecipeProcessingAwareHatch aware && mInputHotFluidHatch.isValid()) {
            aware.startRecipeProcessing();
        }
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        if (mInputHotFluidHatch instanceof IRecipeProcessingAwareHatch aware && mInputHotFluidHatch.isValid()) {
            aware.endRecipeProcessing(this);
        }
    }
}
