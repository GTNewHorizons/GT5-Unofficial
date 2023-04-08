package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;

public class ItemControlCore extends Item {

    public static IIcon[] icons = new IIcon[10];

    public ItemControlCore() {
        super();
        this.setHasSubtypes(true);
        String unlocalizedName = "itemControlCore";
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(GregTech_API.TAB_GREGTECH);
        // this.setCreativeTab(AddToCreativeTab.tabMisc);
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        icons[0] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_0");
        icons[1] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_1");
        icons[2] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_2");
        icons[3] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_3");
        icons[4] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_4");
        icons[5] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_5");
        icons[6] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_6");
        icons[7] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_7");
        icons[8] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_8");
        icons[9] = reg.registerIcon(GTPlusPlus.ID + ":" + "controlcore/Core_9");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 10; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + stack.getItemDamage();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        list.add(
                EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                        "item.itemControlCore.tooltip.0",
                        GT_Values.VN[stack.getItemDamage()]));
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.itemControlCore.tooltip.1"));
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.itemControlCore.tooltip.2"));
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        return StatCollector
                .translateToLocalFormatted("item.itemControlCore.name", GT_Values.VN[tItem.getItemDamage()]);
    }
}
