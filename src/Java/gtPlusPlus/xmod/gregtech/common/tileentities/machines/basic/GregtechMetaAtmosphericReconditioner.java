package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GregtechMetaAtmosphericReconditioner extends GT_MetaTileEntity_BasicMachine {

	protected int mPollutionReduction = 0;
	protected int mBaseEff = 2500;
	protected int mOptimalAirFlow = 0;
	protected boolean mHasPollution = false;
	protected int SLOT_ROTOR = 4;

	private int mDamageFactorLow = 5;
	private float mDamageFactorHigh =  (float) 0.6000000238418579;

	public GregtechMetaAtmosphericReconditioner(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, "Making sure you don't live in Gwalior", 1, 1, "Recycler.png", "", 
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
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	public GregtechMetaAtmosphericReconditioner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaAtmosphericReconditioner(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
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
			ItemStack aStack = this.mInventory[SLOT_ROTOR];
			
			//Enable machine animation/graphic
			if (this.mHasPollution && mCurrentPollution > 0 && slotContainsRotor(aStack)){
				aBaseMetaTileEntity.setActive(true);
				this.mEUt = mTier;
			}
			else if (!this.mHasPollution || mCurrentPollution <= 0 || aStack == null){
				aBaseMetaTileEntity.setActive(false);
				this.mEUt = 0;
			}


			if (aTick % 2L == 0L){
				aBaseMetaTileEntity.decreaseStoredEnergyUnits((long) Math.pow(mTier/4, mTier/2), false);
			}

			//Only try once/sec.
			if (aTick % 20L == 0L){
				//Utils.LOG_INFO("Pollution Cleaner [1]");

				//Check if machine can work.
				if ((aBaseMetaTileEntity.isAllowedToWork())/* && (getBaseMetaTileEntity().getRedstone())*/){
					//Utils.LOG_INFO("Pollution Cleaner [2] | "+aBaseMetaTileEntity.getStoredEU()+" | "+getMinimumStoredEU());

					//Check if enough power to work.
					if (aBaseMetaTileEntity.getStoredEU() >= getMinimumStoredEU()) {
						//Utils.LOG_INFO("Pollution Cleaner [3] | Power Stored:"+aBaseMetaTileEntity.getStoredEU());
						
						//Drain some power.
						if (aBaseMetaTileEntity.decreaseStoredEnergyUnits((long) Math.pow(2, mTier), false)){							
							
							//Do nothing if there is no pollution or machine is not active.
							if(this.mHasPollution && mCurrentPollution > 0){	

								//Use a Turbine
								if(slotContainsRotor(aStack)){
										Utils.LOG_INFO("Found Turbine.");
										mBaseEff = (int) ((50.0F + (10.0F * ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack))) * 100);
										mOptimalAirFlow = (int) Math.max(Float.MIN_NORMAL, ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
												* GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolSpeed * 50);

										//Make sure we have a valid Turbine and Eff/Airflow
										if (this.mBaseEff > 0 && this.mOptimalAirFlow > 0){
											//Utils.LOG_INFO("Pollution Cleaner [5]");

											//Log Debug information.
											Utils.LOG_INFO("mBaseEff[1]:"+mBaseEff);
											Utils.LOG_INFO("mOptimalAirFlow[1]:"+mOptimalAirFlow);

											//Calculate The Voltage we are running
											long tVoltage = maxEUInput();
											byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

											//Check Sides for Air,
											//More air means more pollution processing.
											int mAirSides = getFreeSpaces();

											//If no sides are free, how will you process the atmosphere?
											if (mAirSides > 0){
												mPollutionReduction += (((mTier*2)*100)*mAirSides); //Was originally *100
												Utils.LOG_INFO("mPollutionReduction[1]:"+mPollutionReduction);

												//I stole this code
												mPollutionReduction = (GT_Utility.safeInt((long)mPollutionReduction*this.mBaseEff)/100000)*mAirSides;
												//Utils.LOG_INFO("mPollutionReduction[2]:"+mPollutionReduction);
												//mPollutionReduction = GT_Utility.safeInt((long)mPollutionReduction*this.mOptimalAirFlow/10000);
												//Utils.LOG_INFO("mPollutionReduction[3]:"+mPollutionReduction);

												//Set a temp to remove variable to aleviate duplicate code.
												int toRemove = 0;

												Utils.LOG_INFO("mCurrentPollution[4]:"+mCurrentPollution);
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
													if (doDamageToTurbine()){
														removePollution(toRemove);	
														Utils.LOG_INFO("mNewPollution[4]:"+getCurrentChunkPollution());		
													}
													else {
														Utils.LOG_INFO("Could not damage turbine rotor.");
													}
												} //End of pollution removal block.								
											} //End of valid air sides block.							
										} //End of valid toolstats block.											
									} //End of correct inventory item block.
									else {
										//Utils.LOG_INFO("Wrong Tool metaitem Found.");
									}
								}
								else {
									//Utils.LOG_INFO("No Turbine Rotor Found. Size:"+this.mInventory.length);
									if (this.mInventory.length > 0){
										for (int i=0;i<this.mInventory.length;i++){
											if (this.mInventory[i] != null){
												//Utils.LOG_INFO("Pos:"+i+" | "+"item:"+this.mInventory[i].getDisplayName());
											}
										}
									}
									else {
										Utils.LOG_INFO("Bad Inventory.");
									}								

							} //End of Has Pollution block.
						} //End of power check block.
						else {
							//aBaseMetaTileEntity.setActive(false);
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
		int mCurrentChunkPollution = GT_Pollution.getPollution(aBaseMetaTileEntity);
		if (mCurrentChunkPollution > 0){
			mHasPollution = true;
		}
		else {
			mHasPollution = false;
		}
		return mCurrentChunkPollution;
	}
	
	public boolean slotContainsRotor(ItemStack rotorStack){
		if(rotorStack != null){ 
			if (rotorStack.getItem() instanceof GT_MetaGenerated_Tool  && rotorStack.getItemDamage() >= 170 && rotorStack.getItemDamage() <= 179){
				return true;
			}
		}	
		return false;
	}

	public boolean doDamageToTurbine(){
		try{
			if(mInventory[SLOT_ROTOR].getItem() instanceof GT_MetaGenerated_Tool_01 &&
					((GT_MetaGenerated_Tool) mInventory[SLOT_ROTOR].getItem()).getToolStats(mInventory[SLOT_ROTOR]).getSpeedMultiplier()>0 &&
					GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[SLOT_ROTOR]).mToolSpeed>0 ) {
				long damageValue = (10L*(long) Math.min(-mTier / mDamageFactorLow, Math.pow(-mTier, this.mDamageFactorHigh)));

				if (damageValue <= 1){
					if (this.mOptimalAirFlow > 0){
						damageValue = (this.mOptimalAirFlow/10);
					}
					else {
						return false;
					}
				}

				Utils.LOG_INFO("Trying to do "+damageValue+" damage to the rotor.");

				//Damage Rotor
				//int rotorDurability = this.mInventory[this.SLOT_ROTOR].getItemDamage();
				long rotorDamage = GT_MetaGenerated_Tool.getToolDamage(this.mInventory[this.SLOT_ROTOR]);
				long rotorDurabilityMax = GT_MetaGenerated_Tool.getToolMaxDamage(this.mInventory[this.SLOT_ROTOR]);
				long rotorDurability = (rotorDurabilityMax - rotorDamage);
				Utils.LOG_INFO("Rotor Damage: "+rotorDamage + " | Max Durability: "+rotorDurabilityMax+" | "+" Remaining Durability: "+rotorDurability);
				if (rotorDurability > damageValue){
					Utils.LOG_INFO("Damaging Rotor.");
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
					Utils.LOG_INFO("Destroying Rotor.");
					this.mInventory[this.SLOT_ROTOR] = null;
					return false;
				}


			}else {
				Utils.LOG_INFO("Bad Rotor.");
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
		GT_Pollution.addPollution(this.getBaseMetaTileEntity(), -toRemove);
		int after = getCurrentChunkPollution();
		return (after<before);	
	}

}