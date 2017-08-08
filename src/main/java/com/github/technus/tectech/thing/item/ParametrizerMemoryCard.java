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
        setUnlocalizedName("em.parametrizerMemoryCard");
        setTextureName(MODID + ":itemParametrizerMemoryCardUnlocked");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity != null && tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null && metaTE instanceof GT_MetaTileEntity_Hatch_Param) {
                    GT_MetaTileEntity_Hatch_Param parametrizer = ((GT_MetaTileEntity_Hatch_Param) metaTE);
                    
                    if (tNBT.hasKey("cardLocked")) {
                        //write to parametrizer
                        parametrizer.exponent = tNBT.getInteger("exponent");
                        parametrizer.value2 = tNBT.getInteger("value2");
                        parametrizer.value1 = tNBT.getInteger("value1");
                        parametrizer.param = tNBT.getInteger("param");
                        parametrizer.value1f = tNBT.getFloat("value1f");
                        parametrizer.value2f = tNBT.getFloat("value2f");
                    } else {
                        //read from parametrizer
                        tNBT.setInteger("exponent", parametrizer.exponent);
                        tNBT.setInteger("value2", parametrizer.value2);
                        tNBT.setInteger("value1", parametrizer.value1);
                        tNBT.setInteger("param", parametrizer.param);
                        tNBT.setFloat("value1f", parametrizer.value1f);
                        tNBT.setFloat("value2f", parametrizer.value2f);
                    }
                }
                return true;
            } else {
                //change mode
                if(tNBT.hasKey("cardLocked")) {
                    tNBT.removeTag("cardLocked");
                }else{
                    tNBT.setBoolean("cardLocked",true);
                }
                return true;
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        aList.add(CommonValues.bassMark);
        aList.add("Stores Parameters");
        if(tNBT.hasKey("cardLocked")) {
            aList.add(EnumChatFormatting.BLUE + "Use on Parametrizer to load parameters");
        }else{
            aList.add(EnumChatFormatting.BLUE + "Use on Parametrizer to save parameters");
        }

        if(tNBT.hasKey("param")) {
            aList.add("Parameters ID: "+EnumChatFormatting.AQUA + tNBT.getInteger("param"));
            aList.add("Value 0: "+EnumChatFormatting.AQUA + tNBT.getFloat("value1f"));
            aList.add("Value 1: "+EnumChatFormatting.AQUA + tNBT.getFloat("value2f"));
            aList.add("Mantissa 0: "+EnumChatFormatting.AQUA + tNBT.getInteger("value1"));
            aList.add("Mantissa 1: "+EnumChatFormatting.AQUA + tNBT.getInteger("value2"));
            aList.add("Exponent: "+EnumChatFormatting.AQUA + tNBT.getInteger("exponent"));
        }
        aList.add(EnumChatFormatting.BLUE + "Use somewhere else to lock/unlock");
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack aStack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if(aStack.getTagCompound().hasKey("cardLocked")) {
            return locked;
        }else{
            return unlocked;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack aStack, int pass) {
        if(aStack.getTagCompound().hasKey("cardLocked")) {
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
