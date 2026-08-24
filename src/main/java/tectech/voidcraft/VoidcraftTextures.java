package tectech.voidcraft;

import java.util.EnumMap;
import java.util.Map;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;

/**
 * Resolves each Voidcraft component's block texture exactly once and caches the result forever.
 *
 * <p>
 * <b>Why this class exists:</b> {@code Textures.BlockIcons.custom(name)} deduplicates its
 * {@code GTCustomBlockIconContainer}s in a name-keyed map — but {@code GTClient.onLoadComplete} calls
 * {@code Textures.BlockIcons.cleanup()}, which clears that map. Any lookup performed <em>after</em> that
 * (which is exactly when placed blocks are created, via {@code newMetaEntity}, or when a cover is mounted)
 * hands back a <em>fresh</em> container whose icon was never registered — the icon load phase has already
 * run — so {@code getIcon()} returns null and rendering NPEs in {@code GTIconFlipped}.
 *
 * <p>
 * Therefore: the texture must be resolved once, during the load phase (before the icon registration phase),
 * and that one {@link ITexture} must be reused everywhere forever. This cache is a plain static map that
 * {@code cleanup()} never touches, so it survives.
 */
public final class VoidcraftTextures {

    private static final Map<VoidcraftComponent, ITexture> COMPONENT_TEXTURES = new EnumMap<>(VoidcraftComponent.class);

    private VoidcraftTextures() {}

    /**
     * The shared block texture of a component (its {@code iconsets/EM_DIM_<meta>} icon on all faces).
     *
     * <p>
     * The first call must happen during the load phase (e.g. when registering the component MTEs); that is when
     * the icon container gets registered. Later calls simply return the cached instance — they must not
     * re-resolve the name.
     */
    public static ITexture componentTexture(VoidcraftComponent component) {
        return COMPONENT_TEXTURES.computeIfAbsent(
            component,
            c -> TextureFactory.of(Textures.BlockIcons.custom("iconsets/EM_DIM_" + c.getMeta())));
    }

    /**
     * Resolves every component texture (and every cover's mirrored component texture) up front.
     *
     * <p>
     * Must be called during the load phase — before the icon registration phase — so the icon containers are
     * actually registered. Idempotent; later calls only return cached textures.
     */
    public static void resolveAll() {
        for (VoidcraftComponent component : VoidcraftComponent.ALL) {
            componentTexture(component);
        }
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            componentTexture(cover.getMirroredComponent());
        }
    }
}
