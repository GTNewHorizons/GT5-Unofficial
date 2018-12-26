package gtPlusPlus.xmod.gregtech.common.blueprint;

import gregtech.api.GregTech_API;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockBlueprint;
import gtPlusPlus.xmod.gregtech.api.objects.MultiblockLayer;
import net.minecraft.init.Blocks;

public class Blueprint_Generic_3x3 extends MultiblockBlueprint {

	public Blueprint_Generic_3x3(int aTextureID) {
		super(3, 3, 3, 10, aTextureID);

		// Top/Bottom
		MultiblockLayer a0 = new MultiblockLayer(3, 3);		
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 0, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 1, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 2, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 1, 0, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 1, 1, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 1, 2, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 0, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 1, true);
		a0.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 1, true);
		a0.lock(true);

		//Layer one
		MultiblockLayer a1 = new MultiblockLayer(3, 3);		
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 0, true);
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 1, true);
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 0, 2, true);
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 1, 0, true);
		a1.addBlockForPos(Blocks.air, 0, 1, 1, true);		
		a1.addController(1, 2);		
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 0, true);		
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 1, true);
		a1.addBlockForPos(GregTech_API.sBlockCasings2, 4, 2, 1, true);
		a1.lock(true);

		this.setLayer(a0, 0);
		this.setLayer(a1, 1);
		this.setLayer(a0, 2);
	}

	@Override
	public int getMinimumInputBus() {
		return 0;
	}

	@Override
	public int getMinimumInputHatch() {
		return 0;
	}

	@Override
	public int getMinimumOutputBus() {
		return 0;
	}

	@Override
	public int getMinimumOutputHatch() {
		return 0;
	}

	@Override
	public int getMinimumInputEnergy() {
		return 1;
	}

	@Override
	public int getMinimumOutputEnergy() {
		return 0;
	}

	@Override
	public int getMinimumMaintHatch() {
		return 1;
	}

	@Override
	public int getMinimumMufflers() {
		return 1;
	}

}
