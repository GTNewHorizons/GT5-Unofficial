package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

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
import gregtech.api.structure.error.TooFewCasings;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAdvImplosionCompressor extends MTEExtendedPowerMultiBlockBase<MTEAdvImplosionCompressor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_Z = 0;

    private int casingAmount;
    private static final IStructureDefinition<MTEAdvImplosionCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvImplosionCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                { "       ", "       ", "   A   ", "  A A  ", "   A   ", "   D   ", "  D D  ", "  B~B  ", "  BDB  " },
                { "       ", "  AAA  ", " AA AA ", " A   A ", " AA AA ", " DAAAD ", "       ", " BBBBB ", " DBBBD " },
                { "  CEC  ", " AA AA ", " A   A ", "A     A", " A   A ", " AA AA ", "D  A  D", "BBBBBBB", "BBBBBBB" },
                { "  E E  ", " A   A ", "A     A", "       ", "A     A", "DA   AD", "  A A  ", "BBBBBBB", "DBBBBBD" },
                { "  CEC  ", " AA AA ", " A   A ", "A     A", " A   A ", " AA AA ", "D  A  D", "BBBBBBB", "BBBBBBB" },
                { "       ", "  AAA  ", " AA AA ", " A   A ", " AA AA ", " DAAAD ", "       ", " BBBBB ", " DBBBD " },
                { "       ", "       ", "   A   ", "  A A  ", "   A   ", "   D   ", "  D D  ", "  BBB  ", "  BDB  " } })
        .addElement('A', Casings.RefinedGraphiteBlock.asElement())
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEAdvImplosionCompressor.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                    .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                    .hint(1)
                    .build(),
                onElementPass(x -> ++x.casingAmount, Casings.RobustTungstenSteelMachineCasing.asElement())))
        .addElement('C', ofFrame(Materials.Gold))
        .addElement('D', ofFrame(Materials.TungstenSteel))
        .addElement('E', ofSheetMetal(Materials.Gold))
        .build();

    public MTEAdvImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvImplosionCompressor(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Implosion Compressor")
            .addInfo("Factory Grade Advanced Implosion Compressor")
            .addInfo(TooltipHelper.parallelText("1 + (Tier/2)") + " Parallels")
            .addStaticSpeedInfo(2f)
            .addStaticEuEffInfo(1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 9, 7, true)
            .addController("Front center")
            .addCasingInfoMin("Robust Tungstensteel Machine Casing", 50, false)
            .addCasingInfoExactly("Refined Graphite Block", 80, false)
            .addCasingInfoExactly("Gold Frame Boxes", 4, false)
            .addCasingInfoExactly("Gold Sheetmetal", 4, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 24, false)
            .addInputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addOutputBus("Any Robust Tungstensteel Machine Casing", 1)
            .addEnergyHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMaintenanceHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addMufflerHatch("Any Robust Tungstensteel Machine Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "omegacubed")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEAdvImplosionCompressor> getStructureDefinition() {
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
        if (casingAmount < 50) {
            errors.add(new TooFewCasings(casingAmount, 50));
        }
        checkOneMufflerHatch(errors);
        checkHasEnergyHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
        checkHasMaintenanceHatch(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAdvancedImplosionActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAdvancedImplosionActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAdvancedImplosion)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAAdvancedImplosionGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.RobustTungstenSteelMachineCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.implosionRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.RANDOM_EXPLODE;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiAdvImplosion;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (GTUtility.getTier(this.getMaxInputVoltage()) / 2 + 1);
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
