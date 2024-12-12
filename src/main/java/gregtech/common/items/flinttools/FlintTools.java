package gregtech.common.items.flinttools;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;

import cpw.mods.fml.common.registry.GameRegistry;

public class FlintTools {

    public static Item.ToolMaterial FLINT_MATERIAL = EnumHelper.addToolMaterial("FLINT", 1, 128, 4.0F, 1.0F, 5);

    public static Item AXE;
    public static Item SWORD;
    public static Item PICKAXE;
    public static Item SHOVEL;
    public static Item HOE;

    public static void registerTools() {
        // Create items.
        AXE = new FlintAxe();
        SWORD = new FlintSword();
        PICKAXE = new FlintPickaxe();
        SHOVEL = new FlintShovel();
        HOE = new FlintHoe();

        // Register them with minecraft.
        GameRegistry.registerItem(AXE, "flintAxe");
        GameRegistry.registerItem(SWORD, "flintSword");
        GameRegistry.registerItem(PICKAXE, "flintPickaxe");
        GameRegistry.registerItem(SHOVEL, "flintShovel");
        GameRegistry.registerItem(HOE, "flintHoe");
    }

    public static void registerRecipes() {
        // Flint Sword Recipe, with
        GameRegistry.addRecipe(
            new EnchantedRecipe(
                new ItemStack(FlintTools.SWORD),
                " F ",
                " F ",
                " S ",
                'F',
                Items.flint,
                'S',
                Items.stick));

        // Flint Pickaxe Recipe
        GameRegistry
            .addRecipe(new ItemStack(FlintTools.PICKAXE), "FFF", " S ", " S ", 'F', Items.flint, 'S', Items.stick);

        // Flint Axe Recipe with Fire Aspect I
        GameRegistry.addRecipe(
            new EnchantedRecipe(
                new ItemStack(FlintTools.AXE),
                "FF ",
                "FS ",
                " S ",
                'F',
                Items.flint,
                'S',
                Items.stick));

        GameRegistry.addRecipe(
            new EnchantedRecipe(
                new ItemStack(FlintTools.AXE),
                " FF",
                " SF",
                " S ",
                'F',
                Items.flint,
                'S',
                Items.stick));

        // Flint Shovel Recipe
        GameRegistry
            .addRecipe(new ItemStack(FlintTools.SHOVEL), " F ", " S ", " S ", 'F', Items.flint, 'S', Items.stick);

        // Flint Hoe Recipe
        GameRegistry.addRecipe(new ItemStack(FlintTools.HOE), "FF ", " S ", " S ", 'F', Items.flint, 'S', Items.stick);

        GameRegistry.addRecipe(new ItemStack(FlintTools.HOE), " FF", " S ", " S ", 'F', Items.flint, 'S', Items.stick);
    }

    public static NBTTagCompound transformFlintTool(NBTTagCompound tagCompound) {
        int oldId = tagCompound.getShort("Damage");
        int id;
        boolean fireAspect = false;

        switch (oldId) {
            case 0:
                id = Item.getIdFromItem(FlintTools.SWORD);
                fireAspect = true;
                break;
            case 2:
                id = Item.getIdFromItem(FlintTools.PICKAXE);
                break;
            case 4:
                id = Item.getIdFromItem(FlintTools.SHOVEL);
                break;
            case 6:
                id = Item.getIdFromItem(FlintTools.AXE);
                fireAspect = true;
                break;
            case 8:
                id = Item.getIdFromItem(FlintTools.HOE);
                break;
            case 40: // Sense tool
                id = 0;
                break;
            default:
                return tagCompound; // No transform needed.
        }

        tagCompound.setShort("id", (short) id);
        tagCompound.setShort("Damage", (short) 0);
        tagCompound.setTag("tag", new NBTTagCompound()); // Erase old GT data.

        // Apply Fire Aspect enchantment if fireAspect is true
        if (fireAspect) {
            NBTTagCompound enchantTag = getEnchantedTagCompound();

            // Attach the new tag containing the enchantments to the tool
            tagCompound.setTag("tag", enchantTag);
        }

        return tagCompound;
    }

    @NotNull
    private static NBTTagCompound getEnchantedTagCompound() {
        NBTTagCompound enchantTag = new NBTTagCompound();
        NBTTagList enchantments = new NBTTagList();

        // Add Fire Aspect enchantment
        NBTTagCompound fireAspectTag = new NBTTagCompound();
        fireAspectTag.setShort("id", (short) Enchantment.fireAspect.effectId); // Fire Aspect ID
        fireAspectTag.setShort("lvl", (short) 1); // Level 1
        enchantments.appendTag(fireAspectTag);

        // Attach enchantments list to the "tag" NBTTagCompound
        enchantTag.setTag("ench", enchantments);
        return enchantTag;
    }

    public static void registerPosteaTransformations() {
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metatool.01", FlintTools::transformFlintTool);
    }

    private static class EnchantedRecipe extends ShapedOreRecipe {

        private final ItemStack result;

        public EnchantedRecipe(ItemStack result, Object... recipe) {
            super(result, recipe);
            this.result = result;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inventory) {
            ItemStack craftedItem = result.copy();
            craftedItem.addEnchantment(Enchantment.fireAspect, 1); // Add Fire Aspect I
            return craftedItem;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return result;
        }
    }
}
