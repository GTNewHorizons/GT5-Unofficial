package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;

public class GT_MetaTileEntity_Hatch_AirIntake extends GT_MetaTileEntity_Hatch_Input {
	private static XSTR floatGen;

	public GT_MetaTileEntity_Hatch_AirIntake(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_Hatch_AirIntake(final String aName, final int aTier, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	/*public GT_MetaTileEntity_Hatch_AirIntake(final String aName, final int aTier, final String[] aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}*/

	private static String[] S;
	private static Field F;

	public synchronized String[] getDescription() {
		try {
			if (F == null || S == null) {
				Field t = ReflectionUtils.getField(this.getClass(), "mDescriptionArray");
				if (t != null) {
					F = t;
				}
				else {
					F = ReflectionUtils.getField(this.getClass(), "mDescription");
				}
				if (S == null && F != null) {
					Object o = F.get(this);
					if (o instanceof String[]) {
						S = (String[]) o;
					}
					else if (o instanceof String) {
						S = new String[] {(String) o};
					}
				}

			}
		}
		catch (Throwable t) {

		}
		if (S != null) {
			final String[] desc = new String[S.length + 3];
			System.arraycopy(S, 0, desc, 0, S.length);
			desc[S.length] = "DO NOT OBSTRUCT THE INPUT!";
			desc[S.length + 1] = "Draws in Air from the surrounding environment";
			desc[S.length + 2] = "Creates 1000L of Air every 4 ticks";
			return desc;
		}
		else {
			return new String[] {"DO NOT OBSTRUCT THE INPUT!", "Draws in Air from the surrounding environment", "Creates 1000L of Air every 4 ticks"};
		}


	}

	public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_MUFFLER)};
	}

	public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture,
				new GT_RenderedTexture((IIconContainer) Textures.BlockIcons.OVERLAY_MUFFLER)};
	}

	public boolean isSimpleMachine() {
		return true;
	}

	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	public boolean isValidSlot(final int aIndex) {
		return false;
	}

	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_AirIntake(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);	
		if (addAirToHatch(aTick)) {
			if (aTick % 8 == 0) {
				if (aBaseMetaTileEntity.isClientSide()) {					
					this.pollutionParticles(this.getBaseMetaTileEntity().getWorld(), "cloud");
				}
			}
		}	
	}

	public void pollutionParticles(final World aWorld, final String name) {

		final float ran1 = GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat();
		float ran2 = 0.0f;
		float ran3 = 0.0f;
		ran2 = GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat();
		ran3 = GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat();

		final IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
		final ForgeDirection aDir = ForgeDirection.getOrientation((int) aMuffler.getFrontFacing());
		final float xPos = aDir.offsetX * 0.76f + aMuffler.getXCoord() + 0.25f;
		float yPos = aDir.offsetY * 0.76f + aMuffler.getYCoord() + 0.65f;
		final float zPos = aDir.offsetZ * 0.76f + aMuffler.getZCoord() + 0.25f;
		float ySpd = aDir.offsetY * 0.1f + 0.2f + 0.1f * GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat();
		float xSpd;
		float zSpd;
		if (aDir.offsetY == -1) {
			//Logger.INFO("Y = -1");
			final float temp = GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 2.0f * CORE.PI;
			xSpd = (float) Math.sin(temp) * 0.1f;
			zSpd = (float) Math.cos(temp) * 0.1f;
			ySpd = -ySpd;
			yPos = yPos - 0.8f;
		} else {
			xSpd = aDir.offsetX * (0.1f + 0.2f * GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat());
			zSpd = aDir.offsetZ * (0.1f + 0.2f * GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat());

			xSpd = -xSpd;
			zSpd = -zSpd;


		}

		aWorld.spawnParticle(name, (double) (xPos + ran1 * 0.5f),
				(double) (yPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f),
				(double) (zPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f), (double) xSpd,
				(double) -ySpd, (double) zSpd);
		aWorld.spawnParticle(name, (double) (xPos + ran2 * 0.5f),
				(double) (yPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f),
				(double) (zPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f), (double) xSpd,
				(double) -ySpd, (double) zSpd);
		aWorld.spawnParticle(name, (double) (xPos + ran3 * 0.5f),
				(double) (yPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f),
				(double) (zPos + GT_MetaTileEntity_Hatch_AirIntake.floatGen.nextFloat() * 0.5f), (double) xSpd,
				(double) -ySpd, (double) zSpd);		
	}

	static {
		GT_MetaTileEntity_Hatch_AirIntake.floatGen = new XSTR();
	}

	public int getTankPressure() {
		return 100;
	}

	public int getCapacity() {
		return 128000;
	}

	@Override
	public boolean canTankBeEmptied() {
		return true;
	}

	public boolean addAirToHatch(long aTick) {		
		if (!this.getBaseMetaTileEntity().getAirAtSide(this.getBaseMetaTileEntity().getFrontFacing())) {
			return false;
		}		
		boolean a1 = canTankBeFilled();
		if (aTick % 4 != 0 && a1) {
			return true;
		}
		else if (aTick % 4 != 0 && !a1) {
			return false;
		}
		else {
			if (this.mFluid != null && a1) {
				this.mFluid.amount += 1000;
				return true;
			}
			else if (this.mFluid != null && !a1) {
				return false;
			}
			else {
				if (this.mFluid == null) {
					this.mFluid = FluidUtils.getFluidStack("air", 1000);
					return true;
				}
				else {
					//Not sure how any other fluid got in here
					return false;
				}
			}
		}		
	}

	@Override
	public boolean canTankBeFilled() {
		if (this.mFluid == null || (this.mFluid != null && ((this.mFluid.amount+1000) <= this.getCapacity()))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean doesEmptyContainers() {
		return false;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
	}
}