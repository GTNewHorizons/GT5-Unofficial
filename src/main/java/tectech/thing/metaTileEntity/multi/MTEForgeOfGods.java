package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.formatNumbers;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.loader.recipe.Godforge.godforgeUpgradeMats;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;
import static tectech.thing.casing.TTCasingsContainer.forgeOfGodsRenderBlock;
import static tectech.util.GodforgeMath.allowModuleConnection;
import static tectech.util.GodforgeMath.calculateEnergyDiscountForModules;
import static tectech.util.GodforgeMath.calculateFuelConsumption;
import static tectech.util.GodforgeMath.calculateMaxFuelFactor;
import static tectech.util.GodforgeMath.calculateMaxHeatForModules;
import static tectech.util.GodforgeMath.calculateMaxParallelForModules;
import static tectech.util.GodforgeMath.calculateProcessingVoltageForModules;
import static tectech.util.GodforgeMath.calculateSpeedBonusForModules;
import static tectech.util.GodforgeMath.calculateStartupFuelConsumption;
import static tectech.util.GodforgeMath.queryMilestoneStats;
import static tectech.util.GodforgeMath.setMiscModuleParameters;
import static tectech.util.TTUtility.toExponentForm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.google.common.math.LongMath;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidNameHolderWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import tectech.TecTech;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.TileEntityForgeOfGods;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge_modules.MTESmeltingModule;
import tectech.util.CommonValues;

