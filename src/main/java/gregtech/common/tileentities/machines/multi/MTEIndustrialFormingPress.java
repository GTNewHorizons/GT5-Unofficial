package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialFormingPress extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFormingPress>
    implements ISurvivalConstructable {

    private int casingAmount;
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<MTEIndustrialFormingPress> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialFormingPress>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "     ", "FEEEF", "F   F", "F   F", "FF~FF" },
                { "FFCFF", "  A  ", "BBBBB", "     ", "AAAAA" }, { "     ", "FEEEF", "F   F", "F   F", "FFFFF" } })
        .addElement('A', Casings.SteelPipeCasing.asElement())
        .addElement('B', Casings.FormingCore.asElement())
        .addElement(
            'F',
            buildHatchAdder(MTEIndustrialFormingPress.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                .casingIndex(Casings.MaterialPressCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.MaterialPressCasing.asElement())))
        .addElement('E', ofFrame(Materials.Titanium))
        .addElement('C', Casings.TitaniumGearBoxCasing.asElement())
        .build();

    public MTEIndustrialFormingPress(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialFormingPress(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFormingPress(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Forming Press")
            .addBulkMachineInfo(6, 6f, 1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 3, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Metalworking Machine Casing", 5, false)
            .addCasingInfoExactly("Titanium Frame Box", 6, false)
            .addCasingInfoExactly("Steel Pipe Casing", 6, false)
            .addCasingInfoExactly("Forming Core", 5, false)
            .addCasingInfoExactly("Titanium Gear Box Casing", 1, false)
            .addInputHatch("Metalworking Machine Casing", 1)
            .addInputBus("Metalworking Machine Casing", 1)
            .addOutputBus("Metalworking Machine Casing", 1)
            .addEnergyHatch("Metalworking Machine Casing", 1)
            .addMaintenanceHatch("Metalworking Machine Casing", 1)
            .addMufflerHatch("Metalworking Machine Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "barnac")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFormingPress> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        if (casingAmount < 5) errors.add(StructureErrors.missingCasings(casingAmount, 5));
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_FORGE_HAMMER;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.MaterialPressCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialPlatePressActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialPlatePressActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.MaterialPressCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialPlatePress)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialPlatePressGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.MaterialPressCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.formingPressRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 6F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialPlatePress_ModeForming;
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
