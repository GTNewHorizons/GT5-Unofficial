package kubatech.loaders.item.htgritem;

import static kubatech.kubatech.KT;

import java.util.HashSet;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import codechicken.nei.api.API;
import gregtech.api.enums.Materials;

public class HTGRItem extends Item {

    private IIcon[] icons;
    private HashSet<Materials> knownMaterials = new HashSet<>();

    public HTGRItem() {
        super();
        this.setMaxDamage(0);
        setHasSubtypes(true);
        this.setCreativeTab(KT);
        this.setUnlocalizedName("htgr_item");
        API.hideItem(new ItemStack(this, 1, 0));
    }

    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[5];
        icons[0] = register.registerIcon("kubatech:htgr/TRISOMixture");
        icons[1] = register.registerIcon("kubatech:htgr/IncompleteBISOFuel");
        icons[2] = register.registerIcon("kubatech:htgr/IncompleteTRISOFuel");
        icons[3] = register.registerIcon("kubatech:htgr/TRISOFuel");
        icons[4] = register.registerIcon("kubatech:htgr/BurnedTRISOFuel");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return damage < 5 ? icons[damage] : null;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Materials material = getItemMaterial(stack);
        return StatCollector.translateToLocalFormatted(
            "item.htgr_item." + getDamage(stack) + ".name",
            (material == null ? "NULL" : (material.getLocalizedNameForItem("%material"))));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add(StatCollector.translateToLocal("kubatech.tooltip.htgr_material"));
    }

    private ItemStack getItemWithMaterial(Materials material, int damage) {
        ItemStack stack = new ItemStack(this, 1, damage);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("material", material.mName);
        stack.setTagCompound(tag);
        return stack;
    }

    public Materials getItemMaterial(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("material")) {
                return Materials.get(tag.getString("material"));
            }
        }
        return null;
    }

    private void addKnownMaterial(Materials material) {
        if (knownMaterials.add(material)) {
            API.addItemVariant(this, getItemWithMaterial(material, 0));
            API.addItemVariant(this, getItemWithMaterial(material, 1));
            API.addItemVariant(this, getItemWithMaterial(material, 2));
            API.addItemVariant(this, getItemWithMaterial(material, 3));
            API.addItemVariant(this, getItemWithMaterial(material, 4));
        }
    }

    public ItemStack createTRISOMixture(Materials material) {
        ItemStack stack = getItemWithMaterial(material, 0);
        return stack;
    }

    public ItemStack createIncompleteBISOFuel(Materials material) {
        ItemStack stack = getItemWithMaterial(material, 1);
        return stack;
    }

    public ItemStack createIncompleteTRISOFuel(Materials material) {
        ItemStack stack = getItemWithMaterial(material, 2);
        return stack;
    }

    public ItemStack createTRISOFuel(Materials material) {
        addKnownMaterial(material);
        ItemStack stack = getItemWithMaterial(material, 3);
        return stack;
    }

    public ItemStack createBurnedTRISOFuel(Materials material) {
        ItemStack stack = getItemWithMaterial(material, 4);
        return stack;
    }
}
