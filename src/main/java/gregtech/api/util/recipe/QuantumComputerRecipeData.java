package gregtech.api.util.recipe;

public class QuantumComputerRecipeData {

    public final float heatConstant, coolConstant, computation, maxHeat;
    public final boolean subZero;

    public QuantumComputerRecipeData(float heatConstant, float coolConstant, float computation, float maxHeat,
        boolean subZero) {
        this.heatConstant = heatConstant;
        this.coolConstant = coolConstant;
        this.computation = computation;
        this.maxHeat = maxHeat;
        this.subZero = subZero;
    }
}
