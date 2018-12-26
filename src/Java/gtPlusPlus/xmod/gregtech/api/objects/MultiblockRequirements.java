package gtPlusPlus.xmod.gregtech.api.objects;

public class MultiblockRequirements {

	public final int mInputBusMinimum;
	public final int mInputHatchMinimum;
	
	public final int mOutputBusMinimum;
	public final int mOutputHatchMinimum;
	
	public final int mMaintMinimum;
	
	public final int mEnergyHatchMinimum;
	public final int mDynamoHatchMinimum;
	
	public final MultiblockBlueprint mBlueprint;
	
	public static final int mControlCoreMinimum = 1;
	
	/**
	 * 
	 * @param aInputBusses
	 * @param aOutputBusses
	 * @param aInputHatches
	 * @param aOutputHatches
	 * @param aEnergyHatches
	 * @param aDynamoHatches
	 * @param aBlueprint - A data object containing the structural data for this Multiblock
	 */
	public MultiblockRequirements(final int aInputBusses, final int aOutputBusses, final int aInputHatches, final int aOutputHatches, final int aEnergyHatches, final int aDynamoHatches, final MultiblockBlueprint aBlueprint) {
		this(aInputBusses, aOutputBusses, aInputHatches, aOutputHatches, aEnergyHatches, aDynamoHatches, 1, aBlueprint);
	}
	
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
	public MultiblockRequirements(final int aInputBusses, final int aOutputBusses, final int aInputHatches, final int aOutputHatches, final int aEnergyHatches, final int aDynamoHatches, final int aMaintHatches, final MultiblockBlueprint aBlueprint) {
		mInputBusMinimum = aInputBusses;
		mOutputBusMinimum = aOutputBusses;		
		mInputHatchMinimum = aInputHatches;
		mOutputHatchMinimum = aOutputHatches;		
		mEnergyHatchMinimum = aEnergyHatches;
		mDynamoHatchMinimum = aDynamoHatches;		
		mMaintMinimum = aMaintHatches;
		mBlueprint = aBlueprint;
	}
	
}
