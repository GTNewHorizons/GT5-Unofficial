package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GregtechMetaAtmosphericReconditioner extends GT_MetaTileEntity_BasicMachine {

	protected int mPollutionReduction = 0;
	protected int mBaseEff = 2500;
	protected int mOptimalAirFlow = 0;
	protected boolean mHasPollution = false;

	private int mDamageFactorLow = 5;
	private float mDamageFactorHigh =  (float) 0.6000000238418579;

	public GregtechMetaAtmosphericReconditioner(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 1, "Making sure you don't live in Gwalior", 1, 1, "Massfabricator.png", "", 
				new ITexture[]{
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_MASSFAB),
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
			//Only try once/sec.
			if (aTick % 20L == 0L){
				Utils.LOG_INFO("Pollution Cleaner [1]");

				//Check if machine can work.
				if ((aBaseMetaTileEntity.isAllowedToWork())/* && (getBaseMetaTileEntity().getRedstone())*/){
					Utils.LOG_INFO("Pollution Cleaner [2] | "+aBaseMetaTileEntity.getStoredEU()+" | "+getMinimumStoredEU());

					//Check if enough power to work.
					if (aBaseMetaTileEntity.getStoredEU() >= getMinimumStoredEU()) {
						Utils.LOG_INFO("Pollution Cleaner [3]");

						//Drain some power.
						aBaseMetaTileEntity.decreaseStoredEnergyUnits((long) Math.pow(2, mTier), false);
						
						//Get Inventory Item
						ItemStack aStack = this.mInventory[0];

						//Get Current Pollution Amount.
						int mCurrentPollution = getCurrentChunkPollution();

						//Do nothing if there is no pollution or machine is not active.
						if(this.mHasPollution && mCurrentPollution > 0){	
							Utils.LOG_INFO("Pollution Cleaner [4]");		

							//Use a Turbine
							if(aStack != null && (aStack.getItem() instanceof GT_MetaGenerated_Tool)  && aStack.getItemDamage() >= 170 && aStack.getItemDamage() <= 179){
								Utils.LOG_INFO("Found Turbine.");
								mBaseEff = (int) ((50.0F + (10.0F * ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack))) * 100);
								mOptimalAirFlow = (int) Math.max(Float.MIN_NORMAL, ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
										* GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolSpeed * 50);

								//Make sure we have a valid Turbine and Eff/Airflow
								if (this.mBaseEff > 0 && this.mOptimalAirFlow > 0){
									Utils.LOG_INFO("Pollution Cleaner [5]");

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
										mPollutionReduction += (((mTier*2)*50)*mAirSides); //Was originally *100
										Utils.LOG_INFO("mPollutionReduction[1]:"+mPollutionReduction);

										//I stole this code
										mPollutionReduction = GT_Utility.safeInt((long)mPollutionReduction*this.mBaseEff)/10000;
										Utils.LOG_INFO("mPollutionReduction[2]:"+mPollutionReduction);
										mPollutionReduction = GT_Utility.safeInt((long)mPollutionReduction*this.mOptimalAirFlow/10000);
										Utils.LOG_INFO("mPollutionReduction[3]:"+mPollutionReduction);

										//Set a temp to remove variable to aleviate duplicate code.
										int toRemove = 0;

										Utils.LOG_INFO("mCurrentPollution[4]:"+mCurrentPollution);
										if (mPollutionReduction >= mCurrentPollution){
											//Clean some Air.
											toRemove = mPollutionReduction;
										}
										else {
											//Makes sure we don't get negative pollution.
											toRemove = mCurrentPollution;						
										}	

										//We are good to clean
										if (toRemove > 0){
											removePollution(toRemove);	
											aBaseMetaTileEntity.setActive(true);
											Utils.LOG_INFO("mNewPollution[4]:"+getCurrentChunkPollution());		
										} //End of pollution removal block.								
									} //End of valid air sides block.							
								} //End of valid toolstats block.											
							} //End of correct inventory item block.
						} //End of Has Pollution block.
					} //End of power check block.
				} //End of can work block.
				else { //Disable Machine.
					aBaseMetaTileEntity.setActive(false);
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

public boolean doDamageToTurbine(){
	try{
		if(mInventory[0].getItem() instanceof GT_MetaGenerated_Tool_01 &&
				((GT_MetaGenerated_Tool) mInventory[0].getItem()).getToolStats(mInventory[0]).getSpeedMultiplier()>0 &&
				GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[0]).mToolSpeed>0 ) {
			return ((GT_MetaGenerated_Tool) mInventory[0].getItem()).doDamage(mInventory[0], 10L*(long) Math.min(-mEUt / mDamageFactorLow, Math.pow(-mEUt, this.mDamageFactorHigh)));
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