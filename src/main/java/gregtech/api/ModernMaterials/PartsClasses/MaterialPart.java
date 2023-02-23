package gregtech.api.ModernMaterials.PartsClasses;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIdToMaterial;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.Custom;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.util.GT_LanguageManager;

public class MaterialPart extends Item {

    private final String mPartName;
    private final PartsEnum mPart;

    public MaterialPart(final PartsEnum aPart) {
        setCreativeTab(CreativeTabs.tabMaterials);
        setHasSubtypes(true);

        mPartName = aPart.partName;
        mPart = aPart;
    }

    private final HashMap<Integer, IIcon> mAnimatedMaterialIconMap = new HashMap<>();

    @Override
    public void registerIcons(IIconRegister register) {
        materialIdToMaterial.forEach((materialID, material) -> {
            if (!material.doesPartExist(mPart)) {
                return; // Skip element.
            }

            CustomPartInfo customPartInfo = material.getCustomPartInfo(this.mPart);
            String path;

            if (!customPartInfo.getmTextureType().equals(Custom)) {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/" + customPartInfo.getmTextureType() + "/" + mPartName;
            } else {
                path = RES_PATH_BLOCK + "ModernMaterialsIcons/"
                        + customPartInfo.getmTextureType()
                        + "/"
                        + material.getName().toLowerCase()
                        + "/"
                        + customPartInfo.getmTextureName();
            }

            mAnimatedMaterialIconMap.put(materialID, register.registerIcon(path)); // todo probably inefficient?
        });
    }

    @Override
    public IIcon getIconFromDamage(int damageValue) {
        return mAnimatedMaterialIconMap.get(damageValue);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        ModernMaterial material = materialIdToMaterial.get(itemStack.getItemDamage());

        String trueName = this.mPartName.replace("%", material.getName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }
}
