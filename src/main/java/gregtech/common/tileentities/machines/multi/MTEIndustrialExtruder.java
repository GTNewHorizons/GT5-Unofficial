package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialExtruder extends MTEExtendedPowerMultiBlockBase<MTEIndustrialExtruder>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 8;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;

    private int casingAmount;
    private int casingAmountStainless;
    private static final IStructureDefinition<MTEIndustrialExtruder> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialExtruder>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "         ", "         ", "     CCC~", "     CCCC" },
                { "A ABBBBBA", "AAA     A", "A ABBBBBA", "DDDDDDDDA" },
                { "AAA     A", "AAA     A", "AAA     A", "DDDDDDDDA" },
                { "A ABBBBBA", "AAA     A", "A ABBBBBA", "DDDDDDDDA" } })
        .addElement(
            'A',
            buildHatchAdder(MTEIndustrialExtruder.class).atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                .casingIndex(Casings.PressureContainmentCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.PressureContainmentCasing.asElement())))
        .addElement('B', Casings.FormingCore.asElement())
        .addElement(
            'C',
            buildHatchAdder(MTEIndustrialExtruder.class).atLeast(Maintenance, Energy, Muffler)
                .casingIndex(Casings.CleanStainlessSteelMachineCasing.textureId)
                .hint(2)
                .buildAndChain(
                    onElementPass(
                        x -> ++x.casingAmountStainless,
                        Casings.CleanStainlessSteelMachineCasing.asElement())))
        .addElement('D', Casings.ChemicallyInertMachineCasing.asElement())
        .build();

    public MTEIndustrialExtruder(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialExtruder(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialExtruder(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Extruder, IEM")
            .addBulkMachineInfo(6, 3.5f, 1f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 4, 4, false)
            .addController("Front right, 2nd layer")
            .addCasingInfoMin("Pressure Containment Casing", 8, false)
            .addCasingInfoMin("Clean Stainless Steel Casing", 3, false)
            .addCasingInfoExactly("Chemically Inert Casing", 24, false)
            .addInputBus("Any Pressure Containment Casing", 1)
            .addOutputBus("Any Pressure Containment Casing", 1)
            .addEnergyHatch("Any Pressure Containment or Clean Stainless Steel Casing", 1)
            .addMaintenanceHatch("Any Pressure Containment or Clean Stainless Steel Casing", 1)
            .addMufflerHatch("Any Pressure Containment or Clean Stainless Steel Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "cauchemard")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialExtruder> getStructureDefinition() {
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        casingAmountStainless = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmountStainless >= 3
            && casingAmount >= 8
            && checkHatch();
    }

    public boolean checkHatch() {
        return mMufflerHatches.size() >= 1 && mInputBusses.size() >= 1
            && mOutputBusses.size() >= 1
            && mEnergyHatches.size() >= 1;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialExtruderActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialExtruderActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialExtruder)

                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialExtruderGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture() };
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.extruderRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialExtruder;
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
