package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.util.DescTextLocalization;

public class MTEDigester extends MTEEnhancedMultiBlockBase<MTEDigester>
    implements ISurvivalConstructable, ICasingTextureProvider {

    protected int casingAmount = 0;
    protected int height = 0;

    private HeatingCoilLevel heatLevel;

    private final IStructureDefinition<MTEDigester> multiDefinition = StructureDefinition.<MTEDigester>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { { "       ", " ttttt ", " t---t ", " t---t ", " t---t ", " ttttt ", "       " },
                    { "  ttt  ", " t---t ", "t-----t", "t-----t", "t-----t", " t---t ", "  ttt  " },
                    { " tccct ", "tc---ct", "c-----c", "c-----c", "c-----c", "tc---ct", " tccct " },
                    { " tt~tt ", "thhhhht", "thsssht", "thsssht", "thsssht", "thhhhht", " ttttt " }, }))

        .addElement(
            't',
            buildHatchAdder(MTEDigester.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, Muffler)
                .casingIndex(48) // Robust Tungstensteel Machine Casing
                .hint(1)
                .buildAndChain(onElementPass(MTEDigester::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .addElement('h', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('s', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'c',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEDigester::setCoilLevel, MTEDigester::getCoilLevel))))
        .build();

    public MTEDigester(String name) {
        super(name);
    }

    public MTEDigester(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(mName, 3, 3, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 40);
        checkHasEnergyHatch(errors);
        checkOneMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    private void onCasingAdded() {
        casingAmount++;
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
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).enablePerfectOC();
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                return recipe.mSpecialValue <= MTEDigester.this.getCoilLevel()
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
    public int getPollutionPerSecond(ItemStack aStack) {
        return 400;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEDigester(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(mName, itemStack, b, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("Digester.hint", 6);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_OIL_CRACKER,
            OVERLAY_FRONT_OIL_CRACKER_GLOW,
            OVERLAY_FRONT_OIL_CRACKER_ACTIVE,
            OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return casingTexturePages[0][48];
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gtnhlanth.tt.digester.machinetype"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.digester.info1"))
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 7, 4, true)
            .addController("Front bottom center")
            .addCasing("40-54", Casings.RobustTungstenSteelMachineCasing.getLocalizedName(), false)
            .addCasing("16", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addCasing("16", "Heating Coil", false)
            .addCasing("9", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            .addEnergyHatch("1+", "Any tungstensteel casing", 1)
            .addMaintenanceHatch("1", "Any tungstensteel casing", 1)
            .addMufflerHatch("1", "Any tungstensteel casing", 1)
            .addInputAny("1+", "Any tungstensteel casing", 1)
            .addOutputAny("1+", "Any tungstensteel casing", 1)
            .addAir("Interior and top of the structure")
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEDigester> getStructureDefinition() {
        return multiDefinition;
    }

}
