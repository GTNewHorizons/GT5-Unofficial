package gregtech.common.tools;

public class ToolTurbineNormal extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 2.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 2.0F;
    }

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }
}
