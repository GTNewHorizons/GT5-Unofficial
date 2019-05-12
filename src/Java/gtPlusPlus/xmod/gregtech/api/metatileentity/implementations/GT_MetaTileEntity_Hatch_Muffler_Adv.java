package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_Hatch_Muffler_Advanced;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_Hatch_Muffler_Advanced;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_MetaTileEntity_Hatch_Muffler_Adv extends GT_MetaTileEntity_Hatch_Muffler {

	protected int SLOT_FILTER = 0;
	
	@Override
	public void onConfigLoad(GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {			
			try {
				Integer a1 = (int) StaticFields59.getFieldFromGregtechProxy("mPollutionSmogLimit");
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
		ReflectionUtils.setField(this, "mInventory", new ItemStack[1]);
	}

	public GT_MetaTileEntity_Hatch_Muffler_Adv(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		ReflectionUtils.setField(this, "mInventory", new ItemStack[1]);
	}

	public GT_MetaTileEntity_Hatch_Muffler_Adv(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription[0], aTextures);
		ReflectionUtils.setField(this, "mInventory", new ItemStack[1]);
	}

	public String[] getDescription() {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {		
		String[] mDescArray = StaticFields59.getDescriptionArray(this);			
		String[] desc = new String[mDescArray.length + 6];
		System.arraycopy(mDescArray, 0, desc, 0, mDescArray.length);
		desc[mDescArray.length] = "DO NOT OBSTRUCT THE OUTPUT!";
		desc[mDescArray.length + 1] = "Requires 3 Air on the exhaust face";
		desc[mDescArray.length + 2] = "Requires Air Filters";
		desc[mDescArray.length + 3] = "Mufflers require T2 Filters from IV-"+GT_Values.VN[9];
		desc[mDescArray.length + 4] = "Reduces Pollution to " + this.calculatePollutionReductionForTooltip(100) + "%";
		desc[mDescArray.length + 5] = "Recovers " + (105 - this.calculatePollutionReductionForTooltip(100))
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
		return aIndex == SLOT_FILTER;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Hatch_Muffler_Adv(this.mName, this.mTier, StaticFields59.getDescriptionArray(this), this.mTextures);
	}
	
	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity,
			EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
			return true;
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}



	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_Hatch_Muffler_Advanced(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_Hatch_Muffler_Advanced(aPlayerInventory, aBaseMetaTileEntity, "Advanced Muffler", "machine_Charger.png");
	}
	
	private boolean airCheck() {
		if (
		this.getBaseMetaTileEntity().getAirAtSide(this.getBaseMetaTileEntity().getFrontFacing()) &&
		this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getFrontFacing(), 1) &&
		this.getBaseMetaTileEntity().getAirAtSideAndDistance(this.getBaseMetaTileEntity().getFrontFacing(), 2)
		) {
			return true;
		}		
		return false;
	}

	public boolean polluteEnvironment() {		
		if (airCheck() && damageAirFilter()) {
			int aEmission = this.calculatePollutionReduction(10000);
			PollutionUtils.addPollution(this.getBaseMetaTileEntity(), aEmission);			
			//Logger.INFO("Outputting "+aEmission+"gbl");
			return true;
		} else {	
			//Logger.INFO("Failed to output pollution");
			return false;
		}
	}


	public int calculatePollutionReductionForTooltip(int aPollution) {
		return (int) (aPollution * Math.pow(0.64D, (double) (this.mTier - 1)));	
	}
	
	public int calculatePollutionReduction(int aPollution) {
		double aVal1 = aPollution * Math.pow(0.64D, (double) (this.mTier - 1));
		int aVal2 = (int) aVal1;	
		if (!hasValidFilter()) {
			aVal2 = 0;
		}		
		return aVal2;
	}

	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		if (aIndex == this.SLOT_FILTER) {
			if (isAirFilter(aStack)) {
				return true;
			}
		}		
		return false;
	}
	
	private ItemStack getInventoryStack() {
		if (this.mInventory != null && this.mInventory.length > 0) {
			if (this.mInventory.length-1 >= this.SLOT_FILTER) {
				return this.mInventory[this.SLOT_FILTER];
			}
		}		
		return null;
	}
	
	private void breakAirFilter() {
		if (this.mInventory != null && this.mInventory.length > 0) {
			if (this.mInventory.length-1 >= this.SLOT_FILTER) {
				Logger.INFO("Breaking Filter");
				this.mInventory[this.SLOT_FILTER] = null;
			}
		}
	}
	
	public boolean hasValidFilter() {
		return isAirFilter(getInventoryStack());
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {		

		//Logger.INFO("A1");	
		
		super.onPostTick(aBaseMetaTileEntity, aTick);
		
		//Logger.INFO("A2");	
		
		String aParticleName;		
		if ((aTick % 2) == 0){
			aParticleName = "cloud";			
		}
		else {
			aParticleName = "smoke";			
		}	

		//Logger.INFO("A3");	
		
		if (aBaseMetaTileEntity.isClientSide()) {
			//Logger.INFO("B1");	
			if (this.getBaseMetaTileEntity().isActive()) {
				//Logger.INFO("C1");	
				this.pollutionParticles(this.getBaseMetaTileEntity().getWorld(), aParticleName);
			}
			//return;
		}
		else {
			//Logger.INFO("B2");	
			if (this.getInventoryStack() == null) {
				//Logger.INFO("D1");	
				//Logger.INFO("Empty - "+this.mInventory.length);
			}
			else {
				//Logger.INFO("D2");	
				Logger.INFO("Has Item");			
			}	
		}
		//Logger.INFO("A4");	
		
		
		
	}
	
	public boolean isAirFilter(ItemStack filter){
		if (filter == null) {
			return false;
		}		
		if (filter.getItem() instanceof ItemAirFilter){
			
			if (this.mTier < 5) {
				return true;
			}
			else {
				if (filter.getItemDamage() == 1) {
					return true;
				}
			}
		}		
		return false;
	}

	public boolean damageAirFilter(){		
		ItemStack filter = getInventoryStack();
		if (filter == null) {
			return false;
		}		

		if (isAirFilter(filter)){
			long currentUse = ItemAirFilter.getFilterDamage(filter);
			Logger.INFO("Filter Damage: "+currentUse);
			//Remove broken Filter
			if ((filter.getItemDamage() == 0 && currentUse >= 50-1) || (filter.getItemDamage() == 1 && currentUse >= 2500-1)){	
				breakAirFilter();
				return false;				
			}	
			else {
				//Do Damage
				ItemAirFilter.setFilterDamage(filter, currentUse+1);
				Logger.INFO("Filter Damage now: "+currentUse);
				return true;
			}			
		}		
		return false;
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