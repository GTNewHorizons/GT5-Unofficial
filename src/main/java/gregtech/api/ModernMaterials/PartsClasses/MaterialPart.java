package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

import static gregtech.api.ModernMaterials.ModernMaterials.materialIdToMaterial;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

public class MaterialPart extends Item {

    private final String partName;

    public MaterialPart(final String partName) {
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setHasSubtypes(true);

        this.partName = partName;
    }

//    @SideOnly(Side.CLIENT)
//    public void registerBlockIcon(IIconRegister register) {
//        this.itemIcon = register.registerIcon(MOD_ID + ":" + this.getUnlocalizedName());
//    }

    @Override
    public void getSubItems(Item itm, CreativeTabs tab, List lst) {
        materialIdToMaterial.forEach((materialID, material) -> {
            if (true) {
                ItemStack itemStack = new ItemStack(this, 1, materialID);
                lst.add(itemStack);
            }
        });
    }

    private IIcon materialIcon;

    @Override
    public void registerIcons(IIconRegister register) {
        materialIcon = register.registerIcon(RES_PATH_BLOCK + "ModernMaterialsIcons/METALLIC/" + partName);
    }

    @Override
    public IIcon getIconFromDamage(int p_77617_1_) {
        return materialIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        ModernMaterial material = materialIdToMaterial.get(itemStack.getItemDamage());

        String trueName = this.partName.replace("%", material.getMaterialName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }

    public boolean conditionOfMaterialPartsExistence(final ModernMaterial material) {
        return false;
    }
}
