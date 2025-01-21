package gregtech.api.util.recipe;

public class Sievert {

    public final int sievert;
    public final boolean isExact;

    public Sievert(int sievert, boolean isExact) {
        this.sievert = sievert;
        this.isExact = isExact;
    }

    public Sievert(int sievert) {
        this.sievert = sievert;
        isExact = false;
    }
}
