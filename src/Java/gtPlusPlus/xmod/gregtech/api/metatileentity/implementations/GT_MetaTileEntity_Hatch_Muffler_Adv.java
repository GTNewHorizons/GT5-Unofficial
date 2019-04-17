package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_Hatch_Muffler_Adv extends GT_MetaTileEntity_Hatch_Muffler {

	@Override
	public void onConfigLoad(GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {			
			try {
				Integer a1 = (int) StaticFields59.getFieldFromGregtechProxy(false, "mPollutionSmogLimit");
				if (a1 != null && a1 > 0) {
					mPollutionSmogLimit = a1;
				}
			}
			catch (Throwable t) {
				mPollutionSmogLimit = 500000;				
			}
		}
	}

	private int mPollutionSmogLimit = 500000;
	
	public GT_MetaTileEntity_Hatch_Muffler_Adv(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_Hatch_Muffler_Adv(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public GT_MetaTileEntity_Hatch_Muffler_Adv(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription[0], aTextures);
	}

	public String[] getDescription() {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
		
		String[] mDescArray = StaticFields59.getDescriptionArray(this);
			
		String[] desc = new String[mDescArray.length + 4];
		System.arraycopy(mDescArray, 0, desc, 0, mDescArray.length);
		desc[mDescArray.length] = "DO NOT OBSTRUCT THE OUTPUT!";
		desc[mDescArray.length + 1] = "Requires extra Air on the exhaust face";
		desc[mDescArray.length + 2] = "Reduces Pollution to " + this.calculatePollutionReduction(100) + "%";
		desc[mDescArray.length + 3] = "Recovers " + (105 - this.calculatePollutionReduction(100))
				+ "% of CO2/CO/SO2";
		return desc;
		}
		else {
			return new String[] {};
		}
	}

	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv)};
	}

	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv)};
	}

	public boolean isValidSlot(int aIndex) {
		return false;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Muffler_Adv(this.mName, this.mTier, StaticFields59.getDescriptionArray(this), this.mTextures);
	}
	
	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity,
			EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
			return true;
		//aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	public boolean polluteEnvironment() {		
		if (this.getBaseMetaTileEntity().getAirAtSide(this.getBaseMetaTileEntity().getFrontFacing()) && this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getFrontFacing(), 1)) {
			int aEmission = this.calculatePollutionReduction(10000);
			PollutionUtils.addPollution(this.getBaseMetaTileEntity(), aEmission);			
			//Logger.INFO("Outputting "+aEmission+"gbl");
			return true;
		} else {	
			//Logger.INFO("Failed to output pollution");
			return false;
		}
	}

	public int calculatePollutionReduction(int aPollution) {
		return (int) ((double) aPollution * Math.pow(0.64D, (double) (this.mTier - 1)));
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}
	
	public boolean hasValidFilter() {
		return false;
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		String aParticleName;
		if (hasValidFilter()) {
			aParticleName = "cloud";
		} else {
			aParticleName = "smoke";
		}
		if (aBaseMetaTileEntity.isClientSide() && this.getBaseMetaTileEntity().isActive()) {
			this.pollutionParticles(this.getBaseMetaTileEntity().getWorld(), aParticleName);
		}

	}

	public void pollutionParticles(World aWorld, String name) {
		float ran1 = CORE.RANDOM.nextFloat();
		float ran2 = 0.0F;
		float ran3 = 0.0F;
		boolean chk1 = ran1 * 100.0F < (float) this.calculatePollutionReduction(100);
		boolean chk2;
		boolean chk3;
		int aPollutionAmount = PollutionUtils.getPollution(getBaseMetaTileEntity());
		if (aPollutionAmount >= mPollutionSmogLimit) {
			ran2 = CORE.RANDOM.nextFloat();
			ran3 = CORE.RANDOM.nextFloat();
			chk2 = ran2 * 100.0F < (float) this.calculatePollutionReduction(100);
			chk3 = ran3 * 100.0F < (float) this.calculatePollutionReduction(100);
			if (!chk1 && !chk2 && !chk3) {
				return;
			}
		} else {
			if (!chk1) {
				return;
			}

			chk3 = false;
			chk2 = false;
		}

		IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
		ForgeDirection aDir = ForgeDirection.getOrientation(aMuffler.getFrontFacing());
		float xPos = (float) aDir.offsetX * 0.76F + (float) aMuffler.getXCoord() + 0.25F;
		float yPos = (float) aDir.offsetY * 0.76F + (float) aMuffler.getYCoord() + 0.25F;
		float zPos = (float) aDir.offsetZ * 0.76F + (float) aMuffler.getZCoord() + 0.25F;
		float ySpd = (float) aDir.offsetY * 0.1F + 0.2F + 0.1F * CORE.RANDOM.nextFloat();
		float xSpd;
		float zSpd;
		if (aDir.offsetY == -1) {
			float temp = CORE.RANDOM.nextFloat() * 2.0F * CORE.PI;
			xSpd = (float) Math.sin((double) temp) * 0.1F;
			zSpd = (float) Math.cos((double) temp) * 0.1F;
		} else {
			xSpd = (float) aDir.offsetX * (0.1F + 0.2F * CORE.RANDOM.nextFloat());
			zSpd = (float) aDir.offsetZ * (0.1F + 0.2F * CORE.RANDOM.nextFloat());
		}

		if (chk1) {
			aWorld.spawnParticle(name, (double) (xPos + ran1 * 0.5F), (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
					(double) (zPos + CORE.RANDOM.nextFloat() * 0.5F), (double) xSpd, (double) ySpd, (double) zSpd);
		}

		if (chk2) {
			aWorld.spawnParticle(name, (double) (xPos + ran2 * 0.5F), (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
					(double) (zPos + CORE.RANDOM.nextFloat() * 0.5F), (double) xSpd, (double) ySpd, (double) zSpd);
		}

		if (chk3) {
			aWorld.spawnParticle(name, (double) (xPos + ran3 * 0.5F), (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
					(double) (zPos + CORE.RANDOM.nextFloat() * 0.5F), (double) xSpd, (double) ySpd, (double) zSpd);
		}

	}
	
	
	
}