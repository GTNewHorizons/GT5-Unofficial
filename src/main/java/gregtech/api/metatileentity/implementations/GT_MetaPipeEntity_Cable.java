package gregtech.api.metatileentity.implementations;

import cofh.api.energy.IEnergyReceiver;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
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
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gregtech.api.enums.GT_Values.VN;

public class GT_MetaPipeEntity_Cable extends MetaPipeEntity implements IMetaTileEntityCable {
    public final float mThickNess;
    public final Materials mMaterial;
    public final long mCableLossPerMeter, mAmperage, mVoltage;
    public final boolean mInsulated, mCanShock;
    public int mTransferredAmperage = 0, mTransferredAmperageLast20 = 0,mTransferredAmperageLast20OK=0,mTransferredAmperageOK=0;
    public long mTransferredVoltageLast20 = 0, mTransferredVoltage = 0,mTransferredVoltageLast20OK=0,mTransferredVoltageOK=0;
    public long mRestRF;
    public int mOverheat;
    public static short mMaxOverheat=(short) (GT_Mod.gregtechproxy.mWireHeatingTicks * 100);
    private boolean mCheckConnections = !GT_Mod.gregtechproxy.gt6Cable;

    private int[] lastAmperage;
    private long lastWorldTick;

    public GT_MetaPipeEntity_Cable(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial, long aCableLossPerMeter, long aAmperage, long aVoltage, boolean aInsulated, boolean aCanShock) {
        super(aID, aName, aNameRegional, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mAmperage = aAmperage;
        mVoltage = aVoltage;
        mInsulated = aInsulated;
        mCanShock = aCanShock;
        mCableLossPerMeter = aCableLossPerMeter;
    }

    public GT_MetaPipeEntity_Cable(String aName, float aThickNess, Materials aMaterial, long aCableLossPerMeter, long aAmperage, long aVoltage, boolean aInsulated, boolean aCanShock) {
        super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mAmperage = aAmperage;
        mVoltage = aVoltage;
        mInsulated = aInsulated;
        mCanShock = aCanShock;
        mCableLossPerMeter = aCableLossPerMeter;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mInsulated ? 9 : 8);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Cable(mName, mThickNess, mMaterial, mCableLossPerMeter, mAmperage, mVoltage, mInsulated, mCanShock);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
        if (!mInsulated)
            return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], Dyes.getModulation(aColorIndex, mMaterial.mRGBa) )};
        if (aConnected) {
            float tThickNess = getThickNess();
            if (tThickNess < 0.124F)
                return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            if (tThickNess < 0.374F)//0.375 x1
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_TINY, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            if (tThickNess < 0.499F)//0.500 x2
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_SMALL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            if (tThickNess < 0.624F)//0.625 x4
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            if (tThickNess < 0.749F)//0.750 x8
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_MEDIUM_PLUS, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            if (tThickNess < 0.874F)//0.825 x12
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_LARGE, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
            return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], mMaterial.mRGBa), new GT_RenderedTexture(Textures.BlockIcons.INSULATION_HUGE, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
        }
        return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.INSULATION_FULL, Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa))};
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
        if (mCanShock && (((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0 && aEntity instanceof EntityLivingBase && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity))
            GT_Utility.applyElectricityDamage((EntityLivingBase) aEntity, mTransferredVoltageLast20, mTransferredAmperageLast20);
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
        return transferElectricity(aSide, aVoltage, aAmperage, new ArrayList<TileEntity>(Arrays.asList((TileEntity) getBaseMetaTileEntity())));
    }

    @Override
    public long transferElectricity(byte aSide, long aVoltage, long aAmperage, ArrayList<TileEntity> aAlreadyPassedTileEntityList) {
        long rUsedAmperes = 0;
        aVoltage -= mCableLossPerMeter;
        if (aVoltage > 0) for (byte i = 0; i < 6 && aAmperage > rUsedAmperes; i++)
            if (i != aSide && (mConnections & (1 << i)) != 0 && getBaseMetaTileEntity().getCoverBehaviorAtSide(i).letsEnergyOut(i, getBaseMetaTileEntity().getCoverIDAtSide(i), getBaseMetaTileEntity().getCoverDataAtSide(i), getBaseMetaTileEntity())) {
                TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(i);
                if (!aAlreadyPassedTileEntityList.contains(tTileEntity)) {
                    aAlreadyPassedTileEntityList.add(tTileEntity);
                    if (tTileEntity instanceof IEnergyConnected) {
                        if (getBaseMetaTileEntity().getColorization() >= 0) {
                            byte tColor = ((IEnergyConnected) tTileEntity).getColorization();
                            if (tColor >= 0 && tColor != getBaseMetaTileEntity().getColorization()) continue;
                        }
                        if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof IMetaTileEntityCable && ((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(GT_Utility.getOppositeSide(i)).letsEnergyIn(GT_Utility.getOppositeSide(i), ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(GT_Utility.getOppositeSide(i)), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(GT_Utility.getOppositeSide(i)), ((IGregTechTileEntity) tTileEntity))) {
                            if (((IGregTechTileEntity) tTileEntity).getTimer() > 50)
                                rUsedAmperes += ((IMetaTileEntityCable) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()).transferElectricity(GT_Utility.getOppositeSide(i), aVoltage, aAmperage - rUsedAmperes, aAlreadyPassedTileEntityList);
                        } else {
                            rUsedAmperes += ((IEnergyConnected) tTileEntity).injectEnergyUnits(GT_Utility.getOppositeSide(i), aVoltage, aAmperage - rUsedAmperes);
                        }
//        		} else if (tTileEntity instanceof IEnergySink) {
//            		ForgeDirection tDirection = ForgeDirection.getOrientation(i).getOpposite();
//            		if (((IEnergySink)tTileEntity).acceptsEnergyFrom((TileEntity)getBaseMetaTileEntity(), tDirection)) {
//            			if (((IEnergySink)tTileEntity).demandedEnergyUnits() > 0 && ((IEnergySink)tTileEntity).injectEnergyUnits(tDirection, aVoltage) < aVoltage) rUsedAmperes++;
//            		}
                    } else if (tTileEntity instanceof IEnergySink) {
                        ForgeDirection tDirection = ForgeDirection.getOrientation(i).getOpposite();
                        if (((IEnergySink) tTileEntity).acceptsEnergyFrom((TileEntity) getBaseMetaTileEntity(), tDirection)) {
                            if (((IEnergySink) tTileEntity).getDemandedEnergy() > 0 && ((IEnergySink) tTileEntity).injectEnergy(tDirection, aVoltage, aVoltage) < aVoltage)
                                rUsedAmperes++;
                        }
                    } else if (GregTech_API.mOutputRF && tTileEntity instanceof IEnergyReceiver) {
                        ForgeDirection tDirection = ForgeDirection.getOrientation(i).getOpposite();
                        long rfOUT = aVoltage * GregTech_API.mEUtoRF / 100;
                        int  rfOut = rfOUT>Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)rfOUT;
                        if (((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, true) == rfOut) {
                            ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, false);
                            rUsedAmperes++;
                        } else if (((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, rfOut, true) > 0) {
                            if (mRestRF == 0) {
                                int RFtrans = ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, (int) rfOut, false);
                                rUsedAmperes++;
                                mRestRF = rfOut - RFtrans;
                            } else {
                                int RFtrans = ((IEnergyReceiver) tTileEntity).receiveEnergy(tDirection, (int) mRestRF, false);
                                mRestRF = mRestRF - RFtrans;
                            }
                        }
                        if (GregTech_API.mRFExplosions && ((IEnergyReceiver) tTileEntity).getMaxEnergyStored(tDirection) < rfOut * 600) {
                            if (rfOut > 32 * GregTech_API.mEUtoRF / 100) this.doExplosion(rfOut);
                        }
                    }
                }
            }
        mTransferredVoltage=Math.max(mTransferredVoltage,aVoltage);
        mTransferredAmperage += rUsedAmperes;
        mTransferredVoltageLast20 = Math.max(mTransferredVoltageLast20, aVoltage);
        mTransferredAmperageLast20 = Math.max(mTransferredAmperageLast20, mTransferredAmperage);
        if (aVoltage > mVoltage){
            mOverheat+=Math.max(100,100*GT_Utility.getTier(aVoltage)-GT_Utility.getTier(mVoltage));
        }
        if (mTransferredAmperage > mAmperage) return aAmperage;
        return rUsedAmperes;
        //Always return amount of used amperes, used all on overheat
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if(aBaseMetaTileEntity.isServerSide()) {
            lastAmperage = new int[16];
            lastWorldTick = aBaseMetaTileEntity.getWorld().getTotalWorldTime() - 1;//sets initial value -1 since it is in the same tick as first on post tick
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            {//amp handler
                long worldTick = aBaseMetaTileEntity.getWorld().getTotalWorldTime();
                int tickDiff = (int) (worldTick - lastWorldTick);
                lastWorldTick = worldTick;

                if (tickDiff >= 16) for (int i = 0; i <= 14; i++) lastAmperage[i] = 0;
                else {
                    System.arraycopy(lastAmperage, tickDiff, lastAmperage, 0, 16 - tickDiff);
                    for (int i = 14; i >= 0; i--) {
                        if (--tickDiff > 0) lastAmperage[i] = 0;
                        else break;
                    }
                }

                lastAmperage[15] = mTransferredAmperage;

                if (lastAmperage[15] > mAmperage) {
                    int i = 0;
                    for (; i <= 14; i++) {
                        if (lastAmperage[i] < mAmperage) {
                            lastAmperage[15] -= (int) mAmperage - lastAmperage[i];
                            lastAmperage[i] = (int)mAmperage;
                            if (lastAmperage[15] <= mAmperage) break;
                        }
                    }
                    if (lastAmperage[15] > mAmperage) {
                        mOverheat += 100 * (lastAmperage[15] - mAmperage);
                        lastAmperage[15] = (int) mAmperage;
                    } else if (lastAmperage[15] < mAmperage) {
                        lastAmperage[i] = lastAmperage[15];
                        lastAmperage[15] = (int) mAmperage;
                    }
                }
            }

            if(mOverheat>=mMaxOverheat) {
                //TODO someday
                //int newMeta=aBaseMetaTileEntity.getMetaTileID()-6;
                //if(mInsulated &&
                //        GregTech_API.METATILEENTITIES[newMeta] instanceof GT_MetaPipeEntity_Cable &&
                //        ((GT_MetaPipeEntity_Cable)GregTech_API.METATILEENTITIES[newMeta]).mMaterial==mMaterial &&
                //        ((GT_MetaPipeEntity_Cable)GregTech_API.METATILEENTITIES[newMeta]).mAmperage<=mAmperage){
                //    aBaseMetaTileEntity.setOnFire();
                //    aBaseMetaTileEntity.setMetaTileEntity(GregTech_API.METATILEENTITIES[newMeta]);
                //    return;
                //}else{
                    aBaseMetaTileEntity.setToFire();
                //}
            }else if (mOverheat>0) mOverheat--;

            mTransferredVoltageOK=mTransferredVoltage;
            mTransferredVoltage=0;
            mTransferredAmperageOK=mTransferredAmperage;
            mTransferredAmperage = 0;

            if (aTick % 20 == 0) {
                mTransferredVoltageLast20OK=mTransferredVoltageLast20;
                mTransferredVoltageLast20 = 0;
                mTransferredAmperageLast20OK=mTransferredAmperageLast20;
                mTransferredAmperageLast20 = 0;
                for (byte i = 0, j = 0; i < 6; i++) {
                    if ((mCheckConnections || (mConnections & (1 << i)) == 0) && connect(i) == 0) disconnect(i);
                }
                if (GT_Mod.gregtechproxy.gt6Pipe) mCheckConnections = false;
            }
        }else if(aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected==4) aBaseMetaTileEntity.issueTextureUpdate();
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    	return onConnectionToolRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    	return onConnectionToolRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
    }

    private boolean onConnectionToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    	if (GT_Mod.gregtechproxy.gt6Cable) {
    		byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
    		if ((mConnections & (1 << tSide)) == 0) {
    			if (GT_Mod.gregtechproxy.costlyCableConnection && !GT_ModHandler.consumeSolderingMaterial(aPlayer)) return false;
    			if (connect(tSide) > 0)
    				GT_Utility.sendChatToPlayer(aPlayer, trans("214", "Connected"));
    		}
    		else {
    			disconnect(tSide);
    			GT_Utility.sendChatToPlayer(aPlayer, trans("215", "Disconnected"));
    		}
    		return true;
    	}
        return false;
    }

	@Override
	public int connect(byte aSide) {
		int rConnect = 0;
		if (aSide >= 6) return rConnect;
		byte tSide = GT_Utility.getOppositeSide(aSide);
		TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(aSide);
		if (tTileEntity != null) {
			if (getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).alwaysLookConnected(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), getBaseMetaTileEntity()) || getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), getBaseMetaTileEntity()) || getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).letsEnergyOut(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), getBaseMetaTileEntity())) {
	            if (tTileEntity instanceof IColoredTileEntity) {
	                if (getBaseMetaTileEntity().getColorization() >= 0) {
	                    byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
	                    if (tColor >= 0 && tColor != getBaseMetaTileEntity().getColorization())
	                    	return rConnect;
	                }
	            }
	            if ((tTileEntity instanceof IEnergyConnected && (((IEnergyConnected) tTileEntity).inputEnergyFrom(tSide) || ((IEnergyConnected) tTileEntity).outputsEnergyTo(tSide)))
	            		|| (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof IMetaTileEntityCable
	                    		&& (((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(tSide).alwaysLookConnected(tSide, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(tSide), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(tSide), ((IGregTechTileEntity) tTileEntity)) || ((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(tSide).letsEnergyIn(tSide, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(tSide), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(tSide), ((IGregTechTileEntity) tTileEntity)) || ((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(tSide).letsEnergyOut(tSide, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(tSide), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(tSide), ((IGregTechTileEntity) tTileEntity))))
	            		|| (tTileEntity instanceof IEnergySink && ((IEnergySink) tTileEntity).acceptsEnergyFrom((TileEntity) getBaseMetaTileEntity(), ForgeDirection.getOrientation(tSide)))
	            		|| (GregTech_API.mOutputRF && tTileEntity instanceof IEnergyReceiver && ((IEnergyReceiver) tTileEntity).canConnectEnergy(ForgeDirection.getOrientation(tSide)))
	            		/*|| (tTileEntity instanceof IEnergyEmitter && ((IEnergyEmitter)tTileEntity).emitsEnergyTo((TileEntity)getBaseMetaTileEntity(), ForgeDirection.getOrientation(tSide)))*/) {
	                rConnect = 1;
	            }
	        }
		} else if (getBaseMetaTileEntity().getOffsetX(aSide, 1) >> 4 != getBaseMetaTileEntity().getXCoord() >> 4 
    			|| getBaseMetaTileEntity().getOffsetZ(aSide, 1) >> 4 != getBaseMetaTileEntity().getZCoord() >> 4) { // if chunk unloaded
    		rConnect = -1;
		}
        if (rConnect > 0) {
        	super.connect(aSide);
        }
        return rConnect;
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
                "Max Voltage: %%%" + EnumChatFormatting.GREEN + mVoltage + " (" + VN[GT_Utility.getTier(mVoltage)] + ")" + EnumChatFormatting.GRAY,
                "Max Amperage: %%%" + EnumChatFormatting.YELLOW + mAmperage + EnumChatFormatting.GRAY,
                "Loss/Meter/Ampere: %%%" + EnumChatFormatting.RED + mCableLossPerMeter + EnumChatFormatting.GRAY + "%%% EU-Volt"
        };
    }

    @Override
    public float getThickNess() {
        if(GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) return 0.0625F;
        return mThickNess;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (GT_Mod.gregtechproxy.gt6Cable)
        	aNBT.setByte("mConnections", mConnections);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (GT_Mod.gregtechproxy.gt6Cable) {
        	if (!aNBT.hasKey("mConnections"))
        		mCheckConnections = true;
        	mConnections = aNBT.getByte("mConnections");
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                //EnumChatFormatting.BLUE + mName + EnumChatFormatting.RESET,
                "Heat: "+
                        EnumChatFormatting.RED+ mOverheat +EnumChatFormatting.RESET+" / "+EnumChatFormatting.YELLOW+ mMaxOverheat + EnumChatFormatting.RESET,
                "Max Load (1t):",
                EnumChatFormatting.GREEN + Integer.toString(mTransferredAmperageOK) + EnumChatFormatting.RESET +" A / "+
                        EnumChatFormatting.YELLOW + Long.toString(mAmperage) + EnumChatFormatting.RESET +" A",
                "Max EU/p (1t):",
                EnumChatFormatting.GREEN + Long.toString(mTransferredVoltageOK) + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + Long.toString(mVoltage) + EnumChatFormatting.RESET +" EU",
                "Max Load (20t): "+
                    EnumChatFormatting.GREEN + Integer.toString(mTransferredAmperageLast20OK) + EnumChatFormatting.RESET +" A",
                "Max EU/p (20t): "+
                    EnumChatFormatting.GREEN + Long.toString(mTransferredVoltageLast20OK) + EnumChatFormatting.RESET +" EU"
        };
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
    	if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0)
    		return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
    	else
    		return getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    }

    private AxisAlignedBB getActualCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
    	float tSpace = (1f - mThickNess)/2;
    	float tSide0 = tSpace;
    	float tSide1 = 1f - tSpace;
    	float tSide2 = tSpace;
    	float tSide3 = 1f - tSpace;
    	float tSide4 = tSpace;
    	float tSide5 = 1f - tSpace;
    	
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 0) != 0){tSide0=tSide2=tSide4=0;tSide3=tSide5=1;}
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 1) != 0){tSide2=tSide4=0;tSide1=tSide3=tSide5=1;}
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 2) != 0){tSide0=tSide2=tSide4=0;tSide1=tSide5=1;}
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 3) != 0){tSide0=tSide4=0;tSide1=tSide3=tSide5=1;}
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 4) != 0){tSide0=tSide2=tSide4=0;tSide1=tSide3=1;}
    	if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 5) != 0){tSide0=tSide2=0;tSide1=tSide3=tSide5=1;}
    	
    	byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
    	if((tConn & (1 << ForgeDirection.DOWN.ordinal()) ) != 0) tSide0 = 0f;
    	if((tConn & (1 << ForgeDirection.UP.ordinal())   ) != 0) tSide1 = 1f;
    	if((tConn & (1 << ForgeDirection.NORTH.ordinal())) != 0) tSide2 = 0f;
    	if((tConn & (1 << ForgeDirection.SOUTH.ordinal())) != 0) tSide3 = 1f;
    	if((tConn & (1 << ForgeDirection.WEST.ordinal()) ) != 0) tSide4 = 0f;
    	if((tConn & (1 << ForgeDirection.EAST.ordinal()) ) != 0) tSide5 = 1f;
    	
    	return AxisAlignedBB.getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB, List<AxisAlignedBB> outputAABB, Entity collider) {
    	super.addCollisionBoxesToList(aWorld, aX, aY, aZ, inputAABB, outputAABB, collider);
    	if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x2) != 0) {
    		AxisAlignedBB aabb = getActualCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
    		if (inputAABB.intersectsWith(aabb)) outputAABB.add(aabb);
    	}
    }
}
