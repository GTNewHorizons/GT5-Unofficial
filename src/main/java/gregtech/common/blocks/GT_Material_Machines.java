package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class GT_Material_Machines extends Material {

    public GT_Material_Machines() {
        super(MapColor.ironColor);
        setRequiresTool();
        setImmovableMobility();
        setAdventureModeExempt();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}
