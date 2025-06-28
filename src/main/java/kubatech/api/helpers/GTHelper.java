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

package kubatech.api.helpers;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

public class GTHelper {

    public static long getMaxInputEU(MTEMultiBlockBase mte) {
        if (mte instanceof KubaTechGTMultiBlockBase) return mte.getMaxInputEu();
        long rEU = 0;
        for (MTEHatchEnergy tHatch : mte.mEnergyHatches)
            if (tHatch.isValid()) rEU += tHatch.maxEUInput() * tHatch.maxAmperesIn();
        return rEU;
    }

    public static double getVoltageTierD(long voltage) {
        return GTUtility.log4(voltage / 8);
    }

    public static double getVoltageTierD(MTEMultiBlockBase mte) {
        return GTUtility.log4(getMaxInputEU(mte) / 8L);
    }

    public static int getVoltageTier(long voltage) {
        return (int) getVoltageTierD(voltage);
    }

    public static int getVoltageTier(MTEMultiBlockBase mte) {
        return (int) getVoltageTierD(mte);
    }

    public static class StackableItemSlot {

        public StackableItemSlot(int count, @NotNull ItemStack stack, ArrayList<Integer> realSlots) {
            this.count = count;
            this.stack = stack;
            this.hashcode = ItemId.createNoCopyWithStackSize(stack)
                .hashCode();
            this.realSlots = realSlots;
        }

        public final int count;
        public final ItemStack stack;
        private final int hashcode;
        public final ArrayList<Integer> realSlots;

        public void write(@NotNull PacketBuffer buffer) throws IOException {
            buffer.writeVarIntToBuffer(count);
            buffer.writeItemStackToBuffer(stack);
        }

        public static @NotNull StackableItemSlot read(@NotNull PacketBuffer buffer) throws IOException {
            return new StackableItemSlot(
                buffer.readVarIntFromBuffer(),
                buffer.readItemStackFromBuffer(),
                new ArrayList<>());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof StackableItemSlot other)) return false;
            return count == other.count && hashcode == other.hashcode && realSlots.equals(other.realSlots);
        }
    }
}
