package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.font.TecTechFontRender;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.thing.item.renderElemental.IElementalItem;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabEM;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static cpw.mods.fml.relauncher.Side.CLIENT;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ElementalDefinitionContainer_EM extends Item implements IElementalItem {
    public static ElementalDefinitionContainer_EM INSTANCE;

    private ElementalDefinitionContainer_EM() {
        setMaxStackSize(1);
        setUnlocalizedName("em.definitionContainer");
        setTextureName(MODID + ":itemDefinitionContainer");
        setCreativeTab(creativeTabEM);
    }

    //return previous thing
    public static EMConstantStackMap setContent(ItemStack containerItem, EMConstantStackMap definitions){
        if(containerItem.getItem() instanceof ElementalDefinitionContainer_EM) {
            NBTTagCompound tNBT = containerItem.stackTagCompound;
            if (tNBT == null) {
                tNBT = containerItem.stackTagCompound = new NBTTagCompound();
            }

            EMConstantStackMap oldMap =null;
            if (tNBT.hasKey("content")) {
                try {
                    oldMap= EMConstantStackMap.fromNBT(TecTech.definitionsRegistry,tNBT.getCompoundTag("content"));
                } catch (EMException e) {
                    if (DEBUG_MODE) {
                        e.printStackTrace();
                    }
                }
            }
            tNBT.setTag("info", definitions.getInfoNBT());
            tNBT.setTag("content", definitions.toNBT(TecTech.definitionsRegistry));
            tNBT.setTag("symbols",definitions.getShortSymbolsNBT());
            return oldMap;
        }
        return null;
    }

    public static EMConstantStackMap getContent(ItemStack containerItem){
        if(containerItem.getItem() instanceof ElementalDefinitionContainer_EM){
            NBTTagCompound tNBT = containerItem.stackTagCompound;

            if (tNBT == null || !tNBT.hasKey("content")) {
                return null;
            }
            try {
                return EMConstantStackMap.fromNBT(TecTech.definitionsRegistry,tNBT.getCompoundTag("content"));
            } catch (EMException e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static EMConstantStackMap clearContent(ItemStack containerItem){
        if(containerItem.getItem() instanceof ElementalDefinitionContainer_EM){
            NBTTagCompound tNBT = containerItem.stackTagCompound;
            if (tNBT == null) {
                return null;
            }

            EMConstantStackMap oldMap =null;
            if (tNBT.hasKey("content")) {
                try {
                    oldMap= EMConstantStackMap.fromNBT(TecTech.definitionsRegistry,tNBT.getCompoundTag("content"));
                } catch (EMException e) {
                    if (DEBUG_MODE) {
                        e.printStackTrace();
                    }
                }
            }
            tNBT.removeTag("info");
            tNBT.removeTag("content");
            tNBT.removeTag("symbols");
            return oldMap;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("info")) {
                aList.add(translateToLocal("item.em.definitionContainer.desc.0") + ": ");//Should Contain
                Collections.addAll(aList, Util.infoFromNBT(tNBT.getCompoundTag("info")));
            } else {
                aList.add(translateToLocal("item.em.definitionContainer.desc.1"));//Recipe Hint
            }
        } catch (Exception e) {
            aList.add(translateToLocal("item.em.definitionContainer.desc.2"));//---Unexpected Termination---
        }
    }

    public static void run() {
        INSTANCE = new ElementalDefinitionContainer_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }

    @Override
    public String getSymbol(ItemStack aStack, int index) {
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("symbols")) {
                String[] strings=Util.infoFromNBT(tNBT.getCompoundTag("symbols"));
                return strings[index%strings.length];
            } else {
                return null;
            }
        } catch (Exception e) {
            return "#!";
        }
    }

    @Override
    @SideOnly(CLIENT)
    public FontRenderer getFontRenderer(ItemStack stack) {
        return TecTechFontRender.INSTANCE;
    }
}
