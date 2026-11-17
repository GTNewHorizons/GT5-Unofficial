package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSolenoidCoil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHeatProducer;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.MTEMicrowaveInducedPlasmaReactorGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHeatSensor;

public class MTEMicrowaveInducedPlasmaReactor extends MTEExtendedPowerMultiBlockBase<MTEMicrowaveInducedPlasmaReactor>
    implements ISurvivalConstructable, IHeatProducer {

    private Byte solenoidLevel = null;
    private final List<MTEHeatSensor> temperatureSensors = new ArrayList<>(2);
    private int temperature;
    private boolean outputLossApplied;

    private static final int MIN_SOLENOID_TIER = 6;
    private static final int MAX_SOLENOID_TIER = 12;
    private static final int[] MAX_SPEED_TEMPERATURE = { 2_000, 1_800, 1_600, 1_400, 1_200, 1_000, 800 };
    private static final int[] OUTPUT_LOSS_TEMPERATURE = { 5_000, 6_000, 7_000, 8_000, 9_000, 10_000, 11_000 };
    private static final int[] BOOM_TEMPERATURE = { 10_000, 11_000, 12_000, 13_000, 14_000, 15_000, 16_000 };
    private static final double[] MAX_SPEED_MODIFIER = { 1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4 };
    private static final String TAG_TEMPERATURE = "miprTemperature";

    private static final String MAIN = "main";
    private static final int OFFSET_X = 10;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 1;
    private static final IStructureDefinition<MTEMicrowaveInducedPlasmaReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMicrowaveInducedPlasmaReactor>builder()
        .addShape(
            MAIN,
            transpose(
                new String[][] {
                    { "                     ", "                     ", "                     ",
                        "      JJ     JJ      ", "  EEEJHHH   HHHJGGG  ", "      JJ     JJ      ",
                        "                     ", "                     ", "                     " },
                    { "                     ", "                     ", "  KAAA   JHJ   AAAI  ",
                        "  KAAAHHHHHHHHHAAAI  ", " EEAAACCCHHHCCCAAAGG ", "  KAAAHHHHHHHHHAAAI  ",
                        "  KAAA   JHJ   AAAI  ", "                     ", "                     " },
                    { "                     ", "  KAAA   JHJ   AAAI  ", "  KAAAA  HHH  AAAAI  ",
                        "EEECCCAAACCCAAACCCGGG", " EECCCAAACCCAAACCCGG ", "EEECCCAAACCCAAACCCGGG",
                        "  KAAAA  HHH  AAAAI  ", "  KAAA   JHJ   AAAI  ", "                     " },
                    { "      JJ     JJ      ", "  KAAAHHHHHHHHHAAAI  ", "EEECCCAAACCCAAACCCGGG",
                        "EDDDDDDDDDDDDDDDDDDDG", "EFF               BBG", "EDDDDDDDDDDDDDDDDDDDG",
                        "EEECCCAAACCCAAACCCGGG", "  KAAAHHHHHHHHHAAAI  ", "      JJ     JJ      " },
                    { "  EEEJHHH   HHHJGGG  ", " EEAAACCCH~HCCCAAAGG ", " EECCCAAACCCAAACCCGG ",
                        "EFF               BBG", "LFF               BBM", "EFF               BBG",
                        " EECCCAAACCCAAACCCGG ", " EEAAACCCHHHCCCAAAGG ", "  EEEJHHH   HHHJGGG  " },
                    { "      JJ     JJ      ", "  KAAAHHHHHHHHHAAAI  ", "EEECCCAAACCCAAACCCGGG",
                        "EDDDDDDDDDDDDDDDDDDDG", "EFF               BBG", "EDDDDDDDDDDDDDDDDDDDG",
                        "EEECCCAAACCCAAACCCGGG", "  KAAAHHHHHHHHHAAAI  ", "      JJ     JJ      " },
                    { "                     ", "  KAAA   JHJ   AAAI  ", "  KAAAA  HHH  AAAAI  ",
                        "EEECCCAAACCCAAACCCGGG", " EECCCAAACCCAAACCCGG ", "EEECCCAAACCCAAACCCGGG",
                        "  KAAAA  HHH  AAAAI  ", "  KAAA   JHJ   AAAI  ", "                     " },
                    { "                     ", "                     ", "  KAAA   JHJ   AAAI  ",
                        "  KAAAHHHHHHHHHAAAI  ", " EEAAACCCHHHCCCAAAGG ", "  KAAAHHHHHHHHHAAAI  ",
                        "  KAAA   JHJ   AAAI  ", "                     ", "                     " },
                    { "                     ", "                     ", "                     ",
                        "      JJ     JJ      ", "  EEEJHHH   HHHJGGG  ", "      JJ     JJ      ",
                        "                     ", "                     ", "                     " } }))
        .addElement('A', ofBlock(ItemRegistry.bw_deprecatedglass, 0))
        .addElement('B', Casings.SuperconductingCoilBlock.asElement())
        .addElement(
            'C',
            GTStructureChannels.SOLENOID.use(
                ofSolenoidCoil(
                    MTEMicrowaveInducedPlasmaReactor::setSolenoidLevel,
                    MTEMicrowaveInducedPlasmaReactor::getSolenoidLevel)))
        .addElement('D', Casings.FluxedElectrumItemPipeCasing.asElement())
        .addElement('E', Casings.ChemicallyInertMachineCasing.asElement())
        .addElement('F', Casings.PTFEPipeCasing.asElement())
        .addElement('G', Casings.AdvancedRhodiumPlatedPalladiumMachineCasing.asElement())
        .addElement(
            'H',
            buildHatchAdder(MTEMicrowaveInducedPlasmaReactor.class)
                .atLeast(Maintenance, Energy.or(ExoticEnergy), MTEHeatSensor.HeatSensorHatchElement.HeatSensor)
                .casingIndex(Casings.MagicalMachineCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.MagicalMachineCasing.asElement()))
        .addElement('I', ofFrame(Materials.Chrome))
        .addElement('J', ofFrame(Materials.TungstenCarbide))
        .addElement('K', ofFrame(Materials.Polytetrafluoroethylene))
        .addElement('L', InputBus.newAny(Casings.ChemicallyInertMachineCasing.textureId, 2))
        .addElement('M', OutputBus.newAny(Casings.AdvancedRhodiumPlatedPalladiumMachineCasing.textureId, 3))
        .build();

    public MTEMicrowaveInducedPlasmaReactor(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEMicrowaveInducedPlasmaReactor(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEMicrowaveInducedPlasmaReactor(mName);
    }

    @Override
    public IStructureDefinition<MTEMicrowaveInducedPlasmaReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.miprRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setRecipeEUt(0)
                    .setDuration(5 * 20)
                    .setMaxOverclocks(0);
            }
        }.setSpeedBonusSupplier(this::getTimeMultiplier)
            .setMaxParallelSupplier(this::getTierParallel);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        super.setProcessingLogicPower(logic);
        logic.setAvailableVoltage(GTValues.V[getSolenoidTier()]);
        logic.setAvailableAmperage(1);
    }

    private int getSolenoidTier() {
        return Math
            .clamp(solenoidLevel == null ? MIN_SOLENOID_TIER : solenoidLevel, MIN_SOLENOID_TIER, MAX_SOLENOID_TIER);
    }

    private int getTierIndex() {
        return getSolenoidTier() - MIN_SOLENOID_TIER;
    }

    private int getTierParallel() {
        return 1 << getTierIndex();
    }

    private double getTimeMultiplier() {
        int index = getTierIndex();
        double progress = Math.clamp((double) temperature / MAX_SPEED_TEMPERATURE[index], 0.0, 1.0);
        return 10.0 + (MAX_SPEED_MODIFIER[index] - 10.0) * progress;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        solenoidLevel = null;
        temperatureSensors.clear();
        if (!checkPiece(MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        if (solenoidLevel == null || solenoidLevel < MIN_SOLENOID_TIER) {
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
        if (temperatureSensors.size() < 2) {
            errors.add(
                StructureErrors.hatchCount(
                    ErrorType.TOO_FEW,
                    TranslatableText.lang("GT5U.MBTT.HeatSensorHatch"),
                    temperatureSensors.size(),
                    2));
        } else if (temperatureSensors.size() > 2) {
            errors.add(
                StructureErrors.hatchCount(
                    ErrorType.TOO_MANY,
                    TranslatableText.lang("GT5U.MBTT.HeatSensorHatch"),
                    temperatureSensors.size(),
                    2));
        }
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) outputLossApplied = false;
        return result;
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (!drainEnergyInput(GTValues.V[getSolenoidTier()])) return false;

        if (getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() % 20 == 0) {
            temperature = Math.min(Integer.MAX_VALUE, temperature + 10);
        }

        int boomTemperature = BOOM_TEMPERATURE[getTierIndex()];
        updateTemperatureSensor();
        if (temperature >= boomTemperature) {
            temperature = boomTemperature;
            explodeMultiblock();
            return false;
        }

        if (!outputLossApplied && mProgresstime <= 1 && mOutputItems != null) {
            applyOutputLoss();
            outputLossApplied = true;
        }

        return super.onRunningTick(stack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        super.onPostTick(base, tick);
        if (!base.isServerSide() || tick % 20 != 0 || mMaxProgresstime > 0) return;
        temperature = Math.max(0, temperature - 100);
        updateTemperatureSensor();
    }

    private void updateTemperatureSensor() {
        for (MTEHeatSensor sensor : temperatureSensors) sensor.setHeatValue(temperature);
    }

    private void applyOutputLoss() {
        int index = getTierIndex();
        int lossStart = OUTPUT_LOSS_TEMPERATURE[index];
        int boom = BOOM_TEMPERATURE[index];
        if (temperature <= lossStart || boom <= lossStart) return;
        double lossChance = Math.clamp((double) (temperature - lossStart) / (boom - lossStart), 0.0, 1.0);
        for (int i = 0; i < mOutputItems.length; i++) {
            ItemStack output = mOutputItems[i];
            if (output == null) continue;
            int kept = 0;
            for (int item = 0; item < output.stackSize; item++) {
                if (getBaseMetaTileEntity().getWorld().rand.nextDouble() >= lossChance) kept++;
            }
            if (kept == 0) mOutputItems[i] = null;
            else output.stackSize = kept;
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
        return new MultiblockTooltipBuilder().addMachineType(StatCollector.translateToLocal("gt.recipe.mipr"))
            .beginStructureBlock(21, 9, 9, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center"))
            .addEnergyHatch("1+", "Any Magical Machine Casing", 1)
            .addMaintenanceHatch("1", "Any Magical Machine Casing", 1)
            .addInputBus("1", "Left Middle", 2)
            .addOutputBus("1", "Right Middle", 3)
            .addMiscHatch("2", "Heat Sensor Hatches", "Magical Machine Casing", 1)
            .addSubChannel(GTStructureChannels.SOLENOID)
            .toolTipFinisher();
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMicrowaveInducedPlasmaReactorGui(this);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        if (side != facing)
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(Casings.MagicalMachineCasing.textureId) };
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(Casings.MagicalMachineCasing.textureId),
            TextureFactory.builder()
                .addIcon(
                    active ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE
                        : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                .extFacing()
                .build() };
    }

    private Byte getSolenoidLevel() {
        return solenoidLevel;
    }

    private void setSolenoidLevel(byte level) {
        solenoidLevel = level;
    }

    @Override
    public List<MTEHeatSensor> getHeatSensorHatches() {
        return Collections.unmodifiableList(temperatureSensors);
    }

    @Override
    public boolean addHeatSensorHatchToMachineList(IGregTechTileEntity tile, int casingTexture) {
        if (tile == null || !(tile.getMetaTileEntity() instanceof MTEHeatSensor sensor)) return false;
        sensor.updateTexture(casingTexture);
        return temperatureSensors.add(sensor);
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);
        tag.setInteger(TAG_TEMPERATURE, temperature);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);
        temperature = Math.max(0, tag.getInteger(TAG_TEMPERATURE));
    }
}
