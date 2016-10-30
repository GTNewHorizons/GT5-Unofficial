package com.detrav.blocks;

/**
 * Created by Detrav on 30.10.2016.
 */
import com.detrav.DetravScannerMod;
import com.detrav.utils.DetravCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DetravBlockAnvil extends BlockAnvil
{

    public DetravBlockAnvil()
    {
        super();
        setBlockName("detrav_anvil");
        this.setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            player.addChatMessage(new ChatComponentText("is remote"));
            return true;
        }
        else
        {
            //p_149727_5_.displayGUIAnvil(p_149727_2_, p_149727_3_, p_149727_4_);
            player.addChatMessage(new ChatComponentText("not remote"));
            return true;
        }
    }

}