public class MTEForgeOfGods extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int fuelConsumptionFactor = 1;
    private int selectedFuelType = 0;
    private int internalBattery = 0;
    private int maxBatteryCharge = 100;
    private int gravitonShardsAvailable = 0;
    private int gravitonShardsSpent = 0;
    private int ringAmount = 1;
    private int stellarFuelAmount = 0;
    private int neededStartupFuel = 0;
    private long fuelConsumption = 0;
    private long totalRecipesProcessed = 0;
    private long totalFuelConsumed = 0;
    private float totalExtensionsBuilt = 0;
    private float powerMilestonePercentage = 0;
    private float recipeMilestonePercentage = 0;
    private float fuelMilestonePercentage = 0;
    private float structureMilestonePercentage = 0;
    private float invertedPowerMilestonePercentage = 0;
    private float invertedRecipeMilestonePercentage = 0;
    private float invertedFuelMilestonePercentage = 0;
    private float invertedStructureMilestonePercentage = 0;
    private BigInteger totalPowerConsumed = BigInteger.ZERO;
    private boolean batteryCharging = false;
    private boolean inversion = false;
    private boolean gravitonShardEjection = false;
    private boolean noFormatting = false;
    private boolean isRenderActive = false;
    public ArrayList<MTEBaseModule> moduleHatches = new ArrayList<>();
    protected ItemStackHandler inputSlotHandler = new ItemStackHandler(16);

    private static final int FUEL_CONFIG_WINDOW_ID = 9;
    private static final int UPGRADE_TREE_WINDOW_ID = 10;
    private static final int INDIVIDUAL_UPGRADE_WINDOW_ID = 11;
    private static final int BATTERY_CONFIG_WINDOW_ID = 12;
    private static final int MILESTONE_WINDOW_ID = 13;
    private static final int INDIVIDUAL_MILESTONE_WINDOW_ID = 14;
    private static final int MANUAL_INSERTION_WINDOW_ID = 15;
    private static final int GENERAL_INFO_WINDOW_ID = 16;
    private static final int TEXTURE_INDEX = 960;
    private static final int[] FIRST_SPLIT_UPGRADES = new int[] { 12, 13, 14 };
    private static final Integer[] UPGRADE_MATERIAL_ID_CONVERSION = { 0, 5, 7, 11, 26, 29, 30 };
    private static final long POWER_MILESTONE_CONSTANT = LongMath.pow(10, 15);
    private static final long RECIPE_MILESTONE_CONSTANT = LongMath.pow(10, 7);
    private static final long FUEL_MILESTONE_CONSTANT = 10_000;
    private static final long RECIPE_MILESTONE_T7_CONSTANT = RECIPE_MILESTONE_CONSTANT * LongMath.pow(6, 6);
    private static final long FUEL_MILESTONE_T7_CONSTANT = FUEL_MILESTONE_CONSTANT * LongMath.pow(3, 6);
    private static final BigInteger POWER_MILESTONE_T7_CONSTANT = BigInteger.valueOf(POWER_MILESTONE_CONSTANT)
        .multiply(BigInteger.valueOf(LongMath.pow(9, 6)));
    private static final double POWER_LOG_CONSTANT = Math.log(9);
    private static final double RECIPE_LOG_CONSTANT = Math.log(6);
    private static final double FUEL_LOG_CONSTANT = Math.log(3);
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final String STRUCTURE_PIECE_SHAFT = "beam_shaft";
    protected static final String STRUCTURE_PIECE_FIRST_RING = "first_ring";
    protected static final String STRUCTURE_PIECE_FIRST_RING_AIR = "first_ring_air";
    protected static final String STRUCTURE_PIECE_SECOND_RING = "second_ring";
    protected static final String STRUCTURE_PIECE_SECOND_RING_AIR = "second_ring_air";
    protected static final String STRUCTURE_PIECE_THIRD_RING = "third_ring";
    protected static final String STRUCTURE_PIECE_THIRD_RING_AIR = "third_ring_air";
    private static final String SCANNER_INFO_BAR = EnumChatFormatting.BLUE
        + "--------------------------------------------";
    private static final String TOOLTIP_BAR = EnumChatFormatting.AQUA
        + "--------------------------------------------------------------------------";
    private static final ItemStack STELLAR_FUEL = Avaritia.isModLoaded() ? getModItem(Avaritia.ID, "Resource", 1, 8)
        : GTOreDictUnificator.get(OrePrefixes.block, Materials.CosmicNeutronium, 1);

    private final boolean debugMode = false;

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 1000 ? elementBudget : Math.min(1000, elementBudget * 5);
        // 1000 blocks max per placement.
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 63, 14, 1, realBudget, env, false, true);
        if (stackSize.stackSize > 1) {
            built += survivialBuildPiece(
                STRUCTURE_PIECE_SECOND_RING,
                stackSize,
                55,
                11,
                -67,
                realBudget,
                env,
                false,
                true);
        }
        if (stackSize.stackSize > 2) {
            built += survivialBuildPiece(
                STRUCTURE_PIECE_THIRD_RING,
                stackSize,
                47,
                13,
                -76,
                realBudget,
                env,
                false,
                true);
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEForgeOfGods> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public static final IStructureDefinition<MTEForgeOfGods> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEForgeOfGods>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ForgeOfGodsStructureString.MAIN_STRUCTURE)
        .addShape(STRUCTURE_PIECE_SHAFT, ForgeOfGodsStructureString.BEAM_SHAFT)
        .addShape(STRUCTURE_PIECE_FIRST_RING, ForgeOfGodsStructureString.FIRST_RING)
        .addShape(STRUCTURE_PIECE_FIRST_RING_AIR, ForgeOfGodsStructureString.FIRST_RING_AIR)
        .addShape(STRUCTURE_PIECE_SECOND_RING, ForgeOfGodsRingsStructureString.SECOND_RING)
        .addShape(STRUCTURE_PIECE_SECOND_RING_AIR, ForgeOfGodsRingsStructureString.SECOND_RING_AIR)
        .addShape(STRUCTURE_PIECE_THIRD_RING, ForgeOfGodsRingsStructureString.THIRD_RING)
        .addShape(STRUCTURE_PIECE_THIRD_RING_AIR, ForgeOfGodsRingsStructureString.THIRD_RING_AIR)
        .addElement('A', classicHatches(TEXTURE_INDEX + 3, 1, GodforgeCasings, 3))
        .addElement('B', ofBlock(GodforgeCasings, 0))
        .addElement('C', ofBlock(GodforgeCasings, 1))
        .addElement('D', ofBlock(GodforgeCasings, 2))
        .addElement('E', ofBlock(GodforgeCasings, 3))
        .addElement('F', ofBlock(GodforgeCasings, 4))
        .addElement('G', ofBlock(GodforgeCasings, 5))
        .addElement('H', ofBlock(BlockGodforgeGlass.INSTANCE, 0))
        .addElement('I', ofBlock(GodforgeCasings, 7))
        .addElement(
            'J',
            HatchElementBuilder.<MTEForgeOfGods>builder()
                .atLeast(moduleElement.Module)
                .casingIndex(TEXTURE_INDEX)
                .dot(3)
                .buildAndChain(GodforgeCasings, 0))
        .addElement('K', ofBlock(GodforgeCasings, 6))
        .addElement('L', ofBlock(Blocks.air, 0))
        .build();

    public MTEForgeOfGods(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEForgeOfGods(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEForgeOfGods(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/GODFORGE_CONTROLLER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX + 1),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX + 1) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 63, 14, 1, stackSize, hintsOnly);
        if (stackSize.stackSize > 1) {
            buildPiece(STRUCTURE_PIECE_SECOND_RING, stackSize, hintsOnly, 55, 11, -67);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(STRUCTURE_PIECE_THIRD_RING, stackSize, hintsOnly, 47, 13, -76);
        }
    }

    private final ArrayList<FluidStack> validFuelList = new ArrayList<>() {

        {
            add(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1));
            add(MaterialsUEVplus.RawStarMatter.getFluid(1));
            add(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1));
        }
    };

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {

        moduleHatches.clear();

        // Check structure of multi
        if (isRenderActive) {
            if (!structureCheck_EM(STRUCTURE_PIECE_SHAFT, 63, 14, 1)
                || !structureCheck_EM(STRUCTURE_PIECE_FIRST_RING_AIR, 63, 14, -59)) {
                destroyRenderer();
                return false;
            }
        } else if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 63, 14, 1)) {
            return false;
        }

        if (internalBattery != 0 && !isRenderActive) {
            createRenderer();
        }

        // Check there is 1 input bus
        if (mInputBusses.size() != 1) {
            return false;
        }

        // Check there is 1 me output bus
        {
            if (mOutputBusses.size() != 1) {
                return false;
            }

            if (!(mOutputBusses.get(0) instanceof MTEHatchOutputBusME)) {
                return false;
            }
        }

        // Make sure there are no energy hatches
        {
            if (mEnergyHatches.size() > 0) {
                return false;
            }

            if (mExoticEnergyHatches.size() > 0) {
                return false;
            }
        }

        // Make sure there is 1 input hatch
        if (mInputHatches.size() != 1) {
            return false;
        }

        if (isUpgradeActive(26)) {
            if (checkPiece(STRUCTURE_PIECE_SECOND_RING, 55, 11, -67)) {
                ringAmount = 2;
            }
            if (isRenderActive && ringAmount >= 2 && !checkPiece(STRUCTURE_PIECE_SECOND_RING_AIR, 55, 11, -67)) {
                destroyRenderer();
            }
        }

        if (isUpgradeActive(29)) {
            if (checkPiece(STRUCTURE_PIECE_THIRD_RING, 47, 13, -76)) {
                ringAmount = 3;
            }
            if (isRenderActive && ringAmount == 3 && !checkPiece(STRUCTURE_PIECE_THIRD_RING_AIR, 47, 13, -76)) {
                destroyRenderer();
            }
        }

        return true;
    }

    int ticker = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            ticker++;
            // Check and drain fuel
            if (ticker % (5 * SECONDS) == 0) {
                ticker = 0;
                startRecipeProcessing();
                FluidStack[] fluidInHatch = null;
                boolean fuelDrained = false;
                if (mInputHatches != null && mInputHatches.size() != 0) {
                    fluidInHatch = this.getStoredFluids()
                        .toArray(new FluidStack[0]);
                }
                int maxModuleCount = 8;

                if (upgrades[26]) {
                    maxModuleCount += 4;
                }
                if (upgrades[29]) {
                    maxModuleCount += 4;
                }
                if (mInputBusses.size() != 0) {
                    if (internalBattery == 0) {
                        MTEHatchInputBus inputBus = mInputBusses.get(0);
                        ItemStack[] inputBusInventory = inputBus.getRealInventory();
                        if (inputBusInventory != null) {
                            for (int i = 0; i < inputBusInventory.length; i++) {
                                ItemStack itemStack = inputBusInventory[i];
                                if (itemStack != null && itemStack.isItemEqual(STELLAR_FUEL)) {
                                    int stacksize = itemStack.stackSize;
                                    if (inputBus instanceof MTEHatchInputBusME meBus) {
                                        ItemStack realItem = meBus.getRealInventory()[i + 16];
                                        if (realItem == null) {
                                            break;
                                        }
                                        stacksize = realItem.stackSize;
                                    }
                                    inputBus.decrStackSize(i, stacksize);
                                    stellarFuelAmount += stacksize;
                                    inputBus.updateSlots();
                                }
                            }
                        }
                        neededStartupFuel = calculateStartupFuelConsumption(this);
                        if (stellarFuelAmount >= neededStartupFuel) {
                            stellarFuelAmount -= neededStartupFuel;
                            increaseBattery(neededStartupFuel);
                            createRenderer();
                        }
                    } else {
                        fuelConsumption = (long) calculateFuelConsumption(this) * 5 * (batteryCharging ? 2 : 1);
                        if (fluidInHatch != null && fuelConsumption < Integer.MAX_VALUE) {
                            for (FluidStack fluid : fluidInHatch) {
                                if (fluid.isFluidEqual(validFuelList.get(selectedFuelType))) {
                                    FluidStack fluidNeeded = new FluidStack(
                                        validFuelList.get(selectedFuelType),
                                        (int) fuelConsumption);
                                    FluidStack fluidReal;
                                    if (mInputHatches.get(0) instanceof MTEHatchInputME meHatch) {
                                        fluidReal = meHatch.drain(ForgeDirection.UNKNOWN, fluidNeeded, true);
                                    } else {
                                        fluidReal = mInputHatches.get(0)
                                            .drain(fluidNeeded.amount, true);
                                    }
                                    if (fluidReal == null || fluidReal.amount < fluidNeeded.amount) {
                                        reduceBattery(fuelConsumptionFactor);
                                    } else {
                                        totalFuelConsumed += getFuelFactor();
                                        if (batteryCharging) {
                                            increaseBattery(fuelConsumptionFactor);
                                        }
                                    }
                                    fuelDrained = true;
                                }
                            }
                            if (!fuelDrained) {
                                reduceBattery(fuelConsumptionFactor);
                            }
                        } else {
                            reduceBattery(fuelConsumptionFactor);
                        }
                    }
                }

                determineCompositionMilestoneLevel();
                checkInversionStatus();
                determineMilestoneProgress();
                if (!debugMode) {
                    determineGravitonShardAmount();
                }
                if (upgrades[30] && gravitonShardEjection) {
                    ejectGravitonShards();
                }

                // Do module calculations and checks
                if (moduleHatches.size() > 0 && internalBattery > 0 && moduleHatches.size() <= maxModuleCount) {
                    for (MTEBaseModule module : moduleHatches) {
                        if (allowModuleConnection(module, this)) {
                            module.connect();
                            calculateMaxHeatForModules(module, this);
                            calculateSpeedBonusForModules(module, this);
                            calculateMaxParallelForModules(module, this);
                            calculateEnergyDiscountForModules(module, this);
                            setMiscModuleParameters(module, this);
                            queryMilestoneStats(module, this);
                            if (!upgrades[28]) {
                                calculateProcessingVoltageForModules(module, this);
                            }
                        } else {
                            module.disconnect();
                        }
                    }
                } else if (moduleHatches.size() > maxModuleCount) {
                    for (MTEBaseModule module : moduleHatches) {
                        module.disconnect();
                    }
                }
                if (mEfficiency < 0) mEfficiency = 0;
                endRecipeProcessing();
            }
        }
    }

    public boolean addModuleToMachineList(IGregTechTileEntity tileEntity, int baseCasingIndex) {
        if (tileEntity == null) {
            return false;
        }
        IMetaTileEntity metaTileEntity = tileEntity.getMetaTileEntity();
        if (metaTileEntity == null) {
            return false;
        }
        if (metaTileEntity instanceof MTEBaseModule) {
            return moduleHatches.add((MTEBaseModule) metaTileEntity);
        }
        return false;
    }

    public enum moduleElement implements IHatchElement<MTEForgeOfGods> {

        Module(MTEForgeOfGods::addModuleToMachineList, MTEBaseModule.class) {

            @Override
            public long count(MTEForgeOfGods tileEntity) {
                return tileEntity.moduleHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEForgeOfGods> adder;

        @SafeVarargs
        moduleElement(IGTHatchAdder<MTEForgeOfGods> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEForgeOfGods> adder() {
            return adder;
        }
    }

    private void createRenderer() {

        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), forgeOfGodsRenderBlock);
        TileEntityForgeOfGods rendererTileEntity = (TileEntityForgeOfGods) this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));

        rendererTileEntity.setRenderSize(20);
        rendererTileEntity.setRenderRotationSpeed(5);

        switch (ringAmount) {
            case 2 -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING_AIR, null, false, 63, 14, -59);
                buildPiece(STRUCTURE_PIECE_SECOND_RING_AIR, null, false, 55, 11, -67);
            }
            case 3 -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING_AIR, null, false, 63, 14, -59);
                buildPiece(STRUCTURE_PIECE_SECOND_RING_AIR, null, false, 55, 11, -67);
                buildPiece(STRUCTURE_PIECE_THIRD_RING_AIR, null, false, 47, 13, -76);
            }
            default -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING_AIR, null, false, 63, 14, -59);
            }
        }

        isRenderActive = true;
    }

    private void destroyRenderer() {

        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);

        switch (ringAmount) {
            case 2 -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING, null, false, 63, 14, -59);
                buildPiece(STRUCTURE_PIECE_SECOND_RING, null, false, 55, 11, -67);
            }
            case 3 -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING, null, false, 63, 14, -59);
                buildPiece(STRUCTURE_PIECE_SECOND_RING, null, false, 55, 11, -67);
                buildPiece(STRUCTURE_PIECE_THIRD_RING, null, false, 47, 13, -76);
            }
            default -> {
                buildPiece(STRUCTURE_PIECE_FIRST_RING, null, false, 63, 14, -59);
            }
        }

        isRenderActive = false;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        destroyRenderer();
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(SCANNER_INFO_BAR);
        str.add("Number of Rings: " + EnumChatFormatting.GOLD + ringAmount);
        str.add("Total Upgrades Unlocked: " + EnumChatFormatting.GOLD + getTotalActiveUpgrades());
        str.add("Connected Modules: " + EnumChatFormatting.GOLD + moduleHatches.size());
        str.add(SCANNER_INFO_BAR);
        return str.toArray(new String[0]);
    }

    @Override
    public void onRemoval() {
        if (moduleHatches != null && moduleHatches.size() > 0) {
            for (MTEBaseModule module : moduleHatches) {
                module.disconnect();
            }
        }
        super.onRemoval();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (doesBindPlayerInventory()) {
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                    .setPos(4, 4)
                    .setSize(190, 85));
        } else {
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE_NO_INVENTORY)
                    .setPos(4, 4)
                    .setSize(190, 171));
        }
        buildContext.addSyncedWindow(UPGRADE_TREE_WINDOW_ID, this::createUpgradeTreeWindow);
        buildContext.addSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID, this::createIndividualUpgradeWindow);
        buildContext.addSyncedWindow(FUEL_CONFIG_WINDOW_ID, this::createFuelConfigWindow);
        buildContext.addSyncedWindow(BATTERY_CONFIG_WINDOW_ID, this::createBatteryWindow);
        buildContext.addSyncedWindow(MILESTONE_WINDOW_ID, this::createMilestoneWindow);
        buildContext.addSyncedWindow(INDIVIDUAL_MILESTONE_WINDOW_ID, this::createIndividualMilestoneWindow);
        buildContext.addSyncedWindow(MANUAL_INSERTION_WINDOW_ID, this::createManualInsertionWindow);
        buildContext.addSyncedWindow(GENERAL_INFO_WINDOW_ID, this::createGeneralInfoWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(UPGRADE_TREE_WINDOW_ID);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> button = new ArrayList<>();
                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    button.add(TecTechUITextures.OVERLAY_BUTTON_ARROW_BLUE_UP);
                    return button.toArray(new IDrawable[0]);
                })
                .addTooltip("Path of Celestial Transcendence")
                .setPos(174, 167)
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK_SMALL)
                    .setPos(174, 183)
                    .setSize(16, 6))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(FUEL_CONFIG_WINDOW_ID);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> button = new ArrayList<>();
                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    button.add(TecTechUITextures.OVERLAY_BUTTON_HEAT_ON);
                    return button.toArray(new IDrawable[0]);
                })
                .addTooltip(translateToLocal("fog.button.fuelconfig.tooltip"))
                .setPos(174, 110)
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                TextWidget.dynamicText(this::storedFuel)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setPos(6, 8)
                    .setSize(74, 34))
            .widget(createPowerSwitchButton())
            .widget(createBatteryButton(builder))
            .widget(createEjectionSwitch(builder))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> getBaseMetaTileEntity().isAllowedToWork(), val -> {
                if (val) {
                    getBaseMetaTileEntity().enableWorking();
                } else {
                    getBaseMetaTileEntity().disableWorking();
                }
            }))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    checkMachine_EM(this.getBaseMetaTileEntity(), null);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> button = new ArrayList<>();
                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    button.add(TecTechUITextures.OVERLAY_CYCLIC_BLUE);
                    return button.toArray(new IDrawable[0]);
                })
                .addTooltip(translateToLocal("fog.button.structurecheck.tooltip"))
                .setPos(8, 91)
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(MILESTONE_WINDOW_ID);
                }
            })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> button = new ArrayList<>();
                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    button.add(TecTechUITextures.OVERLAY_BUTTON_FLAG);
                    return button.toArray(new IDrawable[0]);

                })
                .addTooltip(translateToLocal("fog.button.milestones.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(174, 91))
            .widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(GENERAL_INFO_WINDOW_ID);
                    })
                    .setSize(18, 18)
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.clickhere"))
                    .setPos(172, 67)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(172, 67));
    }

    @Override
    protected ButtonWidget createPowerSwitchButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (getBaseMetaTileEntity().isAllowedToWork()) {
                getBaseMetaTileEntity().disableWorking();
            } else {
                getBaseMetaTileEntity().enableWorking();
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (getBaseMetaTileEntity().isAllowedToWork()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 148 : 172)
            .setSize(16, 16);
        button.addTooltip("Power Switch")
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    protected ButtonWidget createEjectionSwitch(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (upgrades[30]) {
                gravitonShardEjection = !gravitonShardEjection;
            }
        })
            .setPlayClickSound(upgrades[30])
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (!upgrades[30]) {
                    return ret.toArray(new IDrawable[0]);
                }
                if (gravitonShardEjection) {
                    ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    ret.add(TecTechUITextures.OVERLAY_EJECTION_ON);
                } else {
                    ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    ret.add(TecTechUITextures.OVERLAY_EJECTION_LOCKED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> gravitonShardEjection, val -> gravitonShardEjection = val),
                builder)
            .setPos(26, 91)
            .setSize(16, 16)
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(() -> upgrades[30], val -> upgrades[30] = val), builder);
        if (upgrades[30]) {
            button.addTooltip(translateToLocal("fog.button.ejection.tooltip"));
            button.setTooltipShowUpDelay(TOOLTIP_DELAY);
        }
        return (ButtonWidget) button;
    }

    protected Widget createBatteryButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (clickData.mouseButton == 0) {
                batteryCharging = !batteryCharging;
            } else if (clickData.mouseButton == 1 && !widget.isClient() && upgrades[8]) {
                widget.getContext()
                    .openSyncedWindow(BATTERY_CONFIG_WINDOW_ID);
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                if (batteryCharging) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_BATTERY_ON);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_BATTERY_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, 129)
            .setSize(16, 16);
        button.addTooltip(translateToLocal("fog.button.battery.tooltip.01"))
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("fog.button.battery.tooltip.02"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> batteryCharging, val -> batteryCharging = val),
                builder);
        return button;
    }

    protected ModularWindow createBatteryWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 52;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.BottomRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)
                        .subtract(0, 10)));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.batteryinfo")
                .setPos(3, 4)
                .setSize(74, 20))
            .widget(
                new NumericWidget().setSetter(val -> maxBatteryCharge = (int) val)
                    .setGetter(() -> maxBatteryCharge)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setDefaultValue(100)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(4, 25)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
        return builder.build();
    }

    protected ModularWindow createFuelConfigWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 130;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelconsumption")
                .setPos(3, 2)
                .setSize(74, 34))
            .widget(
                new NumericWidget().setSetter(val -> fuelConsumptionFactor = (int) val)
                    .setGetter(() -> fuelConsumptionFactor)
                    .setBounds(1, calculateMaxFuelFactor(this))
                    .setDefaultValue(1)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(4, 35)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD))
            .widget(
                new DrawableWidget().setDrawable(ModularUITextures.ICON_INFO)
                    .setPos(64, 24)
                    .setSize(10, 10)
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.0"))
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.1"))
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.2"))
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.3"))
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.4"))
                    .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.5"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                TextWidget.localised("gt.blockmachines.multimachine.FOG.fueltype")
                    .setPos(3, 57)
                    .setSize(74, 24))
            .widget(
                TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelusage")
                    .setPos(3, 100)
                    .setSize(74, 20))
            .widget(
                TextWidget.dynamicText(this::fuelUsage)
                    .setPos(3, 115)
                    .setSize(74, 15))
            .widget(
                new MultiChildWidget().addChild(
                    new FluidNameHolderWidget(
                        () -> MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1)
                            .getUnlocalizedName()
                            .substring(6),
                        (String) -> MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1)
                            .getUnlocalizedName()) {

                        @Override
                        public void buildTooltip(List<Text> tooltip) {
                            FluidStack fluid = createFluidStack();
                            addFluidNameInfo(tooltip, fluid);
                            addAdditionalFluidInfo(tooltip, fluid);
                        }
                    }.setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(1, 1)
                        .setSize(16, 16))
                    .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                        TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                        selectedFuelType = 0;
                    })
                        .setBackground(() -> {
                            if (selectedFuelType == 0) {
                                return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                            } else {
                                return new IDrawable[] {};
                            }
                        })
                        .setSize(18, 18)
                        .attachSyncer(new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType), builder))

                    .setPos(6, 82)
                    .setSize(18, 18))
            .widget(
                new MultiChildWidget().addChild(
                    new FluidNameHolderWidget(
                        () -> MaterialsUEVplus.RawStarMatter.getFluid(1)
                            .getUnlocalizedName()
                            .substring(6),
                        (String) -> MaterialsUEVplus.RawStarMatter.getFluid(1)
                            .getUnlocalizedName()) {

                        @Override
                        public void buildTooltip(List<Text> tooltip) {
                            FluidStack fluid = createFluidStack();
                            addFluidNameInfo(tooltip, fluid);
                            addAdditionalFluidInfo(tooltip, fluid);
                        }
                    }.setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(1, 1)
                        .setSize(16, 16))
                    .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                        TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                        selectedFuelType = 1;
                    })
                        .setBackground(() -> {
                            if (selectedFuelType == 1) {
                                return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                            } else {
                                return new IDrawable[] {};
                            }
                        })
                        .setSize(18, 18))
                    .setPos(29, 82)
                    .setSize(18, 18)
                    .attachSyncer(new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType), builder))
            .widget(
                new MultiChildWidget().addChild(
                    new FluidNameHolderWidget(
                        () -> MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1)
                            .getUnlocalizedName()
                            .substring(6),
                        (String) -> MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1)
                            .getUnlocalizedName()) {

                        @Override
                        public void buildTooltip(List<Text> tooltip) {
                            FluidStack fluid = createFluidStack();
                            addFluidNameInfo(tooltip, fluid);
                            addAdditionalFluidInfo(tooltip, fluid);
                        }
                    }.setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(1, 1)
                        .setSize(16, 16))
                    .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                        TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                        selectedFuelType = 2;
                    })
                        .setBackground(() -> {
                            if (selectedFuelType == 2) {
                                return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                            } else {
                                return new IDrawable[] {};
                            }
                        })
                        .setSize(18, 18))
                    .setPos(52, 82)
                    .setSize(18, 18)
                    .attachSyncer(new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType), builder));

        return builder.build();
    }

    private final int[] milestoneProgress = new int[] { 0, 0, 0, 0 };

    protected ModularWindow createMilestoneWindow(final EntityPlayer player) {
        final int WIDTH = 400;
        final int HEIGHT = 300;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(TecTechUITextures.BACKGROUND_SPACE);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.widget(createMilestoneButton(0, 80, 100, new Pos2d(62, 24)));
        builder.widget(createMilestoneButton(1, 70, 98, new Pos2d(263, 25)));
        builder.widget(createMilestoneButton(2, 100, 100, new Pos2d(52, 169)));
        builder.widget(createMilestoneButton(3, 100, 100, new Pos2d(248, 169)));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.powermilestone")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setPos(77, 45)
                .setSize(50, 30));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.recipemilestone")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setPos(268, 45)
                .setSize(60, 30));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelmilestone")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setPos(77, 190)
                .setSize(50, 30));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.purchasablemilestone")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setPos(268, 190)
                .setSize(60, 30));
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND)
                .setPos(37, 70)
                .setSize(130, 7))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND)
                    .setPos(233, 70)
                    .setSize(130, 7))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND)
                    .setPos(37, 215)
                    .setSize(130, 7))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BACKGROUND)
                    .setPos(233, 215)
                    .setSize(130, 7));
        builder.widget(
            new ProgressBar().setProgress(() -> powerMilestonePercentage)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_RED, 130)
                .setSynced(true, false)
                .setSize(130, 7)
                .setPos(37, 70))
            .widget(
                new ProgressBar().setProgress(() -> recipeMilestonePercentage)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(233, 70))
            .widget(
                new ProgressBar().setProgress(() -> fuelMilestonePercentage)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(37, 215))
            .widget(
                new ProgressBar().setProgress(() -> structureMilestonePercentage)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(233, 215))
            .widget(
                new ProgressBar().setProgress(() -> invertedPowerMilestonePercentage)
                    .setDirection(ProgressBar.Direction.LEFT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_RED_INVERTED, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(37, 70))
            .widget(
                new ProgressBar().setProgress(() -> invertedRecipeMilestonePercentage)
                    .setDirection(ProgressBar.Direction.LEFT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE_INVERTED, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(233, 70))
            .widget(
                new ProgressBar().setProgress(() -> invertedFuelMilestonePercentage)
                    .setDirection(ProgressBar.Direction.LEFT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE_INVERTED, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(37, 215))
            .widget(
                new ProgressBar().setProgress(() -> invertedStructureMilestonePercentage)
                    .setDirection(ProgressBar.Direction.LEFT)
                    .setTexture(TecTechUITextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW_INVERTED, 130)
                    .setSynced(true, false)
                    .setSize(130, 7)
                    .setPos(233, 215))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(382, 6));
        return builder.build();
    }

    protected ModularWindow createIndividualMilestoneWindow(final EntityPlayer player) {
        final int WIDTH = 150;
        final int HEIGHT = 150;
        int symbol_width;
        int symbol_height;
        String milestoneType;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        UITexture symbol;
        switch (currentMilestoneID) {
            case 1 -> {
                symbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CONVERSION;
                symbol_width = 54;
                symbol_height = 75;
                milestoneType = "recipe";
            }
            case 2 -> {
                symbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CATALYST;
                symbol_width = 75;
                symbol_height = 75;
                milestoneType = "fuel";
            }
            case 3 -> {
                symbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION;
                symbol_width = 75;
                symbol_height = 75;
                milestoneType = "purchasable";
            }
            default -> {
                symbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CHARGE;
                symbol_width = 60;
                symbol_height = 75;
                milestoneType = "power";
            }
        }

        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE);
        builder.setDraggable(true);
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(134, 4))
            .widget(
                new DrawableWidget().setDrawable(symbol)
                    .setSize(symbol_width, symbol_height)
                    .setPos((WIDTH - symbol_width) / 2, (HEIGHT - symbol_height) / 2))
            .widget(
                TextWidget.localised("gt.blockmachines.multimachine.FOG." + milestoneType + "milestone")
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setPos(0, 8)
                    .setSize(150, 15))
            .widget(
                TextWidget.dynamicText(this::inversionStatusText)
                    .setDefaultColor(EnumChatFormatting.AQUA)
                    .setTextAlignment(Alignment.Center)
                    .setScale(0.8f)
                    .setPos(0, 120)
                    .setSize(150, 15))
            .widget(
                TextWidget.dynamicText(() -> totalMilestoneProgress(currentMilestoneID))
                    .setScale(0.7f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.Center)
                    .setPos(5, 30)
                    .setSize(140, 30))
            .widget(
                TextWidget.dynamicText(() -> currentMilestone(currentMilestoneID))
                    .setScale(0.7f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.Center)
                    .setPos(5, 50)
                    .setSize(140, 30))
            .widget(
                TextWidget.dynamicText(() -> milestoneProgressText(currentMilestoneID, true))
                    .setScale(0.7f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setSize(140, 30)
                    .setPos(5, 70))
            .widget(
                TextWidget.dynamicText(() -> gravitonShardAmountText(currentMilestoneID))
                    .setScale(0.7f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setSize(140, 30)
                    .setPos(5, 90))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                if (clickData.mouseButton == 0) {
                    noFormatting = !noFormatting;
                }
            })
                .setSize(10, 10)
                .addTooltip(translateToLocal("fog.button.formatting.tooltip"))
                .setBackground(TecTechUITextures.OVERLAY_CYCLIC_BLUE)
                .setPos(5, 135)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .attachSyncer(
                    new FakeSyncWidget.BooleanSyncer(() -> noFormatting, val -> noFormatting = val),
                    builder));

        return builder.build();
    }

    private int currentMilestoneID = 0;

    private Widget createMilestoneButton(int milestoneID, int width, int height, Pos2d pos) {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            currentMilestoneID = milestoneID;
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(INDIVIDUAL_MILESTONE_WINDOW_ID);
            }
        })
            .setSize(width, height)
            .setBackground(() -> switch (milestoneID) {
            case 1 -> new IDrawable[] { TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CONVERSION_GLOW };
            case 2 -> new IDrawable[] { TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CATALYST_GLOW };
            case 3 -> new IDrawable[] { TecTechUITextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION_GLOW };
            default -> new IDrawable[] { TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW };
            })
            .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.milestoneinfo"))
            .setPos(pos)
            .setTooltipShowUpDelay(TOOLTIP_DELAY);
    }

    private int currentUpgradeID = 0;
    private int currentColorCode = 0;
    private int currentMilestoneBG = 0;
    private int gravitonShardCost = 0;
    private int[] prereqUpgrades = new int[] {};
    private int[] followupUpgrades = new int[] {};
    private boolean allPrereqRequired = false;
    private boolean isUpradeSplitStart = false;
    private boolean doesCurrentUpgradeRequireExtraMats = false;
    private boolean[] upgrades = new boolean[31];
    private boolean[] materialPaidUpgrades = new boolean[7];

    protected ModularWindow createUpgradeTreeWindow(final EntityPlayer player) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        final int PARENT_WIDTH = 300;
        final int PARENT_HEIGHT = 300;
        ModularWindow.Builder builder = ModularWindow.builder(PARENT_WIDTH, PARENT_HEIGHT);
        scrollable
            .widget(
                createUpgradeBox(
                    0,
                    0,
                    3,
                    new int[] {},
                    false,
                    new int[] { 1 },
                    false,
                    true,
                    0,
                    new Pos2d(126, 56),
                    scrollable))
            .widget(
                createUpgradeBox(
                    1,
                    0,
                    1,
                    new int[] { 0 },
                    false,
                    new int[] { 2, 3 },
                    false,
                    false,
                    1,
                    new Pos2d(126, 116),
                    scrollable))
            .widget(
                createUpgradeBox(
                    2,
                    0,
                    2,
                    new int[] { 1 },
                    false,
                    new int[] { 4, 5 },
                    false,
                    false,
                    1,
                    new Pos2d(96, 176),
                    scrollable))
            .widget(
                createUpgradeBox(
                    3,
                    0,
                    2,
                    new int[] { 1 },
                    false,
                    new int[] { 5, 6 },
                    false,
                    false,
                    1,
                    new Pos2d(156, 176),
                    scrollable))
            .widget(
                createUpgradeBox(
                    4,
                    0,
                    0,
                    new int[] { 2 },
                    false,
                    new int[] { 8 },
                    false,
                    false,
                    1,
                    new Pos2d(66, 236),
                    scrollable))
            .widget(
                createUpgradeBox(
                    5,
                    0,
                    3,
                    new int[] { 2, 3 },
                    false,
                    new int[] { 7 },
                    false,
                    true,
                    1,
                    new Pos2d(126, 236),
                    scrollable))
            .widget(
                createUpgradeBox(
                    6,
                    0,
                    1,
                    new int[] { 3 },
                    false,
                    new int[] { 10 },
                    false,
                    false,
                    1,
                    new Pos2d(186, 236),
                    scrollable))
            .widget(
                createUpgradeBox(
                    7,
                    0,
                    3,
                    new int[] { 5 },
                    false,
                    new int[] { 8, 9, 10 },
                    false,
                    true,
                    2,
                    new Pos2d(126, 296),
                    scrollable))
            .widget(
                createUpgradeBox(
                    8,
                    0,
                    0,
                    new int[] { 4, 7 },
                    true,
                    new int[] { 11 },
                    false,
                    false,
                    2,
                    new Pos2d(56, 356),
                    scrollable))
            .widget(
                createUpgradeBox(
                    9,
                    0,
                    2,
                    new int[] { 7 },
                    false,
                    new int[] {},
                    false,
                    false,
                    2,
                    new Pos2d(126, 356),
                    scrollable))
            .widget(
                createUpgradeBox(
                    10,
                    0,
                    1,
                    new int[] { 6, 7 },
                    true,
                    new int[] { 11 },
                    false,
                    false,
                    2,
                    new Pos2d(196, 356),
                    scrollable))
            .widget(
                createUpgradeBox(
                    11,
                    0,
                    3,
                    new int[] { 8, 10 },
                    false,
                    new int[] { 12, 13, 14 },
                    false,
                    true,
                    2,
                    new Pos2d(126, 416),
                    scrollable))
            .widget(
                createUpgradeBox(
                    12,
                    1,
                    2,
                    new int[] { 11 },
                    false,
                    new int[] { 17 },
                    true,
                    false,
                    3,
                    new Pos2d(66, 476),
                    scrollable))
            .widget(
                createUpgradeBox(
                    13,
                    2,
                    1,
                    new int[] { 11 },
                    false,
                    new int[] { 18 },
                    true,
                    false,
                    3,
                    new Pos2d(126, 476),
                    scrollable))
            .widget(
                createUpgradeBox(
                    14,
                    3,
                    0,
                    new int[] { 11 },
                    false,
                    new int[] { 15, 19 },
                    true,
                    false,
                    3,
                    new Pos2d(186, 476),
                    scrollable))
            .widget(
                createUpgradeBox(
                    15,
                    3,
                    1,
                    new int[] { 14 },
                    false,
                    new int[] {},
                    false,
                    false,
                    4,
                    new Pos2d(246, 496),
                    scrollable))
            .widget(
                createUpgradeBox(
                    16,
                    1,
                    1,
                    new int[] { 17 },
                    false,
                    new int[] {},
                    false,
                    false,
                    4,
                    new Pos2d(6, 556),
                    scrollable))
            .widget(
                createUpgradeBox(
                    17,
                    1,
                    0,
                    new int[] { 12 },
                    false,
                    new int[] { 16, 20 },
                    false,
                    false,
                    3,
                    new Pos2d(66, 536),
                    scrollable))
            .widget(
                createUpgradeBox(
                    18,
                    2,
                    1,
                    new int[] { 13 },
                    false,
                    new int[] { 21 },
                    false,
                    false,
                    3,
                    new Pos2d(126, 536),
                    scrollable))
            .widget(
                createUpgradeBox(
                    19,
                    3,
                    0,
                    new int[] { 14 },
                    false,
                    new int[] { 22 },
                    false,
                    false,
                    3,
                    new Pos2d(186, 536),
                    scrollable))
            .widget(
                createUpgradeBox(
                    20,
                    1,
                    0,
                    new int[] { 17 },
                    false,
                    new int[] { 23 },
                    false,
                    false,
                    3,
                    new Pos2d(66, 596),
                    scrollable))
            .widget(
                createUpgradeBox(
                    21,
                    2,
                    1,
                    new int[] { 18 },
                    false,
                    new int[] { 23 },
                    false,
                    false,
                    3,
                    new Pos2d(126, 596),
                    scrollable))
            .widget(
                createUpgradeBox(
                    22,
                    3,
                    1,
                    new int[] { 19 },
                    false,
                    new int[] { 23 },
                    false,
                    false,
                    3,
                    new Pos2d(186, 596),
                    scrollable))
            .widget(
                createUpgradeBox(
                    23,
                    0,
                    0,
                    new int[] { 20, 21, 22 },
                    false,
                    new int[] { 24 },
                    false,
                    false,
                    4,
                    new Pos2d(126, 656),
                    scrollable))
            .widget(
                createUpgradeBox(
                    24,
                    0,
                    1,
                    new int[] { 23 },
                    false,
                    new int[] { 25 },
                    false,
                    false,
                    5,
                    new Pos2d(126, 718),
                    scrollable))
            .widget(
                createUpgradeBox(
                    25,
                    0,
                    1,
                    new int[] { 24 },
                    false,
                    new int[] { 26 },
                    false,
                    false,
                    6,
                    new Pos2d(36, 758),
                    scrollable))
            .widget(
                createUpgradeBox(
                    26,
                    0,
                    3,
                    new int[] { 25 },
                    false,
                    new int[] { 27 },
                    false,
                    true,
                    7,
                    new Pos2d(36, 848),
                    scrollable))
            .widget(
                createUpgradeBox(
                    27,
                    0,
                    2,
                    new int[] { 26 },
                    false,
                    new int[] { 28 },
                    false,
                    false,
                    8,
                    new Pos2d(126, 888),
                    scrollable))
            .widget(
                createUpgradeBox(
                    28,
                    0,
                    0,
                    new int[] { 27 },
                    false,
                    new int[] { 29 },
                    false,
                    false,
                    9,
                    new Pos2d(216, 848),
                    scrollable))
            .widget(
                createUpgradeBox(
                    29,
                    0,
                    3,
                    new int[] { 28 },
                    false,
                    new int[] { 30 },
                    false,
                    true,
                    10,
                    new Pos2d(216, 758),
                    scrollable))
            .widget(
                createUpgradeBox(
                    30,
                    0,
                    3,
                    new int[] { 29 },
                    false,
                    new int[] {},
                    false,
                    true,
                    12,
                    new Pos2d(126, 798),
                    scrollable))
            .widget(new TextWidget("").setPos(0, 945));

        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_STAR)
                .setPos(0, 0)
                .setSize(300, 300))
            .widget(
                scrollable.setSize(292, 292)
                    .setPos(4, 4))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(282, 4));
        if (debugMode) {
            builder.widget(new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                upgrades = new boolean[31];
                materialPaidUpgrades = new boolean[7];
            })
                .setSize(40, 15)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .addTooltip(translateToLocal("fog.debug.resetbutton.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
                .addChild(
                    new TextWidget(translateToLocal("fog.debug.resetbutton.text")).setTextAlignment(Alignment.Center)
                        .setScale(0.57f)
                        .setMaxWidth(36)
                        .setPos(3, 3))
                .addChild(
                    new NumericWidget().setSetter(val -> gravitonShardsAvailable = (int) val)
                        .setGetter(() -> gravitonShardsAvailable)
                        .setBounds(0, 112)
                        .setDefaultValue(0)
                        .setScrollValues(1, 4, 64)
                        .setTextAlignment(Alignment.Center)
                        .setTextColor(Color.WHITE.normal)
                        .setSize(25, 18)
                        .setPos(4, 16)
                        .addTooltip(translateToLocal("fog.debug.gravitonshardsetter.tooltip"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD))
                .addChild(
                    new ButtonWidget().setOnClick((clickData, widget) -> Arrays.fill(upgrades, true))
                        .setSize(40, 15)
                        .setBackground(GTUITextures.BUTTON_STANDARD)
                        .addTooltip(translateToLocal("fog.debug.unlockall.text"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(0, 35))
                .addChild(
                    new TextWidget(translateToLocal("fog.debug.unlockall.text")).setTextAlignment(Alignment.Center)
                        .setScale(0.57f)
                        .setMaxWidth(36)
                        .setPos(3, 38))
                .setPos(4, 4));

        }
        return builder.build();
    }

    protected ModularWindow createIndividualUpgradeWindow(final EntityPlayer player) {
        UITexture background;
        UITexture overlay;
        UITexture milestoneSymbol;
        float widthRatio;
        switch (currentColorCode) {
            case 1 -> {
                background = TecTechUITextures.BACKGROUND_GLOW_PURPLE;
                overlay = TecTechUITextures.PICTURE_OVERLAY_PURPLE;
            }
            case 2 -> {
                background = TecTechUITextures.BACKGROUND_GLOW_ORANGE;
                overlay = TecTechUITextures.PICTURE_OVERLAY_ORANGE;
            }
            case 3 -> {
                background = TecTechUITextures.BACKGROUND_GLOW_GREEN;
                overlay = TecTechUITextures.PICTURE_OVERLAY_GREEN;
            }
            default -> {
                background = TecTechUITextures.BACKGROUND_GLOW_BLUE;
                overlay = TecTechUITextures.PICTURE_OVERLAY_BLUE;
            }
        }
        switch (currentMilestoneBG) {
            case 1 -> {
                milestoneSymbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CONVERSION;
                widthRatio = 0.72f;
            }
            case 2 -> {
                milestoneSymbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CATALYST;
                widthRatio = 1f;
            }
            case 3 -> {
                milestoneSymbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION;
                widthRatio = 1f;
            }
            default -> {
                milestoneSymbol = TecTechUITextures.PICTURE_GODFORGE_MILESTONE_CHARGE;
                widthRatio = 0.8f;
            }
        }
        int WIDTH = 250;
        int HEIGHT = 250;
        int LORE_POS = 110;
        if (currentUpgradeID == 0 || currentUpgradeID == 30) {
            WIDTH = 300;
            HEIGHT = 300;
            LORE_POS = 85;
        }
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT)
            .setBackground(background)
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(WIDTH - 15, 3))
            .widget(
                new DrawableWidget().setDrawable(milestoneSymbol)
                    .setPos((int) ((1 - widthRatio / 2) * WIDTH / 2), HEIGHT / 4)
                    .setSize((int) (WIDTH / 2 * widthRatio), HEIGHT / 2))
            .widget(
                new DrawableWidget().setDrawable(overlay)
                    .setPos(WIDTH / 4, HEIGHT / 4)
                    .setSize(WIDTH / 2, HEIGHT / 2))
            .widget(
                new MultiChildWidget()
                    .addChild(
                        new TextWidget(translateToLocal("fog.upgrade.tt." + (currentUpgradeID)))
                            .setTextAlignment(Alignment.Center)
                            .setDefaultColor(EnumChatFormatting.GOLD)
                            .setSize(WIDTH - 15, 30)
                            .setPos(9, 5))
                    .addChild(
                        new TextWidget(translateToLocal("fog.upgrade.text." + (currentUpgradeID)))
                            .setTextAlignment(Alignment.CenterLeft)
                            .setDefaultColor(EnumChatFormatting.WHITE)
                            .setSize(WIDTH - 15, LORE_POS - 30)
                            .setPos(9, 30))
                    .addChild(
                        new TextWidget(
                            EnumChatFormatting.ITALIC + translateToLocal("fog.upgrade.lore." + (currentUpgradeID)))
                                .setTextAlignment(Alignment.Center)
                                .setDefaultColor(0xbbbdbd)
                                .setSize(WIDTH - 15, (int) (HEIGHT * 0.9) - LORE_POS)
                                .setPos(9, LORE_POS))
                    .addChild(
                        new TextWidget(
                            translateToLocal("gt.blockmachines.multimachine.FOG.shardcost") + " "
                                + EnumChatFormatting.BLUE
                                + gravitonShardCost).setTextAlignment(Alignment.Center)
                                    .setScale(0.7f)
                                    .setMaxWidth(70)
                                    .setDefaultColor(0x9c9c9c)
                                    .setPos(11, HEIGHT - 25))
                    .addChild(
                        new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.availableshards"))
                            .setTextAlignment(Alignment.Center)
                            .setScale(0.7f)
                            .setMaxWidth(90)
                            .setDefaultColor(0x9c9c9c)
                            .setPos(WIDTH - 87, HEIGHT - 25))
                    .addChild(
                        TextWidget.dynamicText(this::gravitonShardAmount)
                            .setTextAlignment(Alignment.Center)
                            .setScale(0.7f)
                            .setMaxWidth(90)
                            .setDefaultColor(0x9c9c9c)
                            .setPos(WIDTH - 27, HEIGHT - 18)))
            .setSize(WIDTH, HEIGHT)

            .widget(new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                int unlockedPrereqUpgrades = 0;
                int unlockedFollowupUpgrades = 0;
                int unlockedSplitUpgrades = 0;
                if (!upgrades[currentUpgradeID]) {
                    for (int prereqUpgrade : prereqUpgrades) {
                        if (upgrades[prereqUpgrade]) {
                            unlockedPrereqUpgrades++;
                        }
                    }
                    if (!doesCurrentUpgradeRequireExtraMats
                        || materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
                            .indexOf(currentUpgradeID)]) {
                        if (allPrereqRequired) {
                            if (unlockedPrereqUpgrades == prereqUpgrades.length
                                && gravitonShardsAvailable >= gravitonShardCost) {
                                gravitonShardsAvailable -= gravitonShardCost;
                                gravitonShardsSpent += gravitonShardCost;
                                upgrades[currentUpgradeID] = true;
                            }
                        } else if (unlockedPrereqUpgrades > 0 || prereqUpgrades.length == 0) {
                            if (isUpradeSplitStart) {
                                for (int splitUpgrade : FIRST_SPLIT_UPGRADES) {
                                    if (upgrades[splitUpgrade]) {
                                        unlockedSplitUpgrades++;
                                    }
                                }
                                unlockedSplitUpgrades -= (ringAmount - 1);
                            }
                            if (unlockedSplitUpgrades <= 0 && gravitonShardsAvailable >= gravitonShardCost) {
                                gravitonShardsAvailable -= gravitonShardCost;
                                gravitonShardsSpent += gravitonShardCost;
                                upgrades[currentUpgradeID] = true;
                            }
                        }
                    }
                } else {
                    for (int followupUpgrade : followupUpgrades) {
                        if (upgrades[followupUpgrade]) {
                            unlockedFollowupUpgrades++;
                        }
                    }
                    if (unlockedFollowupUpgrades == 0) {
                        gravitonShardsAvailable += gravitonShardCost;
                        gravitonShardsSpent -= gravitonShardCost;
                        upgrades[currentUpgradeID] = false;
                    }
                }
            })
                .setSize(40, 15)
                .setBackground(() -> {
                    if (upgrades[currentUpgradeID]) {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED };
                    } else {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD };
                    }
                })
                .addTooltip(translateToLocal("fog.upgrade.confirm"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
                .addChild(
                    new TextWidget(translateToLocal("fog.upgrade.confirm")).setTextAlignment(Alignment.Center)
                        .setScale(0.7f)
                        .setMaxWidth(36)
                        .setPos(3, 5))
                .setPos(WIDTH / 2 - 21, (int) (HEIGHT * 0.9)));
        if (Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
            .contains(currentUpgradeID)) {
            builder.widget(createMaterialInputButton(currentUpgradeID, WIDTH / 2 - 40, (int) (HEIGHT * 0.9), builder));
        }
        return builder.build();
    }

    private Widget createMaterialInputButton(int upgradeID, int xCoord, int yCoord, IWidgetBuilder<?> builder) {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient() && doesCurrentUpgradeRequireExtraMats) {
                widget.getContext()
                    .openSyncedWindow(MANUAL_INSERTION_WINDOW_ID);
                widget.getContext()
                    .closeWindow(INDIVIDUAL_UPGRADE_WINDOW_ID);
                widget.getContext()
                    .closeWindow(UPGRADE_TREE_WINDOW_ID);
            }
        })
            .setPlayClickSound(doesCurrentUpgradeRequireExtraMats)
            .setBackground(() -> {
                if (doesCurrentUpgradeRequireExtraMats) {
                    if (materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
                        .indexOf(upgradeID)]) {
                        return new IDrawable[] { TecTechUITextures.BUTTON_BOXED_CHECKMARK_18x18 };
                    } else {
                        return new IDrawable[] { TecTechUITextures.BUTTON_BOXED_EXCLAMATION_POINT_18x18 };
                    }
                } else {
                    return new IDrawable[] { GTUITextures.TRANSPARENT };
                }
            })
            .setPos(xCoord, yCoord)
            .setSize(15, 15)
            .dynamicTooltip(this::upgradeMaterialRequirements)
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("fog.button.materialrequirements.tooltip.clickhere"))
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(
                    () -> materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
                        .indexOf(upgradeID)],
                    val -> materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
                        .indexOf(upgradeID)] = val),
                builder);
    }

    /**
     * @param upgradeID               ID of the upgrade
     * @param colorCode               Number deciding which colored background to use, 0 for blue, 1 for purple, 2 for
     *                                orange and 3 for green
     * @param milestone               Number deciding which milestone symbol to display in the background, 0 for charge,
     *                                1 for conversion, 2 for catalyst and 3 for composition
     * @param prerequisiteUpgradeIDs  IDs of the prior upgrades directly connected to the current one
     * @param requireAllPrerequisites Decides how many connected prerequisite upgrades have to be unlocked to be able to
     *                                unlock this one. True means ALL, False means AT LEAST ONE
     * @param followingUpgradeIDs     IDs of the following upgrades directly connected to the current one
     * @param isStartOfSplit          Whether this upgrade is one of the initial split upgrades
     * @param requiresExtraMaterials  Whether this upgrade requires materials other than graviton shards to unlock
     * @param shardCost               How many graviton shards are needed to unlock this upgrade
     * @param pos                     Position of the upgrade inside the scrollableWidget
     */
    private Widget createUpgradeBox(int upgradeID, int colorCode, int milestone, int[] prerequisiteUpgradeIDs,
        boolean requireAllPrerequisites, int[] followingUpgradeIDs, boolean isStartOfSplit,
        boolean requiresExtraMaterials, int shardCost, Pos2d pos, IWidgetBuilder<?> builder) {
        return new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
            currentUpgradeID = upgradeID;
            currentColorCode = colorCode;
            currentMilestoneBG = milestone;
            gravitonShardCost = shardCost;
            prereqUpgrades = prerequisiteUpgradeIDs;
            allPrereqRequired = requireAllPrerequisites;
            followupUpgrades = followingUpgradeIDs;
            isUpradeSplitStart = isStartOfSplit;
            doesCurrentUpgradeRequireExtraMats = requiresExtraMaterials;
            if (!widget.isClient()) widget.getContext()
                .openSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID);
        })
            .setSize(40, 15)
            .setBackground(() -> {
                if (upgrades[upgradeID]) {
                    return new IDrawable[] { TecTechUITextures.BUTTON_SPACE_PRESSED_32x16 };
                } else {
                    return new IDrawable[] { TecTechUITextures.BUTTON_SPACE_32x16 };
                }
            })
            .addTooltip(translateToLocal("fog.upgrade.tt." + upgradeID))
            .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .addChild(
                new TextWidget(translateToLocal("fog.upgrade.tt.short." + upgradeID)).setScale(0.8f)
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setSize(34, 9)
                    .setPos(3, 4))
            .setPos(pos)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> upgrades[upgradeID], val -> upgrades[upgradeID] = val),
                builder);
    }

    protected ModularWindow createManualInsertionWindow(final EntityPlayer player) {
        ItemStack[] inputs = godforgeUpgradeMats.get(currentUpgradeID);
        final int WIDTH = 189;
        final int HEIGHT = 106;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        final MultiChildWidget columns = new MultiChildWidget();
        final DynamicPositionedColumn column1 = new DynamicPositionedColumn();
        final DynamicPositionedColumn column2 = new DynamicPositionedColumn();
        final DynamicPositionedColumn column3 = new DynamicPositionedColumn();
        final DynamicPositionedColumn column4 = new DynamicPositionedColumn();
        final DynamicPositionedColumn column5 = new DynamicPositionedColumn();
        final DynamicPositionedColumn column6 = new DynamicPositionedColumn();
        List<DynamicPositionedColumn> columnList = Arrays.asList(column1, column2, column3, column4, column5, column6);
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT)))
                .subtract(5, 0)
                .add(0, 4));
        builder.widget(
            SlotGroup.ofItemHandler(inputSlotHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .phantom(false)
                .background(getGUITextureSet().getItemSlot())
                .build()
                .setPos(112, 6));
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getWindow()
                    .closeWindow();
                widget.getContext()
                    .openSyncedWindow(UPGRADE_TREE_WINDOW_ID);
                widget.getContext()
                    .openSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID);
            }
        })
            .setBackground(ModularUITextures.VANILLA_BACKGROUND, new Text("x"))
            .setPos(179, 0)
            .setSize(10, 10));
        builder.widget(new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                ArrayList<ItemStack> list = new ArrayList<>(inputSlotHandler.getStacks());
                list.removeIf(Objects::isNull);
                int foundInputs = 0;
                int[] foundInputIndices = new int[inputs.length];
                for (ItemStack inputStack : list) {
                    for (ItemStack requiredStack : inputs) {
                        if (ItemStack.areItemStacksEqual(requiredStack, inputStack)) {
                            foundInputIndices[foundInputs] = inputSlotHandler.getStacks()
                                .indexOf(inputStack);
                            foundInputs++;
                        }
                    }
                }
                if (foundInputs == inputs.length) {
                    for (int index : foundInputIndices) {
                        inputSlotHandler.extractItem(index, inputSlotHandler.getStackInSlot(index).stackSize, false);
                    }
                    materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
                        .indexOf(currentUpgradeID)] = true;
                }
            }
        })
            .setPlayClickSound(true)
            .setBackground(GTUITextures.BUTTON_STANDARD)
            .setSize(179, 18))
            .addChild(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.consumeUpgradeMats"))
                    .setTextAlignment(Alignment.Center)
                    .setScale(0.75f)
                    .setPos(0, 1)
                    .setSize(179, 18))
            .setPos(5, 82)
            .setSize(179, 16));

        int uniqueItems = inputs.length;
        for (int i = 0; i < 12; i++) {
            int index = i;
            int cleanDiv4 = index / 4;
            if (i < uniqueItems) {
                builder.widget(
                    new DrawableWidget().setDrawable(GTUITextures.BUTTON_STANDARD_PRESSED)
                        .setPos(5 + cleanDiv4 * 36, 6 + index % 4 * 18)
                        .setSize(18, 18));
                columnList.get(cleanDiv4)
                    .addChild(
                        new ItemDrawable().setItem(inputs[index])
                            .asWidget()
                            .dynamicTooltip(() -> {
                                List<String> tooltip = new ArrayList<>();
                                tooltip.add(inputs[index] != null ? inputs[index].getDisplayName() : "");
                                return tooltip;
                            })
                            .setSize(16, 16));
                columnList.get(cleanDiv4 + 3)
                    .addChild(
                        new TextWidget("x" + inputs[i].stackSize).setTextAlignment(Alignment.CenterLeft)
                            .setScale(0.8f)
                            .setSize(18, 8));
            } else {
                builder.widget(
                    new DrawableWidget().setDrawable(GTUITextures.BUTTON_STANDARD_DISABLED)
                        .setPos(5 + cleanDiv4 * 36, 6 + index % 4 * 18)
                        .setSize(18, 18));
            }
        }

        int counter = 0;
        for (DynamicPositionedColumn column : columnList) {
            int spacing = 2;
            int xCord = 1 + counter * 36;
            int yCord = 1;
            if (counter > 2) {
                spacing = 10;
                xCord = 19 + (counter - 3) * 36;
                yCord = 5;
            }
            columns.addChild(
                column.setSpace(spacing)
                    .setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                    .setSize(16, 72)
                    .setPos(xCord, yCord));
            counter++;
        }

        builder.widget(
            columns.setSize(108, 72)
                .setPos(5, 6));

        return builder.build();
    }

    protected ModularWindow createGeneralInfoWindow(final EntityPlayer player) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        final int WIDTH = 300;
        final int HEIGHT = 300;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);

        builder.setDraggable(true);
        scrollable.widget(
            new TextWidget(EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.introduction"))
                .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                .setTextAlignment(Alignment.TopCenter)
                .setPos(7, 13)
                .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.introductioninfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 30)
                    .setSize(280, 50))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.tableofcontents"))
                        .setDefaultColor(EnumChatFormatting.AQUA)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setPos(7, 80)
                        .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(150))
                    .setBackground(
                        new Text(EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.fuel"))
                            .alignment(Alignment.CenterLeft)
                            .color(0x55ffff))
                    .setPos(7, 95)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(434))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.modules"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 110)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(1088))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.upgrades"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 125)
                    .setSize(150, 15))
            .widget(
                new ButtonWidget().setOnClick((clickData, widget) -> scrollable.setVerticalScrollOffset(1412))
                    .setBackground(
                        new Text(
                            EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.FOG.milestones"))
                                .alignment(Alignment.CenterLeft)
                                .color(0x55ffff))
                    .setPos(7, 140)
                    .setSize(150, 15))
            .widget(
                TextWidget.dynamicText(this::inversionHeaderText)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 155)
                    .setSize(150, 15))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (inversion) {
                    scrollable.setVerticalScrollOffset(1766);
                }
            })
                .setPlayClickSound(inversion)
                .setPos(7, 155)
                .setSize(150, 15)
                .attachSyncer(new FakeSyncWidget.BooleanSyncer(() -> inversion, (val) -> inversion = val), scrollable))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.fuel"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(127, 160)
                        .setSize(40, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 177)
                    .setSize(280, 250))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.modules"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 440)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.moduleinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 461)
                    .setSize(280, 620))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.upgrades"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 1098)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.upgradeinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1115)
                    .setSize(280, 290))
            .widget(
                new TextWidget(
                    EnumChatFormatting.BOLD + "§N" + translateToLocal("gt.blockmachines.multimachine.FOG.milestones"))
                        .setDefaultColor(EnumChatFormatting.DARK_PURPLE)
                        .setTextAlignment(Alignment.TopCenter)
                        .setPos(7, 1422)
                        .setSize(280, 15))
            .widget(
                new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.milestoneinfotext"))
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1439)
                    .setSize(280, 320))
            .widget(
                TextWidget.dynamicText(this::inversionHeaderText)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.TopCenter)
                    .setPos(7, 1776)
                    .setSize(280, 15))
            .widget(
                TextWidget.dynamicText(this::inversionInfoText)
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(7, 1793)
                    .setSize(280, 160))
            .widget(
                new TextWidget("").setPos(7, 1965)
                    .setSize(10, 10));

        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_GLOW_WHITE)
                .setPos(0, 0)
                .setSize(300, 300))
            .widget(
                scrollable.setSize(292, 292)
                    .setPos(4, 4))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(284, 4));

        return builder.build();
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Stellar Forge")
            .addInfo(EnumChatFormatting.ITALIC + "Also known as Godforge or Gorge for short.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("Controller block for the Godforge, a massive structure harnessing the thermal,")
            .addInfo("gravitational and kinetic energy of a stabilised neutron star for material processing.")
            .addInfo(
                "This multiblock can house " + EnumChatFormatting.RED
                    + "up to 16 modules "
                    + EnumChatFormatting.GRAY
                    + "which utilize the star to energize materials")
            .addInfo("to varying degrees, ranging from regular smelting to matter degeneration.")
            .addInfo(TOOLTIP_BAR)
            .addInfo(
                "This multiblock has an " + EnumChatFormatting.GOLD
                    + "extensive upgrade tree "
                    + EnumChatFormatting.GRAY
                    + "which influences all of its functions,")
            .addInfo(
                "such as " + EnumChatFormatting.GOLD
                    + "unlocking new module types, increasing heat levels "
                    + EnumChatFormatting.GRAY
                    + "and "
                    + EnumChatFormatting.GOLD
                    + "granting")
            .addInfo(
                EnumChatFormatting.GOLD + "various processing speed bonuses. "
                    + EnumChatFormatting.GRAY
                    + "These upgrades can be unlocked by reaching")
            .addInfo("certain milestones and/or spending materials.")
            .addInfo(TOOLTIP_BAR)
            .addInfo(
                EnumChatFormatting.GREEN
                    + "Clicking on the logo in the controller gui opens an extensive information window,")
            .addInfo("explaining everything there is to know about this multiblock.")
            .addInfo(TOOLTIP_BAR)
            .beginStructureBlock(126, 29, 186, false)
            .addStructureInfo("The structure is too complex! See schematic for details.")
            .addStructureInfo(
                "Total blocks needed for the structure with " + EnumChatFormatting.DARK_PURPLE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "2"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "3"
                    + EnumChatFormatting.GRAY
                    + " rings:")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "3943"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "7279"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "11005"
                    + EnumChatFormatting.GRAY
                    + " Transcendentally Amplified Magnetic Confinement Casing")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "2819"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "4831"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "6567"
                    + EnumChatFormatting.GRAY
                    + " Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "272"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "512"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "824"
                    + EnumChatFormatting.GRAY
                    + " Celestial Matter Guidance Casing")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "130"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "144"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "158"
                    + EnumChatFormatting.GRAY
                    + " Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "9"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "54"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "155"
                    + EnumChatFormatting.GRAY
                    + " Spatially Transcendent Gravitational Lens Block")
            .addStructureInfo(
                EnumChatFormatting.DARK_PURPLE + "345"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "357"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "397"
                    + EnumChatFormatting.DARK_PURPLE
                    + " Remote"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.DARK_GREEN
                    + "Medial"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.AQUA
                    + "Central"
                    + EnumChatFormatting.GRAY
                    + " Graviton Flow Modulator")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "36" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Hatch")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Output Bus")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Bus")
            .addStructureInfo("--------------------------------------------")
            .toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

    @Override
    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        return true;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] { EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.FOG.hint.0"),
            translateToLocal("gt.blockmachines.multimachine.FOG.hint.1") };
    }

    public int getFuelType() {
        return selectedFuelType;
    }

    private void setFuelType(int fuelType) {
        selectedFuelType = fuelType;
    }

    public int getFuelFactor() {
        return fuelConsumptionFactor;
    }

    public boolean isUpgradeActive(int upgradeID) {
        return upgrades[upgradeID];
    }

    public int getRingAmount() {
        return ringAmount;
    }

    public int getTotalActiveUpgrades() {
        int totalUpgrades = 0;
        for (boolean upgrade : upgrades) {
            if (upgrade) {
                totalUpgrades++;
            }
        }
        return totalUpgrades;
    }

    private Text fuelUsage() {
        return new Text(fuelConsumption + " L/5s");
    }

    private Text gravitonShardAmount() {
        EnumChatFormatting enoughGravitonShards = EnumChatFormatting.RED;
        if (gravitonShardsAvailable >= gravitonShardCost) {
            enoughGravitonShards = EnumChatFormatting.GREEN;
        }
        return new Text(enoughGravitonShards + Integer.toString(gravitonShardsAvailable));

    }

    private Text storedFuel() {
        if (internalBattery == 0) {
            return new Text(
                translateToLocal("gt.blockmachines.multimachine.FOG.storedstartupfuel") + " "
                    + stellarFuelAmount
                    + "/"
                    + neededStartupFuel);
        }
        return new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.storedfuel") + " "
                + internalBattery
                + "/"
                + maxBatteryCharge);
    }

    private void checkInversionStatus() {
        int inversionChecker = 0;
        for (int progress : milestoneProgress) {
            if (progress < 7) {
                break;
            }
            inversionChecker++;
        }
        inversion = inversionChecker == 4;
    }

    private Text inversionStatusText() {
        String inversionStatus = "";
        if (inversion) {
            inversionStatus = EnumChatFormatting.BOLD
                + translateToLocal("gt.blockmachines.multimachine.FOG.inversionactive");
        }
        return new Text(inversionStatus);
    }

    private void determineCompositionMilestoneLevel() {
        int[] uniqueModuleCount = new int[5];
        int smelting = 0;
        int molten = 0;
        int plasma = 0;
        int exotic = 0;
        int exoticMagmatter = 0;
        for (MTEBaseModule module : moduleHatches) {
            if (module instanceof MTESmeltingModule) {
                uniqueModuleCount[0] = 1;
                smelting++;
                continue;
            }
            if (module instanceof MTEMoltenModule) {
                uniqueModuleCount[1] = 1;
                molten++;
                continue;
            }
            if (module instanceof MTEPlasmaModule) {
                uniqueModuleCount[2] = 1;
                plasma++;
                continue;
            }
            if (module instanceof MTEExoticModule) {
                if (!((MTEExoticModule) module).isMagmatterModeOn()) {
                    uniqueModuleCount[3] = 1;
                    exotic++;
                } else {
                    uniqueModuleCount[4] = 1;
                    exoticMagmatter++;
                }
            }

        }
        totalExtensionsBuilt = Arrays.stream(uniqueModuleCount)
            .sum() + ringAmount
            - 1;
        if (inversion) {
            totalExtensionsBuilt += (smelting - 1
                + (molten - 1) * 2
                + (plasma - 1) * 3
                + (exotic - 1) * 4
                + (exoticMagmatter - 1) * 5) / 5f;
        }
        milestoneProgress[3] = (int) Math.floor(totalExtensionsBuilt);
    }

    private void determineMilestoneProgress() {
        int closestRelevantSeven;
        float rawProgress;
        float actualProgress;
        if (milestoneProgress[0] < 7) {
            powerMilestonePercentage = (float) max(
                (log((totalPowerConsumed.divide(BigInteger.valueOf(POWER_MILESTONE_CONSTANT))).longValue())
                    / POWER_LOG_CONSTANT + 1),
                0) / 7;
            milestoneProgress[0] = (int) floor(powerMilestonePercentage * 7);
        }
        if (inversion) {
            rawProgress = (totalPowerConsumed.divide(POWER_MILESTONE_T7_CONSTANT)
                .floatValue() - 1) / 7;
            closestRelevantSeven = (int) floor(rawProgress);
            actualProgress = rawProgress - closestRelevantSeven;
            milestoneProgress[0] = 7 + (int) floor(rawProgress * 7);
            if (closestRelevantSeven % 2 == 0) {
                invertedPowerMilestonePercentage = actualProgress;
                powerMilestonePercentage = 1 - invertedPowerMilestonePercentage;
            } else {
                powerMilestonePercentage = actualProgress;
                invertedPowerMilestonePercentage = 1 - powerMilestonePercentage;
            }
        }

        if (milestoneProgress[1] < 7) {
            recipeMilestonePercentage = (float) max(
                (log(totalRecipesProcessed * 1f / RECIPE_MILESTONE_CONSTANT) / RECIPE_LOG_CONSTANT + 1),
                0) / 7;
            milestoneProgress[1] = (int) floor(recipeMilestonePercentage * 7);
        }
        if (inversion) {
            rawProgress = (((float) totalRecipesProcessed / RECIPE_MILESTONE_T7_CONSTANT) - 1) / 7;
            closestRelevantSeven = (int) floor(rawProgress);
            actualProgress = rawProgress - closestRelevantSeven;
            milestoneProgress[1] = 7 + (int) floor(rawProgress * 7);
            if (closestRelevantSeven % 2 == 0) {
                invertedRecipeMilestonePercentage = actualProgress;
                recipeMilestonePercentage = 1 - invertedRecipeMilestonePercentage;
            } else {
                recipeMilestonePercentage = actualProgress;
                invertedRecipeMilestonePercentage = 1 - recipeMilestonePercentage;
            }
        }
        if (milestoneProgress[2] < 7) {
            fuelMilestonePercentage = (float) max(
                (log(totalFuelConsumed * 1f / FUEL_MILESTONE_CONSTANT) / FUEL_LOG_CONSTANT + 1),
                0) / 7;
            milestoneProgress[2] = (int) floor(fuelMilestonePercentage * 7);
        }
        if (inversion) {
            rawProgress = (((float) totalFuelConsumed / FUEL_MILESTONE_T7_CONSTANT) - 1) / 7;
            closestRelevantSeven = (int) floor(rawProgress);
            actualProgress = rawProgress - closestRelevantSeven;
            milestoneProgress[2] = 7 + (int) floor(rawProgress * 7);
            if ((closestRelevantSeven % 2) == 0) {
                invertedFuelMilestonePercentage = actualProgress;
                fuelMilestonePercentage = 1 - invertedFuelMilestonePercentage;
            } else {
                fuelMilestonePercentage = actualProgress;
                invertedFuelMilestonePercentage = 1 - fuelMilestonePercentage;
            }
        }

        if (milestoneProgress[3] <= 7) {
            structureMilestonePercentage = totalExtensionsBuilt / 7f;
        }
        if (inversion) {
            rawProgress = (totalExtensionsBuilt - 7) / 7f;
            closestRelevantSeven = (int) floor(rawProgress);
            actualProgress = rawProgress - closestRelevantSeven;
            if ((closestRelevantSeven % 2) == 0) {
                invertedStructureMilestonePercentage = actualProgress;
                structureMilestonePercentage = 1 - invertedStructureMilestonePercentage;
            } else {
                structureMilestonePercentage = actualProgress;
                invertedStructureMilestonePercentage = 1 - structureMilestonePercentage;
            }
        }
    }

    private void determineGravitonShardAmount() {
        int sum = 0;
        for (int progress : milestoneProgress) {
            if (!inversion) {
                progress = Math.min(progress, 7);
            }
            sum += progress * (progress + 1) / 2;
        }
        gravitonShardsAvailable = sum - gravitonShardsSpent;
    }

    private void ejectGravitonShards() {
        if (mOutputBusses.size() == 1) {
            while (gravitonShardsAvailable >= 64) {
                addOutput(GTOreDictUnificator.get(OrePrefixes.gem, MaterialsUEVplus.GravitonShard, 64));
                gravitonShardsAvailable -= 64;
            }
            addOutput(
                GTOreDictUnificator.get(OrePrefixes.gem, MaterialsUEVplus.GravitonShard, gravitonShardsAvailable));
            gravitonShardsAvailable = 0;
        }
    }

    private Text gravitonShardAmountText(int milestoneID) {
        int sum;
        int progress = milestoneProgress[milestoneID];
        if (!inversion) {
            progress = Math.min(progress, 7);
        }
        sum = progress * (progress + 1) / 2;
        return new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.shardgain") + ": " + EnumChatFormatting.GRAY + sum);
    }

    private Text totalMilestoneProgress(int milestoneID) {
        long progress;
        BigInteger bigProgress;
        String suffix;
        switch (milestoneID) {
            case 1 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.recipes");
                progress = totalRecipesProcessed;
            }
            case 2 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.fuelconsumed");
                progress = totalFuelConsumed;
            }
            case 3 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.extensions");
                progress = milestoneProgress[3];
            }
            default -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.power");
                bigProgress = totalPowerConsumed;
                if (!noFormatting && (totalPowerConsumed.compareTo(BigInteger.valueOf(1_000L)) > 0)) {
                    return new Text(
                        translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress") + ": "
                            + EnumChatFormatting.GRAY
                            + toExponentForm(bigProgress)
                            + " "
                            + suffix);
                } else {
                    return new Text(
                        translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress") + ": "
                            + EnumChatFormatting.GRAY
                            + bigProgress
                            + " "
                            + suffix);
                }
            }
        }
        if (!noFormatting) {
            return new Text(
                translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress") + ": "
                    + EnumChatFormatting.GRAY
                    + formatNumbers(progress)
                    + " "
                    + suffix);
        } else {
            return new Text(
                translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress") + ": "
                    + EnumChatFormatting.GRAY
                    + progress
                    + " "
                    + suffix);
        }

    }

    private Text currentMilestone(int milestoneID) {
        return new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.milestoneprogress") + ": "
                + EnumChatFormatting.GRAY
                + milestoneProgress[milestoneID]);
    }

    private Text milestoneProgressText(int milestoneID, boolean formatting) {
        long max;
        BigInteger bigMax;
        String suffix;
        String progressText = translateToLocal("gt.blockmachines.multimachine.FOG.progress");
        Text done = new Text(translateToLocal("gt.blockmachines.multimachine.FOG.milestonecomplete"));
        if (noFormatting) {
            formatting = false;
            done = new Text(
                translateToLocal("gt.blockmachines.multimachine.FOG.milestonecomplete") + EnumChatFormatting.DARK_RED
                    + "?");
        }
        switch (milestoneID) {
            case 0:
                if (milestoneProgress[0] < 7 || inversion) {
                    suffix = translateToLocal("gt.blockmachines.multimachine.FOG.power");
                    if (inversion) {
                        bigMax = POWER_MILESTONE_T7_CONSTANT.multiply(BigInteger.valueOf(milestoneProgress[0] - 5));
                    } else {
                        bigMax = BigInteger.valueOf(LongMath.pow(9, milestoneProgress[0]))
                            .multiply(BigInteger.valueOf(LongMath.pow(10, 15)));
                    }
                    if (formatting && (totalPowerConsumed.compareTo(BigInteger.valueOf(1_000L)) > 0)) {
                        return new Text(
                            progressText + ": " + EnumChatFormatting.GRAY + toExponentForm(bigMax) + " " + suffix);
                    } else {
                        return new Text(progressText + ": " + EnumChatFormatting.GRAY + bigMax + " " + suffix);
                    }
                } else {
                    return done;
                }
            case 1:
                if (milestoneProgress[1] < 7 || inversion) {
                    suffix = translateToLocal("gt.blockmachines.multimachine.FOG.recipes");
                    if (inversion) {
                        max = RECIPE_MILESTONE_T7_CONSTANT * (milestoneProgress[1] - 5);
                    } else {
                        max = LongMath.pow(6, milestoneProgress[1]) * LongMath.pow(10, 7);
                    }
                    break;
                } else {
                    return done;
                }
            case 2:
                if (milestoneProgress[2] < 7 || inversion) {
                    suffix = translateToLocal("gt.blockmachines.multimachine.FOG.fuelconsumed");
                    if (inversion) {
                        max = FUEL_MILESTONE_T7_CONSTANT * (milestoneProgress[2] - 5);
                    } else {
                        max = LongMath.pow(3, milestoneProgress[2]) * LongMath.pow(10, 4);
                    }
                    break;
                } else {
                    return done;
                }
            case 3:
                if (milestoneProgress[3] < 7 || inversion) {
                    suffix = translateToLocal("gt.blockmachines.multimachine.FOG.extensions");
                    max = milestoneProgress[3] + 1;
                    break;
                } else {
                    return done;
                }
            default:
                return new Text("Error");
        }
        if (formatting) {
            return new Text(progressText + ": " + EnumChatFormatting.GRAY + formatNumbers(max) + " " + suffix);
        } else {
            return new Text(progressText + ": " + EnumChatFormatting.GRAY + max + " " + suffix);
        }
    }

    private Text inversionHeaderText() {
        return inversion
            ? new Text(
                EnumChatFormatting.BOLD + "§k2"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.WHITE
                    + EnumChatFormatting.BOLD
                    + translateToLocal("gt.blockmachines.multimachine.FOG.inversion")
                    + EnumChatFormatting.BOLD
                    + "§k2")
            : new Text("");
    }

    private Text inversionInfoText() {
        return inversion ? new Text(translateToLocal("gt.blockmachines.multimachine.FOG.inversioninfotext"))
            : new Text("");
    }

    private List<String> upgradeMaterialRequirements() {
        if (materialPaidUpgrades[Arrays.asList(UPGRADE_MATERIAL_ID_CONVERSION)
            .indexOf(currentUpgradeID)]) {
            return ImmutableList.of(translateToLocal("fog.button.materialrequirementsmet.tooltip"));
        }
        return ImmutableList.of(translateToLocal("fog.button.materialrequirements.tooltip"));
    }

    private void increaseBattery(int amount) {
        if ((internalBattery + amount) <= maxBatteryCharge) {
            internalBattery += amount;
        } else {
            internalBattery = maxBatteryCharge;
            batteryCharging = false;
        }
    }

    public void reduceBattery(int amount) {
        if (internalBattery - amount <= 0) {
            internalBattery = 0;
            if (moduleHatches.size() > 0) {
                for (MTEBaseModule module : moduleHatches) {
                    module.disconnect();
                }
            }
            destroyRenderer();
        } else {
            internalBattery -= amount;
            totalFuelConsumed += amount;
        }

    }

    public int getBatteryCharge() {
        return internalBattery;
    }

    public int getMaxBatteryCharge() {
        return maxBatteryCharge;
    }

    public void addTotalPowerConsumed(BigInteger amount) {
        totalPowerConsumed = totalPowerConsumed.add(amount);
    }

    public void addTotalRecipesProcessed(long amount) {
        totalRecipesProcessed += amount;
    }

    @Override
    protected void setHatchRecipeMap(MTEHatchInput hatch) {}

    @Override
    public void setItemNBT(NBTTagCompound NBT) {
        NBT.setInteger("selectedFuelType", selectedFuelType);
        NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        NBT.setInteger("internalBattery", internalBattery);
        NBT.setBoolean("batteryCharging", batteryCharging);
        NBT.setInteger("batterySize", maxBatteryCharge);
        NBT.setInteger("gravitonShardsAvailable", gravitonShardsAvailable);
        NBT.setInteger("gravitonShardsSpent", gravitonShardsSpent);
        NBT.setByteArray("totalPowerConsumed", totalPowerConsumed.toByteArray());
        NBT.setLong("totalRecipesProcessed", totalRecipesProcessed);
        NBT.setLong("totalFuelConsumed", totalFuelConsumed);
        NBT.setInteger("starFuelStored", stellarFuelAmount);
        NBT.setBoolean("gravitonShardEjection", gravitonShardEjection);

        // Store booleanArrays of all upgrades
        NBTTagCompound upgradeBooleanArrayNBTTag = new NBTTagCompound();

        int upgradeIndex = 0;
        for (Boolean upgrade : upgrades) {
            upgradeBooleanArrayNBTTag.setBoolean("upgrade" + upgradeIndex, upgrade);
            upgradeIndex++;
        }

        NBT.setTag("upgrades", upgradeBooleanArrayNBTTag);

        NBTTagCompound upgradeMaterialBooleanArrayNBTTag = new NBTTagCompound();

        int upgradeMaterialIndex = 0;
        for (Boolean upgrade : materialPaidUpgrades) {
            upgradeBooleanArrayNBTTag.setBoolean("upgradeMaterial" + upgradeMaterialIndex, upgrade);
            upgradeMaterialIndex++;
        }

        NBT.setTag("upgradeMaterials", upgradeMaterialBooleanArrayNBTTag);
        super.saveNBTData(NBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setInteger("selectedFuelType", selectedFuelType);
        NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        NBT.setInteger("internalBattery", internalBattery);
        NBT.setBoolean("batteryCharging", batteryCharging);
        NBT.setInteger("batterySize", maxBatteryCharge);
        NBT.setInteger("gravitonShardsAvailable", gravitonShardsAvailable);
        NBT.setInteger("gravitonShardsSpent", gravitonShardsSpent);
        NBT.setByteArray("totalPowerConsumed", totalPowerConsumed.toByteArray());
        NBT.setLong("totalRecipesProcessed", totalRecipesProcessed);
        NBT.setLong("totalFuelConsumed", totalFuelConsumed);
        NBT.setInteger("starFuelStored", stellarFuelAmount);
        NBT.setBoolean("gravitonShardEjection", gravitonShardEjection);
        NBT.setBoolean("isRenderActive", isRenderActive);
        NBT.setInteger("ringAmount", ringAmount);

        // Store booleanArray of all upgrades
        NBTTagCompound upgradeBooleanArrayNBTTag = new NBTTagCompound();

        int upgradeIndex = 0;
        for (boolean upgrade : upgrades) {
            upgradeBooleanArrayNBTTag.setBoolean("upgrade" + upgradeIndex, upgrade);
            upgradeIndex++;
        }

        NBT.setTag("upgrades", upgradeBooleanArrayNBTTag);

        NBTTagCompound upgradeMaterialBooleanArrayNBTTag = new NBTTagCompound();

        int upgradeMaterialIndex = 0;
        for (boolean upgrade : materialPaidUpgrades) {
            upgradeMaterialBooleanArrayNBTTag.setBoolean("upgradeMaterial" + upgradeMaterialIndex, upgrade);
            upgradeMaterialIndex++;
        }

        NBT.setTag("upgradeMaterials", upgradeMaterialBooleanArrayNBTTag);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        selectedFuelType = NBT.getInteger("selectedFuelType");
        fuelConsumptionFactor = NBT.getInteger("fuelConsumptionFactor");
        internalBattery = NBT.getInteger("internalBattery");
        batteryCharging = NBT.getBoolean("batteryCharging");
        maxBatteryCharge = NBT.getInteger("batterySize");
        gravitonShardsAvailable = NBT.getInteger("gravitonShardsAvailable");
        gravitonShardsSpent = NBT.getInteger("gravitonShardsSpent");
        totalPowerConsumed = new BigInteger(NBT.getByteArray("totalPowerConsumed"));
        totalRecipesProcessed = NBT.getLong("totalRecipesProcessed");
        totalFuelConsumed = NBT.getLong("totalFuelConsumed");
        stellarFuelAmount = NBT.getInteger("starFuelStored");
        gravitonShardEjection = NBT.getBoolean("gravitonShardEjection");
        isRenderActive = NBT.getBoolean("isRenderActive");
        ringAmount = NBT.getInteger("ringAmount");

        NBTTagCompound tempBooleanTag = NBT.getCompoundTag("upgrades");

        for (int upgradeIndex = 0; upgradeIndex < 31; upgradeIndex++) {
            boolean upgrade = tempBooleanTag.getBoolean("upgrade" + upgradeIndex);
            upgrades[upgradeIndex] = upgrade;
        }

        tempBooleanTag = NBT.getCompoundTag("upgradeMaterials");

        for (int upgradeIndex = 0; upgradeIndex < 7; upgradeIndex++) {
            boolean upgrade = tempBooleanTag.getBoolean("upgradeMaterial" + upgradeIndex);
            materialPaidUpgrades[upgradeIndex] = upgrade;
        }

        super.loadNBTData(NBT);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
