package gtPlusPlus.xmod.gregtech.common.blueprint;

import gregtech.api.enums.TAE;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockBlueprint;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockLayer;
import net.minecraft.block.Block;

public class Blueprint_LFTR extends MultiblockBlueprint {

	public Blueprint_LFTR() {
		super(7, 4, 7, 10, TAE.GTPP_INDEX(12));


		Block aCasingMain = ModBlocks.blockCasingsMisc;
		int aMetaCasingMain = 12;
		int aMetaCasingSecondary = 13;

		
		
		/**
		 * First Layer (All edges can be Hatches, controller is centered in the front)
		 */
		
		MultiblockLayer a0 = new MultiblockLayer(7, 7);
		for (int i = 0; i < 7; i++) {
			a0.addBlockForPos(aCasingMain, aMetaCasingMain, i, 0, true);			
		}
		for (int i = 0; i < 7; i++) {
			for (int u = 1; u < 6; u++) {
				a0.addBlockForPos(aCasingMain, aMetaCasingMain, i, u, i == 0 ? true : i == 6 ? true : false);		
			}
		}
		for (int i = 0; i < 7; i++) {
			if (i != 3) {
				a0.addBlockForPos(aCasingMain, aMetaCasingMain, i, 6, true);					
			}
			else {	
				a0.addController(i, 6);				
			}		
		}
		a0.lock(true);


		
		
		/**
		 * Middle Layer(s)
		 */
		
		MultiblockLayer a1 = new MultiblockLayer(7, 7);
		for (int i = 0; i < 7; i++) {
			a1.addBlockForPos(aCasingMain, aMetaCasingSecondary, i, 0, false);			
		}
		for (int i = 0; i < 7; i++) {
			for (int u = 1; u < 6; u++) {
				if (i == 0 || i == 6)
					a1.addBlockForPos(aCasingMain, aMetaCasingSecondary, i, u, false);		
			}
		}
		for (int i = 0; i < 7; i++) {
			a1.addBlockForPos(aCasingMain, aMetaCasingSecondary, i, 6, false);					

		}
		a1.lock(true);

		
		/**
		 * Top Layer  (All edges can be Hatches, Mufflers required in inner 3x3)
		 */
		
		MultiblockLayer a2 = new MultiblockLayer(7, 7);
		for (int i = 0; i < 7; i++) {
			a2.addBlockForPos(aCasingMain, aMetaCasingMain, i, 0, true);			
		}
		for (int i = 0; i < 7; i++) {
			for (int u = 1; u < 6; u++) {				
				if ((i == 2 || i == 3 || i == 4) && (u == 2 || u ==3 || u == 4)) {
					a2.addMuffler(aCasingMain, aMetaCasingMain, i, u);
				}
				else {
					a2.addBlockForPos(aCasingMain, aMetaCasingMain, i, u, true);					
				}					
			}
		}
		for (int i = 0; i < 7; i++) {
			a2.addBlockForPos(aCasingMain, aMetaCasingMain, i, 6, true);				
		}
		a2.lock(true);

		this.setLayer(a0, 0);
		this.setLayer(a1, 1);
		this.setLayer(a1, 2);
		this.setLayer(a2, 3);
	}

	@Override
	public int getMinimumInputBus() {
		return 0;
	}

	@Override
	public int getMinimumInputHatch() {
		return 4;
	}

	@Override
	public int getMinimumOutputBus() {
		return 0;
	}

	@Override
	public int getMinimumOutputHatch() {
		return 4;
	}

	@Override
	public int getMinimumInputEnergy() {
		return 0;
	}

	@Override
	public int getMinimumOutputEnergy() {
		return 4;
	}

	@Override
	public int getMinimumMaintHatch() {
		return 1;
	}

	@Override
	public int getMinimumMufflers() {
		return 4;
	}

}
