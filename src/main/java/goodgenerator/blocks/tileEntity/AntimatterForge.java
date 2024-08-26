package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
<<<<<<< HEAD
<<<<<<< HEAD
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;
=======
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)

=======
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
>>>>>>> 3bd491c92e (Generator structure def do be crashing)

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

<<<<<<< HEAD
import org.jetbrains.annotations.NotNull;

import bartworks.common.loaders.ItemRegistry;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
=======
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

<<<<<<< HEAD
import goodgenerator.loader.Loaders;
<<<<<<< HEAD
<<<<<<< HEAD
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.GTValues;
=======
=======
import goodgenerator.items.MyMaterial;
>>>>>>> 97ed546ec8 (Add antimatter forge mechanics)
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
=======
import goodgenerator.blocks.structures.AntimatterStructures;
import goodgenerator.blocks.tileEntity.render.TileAntimatter;
import goodgenerator.items.MyMaterial;
import goodgenerator.loader.Loaders;
import gregtech.api.enums.GT_HatchElement;
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
<<<<<<< HEAD
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.overclockdescriber.FusionOverclockDescriber;
=======
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.GT_ItemStack;
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
<<<<<<< HEAD
<<<<<<< HEAD
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasingsAbstract;
=======
=======
import gregtech.api.util.GT_ExoticEnergyInputHelper;
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
<<<<<<< HEAD
import gregtech.common.blocks.GT_Block_Casings_Abstract;
>>>>>>> ef58e42a27 (Add antimatter)
=======
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
import gregtech.common.tileentities.machines.IDualInputHatch;

<<<<<<< HEAD
public class AntimatterForge extends MTEExtendedPowerMultiBlockBase
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    public static final String MAINNAME = "antimatterForge";
    public static final int M = 1000000;
=======
public class AntimatterForge extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<AntimatterForge>
    implements ISurvivalConstructable, IOverclockDescriptionProvider {

    private static final FluidStack[] magneticUpgrades = { Materials.TengamAttuned.getMolten(1L),
        MaterialsUEVplus.Time.getMolten(1L) };
    private static final FluidStack[] gravityUpgrades = { MaterialsUEVplus.SpaceTime.getMolten(1L),
        MaterialsUEVplus.Space.getMolten(1L), MaterialsUEVplus.Eternity.getMolten(1L) };
    private static final FluidStack[] containmentUpgrades = { MyMaterial.shirabon.getMolten(1),
        MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1L) };
    private static final FluidStack[] activationUpgrades = { MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
        MyMaterial.naquadahBasedFuelMkVIDepleted.getFluidOrGas(1) };

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
<<<<<<< HEAD
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
=======
    private long rollingCost = 0L;
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
    private boolean isLoadedChunk;
    public GTRecipe mLastRecipe;
    public int para;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    protected OverclockDescriber overclockDescriber;
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTUREDEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {
=======
    private List<AntimatterOutputHatch> antimatterOutputHatches = new ArrayList<>(16);
=======
=======
    private Random r = new Random();
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
    private List<AntimatterOutputHatch> amOutputHatches = new ArrayList<>(16);
>>>>>>> fa2399b90b (Add new structure for Antimatter Forge)
    private static final ClassValue<IStructureDefinition<AntimatterForge>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<AntimatterForge>>() {
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)

        @Override
        protected IStructureDefinition<AntimatterForge> computeValue(Class<?> type) {
            return StructureDefinition.<AntimatterForge>builder()
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
                .addShape(MAINNAME, transpose(new String[][] { L0, L1, L2, L3, L2, L1, L0 }))
                .addElement('H', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(), x.getCasingMeta())))
=======
                .addShape(MAIN_NAME, transpose(ForgeStructure))
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCoilMeta())))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta())))
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
                .addElement(
                    'F',
                    lazy(
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
                                HatchElement.InputHatch.or(HatchElement.InputBus))
=======
                .addShape(MAIN_NAME, ForgeStructure)
=======
                .addShape(MAIN_NAME, AntimatterStructures.ANTIMATTER_FORGE)
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
                .addElement('A', lazy(x -> ofBlock(x.getFrameBlock(), x.getFrameMeta())))
                .addElement('B', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('C', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta(1))))
                .addElement(
                    'F',
                    lazy(
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
<<<<<<< HEAD
                            .anyOf(
<<<<<<< HEAD
                                GT_HatchElement.InputHatch.or(GT_HatchElement.InputBus))
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
=======
                                GT_HatchElement.InputHatch)
>>>>>>> 97ed546ec8 (Add antimatter forge mechanics)
=======
                            .anyOf(GT_HatchElement.InputHatch)
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex(2))
                            .dot(1)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
