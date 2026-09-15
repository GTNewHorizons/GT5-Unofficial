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

public final class ItemEnderFluidLinkCover extends Item {

    public static ItemEnderFluidLinkCover INSTANCE;

    private ItemEnderFluidLinkCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.enderfluidlinkcover");
        setTextureName(Reference.MODID + ":itemEnderFluidLinkCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.enderfluidlinkcover.desc.0")); // Ender-Fluid-Enables Machines!
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.enderfluidlinkcover.desc.1")); // Use on any side
                                                                                                     // of a fluid tank
                                                                                                     // to link it to
        // the Ender
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.enderfluidlinkcover.desc.2")); // Ender Tanks so
                                                                                                     // are laggy -Bot
                                                                                                     // from the Chads
                                                                                                     // of NH
    }

    public static void run() {
        INSTANCE = new ItemEnderFluidLinkCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.enderLinkFluidCover.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
