package gtPlusPlus.xmod.gregtech.api.objects;

public class MultiblockRequirements {

	public int mInputBusMinimum = 0;
	public int mInputHatchMinimum = 0;
	
	public int mOutputBusMinimum = 0;
	public int mOutputHatchMinimum = 0;
	
	public int mMaintMinimum = 1;
	
	public int mEnergyHatchMinimum = 1;
	public int mDynamoHatchMinimum = 0;
	
	public final int mMinimumCasingCount;
	
	public final MultiblockBlueprint mBlueprint;
	
	//public static final int mControlCoreMinimum = 1;
	/**
	 * 
	 * @param aInputBusses
	 * @param aOutputBusses
	 * @param aInputHatches
	 * @param aOutputHatches
	 * @param aEnergyHatches
	 * @param aDynamoHatches
	 * @param aMaintHatches
	 * @param aBlueprint - A data object containing the structural data for this Multiblock
	 */
	public MultiblockRequirements(int aCasingCount, MultiblockBlueprint aBlueprint) {
		mMinimumCasingCount = aCasingCount;
		mBlueprint = aBlueprint;
	}	

	public final int getInputBusMinimum() {
		return this.mInputBusMinimum;
	}

	public final MultiblockRequirements setInputBusMinimum(int mInputBusMinimum) {
		this.mInputBusMinimum = mInputBusMinimum;
		return this;
	}

	public final int getInputHatchMinimum() {
		return this.mInputHatchMinimum;
	}

	public final MultiblockRequirements setInputHatchMinimum(int mInputHatchMinimum) {
		this.mInputHatchMinimum = mInputHatchMinimum;
		return this;
	}

	public final int getOutputBusMinimum() {
		return this.mOutputBusMinimum;
	}

	public final MultiblockRequirements setOutputBusMinimum(int mOutputBusMinimum) {
		this.mOutputBusMinimum = mOutputBusMinimum;
		return this;
	}

	public final int getOutputHatchMinimum() {
		return this.mOutputHatchMinimum;
	}

	public final MultiblockRequirements setOutputHatchMinimum(int mOutputHatchMinimum) {
		this.mOutputHatchMinimum = mOutputHatchMinimum;
		return this;
	}

	public final int getMaintMinimum() {
		return this.mMaintMinimum;
	}

	public final MultiblockRequirements setMaintMinimum(int mMaintMinimum) {
		this.mMaintMinimum = mMaintMinimum;
		return this;
	}

	public final int getEnergyHatchMinimum() {
		return this.mEnergyHatchMinimum;
	}

	public final MultiblockRequirements setEnergyHatchMinimum(int mEnergyHatchMinimum) {
		this.mEnergyHatchMinimum = mEnergyHatchMinimum;
		return this;
	}

	public final int getDynamoHatchMinimum() {
		return this.mDynamoHatchMinimum;
	}

	public final MultiblockRequirements setDynamoHatchMinimum(int mDynamoHatchMinimum) {
		this.mDynamoHatchMinimum = mDynamoHatchMinimum;
		return this;
	}

	public final MultiblockBlueprint getBlueprint() {
		return this.mBlueprint;
	}

	public final int getMinimumCasingCount() {
		return this.mMinimumCasingCount;
	}
	
}