<<<<<<< HEAD
                    'G',
                    lazy(
<<<<<<< HEAD
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .atLeast(
<<<<<<< HEAD
                                HatchElement.OutputHatch)
=======
                                GT_HatchElement.InputHatch)
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
=======
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(
                                GT_HatchElement.OutputHatch)
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
                            .adder(AntimatterForge::addFluidIO)
                            .casingIndex(x.textureIndex(2))
                            .dot(2)
                            .buildAndChain(x.getCasingBlock(2), x.getCasingMeta(2))))
                .addElement(
=======
>>>>>>> 97ed546ec8 (Add antimatter forge mechanics)
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
<<<<<<< HEAD
                        x -> HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(HatchElement.Energy)
=======
                        x -> GT_HatchElementBuilder.<AntimatterForge>builder()
                            .anyOf(GT_HatchElement.Energy.or(GT_HatchElement.ExoticEnergy))
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
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
<<<<<<< HEAD
        return new AntimatterForge(this.MAINNAME);
    }

    protected OverclockDescriber createOverclockDescriber() {
        return new FusionOverclockDescriber((byte) tier(), capableStartupCanonical());
=======
        return new AntimatterForge(MAIN_NAME);
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
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
        return STRUCTUREDEFINITION.get(getClass());
    }

    public int tier() {
        return 1;
    }

    @Override
    public long maxEUStore() {
        return 100000000;
    }

<<<<<<< HEAD
    /**
     * Unlike {@link #maxEUStore()}, this provides theoretical limit of startup EU, without considering the amount of
     * hatches nor the room for extra energy. Intended for simulation.
     */

    public long capableStartupCanonical() {
        return 160000000;
    }

<<<<<<< HEAD
=======
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
    public Block getCasingBlock() {
        return Loaders.antimatterContainmentCasing;
=======
    public Block getCasingBlock(int type) {
        switch (type) {
            case 1:
                return Loaders.magneticFluxCasing;
            case 2:
                return Loaders.gravityStabilizationCasing;
            default:
                return Loaders.magneticFluxCasing;
        }
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
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
<<<<<<< HEAD
        return checkPiece(MAINNAME, 23, 3, 40);
=======
        return checkPiece(MAIN_NAME, 26, 26, 4);
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
<<<<<<< HEAD
        buildPiece(MAINNAME, itemStack, b, 23, 3, 40);
=======
        buildPiece(MAIN_NAME, itemStack, b, 26, 26, 4);
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
<<<<<<< HEAD
        return survivialBuildPiece(MAINNAME, stackSize, 23, 3, 40, realBudget, env, false, true);
=======
        return survivialBuildPiece(MAIN_NAME, stackSize, 26, 26, 4, realBudget, env, false, true);
>>>>>>> aa1036fda0 (Add new structure check (It's broken))
    }

<<<<<<< HEAD
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            this.isLoadedChunk = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !this.isLoadedChunk) {
            // load a 3x3 area when machine is running
            GTChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
            int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + 1 + offX, getChunkZ() - 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() - 1 + offX, getChunkZ() - 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() + 1 + offZ));
            GTChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(getChunkX() + offX, getChunkZ() - 1 + offZ));
            this.isLoadedChunk = true;
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
                checkRecipe();
            }
            if (mUpdated) {
                mUpdate = 50;
                mUpdated = false;
            }
            if (--mUpdate == 0 || --mStartUpCheck == 0
                || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                if (mUpdate <= -1000) {
                    mUpdate = 5000;
                }
                checkStructure(true, aBaseMetaTileEntity);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    if (aBaseMetaTileEntity.getStoredEU() <= 0 && mMaxProgresstime > 0) {
                        criticalStopMachine();
                    }

                    long energyLimit = getSingleHatchPower();
                    List<MTEHatch> hatches = getExoticAndNormalEnergyHatchList();
                    for (MTEHatch hatch : filterValidMTEs(hatches)) {
                        long consumableEnergy = Math.min(hatch.getEUVar(), energyLimit);
                        long receivedEnergy = Math
                            .min(consumableEnergy, maxEUStore() - aBaseMetaTileEntity.getStoredEU());
                        if (receivedEnergy > 0) {
                            hatch.getBaseMetaTileEntity()
                                .decreaseStoredEnergyUnits(receivedEnergy, false);
                            aBaseMetaTileEntity.increaseStoredEnergyUnits(receivedEnergy, true);
                        }
                    }

                    if (mMaxProgresstime > 0) {
                        this.getBaseMetaTileEntity()
                            .decreaseStoredEnergyUnits(-lEUt, true);
                        if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                            if (mOutputItems != null)
                                for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
                            if (mOutputFluids != null)
                                for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
                            mEfficiency = Math
                                .max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1])));
                            mOutputItems = null;
                            mOutputFluids = null;
                            mProgresstime = 0;
                            mMaxProgresstime = 0;
                            mEfficiencyIncrease = 0;
                            para = 0;
                            if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe();
                        }
                    } else {
                        if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                            || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                            turnCasingActive(mMaxProgresstime > 0);
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe()) {
                                    if (aBaseMetaTileEntity.getStoredEU()
                                        < this.mLastRecipe.mSpecialValue + this.lEUt) {
                                        mMaxProgresstime = 0;
                                        turnCasingActive(false);
                                        criticalStopMachine();
                                    }
                                    getBaseMetaTileEntity()
                                        .decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue + this.lEUt, false);
                                }
                            }
                            if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                        }
                    }
                } else {
                    turnCasingActive(false);
                    this.mLastRecipe = null;
                    stopMachine();
                }
            }
            aBaseMetaTileEntity
                .setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        }
    }

    /**
     * @return The power one hatch can deliver to the reactor
     */
    protected long getSingleHatchPower() {
        return GTValues.V[tier()] * getMaxPara() * extraPara(100) / 32;
    }

