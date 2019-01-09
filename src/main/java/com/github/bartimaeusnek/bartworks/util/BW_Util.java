/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.util;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;

public class BW_Util {

    public static final int STANDART = 0;
    public static final int CLEANROOM = -100;
    public static final int LOWGRAVITY = -200;

    public static byte specialToByte(int aSpecialValue) {
        byte special = 0;
        if (aSpecialValue == (CLEANROOM))
            special = 1;
        else if (aSpecialValue == (LOWGRAVITY))
            special = 2;
        else if (aSpecialValue == (CLEANROOM | LOWGRAVITY)) {
            special = 3;
        }
        return special;
    }

    public static boolean addBlockToMachine(int x, int y, int z, int offsetsize, IGregTechTileEntity aBaseMetaTileEntity, Block block) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * offsetsize;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * offsetsize;

        return aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z).equals(block);
    }

    public static boolean addBlockToMachine(int x, int y, int z, int offsetsize, IGregTechTileEntity aBaseMetaTileEntity, Block block, int damage) {
        byte dmg = (byte) damage;
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * offsetsize;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * offsetsize;

        return aBaseMetaTileEntity.getBlockOffset(xDir + x, y, zDir + z).equals(block) && aBaseMetaTileEntity.getMetaIDOffset(xDir + x, y, zDir + z) == dmg;
    }

    public static int calculateSv(Materials materials) {
        Iterator it = BioVatLogicAdder.RadioHatch.getMaSv().iterator();
        while (it.hasNext()) {
            BioVatLogicAdder.MaterialSvPair pair = (BioVatLogicAdder.MaterialSvPair) it.next();
            if (pair.getMaterials().equals(materials))
                return pair.getSievert();
        }
        return (int) (materials.getProtons() == 43L ? (materials.equals(Materials.NaquadahEnriched) ? 140 : materials.equals(Materials.Naquadria) ? 150 : materials.equals(Materials.Naquadah) ? 130 : 43) : materials.getProtons());
    }

    public static int getMachineVoltageFromTier(int tier) {
        return (int) (30 * Math.pow(4, (tier - 1)));
    }

    public static short[] splitColortoArray(int rgb) {
        return new short[]{(short) ((rgb >> 16) & 0xFF), (short) ((rgb >> 8) & 0xFF), (short) (rgb & 0xFF)};
    }

    public static int getColorFromArray(short[] color) {
        return ((color[0] & 0x0ff) << 16) | ((color[1] & 0x0ff) << 8) | (color[2] & 0x0ff);
    }

    public static int getColorFromArray(int[] color) {
        return ((color[0] & 0x0ff) << 16) | ((color[1] & 0x0ff) << 8) | (color[2] & 0x0ff);
    }

    public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2) {
        return (aStack1 == null && aStack2 == null) || GT_Utility.areStacksEqual(aStack1, aStack2);
    }

    public static byte getByteFromRarity(EnumRarity rarity) {
        if (rarity.equals(EnumRarity.uncommon))
            return 1;
        else if (rarity.equals(EnumRarity.epic))
            return 2;
        else if (rarity.equals(EnumRarity.rare))
            return 3;
        return 0;
    }

    public static EnumRarity getRarityFromByte(byte b) {
        switch (b) {
            case 1:
                return EnumRarity.uncommon;
            case 2:
                return EnumRarity.rare;
            case 3:
                return EnumRarity.epic;
            default:
                return EnumRarity.common;
        }
    }

}
