package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.gui.basic.CONTAINER_PollutionCleaner;
import gtPlusPlus.xmod.gregtech.api.gui.basic.GUI_PollutionCleaner;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaAtmosphericReconditioner extends GT_MetaTileEntity_BasicMachine {

	protected int mPollutionReduction = 0;
	protected int mBaseEff = 2500;
	protected int mOptimalAirFlow = 0;
	protected boolean mHasPollution = false;
	protected int SLOT_ROTOR = 4;
	protected int SLOT_FILTER = 5;

	private int mDamageFactorLow = 5;
	private float mDamageFactorHigh =  (float) 0.6000000238418579;

	public GregtechMetaAtmosphericReconditioner(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 2, "Making sure you don't live in Gwalior - Uses 2A", 2, 0, "Recycler.png", "", 
				new ITexture[]{
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent_Fast),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB)
		});
	}

	public GregtechMetaAtmosphericReconditioner(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 2, aDescription, aTextures, 2, 0, aGUIName, aNEIName);
	}

	/*public GregtechMetaAtmosphericReconditioner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 2, aDescription, aTextures, 2, 0, aGUIName, aNEIName);
	}*/

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaAtmosphericReconditioner(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Requires a Turbine Rotor and an Air Filter to run.", CORE.GT_Tooltip};
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("mOptimalAirFlow", this.mOptimalAirFlow);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mOptimalAirFlow = aNBT.getInteger("mOptimalAirFlow");
	}

	@Override
	public long maxAmperesIn() {
		return 4;
	}

	@Override
	public long getMinimumStoredEU() {
		return V[mTier] * 2;
	}

	@Override
	public long maxEUStore() {
		return V[mTier] * 256;
	}

	@Override
	public long maxEUInput() {
		return V[mTier];
	}

	@Override
	public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
		if (getBaseMetaTileEntity().isServerSide()) {

		}
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aBaseMetaTileEntity.isServerSide()) {

			//Get Current Pollution Amount.
			int mCurrentPollution = getCurrentChunkPollution();

			//Get Inventory Item
			ItemStack stackRotor = this.mInventory[SLOT_ROTOR];
			ItemStack stackFilter = this.mInventory[SLOT_FILTER];

			//Enable machine animation/graphic
			if (this.mHasPollution && mCurrentPollution > 0 && hasRotor(stackRotor) && hasAirFilter(stackFilter)){
				aBaseMetaTileEntity.setActive(true);
			}
			else if (!this.mHasPollution || mCurrentPollution <= 0 || stackRotor == null || stackFilter == null || hasRotor(stackRotor) || !hasAirFilter(stackFilter)){
				aBaseMetaTileEntity.setActive(false);
			}			

			//Power Drain
			long drainEU = V[mTier];
			if (aBaseMetaTileEntity.isActive() && aBaseMetaTileEntity.getStoredEU() >= drainEU){
				if(aBaseMetaTileEntity.decreaseStoredEnergyUnits(drainEU, false)){
					//Utils.LOG_WARNING("Draining "+drainEU+" EU");
				}
			}
			else if (!aBaseMetaTileEntity.isActive() && aBaseMetaTileEntity.getStoredEU() >= drainEU/4){
				if(aBaseMetaTileEntity.decreaseStoredEnergyUnits((drainEU/4), false)){
					//Utils.LOG_WARNING("Draining "+(drainEU/4)+" EU");
				}
			}
			else {
				aBaseMetaTileEntity.setActive(false);
			}


			//Only try once/sec.
			if (aTick % 20L == 0L){

				//Check if machine can work.
				if ((aBaseMetaTileEntity.isAllowedToWork())){

					//If Active.
					if (aBaseMetaTileEntity.isActive()){							

						//Do nothing if there is no pollution.
						if(this.mHasPollution && mCurrentPollution > 0){	

							//Use a Turbine
							if(hasRotor(stackRotor) && hasAirFilter(stackFilter)){
								Logger.WARNING("Found Turbine.");
								mBaseEff = (int) ((50.0F + (10.0F * ((GT_MetaGenerated_Tool) stackRotor.getItem()).getToolCombatDamage(stackRotor))) * 100);
								mOptimalAirFlow = (int) Math.max(Float.MIN_NORMAL, ((GT_MetaGenerated_Tool) stackRotor.getItem()).getToolStats(stackRotor).getSpeedMultiplier()
										* GT_MetaGenerated_Tool.getPrimaryMaterial(stackRotor).mToolSpeed * 50);

								//Make sure we have a valid Turbine and Eff/Airflow
								if (this.mBaseEff > 0 && this.mOptimalAirFlow > 0){
									//Utils.LOG_WARNING("Pollution Cleaner [5]");

									//Log Debug information.
									Logger.WARNING("mBaseEff[1]:"+mBaseEff);
									Logger.WARNING("mOptimalAirFlow[1]:"+mOptimalAirFlow);

									//Calculate The Voltage we are running
									long tVoltage = maxEUInput();
									byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

									//Check Sides for Air,
									//More air means more pollution processing.
									int mAirSides = getFreeSpaces();

									//If no sides are free, how will you process the atmosphere?
									if (mAirSides > 0){
										mPollutionReduction += (((mTier*2)*100)*mAirSides); //Was originally *100
										Logger.WARNING("mPollutionReduction[1]:"+mPollutionReduction);

										//I stole this code
										mPollutionReduction = (MathUtils.safeInt((long)mPollutionReduction*this.mBaseEff)/100000)*mAirSides;
										//Utils.LOG_WARNING("mPollutionReduction[2]:"+mPollutionReduction);
										//mPollutionReduction = GT_Utility.safeInt((long)mPollutionReduction*this.mOptimalAirFlow/10000);
										//Utils.LOG_WARNING("mPollutionReduction[3]:"+mPollutionReduction);

										//Set a temp to remove variable to aleviate duplicate code.
										int toRemove = 0;

										Logger.WARNING("mCurrentPollution[4]:"+mCurrentPollution);
										if (mPollutionReduction <= mCurrentPollution){
											//Clean some Air.
											toRemove = mPollutionReduction;
										}
										else {
											//Makes sure we don't get negative pollution.
											toRemove = mCurrentPollution;						
										}	

										//We are good to clean
										if (toRemove > 0){
											if (damageTurbineRotor() && damageAirFilter()){
												removePollution(toRemove);	
												Logger.WARNING("mNewPollution[4]:"+getCurrentChunkPollution());		
											}
											else {
												Logger.WARNING("Could not damage turbine rotor or Air Filter.");
												aBaseMetaTileEntity.setActive(false);
											}
										} //End of pollution removal block.								
									} //End of valid air sides block.							
								} //End of valid toolstats block.											
							} //End of correct inventory item block.
							else {
								//Utils.LOG_WARNING("Wrong Tool metaitem Found.");
							}
						}
					}					
				} //End of can work block.
				else { //Disable Machine.
					//aBaseMetaTileEntity.setActive(false);
				}
			} //End of 1/sec action block.
		} //End of is serverside block.
	}


	public int getCurrentChunkPollution(){
		return getCurrentChunkPollution(this.getBaseMetaTileEntity());
	}

	public int getCurrentChunkPollution(IGregTechTileEntity aBaseMetaTileEntity){
		int mCurrentChunkPollution = PollutionUtils.getPollution(aBaseMetaTileEntity);
		if (mCurrentChunkPollution > 0){
			mHasPollution = true;
		}
		else {
			mHasPollution = false;
		}
		return mCurrentChunkPollution;
	}

	public boolean hasRotor(ItemStack rotorStack){
		if(rotorStack != null){ 
			if (rotorStack.getItem() instanceof GT_MetaGenerated_Tool  && rotorStack.getItemDamage() >= 170 && rotorStack.getItemDamage() <= 179){
				return true;
			}
		}	
		return false;
	}

	public boolean damageTurbineRotor(){
		try{
			if(mInventory[SLOT_ROTOR].getItem() instanceof GT_MetaGenerated_Tool_01 &&
					((GT_MetaGenerated_Tool) mInventory[SLOT_ROTOR].getItem()).getToolStats(mInventory[SLOT_ROTOR]).getSpeedMultiplier()>0 &&
					GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[SLOT_ROTOR]).mToolSpeed>0 ) {
				long damageValue = ((10L*(long) Math.min(-mTier / mDamageFactorLow, Math.pow(-mTier, this.mDamageFactorHigh)))/10);

				if (damageValue <= 1){
					if (this.mOptimalAirFlow > 0){
						damageValue = (this.mOptimalAirFlow/10/2);
					}
					else {
						return false;
					}
				}

				Logger.WARNING("Trying to do "+damageValue+" damage to the rotor.");

				//Damage Rotor
				//int rotorDurability = this.mInventory[this.SLOT_ROTOR].getItemDamage();
				long rotorDamage = GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
				long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(this.mInventory[this.SLOT_ROTOR]);
				long rotorDurability = (rotorDurabilityMax - rotorDamage);
				Logger.WARNING("Rotor Damage: "+rotorDamage + " | Max Durability: "+rotorDurabilityMax+" | "+" Remaining Durability: "+rotorDurability);
				if (rotorDurability > damageValue){
					Logger.WARNING("Damaging Rotor.");
					GT_ModHandler.damageOrDechargeItem(this.mInventory[this.SLOT_ROTOR], (int) damageValue, 0, null);

					long tempDur = GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
					if (tempDur < rotorDurabilityMax){
						return true;
					}
					else {
						rotorDurability = 0;
					}
				}

				if (rotorDurability <= 0) {
					Logger.WARNING("Destroying Rotor.");
					this.mInventory[this.SLOT_ROTOR] = null;
					return false;
				}


			}else {
				Logger.WARNING("Bad Rotor.");
				return false;
			}
		}
		catch (Throwable t){}
		return false;
	}

	public int getFreeSpaces(){
		int mAir = 0;
		IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
		int x = aBaseMetaTileEntity.getXCoord();
		int y = aBaseMetaTileEntity.getYCoord();
		int z = aBaseMetaTileEntity.getZCoord();
		if(aBaseMetaTileEntity.getAirOffset(x+1, y, z)){
			mAir++;
		}
		if(aBaseMetaTileEntity.getAirOffset(x-1, y, z)){
			mAir++;
		}
		if(aBaseMetaTileEntity.getAirOffset(x, y, z+1)){
			mAir++;
		}
		if(aBaseMetaTileEntity.getAirOffset(x, y, z-1)){
			mAir++;
		}
		if(aBaseMetaTileEntity.getAirOffset(x, y+1, z)){
			mAir++;
		}
		if(aBaseMetaTileEntity.getAirOffset(x, y-1, z)){
			mAir++;
		}
		return mAir;						
	}

	public boolean removePollution(int toRemove){
		int before = getCurrentChunkPollution();
		PollutionUtils.addPollution(this.getBaseMetaTileEntity(), -toRemove);
		int after = getCurrentChunkPollution();
		return (after<before);	
	}


	public boolean hasAirFilter(ItemStack filter){
		if (filter.getItem() instanceof ItemAirFilter){
			return true;
		}		
		return false;
	}

	public boolean damageAirFilter(){
		ItemStack filter = this.mInventory[this.SLOT_FILTER];

		if (filter.getItem() instanceof ItemAirFilter){

			long currentUse = ItemAirFilter.getFilterDamage(filter);

			//Remove broken Filter
			if (filter.getItemDamage() == 0 && currentUse >= 50-1){			
				this.mInventory[this.SLOT_FILTER] = null;
				return false;				
			}
			else if (filter.getItemDamage() == 1 && currentUse >= 150-1){
				this.mInventory[this.SLOT_FILTER] = null;
				return false;			
			}		
			else {
				//Do Damage
				ItemAirFilter.setFilterDamage(filter, currentUse+1);
				Logger.WARNING("Filter Damage: "+currentUse);
				return true;
			}			
		}		
		return false;
	}


	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_PollutionCleaner(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_PollutionCleaner(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),	this.mGUIName);
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		if (aIndex == 5){
			return false;
		}
		return super.canInsertItem(aIndex, aStack, aSide);
	}

}