/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import bartworks.util.MathUtils;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;

public class BWMetaGeneratedOres extends BWMetaGeneratedBlocks {

    public BWMetaGeneratedOres(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_, tileEntity, blockName);
        this.blockTypeLocalizedName = GTLanguageManager.addStringLocalization(
            "bw.blocktype." + OrePrefixes.ore,
            OrePrefixes.ore.mLocalizedMaterialPre + "%material" + OrePrefixes.ore.mLocalizedMaterialPost);
    }

    @Override
    protected void doRegistrationStuff(Werkstoff w) {
        if (w != null) {
            if (!w.hasItemType(OrePrefixes.ore) || (w.getGenerationFeatures().blacklist & 0b1000) != 0) return;
            GTModHandler.addValuableOre(this, w.getmID(), 1);
        }
    }

    public static boolean setOreBlock(World aWorld, int aX, int aY, int aZ, int aMetaData, boolean air, Block block,
        int[] aBlockMeta) {
        if (!air) {
            aY = MathUtils.clamp(aY, 1, aWorld.getActualHeight());
        }

        Block tBlock = aWorld.getBlock(aX, aY, aZ);
        Block tOreBlock = WerkstoffLoader.BWOres;
        if (aMetaData < 0 || tBlock == Blocks.air && !air
            || Block.getIdFromBlock(tBlock) != Block.getIdFromBlock(block)) {
            return false;
        }
        final int aaY = aY;
        if (Arrays.stream(aBlockMeta)
            .noneMatch(e -> e == aWorld.getBlockMetadata(aX, aaY, aZ))) {
            return false;
        }

        aWorld.setBlock(aX, aY, aZ, tOreBlock, aMetaData, 0);
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof BWTileEntityMetaGeneratedOre metaTE) {
            metaTE.mMetaData = (short) aMetaData;
            metaTE.mNatural = true;
        }

        return true;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public int getHarvestLevel(int metadata) {
        return 3;
    }

    @Override
    public String getUnlocalizedName() {
        return "bw.blockores.01";
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
            if (tMaterial != null && tMaterial.hasItemType(OrePrefixes.ore)
                && (tMaterial.getGenerationFeatures().blacklist & 0x8) == 0) {
                aList.add(new ItemStack(aItem, 1, tMaterial.getmID()));
            }
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            BWTileEntityMetaGeneratedOre.shouldSilkTouch = true;
            super.harvestBlock(worldIn, player, x, y, z, meta);

            if (BWTileEntityMetaGeneratedOre.shouldSilkTouch) {
                BWTileEntityMetaGeneratedOre.shouldSilkTouch = false;
            }
            return;
        }

        if (!(player instanceof FakePlayer)) {
            BWTileEntityMetaGeneratedOre.shouldFortune = true;
        }
        super.harvestBlock(worldIn, player, x, y, z, meta);
        if (BWTileEntityMetaGeneratedOre.shouldFortune) {
            BWTileEntityMetaGeneratedOre.shouldFortune = false;
        }
    }
}