<<<<<<< HEAD
=======
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
        }
        //if (this.eEnergyMulti != null) {
        //    for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
        //        hatch.updateTexture(status ? 52 : 53);
        //    }
        //}
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? 52 : 53);
            }
        }
        return true;
    }

=======
>>>>>>> 97ed546ec8 (Add antimatter forge mechanics)
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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    @Override
    protected ProcessingLogic createProcessingLogic() {

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                // When the fusion first loads and is still processing, it does the recipe check without consuming.
                return super.createParallelHelper(recipe).setConsumption(!mRunningOnLoad);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return overclockDescriber.createCalculator(super.createOverclockCalculator(recipe), recipe);
=======
        for (int i = 0; i < antimatterOutputHatches.size(); i++) {
            if (antimatterOutputHatches.get(i) == null || !antimatterOutputHatches.get(i).isValid() || antimatterOutputHatches.get(i).getFluid() == null) continue;
        }

<<<<<<< HEAD
            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!mRunningOnLoad) {
                    if (recipe.mSpecialValue > maxEUStore()) {
                        return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mSpecialValue);
                    }
                    if (recipe.mEUt > GTValues.V[tier()]) {
                        return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                    }
                }
                maxParallel = getMaxPara() * extraPara(recipe.mSpecialValue);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
<<<<<<< HEAD
=======
=======
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return CheckRecipeResultRegistry.insufficientPower(energyCost);
        }

        long protomatterCost = calculateProtoMatterCost(totalAntimatterAmount);
        long containedProtomatter = 0;
        List<FluidStack> inputFluids = getStoredFluids();
        for (int i = 0; i < inputFluids.size(); i++) {
            if (inputFluids.get(i).isFluidEqual(Materials.Antimatter.getFluid(1))) {
                containedProtomatter += Math.min(inputFluids.get(i).amount, protomatterCost - containedProtomatter);
                inputFluids.get(i).amount -= Math.min(protomatterCost - containedProtomatter, inputFluids.get(i).amount);
            }
        }

        distributeAntimatterToHatch(antimatterOutputHatches, totalAntimatterAmount, ((float) containedProtomatter)/((float) protomatterCost));
        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = speed;

>>>>>>> 579cb15fe5 (implement skeleton for antimatter production)
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }
>>>>>>> f158c75f7f (Implement the start of the processing of SSASS)

