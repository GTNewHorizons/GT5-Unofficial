package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.loader.Loaders;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.TooFewCasings;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialMolecularTransformer extends MTEExtendedPowerMultiBlockBase<MTEIndustrialMolecularTransformer>
    implements ISurvivalConstructable {

    private static IStructureDefinition<MTEIndustrialMolecularTransformer> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private int casingAmount;

    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_Z = 0;

    public MTEIndustrialMolecularTransformer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialMolecularTransformer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialMolecularTransformer(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {

        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Molecular Transformer")
            .addInfo("Changes the structure of items to produce new ones")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 9, 7, false)
            .addController("Front center, 2nd layer")
            .addCasingInfoMin("Molecular Containment Casing", 95, false)
            .addCasingInfoExactly("Speeding Pipe Casing", 35, false)
            .addCasingInfoExactly("Advanced Iridium Plated Machine Casing", 14, false)
            .addCasingInfoExactly("Any Glass", 30, false)
            .addInputBus("Any Molecular Containment Casing", 1)
            .addOutputBus("Any Molecular Containment Casing", 1)
            .addEnergyHatch("Any Molecular Containment Casing", 1)
            .addMaintenanceHatch("Any Molecular Containment Casing", 1)
            .addMufflerHatch("Any Molecular Containment Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Fox")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialMolecularTransformer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialMolecularTransformer>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (new String[][] {
                        { "  CCC  ", "   C   ", "       ", "       ", "       ", "       ", "       ", "   ~   ",
                            "  CCC  " },
                        { " CCCCC ", "  CCC  ", "   A   ", "   A   ", "   A   ", "   A   ", "   A   ", "  CCC  ",
                            " CCCCC " },
                        { "CCCCCCC", " CCDCC ", "  ADA  ", "  ADA  ", "  ADA  ", "  ADA  ", "  ADA  ", " CCDCC ",
                            "CCCCCCC" },
                        { "CCCCCCC", "CBDDDBC", " BDDDB ", " BDDDB ", " BDDDB ", " BDDDB ", " BDDDB ", "CBDDDBC",
                            "CCCCCCC" },
                        { "CCCCCCC", " CCDCC ", "  ADA  ", "  ADA  ", "  ADA  ", "  ADA  ", "  ADA  ", " CCDCC ",
                            "CCCCCCC" },
                        { " CCCCC ", "  CCC  ", "   A   ", "   A   ", "   A   ", "   A   ", "   A   ", "  CCC  ",
                            " CCCCC " },
                        { "  CCC  ", "   C   ", "       ", "       ", "       ", "       ", "       ", "   C   ",
                            "  CCC  " } }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialMolecularTransformer.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.MolecularContainmentCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.MolecularContainmentCasing.asElement())))
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.AdvancedIridiumPlatedMachineCasing.asElement())
                .addElement('D', ofBlock(Loaders.speedingPipe, 0))
                .build();
        }
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
        if (casingAmount < 95) errors.add(new TooFewCasings(casingAmount, 95));
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkOneMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.MolecularContainmentCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialMolecularTransformerActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialMolecularTransformerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.MolecularContainmentCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAIndustrialMolecularTransformer)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialMolecularTransformerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.MolecularContainmentCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.molecularTransformerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching();
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiMolecularTransformer;
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
