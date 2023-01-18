package gregtech.api.ModernMaterials.PartsClasses;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.HashMap;
import java.util.List;

import static gregtech.api.ModernMaterials.ModernMaterials.materialIdToMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Custom;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

public class MaterialPart extends Item {

    private final String partName;
    private final PartsEnum partsEnum;

    public MaterialPart(final PartsEnum partsEnum) {
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setHasSubtypes(true);

        this.partName = partsEnum.partName;
        this.partsEnum = partsEnum;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List lst) {
        materialIdToMaterial.forEach((materialID, material) -> {
            if (material.doesPartExist(partsEnum)) {
                ItemStack itemStack = new ItemStack(item, 1, materialID);
                lst.add(itemStack);
            }
        });
    }

    private final HashMap<Integer, IIcon> animatedMaterialIconMap = new HashMap<>();

    @Override
    public void registerIcons(IIconRegister register) {
        materialIdToMaterial.forEach((materialID, material) -> {

            if (!material.doesPartExist(partsEnum)) {
                return; // Skip element.
            }

            CustomPartInfo customPartInfo = material.getPart(this.partsEnum);
            String path;

            if (!customPartInfo.getTextureType().equals(Custom)) {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/" + customPartInfo.getTextureType() + "/" + partName;
            } else {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/" + customPartInfo.getTextureType() + "/" +
                    material.getMaterialName().toLowerCase()  + "/" + customPartInfo.getTextureName();
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

        ModernMaterial material = materialIdToMaterial.get(itemStack.getItemDamage());

        String trueName = this.partName.replace("%", material.getMaterialName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }

}
