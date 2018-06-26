package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.VN;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import ic2.api.energy.tile.IEnergySink;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaPipeEntityBase_Cable extends MetaPipeEntity implements IMetaTileEntityCable {
	public final float mThickNess;
	public final GT_Materials mMaterial;
	public final long mCableLossPerMeter, mAmperage, mVoltage;
	public final boolean mInsulated, mCanShock;
	public long mTransferredAmperage = 0, mTransferredAmperageLast20 = 0, mTransferredVoltageLast20 = 0;
	public long mRestRF;
	public short mOverheat;
	public final int mWireHeatingTicks;

	public GregtechMetaPipeEntityBase_Cable(final int aID, final String aName, final String aNameRegional,
			final float aThickNess, final GT_Materials aMaterial, final long aCableLossPerMeter, final long aAmperage,
			final long aVoltage, final boolean aInsulated, final boolean aCanShock) {
		super(aID, aName, aNameRegional, 0);
		this.mThickNess = aThickNess;
		this.mMaterial = aMaterial;
		this.mAmperage = aAmperage;
		this.mVoltage = aVoltage;
		this.mInsulated = aInsulated;
		this.mCanShock = aCanShock;
		this.mCableLossPerMeter = aCableLossPerMeter;
		this.mWireHeatingTicks = this.getGT5Var();
	}

	public GregtechMetaPipeEntityBase_Cable(final String aName, final float aThickNess, final GT_Materials aMaterial,
			final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated,
			final boolean aCanShock) {
		super(aName, 0);
		this.mThickNess = aThickNess;
		this.mMaterial = aMaterial;
		this.mAmperage = aAmperage;
		this.mVoltage = aVoltage;
		this.mInsulated = aInsulated;
		this.mCanShock = aCanShock;
		this.mCableLossPerMeter = aCableLossPerMeter;
		this.mWireHeatingTicks = this.getGT5Var();
	}

	private int getGT5Var() {
		final Class<? extends GT_Proxy> clazz = GT_Mod.gregtechproxy.getClass();
		final String lookingForValue = "mWireHeatingTicks";
		int temp = 4;
		Field field;
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			try {
				field = clazz.getClass().getField(lookingForValue);
				final Class<?> clazzType = field.getType();
				if (clazzType.toString().equals("int")) {
					temp = (field.getInt(clazz));
				} else {
					temp = 4;
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// Utils.LOG_INFO("FATAL ERROR - REFLECTION FAILED FOR GT CABLES
				// - PLEASE REPORT THIS.");
				Logger.WARNING("FATAL ERROR - REFLECTION FAILED FOR GT CABLES - PLEASE REPORT THIS.");
				Logger.ERROR("FATAL ERROR - REFLECTION FAILED FOR GT CABLES - PLEASE REPORT THIS.");
				temp = 4;
			}
		}
		return temp;
	}

	@Override
	public byte getTileEntityBaseType() {
		return (byte) (this.mInsulated ? 9 : 8);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaPipeEntityBase_Cable(this.mName, this.mThickNess, this.mMaterial,
				this.mCableLossPerMeter, this.mAmperage, this.mVoltage, this.mInsulated, this.mCanShock);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide,
			final byte aConnections, final byte aColorIndex, final boolean aConnected, final boolean aRedstone) {
		if (!this.mInsulated) {
			return new ITexture[] { new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
					this.mMaterial.mRGBa) };
		}
		if (aConnected) {
			final float tThickNess = this.getThickNess();
			if (tThickNess < 0.37F) {
				return new ITexture[] {
						new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
								this.mMaterial.mRGBa),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_TINY,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			}
			if (tThickNess < 0.49F) {
				return new ITexture[] {
						new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
								this.mMaterial.mRGBa),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_SMALL,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			}
			if (tThickNess < 0.74F) {
				return new ITexture[] {
						new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
								this.mMaterial.mRGBa),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			}
			if (tThickNess < 0.99F) {
				return new ITexture[] {
						new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
								this.mMaterial.mRGBa),
						new GT_RenderedTexture(Textures.BlockIcons.INSULATION_LARGE,
								Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
			}
			return new ITexture[] {
					new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
							this.mMaterial.mRGBa),
					new GT_RenderedTexture(Textures.BlockIcons.INSULATION_HUGE,
							Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
		}
		return new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL,
				Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
	}

	@Override
	public void onEntityCollidedWithBlock(final World aWorld, final int aX, final int aY, final int aZ,
			final Entity aEntity) {
		if (this.mCanShock && ((((BaseMetaPipeEntity) this.getBaseMetaTileEntity()).mConnections & -128) == 0)
				&& (aEntity instanceof EntityLivingBase)) {
			GT_Utility.applyElectricityDamage((EntityLivingBase) aEntity, this.mTransferredVoltageLast20,
					this.mTransferredAmperageLast20);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World aWorld, final int aX, final int aY, final int aZ) {
		if (!this.mCanShock) {
			return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
		}
		return AxisAlignedBB.getBoundingBox(aX + 0.125D, aY + 0.125D, aZ + 0.125D, aX + 0.875D, aY + 0.875D,
				aZ + 0.875D);
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
		return true;
	}

	@Override
	public final boolean renderInside(final byte aSide) {
		return false;
	}

	@Override
	public int getProgresstime() {
		return (int) this.mTransferredAmperage * 64;
	}

	@Override
	public int maxProgresstime() {
		return (int) this.mAmperage * 64;
	}

	@Override
	public long injectEnergyUnits(final byte aSide, final long aVoltage, final long aAmperage) {
		if (!this.getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide,
				this.getBaseMetaTileEntity().getCoverIDAtSide(aSide),
				this.getBaseMetaTileEntity().getCoverDataAtSide(aSide), this.getBaseMetaTileEntity())) {
			return 0;
		}
		return this.transferElectricity(aSide, aVoltage, aAmperage,
				new ArrayList<>(Arrays.asList((TileEntity) this.getBaseMetaTileEntity())));
	}

	@Override
	public long transferElectricity(final byte aSide, long aVoltage, final long aAmperage,
			final ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
		long rUsedAmperes = 0;
		aVoltage -= this.mCableLossPerMeter;
		if (aVoltage > 0) {
			for (byte i = 0; (i < 6) && (aAmperage > rUsedAmperes); i++) {
				if ((i != aSide) && ((this.mConnections & (1 << i)) != 0)
						&& this.getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsEnergyOut(i,
								this.getBaseMetaTileEntity().getCoverIDAtSide(i),
								this.getBaseMetaTileEntity().getCoverDataAtSide(i), this.getBaseMetaTileEntity())) {
					final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(i);
					if (!aAlreadyPassedTileEntityList.contains(tTileEntity)) {
						aAlreadyPassedTileEntityList.add(tTileEntity);
						if (tTileEntity instanceof IEnergyConnected) {
							if (this.getBaseMetaTileEntity().getColorization() >= 0) {
								final byte tColor = ((IEnergyConnected) tTileEntity).getColorization();
								if ((tColor >= 0) && (tColor != this.getBaseMetaTileEntity().getColorization())) {
									continue;
								}
							}
							if ((tTileEntity instanceof IGregTechTileEntity)
									&& (((IGregTechTileEntity) tTileEntity)
											.getMetaTileEntity() instanceof IMetaTileEntityCable)
									&& ((IGregTechTileEntity) tTileEntity)
											.getCoverBehaviorAtSide(GT_Utility.getOppositeSide(i))
											.letsEnergyIn(GT_Utility.getOppositeSide(i),
													((IGregTechTileEntity) tTileEntity)
															.getCoverIDAtSide(GT_Utility.getOppositeSide(i)),
													((IGregTechTileEntity) tTileEntity)
															.getCoverDataAtSide(GT_Utility.getOppositeSide(i)),
													((IGregTechTileEntity) tTileEntity))) {
								if (((IGregTechTileEntity) tTileEntity).getTimer() > 50) {
									rUsedAmperes += ((IMetaTileEntityCable) ((IGregTechTileEntity) tTileEntity)
											.getMetaTileEntity()).transferElectricity(GT_Utility.getOppositeSide(i),
													aVoltage, aAmperage - rUsedAmperes, aAlreadyPassedTileEntityList);
								}
							} else {
								rUsedAmperes += ((IEnergyConnected) tTileEntity).injectEnergyUnits(
										GT_Utility.getOppositeSide(i), aVoltage, aAmperage - rUsedAmperes);
							}
							// } else if (tTileEntity instanceof IEnergySink) {
							// ForgeDirection tDirection =
							// ForgeDirection.getOrientation(i).getOpposite();
							// if
							// (((IEnergySink)tTileEntity).acceptsEnergyFrom((TileEntity)getBaseMetaTileEntity(),
							// tDirection)) {
							// if
							// (((IEnergySink)tTileEntity).demandedEnergyUnits()
							// > 0 &&
							// ((IEnergySink)tTileEntity).injectEnergyUnits(tDirection,
							// aVoltage) < aVoltage) rUsedAmperes++;
							// }
						} else if (tTileEntity instanceof IEnergySink) {
							final ForgeDirection tDirection = ForgeDirection.getOrientation(i).getOpposite();
							if (((IEnergySink) tTileEntity).acceptsEnergyFrom((TileEntity) this.getBaseMetaTileEntity(),
									tDirection)) {
								if ((((IEnergySink) tTileEntity).getDemandedEnergy() > 0)
										&& (((IEnergySink) tTileEntity).injectEnergy(tDirection, aVoltage,
												aVoltage) < aVoltage)) {
									rUsedAmperes++;
								}
							}
						} else if (GregTech_API.mOutputRF && (tTileEntity instanceof IEnergyReceiver)) {
							final ForgeDirection tDirection = ForgeDirection.getOrientation(i).getOpposite();
							final int rfOut = (int) ((aVoltage * GregTech_API.mEUtoRF) / 100);
							if (((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, true) == rfOut) {
								((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, false);
								rUsedAmperes++;
							} else if (((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, true) > 0) {
								if (this.mRestRF == 0) {
									final int RFtrans = ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut,
											false);
									rUsedAmperes++;
									this.mRestRF = rfOut - RFtrans;
								} else {
									final int RFtrans = ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection,
											(int) this.mRestRF, false);
									this.mRestRF = this.mRestRF - RFtrans;
								}
							}
							if (GregTech_API.mRFExplosions && (((IEnergyReceiver) tTileEntity)
									.getMaxEnergyStored(tDirection) < (rfOut * 600))) {
								if (rfOut > ((32 * GregTech_API.mEUtoRF) / 100)) {
									this.doExplosion(rfOut);
								}
							}
						}
					}
				}
			}
		}

		this.mTransferredAmperage += rUsedAmperes;
		this.mTransferredVoltageLast20 = Math.max(this.mTransferredVoltageLast20, aVoltage);
		this.mTransferredAmperageLast20 = Math.max(this.mTransferredAmperageLast20, this.mTransferredAmperage);

		if ((aVoltage > this.mVoltage) || (this.mTransferredAmperage > this.mAmperage)) {
			// GT 5.09
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				if (this.mOverheat > (this.mWireHeatingTicks * 100)) {
					this.getBaseMetaTileEntity().setToFire();
				} else {
					this.mOverheat += 100;
				}
				return aAmperage;
			}
			// GT 5.08
			else {
				this.getBaseMetaTileEntity().setToFire();
				return aAmperage;
			}
		}

		return rUsedAmperes;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			this.mTransferredAmperage = 0;
			if (this.mOverheat > 0) {
				this.mOverheat--;
			}

			if ((aTick % 20) == 0) {
				this.mTransferredVoltageLast20 = 0;
				this.mTransferredAmperageLast20 = 0;
				this.mConnections = 0;
				for (byte i = 0, j = 0; i < 6; i++) {
					j = GT_Utility.getOppositeSide(i);
					if (aBaseMetaTileEntity.getCoverBehaviorAtSide(i).alwaysLookConnected(i,
							aBaseMetaTileEntity.getCoverIDAtSide(i), aBaseMetaTileEntity.getCoverDataAtSide(i),
							aBaseMetaTileEntity)
							|| aBaseMetaTileEntity.getCoverBehaviorAtSide(i).letsEnergyIn(i,
									aBaseMetaTileEntity.getCoverIDAtSide(i), aBaseMetaTileEntity.getCoverDataAtSide(i),
									aBaseMetaTileEntity)
							|| aBaseMetaTileEntity.getCoverBehaviorAtSide(i).letsEnergyOut(i,
									aBaseMetaTileEntity.getCoverIDAtSide(i), aBaseMetaTileEntity.getCoverDataAtSide(i),
									aBaseMetaTileEntity)) {
						final TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(i);
						if (tTileEntity instanceof IColoredTileEntity) {
							if (aBaseMetaTileEntity.getColorization() >= 0) {
								final byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
								if ((tColor >= 0) && (tColor != aBaseMetaTileEntity.getColorization())) {
									continue;
								}
							}
						}
						if ((tTileEntity instanceof IEnergyConnected)
								&& (((IEnergyConnected) tTileEntity).inputEnergyFrom(j)
										|| ((IEnergyConnected) tTileEntity).outputsEnergyTo(j))) {
							this.mConnections |= (1 << i);
							continue;
						}
						if ((tTileEntity instanceof IGregTechTileEntity) && (((IGregTechTileEntity) tTileEntity)
								.getMetaTileEntity() instanceof IMetaTileEntityCable)) {
							if (((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(j).alwaysLookConnected(j,
									((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(j),
									((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(j),
									((IGregTechTileEntity) tTileEntity))
									|| ((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(j).letsEnergyIn(j,
											((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(j),
											((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(j),
											((IGregTechTileEntity) tTileEntity))
									|| ((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(j).letsEnergyOut(j,
											((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(j),
											((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(j),
											((IGregTechTileEntity) tTileEntity))) {
								this.mConnections |= (1 << i);
								continue;
							}
						}
						if ((tTileEntity instanceof IEnergySink) && ((IEnergySink) tTileEntity).acceptsEnergyFrom(
								(TileEntity) aBaseMetaTileEntity, ForgeDirection.getOrientation(j))) {
							this.mConnections |= (1 << i);
							continue;
						}
						if (GregTech_API.mOutputRF && (tTileEntity instanceof IEnergyReceiver)
								&& ((IEnergyReceiver) tTileEntity).canConnectEnergy(ForgeDirection.getOrientation(j))) {
							this.mConnections |= (1 << i);
							continue;
						}
						/*
						 * if (tTileEntity instanceof IEnergyEmitter &&
						 * ((IEnergyEmitter)tTileEntity).emitsEnergyTo((
						 * TileEntity)aBaseMetaTileEntity,
						 * ForgeDirection.getOrientation(j))) { mConnections |=
						 * (1<<i); continue; }
						 */
					}
				}
			}
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"Max Voltage: " + EnumChatFormatting.GREEN + this.mVoltage + " ("
						+ VN[GT_Utility.getTier(this.mVoltage)] + ")" + EnumChatFormatting.GRAY,
				"Max Amperage: " + EnumChatFormatting.YELLOW + this.mAmperage + EnumChatFormatting.GRAY,
				"Loss/Meter/Ampere: " + EnumChatFormatting.RED + this.mCableLossPerMeter + EnumChatFormatting.GRAY
						+ " EU-Volt" };
	}

	@Override
	public float getThickNess() {
		return this.mThickNess;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		//
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		//
	}
}