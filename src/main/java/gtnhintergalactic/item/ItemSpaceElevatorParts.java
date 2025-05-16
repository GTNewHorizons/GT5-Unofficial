package gtnhintergalactic.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gtnhintergalactic.GTNHIntergalactic;

/**
 * Item parts used in crafting the Space Elevator
 *
 * @author minecraft7771
 */
public class ItemSpaceElevatorParts extends Item {

    /** Names of the items */
    private static final String[] names = new String[] { "nanotubeSpool", };

    /** Icons of the items */
    private static final IIcon[] icons = new IIcon[names.length];

    /**
     * Initialize new item part for the Space Elevator crafting
     */
    public ItemSpaceElevatorParts() {
        setCreativeTab(GTNHIntergalactic.tab);
        setHasSubtypes(true);
        setUnlocalizedName("SpaceElevatorParts");
        ItemList.NanotubeSpool.set(new ItemStack(this, 1, 0));
    }

    /**
     * Get the item icon from its damage value
     *
     * @param meta Damage value of the item
     * @return Icon for this damage value of the item
     */
    @Override
    public IIcon getIconFromDamage(int meta) {
        return icons[meta % icons.length];
    }

    /**
     * Get the unlocalized name for the item stack
     *
     * @param stack Item stack for which the unlocalized name will be gotten
     * @return Unlocalized name of this item stack
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + names[stack.getItemDamage() % names.length];
    }

    /**
     * Register the icons of the items
     *
     * @param iconRegister Register that the icons will be registered to
     */
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        for (int i = 0; i < names.length; i++) {
            icons[i] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":" + names[i]);
        }
    }
}
