package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.thing.CustomItemList;
import tectech.util.CommonValues;

public final class ItemPowerPassUpgradeCover extends Item {

    public static ItemPowerPassUpgradeCover INSTANCE;

    private ItemPowerPassUpgradeCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.powerpassupgradecover");
        setTextureName(Reference.MODID + ":itemPowerPassUpgradeCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.powerpassupgradecover.desc.0")); // Add power pass functionality to TecTech
                                                                             // Multiblocks
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.powerpassupgradecover.desc.1")); // Active
                                                                                                       // transformer in
                                                                                                       // a can??
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.powerpassupgradecover.desc.2")); // Chain them up
                                                                                                       // like Christmas
                                                                                                       // lights!
    }

    public static void run() {
        INSTANCE = new ItemPowerPassUpgradeCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.powerPassUpgradeCover.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
