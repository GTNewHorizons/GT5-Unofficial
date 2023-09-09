package gregtech.api.util;

import gregtech.api.enums.Materials;

public class ExternalMaterials {

    public static Materials getRhodiumPlatedPalladium() {
        return Materials.getWithFallback("Rhodium-PlatedPalladium", Materials.Chrome);
    }

    public static Materials getRuridit() {
        return Materials.getWithFallback("Ruridit", Materials.Osmiridium);
    }
}
