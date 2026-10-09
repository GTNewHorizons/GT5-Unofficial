package gregtech.api.enums;

public enum VariantNames {

    NoRocket("NO_ROCKET"),

    ;

    public final String ID;

    VariantNames(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    };
}
