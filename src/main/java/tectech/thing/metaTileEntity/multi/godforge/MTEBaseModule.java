package tectech.thing.metaTileEntity.multi.godforge;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.casing.TTCasingsContainer.GodforgeCasings;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import tectech.TecTech;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsUI;

public class MTEBaseModule extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    protected final int tier = getTier();
    protected boolean isConnected = false;
    protected double overclockTimeFactor = 2d;
    protected boolean isUpgrade83Unlocked = false;
    protected boolean isMultiStepPlasmaCapable = false;
    protected boolean isMagmatterCapable = false;
    private boolean isVoltageConfigUnlocked = false;
    private boolean isInversionUnlocked = false;
    protected UUID userUUID;
    protected int machineHeat = 0;
    protected int overclockHeat = 0;
    protected int maximumParallel = 0;
    protected int plasmaTier = 0;
    protected double processingSpeedBonus = 0;
    protected double energyDiscount = 0;
    protected long processingVoltage = 2_000_000_000;
    protected BigInteger powerTally = BigInteger.ZERO;
    protected long recipeTally = 0;
    private long currentRecipeHeat = 0;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int GENERAL_INFO_WINDOW_ID = 10;
    private static final int TEXTURE_INDEX = 960;

    public MTEBaseModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBaseModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBaseModule(mName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && isConnected) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
            if (mEfficiency < 0) mEfficiency = 0;
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.blastFurnaceRecipes;
    }

    @Override
    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EU_drain = EUtEffective * Amperes;
        if (EU_drain == 0L) {
            return true;
        } else {
            if (EU_drain > 0L) {
                EU_drain = -EU_drain;
            }
            return addEUToGlobalEnergyMap(userUUID, EU_drain);
        }
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    public void setHeat(int heat) {
        machineHeat = heat;
    }

    public int getHeat() {
        return machineHeat;
    }

    public void setHeatForOC(int heat) {
        overclockHeat = heat;
    }

    public int getHeatForOC() {
        return overclockHeat;
    }

    public void setMaxParallel(int parallel) {
        maximumParallel = parallel;
    }

    public int getMaxParallel() {
        return maximumParallel;
    }

    public int getActualParallel() {
        return Math.max(1, alwaysMaxParallel ? getMaxParallel() : Math.min(getMaxParallel(), powerPanelMaxParallel));
    }

    public void setSpeedBonus(double bonus) {
        processingSpeedBonus = bonus;
    }

    public double getSpeedBonus() {
        return processingSpeedBonus;
    }

    public void setEnergyDiscount(double discount) {
        energyDiscount = discount;
    }

    public double getEnergyDiscount() {
        return energyDiscount;
    }

    public void setUpgrade83(boolean unlocked) {
        isUpgrade83Unlocked = unlocked;
    }

    public void setOverclockTimeFactor(double factor) {
        overclockTimeFactor = factor;
    }

    public double getOverclockTimeFactor() {
        return overclockTimeFactor;
    }

    public void setMultiStepPlasma(boolean isCapable) {
        isMultiStepPlasmaCapable = isCapable;
    }

    public void setProcessingVoltage(long Voltage) {
        processingVoltage = Voltage;
    }

    public long getProcessingVoltage() {
        return processingVoltage;
    }

    public void setMagmatterCapable(boolean isCapable) {
        isMagmatterCapable = isCapable;
    }

    public double getHeatEnergyDiscount() {
        return isUpgrade83Unlocked ? 0.92 : 0.95;
    }

    public void setPlasmaTier(int tier) {
        plasmaTier = tier;
    }

    public int getPlasmaTier() {
        return plasmaTier;
    }

    public void setVoltageConfig(boolean unlocked) {
        isVoltageConfigUnlocked = unlocked;
    }

    public void setInversionConfig(boolean inversion) {
        isInversionUnlocked = inversion;
    }

    public void setPowerTally(BigInteger amount) {
        powerTally = amount;
    }

    public BigInteger getPowerTally() {
        return powerTally;
    }

    public void addToPowerTally(BigInteger amount) {
        powerTally = powerTally.add(amount);
    }

    public void setRecipeTally(long amount) {
        recipeTally = amount;
    }

    public long getRecipeTally() {
        return recipeTally;
    }

    public void addToRecipeTally(long amount) {
        recipeTally += amount;
    }

    public int getTier() {
        return tier;
    }

    public void setCurrentRecipeHeat(long heat) {
        currentRecipeHeat = heat;
    }

    public long getCurrentRecipeHeat() {
        return currentRecipeHeat;
    }

    @Override
    public long getMaxInputVoltage() {
        return GTValues.V[tier];
    }

    // This prevents processingLogic from overflowing on energy, can be changed if/when it can handle > max long
    protected long getSafeProcessingVoltage() {
        return Math.min(getProcessingVoltage(), Long.MAX_VALUE / getActualParallel());
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        if (this instanceof MTESmeltingModule) {
            return getStructureDefinition(GregTechAPI.sBlockCasings5, 12);
        }
        return getStructureDefinition(GodforgeCasings, 8);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0, stackSize, hintsOnly);
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(1000, elementBudget * 5);
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, realBudget, env, false, true);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0)) {
            return false;
        }

        if (this instanceof MTEExoticModule) {
            if (mOutputHatches.isEmpty()) {
                return false;
            }
            return !mOutputBusses.isEmpty();
        }

        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            userUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        drawTexts(screenElements, inventorySlot);

        buildContext.addSyncedWindow(GENERAL_INFO_WINDOW_ID, this::createGeneralInfoWindow);
        buildContext.addSyncedWindow(POWER_PANEL_WINDOW_ID, this::createPowerPanel);

        builder.widget(
            new DrawableWidget().setSize(18, 18)
                .setPos(172, 67)
                .addTooltip(translateToLocal("gt.blockmachines.multimachine.FOG.clickhere"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY));

        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                .setPos(4, 4)
                .setSize(190, 85))
            .widget(
                inventorySlot.setPos(173, 167)
                    .setBackground(getGUITextureSet().getItemSlot(), TecTechUITextures.OVERLAY_SLOT_MESH))
            .widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK_SMALL)
                    .setPos(173, 185)
                    .setSize(18, 6))
            .widget(
                new Scrollable().setVerticalScroll()
                    .widget(screenElements)
                    .setPos(10, 7)
                    .setSize(182, 79))
            .widget(
                TextWidget.dynamicText(this::connectionStatus)
                    .setDefaultColor(EnumChatFormatting.BLACK)
                    .setPos(75, 94)
                    .setSize(100, 10))
            .widget(
                new ButtonWidget().setOnClick(
                    (data, widget) -> {
                        if (!widget.isClient()) widget.getContext()
                            .openSyncedWindow(GENERAL_INFO_WINDOW_ID);
                    })
                    .setSize(18, 18)
                    .setPos(172, 67)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(createPowerSwitchButton(builder))
            .widget(createStructureUpdateButton(builder))
            .widget(createPowerPanelButton(builder));

        if (supportsVoidProtection()) builder.widget(createVoidExcessButton(builder));
        if (supportsInputSeparation()) builder.widget(createInputSeparationButton(builder));
        if (supportsBatchMode()) builder.widget(createBatchModeButton(builder));
        if (supportsSingleRecipeLocking()) builder.widget(createLockToSingleRecipeButton(builder));
    }

    protected ModularWindow createGeneralInfoWindow(final EntityPlayer player) {
        return ForgeOfGodsUI.createGeneralInfoWindow(() -> isInversionUnlocked, val -> isInversionUnlocked = val);
    }

    @Override
    public ButtonWidget createPowerSwitchButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createPowerSwitchButton(getBaseMetaTileEntity());
    }

    @Override
    public ButtonWidget createInputSeparationButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createInputSeparationButton(getBaseMetaTileEntity(), this, builder);
    }

    @Override
    public ButtonWidget createBatchModeButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createBatchModeButton(getBaseMetaTileEntity(), this, builder);
    }

    @Override
    public ButtonWidget createLockToSingleRecipeButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createLockToSingleRecipeButton(getBaseMetaTileEntity(), this, builder);
    }

    @Override
    public ButtonWidget createStructureUpdateButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createStructureUpdateButton(getBaseMetaTileEntity(), this, builder);
    }

    @Override
    public ButtonWidget createVoidExcessButton(IWidgetBuilder<?> builder) {
        return ForgeOfGodsUI.createVoidExcessButton(getBaseMetaTileEntity(), this, builder);
    }

    @Override
    public ButtonWidget createPowerPanelButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
            if (!widget.isClient()) widget.getContext()
                .openSyncedWindow(POWER_PANEL_WINDOW_ID);
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_CELESTIAL_32x32);
                ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PANEL);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.power_panel"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 112)
            .setSize(16, 16);
        return (ButtonWidget) button;
    }

    @Override
    public ModularWindow createPowerPanel(EntityPlayer player) {
        if (getBaseMetaTileEntity().isServerSide()) maxParallel = getMaxParallel();
        if (alwaysMaxParallel) powerPanelMaxParallel = maxParallel;

        final int w = 138;
        final int h = 105;
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

        builder.widget(
            new TextWidget(EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.power_panel"))
                .setPos(0, 2)
                .setSize(138, 18));

        builder.widget(new FakeSyncWidget.IntegerSyncer(this::getMaxParallel, val -> maxParallel = val));
        builder
            .widget(new FakeSyncWidget.IntegerSyncer(() -> powerPanelMaxParallel, val -> powerPanelMaxParallel = val));
        builder.widget(new FakeSyncWidget.BooleanSyncer(() -> alwaysMaxParallel, val -> alwaysMaxParallel = val));

        builder.widget(
            TextWidget.localised("GTPP.CC.parallel")
                .setPos(0, 24)
                .setSize(125, 18));

        NumericWidget textField = (NumericWidget) new NumericWidget()
            .setSetter(val -> powerPanelMaxParallel = (int) val)
            .setGetter(() -> powerPanelMaxParallel)
            .setValidator(val -> {
                powerPanelMaxParallel = (int) Math
                    .min(maxParallel, Math.max(val, (alwaysMaxParallel ? maxParallel : 1)));
                return powerPanelMaxParallel;
            })
            .setDefaultValue(powerPanelMaxParallel)
            .setScrollValues(1, 4, 64)
            .setTextAlignment(Alignment.Center)
            .setTextColor(Color.WHITE.normal)
            .dynamicTooltip(
                () -> Collections.singletonList(
                    alwaysMaxParallel
                        ? StatCollector.translateToLocalFormatted("GT5U.gui.text.lockedvalue", maxParallel)
                        : StatCollector.translateToLocalFormatted("GT5U.gui.text.rangedvalue", 1, maxParallel)))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(70, 18)
            .setPos(12, 40)
            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD);

        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.voltageinfo")
                .setPos(0, 60)
                .setSize(138, 18));
        builder.widget(
            new DrawableWidget().setDrawable(ModularUITextures.ICON_INFO)
                .setPos(126, 67)
                .setSize(8, 8)
                .addTooltip(translateToLocal("fog.text.tooltip.voltageadjustment"))
                .addTooltip(translateToLocal("fog.text.tooltip.voltageadjustment.1"))
                .setEnabled($ -> isVoltageConfigUnlocked)
                .setTooltipShowUpDelay(TOOLTIP_DELAY))
            .widget(
                new NumericWidget().setSetter(val -> processingVoltage = (long) val)
                    .setGetter(() -> processingVoltage)
                    .setBounds(2_000_000_000, Long.MAX_VALUE)
                    .setDefaultValue(2_000_000_000)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(130, 18)
                    .setPos(4, 76)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                    .setEnabled($ -> isVoltageConfigUnlocked)
                    .attachSyncer(
                        new FakeSyncWidget.LongSyncer(this::getProcessingVoltage, this::setProcessingVoltage),
                        builder)
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(
                            () -> isVoltageConfigUnlocked,
                            val -> isVoltageConfigUnlocked = val),
                        builder))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CROSS)
                    .setPos(54, 70)
                    .setSize(30, 30)
                    .addTooltip(translateToLocal("fog.button.voltageconfig.tooltip.02"))
                    .setEnabled($ -> !isVoltageConfigUnlocked)
                    .setTooltipShowUpDelay(TOOLTIP_DELAY)
                    .attachSyncer(
                        new FakeSyncWidget.BooleanSyncer(
                            () -> isVoltageConfigUnlocked,
                            val -> isVoltageConfigUnlocked = val),
                        builder));

        builder.widget(textField);
        builder.widget(createMaxParallelCheckBox(textField));

        return builder.build();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(172, 67));
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    private static IStructureDefinition<MTEBaseModule> getStructureDefinition(Block coilBlock, int meta) {
        return StructureDefinition.<MTEBaseModule>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                new String[][] { { "       ", "  BBB  ", " BBBBB ", " BB~BB ", " BBBBB ", "  BBB  ", "       " },
                    { "  CCC  ", " CFFFC ", "CFFFFFC", "CFFFFFC", "CFFFFFC", " CFFFC ", "  CCC  " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "   E   ", "  EAE  ", "   E   ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   D   ", "       ", "       ", "       " },
                    { "       ", "       ", "       ", "   G   ", "       ", "       ", "       " } })
            .addElement('A', ofBlock(coilBlock, meta))
            .addElement(
                'B',
                GTStructureUtility
                    .ofHatchAdderOptional(MTEBaseModule::addClassicToMachineList, TEXTURE_INDEX, 1, GodforgeCasings, 0))
            .addElement('C', ofBlock(GodforgeCasings, 0))
            .addElement('D', ofBlock(GodforgeCasings, 1))
            .addElement('E', ofBlock(GodforgeCasings, 2))
            .addElement('F', ofBlock(GodforgeCasings, 3))
            .addElement('G', ofBlock(GodforgeCasings, 4))
            .build();
    }

    private Text connectionStatus() {
        String status = EnumChatFormatting.RED
            + translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus.false");
        if (isConnected) {
            status = EnumChatFormatting.GREEN + translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus.true");
        }
        return new Text(translateToLocal("gt.blockmachines.multimachine.FOG.modulestatus") + " " + status);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        NBT.setBoolean("isConnected", isConnected);
        NBT.setBoolean("isVoltageConfigUnlocked", isVoltageConfigUnlocked);
        NBT.setLong("processingVoltage", processingVoltage);
        NBT.setLong("recipeTally", recipeTally);
        NBT.setLong("currentRecipeHeat", currentRecipeHeat);
        NBT.setByteArray("powerTally", powerTally.toByteArray());
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        isConnected = NBT.getBoolean("isConnected");
        isVoltageConfigUnlocked = NBT.getBoolean("isVoltageConfigUnlocked");
        processingVoltage = NBT.getLong("processingVoltage");
        recipeTally = NBT.getLong("recipeTally");
        currentRecipeHeat = NBT.getLong("currentRecipeHeat");
        powerTally = new BigInteger(NBT.getByteArray("powerTally"));
        super.loadNBTData(NBT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/GODFORGE_MODULE_ACTIVE");
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/SCREEN_OFF");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(ScreenON)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX), TextureFactory.builder()
                .addIcon(ScreenOFF)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX) };
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
