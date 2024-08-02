package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import goodgenerator.loader.Loaders;
import goodgenerator.items.MyMaterial;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ExoticEnergyInputHelper;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class AntimatterForge extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<AntimatterForge>
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    
    private static final FluidStack[] magneticUpgrades = { Materials.TengamAttuned.getMolten(1L), MaterialsUEVplus.Time.getMolten(1L) };
    private static final FluidStack[] gravityUpgrades = { MaterialsUEVplus.SpaceTime.getMolten(1L), MaterialsUEVplus.Space.getMolten(1L), MaterialsUEVplus.Eternity.getMolten(1L) };
    private static final FluidStack[] containmentUpgrades = { MyMaterial.shirabon.getMolten(1), MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1L) };
    private static final FluidStack[] activationUpgrades = { MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1), MyMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };

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

    private float[] modifiers = {0.0f, 0.0f, 0.0f, 0.0f};
    private FluidStack[] upgradeFluids = {null, null, null, null};
    private int[] fluidConsumptions = {0, 0, 0, 0};

    public static final String MAIN_NAME = "antimatterForge";
    public static final int M = 1_000_000;
    private int speed = 100;
    private long rollingCost = 0L;
    private boolean isLoadedChunk;
    public GT_Recipe mLastRecipe;
    public int para;
    private Random r = new Random();
    private List<AntimatterOutputHatch> amOutputHatches = new ArrayList<>(16);
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {

        @Override
        protected IStructureDefinition<AntimatterForge> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterForge>builder()
                .addShape(MAIN_NAME, ForgeStructure)
                .addElement('A', lazy(x -> ofBlock(x.getFrameBlock(), x.getFrameMeta())))
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta(1))))
                .addElement(
                    'F',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(
                                GT_HatchElement.InputHatch)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex(2))
                            .dot(1)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
                    'E',
                    lazy(
                        x -> buildHatchAdder(AntimatterForge.class)
                            .adder(AntimatterForge::addAntimatterHatch)
                            .hatchClass(AntimatterOutputHatch.class)
                            .casingIndex(x.textureIndex(1))
                            .dot(3)
                            .build()))
                .addElement(
                    'H',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(GT_HatchElement.Energy.or(GT_HatchElement.ExoticEnergy))
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
     * 
     * Produces (Antimatter^0.6) * N(0.2, 1) of antimatter per cycle, consuming Protomatter equal to the change in Antimatter.
     * The change can be negative! (Normal Distribution)
     * Consumes (Antimatter * 1000)^(1.5) EU/t passively. The consumption will decay by 0.5% every tick if no antimatter is found.
     * Uses (Antimatter * 10000)^1.5 EU per operation to produce antimatter.
     * 
     * Every cycle, the lowest amount of antimatter in the 16 antimatter hatches is recoded.
     * All other hatches will have their antimatter amount reduced
     * 
     * 
     * If the machine runs out of energy or protomatter during a cycle, one tenth of the antimatter will be voided!
     * 
     * Can be supplied with stabilization fluids to improve antimatter generation.
     * Magnetic Stabilization (Uses Antimatter^(1/2) per operation)
     * 1. Molten Purified Tengam        - Passive cost exponent -0.15
     * 2. Tachyon Rich Fluid            - Passive cost exponent -0.30
     * Gravity Stabilization (Uses Antimatter^(1/2) per operation)
     * 1. Molten Spacetime              - Active cost exponent -0.05
     * 2. Spatially Enlarged Fluid      - Active cost exponent -0.10
     * 3. Molten Eternity               - Active cost exponent -0.15
     * Containment Stabilization (Uses Antimatter^(2/7) per operation)
     * 1. Molten Shirabon               - Production exponent +0.05
     * 2. Molten MHDCSM                 - Production exponent +0.10
     * Activation Stabilization (Uses Antimatter^(1/3) per operation)
     * 1. Depleted Naquadah Fuel Mk V   - Distribution skew +0.05
     * 2. Depleted Naquadah Fuel Mk VI  - Distribution skew +0.10
     */

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Antimatter Forge")
            .addInfo("Dimensions not included!")
            .addSeparator()
            .addCasingInfoMin("Placeholder", 1664, false)
            .addCasingInfoMin("Placeholder", 560, false)
            .addCasingInfoMin("Placeholder", 128, false)
            .addCasingInfoMin("Placeholder", 63, false)
            .addEnergyHatch("1-32, Hint block with dot 2", 2)
            .addInputHatch("1-16, Hint block with dot 1", 1)
            .addOutputHatch("1-16, Hint block with dot 1", 1)
            .addStructureInfo(
                "ALL Hatches must be " + GT_Utility.getColoredTierNameFromTier((byte) hatchTier())
                    + EnumChatFormatting.GRAY
                    + " or better")
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
        switch(type) {
            case 1:
                return Loaders.magneticFluxCasing;
            case 2:
                return Loaders.gravityStabilizationCasing;
            default:
                return Loaders.magneticFluxCasing;
        }
    }

    public int getCasingMeta(int type) {
        switch(type) {
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
        switch(type) {
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
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
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
                if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
                antimatterStored[i] = amOutputHatches.get(i).getFluid().copy();
                totalAntimatterAmount += antimatterStored[i].amount;
            }
            drainEnergyInput(calculateEnergyContainmentCost(totalAntimatterAmount));
        }
    }

    @Override
    public CheckRecipeResult checkProcessing() {
        startRecipeProcessing();
        FluidStack[] antimatterStored = new FluidStack[16];
        long totalAntimatterAmount = 0;
        long minAntimatterAmount = Long.MAX_VALUE;
        //Calculate the total amount of antimatter in all 16 hatches and the minimum amount found in any individual hatch
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
            antimatterStored[i] = amOutputHatches.get(i).getFluid().copy();
            totalAntimatterAmount += antimatterStored[i].amount;
            minAntimatterAmount = Math.min(minAntimatterAmount, antimatterStored[i].amount);
        }

        //Reduce the amount of antimatter in each hatch by half of the difference between the lowest amount and current hatch contents 
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
            FluidStack fluid = amOutputHatches.get(i).getFluid().copy();
            amOutputHatches.get(i).drain((int)((fluid.amount - minAntimatterAmount) * 0.5), true);
        }

        //Check for upgrade fluids
        long protomatterCost = calculateProtoMatterCost(totalAntimatterAmount);
        long containedProtomatter = 0;

        fluidConsumptions[MAGNETIC_ID] = (int)Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[GRAVITY_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 0.5));
        fluidConsumptions[CONTAINMENT_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 2/7));
        fluidConsumptions[ACTIVATION_ID] = (int) Math.ceil(Math.pow(totalAntimatterAmount, 1/3));

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
                if (inputFluid.isFluidEqual(magneticUpgrades[tier-1])) {
                    if (inputFluid.amount >= fluidConsumptions[MAGNETIC_ID]) {
                        modifiers[MAGNETIC_ID] = -0.15f * tier;
                        upgradeFluids[MAGNETIC_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= gravityUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(gravityUpgrades[tier-1])) {
                    if (inputFluid.amount >= fluidConsumptions[GRAVITY_ID]) {
                        modifiers[GRAVITY_ID] = -0.05f * tier;
                        upgradeFluids[GRAVITY_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= containmentUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(containmentUpgrades[tier-1])) {
                    if (inputFluid.amount >= fluidConsumptions[CONTAINMENT_ID]) {
                        modifiers[CONTAINMENT_ID] = 0.05f * tier;
                        upgradeFluids[CONTAINMENT_ID] = inputFluid;
                    }
                }
            }
            for (int tier = 1; tier <= activationUpgrades.length; tier++) {
                if (inputFluid.isFluidEqual(activationUpgrades[tier-1])) {
                    if (inputFluid.amount >= fluidConsumptions[ACTIVATION_ID]) {
                        modifiers[ACTIVATION_ID] = 0.05f * tier;
                        upgradeFluids[ACTIVATION_ID] = inputFluid;
                    }
                }
            }

        }

        long energyCost = calculateEnergyCost(totalAntimatterAmount);

        //If we run out of energy, reduce contained antimatter by 10%
        if (!drainEnergyInput(energyCost)) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            endRecipeProcessing();
            return CheckRecipeResultRegistry.insufficientPower(energyCost);
        }

        System.out.println("\nCalculating antimatter cycle:");
        System.out.format("Antimatter found: %d\n", totalAntimatterAmount);
        System.out.format("Protomatted found: %d\n", containedProtomatter);

        //Drain upgrade fluids
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

        int antimatterChange = distributeAntimatterToHatch(amOutputHatches, totalAntimatterAmount, containedProtomatter);

        //We didn't have enough protomatter, reduce antimatter by 10% and stop the machine.
        if (!this.depleteInput(MaterialsUEVplus.Protomatter.getFluid((long)Math.abs(antimatterChange)))) {
            decimateAntimatter();
            stopMachine(ShutDownReasonRegistry.outOfFluid(MaterialsUEVplus.Protomatter.getFluid(1L)));
            endRecipeProcessing();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = speed;

        endRecipeProcessing();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    /* How much passive energy is drained every tick
    *  Base containment cost: 10M EU/t
    *  The containment cost ramps up by the amount of antimatter each tick, up to 1000 times
    *  If the current cost is more than 1000 times the amount of antimatter, or
    *  if no antimatter is in the hatches, the value will decay by 1% every tick
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

    //How much energy is consumed when machine does one operation
    //Base formula: (Antimatter * 10000) ^ (1.5) 
    private long calculateEnergyCost(long antimatterAmount) {
        return (long) Math.pow(antimatterAmount * activeBaseMult, activeBaseExp + modifiers[GRAVITY_ID]);
    }

    //How much protomatter is required to do one operation
    private long calculateProtoMatterCost(long antimatterAmount) {
        return antimatterAmount + 1;
    }

    private void decimateAntimatter() {
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
            FluidStack fluid = amOutputHatches.get(i).getFluid().copy();
            amOutputHatches.get(i).drain((int)Math.floor(fluid.amount * 0.1), true);
        }
    }

    private int distributeAntimatterToHatch(List<AntimatterOutputHatch> hatches, long totalAntimatterAmount, long protomatterAmount) {
        double coeff = Math.pow((totalAntimatterAmount), 0.5 + modifiers[CONTAINMENT_ID]);
        int difference = 0;

        for (AntimatterOutputHatch hatch : hatches) {
            //Skewed normal distribution multiplied by coefficient from antimatter amount
            //We round up so you are guaranteed to be antimatter positive on the first run (reduces startup RNG)
            int change = (int) (Math.ceil((r.nextGaussian() + baseSkew + modifiers[ACTIVATION_ID]) * (coeff / 16)));
            difference += change;
            if (change >= 0) {
                hatch.fill(MaterialsUEVplus.Antimatter.getFluid((long)(change)), true);
            } else {
                hatch.drain(-change, true);
            }
        }
        System.out.format("Change this cycle: %d\n", difference);
        return difference;
    }

    @Override
    protected boolean shouldCheckRecipeThisTick(long aTick) {
        return (aTick % speed) == 0;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        amOutputHatches.clear();
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk) GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch
            && GT_ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mExoticEnergyHatches.add(hatch);
        }
        return false;
    }

    private boolean addFluidIO(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input tInput) {
            if (tInput.getTierForStructure() < hatchTier()) return false;
            tInput.mRecipeMap = getRecipeMap();
            return mInputHatches.add(tInput);
        }
        if (aMetaTileEntity instanceof AntimatterOutputHatch tAntimatter) {
            return amOutputHatches.add(tAntimatter);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output tOutput) {
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch) {
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
            case 6 -> EnumChatFormatting.RED + "I" + EnumChatFormatting.RESET;
            case 7 -> EnumChatFormatting.RED + "II" + EnumChatFormatting.RESET;
            case 8 -> EnumChatFormatting.RED + "III" + EnumChatFormatting.RESET;
            case 9 -> EnumChatFormatting.RED + "IV" + EnumChatFormatting.RESET;
            default -> EnumChatFormatting.GOLD + "V" + EnumChatFormatting.RESET;
        };
        double plasmaOut = 0;
        if (mMaxProgresstime > 0) plasmaOut = (double) mOutputFluids[0].amount / mMaxProgresstime;

        return new String[] { EnumChatFormatting.BLUE + "Fusion Reactor MK " + EnumChatFormatting.RESET + tier,
            StatCollector.translateToLocal("scanner.info.UX.0") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + GT_Utility.formatNumbers(this.para)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEUStore())
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(plasmaOut)
                + EnumChatFormatting.RESET
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

        private static String[][] ForgeStructure = {{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "          D                               D          ",
            "                                                     ",
            "          D                               D          ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "         DD                               DD         ",
            "          A                               A          ",
            "         DD                               DD         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "        DD                                 DD        ",
            "         A                                 A         ",
            "        DD                                 DD        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "       DDD                                 DDD       ",
            "        AA                                 AA        ",
            "       DDD                                 DDD       ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "       DD               D   D               DD       ",
            "        A               DD~DD               A        ",
            "       DD               D   D               DD       ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "      DDD              DDDDDDD              DDD      ",
            "       CA                                   AC       ",
            "      DDD              DDDDDDD              DDD      ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "     DDD            DDDCCCCCCCDDD            DDD     ",
            "      CA               CCCCCCC               AC      ",
            "     DDD            DDDCCCCCCCDDD            DDD     ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "     DDD          DDCCDCAAAAACDCCDD          DDD     ",
            "      CA            CCCBBBBBBBCCC            AC      ",
            "     DDD          DDCCCAAAAAAACCCDD          DDD     ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "    DDD          DCCACDCCCCCCCDCACCD          DDD    ",
            "     CA           CCBBBCCCCCCCBBBCC           AC     ",
            "    DDD          DCCAAACCCCCCCAAACCD          DDD    ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D                  DD   DD                  D    ",
            "   DDDD         DCAAACDDDDDDDDDCAAACD         DDDD   ",
            "    CAA          CBBBCC       CCBBBC          AAC    ",
            "   DDDD         DCAAACCDDDDDDDCCAAACD         DDDD   ",
            "    D                  DD   DD                  D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D                DD       DD                D    ",
            "   DDD         DCAACCDD E   E DDCCAACD         DDD   ",
            "    CA          CBBCC   D   D   CCBBC          AC    ",
            "   DDD         DCAACCDD E   E DDCCAACD         DDD   ",
            "    D                DD       DD                D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D              DD           DD              D    ",
            "   DDD        DCAACDD           DDCAACD        DDD   ",
            "    CA         CBBC               CBBC         AC    ",
            "   DDD        DCAACDD           DDCAACD        DDD   ",
            "    D              DD           DD              D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D              D               D              D   ",
            "  DDD        DCAACD               DCAACD        DDD  ",
            "   CA         CBBC                 CBBC         AC   ",
            "  DDD        DCAACD               DCAACD        DDD  ",
            "   D              D               D              D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D             D                 D             D   ",
            "  DDD        DCACD                 DCACD        DDD  ",
            "   CA         CBC                   CBC         AC   ",
            "  DDD        DCACD                 DCACD        DDD  ",
            "   D             D                 D             D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D             D                 D             D   ",
            "  DDD       DCAACD                 DCAACD       DDD  ",
            "   CA        CBBC                   CBBC        AC   ",
            "  DDD       DCAACD                 DCAACD       DDD  ",
            "   D             D                 D             D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D             D                   D             D  ",
            " DDDD       DCCCD                   DCCCD       DDDD ",
            "  CAA        CBC                     CBC        AAC  ",
            " DDDD       DCACD                   DCACD       DDDD ",
            "  D             D                   D             D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D             D                   D             D  ",
            " DDD        DDDDD                   DDDDD        DDD ",
            "  CA         CBC                     CBC         AC  ",
            " DDD        DCACD                   DCACD        DDD ",
            "  D             D                   D             D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D            D                     D            D  ",
            " DDD       DCCCD                     DCCCD       DDD ",
            "  CA        CBC                       CBC        AC  ",
            " DDD       DCACD                     DCACD       DDD ",
            "  D            D                     D            D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                        DDDDD                        ",
            "                        DD DD                        ",
            "                        DDCDD                        ",
            "                        DDADD                        ",
            "                        DDDDD                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "DDDDDDDDDDDDDDDD                     DDDDDDDDDDDDDDDD",
            "DDDD       DCACDE                   EDCACD       DDDD",
            "D CA        CBC D                   D CBC        AC D",
            "DDDD       DCACDE                   EDCACD       DDDD",
            "DDDDDDDDDDDDDDDD                     DDDDDDDDDDDDDDDD",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        DDDDD                        ",
            "                        DDADD                        ",
            "                        DDCDD                        ",
            "                        DD DD                        ",
            "                        DDDDD                        "
        },{
            "                                                     ",
            "                         D D                         ",
            "                         AFA                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            " DAD       DCACD                     DCACD       DAD ",
            "  HAAAAAAAAACBC                       CBCAAAAAAAAAH  ",
            " DAD       DCACD                     DCACD       DAD ",
            "               D                     D               ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         AFA                         ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                         AFA                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            " DAAAAAAAAAAAACD                     DCAAAAAAAAAAAAD ",
            "  HBBBBBBBBBBBC                       CBBBBBBBBBBBH  ",
            " DAAAAAAAAAAAACD                     DCAAAAAAAAAAAAD ",
            "               D                     D               ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         AFA                         ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                         AFA                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            " DAD       DCACD                     DCACD       DAD ",
            "  HAAAAAAAAACBC                       CBCAAAAAAAAAH  ",
            " DAD       DCACD                     DCACD       DAD ",
            "               D                     D               ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         AFA                         ",
            "                         D D                         ",
            "                                                     "
        },{
            "                        DDDDD                        ",
            "                        DD DD                        ",
            "                        DDCDD                        ",
            "                        DDADD                        ",
            "                        DDDDD                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "DDDDDDDDDDDDDDDD                     DDDDDDDDDDDDDDDD",
            "DDDD       DCACDE                   EDCACD       DDDD",
            "D CA        CBC D                   D CBC        AC D",
            "DDDD       DCACDE                   EDCACD       DDDD",
            "DDDDDDDDDDDDDDDD                     DDDDDDDDDDDDDDDD",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        DDDDD                        ",
            "                        DDADD                        ",
            "                        DDCDD                        ",
            "                        DD DD                        ",
            "                        DDDDD                        "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D            D                     D            D  ",
            " DDD       DCCCD                     DCCCD       DDD ",
            "  CA        CBC                       CBC        AC  ",
            " DDD       DCACD                     DCACD       DDD ",
            "  D            D                     D            D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D             D                   D             D  ",
            " DDD        DDDDD                   DDDDD        DDD ",
            "  CA         CBC                     CBC         AC  ",
            " DDD        DCACD                   DCACD        DDD ",
            "  D             D                   D             D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "  D             D                   D             D  ",
            " DDDD       DCCCD                   DCCCD       DDDD ",
            "  C A        CBC                     CBC        A C  ",
            " DDDD       DCACD                   DCACD       DDDD ",
            "  D             D                   D             D  ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D             D                 D             D   ",
            "  DDD       DCAACD                 DCAACD       DDD  ",
            "   CA        CBBC                   CBBC        AC   ",
            "  DDD       DCAACD                 DCAACD       DDD  ",
            "   D             D                 D             D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D             D                 D             D   ",
            "  DDD        DCACD                 DCACD        DDD  ",
            "   CA         CBC                   CBC         AC   ",
            "  DDD        DCACD                 DCACD        DDD  ",
            "   D             D                 D             D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "   D              D               D              D   ",
            "  DDD        DCAACD               DCAACD        DDD  ",
            "   CA         CBBC                 CBBC         AC   ",
            "  DDD        DCAACD               DCAACD        DDD  ",
            "   D              D               D              D   ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D              DD           DD              D    ",
            "   DDD        DCAACDD           DDCAACD        DDD   ",
            "    CA         CBBC               CBBC         AC    ",
            "   DDD        DCAACDD           DDCAACD        DDD   ",
            "    D              DD           DD              D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D                DD       DD                D    ",
            "   DDD         DCAACCDD E   E DDCCAACD         DDD   ",
            "    CA          CBBCC   D   D   CCBBC          AC    ",
            "   DDD         DCAACCDD E   E DDCCAACD         DDD   ",
            "    D                DD       DD                D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "    D                  DD   DD                  D    ",
            "   DDDD         DCAAACDDDDDDDDDCAAACD         DDDD   ",
            "    C A          CBBBCC       CCBBBC          A C    ",
            "   DDDD         DCAAACCDDDDDDDCCAAACD         DDDD   ",
            "    D                  DDDDDDD                  D    ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "    DDD          DCCACDCCCCCCCDCACCD          DDD    ",
            "     CA           CCBBBCCCCCCCBBBCC           AC     ",
            "    DDD          DCCAAACCCCCCCAAACCD          DDD    ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "     DDD          DDCCDCAAAAACDCCDD          DDD     ",
            "      CA            CCCBBBBBBBCCC            AC      ",
            "     DDD          DDCCCAAAAAAACCCDD          DDD     ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "     DDDD           DDDCCCACCCDDD           DDDD     ",
            "      C A              CCCBCCC              A C      ",
            "     DDDD           DDDCCCACCCDDD           DDDD     ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "      DDD              DDDADDD              DDD      ",
            "       CA                ABA                AC       ",
            "      DDD              DDDADDD              DDD      ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "       DDD                A                DDD       ",
            "        CA               ABA               AC        ",
            "       DDD                A                DDD       ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "       DDDD               A               DDDD       ",
            "        C A              ABA              A C        ",
            "       DDDD               A               DDDD       ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "        DDDDD             A             DDDDD        ",
            "         CCAA            ABA            AACC         ",
            "        DDDDD             A             DDDDD        ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "         DDDDD            A            DDDDD         ",
            "           C A           ABA           A C           ",
            "         DDDDD            A            DDDDD         ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "           DDDDD          A          DDDDD           ",
            "            CCAA         ABA         AACC            ",
            "           DDDDD          A          DDDDD           ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DCD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         D D                         ",
            "                         DCD                         ",
            "                         DCD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        D   D                        ",
            "            DDDDDD        A        DDDDDD            ",
            "              CCAA       ABA       AA C              ",
            "            DDDDDD        A        DDDDDD            ",
            "                        D   D                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                         DCD                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                                                     ",
            "                                                     ",
            "               DDD      DDDDD      DDD               ",
            "              DDDDDDDD    A    DDDDDDDD              ",
            "               CCCAAAA   ABA   AAAACCC               ",
            "              DDDDDDDD    A    DDDDDDDD              ",
            "               DDD      DDDDD      DDD               ",
            "                                                     ",
            "                                                     ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                         DDD                         ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                  DDD   DDADD   DDD                  ",
            "               DDDDDDDDDDDADDDDDDDDDDD               ",
            "                  CCC AAADBDAAA CCC                  ",
            "               DDDDDDDDDDDADDDDDDDDDDD               ",
            "                  DDD   DDADD   DDD                  ",
            "                         DAD                         ",
            "                         DAD                         ",
            "                         DDD                         ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                     DDDDDCDDDDD                     ",
            "                  DDDDDDDDCDDDDDDDD                  ",
            "                     CCCCHHHCCCC                     ",
            "                  DDDDDDDDCDDDDDDDD                  ",
            "                     DDDDDCDDDDD                     ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                        DDCDD                        ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                        DDDDD                        ",
            "                     DDDDDDDDDDD                     ",
            "                                                     ",
            "                     DDDDDDDDDDD                     ",
            "                        DD DD                        ",
            "                         D D                         ",
            "                         D D                         ",
            "                         D D                         ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        },{
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                        DDDDD                        ",
            "                        D   D                        ",
            "                        D   D                        ",
            "                        D   D                        ",
            "                        DDDDD                        ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     ",
            "                                                     "
        }};
    }
