package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

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

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_HeatExchanger extends
    GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_HeatExchanger> implements ISurvivalConstructable {

    private int dryHeatCounter = 0; // Counts up to dryHeatMaximum to check for explosion conditions
    private static final int dryHeatMaximum = 2000; // 2000 ticks = 100 seconds
    private static final int CASING_INDEX = 50;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_HeatExchanger> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_HeatExchanger>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccc", "cCc", "ccc" }, { "ccc", "cPc", "ccc" }, { "ccc", "cPc", "ccc" },
                    { "c~c", "cHc", "ccc" }, }))
        .addElement('P', ofBlock(GregTech_API.sBlockCasings2, 14))
        .addElement(
            'C',
            OutputHatch.withAdder(GT_MetaTileEntity_HeatExchanger::addColdFluidOutputToMachineList)
                .withCount(t -> isValidMetaTileEntity(t.mOutputColdFluidHatch) ? 1 : 0)
                .newAny(CASING_INDEX, 3))
        .addElement(
            'H',
            OutputHatch.withAdder(GT_MetaTileEntity_HeatExchanger::addHotFluidInputToMachineList)
                .withCount(t -> isValidMetaTileEntity(t.mInputHotFluidHatch) ? 1 : 0)
                .newAny(CASING_INDEX, 3))
        .addElement(
            'c',
            buildHatchAdder(GT_MetaTileEntity_HeatExchanger.class)
                .atLeast(InputBus, InputHatch, OutputBus, OutputHatch, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_HeatExchanger::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, (byte) 2))))
        .build();
    public static float penalty_per_config = 0.015f; // penalize 1.5% efficiency per circuitry level (1-25)

    private GT_MetaTileEntity_Hatch_Input mInputHotFluidHatch;
    private GT_MetaTileEntity_Hatch_Output mOutputColdFluidHatch;
    private boolean superheated = false;
    private int superheated_threshold = 0;
    /**
     * How much more steam we can make without draining real water. Unit is (1L/GT_Values.STEAM_PER_WATER)
     */
    private int steamBudget;

    private int mCasingAmount;

    public GT_MetaTileEntity_HeatExchanger(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_HeatExchanger(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Heat Exchanger")
            .addInfo("Controller Block for the Large Heat Exchanger")
            .addInfo("More complicated than a Fusion Reactor. Seriously")
            .addInfo("Inputs are Hot Coolant or Lava")
            .addInfo("Outputs Coolant or Pahoehoe Lava and SH Steam/Steam")
            .addInfo("Read the wiki article to understand how it works")
            .addInfo("Then go to the Discord to understand the wiki")
            .addSeparator()
            .beginStructureBlock(3, 4, 3, false)
            .addController("Front bottom")
            .addCasingInfoRange("Stable Titanium Machine Casing", 20, 32, false)
            .addOtherStructurePart("Titanium Pipe Casing", "Center 2 blocks")
            .addMaintenanceHatch("Any casing", 1)
            .addInputHatch("Hot fluid, bottom center", 2)
            .addInputHatch("Distilled water, any casing", 1)
            .addOutputHatch("Cold fluid, top center", 3)
            .addOutputHatch("Steam/SH Steam, any casing", 1)
            .toolTipFinisher("Gregtech");
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !r.isUpsideDown() && !f.isVerticallyFliped();
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (mInputHotFluidHatch.getFluid() == null) return true;

        int fluidAmountToConsume = mInputHotFluidHatch.getFluidAmount(); // how much fluid is in hatch

        superheated_threshold = 4000; // default: must have 4000L per second to generate superheated steam
        float efficiency = 1f; // default: operate at 100% efficiency with no integrated circuitry
        int shs_reduction_per_config = 150; // reduce threshold 150L/s per circuitry level (1-25)
        float steam_output_multiplier = 20f; // default: multiply output by 4 * 10 (boosted x5)
        float penalty = 0.0f; // penalty to apply to output based on circuitry level (1-25).
        boolean do_lava = false;
        boolean do_coolant = false;
        boolean do_solarSalt = false;

        // Do we have an integrated circuit with a valid configuration?
        if (mInventory[1] != null && mInventory[1].getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, apply the penalty and reduce the threshold.
                penalty = (circuit_config - 1) * penalty_per_config;
                superheated_threshold -= (shs_reduction_per_config * (circuit_config - 1));
            }
        }
        efficiency -= penalty;

        // If we're working with lava, adjust the threshold and multipliers accordingly.
        if (GT_ModHandler.isLava(mInputHotFluidHatch.getFluid())) {
            steam_output_multiplier /= 5f; // lava is not boosted
            superheated_threshold /= 4f; // unchanged
            do_lava = true;
        } else if (mInputHotFluidHatch.getFluid()
            .isFluidEqual(FluidRegistry.getFluidStack("ic2hotcoolant", 1))) {
                steam_output_multiplier /= 2f; // was boosted x2 on top of x5 -> total x10 ->
                                               // nerf with this code back to 5x
                superheated_threshold /= 5f; // 10x smaller since the Hot Things production in
                                             // reactor is the same.
                do_coolant = true;
            } else if (mInputHotFluidHatch.getFluid()
                .isFluidEqual(FluidRegistry.getFluidStack("molten.solarsalthot", 1))) {
                    steam_output_multiplier *= 2.5f; // Solar Salt:Steam value is 5x higher than Hot
                                                     // Coolant's value
                    superheated_threshold /= 25f; // Given that, multiplier is 5x higher and
                                                  // threshold is 5x lower
                    do_solarSalt = true;
                } else {
                    // If we're working with neither, fail out
                    superheated_threshold = 0;
                    return false;
                }

        superheated = fluidAmountToConsume >= superheated_threshold; // set the internal superheated flag if we have
                                                                     // enough hot fluid. Used in the
        // onRunningTick method.
        fluidAmountToConsume = Math.min(fluidAmountToConsume, superheated_threshold * 2); // Don't consume too much hot
                                                                                          // fluid per second
        mInputHotFluidHatch.drain(fluidAmountToConsume, true);
        this.mMaxProgresstime = 20;
        this.mEUt = (int) (fluidAmountToConsume * steam_output_multiplier * efficiency);
        if (do_lava) {
            mOutputColdFluidHatch.fill(FluidRegistry.getFluidStack("ic2pahoehoelava", fluidAmountToConsume), true);
        } else if (do_coolant) {
            mOutputColdFluidHatch.fill(FluidRegistry.getFluidStack("ic2coolant", fluidAmountToConsume), true);
        } else if (do_solarSalt) {
            mOutputColdFluidHatch.fill(FluidRegistry.getFluidStack("molten.solarsaltcold", fluidAmountToConsume), true);
        } else {
            return false;
        }
        this.mEfficiencyIncrease = 80;
        return true;
    }

    private int useWater(int steam) {
        steamBudget -= steam;
        int usage = -Math.min(0, Math.floorDiv(steamBudget, GT_Values.STEAM_PER_WATER));
        // still subtract, because usage will be a negative number
        steamBudget += usage * GT_Values.STEAM_PER_WATER;
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

                FluidStack distilledStack = GT_ModHandler.getDistilledWater(distilledConsumed);
                if (depleteInput(distilledStack)) // Consume the distilled water
                {
                    if (superheated) {
                        addOutput(FluidRegistry.getFluidStack("ic2superheatedsteam", tGeneratedEU)); // Generate
                                                                                                     // superheated
                                                                                                     // steam
                    } else {
                        addOutput(GT_ModHandler.getSteam(tGeneratedEU)); // Generate regular steam
                    }
                    dryHeatCounter = 0;
                } else {
                    if (dryHeatCounter < dryHeatMaximum) {
                        dryHeatCounter += 1;
                    } else {
                        GT_Log.exp.println(this.mName + " was too hot and had no more Distilled Water!");
                        explodeMultiblock(); // Generate crater
                    }
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_HeatExchanger> getStructureDefinition() {
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mOutputColdFluidHatch = (GT_MetaTileEntity_Hatch_Output) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addHotFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            mInputHotFluidHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_HeatExchanger(this.mName);
    }

    @Override
    public boolean isGivingInformation() {
        return super.isGivingInformation();
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + " "
                + StatCollector.translateToLocal("GT5U.LHE.steam")
                + ": "
                + (superheated ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW)
                + GT_Utility.formatNumbers(superheated ? -2 * mEUt : -mEUt)
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
                + GT_Utility.formatNumbers(superheated_threshold)
                + EnumChatFormatting.RESET };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }
}
