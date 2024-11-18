package gregtech.api.util.recipe;

public class AORecipeData {

    public final int requiredIntelligence;
    public final int requiredCount;
    public final int dangerLevel;

    public AORecipeData(int requiredIntelligence, int requiredCount, int dangerLevel) {
        this.requiredIntelligence = requiredIntelligence;
        this.requiredCount = requiredCount;
        this.dangerLevel = dangerLevel;
    }

}
