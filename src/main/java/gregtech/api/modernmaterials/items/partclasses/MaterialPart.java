package gregtech.api.modernmaterials.items.partclasses;

import static gregtech.api.modernmaterials.ModernMaterialUtilities.tooltipGenerator;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.effects.IMaterialEffect;

public class MaterialPart extends Item {

    private final ItemsEnum part;

    public MaterialPart(final ItemsEnum part) {
        setCreativeTab(CreativeTabs.tabMaterials);
        setHasSubtypes(true);
        this.part = part;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
        for (ModernMaterial material : part.getAssociatedMaterials()) {
            ItemStack itemStack = new ItemStack(item, 1, material.getMaterialID());

            itemList.add(itemStack);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);
        if (material == null) return "ERROR: Null Material";
        return part.getLocalizedName(material);
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList,
        boolean F3_H) {
        tooltipList.addAll(tooltipGenerator(itemStack, player, tooltipList, F3_H));
    }

    public ItemsEnum getPart() {
        return part;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        final ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        if (material == null) return;

        for (IMaterialEffect effect : material.getEffects()) {
            effect.apply(itemStack, world, entity, slotIndex, isCurrentItem);
        }
    }
}
