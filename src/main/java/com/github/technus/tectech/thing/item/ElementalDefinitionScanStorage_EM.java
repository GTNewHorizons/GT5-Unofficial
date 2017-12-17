package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.CustomItemList;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.auxiliary.Reference.MODID;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;

/**
 * Created by Tec on 15.03.2017.
 */
public class ElementalDefinitionScanStorage_EM extends Item {
    public static ElementalDefinitionScanStorage_EM INSTANCE;
    public static IIcon offline, online;

    private ElementalDefinitionScanStorage_EM() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("em.definitionScanStorage");
        setTextureName(MODID + ":itemDefinitionScanStorage");
    }

    //return previous thing
    public static void setContent(ItemStack containerItem, cElementalInstanceStackMap definitions, int[] detailsOnDepthLevels){
        if(containerItem.getItem() instanceof ElementalDefinitionScanStorage_EM) {
            if (containerItem.stackTagCompound == null) containerItem.stackTagCompound=new NBTTagCompound();
            containerItem.stackTagCompound.setTag("elementalInfo", definitions.getScanInfoNBT(detailsOnDepthLevels));
        }
    }

    public static void clearContent(ItemStack containerItem){
        if(containerItem.getItem() instanceof ElementalDefinitionScanStorage_EM){
            if (containerItem.stackTagCompound == null) return;
            containerItem.stackTagCompound=null;
            return;
        }
        return;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            if  (aStack.stackTagCompound != null &&  aStack.stackTagCompound.hasKey("elementalInfo")) {
                aList.add("Contains scan result");
                if(DEBUG_MODE) {
                    aList.add("DEBUG MODE INFO - U CHEATER");
                    Collections.addAll(aList, Util.infoFromNBT(aStack.stackTagCompound.getCompoundTag("elementalInfo")));
                }
            } else {
                aList.add("Storage for matter scan data");
            }
        } catch (Exception e) {
            aList.add("---Unexpected Termination---");
        }
    }

    public static void run() {
        INSTANCE = new ElementalDefinitionScanStorage_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.scanContainer.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        offline =iconRegister.registerIcon(MODID + ":itemDefinitionScanStorageOff");
        online =this.itemIcon = iconRegister.registerIcon(this.getIconString());
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        NBTTagCompound tagCompound=itemStack.stackTagCompound;
        if(tagCompound!=null && tagCompound.hasKey("elementalInfo"))
            return online;
        return offline;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        list.add(that);
    }
}
