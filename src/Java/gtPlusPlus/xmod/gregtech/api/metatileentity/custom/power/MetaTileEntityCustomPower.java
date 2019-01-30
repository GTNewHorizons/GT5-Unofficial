package gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.CustomMetaTileBase;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class MetaTileEntityCustomPower extends CustomMetaTileBase {

	public MetaTileEntityCustomPower(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
		super(aID, aBasicName, aRegionalName, aInvSlotCount);
		this.setBaseMetaTileEntity(Meta_GT_Proxy.constructBaseMetaTileEntityCustomPower());
	}
	
	public MetaTileEntityCustomPower(String aStack, int aInvSlotCount) {
		super(aStack, aInvSlotCount);
	}
	
	public long getMinimumStoredEU() {
		return 0L;
	}

	public void doExplosion(long aExplosionPower) {
		
		if (MathUtils.randInt(1, 10) > 0) {
			Logger.INFO("Machine tried to explode, let's stop that. xo");
			return;
		}
		
		float tStrength = aExplosionPower < GT_Values.V[0]
				? 1.0F
				: (aExplosionPower < GT_Values.V[1]
						? 2.0F
						: (aExplosionPower < GT_Values.V[2]
								? 3.0F
								: (aExplosionPower < GT_Values.V[3]
										? 4.0F
										: (aExplosionPower < GT_Values.V[4]
												? 5.0F
												: (aExplosionPower < GT_Values.V[4] * 2L
														? 6.0F
														: (aExplosionPower < GT_Values.V[5]
																? 7.0F
																: (aExplosionPower < GT_Values.V[6]
																		? 8.0F
																		: (aExplosionPower < GT_Values.V[7]
																				? 9.0F
																				: 10.0F))))))));
		int tX = this.getBaseMetaTileEntity().getXCoord();
		short tY = this.getBaseMetaTileEntity().getYCoord();
		int tZ = this.getBaseMetaTileEntity().getZCoord();
		World tWorld = this.getBaseMetaTileEntity().getWorld();
		GT_Utility.sendSoundToPlayers(tWorld, (String) GregTech_API.sSoundList.get(Integer.valueOf(209)), 1.0F, -1.0F,
				tX, tY, tZ);
		tWorld.setBlock(tX, tY, tZ, Blocks.air);
		if (GregTech_API.sMachineExplosions) {
			tWorld.createExplosion((Entity) null, (double) tX + 0.5D, (double) tY + 0.5D, (double) tZ + 0.5D, tStrength,
					true);
		}

	}

	@Override
	public void onExplosion() {
		
		if (MathUtils.randInt(1, 10) > 0) {
			Logger.INFO("Machine tried to explode, let's stop that. xo");
			return;
		}
		// TODO Auto-generated method stub
		super.onExplosion();
	}

	@Override
	public boolean isElectric() {
		return true;
	}

	@Override
	public long getEUVar() {
		// TODO Auto-generated method stub
		return super.getEUVar();
	}

}
