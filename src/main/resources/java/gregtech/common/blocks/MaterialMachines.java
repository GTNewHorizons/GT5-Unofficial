package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialMachines extends Material {

    public MaterialMachines() {
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
