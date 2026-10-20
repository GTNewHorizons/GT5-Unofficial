package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
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

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.registries.LHECoolantRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHeatExchanger extends MTEEnhancedMultiBlockBase<MTEHeatExchanger>
    implements ISurvivalConstructable, ICasingTextureProvider {

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
    private static final int BASE_SH_THRESHOLD = 4000; // L per second, scaled by the coolant
    private static final int SH_THRESHOLD_REDUCTION_PER_CONFIG = 150; // L per second per circuitry level (1-25)
    private static final float BASE_STEAM_MULTIPLIER = 20f; // scaled by the coolant

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
        final ImmutableMap.Builder<String, Object> vars = ImmutableMap.builder();
        vars.put("explode_seconds", formatNumber(dryHeatMaximum / 20))
            .put("threshold_reduction", formatNumber(100.0 * SH_THRESHOLD_REDUCTION_PER_CONFIG / BASE_SH_THRESHOLD))
            .put("steam_penalty", formatNumber(100.0 * penalty_per_config));
        putCoolantVars(vars, "lava", "lava");
        putCoolantVars(vars, "coolant", "ic2hotcoolant");
        putCoolantVars(vars, "salt", "molten.solarsalthot");
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.large_heat_exchanger"))
            .addMarkdown(new ResourceLocation("gregtech", "heat-exchanger"), vars.build())
            .beginStructureBlock(3, 4, 3, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("20-28", Casings.StableTitaniumMachineCasing.getLocalizedName(), false)
            .addCasing("2", Casings.TitaniumPipeCasing.getLocalizedName(), false)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputHatch("2+", StatCollector.translateToLocal("gt.mbtt.structure.heat_exchanger_input_hatch"), 1, 2)
            .addOutputHatch("2+", StatCollector.translateToLocal("gt.mbtt.structure.heat_exchanger_output_hatch"), 1, 3)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    /** Puts the SH threshold, max input and max SH steam output of a coolant into the tooltip vars. */
    private static void putCoolantVars(ImmutableMap.Builder<String, Object> vars, String prefix, String hotFluid) {
        // Same math as checkProcessing() and onRunningTick() at full efficiency without a circuit
        LHECoolantRegistry.LHECoolantInfo coolant = LHECoolantRegistry.getCoolant(hotFluid);
        int threshold = (int) (BASE_SH_THRESHOLD * coolant.superheatedThreshold);
        int maxInput = threshold * 2;
        vars.put(prefix + "_threshold", formatNumber(threshold))
            .put(prefix + "_max_input", formatNumber(maxInput))
            .put(prefix + "_max_output", formatNumber((int) (maxInput * BASE_STEAM_MULTIPLIER * coolant.steamMultiplier)));
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
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_HEAT_EXCHANGER,
            OVERLAY_FRONT_HEAT_EXCHANGER_GLOW,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE,
            OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return casingTexturePages[0][CASING_INDEX];
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

        superheated_threshold = BASE_SH_THRESHOLD; // default: must have 4000L per second to generate superheated steam
        float efficiency = 1f; // default: operate at 100% efficiency with no integrated circuitry
        int shs_reduction_per_config = SH_THRESHOLD_REDUCTION_PER_CONFIG; // reduce threshold 150L/s per circuitry level (1-25)
        float steam_output_multiplier = BASE_STEAM_MULTIPLIER; // default: multiply output by 4 * 10 (boosted x5)
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
                        GTLog.writeExplosionLog(this, "was too hot and had no more Distilled Water!");
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mOutputColdFluidHatch = null;
        mInputHotFluidHatch = null;
        mCasingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 3, 0, errors)) return;
        checkCasingMin(errors, mCasingAmount, 20);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    public boolean addColdFluidOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            addIfSmartInput(aMetaTileEntity);
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
            addIfSmartInput(aMetaTileEntity);
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
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.Progress.fmt.s",
                formatNumber(mProgresstime / 20),
                formatNumber(mMaxProgresstime / 20)),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.lhe.steam",
                (superheated ? "§c" : "§e") + formatNumber(superheated ? -2L * mEUt : -mEUt) + "§r"),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.problems.efficiency.fmt",
                getIdealStatus() - getRepairStatus(),
                mEfficiency / 100.0F + " %"),
            IGregTechDeviceInformation
                .encode("GT5U.infodata.lhe.superheated", (superheated ? "§c" : "§9") + superheated + "§r"),
            IGregTechDeviceInformation.encode("GT5U.infodata.lhe.threshold", formatNumber(superheated_threshold)),
            IGregTechDeviceInformation.encode("GT5U.multiblock.recipesDone.fmt", formatNumber(recipesDone)) };
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}
