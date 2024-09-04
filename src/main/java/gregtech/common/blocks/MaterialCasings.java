package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialCasings extends Material {

    public static final Material INSTANCE = new MaterialCasings();

    private MaterialCasings() {
        super(MapColor.ironColor);
        setRequiresTool();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
