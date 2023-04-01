package gregtech.common.items;

import static gregtech.api.enums.GT_Values.MOD_ID;

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

public class ItemPollen extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemPollen() {
        super();
        this.setCreativeTab(Tabs.tabApiculture);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("gt.pollen");
        GameRegistry.registerItem(this, "gt.pollen", MOD_ID);
    }

    public ItemStack getStackForType(PollenType type) {
        return new ItemStack(this, 1, type.ordinal());
    }

    public ItemStack getStackForType(PollenType type, int count) {
        return new ItemStack(this, count, type.ordinal());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs,List<ItemStack> list) {
        for (PollenType type : PollenType.values()) {
            if (type.showInList) {
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
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("forestry:pollen.0");
        this.secondIcon = iconRegister.registerIcon("forestry:pollen.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        int meta = Math.max(0, Math.min(PollenType.values().length - 1, stack.getItemDamage()));
        int colour = PollenType.values()[meta].getColours()[0];

        if (pass >= 1) {
            colour = PollenType.values()[meta].getColours()[1];
        }

        return colour;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return PollenType.values()[stack.getItemDamage()].getName();
    }
}
