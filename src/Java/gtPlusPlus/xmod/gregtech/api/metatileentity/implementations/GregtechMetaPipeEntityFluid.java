package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.D1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class GregtechMetaPipeEntityFluid extends MetaPipeEntity {
	public final float mThickNess;
	public final GT_Materials mMaterial;
	public final int mCapacity, mHeatResistance;
	public final boolean mGasProof;
	public FluidStack mFluid;
	public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;

	public GregtechMetaPipeEntityFluid(final int aID, final String aName, final String aNameRegional, final float aThickNess, final GT_Materials aMaterial, final int aCapacity, final int aHeatResistance, final boolean aGasProof) {
		super(aID, aName, aNameRegional, 0);
		this.mThickNess = aThickNess;
		this.mMaterial = aMaterial;
		this.mCapacity = aCapacity;
		this.mGasProof = aGasProof;
		this.mHeatResistance = aHeatResistance;
	}

	public GregtechMetaPipeEntityFluid(final String aName, final float aThickNess, final GT_Materials aMaterial, final int aCapacity, final int aHeatResistance, final boolean aGasProof) {
		super(aName, 0);
		this.mThickNess = aThickNess;
		this.mMaterial = aMaterial;
		this.mCapacity = aCapacity;
		this.mGasProof = aGasProof;
		this.mHeatResistance = aHeatResistance;
	}

	@Override
	public byte getTileEntityBaseType() {
		return this.mMaterial == null ? 4 : (byte) ((this.mMaterial.contains(SubTag.WOOD) ? 12 : 4) + Math.max(0, Math.min(3, this.mMaterial.mToolQuality)));
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPipeEntityFluid(this.mName, this.mThickNess, this.mMaterial, this.mCapacity, this.mHeatResistance, this.mGasProof);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aConnections, final byte aColorIndex, final boolean aConnected, final boolean aRedstone) {

		final short[] colours = Dyes.getModulation(aColorIndex, this.mMaterial.mRGBa);
		if (aConnected) {
			final float tThickNess = this.getThickNess();
			if (tThickNess < 0.37F){
				return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex], colours)};
			}
			if (tThickNess < 0.49F){
				return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex], colours)};
			}
			if (tThickNess < 0.74F){
				return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex], colours)};
			}
			if (tThickNess < 0.99F){
				return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex], colours)};
			}
			return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex], colours)};
		}
		return new ITexture[]{new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], colours)};
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return false;
	}

	@Override
	public final boolean renderInside(final byte aSide) {
		return false;
	}

	@Override
	public int getProgresstime() {
		return this.getFluidAmount();
	}

	@Override
	public int maxProgresstime() {
		return this.getCapacity();
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		if (this.mFluid != null) {
			aNBT.setTag("mFluid", this.mFluid.writeToNBT(new NBTTagCompound()));
		}
		aNBT.setByte("mLastReceivedFrom", this.mLastReceivedFrom);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mFluid = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
		this.mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
	}

	@Override
	public void onEntityCollidedWithBlock(final World aWorld, final int aX, final int aY, final int aZ, final Entity aEntity) {
		if ((this.mFluid != null) && ((((BaseMetaPipeEntity) this.getBaseMetaTileEntity()).mConnections & -128) == 0) && (aEntity instanceof EntityLivingBase)) {
			final int tTemperature = this.mFluid.getFluid().getTemperature(this.mFluid);
			if (tTemperature > 320) {
				GT_Utility.applyHeatDamage((EntityLivingBase) aEntity, (tTemperature - 300) / 50.0F);
			} else if (tTemperature < 260) {
				GT_Utility.applyFrostDamage((EntityLivingBase) aEntity, (270 - tTemperature) / 25.0F);
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World aWorld, final int aX, final int aY, final int aZ) {
		return AxisAlignedBB.getBoundingBox(aX + 0.125D, aY + 0.125D, aZ + 0.125D, aX + 0.875D, aY + 0.875D, aZ + 0.875D);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide() && ((aTick % 5) == 0)) {
			this.mLastReceivedFrom &= 63;
			if (this.mLastReceivedFrom == 63) {
				this.mLastReceivedFrom = 0;
			}

			if ((this.mFluid != null) && (this.mFluid.amount > 0)) {
				final int tTemperature = this.mFluid.getFluid().getTemperature(this.mFluid);
				if (tTemperature > this.mHeatResistance) {
					if (aBaseMetaTileEntity.getRandomNumber(100) == 0) {
						aBaseMetaTileEntity.setToFire();
						return;
					}
					aBaseMetaTileEntity.setOnFire();
				}
				if (!this.mGasProof && this.mFluid.getFluid().isGaseous(this.mFluid)) {
					this.mFluid.amount -= 5;
					this.sendSound((byte) 9);
					if (tTemperature > 320) {
						try {
							for (final EntityLivingBase tLiving : (ArrayList<EntityLivingBase>) this.getBaseMetaTileEntity().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.getBaseMetaTileEntity().getXCoord() - 2, this.getBaseMetaTileEntity().getYCoord() - 2, this.getBaseMetaTileEntity().getZCoord() - 2, this.getBaseMetaTileEntity().getXCoord() + 3, this.getBaseMetaTileEntity().getYCoord() + 3, this.getBaseMetaTileEntity().getZCoord() + 3))) {
								GT_Utility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
							}
						} catch (final Throwable e) {
							if (D1) {
								e.printStackTrace(GT_Log.err);
							}
						}
					} else if (tTemperature < 260) {
						try {
							for (final EntityLivingBase tLiving : (ArrayList<EntityLivingBase>) this.getBaseMetaTileEntity().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.getBaseMetaTileEntity().getXCoord() - 2, this.getBaseMetaTileEntity().getYCoord() - 2, this.getBaseMetaTileEntity().getZCoord() - 2, this.getBaseMetaTileEntity().getXCoord() + 3, this.getBaseMetaTileEntity().getYCoord() + 3, this.getBaseMetaTileEntity().getZCoord() + 3))) {
								GT_Utility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
							}
						} catch (final Throwable e) {
							if (D1) {
								e.printStackTrace(GT_Log.err);
							}
						}
					}
					if (this.mFluid.amount <= 0) {
						this.mFluid = null;
					}
				}
			}

			if (this.mLastReceivedFrom == this.oLastReceivedFrom) {
				final HashMap<IFluidHandler, ForgeDirection> tTanks = new HashMap<>();

				this.mConnections = 0;

				for (byte tSide = 0, i = 0, j = (byte) aBaseMetaTileEntity.getRandomNumber(6); i < 6; i++) {
					tSide = (byte) ((j + i) % 6);

					final IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(tSide);
					if (tTileEntity != null) {
						if (tTileEntity instanceof IGregTechTileEntity) {
							if (aBaseMetaTileEntity.getColorization() >= 0) {
								final byte tColor = ((IGregTechTileEntity) tTileEntity).getColorization();
								if ((tColor >= 0) && ((tColor & 15) != (aBaseMetaTileEntity.getColorization() & 15))) {
									continue;
								}
							}
						}
						final FluidTankInfo[] tInfo = tTileEntity.getTankInfo(ForgeDirection.getOrientation(tSide).getOpposite());
						if ((tInfo != null) && (tInfo.length > 0)) {
							if ((tTileEntity instanceof ICoverable) && ((ICoverable) tTileEntity).getCoverBehaviorAtSide(GT_Utility.getOppositeSide(tSide)).alwaysLookConnected(GT_Utility.getOppositeSide(tSide), ((ICoverable) tTileEntity).getCoverIDAtSide(GT_Utility.getOppositeSide(tSide)), ((ICoverable) tTileEntity).getCoverDataAtSide(GT_Utility.getOppositeSide(tSide)), ((ICoverable) tTileEntity))) {
								this.mConnections |= (1 << tSide);
							}
							if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).letsFluidIn(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), null, aBaseMetaTileEntity)) {
								this.mConnections |= (1 << tSide);
							}
							if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).letsFluidOut(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), null, aBaseMetaTileEntity)) {
								this.mConnections |= (1 << tSide);
								if (((1 << tSide) & this.mLastReceivedFrom) == 0) {
									tTanks.put(tTileEntity, ForgeDirection.getOrientation(tSide).getOpposite());
								}
							}
							if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).alwaysLookConnected(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), aBaseMetaTileEntity)) {
								this.mConnections |= (1 << tSide);
							}
						}
					}
				}

				if ((this.mFluid != null) && (this.mFluid.amount > 0)) {
					int tAmount = Math.max(1, Math.min(this.mCapacity * 10, this.mFluid.amount / 2)), tSuccessfulTankAmount = 0;

					for (final Entry<IFluidHandler, ForgeDirection> tEntry : tTanks.entrySet()) {
						if (tEntry.getKey().fill(tEntry.getValue(), this.drain(tAmount, false), false) > 0) {
							tSuccessfulTankAmount++;
						}
					}

					if (tSuccessfulTankAmount > 0) {
						if (tAmount >= tSuccessfulTankAmount) {
							tAmount /= tSuccessfulTankAmount;
							for (final Entry<IFluidHandler, ForgeDirection> tTileEntity : tTanks.entrySet()) {
								if ((this.mFluid == null) || (this.mFluid.amount <= 0)) {
									break;
								}
								final int tFilledAmount = tTileEntity.getKey().fill(tTileEntity.getValue(), this.drain(tAmount, false), false);
								if (tFilledAmount > 0) {
									tTileEntity.getKey().fill(tTileEntity.getValue(), this.drain(tFilledAmount, true), true);
								}
							}
						} else {
							for (final Entry<IFluidHandler, ForgeDirection> tTileEntity : tTanks.entrySet()) {
								if ((this.mFluid == null) || (this.mFluid.amount <= 0)) {
									break;
								}
								final int tFilledAmount = tTileEntity.getKey().fill(tTileEntity.getValue(), this.drain(this.mFluid.amount, false), false);
								if (tFilledAmount > 0) {
									tTileEntity.getKey().fill(tTileEntity.getValue(), this.drain(tFilledAmount, true), true);
								}
							}
						}
					}
				}

				this.mLastReceivedFrom = 0;
			}

			this.oLastReceivedFrom = this.mLastReceivedFrom;
		}
	}

	@Override
	public void doSound(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.doSound(aIndex, aX, aY, aZ);
		if (aIndex == 9) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(4), 5, 1.0F, aX, aY, aZ);
			for (byte i = 0; i < 6; i++) {
				for (int l = 0; l < 2; ++l) {
					this.getBaseMetaTileEntity().getWorld().spawnParticle("largesmoke", (aX - 0.5) + Math.random(), (aY - 0.5) + Math.random(), (aZ - 0.5) + Math.random(), ForgeDirection.getOrientation(i).offsetX / 5.0, ForgeDirection.getOrientation(i).offsetY / 5.0, ForgeDirection.getOrientation(i).offsetZ / 5.0);
				}
			}
		}
	}

	@Override
	public final int getCapacity() {
		return this.mCapacity * 20;
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public final FluidStack getFluid() {
		return this.mFluid;
	}

	@Override
	public final int getFluidAmount() {
		return this.mFluid != null ? this.mFluid.amount : 0;
	}

	@Override
	public final int fill_default(final ForgeDirection aSide, final FluidStack aFluid, final boolean doFill) {
		if ((aFluid == null) || (aFluid.getFluid().getID() <= 0)) {
			return 0;
		}

		if ((this.mFluid == null) || (this.mFluid.getFluid().getID() <= 0)) {
			if (aFluid.amount <= this.getCapacity()) {
				if (doFill) {
					this.mFluid = aFluid.copy();
					this.mLastReceivedFrom |= (1 << aSide.ordinal());
				}
				return aFluid.amount;
			}
			if (doFill) {
				this.mFluid = aFluid.copy();
				this.mLastReceivedFrom |= (1 << aSide.ordinal());
				this.mFluid.amount = this.getCapacity();
			}
			return this.getCapacity();
		}

		if (!this.mFluid.isFluidEqual(aFluid)) {
			return 0;
		}

		final int space = this.getCapacity() - this.mFluid.amount;
		if (aFluid.amount <= space) {
			if (doFill) {
				this.mFluid.amount += aFluid.amount;
				this.mLastReceivedFrom |= (1 << aSide.ordinal());
			}
			return aFluid.amount;
		}
		if (doFill) {
			this.mFluid.amount = this.getCapacity();
			this.mLastReceivedFrom |= (1 << aSide.ordinal());
		}
		return space;
	}

	@Override
	public final FluidStack drain(final int maxDrain, final boolean doDrain) {
		if (this.mFluid == null) {
			return null;
		}
		if (this.mFluid.amount <= 0) {
			this.mFluid = null;
			return null;
		}

		int used = maxDrain;
		if (this.mFluid.amount < used) {
			used = this.mFluid.amount;
		}

		if (doDrain) {
			this.mFluid.amount -= used;
		}

		final FluidStack drained = this.mFluid.copy();
		drained.amount = used;

		if (this.mFluid.amount <= 0) {
			this.mFluid = null;
		}

		return drained;
	}

	@Override
	public int getTankPressure() {
		return (this.mFluid == null ? 0 : this.mFluid.amount) - (this.getCapacity() / 2);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				EnumChatFormatting.BLUE + "Fluid Capacity: " + (this.mCapacity * 20) + "L/sec" + EnumChatFormatting.GRAY,
				EnumChatFormatting.RED + "Heat Limit: " + this.mHeatResistance + " K" + EnumChatFormatting.GRAY,
				EnumChatFormatting.DARK_GREEN + "Gas Proof: " + (this.mGasProof) + EnumChatFormatting.GRAY,
				CORE.GT_Tooltip
		};
	}

	@Override
	public float getThickNess() {
		return this.mThickNess;
	}
}