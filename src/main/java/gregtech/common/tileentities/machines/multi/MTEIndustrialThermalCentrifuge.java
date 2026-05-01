package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
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
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialThermalCentrifuge extends MTEExtendedPowerMultiBlockBase<MTEIndustrialThermalCentrifuge>
    implements ISurvivalConstructable {

    private int casingAmount;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_Z = 0;

    private HeatingCoilLevel coilLevel = null;
    private Byte solenoidLevel = null;

    private static final double SPEED_PER_COIL = 0.05;
    private static final int PARALLELS_PER_SOLENOID = 2;
    private static final double HEATING_COIL_EU_MULTIPLIER = 0.95;

    private static final double BASE_SPEED_BONUS = 2.5;
    private static final double BASE_EU_MULTIPLIER = 0.8;
    private static final int BASE_PARALLELS = 8;
    private static IStructureDefinition<MTEIndustrialThermalCentrifuge> STRUCTURE_DEFINITION = null;

    public MTEIndustrialThermalCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialThermalCentrifuge(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialThermalCentrifuge(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Thermal Centrifuge, LTR")
            .addBulkMachineInfo(BASE_PARALLELS, (float) BASE_SPEED_BONUS, (float) BASE_EU_MULTIPLIER)
            .addDynamicParallelInfo(PARALLELS_PER_SOLENOID, TooltipTier.SOLENOID)
            .addInfo(
                String.format(
                    "Every coil tier gives a %s speed bonus and a %s EU/t discount (multiplicative)",
                    TooltipHelper.speedText("+") + TooltipHelper.speedText((float) SPEED_PER_COIL),
                    TooltipHelper.effText((float) (1 - HEATING_COIL_EU_MULTIPLIER))))
            .addInfo(
                String.format(
                    "The EU multiplier is %s%.2f * (%.2f ^ Heating Coil Tier)%s, prior to overclocks",
                    EnumChatFormatting.ITALIC,
                    BASE_EU_MULTIPLIER,
                    HEATING_COIL_EU_MULTIPLIER,
                    EnumChatFormatting.GRAY))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 8, 5, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Thermal Processing Casings", 85, false)
            .addCasingInfoExactly("Heat Proof Machine Casings", 4, false)
            .addCasingInfoExactly("Red Steel Frame Box", 16, false)
            .addCasingInfoExactly("Solenoid Superconducting Coil", 6, true)
            .addCasingInfoExactly("Heating Coils", 16, true)
            .addCasingInfoExactly("Any Glass", 6, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.SOLENOID)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Oasis_Cactus")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialThermalCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialThermalCentrifuge>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (new String[][] { { " FFF ", " FFF ", "     ", "     ", "     ", "     ", " FFF ", " F~F " },
                        { "FFFFF", "FBDBF", "  D  ", "  E  ", "  E  ", "F D F", "FBDBF", "FFFFF" },
                        { "FFFFF", "FDCDF", "FDCDF", " ECE ", "FECEF", "FDCDF", "FDCDF", "FFFFF" },
                        { "FFFFF", "F D F", "F D F", "F E F", "F E F", "F D F", "F D F", "FFFFF" },
                        { "FFFFF", "EFAFE", "EFAFE", "EFAFE", "EFAFE", "EFAFE", "EFAFE", "FFFFF" },
                        { " FFF ", "     ", "     ", "     ", "     ", "     ", "     ", " FFF " } }))
                .addElement(
                    'F',
                    ofChain(
                        buildHatchAdder(MTEIndustrialThermalCentrifuge.class)
                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                            .casingIndex(Casings.ThermalProcessingCasing.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.ThermalProcessingCasing.asElement())))
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.HeatProofMachineCasing.asElement())
                .addElement(
                    'C',
                    GTStructureChannels.SOLENOID.use(
                        ofSolenoidCoil(
                            MTEIndustrialThermalCentrifuge::setSolenoidLevel,
                            MTEIndustrialThermalCentrifuge::getSolenoidLevel)))
                .addElement(
                    'D',
                    GTStructureChannels.HEATING_COIL.use(
                        activeCoils(
                            ofCoil(
                                MTEIndustrialThermalCentrifuge::setCoilLevel,
                                MTEIndustrialThermalCentrifuge::getCoilLevel))))
                .addElement('E', ofFrame(Materials.RedSteel))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifugeActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifugeActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifuge)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialThermalCentrifugeGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.ThermalProcessingCasing.getCasingTexture() };
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
        coilLevel = null;
        solenoidLevel = null;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        if (casingAmount < 85) errors.add(new TooFewCasings(casingAmount, 85));
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkOneMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.thermalCentrifugeRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifierSupplier(this::getEUMultiplier)
            .setSpeedBonusSupplier(this::getSpeedBonus);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (BASE_PARALLELS * GTUtility.getTier(this.getMaxInputVoltage()))
            + (solenoidLevel == null ? 0 : (PARALLELS_PER_SOLENOID * solenoidLevel));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialThermalCentrifuge;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_THERMAL_CENTRIFUGE_LOOP;
    }

    private HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    private void setCoilLevel(HeatingCoilLevel level) {
        coilLevel = level;
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
    }

    public double getCoilSpeedBonus() {
        return (coilLevel == null ? 0 : SPEED_PER_COIL * coilLevel.getTier());
    }

    public double getSpeedBonus() {
        return 1F / (BASE_SPEED_BONUS + getCoilSpeedBonus());
    }

    public double getEUMultiplier() {
        double heatingBonus = (coilLevel == null ? 0
            : GTUtility.powInt(HEATING_COIL_EU_MULTIPLIER, coilLevel.getTier()));

        return BASE_EU_MULTIPLIER * heatingBonus;
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
