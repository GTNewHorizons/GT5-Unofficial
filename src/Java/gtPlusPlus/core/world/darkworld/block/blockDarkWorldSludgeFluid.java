package gtPlusPlus.core.world.darkworld.block;

import net.minecraft.block.material.*;
import net.minecraftforge.fluids.Fluid;

public class blockDarkWorldSludgeFluid extends Fluid {


	public static final Material SLUDGE = new MaterialLiquid(MapColor.dirtColor);

	protected static int mapColor = 0xFFFFFFFF;
	protected static float overlayAlpha = 0.2F;
	//protected static SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
	//protected static SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
	protected static Material material = SLUDGE;


	public blockDarkWorldSludgeFluid(String fluidName, int rgbColour) {
		this(fluidName, rgbColour, null);
	}
	
	public blockDarkWorldSludgeFluid(String fluidName, int rgbColour, Float overlayAlpha) {
		super(fluidName);
		setColor(rgbColour);
		if (overlayAlpha != null){
			setAlpha(overlayAlpha.floatValue());			
		}
		else {
			setAlpha(0);
		}
	}

	@Override
	public int getColor()
	{
		return mapColor;
	}

	public blockDarkWorldSludgeFluid setColor(int parColor)
	{
		mapColor = parColor;
		return this;
	}

	public float getAlpha()
	{
		return overlayAlpha;
	}

	public blockDarkWorldSludgeFluid setAlpha(float parOverlayAlpha)
	{
		overlayAlpha = parOverlayAlpha;
		return this;
	}

	/*public blockDarkWorldSludgeFluid setEmptySound(SoundEvent parSound)
	{
		emptySound = parSound;
		return this;
	}

	public SoundEvent getEmptySound()
	{
		return emptySound;
	}

	@Override
	public blockDarkWorldSludgeFluid setFillSound(SoundEvent parSound)
	{
		fillSound = parSound;
		return this;
	}

	@Override
	public SoundEvent getFillSound()
	{
		return fillSound;
	}*/

	public blockDarkWorldSludgeFluid setMaterial(Material parMaterial)
	{
		material = parMaterial;
		return this;
	}

	public Material getMaterial()
	{
		return material;
	}

	/*@Override
	public boolean doesVaporize(FluidStack fluidStack)
	{
		if (block == null)
			return false;
		return block.getDefaultState().getMaterial() == getMaterial();
	}*/
}
