package detrav.client;

import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.IIconContainer;

/** A temporary client-side prospecting marker dropped from the Detrav scanner GUI. */
public class DetravOreMarker {

    public final int dim;
    public final int x;
    public final int y;
    public final int z;
    public final String name;
    /** ARGB ore colour, used to tint the ore icon. */
    public final int color;
    /** Resolved GT ore icon, or null when the material could not be resolved. */
    @Nullable
    public final IIconContainer ore;
    /** When this marker was placed, for the expiry timeout. */
    public final long createdAt;

    public DetravOreMarker(int dim, int x, int y, int z, String name, int color, @Nullable IIconContainer ore) {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.color = color;
        this.ore = ore;
        this.createdAt = System.currentTimeMillis();
    }
}
