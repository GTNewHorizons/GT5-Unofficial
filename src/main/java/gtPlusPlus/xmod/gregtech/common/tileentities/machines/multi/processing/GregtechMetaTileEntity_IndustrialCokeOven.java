package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

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

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialCokeOven extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialCokeOven> implements ISurvivalConstructable {

    private int mLevel = 0;
    private int mCasing;
    private int mCasing1;
    private int mCasing2;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialCokeOven> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialCokeOven(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialCokeOven(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Coke Oven";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Processes Logs and Coal into Charcoal and Coal Coke.")
                .addInfo("Controller Block for the Industrial Coke Oven")
                .addInfo("Gain 4% energy discount per voltage tier")
                .addInfo("Process 12x materials with Heat Resistant Casings")
                .addInfo("Or 24x materials with Heat Proof Casings").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 3, 3, true).addController("Front middle at bottom")
                .addCasingInfoMin("Structural Coke Oven Casings", 8, false)
                .addCasingInfoMin("Heat Resistant/Proof Coke Oven Casings", 8, false).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialCokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialCokeOven>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "C~C", "CCC", "CCC" }, }))
                    .addShape(
                            mName + "1",
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "aaa", "a-a", "aaa" },
                                            { "C~C", "CCC", "CCC" }, }))
                    .addShape(
                            mName + "2",
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "bbb", "b-b", "bbb" },
                                            { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialCokeOven.class)
                                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(1)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 1))))
                    .addElement(
                            'H',
                            ofChain(
                                    onElementPass(x -> ++x.mCasing1, ofBlock(ModBlocks.blockCasingsMisc, 2)),
                                    onElementPass(x -> ++x.mCasing2, ofBlock(ModBlocks.blockCasingsMisc, 3))))
                    .addElement('a', ofBlock(ModBlocks.blockCasingsMisc, 2))
                    .addElement('b', ofBlock(ModBlocks.blockCasingsMisc, 3)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize == 1) buildPiece(mName + "1", stackSize, hintsOnly, 1, 2, 0);
        else buildPiece(mName + "2", stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (stackSize.stackSize == 1)
            return survivialBuildPiece(mName + "1", stackSize, 1, 2, 0, elementBudget, env, false, true);
        else return survivialBuildPiece(mName + "2", stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mCasing1 = 0;
        mCasing2 = 0;
        mLevel = 0;
        if (checkPiece(mName, 1, 2, 0)) {
            if (mCasing1 == 8) mLevel = 1;
            if (mCasing2 == 8) mLevel = 2;
            return mLevel > 0 && mCasing >= 8 && checkHatch();
        }
        return false;
    }

    @Override
    public String getSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP.toString();
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
        return TAE.GTPP_INDEX(1);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes;
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
    }

    @Override
    public int getMaxParallelRecipes() {
        return this.mLevel * 12;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return (100 - (GT_Utility.getTier(this.getMaxInputVoltage()) * 4));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCokeOven;
    }

    @Override
    public int getAmountOfOutputs() {
        return 24;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }
}
