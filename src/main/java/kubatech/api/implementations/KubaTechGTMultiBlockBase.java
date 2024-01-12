/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.implementations;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static kubatech.api.Variables.ln2;
import static kubatech.api.Variables.ln4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.builder.UIBuilder;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import kubatech.Tags;

public abstract class KubaTechGTMultiBlockBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    @Deprecated
    public final int mEUt = 0;

    @SuppressWarnings("unchecked")
    protected static <K extends KubaTechGTMultiBlockBase<?>> UIInfo<?, ?> createKTMetaTileEntityUI(
        KTContainerConstructor<K> containerConstructor) {
        return UIBuilder.of()
            .container((player, world, x, y, z) -> {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof BaseMetaTileEntity) {
                    IMetaTileEntity mte = ((BaseMetaTileEntity) te).getMetaTileEntity();
                    if (!(mte instanceof KubaTechGTMultiBlockBase)) return null;
                    final UIBuildContext buildContext = new UIBuildContext(player);
                    final ModularWindow window = ((ITileWithModularUI) te).createWindow(buildContext);
                    return containerConstructor.of(new ModularUIContext(buildContext, te::markDirty), window, (K) mte);
                }
                return null;
            })
            .gui(((player, world, x, y, z) -> {
                if (!world.isRemote) return null;
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof BaseMetaTileEntity) {
                    IMetaTileEntity mte = ((BaseMetaTileEntity) te).getMetaTileEntity();
                    if (!(mte instanceof KubaTechGTMultiBlockBase)) return null;
                    final UIBuildContext buildContext = new UIBuildContext(player);
                    final ModularWindow window = ((ITileWithModularUI) te).createWindow(buildContext);
                    return new ModularGui(
                        containerConstructor.of(new ModularUIContext(buildContext, null), window, (K) mte));
                }
                return null;
            }))
            .build();
    }

    @FunctionalInterface
    protected interface KTContainerConstructor<T extends KubaTechGTMultiBlockBase<?>> {

        ModularUIContainer of(ModularUIContext context, ModularWindow mainWindow, T multiBlock);
    }

    protected KubaTechGTMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected KubaTechGTMultiBlockBase(String aName) {
        super(aName);
    }

    /**
     * Enables infinite overclocking (will give more outputs with more energy past 1 tick) Currently doesn't support
     * recipe inputs
     *
     * @return If this supports infinite overclock
     */
    protected boolean isOverclockingInfinite() {
        return false;
    }

    /**
     * @return The minimum amount of ticks this multiblock can overclock to
     */
    protected int getOverclockTimeLimit() {
        return 1;
    }

    @Override
    protected void calculateOverclockedNessMultiInternal(long aEUt, int aDuration, int mAmperage, long maxInputVoltage,
        boolean perfectOC) {
        calculateOverclock(aEUt, aDuration, getMaxInputEu(), perfectOC);
    }

    /**
     * @param aEUt       Recipe EU/t
     * @param aDuration  Recipe duration (in ticks)
     * @param maxInputEU The amount of energy we want to overclock to
     * @param isPerfect  Is this overclock perfect ?
     * @return The amount of overclocks
     */
    protected int calculateOverclock(long aEUt, int aDuration, final long maxInputEU, final boolean isPerfect) {
        final int minDuration = getOverclockTimeLimit();
        int tiers = (int) (Math.log((double) maxInputEU / (double) aEUt) / ln4);
        if (tiers <= 0) {
            this.lEUt = aEUt;
            this.mMaxProgresstime = aDuration;
            return 0;
        }
        int durationTiers = (int) Math
            .ceil(Math.log((double) aDuration / (double) minDuration) / (isPerfect ? ln4 : ln2));
        if (durationTiers < 0) durationTiers = 0; // We do not support downclocks (yet)
        if (durationTiers > tiers) durationTiers = tiers;
        if (!isOverclockingInfinite()) {
            tiers = durationTiers;
            if (tiers == 0) {
                this.lEUt = aEUt;
                this.mMaxProgresstime = aDuration;
                return 0;
            }
            this.lEUt = aEUt << (tiers << 1);
            aDuration >>= isPerfect ? (tiers << 1) : tiers;
            if (aDuration < minDuration) aDuration = minDuration;
            this.mMaxProgresstime = aDuration;
            return tiers;
        }
        this.lEUt = aEUt << (tiers << 1);
        aDuration >>= isPerfect ? (durationTiers << 1) : durationTiers;
        int dMulti = tiers - durationTiers;
        if (dMulti > 0) {
            dMulti = 1 << (isPerfect ? (dMulti << 1) : dMulti);
            // TODO: Use more inputs???
            for (ItemStack mOutputItem : this.mOutputItems) mOutputItem.stackSize *= dMulti;
            for (FluidStack mOutputFluid : this.mOutputFluids) mOutputFluid.amount *= dMulti;
        }
        if (aDuration < minDuration) aDuration = minDuration;
        this.mMaxProgresstime = aDuration;
        return tiers;
    }

    protected int calculateOverclock(long aEUt, int aDuration, boolean isPerfect) {
        return calculateOverclock(aEUt, aDuration, getMaxInputEu(), isPerfect);
    }

    protected int calculateOverclock(long aEUt, int aDuration) {
        return calculateOverclock(aEUt, aDuration, false);
    }

    protected int calculatePerfectOverclock(long aEUt, int aDuration) {
        return calculateOverclock(aEUt, aDuration, true);
    }

    public int getVoltageTier() {
        return (int) getVoltageTierExact();
    }

    public double getVoltageTierExact() {
        return Math.log((double) getMaxInputEu() / 8d) / ln4 + 1e-8d;
    }

    protected boolean tryOutputAll(List<ItemStack> list) {
        return tryOutputAll(list, l -> Collections.singletonList((ItemStack) l));
    }

    protected boolean tryOutputAll(List<?> list, Function<Object, List<ItemStack>> mappingFunction) {
        if (list == null || list.isEmpty() || mappingFunction == null) return false;
        int emptySlots = 0;
        boolean ignoreEmptiness = false;
        for (GT_MetaTileEntity_Hatch_OutputBus i : mOutputBusses) {
            if (i instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
                ignoreEmptiness = true;
                break;
            }
            for (int j = 0; j < i.getSizeInventory(); j++)
                if (i.isValidSlot(j)) if (i.getStackInSlot(j) == null) emptySlots++;
        }
        if (emptySlots == 0 && !ignoreEmptiness) return false;
        boolean wasSomethingRemoved = false;
        while (!list.isEmpty()) {
            List<ItemStack> toOutputNow = mappingFunction.apply(list.get(0));
            if (!ignoreEmptiness && emptySlots < toOutputNow.size()) break;
            emptySlots -= toOutputNow.size();
            list.remove(0);
            wasSomethingRemoved = true;
            for (ItemStack stack : toOutputNow) {
                addOutput(stack);
            }
        }
        return wasSomethingRemoved;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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

    // UI stuff

    public static final UITexture PICTURE_KUBATECH_LOGO = UITexture.fullImage(Tags.MODID, "gui/logo_13x15_dark");

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(PICTURE_KUBATECH_LOGO)
                .setSize(13, 15)
                .setPos(191 - 13, 86 - 15)
                .addTooltip(new Text(Tags.MODNAME).color(Color.GRAY.normal))
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
    }

    protected List<SlotWidget> slotWidgets = new ArrayList<>(1);

    public void createInventorySlots() {
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1);
        inventorySlot.setBackground(GT_UITextures.SLOT_DARK_GRAY);
        slotWidgets.add(inventorySlot);
    }

    @Override
    public Pos2d getPowerSwitchButtonPos() {
        return new Pos2d(174, 166 - (slotWidgets.size() * 18));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85));

        slotWidgets.clear();
        createInventorySlots();

        Column slotsColumn = new Column();
        for (int i = slotWidgets.size() - 1; i >= 0; i--) {
            slotsColumn.widget(slotWidgets.get(i));
        }
        builder.widget(
            slotsColumn.setAlignment(MainAxisAlignment.END)
                .setPos(173, 167 - 1));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, slotWidgets.size() > 0 ? slotWidgets.get(0) : null);
        builder.widget(screenElements);

        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createInputSeparationButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder));

        DynamicPositionedRow configurationElements = new DynamicPositionedRow();
        addConfigurationWidgets(configurationElements, buildContext);

        builder.widget(
            configurationElements.setSpace(2)
                .setAlignment(MainAxisAlignment.SPACE_BETWEEN)
                .setPos(getRecipeLockingButtonPos().add(18, 0)));
    }

    protected void addConfigurationWidgets(DynamicPositionedRow configurationElements, UIBuildContext buildContext) {

    }

    protected static String voltageTooltipFormatted(int tier) {
        return GT_Values.TIER_COLORS[tier] + GT_Values.VN[tier] + EnumChatFormatting.GRAY;
    }

    protected final Function<Widget, Boolean> isFixed = widget -> getIdealStatus() == getRepairStatus() && mMachine;
    protected static final Function<Integer, IDrawable> toggleButtonTextureGetter = val -> val == 0
        ? GT_UITextures.OVERLAY_BUTTON_CROSS
        : GT_UITextures.OVERLAY_BUTTON_CHECKMARK;
    protected static final Function<Integer, IDrawable[]> toggleButtonBackgroundGetter = val -> new IDrawable[] {
        val == 0 ? GT_UITextures.BUTTON_STANDARD : GT_UITextures.BUTTON_STANDARD_PRESSED };
}
