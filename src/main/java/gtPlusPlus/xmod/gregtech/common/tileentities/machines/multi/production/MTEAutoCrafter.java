package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class MTEAutoCrafter extends GTPPMultiBlockBase<MTEAutoCrafter> implements ISurvivalConstructable {

    protected GTRecipe lastRecipeToBuffer;
    private int casing;
    private static IStructureDefinition<MTEAutoCrafter> STRUCTURE_DEFINITION = null;

    public MTEAutoCrafter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAutoCrafter(String mName) {
        super(mName);
    }

    @Override
    public String getMachineType() {
        return "Assembler, LSAA";
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEAutoCrafter(this.mName);
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAutoCrafter;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Highly Advanced Assembling Machine")
            .addBulkMachineInfo(2, 3f, 1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoRange("Bulk Production Frame", 10, 25, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE_GLOW;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_GLOW;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(0, 10);
    }

    @Override
    public IStructureDefinition<MTEAutoCrafter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAutoCrafter>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEAutoCrafter.class)
                        .atLeast(InputBus, OutputBus, InputHatch, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.getIndexFromPage(0, 10))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        buildPiece(mName, itemStack, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack itemStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, itemStack, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity baseMetaTileEntity, ItemStack itemStack) {
        casing = 0;
        return checkPiece(mName, 1, 1, 0) && casing >= 10 && checkHatch();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 2 * (Math.max(1, GTUtility.getTier(getMaxInputVoltage())));
    }

    @Override
    public String[] getExtraInfoData() {
        final String running = (this.mMaxProgresstime > 0 ? "Auto-Crafter running" : "Auto-Crafter stopped");
        final String maintenance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintenance issues"
            : "Needs Maintenance");
        String tSpecialText;

        if (lastRecipeToBuffer != null && lastRecipeToBuffer.mOutputs[0].getDisplayName() != null) {
            tSpecialText = "Currently processing: " + lastRecipeToBuffer.mOutputs[0].getDisplayName();
        } else {
            tSpecialText = "Currently processing: Nothing";
        }

        return new String[] { "Large Scale Auto-Assembler v1.01c", running, maintenance, tSpecialText };
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
