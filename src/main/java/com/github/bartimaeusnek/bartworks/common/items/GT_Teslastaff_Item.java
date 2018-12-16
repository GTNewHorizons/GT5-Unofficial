package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
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

import java.util.List;
import java.util.Set;

public class GT_Teslastaff_Item extends ItemTool implements IElectricItem
{
    public double mCharge;
    public double mTransfer;
    public int mTier;
    private static Set effective = Sets.newHashSet(Blocks.web);

    @SideOnly(Side.CLIENT)
    private IIcon icon;
    
    public GT_Teslastaff_Item() {
        super(0, ToolMaterial.GOLD, effective);
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
    public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, final List aList, final boolean aF3_H) {
        aList.add("No warranty!");
        aList.add("Added by"+ ChatColorHelper.DARKGREEN +" BartWorks");
    }

    public boolean hitEntity(ItemStack aStack, EntityLivingBase aTarget, EntityLivingBase aPlayer) {
        if (aTarget instanceof EntityLiving && ElectricItem.manager.canUse(aStack, 9000000)) {
            final EntityLiving tTarget = (EntityLiving)aTarget;
            final EntityLivingBase tPlayer = (EntityLivingBase)aPlayer;
            ElectricItem.manager.use(aStack, 9000000, tPlayer);
            for (int i = 1; i < 5; ++i) {
                if (tTarget.getEquipmentInSlot(i) != null && tTarget.getEquipmentInSlot(i).getItem() instanceof IElectricItem) {
                    tTarget.setCurrentItemOrArmor(i,null);
                }
            }
        }
        return true;
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
