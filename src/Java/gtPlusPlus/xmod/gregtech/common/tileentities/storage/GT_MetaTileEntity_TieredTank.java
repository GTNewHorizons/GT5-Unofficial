package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.fluid.FluidUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_TieredTank extends GT_MetaTileEntity_BasicTank {

	private NBTTagCompound mRecipeStuff = new NBTTagCompound();
	private String thisName = "";
	private int thisAmount = 0;

	public GT_MetaTileEntity_TieredTank(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + ((int) (Math.pow(2, aTier) * 32000)) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		NBTTagCompound dRecipeStuff = this.mRecipeStuff;
		if (dRecipeStuff != null){
			this.thisName = dRecipeStuff.getString("xFluidName");
			this.thisAmount = dRecipeStuff.getInteger("xFluidAmount");        
			if (this.thisName.equals("")){
				this.thisName = "Empty";
			}            
			if (this.thisName == "Empty" && this.thisAmount == 0){
				//Do Nothing
			}
			else {
				return new String[] {mDescription, "Stored Fluid: "+this.thisName, "Stored Amount: "+this.thisAmount+"l", CORE.GT_Tooltip};            	
			}            
		}
		return new String[] {mDescription, CORE.GT_Tooltip};
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER_ACTIVE)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER)};
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		//Utils.LOG_INFO("Dumping Fluid data. Name: "+mFluid.getFluid().getName()+" Amount: "+mFluid.amount+"L");
		if (mFluid != null){
			aNBT.setTag("mFluid", mFluid.writeToNBT(new NBTTagCompound()));
			mRecipeStuff.setString("xFluidName", mFluid.getFluid().getName());
			mRecipeStuff.setInteger("xFluidAmount", mFluid.amount);
			aNBT.setTag("GT.CraftingComponents", mRecipeStuff);
			this.thisName = mRecipeStuff.getString("xFluidName");
			this.thisAmount = mRecipeStuff.getInteger("xFluidAmount");
		}



	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);  
		mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");  
		this.thisName = mRecipeStuff.getString("xFluidName");
		this.thisAmount = mRecipeStuff.getInteger("xFluidAmount");
		//mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));   
		mFluid = FluidUtils.getFluidStack(mRecipeStuff.getString("xFluidName"), mRecipeStuff.getInteger("xFluidAmount"));
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public final byte getUpdateData() {
		return 0x00;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
	}

	@Override
	public boolean doesEmptyContainers() {
		return true;
	}

	@Override
	public boolean canTankBeFilled() {
		return true;
	}

	@Override
	public boolean canTankBeEmptied() {
		return true;
	}

	@Override
	public boolean displaysItemStack() {
		return true;
	}

	@Override
	public boolean displaysStackSize() {
		return false;
	}

	@Override
	public String[] getInfoData() {

		if (mFluid == null) {
			return new String[]{
					GT_Values.VOLTAGE_NAMES[mTier]+" Fluid Tank",
					"Stored Fluid:",
					"No Fluid",
					Integer.toString(0) + "L",
					Integer.toString(getCapacity()) + "L"};
		}
		return new String[]{
				GT_Values.VOLTAGE_NAMES[mTier]+" Fluid Tank",
				"Stored Fluid:",
				mFluid.getLocalizedName(),
				Integer.toString(mFluid.amount) + "L",
				Integer.toString(getCapacity()) + "L"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredTank(mName, mTier, mDescription, mTextures);
	}

	@Override
	public int getCapacity() {
		this.thisName = mRecipeStuff.getString("xFluidName");
		this.thisAmount = mRecipeStuff.getInteger("xFluidAmount");
		return (int) (Math.pow(2, mTier) * 32000);
	}

	@Override
	public int getTankPressure() {
		return 100;
	}

}