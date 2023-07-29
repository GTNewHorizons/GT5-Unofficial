package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

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

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GT_MetaTileEntity_Hatch_CustomFluidBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_Adv_EBF extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Adv_EBF>
        implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String mHotFuelName = "Blazing Pyrotheum";
    public static String mCasingName = "Volcanus Casing";
    public static String mHatchName = "Pyrotheum Hatch";
    private static IStructureDefinition<GregtechMetaTileEntity_Adv_EBF> STRUCTURE_DEFINITION = null;
    private int mCasing;
    private final ArrayList<GT_MetaTileEntity_Hatch_CustomFluidBase> mPyrotheumHatches = new ArrayList<>();

    private HeatingCoilLevel mHeatingCapacity;

    public GregtechMetaTileEntity_Adv_EBF(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
    }

    public GregtechMetaTileEntity_Adv_EBF(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
    }

    @Override
    public String getMachineType() {
        return "Blast Furnace";
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_EBF(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Factory Grade Advanced Blast Furnace")
                .addInfo("Speed: +120% | EU Usage: 90% | Parallel: 8")
                .addInfo("Consumes 10L of " + mHotFuelName + " per second during operation")
                .addInfo("Constructed exactly the same as a normal EBF").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().addController("Bottom center").addCasingInfoMin(mCasingName, 8, false)
                .addInputHatch("Any Casing", 1).addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1)
                .addOutputHatch("Any Casing", 1).addStructureHint(mHatchName, 1).addEnergyHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mHeatingCapacity.getHeat())
                + EnumChatFormatting.RESET
                + " K" };
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_Adv_EBF> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Adv_EBF>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                                    { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(GregtechMetaTileEntity_Adv_EBF.class)
                                            .adder(GregtechMetaTileEntity_Adv_EBF::addPyrotheumHatch).hatchId(968)
                                            .casingIndex(CASING_TEXTURE_ID).dot(1).build(),
                                    buildHatchAdder(GregtechMetaTileEntity_Adv_EBF.class).atLeast(
                                            InputBus,
                                            OutputBus,
                                            Maintenance,
                                            Energy,
                                            Muffler,
                                            InputHatch,
                                            OutputHatch).casingIndex(CASING_TEXTURE_ID).dot(1).build(),
                                    onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 11))))
                    .addElement(
                            'H',
                            ofCoil(
                                    GregtechMetaTileEntity_Adv_EBF::setCoilLevel,
                                    GregtechMetaTileEntity_Adv_EBF::getCoilLevel))
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
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mPyrotheumHatches.clear();
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 3, 0) && mCasing >= 8 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
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
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CustomFluidBase
                    && aMetaTileEntity.getBaseMetaTileEntity().getMetaTileID() == 968) {
                return addToMachineListInternal(mPyrotheumHatches, aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mPyrotheumHatches)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
        super.updateSlots();
    }

    private boolean depleteFuel(int aAmount) {
        for (final GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : this.mPyrotheumHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid == null || tLiquid.amount < aAmount) {
                    continue;
                }
                tLiquid = tHatch.drain(aAmount, false);
                if (tLiquid != null && tLiquid.amount >= aAmount) {
                    tLiquid = tHatch.drain(aAmount, true);
                    return tLiquid != null && tLiquid.amount >= aAmount;
                }
            }
        }
        return false;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                return recipe.mSpecialValue <= getCoilLevel().getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setHeatOC(true).setHeatDiscount(true)
                        .setRecipeHeat(recipe.mSpecialValue).setMultiHeat((int) getCoilLevel().getHeat());
            }
        }.setSpeedBonus(1F / 2.2F).setEuModifier(0.9F).setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiAdvEBF;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    private int mGraceTimer = 2;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Try dry Pyrotheum after all other logic
        if (this.mStartUpCheck < 0) {
            if (this.mMaxProgresstime > 0 && this.mProgresstime != 0
                    || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {
                if (aTick % 10 == 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {
                    if (!this.depleteInputFromRestrictedHatches(this.mPyrotheumHatches, 5)) {
                        if (mGraceTimer-- == 0) {
                            this.causeMaintenanceIssue();
                            this.stopMachine();
                            mGraceTimer = 2;
                        }
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
                        inputSeparation ? "interaction.separateBusses.enabled"
                                : "interaction.separateBusses.disabled"));
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
}
