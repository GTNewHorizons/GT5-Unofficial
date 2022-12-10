/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import static gregtech.api.enums.GT_Values.ticksBetweenSounds;

import com.github.bartimaeusnek.bartworks.API.modularUI.BW_UITextures;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
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
import com.gtnewhorizons.modularui.common.widget.ProgressBar.Direction;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import java.util.Collections;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GT_MetaTileEntity_RadioHatch extends GT_MetaTileEntity_Hatch implements IAddGregtechLogo {

    private final int cap;
    public int sievert;
    private long timer = 1;
    private long decayTime = 1;
    private short[] colorForGUI = {0x02, 0x02, 0x02};
    private byte mass;
    private String material;
    private byte coverage;
    private ItemStack lastUsedItem = null;
    private boolean lastFail = false;
    private GT_Recipe lastRecipe = null;

    public GT_MetaTileEntity_RadioHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[] {
            StatCollector.translateToLocal("tooltip.tile.radhatch.0.name"),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.3.name") + " " + (aTier - 2) + " "
                    + ((aTier - 2) >= 2
                            ? StatCollector.translateToLocal("tooltip.bw.kg.1.name")
                            : StatCollector.translateToLocal("tooltip.bw.kg.0.name")),
            StatCollector.translateToLocal("tooltip.tile.radhatch.1.name"),
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
        });
        this.cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        this.cap = aTier - 2;
    }

    public GT_MetaTileEntity_RadioHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
        this.cap = aTier - 2;
    }

    public int getSievert() {
        return this.sievert - MathUtils.ceilInt((float) this.sievert / 100f * (float) this.coverage);
    }

    public short[] getColorForGUI() {
        if (this.colorForGUI != null) return this.colorForGUI;
        return colorForGUI = new short[] {0xFA, 0xFA, 0xFF};
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
        return new ITexture[] {aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_RadioHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public void updateSlots() {
        if (this.mInventory[0] != null && this.mInventory[0].stackSize <= 0) this.mInventory[0] = null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        BaseMetaTileEntity myMetaTileEntity = ((BaseMetaTileEntity) this.getBaseMetaTileEntity());
        if (myMetaTileEntity.isServerSide()) {

            if (this.mass > 0) {
                ++this.timer;
            }

            if (this.mass > 0) {
                if (this.decayTime == 0 || (this.decayTime > 0 && this.timer % this.decayTime == 0)) {
                    this.mass--;
                    if (this.mass == 0) {
                        this.material = StatCollector.translateToLocal("tooltip.bw.empty.name");
                        this.sievert = 0;
                    }
                    this.timer = 1;
                }
            }

            if (myMetaTileEntity.mTickTimer > (myMetaTileEntity.mLastSoundTick + ticksBetweenSounds)) {
                if (this.sievert > 0) {
                    sendLoopStart((byte) 1);
                    myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
                }
            }

            if (this.mass == 0) {
                ItemStack lStack = this.mInventory[0];

                if (lStack == null) {
                    this.colorForGUI = new short[] {0x37, 0x37, 0x37};
                    return;
                } else {
                    Materials mat = GT_OreDictUnificator.getAssociation(lStack).mMaterial.mMaterial;
                    this.colorForGUI = new short[] {mat.getRGBA()[0], mat.getRGBA()[1], mat.getRGBA()[2]};
                }

                if (this.lastFail && GT_Utility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                    return;
                }

                if (!this.lastFail && this.lastUsedItem != null && this.lastRecipe != null) {
                    if (GT_Utility.areStacksEqual(this.lastUsedItem, lStack, true)) {
                        this.mass = (byte) this.lastRecipe.mDuration;
                        this.decayTime = this.lastRecipe.mSpecialValue;
                        this.sievert = this.lastRecipe.mEUt;
                        this.material = this.lastUsedItem.getDisplayName();
                        lStack.stackSize--;
                        updateSlots();
                    } else {
                        this.lastRecipe = null;
                    }
                }

                if (this.lastRecipe == null || this.lastFail) {
                    this.lastRecipe = BWRecipes.instance
                            .getMappingsFor(BWRecipes.RADHATCH)
                            .findRecipe(
                                    this.getBaseMetaTileEntity(), false, Integer.MAX_VALUE - 7, null, mInventory[0]);
                    if (this.lastRecipe == null) {
                        this.lastFail = true;
                        this.lastUsedItem = this.mInventory[0] == null ? null : this.mInventory[0].copy();
                    } else {
                        if (this.lastRecipe.mDuration > this.cap) {
                            this.lastFail = true;
                            this.lastUsedItem = this.mInventory[0].copy();
                            return;
                        }
                        this.lastFail = false;
                        this.lastUsedItem = this.mInventory[0].copy();
                        this.mass = (byte) this.lastRecipe.mDuration;
                        this.decayTime = this.lastRecipe.mSpecialValue;
                        this.sievert = this.lastRecipe.mEUt;
                        this.material = lStack.getDisplayName();
                        lStack.stackSize--;
                        updateSlots();
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
        if (this.sievert != 0)
            return new String[] {
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
                        + StatCollector.translateToLocal("tooltip.tile.radhatch.9.name")
            };
        else
            return new String[] {
                StatCollector.translateToLocal("tooltip.tile.radhatch.2.name") + " "
                        + StatCollector.translateToLocal("tooltip.bw.empty.name"),
                StatCollector.translateToLocal("tooltip.tile.radhatch.3.name") + " " + "0",
                StatCollector.translateToLocal("tooltip.tile.radhatch.4.name") + " " + "0"
            };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
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
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing()
                && BWRecipes.instance.getMappingsFor(BWRecipes.RADHATCH).containsInput(aStack);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mMass", this.mass);
        aNBT.setByte("mSv", (byte) (this.sievert - 100));
        aNBT.setByte("mCoverage", this.coverage);
        aNBT.setInteger("mTextColor", BW_ColorUtil.getColorFromRGBArray(this.getColorForGUI()));
        if (this.material != null && !this.material.isEmpty()) aNBT.setString("mMaterial", this.material);
        aNBT.setLong("timer", this.timer);
        aNBT.setLong("decay", this.decayTime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.timer = aNBT.getLong("timer");
        this.mass = aNBT.getByte("mMass");
        this.sievert = aNBT.getByte("mSv") + 100;
        this.coverage = aNBT.getByte("mCoverage");
        this.colorForGUI = BW_ColorUtil.splitColorToRBGArray(aNBT.getInteger("mTextColor"));
        this.material = aNBT.getString("mMaterial");
        this.decayTime = aNBT.getLong("decay");
        super.loadNBTData(aNBT);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        ResourceLocation rl = new ResourceLocation(MainMod.MOD_ID, "hatch.RadOn");
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(rl, 10, 1.0F, aX, aY, aZ);
        }
    }

    private static final int RADIATION_SHUTTER_WINDOW_ID = 999;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(RADIATION_SHUTTER_WINDOW_ID, this::createShutterWindow);

        getBaseMetaTileEntity().add1by1Slot(builder);
        builder.widget(new DrawableWidget()
                        .setBackground(BW_UITextures.PICTURE_SIEVERT_CONTAINER)
                        .setPos(61, 9)
                        .setSize(56, 24))
                .widget(new ProgressBar()
                        .setProgress(() -> getSievert() / 148f)
                        .setDirection(Direction.RIGHT)
                        .setTexture(BW_UITextures.PROGRESSBAR_SIEVERT, 24)
                        .setPos(65, 13)
                        .setSize(48, 16))
                .widget(new DrawableWidget()
                        .setBackground(BW_UITextures.PICTURE_DECAY_TIME_INSIDE)
                        .setPos(124, 18)
                        .setSize(16, 48))
                .widget(
                        new DrawableWidget() {
                            @Override
                            public void draw(float partialTicks) {
                                if (decayTime > 0) {
                                    int height = MathUtils.ceilInt(
                                            48 * ((decayTime - timer % decayTime) / (float) decayTime));
                                    new Rectangle()
                                            .setColor(Color.argb(colorForGUI[0], colorForGUI[1], colorForGUI[2], 255))
                                            .draw(new Pos2d(0, 48 - height), new Size(16, height), partialTicks);
                                }
                            }
                        }.dynamicTooltip(() -> Collections.singletonList(StatCollector.translateToLocalFormatted(
                                        "tooltip.tile.radhatch.10.name",
                                        timer <= 1 ? 0 : (decayTime - timer) / 20,
                                        timer <= 1 ? 0 : decayTime / 20)))
                                .setPos(124, 18)
                                .setSize(16, 48)
                                .attachSyncer(
                                        new FakeSyncWidget.LongSyncer(() -> decayTime, val -> decayTime = val),
                                        builder,
                                        (widget, val) -> widget.notifyTooltipChange())
                                .attachSyncer(
                                        new FakeSyncWidget.LongSyncer(() -> timer, val -> timer = val),
                                        builder,
                                        (widget, val) -> widget.notifyTooltipChange()))
                .widget(new FakeSyncWidget.ShortSyncer(() -> colorForGUI[0], val -> colorForGUI[0] = val))
                .widget(new FakeSyncWidget.ShortSyncer(() -> colorForGUI[1], val -> colorForGUI[1] = val))
                .widget(new FakeSyncWidget.ShortSyncer(() -> colorForGUI[2], val -> colorForGUI[2] = val))
                .widget(new DrawableWidget()
                        .setBackground(BW_UITextures.PICTURE_DECAY_TIME_CONTAINER)
                        .setPos(120, 14)
                        .setSize(24, 56))
                .widget(TextWidget.dynamicString(
                                () -> StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.1", mass))
                        .setTextAlignment(Alignment.Center)
                        .setPos(65, 62))
                .widget(TextWidget.dynamicString(() ->
                                StatCollector.translateToLocalFormatted("BW.NEI.display.radhatch.0", getSievert()))
                        .setTextAlignment(Alignment.Center)
                        .setPos(60, 72))
                .widget(new ButtonWidget()
                        .setOnClick((clickData, widget) -> {
                            if (!widget.isClient()) {
                                widget.getContext().openSyncedWindow(RADIATION_SHUTTER_WINDOW_ID);
                            }
                        })
                        .addTooltip("Radiation Shutter")
                        .setBackground(GT_UITextures.BUTTON_STANDARD)
                        .setPos(153, 5)
                        .setSize(18, 18))
                .widget(new ItemDrawable(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, null, null, null))
                        .asWidget()
                        .setPos(154, 6));
    }

    private ModularWindow createShutterWindow(EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(176, 107);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.setGuiTint(getGUIColorization());

        builder.widget(new TextWidget("Radiation Shutter Control")
                        .setDefaultColor(COLOR_TITLE.get())
                        .setPos(10, 9))
                .widget(new DrawableWidget()
                        .setDrawable(BW_UITextures.PICTURE_RADIATION_SHUTTER_FRAME)
                        .setPos(14, 27)
                        .setSize(55, 54))
                .widget(new DrawableWidget()
                        .setDrawable(() -> coverage < 100 ? BW_UITextures.PICTURE_RADIATION_SHUTTER_INSIDE : null)
                        .setPos(16, 29)
                        .setSize(51, 50)
                        .attachSyncer(
                                new FakeSyncWidget.ByteSyncer(this::getCoverage, this::setCoverage),
                                builder,
                                (widget, val) ->
                                        widget.setPos(16, 29 + coverage / 2).setSize(51, 50 - coverage / 2)))
                .widget(new TextFieldWidget()
                        .setSetterInt(val -> coverage = val.byteValue())
                        .setGetterInt(() -> (int) coverage)
                        .setNumbers(0, 100)
                        .setTextColor(Color.WHITE.dark(1))
                        .setOnScrollNumbers(1, 5, 50)
                        .setTextAlignment(Alignment.CenterLeft)
                        .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2))
                        .setPos(86, 27)
                        .setSize(30, 12))
                .widget(ButtonWidget.closeWindowButton(true).setPos(176 - 15, 3));

        return builder.build();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(new DrawableWidget()
                .setDrawable(BW_UITextures.PICTURE_BW_LOGO_47X21)
                .setSize(47, 21)
                .setPos(10, 53));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet()
                .setMainBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT)
                .setGregTechLogo(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }
}
