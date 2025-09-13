package gregtech.api.enums;

public enum DimensionConditions {

    // T0
    OVERWORLD("Overworld", 290, 101000),
    NETHER("Nether", 900, 200000),
    TWILIGHT("Twilight", 270, 101000),
    END("End", 220, 0),
    // T1
    MOON("Moon", 250, 0),
    // T2
    DEIMOS("Deimos", 233, 0),
    MARS("Mars", 215, 600),
    PHOBOS("Phobos", 233, 0),
    // T3
    ASTEROIDS("Asteroids", 200, 0),
    CALLISTO("Callisto", 134, 0),
    CERES("Ceres", 168, 0),
    EUROPA("Europa", 102, 0),
    GANYMEDE("Ganymede", 132, 0),
    ROSS128B("Ross128b", 295, 101000),
    // T4
    IO("Io", 130, 0),
    MERCURY("Mercury", 440, 0),
    VENUS("Venus", 740, 9200000),
    // T5
    ENCELADUS("Enceladus", 70, 1),
    MIRANDA("Miranda", 60, 0),
    OBERON("Oberon", 70, 0),
    TITAN("Titan", 94, 146700),
    ROSS128BA("Ross128ba", 285, 101000),
    // T6
    PROTEUS("Proteus", 50, 0),
    TRITON("Triton", 38, 1),
    // T7
    HAUMEA("Haumea", 32, 0),
    KUIPERBELT("Kuiperbelt", 50, 0),
    MAKEMAKE("Makemake", 30, 0),
    PLUTO("Pluto", 44, 1),
    // T8
    BARNARD_C("BarnardC", 290, 110000),
    BARNARD_E("BarnardE", 170, 30000),
    BARNARD_F("BarnardF", 105, 0),
    CENTAURI_A("CentauriA", 1500, 0),
    TCETI_E("TCetiE", 320, 85000),
    VEGA_B("VegaB", 210, 0),
    // T9
    ANUBIS("Anubis", 300, 10000),
    HORUS("Horus", 350, 15000),
    MAAHES("Maahes", 120, 0),
    NEPER("Neper", 30, 0),
    SETH("Seth", 24, 43000),
    // T10
    UNDERDARK("Underdark", 270, 131000);

    private final String dimensionName;
    private final int ambientTemp;
    private final int ambientPressure;

    DimensionConditions(String dimensionName, int ambientTemp, int ambientPressure) {
        this.dimensionName = dimensionName;
        this.ambientTemp = ambientTemp;
        this.ambientPressure = ambientPressure;
    }

    public int getAmbientTemp() {
        return ambientTemp;
    }

    public int getAmbientPressure() {
        return ambientPressure;
    }

    public static DimensionConditions fromDimensionName(String dimensionName) {
        for (DimensionConditions condition : values()) {
            if (condition.dimensionName.equalsIgnoreCase(dimensionName)) {
                return condition;
            }
        }
        return OVERWORLD;
    }
}
