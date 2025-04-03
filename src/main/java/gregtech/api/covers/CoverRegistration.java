package gregtech.api.covers;

final class CoverRegistration {

    private final CoverFactory factory;
    private final CoverPlacer coverPlacer;

    CoverRegistration(CoverFactory factory, CoverPlacer coverPlacer) {
        this.factory = factory;
        this.coverPlacer = coverPlacer;
    }

    CoverFactory getFactory() {
        return factory;
    }

    CoverPlacer getCoverPlacer() {
        return coverPlacer;
    }
}
