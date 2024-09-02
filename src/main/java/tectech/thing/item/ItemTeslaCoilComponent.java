package tectech.thing.item;

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

public final class ItemTeslaCoilComponent extends Item {

    public static ItemTeslaCoilComponent INSTANCE;
    private static IIcon ultItemIcon;

    private ItemTeslaCoilComponent() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.itemTeslaComponent");
        setTextureName(Reference.MODID + ":itemTeslaComponent");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.itemTeslaComponent.desc")); // Tesla bois need
                                                                                                  // these!
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    public static void run() {
        INSTANCE = new ItemTeslaCoilComponent();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.teslaComponent.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
        ultItemIcon = iconRegister.registerIcon(Reference.MODID + ":itemTeslaComponentUltimate");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage == 1) {
            return ultItemIcon;
        }
        return itemIcon;
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 1));
    }
}
