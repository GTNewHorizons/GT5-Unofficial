package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GT_Material_Glass extends Material {

    public static final Material INSTANCE = new GT_Material_Glass();

    private GT_Material_Glass() {
        super(MapColor.airColor);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}
