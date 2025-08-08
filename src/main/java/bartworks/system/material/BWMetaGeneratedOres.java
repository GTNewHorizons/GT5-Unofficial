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

import static bartworks.system.material.BWMetaGeneratedItems.metaTab;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.render.GTRendererBlock;

public class BWMetaGeneratedOres extends Block implements IBlockWithTextures {

    public final String blockName;
    public final String blockTypeLocalizedName;
    public final StoneType stoneType;
    public final boolean isSmall, isNatural;

    public BWMetaGeneratedOres(String blockName, StoneType stoneType, boolean small, boolean natural) {
        super(Material.rock);

        this.setBlockName(blockName);
        this.setHardness(5.0F);
        this.setResistance(5.0F);
        this.setCreativeTab(metaTab);

        if (small) {
            this.blockTypeLocalizedName = GTLanguageManager.addStringLocalization(
                blockName,
                OrePrefixes.oreSmall.mLocalizedMaterialPre + "%material" + OrePrefixes.oreSmall.mLocalizedMaterialPost);
        } else {
            this.blockTypeLocalizedName = GTLanguageManager.addStringLocalization(
                blockName,
                OrePrefixes.ore.mLocalizedMaterialPre + "%material" + OrePrefixes.ore.mLocalizedMaterialPost);
        }

        this.blockName = blockName;
        this.stoneType = stoneType;
        this.isSmall = small;
        this.isNatural = natural;
    }

    public void registerOredict() {
        Werkstoff.werkstoffHashSet.forEach(this::doRegistrationStuff);
    }

    protected void doRegistrationStuff(Werkstoff w) {
        if (w == null) return;
        if (!w.hasItemType(OrePrefixes.ore)) return;
        if ((w.getGenerationFeatures().blacklist & 0b1000) != 0) return;

        ItemStack self = new ItemStack(this, 1, w.getmID());
        OrePrefixes prefix = isSmall ? OrePrefixes.oreSmall : OrePrefixes.ore;

        GTOreDictUnificator.registerOre(prefix + w.getVarName(), self);
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
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName() {
        return blockName;
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        if (!isNatural) {
            for (Werkstoff tMaterial : Werkstoff.werkstoffHashSet) {
                if (tMaterial != null && tMaterial.hasItemType(OrePrefixes.ore)
                    && (tMaterial.getGenerationFeatures().blacklist & 0x8) == 0) {
                    aList.add(new ItemStack(aItem, 1, tMaterial.getmID()));
                }
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        EntityPlayer harvester = this.harvesters.get();

        boolean doFortune = GTUtility.isRealPlayer(harvester);
        boolean doSilktouch = harvester != null && EnchantmentHelper.getSilkTouchModifier(harvester);

        try (OreInfo<Werkstoff> info = BWOreAdapter.INSTANCE.getOreInfo(this, metadata)) {
            return BWOreAdapter.INSTANCE
                .getOreDrops(ThreadLocalRandom.current(), info, doSilktouch, doFortune ? fortune : 0);
        }
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.mRenderID;
    }

    @Override
    @Nullable
    public ITexture[][] getTextures(int metadata) {
        Werkstoff material = Werkstoff.werkstoffHashMap.get((short) metadata);

        ITexture[] layers;

        if (material != null) {
            ITexture aIconSet = TextureFactory.of(
                material.getTexSet().mTextures[isSmall ? OrePrefixes.oreSmall.mTextureIndex
                    : OrePrefixes.ore.mTextureIndex],
                material.getRGBA());
            layers = new ITexture[] { stoneType.getTexture(0), aIconSet };
        } else {
            layers = new ITexture[] { stoneType.getTexture(0),
                TextureFactory.of(
                    gregtech.api.enums.TextureSet.SET_NONE.mTextures[isSmall ? OrePrefixes.oreSmall.mTextureIndex
                        : OrePrefixes.ore.mTextureIndex]) };
        }

        return new ITexture[][] { layers, layers, layers, layers, layers, layers };
    }
}
