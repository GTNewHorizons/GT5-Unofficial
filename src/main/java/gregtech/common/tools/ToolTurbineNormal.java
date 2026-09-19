package gregtech.common.tools;

public class ToolTurbineNormal extends ToolTurbine {

    @Override
    public float getSpeedMultiplier() {
        return 2.0F;
    }

    /**
     * A hundred times the rotor size, because a rotor counts its durability in hundredths of a point: that is the
     * unit the multiblock wear formula has always produced, and a single wear event is worth far less than a whole
     * point. Keeping the item on that scale means nothing has to convert, so the numbers a machine compares against
     * mean what they did before rotors became items of their own.
     */
    @Override
    public float getMaxDurabilityMultiplier() {
        return 200.0F;
    }

    @Override
    public float getBaseDamage() {
        return 2.5F;
    }
}
