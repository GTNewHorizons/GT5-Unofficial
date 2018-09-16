package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_Config;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sun.applet.Main;

public class GT_Destructopack_Item extends GT_Generic_Item
{

    public GT_Destructopack_Item() {
        super("GT2Destructopack","Destructopack","Mobile Trash Bin");
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setHasSubtypes(false);
        this.setCreativeTab(MainMod.GT2);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        aPlayer.openGui(MainMod.instance, 0, aWorld, 0, 0, 0);
        return aStack;
    }
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        this.mIcon = aIconRegister.registerIcon("bartworks:gt.GT2Destructopack");
    }
}
