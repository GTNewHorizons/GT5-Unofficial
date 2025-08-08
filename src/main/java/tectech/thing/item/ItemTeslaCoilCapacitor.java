package tectech.thing.item;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.thing.CustomItemList;
import tectech.util.CommonValues;

public final class ItemTeslaCoilCapacitor extends Item {

    public static ItemTeslaCoilCapacitor INSTANCE;
    private static IIcon LVicon, MVicon, HVicon, EVicon, IVicon, LuVicon, ZPMicon;

    private ItemTeslaCoilCapacitor() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.teslaCoilCapacitor");
        setTextureName(Reference.MODID + ":itemCapacitorLV");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        if (aStack.getItemDamage() >= 0 && aStack.getItemDamage() <= 6) {
            aList.add(
                translateToLocal("item.tm.teslaCoilCapacitor.desc.0") + " "
                    + V[aStack.getItemDamage() + 1] * 512
                    + " "
                    + translateToLocal("item.tm.teslaCoilCapacitor.desc.1")
                    + " "
                    + V[aStack.getItemDamage() + 1]
                    + " EU/t"); // Stores 16384 EU in a tesla tower at 32 EU/t
        } else {
            aList.add(translateToLocal("item.tm.teslaCoilCapacitor.desc.2")); // Yeet this broken item into some spicy
                                                                              // water!
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCoilCapacitor.desc.3")); // Insert into a
                                                                                                    // Capacitor hatch
                                                                                                    // of a Tesla Tower
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCoilCapacitor.desc.4")); // Capacitors are
                                                                                                    // the same thing as
                                                                                                    // batteries, right?
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    public static void run() {
        INSTANCE = new ItemTeslaCoilCapacitor();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.teslaCapacitor.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        LVicon = itemIcon = iconRegister.registerIcon(getIconString());
        MVicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorMV");
        HVicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorHV");
        EVicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorEV");
        IVicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorIV");
        LuVicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorLuV");
        ZPMicon = iconRegister.registerIcon(Reference.MODID + ":itemCapacitorZPM");
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
            case 5:
                return LuVicon;
            case 6:
                return ZPMicon;
            default:
                return LVicon;
        }
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (int i = 0; i <= 6; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
