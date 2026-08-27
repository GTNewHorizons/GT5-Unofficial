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
    private static final Map<VoidcraftCoverComponent, ITexture> COVER_TEXTURES = new EnumMap<>(
        VoidcraftCoverComponent.class);

    private VoidcraftTextures() {}

    /**
     * PASS 23: the Voidcraft Frame's mostly-transparent framebox texture (a per-face square frame with a
     * fully-transparent interior — assembled from six faces it reads as a hollow wireframe box).
     */
    public static final String FRAME_ICON_NAME = "tectech:iconsets/VC_FRAME";

    /**
     * Cover icon names in {@link VoidcraftCoverComponent#getId()} order — each cover has its OWN 16×16 icon
     * (pass 24: the old {@code iconsets/EM_DIM_*} names pointed at the IORE dimension-block planet sheets — 16×64
     * multi-tile textures, which rendered squashed in-world and made covers like the armor plate look
     * unrendered).
     */
    /** Cover icon basenames in {@link VoidcraftCoverComponent#getId()} order (also exposed for the item icons). */
    public static final String[] COVER_ICON_BASE = { "VC_COVER_NOZZLE", "VC_COVER_ARMOR", "VC_COVER_POD",
        "VC_COVER_MINING", "VC_COVER_SIPHON", "VC_COVER_DISH", "VC_COVER_FABRICATOR", "VC_COVER_CELL",
        "VC_COVER_REPAIR", "VC_COVER_SOLAR" };

    private static final String[] COVER_ICON_NAMES = { "tectech:iconsets/" + COVER_ICON_BASE[0],
        "tectech:iconsets/" + COVER_ICON_BASE[1], "tectech:iconsets/" + COVER_ICON_BASE[2],
        "tectech:iconsets/" + COVER_ICON_BASE[3], "tectech:iconsets/" + COVER_ICON_BASE[4],
        "tectech:iconsets/" + COVER_ICON_BASE[5], "tectech:iconsets/" + COVER_ICON_BASE[6],
        "tectech:iconsets/" + COVER_ICON_BASE[7], "tectech:iconsets/" + COVER_ICON_BASE[8],
        "tectech:iconsets/" + COVER_ICON_BASE[9] };

    /** The controller's dedicated block icon (pass 24; was a planet-sheet squashed onto its faces). */
    public static final String CONTROLLER_ICON_NAME = "tectech:iconsets/VC_CONTROLLER";

    /** Multiblock component block icons — one dedicated 16×16 icon per entry: {@code VC_MBLK_<entry name>}. */
    public static final String MULTIBLOCK_ICON_PREFIX = "tectech:iconsets/VC_MBLK_";

    /**
     * The face texture of a cover part — its own dedicated 16×16 icon (also used as the cover ITEM icon, so the
     * inventory, the mounted face, and the in-flight model all show the same art).
     *
     * <p>
     * The first call must happen during the load phase (see {@link #resolveAll()}); later calls return the cached
     * instance and must not re-resolve the name.
     */
    public static ITexture coverTexture(VoidcraftCoverComponent cover) {
        return COVER_TEXTURES
            .computeIfAbsent(cover, c -> TextureFactory.of(Textures.BlockIcons.custom(COVER_ICON_NAMES[c.getId()])));
    }

    /**
     * The registered block-atlas name of a cover's icon — for client code that needs the {@code IIcon} itself
     * (UVs) rather than the {@link ITexture} (e.g. the in-flight ship model).
     */
    public static String coverIconName(VoidcraftCoverComponent cover) {
        return COVER_ICON_NAMES[cover.getId()];
    }

    /**
     * The shared block texture of a component, on all faces.
     *
     * <p>
     * Pass 23/24: the two placeable blocks have their own icons (controller panel, framebox), and the seven
     * cover-only catalog entries map to their cover part's icon (their face is what a mounted cover shows).
     *
     * <p>
     * The first call must happen during the load phase (e.g. when registering the component MTEs); that is when
     * the icon container gets registered. Later calls simply return the cached instance — they must not
     * re-resolve the name.
     */
    public static ITexture componentTexture(VoidcraftComponent component) {
        return COMPONENT_TEXTURES.computeIfAbsent(component, c -> resolveComponentTexture(c));
    }

    private static ITexture resolveComponentTexture(VoidcraftComponent c) {
        if (c == VoidcraftComponent.CONTROLLER) {
            return TextureFactory.of(Textures.BlockIcons.custom(CONTROLLER_ICON_NAME));
        }
        if (c == VoidcraftComponent.FRAME) {
            return TextureFactory.of(Textures.BlockIcons.custom(FRAME_ICON_NAME));
        }
        if (c.isMultiblock()) {
            // Multiblock component block — its own dedicated 16×16 icon (VC_MBLK_<entry name>)
            return TextureFactory.of(Textures.BlockIcons.custom(MULTIBLOCK_ICON_PREFIX + c.name()));
        }
        // cover-only catalog entry — the icon of the cover part that provides this function
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            if (cover.getMirroredComponent() == c) {
                return coverTexture(cover);
            }
        }
        throw new IllegalStateException("No texture for component " + c);
    }

    /**
     * Resolves every component and cover texture up front.
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
            coverTexture(cover);
        }
    }
}
