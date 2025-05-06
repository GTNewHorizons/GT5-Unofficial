package gtnhintergalactic.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gtnhintergalactic.GTNHIntergalactic;

public class ItemDysonSwarmParts extends Item {

    private static final String[] names = { "dysonSwarmModule", "TaHfCNanofibers", "NtNanofibers", "UHTResistantMesh",
        "TaHfNanoparticles", "NtNanoparticles" };
    private static final IIcon[] icons = new IIcon[names.length];

    public ItemDysonSwarmParts() {
        setCreativeTab(GTNHIntergalactic.tab);
        setHasSubtypes(true);
        setUnlocalizedName("DysonSwarmParts");

        ItemList.DysonSwarmModule.set(new ItemStack(this, 1, 0));
        ItemList.TaHfCNanofibers.set(new ItemStack(this, 1, 1));
        ItemList.NtNanofibers.set(new ItemStack(this, 1, 2));
        ItemList.UHTResistantMesh.set(new ItemStack(this, 1, 3));
        ItemList.TaHfNanoparticles.set(new ItemStack(this, 1, 4));
        ItemList.NtNanoparticles.set(new ItemStack(this, 1, 5));
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta % icons.length];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List variants) {
        for (int i = 0; i < names.length; i++) {
            variants.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + names[stack.getItemDamage() % names.length];
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":" + names[i]);
        }
    }
}
