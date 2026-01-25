package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.registries.LHECoolantRegistry;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAdvHeatExchanger extends GTPPMultiBlockBase<MTEAdvHeatExchanger> implements ISurvivalConstructable {

    private static final int CASING_INDEX = TAE.getIndexFromPage(1, 12);
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTEAdvHeatExchanger> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvHeatExchanger>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { " ccc ", "cCCCc", "cCCCc", "cCCCc", " ccc " },
                    { " ccc ", "cPPPc", "cPPPc", "cPPPc", " ccc " }, { " ccc ", "cPPPc", "cPPPc", "cPPPc", " ccc " },
                    { " ccc ", "cPPPc", "cPPPc", "cPPPc", " ccc " }, { " ccc ", "cPPPc", "cPPPc", "cPPPc", " ccc " },
                    { " c~c ", "cPPPc", "cPPPc", "cPPPc", " ccc " }, { " hhh ", "hHHHh", "hHHHh", "hHHHh", " hhh " },
                    { " f f ", "f   f", "     ", "f   f", " f f " },
                    { " f f ", "f   f", "     ", "f   f", " f f " }, }))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement('f', ofBlock(getFrame(), 0))
        .addElement(
            'C',
            ofChain(
                buildHatchAdder(MTEAdvHeatExchanger.class).atLeast(AdvHEHatches.ColdOutputHatch)
                    .hint(2)
                    .casingIndex(CASING_INDEX)
                    .build(),
                onElementPass(MTEAdvHeatExchanger::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 14))))
        .addElement(
            'H',
            ofChain(
                buildHatchAdder(MTEAdvHeatExchanger.class).atLeast(AdvHEHatches.HotInputHatch)
                    .hint(3)
                    .casingIndex(CASING_INDEX)
                    .build(),
                onElementPass(MTEAdvHeatExchanger::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 14))))
        .addElement(
            'h',
            ofChain(
                buildHatchAdder(MTEAdvHeatExchanger.class).atLeast(InputHatch, OutputHatch, Maintenance)
                    .hint(1)
                    .casingIndex(CASING_INDEX)
                    .build(),
                onElementPass(MTEAdvHeatExchanger::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 14))))
        .addElement(
            'c',
            ofChain(onElementPass(MTEAdvHeatExchanger::onCasingAdded, ofBlock(ModBlocks.blockSpecialMultiCasings, 14))))
        .build();
    public static float penalty_per_config = 0.015f; // penalize 1.5% efficiency per circuitry level (1-25)

    private MTEHatchInput mInputHotFluidHatch;
    private MTEHatchOutput mOutputColdFluidHatch;
    private boolean superheated = false;
    private int superheated_threshold = 0;
    private float water;
    private int mCasingAmount;

    public MTEAdvHeatExchanger(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvHeatExchanger(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Works as fast as 32 Large Heat Exchangers")
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
                    + " 32,000 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 64,000 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 5,120,000 SH Steam/s")
            .addInfo(
                EnumChatFormatting.RED + "Hot Coolant"
                    + EnumChatFormatting.GRAY
                    + " : SH Threshold"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " 25,600 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 51,200 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 10,240,000 SH Steam/s")
            .addInfo(
                EnumChatFormatting.RED + "Hot Solar Salt"
                    + EnumChatFormatting.GRAY
                    + " : SH Threshold"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + " 5,120 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Input"
                    + EnumChatFormatting.RED
                    + " 10,240 L/s"
                    + EnumChatFormatting.GRAY
                    + " : Max Output"
                    + EnumChatFormatting.WHITE
                    + " 10,240,000 SH Steam/s")
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
            .beginStructureBlock(5, 9, 5, false)
            .addController("Front bottom")
            .addCasingInfoMin("Reinforced Heat Exchanger Casing", 90, false)
            .addOtherStructurePart("Tungstensteel Pipe Casing", "Center 3x5x3 (45 blocks)")
            .addMaintenanceHatch("Any casing", 1)
            .addInputHatch("Hot fluid, bottom center casing", 2)
            .addInputHatch("Distilled water, any bottom layer casing", 1)
            .addOutputHatch("Cold fluid, top center casing", 3)
            .addOutputHatch("Steam/SH Steam, any bottom layer casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        superheated = aNBT.getBoolean("superheated");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("superheated", superheated);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedHeatExchangerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedHeatExchangerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedHeatExchanger;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedHeatExchangerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_INDEX;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !r.isUpsideDown() && !f.isVerticallyFliped();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        FluidStack hotFluid = null;
        if (mInputHotFluidHatch instanceof MTEHatchInputME inputME) {
            FluidStack[] fluids = inputME.getStoredFluids();
            if (fluids.length > 0) {
                hotFluid = fluids[0];
            }
        } else {
            hotFluid = mInputHotFluidHatch.getFluid();
        }
        if (hotFluid == null) return CheckRecipeResultRegistry.SUCCESSFUL;

        int fluidAmountToConsume = hotFluid.amount; // how much fluid is in hatch

        // The XL LHE works as fast as 32 regular LHEs. These are the comments from the original LHE,
        // with changes where the values needed to change for the 32x speed multiplier
        superheated_threshold = 128000; // default: must have 4000L -> 128000L per second to generate superheated steam
        float efficiency = 1f; // default: operate at 100% efficiency with no integrated circuitry
        int shs_reduction_per_config = 4800; // reduce threshold 150L -> 4800L per second per circuitry level (1-25)
        float steam_output_multiplier = 20f; // default: multiply output by 4 * 10 (boosted x5)
        float penalty = 0.0f; // penalty to apply to output based on circuitry level (1-25).

        // Do we have an integrated circuit with a valid configuration?
        if (mInventory[1] != null && mInventory[1].getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, apply the penalty and reduced threshold.
                penalty = (circuit_config - 1) * penalty_per_config;
                superheated_threshold -= (shs_reduction_per_config * (circuit_config - 1));
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

        // set the internal superheated flag if we have enough hot fluid. Used in the onRunningTick method.
        this.superheated = fluidAmountToConsume >= superheated_threshold;

        // Don't consume too much hot fluid per second, maximum is 2x SH threshold.
        fluidAmountToConsume = Math.min(fluidAmountToConsume, superheated_threshold * 2);
        // the 3-arg drain will work on both normal hatch and ME hatch
        mInputHotFluidHatch
            .drain(ForgeDirection.UNKNOWN, new FluidStack(hotFluid.getFluid(), fluidAmountToConsume), true);
        mOutputColdFluidHatch.fill(coolant.getColdFluid(fluidAmountToConsume), true);

        this.mMaxProgresstime = 20;
        this.lEUt = (long) (fluidAmountToConsume * steam_output_multiplier * efficiency);
        this.mEfficiencyIncrease = 80;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int useWater(float input) {
        water = water + input;
        int usage = (int) water;
        water = water - usage;
        return usage;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            int tGeneratedEU = (int) (this.lEUt * 2L * this.mEfficiency / 10000L); // APPROXIMATELY how much steam to
                                                                                   // generate.
            if (tGeneratedEU > 0) {

                if (superheated) tGeneratedEU /= 2; // We produce half as much superheated steam if necessary

                int distilledConsumed = useWater(tGeneratedEU / 160f); // how much distilled water to consume
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
                } else {
                    GTLog.exp.println(this.mName + " had no more Distilled water!");
                    explodeMultiblock(); // Generate crater
                }
                endRecipeProcessing();
            }
            return true;
        }
        return true;
    }

    @Override
    public IStructureDefinition<MTEAdvHeatExchanger> getStructureDefinition() {
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
        return checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0) && mCasingAmount >= 90 && mMaintenanceHatches.size() == 1;
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
        return new MTEAdvHeatExchanger(this.mName);
    }

    @Override
    public boolean isGivingInformation() {
        return super.isGivingInformation();
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocal("GT5U.LHE.superheated") + ": "
                + (superheated ? EnumChatFormatting.RED : EnumChatFormatting.BLUE)
                + superheated
                + EnumChatFormatting.RESET);

        info.add(
            StatCollector.translateToLocal("GT5U.LHE.superheated") + " "
                + StatCollector.translateToLocal("GT5U.LHE.threshold")
                + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(superheated_threshold)
                + EnumChatFormatting.RESET);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 5, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 5, 0, elementBudget, env, false, true);
    }

    @Override
    public String getMachineType() {
        return "Heat Exchanger, WWXL";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    private static Block sFrame;

    public static Block getFrame() {
        if (sFrame == null) {
            sFrame = BlockBaseModular.getMaterialBlock(MaterialsAlloy.TALONITE, BlockTypes.FRAME);
        }
        return sFrame;
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

    private enum AdvHEHatches implements IHatchElement<MTEAdvHeatExchanger> {

        HotInputHatch(MTEAdvHeatExchanger::addHotFluidInputToMachineList, MTEHatchInput.class) {

            @Override
            public long count(MTEAdvHeatExchanger t) {
                if (t.mInputHotFluidHatch == null) return 0;
                return 1;
            }
        },
        ColdOutputHatch(MTEAdvHeatExchanger::addColdFluidOutputToMachineList, MTEHatchOutput.class) {

            @Override
            public long count(MTEAdvHeatExchanger t) {
                if (t.mOutputColdFluidHatch == null) return 0;
                return 1;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEAdvHeatExchanger> adder;

        @SafeVarargs
        AdvHEHatches(IGTHatchAdder<MTEAdvHeatExchanger> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEAdvHeatExchanger> adder() {
            return adder;
        }
    }
}
