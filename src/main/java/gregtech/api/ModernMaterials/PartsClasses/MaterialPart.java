package gregtech.api.ModernMaterials.PartsClasses;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.materialIDToMaterial;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.tooltipGenerator;
import static gregtech.api.ModernMaterials.PartProperties.Textures.TextureType.*;
import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
    public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
        for(ModernMaterial material : part.getAssociatedMaterials()) {
            ItemStack itemStack = new ItemStack(item, 1, material.getMaterialID());
            itemList.add(itemStack);
        }
    }

    private final HashMap<Integer, IIcon> animatedMaterialIconMap = new HashMap<>();

    @Override
    public void registerIcons(IIconRegister register) {

        for (final ModernMaterial material : this.part.getAssociatedMaterials()) {

            CustomPartInfo customPartInfo = material.getCustomPartInfo(this.part);
            String path = RES_PATH_BLOCK + "ModernMaterialsIcons/";

            if (customPartInfo.getTextureType().equals(CustomIndividual)) {
                path += "Custom/" + material.getMaterialName().toLowerCase() + "/" + partName;
            } else if (customPartInfo.getTextureType().equals(CustomUnified)) {
                path += "Custom/" + material.getMaterialName().toLowerCase() + "/unified";
            } else {
                path += customPartInfo.getTextureType()
                    + "/"
                    + customPartInfo.getTextureName();
            }

            animatedMaterialIconMap.put(material.getMaterialID(), register.registerIcon(path)); // todo probably inefficient?
        }
    }

    @Override
    public IIcon getIconFromDamage(int damageValue) {
        return animatedMaterialIconMap.get(damageValue);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        ModernMaterial material = materialIDToMaterial.get(itemStack.getItemDamage());

        String trueName = this.partName.replace("%", material.getMaterialName());

        // Localise the material name.
        GT_LanguageManager.addStringLocalization(trueName + ".name", trueName);

        return trueName;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List tooltipList, boolean aF3_H)  {

        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        for (String line : tooltipGenerator(material)) {
            tooltipList.add(line);
        }
    }
}
