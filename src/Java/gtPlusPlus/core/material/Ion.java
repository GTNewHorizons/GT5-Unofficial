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
	
	public synchronized final Material getElement() {
		return mElement;
	}

	public synchronized final boolean containsPositiveCharge() {
		return mContainsPositiveCharge;
	}

	public synchronized final int getTotalIonization() {
		return mTotalIonization;
	}
	
	public final boolean isNeutral() {
		return mTotalIonization == 0;
	}
}
