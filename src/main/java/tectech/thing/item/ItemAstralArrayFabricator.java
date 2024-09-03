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
import tectech.TecTech;
import tectech.thing.CustomItemList;

public class ItemAstralArrayFabricator extends Item {

    public static ItemAstralArrayFabricator INSTANCE;

    private ItemAstralArrayFabricator() {
        setHasSubtypes(false);
        setUnlocalizedName("tm.itemAstralArrayFabricator");
        setTextureName(Reference.MODID + ":itemAstralArray");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.itemAstralArrayFabricator.desc1"));
        aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.itemAstralArrayFabricator.desc2"));
        aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.itemAstralArrayFabricator.desc3"));
        aList.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("item.tm.itemAstralArrayFabricator.desc0"));
    }

    public static void run() {
        INSTANCE = new ItemAstralArrayFabricator();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.astralArrayFabricator.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }

}
