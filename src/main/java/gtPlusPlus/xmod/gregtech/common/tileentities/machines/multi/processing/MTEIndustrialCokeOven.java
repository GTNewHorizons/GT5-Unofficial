package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

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
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialCokeOven extends GTPPMultiBlockBase<MTEIndustrialCokeOven> implements ISurvivalConstructable {

    private int tier = 0;
    private int mCasing;
    private int mCasing1;
    private int mCasing2;
    private static IStructureDefinition<MTEIndustrialCokeOven> STRUCTURE_DEFINITION = null;

    public MTEIndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCokeOven(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCokeOven(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Coke Oven, ICO";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Processes Logs and Coal into Charcoal and Coal Coke.")
            .addInfo(TooltipHelper.parallelText(18) + " Parallels with Heat Resistant Casings")
            .addInfo(TooltipHelper.parallelText(30) + " Parallels with Heat Proof Casings")
            .addDynamicEuEffInfo(0.04f, TooltipTier.VOLTAGE)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front middle at bottom")
            .addCasingInfoMin("Structural Coke Oven Casings", 8, false)
            .addCasingInfoExactly("Heat Resistant/Proof Coke Oven Casings", 8, false)
            .addInputBus("Any Structural Coke Oven Casing", 1)
            .addOutputBus("Any Structural Coke Oven Casing", 1)
            .addInputHatch("Any Structural Coke Oven Casing", 1)
            .addOutputHatch("Any Structural Coke Oven Casing", 1)
            .addEnergyHatch("Any Structural Coke Oven Casing", 1)
            .addMaintenanceHatch("Any Structural Coke Oven Casing", 1)
            .addMufflerHatch("Any Structural Coke Oven Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialCokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCokeOven>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                .addShape(
                    mName + "1",
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "aaa", "a-a", "aaa" }, { "C~C", "CCC", "CCC" }, }))
                .addShape(
                    mName + "2",
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "bbb", "b-b", "bbb" }, { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialCokeOven.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.GTPP_INDEX(1))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 1))))
                .addElement(
                    'H',
                    ofChain(
                        onElementPass(x -> ++x.mCasing1, ofBlock(ModBlocks.blockCasingsMisc, 2)),
                        onElementPass(x -> ++x.mCasing2, ofBlock(ModBlocks.blockCasingsMisc, 3))))
                .addElement('a', ofBlock(ModBlocks.blockCasingsMisc, 2))
                .addElement('b', ofBlock(ModBlocks.blockCasingsMisc, 3))
                .build();
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
            return survivalBuildPiece(mName + "1", stackSize, 1, 2, 0, elementBudget, env, false, true);
        else return survivalBuildPiece(mName + "2", stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mCasing1 = 0;
        mCasing2 = 0;
        tier = 0;
        if (checkPiece(mName, 1, 2, 0)) {
            if (mCasing1 == 8) tier = 1;
            if (mCasing2 == 8) tier = 2;
            return tier > 0 && mCasing >= 8 && checkHatch();
        }
        return false;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_OP_CLICK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCACokeOvenActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCACokeOvenActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCACokeOven;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCACokeOvenGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(1);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cokeOvenRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        logic.setEuModifier((100F - (GTUtility.getTier(getMaxInputVoltage()) * 4)) / 100F);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 6 + tier * 12;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCokeOven;
    }

}
