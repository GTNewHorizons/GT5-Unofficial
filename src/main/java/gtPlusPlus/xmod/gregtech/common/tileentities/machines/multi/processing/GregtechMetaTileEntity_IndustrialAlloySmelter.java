package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_IndustrialAlloySmelter extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialAlloySmelter> implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    private HeatingCoilLevel mHeatingCapacity;
    private int mLevel = 0;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialAlloySmelter> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialAlloySmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
    }

    public GregtechMetaTileEntity_IndustrialAlloySmelter(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialAlloySmelter(this.mName);
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.alloySmelterRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialAlloySmelter;
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
    public String getMachineType() {
        return "Alloy Smelter";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Alloy Smelter")
                .addInfo("Gains one parallel per voltage tier").addInfo("Gains one multiplier per coil tier")
                .addInfo("Parallel = Tier * Coil Tier").addInfo("Gains 5% speed bonus per coil tier")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 5, 3, true)
                .addController("Bottom center").addCasingInfoMin("Inconel Reinforced Casings", 8, false)
                .addOtherStructurePart("Integral Encasement V", "Middle Layer")
                .addOtherStructurePart("Heating Coils", "Above and below Integral Encasements")
                .addInputBus("Any Inconel Reinforced Casing", 1).addOutputBus("Any Inconel Reinforced Casing", 1)
                .addEnergyHatch("Any Inconel Reinforced Casing", 1)
                .addMaintenanceHatch("Any Inconel Reinforced Casing", 1)
                .addMufflerHatch("Any Inconel Reinforced Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialAlloySmelter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialAlloySmelter>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "VVV", "V-V", "VVV" }, { "HHH", "H-H", "HHH" },
                                            { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialAlloySmelter.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(CASING_TEXTURE_ID).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 1))))
                    .addElement(
                            'H',
                            ofCoil(
                                    GregtechMetaTileEntity_IndustrialAlloySmelter::setCoilLevel,
                                    GregtechMetaTileEntity_IndustrialAlloySmelter::getCoilLevel))
                    .addElement('V', ofBlock(ModBlocks.blockCasingsTieredGTPP, 4)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mLevel = 0;
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 4, 0) && mCasing >= 8
                && getCoilLevel() != HeatingCoilLevel.None
                && (mLevel = getCoilLevel().getTier() + 1) > 0
                && checkHatch();
    }

    @Override
    public int getMaxParallelRecipes() {
        return (this.mLevel * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setSpeedBoost(100F / (100F + 5F * mLevel))
                        .setHeatOC(true).setRecipeHeat(0)
                        // Need to multiply by 2 because heat OC is done only once every 1800 and this one does it once
                        // every
                        // 900
                        .setMachineHeat((int) (getCoilLevel().getHeat() * 2));
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }
}
