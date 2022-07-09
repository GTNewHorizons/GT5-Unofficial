package pers.gwyog.gtneioreplugin.plugin.items;

import cpw.mods.fml.common.Loader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;

public class ItemDimensionDisplay extends Item {

    private static final String TAG_DIMENSION = "dimension";
    private static final Map<String, ItemStack> items = new HashMap<>();

    public ItemDimensionDisplay() {
        setUnlocalizedName(GTNEIOrePlugin.MODID + ".itemDimensionDisplay");
        setCreativeTab(GTNEIOrePlugin.creativeTab);
    }

    public static ItemStack getItem(String dimension) {
        if (items.get(dimension) != null) {
            return items.get(dimension).copy();
        }
        return null;
    }

    public static void setDimension(ItemStack stack, String dimension) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(TAG_DIMENSION, dimension);
        stack.setTagCompound(nbt);
    }

    public static String getDimension(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_DIMENSION, Constants.NBT.TAG_STRING)) {
            return stack.getTagCompound().getString(TAG_DIMENSION);
        }
        return null;
    }

    public static boolean isDimensionEqual(ItemStack stack1, ItemStack stack2) {
        if (!(stack1.getItem() instanceof ItemDimensionDisplay)
                || !(stack2.getItem() instanceof ItemDimensionDisplay)) {
            return false;
        }
        return Objects.equals(getDimension(stack1), getDimension(stack2));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dimension = getDimension(stack);
        if (dimension != null) {
            return DimensionHelper.convertCondensedStringToToolTip(dimension).get(0);
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public Item setTextureName(String p_111206_1_) {
        return super.setTextureName(p_111206_1_);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List itemList) {
        for (ItemStack stack : items.values()) {
            itemList.add(stack.copy());
        }
    }

    public static void loadItems() {
        String[] vanilla_gt = new String[] {"Ow", "Ne", "TF", "EN", "VA", "EA"};
        for (String dimension : vanilla_gt) {
            ItemStack stack = new ItemStack(ModItems.itemDimensionDisplay);
            setDimension(stack, dimension);
            items.put(dimension, stack);
        }
        String[] gc = new String[] {"Mo", "Ma", "As"};
        if (Loader.isModLoaded("GalacticraftCore")) {
            for (String dimension : gc) {
                ItemStack stack = new ItemStack(ModItems.itemDimensionDisplay);
                setDimension(stack, dimension);
                items.put(dimension, stack);
            }
        }
        String[] gs = new String[] {
            "De", "Ph", "Ca", "Ce", "Eu", "Ga", "Io", "Me", "Ve", "En", "Mi", "Ob", "Ti", "Pr", "Tr", "Ha", "KB", "MM",
            "Pl", "BC", "BE", "BF", "CB", "TE", "VB"
        };
        if (Loader.isModLoaded("GalaxySpace")) {
            for (String dimension : gs) {
                ItemStack stack = new ItemStack(ModItems.itemDimensionDisplay);
                setDimension(stack, dimension);
                items.put(dimension, stack);
            }
        }
        String[] xu = new String[] {"DD"};
        if (Loader.isModLoaded("ExtraUtilities")) {
            for (String dimension : xu) {
                ItemStack stack = new ItemStack(ModItems.itemDimensionDisplay);
                setDimension(stack, dimension);
                items.put(dimension, stack);
            }
        }
    }
}
