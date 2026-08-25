package tectech.voidcraft.render;

import java.util.LinkedHashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.voidcraft.ship.VoidcraftBlueprint;

/**
 * Client-side LRU cache of built ship holograms, keyed by blueprint identity (dimensions + component grid).
 *
 * <p>
 * A digitized ship's geometry (hull grid + per-face covers) never changes after digitization, so the VBO is
 * built once and reused for every render of that ship — docked, in flight, or across re-launches. The key is
 * {@link VoidcraftBlueprint#hashCode()}, which covers the cover grid as well.
 *
 * <p>
 * Client main thread only (built during tile-entity rendering).
 */
@SideOnly(Side.CLIENT)
public final class VoidcraftShipModelCache {

    /** Cache capacity (distinct ship shapes kept in memory). */
    private static final int CAPACITY = 16;

    private static final Map<Integer, ShipModel> CACHE = new LinkedHashMap<Integer, ShipModel>(16, 0.75f, true) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, ShipModel> eldest) {
            return size() > CAPACITY;
        }
    };

    private VoidcraftShipModelCache() {
        throw new AssertionError("Static cache");
    }

    /**
     * Get (building if absent) the hologram for this blueprint.
     *
     * @param blueprint the ship geometry (non-null)
     * @return the model (never null)
     */
    public static ShipModel get(VoidcraftBlueprint blueprint) {
        int key = blueprint.hashCode();
        ShipModel model = CACHE.get(key);
        if (model == null) {
            model = ShipModelBuilder.build(blueprint);
            CACHE.put(key, model);
        }
        return model;
    }

    /**
     * Drop every cached hologram (e.g. on a client world switch — the VBOs are bound to the client context).
     */
    public static void clear() {
        for (ShipModel model : CACHE.values()) {
            if (model.vao != null) {
                model.vao.delete();
            }
        }
        CACHE.clear();
    }
}