=======
        for (int i = 0; i < antimatterOutputHatches.size(); i++) {
            if (antimatterOutputHatches.get(i) == null || !antimatterOutputHatches.get(i).isValid() || antimatterOutputHatches.get(i).getFluid() == null) continue;
            FluidStack fluid = antimatterOutputHatches.get(i).getFluid().copy();
            antimatterOutputHatches.get(i).drain((int)((fluid.amount - minAntimatterAmount) / 2), true);
=======
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
            FluidStack fluid = amOutputHatches.get(i).getFluid().copy();
            amOutputHatches.get(i).drain((int)((fluid.amount - minAntimatterAmount) / 2), true);
>>>>>>> fa2399b90b (Add new structure for Antimatter Forge)
=======
        //Reduce the amount of antimatter in each hatch by half of the difference between the lowest amount and current hatch contents 
        for (int i = 0; i < amOutputHatches.size(); i++) {
            if (amOutputHatches.get(i) == null || !amOutputHatches.get(i).isValid() || amOutputHatches.get(i).getFluid() == null) continue;
            FluidStack fluid = amOutputHatches.get(i).getFluid().copy();
            amOutputHatches.get(i).drain((int)((fluid.amount - minAntimatterAmount) * 0.5), true);
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
=======
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
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
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

        System.out.println("\nCalculating antimatter cycle:");
        System.out.format("Antimatter found: %d\n", totalAntimatterAmount);
        System.out.format("Protomatted found: %d\n", containedProtomatter);

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

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
=======
    /* How much passive energy is drained every tick
    *  Base containment cost: 10M EU/t
    *  The containment cost ramps up by the amount of antimatter each tick, up to 1000 times
    *  If the current cost is more than 1000 times the amount of antimatter, or
    *  if no antimatter is in the hatches, the value will decay by 1% every tick
    */
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
=======
    /*
     * How much passive energy is drained every tick
     * Base containment cost: 10M EU/t
     * The containment cost ramps up by the amount of antimatter each tick, up to 1000 times
     * If the current cost is more than 1000 times the amount of antimatter, or
     * if no antimatter is in the hatches, the value will decay by 1% every tick
     */
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
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

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> dd4a3d69ac (implement skeleton for antimatter production)
=======
    //How much protomatter is required to do one operation
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
=======
    // How much protomatter is required to do one operation
>>>>>>> 3bd491c92e (Generator structure def do be crashing)
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
        System.out.format("Change this cycle: %d\n", difference);
        return difference;
    }

<<<<<<< HEAD
=======
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
=======
>>>>>>> dd4a3d69ac (implement skeleton for antimatter production)
    @Override
    protected boolean shouldCheckRecipeThisTick(long aTick) {
        return (aTick % speed) == 0;
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[tier()]);
        logic.setAvailableAmperage(getSingleHatchPower() * 32 / GTValues.V[tier()]);
=======
    public void clearHatches() {
        super.clearHatches();
<<<<<<< HEAD
        antimatterOutputHatches.clear();
>>>>>>> f158c75f7f (Implement the start of the processing of SSASS)
=======
    public void clearHatches() {
        super.clearHatches();
        antimatterOutputHatches.clear();
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
=======
        amOutputHatches.clear();
>>>>>>> fa2399b90b (Add new structure for Antimatter Forge)
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
<<<<<<< HEAD
        if (aMetaTileEntity instanceof MTEHatchEnergy tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add(tHatch);
        } else if (aMetaTileEntity instanceof MTEHatchEnergyMulti tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            //return eEnergyMulti.add(tHatch);
=======
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch hatch
            && GT_ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mExoticEnergyHatches.add(hatch);
>>>>>>> 6afb7f13f6 (Add protomatter, implement new structure and make it work)
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
<<<<<<< HEAD

        if (aMetaTileEntity instanceof MTEHatchOutput tOutput) {

=======
>>>>>>> d4f1bf606f (Implement the start of the processing of SSASS)
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
                + GTUtility.formatNumbers(this.para)
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.fusion.req") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + "EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(baseMetaTileEntity != null ? baseMetaTileEntity.getStoredEU() : 0)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEUStore())
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.fusion.plasma") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(plasmaOut)
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

        float size = (float) Math.log(antimatterAmount);
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
