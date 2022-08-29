package gtPlusPlus.core.material;

public class Ion {

    private final Material mElement;
    private final boolean mContainsPositiveCharge;
    private final int mTotalIonization;

    public Ion(Material aMat, int chargeAmount) {
        mElement = aMat;
        mContainsPositiveCharge = (chargeAmount >= 0);
        mTotalIonization = chargeAmount;
    }

    public final synchronized Material getElement() {
        return mElement;
    }

    public final synchronized boolean containsPositiveCharge() {
        return mContainsPositiveCharge;
    }

    public final synchronized int getTotalIonization() {
        return mTotalIonization;
    }

    public final boolean isNeutral() {
        return mTotalIonization == 0;
    }
}
