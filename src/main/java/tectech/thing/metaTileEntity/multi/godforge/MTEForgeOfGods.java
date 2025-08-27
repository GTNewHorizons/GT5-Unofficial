package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static gregtech.api.util.GTUtility.formatNumbers;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;
import static tectech.thing.casing.TTCasingsContainer.forgeOfGodsRenderBlock;
import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.*;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.allowModuleConnection;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateEnergyDiscountForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateFuelConsumption;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateMaxFuelFactor;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateMaxHeatForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateMaxParallelForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateProcessingVoltageForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateSpeedBonusForModules;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.calculateStartupFuelConsumption;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.factorChangeDuringRecipeAntiCheese;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.queryMilestoneStats;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.setMiscModuleParameters;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
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
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidNameHolderWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.threads.RunnableMachineUpdate;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import tectech.TecTech;
import tectech.loader.ConfigHandler;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.TileEntityForgeOfGods;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorStorage;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsRingsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.structure.ForgeOfGodsStructureString;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.UpgradeStorage;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsUI;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsUI.StarColorRGBM;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneFormatter;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneIcon;
import tectech.thing.metaTileEntity.multi.godforge.util.UpgradeColor;

public class MTEForgeOfGods extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    // Field default values for non-zero value defaults for item NBT checks
    private static final int DEFAULT_FUEL_CONSUMPTION_FACTOR = 1;
    private static final int DEFAULT_MAX_BATTERY_CHARGE = 100;
    private static final int DEFAULT_RING_AMOUNT = 1;
    private static final int DEFAULT_ROTATION_SPEED = 5;
    private static final int DEFAULT_STAR_SIZE = 20;
    private static final String DEFAULT_STAR_COLOR = ForgeOfGodsStarColor.DEFAULT.getName();
    private static final MilestoneFormatter DEFAULT_FORMATTING_MODE = MilestoneFormatter.COMMA;
    private static final BigInteger DEFAULT_TOTAL_POWER = BigInteger.ZERO;

    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int fuelConsumptionFactor = DEFAULT_FUEL_CONSUMPTION_FACTOR;
    private int selectedFuelType;
    private int internalBattery;
    private int maxBatteryCharge = DEFAULT_MAX_BATTERY_CHARGE;
    private int gravitonShardsAvailable;
    private int gravitonShardsSpent;
    private int ringAmount = DEFAULT_RING_AMOUNT;
    private int stellarFuelAmount;
    private int neededStartupFuel;
    private long fuelConsumption;
    private long totalRecipesProcessed;
    private long totalFuelConsumed;
    private float totalExtensionsBuilt;
    private float powerMilestonePercentage;
    private float recipeMilestonePercentage;
    private float fuelMilestonePercentage;
    private float structureMilestonePercentage;
    private float invertedPowerMilestonePercentage;
    private float invertedRecipeMilestonePercentage;
    private float invertedFuelMilestonePercentage;
    private float invertedStructureMilestonePercentage;
    private BigInteger totalPowerConsumed = DEFAULT_TOTAL_POWER;
    private boolean batteryCharging;
    private boolean inversion;
    private boolean gravitonShardEjection;
    private MilestoneFormatter formattingMode = DEFAULT_FORMATTING_MODE;
    private boolean isRenderActive;
    private boolean secretUpgrade;
    private boolean isRendererDisabled;
    private final ItemStack[] storedUpgradeWindowItems = new ItemStack[16];
    public ArrayList<MTEBaseModule> moduleHatches = new ArrayList<>();
    protected ItemStackHandler inputSlotHandler = new ItemStackHandler(16);

    private final UpgradeStorage upgrades = new UpgradeStorage();
    private ForgeOfGodsUpgrade currentUpgradeWindow;

    // Star cosmetics fields
    // actual star cosmetics
    private final StarColorStorage starColors = new StarColorStorage();
    private String selectedStarColor = DEFAULT_STAR_COLOR;
    private int rotationSpeed = DEFAULT_ROTATION_SPEED;
    private int starSize = DEFAULT_STAR_SIZE;
    // editing star color
    private ForgeOfGodsStarColor newStarColor = starColors.newTemplateColor();
    private int starColorR, starColorG, starColorB;
    private float starGamma;
    private int editingStarIndex; // editing a full color preset
    private int editingColorIndex; // editing a single color in a preset
    private ForgeOfGodsStarColor importedStarColor;

    private static final int FUEL_CONFIG_WINDOW_ID = 9;
    private static final int UPGRADE_TREE_WINDOW_ID = 10;
    private static final int INDIVIDUAL_UPGRADE_WINDOW_ID = 11;
    private static final int BATTERY_CONFIG_WINDOW_ID = 12;
    private static final int MILESTONE_WINDOW_ID = 13;
    private static final int INDIVIDUAL_MILESTONE_WINDOW_ID = 14;
    private static final int MANUAL_INSERTION_WINDOW_ID = 15;
    private static final int GENERAL_INFO_WINDOW_ID = 16;
    private static final int SPECIAL_THANKS_WINDOW_ID = 17;
    private static final int STAR_COSMETICS_WINDOW_ID = 18;
    private static final int STAR_CUSTOM_COLOR_WINDOW_ID = 19;
    private static final int STAR_CUSTOM_COLOR_IMPORT_WINDOW_ID = 20;
    private static final int STATISTICS_WINDOW_ID = 21;
    private static final int TEXTURE_INDEX = 960;
    private static final long SOUND_LOOP_LENGTH = 440;
    private static final long POWER_MILESTONE_CONSTANT = LongMath.pow(10, 15);
    private static final long RECIPE_MILESTONE_CONSTANT = LongMath.pow(10, 7);
    private static final long FUEL_MILESTONE_CONSTANT = 10_000;
    private static final long RECIPE_MILESTONE_T7_CONSTANT = RECIPE_MILESTONE_CONSTANT * LongMath.pow(4, 6);
    private static final long FUEL_MILESTONE_T7_CONSTANT = FUEL_MILESTONE_CONSTANT * LongMath.pow(3, 6);
    private static final BigInteger POWER_MILESTONE_T7_CONSTANT = BigInteger.valueOf(POWER_MILESTONE_CONSTANT)
        .multiply(BigInteger.valueOf(LongMath.pow(9, 6)));
    private static final double POWER_LOG_CONSTANT = Math.log(9);
    private static final double RECIPE_LOG_CONSTANT = Math.log(4);
    private static final double FUEL_LOG_CONSTANT = Math.log(3);
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final String STRUCTURE_PIECE_SHAFT = "beam_shaft";
    protected static final String STRUCTURE_PIECE_FIRST_RING = "first_ring";
    protected static final String STRUCTURE_PIECE_FIRST_RING_AIR = "first_ring_air";
    protected static final String STRUCTURE_PIECE_SECOND_RING = "second_ring";
    protected static final String STRUCTURE_PIECE_SECOND_RING_AIR = "second_ring_air";
    protected static final String STRUCTURE_PIECE_THIRD_RING = "third_ring";
    protected static final String STRUCTURE_PIECE_THIRD_RING_AIR = "third_ring_air";
    private static final String SCANNER_INFO_BAR = EnumChatFormatting.BLUE.toString() + EnumChatFormatting.STRIKETHROUGH
        + "--------------------------------------------";
    private static final ItemStack STELLAR_FUEL = Avaritia.isModLoaded() ? getModItem(Avaritia.ID, "Resource", 1, 8)
        : GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 1);

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        // 1000 blocks max per placement.
        int realBudget = elementBudget >= 1000 ? elementBudget : Math.min(1000, elementBudget * 5);
        int built = 0;

        survivalBuildPiece(STRUCTURE_PIECE_SHAFT, stackSize, 63, 14, 1, realBudget, env, false, true);

        if ((stackSize.stackSize > 0 && !isRenderActive)) {
            built += survivalBuildPiece(
                STRUCTURE_PIECE_FIRST_RING,
                stackSize,
                63,
                14,
                -59,
                realBudget,
                env,
                false,
                true);
        }

        if (stackSize.stackSize > 1 && ringAmount < 2) {
            built += survivalBuildPiece(
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

        if (stackSize.stackSize > 2 && ringAmount < 3) {
            built += survivalBuildPiece(
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
        .addElement(
            'A',
            HatchElementBuilder.<MTEForgeOfGods>builder()
                .atLeast(InputBus, InputHatch, OutputBus)
                .casingIndex(TEXTURE_INDEX + 3)
                .dot(1)
                .buildAndChain(GodforgeCasings, 3))
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
                .dot(2)
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
    public boolean supportsPowerPanel() {
        return false;
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

        if (internalBattery != 0 && !isRenderActive && !isRendererDisabled) {
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
            if (!mEnergyHatches.isEmpty()) {
                return false;
            }

            if (!mExoticEnergyHatches.isEmpty()) {
                return false;
            }
        }

        // Make sure there is 1 input hatch
        if (mInputHatches.size() != 1) {
            return false;
        }

        if (isUpgradeActive(CD)) {
            if (checkPiece(STRUCTURE_PIECE_SECOND_RING, 55, 11, -67)) {
                ringAmount = 2;
                if (!isRendererDisabled) {
                    destroySecondRing();
                    updateRenderer();
                }
            }
            if (isRenderActive && ringAmount >= 2 && !checkPiece(STRUCTURE_PIECE_SECOND_RING_AIR, 55, 11, -67)) {
                destroyRenderer();
            }
        } else {
            if (ringAmount == 3) {
                buildThirdRing();
            }
            if (ringAmount >= 2) {
                ringAmount = 1;
                if (!isRendererDisabled) {
                    updateRenderer();
                }
                buildSecondRing();
            }
        }

        if (isUpgradeActive(EE)) {
            if (checkPiece(STRUCTURE_PIECE_THIRD_RING, 47, 13, -76)) {
                ringAmount = 3;
                if (!isRendererDisabled) {
                    destroyThirdRing();
                    updateRenderer();
                }
            }
            if (isRenderActive && ringAmount == 3 && !checkPiece(STRUCTURE_PIECE_THIRD_RING_AIR, 47, 13, -76)) {
                destroyRenderer();
            }
        } else {
            if (ringAmount == 3) {
                ringAmount = 2;
                if (!isRendererDisabled) {
                    updateRenderer();
                }
                buildThirdRing();
            }
        }

        return true;
    }

    long ticker = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick == 1) {
                updateRenderer();
            }
            ticker++;
            // Check and drain fuel
            if (ticker % (5 * SECONDS) == 0) {
                startRecipeProcessing();

                int maxModuleCount = 8;
                if (isUpgradeActive(CD)) {
                    maxModuleCount += 4;
                }
                if (isUpgradeActive(EE)) {
                    maxModuleCount += 4;
                }

                if (!mInputBusses.isEmpty()) {
                    if (internalBattery == 0 || isUpgradeActive(END)) {
                        MTEHatchInputBus inputBus = mInputBusses.get(0);
                        ItemStack[] inputBusInventory = inputBus.getRealInventory();
                        ItemStack itemToAbsorb = STELLAR_FUEL;
                        if (isUpgradeActive(END) && internalBattery != 0) {
                            itemToAbsorb = GTOreDictUnificator.get(OrePrefixes.gem, MaterialsUEVplus.GravitonShard, 1);
                        }
                        if (inputBusInventory != null) {
                            for (int i = 0; i < inputBusInventory.length; i++) {
                                ItemStack itemStack = inputBusInventory[i];
                                if (itemStack != null && itemStack.isItemEqual(itemToAbsorb)) {
                                    int stacksize = itemStack.stackSize;
                                    if (inputBus instanceof MTEHatchInputBusME meBus) {
                                        ItemStack realItem = meBus.getStackInSlot(i);
                                        if (realItem == null) {
                                            break;
                                        }
                                        stacksize = realItem.stackSize;
                                    }
                                    inputBus.decrStackSize(i, stacksize);
                                    if (internalBattery == 0) {
                                        stellarFuelAmount += stacksize;
                                    } else {
                                        gravitonShardsAvailable += stacksize;
                                        gravitonShardsSpent -= stacksize;
                                    }
                                    inputBus.updateSlots();
                                }
                            }
                        }
                        if (internalBattery == 0) {
                            neededStartupFuel = calculateStartupFuelConsumption(this);
                            if (stellarFuelAmount >= neededStartupFuel) {
                                stellarFuelAmount -= neededStartupFuel;
                                increaseBattery(neededStartupFuel);
                                if (!isRendererDisabled) createRenderer();
                            }
                        }
                    }
                }

                if (internalBattery != 0) {
                    drainFuel();
                }

                determineCompositionMilestoneLevel();
                determineMilestoneProgress();
                checkInversionStatus();
                if (!ConfigHandler.debug.DEBUG_MODE) {
                    determineGravitonShardAmount();
                }
                if (isUpgradeActive(END) && gravitonShardEjection) {
                    ejectGravitonShards();
                }

                // Do module calculations and checks
                if (!moduleHatches.isEmpty() && internalBattery > 0 && moduleHatches.size() <= maxModuleCount) {
                    for (MTEBaseModule module : moduleHatches) {
                        if (allowModuleConnection(module, this)) {
                            module.connect();
                            calculateMaxHeatForModules(module, this);
                            calculateSpeedBonusForModules(module, this);
                            calculateMaxParallelForModules(module, this);
                            calculateEnergyDiscountForModules(module, this);
                            setMiscModuleParameters(module, this);
                            queryMilestoneStats(module, this);
                            if (!isUpgradeActive(TBF)) {
                                calculateProcessingVoltageForModules(module, this);
                            }
                            if (factorChangeDuringRecipeAntiCheese(module)) {
                                module.disconnect();
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

            if (ticker % SOUND_LOOP_LENGTH == 0 && isRenderActive) {
                sendLoopStart((byte) 1);
            }
        }
    }

    private void drainFuel() {
        fuelConsumption = (long) Math.max(calculateFuelConsumption(this) * 5 * (batteryCharging ? 2 : 1), 1);
        if (fuelConsumption >= Integer.MAX_VALUE) {
            reduceBattery(fuelConsumptionFactor);
            return;
        }

        FluidStack fuelToDrain = new FluidStack(validFuelList.get(selectedFuelType), (int) fuelConsumption);
        for (MTEHatchInput hatch : filterValidMTEs(mInputHatches)) {
            FluidStack drained = hatch.drain(ForgeDirection.UNKNOWN, fuelToDrain, true);
            if (drained == null) {
                continue;
            }

            fuelToDrain.amount -= drained.amount;

            if (fuelToDrain.amount == 0) {
                totalFuelConsumed += getFuelFactor();
                if (batteryCharging) {
                    increaseBattery(fuelConsumptionFactor);
                }
                return;
            }
        }
        reduceBattery(fuelConsumptionFactor);
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

    private TileEntityForgeOfGods getRenderer() {
        ChunkCoordinates renderPos = getRenderPos();
        TileEntity tile = this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity(renderPos.posX, renderPos.posY, renderPos.posZ);

        if (tile instanceof TileEntityForgeOfGods forgeTile) {
            return forgeTile;
        }
        return null;
    }

    private void updateRenderer() {
        TileEntityForgeOfGods tile = getRenderer();
        if (tile == null) return;

        tile.setRingCount(ringAmount);
        tile.setStarRadius(starSize);
        tile.setRotationSpeed(rotationSpeed);
        tile.setColor(starColors.getByName(selectedStarColor));

        tile.updateToClient();
    }

    private void createRenderer() {
        ChunkCoordinates renderPos = getRenderPos();

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, forgeOfGodsRenderBlock);
        TileEntityForgeOfGods rendererTileEntity = (TileEntityForgeOfGods) this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity(renderPos.posX, renderPos.posY, renderPos.posZ);

        boolean wasStructureCheckEnabled = RunnableMachineUpdate.isEnabled();
        RunnableMachineUpdate.setEnabled(false);

        switch (ringAmount) {
            case 2 -> {
                destroyFirstRing();
                destroySecondRing();
            }
            case 3 -> {
                destroyFirstRing();
                destroySecondRing();
                destroyThirdRing();
            }
            default -> destroyFirstRing();
        }

        RunnableMachineUpdate.setEnabled(wasStructureCheckEnabled);

        rendererTileEntity.setRenderRotation(getRotation(), getDirection());
        updateRenderer();

        isRenderActive = true;
        enableWorking();
    }

    private void destroyRenderer() {
        ChunkCoordinates renderPos = getRenderPos();
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(renderPos.posX, renderPos.posY, renderPos.posZ, Blocks.air);

        boolean wasStructureCheckEnabled = RunnableMachineUpdate.isEnabled();
        RunnableMachineUpdate.setEnabled(false);

        switch (ringAmount) {
            case 2 -> {
                buildFirstRing();
                buildSecondRing();
            }
            case 3 -> {
                buildFirstRing();
                buildSecondRing();
                buildThirdRing();
            }
            default -> buildFirstRing();
        }

        RunnableMachineUpdate.setEnabled(wasStructureCheckEnabled);

        isRenderActive = false;
        disableWorking();
    }

    private ChunkCoordinates getRenderPos() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();
        double xOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double yOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        double zOffset = 122 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        return new ChunkCoordinates((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));
    }

    private void destroyFirstRing() {
        buildPiece(STRUCTURE_PIECE_FIRST_RING_AIR, null, false, 63, 14, -59);
    }

    private void destroySecondRing() {
        buildPiece(STRUCTURE_PIECE_SECOND_RING_AIR, null, false, 55, 11, -67);
    }

    private void destroyThirdRing() {
        buildPiece(STRUCTURE_PIECE_THIRD_RING_AIR, null, false, 47, 13, -76);
    }

    private void buildFirstRing() {
        buildPiece(STRUCTURE_PIECE_FIRST_RING, null, false, 63, 14, -59);
    }

    private void buildSecondRing() {
        buildPiece(STRUCTURE_PIECE_SECOND_RING, null, false, 55, 11, -67);
    }

    private void buildThirdRing() {
        buildPiece(STRUCTURE_PIECE_THIRD_RING, null, false, 47, 13, -76);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (isRendererDisabled) {
            isRendererDisabled = false;
            // let the renderer automatically rebuild itself as needed through normal logic
        } else {
            isRendererDisabled = true;
            if (isRenderActive) destroyRenderer();
        }
        aPlayer.addChatMessage(
            new ChatComponentText("Animations are now " + (isRendererDisabled ? "disabled" : "enabled") + "."));
    }

    @Override
    public boolean isFlipChangeAllowed() {
        // If the machine is formed, or if rings are taken into the controller
        if (mMachine || isRenderActive) return false;
        return super.isFlipChangeAllowed();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        // If the machine is formed, or if rings are taken into the controller
        if (mMachine || isRenderActive) return false;
        return super.isRotationChangeAllowed();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (isRenderActive) {
            destroyRenderer();
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(SCANNER_INFO_BAR);
        str.add(
            StatCollector
                .translateToLocalFormatted("tt.infodata.fog.rings", "" + EnumChatFormatting.GOLD + ringAmount));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.fog.upgrades.unlocked",
                "" + EnumChatFormatting.GOLD + getTotalActiveUpgrades()));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.fog.connected",
                "" + EnumChatFormatting.GOLD + moduleHatches.size()));
        str.add(SCANNER_INFO_BAR);
        return str.toArray(new String[0]);
    }

    @Override
    public void onRemoval() {
        if (moduleHatches != null && !moduleHatches.isEmpty()) {
            for (MTEBaseModule module : moduleHatches) {
                module.disconnect();
            }
        }
        super.onRemoval();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(UPGRADE_TREE_WINDOW_ID, this::createUpgradeTreeWindow);
        buildContext.addSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID, this::createIndividualUpgradeWindow);
        buildContext.addSyncedWindow(FUEL_CONFIG_WINDOW_ID, this::createFuelConfigWindow);
        buildContext.addSyncedWindow(BATTERY_CONFIG_WINDOW_ID, this::createBatteryWindow);
        buildContext.addSyncedWindow(MILESTONE_WINDOW_ID, this::createMilestoneWindow);
        buildContext.addSyncedWindow(INDIVIDUAL_MILESTONE_WINDOW_ID, this::createIndividualMilestoneWindow);
        buildContext.addSyncedWindow(MANUAL_INSERTION_WINDOW_ID, this::createManualInsertionWindow);
        buildContext.addSyncedWindow(GENERAL_INFO_WINDOW_ID, this::createGeneralInfoWindow);
        buildContext.addSyncedWindow(SPECIAL_THANKS_WINDOW_ID, this::createSpecialThanksWindow);
        buildContext.addSyncedWindow(STAR_COSMETICS_WINDOW_ID, this::createStarCosmeticsWindow);
        buildContext.addSyncedWindow(STAR_CUSTOM_COLOR_WINDOW_ID, this::createStarCustomColorWindow);
        buildContext.addSyncedWindow(STAR_CUSTOM_COLOR_IMPORT_WINDOW_ID, this::createStarColorImportWindow);
        buildContext.addSyncedWindow(STATISTICS_WINDOW_ID, this::createStatisticsWindow);

        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                .setPos(4, 4)
                .setSize(190, 85))
            .widget(
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
                    .addTooltip(translateToLocal("fog.button.upgradetree.tooltip"))
                    .setPos(174, 167)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK_16x8)
                    .setPos(174, 183)
                    .setSize(16, 8))
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
                TextWidget.dynamicText(this::storedFuelHeaderText)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setPos(6, 8)
                    .setSize(185, 10))
            .widget(
                TextWidget.dynamicText(this::storedFuel)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setPos(6, 20)
                    .setSize(185, 10))
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
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(SPECIAL_THANKS_WINDOW_ID);
                    })
                    .setSize(16, 16)
                    .addTooltip(translateToLocal("fog.button.thanks.tooltip"))
                    .setBackground(TecTechUITextures.OVERLAY_BUTTON_HEART)
                    .setPos(8, 69)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new ButtonWidget().setOnClick(
                    (clickData, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(STAR_COSMETICS_WINDOW_ID);
                    })
                    .addTooltip(translateToLocal("fog.button.color.tooltip"))
                    .setBackground(() -> {
                        List<UITexture> button = new ArrayList<>();
                        button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                        button.add(TecTechUITextures.OVERLAY_BUTTON_RAINBOW_SPIRAL);
                        return button.toArray(new IDrawable[0]);
                    })
                    .setPos(174, 148)
                    .setSize(16, 16)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(STATISTICS_WINDOW_ID);
                }
            })
                .addTooltip(translateToLocal("fog.button.statistics.tooltip"))
                .setBackground(() -> {
                    List<UITexture> button = new ArrayList<>();
                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                    button.add(TecTechUITextures.OVERLAY_BUTTON_STATISTICS);
                    return button.toArray(new IDrawable[0]);
                })
                .setPos(26, 91)
                .setSize(16, 16)
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(172, 67));
    }

    protected ButtonWidget createEjectionSwitch(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            gravitonShardEjection = !gravitonShardEjection;
        })
            .setBackground(
                () -> new UITexture[] { TecTechUITextures.BUTTON_CELESTIAL_32x32,
                    gravitonShardEjection ? TecTechUITextures.OVERLAY_EJECTION_ON
                        : TecTechUITextures.OVERLAY_EJECTION_LOCKED })
            .addTooltip(translateToLocal("fog.button.ejection.tooltip"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> gravitonShardEjection, val -> gravitonShardEjection = val),
                builder)
            .setPos(44, 91)
            .setSize(16, 16)
            .setEnabled($ -> isUpgradeActive(END))
            .attachSyncer(upgrades.getSyncer(END), builder);
        return (ButtonWidget) button;
    }

    protected Widget createBatteryButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (clickData.mouseButton == 0) {
                batteryCharging = !batteryCharging;
            } else if (clickData.mouseButton == 1 && !widget.isClient() && isUpgradeActive(REC)) {
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
        final int w = 78;
        final int h = 130;
        final int parentW = getGUIWidth();
        final int parentH = getGUIHeight();

        ModularWindow.Builder builder = ModularWindow.builder(w, h);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(parentW, parentH))
                .add(
                    Alignment.TopRight.getAlignedPos(new Size(parentW, parentH), new Size(w, h))
                        .add(w - 3, 0)));

        // Window header
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelconsumption")
                .setPos(3, 2)
                .setSize(74, 34));

        // Fuel factor textbox
        NumericWidget fuelFactor = new NumericWidget();
        fuelFactor.setSetter(val -> fuelConsumptionFactor = (int) val)
            .setGetter(() -> fuelConsumptionFactor)
            .setBounds(1, calculateMaxFuelFactor(this))
            .setDefaultValue(1)
            .setScrollValues(1, 4, 64)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .setSize(70, 18)
            .setPos(4, 35)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);
        builder.widget(fuelFactor);

        // Syncers for max fuel factor
        builder.widget(
            upgrades.getSyncer(CFCE)
                .setOnClientUpdate($ -> fuelFactor.setMaxValue(calculateMaxFuelFactor(this))));
        builder.widget(
            upgrades.getSyncer(GEM)
                .setOnClientUpdate($ -> fuelFactor.setMaxValue(calculateMaxFuelFactor(this))));
        builder.widget(
            upgrades.getSyncer(TSE)
                .setOnClientUpdate($ -> fuelFactor.setMaxValue(calculateMaxFuelFactor(this))));

        builder.widget(
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
                    .setSize(18, 18))
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
                    .setOnClick((data, widget) -> {
                        if (!widget.isClient()) {
                            widget.getWindow()
                                .closeWindow();
                            ForgeOfGodsUI.closeWindow(widget, INDIVIDUAL_MILESTONE_WINDOW_ID);
                        }
                    })
                    .setPos(382, 6));
        return builder.build();
    }

    protected ModularWindow createIndividualMilestoneWindow(final EntityPlayer player) {
        final int w = 150;
        final int h = 150;
        final MilestoneIcon icon = MilestoneIcon.VALUES[currentMilestoneID];
        final Size iconSize = icon.getSize();

        ModularWindow.Builder builder = ModularWindow.builder(w, h);
        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE);
        builder.setDraggable(true);

        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(134, 4))
            .widget(
                new DrawableWidget().setDrawable(icon.getSymbol())
                    .setSize(iconSize)
                    .setPos((w - iconSize.width) / 2, (h - iconSize.height) / 2))
            .widget(
                new TextWidget(icon.getNameText()).setDefaultColor(EnumChatFormatting.GOLD)
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
                TextWidget.dynamicText(() -> currentMilestoneLevel(currentMilestoneID))
                    .setScale(0.7f)
                    .setDefaultColor(EnumChatFormatting.WHITE)
                    .setTextAlignment(Alignment.Center)
                    .setPos(5, 50)
                    .setSize(140, 30))
            .widget(
                TextWidget.dynamicText(() -> milestoneProgressText(currentMilestoneID))
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
                    formattingMode = formattingMode.cycle();
                }
            })
                .setSize(10, 10)
                .addTooltip(translateToLocal("fog.button.formatting.tooltip"))
                .setBackground(TecTechUITextures.OVERLAY_CYCLIC_BLUE)
                .setPos(5, 135)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .attachSyncer(
                    new FakeSyncWidget.ByteSyncer(
                        () -> (byte) formattingMode.ordinal(),
                        val -> formattingMode = MilestoneFormatter.VALUES[MathHelper
                            .clamp_int(val, 0, MilestoneFormatter.VALUES.length - 1)]),
                    builder));

        return builder.build();
    }

    private int currentMilestoneID = 0;

    private Widget createMilestoneButton(int milestoneID, int width, int height, Pos2d pos) {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            currentMilestoneID = milestoneID;
            ForgeOfGodsUI.reopenWindow(widget, INDIVIDUAL_MILESTONE_WINDOW_ID);
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

    protected ModularWindow createUpgradeTreeWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(300, 300);
        Scrollable scrollable = new Scrollable().setVerticalScroll();

        // spotless:off
        scrollable
            .widget(createUpgradeConnectorLine(new Pos2d(143, 71),  45,  0,      UpgradeColor.BLUE,   START, IGCC))
            .widget(createUpgradeConnectorLine(new Pos2d(124, 124), 60,  27,     UpgradeColor.BLUE,   IGCC,  STEM))
            .widget(createUpgradeConnectorLine(new Pos2d(162, 124), 60,  333,    UpgradeColor.BLUE,   IGCC,  CFCE))
            .widget(createUpgradeConnectorLine(new Pos2d(94,  184), 60,  27,     UpgradeColor.BLUE,   STEM,  GISS))
            .widget(createUpgradeConnectorLine(new Pos2d(130, 184), 60,  336,    UpgradeColor.BLUE,   STEM,  FDIM))
            .widget(createUpgradeConnectorLine(new Pos2d(156, 184), 60,  24,     UpgradeColor.BLUE,   CFCE,  FDIM))
            .widget(createUpgradeConnectorLine(new Pos2d(192, 184), 60,  333,    UpgradeColor.BLUE,   CFCE,  SA))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 251), 45,  0,      UpgradeColor.BLUE,   FDIM,  GPCI))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 311), 45,  0,      UpgradeColor.BLUE,   GPCI,  GEM))
            .widget(createUpgradeConnectorLine(new Pos2d(78,  250), 110, 5,      UpgradeColor.RED,    GISS,  REC))
            .widget(createUpgradeConnectorLine(new Pos2d(110, 290), 80,  40,     UpgradeColor.RED,    GPCI,  REC))
            .widget(createUpgradeConnectorLine(new Pos2d(208, 250), 110, 355,    UpgradeColor.RED,    SA,    CTCDD))
            .widget(createUpgradeConnectorLine(new Pos2d(176, 290), 80,  320,    UpgradeColor.RED,    GPCI,  CTCDD))
            .widget(createUpgradeConnectorLine(new Pos2d(100, 355), 80,  313,    UpgradeColor.BLUE,   REC,   QGPIU))
            .widget(createUpgradeConnectorLine(new Pos2d(186, 355), 80,  47,     UpgradeColor.BLUE,   CTCDD, QGPIU))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 430), 48,  0,      UpgradeColor.ORANGE, QGPIU, TCT))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 490), 48,  0,      UpgradeColor.ORANGE, TCT,   EPEC))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 550), 48,  0,      UpgradeColor.ORANGE, EPEC,  POS))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 610), 48,  0,      UpgradeColor.ORANGE, POS,   NGMS))
            .widget(createUpgradeConnectorLine(new Pos2d(110, 410), 80,  40,     UpgradeColor.PURPLE, QGPIU, SEFCP))
            .widget(createUpgradeConnectorLine(new Pos2d(83,  490), 48,  0,      UpgradeColor.PURPLE, SEFCP, CNTI))
            .widget(createUpgradeConnectorLine(new Pos2d(83,  550), 48,  0,      UpgradeColor.PURPLE, CNTI,  NDPE))
            .widget(createUpgradeConnectorLine(new Pos2d(101, 590), 80,  320,    UpgradeColor.PURPLE, NDPE,  NGMS))
            .widget(createUpgradeConnectorLine(new Pos2d(53,  536), 35,  45,     UpgradeColor.PURPLE, CNTI,  DOP))
            .widget(createUpgradeConnectorLine(new Pos2d(176, 410), 80,  320,    UpgradeColor.GREEN,  QGPIU, GGEBE))
            .widget(createUpgradeConnectorLine(new Pos2d(203, 490), 48,  0,      UpgradeColor.GREEN,  GGEBE, IMKG))
            .widget(createUpgradeConnectorLine(new Pos2d(203, 550), 48,  0,      UpgradeColor.GREEN,  IMKG,  DOR))
            .widget(createUpgradeConnectorLine(new Pos2d(185, 590), 80,  40,     UpgradeColor.GREEN,  DOR,   NGMS))
            .widget(createUpgradeConnectorLine(new Pos2d(233, 476), 35,  315,    UpgradeColor.GREEN,  GGEBE, TPTP))
            .widget(createUpgradeConnectorLine(new Pos2d(143, 670), 48,  0,      UpgradeColor.BLUE,   NGMS,  SEDS))
            .widget(createUpgradeConnectorLine(new Pos2d(101, 707), 75,  62.3f,  UpgradeColor.BLUE,   SEDS,  PA))
            .widget(createUpgradeConnectorLine(new Pos2d(53,  772), 78,  0,      UpgradeColor.BLUE,   PA,    CD))
            .widget(createUpgradeConnectorLine(new Pos2d(95,  837), 75,  297.7f, UpgradeColor.BLUE,   CD,    TSE))
            .widget(createUpgradeConnectorLine(new Pos2d(191, 837), 75,  62.3f,  UpgradeColor.BLUE,   TSE,   TBF))
            .widget(createUpgradeConnectorLine(new Pos2d(233, 772), 78,  0,      UpgradeColor.BLUE,   TBF,   EE))
            .widget(createUpgradeConnectorLine(new Pos2d(191, 747), 75,  62.3f,  UpgradeColor.BLUE,   EE,    END));
        // spotless:on

        for (ForgeOfGodsUpgrade upgrade : upgrades.getAllUpgrades()) {
            scrollable.widget(createUpgradeBox(upgrade, scrollable));
        }

        scrollable.widget(
            new MultiChildWidget().addChild(
                new ButtonWidget().setOnClick(((clickData, widget) -> secretUpgrade = !secretUpgrade))
                    .setSize(40, 15)
                    .setBackground(() -> {
                        if (secretUpgrade) {
                            return new IDrawable[] { TecTechUITextures.BUTTON_SPACE_PRESSED_32x16 };
                        }
                        return new IDrawable[0];
                    })
                    .addTooltip(translateToLocal("fog.upgrade.tt.secret"))
                    .setTooltipShowUpDelay(20))
                .addChild(
                    new TextWidget(translateToLocal("fog.upgrade.tt.short.secret")).setScale(0.8f)
                        .setDefaultColor(EnumChatFormatting.GOLD)
                        .setTextAlignment(Alignment.Center)
                        .setSize(34, 9)
                        .setPos(3, 4)
                        .setEnabled((widget -> secretUpgrade)))
                .addChild(
                    new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_UPGRADE_CONNECTOR_BLUE_OPAQUE)
                        .setEnabled(widget -> secretUpgrade)
                        .setPos(40, 4)
                        .setSize(20, 6))
                .setPos(new Pos2d(66, 56)))
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
                    .setOnClick((data, widget) -> {
                        if (!widget.isClient()) {
                            widget.getWindow()
                                .closeWindow();
                            ForgeOfGodsUI.closeWindow(widget, INDIVIDUAL_UPGRADE_WINDOW_ID);
                        }
                    })
                    .setPos(282, 4));
        if (ConfigHandler.debug.DEBUG_MODE) {
            builder.widget(
                new MultiChildWidget()
                    .addChild(
                        new ButtonWidget().setOnClick((clickData, widget) -> upgrades.resetAll())
                            .setSize(40, 15)
                            .setBackground(GTUITextures.BUTTON_STANDARD)
                            .addTooltip(translateToLocal("fog.debug.resetbutton.tooltip"))
                            .setTooltipShowUpDelay(TOOLTIP_DELAY))
                    .addChild(
                        new TextWidget(translateToLocal("fog.debug.resetbutton.text"))
                            .setTextAlignment(Alignment.Center)
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
                        new ButtonWidget().setOnClick((clickData, widget) -> upgrades.unlockAll())
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
        ForgeOfGodsUpgrade upgrade = currentUpgradeWindow;

        ModularWindow.Builder builder = ModularWindow.builder(upgrade.getWindowSize());
        builder.setBackground(upgrade.getBackground());

        // Syncers
        builder.widget(
            new FakeSyncWidget.IntegerSyncer(() -> gravitonShardsAvailable, val -> gravitonShardsAvailable = val));
        builder.widget(upgrades.getSyncer(upgrade));

        builder.widget(
            ForgeOfGodsUI.getIndividualUpgradeGroup(
                upgrade,
                () -> gravitonShardsAvailable,
                () -> completeUpgrade(upgrade),
                () -> respecUpgrade(upgrade),
                () -> isUpgradeActive(upgrade),
                () -> formattingMode));

        if (upgrade.hasExtraCost()) {
            builder.widget(
                ForgeOfGodsUI
                    .createMaterialInputButton(upgrade, () -> upgrades.isCostPaid(upgrade), (clickData, widget) -> {
                        ForgeOfGodsUI.reopenWindow(widget, MANUAL_INSERTION_WINDOW_ID);
                        ForgeOfGodsUI.closeWindow(widget, INDIVIDUAL_UPGRADE_WINDOW_ID);
                        ForgeOfGodsUI.closeWindow(widget, UPGRADE_TREE_WINDOW_ID);
                    }));
        }

        return builder.build();
    }

    // todo ringAmount is not client synced, causing a UI desync when unlocking split upgrades
    private void completeUpgrade(ForgeOfGodsUpgrade upgrade) {
        if (isUpgradeActive(upgrade)) return;
        if (!upgrades.checkPrerequisites(upgrade)) return;
        if (!upgrades.checkSplit(upgrade, ringAmount)) return;
        if (!upgrades.checkCost(upgrade, gravitonShardsAvailable)) return;

        upgrades.unlockUpgrade(upgrade);
        gravitonShardsAvailable -= upgrade.getShardCost();
        gravitonShardsSpent += upgrade.getShardCost();
    }

    private void respecUpgrade(ForgeOfGodsUpgrade upgrade) {
        if (!isUpgradeActive(upgrade)) return;
        if (!upgrades.checkDependents(upgrade)) return;

        upgrades.respecUpgrade(upgrade);
        gravitonShardsAvailable += upgrade.getShardCost();
        gravitonShardsSpent -= upgrade.getShardCost();

        if (upgrade == END) {
            gravitonShardEjection = false;
        }
    }

    private Widget createUpgradeBox(ForgeOfGodsUpgrade upgrade, IWidgetBuilder<?> builder) {
        return new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
            currentUpgradeWindow = upgrade;
            if (clickData.mouseButton == 0) {
                if (clickData.shift) {
                    if (!upgrade.hasExtraCost() || upgrades.isCostPaid(upgrade)) {
                        completeUpgrade(upgrade);
                    } else {
                        ForgeOfGodsUI.reopenWindow(widget, MANUAL_INSERTION_WINDOW_ID);
                        ForgeOfGodsUI.closeWindow(widget, INDIVIDUAL_UPGRADE_WINDOW_ID);
                        ForgeOfGodsUI.closeWindow(widget, UPGRADE_TREE_WINDOW_ID);
                    }
                } else {
                    ForgeOfGodsUI.reopenWindow(widget, INDIVIDUAL_UPGRADE_WINDOW_ID);
                }
            } else if (clickData.mouseButton == 1) {
                respecUpgrade(upgrade);
            }
        })
            .setSize(40, 15)
            .setBackground(() -> {
                if (isUpgradeActive(upgrade)) {
                    return new IDrawable[] { TecTechUITextures.BUTTON_SPACE_PRESSED_32x16 };
                } else {
                    return new IDrawable[] { TecTechUITextures.BUTTON_SPACE_32x16 };
                }
            })
            .addTooltip(upgrade.getNameText())
            .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .addChild(
                new TextWidget(upgrade.getShortNameText()).setScale(0.8f)
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setSize(34, 9)
                    .setPos(3, 4))
            .setPos(upgrade.getTreePos())
            .attachSyncer(upgrades.getSyncer(upgrade), builder);
    }

    private Widget createUpgradeConnectorLine(Pos2d pos, int length, float rotationAngle, UpgradeColor color,
        ForgeOfGodsUpgrade startUpgrade, ForgeOfGodsUpgrade endUpgrade) {
        return new DrawableWidget().setDrawable(() -> {
            UITexture texture = color.getConnector();
            if (isUpgradeActive(startUpgrade) && isUpgradeActive(endUpgrade)) {
                texture = color.getOpaqueConnector();
            }
            return texture.withRotationDegree(rotationAngle);
        })
            .setPos(pos)
            .setSize(6, length);
    }

    protected ModularWindow createManualInsertionWindow(final EntityPlayer player) {
        ForgeOfGodsUpgrade upgrade = currentUpgradeWindow;
        ItemStack[] inputs = upgrade.getExtraCost();
        final int WIDTH = 189;
        final int HEIGHT = 106;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();

        for (int i = 0; i < 16; i++) {
            inputSlotHandler.insertItem(i, storedUpgradeWindowItems[i], false);
            storedUpgradeWindowItems[i] = null;
        }

        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT)))
                .subtract(5, 0)
                .add(0, 4));
        builder.widget(upgrades.getSyncer(upgrade));
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
                ForgeOfGodsUI.reopenWindow(widget, UPGRADE_TREE_WINDOW_ID);
                ForgeOfGodsUI.reopenWindow(widget, INDIVIDUAL_UPGRADE_WINDOW_ID);
            }
        })
            .setBackground(ModularUITextures.VANILLA_BACKGROUND, new Text("x"))
            .setPos(179, 0)
            .setSize(10, 10));
        builder.widget(new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                upgrades.payCost(upgrade, inputSlotHandler);
                ForgeOfGodsUI.reopenWindow(widget, MANUAL_INSERTION_WINDOW_ID);
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

        for (int i = 0; i < 12; i++) {
            final int ii = i;
            ItemStack stack = null;
            if (i < inputs.length) {
                stack = inputs[i];
            }
            Widget costWidget = ForgeOfGodsUI.createExtraCostWidget(stack, () -> upgrades.getPaidCosts(upgrade)[ii]);
            costWidget.setPos(5 + (36 * (i / 4)), 6 + (18 * (i % 4)));
            builder.widget(costWidget);
        }

        return builder.build();
    }

    protected ModularWindow createGeneralInfoWindow(final EntityPlayer player) {
        return ForgeOfGodsUI.createGeneralInfoWindow(() -> inversion, val -> inversion = val);
    }

    protected ModularWindow createSpecialThanksWindow(final EntityPlayer player) {
        return ForgeOfGodsUI.createSpecialThanksWindow();
    }

    protected ModularWindow createStarCosmeticsWindow(final EntityPlayer player) {
        final int WIDTH = 200;
        final int HEIGHT = 200;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE);
        builder.setDraggable(true);

        // Syncers
        builder.widget(new FakeSyncWidget.StringSyncer(() -> selectedStarColor, val -> selectedStarColor = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> editingStarIndex, val -> editingStarIndex = val));
        builder.widget(starColors.getSyncer());

        // Exit button and header
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(184, 4))
            .widget(
                new TextWidget(translateToLocal("fog.cosmetics.header")).setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setScale(1f)
                    .setPos(0, 5)
                    .setSize(200, 15));

        // Color options
        // Header
        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + translateToLocal("fog.cosmetics.color"))
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(9, 25)
                .setSize(60, 10));

        // Current presets
        for (int i = 0; i < 7; i++) {
            MultiChildWidget widget = createColorGroup(i);
            widget.setPos(8, 45 + i * 20);
            builder.widget(widget);
        }

        // Option to add a new preset, only shown if there is room available
        MultiChildWidget newPreset = new MultiChildWidget();
        Function<Widget, Boolean> newPresetEnabled = $ -> !starColors.isFull();
        newPreset.setSize(18, 80);
        newPreset.setPosProvider(($, $$, $$$) -> new Pos2d(8, 45 + starColors.size() * 20));
        newPreset.setEnabled(newPresetEnabled);

        // New preset button
        newPreset.addChild(new ButtonWidget().setOnClick((data, widget) -> {
            if (!widget.isClient()) {
                editingStarIndex = -1;
                openCustomStarColorWindowFresh(widget, null);
            }
        })
            .setPlayClickSound(true)
            .setBackground(GTUITextures.BUTTON_STANDARD)
            .addTooltip(translateToLocal("fog.cosmetics.starcolor"))
            .setSize(16, 16)
            .setPos(0, 0)
            .setEnabled(newPresetEnabled));

        // Text overlaid on the above button
        newPreset.addChild(
            new TextWidget("+").setDefaultColor(EnumChatFormatting.DARK_GRAY)
                .setTextAlignment(Alignment.Center)
                .setSize(16, 16)
                .setPos(0, 0)
                .setEnabled(newPresetEnabled));

        // Text for what this button does
        newPreset.addChild(
            new TextWidget(translateToLocal("fog.cosmetics.customstarcolor")).setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setSize(60, 18)
                .setPos(20, 0)
                .setEnabled(newPresetEnabled));

        builder.widget(newPreset);

        // Misc Section

        // Header
        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + translateToLocal("fog.cosmetics.misc"))
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(120, 25)
                .setSize(80, 10));

        // Spin Textbox
        builder.widget(
            TextWidget.localised("fog.cosmetics.spin")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(120, 45)
                .setSize(60, 18));

        NumericWidget rotationWidget = new NumericWidget();
        rotationWidget.setGetter(() -> rotationSpeed)
            .setSetter(val -> {
                if (rotationSpeed != (int) val) {
                    rotationSpeed = (int) val;
                    if (!rotationWidget.isClient()) {
                        updateRenderer();
                    }
                }
            })
            .setBounds(0, 100)
            .setDefaultValue(5)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .setSize(35, 18)
            .setPos(155, 45)
            .addTooltip(translateToLocal("fog.cosmetics.onlyintegers"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);
        builder.widget(rotationWidget);

        // Size Textbox
        builder.widget(
            TextWidget.localised("fog.cosmetics.size")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(120, 65)
                .setSize(60, 18));

        NumericWidget spinWidget = new NumericWidget();
        spinWidget.setGetter(() -> starSize)
            .setSetter(val -> {
                if (starSize != (int) val) {
                    starSize = (int) val;
                    if (!spinWidget.isClient()) {
                        updateRenderer();
                    }
                }
            })
            .setBounds(1, 40)
            .setDefaultValue(20)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .setSize(35, 18)
            .setPos(155, 65)
            .addTooltip(translateToLocal("fog.cosmetics.onlyintegers"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);
        builder.widget(spinWidget);

        // Animations Textbox
        builder.widget(
            TextWidget.localised("fog.cosmetics.animations")
                .setDefaultColor(EnumChatFormatting.GOLD)
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(120, 85)
                .setSize(60, 18));

        Widget animationToggle = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (isRendererDisabled) {
                isRendererDisabled = false;
                // let the renderer automatically rebuild itself as needed through normal logic
            } else {
                isRendererDisabled = true;
                if (isRenderActive) destroyRenderer();
            }
        })
            .setBackground(
                () -> new UITexture[] { TecTechUITextures.BUTTON_CELESTIAL_32x32,
                    isRendererDisabled ? TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED
                        : TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON })
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isRendererDisabled, val -> isRendererDisabled = val),
                builder)
            .setPos(174, 86)
            .setSize(16, 16);
        builder.widget(animationToggle);

        return builder.build();
    }

    private MultiChildWidget createColorGroup(final int index) {
        final int textWidth = 60; // for convenient changing, if we need to make this wider

        MultiChildWidget parent = new MultiChildWidget();
        parent.setSize(18, 20 + textWidth);
        Function<Widget, Boolean> enabledCheck = $ -> index < starColors.size();

        // Button to enable this star color
        parent.addChild(new ButtonWidget().setOnClick((data, widget) -> {
            if (!widget.isClient()) {
                if (index < starColors.size()) {
                    ForgeOfGodsStarColor color = starColors.getByIndex(index);
                    if (data.shift && !color.isPresetColor()) {
                        // if shift is held, open color editor for this preset, if not a default preset
                        editingStarIndex = index;
                        openCustomStarColorWindowFresh(widget, color);
                    } else {
                        // otherwise select this color
                        selectedStarColor = color.getName();
                        updateRenderer();
                    }
                }
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                IDrawable bg = GTUITextures.BUTTON_STANDARD;
                if (index < starColors.size()) {
                    ForgeOfGodsStarColor color = starColors.getByIndex(index);
                    if (color.getName()
                        .equals(selectedStarColor)) {
                        bg = GTUITextures.BUTTON_STANDARD_PRESSED;
                    }
                }
                return new IDrawable[] { bg };
            })
            .addTooltips(
                ImmutableList.of(
                    translateToLocal("fog.cosmetics.selectcolor.tooltip.1"),
                    translateToLocal("fog.cosmetics.selectcolor.tooltip.2")))
            .setSize(16, 16)
            .setPos(0, 0)
            .setEnabled(enabledCheck));

        // Drawable representation of this star color, overlaid on the above button
        parent.addChild(new DrawableWidget().setDrawable(() -> {
            if (index < starColors.size()) {
                return starColors.getByIndex(index)
                    .getDrawable();
            }
            return IDrawable.EMPTY;
        })
            .setPos(1, 1)
            .setSize(14, 14)
            .setEnabled(enabledCheck));

        // Name of this star color
        parent.addChild(new DynamicTextWidget(() -> {
            if (index < starColors.size()) {
                ForgeOfGodsStarColor color = starColors.getByIndex(index);
                return new Text(color.getLocalizedName());
            }
            return Text.EMPTY;
        }).setDefaultColor(ForgeOfGodsUI.GOLD_ARGB)
            .setTextAlignment(Alignment.CenterLeft)
            .setSize(textWidth, 18)
            .setPos(20, 0)
            .setEnabled(enabledCheck));

        return parent;
    }

    protected ModularWindow createStarCustomColorWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 200);
        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE);
        builder.setDraggable(true);

        // Syncers
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> starColorR, val -> starColorR = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> starColorG, val -> starColorG = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> starColorB, val -> starColorB = val));
        builder.widget(new FakeSyncWidget.FloatSyncer(() -> starGamma, val -> starGamma = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> editingColorIndex, val -> editingColorIndex = val));

        builder.widget(
            new FakeSyncWidget<>(
                () -> newStarColor,
                val -> newStarColor = val,
                ForgeOfGodsStarColor::writeToBuffer,
                ForgeOfGodsStarColor::readFromBuffer));

        // Exit button and header
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(184, 4))
            .widget(
                TextWidget.localised("fog.cosmetics.starcolor")
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setScale(1f)
                    .setPos(0, 5)
                    .setSize(200, 15));

        // ***********
        // Color Entry
        // ***********

        // RGB + Gamma text fields
        Widget redField = ForgeOfGodsUI
            .createStarColorRGBMGroup(StarColorRGBM.RED, val -> starColorR = (int) val, () -> starColorR);
        builder.widget(redField.setPos(8, 21));

        Widget greenField = ForgeOfGodsUI
            .createStarColorRGBMGroup(StarColorRGBM.GREEN, val -> starColorG = (int) val, () -> starColorG);
        builder.widget(greenField.setPos(8, 40));

        Widget blueField = ForgeOfGodsUI
            .createStarColorRGBMGroup(StarColorRGBM.BLUE, val -> starColorB = (int) val, () -> starColorB);
        builder.widget(blueField.setPos(8, 59));

        Widget gammaField = ForgeOfGodsUI
            .createStarColorRGBMGroup(StarColorRGBM.GAMMA, val -> starGamma = (float) val, () -> starGamma);
        builder.widget(gammaField.setPos(8, 78));

        // Color preview box
        Widget colorPreviewBox = new DrawableWidget()
            .setDrawable(() -> new Rectangle().setColor(Color.rgb(starColorR, starColorG, starColorB)))
            .setSize(168, 15)
            .setPos(16, 99);
        builder.widget(colorPreviewBox);

        // Apply color button
        Widget colorApplyButton = ForgeOfGodsUI.createStarColorButton(() -> {
            if (editingColorIndex >= 0) {
                return "fog.cosmetics.applycolor";
            }
            return "fog.cosmetics.addcolor";
        }, () -> {
            if (editingColorIndex >= 0) {
                return "fog.cosmetics.applycolor.tooltip";
            }
            return "fog.cosmetics.addcolor.tooltip";
        }, (clickData, widget) -> {
            if (!widget.isClient()) {
                StarColorSetting color = new StarColorSetting(starColorR, starColorG, starColorB, starGamma);
                if (editingColorIndex >= 0 && editingColorIndex < newStarColor.numColors()) {
                    // insert into the list, as we are editing an existing color
                    newStarColor.setColor(editingColorIndex, color);
                } else {
                    // add a new color at the end of the list
                    newStarColor.addColor(color);
                }
                ForgeOfGodsUI.reopenWindow(widget, STAR_CUSTOM_COLOR_WINDOW_ID);
            }
        });
        builder.widget(colorApplyButton.setPos(63, 118));

        // Reset color button
        Widget colorResetButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.resetcolor",
            "fog.cosmetics.resetcolor.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    starColorR = ForgeOfGodsStarColor.DEFAULT_RED;
                    starColorG = ForgeOfGodsStarColor.DEFAULT_GREEN;
                    starColorB = ForgeOfGodsStarColor.DEFAULT_BLUE;
                    starGamma = ForgeOfGodsStarColor.DEFAULT_GAMMA;
                }
            });
        builder.widget(colorResetButton.setPos(102, 118));

        // **********
        // Color List
        // **********

        // Color list
        for (int i = 0; i < 9; i++) {
            final int ii = i;

            MultiChildWidget colorListEntry = new MultiChildWidget();
            colorListEntry.setSize(18, 18);
            colorListEntry.setPos(8 + i * 18, 136);

            // List entry button + selector outline
            colorListEntry.addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (widget.isClient()) return;

                if (clickData.mouseButton == 0) { // left click
                    // deselect this if its already selected
                    if (editingColorIndex == ii) {
                        editingColorIndex = -1;
                        return;
                    }

                    // otherwise select this if it's valid to select
                    if (ii < newStarColor.numColors()) {
                        editingColorIndex = ii;
                        StarColorSetting color = newStarColor.getColor(ii);
                        starColorR = color.getColorR();
                        starColorG = color.getColorG();
                        starColorB = color.getColorB();
                        starGamma = color.getGamma();
                    }
                } else if (clickData.mouseButton == 1) { // right click
                    // if this is valid, remove it and shift other values down
                    if (ii < newStarColor.numColors()) {
                        newStarColor.removeColor(ii);

                        if (editingColorIndex == ii) {
                            // deselect if this index was selected
                            editingColorIndex = -1;
                        } else if (editingColorIndex > ii) {
                            // shift down the editing index if it was after this entry
                            editingColorIndex -= 1;
                        }
                        ForgeOfGodsUI.reopenWindow(widget, STAR_CUSTOM_COLOR_WINDOW_ID);
                    }
                }
            })
                .setPlayClickSound(false)
                .setBackground(() -> {
                    if (editingColorIndex == ii) {
                        return new IDrawable[] { TecTechUITextures.UNSELECTED_OPTION,
                            TecTechUITextures.SLOT_OUTLINE_GREEN };
                    }
                    return new IDrawable[] { TecTechUITextures.UNSELECTED_OPTION };
                })
                .dynamicTooltip(() -> {
                    List<String> ret = new ArrayList<>();
                    ret.add(translateToLocal("fog.cosmetics.colorlist.tooltip.1"));
                    ret.add(translateToLocal("fog.cosmetics.colorlist.tooltip.2"));

                    if (ii < newStarColor.numColors()) {
                        ret.add("");
                        StarColorSetting color = newStarColor.getColor(ii);
                        ret.add(StarColorRGBM.RED.tooltip(color.getColorR()));
                        ret.add(StarColorRGBM.GREEN.tooltip(color.getColorG()));
                        ret.add(StarColorRGBM.BLUE.tooltip(color.getColorB()));
                        ret.add(StarColorRGBM.GAMMA.tooltip(color.getGamma()));
                    }
                    return ret;
                })
                .setUpdateTooltipEveryTick(true)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setSize(18, 18)
                .setPos(0, 0));

            // List entry color
            colorListEntry.addChild(new DrawableWidget().setDrawable(() -> {
                if (ii < newStarColor.numColors()) {
                    StarColorSetting color = newStarColor.getColor(ii);
                    return new Rectangle().setColor(Color.rgb(color.getColorR(), color.getColorG(), color.getColorB()));
                }
                return IDrawable.EMPTY;
            })
                .setSize(16, 16)
                .setPos(1, 1));

            builder.widget(colorListEntry);
        }

        // Cycle rate text field
        Widget cycleRateField = new NumericWidget().setSetter(val -> newStarColor.setCycleSpeed((int) val))
            .setGetter(() -> newStarColor.getCycleSpeed())
            .setBounds(1, 100)
            .setDefaultValue(1)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .addTooltip(translateToLocal("fog.cosmetics.cyclespeed"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
            .setSize(21, 16)
            .setPos(171, 137);
        builder.widget(cycleRateField);

        // Name text field
        Widget nameEntryField = new TextFieldWidget().setSetter(val -> newStarColor.setName(val))
            .setGetter(() -> newStarColor.getName())
            .setMaxLength(15)
            .setTextAlignment(Alignment.CenterLeft)
            .setTextColor(ForgeOfGodsUI.GOLD_ARGB)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
            .addTooltips(
                ImmutableList.of(
                    translateToLocal("fog.cosmetics.starcolorname.tooltip.1"),
                    translateToLocal("fog.cosmetics.starcolorname.tooltip.2")))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(101, 158)
            .setSize(91, 16);
        builder.widget(nameEntryField);

        // Name label
        Widget nameLabel = TextWidget.localised("fog.cosmetics.starcolorname")
            .setTextAlignment(Alignment.CenterLeft)
            .setDefaultColor(EnumChatFormatting.GOLD)
            .setPos(8, 158)
            .setSize(100, 16);
        builder.widget(nameLabel);

        // **************
        // Preset Buttons
        // **************

        // Save preset button
        Widget savePresetButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.savecolors",
            "fog.cosmetics.savecolors.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    if (newStarColor.numColors() == 0) return;
                    if (editingStarIndex >= 0) {
                        starColors.insert(newStarColor, editingStarIndex);
                        selectedStarColor = newStarColor.getName();
                        updateRenderer();
                    } else {
                        starColors.store(newStarColor);
                    }
                    widget.getWindow()
                        .closeWindow();
                    ForgeOfGodsUI.reopenWindow(widget, STAR_COSMETICS_WINDOW_ID);
                }
            });
        builder.widget(savePresetButton.setPos(138, 177));

        // Delete preset button
        Widget deletePresetButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.deletecolors",
            "fog.cosmetics.deletecolors.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    starColors.drop(newStarColor);
                    if (selectedStarColor.equals(newStarColor.getName())) {
                        // set to default if the deleted color was selected
                        selectedStarColor = ForgeOfGodsStarColor.DEFAULT.getName();
                        updateRenderer();
                    }
                    widget.getWindow()
                        .closeWindow();
                    ForgeOfGodsUI.reopenWindow(widget, STAR_COSMETICS_WINDOW_ID);
                }
            });
        builder.widget(deletePresetButton.setPos(101, 177));

        // Import preset button
        Widget importPresetButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.importcolors",
            "fog.cosmetics.importcolors.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    // reset state from before if it exists
                    importedStarColor = null;
                    widget.getContext()
                        .openSyncedWindow(STAR_CUSTOM_COLOR_IMPORT_WINDOW_ID);
                }
            });
        builder.widget(importPresetButton.setPos(64, 177));

        // Export preset button
        Widget exportPresetButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.exportcolors",
            "fog.cosmetics.exportcolors.tooltip",
            (clickData, widget) -> {
                if (widget.isClient()) {
                    if (newStarColor.numColors() == 0) return;
                    if (Desktop.isDesktopSupported()) {
                        String output = newStarColor.serializeToString();
                        Clipboard clipboard = Toolkit.getDefaultToolkit()
                            .getSystemClipboard();
                        clipboard.setContents(new StringSelection(output), null);
                    }
                }
            });
        builder.widget(exportPresetButton.setPos(27, 177));

        return builder.build();
    }

    protected ModularWindow createStarColorImportWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 100);
        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE_HALF);
        builder.setDraggable(true);

        // Syncers
        builder.widget(
            new FakeSyncWidget<>(
                () -> importedStarColor,
                val -> importedStarColor = val,
                ForgeOfGodsStarColor::writeToBuffer,
                ForgeOfGodsStarColor::readFromBuffer));

        // Exit button and header
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(184, 4))
            .widget(
                TextWidget.localised("fog.cosmetics.importer.import")
                    .setDefaultColor(EnumChatFormatting.GOLD)
                    .setTextAlignment(Alignment.Center)
                    .setScale(1f)
                    .setPos(0, 5)
                    .setSize(200, 15));

        // Serialized star color input
        TextFieldWidget textField = new TextFieldWidget();
        textField.setSynced(false, true)
            .setSetter(val -> {
                if (!textField.isClient()) {
                    if (val == null || val.isEmpty()) {
                        importedStarColor = null;
                        return;
                    }
                    importedStarColor = ForgeOfGodsStarColor.deserialize(val);
                }
            })
            .setMaxLength(32767)
            .setScrollBar()
            .setTextAlignment(Alignment.CenterLeft)
            .setTextColor(Color.WHITE.normal)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
            .setSize(184, 18)
            .setPos(8, 20);
        builder.widget(textField);

        // Color preview for a valid star color string
        for (int i = 0; i < 9; i++) {
            int ii = i;
            Widget colorEntry = new DrawableWidget().setDrawable(() -> {
                if (importedStarColor != null && ii < importedStarColor.numColors()) {
                    StarColorSetting color = importedStarColor.getColor(ii);
                    return new Rectangle().setColor(Color.rgb(color.getColorR(), color.getColorG(), color.getColorB()))
                        .withOffset(1, 1, -2, -2);
                }
                return IDrawable.EMPTY;
            })
                .setBackground(TecTechUITextures.UNSELECTED_OPTION)
                .dynamicTooltip(() -> {
                    if (importedStarColor != null && ii < importedStarColor.numColors()) {
                        StarColorSetting color = importedStarColor.getColor(ii);
                        List<String> ret = new ArrayList<>();
                        ret.add(StarColorRGBM.RED.tooltip(color.getColorR()));
                        ret.add(StarColorRGBM.GREEN.tooltip(color.getColorG()));
                        ret.add(StarColorRGBM.BLUE.tooltip(color.getColorB()));
                        ret.add(StarColorRGBM.GAMMA.tooltip(color.getGamma()));
                        return ret;
                    }
                    return Collections.emptyList();
                })
                .setSize(18, 18)
                .setPos(8 + i * 18, 42);
            builder.widget(colorEntry);
        }

        // Cycle rate
        Widget cycleRateText = new DynamicTextWidget(() -> {
            if (importedStarColor != null) {
                return new Text(Integer.toString(importedStarColor.getCycleSpeed()));
            }
            return Text.EMPTY;
        }).setTextAlignment(Alignment.Center)
            .setDefaultColor(Color.WHITE.normal)
            .addTooltip(translateToLocal("fog.cosmetics.cyclespeed"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
            .setSize(21, 16)
            .setPos(171, 43);
        builder.widget(cycleRateText);

        // Validator string
        Widget validatorText = new DynamicTextWidget(() -> {
            if (importedStarColor == null) {
                return new Text(EnumChatFormatting.RED + translateToLocal("fog.cosmetics.importer.error"));
            }
            return new Text(EnumChatFormatting.GREEN + translateToLocal("fog.cosmetics.importer.valid"));
        }).setTextAlignment(Alignment.Center)
            .setPos(0, 62)
            .setSize(200, 16);
        builder.widget(validatorText);

        // Confirm button
        Widget confirmImportButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.importer.apply",
            "fog.cosmetics.importer.apply.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    // no action if a valid star color isn't provided
                    if (importedStarColor != null) {
                        widget.getWindow()
                            .closeWindow();
                        openCustomStarColorWindowFresh(widget, importedStarColor);
                    }
                }
            });
        builder.widget(confirmImportButton.setPos(101, 77));

        // Reset button
        Widget resetImportButton = ForgeOfGodsUI.createStarColorButton(
            "fog.cosmetics.importer.reset",
            "fog.cosmetics.importer.reset.tooltip",
            (clickData, widget) -> {
                if (!widget.isClient()) {
                    importedStarColor = null;
                }
            });
        builder.widget(resetImportButton.setPos(64, 77));

        return builder.build();
    }

    // Opens the custom color window while also resetting the editing values.
    // Can optionally pass a star color to set as the editing color
    private void openCustomStarColorWindowFresh(Widget widget, @Nullable ForgeOfGodsStarColor importedColor) {
        if (!widget.isClient()) {
            // Reset star color state
            if (importedColor == null) {
                importedColor = starColors.newTemplateColor();
            }
            newStarColor = importedColor;
            editingColorIndex = -1;
            starColorR = ForgeOfGodsStarColor.DEFAULT_RED;
            starColorG = ForgeOfGodsStarColor.DEFAULT_GREEN;
            starColorB = ForgeOfGodsStarColor.DEFAULT_BLUE;
            starGamma = ForgeOfGodsStarColor.DEFAULT_GAMMA;

            ForgeOfGodsUI.reopenWindow(widget, STAR_CUSTOM_COLOR_WINDOW_ID);
        }
    }

    private int previewFactor = 0;
    private boolean usePreviewValue = false;
    private final List<String> infoTexts = Arrays.asList(new String[28]);

    protected ModularWindow createStatisticsWindow(final EntityPlayer player) {
        final int WIDTH = 300;
        final int HEIGHT = 300;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(TecTechUITextures.BACKGROUND_GLOW_WHITE);
        builder.setDraggable(true);
        usePreviewValue = false;

        builder.widget(
            new TextWidget(EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.modulestats"))
                .setTextAlignment(Alignment.Center)
                .setScale(1f)
                .setPos(0, 8)
                .setSize(300, 15))
            .widget(
                new TextWidget(EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.heat"))
                    .setScale(0.8f)
                    .setTextAlignment(Alignment.Center)
                    .setPos(12, 56)
                    .setSize(69, 30)
                    .addTooltip(translateToLocal("fog.text.tooltip.heat"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.effectiveheat"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(12, 86)
                        .setSize(69, 30)
                        .addTooltip(translateToLocal("fog.text.tooltip.effectiveheat"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.parallel"))
                    .setScale(0.8f)
                    .setTextAlignment(Alignment.Center)
                    .setPos(12, 116)
                    .setSize(69, 30)
                    .addTooltip(translateToLocal("fog.text.tooltip.parallel"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.speedbonus"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(12, 146)
                        .setSize(69, 30)
                        .addTooltip(translateToLocal("fog.text.tooltip.speedbonus"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.energydiscount"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(12, 176)
                        .setSize(69, 30)
                        .addTooltip(translateToLocal("fog.text.tooltip.energydiscount"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.ocdivisor"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(12, 206)
                        .setSize(69, 30)
                        .addTooltip(translateToLocal("fog.text.tooltip.ocdivisor"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.processingvoltage"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(12, 236)
                        .setSize(69, 30)
                        .addTooltip(translateToLocal("fog.text.tooltip.processingvoltage"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.powerforge"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(88, 40)
                        .setSize(40, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.meltingcore"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(141, 40)
                        .setSize(40, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.plasmafab"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(193, 40)
                        .setSize(44, 10))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.exoticizer"))
                        .setScale(0.8f)
                        .setTextAlignment(Alignment.Center)
                        .setPos(246, 43)
                        .setSize(40, 10));

        // Vertical grid lines
        for (int i = 0; i < 4; i++) {
            builder.widget(
                new DrawableWidget().setDrawable(new Rectangle().setColor(Color.rgb(190, 200, 0)))
                    .setSize(1, 227)
                    .setPos(81 + 53 * i, 38));
        }

        // Horizontal grid lines
        for (int i = 0; i < 8; i++) {
            builder.widget(
                new DrawableWidget().setDrawable(new Rectangle().setColor(Color.rgb(0, 170, 170)))
                    .setSize(276, 1)
                    .setPos(12, 55 + 30 * i));
        }

        MTESmeltingModule smelting = new MTESmeltingModule("smelting");
        MTEMoltenModule molten = new MTEMoltenModule("molten");
        MTEPlasmaModule plasma = new MTEPlasmaModule("plasma");
        MTEExoticModule exotic = new MTEExoticModule("exotic");

        builder.widget(
            new FakeSyncWidget.ByteSyncer(
                () -> (byte) formattingMode.ordinal(),
                val -> formattingMode = MilestoneFormatter.VALUES[MathHelper
                    .clamp_int(val, 0, MilestoneFormatter.VALUES.length - 1)]));
        builder
            .widget(new FakeSyncWidget.IntegerSyncer(() -> fuelConsumptionFactor, val -> fuelConsumptionFactor = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> previewFactor, val -> previewFactor = val));
        builder.widget(upgrades.getFullSyncer());

        for (int i = 0; i < 28; i++) {
            calculateRelevantStatInfo(i, smelting, molten, plasma, exotic, fuelConsumptionFactor);

            int finalI = i;
            builder.widget(
                new MultiChildWidget().addChild(
                    new ButtonWidget().setOnClick((clickData, widget) -> {})
                        .setSize(53, 30))
                    .addChild(
                        new DynamicTextWidget(() -> new Text(EnumChatFormatting.GREEN + infoTexts.get(finalI)))
                            .setScale(1f)
                            .setTextAlignment(Alignment.Center)
                            .setSize(53, 30))
                    .setPos(81 + (i % 4) * 53, 55 + (i / 4 * 30)));
        }

        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(284, 4))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                if (clickData.mouseButton == 0) {
                    formattingMode = formattingMode.cycle();
                    for (int i = 0; i < 28; i++) {
                        calculateRelevantStatInfo(
                            i,
                            smelting,
                            molten,
                            plasma,
                            exotic,
                            usePreviewValue ? (previewFactor == 0 ? 1 : previewFactor) : fuelConsumptionFactor);
                    }
                }
            })
                .setSize(20, 20)
                .addTooltip(translateToLocal("fog.button.formatting.tooltip"))
                .setBackground(TecTechUITextures.OVERLAY_CYCLIC_BLUE)
                .setPos(8, 272)
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new TextWidget(
                    EnumChatFormatting.GOLD + translateToLocal("gt.blockmachines.multimachine.FOG.factorpreview"))
                        .setScale(0.9f)
                        .setSize(100, 18)
                        .setPos(80, 270))
            .widget(
                new NumericWidget().setSetter(val -> previewFactor = (int) val)
                    .setGetter(() -> previewFactor)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setDefaultValue(fuelConsumptionFactor)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .addTooltip(translateToLocal("fog.text.tooltip.factorpreview"))
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .setSize(70, 18)
                    .setPos(180, 270)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD))
            .widget(new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                if (clickData.mouseButton == 0) {
                    usePreviewValue = true;
                    for (int i = 0; i < 28; i++) {
                        calculateRelevantStatInfo(
                            i,
                            smelting,
                            molten,
                            plasma,
                            exotic,
                            previewFactor == 0 ? 1 : previewFactor);
                    }
                }
            })
                .addTooltip(translateToLocal("fog.text.tooltip.applysimulationchanges"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setSize(35, 18)
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(0, 0))
                .addChild(
                    new TextWidget(translateToLocal("fog.cosmetics.applycolor")).setScale(1f)
                        .setTextAlignment(Alignment.Center)
                        .setSize(35, 18)
                        .setPos(0, 0))
                .setPos(253, 270)
                .setSize(35, 18));

        return builder.build();
    }

    private void calculateRelevantStatInfo(int index, MTESmeltingModule smelting, MTEMoltenModule molten,
        MTEPlasmaModule plasma, MTEExoticModule exotic, int fuelFactor) {
        MTEBaseModule module;
        switch (index % 4) {
            case 1 -> {
                module = molten;
            }
            case 2 -> {
                module = plasma;
            }
            case 3 -> {
                module = exotic;
            }
            default -> {
                module = smelting;
            }
        }

        int cleanDiv4 = index / 4;
        String relevantInfo;
        switch (cleanDiv4) {
            case 1 -> {
                calculateMaxHeatForModules(module, this, fuelFactor);
                relevantInfo = String.valueOf(formattingMode.format(module.getHeatForOC()));
            }
            case 2 -> {
                calculateMaxParallelForModules(module, this, fuelFactor);
                relevantInfo = String.valueOf(formattingMode.format(module.getMaxParallel()));
            }
            case 3 -> {
                calculateSpeedBonusForModules(module, this);
                relevantInfo = String.valueOf(formatNumbers(module.getSpeedBonus()));
            }
            case 4 -> {
                calculateEnergyDiscountForModules(module, this);
                relevantInfo = String.valueOf(formatNumbers(module.getEnergyDiscount()));
            }
            case 5 -> {
                setMiscModuleParameters(module, this);
                relevantInfo = String.valueOf(formatNumbers(module.getOverclockTimeFactor()));
            }
            case 6 -> {
                calculateProcessingVoltageForModules(module, this, fuelFactor);
                relevantInfo = String.valueOf(MilestoneFormatter.EXPONENT.format((module.getProcessingVoltage())));
            }
            default -> {
                calculateMaxHeatForModules(module, this, fuelFactor);
                relevantInfo = String.valueOf(formattingMode.format(module.getHeat()));
            }
        }

        infoTexts.set(index, relevantInfo);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Stellar Forge")
            .addInfo(EnumChatFormatting.ITALIC + "Also known as Godforge or Gorge for short.")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo("A massive structure harnessing the thermal, gravitational and")
            .addInfo("kinetic energy of a stabilised neutron star for material processing.")
            .addInfo(
                "This multiblock can house " + EnumChatFormatting.RED
                    + "up to 16 modules "
                    + EnumChatFormatting.GRAY
                    + "which utilize the star to energize materials")
            .addInfo("to varying degrees, ranging from regular smelting to matter degeneration.")
            .addInfo("EU requirements for all modules are handled via wireless energy directly.")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo(
                "This multiblock has an " + EnumChatFormatting.GOLD
                    + "extensive upgrade tree "
                    + EnumChatFormatting.GRAY
                    + "which influences all of its functions,")
            .addInfo(
                "such as " + EnumChatFormatting.GOLD
                    + "unlocking new module types"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.GOLD
                    + "increasing heat levels "
                    + EnumChatFormatting.GRAY
                    + "and "
                    + EnumChatFormatting.GOLD
                    + "granting")
            .addInfo(
                EnumChatFormatting.GOLD + "various processing speed bonuses"
                    + EnumChatFormatting.GRAY
                    + ". "
                    + EnumChatFormatting.GRAY
                    + "These upgrades can be unlocked by reaching")
            .addInfo("certain milestones and/or spending materials.")
            .addSeparator(EnumChatFormatting.AQUA, 73)
            .addInfo(
                EnumChatFormatting.GREEN
                    + "Clicking on the logo in the controller gui opens an extensive information window"
                    + EnumChatFormatting.GRAY
                    + ",")
            .addInfo("explaining everything there is to know about this multiblock.")
            .beginStructureBlock(127, 29, 186, false)
            .addStructureInfo("Total blocks needed for the structure with " + getRingText("1", "2", "3") + "rings:")
            .addStructureInfo(
                getRingText("3943", "7279", "11005") + "Transcendentally Amplified Magnetic Confinement Casing")
            .addStructureInfo(getRingText("2818", "4831", "6567") + "Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(getRingText("272", "512", "824") + "Celestial Matter Guidance Casing")
            .addStructureInfo(getRingText("130", "144", "158") + "Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(getRingText("9", "54", "155") + "Spatially Transcendent Gravitational Lens Block")
            .addStructureInfo(
                getRingText("345", "357", "397") + getRingText("Remote", "Medial", "Central")
                    + "Graviton Flow Modulator")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "36" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .addStructureInfoSeparator()
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Hatch")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Output Bus (ME)")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Input Bus")
            .toolTipFinisher(EnumChatFormatting.AQUA, 73);
        return tt;
    }

    private static String getRingText(String oneRing, String twoRings, String threeRings) {
        return EnumChatFormatting.DARK_PURPLE + oneRing
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.DARK_GREEN
            + twoRings
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.AQUA
            + threeRings
            + EnumChatFormatting.GRAY
            + " ";
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

    public boolean isUpgradeActive(ForgeOfGodsUpgrade upgrade) {
        return upgrades.isUpgradeActive(upgrade);
    }

    public int getRingAmount() {
        return ringAmount;
    }

    public int getTotalActiveUpgrades() {
        return upgrades.getTotalActiveUpgrades();
    }

    private Text fuelUsage() {
        return new Text(formattingMode.format(fuelConsumption) + " L/5s");
    }

    private Text storedFuel() {
        if (internalBattery == 0) {
            return new Text(formattingMode.format(stellarFuelAmount) + "/" + formattingMode.format(neededStartupFuel));
        }
        return new Text(formattingMode.format(internalBattery) + "/" + formattingMode.format(maxBatteryCharge));
    }

    private Text storedFuelHeaderText() {
        if (internalBattery == 0) {
            return new Text(translateToLocal("gt.blockmachines.multimachine.FOG.storedstartupfuel"));
        }
        return new Text(translateToLocal("gt.blockmachines.multimachine.FOG.storedfuel"));
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

    public boolean isInversionAvailable() {
        return inversion;
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
                gravitonShardsSpent += 64;
            }
            addOutput(
                GTOreDictUnificator.get(OrePrefixes.gem, MaterialsUEVplus.GravitonShard, gravitonShardsAvailable));
            gravitonShardsSpent += gravitonShardsAvailable;
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
            translateToLocal("gt.blockmachines.multimachine.FOG.shardgain") + ": "
                + EnumChatFormatting.GRAY
                + formattingMode.format(sum));
    }

    private Text totalMilestoneProgress(int milestoneID) {
        Number progress;
        String suffix;
        switch (milestoneID) {
            case 0 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.power");
                progress = totalPowerConsumed;
            }
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
            default -> throw new IllegalArgumentException("Invalid Milestone ID");
        }
        return new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.totalprogress") + ": "
                + EnumChatFormatting.GRAY
                + formattingMode.format(progress)
                + " "
                + suffix);
    }

    private Text currentMilestoneLevel(int milestoneID) {
        int milestoneLevel = inversion ? milestoneProgress[milestoneID] : Math.min(milestoneProgress[milestoneID], 7);
        return new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.milestoneprogress") + ": "
                + EnumChatFormatting.GRAY
                + milestoneLevel);
    }

    private Text milestoneProgressText(int milestoneID) {
        Number max;
        String suffix;
        String progressText = translateToLocal("gt.blockmachines.multimachine.FOG.progress");
        Text done = new Text(
            translateToLocal("gt.blockmachines.multimachine.FOG.milestonecomplete")
                + (formattingMode != DEFAULT_FORMATTING_MODE ? EnumChatFormatting.DARK_RED + "?" : ""));

        if (milestoneProgress[milestoneID] >= 7 && !inversion) {
            return done;
        }

        switch (milestoneID) {
            case 0 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.power");
                if (inversion) {
                    max = POWER_MILESTONE_T7_CONSTANT.multiply(BigInteger.valueOf(milestoneProgress[0] - 5));
                } else {
                    max = BigInteger.valueOf(LongMath.pow(9, milestoneProgress[0]))
                        .multiply(BigInteger.valueOf(LongMath.pow(10, 15)));
                }
            }
            case 1 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.recipes");
                if (inversion) {
                    max = RECIPE_MILESTONE_T7_CONSTANT * (milestoneProgress[1] - 5);
                } else {
                    max = LongMath.pow(4, milestoneProgress[1]) * LongMath.pow(10, 7);
                }
            }
            case 2 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.fuelconsumed");
                if (inversion) {
                    max = FUEL_MILESTONE_T7_CONSTANT * (milestoneProgress[2] - 5);
                } else {
                    max = LongMath.pow(3, milestoneProgress[2]) * LongMath.pow(10, 4);
                }
            }
            case 3 -> {
                suffix = translateToLocal("gt.blockmachines.multimachine.FOG.extensions");
                max = milestoneProgress[3] + 1;
            }
            default -> throw new IllegalArgumentException("Invalid Milestone ID");
        }
        return new Text(progressText + ": " + EnumChatFormatting.GRAY + formattingMode.format(max) + " " + suffix);
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
            if (!moduleHatches.isEmpty()) {
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
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes;
    }

    @Override
    public void setItemNBT(NBTTagCompound NBT) {
        saveGeneralNBT(NBT, false);
        super.saveNBTData(NBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        saveGeneralNBT(NBT, true);

        // Upgrade window stored items
        NBTTagCompound upgradeWindowStorageNBTTag = new NBTTagCompound();
        int storageIndex = 0;
        for (ItemStack itemStack : inputSlotHandler.getStacks()) {
            if (itemStack != null) {
                upgradeWindowStorageNBTTag
                    .setInteger(storageIndex + "stacksizeOfStoredUpgradeItems", itemStack.stackSize);
                NBT.setTag(storageIndex + "storedUpgradeItem", itemStack.writeToNBT(new NBTTagCompound()));
            }
            storageIndex++;
        }
        NBT.setTag("upgradeWindowStorage", upgradeWindowStorageNBTTag);

        // Renderer information
        NBT.setInteger("rotationSpeed", rotationSpeed);
        NBT.setInteger("starSize", starSize);
        NBT.setString("selectedStarColor", selectedStarColor);
        NBT.setInteger("ringAmount", ringAmount);
        NBT.setBoolean("isRenderActive", isRenderActive);
        NBT.setBoolean("isRendererDisabled", isRendererDisabled);

        super.saveNBTData(NBT);
    }

    private void saveGeneralNBT(NBTTagCompound NBT, boolean force) {
        if (force || selectedFuelType != 0) NBT.setInteger("selectedFuelType", selectedFuelType);
        if (force || internalBattery != 0) NBT.setInteger("internalBattery", internalBattery);
        if (force || batteryCharging) NBT.setBoolean("batteryCharging", batteryCharging);
        if (force || gravitonShardsAvailable != 0) NBT.setInteger("gravitonShardsAvailable", gravitonShardsAvailable);
        if (force || gravitonShardsSpent != 0) NBT.setInteger("gravitonShardsSpent", gravitonShardsSpent);
        if (force || totalRecipesProcessed != 0) NBT.setLong("totalRecipesProcessed", totalRecipesProcessed);
        if (force || totalFuelConsumed != 0) NBT.setLong("totalFuelConsumed", totalFuelConsumed);
        if (force || stellarFuelAmount != 0) NBT.setInteger("starFuelStored", stellarFuelAmount);
        if (force || gravitonShardEjection) NBT.setBoolean("gravitonShardEjection", gravitonShardEjection);
        if (force || secretUpgrade) NBT.setBoolean("secretUpgrade", secretUpgrade);
        if (force || inversion) NBT.setBoolean("inversion", inversion);

        // Fields with non-zero defaults
        if (force || fuelConsumptionFactor != DEFAULT_FUEL_CONSUMPTION_FACTOR) {
            NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        }
        if (force || maxBatteryCharge != DEFAULT_MAX_BATTERY_CHARGE) {
            NBT.setInteger("batterySize", maxBatteryCharge);
        }
        if (force || !DEFAULT_TOTAL_POWER.equals(totalPowerConsumed)) {
            NBT.setByteArray("totalPowerConsumed", totalPowerConsumed.toByteArray());
        }
        if (force || formattingMode != DEFAULT_FORMATTING_MODE) {
            NBT.setInteger("formattingMode", formattingMode.ordinal());
        }

        upgrades.serializeToNBT(NBT, force);
        starColors.serializeToNBT(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        selectedFuelType = NBT.getInteger("selectedFuelType");
        internalBattery = NBT.getInteger("internalBattery");
        batteryCharging = NBT.getBoolean("batteryCharging");
        gravitonShardsAvailable = NBT.getInteger("gravitonShardsAvailable");
        gravitonShardsSpent = NBT.getInteger("gravitonShardsSpent");
        totalRecipesProcessed = NBT.getLong("totalRecipesProcessed");
        totalFuelConsumed = NBT.getLong("totalFuelConsumed");
        stellarFuelAmount = NBT.getInteger("starFuelStored");
        gravitonShardEjection = NBT.getBoolean("gravitonShardEjection");
        secretUpgrade = NBT.getBoolean("secretUpgrade");
        inversion = NBT.getBoolean("inversion");

        // Fields with non-zero defaults
        if (NBT.hasKey("fuelConsumptionFactor")) {
            fuelConsumptionFactor = NBT.getInteger("fuelConsumptionFactor");
        }
        if (NBT.hasKey("batterySize")) {
            maxBatteryCharge = NBT.getInteger("batterySize");
        }
        if (NBT.hasKey("totalPowerConsumed")) {
            totalPowerConsumed = new BigInteger(NBT.getByteArray("totalPowerConsumed"));
        }
        if (NBT.hasKey("formattingMode")) {
            int index = MathHelper.clamp_int(NBT.getInteger("formattingMode"), 0, MilestoneFormatter.VALUES.length);
            formattingMode = MilestoneFormatter.VALUES[index];
        }

        // Stored items
        NBTTagCompound tempItemTag = NBT.getCompoundTag("upgradeWindowStorage");
        for (int index = 0; index < 16; index++) {
            int stackSize = tempItemTag.getInteger(index + "stacksizeOfStoredUpgradeItems");
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(NBT.getCompoundTag(index + "storedUpgradeItem"));
            if (itemStack != null) {
                storedUpgradeWindowItems[index] = itemStack.splitStack(stackSize);
            }
        }

        // Renderer information
        if (NBT.hasKey("rotationSpeed")) rotationSpeed = NBT.getInteger("rotationSpeed");
        if (NBT.hasKey("starSize")) starSize = NBT.getInteger("starSize");
        if (NBT.hasKey("selectedStarColor")) selectedStarColor = NBT.getString("selectedStarColor");
        if (NBT.hasKey("ringAmount")) ringAmount = NBT.getInteger("ringAmount");
        isRenderActive = NBT.getBoolean("isRenderActive");
        isRendererDisabled = NBT.getBoolean("isRendererDisabled");

        upgrades.rebuildFromNBT(NBT);
        starColors.rebuildFromNBT(NBT);

        super.loadNBTData(NBT);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public void enableWorking() {
        super.enableWorking();
        sendLoopStart((byte) 1);
    }

    @Override
    public void disableWorking() {
        super.disableWorking();
        sendLoopEnd((byte) 1);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_GOD_FORGE_LOOP;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.GT_MACHINES_GOD_FORGE_LOOP, 22, 1.0F, aX, aY, aZ);
        }
    }

}
