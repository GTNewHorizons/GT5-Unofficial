package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

import static com.github.technus.tectech.auxiliary.Reference.MODID;

/**
 * Created by Tec on 15.03.2017.
 */
public class ParametrizerMemoryCard extends Item {
    public static ParametrizerMemoryCard INSTANCE;
    public static IIcon locked,unlocked;

    private ParametrizerMemoryCard() {
        super();
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("em.parametrizerMemoryCard");
        setTextureName(MODID + ":itemParametrizerMemoryCardUnlocked");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity != null && tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null && metaTE instanceof GT_MetaTileEntity_Hatch_Param) {
                    GT_MetaTileEntity_Hatch_Param parametrizer = ((GT_MetaTileEntity_Hatch_Param) metaTE);
                    if(aStack.getTagCompound()==null) aStack.setTagCompound(new NBTTagCompound());
                    NBTTagCompound tNBT=aStack.getTagCompound();
                    if (aStack.getItemDamage()==1) {
                        //write to parametrizer
                        parametrizer.param = tNBT.getInteger("param");
                        parametrizer.value0i = tNBT.getInteger("value0i");
                        parametrizer.value1i = tNBT.getInteger("value1i");
                        parametrizer.usesFloat = tNBT.getInteger("extra");
                    } else {
                        //read from parametrizer
                        tNBT.setInteger("param", parametrizer.param);
                        tNBT.setInteger("value0i", parametrizer.value0i);
                        tNBT.setInteger("value1i", parametrizer.value1i);
                        tNBT.setInteger("extra", parametrizer.usesFloat);
                    }
                    return true;
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aPlayer instanceof EntityPlayerMP && aPlayer.isSneaking()) {
            aStack.stackSize = 1;
            if(aStack.getItemDamage()==1) {
                aStack.setItemDamage(0);
            }else{
                aStack.setItemDamage(1);
            }
            return aStack;
        }
        return aStack;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        aList.add(CommonValues.BASS_MARK);
        aList.add("Stores Parameters");

        if(aStack.getItemDamage()==1) {
            aList.add(EnumChatFormatting.BLUE + "Use on Parametrizer to configure it");
        }else{
            aList.add(EnumChatFormatting.BLUE + "Use on Parametrizer to save parameters");
        }
        aList.add(EnumChatFormatting.BLUE + "Sneak right click to lock/unlock");

        int temp;
        if(tNBT!=null && tNBT.hasKey("param")) {
            aList.add("Hatch ID: "+EnumChatFormatting.AQUA + tNBT.getInteger("param"));
            temp=tNBT.getInteger("value0i");
            aList.add("Value 0|I: "+EnumChatFormatting.AQUA + temp);
            aList.add("Value 0|F: "+EnumChatFormatting.AQUA + Float.intBitsToFloat(temp));
            aList.add("Value 0|B: "+EnumChatFormatting.AQUA + Util.intToShortString(temp));
            temp=tNBT.getInteger("value1i");
            aList.add("Value 1|I: "+EnumChatFormatting.AQUA + temp);
            aList.add("Value 1|F: "+EnumChatFormatting.AQUA + Float.intBitsToFloat(temp));
            aList.add("Value 1|B: "+EnumChatFormatting.AQUA + Util.intToShortString(temp));
        }

    }

    public static void run() {
        INSTANCE = new ParametrizerMemoryCard();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        locked=iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        unlocked=this.itemIcon = iconRegister.registerIcon(this.getIconString());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if(damage==1) {
            return locked;
        }else{
            return unlocked;
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }
}
