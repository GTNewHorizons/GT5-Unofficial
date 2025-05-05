/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.tiered;

import static bartworks.common.loaders.RadioHatchMaterialLoader.getRadioHatchMaterialFromInput;
import static bartworks.common.loaders.RadioHatchMaterialLoader.getRadioHatchMaterialList;
import static gregtech.api.enums.GTValues.ticksBetweenSounds;
import static gregtech.api.modularui2.GTGuis.createPopUpPanel;
import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate1by1;

import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import bartworks.API.modularUI.BWUITextures;
import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.MainMod;
import bartworks.common.loaders.RadioHatchMaterialLoader.RadioHatchMaterial;
import bartworks.util.BWColorUtil;
import bartworks.util.BWTooltipReference;
import bartworks.util.MathUtils;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.RecipeMapWorkable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class MTERadioHatch extends MTEHatch implements RecipeMapWorkable, IAddGregtechLogo {

    public int sievert;
    private long timer = 1;
    private long decayTime = 1;
    private short[] colorForGUI = { 0x02, 0x02, 0x02 };
    private byte mass;
    private String material;
    private byte coverage;
    private ItemStack lastUsedItem = null;
    private boolean lastFail = false;
    private RadioHatchMaterial radioHatchMaterial = null;

    public MTERadioHatch(int aID, String aName, String aNameRegional, int aTier, boolean isDeprecated) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            new String[] { StatCollector.translateToLocal("tooltip.tile.radhatch.0.name"),
                (isDeprecated
                    ? EnumChatFormatting.RED
                        + "DEPRECATED! This hatch will be removed in the next major update, use \"Radio Hatch\" instead"
                    : StatCollector.translateToLocal("tooltip.tile.radhatch.1.name")),
                BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get(), });
    }

    public MTERadioHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    public int getSievert() {
        return this.sievert - MathUtils.ceilInt(this.sievert / 100f * this.coverage);
    }

    public short[] getColorForGUI() {
        if (this.colorForGUI != null) return this.colorForGUI;
        return this.colorForGUI = new short[] { 0xFA, 0xFA, 0xFF };
    }

    public byte getMass() {
        return this.mass;
    }

    public byte getCoverage() {
        return this.coverage;
    }

    public void setCoverage(short coverage) {
        byte nu;
        if (coverage > 100) nu = 100;
        else if (coverage < 0) nu = 0;
        else nu = (byte) coverage;
        this.coverage = nu;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN) };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTERadioHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    public void updateSlots() {
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        BaseMetaTileEntity myMetaTileEntity = (BaseMetaTileEntity) this.getBaseMetaTileEntity();
        if (myMetaTileEntity.isServerSide()) {

            if (this.mass > 0) {
                ++this.timer;
            }

            if (this.mass > 0 && (this.decayTime == 0 || this.decayTime > 0 && this.timer % this.decayTime == 0)) {
                this.mass--;
                if (this.mass == 0) {
                    this.material = StatCollector.translateToLocal("tooltip.bw.empty.name");
                    this.sievert = 0;
                }
                this.timer = 1;
            }

            if (myMetaTileEntity.mTickTimer > myMetaTileEntity.mLastSoundTick + ticksBetweenSounds
                && this.sievert > 0) {
                this.sendLoopStart((byte) 1);
                myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
            }

            if (this.mass == 0) {
                ItemStack lStack = this.mInventory[0];

                if (lStack == null) {
                    this.colorForGUI = new short[] { 0x37, 0x37, 0x37 };
                    return;
                }

                ItemData itemData = GTOreDictUnificator.getAssociation(lStack);
                if (itemData != null) {
                    Materials mat = itemData.mMaterial.mMaterial;
                    this.colorForGUI = new short[] { mat.getRGBA()[0], mat.getRGBA()[1], mat.getRGBA()[2] };
                } else {
                    this.colorForGUI = new short[] { 0x37, 0x37, 0x37 };
                }
                if (this.lastFail && GTUtility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                    return;
                }
                if (!this.lastFail && this.lastUsedItem != null && this.radioHatchMaterial != null) {
                    if (GTUtility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                        for (RadioHatchMaterial recipes : getRadioHatchMaterialList()) {
                            this.radioHatchMaterial = getRadioHatchMaterialFromInput(recipes, this.lastUsedItem);
                            if (radioHatchMaterial != null) {
                                break;
                            }
                        }
                        if (radioHatchMaterial != null && getBaseMetaTileEntity().isAllowedToWork()) {
                            this.sievert = this.radioHatchMaterial.recipeSievert;
                            this.mass = this.radioHatchMaterial.recipeMass;
                            this.decayTime = calcDecayTicks(radioHatchMaterial.recipeSievert);
                            this.material = this.lastUsedItem.getDisplayName();
                            lStack.stackSize--;
                            this.updateSlots();
                        }
                    } else {
                        this.radioHatchMaterial = null;
                    }
                }

                if (this.radioHatchMaterial == null || this.lastFail || this.radioHatchMaterial.recipeSievert == 0) {
                    for (RadioHatchMaterial recipes : getRadioHatchMaterialList()) {
                        this.radioHatchMaterial = getRadioHatchMaterialFromInput(recipes, this.mInventory[0]);
                        if (radioHatchMaterial != null) {
                            break;
                        }
                    }
                    if (this.radioHatchMaterial == null) {
                        this.lastFail = true;
                        this.lastUsedItem = this.mInventory[0] == null ? null : this.mInventory[0].copy();
                    } else {
                        this.lastFail = false;
                        this.lastUsedItem = this.mInventory[0].copy();
                        this.sievert = radioHatchMaterial.recipeSievert;
                        this.mass = radioHatchMaterial.recipeMass;
                        this.decayTime = calcDecayTicks(radioHatchMaterial.recipeSievert);
                        this.material = lStack.getDisplayName();
                        lStack.stackSize--;
                        this.updateSlots();
                    }
                }
            }
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (this.sievert != 0) return new String[] {
            StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                + StatCollector.translateToLocal(this.material),
            StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + this.sievert,
            StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + this.mass,
            StatCollector.translateToLocal("tooltip.tile.radhatch.5.name") + " "
                + (this.decayTime - this.timer % (this.decayTime * 60))
                + StatCollector.translateToLocal("tooltip.tile.radhatch.6.name")
                + "/"
                + (this.decayTime - this.timer % this.decayTime) / 20
                + StatCollector.translateToLocal("tooltip.tile.radhatch.7.name")
                + "/"
                + (this.decayTime - this.timer % this.decayTime) / 20 / 60
                + StatCollector.translateToLocal("tooltip.tile.radhatch.8.name")
                + "/"
                + (this.decayTime - this.timer % this.decayTime) / 20 / 60 / 60
                + StatCollector.translateToLocal("tooltip.tile.radhatch.9.name") };
        return new String[] {
            StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                + StatCollector.translateToLocal("tooltip.bw.empty.name"),
            StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + "0",
            StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + "0" };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing() && BartWorksRecipeMaps.radioHatchFakeRecipes.containsInput(aStack);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMass", this.mass);
        aNBT.setInteger("mSievert", this.sievert);
        aNBT.setByte("mCoverage", this.coverage);
        aNBT.setInteger("mTextColor", BWColorUtil.getColorFromRGBArray(this.getColorForGUI()));
        if (this.material != null && !this.material.isEmpty()) aNBT.setString("mMaterial", this.material);
        aNBT.setLong("timer", this.timer);
        aNBT.setLong("decay", this.decayTime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.timer = aNBT.getLong("timer");
        this.mass = aNBT.getByte("mMass");
        this.sievert = (aNBT.getByte("mSv") != 0) ? aNBT.getByte("mSv") + 100 : aNBT.getInteger("mSievert");
        this.coverage = aNBT.getByte("mCoverage");
        this.colorForGUI = BWColorUtil.splitColorToRBGArray(aNBT.getInteger("mTextColor"));
        this.material = aNBT.getString("mMaterial");
        this.decayTime = aNBT.getLong("decay");
        super.loadNBTData(aNBT);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        ResourceLocation rl = new ResourceLocation(MainMod.MOD_ID, "hatch.RadOn");
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(rl, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return BartWorksRecipeMaps.radioHatchFakeRecipes;
    }

    private static final int RADIATION_SHUTTER_WINDOW_ID = 999;

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        IPanelHandler popupPanel = syncManager.panel("popup", (manager, handler) -> createShutterUI(syncManager), true);
        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.syncValue("mass", new IntSyncValue(() -> mass, value -> mass = (byte) value));
        syncManager.syncValue("sievert", new IntSyncValue(() -> sievert, value -> sievert = value));
        syncManager.syncValue("color0", new IntSyncValue(() -> colorForGUI[0], c -> colorForGUI[0] = (short) c));
        syncManager.syncValue("color1", new IntSyncValue(() -> colorForGUI[1], c -> colorForGUI[1] = (short) c));
        syncManager.syncValue("color2", new IntSyncValue(() -> colorForGUI[2], c -> colorForGUI[2] = (short) c));
        syncManager.syncValue("coverage", 0, new IntSyncValue(() -> coverage, value -> coverage = (byte) value));

        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager)
            .doesAddGregTechLogo(false)
            .build()
            .child(
                gridTemplate1by1(
                    index -> new ItemSlot().slot(new ModularSlot(inventoryHandler, index).slotGroup("item_inv"))))
            .child(
                GTGuiTextures.PICTURE_SIEVERT_CONTAINER.asWidget()
                    .pos(61, 9)
                    .size(56, 24))
            .child(
                new ProgressWidget().progress(() -> this.getSievert() / 148f)
                    .direction(ProgressWidget.Direction.RIGHT)
                    .texture(GTGuiTextures.PROGRESSBAR_SIEVERT, 24)
                    .pos(65, 13)
                    .size(48, 16))
            .child(
                GTGuiTextures.PICTURE_DECAY_TIME_INSIDE.asWidget()
                    .pos(124, 18)
                    .size(16, 48))
            .child(new IDrawable.DrawableWidget((context, x, y, width, height, widgetTheme) -> {
                if (MTERadioHatch.this.decayTime > 0) {
                    int drawableHeight = MathUtils.ceilInt(
                        48 * ((MTERadioHatch.this.decayTime - MTERadioHatch.this.timer % MTERadioHatch.this.decayTime)
                            / (float) MTERadioHatch.this.decayTime));
                    new com.cleanroommc.modularui.drawable.Rectangle()
                        .setColor(
                            com.cleanroommc.modularui.utils.Color.rgb(
                                MTERadioHatch.this.colorForGUI[0],
                                MTERadioHatch.this.colorForGUI[1],
                                MTERadioHatch.this.colorForGUI[2]))
                        .draw(context, new Area(0, 48 - drawableHeight, 16, drawableHeight), widgetTheme);
                }
            }).pos(124, 18)
                .size(18, 48))
            .child(createDurationMeterContainer(syncManager))
            .child(
                IKey.dynamic(() -> StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", this.getMass()))
                    .alignment(com.cleanroommc.modularui.utils.Alignment.Center)
                    .asWidget()
                    .pos(65, 62))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", this.getSievert()))
                    .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .asWidget()
                    .pos(60, 72)
                    .size(80, 8))
            .child(new com.cleanroommc.modularui.widgets.ButtonWidget<>().onMousePressed(mouseButton -> {
                popupPanel.openPanel();
                return popupPanel.isPanelOpen();
            })
                .background(GTGuiTextures.BUTTON_STANDARD, GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.add("Radiation Shutter"))
                .pos(153, 5)
                .size(18, 18))
            .child(
                GTGuiTextures.PICTURE_BARTWORKS_LOGO_STANDARD.asWidget()
                    .pos(10, 53)
                    .size(47, 21))
            .bindPlayerInventory();
    }

    private ModularPanel createShutterUI(PanelSyncManager syncManager) {
        IntSyncValue coverageSyncer = (IntSyncValue) syncManager.getSyncHandler("coverage:0");
        return createPopUpPanel("bw:radio_hatch_shutter", false, false).size(176, 107)
            .child(
                IKey.str("Radiation Shutter Control")
                    .color(this.COLOR_TITLE.get())
                    .asWidget()
                    .pos(10, 9))
            .child(
                GTGuiTextures.PICTURE_RADIATION_SHUTTER_FRAME.asWidget()
                    .pos(14, 27)
                    .size(55, 54))
            .child(
                new ProgressWidget().progress(() -> 1 - ((double) this.coverage / 100D))
                    .texture(GTGuiTextures.PICTURE_TRANSPARENT, GTGuiTextures.PICTURE_RADIATION_SHUTTER_INSIDE, 50)
                    .direction(ProgressWidget.Direction.UP)
                    .pos(16, 29)
                    .size(51, 50))
            .child(
                new TextFieldWidget().setNumbers(0, 100)
                    .value(new StringSyncValue(coverageSyncer::getStringValue, coverageSyncer::setStringValue))
                    .setTextColor(com.cleanroommc.modularui.utils.Color.WHITE.darker(1))
                    .setTextAlignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                    .pos(86, 27)
                    .size(30, 12))
            .child(
                IKey.str("%")
                    .asWidget()
                    .pos(118, 31));
    }

    private IWidget createDurationMeterContainer(PanelSyncManager syncManager) {
        IWidget widget = GTGuiTextures.PICTURE_DECAY_TIME_CONTAINER.asWidget()
            .tooltipBuilder(
                tooltip -> tooltip.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.tile.radhatch.10.name",
                        this.timer <= 1 ? 0 : (this.decayTime - this.timer) / 20,
                        this.timer <= 1 ? 0 : this.decayTime / 20)))
            .pos(120, 14)
            .size(24, 56);

        LongSyncValue timeSyncHandler = new LongSyncValue(() -> timer, time -> timer = time);
        timeSyncHandler.setChangeListener(widget::markTooltipDirty);
        syncManager.syncValue("decayTime", new LongSyncValue(() -> decayTime, time -> decayTime = time));
        syncManager.syncValue("timer", timeSyncHandler);
        return widget;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(RADIATION_SHUTTER_WINDOW_ID, this::createMui1ShutterWindow);

        this.getBaseMetaTileEntity()
            .add1by1Slot(builder);
        builder.widget(
            new DrawableWidget().setBackground(BWUITextures.PICTURE_SIEVERT_CONTAINER)
                .setPos(61, 9)
                .setSize(56, 24))
            .widget(
                new ProgressBar().setProgress(() -> this.getSievert() / 148f)
                    .setDirection(ProgressBar.Direction.RIGHT)
                    .setTexture(BWUITextures.PROGRESSBAR_SIEVERT, 24)
                    .setPos(65, 13)
                    .setSize(48, 16))
            .widget(
                new DrawableWidget().setBackground(BWUITextures.PICTURE_DECAY_TIME_INSIDE)
                    .setPos(124, 18)
                    .setSize(16, 48))
            .widget(new DrawableWidget() {

                @Override
                public void draw(float partialTicks) {
                    if (MTERadioHatch.this.decayTime > 0) {
                        int height = MathUtils.ceilInt(
                            48 * ((MTERadioHatch.this.decayTime
                                - MTERadioHatch.this.timer % MTERadioHatch.this.decayTime)
                                / (float) MTERadioHatch.this.decayTime));
                        new Rectangle()
                            .setColor(
                                Color.argb(
                                    MTERadioHatch.this.colorForGUI[0],
                                    MTERadioHatch.this.colorForGUI[1],
                                    MTERadioHatch.this.colorForGUI[2],
                                    255))
                            .draw(new Pos2d(0, 48 - height), new Size(16, height), partialTicks);
                    }
                }
            }.dynamicTooltip(
                () -> Collections.singletonList(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.tile.radhatch.10.name",
                        this.timer <= 1 ? 0 : (this.decayTime - this.timer) / 20,
                        this.timer <= 1 ? 0 : this.decayTime / 20)))
                .setPos(124, 18)
                .setSize(16, 48)
                .attachSyncer(
                    new FakeSyncWidget.LongSyncer(() -> this.decayTime, val -> this.decayTime = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.LongSyncer(() -> this.timer, val -> this.timer = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange()))
            .widget(new FakeSyncWidget.ShortSyncer(() -> this.colorForGUI[0], val -> this.colorForGUI[0] = val))
            .widget(new FakeSyncWidget.ShortSyncer(() -> this.colorForGUI[1], val -> this.colorForGUI[1] = val))
            .widget(new FakeSyncWidget.ShortSyncer(() -> this.colorForGUI[2], val -> this.colorForGUI[2] = val))
            .widget(
                new DrawableWidget().setBackground(BWUITextures.PICTURE_DECAY_TIME_CONTAINER)
                    .setPos(120, 14)
                    .setSize(24, 56))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", this.mass))
                    .setTextAlignment(Alignment.Center)
                    .setPos(65, 62))
            .widget(new FakeSyncWidget.ByteSyncer(() -> this.mass, val -> this.mass = val))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", this.getSievert()))
                    .setTextAlignment(Alignment.Center)
                    .setPos(60, 72))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> this.sievert, val -> this.sievert = val))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                if (!widget.isClient()) {
                    widget.getContext()
                        .openSyncedWindow(RADIATION_SHUTTER_WINDOW_ID);
                }
            })
                .addTooltip("Radiation Shutter")
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .setPos(153, 5)
                .setSize(18, 18))
            .widget(
                new ItemDrawable(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(IDMetaTool01.SCREWDRIVER.ID, 1, null, null, null))
                        .asWidget()
                        .setPos(154, 6));
    }

    private ModularWindow createMui1ShutterWindow(EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(176, 107);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.setGuiTint(this.getGUIColorization());

        builder.widget(
            new TextWidget("Radiation Shutter Control").setDefaultColor(this.COLOR_TITLE.get())
                .setPos(10, 9))
            .widget(
                new DrawableWidget().setDrawable(BWUITextures.PICTURE_RADIATION_SHUTTER_FRAME)
                    .setPos(14, 27)
                    .setSize(55, 54))
            .widget(
                new DrawableWidget()
                    .setDrawable(() -> this.coverage < 100 ? BWUITextures.PICTURE_RADIATION_SHUTTER_INSIDE : null)
                    .setPos(16, 29)
                    .setSize(51, 50)
                    .attachSyncer(
                        new FakeSyncWidget.ByteSyncer(this::getCoverage, this::setCoverage),
                        builder,
                        (widget, val) -> widget.setPos(16, 29 + this.coverage / 2)
                            .setSize(51, 50 - this.coverage / 2)))
            .widget(
                new NumericWidget().setSetter(val -> this.coverage = (byte) val)
                    .setGetter(() -> this.coverage)
                    .setBounds(0, 100)
                    .setScrollValues(1, 5, 50)
                    .setTextColor(Color.WHITE.dark(1))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                    .setPos(86, 27)
                    .setSize(30, 12))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(176 - 15, 3));

        return builder.build();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(BWUITextures.PICTURE_BW_LOGO_47X21)
                .setSize(47, 21)
                .setPos(10, 53));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT)
            .setGregTechLogo(GTUITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT);
    }

    public static long calcDecayTicks(int x) {
        long ret;
        if (x == 43) ret = 5000;
        else if (x == 61) ret = 4500;
        else if (x <= 100) ret = MathUtils.ceilLong((8000D * Math.tanh(-x / 20D) + 8000D) * 1000D);
        else ret = MathUtils.ceilLong(8000D * Math.tanh(-x / 65D) + 8000D);
        return ret;
    }
}
