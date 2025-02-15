package gregtech.common.capability;

import javax.annotation.Nullable;

import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;

public class CleanroomReference implements ICleanroomReceiver {

    private ICleanroom cleanroom;

    public CleanroomReference() {}

    @Nullable
    @Override
    public ICleanroom getCleanroom() {
        return this.cleanroom;
    }

    @Override
    public void setCleanroom(ICleanroom cleanroom) {
        this.cleanroom = cleanroom;
    }
}
