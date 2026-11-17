package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.GTStructureChannels;

public class MTEZoneMelter extends MTEExtendedPowerMultiBlockBase<MTEZoneMelter> implements ISurvivalConstructable {

    private static final String MAIN = "main";
    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 0;
    private static final IStructureDefinition<MTEZoneMelter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEZoneMelter>builder()
        .addShape(
            MAIN,
            transpose(
                new String[][] {
                    { "             ", "             ", "     CCC     ", "     CFC     ", "     CCC     ",
                        "             ", "             " },
                    { "             ", "     CCC     ", "C DDEBBBEDD C", "C DDEBBBEDD C", "C DDEBBBEDD C",
                        "     CCC     ", "             " },
                    { "     CCC     ", "C DDEBBBEDD C", "CAAAAAAAAAAAC", "CAAAAAAAAAAAC", "CAAAAAAAAAAAC",
                        "C DDEBBBEDD C", "     CCC     " },
                    { "     C~C     ", "C DDEBBBEDD C", "CAAAAAAAAAAAC", "G           G", "CAAAAAAAAAAAC",
                        "C DDEBBBEDD C", "     CCC     " },
                    { "     CCC     ", "C DDEBBBEDD C", "CAAAAAAAAAAAC", "CAAAAAAAAAAAC", "CAAAAAAAAAAAC",
                        "C DDEBBBEDD C", "     CCC     " },
                    { "             ", "     CCC     ", "C DDEBBBEDD C", "C DDEBBBEDD C", "C DDEBBBEDD C",
                        "     CCC     ", "             " },
                    { "             ", "             ", "     CCC     ", "     CCC     ", "     CCC     ",
                        "             ", "             " } }))
        .addElement('A', chainAllGlasses(-1, (machine, tier) -> machine.glassTier = tier, machine -> machine.glassTier))
        .addElement(
            'B',
            GTStructureChannels.SOLENOID.use(ofSolenoidCoil(MTEZoneMelter::setSolenoid, MTEZoneMelter::getSolenoid)))
        .addElement(
            'C',
            buildHatchAdder(MTEZoneMelter.class).atLeast(Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(Casings.CleanStainlessSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.CleanStainlessSteelMachineCasing.asElement()))
        .addElement(
            'D',
            GTStructureChannels.HEATING_COIL.use(activeCoils(ofCoil(MTEZoneMelter::setCoil, MTEZoneMelter::getCoil))))
        .addElement('E', ofFrame(Materials.StainlessSteel))
        .addElement('F', Muffler.newAny(Casings.CleanStainlessSteelMachineCasing.textureId, 2))
        .addElement(
            'G',
            buildHatchAdder(MTEZoneMelter.class).anyOf(InputBus, OutputBus)
                .casingIndex(Casings.CleanStainlessSteelMachineCasing.textureId)
                .hint(3)
                .build())
        .build();

    private HeatingCoilLevel coilLevel;
    private Byte solenoidLevel;
    private int glassTier = -1;

    public MTEZoneMelter(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEZoneMelter(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEZoneMelter(mName);
    }

    @Override
    public IStructureDefinition<MTEZoneMelter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.zoneMelterRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                int coilOverclocks = coilLevel == null ? 0 : Math.max(0, coilLevel.getTier());
                return super.createOverclockCalculator(recipe).setMaxOverclocks(coilOverclocks);
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (coilLevel == null || coilLevel == HeatingCoilLevel.None) {
                    return CheckRecipeResultRegistry.insufficientHeat(1);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setSpeedBonusSupplier(this::getSolenoidSpeedBonus)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        super.setProcessingLogicPower(logic);
        if (hasAdvancedParallelSetup()) {
            logic.setAvailableAmperage(Math.clamp(getMaxInputAmps(), 0L, 64L));
        }
    }

    public int getMaxParallelRecipes() {
        if (hasAdvancedParallelSetup()) {
            int amps = (int) Math.clamp(getMaxInputAmps(), 0L, 64L);
            return calculateParallelFromAmperage(amps);
        }
        return 1;
    }

    private double getSolenoidSpeedBonus() {
        return 1.0D / Math.max(1, solenoidLevel == null ? 1 : solenoidLevel + 1);
    }

    private int calculateParallelFromAmperage(int amperage) {
        return (int) Math.sqrt(Math.clamp(amperage, 0, 64));
    }

    private boolean hasAdvancedParallelSetup() {
        return coilLevel != null && coilLevel.ordinal() >= HeatingCoilLevel.UIV.ordinal()
            && solenoidLevel != null
            && solenoidLevel >= VoltageIndex.UEV
            && mEnergyHatches.size() + mExoticEnergyHatches.size() == 1;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        glassTier = -1;
        coilLevel = null;
        solenoidLevel = null;
        if (!checkPiece(MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        if (coilLevel == null || coilLevel == HeatingCoilLevel.None)
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        if (solenoidLevel == null) errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkOneMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
        for (var energyHatch : getExoticAndNormalEnergyHatchList()) {
            if (energyHatch.getTierForStructure() > glassTier) {
                errors.add(StructureErrors.glassTierNotEnough(energyHatch.getTierForStructure()));
                return;
            }
        }
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        buildPiece(MAIN, stack, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(MAIN, stack, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(StatCollector.translateToLocal("gt.recipe.zone_melter"))
            .beginStructureBlock(13, 7, 7, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center"))
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.top_center"), 2)
            .addInputBus("1", "Left/Right side", 3)
            .addOutputBus("1", "Left/Right side", 3)
            .addCasing("88", StatCollector.translateToLocal("gt.mbtt.structure.any_tiered_glass"), true)
            .addCasing("80", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            .addCasing("48", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("36", "Solenoid Superconductor Coil", true)
            .addCasing("24", "Stainless Steel Frame Box", false)
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addSubChannel(GTStructureChannels.SOLENOID)
            .toolTipFinisher();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        if (side != facing) return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(Casings.CleanStainlessSteelMachineCasing.textureId) };
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(Casings.CleanStainlessSteelMachineCasing.textureId),
            TextureFactory.builder()
                .addIcon(active ? OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build() };
    }

    private HeatingCoilLevel getCoil() {
        return coilLevel;
    }

    private void setCoil(HeatingCoilLevel level) {
        coilLevel = level;
    }

    private Byte getSolenoid() {
        return solenoidLevel;
    }

    private void setSolenoid(byte level) {
        solenoidLevel = level;
    }
}
