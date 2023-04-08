package gtPlusPlus.core.item.general.chassis;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class ItemDehydratorCoilWire extends Item {

    public IIcon[] icons = new IIcon[4];

    public ItemDehydratorCoilWire() {
        super();
        this.setHasSubtypes(true);
        String unlocalizedName = "itemDehydratorCoilWire";
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(GTPlusPlus.ID + ":" + "dehydrator/itemDehydratorCoilWire_0");
        this.icons[1] = reg.registerIcon(GTPlusPlus.ID + ":" + "dehydrator/itemDehydratorCoilWire_1");
        this.icons[2] = reg.registerIcon(GTPlusPlus.ID + ":" + "dehydrator/itemDehydratorCoilWire_2");
        this.icons[3] = reg.registerIcon(GTPlusPlus.ID + ":" + "dehydrator/itemDehydratorCoilWire_3");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + stack.getItemDamage();
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        return StatCollector
                .translateToLocalFormatted("item.itemDehydratorCoilWire.name", GT_Values.VN[tItem.getItemDamage() + 4]);
    }

    /*
     * @Override public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) { int meta =
     * stack.getItemDamage(); if (meta == 0){ HEX_OxFFFFFF = Utils.rgbtoHexValue(10,110,30); } else if (meta == 1){
     * HEX_OxFFFFFF = Utils.rgbtoHexValue(150,180,35); } else if (meta == 2){ HEX_OxFFFFFF =
     * Utils.rgbtoHexValue(200,85,40); } else if (meta == 3){ HEX_OxFFFFFF = Utils.rgbtoHexValue(255,150,50); } return
     * HEX_OxFFFFFF; }
     */

}
