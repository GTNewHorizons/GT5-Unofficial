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
	private final NBTTagCompound internalCraftingComponentsTag = new NBTTagCompound();
	private boolean isServerSide;

	public GT_MetaTileEntity_TieredTank(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + ((aTier+1) * 32000) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 3, "Stores " + ((aTier+1) * 32000) + "L of fluid", aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, CORE.GT_Tooltip};
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == 1 ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER_ACTIVE)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER)};
	}

	/*@Override
	public String[] getDescription() {
		//setVars();
		if ((this.mFluidName.equals("Empty")||this.mFluidName.equals("")) || (this.mFluidAmount <= 0)){
			return new String[] {"Stores " + (this.mTier * 32000) + "L of fluid", CORE.GT_Tooltip};
		}
		return new String[] {"Stores " + (this.mTier * 32000) + "L of fluid", "Stored Fluid: "+this.mFluidName, "Stored Amount: "+this.mFluidAmount+"l", CORE.GT_Tooltip};
	}*/


	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		final NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		if (gtCraftingComponentsTag != null){

			//Utils.LOG_INFO("Got Crafting Tag");

			if (this.mFluid != null){
				Utils.LOG_INFO("mFluid was not null, Saving TileEntity NBT data. Saving "+this.mFluid.amount+"L of "+this.mFluid.getFluid().getName());
				gtCraftingComponentsTag.setInteger("xAmount", this.mFluid.amount);
				gtCraftingComponentsTag.setString("xFluid", this.mFluid.getFluid().getName());
				this.mFluidName = this.mFluid.getFluid().getName();

				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
				this.markDirty();
			}
			else {
				/*Utils.LOG_INFO("mFluid was null, Saving TileEntity NBT data.");
				gtCraftingComponentsTag.removeTag("xFluid");
				gtCraftingComponentsTag.removeTag("xAmount");

				//Backup the current tag
				//gtCraftingComponentsTag.setTag("backupTag", internalCraftingComponentsTag);
				//internalCraftingComponentsTag = gtCraftingComponentsTag;

				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);*/
			}
		}
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		final NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		String xFluid = null;
		int xAmount = 0;
		if (gtCraftingComponentsTag.hasNoTags()){
			if (this.mFluid != null){
				Utils.LOG_INFO("mFluid was not null, Creating TileEntity NBT data.");
				gtCraftingComponentsTag.setInteger("xAmount", this.mFluid.amount);
				gtCraftingComponentsTag.setString("xFluid", this.mFluid.getFluid().getName());
				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
			}
		}
		else {

			//internalCraftingComponentsTag = gtCraftingComponentsTag.getCompoundTag("backupTag");

			if (gtCraftingComponentsTag.hasKey("xFluid")){
				Utils.LOG_INFO("xFluid was not null, Loading TileEntity NBT data.");
				xFluid = gtCraftingComponentsTag.getString("xFluid");
			}
			if (gtCraftingComponentsTag.hasKey("xAmount")){
				Utils.LOG_INFO("xAmount was not null, Loading TileEntity NBT data.");
				xAmount = gtCraftingComponentsTag.getInteger("xAmount");
			}
			if ((xFluid != null) && (xAmount != 0)){
				Utils.LOG_INFO("Setting Internal Tank, loading "+xAmount+"L of "+xFluid);
				this.setInternalTank(xFluid, xAmount);
			}
		}

	}

	private boolean setInternalTank(final String fluidName, final int amount){
		final FluidStack temp = FluidUtils.getFluidStack(fluidName, amount);
		if (temp != null){
			if (this.mFluid == null){
				this.mFluid = temp;
				Utils.LOG_INFO(temp.getFluid().getName()+" Amount: "+temp.amount+"L");
			}
			else{
				Utils.LOG_INFO("Retained Fluid.");
				Utils.LOG_INFO(this.mFluid.getFluid().getName()+" Amxount: "+this.mFluid.amount+"L");
			}
			this.markDirty();
			return true;
		}
		return false;


	}

	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.drain(maxDrain, doDrain);
	}

	@Override
	public int fill(final FluidStack aFluid, final boolean doFill) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.fill(aFluid, doFill);
	}

	@Override
	public void setItemNBT(final NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		Utils.LOG_INFO("setItemNBT");
		//aNBT.setTag("GT.CraftingComponents", lRecipeStuff);

	}

	@Override
	public void closeInventory() {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		super.closeInventory();
	}

	@Override
	public boolean onWrenchRightClick(final byte aSide, final byte aWrenchingSide,
			final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			//setVars();
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return true;
	}

	@Override
	public void onLeftclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		super.onLeftclick(aBaseMetaTileEntity, aPlayer);

		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
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

		if (this.mFluid == null) {
			return new String[]{
					GT_Values.VOLTAGE_NAMES[this.mTier]+" Fluid Tank",
					"Stored Fluid:",
					"No Fluid",
					Integer.toString(0) + "L",
					Integer.toString(this.getCapacity()) + "L"};
		}
		return new String[]{
				GT_Values.VOLTAGE_NAMES[this.mTier]+" Fluid Tank",
				"Stored Fluid:",
				this.mFluid.getLocalizedName(),
				Integer.toString(this.mFluid.amount) + "L",
				Integer.toString(this.getCapacity()) + "L"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredTank(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public int getCapacity() {
		return (int) (Math.pow(2, this.mTier) * 32000);
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

		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		super.onRemoval();
	}

	@Override
	public void onCloseGUI() {
		super.onCloseGUI();
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.drain(aSide, aFluid, doDrain);
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final int maxDrain, final boolean doDrain) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.drain(aSide, maxDrain, doDrain);
	}

	@Override
	public int fill(final ForgeDirection arg0, final FluidStack arg1, final boolean arg2) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.fill(arg0, arg1, arg2);
	}

	@Override
	public int fill_default(final ForgeDirection aSide, final FluidStack aFluid, final boolean doFill) {
		//Save NBT Data server side
		if (isServerSide){
			this.tryForceNBTUpdate();
		}
		return super.fill_default(aSide, aFluid, doFill);
	}

	private int mInternalSaveClock = 0;

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);

		isServerSide = aBaseMetaTileEntity.isServerSide();
		
		if (this.mInternalSaveClock != 20){
			this.mInternalSaveClock++;
		}
		else {
			this.mInternalSaveClock = 0;
			//Save NBT Data server side
			if (isServerSide){
				this.tryForceNBTUpdate();
			}
		}

	}

	private void tryForceNBTUpdate(){

		//Block is invalid.
		if ((this == null) || (this.getBaseMetaTileEntity() == null)){
			Utils.LOG_INFO("Block was not valid for saving data.");
			return;
		}

		//Don't need this to run clientside.
		if (!this.getBaseMetaTileEntity().isServerSide()) {
			return;
		}

		//Internal Tag was not valid.
		if (this.internalCraftingComponentsTag == null){
			Utils.LOG_INFO("Internal NBT data tag was null.");
			return;
		}
		/*if (internalCraftingComponentsTag.hasNoTags()){
			Utils.LOG_WARNING("Internal NBT data tag was not valid.");
			return;
		}*/

		//Internal tag was valid and contains tags.
		if (!this.internalCraftingComponentsTag.hasNoTags()){
			Utils.LOG_WARNING("Found tags to save.");
			this.saveNBTData(this.internalCraftingComponentsTag);
		}
		//Internal tag has no tags.
		else {
			Utils.LOG_INFO("Found no tags to save.");
			this.saveNBTData(this.internalCraftingComponentsTag);
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