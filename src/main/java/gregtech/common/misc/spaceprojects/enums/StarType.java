package gregtech.common.misc.spaceprojects.enums;

/**
 * @author BlueWeabo
 */
public enum StarType {

    OClass(30000, 100),
    BClass(10000, 20),
    AClass(20, 5),
    FClass(4, 2),
    GClass(1, 1),
    KClass(0.4, 0.5f),
    MClass(0.08, 0.1f),
    NotAStar(0, 0);

    private double solarLuminosity;
    private float costMultiplier;

    StarType(double solarLuminosity, float costMultiplier) {
        this.solarLuminosity = solarLuminosity;
        this.costMultiplier = costMultiplier;
    }

    public double getSolarLuminosity() {
        return solarLuminosity;
    }

    public float getCostMultiplier() {
        return costMultiplier;
    }
}
