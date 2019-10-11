package gtPlusPlus.core.item.base.dusts;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemDust extends BaseItemComponent {

	private BaseItemComponent[] mSizedDusts = new BaseItemComponent[2];

	public BaseItemDust(Material aMat) {
		this(aMat, true);
	}

	public BaseItemDust(Material aMat, boolean generateSmallDusts) {
		super(aMat, ComponentTypes.DUST);
		if (generateSmallDusts) {
			mSizedDusts[0] = new BaseItemComponent(aMat, ComponentTypes.DUSTSMALL);
			mSizedDusts[1] = new BaseItemComponent(aMat, ComponentTypes.DUSTTINY);
		}
	}

	public BaseItemDust(DustState aState, Material aMat) {
		super(aMat, ComponentTypes.DUST);		
		if (aState.generatesSmallDust()) {
			mSizedDusts[0] = new BaseItemComponent(aMat, ComponentTypes.DUSTSMALL);			
		}
		if (aState.generatesTinyDust()) {
			mSizedDusts[1] = new BaseItemComponent(aMat, ComponentTypes.DUSTTINY);			
		}
	}

	private BaseItemDust(String unlocalizedName, String materialName, Material matInfo, int colour, String pileSize, int tier, boolean addRecipes) {
		super(matInfo, ComponentTypes.DUST);
	}

	public static class DustState {
		static final int NORMAL = (1);
		static final int SMALL = (10);
		static final int TINY = (100);
		final int MIXTURE;	
		final boolean[] doesThings = new boolean[3];

		public DustState (boolean genDust, boolean genSmallDust, boolean genDustTiny){
			int aTotal = 0;
			if (genDust) {
				aTotal += NORMAL;
				doesThings[0] = true;
			}
			else {
				doesThings[0] = false;				
			}
			if (genSmallDust) {
				aTotal += SMALL;
				doesThings[1] = true;
			}
			else {
				doesThings[1] = false;				
			}
			if (genDustTiny) {
				aTotal += TINY;
				doesThings[2] = true;
			}
			else {
				doesThings[2] = false;				
			}
			MIXTURE = aTotal;
		}

		public boolean generatesDust() {
			return doesThings[0];
		}
		public boolean generatesSmallDust() {
			return doesThings[1];
		}
		public boolean generatesTinyDust() {
			return doesThings[2];
		}

		private DustState(int amount) {
			
			if (amount == 1) {
				doesThings[0] = true;
				doesThings[1] = false;
				doesThings[2] = false;
				
			}
			else if (amount == 10) {
				doesThings[0] = false;
				doesThings[1] = true;
				doesThings[2] = false;
				
			}
			else if (amount == 100) {
				doesThings[0] = false;
				doesThings[1] = false;
				doesThings[2] = true;
				
			}
			else if (amount == 11) {
				doesThings[0] = true;
				doesThings[1] = true;
				doesThings[2] = false;
				
			}
			else if (amount == 101) {
				doesThings[0] = true;
				doesThings[1] = false;
				doesThings[2] = true;
				
			}
			else if (amount == 110) {
				doesThings[0] = false;
				doesThings[1] = true;
				doesThings[2] = true;
				
			}
			else if (amount == 111) {
				doesThings[0] = true;
				doesThings[1] = true;
				doesThings[2] = true;				
			}
			else {
				doesThings[0] = false;
				doesThings[1] = false;
				doesThings[2] = false;
			}
			MIXTURE = amount;			
		}

		public DustState get(int a) {
			if (a == 1) {
				return new DustState(NORMAL);
			}
			else if (a == 10) {
				return new DustState(SMALL);
			}
			else if (a == 100) {
				return new DustState(TINY);
			}
			else {
				return new DustState(MIXTURE);
			}
		}
	}

}
