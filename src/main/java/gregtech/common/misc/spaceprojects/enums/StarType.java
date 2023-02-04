package gregtech.common.misc.spaceprojects.enums;

public enum StarType {

    OClass(30000, 100),
    BClass(10000, 20),
    AClass(20, 5),
    FClass(4, 2),
    GClass(1, 1),
    KClass(0.4, 0.5f),
    MClass(0.08, 0.1f),
    NotAStar(0, 0);

    private double mSolarLuminosity;
    private float mCostMultiplier;

    private StarType(double aSolarLuminosity, float aCostMultiplier) {
        mSolarLuminosity = aSolarLuminosity;
        mCostMultiplier = aCostMultiplier;
    }

    public double getSolarLuminosity() {
        return mSolarLuminosity;
    }

    public float getCostMultiplier() {
        return mCostMultiplier;
    }
}
