package gregtech.api.ModernMaterials.Items.PartsClasses;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.tooltipGenerator;

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
    public void addInformation(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList,
        boolean aF3_H) {

        final ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        tooltipList.addAll(tooltipGenerator(itemStack.getItem(), material));
    }

    public ItemsEnum getPart() {
        return part;
    }
}
