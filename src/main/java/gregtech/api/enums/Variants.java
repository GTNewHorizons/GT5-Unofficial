package gregtech.api.enums;

import com.LazyFlesh.variablehorizons.variants.VariantNames;

public enum Variants {

    NoRocket("NO_ROCKET"),
    GardenOfGrind("GARDEN_OF_GRIND"),
    AlteredPricing("ALTERED_PRICING"),

    ;

    public final String ID;

    Variants(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    };

    public static boolean checkIfActive(Variants... variants) {
        for (final Variants variant : variants) {
            if (VariantNames.activeContains(variant.ID)) {
                return true;
            }
        }
        return false;
    }
}
