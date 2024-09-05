package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;

public class MTEElectricBlastFurnace extends MTEAbstractMultiFurnace<MTEElectricBlastFurnace>
    implements ISurvivalConstructable {

    private int mHeatingCapacity = 0;
    protected final ArrayList<MTEHatchOutput> mPollutionOutputHatches = new ArrayList<>();
    protected final FluidStack[] pollutionFluidStacks = { Materials.CarbonDioxide.getGas(1000),
        Materials.CarbonMonoxide.getGas(1000), Materials.SulfurDioxide.getGas(1000) };

    protected static final int CASING_INDEX = 11;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEElectricBlastFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEElectricBlastFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ttt", "tmt", "ttt" }, { "CCC", "C-C", "CCC" }, { "CCC", "C-C", "CCC" },
                    { "b~b", "bbb", "bbb" } }))
        .addElement(
            't',
            buildHatchAdder(MTEElectricBlastFurnace.class)
                .atLeast(
                    OutputHatch.withAdder(MTEElectricBlastFurnace::addOutputHatchToTopList)
                        .withCount(t -> t.mPollutionOutputHatches.size()))
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .addElement('m', Muffler.newAny(CASING_INDEX, 2))
        .addElement('C', ofCoil(MTEElectricBlastFurnace::setCoilLevel, MTEElectricBlastFurnace::getCoilLevel))
        .addElement(
            'b',
            buildHatchAdder(MTEElectricBlastFurnace.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .build();

    public MTEElectricBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEElectricBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEElectricBlastFurnace(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Furnace")
            .addInfo("Controller block for the Electric Blast Furnace")
            .addInfo("You can use some fluids to reduce recipe time. Place the circuit in the Input Bus")
            .addInfo("Each 900K over the min. Heat required reduces power consumption by 5% (multiplicatively)")
            .addInfo("Each 1800K over the min. Heat allows for an overclock to be upgraded to a perfect overclock.")
            .addInfo("That means the EBF will reduce recipe time by a factor 4 instead of 2 (giving 100% efficiency).")
            .addInfo("Additionally gives +100K for every tier past MV")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 4, 3, true)
            .addController("Front bottom")
            .addCasingInfoRange("Heat Proof Machine Casing", 0, 15, false)
            .addOtherStructurePart("Heating Coils", "Two middle Layers")
            .addEnergyHatch("Any bottom layer casing", 3)
            .addMaintenanceHatch("Any bottom layer casing", 3)
            .addMufflerHatch("Top middle", 2)
            .addInputBus("Any bottom layer casing", 3)
            .addInputHatch("Any bottom layer casing", 3)
            .addOutputBus("Any bottom layer casing", 3)
            .addOutputHatch("Fluid outputs, Any bottom layer casing")
            .addOutputHatch("Pollution gases (CO2/CO/SO2), Any top layer casing", 1)
            .addStructureInfo("Pollution gas output amount scales with Muffler Hatch tier")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][CASING_INDEX], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][CASING_INDEX] };
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.gregtechproxy.mPollutionEBFPerSecond;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IStructureDefinition<MTEElectricBlastFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(mHeatingCapacity)
                    .setHeatOC(true)
                    .setHeatDiscount(true);
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= mHeatingCapacity ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }
        };
    }

    public boolean addOutputHatchToTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mPollutionOutputHatches.add((MTEHatchOutput) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mHeatingCapacity = 0;

        setCoilLevel(HeatingCoilLevel.None);

        mPollutionOutputHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 3, 0)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        if (mMaintenanceHatches.size() != 1) return false;

        this.mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GTUtility.getTier(getMaxInputVoltage()) - 2);
        return true;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack tLiquid = aLiquid.copy();
        ArrayList<MTEHatchOutput> tOutputHatches;
        if (isPollutionFluid(tLiquid)) {
            tOutputHatches = this.mPollutionOutputHatches;
            multiplyPollutionFluidAmount(tLiquid);
        } else {
            tOutputHatches = this.mOutputHatches;
        }
        return dumpFluid(tOutputHatches, tLiquid, true) || dumpFluid(tOutputHatches, tLiquid, false);
    }

    protected boolean isPollutionFluid(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return false;
        for (FluidStack pollutionFluidStack : pollutionFluidStacks) {
            if (!fluidStack.isFluidEqual(pollutionFluidStack)) continue;
            return true;
        }
        return false;
    }

    @Override
    public List<? extends IFluidStore> getFluidOutputSlots(FluidStack[] toOutput) {
        if (Arrays.stream(toOutput)
            .anyMatch(this::isPollutionFluid)) {
            return filterValidMTEs(mPollutionOutputHatches);
        }
        return filterValidMTEs(mOutputHatches);
    }

    /**
     * @return 100 -> all released to air, 0 -> all dumped to hatch
     */
    public int getPollutionReduction() {
        int reduction = 100;
        for (MTEHatchMuffler tHatch : filterValidMTEs(mMufflerHatches)) {
            reduction = Math.min(tHatch.calculatePollutionReduction(100), reduction);
        }
        return reduction;
    }

    protected void multiplyPollutionFluidAmount(@Nonnull FluidStack fluid) {
        fluid.amount = fluid.amount * Math.min(100 - getPollutionReduction(), 100) / 100;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = getPollutionReduction();
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
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
            StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mHeatingCapacity)
                + EnumChatFormatting.RESET
                + " K",
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" };
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

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("isBussesSeparate")) {
            // backward compatibility
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
