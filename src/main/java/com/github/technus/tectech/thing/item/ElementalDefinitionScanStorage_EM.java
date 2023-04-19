package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabTecTech;
import static cpw.mods.fml.relauncher.Side.CLIENT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.font.TecTechFontRender;
import com.github.technus.tectech.loader.gui.ModGuiHandler;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ElementalDefinitionScanStorage_EM extends Item {

    public static ElementalDefinitionScanStorage_EM INSTANCE;
    public static IIcon offline, online;

    private ElementalDefinitionScanStorage_EM() {
        setMaxStackSize(1);
        setUnlocalizedName("em.definitionScanStorage");
        setTextureName(MODID + ":itemDefinitionScanStorage");
        setCreativeTab(creativeTabTecTech);
    }

    // return previous thing
    public static void setContent(ItemStack aStack, EMInstanceStackMap instances, int[] detailsOnDepthLevels) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setTag("content", instances.toNBT(TecTech.definitionsRegistry));
        tNBT.setIntArray("scanConfiguration", detailsOnDepthLevels);
    }

    public static void clearContent(ItemStack containerItem) {
        if (containerItem.getItem() instanceof ElementalDefinitionScanStorage_EM) {
            if (containerItem.stackTagCompound == null) {
                return;
            }
            containerItem.stackTagCompound = null;
        }
    }

    public static ArrayList<String> getLines(ItemStack containerItem) {
        if (containerItem.stackTagCompound != null && containerItem.stackTagCompound.hasKey("content")) {
            EMInstanceStackMap content = EMInstanceStackMap
                    .fromNBT(TecTech.definitionsRegistry, containerItem.stackTagCompound.getCompoundTag("content"));
            return content.getScanInfo(containerItem.stackTagCompound.getIntArray("scanConfiguration"));
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        if (aStack.stackTagCompound != null && aStack.stackTagCompound.hasKey("content")) {
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.definitionScanStorage.desc.0")); // Contains
                                                                                                           // scan
                                                                                                           // result
            aList.add(translateToLocal("item.em.definitionScanStorage.desc.1")); // Use to read
        } else {
            aList.add(translateToLocal("item.em.definitionScanStorage.desc.2")); // Storage for matter scan data
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
        offline = iconRegister.registerIcon(MODID + ":itemDefinitionScanStorageOff");
        online = itemIcon = iconRegister.registerIcon(getIconString());
    }

    @Override
    public IIcon getIconIndex(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.stackTagCompound;
        return tagCompound != null && tagCompound.hasKey("content") ? online : offline;
    }

    @Override
    public IIcon getIcon(ItemStack itemStack, int pass) {
        NBTTagCompound tagCompound = itemStack.stackTagCompound;
        return tagCompound != null && tagCompound.hasKey("content") ? online : offline;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemStack that = new ItemStack(this, 1);
        list.add(that);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            NBTTagCompound tagCompound = itemStack.stackTagCompound;
            if (tagCompound != null && tagCompound.hasKey("content")) {
                player.openGui(TecTech.instance, ModGuiHandler.SCAN_DISPLAY_SCREEN_ID, world, 0, 0, 0);
            }
        }
        return itemStack;
    }

    @Override
    @SideOnly(CLIENT)
    public FontRenderer getFontRenderer(ItemStack stack) {
        return TecTechFontRender.INSTANCE;
    }
}
