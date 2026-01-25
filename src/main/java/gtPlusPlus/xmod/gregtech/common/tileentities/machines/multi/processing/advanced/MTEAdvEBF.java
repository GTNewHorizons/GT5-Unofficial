package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class MTEAdvEBF extends GTPPMultiBlockBase<MTEAdvEBF> implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String mHotFuelName = "Blazing Pyrotheum";
    public static String mCasingName = "Volcanus Casing";
    public static String mHatchName = "Pyrotheum Hatch";
    private static IStructureDefinition<MTEAdvEBF> STRUCTURE_DEFINITION = null;
    private int mCasing;
    private final ArrayList<MTEHatchCustomFluidBase> mPyrotheumHatches = new ArrayList<>();

    private HeatingCoilLevel mHeatingCapacity = HeatingCoilLevel.None;

    public MTEAdvEBF(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
    }

    public MTEAdvEBF(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
    }

    @Override
    public String getMachineType() {
        return "Blast Furnace";
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvEBF(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Factory Grade Advanced Blast Furnace")
            .addStaticParallelInfo(8)
            .addStaticSpeedInfo(2.2f)
            .addStaticEuEffInfo(0.9f)
            .addInfo("Consumes 10L of " + mHotFuelName + " per second during operation")
            .addInfo("Constructed exactly the same as a normal EBF")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addController("Bottom center")
            .addCasingInfoMin(mCasingName, 6, false)
            .addInputHatch("Any Casing", 1)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addOtherStructurePart(mHatchName, "Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mHeatingCapacity.getHeat())
                + EnumChatFormatting.RESET
                + " K");
    }

    @Override
    public IStructureDefinition<MTEAdvEBF> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAdvEBF>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                            { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEAdvEBF.class).adder(MTEAdvEBF::addPyrotheumHatch)
                            .hatchId(968)
                            .shouldReject(x -> !x.mPyrotheumHatches.isEmpty())
                            .casingIndex(CASING_TEXTURE_ID)
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEAdvEBF.class)
                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                            .casingIndex(CASING_TEXTURE_ID)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 11))))
                .addElement(
                    'H',
                    GTStructureChannels.HEATING_COIL
                        .use(activeCoils(ofCoil(MTEAdvEBF::setCoilLevel, MTEAdvEBF::getCoilLevel))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mPyrotheumHatches.clear();
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 3, 0) && mCasing >= 6 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
    }

    @Override
    public boolean checkHatch() {
        return super.checkHatch() && !mPyrotheumHatches.isEmpty();
    }

    private boolean addPyrotheumHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchCustomFluidBase && aMetaTileEntity.getBaseMetaTileEntity()
                .getMetaTileID() == 968) {
                return addToMachineListInternal(mPyrotheumHatches, aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mPyrotheumHatches)) tHatch.updateSlots();
        super.updateSlots();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedEBFActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedEBFActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAAdvancedEBF;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAAdvancedEBFGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                return recipe.mSpecialValue <= getCoilLevel().getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setHeatOC(true)
                    .setHeatDiscount(true)
                    .setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat((int) getCoilLevel().getHeat());
            }
        }.setSpeedBonus(1F / 2.2F)
            .setEuModifier(0.9F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAdvEBF;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Try dry Pyrotheum after all other logic
        if (this.mStartUpCheck < 0) {
            if (this.mMaxProgresstime > 0 && this.mProgresstime != 0 || this.getBaseMetaTileEntity()
                .hasWorkJustBeenEnabled()) {
                if (aTick % 20 == 0 || this.getBaseMetaTileEntity()
                    .hasWorkJustBeenEnabled()) {
                    if (!this.depleteInputFromRestrictedHatches(this.mPyrotheumHatches, 10)) {
                        this.causeMaintenanceIssue();
                        this.stopMachine(
                            ShutDownReasonRegistry.outOfFluid(new FluidStack(TFFluids.fluidPyrotheum, 10)));
                    }
                }
            }
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        aPlayer.addChatMessage(
            new ChatComponentTranslation(
                inputSeparation ? "interaction.separateBusses.enabled" : "interaction.separateBusses.disabled"));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("isBussesSeparate");
        }
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ADV_EBF_LOOP;
    }
}
