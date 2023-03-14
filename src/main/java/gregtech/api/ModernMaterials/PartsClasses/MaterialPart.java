package gregtech.api.ModernMaterials.PartsClasses;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Custom;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.util.GT_LanguageManager;

public class MaterialPart extends Item {

    private final String partName;
    private final PartsEnum part;

    public MaterialPart(final PartsEnum part) {
        setCreativeTab(CreativeTabs.tabMaterials);
        setHasSubtypes(true);

        partName = part.partName;
        this.part = part;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs aTab, List itemList) {
        materialIDToMaterial.forEach((materialID, material) -> {
            if (material.doesPartExist(part)) {
                ItemStack itemStack = new ItemStack(item, 1, materialID);
                itemList.add(itemStack);
            }
        });
    }

    private final HashMap<Integer, IIcon> animatedMaterialIconMap = new HashMap<>();

    @Override
    public void registerIcons(IIconRegister register) {
        materialIDToMaterial.forEach((materialID, material) -> {
            if (!material.doesPartExist(part)) {
                return; // Skip element.
            }

            CustomPartInfo customPartInfo = material.getCustomPartInfo(this.part);
            String path;

            if (!customPartInfo.getTextureType().equals(Custom)) {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/" + customPartInfo.getTextureType() + "/" + partName;
            } else {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/"
                        + customPartInfo.getTextureType()
                        + "/"
                        + material.getName().toLowerCase()
                        + "/"
                        + customPartInfo.getTextureName();
            }

            animatedMaterialIconMap.put(materialID, register.registerIcon(path)); // todo probably inefficient?
        });
    }

    @Override
    public IIcon getIconFromDamage(int damageValue) {
        return animatedMaterialIconMap.get(damageValue);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        ModernMaterial material = materialIDToMaterial.get(itemStack.getItemDamage());

        String trueName = this.partName.replace("%", material.getName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }
}
