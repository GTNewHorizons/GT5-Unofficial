package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.forgeOfGodsRenderBlock;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.GodforgeMath.calculateFuelConsumption;
import static com.github.technus.tectech.util.GodforgeMath.calculateMaxFuelFactor;
import static com.github.technus.tectech.util.GodforgeMath.calculateMaxHeatForModules;
import static com.github.technus.tectech.util.GodforgeMath.calucateMaxParallelForModules;
import static com.github.technus.tectech.util.GodforgeMath.calucateSpeedBonusForModules;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.TileForgeOfGods;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules.GT_MetaTileEntity_EM_BaseModule;
import com.github.technus.tectech.util.CommonValues;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
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
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidNameHolderWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.IGT_HatchAdder;

public class GT_MetaTileEntity_EM_ForgeOfGods extends GT_MetaTileEntity_MultiblockBase_EM
        implements IConstructable, ISurvivalConstructable {

    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int fuelConsumptionFactor = 1;
    private int selectedFuelType = 0;
    private long fuelConsumption = 0;
    private int internalBattery = 0;
    public ArrayList<GT_MetaTileEntity_EM_BaseModule> moduleHatches = new ArrayList<>();

    private static int spacetimeCompressionFieldMetadata = -1;
    private int solenoidCoilMetadata = -1;
    private static final int FUEL_CONFIG_WINDOW_ID = 9;
    private static final int UPGRADE_TREE_WINDOW_ID = 10;
    private static final int INDIVIDUAL_UPGRADE_WINDOW_ID = 11;
    private static final int[] FIRST_SPLIT_UPGRADES = new int[] { 12, 13, 14 };
    private static final int[] RING_UPGRADES = new int[] { 26, 29 };
    protected static final String STRUCTURE_PIECE_MAIN = "main";

    private Boolean debugMode = true;

    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5); // 200 blocks max per
        // placement.
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 31, 34, 0, realBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_ForgeOfGods> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public static final IStructureDefinition<GT_MetaTileEntity_EM_ForgeOfGods> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_ForgeOfGods>builder()
            .addShape("main", transpose(ForgeofGodsStructureString.godForge))

            .addElement(
                    'A',
                    buildHatchAdder(GT_MetaTileEntity_EM_ForgeOfGods.class)
                            .atLeast(InputHatch, OutputHatch, InputBus, OutputBus).casingIndex(texturePage << 7).dot(1)
                            .buildAndChain(sBlockCasingsBA0, 12))
            .addElement('B', ofBlock(sBlockCasingsTT, 11)).addElement('C', ofBlock(sBlockCasingsTT, 12))
            .addElement(
                    'D',
                    ofBlocksTiered(
                            (block, meta) -> block == GregTech_API.sSolenoidCoilCasings ? meta : -1,
                            ImmutableList.of(
                                    Pair.of(GregTech_API.sSolenoidCoilCasings, 7),
                                    Pair.of(GregTech_API.sSolenoidCoilCasings, 8),
                                    Pair.of(GregTech_API.sSolenoidCoilCasings, 9),
                                    Pair.of(GregTech_API.sSolenoidCoilCasings, 10)),
                            -1,
                            (t, meta) -> t.solenoidCoilMetadata = meta,
                            t -> t.solenoidCoilMetadata))
            .addElement(
                    'E',
                    ofBlocksTiered(
                            (block, meta) -> block == TT_Container_Casings.SpacetimeCompressionFieldGenerators ? meta
                                    : -1,
                            ImmutableList.of(
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 0),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 1),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 2),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 3),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 4),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 5),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 6),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 7),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 8)),
                            -1,
                            (t, meta) -> spacetimeCompressionFieldMetadata = meta,
                            t -> spacetimeCompressionFieldMetadata))
            .addElement(
                    'F',
                    buildHatchAdder(GT_MetaTileEntity_EM_ForgeOfGods.class).atLeast(InputHatch)
                            .casingIndex(texturePage << 7).dot(2).buildAndChain(sBlockCasingsBA0, 12))
            .addElement(
                    'G',
                    GT_HatchElementBuilder.<GT_MetaTileEntity_EM_ForgeOfGods>builder().atLeast(moduleElement.Module)
                            .casingIndex(texturePage << 7).dot(3).buildAndChain(sBlockCasingsBA0, 12))
            .build();

    public GT_MetaTileEntity_EM_ForgeOfGods(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_ForgeOfGods(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_ForgeOfGods(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12],
                    new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12] };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 31, 34, 0, stackSize, hintsOnly);
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

        spacetimeCompressionFieldMetadata = -1;
        moduleHatches.clear();

        // Check structure of multi.
        if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 31, 34, 0)) {
            return false;
        }

        // Check there is 1 input bus.
        if (mInputBusses.size() != 1) {
            return false;
        }

        // Make sure there are no energy hatches.

        if (mEnergyHatches.size() > 0) {
            return false;
        }

        if (mExoticEnergyHatches.size() > 0) {
            return false;
        }

        // Make sure there is 1 input hatch.
        if (mInputHatches.size() != 1) {
            return false;
        }

        mHardHammer = true;
        mSoftHammer = true;
        mScrewdriver = true;
        mCrowbar = true;
        mSolderingTool = true;
        mWrench = true;
        return true;
    }

    int ticker = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (getBaseMetaTileEntity().isAllowedToWork()) {
                ticker++;
                // Check and drain fuel
                if (ticker % SECONDS == 0) {
                    ticker = 0;
                    FluidStack fluidInHatch = mInputHatches.get(0).getFluid();

                    fuelConsumption = (long) calculateFuelConsumption(this);
                    if (fluidInHatch != null && fluidInHatch.isFluidEqual(validFuelList.get(selectedFuelType))) {
                        FluidStack fluidNeeded = new FluidStack(
                                validFuelList.get(selectedFuelType),
                                (int) fuelConsumption);
                        FluidStack fluidReal = mInputHatches.get(0).drain(fluidNeeded.amount, true);
                        if (fluidReal == null || fluidReal.amount < fluidNeeded.amount) {
                            reduceBattery(1);
                        } else {
                            increaseBattery(1);
                        }
                    } else {
                        reduceBattery(1);
                    }
                    // Do module calculations and checks
                    if (moduleHatches.size() > 0 && internalBattery > 0) {
                        for (GT_MetaTileEntity_EM_BaseModule module : moduleHatches) {
                            module.connect();
                            module.setHeat(calculateMaxHeatForModules(module, this));
                            module.setSpeedBonus(calucateSpeedBonusForModules(module, this));
                            module.setMaxParallel(calucateMaxParallelForModules(module, this));
                        }
                    }
                }
            } else {
                if (moduleHatches.size() > 0) {
                    for (GT_MetaTileEntity_EM_BaseModule module : moduleHatches) {
                        module.disconnect();
                    }
                }
            }
            if (mEfficiency < 0) mEfficiency = 0;
            fixAllMaintenance();
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
        if (metaTileEntity instanceof GT_MetaTileEntity_EM_BaseModule) {
            return moduleHatches.add((GT_MetaTileEntity_EM_BaseModule) metaTileEntity);
        }
        return false;
    }

    public enum moduleElement implements IHatchElement<GT_MetaTileEntity_EM_ForgeOfGods> {

        Module(GT_MetaTileEntity_EM_ForgeOfGods::addModuleToMachineList, GT_MetaTileEntity_EM_BaseModule.class) {

            @Override
            public long count(GT_MetaTileEntity_EM_ForgeOfGods tileEntity) {
                return tileEntity.moduleHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_EM_ForgeOfGods> adder;

        @SafeVarargs
        moduleElement(IGT_HatchAdder<GT_MetaTileEntity_EM_ForgeOfGods> adder,
                Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_EM_ForgeOfGods> adder() {
            return adder;
        }
    }

    private void createRenderBlock() {

        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity().getWorld()
                .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity().getWorld()
                .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), forgeOfGodsRenderBlock);
        TileForgeOfGods rendererTileEntity = (TileForgeOfGods) this.getBaseMetaTileEntity().getWorld()
                .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));

        rendererTileEntity.setRenderSize(20);
        rendererTileEntity.setRenderRotationSpeed(5);
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add("Output Buses:" + formatNumbers(mOutputBusses.size()));
        str.add("Output Hatches:" + formatNumbers(mOutputHatches.size()));
        str.add("Input Buses:" + formatNumbers(mInputBusses.size()));
        str.add("Input Hatches:" + formatNumbers(mInputHatches.size()));
        str.add("Max Parallel:" + formatNumbers(Math.pow(4, spacetimeCompressionFieldMetadata + 1)));
        return str.toArray(new String[0]);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        createRenderBlock();
    }

    @Override
    public void onRemoval() {
        if (moduleHatches != null && moduleHatches.size() > 0) {
            for (GT_MetaTileEntity_EM_BaseModule module : moduleHatches) {
                module.disconnect();
            }
        }
        super.onRemoval();
    }

    protected void fixAllMaintenance() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (doesBindPlayerInventory()) {
            builder.widget(
                    new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE).setPos(4, 4)
                            .setSize(190, 91));
        } else {
            builder.widget(
                    new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE_NO_INVENTORY).setPos(4, 4)
                            .setSize(190, 171));
        }
        buildContext.addSyncedWindow(UPGRADE_TREE_WINDOW_ID, this::createUpgradeTreeWindow);
        buildContext.addSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID, this::createIndividualUpgradeWindow);
        buildContext.addSyncedWindow(FUEL_CONFIG_WINDOW_ID, this::createFuelConfigWindow);
        builder.widget(
                new ButtonWidget().setOnClick(
                        (clickData, widget) -> {
                            if (!widget.isClient()) widget.getContext().openSyncedWindow(UPGRADE_TREE_WINDOW_ID);
                        }).setSize(16, 16).setBackground(() -> {
                            List<UITexture> button = new ArrayList<>();
                            button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                            button.add(TecTechUITextures.OVERLAY_BUTTON_ARROW_BLUE_UP);
                            return button.toArray(new IDrawable[0]);
                        }).addTooltip("Path of Celestial Transcendence").setPos(174, 167))
                .widget(
                        new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK_SMALL).setPos(174, 183)
                                .setSize(16, 6))
                .widget(
                        new ButtonWidget().setOnClick(
                                (clickData, widget) -> {
                                    if (!widget.isClient()) widget.getContext().openSyncedWindow(FUEL_CONFIG_WINDOW_ID);
                                }).setSize(16, 16).setBackground(() -> {
                                    List<UITexture> button = new ArrayList<>();
                                    button.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                                    button.add(TecTechUITextures.OVERLAY_BUTTON_HEAT_ON);
                                    return button.toArray(new IDrawable[0]);
                                }).addTooltip(translateToLocal("fog.button.fuelconfig.tooltip")).setPos(174, 129))
                .widget(
                        TextWidget.dynamicText(this::storedFuel).setDefaultColor(EnumChatFormatting.WHITE).setPos(3, 5)
                                .setSize(74, 34));

        Widget powerSwitchButton = createPowerSwitchButton();
        builder.widget(powerSwitchButton)
                .widget(new FakeSyncWidget.BooleanSyncer(() -> getBaseMetaTileEntity().isAllowedToWork(), val -> {
                    if (val) {
                        getBaseMetaTileEntity().enableWorking();
                    } else {
                        getBaseMetaTileEntity().disableWorking();
                    }
                }));
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
        }).setPlayClickSound(false).setBackground(() -> {
            List<UITexture> ret = new ArrayList<>();
            ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
            if (getBaseMetaTileEntity().isAllowedToWork()) {
                ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
            } else {
                ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
            }
            return ret.toArray(new IDrawable[0]);
        }).setPos(174, doesBindPlayerInventory() ? 148 : 172).setSize(16, 16);
        button.addTooltip("Power Switch").setTooltipShowUpDelay(TOOLTIP_DELAY);
        return (ButtonWidget) button;
    }

    protected ModularWindow createFuelConfigWindow(final EntityPlayer player) {
        final int WIDTH = 78;
        final int HEIGHT = 130;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
                (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT)).add(
                        Alignment.TopRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                                .add(WIDTH - 3, 0)));
        builder.widget(
                TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelconsumption").setPos(3, 2).setSize(74, 34))
                .widget(
                        new NumericWidget().setSetter(val -> fuelConsumptionFactor = (int) val)
                                .setGetter(() -> fuelConsumptionFactor).setBounds(1, calculateMaxFuelFactor(this))
                                .setDefaultValue(1).setScrollValues(1, 4, 64).setTextAlignment(Alignment.Center)
                                .setTextColor(Color.WHITE.normal).setSize(70, 18).setPos(3, 35)
                                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD))
                .widget(
                        new DrawableWidget().setDrawable(GT_UITextures.PICTURE_INFORMATION).setPos(65, 25)
                                .setSize(4, 10)
                                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.0"))
                                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.1"))
                                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.2"))
                                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.3"))
                                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.4")))
                .widget(
                        TextWidget.localised("gt.blockmachines.multimachine.FOG.fueltype").setPos(3, 57)
                                .setSize(74, 24))
                .widget(
                        TextWidget.localised("gt.blockmachines.multimachine.FOG.fuelusage").setPos(3, 100)
                                .setSize(74, 20))
                .widget(TextWidget.dynamicText(this::fuelUsage).setPos(3, 115).setSize(74, 15))
                .widget(
                        new MultiChildWidget().addChild(
                                new FluidNameHolderWidget(
                                        () -> MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1)
                                                .getUnlocalizedName().substring(6),
                                        (String) -> MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1)
                                                .getUnlocalizedName()) {

                                    @Override
                                    public void buildTooltip(List<Text> tooltip) {
                                        FluidStack fluid = createFluidStack();
                                        addFluidNameInfo(tooltip, fluid);
                                        addAdditionalFluidInfo(tooltip, fluid);
                                    }
                                }.setPos(1, 1).setSize(16, 16))
                                .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                                    selectedFuelType = 0;
                                }).setBackground(() -> {
                                    if (selectedFuelType == 0) {
                                        return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                                    } else {
                                        return new IDrawable[] {};
                                    }
                                }).setSize(18, 18).attachSyncer(
                                        new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType),
                                        builder))

                                .setPos(6, 82).setSize(18, 18))
                .widget(
                        new MultiChildWidget().addChild(
                                new FluidNameHolderWidget(
                                        () -> MaterialsUEVplus.RawStarMatter.getFluid(1).getUnlocalizedName()
                                                .substring(6),
                                        (String) -> MaterialsUEVplus.RawStarMatter.getFluid(1).getUnlocalizedName()) {

                                    @Override
                                    public void buildTooltip(List<Text> tooltip) {
                                        FluidStack fluid = createFluidStack();
                                        addFluidNameInfo(tooltip, fluid);
                                        addAdditionalFluidInfo(tooltip, fluid);
                                    }
                                }.setPos(1, 1).setSize(16, 16))
                                .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                                    selectedFuelType = 1;
                                }).setBackground(() -> {
                                    if (selectedFuelType == 1) {
                                        return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                                    } else {
                                        return new IDrawable[] {};
                                    }
                                }).setSize(18, 18)).setPos(29, 82).setSize(18, 18).attachSyncer(
                                        new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType),
                                        builder))
                .widget(
                        new MultiChildWidget().addChild(
                                new FluidNameHolderWidget(
                                        () -> MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1)
                                                .getUnlocalizedName().substring(6),
                                        (String) -> MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter
                                                .getMolten(1).getUnlocalizedName()) {

                                    @Override
                                    public void buildTooltip(List<Text> tooltip) {
                                        FluidStack fluid = createFluidStack();
                                        addFluidNameInfo(tooltip, fluid);
                                        addAdditionalFluidInfo(tooltip, fluid);
                                    }
                                }.setPos(1, 1).setSize(16, 16))
                                .addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
                                    TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                                    selectedFuelType = 2;
                                }).setBackground(() -> {
                                    if (selectedFuelType == 2) {
                                        return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                                    } else {
                                        return new IDrawable[] {};
                                    }
                                }).setSize(18, 18)).setPos(52, 82).setSize(18, 18).attachSyncer(
                                        new FakeSyncWidget.IntegerSyncer(this::getFuelType, this::setFuelType),
                                        builder));

        return builder.build();
    }

    private int currentUpgradeID = 0;
    private int currentColorCode = 0;
    private int[] prereqUpgrades = new int[] {};
    private int[] followupUpgrades = new int[] {};
    private boolean allPrereqRequired = false;
    private boolean isUpradeSplitStart = false;
    private boolean[] upgrades = new boolean[31];

    protected ModularWindow createUpgradeTreeWindow(final EntityPlayer player) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        final int PARENT_WIDTH = 300;
        final int PARENT_HEIGHT = 1000;
        ModularWindow.Builder builder = ModularWindow.builder(PARENT_WIDTH, PARENT_HEIGHT);
        scrollable.widget(
                createUpgradeBox(0, 0, new int[] {}, false, new int[] { 1 }, false, new Pos2d(126, 56), scrollable))
                .widget(
                        createUpgradeBox(
                                1,
                                0,
                                new int[] { 0 },
                                false,
                                new int[] { 2, 3 },
                                false,
                                new Pos2d(126, 116),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                2,
                                0,
                                new int[] { 1 },
                                false,
                                new int[] { 4, 5 },
                                false,
                                new Pos2d(96, 176),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                3,
                                0,
                                new int[] { 1 },
                                false,
                                new int[] { 5, 6 },
                                false,
                                new Pos2d(156, 176),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                4,
                                0,
                                new int[] { 2 },
                                false,
                                new int[] { 8 },
                                false,
                                new Pos2d(66, 236),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                5,
                                0,
                                new int[] { 2, 3 },
                                false,
                                new int[] { 7 },
                                false,
                                new Pos2d(126, 236),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                6,
                                0,
                                new int[] { 3 },
                                false,
                                new int[] { 10 },
                                false,
                                new Pos2d(186, 236),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                7,
                                0,
                                new int[] { 5 },
                                false,
                                new int[] { 8, 9, 10 },
                                false,
                                new Pos2d(126, 296),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                8,
                                0,
                                new int[] { 4, 7 },
                                true,
                                new int[] { 11 },
                                false,
                                new Pos2d(56, 356),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                9,
                                0,
                                new int[] { 7 },
                                false,
                                new int[] {},
                                false,
                                new Pos2d(126, 356),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                10,
                                0,
                                new int[] { 6, 7 },
                                true,
                                new int[] { 11 },
                                false,
                                new Pos2d(196, 356),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                11,
                                0,
                                new int[] { 8, 10 },
                                false,
                                new int[] { 12, 13, 14 },
                                false,
                                new Pos2d(126, 416),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                12,
                                1,
                                new int[] { 11 },
                                false,
                                new int[] { 17 },
                                true,
                                new Pos2d(66, 476),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                13,
                                2,
                                new int[] { 11 },
                                false,
                                new int[] { 18 },
                                true,
                                new Pos2d(126, 476),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                14,
                                3,
                                new int[] { 11 },
                                false,
                                new int[] { 15, 19 },
                                true,
                                new Pos2d(186, 476),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                15,
                                3,
                                new int[] { 14 },
                                false,
                                new int[] {},
                                false,
                                new Pos2d(246, 496),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                16,
                                1,
                                new int[] { 17 },
                                false,
                                new int[] {},
                                false,
                                new Pos2d(6, 556),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                17,
                                1,
                                new int[] { 12 },
                                false,
                                new int[] { 16, 20 },
                                false,
                                new Pos2d(66, 536),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                18,
                                2,
                                new int[] { 13 },
                                false,
                                new int[] { 21 },
                                false,
                                new Pos2d(126, 536),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                19,
                                3,
                                new int[] { 14 },
                                false,
                                new int[] { 22 },
                                false,
                                new Pos2d(186, 536),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                20,
                                1,
                                new int[] { 17 },
                                false,
                                new int[] { 23 },
                                false,
                                new Pos2d(66, 596),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                21,
                                2,
                                new int[] { 18 },
                                false,
                                new int[] { 23 },
                                false,
                                new Pos2d(126, 596),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                22,
                                3,
                                new int[] { 19 },
                                false,
                                new int[] { 23 },
                                false,
                                new Pos2d(186, 596),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                23,
                                0,
                                new int[] { 20, 21, 22 },
                                false,
                                new int[] { 24 },
                                false,
                                new Pos2d(126, 656),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                24,
                                0,
                                new int[] { 23 },
                                false,
                                new int[] { 25 },
                                false,
                                new Pos2d(126, 718),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                25,
                                0,
                                new int[] { 24 },
                                false,
                                new int[] { 26 },
                                false,
                                new Pos2d(36, 758),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                26,
                                0,
                                new int[] { 25 },
                                false,
                                new int[] { 27 },
                                false,
                                new Pos2d(36, 848),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                27,
                                0,
                                new int[] { 26 },
                                false,
                                new int[] { 28 },
                                false,
                                new Pos2d(126, 888),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                28,
                                0,
                                new int[] { 27 },
                                false,
                                new int[] { 29 },
                                false,
                                new Pos2d(216, 848),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                29,
                                0,
                                new int[] { 28 },
                                false,
                                new int[] { 30 },
                                false,
                                new Pos2d(216, 758),
                                scrollable))
                .widget(
                        createUpgradeBox(
                                30,
                                0,
                                new int[] { 29 },
                                false,
                                new int[] {},
                                false,
                                new Pos2d(126, 798),
                                scrollable))
                .widget(new TextWidget("").setPos(0, 1000));

        builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_STAR).setPos(0, 350).setSize(300, 300))
                .widget(scrollable.setSize(292, 292).setPos(4, 354))
                .widget(ButtonWidget.closeWindowButton(true).setPos(282, 354));
        if (debugMode) {
            builder.widget(
                    new MultiChildWidget()
                            .addChild(
                                    new ButtonWidget().setOnClick((clickData, widget) -> upgrades = new boolean[31])
                                            .setSize(40, 15).setBackground(GT_UITextures.BUTTON_STANDARD)
                                            .addTooltip(translateToLocal("fog.debug.resetbutton.tooltip")))
                            .addChild(
                                    new TextWidget(translateToLocal("fog.debug.resetbutton.text"))
                                            .setTextAlignment(Alignment.Center).setScale(0.57f).setMaxWidth(36)
                                            .setPos(3, 3))
                            .setPos(4, 354));
        }
        return builder.build();
    }

    protected ModularWindow createIndividualUpgradeWindow(final EntityPlayer player) {
        UITexture background = switch (currentColorCode) {
            case 1 -> TecTechUITextures.BACKGROUND_GLOW_PURPLE;
            case 2 -> TecTechUITextures.BACKGROUND_GLOW_ORANGE;
            case 3 -> TecTechUITextures.BACKGROUND_GLOW_GREEN;
            default -> TecTechUITextures.BACKGROUND_GLOW_BLUE;
        };
        ModularWindow.Builder builder = ModularWindow.builder(200, 200).setBackground(background)
                .widget(ButtonWidget.closeWindowButton(true).setPos(185, 3))
                .widget(
                        new MultiChildWidget()
                                .addChild(
                                        new TextWidget(translateToLocal("fog.upgrade.text." + (currentUpgradeID)))
                                                .setTextAlignment(Alignment.Center).setMaxWidth(185)
                                                .setDefaultColor(0x9c9c9c).setPos(9, 35))
                                .addChild(
                                        new TextWidget(translateToLocal("fog.upgrade.lore." + (currentUpgradeID)))
                                                .setTextAlignment(Alignment.Center).setMaxWidth(185)
                                                .setDefaultColor(0x9c9c9c).setPos(9, 110))
                                .setSize(200, 200))
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
                        if (allPrereqRequired) {
                            if (unlockedPrereqUpgrades == prereqUpgrades.length) {
                                upgrades[currentUpgradeID] = true;
                            }
                        } else if (unlockedPrereqUpgrades > 0 || prereqUpgrades.length == 0) {
                            if (isUpradeSplitStart) {
                                for (int splitUpgrade : FIRST_SPLIT_UPGRADES) {
                                    if (upgrades[splitUpgrade]) {
                                        unlockedSplitUpgrades++;
                                    }
                                }
                                for (int maxSplitUpgrades : RING_UPGRADES) {
                                    if (upgrades[maxSplitUpgrades]) {
                                        unlockedSplitUpgrades--;
                                    }
                                }
                            }
                            if (unlockedSplitUpgrades <= 0) {
                                upgrades[currentUpgradeID] = true;
                            }
                        }
                    } else {
                        for (int followupUpgrade : followupUpgrades) {
                            if (upgrades[followupUpgrade]) {
                                unlockedFollowupUpgrades++;
                            }
                        }
                        if (unlockedFollowupUpgrades == 0) {
                            upgrades[currentUpgradeID] = false;
                        }
                    }
                }).setSize(40, 15).setBackground(() -> {
                    if (upgrades[currentUpgradeID]) {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED };
                    } else {
                        return new IDrawable[] { GT_UITextures.BUTTON_STANDARD };
                    }
                }).addTooltip(translateToLocal("fog.upgrade.confirm")))
                        .addChild(
                                new TextWidget(translateToLocal("fog.upgrade.confirm"))
                                        .setTextAlignment(Alignment.Center).setScale(0.7f).setMaxWidth(36).setPos(3, 5))
                        .setPos(79, 177));
        return builder.build();
    }

    /**
     * @param upgradeID               ID of the upgrade
     * @param colorCode               Number deciding which colored background to use, 0 for blue, 1 for purple, 2 for
     *                                orange and 3 for green
     * @param prerequisiteUpgradeIDs  IDs of the prior upgrades directly connected to the current one
     * @param requireAllPrerequisites Decides how many connected prerequisite upgrades have to be unlocked to be able to
     *                                unlock this one. True means ALL, False means AT LEAST ONE
     * @param followingUpgradeIDs     IDs of the following upgrades directly connected to the current one
     * @param isStartOfSplit          Whether this upgrade is one of the initial split upgrades
     * @param pos                     Position of the upgrade inside the scrollableWidget
     */
    private Widget createUpgradeBox(int upgradeID, int colorCode, int[] prerequisiteUpgradeIDs,
            boolean requireAllPrerequisites, int[] followingUpgradeIDs, boolean isStartOfSplit, Pos2d pos,
            IWidgetBuilder<?> builder) {
        return new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, widget) -> {
            currentUpgradeID = upgradeID;
            currentColorCode = colorCode;
            prereqUpgrades = prerequisiteUpgradeIDs;
            allPrereqRequired = requireAllPrerequisites;
            followupUpgrades = followingUpgradeIDs;
            isUpradeSplitStart = isStartOfSplit;
            if (!widget.isClient()) widget.getContext().openSyncedWindow(INDIVIDUAL_UPGRADE_WINDOW_ID);
        }).setSize(40, 15).setBackground(() -> {
            if (upgrades[upgradeID]) {
                return new IDrawable[] { GT_UITextures.BUTTON_STANDARD_PRESSED };
            } else {
                return new IDrawable[] { GT_UITextures.BUTTON_STANDARD };
            }
        }).addTooltip(translateToLocal("fog.upgrade.tt." + upgradeID)))
                .addChild(
                        new TextWidget(translateToLocal("fog.upgrade.tt." + upgradeID))
                                .setTextAlignment(Alignment.Center).setScale(0.57f).setMaxWidth(36).setPos(3, 3))
                .setPos(pos).attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(() -> upgrades[upgradeID], val -> upgrades[upgradeID] = val),
                        builder);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Material Manipulator") // Machine Type:
                .addInfo("Controller block for the Forge of Gods") // Controller
                .addInfo("Uses a Star to to manipulate metals").addSeparator().beginStructureBlock(1, 4, 2, false)
                .addStructureInfo("Output bus/hatch has to be the ME variant")
                .addStructureInfo("Dot 2 of Input Hatch is the Fuel Input Hatch")
                .addInputHatch("Any Infinite Spacetime Casing", 1).addInputHatch("Any Infinite Spacetime Casing", 2) // Fuel
                                                                                                                     // Input
                                                                                                                     // Hatch
                .addInputBus("Any Infinite Spacetime Casing", 1).addOutputBus("Any Infinite Spacetime Casing", 1)
                .addOutputHatch("Any Infinite Spacetime Casing", 1).toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

    @Override
    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        return true;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] { "Forge of Gods multiblock" };
    }

    public Integer getFuelType() {
        return selectedFuelType;
    }

    private void setFuelType(Integer fuelType) {
        selectedFuelType = fuelType;
    }

    public Integer getFuelFactor() {
        return fuelConsumptionFactor;
    }

    public Boolean isUpgradeActive(Integer upgradeID) {
        return upgrades[upgradeID];
    }

    public Integer getTotalActiveUpgrades() {
        int totalUpgrades = 0;
        for (boolean upgrade : upgrades) {
            if (upgrade) {
                totalUpgrades++;
            }
        }
        return totalUpgrades;
    }

    private Text fuelUsage() {
        return new Text(fuelConsumption + " L/s");
    }

    private Text storedFuel() {
        return new Text(
                translateToLocal("gt.blockmachines.multimachine.FOG.storedfuel") + " " + internalBattery + "/100");
    }

    private void increaseBattery(Integer amount) {
        if ((internalBattery + amount) <= 100) {
            internalBattery += amount;
        }
    }

    private void reduceBattery(Integer amount) {
        internalBattery -= amount;
        if (internalBattery <= 0) {
            internalBattery = 0;
            if (moduleHatches.size() > 0) {
                for (GT_MetaTileEntity_EM_BaseModule module : moduleHatches) {
                    module.disconnect();
                }
            }

        }
    }

    @Override
    protected void setHatchRecipeMap(GT_MetaTileEntity_Hatch_Input hatch) {}

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setInteger("spacetimeCompressionTier", spacetimeCompressionFieldMetadata + 1);
        NBT.setInteger("solenoidCoilTier", solenoidCoilMetadata - 7);
        NBT.setInteger("selectedFuelType", selectedFuelType);
        NBT.setInteger("fuelConsumptionFactor", fuelConsumptionFactor);
        NBT.setInteger("internalBattery", internalBattery);

        // Store booleanArray of all upgrades
        NBTTagCompound upgradeBooleanArrayNBTTag = new NBTTagCompound();

        int upgradeIndex = 0;
        for (Boolean upgrade : upgrades) {
            upgradeBooleanArrayNBTTag.setBoolean("upgrade" + upgradeIndex, upgrade);
            upgradeIndex++;
        }

        NBT.setTag("upgrades", upgradeBooleanArrayNBTTag);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        spacetimeCompressionFieldMetadata = NBT.getInteger("spacetimeCompressionTier") - 1;
        solenoidCoilMetadata = NBT.getInteger("solenoidCoilTier") + 7;
        selectedFuelType = NBT.getInteger("selectedFuelType");
        fuelConsumptionFactor = NBT.getInteger("fuelConsumptionFactor");
        internalBattery = NBT.getInteger("internalBattery");

        NBTTagCompound tempBooleanTag = NBT.getCompoundTag("upgrades");

        for (int upgradeIndex = 0; upgradeIndex < 31; upgradeIndex++) {
            boolean upgrade = tempBooleanTag.getBoolean("upgrade" + upgradeIndex);
            upgrades[upgradeIndex] = upgrade;
        }

        super.loadNBTData(NBT);
    }
}
