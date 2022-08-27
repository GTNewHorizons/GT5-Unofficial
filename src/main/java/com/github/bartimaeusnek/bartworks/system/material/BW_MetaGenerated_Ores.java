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

import com.github.bartimaeusnek.bartworks.util.MathUtils;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BW_MetaGenerated_Ores extends BW_MetaGenerated_Blocks {

    public BW_MetaGenerated_Ores(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_, tileEntity, blockName);
        this.blockTypeLocalizedName = GT_LanguageManager.addStringLocalization(
                "bw.blocktype." + OrePrefixes.ore,
                OrePrefixes.ore.mLocalizedMaterialPre + "%material" + OrePrefixes.ore.mLocalizedMaterialPost);
    }

    protected void doRegistrationStuff(Werkstoff w) {
        if (w != null) {
            if (!w.hasItemType(OrePrefixes.ore) || ((w.getGenerationFeatures().blacklist & 0b1000) != 0)) return;
            GT_ModHandler.addValuableOre(this, w.getmID(), 1);
        }
    }

    public static boolean setOreBlock(
            World aWorld, int aX, int aY, int aZ, int aMetaData, boolean air, Block block, int[] aBlockMeta) {
        if (!air) {
            aY = MathUtils.clamp(aY, 1, aWorld.getActualHeight());
        }

        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = WerkstoffLoader.BWOres;
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

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
        return Blocks.stone.getIcon(0, 0);
    }

    public int getHarvestLevel(int metadata) {
        return 3;
    }

    @Override
    public String getUnlocalizedName() {
        return "bw.blockores.01";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
        for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
            if ((tMaterial != null)
                    && tMaterial.hasItemType(OrePrefixes.ore)
                    && ((tMaterial.getGenerationFeatures().blacklist & 0x8) == 0)) {
                aList.add(new ItemStack(aItem, 1, tMaterial.getmID()));
            }
        }
    }

    @SuppressWarnings("unused")
    private boolean checkForAir(IBlockAccess aWorld, int aX, int aY, int aZ) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    if (aWorld.getBlock(aX + x, aY + y, aZ + z).isAir(aWorld, aX + x, aY + y, aZ + z)) return true;
                }
            }
        }
        return false;
    }
}
