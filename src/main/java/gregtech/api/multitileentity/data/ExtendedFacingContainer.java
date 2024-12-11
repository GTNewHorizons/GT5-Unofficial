package gregtech.api.multitileentity.data;

import com.badlogic.ashley.core.Component;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

public class ExtendedFacingContainer implements Component {
    private ExtendedFacing facing;

    public ExtendedFacingContainer(ExtendedFacing facing) {
        this.facing = facing;
    }

    public ExtendedFacing getExtendedFacing() {
        return facing;
    }

    public void setExtendedFacing(ExtendedFacing facing) {
        this.facing = facing;
    }
}
