package gregtech.common.tools;

public class ToolTurbineHuge extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 4.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    @Override
    public float getBaseDamage() {
        return 7.5F;
    }
}
