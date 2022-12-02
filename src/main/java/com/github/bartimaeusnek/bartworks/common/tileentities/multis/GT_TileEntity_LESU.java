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

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import com.github.bartimaeusnek.bartworks.API.modularUI.BW_UITextures;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.bartworks.util.ConnectedBlocksChecker;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GT_TileEntity_LESU extends GT_MetaTileEntity_MultiBlockBase {

    private static final byte TEXID_SIDE = 0;
    private static final byte TEXID_CHARGING = 1;
    private static final byte TEXID_IDLE = 2;
    private static final byte TEXID_EMPTY = 3;
    private static final IIcon[] iIcons = new IIcon[4];
    private static final IIconContainer[] iIconContainers = new IIconContainer[4];
    private static final ITexture[][] iTextures = new ITexture[4][1];
    public ConnectedBlocksChecker connectedcells;
    public final ItemStack[] circuits = new ItemStack[5];
    private final ItemStackHandler circuitsInventoryHandler = new ItemStackHandler(circuits) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private long mStorage;

    public GT_TileEntity_LESU(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.mStorage = ConfigHandler.energyPerCell;
    }

    public GT_TileEntity_LESU(String aName) {
        super(aName);
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public long maxEUStore() {
        return (this.mStorage >= Long.MAX_VALUE - 1 || this.mStorage < 0) ? Long.MAX_VALUE - 1 : this.mStorage;
    }

    @Override
    public long maxAmperesIn() {
        int ret = 0;
        for (int i = 0; i < 5; ++i)
            if (this.circuits[i] != null
                    && this.circuits[i]
                            .getItem()
                            .equals(GT_Utility.getIntegratedCircuit(0).getItem()))
                ret += this.circuits[i].getItemDamage();
        return ret > 0 ? ret : 1;
    }

    @Override
    public long maxAmperesOut() {
        return this.maxAmperesIn();
    }

    @Override
    public long maxEUInput() {

        for (int i = 1; i < GT_Values.V.length; i++) {
            if (this.maxEUOutput() <= GT_Values.V[i] && this.maxEUOutput() > GT_Values.V[i - 1])
                return Math.min(GT_Values.V[i], 32768L);
        }

        return 8;
    }

    @Override
    public long maxEUOutput() {
        return Math.min(Math.max(this.mStorage / ConfigHandler.energyPerCell, 1L), 32768L);
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 1;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 1;
    }

    @Override
    public int dechargerSlotCount() {
        return 1;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_LESU(this.mName);
    }

    @Override
    public String[] getDescription() {
        ArrayList<String> e = new ArrayList<>();
        String[] dsc =
                StatCollector.translateToLocal("tooltip.tile.lesu.0.name").split(";");
        Collections.addAll(e, dsc);
        e.add(StatCollector.translateToLocal("tooltip.tile.lesu.1.name") + " "
                + GT_Utility.formatNumbers(ConfigHandler.energyPerCell) + "EU");
        dsc = StatCollector.translateToLocal("tooltip.tile.lesu.2.name").split(";");
        Collections.addAll(e, dsc);
        e.add(ChatColorHelper.RED + StatCollector.translateToLocal("tooltip.tile.lesu.3.name"));
        e.add(BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get());
        return e.toArray(new String[0]);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {

        for (int i = 0; i < GT_TileEntity_LESU.iTextures.length; i++) {
            GT_TileEntity_LESU.iIcons[i] = aBlockIconRegister.registerIcon(MainMod.MOD_ID + ":LESU_CASING_" + i);
            int finalI = i;
            GT_TileEntity_LESU.iIconContainers[i] = new IIconContainer() {
                @Override
                public IIcon getIcon() {
                    return GT_TileEntity_LESU.iIcons[finalI];
                }

                @Override
                public IIcon getOverlayIcon() {
                    return GT_TileEntity_LESU.iIcons[finalI];
                }

                @Override
                public ResourceLocation getTextureFile() {
                    return new ResourceLocation(MainMod.MOD_ID + ":LESU_CASING_" + finalI);
                }
            };
        }
    }

    public boolean isClientSide() {
        if (this.getWorld() != null)
            return this.getWorld().isRemote
                    ? FMLCommonHandler.instance().getSide() == Side.CLIENT
                    : FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {

        ITexture[] ret = new ITexture[0];

        if (this.isClientSide()) {

            for (int i = 0; i < GT_TileEntity_LESU.iTextures.length; i++) {
                GT_TileEntity_LESU.iTextures[i][0] = TextureFactory.of(
                        GT_TileEntity_LESU.iIconContainers[i], Dyes.getModulation(0, Dyes.MACHINE_METAL.mRGBa));
            }

            if (aSide == aFacing && this.getBaseMetaTileEntity().getUniversalEnergyStored() <= 0)
                ret = GT_TileEntity_LESU.iTextures[GT_TileEntity_LESU.TEXID_EMPTY];
            else if (aSide == aFacing && !aActive) ret = GT_TileEntity_LESU.iTextures[GT_TileEntity_LESU.TEXID_IDLE];
            else if (aSide == aFacing && aActive) ret = GT_TileEntity_LESU.iTextures[GT_TileEntity_LESU.TEXID_CHARGING];
            else ret = GT_TileEntity_LESU.iTextures[GT_TileEntity_LESU.TEXID_SIDE];
        }

        return ret;
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return true;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        if (p_70301_1_ > 1) return this.circuits[(p_70301_1_ - 2)];
        return this.mInventory[p_70301_1_];
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        if (p_70299_1_ < 2) this.mInventory[p_70299_1_] = p_70299_2_;
        else this.circuits[(p_70299_1_ - 2)] = p_70299_2_;
    }

    @Override
    public String getInventoryName() {
        return "L.E.S.U.";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        switch (p_94041_1_) {
            case 0:
            case 1:
                return true;
            default:
                return p_94041_2_ != null
                        && p_94041_2_
                                .getItem()
                                .equals(GT_Utility.getIntegratedCircuit(0).getItem());
        }
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide != this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        this.checkMachine(aBaseMetaTileEntity, null);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            this.mMaxProgresstime = 1;
            if (aTick % 20 == 0) this.checkMachine(aBaseMetaTileEntity, null);
            this.mWrench = true;
            this.mScrewdriver = true;
            this.mSoftHammer = true;
            this.mHardHammer = true;
            this.mSolderingTool = true;
            this.mCrowbar = true;
        }
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        this.mMaxProgresstime = 1;
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setIntArray("customCircuitInv", GT_Utility.stacksToIntArray(this.circuits));
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        int[] stacks = aNBT.getIntArray("customCircuitInv");
        for (int i = 0; i < stacks.length; i++) {
            this.circuits[i] = GT_Utility.intToStack(stacks[i]);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        long startingTime = System.nanoTime();
        this.connectedcells = new ConnectedBlocksChecker();
        this.connectedcells.get_connected(
                aBaseMetaTileEntity.getWorld(),
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getYCoord(),
                aBaseMetaTileEntity.getZCoord(),
                ItemRegistry.BW_BLOCKS[1]);

        if (this.connectedcells.get_meta_of_sideblocks(
                aBaseMetaTileEntity.getWorld(),
                this.getBaseMetaTileEntity().getMetaTileID(),
                new int[] {
                    aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord()
                },
                true)) {
            this.getBaseMetaTileEntity().disableWorking();
            this.getBaseMetaTileEntity().setActive(false);
            this.mStorage = 0;
            this.mMaxProgresstime = 0;
            this.mProgresstime = 0;
            return false;
        }

        this.mEfficiency = this.getMaxEfficiency(null);
        this.mStorage = (ConfigHandler.energyPerCell * this.connectedcells.hashset.size() >= Long.MAX_VALUE - 1
                        || ConfigHandler.energyPerCell * this.connectedcells.hashset.size() < 0)
                ? Long.MAX_VALUE - 1
                : ConfigHandler.energyPerCell * this.connectedcells.hashset.size();
        this.mMaxProgresstime = 1;
        this.mProgresstime = 0;

        this.mCrowbar = true;
        this.mHardHammer = true;
        this.mScrewdriver = true;
        this.mSoftHammer = true;
        this.mSolderingTool = true;
        this.mWrench = true;

        this.getBaseMetaTileEntity().enableWorking();
        this.getBaseMetaTileEntity().setActive(true);

        long finishedTime = System.nanoTime();
        // System.out.println("LESU LookUp: "+((finishedTime - startingTime) / 1000000)+"ms");
        if (finishedTime - startingTime > 5000000)
            MainMod.LOGGER.warn("LESU LookUp took longer than 5ms!(" + (finishedTime - startingTime) + "ns / "
                    + ((finishedTime - startingTime) / 1000000) + "ms) Owner:"
                    + this.getBaseMetaTileEntity().getOwnerName() + " Check at x:"
                    + this.getBaseMetaTileEntity().getXCoord() + " y:"
                    + this.getBaseMetaTileEntity().getYCoord() + " z:"
                    + this.getBaseMetaTileEntity().getZCoord() + " DIM-ID: "
                    + this.getBaseMetaTileEntity().getWorld().provider.dimensionId);
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    public World getWorld() {
        return this.getBaseMetaTileEntity().getWorld();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(new DrawableWidget()
                .setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY)
                .setSize(17, 17)
                .setPos(105, 51));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(new DrawableWidget()
                        .setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                        .setPos(7, 4)
                        .setSize(118, 67))
                .widget(new SlotWidget(new BaseSlot(inventoryHandler, 1) {
                            @Override
                            public int getSlotStackLimit() {
                                return 1;
                            }
                        })
                        .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_IN)
                        .setPos(127, 13))
                .widget(new SlotWidget(new BaseSlot(inventoryHandler, 0) {
                            @Override
                            public int getSlotStackLimit() {
                                return 1;
                            }
                        })
                        .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_CHARGER)
                        .setPos(127, 49));
        for (int i = 0; i < 4; i++) {
            builder.widget(new SlotWidget(circuitsInventoryHandler, i)
                    .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_INT_CIRCUIT)
                    .setPos(151, 4 + i * 18));
        }

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements);
        builder.widget(screenElements);

        builder.widget(new DrawableWidget()
                        .setDrawable(BW_UITextures.PICTURE_STORED_EU_FRAME)
                        .setPos(7, 72)
                        .setSize(118, 7))
                .widget(new ProgressBar()
                        .setProgress(() -> (float) getBaseMetaTileEntity().getStoredEU()
                                / getBaseMetaTileEntity().getEUCapacity())
                        .setDirection(ProgressBar.Direction.RIGHT)
                        .setTexture(BW_UITextures.PROGRESSBAR_STORED_EU_116, 116)
                        .setPos(8, 73)
                        .setSize(116, 5));
    }

    private void drawTexts(DynamicPositionedColumn screenElements) {
        screenElements.setSpace(0).setPos(11, 8);

        screenElements
                .widget(TextWidget.dynamicString(() -> "EU: "
                                + GT_Utility.formatNumbers(
                                        getBaseMetaTileEntity().getStoredEU()))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(TextWidget.dynamicString(() -> "MAX: "
                                + (getBaseMetaTileEntity().isActive()
                                        ? GT_Utility.formatNumbers(
                                                        getBaseMetaTileEntity().getOutputVoltage())
                                                + String.valueOf(ConfigHandler.energyPerCell)
                                                        .substring(1)
                                        : Integer.toString(0)))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(TextWidget.dynamicString(() -> "MAX EU/t IN: "
                                + GT_Utility.formatNumbers(
                                        getBaseMetaTileEntity().getInputVoltage()))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(TextWidget.dynamicString(() -> "EU/t OUT: "
                                + GT_Utility.formatNumbers(
                                        getBaseMetaTileEntity().getOutputVoltage()))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(TextWidget.dynamicString(() -> "AMP/t IN/OUT: "
                                + GT_Utility.formatNumbers(
                                        getBaseMetaTileEntity().getInputAmperage()))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()))
                .widget(new TextWidget(Text.localised("tooltip.LESU.0.name"))
                        .setDefaultColor(Color.YELLOW.getRGB())
                        .setEnabled(widget -> maxEUStore() >= Long.MAX_VALUE - 1))
                .widget(new TextWidget(Text.localised("tooltip.LESU.1.name"))
                        .setDefaultColor(Color.RED.getRGB())
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()));
    }
}
