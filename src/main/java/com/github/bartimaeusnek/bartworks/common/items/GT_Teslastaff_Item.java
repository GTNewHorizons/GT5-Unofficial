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

package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class GT_Teslastaff_Item extends ItemTool implements IElectricItem {
    private static final Set<Block> effective = Sets.newHashSet(Blocks.web);
    private final double mCharge;
    private final double mTransfer;
    public int mTier;

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public GT_Teslastaff_Item() {
        super(0, Item.ToolMaterial.GOLD, GT_Teslastaff_Item.effective);
        this.setCreativeTab(MainMod.GT2);
        this.setNoRepair();
        this.mCharge = 10000000D;
        this.mTransfer = 8192D;
        this.mTier = 4;
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setUnlocalizedName("GT_Teslastaff_Item");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(StatCollector.translateToLocal("tooltip.teslastaff.0.name"));
        aList.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
    }

    public boolean hitEntity(ItemStack aStack, EntityLivingBase aTarget, EntityLivingBase aPlayer) {
        if (aTarget instanceof EntityLiving && ElectricItem.manager.canUse(aStack, 9000000)) {
            EntityLiving tTarget = (EntityLiving) aTarget;
            ElectricItem.manager.use(aStack, 9000000, aPlayer);
            for (int i = 1; i < 5; ++i) {
                if (tTarget.getEquipmentInSlot(i) != null
                        && tTarget.getEquipmentInSlot(i).getItem() instanceof IElectricItem) {
                    tTarget.setCurrentItemOrArmor(i, null);
                }
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (this.getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
        if (this.getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return this.mCharge;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return this.mTier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return this.mTransfer;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("bartworks:GT_Teslastaff");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }
}
