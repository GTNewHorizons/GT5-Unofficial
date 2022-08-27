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

package com.github.bartimaeusnek.bartworks.system.material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BW_MetaGenerated_SmallOres extends BW_MetaGenerated_Ores {
    public BW_MetaGenerated_SmallOres(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_, tileEntity, blockName);
        this.blockTypeLocalizedName = GT_LanguageManager.addStringLocalization(
                "bw.blocktype." + OrePrefixes.oreSmall,
                OrePrefixes.oreSmall.mLocalizedMaterialPre + "%material" + OrePrefixes.oreSmall.mLocalizedMaterialPost);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff w) {
        if (w != null) {
            if (!w.hasItemType(OrePrefixes.ore) || ((w.getGenerationFeatures().blacklist & 0b1000) != 0)) return;
            GT_ModHandler.addValuableOre(this, w.getmID(), 1);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "bw.blockores.02";
    }

    public static boolean setOreBlock(
            World aWorld, int aX, int aY, int aZ, int aMetaData, boolean air, Block block, int[] aBlockMeta) {
        if (!air) {
            aY = Math.min(aWorld.getActualHeight(), Math.max(aY, 1));
        }

        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = WerkstoffLoader.BWSmallOres;
        if (aMetaData < 0 || tBlock == Blocks.air && !air) {
            return false;
        } else {

            if (Block.getIdFromBlock(tBlock) != Block.getIdFromBlock(block)) {
                return false;
            }
            final int aaY = aY;
            if (Arrays.stream(aBlockMeta).noneMatch(e -> e == aWorld.getBlockMetadata(aX, aaY, aZ))) {
                return false;
            }

            aWorld.setBlock(aX, aY, aZ, tOreBlock, aMetaData, 0);
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity instanceof BW_MetaGeneratedOreTE) {
                ((BW_MetaGeneratedOreTE) tTileEntity).mMetaData = (short) aMetaData;
            }

            return true;
        }
    }
}
