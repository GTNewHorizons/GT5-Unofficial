package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.interfaces.IHeatEntity;
import gtPlusPlus.xmod.gregtech.api.interfaces.IMetaTileEntityHeatPipe;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_MetaPipeEntity_Heat extends MetaPipeEntity implements IMetaTileEntityHeatPipe, IHeatEntity {

	public final Materials mMaterial;
	public final long mHeatLossPerMeter, mAmperage, mMaxTemp;
	public final boolean mInsulated, mCanShock;
	public long mTransferredAmperage = 0, mTransferredAmperageLast20 = 0, mTransferredVoltageLast20 = 0;
	public short mOverheat;

	private boolean mCheckConnections;
	public byte mDisableInput;

	public GT_MetaPipeEntity_Heat(int aID, String aName, String aNameRegional, Materials aMaterial, long aMaxTemp) {
		super(aID, aName, aNameRegional, 0);
		mMaterial = aMaterial;
		mAmperage = 1;
		mMaxTemp = aMaxTemp;
		mInsulated = false;
		mCanShock = true;
		mHeatLossPerMeter = aMaxTemp/1000;
	}

	public GT_MetaPipeEntity_Heat(String aName, Materials aMaterial, long aMaxTemp) {
		super(aName, 0);
		mMaterial = aMaterial;
		mAmperage = 1;
		mMaxTemp = aMaxTemp;
		mInsulated = false;
		mCanShock = true;
		mHeatLossPerMeter = aMaxTemp/1000;
	}


	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaPipeEntity_Heat(mName, mMaterial, mMaxTemp);
	}

	@Override
	public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
		if (mCanShock && (((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0 && aEntity instanceof EntityLivingBase)
			GT_Utility.applyHeatDamage((EntityLivingBase) aEntity, mTransferredVoltageLast20);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
		if (!mCanShock) return super.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
		return AxisAlignedBB.getBoundingBox(aX + 0.125D, aY + 0.125D, aZ + 0.125D, aX + 0.875D, aY + 0.875D, aZ + 0.875D);
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return false;
	}

	@Override
	public boolean isValidSlot(int aIndex) {
		return true;
	}

	@Override
	public final boolean renderInside(byte aSide) {
		return false;
	}

	@Override
	public int getProgresstime() {
		return (int) mTransferredAmperage * 64;
	}

	@Override
	public int maxProgresstime() {
		return (int) mAmperage * 64;
	}

	@Override
	public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		if (!getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), getBaseMetaTileEntity()))
			return 0;
		return transferHeat(aSide, aVoltage, aAmperage, new ArrayList<TileEntity>(Arrays.asList((TileEntity) getBaseMetaTileEntity())));
	}

	@Override
	public long transferHeat(byte aSide, long aVoltage, long aAmperage, ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
		if (!this.isConnectedAtSide(aSide) && aSide != 6) {
			return 0L;
		} else {
			long rUsedAmperes = 0L;
			aVoltage -= this.mHeatLossPerMeter;
			if (aVoltage > 0L) {
				for (byte i = 0; i < 6 && aAmperage > rUsedAmperes; ++i) {
					if (i != aSide && this.isConnectedAtSide(i)
							&& this.getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsEnergyOut(i,
									this.getBaseMetaTileEntity().getCoverIDAtSide(i),
									this.getBaseMetaTileEntity().getCoverDataAtSide(i), this.getBaseMetaTileEntity())) {
						TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntityAtSide(i);
						if (!aAlreadyPassedTileEntityList.contains(tTileEntity)) {
							aAlreadyPassedTileEntityList.add(tTileEntity);
							if (tTileEntity instanceof IEnergyConnected) {
								if (this.getBaseMetaTileEntity().getColorization() >= 0) {
									byte tColor = ((IEnergyConnected) tTileEntity).getColorization();
									if (tColor >= 0 && tColor != this.getBaseMetaTileEntity().getColorization()) {
										continue;
									}
								}

								if (tTileEntity instanceof IGregTechTileEntity
										&& ((IGregTechTileEntity) tTileEntity)
										.getMetaTileEntity() instanceof IMetaTileEntityHeatPipe
										&& ((IGregTechTileEntity) tTileEntity)
										.getCoverBehaviorAtSide(GT_Utility.getOppositeSide(i))
										.letsEnergyIn(GT_Utility.getOppositeSide(i),
												((IGregTechTileEntity) tTileEntity)
												.getCoverIDAtSide(GT_Utility.getOppositeSide(i)),
												((IGregTechTileEntity) tTileEntity)
												.getCoverDataAtSide(GT_Utility.getOppositeSide(i)),
												(IGregTechTileEntity) tTileEntity)) {
									if (((IGregTechTileEntity) tTileEntity).getTimer() > 50L) {
										rUsedAmperes += ((IMetaTileEntityHeatPipe) ((IGregTechTileEntity) tTileEntity)
												.getMetaTileEntity()).transferHeat(GT_Utility.getOppositeSide(i),
														aVoltage, aAmperage - rUsedAmperes,
														aAlreadyPassedTileEntityList);
									}
								} else {
									rUsedAmperes += ((IEnergyConnected) tTileEntity).injectEnergyUnits(
											GT_Utility.getOppositeSide(i), aVoltage, aAmperage - rUsedAmperes);
								}
							} else {
								ForgeDirection tDirection;

								if (tTileEntity instanceof IEnergySink) {
									tDirection = ForgeDirection.getOrientation(i).getOpposite();
									if (((IEnergySink) tTileEntity)
											.acceptsEnergyFrom((TileEntity) this.getBaseMetaTileEntity(), tDirection)
											&& ((IEnergySink) tTileEntity).getDemandedEnergy() > 0.0D
											&& ((IEnergySink) tTileEntity).injectEnergy(tDirection, (double) aVoltage,
													(double) aVoltage) < (double) aVoltage) {
										++rUsedAmperes;
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
			if (aVoltage <= this.mMaxTemp && this.mTransferredAmperage <= this.mAmperage) {
				return rUsedAmperes;
			} else {
				if (this.mOverheat > GT_Mod.gregtechproxy.mWireHeatingTicks * 100) {
					//this.getBaseMetaTileEntity().setToFire();
				} else {
					this.mOverheat = (short) (this.mOverheat + 100);
				}

				return aAmperage;
			}
		}
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			this.mTransferredAmperage = 0L;
			if (this.mOverheat > 0) {
				--this.mOverheat;
			}

			if (aTick % 20L == 0L) {
				this.mTransferredVoltageLast20 = 0L;
				this.mTransferredAmperageLast20 = 0L;

				for (byte tSide = 0; tSide < 6; ++tSide) {
					IGregTechTileEntity tBaseMetaTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(tSide);
					byte uSide = GT_Utility.getOppositeSide(tSide);
					if ((this.mCheckConnections || this.isConnectedAtSide(tSide)
							|| aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).alwaysLookConnected(tSide,
									aBaseMetaTileEntity.getCoverIDAtSide(tSide),
									aBaseMetaTileEntity.getCoverDataAtSide(tSide), aBaseMetaTileEntity)
							|| tBaseMetaTileEntity != null && tBaseMetaTileEntity.getCoverBehaviorAtSide(uSide)
							.alwaysLookConnected(uSide, tBaseMetaTileEntity.getCoverIDAtSide(uSide),
									tBaseMetaTileEntity.getCoverDataAtSide(uSide), tBaseMetaTileEntity))
							&& this.connect(tSide) == 0) {
						this.disconnect(tSide);
					}
				}

				if (isGT6Pipes()) {
					this.mCheckConnections = false;
				}
			}
		} else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
			aBaseMetaTileEntity.issueTextureUpdate();
		}

	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Max Voltage: " + EnumChatFormatting.GOLD + mMaxTemp + "C" + EnumChatFormatting.GRAY,
				"Loss: " + EnumChatFormatting.RED + mHeatLossPerMeter + EnumChatFormatting.GRAY + " HU per meter",
				CORE.GT_Tooltip
		};
	}

	@Override
	public float getThickNess() {
		return 1;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("HeatBuffer", this.HeatBuffer);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.HeatBuffer = aNBT.getInteger("HeatBuffer");
	}













	protected int transmitHeat;
	protected int maxHeatEmitpeerTick;
	protected int HeatBuffer;




	public byte getTileEntityBaseType() {
		return 4;
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections,
			byte aColorIndex, boolean aConnected, boolean aRedstone) {
		float tThickNess = this.getThickNess();
		if (this.mDisableInput == 0) {
			return new ITexture[]{(ITexture) (aConnected
					? getBaseTexture(tThickNess, 1, this.mMaterial, aColorIndex)
							: new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
									Dyes.getModulation(aColorIndex, this.mMaterial.mRGBa)))};
		} else {
			byte tMask = 0;
			byte[][] sRestrictionArray = new byte[][]{{2, 3, 5, 4}, {2, 3, 4, 5}, {1, 0, 4, 5}, {1, 0, 4, 5},
				{1, 0, 2, 3}, {1, 0, 2, 3}};
				if (aSide >= 0 && aSide < 6) {
					for (byte i = 0; i < 4; ++i) {
						if (this.isInputDisabledAtSide(sRestrictionArray[aSide][i])) {
							tMask = (byte) (tMask | 1 << i);
						}
					}
				}

				return new ITexture[]{
						(ITexture) (aConnected
								? getBaseTexture(tThickNess, 1, this.mMaterial, aColorIndex)
										: new GT_RenderedTexture(this.mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
												Dyes.getModulation(aColorIndex, this.mMaterial.mRGBa))),
						getRestrictorTexture(tMask)};
		}
	}

	protected static final ITexture getBaseTexture(float aThickNess, int aPipeAmount, Materials aMaterial,
			byte aColorIndex) {
		if (aPipeAmount >= 9) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else if (aPipeAmount >= 4) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else if (aThickNess < 0.124F) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else if (aThickNess < 0.374F) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else if (aThickNess < 0.499F) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else if (aThickNess < 0.749F) {
			return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
					Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		} else {
			return aThickNess < 0.874F
					? new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
							Dyes.getModulation(aColorIndex, aMaterial.mRGBa))
							: new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
									Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
		}
	}

	protected static final ITexture getRestrictorTexture(byte aMask) {
		switch (aMask) {
		case 1 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_UP);
		case 2 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_DOWN);
		case 3 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_UD);
		case 4 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_LEFT);
		case 5 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_UL);
		case 6 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_DL);
		case 7 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_NR);
		case 8 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_RIGHT);
		case 9 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_UR);
		case 10 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_DR);
		case 11 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_NL);
		case 12 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_LR);
		case 13 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_ND);
		case 14 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR_NU);
		case 15 :
			return new GT_RenderedTexture(BlockIcons.PIPE_RESTRICTOR);
		default :
			return null;
		}
	}


	public final boolean isGT6Pipes() {
		return StaticFields59.mGT6StylePipes;
	}

	public void updateHeatEntity() {
		int amount = this.getMaxHeatEmittedPerTick() - this.HeatBuffer;
		if (amount > 0) {
			this.addtoHeatBuffer(this.fillHeatBuffer(amount));
		}		
	}

	public boolean facingMatchesDirection(ForgeDirection direction) {
		return true;
	}

	public int maxrequestHeatTick(ForgeDirection directionFrom) {
		return this.getMaxHeatEmittedPerTick();
	}

	public int requestHeat(ForgeDirection directionFrom, int requestheat) {
		if (this.facingMatchesDirection(directionFrom)) {
			int heatbuffertemp = this.getHeatBuffer();
			if (this.getHeatBuffer() >= requestheat) {
				this.setHeatBuffer(this.getHeatBuffer() - requestheat);
				this.transmitHeat = requestheat;
				return requestheat;
			} else {
				this.transmitHeat = heatbuffertemp;
				this.setHeatBuffer(0);
				return heatbuffertemp;
			}
		} else {
			return 0;
		}
	}


	public int getHeatBuffer() {
		return this.HeatBuffer;
	}

	public void setHeatBuffer(int HeatBuffer) {
		this.HeatBuffer = HeatBuffer;
	}

	public void addtoHeatBuffer(int heat) {
		this.setHeatBuffer(this.getHeatBuffer() + heat);
	}
	

	public int fillHeatBuffer(int maxAmount) {
		return maxAmount >= this.getMaxHeatEmittedPerTick() ? this.getMaxHeatEmittedPerTick() : maxAmount;
	}

	public int getMaxHeatEmittedPerTick() {
		return (int) (this.mMaxTemp/1000);		
	}
	
	
	public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
			float aZ) {
		if (isGT6Pipes()) {
			byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
			byte tMask = (byte) (1 << tSide);
			if (aPlayer.isSneaking()) {
				if (this.isInputDisabledAtSide(tSide)) {
					this.mDisableInput = (byte) (this.mDisableInput & ~tMask);
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("212", "Input enabled"));
					if (!this.isConnectedAtSide(tSide)) {
						this.connect(tSide);
					}
				} else {
					this.mDisableInput |= tMask;
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("213", "Input disabled"));
				}
			} else if (!this.isConnectedAtSide(tSide)) {
				if (this.connect(tSide) > 0) {
					GT_Utility.sendChatToPlayer(aPlayer, this.trans("214", "Connected"));
				}
			} else {
				this.disconnect(tSide);
				GT_Utility.sendChatToPlayer(aPlayer, this.trans("215", "Disconnected"));
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean isInputDisabledAtSide(int aSide) {
		return (this.mDisableInput & 1 << aSide) != 0;
	}

	
	
	
	@Override
	public int maxHeatInPerTick(ForgeDirection var1) {
		return (int) (this.mMaxTemp/500);
	}

	@Override
	public int addHeat(ForgeDirection var1, int var2) {
		
		
		
		/*ForgeDirection dir = ForgeDirection.getOrientation(this.getFacing());
		TileEntity te = this.getBaseMetaTileEntity().getWorld().getTileEntity(this.xCoord + dir.offsetX, this.yCoord + dir.offsetY,
				this.zCoord + dir.offsetZ);
		if (te instanceof IHeatSource) {
			int heatbandwith = ((IHeatSource) te).maxrequestHeatTick(dir.getOpposite());
			double freeEUstorage = (double) this.maxEUStorage - this.EUstorage;
			if (freeEUstorage >= this.productionpeerheat * (double) heatbandwith) {
				this.receivedheat = ((IHeatSource) te).requestHeat(dir.getOpposite(), heatbandwith);
				if (this.receivedheat != 0) {
					this.production = (double) this.receivedheat * this.productionpeerheat;
					this.EUstorage += this.production;
					return true;
				}
			}
		}

		this.production = 0.0D;
		this.receivedheat = 0;*/
		
		
		return 0;
	}

	@Override
	public int getTransmitHeat() {
		return this.transmitHeat;
	}





}