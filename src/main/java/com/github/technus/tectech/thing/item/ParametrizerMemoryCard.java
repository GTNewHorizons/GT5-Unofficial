package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
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
                        parametrizer.data0 = tNBT.getInteger("value1");
                        parametrizer.data1 = tNBT.getInteger("value2");
                        parametrizer.value0f = tNBT.getFloat("value1f");
                        parametrizer.value1f = tNBT.getFloat("value2f");
                        parametrizer.extra = tNBT.getInteger("exponent");
                    } else {
                        //read from parametrizer
                        tNBT.setInteger("param", parametrizer.param);
                        tNBT.setInteger("value1", parametrizer.data0);
                        tNBT.setInteger("value2", parametrizer.data1);
                        tNBT.setFloat("value2f", parametrizer.value1f);
                        tNBT.setFloat("value1f", parametrizer.value0f);
                        tNBT.setInteger("exponent", parametrizer.extra);
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

        if(tNBT!=null && tNBT.hasKey("param")) {
            aList.add("Hatch ID: "+EnumChatFormatting.AQUA + tNBT.getInteger("param"));
            aList.add("Value 0|F: "+EnumChatFormatting.AQUA + tNBT.getFloat("value1f"));
            aList.add("Value 1|F: "+EnumChatFormatting.AQUA + tNBT.getFloat("value2f"));
            aList.add("Data 0|I: "+EnumChatFormatting.AQUA + tNBT.getInteger("value1"));
            aList.add("Data 1|I: "+EnumChatFormatting.AQUA + tNBT.getInteger("value2"));
            aList.add("Data x|I: "+EnumChatFormatting.AQUA + tNBT.getInteger("exponent"));
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
