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

package com.github.bartimaeusnek.crossmod.tectech.helper;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.structure.IStructureElement;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.IHeatingCoil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CoilAdder<MultiBlock extends GT_MetaTileEntity_MultiblockBase_EM & IHasCoils> implements IStructureElement<MultiBlock> {

    private CoilAdder(){}

    @SuppressWarnings("rawtypes")
    private static final CoilAdder INSTANCE = new CoilAdder();

    @SuppressWarnings("unchecked")
    public static <MultiBlock extends GT_MetaTileEntity_MultiblockBase_EM & IHasCoils> CoilAdder<MultiBlock> getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public boolean check(MultiBlock multiBlock, World world, int x, int y, int z) {
        Block coil = world.getBlock(x, y, z);
        if (!(coil instanceof IHeatingCoil))
            return false;
        int meta = world.getBlockMetadata(x, y, z);
        HeatingCoilLevel heat = ((IHeatingCoil) coil).getCoilHeat(meta);
        if (multiBlock.getCoilHeat() == HeatingCoilLevel.None)
            multiBlock.setCoilHeat(heat);
        return multiBlock.getCoilHeat() == heat;
    }

    @Override
    public boolean placeBlock(MultiBlock MultiBlock, World world, int x, int y, int z, ItemStack trigger) {
        world.setBlock(x, y, z, GregTech_API.sBlockCasings5, 0, 2);
        return true;
    }

    @Override
    public boolean spawnHint(MultiBlock MultiBlock, World world, int x, int y, int z, ItemStack trigger) {
        TecTech.proxy.hint_particle(world, x, y, z, GregTech_API.sBlockCasings5, 0);
        return true;
    }
}