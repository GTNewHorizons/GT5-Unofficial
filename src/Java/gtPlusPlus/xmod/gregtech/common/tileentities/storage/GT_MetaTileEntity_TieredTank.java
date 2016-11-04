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

	private NBTTagCompound	mRecipeStuff	= new NBTTagCompound();
	private String			mFluidName;
	private int				mFluidAmount;

	public GT_MetaTileEntity_TieredTank(final int aID, final String aName, final String aNameRegional,
			final int aTier) {
		super(aID, aName, aNameRegional, aTier, 3, "Stores " + (int) (Math.pow(2, aTier) * 32000) + "L of fluid");
	}

	public GT_MetaTileEntity_TieredTank(final String aName, final int aTier, final String aDescription,
			final ITexture[][][] aTextures) {
		super(aName, aTier, 3, aDescription, aTextures);
	}

	@Override
	public boolean canTankBeEmptied() {
		return true;
	}

	@Override
	public boolean canTankBeFilled() {
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
	public boolean doesEmptyContainers() {
		return true;
	}

	@Override
	public boolean doesFillContainers() {
		return true;
	}

	@Override
	public int getCapacity() {
		return (int) (Math.pow(2, this.mTier) * 32000);
	}

	@Override
	public String[] getDescription() {

		this.setVars();

		if (this.mFluidName.equals("Empty") || this.mFluidName.equals("") || this.mFluidAmount <= 0) {
			return new String[] {
					this.mDescription, CORE.GT_Tooltip
			};
		}
		return new String[] {
				this.mDescription, "Stored Fluid: " + this.mFluidName, "Stored Amount: " + this.mFluidAmount + "l",
				CORE.GT_Tooltip
		};
	}

	@Override
	public String[] getInfoData() {

		if (this.mFluid == null) {
			return new String[] {
					GT_Values.VOLTAGE_NAMES[this.mTier] + " Fluid Tank", "Stored Fluid:", "No Fluid",
					Integer.toString(0) + "L", Integer.toString(this.getCapacity()) + "L"
			};
		}
		return new String[] {
				GT_Values.VOLTAGE_NAMES[this.mTier] + " Fluid Tank", "Stored Fluid:", this.mFluid.getLocalizedName(),
				Integer.toString(this.mFluid.amount) + "L", Integer.toString(this.getCapacity()) + "L"
		};
	}

	@Override
	public int getTankPressure() {
		return 100;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == 1 ? new ITexture[] {
				Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
				new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER_ACTIVE)
		} : new ITexture[] {
				Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
				new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_POTIONBREWER)
		};
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public final byte getUpdateData() {
		return 0x00;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mRecipeStuff = aNBT.getCompoundTag("GT.CraftingComponents");
		this.mFluidName = this.mRecipeStuff.getString("mFluidName");
		this.mFluidAmount = this.mRecipeStuff.getInteger("mFluidAmount");
		this.mFluid = FluidUtils.getFluidStack(this.mFluidName, this.mFluidAmount);
		this.setItemNBT(aNBT);
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_TieredTank(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	@Override
	public void onMachineBlockUpdate() {
		this.getBaseMetaTileEntity().markDirty();
		super.onMachineBlockUpdate();
	}

	@Override
	public void onRemoval() {
		this.getBaseMetaTileEntity().markDirty();
		super.onRemoval();
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		this.setVars();
		this.mRecipeStuff.setString("mFluidName", this.mFluidName);
		this.mRecipeStuff.setInteger("mFluidAmount", this.mFluidAmount);
		aNBT.setTag("GT.CraftingComponents", this.mRecipeStuff);
	}

	@Override
	public void setItemNBT(final NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		this.mRecipeStuff.setString("mFluidName", this.mFluidName);
		this.mRecipeStuff.setInteger("mFluidAmount", this.mFluidAmount);
		aNBT.setTag("GT.CraftingComponents", this.mRecipeStuff);
	}

	private boolean setVars() {
		// Utils.LOG_INFO("setting Vars.");
		if (this.mFluidName.equals("") || !this.mFluidName.equals(null)) {
			if (this.mFluid != null) {
				this.mFluidName = this.mFluid.getFluid().getName();
			}
		}
		else {
			if (this.mFluid != null) {
				if (!this.mFluidName.equalsIgnoreCase(this.mFluid.getFluid().getName())) {
					this.mFluidName = this.mFluid.getFluid().getName();
				}
			}
			else {
				// Leave Values Blank.
				return false;
			}
		}

		if (this.mFluidAmount <= 0) {
			if (this.mFluid != null) {
				this.mFluidAmount = this.mFluid.amount;
			}
		}
		else {
			if (this.mFluid != null) {
				if (this.mFluidAmount != this.mFluid.amount) {
					this.mFluidAmount = this.mFluid.amount;
				}
			}
			else {
				// Leave Values Blank.
				return false;
			}
		}
		return true;
	}

}