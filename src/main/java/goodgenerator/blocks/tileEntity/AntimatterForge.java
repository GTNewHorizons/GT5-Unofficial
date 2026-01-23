package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.objects.GTChunkManager;
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
import gregtech.common.gui.modularui.multiblock.AntimatterForgeGui;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class AntimatterForge extends MTEExtendedPowerMultiBlockBase<AntimatterForge>
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    private static final FluidStack[] magneticUpgrades = { Materials.TengamPurified.getMolten(1L),
        Materials.Time.getMolten(1L), Materials.MagMatter.getMolten(1L) };
    private static final FluidStack[] gravityUpgrades = { Materials.SpaceTime.getMolten(1L),
        Materials.Space.getMolten(1L), Materials.Eternity.getMolten(1L) };
    private static final FluidStack[] containmentUpgrades = { GGMaterial.shirabon.getMolten(1),
        Materials.MHDCSM.getMolten(1L) };
    private static final FluidStack[] activationUpgrades = { GGMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
        GGMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };

    private static final int MAGNETIC_ID = 0;
    private static final int GRAVITY_ID = 1;
    private static final int CONTAINMENT_ID = 2;
    private static final int ACTIVATION_ID = 3;

    private static final int BASE_CONSUMPTION = 10_000_000;
    private static final int passiveBaseMult = 1_000;
    private static final int activeBaseMult = 10_000;

    private static final float passiveBaseExp = 1.5f;
    private static final float activeBaseExp = 1.5f;
    private static final float coefficientBaseExp = 0.5f;
    private static final float baseSkew = 0.2f;

    private final float[] modifiers = { 0.0f, 0.0f, 0.0f, 0.0f };
    private final FluidStack[] upgradeFluids = { null, null, null, null };
    private final int[] fluidConsumptions = { 0, 0, 0, 0 };

    private static final FluidStack ZERO_ANTIMATTER = Materials.Antimatter.getFluid(0);

    public static final String MAIN_NAME = "antimatterForge";
    private final int speed = 20;
    private long rollingCost = 0L;
    private boolean isLoadedChunk;
    public GTRecipe mLastRecipe;
    public int para;
    private final Random r = new Random();
    // Values for displaying cycle data
    private long guiAntimatterAmount = 0;
    private long guiAntimatterChange = 0;
    private long guiPassiveEnergy = 0;
    private long guiActiveEnergy = 0;

    private boolean canRender = true;

    private final List<AntimatterOutputHatch> amOutputHatches = new ArrayList<>(16);
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTURE_DEFINITION = new ClassValue<>() {

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
                            .hint(1)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'E',
                    lazy(
                        x -> buildHatchAdder(AntimatterForge.class).adder(AntimatterForge::addAntimatterHatch)
                            .hatchClass(AntimatterOutputHatch.class)
                            .casingIndex(x.textureIndex(1))
                            .hint(3)
                            .build()))
                .addElement(
                    'H',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(HatchElement.Energy.or(HatchElement.ExoticEnergy))
                            .adder(AntimatterForge::addEnergyInjector)
                            .casingIndex(x.textureIndex(2))
                            .hint(2)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .build();
        }
    };

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

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Antimatter Forge, SSASS")
            .addInfo(EnumChatFormatting.LIGHT_PURPLE + "Dimensions not included!" + EnumChatFormatting.GRAY)
            .addInfo("Converts protomatter into antimatter")
            .addInfo("Use screwdriver to disable rendering")
            .addInfo(
                "Passively consumes " + formatNumber(BASE_CONSUMPTION)
                    + " + ("
                    + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " * "
                    + passiveBaseMult
                    + ")^("
                    + passiveBaseExp
                    + " - "
                    + EnumChatFormatting.GREEN
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "M"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ") EU/t")
            .addInfo("The consumption decays by 0.5% every tick when empty")
            .addInfo(
                "Actively uses (" + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + " * "
                    + formatNumber(activeBaseMult)
                    + ")^("
                    + activeBaseExp
                    + " - "
                    + EnumChatFormatting.DARK_PURPLE
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "G"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ") EU per cycle to produce antimatter")
            .addSeparator()
            .addInfo("Cycles every second")
            .addInfo("Every cycle, the lowest amount of antimatter in the 16 antimatter hatches is recorded")
            .addInfo(
                "All hatches with more than the lowest amount " + EnumChatFormatting.RED
                    + "lose half the difference!"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                EnumChatFormatting.RED + "Voids 10% of antimatter "
                    + EnumChatFormatting.GRAY
                    + "if it runs out of energy/protomatter during a cycle")
            .addInfo(
                "Base production per hatch is (1/16) * (" + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^("
                    + coefficientBaseExp
                    + " + "
                    + EnumChatFormatting.GOLD
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "C"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ")) of antimatter each cycle")
            .addInfo("Each hatch multiplies the base production per hatch with a random number pulled from")
            .addInfo(
                "a normal distribution with a mean of " + baseSkew
                    + " + "
                    + EnumChatFormatting.AQUA
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "A"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " and a variance of 1")
            .addInfo("The total gain of antimatter can be negative!")
            .addSeparator()
            .addInfo("Can be supplied with stabilization fluids to improve antimatter generation")
            .addInfo("Each stabilization can only use one of the fluids at a time")
            .addInfo(
                "" + EnumChatFormatting.GREEN
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "M"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GREEN
                    + "agnetic Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Consumes "
                    + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^0.5 L of fluid per cycle)")
            .addInfo("1. Molten Purified Tengam = " + EnumChatFormatting.GREEN + "0.1" + EnumChatFormatting.GRAY)
            .addInfo("2. Tachyon Rich Fluid = " + EnumChatFormatting.GREEN + "0.2" + EnumChatFormatting.GRAY)
            .addInfo("3. Molten MagMatter = " + EnumChatFormatting.GREEN + "0.3" + EnumChatFormatting.GRAY)
            .addInfo(
                "" + EnumChatFormatting.DARK_PURPLE
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "G"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.DARK_PURPLE
                    + "ravity Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Consumes "
                    + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^0.5 L of fluid per cycle)")
            .addInfo("1. Molten Spacetime = " + EnumChatFormatting.DARK_PURPLE + "0.05" + EnumChatFormatting.GRAY)
            .addInfo(
                "2. Spatially Enlarged Fluid = " + EnumChatFormatting.DARK_PURPLE + "0.10" + EnumChatFormatting.GRAY)
            .addInfo("3. Molten Eternity = " + EnumChatFormatting.DARK_PURPLE + "0.15" + EnumChatFormatting.GRAY)
            .addInfo(
                "" + EnumChatFormatting.GOLD
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "C"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GOLD
                    + "ontainment Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Consumes "
                    + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^(2/7) L of fluid per cycle)")
            .addInfo("1. Molten Shirabon = " + EnumChatFormatting.GOLD + "0.05" + EnumChatFormatting.GRAY)
            .addInfo("2. Molten MHDCSM = " + EnumChatFormatting.GOLD + "0.10" + EnumChatFormatting.GRAY)
            .addInfo(
                "" + EnumChatFormatting.AQUA
                    + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + "A"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.AQUA
                    + "ctivation Stabilization"
                    + EnumChatFormatting.GRAY
                    + " (Consumes "
                    + EnumChatFormatting.DARK_AQUA
                    + "Antimatter"
                    + EnumChatFormatting.GRAY
                    + "^(1/3) L of fluid per cycle)")
            .addInfo("1. Depleted Naquadah Fuel Mk V = " + EnumChatFormatting.AQUA + "0.05" + EnumChatFormatting.GRAY)
            .addInfo("2. Depleted Naquadah Fuel Mk VI = " + EnumChatFormatting.AQUA + "0.10" + EnumChatFormatting.GRAY)
            .beginStructureBlock(53, 53, 47, false)
            .addCasingInfoMin("Antimatter Containment Casing", 512, false)
            .addCasingInfoMin("Magnetic Flux Casing", 2274, false)
            .addCasingInfoMin("Gravity Stabilization Casing", 623, false)
            .addCasingInfoMin("Protomatter Activation Coil", 126, false)
            .addInputHatch("1-6, Hint Block Number 1", 1)
            .addEnergyHatch("1-9, Hint Block Number 2", 2)
            .addOtherStructurePart(
                StatCollector.translateToLocal("gg.structure.tooltip.antimatter_hatch"),
                "16, Hint Block Number 3",
                3)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<AntimatterForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    public Block getCasingBlock(int type) {
        if (type == 2) {
            return Loaders.gravityStabilizationCasing;
        }
        return Loaders.magneticFluxCasing;
    }

    public int getCasingMeta(int type) {
        return 0;
    }

    public Block getCoilBlock() {
        return Loaders.protomatterActivationCoil;
    }

    public int getCoilMeta() {
        return 0;
    }

    public Block getFrameBlock() {
        return Loaders.antimatterContainmentCasing;
    }

    public int getFrameMeta() {
        return 0;
    }

    public int textureIndex(int type) {
        if (type == 2) {
            return (12 << 7) + 10;
        }
        return (12 << 7) + 9;
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
        return survivalBuildPiece(MAIN_NAME, stackSize, 26, 26, 4, realBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1_GLOW)
                .extFacing()
                .glow()
                .build() };
        if (aActive) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(MACHINE_CASING_ANTIMATTER_GLOW)
                .extFacing()
                .glow()
                .build() };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_ANTIMATTER)
            .extFacing()
            .build() };
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

    private long calculateContainedAntimatter() {
        long antimatterStored = 0;
        for (AntimatterOutputHatch amOutputHatch : amOutputHatches) {
            if (amOutputHatch != null && amOutputHatch.isValid() && amOutputHatch.getFluid() != null) {
                antimatterStored += amOutputHatch.getFluid().amount;
            }
        }
        return antimatterStored;
    }

    private void setModifiers(FluidStack inputFluid, float step, FluidStack[] fluidArray, int upgradeId) {
        for (int tier = 1; tier <= fluidArray.length; tier++) {
            if (inputFluid.isFluidEqual(fluidArray[tier - 1])) {
                if (inputFluid.amount >= fluidConsumptions[upgradeId]) {
                    modifiers[upgradeId] = step * tier;
                    upgradeFluids[upgradeId] = inputFluid;
                }
            }
        }
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        FluidStack[] antimatterStored = new FluidStack[16];
        long totalAntimatterAmount = 0;
        long minAntimatterAmount = Long.MAX_VALUE;
        boolean hatchEmpty = false;
        // Calculate the total amount of antimatter in all 16 hatches and the minimum amount found in any individual
        // hatch
        for (int i = 0; i < amOutputHatches.size(); i++) {
            hatchEmpty = false;
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i)
                .isValid()) continue;

            if (amOutputHatches.get(i)
                .getFluid() == null) hatchEmpty = true;

            antimatterStored[i] = hatchEmpty ? ZERO_ANTIMATTER.copy()
                : amOutputHatches.get(i)
                    .getFluid()
                    .copy();
            totalAntimatterAmount += antimatterStored[i].amount;
            minAntimatterAmount = Math.min(minAntimatterAmount, antimatterStored[i].amount);
        }
        int ratioLosses = 0;
        // Reduce the amount of antimatter in each hatch by half of the difference between the lowest amount and current
        // hatch contents
        for (AntimatterOutputHatch amOutputHatch : amOutputHatches) {
            if (amOutputHatch != null && amOutputHatch.isValid() && amOutputHatch.getFluid() != null) {
                FluidStack fluid = amOutputHatch.getFluid()
                    .copy();
                ratioLosses -= amOutputHatch.drain((int) ((fluid.amount - minAntimatterAmount) * 0.5), true).amount;
            }
        }

        // Check for upgrade fluids
        long containedProtomatter = 0;

        fluidConsumptions[MAGNETIC_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[GRAVITY_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[CONTAINMENT_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 2.0f / 7.0f));
        fluidConsumptions[ACTIVATION_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 1.0f / 3.0f));

        for (int i = 0; i < modifiers.length; i++) {
            modifiers[i] = 0.0f;
            upgradeFluids[i] = null;
        }

        List<FluidStack> inputFluids = getStoredFluids();
        for (FluidStack inputFluid : inputFluids) {
            setModifiers(inputFluid, -0.1f, magneticUpgrades, MAGNETIC_ID);
            setModifiers(inputFluid, -0.05f, gravityUpgrades, GRAVITY_ID);
            setModifiers(inputFluid, 0.05f, containmentUpgrades, CONTAINMENT_ID);
            setModifiers(inputFluid, 0.05f, activationUpgrades, ACTIVATION_ID);
        }

        long energyCost = calculateEnergyCost(totalAntimatterAmount);

        // If we run out of energy, reduce contained antimatter by 10%
        if (!drainEnergyInput(energyCost)) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
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
        if (!this.depleteInput(Materials.Protomatter.getFluid(Math.abs(antimatterChange)))) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.outOfFluid(Materials.Protomatter.getFluid(1L)));
            setProtoRender(false);
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        this.guiAntimatterChange = ratioLosses + antimatterChange;
        this.guiAntimatterAmount = calculateContainedAntimatter();

        if (this.canRender) {
            updateAntimatterSize(this.guiAntimatterAmount);
            setProtoRender(true);
        }

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = speed;
        recipesDone++;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /*
     * How much passive energy is drained every tick Base containment cost: 10M EU/t The containment cost ramps up by
     * the amount of antimatter each tick, up to 1000 times If the current cost is more than 1000 times the amount of
     * antimatter, or if no antimatter is in the hatches, the value will decay by 1% every tick
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
        long value = BASE_CONSUMPTION + (long) Math.pow(rollingCost, 1.5 + modifiers[MAGNETIC_ID]);
        this.guiPassiveEnergy = value;
        return value;
    }

    // How much energy is consumed when machine does one cycle
    // Base formula: (Antimatter * 10000) ^ (1.5)
    private long calculateEnergyCost(long antimatterAmount) {
        long value = (long) Math.pow(antimatterAmount * activeBaseMult, activeBaseExp + modifiers[GRAVITY_ID]);
        this.guiActiveEnergy = value;
        return value;
    }

    private void decimateAntimatter() {
        for (AntimatterOutputHatch amOutputHatch : amOutputHatches) {
            if (amOutputHatch != null && amOutputHatch.isValid() && amOutputHatch.getFluid() != null) {
                FluidStack fluid = amOutputHatch.getFluid()
                    .copy();
                amOutputHatch.drain((int) Math.floor(fluid.amount * 0.1), true);
            }
        }
    }

    private int distributeAntimatterToHatch(List<AntimatterOutputHatch> hatches, long totalAntimatterAmount,
        long protomatterAmount) {
        double coeff = Math.pow((totalAntimatterAmount), 0.5 + modifiers[CONTAINMENT_ID]);
        int difference = 0;

        // a pull per hatch instead of a global one distributed randomly to increase the odds of pulling a outlier
        // in one of the hatches. It changes nothing when the automation is done successfully, but punishes more the
        // player when the automation is improperly done.

        for (AntimatterOutputHatch hatch : hatches) {
            // Skewed normal distribution multiplied by coefficient from antimatter amount
            // We round up so you are guaranteed to be antimatter positive on the first run (reduces startup RNG)
            int change = (int) (Math.ceil((r.nextGaussian() + baseSkew + modifiers[ACTIVATION_ID]) * (coeff / 16)));
            difference += change;
            if (change >= 0) {
                hatch.fill(Materials.Antimatter.getFluid(change), true);
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
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
        }
        if (aMetaTileEntity instanceof AntimatterOutputHatch tAntimatter) {
            return amOutputHatches.add(tAntimatter);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tOutput) {
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
    public OverclockDescriber getOverclockDescriber() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();

        long storedEnergy = 0;
        long maxEnergy = 0;

        for (MTEHatch tHatch : mExoticEnergyHatches) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("gg.scanner.info.antimatter_forge")
                + " "
                + EnumChatFormatting.GRAY,
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + formatNumber(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("gui.AntimatterForge.0") + ": "
                + EnumChatFormatting.BLUE
                + formatNumber(getAntimatterAmount())
                + EnumChatFormatting.RESET
                + " L",
            StatCollector.translateToLocal("gui.AntimatterForge.1") + ": "
                + EnumChatFormatting.RED
                + formatNumber(getPassiveConsumption())
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("gui.AntimatterForge.2") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + formatNumber(getActiveConsumption())
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("gui.AntimatterForge.3") + ": "
                + EnumChatFormatting.AQUA
                + formatNumber(getAntimatterChange())
                + EnumChatFormatting.RESET
                + " L",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    public long getAntimatterAmount() {
        return this.guiAntimatterAmount;
    }

    public long getPassiveConsumption() {
        return this.guiPassiveEnergy;
    }

    public long getActiveConsumption() {
        return this.guiActiveEnergy / 20;
    }

    public long getAntimatterChange() {
        return this.guiAntimatterChange;
    }

    @Override
    public boolean supportsLogo() {
        return false;
    }

    @Override
    protected @NotNull AntimatterForgeGui getGui() {
        return new AntimatterForgeGui(this);
    }

    @Override
    public boolean canBeMuffled() {
        return false;
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

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("canRender", this.canRender);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("canRender")) {
            this.canRender = aNBT.getBoolean("canRender");
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        this.canRender = !this.canRender;
        if (!this.canRender) {
            aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.antimatter_forge.disableRender"));
            destroyAntimatterRender();
        } else aPlayer.addChatMessage(new ChatComponentTranslation("GT5U.machines.antimatter_forge.enableRender"));
    }

    public void updateAntimatterSize(float antimatterAmount) {
        if (antimatterAmount <= 0 || !this.canRender) {
            destroyAntimatterRender();
            return;
        }

        TileAntimatter render = getAntimatterRender();
        if (render == null) {
            createAntimatterRender();
            render = getAntimatterRender();
            if (render == null) return;
        }

        float size = (float) Math.pow(antimatterAmount, 0.17);
        render.setCoreSize(size);
    }

    public void setProtoRender(boolean flag) {
        if (!this.canRender) return;
        TileAntimatter render = getAntimatterRender();
        if (render == null) return;
        render.setProtomatterRender(flag);
        if (!flag) return;
        render.setRotationFields(getDirection(), getRotation());
    }

    private int getTargetX(@NotNull IGregTechTileEntity gregTechTileEntity) {
        int x = gregTechTileEntity.getXCoord();
        int xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        return x + xOffset;
    }

    private int getTargetY(@NotNull IGregTechTileEntity gregTechTileEntity) {
        int y = gregTechTileEntity.getYCoord();
        int yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        return y + yOffset;
    }

    private int getTargetZ(@NotNull IGregTechTileEntity gregTechTileEntity) {
        int z = gregTechTileEntity.getZCoord();
        int zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        return z + zOffset;
    }

    public @Nullable TileAntimatter getAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return null;

        World world = gregTechTileEntity.getWorld();
        if (world == null) return null;

        final int x = getTargetX(gregTechTileEntity);
        final int y = getTargetY(gregTechTileEntity);
        final int z = getTargetZ(gregTechTileEntity);

        if (world.getTileEntity(x, y, z) instanceof TileAntimatter antimatterRender) return antimatterRender;
        return null;
    }

    public void destroyAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        World world = gregTechTileEntity.getWorld();
        if (world == null) return;

        final int x = getTargetX(gregTechTileEntity);
        final int y = getTargetY(gregTechTileEntity);
        final int z = getTargetZ(gregTechTileEntity);

        if (world.getBlock(x, y, z)
            .equals(Loaders.antimatterRenderBlock)) {
            world.setBlock(x, y, z, Blocks.air);
        }
    }

    public void createAntimatterRender() {
        IGregTechTileEntity gregTechTileEntity = getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        World world = gregTechTileEntity.getWorld();
        if (world == null) return;

        final int x = getTargetX(gregTechTileEntity);
        final int y = getTargetY(gregTechTileEntity);
        final int z = getTargetZ(gregTechTileEntity);

        if (world.isAirBlock(x, y, z)) {
            world.setBlock(x, y, z, Loaders.antimatterRenderBlock);
        }
    }
}
