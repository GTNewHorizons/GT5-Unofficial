package tectech.voidcraft.cover;

import javax.annotation.Nullable;

import net.minecraft.util.StatCollector;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.common.covers.Cover;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;

/**
 * A Voidcraft component cover: a compact part mounted on a face of a Voidcraft hull block.
 *
 * <p>
 * Renders the cover part's own 16×16 icon as a full-face texture over the hull (each cover has a dedicated
 * icon). Carries its
 * {@link VoidcraftCoverComponent} so the assembler scan and the stat math can see it (covers count toward the ship's
 * stats, and thruster covers count toward thrust only on the ship's REAR face, the assembler side).
 */
public class CoverVoidcraftComponent extends Cover {

    @Nullable
    private final VoidcraftCoverComponent component;
    private final ITexture faceTexture;

    public CoverVoidcraftComponent(CoverContext context) {
        super(context, null);
        this.component = VoidcraftCoverRegistry.byStack(context.getCoverItem());
        // Cover instances are constructed at mount time — long after the icon phase — so the texture must come
        // from the load-phase cache (VoidcraftTextures), never re-resolved by name here.
        this.faceTexture = component == null ? null : VoidcraftTextures.coverTexture(component);
    }

    /** Full-face texture so the mounted part replaces the face art. */
    @Override
    public ITexture getSpecialFaceTexture() {
        return faceTexture;
    }

    /** Fallback overlay (same icon) if a renderer only draws overlays. */
    @Override
    public ITexture getOverlayTexture() {
        return faceTexture == null ? super.getOverlayTexture() : faceTexture;
    }

    @Override
    public String getDescription() {
        return component == null ? "" : StatCollector.translateToLocal(component.getLangKey());
    }

    /** @return the cover part behind this cover instance (null only if the item was unregistered at build time) */
    @Nullable
    public VoidcraftCoverComponent getComponent() {
        return component;
    }
}
