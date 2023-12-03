package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialSifter
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialSifter> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialSifter> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialSifter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialSifter(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialSifter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Sifter";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Sifter")
                .addInfo("400% faster than single-block machines of the same voltage")
                .addInfo("Only uses 75% of the EU/t normally required").addInfo("Processes four items per voltage tier")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(5, 3, 5, false)
                .addController("Bottom Center").addCasingInfoMin("Sieve Grate", 18, false)
                .addCasingInfoMin("Sieve Casings", 35, false).addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialSifter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialSifter>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCCCC", "CMMMC", "CMMMC", "CMMMC", "CCCCC" },
                                            { "CCCCC", "CMMMC", "CMMMC", "CMMMC", "CCCCC" },
                                            { "CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialSifter.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(TAE.GTPP_INDEX(21)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 5))))
                    .addElement('M', ofBlock(ModBlocks.blockCasings2Misc, 6)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 2, 2, 0) && mCasing >= 35 && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(21);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.sifterRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())
                && (aBaseMetaTileEntity.getFrontFacing() != ForgeDirection.UP)
                && (aBaseMetaTileEntity.getCoverIDAtSide(ForgeDirection.UP) == 0)
                && (!aBaseMetaTileEntity.getOpacityAtSide(ForgeDirection.UP))) {
            final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
            if (tRandom.nextFloat() > 0.4) return;

            final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * 2;
            final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * 2;

            aBaseMetaTileEntity.getWorld().spawnParticle(
                    "smoke",
                    (aBaseMetaTileEntity.getXCoord() + xDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    aBaseMetaTileEntity.getYCoord() + 2.5f + (tRandom.nextFloat() * 1.2F),
                    (aBaseMetaTileEntity.getZCoord() + zDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    0.0,
                    0.0,
                    0.0);
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 5F).setEuModifier(0.75F)
                .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialSifter;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return true;
    }
}
