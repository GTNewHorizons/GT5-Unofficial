package gregtech.api.enums;

public enum Dimensions {

    Overworld("0"),
    Moon("Moon"),
    Mercury("Mercury"),
    Venus("Venus"),
    Mars("Mars"),
    Io("Io"),
    Europa("Europa"),
    Callisto("Callisto"),
    Titan("Titan"),
    Miranda("Miranda"),
    Oberon("oberon"),
    Triton("Triton"),
    Proteus("Proteus"),
    Pluto("Pluto"),
    Makemake("Makemake"),
    AlphaCentauriBb("aCentauriBb"),
    BarnardaC("BarnardaC"),
    BarnardaE("BarnardaE"),
    BarnardaF("BarnardaF"),
    TCetiE("TCetiE"),
    Ross128b("Ross128b"),
    Ross128ba("Ross128ba"),;

    public final String id;

    private Dimensions(String id) {
        this.id = id;
    }
}
