package gregtech.api.covers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.ITexture;

final class CoverRegistration {

    private final CoverFactory factory;
    private final CoverPlacer coverPlacer;
    private final ITexture coverTexture;

    CoverRegistration(@NotNull CoverFactory factory, @NotNull CoverPlacer coverPlacer,
        @Nullable ITexture coverTexture) {
        this.factory = factory;
        this.coverPlacer = coverPlacer;
        this.coverTexture = coverTexture;
    }

    CoverFactory getFactory() {
        return factory;
    }

    CoverPlacer getCoverPlacer() {
        return coverPlacer;
    }

    public ITexture getCoverTexture() {
        return coverTexture;
    }
}
