/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
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

import static kubatech.api.Variables.ln4;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;

public abstract class KubaTechGTMultiBlockBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    @Deprecated
    public int mEUt;

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

    /**
     * @param aEUt       Recipe EU/t
     * @param aDuration  Recipe duration (in ticks)
     * @param maxInputEU The amount of energy we want to overclock to
     * @param isPerfect  Is this overclock perfect ?
     * @return The amount of overclocks
     */
    protected int calculateOverclock(long aEUt, int aDuration, final long maxInputEU, final boolean isPerfect) {
        final int minDuration = getOverclockTimeLimit();
        int tiers = (int) GTUtility.log4(maxInputEU / aEUt);
        if (tiers <= 0) {
            this.lEUt = aEUt;
            this.mMaxProgresstime = aDuration;
            return 0;
        }
        int durationTiers = isPerfect ? GTUtility.log4ceil(aDuration / minDuration)
            : GTUtility.log2ceil(aDuration / minDuration);
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
        return tryOutputAll(list, l -> Collections.singletonList(l));
    }

    protected <Y> boolean tryOutputAll(@Nullable List<Y> list, @Nullable Function<Y, List<ItemStack>> mappingFunction) {
        if (list == null || list.isEmpty() || mappingFunction == null) return false;
        int emptySlots = 0;
        boolean ignoreEmptiness = false;
        for (MTEHatchOutputBus i : mOutputBusses) {
            if (i instanceof MTEHatchOutputBusME) {
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
            if (toOutputNow == null) {
                list.remove(0);
                continue;
            }
            if (!ignoreEmptiness && emptySlots < toOutputNow.size()) break;
            emptySlots -= toOutputNow.size();
            list.remove(0);
            wasSomethingRemoved = true;
            for (ItemStack stack : toOutputNow) {
                addItemOutput(stack);
            }
        }
        return wasSomethingRemoved;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new KubaTechGTMultiBlockBaseGUI<>(this);
    }

    protected @NotNull String[] getCreditsText() {
        return new String[] {
            translateToLocalFormatted("kubatech.gui.tooltip.contributors.added", GTAuthors.AuthorKuba), };
    }

    protected static @NotNull String voltageTooltipFormatted(int tier) {
        return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + EnumChatFormatting.GRAY;
    }
}
