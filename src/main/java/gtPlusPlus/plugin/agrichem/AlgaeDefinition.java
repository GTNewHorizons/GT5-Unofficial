package gtPlusPlus.plugin.agrichem;

public enum AlgaeDefinition {

	/*
	 * In general, the productivity of freshwater algae is primarily limited by 
	 * the availability of the nutrient phosphate (PO4-3), while that of 
	 * marine algae is limited by nitrate (NO3-) or ammonium (NH4+). 
	 * Some algal species, however, may have unusual nutrient requirements, 
	 * and their productivity may be limited by certain micronutrients, 
	 * such as silica, in the case of diatoms.
	 */
	
	Euglenophyta("Euglenophyta", "Euglenoids", true, false, getRGB(147, 168, 50)),
	Chrysophyta("Chrysophyta", "Golden-Brown Algae", true, true, getRGB(186, 146, 0)),
	Pyrrophyta("Pyrrophyta", "Fire Algae", true, true, getRGB(250, 118, 2)),
	Chlorophyta("Chlorophyta", "Green Algae", true, true, getRGB(99, 181, 62)),
	Rhodophyta("Rhodophyta", "Red Algae", false, true, getRGB(153, 5, 22)),
	Paeophyta("Paeophyta", "Brown Algae", false, true, getRGB(94, 78, 47)),
	Xanthophyta("Xanthophyta", "Yellow-Green Algae", true, false, getRGB(118, 138, 16));	

	public final String mScientificName;
	public final String mSimpleName;
	public final boolean mSaltWater;
	public final boolean mFreshWater;
	public final int mColour;
	
	AlgaeDefinition(String aScientificName, String aSimpleName, boolean aFresh, boolean aSalt, int aColour) {
		mScientificName = aScientificName;
		mSimpleName = aSimpleName;
		mFreshWater = aFresh;
		mSaltWater = aSalt;
		mColour = aColour;
	}
	
	public static AlgaeDefinition getByIndex(int aIndex) {
		switch(aIndex) {
			  default:
			  case 0:
				 return Euglenophyta;
			  case 1:
				 return Chrysophyta;
			  case 2:
				 return Pyrrophyta;
			  case 3:
				 return Chlorophyta;
			  case 4:
				 return Rhodophyta;
			  case 5:
				 return Paeophyta;
			  case 6:
				 return Xanthophyta;    
			}
	}
	
	private final static int getRGB(int r, int g, int b) {
		return AlgaeUtils.rgbtoHexValue(r, g, b);
	}
	
}
