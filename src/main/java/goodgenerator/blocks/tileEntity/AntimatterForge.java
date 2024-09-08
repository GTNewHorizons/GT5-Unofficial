package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class AntimatterForge extends MTEExtendedPowerMultiBlockBase<AntimatterForge>
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    private static final FluidStack[] magneticUpgrades = { Materials.TengamAttuned.getMolten(1L),
        MaterialsUEVplus.Time.getMolten(1L) };
    private static final FluidStack[] gravityUpgrades = { MaterialsUEVplus.SpaceTime.getMolten(1L),
        MaterialsUEVplus.Space.getMolten(1L), MaterialsUEVplus.Eternity.getMolten(1L) };
    private static final FluidStack[] containmentUpgrades = { GGMaterial.shirabon.getMolten(1),
        MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1L) };
    private static final FluidStack[] activationUpgrades = { GGMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
        GGMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };

    private static final int MAGNETIC_ID = 0;
    private static final int GRAVITY_ID = 1;
    private static final int CONTAINMENT_ID = 2;
    private static final int ACTIVATION_ID = 3;

    private static final int passiveBaseMult = 1000;
    private static final int activeBaseMult = 10000;

    private static final float passiveBaseExp = 1.5f;
    private static final float activeBaseExp = 1.5f;
    private static final float coefficientBaseExp = 0.5f;
    private static final float baseSkew = 0.2f;

    private float[] modifiers = { 0.0f, 0.0f, 0.0f, 0.0f };
    private FluidStack[] upgradeFluids = { null, null, null, null };
    private int[] fluidConsumptions = { 0, 0, 0, 0 };

    public static final String MAIN_NAME = "antimatterForge";
    public static final int M = 1_000_000;
    private int speed = 100;
    private long rollingCost = 0L;
    private boolean isLoadedChunk;
    public GTRecipe mLastRecipe;
    public int para;
    private Random r = new Random();
    private List<AntimatterOutputHatch> amOutputHatches = new ArrayList<>(16);
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {

        @Override
        protected IStructureDefinition<AntimatterForge> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterForge>builder()
                .addShape(MAIN_NAME, AntimatterStructures.ANTIMATTER_FORGE)
                .addElement('A', lazy(x -> ofBlock(x.getFrameBlock(), x.getFrameMeta())))
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta(1))))
                .addElement(
                    'F',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(HatchElement.InputHatch)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex(2))
                            .dot(1)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'E',
                    lazy(
                        x -> buildHatchAdder(AntimatterForge.class).adder(AntimatterForge::addAntimatterHatch)
                            .hatchClass(AntimatterOutputHatch.class)
                            .casingIndex(x.textureIndex(1))
                            .dot(3)
                            .build()))
                .addElement(
                    'H',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(HatchElement.Energy.or(HatchElement.ExoticEnergy))
                            .adder(AntimatterForge::addEnergyInjector)
                            .casingIndex(x.textureIndex(2))
                            .dot(4)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .build();
        }
    };

    static {
        Textures.BlockIcons.setCasingTextureForId(
            52,
            TextureFactory.of(
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_ANTIMATTER_GLOW)
                    .extFacing()
                    .glow()
                    .build()));
    }

    public AntimatterForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public AntimatterForge(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new AntimatterForge(MAIN_NAME);
    }

    /*
     * Produces (Antimatter^0.6) * N(0.2, 1) of antimatter per cycle, consuming Protomatter equal to the change in
     * Antimatter.
     * The change can be negative! (Normal Distribution)
     * Consumes (Antimatter * 1000)^(1.5) EU/t passively. The consumption will decay by 0.5% every tick if no antimatter
     * is found.
     * Uses (Antimatter * 10000)^1.5 EU per operation to produce antimatter.
     * Every cycle, the lowest amount of antimatter in the 16 antimatter hatches is recoded.
     * All other hatches will have their antimatter amount reduced
     * If the machine runs out of energy or protomatter during a cycle, one tenth of the antimatter will be voided!
     * Can be supplied with stabilization fluids to improve antimatter generation.
     * Magnetic Stabilization (Uses Antimatter^(1/2) per operation)
     * 1. Molten Purified Tengam - Passive cost exponent -0.15
     * 2. Tachyon Rich Fluid - Passive cost exponent -0.30
     * Gravity Stabilization (Uses Antimatter^(1/2) per operation)
     * 1. Molten Spacetime - Active cost exponent -0.05
     * 2. Spatially Enlarged Fluid - Active cost exponent -0.10
     * 3. Molten Eternity - Active cost exponent -0.15
     * Containment Stabilization (Uses Antimatter^(2/7) per operation)
     * 1. Molten Shirabon - Production exponent +0.05
     * 2. Molten MHDCSM - Production exponent +0.10
     * Activation Stabilization (Uses Antimatter^(1/3) per operation)
     * 1. Depleted Naquadah Fuel Mk V - Distribution skew +0.05
     * 2. Depleted Naquadah Fuel Mk VI - Distribution skew +0.10
     */

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Antimatter Forge")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Dimensions not included!" + EnumChatFormatting.GRAY)
            .addInfo("Converts protomatter into antimatter")
            .addInfo(
                "Consumes (" + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " * 1000)^"
                    + EnumChatFormatting.GREEN
                    + "1.5"
                    + EnumChatFormatting.GRAY
                    + " EU/t passively. The consumption decays by 0.5% every tick when empty")
            .addInfo(
                "Uses (" + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " * 10000)^"
                    + EnumChatFormatting.DARK_PURPLE
                    + "1.5"
                    + EnumChatFormatting.GRAY
                    + " EU per operation to produce antimatter")
            .addSeparator()
            .addInfo("Every cycle, the lowest amount of antimatter in the 16 antimatter hatches is recorded")
            .addInfo(
                "All hatches with more than the lowest amount will " + EnumChatFormatting.RED
                    + "lose half the difference!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "If the machine runs out of energy or protomatter during a cycle, " + EnumChatFormatting.RED
                    + "10% of antimatter will be voided!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "Produces (" + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^"
                    + EnumChatFormatting.GOLD
                    + "0.5"
                    + EnumChatFormatting.GRAY
                    + ") * N("
                    + EnumChatFormatting.AQUA
                    + "0.5"
                    + EnumChatFormatting.GRAY
                    + ", 1) of antimatter per cycle, consuming equal amounts of Protomatter")
            .addInfo("The change can be negative! (N = Skewed Normal Distribution)")
            .addSeparator()
            .addInfo("Can be supplied with stabilization fluids to improve antimatter generation")
            .addInfo(
                EnumChatFormatting.GREEN + "Magnetic Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Uses "
                    + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^0.5 per cycle)")
            .addInfo(
                "1. Molten Purified Tengam - Passive cost exponent " + EnumChatFormatting.GREEN
                    + "-0.15"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "2. Tachyon Rich Fluid - Passive cost exponent " + EnumChatFormatting.GREEN
                    + "-0.3"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                EnumChatFormatting.DARK_PURPLE + "Gravity Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Uses "
                    + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^0.5 per cycle)")
            .addInfo(
                "1. Molten Spacetime - Active cost exponent " + EnumChatFormatting.DARK_PURPLE
                    + "-0.05"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "2. Spatially Enlarged Fluid - Active cost exponent " + EnumChatFormatting.DARK_PURPLE
                    + "-0.10"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "3. Molten Eternity - Active cost exponent " + EnumChatFormatting.DARK_PURPLE
                    + "-0.15"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                EnumChatFormatting.GOLD + "Containment Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Uses "
                    + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^(2/7) per operation)")
            .addInfo(
                "1. Molten Shirabon - Production exponent " + EnumChatFormatting.GOLD
                    + "+0.05"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "2. Molten MHDCSM - Production exponent " + EnumChatFormatting.GOLD + "+0.10" + EnumChatFormatting.GRAY)
            .addInfo(
                EnumChatFormatting.AQUA + "Activation Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Uses "
                    + EnumChatFormatting.DARK_BLUE
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^(1/3) per operation)")
            .addInfo(
                "1. Depleted Naquadah Fuel Mk V - Distribution skew " + EnumChatFormatting.AQUA
                    + "+0.05"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "2. Depleted Naquadah Fuel Mk VI - Distribution skew " + EnumChatFormatting.AQUA
                    + "+0.10"
                    + EnumChatFormatting.GRAY)
            .addSeparator()
            .addEnergyHatch("1-9, Hint block with dot 4", 4)
            .addInputHatch("1-6, Hint block with dot 1", 1)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public IStructureDefinition<AntimatterForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    public int tier() {
        return 1;
    }

    @Override
    public long maxEUStore() {
        return 100_000_000;
    }

    public Block getCasingBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.magneticFluxCasing;
            case 2:
                return Loaders.gravityStabilizationCasing;
            default:
                return Loaders.magneticFluxCasing;
        }
    }

    public int getCasingMeta(int type) {
        switch (type) {
            case 1:
                return 0;
            case 2:
                return 0;
            default:
                return 0;
        }
    }

    public Block getCoilBlock() {
        return Loaders.protomatterActivationCoil;
    }

    public int getCoilMeta() {
        return 0;
    }

    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    public int getGlassMeta() {
        return 3;
    }

    public int hatchTier() {
        return 6;
    }

    public Block getFrameBlock() {
        return Loaders.antimatterContainmentCasing;
    }

    public int getFrameMeta() {
        return 0;
    }

    public int textureIndex(int type) {
        switch (type) {
            case 1:
                return (12 << 7) + 9;
            case 2:
                return (12 << 7) + 10;
            default:
                return (12 << 7) + 9;
        }
    }

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION1_GLOW)
            .extFacing()
            .glow()
            .build());

    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(MAIN_NAME, 26, 26, 4);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(MAIN_NAME, itemStack, b, 26, 26, 4);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(MAIN_NAME, stackSize, 26, 26, 4, realBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(), getTextureOverlay() };
        if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build() };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 100;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            FluidStack[] antimatterStored = new FluidStack[16];
            long totalAntimatterAmount = 0;
            for (int i = 0; i < amOutputHatches.size(); i++) {
                if (amOutputHatches.get(i) == null || !amOutputHatches.get(i)
                    .isValid()
                    || amOutputHatches.get(i)
                        .getFluid() == null)
                    continue;
                antimatterStored[i] = amOutputHatches.get(i)
                    .getFluid()
                    .copy();
                totalAntimatterAmount += antimatterStored[i].amount;
            }
            drainEnergyInput(calculateEnergyContainmentCost(totalAntimatterAmount));

            if ((this.mProgresstime >= this.mMaxProgresstime) && (!isAllowedToWork())) {
                setProtoRender(false);
            }
        }
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        startRecipeProcessing();
        FluidStack[] antimatterStored = new FluidStack[16];
        long totalAntimatterAmount = 0;
        long minAntimatterAmount = Long.MAX_VALUE;
        // Calculate the total amount of antimatter in all 16 hatches and the minimum amount found in any individual
        // hatch
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i)
                .isValid()
                || amOutputHatches.get(i)
                    .getFluid() == null)
                continue;
            antimatterStored[i] = amOutputHatches.get(i)
                .getFluid()
                .copy();
            totalAntimatterAmount += antimatterStored[i].amount;
            minAntimatterAmount = Math.min(minAntimatterAmount, antimatterStored[i].amount);
        }

        // Reduce the amount of antimatter in each hatch by half of the difference between the lowest amount and current
        // hatch contents
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i)
                .isValid()
                || amOutputHatches.get(i)
                    .getFluid() == null)
                continue;
            FluidStack fluid = amOutputHatches.get(i)
                .getFluid()
                .copy();
            amOutputHatches.get(i)
                .drain((int) ((fluid.amount - minAntimatterAmount) * 0.5), true);
        }

        // Check for upgrade fluids
        long protomatterCost = calculateProtoMatterCost(totalAntimatterAmount);
        long containedProtomatter = 0;

        fluidConsumptions[MAGNETIC_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[GRAVITY_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[CONTAINMENT_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 2 / 7));
        fluidConsumptions[ACTIVATION_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 1 / 3));

        for (int i = 0; i < modifiers.length; i++) {
            modifiers[i] = 0.0f;
            upgradeFluids[i] = null;
        }

        List<FluidStack> inputFluids = getStoredFluids();
        for (int i = 0; i < inputFluids.size(); i++) {
            FluidStack inputFluid = inputFluids.get(i);
            if (inputFluid.isFluidEqual(MaterialsUEVplus.Protomatter.getFluid(1))) {
                containedProtomatter += inputFluid.amount;
                continue;
            }
            for (int tier = 1; tier <= magneticUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(magneticUpgrades[tier - 1])) {
                    if (inputFluid.amount >= fluidConsumptions[MAGNETIC_ID]) {
                        modifiers[MAGNETIC_ID] = -0.15f * tier;
                        upgradeFluids[MAGNETIC_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= gravityUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(gravityUpgrades[tier - 1])) {
                    if (inputFluid.amount >= fluidConsumptions[GRAVITY_ID]) {
                        modifiers[GRAVITY_ID] = -0.05f * tier;
                        upgradeFluids[GRAVITY_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= containmentUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(containmentUpgrades[tier - 1])) {
                    if (inputFluid.amount >= fluidConsumptions[CONTAINMENT_ID]) {
                        modifiers[CONTAINMENT_ID] = 0.05f * tier;
                        upgradeFluids[CONTAINMENT_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= activationUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(activationUpgrades[tier - 1])) {
                    if (inputFluid.amount >= fluidConsumptions[ACTIVATION_ID]) {
                        modifiers[ACTIVATION_ID] = 0.05f * tier;
                        upgradeFluids[ACTIVATION_ID] = inputFluid;
                    }
                }
            }

        }

        long energyCost = calculateEnergyCost(totalAntimatterAmount);

        // If we run out of energy, reduce contained antimatter by 10%
        if (!drainEnergyInput(energyCost)) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            endRecipeProcessing();
            setProtoRender(false);
            return CheckRecipeResultRegistry.insufficientPower(energyCost);
        }

        // Drain upgrade fluids
        for (int i = 0; i < upgradeFluids.length; i++) {
            FluidStack upgradeFluid = upgradeFluids[i];
            if (upgradeFluid != null) {
                for (FluidStack inputFluid : inputFluids.toArray(new FluidStack[0])) {
                    if (inputFluid.isFluidEqual(upgradeFluid)) {
                        inputFluid.amount -= fluidConsumptions[i];
                    }
                }
            }
        }

        int antimatterChange = distributeAntimatterToHatch(
            amOutputHatches,
            totalAntimatterAmount,
            containedProtomatter);

        // We didn't have enough protomatter, reduce antimatter by 10% and stop the machine.
        if (!this.depleteInput(MaterialsUEVplus.Protomatter.getFluid((long) Math.abs(antimatterChange)))) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.outOfFluid(MaterialsUEVplus.Protomatter.getFluid(1L)));
            endRecipeProcessing();
            setProtoRender(false);
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        updateAntimatterSize(totalAntimatterAmount + antimatterChange);
        setProtoRender(true);

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = speed;

        endRecipeProcessing();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /*
     * How much passive energy is drained every tick
     * Base containment cost: 10M EU/t
     * The containment cost ramps up by the amount of antimatter each tick, up to 1000 times
     * If the current cost is more than 1000 times the amount of antimatter, or
     * if no antimatter is in the hatches, the value will decay by 1% every tick
     */
    private long calculateEnergyContainmentCost(long antimatterAmount) {
        if (antimatterAmount == 0) {
            rollingCost *= 0.995;
            if (rollingCost < 100) rollingCost = 0;
        } else if (rollingCost < antimatterAmount * 1000) {
            rollingCost += antimatterAmount;
        } else {
            rollingCost *= 0.995;
        }
        return 10_000_000 + (long) Math.pow(rollingCost, 1.5 + modifiers[MAGNETIC_ID]);
    }

    // How much energy is consumed when machine does one operation
    // Base formula: (Antimatter * 10000) ^ (1.5)
    private long calculateEnergyCost(long antimatterAmount) {
        return (long) Math.pow(antimatterAmount * activeBaseMult, activeBaseExp + modifiers[GRAVITY_ID]);
    }

    // How much protomatter is required to do one operation
    private long calculateProtoMatterCost(long antimatterAmount) {
        return antimatterAmount + 1;
    }

    private void decimateAntimatter() {
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i)
                .isValid()
                || amOutputHatches.get(i)
                    .getFluid() == null)
                continue;
            FluidStack fluid = amOutputHatches.get(i)
                .getFluid()
                .copy();
            amOutputHatches.get(i)
                .drain((int) Math.floor(fluid.amount * 0.1), true);
        }
    }

    private int distributeAntimatterToHatch(List<AntimatterOutputHatch> hatches, long totalAntimatterAmount,
        long protomatterAmount) {
        double coeff = Math.pow((totalAntimatterAmount), 0.5 + modifiers[CONTAINMENT_ID]);
        int difference = 0;

        for (AntimatterOutputHatch hatch : hatches) {
            // Skewed normal distribution multiplied by coefficient from antimatter amount
            // We round up so you are guaranteed to be antimatter positive on the first run (reduces startup RNG)
            int change = (int) (Math.ceil((r.nextGaussian() + baseSkew + modifiers[ACTIVATION_ID]) * (coeff / 16)));
            difference += change;
            if (change >= 0) {
                hatch.fill(MaterialsUEVplus.Antimatter.getFluid((long) (change)), true);
            } else {
                hatch.drain(-change, true);
            }
        }
        return difference;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        amOutputHatches.clear();
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

    private boolean addEnergyInjector(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch && ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mExoticEnergyHatches.add(hatch);
        }
        return false;
    }

    private boolean addFluidIO(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof MTEHatchInput tInput) {
            if (tInput.getTierForStructure() < hatchTier()) return false;
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
        }
        if (aMetaTileEntity instanceof AntimatterOutputHatch tAntimatter) {
            return amOutputHatches.add(tAntimatter);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tOutput) {
            if (tOutput.getTierForStructure() < hatchTier()) return false;
            return mOutputHatches.add(tOutput);
        }
        if (aMetaTileEntity instanceof IDualInputHatch tInput) {
            tInput.updateCraftingIcon(this.getMachineCraftingIcon());
            return mDualInputHatches.add(tInput);
        }
        return false;
    }

    private boolean addAntimatterHatch(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof AntimatterOutputHatch tAntimatter) {
            return amOutputHatches.add(tAntimatter);
        }

        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public OverclockDescriber getOverclockDescriber() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        String tier = switch (tier()) {
            case 6 -> EnumChatFormatting.RED + "I" + EnumChatFormatting.GRAY;
            case 7 -> EnumChatFormatting.RED + "II" + EnumChatFormatting.GRAY;
            case 8 -> EnumChatFormatting.RED + "III" + EnumChatFormatting.GRAY;
            case 9 -> EnumChatFormatting.RED + "IV" + EnumChatFormatting.GRAY;
            default -> EnumChatFormatting.GOLD + "V" + EnumChatFormatting.GRAY;
        };
        double plasmaOut = 0;
        if (mMaxProgresstime > 0) plasmaOut = (double) mOutputFluids[0].amount / mMaxProgresstime;

        return new String[] { EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.GRAY + tier,
            StatCollector.translateToLocal("scanner.info.UX.0") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + GTUtility.formatNumbers(this.para)
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.GRAY
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                + EnumChatFormatting.GRAY
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEUStore())
                + EnumChatFormatting.GRAY
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(plasmaOut)
                + EnumChatFormatting.GRAY
                + "L/t" };
    }

    protected long energyStorageCache;
    protected long containmentCostCache;
    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    private long getContainmentCost() {
        return 10000000 + rollingCost;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.0") + " "
                            + numberFormat.format(energyStorageCache)
                            + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::maxEUStore, val -> energyStorageCache = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("gui.LargeFusion.1") + " "
                            + numberFormat.format((double) getContainmentCost())
                            + " EU")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0))
            .widget(new FakeSyncWidget.LongSyncer(this::getContainmentCost, val -> containmentCostCache = val));
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        setProtoRender(false);
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        destroyAntimatterRender();
    }

    public void updateAntimatterSize(float antimatterAmount) {
        TileAntimatter render = forceGetAntimatterRender();

        if (antimatterAmount < 0) {
            setProtoRender(false);
            render.setCoreSize(0);
            return;
        }

        float size = (float) Math.pow(antimatterAmount, 0.17);
        render.setCoreSize(size);
    }

    public void setProtoRender(boolean flag) {
        TileAntimatter render = forceGetAntimatterRender();
        render.setProtomatterRender(flag);
        if (flag) render.setRotationFields(getRotation(), getDirection());
    }

    public TileAntimatter getAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        World world = gregTechTileEntity.getWorld();

        if (world == null) {
            return null;
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        int wX = (int) (x + xOffset);
        int wY = (int) (y + yOffset);
        int wZ = (int) (z + zOffset);

        return (TileAntimatter) world.getTileEntity(wX, wY, wZ);
    }

    public void destroyAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        World world = gregTechTileEntity.getWorld();

        if (world == null) {
            return;
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        int xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        int xTarget = x + xOffset;
        int yTarget = y + yOffset;
        int zTarget = z + zOffset;

        world.setBlock(xTarget, yTarget, zTarget, Blocks.air);
    }

    public void createAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        World world = gregTechTileEntity.getWorld();

        if (world == null) {
            return;
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        int xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        int wX = x + xOffset;
        int wY = y + yOffset;
        int wZ = z + zOffset;

        world.setBlock(wX, wY, wZ, Blocks.air);
        world.setBlock(wX, wY, wZ, Loaders.antimatterRenderBlock);
    }

    public TileAntimatter forceGetAntimatterRender() {
        TileAntimatter render = getAntimatterRender();
        if (render != null) return render;
        else createAntimatterRender();
        return getAntimatterRender();
    }

}
