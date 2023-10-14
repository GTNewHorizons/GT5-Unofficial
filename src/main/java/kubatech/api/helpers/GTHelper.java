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

package kubatech.api.helpers;

import static kubatech.api.Variables.ln4;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.kuba6000.mobsinfo.api.utils.ItemID;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

public class GTHelper {

    public static long getMaxInputEU(GT_MetaTileEntity_MultiBlockBase mte) {
        if (mte instanceof KubaTechGTMultiBlockBase) return ((KubaTechGTMultiBlockBase<?>) mte).getMaxInputEu();
        long rEU = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mte.mEnergyHatches)
            if (tHatch.isValid()) rEU += tHatch.maxEUInput() * tHatch.maxAmperesIn();
        return rEU;
    }

    public static double getVoltageTierD(long voltage) {
        return Math.log((double) voltage / 8L) / ln4;
    }

    public static double getVoltageTierD(GT_MetaTileEntity_MultiBlockBase mte) {
        return Math.log((double) getMaxInputEU(mte) / 8L) / ln4;
    }

    public static int getVoltageTier(long voltage) {
        return (int) getVoltageTierD(voltage);
    }

    public static int getVoltageTier(GT_MetaTileEntity_MultiBlockBase mte) {
        return (int) getVoltageTierD(mte);
    }

    public static class StackableItemSlot {

        public StackableItemSlot(int count, ItemStack stack, ArrayList<Integer> realSlots) {
            this.count = count;
            this.stack = stack;
            this.realSlots = realSlots;
        }

        public final int count;
        public final ItemStack stack;
        public final ArrayList<Integer> realSlots;

        public void write(PacketBuffer buffer) throws IOException {
            buffer.writeVarIntToBuffer(count);
            buffer.writeItemStackToBuffer(stack);
        }

        public static StackableItemSlot read(PacketBuffer buffer) throws IOException {
            return new StackableItemSlot(
                buffer.readVarIntFromBuffer(),
                buffer.readItemStackFromBuffer(),
                new ArrayList<>());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof StackableItemSlot)) return false;
            StackableItemSlot other = (StackableItemSlot) obj;
            return count == other.count && ItemID.createNoCopy(stack, false)
                .hashCode()
                == ItemID.createNoCopy(other.stack, false)
                    .hashCode()
                && realSlots.equals(other.realSlots);
        }
    }
}
