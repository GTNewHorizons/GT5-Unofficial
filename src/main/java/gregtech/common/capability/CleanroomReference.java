package gregtech.common.capability;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.util.FakeCleanroom;

public class CleanroomReference implements ICleanroomReceiver {

    private Set<ICleanroom> cleanrooms;

    public CleanroomReference() {}

    @Nullable
    @Override
    public ICleanroom getCleanroom() {
        if (FakeCleanroom.isCleanroomBypassEnabled()) return FakeCleanroom.INSTANCE;
        if (cleanrooms == null) return null;

        ICleanroom cleanest = null;
        int cleanness = 0;
        for (ICleanroom room : cleanrooms) {
            if (!room.isValidCleanroom()) continue;
            int roomCleanness = room.getCleanness();
            if (cleanest == null || roomCleanness > cleanness) {
                cleanest = room;
                cleanness = roomCleanness;
            }
        }
        return cleanest;
    }

    @Override
    public void setCleanroom(@Nullable ICleanroom cleanroom) {
        if (cleanroom == null) {
            if (cleanrooms != null && !cleanrooms.isEmpty()) {
                cleanrooms.clear();
                onCleanroomChanged();
            }
        } else {
            if (cleanrooms != null) cleanrooms.clear();
            addCleanroom(cleanroom);
        }
    }

    @Override
    public void addCleanroom(ICleanroom cleanroom) {
        Objects.requireNonNull(cleanroom);
        if (cleanrooms == null) cleanrooms = Collections.newSetFromMap(new IdentityHashMap<>());
        if (cleanrooms.add(cleanroom)) onCleanroomChanged();
    }

    @Override
    public void removeCleanroom(ICleanroom cleanroom) {
        if (cleanrooms != null && cleanrooms.remove(cleanroom)) onCleanroomChanged();
    }

}
