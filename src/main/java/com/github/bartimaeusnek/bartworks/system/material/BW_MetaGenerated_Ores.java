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

package com.github.bartimaeusnek.bartworks.system.material;

import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items.metaTab;

public class BW_MetaGenerated_Ores extends BW_TileEntityContainer {

    public static ThreadLocal<BW_MetaGeneratedOreTE> mTemporaryTileEntity = new ThreadLocal();

    public BW_MetaGenerated_Ores(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_, tileEntity, blockName);

        this.setHardness(5.0F);
        this.setResistance(5.0F);
        this.setBlockTextureName("stone");
        this.setCreativeTab(metaTab);
        for (Werkstoff w : Werkstoff.werkstoffHashSet)
            doRegistrationStuff(w);
    }

    protected void doRegistrationStuff(Werkstoff w){
        if (w != null) {
            if ((w.getGenerationFeatures().toGenerate & 0b1000) == 0 || ((w.getGenerationFeatures().blacklist & 0b1000) != 0))
                return;
            GT_ModHandler.addValuableOre(this, w.getmID(), 1);
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + "." + w.getmID() + ".name", w.getDefaultName() + OrePrefixes.ore.mLocalizedMaterialPost);
        }
    }

    public static boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean air, Block block, int[] aBlockMeta) {
        if (!air) {
            aY = Math.min(aWorld.getActualHeight(), Math.max(aY, 1));
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

    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public String getHarvestTool(int metadata) {
        return "pickaxe";
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public int getRenderType() {
        if (BW_Renderer_Block_Ores.INSTANCE == null) {
            return super.getRenderType();
        }
        return BW_Renderer_Block_Ores.INSTANCE.mRenderID;
    }

    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((tTileEntity instanceof BW_MetaGeneratedOreTE))) {
            return ((BW_MetaGeneratedOreTE) tTileEntity).mMetaData;
        }
        return 0;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tTileEntity = world.getTileEntity(x, y, z);
        if ((tTileEntity instanceof BW_MetaGeneratedOreTE)) {
            BW_MetaGenerated_Ores.mTemporaryTileEntity.set((BW_MetaGeneratedOreTE) tTileEntity);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof BW_MetaGeneratedOreTE)) {
            return ((BW_MetaGeneratedOreTE) tTileEntity).getDrops(WerkstoffLoader.BWOres,aFortune);
        }
        return BW_MetaGenerated_Ores.mTemporaryTileEntity.get() == null ? new ArrayList() : BW_MetaGenerated_Ores.mTemporaryTileEntity.get().getDrops(WerkstoffLoader.BWOres,aFortune);
    }

    public int getHarvestLevel(int metadata) {
        return 3;
    }

    @Override
    public String getUnlocalizedName() {
        return "bw.blockores.01";
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList) {
        for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
            if ((tMaterial != null) && ((tMaterial.getGenerationFeatures().toGenerate & 0x8) != 0) && ((tMaterial.getGenerationFeatures().blacklist & 0x8) == 0)) {
                aList.add(new ItemStack(aItem, 1, tMaterial.getmID()));
            }
        }
    }

//    @Override
//    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block p_149695_5_) {
//        if ((!aWorld.isRemote || this.checkForAir(aWorld,aX,aY,aZ)) && aWorld.getTileEntity(aX, aY, aZ) instanceof BW_MetaGeneratedOreTE)
//            ((BW_MetaGeneratedOreTE)aWorld.getTileEntity(aX, aY, aZ)).sendPacket();
//    }
//
//    @Override
//    public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int tileX, int tileY, int tileZ) {
//        if ((FMLCommonHandler.instance().getEffectiveSide().isServer() || this.checkForAir(aWorld,aX,aY,aZ)) && aWorld.getTileEntity(aX, aY, aZ) instanceof BW_MetaGeneratedOreTE)
//            ((BW_MetaGeneratedOreTE)aWorld.getTileEntity(aX, aY, aZ)).sendPacket();
//    }

    private boolean checkForAir(IBlockAccess aWorld, int aX, int aY, int aZ){
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)
                        continue;
                    if (aWorld.getBlock(aX+x,aY+y,aZ+z).isAir(aWorld,aX+x,aY+y,aZ+z))
                        return true;
                }
            }
        }
        return false;
    }
}