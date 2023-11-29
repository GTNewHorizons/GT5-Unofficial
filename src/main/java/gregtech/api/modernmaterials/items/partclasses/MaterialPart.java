package gregtech.api.modernmaterials.items.partclasses;

import static gregtech.api.modernmaterials.ModernMaterialUtilities.tooltipGenerator;

import java.util.List;

import gregtech.api.modernmaterials.effects.IMaterialEffect;
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
import gregtech.api.util.GT_LanguageManager;

public class MaterialPart extends Item {

    private final String partName;
    private final ItemsEnum part;

    public MaterialPart(final ItemsEnum part) {
        setCreativeTab(CreativeTabs.tabMaterials);
        setHasSubtypes(true);

        partName = part.partName;
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
    public String getUnlocalizedName(ItemStack itemStack) {

        ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        String trueName = partName.replace("%", material.getMaterialName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean F3_H) {

        final ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        if (material.getCustomTooltipGenerator() != null) {
            tooltipList.addAll(material.getCustomTooltipGenerator().apply(itemStack, player, tooltipList, F3_H));
            return; // No more processing needed.
        }

        tooltipList.addAll(tooltipGenerator(itemStack.getItem(), material));
    }

    public ItemsEnum getPart() {
        return part;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slotIndex, boolean isCurrentItem)  {
        final ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        for (IMaterialEffect effect : material.getEffects()) {
            effect.apply(itemStack, world, entity, slotIndex, isCurrentItem);
        }
    }
}
