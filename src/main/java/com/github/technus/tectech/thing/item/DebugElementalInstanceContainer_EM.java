package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMDefinitionsRegistry;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.font.TecTechFontRender;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.IEMContainer;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.thing.item.renderElemental.IElementalItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.loader.gui.CreativeTabTecTech.creativeTabTecTech;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.*;
import static cpw.mods.fml.relauncher.Side.CLIENT;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 15.03.2017.
 */
public final class DebugElementalInstanceContainer_EM extends Item implements IElementalItem {

    public static DebugElementalInstanceContainer_EM INSTANCE;

    private DebugElementalInstanceContainer_EM() {
        setMaxStackSize(1);
        setUnlocalizedName("em.debugContainer");
        setTextureName(MODID + ":itemDebugContainer");
        setCreativeTab(creativeTabTecTech);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE instanceof IEMContainer) {
                    EMInstanceStackMap content = ((IEMContainer) metaTE).getContentHandler();
                    if (tNBT.hasKey("content")) {
                        try {
                            content.putUnifyAll(EMInstanceStackMap.fromNBT(tNBT.getCompoundTag("content")));
                        } catch (EMException e) {
                            if (DEBUG_MODE) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        ((IEMContainer) metaTE).purgeOverflow();
                        tNBT.removeTag("content");
                        tNBT.removeTag("symbols");
                        tNBT.removeTag("info");
                    } else if (content.hasStacks()) {
                        ((IEMContainer) metaTE).purgeOverflow();
                        tNBT.setTag("info", content.getInfoNBT());
                        tNBT.setTag("content", content.toNBT());
                        tNBT.setTag("symbols", content.getShortSymbolsNBT());
                        content.clear();
                    }
                    return true;
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    public ItemStack setContent(ItemStack aStack, EMInstanceStackMap content){
        NBTTagCompound tNBT = aStack.getTagCompound();
        if(tNBT==null){
            tNBT=new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        if (tNBT.hasKey("content")) {
            try {
                content.putUnifyAll(EMInstanceStackMap.fromNBT(tNBT.getCompoundTag("content")));
            } catch (EMException e) {
                if (DEBUG_MODE) {
                    e.printStackTrace();
                }
                return aStack;
            }
            tNBT.removeTag("content");
            tNBT.removeTag("info");
            tNBT.removeTag("symbols");
        } else if (content.hasStacks()) {
            tNBT.setTag("info", content.getInfoNBT());
            tNBT.setTag("content", content.toNBT());
            tNBT.setTag("symbols", content.getShortSymbolsNBT());
            content.clear();
        }
        return aStack;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("info")) {
                aList.add(translateToLocal("item.em.debugContainer.desc.0") + ": ");//Contains
                Collections.addAll(aList, Util.infoFromNBT(tNBT.getCompoundTag("info")));
            } else {
                aList.add(translateToLocal("item.em.debugContainer.desc.1"));//Container for elemental matter
                aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.debugContainer.desc.2"));//Right click on elemental hatches
            }
        } catch (Exception e) {
            aList.add(translateToLocal("item.em.debugContainer.desc.3"));//---Unexpected Termination---
        }
    }

    public static void run() {
        INSTANCE = new DebugElementalInstanceContainer_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
        for(IEMDefinition definition: EMDefinitionsRegistry.getStacksRegisteredForDisplay()){
            list.add(setContent(new ItemStack(this).setStackDisplayName(definition.getLocalizedName()+" 1 mol"),new EMInstanceStackMap(new EMInstanceStack(definition, AVOGADRO_CONSTANT))));
            list.add(setContent(new ItemStack(this).setStackDisplayName(definition.getLocalizedName()+" 144 mol"),new EMInstanceStackMap(new EMInstanceStack(definition, AVOGADRO_CONSTANT_144))));
            list.add(setContent(new ItemStack(this).setStackDisplayName(definition.getLocalizedName()+" 1000 mol"),new EMInstanceStackMap(new EMInstanceStack(definition, AVOGADRO_CONSTANT_1000))));
        }
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
