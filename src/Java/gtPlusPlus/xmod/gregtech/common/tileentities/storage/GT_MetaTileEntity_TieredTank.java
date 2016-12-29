package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_TieredTank
extends GT_MetaTileEntity_BasicTank {

	private String mFluidName;
	private int mFluidAmount;
	private NBTTagCompound internalCraftingComponentsTag = new NBTTagCompound();

	public GT_MetaTileEntity_TieredTank(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + ((int) (Math.pow(2, aTier) * 32000)) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
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
	public String[] getDescription() {
		//setVars();
		if ((mFluidName.equals("Empty")||mFluidName.equals("")) || mFluidAmount <= 0){
			return new String[] {mDescription, CORE.GT_Tooltip};
		}
		return new String[] {mDescription, "Stored Fluid: "+mFluidName, "Stored Amount: "+mFluidAmount+"l", CORE.GT_Tooltip};
	}


	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		if (gtCraftingComponentsTag != null){
			
			Utils.LOG_WARNING("Got Crafting Tag");
			
				if (mFluid != null){
					Utils.LOG_WARNING("mFluid was not null, Saving TileEntity NBT data.");
					gtCraftingComponentsTag.setInteger("xAmount", mFluid.amount);
					gtCraftingComponentsTag.setString("xFluid", mFluid.getFluid().getName());

					//Backup the current tag
					//gtCraftingComponentsTag.setTag("backupTag", internalCraftingComponentsTag);
					//internalCraftingComponentsTag = gtCraftingComponentsTag;

					aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
				}
				else {
					Utils.LOG_WARNING("mFluid was null, Saving TileEntity NBT data.");
					gtCraftingComponentsTag.removeTag("xFluid");
					gtCraftingComponentsTag.removeTag("xAmount");

					//Backup the current tag
					//gtCraftingComponentsTag.setTag("backupTag", internalCraftingComponentsTag);
					//internalCraftingComponentsTag = gtCraftingComponentsTag;

					aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
				}
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);  	
		NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		String xFluid = null;
		int xAmount = 0;
		if (gtCraftingComponentsTag.hasNoTags()){
			if (mFluid != null){
				Utils.LOG_WARNING("mFluid was not null, Creating TileEntity NBT data.");
				gtCraftingComponentsTag.setInteger("xAmount", mFluid.amount);
				gtCraftingComponentsTag.setString("xFluid", mFluid.getFluid().getName());
				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
			}
		}
		else {

			//internalCraftingComponentsTag = gtCraftingComponentsTag.getCompoundTag("backupTag");

			if (gtCraftingComponentsTag.hasKey("xFluid")){
				Utils.LOG_WARNING("xFluid was not null, Loading TileEntity NBT data.");
				xFluid = gtCraftingComponentsTag.getString("xFluid");
			}
			if (gtCraftingComponentsTag.hasKey("xAmount")){
				Utils.LOG_WARNING("xAmount was not null, Loading TileEntity NBT data.");
				xAmount = gtCraftingComponentsTag.getInteger("xAmount");
			}
			if (xFluid != null && xAmount != 0){
				Utils.LOG_WARNING("Setting Internal Tank, loading "+xAmount+"L of "+xFluid);
				setInternalTank(xFluid, xAmount);
			}
		}

	}

	private boolean setInternalTank(String fluidName, int amount){
		FluidStack temp = FluidUtils.getFluidStack(fluidName, amount);
		if (temp != null){
			if (mFluid == null){
				mFluid = temp;
				Utils.LOG_WARNING(temp.getFluid().getName()+" Amount: "+temp.amount+"L");
			}
			else{
				Utils.LOG_WARNING("Retained Fluid.");
				Utils.LOG_WARNING(mFluid.getFluid().getName()+" Amxount: "+mFluid.amount+"L");
			}
			markDirty();
			return true;
		}
		return false;


	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		tryForceNBTUpdate();
		return super.drain(maxDrain, doDrain);
	}

	@Override
	public int fill(FluidStack aFluid, boolean doFill) {
		tryForceNBTUpdate();
		return super.fill(aFluid, doFill);
	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		Utils.LOG_WARNING("setItemNBT");
		//aNBT.setTag("GT.CraftingComponents", lRecipeStuff);

	}

	@Override
	public void closeInventory() {
		tryForceNBTUpdate();
		super.closeInventory();
	}

	@Override
	public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		tryForceNBTUpdate();
		return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			//setVars();
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		tryForceNBTUpdate();
		return true;
	}

	@Override
	public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		super.onLeftclick(aBaseMetaTileEntity, aPlayer);
		Utils.LOG_INFO("Left Clicking on Tank.");
		tryForceNBTUpdate();
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
		return (int) (Math.pow(2, mTier) * 32000);
	}

	@Override
	public int getTankPressure() {
		return 100;
	}

	@Override
	public void onMachineBlockUpdate() {
		//tryForceNBTUpdate();
		super.onMachineBlockUpdate();
	}

	@Override
	public void onRemoval() {
		Utils.LOG_INFO("Tank Removel?");
		tryForceNBTUpdate();
		super.onRemoval();
	}

	@Override
	public void onCloseGUI() {
		super.onCloseGUI();
		tryForceNBTUpdate();
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, FluidStack aFluid, boolean doDrain) {
		tryForceNBTUpdate();
		return super.drain(aSide, aFluid, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection aSide, int maxDrain, boolean doDrain) {
		tryForceNBTUpdate();
		return super.drain(aSide, maxDrain, doDrain);
	}

	@Override
	public int fill(ForgeDirection arg0, FluidStack arg1, boolean arg2) {
		tryForceNBTUpdate();
		return super.fill(arg0, arg1, arg2);
	}

	@Override
	public int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
		tryForceNBTUpdate();
		return super.fill_default(aSide, aFluid, doFill);
	}

	private int mInternalSaveClock = 0;

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);

		if (mInternalSaveClock != 20){
			mInternalSaveClock++;
		}
		else {
			mInternalSaveClock = 0;
			tryForceNBTUpdate();
		}

	}

	private void tryForceNBTUpdate(){

		//Block is invalid.
		if (this == null || this.getBaseMetaTileEntity() == null){
			Utils.LOG_WARNING("Block was not valid for saving data.");
			return;
		}

		//Don't need this to run clientside.
		if (!this.getBaseMetaTileEntity().isServerSide()) {
			return;
		}

		//Internal Tag was not valid.
		try{
		if (internalCraftingComponentsTag == null){
			Utils.LOG_WARNING("Internal NBT data tag was null.");
			return;
		}	
		} catch (NullPointerException x){
			Utils.LOG_WARNING("Caught null NBT.");
		}
		/*if (internalCraftingComponentsTag.hasNoTags()){
			Utils.LOG_WARNING("Internal NBT data tag was not valid.");
			return;
		}*/

		//Internal tag was valid and contains tags.
		if (!this.internalCraftingComponentsTag.hasNoTags()){
			Utils.LOG_WARNING("Found tags to save.");
			saveNBTData(internalCraftingComponentsTag);
		}
		//Internal tag has no tags.
		else {
			Utils.LOG_WARNING("Found no tags to save.");	
			saveNBTData(internalCraftingComponentsTag);		
		}

		//Mark block for update
		int x,y,z = 0;
		x = this.getBaseMetaTileEntity().getXCoord();
		y = this.getBaseMetaTileEntity().getYCoord();
		z = this.getBaseMetaTileEntity().getZCoord();
		this.getBaseMetaTileEntity().getWorld().markBlockForUpdate(x, y, z);

		//Mark block dirty, let chunk know it's data has changed and it must be saved to disk. (Albeit slowly)
		this.getBaseMetaTileEntity().markDirty();		
	}

}