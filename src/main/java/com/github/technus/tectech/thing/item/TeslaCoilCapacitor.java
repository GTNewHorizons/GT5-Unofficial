package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.List;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.teslaCapacitor;
import static net.minecraft.util.StatCollector.translateToLocal;


public final class TeslaCoilCapacitor extends Item {
    public static TeslaCoilCapacitor INSTANCE;
    private static IIcon LVicon, MVicon, HVicon, EVicon, IVicon;

    private TeslaCoilCapacitor() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.teslaCoilCapacitor");
        setTextureName(MODID + ":itemCapacitorLV");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.BASS_MARK);
        if (aStack.getItemDamage() >= 0 && aStack.getItemDamage() <= 4) {
            aList.add(translateToLocal("item.tm.teslaCoilCapacitor.desc.0") + " " + V[aStack.getItemDamage() + 1] * 512 + " " + translateToLocal("item.tm.teslaCoilCapacitor.desc.1") +" " + V[aStack.getItemDamage() + 1] + " EU/t");//Stores 16384 EU in a tesla tower at 32 EU/t
        } else {
            aList.add(translateToLocal("item.tm.teslaCoilCapacitor.desc.2"));//Yeet this broken item into some spicy water!
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCoilCapacitor.desc.3"));//Insert into a Capacitor hatch of a Tesla Tower
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCoilCapacitor.desc.4"));//Capacitors are the same thing as batteries, right?
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    public static void run() {
        INSTANCE = new TeslaCoilCapacitor();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        teslaCapacitor.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        LVicon = itemIcon = iconRegister.registerIcon(getIconString());
        MVicon = iconRegister.registerIcon(MODID + ":itemCapacitorMV");
        HVicon = iconRegister.registerIcon(MODID + ":itemCapacitorHV");
        EVicon = iconRegister.registerIcon(MODID + ":itemCapacitorEV");
        IVicon = iconRegister.registerIcon(MODID + ":itemCapacitorIV");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        switch (damage) {
            case 1:
                return MVicon;
            case 2:
                return HVicon;
            case 3:
                return EVicon;
            case 4:
                return IVicon;
            default:
                return LVicon;
        }
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 4; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
