package gregtech.common.tools;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Textures;
import gregtech.api.enums.materials.Materials;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.MaterialUtils;

public class ToolFileMV extends ToolFileLV {

    @Override
    public int getToolDamagePerContainerCraft() {
        return 3200;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MaterialUtils.rgba(MetaGeneratedTool.getPrimaryMaterialML(aStack))
            : MaterialUtils.rgba(Materials.Aluminium);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? GTMaterialIcons.item("toolHeadAngleGrinder", MetaGeneratedTool.getPrimaryMaterialML(aStack))
            : Textures.ItemIcons.POWER_UNIT_MV;
    }

}
