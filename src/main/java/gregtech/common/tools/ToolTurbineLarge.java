package gregtech.common.tools;

public class ToolTurbineLarge extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 3.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 3.0F;
    }

    @Override
    public float getBaseDamage() {
        return 5.0F;
    }
}
