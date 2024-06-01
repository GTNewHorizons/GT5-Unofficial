package com.github.technus.tectech.thing.metaTileEntity.multi.godforge_modules;

import static com.github.technus.tectech.thing.casing.TT_Container_Casings.GodforgeCasings;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
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
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_StructureUtility;

public class GT_MetaTileEntity_EM_BaseModule extends GT_MetaTileEntity_MultiblockBase_EM {

    protected final int tier = getTier();
    protected boolean isConnected = false;
    protected double overclockTimeFactor = 2d;
    protected boolean isUpgrade83Unlocked = false;
    protected boolean isMultiStepPlasmaCapable = false;
    protected boolean isMagmatterCapable = false;
    private boolean isVoltageConfigUnlocked = false;
    protected UUID userUUID;
    protected int machineHeat = 0;
    protected int overclockHeat = 0;
    protected int maximumParallel = 0;
    protected int plasmaTier = 0;
    protected float processingSpeedBonus = 0;
    protected float energyDiscount = 0;
    protected long processingVoltage = 2_000_000_000;
    protected BigInteger powerTally = BigInteger.ZERO;
    protected long recipeTally = 0;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int VOLTAGE_WINDOW_ID = 9;
    private static final int TEXTURE_INDEX = 960;
    private static final IStructureDefinition<GT_MetaTileEntity_EM_BaseModule> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_EM_BaseModule>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "       ", "  BBB  ", " BBBBB ", " BB~BB ", " BBBBB ", "  BBB  ", "       " },
                { "  CCC  ", " CFFFC ", "CFFFFFC", "CFFFFFC", "CFFFFFC", " CFFFC ", "  CCC  " },
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
        .addElement('A', ofBlock(GregTech_API.sSolenoidCoilCasings, 9))
        .addElement(
            'B',
            GT_StructureUtility.ofHatchAdderOptional(
                GT_MetaTileEntity_EM_BaseModule::addClassicToMachineList,
                TEXTURE_INDEX,
                1,
                GodforgeCasings,
                0))
        .addElement('C', ofBlock(GodforgeCasings, 0))
        .addElement('D', ofBlock(GodforgeCasings, 1))
        .addElement('E', ofBlock(GodforgeCasings, 2))
        .addElement('F', ofBlock(GodforgeCasings, 3))
        .addElement('G', ofBlock(GodforgeCasings, 4))
        .build();

    public GT_MetaTileEntity_EM_BaseModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_BaseModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_BaseModule(mName);
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

    public void setSpeedBonus(float bonus) {
        processingSpeedBonus = bonus;
    }

    public float getSpeedBonus() {
        return processingSpeedBonus;
    }

    public void setEnergyDiscount(float discount) {
        energyDiscount = discount;
    }

    public float getEnergyDiscount() {
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

    public float getHeatEnergyDiscount() {
        return isUpgrade83Unlocked ? 0.92f : 0.95f;
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

    @Override
    public long getMaxInputVoltage() {
        return gregtech.api.enums.GT_Values.V[tier];
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0, stackSize, hintsOnly);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, 3, 3, 0);
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
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85));
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        builder.widget(
            inventorySlot.setPos(173, 167)
                .setBackground(GT_UITextures.SLOT_DARK_GRAY));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(screenElements);

        buildContext.addSyncedWindow(VOLTAGE_WINDOW_ID, this::createVoltageWindow);

        builder.widget(
            TextWidget.dynamicText(this::connectionStatus)
                .setDefaultColor(EnumChatFormatting.BLACK)
                .setPos(75, 94)
                .setSize(100, 10));

        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createInputSeparationButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder))
            .widget(createVoltageButton(builder));
    }

    protected Widget createVoltageButton(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isVoltageConfigUnlocked) {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(VOLTAGE_WINDOW_ID);
                }
            }
        })
            .setPlayClickSound(isVoltageConfigUnlocked)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GT_UITextures.BUTTON_STANDARD);
                if (isVoltageConfigUnlocked) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PASS_ON);
                } else {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PASS_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(translateToLocal("fog.button.voltageconfig.tooltip.01"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 129)
            .setSize(16, 16)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isVoltageConfigUnlocked, val -> isVoltageConfigUnlocked = val),
                builder);
        if (!isVoltageConfigUnlocked) {
            button.addTooltip(EnumChatFormatting.GRAY + translateToLocal("fog.button.voltageconfig.tooltip.02"));
        }
        return button;
    }

    protected ModularWindow createVoltageWindow(final EntityPlayer player) {
        final int WIDTH = 158;
        final int HEIGHT = 52;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.BottomRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)
                        .subtract(0, 10)));
        builder.widget(
            TextWidget.localised("gt.blockmachines.multimachine.FOG.voltageinfo")
                .setPos(3, 4)
                .setSize(150, 20))
            .widget(
                new NumericWidget().setSetter(val -> processingVoltage = (long) val)
                    .setGetter(() -> processingVoltage)
                    .setBounds(2_000_000_000, Long.MAX_VALUE)
                    .setDefaultValue(2_000_000_000)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(150, 18)
                    .setPos(4, 25)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                    .attachSyncer(
                        new FakeSyncWidget.LongSyncer(this::getProcessingVoltage, this::setProcessingVoltage),
                        builder));
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
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
        NBT.setByteArray("powerTally", powerTally.toByteArray());
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {
        isConnected = NBT.getBoolean("isConnected");
        isVoltageConfigUnlocked = NBT.getBoolean("isVoltageConfigUnlocked");
        processingVoltage = NBT.getLong("processingVoltage");
        recipeTally = NBT.getLong("recipeTally");
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
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX),
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_INDEX) };
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
