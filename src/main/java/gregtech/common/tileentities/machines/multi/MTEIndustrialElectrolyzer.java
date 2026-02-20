package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialElectrolyzer extends MTEExtendedPowerMultiBlockBase<MTEIndustrialElectrolyzer>
    implements ISurvivalConstructable {

    private static IStructureDefinition<MTEIndustrialElectrolyzer> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 1;

    private static final int PARALLEL_PER_TIER = 4;
    private static final float SPEED = 2.8f;
    private static final float EU_EFFICIENCY = 0.9f;

    public MTEIndustrialElectrolyzer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialElectrolyzer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialElectrolyzer(this.mName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialElectrolyzer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialElectrolyzer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    // spotless:off
                    new String[][]{{
                        " DDD ",
                        "     ",
                        "     ",
                        "     ",
                        " DDD "
                    },{
                        "DDDDD",
                        " ADA ",
                        " A~A ",
                        " ADA ",
                        "DDDDD"
                    },{
                        "DCDBD",
                        " CDB ",
                        " CDB ",
                        " CDB ",
                        "DDDDD"
                    },{
                        "DDDDD",
                        " ADA ",
                        " ADA ",
                        " ADA ",
                        "DDDDD"
                    },{
                        " DDD ",
                        "     ",
                        "     ",
                        "     ",
                        " DDD "
                    }})
                //spotless:on
                .addElement('A', ofFrame(MaterialsAlloy.POTIN))
                .addElement('B', Casings.BrassItemPipeCasing.asElement())
                .addElement('C', Casings.TinItemPipeCasing.asElement())
                .addElement(
                    'D',
                    buildHatchAdder(MTEIndustrialElectrolyzer.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.ElectrolyzerCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(
                                MTEIndustrialElectrolyzer::onCasingAdded,
                                Casings.ElectrolyzerCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.ElectrolyzerCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialElectrolyzerActive)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialElectrolyzerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.ElectrolyzerCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialElectrolyzer)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialElectrolyzerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.ElectrolyzerCasing.getCasingTexture() };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Electrolyzer")
            .addBulkMachineInfo(PARALLEL_PER_TIER, SPEED, EU_EFFICIENCY)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front Center")
            .addCasingInfoMin("Electrolyzer Casings", 6, false)
            .addCasingInfoExactly("Potin Frame Box", 12, false)
            .addCasingInfoExactly("Tin Item Pipe Casing", 12, false)
            .addCasingInfoExactly("Brass Item Pipe Casing", 12, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addStructureAuthors("Vortex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / SPEED)
            .setEuModifier(EU_EFFICIENCY)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (PARALLEL_PER_TIER * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 6;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialElectrolyzer;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.electrolyzerNonCellRecipes;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
