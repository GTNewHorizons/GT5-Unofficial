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

import static com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items.metaTab;

import com.github.bartimaeusnek.bartworks.client.renderer.BW_Renderer_Block_Ores;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BW_MetaGenerated_Blocks extends BW_TileEntityContainer {

    public static ThreadLocal<BW_MetaGenerated_Block_TE> mTemporaryTileEntity = new ThreadLocal<>();
    protected OrePrefixes _prefixes;
    protected String blockTypeLocalizedName;

    public BW_MetaGenerated_Blocks(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        this(p_i45386_1_, tileEntity, blockName, null);
    }

    public BW_MetaGenerated_Blocks(
            Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName, OrePrefixes types) {
        super(p_i45386_1_, tileEntity, blockName);
        this.setHardness(5.0F);
        this.setResistance(5.0F);
        this.setBlockTextureName("stone");
        this.setCreativeTab(metaTab);
        _prefixes = types;
        if (_prefixes != null) {
            this.blockTypeLocalizedName = GT_LanguageManager.addStringLocalization(
                    "bw.blocktype." + _prefixes,
                    _prefixes.mLocalizedMaterialPre + "%material" + _prefixes.mLocalizedMaterialPost);
        }
        Werkstoff.werkstoffHashSet.forEach(this::doRegistrationStuff);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        // Waste some time to allow the TE to be set, do not use thread sleep here, it doesnt allow for nanoseconds.
        // This will just waste a few cpu cycles to allow the TE to be set
        BW_Util.shortSleep(0);
    }

    @SideOnly(Side.CLIENT)
    public final BW_MetaGenerated_Block_TE getProperTileEntityForRendering() {
        return (BW_MetaGenerated_Block_TE) createNewTileEntity(null, 0);
    }

    protected abstract void doRegistrationStuff(Werkstoff w);

    @Override
    public String getHarvestTool(int metadata) {
        return "pickaxe";
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public int getRenderType() {
        if (BW_Renderer_Block_Ores.INSTANCE == null) return super.getRenderType();
        return BW_Renderer_Block_Ores.INSTANCE.mRenderID;
    }

    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((tTileEntity instanceof BW_MetaGenerated_Block_TE))) {
            return ((BW_MetaGenerated_Block_TE) tTileEntity).mMetaData;
        }
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return ((ITileEntityProvider) this).createNewTileEntity(world, metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tTileEntity = world.getTileEntity(x, y, z);
        if ((tTileEntity instanceof BW_MetaGenerated_Block_TE)) {
            mTemporaryTileEntity.set((BW_MetaGenerated_Block_TE) tTileEntity);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof BW_MetaGenerated_Block_TE)) {
            return ((BW_MetaGenerated_Block_TE) tTileEntity).getDrops(aFortune);
        }
        return mTemporaryTileEntity.get() == null
                ? new ArrayList<>()
                : mTemporaryTileEntity.get().getDrops(aFortune);
    }
}
