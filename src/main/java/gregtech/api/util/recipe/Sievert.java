package gregtech.api.util.recipe;

public record Sievert(int sievert, boolean isExact) {

    public Sievert(int sievert) {
        this(sievert, false);
    }
}
