package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GT_Material_Reinforced extends Material {

    public GT_Material_Reinforced() {
        super(MapColor.stoneColor);
        setRequiresTool();
        setAdventureModeExempt();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
