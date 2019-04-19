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

import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;

import static com.github.bartimaeusnek.bartworks.system.material.Werkstoff.werkstoffHashMap;

public class BW_MetaGenerated_Items extends GT_MetaGenerated_Item {

    public static final CreativeTabs metaTab = new CreativeTabs("bartworksMetaMaterials") {

        @Override
        public Item getTabIconItem() {
            return new ItemStack(Blocks.iron_ore).getItem();
        }
    };
    protected final OrePrefixes orePrefixes;
    private final short aNumToGen = (short) werkstoffHashMap.size();

    public BW_MetaGenerated_Items(OrePrefixes orePrefixes) {
        super("bwMetaGenerated" + orePrefixes.name(), (short) 32766, (short) 0);
        this.orePrefixes = orePrefixes;
        this.setCreativeTab(metaTab);
        for (int i = 0; i < aNumToGen; i++) {
            ItemStack tStack = new ItemStack(this, 1, i);
            Werkstoff w = werkstoffHashMap.get((short) i);
            if (w == null || ((w.getGenerationFeatures().toGenerate & orePrefixes.mMaterialGenerationBits) == 0) || ((w.getGenerationFeatures().blacklist & orePrefixes.mMaterialGenerationBits) != 0) )
                continue;
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".name", this.getDefaultLocalization(w));
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".tooltip", w.getToolTip());
            GT_OreDictUnificator.registerOre(this.orePrefixes.name() + w.getDefaultName().replaceAll(" ",""), tStack);
        }
    }

    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        if (orePrefixes == OrePrefixes.dustImpure || orePrefixes == OrePrefixes.dustPure || orePrefixes == OrePrefixes.crushed) {
            int aDamage = aItemEntity.getEntityItem().getItemDamage();
            if ((aDamage >= 0) && (!aItemEntity.worldObj.isRemote)) {
                Werkstoff aMaterial = werkstoffHashMap.get((short) aDamage);
                if ((aMaterial != null) && (aMaterial != Werkstoff.default_null_Werkstoff)) {
                    int tX = MathHelper.floor_double(aItemEntity.posX);
                    int tY = MathHelper.floor_double(aItemEntity.posY);
                    int tZ = MathHelper.floor_double(aItemEntity.posZ);
                    if ((orePrefixes == OrePrefixes.dustImpure) || (orePrefixes == OrePrefixes.dustPure)) {
                        Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                        byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                        if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                            aItemEntity.setEntityItemStack(WerkstoffLoader.getCorresopndingItemStack(OrePrefixes.dust, aMaterial, aItemEntity.getEntityItem().stackSize));
                            aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                            return true;
                        }
                    } else if (orePrefixes == OrePrefixes.crushed) {
                        Block tBlock = aItemEntity.worldObj.getBlock(tX, tY, tZ);
                        byte tMetaData = (byte) aItemEntity.worldObj.getBlockMetadata(tX, tY, tZ);
                        if ((tBlock == Blocks.cauldron) && (tMetaData > 0)) {
                            aItemEntity.setEntityItemStack(WerkstoffLoader.getCorresopndingItemStack(OrePrefixes.crushedPurified, aMaterial, aItemEntity.getEntityItem().stackSize));
                            aItemEntity.worldObj.setBlockMetadataWithNotify(tX, tY, tZ, tMetaData - 1, 3);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
//        String tooltip = GT_LanguageManager.getTranslation(this.getUnlocalizedName(aStack) + ".tooltip");
//        if (!tooltip.isEmpty())
//            aList.add(tooltip);
        if (orePrefixes == OrePrefixes.dustImpure || orePrefixes == OrePrefixes.dustPure) {
            aList.add(GT_LanguageManager.getTranslation("metaitem.01.tooltip.purify"));
        }
        if (orePrefixes == OrePrefixes.crushed)
            aList.add(GT_LanguageManager.getTranslation("metaitem.01.tooltip.purify.2"));
        aList.add(StatCollector.translateToLocal("tooltip.bw.0.name") + ChatColorHelper.DARKGREEN + " BartWorks");
    }

    public String getDefaultLocalization(Werkstoff werkstoff) {
        return werkstoff != null ? orePrefixes.mLocalizedMaterialPre + werkstoff.getDefaultName() + orePrefixes.mLocalizedMaterialPost : Werkstoff.default_null_Werkstoff.getDefaultName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        return GT_LanguageManager.getTranslation(this.getUnlocalizedName(aStack) + ".name");
    }

    @Override
    public final IIconContainer getIconContainer(int aMetaData) {
        return werkstoffHashMap.get((short) aMetaData) == null ? null : werkstoffHashMap.get((short) aMetaData).getTexSet().mTextures[orePrefixes.mTextureIndex];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < aNumToGen; i++) {
            Werkstoff werkstoff = werkstoffHashMap.get((short) i);
            if (werkstoff != null && ((werkstoff.getGenerationFeatures().toGenerate & orePrefixes.mMaterialGenerationBits) != 0) && ((werkstoff.getGenerationFeatures().blacklist & orePrefixes.mMaterialGenerationBits) == 0)) {
                ItemStack tStack = new ItemStack(this, 1, i);
                aList.add(tStack);
            }
        }
        super.getSubItems(var1, aCreativeTab, aList);
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        Werkstoff werkstoff = werkstoffHashMap.get((short) getDamage(aStack));
        return werkstoff == null ? Materials._NULL.mRGBa : werkstoff.getRGBA();
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0)
            return null;
        Werkstoff tMaterial = werkstoffHashMap.get((short) aMetaData);
        if (tMaterial == null)
            return null;
        IIconContainer tIcon = getIconContainer(aMetaData);
        if (tIcon != null)
            return tIcon.getIcon();
        return null;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        return 64;
    }
}
