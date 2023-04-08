package gtPlusPlus.xmod.forestry.bees.items.output;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PollenType;

public class GTPP_Pollen extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public GTPP_Pollen() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gtpp.pollen");
        GameRegistry.registerItem(this, "gtpp.pollen", GTPlusPlus.ID);
    }

    public ItemStack getStackForType(GTPP_PollenType type) {
        return new ItemStack(this, 1, type.mID);
    }

    public ItemStack getStackForType(GTPP_PollenType type, int count) {
        return new ItemStack(this, count, type.mID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (GTPP_PollenType type : GTPP_PollenType.values()) {
            if (type.mShowInList) {
                list.add(this.getStackForType(type));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon("forestry:pollen.0");
        this.secondIcon = par1IconRegister.registerIcon("forestry:pollen.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int colour = GTPP_PollenType.get(stack.getItemDamage()).getColours()[0];

        if (pass >= 1) {
            colour = GTPP_PollenType.get(stack.getItemDamage()).getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GTPP_PollenType.get(stack.getItemDamage()).getName();
    }
}
