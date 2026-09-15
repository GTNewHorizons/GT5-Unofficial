package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialReinforced extends Material {

    public MaterialReinforced() {
        super(MapColor.stoneColor);
        setRequiresTool();
        setAdventureModeExempt();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
