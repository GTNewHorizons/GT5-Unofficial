package gregtech.client.volumetric;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class StaticSound implements ISoundPosition {

    private final Vector3f pos;

    public StaticSound(float x, float y, float z) {
        this.pos = new Vector3f(x, y, z);
    }

    @Override
    public @Nullable Vector3f getPosition() {
        return pos;
    }
}
