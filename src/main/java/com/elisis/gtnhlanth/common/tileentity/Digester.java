package com.elisis.gtnhlanth.common.tileentity;

import static com.elisis.gtnhlanth.util.DescTextLocalization.BLUEPRINT_INFO;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.elisis.gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import com.elisis.gtnhlanth.util.DescTextLocalization;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;

public class Digester extends GT_MetaTileEntity_EnhancedMultiBlockBase<Digester> implements ISurvivalConstructable {

    protected int casingAmount = 0;
    protected int height = 0;

    private HeatingCoilLevel heatLevel;

    private final IStructureDefinition<Digester> multiDefinition = StructureDefinition.<Digester>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { { "       ", " ttttt ", " t---t ", " t---t ", " t---t ", " ttttt ", "       " },
                    { "  ttt  ", " t---t ", "t-----t", "t-----t", "t-----t", " t---t ", "  ttt  " },
                    { " tccct ", "tc---ct", "c-----c", "c-----c", "c-----c", "tc---ct", " tccct " },
                    { " tt~tt ", "thhhhht", "thsssht", "thsssht", "thsssht", "thhhhht", " ttttt " }, }))

        .addElement(
            't',
            buildHatchAdder(Digester.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, Muffler)
                .casingIndex(47)
                .dot(1)
                .buildAndChain(GregTech_API.sBlockCasings4, 0))
        .addElement('h', ofBlock(GregTech_API.sBlockCasings1, 11))
        .addElement('s', ofBlock(GregTech_API.sBlockCasings4, 1))
        .addElement('c', ofCoil(Digester::setCoilLevel, Digester::getCoilLevel))
        .build();

    public Digester(String name) {
        super(name);
    }

    public Digester(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(mName, 3, 3, 0) && !mMufflerHatches.isEmpty() && mMaintenanceHatches.size() == 1;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel level) {
        this.heatLevel = level;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.digesterRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).enablePerfectOC();
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                return recipe.mSpecialValue <= Digester.this.getCoilLevel()
                    .getHeat() ? CheckRecipeResultRegistry.SUCCESSFUL
                        : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }

        };
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 20;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new Digester(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(mName, itemStack, b, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("Digester.hint", 6);
    }

    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstone) {

        // Oil Cracker textures cuz I'm lazy

        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][47], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][47] };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Digester")
            .addInfo("Controller block for the Digester")
            .addInfo("Input ores and fluid, output water.")
            .addInfo(BLUEPRINT_INFO)
            .addSeparator()
            .addController("Front bottom")
            .addInputHatch("Hint block with dot 1")
            .addInputBus("Hint block with dot 1")
            .addOutputHatch("Hint block with dot 1")
            .addOutputBus("Hint block with dot 1")
            .addMaintenanceHatch("Hint block with dot 1")
            .addMufflerHatch("Hint block with dot 1")
            .toolTipFinisher("GTNH: Lanthanides");
        return tt;
    }

    @Override
    public IStructureDefinition<Digester> getStructureDefinition() {
        return multiDefinition;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
}
