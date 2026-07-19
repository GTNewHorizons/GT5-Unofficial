package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.blocks.BlockCasings2;

public class MTEMultiCanner extends MTEExtendedPowerMultiBlockBase<MTEMultiCanner>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMultiCanner> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiCanner>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "  AAA  ", " AAAAA ", "AAAAAAA", "AAAAAAA", "AAAAAAA", " AAAAA ", "  AAA  " },
                    { "       ", "  B B  ", " BAAAB ", "  A A  ", " BAAAB ", "  B B  ", "       " },
                    { "       ", "  B B  ", " BA~AB ", "  A A  ", " BAAAB ", "  B B  ", "       " },
                    { "       ", "  B B  ", " BAAAB ", "  A A  ", " BAAAB ", "  B B  ", "       " },
                    { "  AAA  ", " AAAAA ", "AAAAAAA", "AAAAAAA", "AAAAAAA", " AAAAA ", "  AAA  " } })))
        .addElement(
            'A',
            buildHatchAdder(MTEMultiCanner.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                .hint(1)
                .buildAndChain(onElementPass(MTEMultiCanner::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 13))
        .build();

    public MTEMultiCanner(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiCanner(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEMultiCanner> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiCanner(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_MULTI_CANNER,
            OVERLAY_FRONT_MULTI_CANNER_GLOW,
            OVERLAY_FRONT_MULTI_CANNER_ACTIVE,
            OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        String anyCasing = TooltipHelper.anyCasingText(Casings.SolidSteelMachineCasing);
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("gt.recipe.canner")
            .addBulkMachineInfo(8, 2f, 1)
            .addInfo("gt.turbocan.tips")
            .beginStructureBlock(7, 7, 5, true)
            .addController("Front center, 3rd layer")
            .addCasing("85-93", "Solid Steel Machine Casing", false)
            .addCasing("24", "Steel Pipe Casing", false)
            .addEnergyHatch("1+", "Any machine casing", 1)
            .addMaintenanceHatch("1", "Any machine casing", 1)
            .addInputAny("1+", "Any machine casing", 1)
            .addOutputAny("1+", "Any machine casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 2, 2);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 2, 2, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 2, 2, errors)) return;
        checkCasingMin(errors, mCasingAmount, 85);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cannerRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
