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

package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GT_Rockcutter_Item extends ItemTool implements IElectricItem {
    public static Set mineableBlocks = Sets.newHashSet(Blocks.stone, Blocks.cobblestone, Blocks.sand, Blocks.clay);
    public int mCharge;
    public int mTransfer;
    public int mTier;
    @SideOnly(Side.CLIENT)
    private IIcon icon;
    private int multi;

    public GT_Rockcutter_Item(int aTier) {
        super(2 * aTier, ToolMaterial.EMERALD, mineableBlocks);
        this.mTier = aTier;
        multi = (int) Math.pow(10, (mTier - 1));
        this.mineableBlocks = new HashSet();
        this.maxStackSize = 1;
        this.mCharge = 10000 * multi;
        this.mTransfer = (int) GT_Values.V[mTier];
        this.efficiencyOnProperMaterial = 2.0f * mTier;
        this.setCreativeTab(MainMod.GT2);
        this.setMaxDamage(27 + 10 * multi);
        this.setNoRepair();
        this.setUnlocalizedName("GT_Rockcutter_Item_" + GT_Values.VN[mTier]);
    }

    public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, final List aList, final boolean aF3_H) {
        aList.add("Tier: " + GT_Values.VN[this.mTier]);
        aList.add("Added by" + ChatColorHelper.DARKGREEN + " BartWorks");
    }

    public void onUpdate(ItemStack aStack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (!ElectricItem.manager.canUse(aStack, 500 * multi)) {
            if (aStack.isItemEnchanted()) {
                aStack.getTagCompound().removeTag("ench");
            }
        } else if (!aStack.isItemEnchanted()) {
            aStack.addEnchantment(Enchantment.silkTouch, 3);
        }
    }

    public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        ElectricItem.manager.use(aStack, 0, aPlayer);
        return false;
    }

    public boolean onBlockDestroyed(final ItemStack var1, final World var2, final Block var3, final int var4, final int var5, final int var6, final EntityLivingBase var7) {
        ElectricItem.manager.use(var1, 0, var7);
        if (ElectricItem.manager.canUse(var1, 500 * multi)) {
            ElectricItem.manager.use(var1, 500 * multi, var7);
        } else {
            ElectricItem.manager.discharge(var1, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false);
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return par1Block.getMaterial().equals(Material.glass) || par1Block.getMaterial().equals(Material.clay) || par1Block.getMaterial().equals(Material.packedIce) || par1Block.getMaterial().equals(Material.ice) || par1Block.getMaterial().equals(Material.sand) || par1Block.getMaterial().equals(Material.ground) || par1Block.getMaterial().equals(Material.rock) || this.mineableBlocks.contains(par1Block);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
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
    public int getTier(ItemStack itemStack) {
        return this.mTier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return this.mTransfer;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icon = iconRegister.registerIcon("bartworks:GT_Rockcutter");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }
}
