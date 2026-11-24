package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.ECHatchElement.Energy;
import static gregtech.api.enums.ECHatchElement.InputBus;
import static gregtech.api.enums.ECHatchElement.Maintenance;
import static gregtech.api.enums.ECHatchElement.Muffler;
import static gregtech.api.enums.ECHatchElement.OutputBus;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.GTValues.VP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofECActiveCoil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.ec.component.IPollutionLogicComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.multiblock.IBatchConfigComponent;
import gregtech.api.ec.component.multiblock.IMultiblockEnergyInputLogicComponent;
import gregtech.api.ec.component.multiblock.IRecipeEfficiencyLogicComponent;
import gregtech.api.ec.implementations.MultiblockComponentBuilder;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.ec.multiblock.CustomRecipeLogicComponent;
import gregtech.common.misc.GTStructureChannels;

@IMetaTileEntity.SkipGenerateDescription
public class MTEMultiFurnace extends MTEAbstractMultiFurnace<MTEMultiFurnace>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int mLevel = 0;

    private static final long RECIPE_EUT = 4;
    private static final int RECIPE_DURATION = 128;
    private static final int CASING_INDEX = 11;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMultiFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "ccc", "cmc", "ccc" }, { "CCC", "C-C", "CCC" }, { "b~b", "bbb", "bbb" } }))
        .addElement(
            'c',
            buildHatchAdder(MTEMultiFurnace.class).atLeast(Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(3)
                .buildAndChain(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .addElement('m', Muffler.newAny(CASING_INDEX, 2))
        .addElement('C', GTStructureChannels.HEATING_COIL.use(ofECActiveCoil()))
        .addElement(
            'b',
            buildHatchAdder(MTEMultiFurnace.class).atLeast(Maintenance, InputBus, OutputBus, Energy)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings1, CASING_INDEX))
        .build();

    public MTEMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiFurnace(String aName) {
        super(aName);
    }

    @Override
    protected @NotNull MTEMultiFurnace createNewMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiFurnace(this.mName);
    }

    @Override
    protected void configureComponents(@NotNull MultiblockComponentBuilder builder) {
        super.configureComponents(builder);
        builder.enableBatchMode()
            .enableVoidProtection()
            .noFluidIO()
            .setRecipeLogic(
                new CustomRecipeLogicComponent(this::checkProcessing).setRecipeMapProvider(this::getRecipeMap))
            .configureCoil(comp -> comp.setOnCoilValidated(coilLevel -> mLevel = 4 << (coilLevel.ordinal() - 1)))
            .enablePollution(GTMod.proxy.mPollutionMultiSmelterPerSecond)
            .setActivitySound(SoundResource.GT_MACHINES_EBF_LOOP);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.furnace"))
            .addMarkdown(new ResourceLocation("gregtech", "multi-furnace"))
            .addPollutionAmount(GTMod.proxy.mPollutionMultiSmelterPerSecond)
            .beginStructureBlock(3, 3, 3, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("8-12", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addCasing("8", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addEnergyHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.top_center_casing"), 2)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_MULTI_SMELTER,
            OVERLAY_FRONT_MULTI_SMELTER_GLOW,
            OVERLAY_FRONT_MULTI_SMELTER_ACTIVE,
            OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return casingTexturePages[0][CASING_INDEX];
    }

    /*
     * NOTE: If you are wondering why your machine is not showing up in the NEIHandler for furnaces...
     * it is handled in the NEI fork's catalysts.csv . so that multiple mods can show up in the same handler.
     */
    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.furnaceRecipes;
    }

    // Not GPL
    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @NotNull
    public CheckRecipeResult checkProcessing(@NotNull CustomRecipeLogicComponent logic) {
        List<ItemStack> tInput = getAllStoredItems();
        var energyInputLogic = getComponent(IMultiblockEnergyInputLogicComponent.KEY);
        long availableEUt = GTUtility.roundUpVoltage(energyInputLogic.getMaxInputVoltage());
        if (availableEUt < RECIPE_EUT) {
            return CheckRecipeResultRegistry.insufficientPower(RECIPE_EUT);
        }
        if (tInput.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int maxParallel = this.mLevel;
        int originalMaxParallel = this.mLevel;

        OverclockCalculator calculator = new OverclockCalculator().setEUt(availableEUt)
            .setRecipeEUt(RECIPE_EUT)
            .setDuration(RECIPE_DURATION)
            .setParallel(originalMaxParallel);

        maxParallel = GTUtility.longToInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()));

        int maxParallelBeforeBatchMode = maxParallel;
        var batchMode = getComponent(IBatchConfigComponent.KEY);
        int maxBatchSize = batchMode.getMaxBatchSize();
        if (batchMode.isBatchModeEnabled()) {
            maxParallel = GTUtility.longToInt((long) maxParallel * maxBatchSize);
        }

        maxParallel = Math.min(maxParallel, GTUtility.longToInt(availableEUt / RECIPE_EUT));

        int currentParallel = 0;
        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);

            if (smeltedOutput == null) continue;

            int parallelsLeft = maxParallel - currentParallel;
            if (parallelsLeft <= 0) break;

            currentParallel += Math.min(item.stackSize, parallelsLeft);
        }

        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);
        calculator.setCurrentParallel(currentParallelBeforeBatchMode)
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < maxBatchSize) {
            batchMultiplierMax = (double) maxBatchSize / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
        }

        int finalParallel = (int) (batchMultiplierMax * currentParallelBeforeBatchMode);

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(getOutputBusses(), protectsExcessItem(), true);

        // Consume items and generate outputs
        HashMap<GTUtility.ItemId, ItemStack> smeltedOutputs = new HashMap<>();
        int toSmelt = finalParallel;

        for (ItemStack item : tInput) {
            ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(item, false, null);

            if (smeltedOutput == null) continue;

            int remainingToSmelt = Math.min(toSmelt, item.stackSize);

            int smeltable = ejectionHelper
                .ejectItems(Collections.singletonList(smeltedOutput.copy()), remainingToSmelt);

            if (smeltable == 0) continue;

            ItemStack outputStack = smeltedOutputs
                .computeIfAbsent(GTUtility.ItemId.create(smeltedOutput), x -> GTUtility.copyAmount(0, smeltedOutput));
            outputStack.stackSize += smeltedOutput.stackSize * smeltable;

            item.stackSize -= smeltable;
            toSmelt -= smeltable;
            if (toSmelt <= 0) break;
        }

        if (smeltedOutputs.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        logic.setOutputItems(
            smeltedOutputs.values()
                .toArray(new ItemStack[0]));
        logic.setMaxProgressTime((int) (calculator.getDuration() * batchMultiplierMax));
        logic.setEUt(
            -Math.abs(
                Math.min(
                    VP[GTUtility.getTier(energyInputLogic.getAverageInputVoltage())],
                    calculator.getConsumption())));
        logic.setCurrentParallels(finalParallel);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public IStructureDefinition<MTEMultiFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.mLevel = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0, errors)) return;
    }

    @Override
    public void verifyMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public String[] getInfoData() {
        var energyInput = getComponent(IMultiblockEnergyInputLogicComponent.KEY);
        var recipeLogic = getComponent(IRecipeLogicComponent.KEY);
        var efficiencyLogic = getComponent(IRecipeEfficiencyLogicComponent.KEY);
        var pollutionLogic = getComponent(IPollutionLogicComponent.KEY);
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (var hatch : energyInput.getValidNormalEnergyHatches()) {
            storedEnergy += hatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += hatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long maxInputVoltage = energyInput.getMaxInputVoltage();
        return new String[] {
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.Progress.fmt.s",
                formatNumber(recipeLogic.getProgressTime() / 20),
                formatNumber(recipeLogic.getMaxProgressTime() / 20)),
            IGregTechDeviceInformation
                .encode("GT5U.multiblock.energy.fmt", formatNumber(storedEnergy), formatNumber(maxEnergy)),
            IGregTechDeviceInformation.encode("GT5U.multiblock.usage.fmt", formatNumber(-recipeLogic.getEUt())),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.mei.fmt.2A",
                formatNumber(maxInputVoltage),
                VN[GTUtility.getTier(maxInputVoltage)]),
            IGregTechDeviceInformation.encode(
                "GT5U.multiblock.problems.efficiency.fmt",
                getIdealStatus() - getRepairStatus(),
                efficiencyLogic.getEfficiency() / 100.0F + " %"),
            IGregTechDeviceInformation.encode("GT5U.infodata.ms.multismelting", mLevel),
            IGregTechDeviceInformation
                .encode("GT5U.multiblock.pollution.fmt", pollutionLogic.getAveragePollutionPercentage()),
            IGregTechDeviceInformation
                .encode("GT5U.multiblock.recipesDone.fmt", formatNumber(recipeLogic.getRecipesDone())) };
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setInteger("maxParallelRecipes", mLevel);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (isFormed()) return SURVIVAL_BUILD_DONE;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }
}
