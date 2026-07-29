package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.MaterialIconRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.MaterialUtils;

public class ToolFileLV extends ToolFile {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MaterialUtils.rgba(MetaGeneratedTool.getPrimaryMaterialML(aStack))
            : MaterialUtils.rgba(Materials2Materials.Steel);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MaterialUtils.iconSet(
            MetaGeneratedTool
                .getPrimaryMaterialML(aStack)).mTextures[MaterialIconRegistry.IconType.TOOL_HEAD_ANGLE_GRINDER
                    .ordinal()]
            : Textures.ItemIcons.POWER_UNIT_LV;
    }

}
