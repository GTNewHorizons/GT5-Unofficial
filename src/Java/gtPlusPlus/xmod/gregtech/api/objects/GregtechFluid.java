package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.GregTech_API;

import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.fluids.Fluid;

public class GregtechFluid extends Fluid implements Runnable {
	public final String mTextureName;
	private final short[] mRGBa;

	public GregtechFluid(final String aName, final String aTextureName, final short[] aRGBa) {
		super(aName);
		this.mRGBa = aRGBa;
		this.mTextureName = aTextureName;
		GregTech_API.sGTBlockIconload.add(this);
	}

	@Override
	public int getColor() {
		return (Math.max(0, Math.min(255, this.mRGBa[0])) << 16) | (Math.max(0, Math.min(255, this.mRGBa[1])) << 8) | Math.max(0, Math.min(255, this.mRGBa[2]));
	}

	@Override
	public void run() {
		this.setIcons(GregTech_API.sBlockIcons.registerIcon(CORE.MODID+ ":" + "fluids/fluid." + this.mTextureName));
	}
}