package gregtech.api.metatileentity.implementations;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import gregtech.common.covers.GT_Cover_Drain;
import gregtech.common.covers.GT_Cover_FluidRegulator;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.enums.GT_Values.D1;

public class GT_MetaPipeEntity_Fluid extends MetaPipeEntity {
    public final float mThickNess;
    public final Materials mMaterial;
    public final int mCapacity, mHeatResistance, mPipeAmount;
    public final boolean mGasProof;
    public final FluidStack[] mFluids;
    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;
    /**
     * Bitmask for whether disable fluid input form each side.
     */
    public byte mDisableInput = 0;

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
    	super(aID, aName, aNameRegional, 0, false);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
        
        addInfo(aID);
    }

    @Deprecated
    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[mPipeAmount];
    }

    @Override
    public byte getTileEntityBaseType() {
        return mMaterial == null ? 4 : (byte) ((mMaterial.contains(SubTag.WOOD) ? 12 : 4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Fluid(mName, mThickNess, mMaterial, mCapacity, mHeatResistance, mGasProof, mPipeAmount);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
    	float tThickNess = getThickNess();
    	if (mDisableInput == 0) return new ITexture[]{aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex) : new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
        byte tMask = 0;
        byte[][] sRestrictionArray = new byte[][]{
        	{2, 3, 5, 4},
        	{2, 3, 4, 5},
        	{1, 0, 4, 5},
        	{1, 0, 4, 5},
        	{1, 0, 2, 3},
        	{1, 0, 2, 3}
        };
        if (aSide >= 0 && aSide < 6) for (byte i = 0; i < 4; i++) if (isInputDisabledAtSide(sRestrictionArray[aSide][i])) tMask |= 1 << i;
        return new ITexture[]{aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex) : new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), getRestrictorTexture(tMask)};
    }

    protected static final ITexture getBaseTexture(float aThickNess, int aPipeAmount, Materials aMaterial, byte aColorIndex) {
    	if (aPipeAmount >= 9) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aPipeAmount >= 4) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aThickNess < 0.124F) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aThickNess < 0.374F) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aThickNess < 0.499F) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aThickNess < 0.749F) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	if (aThickNess < 0.874F) return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    	return new GT_RenderedTexture(aMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex], Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    }

    protected static final ITexture getRestrictorTexture(byte aMask) {
    	switch (aMask) {
    	case 1: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_UP);
    	case 2: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_DOWN);
    	case 3: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_UD);
    	case 4: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_LEFT);
    	case 5: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_UL);
    	case 6: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_DL);
    	case 7: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_NR);
    	case 8: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_RIGHT);
    	case 9: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_UR);
    	case 10: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_DR);
    	case 11: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_NL);
    	case 12: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_LR);
    	case 13: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_ND);
    	case 14: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR_NU);
    	case 15: return new GT_RenderedTexture(Textures.BlockIcons.PIPE_RESTRICTOR);
    	default: return null;
    	}
    }

    @Override
    public void onValueUpdate(byte aValue) {
    	mDisableInput = aValue;
    }

    @Override
    public byte getUpdateData() {
        return mDisableInput;
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
        return false;
    }

    @Override
    public final boolean renderInside(byte aSide) {
        return false;
    }

    @Override
    public int getProgresstime() {
        return getFluidAmount();
    }

    @Override
    public int maxProgresstime() {
        return getCapacity();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
    	for (int i = 0; i < mPipeAmount; i++)
    		if (mFluids[i] != null)
    			aNBT.setTag("mFluid"+(i==0?"":i), mFluids[i].writeToNBT(new NBTTagCompound()));
        aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
        if (GT_Mod.gregtechproxy.gt6Pipe) {
        	aNBT.setByte("mConnections", mConnections);
        	aNBT.setByte("mDisableInput", mDisableInput);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
    	for (int i = 0; i < mPipeAmount; i++)
    		mFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"+(i==0?"":i)));
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
        if (GT_Mod.gregtechproxy.gt6Pipe) {
        	mConnections = aNBT.getByte("mConnections");
        	mDisableInput = aNBT.getByte("mDisableInput");
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
        if ((((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0 && aEntity instanceof EntityLivingBase) {
        	for (FluidStack tFluid : mFluids) {
        		if (tFluid != null) {
        			int tTemperature = tFluid.getFluid().getTemperature(tFluid);
                    if (tTemperature > 320 && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                        GT_Utility.applyHeatDamage((EntityLivingBase) aEntity, (tTemperature - 300) / 50.0F); break;
                    } else if (tTemperature < 260 && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                        GT_Utility.applyFrostDamage((EntityLivingBase) aEntity, (270 - tTemperature) / 25.0F); break;
                    }
        		}
        	}
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % 5 == 0) {
            mLastReceivedFrom &= 63;
            if (mLastReceivedFrom == 63) {
                mLastReceivedFrom = 0;
            }

            if (!GT_Mod.gregtechproxy.gt6Pipe || mCheckConnections) checkConnections();

            boolean shouldDistribute = (oLastReceivedFrom == mLastReceivedFrom);
            for (int i = 0, j = aBaseMetaTileEntity.getRandomNumber(mPipeAmount); i < mPipeAmount; i++) {
                int index = (i + j) % mPipeAmount;
                if (mFluids[index] != null && mFluids[index].amount <= 0) mFluids[index] = null;
                if (mFluids[index] == null) continue;

                if (checkEnvironment(index, aBaseMetaTileEntity)) return;

                if (shouldDistribute) {
                    distributeFluid(index, aBaseMetaTileEntity);
                    mLastReceivedFrom = 0;
                }
            }

            oLastReceivedFrom = mLastReceivedFrom;

        } else if(aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected==4) aBaseMetaTileEntity.issueTextureUpdate();
    }

    private boolean checkEnvironment(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        // Check for hot liquids that melt the pipe or gasses that escape and burn/freeze people
        final FluidStack tFluid = mFluids[index];

                if (tFluid != null && tFluid.amount > 0) {
                    int tTemperature = tFluid.getFluid().getTemperature(tFluid);
                    if (tTemperature > mHeatResistance) {
                        if (aBaseMetaTileEntity.getRandomNumber(100) == 0) {
                    // Poof
                            aBaseMetaTileEntity.setToFire();
                    return true;
                        }
                // Mmhmm, Fire
                        aBaseMetaTileEntity.setOnFire();
                    }
                    if (!mGasProof && tFluid.getFluid().isGaseous(tFluid)) {
                        tFluid.amount -= 5;
                        sendSound((byte) 9);
                        if (tTemperature > 320) {
                            try {
                                for (EntityLivingBase tLiving : (ArrayList<EntityLivingBase>) getBaseMetaTileEntity().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(getBaseMetaTileEntity().getXCoord() - 2, getBaseMetaTileEntity().getYCoord() - 2, getBaseMetaTileEntity().getZCoord() - 2, getBaseMetaTileEntity().getXCoord() + 3, getBaseMetaTileEntity().getYCoord() + 3, getBaseMetaTileEntity().getZCoord() + 3))) {
                                    GT_Utility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
                                }
                            } catch (Throwable e) {
                                if (D1) e.printStackTrace(GT_Log.err);
                            }
                        } else if (tTemperature < 260) {
                            try {
                                for (EntityLivingBase tLiving : (ArrayList<EntityLivingBase>) getBaseMetaTileEntity().getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(getBaseMetaTileEntity().getXCoord() - 2, getBaseMetaTileEntity().getYCoord() - 2, getBaseMetaTileEntity().getZCoord() - 2, getBaseMetaTileEntity().getXCoord() + 3, getBaseMetaTileEntity().getYCoord() + 3, getBaseMetaTileEntity().getZCoord() + 3))) {
                                    GT_Utility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
                                }
                            } catch (Throwable e) {
                                if (D1) e.printStackTrace(GT_Log.err);
                            }
                        }
                    }
            if (tFluid.amount <= 0) mFluids[index] = null;
                }
        return false;
            }

    private void distributeFluid(int index, IGregTechTileEntity aBaseMetaTileEntity) {
        final FluidStack tFluid = mFluids[index];
        if (tFluid == null) return;

        // Tank, From, Amount to receive
        List<MutableTriple<IFluidHandler, ForgeDirection, Integer>> tTanks = new ArrayList<>();

        for (byte aSide, i = 0, j = (byte) aBaseMetaTileEntity.getRandomNumber(6); i < 6; i++) {
            // Get a list of tanks accepting fluids, and what side they're on
            aSide = (byte) ((i + j) % 6);
            final byte tSide = GT_Utility.getOppositeSide(aSide);
            final IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide(aSide);
            final IGregTechTileEntity gTank = tTank instanceof IGregTechTileEntity ? (IGregTechTileEntity) tTank : null;

            if (isConnectedAtSide(aSide) && tTank != null && (mLastReceivedFrom & (1 << aSide)) == 0 &&
                getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide).letsFluidOut(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getCoverDataAtSide(aSide), tFluid.getFluid(), getBaseMetaTileEntity()) &&
                (gTank == null || gTank.getCoverBehaviorAtSide(tSide).letsFluidIn(tSide, gTank.getCoverIDAtSide(tSide), gTank.getCoverDataAtSide(tSide), tFluid.getFluid(), gTank)))
            {
                if (tTank.fill(ForgeDirection.getOrientation(tSide), tFluid, false) > 0) {
                    tTanks.add(new MutableTriple<>(tTank, ForgeDirection.getOrientation(tSide), 0));
                                }
                        }
                    }

        // How much of this fluid is available for distribution?
        double tAmount = Math.max(1, Math.min(mCapacity * 10, tFluid.amount)), tNumTanks = tTanks.size();
        FluidStack maxFluid = tFluid.copy();
        maxFluid.amount = Integer.MAX_VALUE;

        double availableCapacity = 0;
        // Calculate available capacity for distribution from all tanks
        for (MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry: tTanks) {
            tEntry.right = tEntry.left.fill(tEntry.middle, maxFluid, false);
            availableCapacity += tEntry.right;
                        }

        // Now distribute
        for (MutableTriple<IFluidHandler, ForgeDirection, Integer> tEntry: tTanks) {
            if (availableCapacity > tAmount) tEntry.right = (int) Math.floor(tEntry.right * tAmount / availableCapacity);
            if (tEntry.right <= 0) continue;

            int tFilledAmount = tEntry.left.fill(tEntry.middle, drainFromIndex(tEntry.right, false, index), false);

            if (tFilledAmount > 0) tEntry.left.fill(tEntry.middle, drainFromIndex(tFilledAmount, true, index), true);
            }

    }

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    	if (GT_Mod.gregtechproxy.gt6Pipe) {
    		byte tSide = GT_Utility.determineWrenchingSide(aSide, aX, aY, aZ);
    		byte tMask = (byte) (1 << tSide);
    		if (aPlayer.isSneaking()) {
    			if (isInputDisabledAtSide(tSide)) {
    				mDisableInput &= ~tMask;
    				GT_Utility.sendChatToPlayer(aPlayer, trans("212", "Input enabled"));
    				if (!isConnectedAtSide(tSide))
            			connect(tSide);
    			} else {
    				mDisableInput |= tMask;
    				GT_Utility.sendChatToPlayer(aPlayer, trans("213", "Input disabled"));
    			}
    		} else {
    			if (!isConnectedAtSide(tSide)) {
    				if (connect(tSide) > 0)
    				GT_Utility.sendChatToPlayer(aPlayer, trans("214", "Connected"));
    			}
        		else {
        			disconnect(tSide);
        			GT_Utility.sendChatToPlayer(aPlayer, trans("215", "Disconnected"));
        		}
    		}
    		return true;
    	}
        return false;
    }

	@Override
    public boolean letsIn(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsFluidIn(aSide, aCoverID, aCoverVariable, null, aTileEntity);
    }

    @Override
    public boolean letsOut(GT_CoverBehavior coverBehavior, byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return coverBehavior.letsFluidOut(aSide, aCoverID, aCoverVariable, null, aTileEntity);
                }

    @Override
    public boolean canConnect(byte aSide, TileEntity tTileEntity) {
        if (tTileEntity == null) return false;

        final byte tSide = (byte)ForgeDirection.getOrientation(aSide).getOpposite().ordinal();
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        if (baseMetaTile == null) return false;

        final GT_CoverBehavior coverBehavior = baseMetaTile.getCoverBehaviorAtSide(aSide);
        final IGregTechTileEntity gTileEntity = (tTileEntity instanceof IGregTechTileEntity) ? (IGregTechTileEntity) tTileEntity : null;

        if (coverBehavior instanceof GT_Cover_Drain) return true;

        // Tinker Construct Faucets return a null tank info, so check the class
        if (GregTech_API.mTConstruct && tTileEntity instanceof tconstruct.smeltery.logic.FaucetLogic) return true;

        final IFluidHandler fTileEntity = (tTileEntity instanceof IFluidHandler) ? (IFluidHandler) tTileEntity : null;

        if (fTileEntity != null) {
            FluidTankInfo[] tInfo = fTileEntity.getTankInfo(ForgeDirection.getOrientation(tSide));
            if (tInfo != null) {
                if (tInfo.length > 0) return true;

                // Translocators return a TankInfo, but it's of 0 length - so check the class if we see this pattern
                if (GregTech_API.mTranslocator  && tTileEntity instanceof codechicken.translocator.TileLiquidTranslocator) return true;
                if (gTileEntity != null && gTileEntity.getCoverBehaviorAtSide(tSide) instanceof GT_Cover_FluidRegulator) return true;

            }
        }
        return false;
    }

    @Override
    public boolean getGT6StyleConnection() {
        // Yes if GT6 pipes are enabled
        return GT_Mod.gregtechproxy.gt6Pipe;
	}

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(4), 5, 1.0F, aX, aY, aZ);
            for (byte i = 0; i < 6; i++)
                for (int l = 0; l < 2; ++l)
                    getBaseMetaTileEntity().getWorld().spawnParticle("largesmoke", aX - 0.5 + (new XSTR()).nextFloat(), aY - 0.5 + (new XSTR()).nextFloat(), aZ - 0.5 + (new XSTR()).nextFloat(), ForgeDirection.getOrientation(i).offsetX / 5.0, ForgeDirection.getOrientation(i).offsetY / 5.0, ForgeDirection.getOrientation(i).offsetZ / 5.0);
        }
    }

    @Override
    public final int getCapacity() {
        return mCapacity * 20 * mPipeAmount;
    }

    @Override
    public FluidTankInfo getInfo() {
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			return new FluidTankInfo(tFluid, mCapacity * 20);
    	}
    	return new FluidTankInfo(null, mCapacity * 20);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aSide) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[]{};
        ArrayList<FluidTankInfo> tList = new ArrayList<>();
        for (FluidStack tFluid : mFluids)
        	tList.add(new FluidTankInfo(tFluid, mCapacity * 20));
        return tList.toArray(new FluidTankInfo[mPipeAmount]);
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
    public final FluidStack getFluid() {
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			return tFluid;
    	}
        return null;
    }

    @Override
    public final int getFluidAmount() {
    	int rAmount = 0;
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			rAmount += tFluid.amount;
    	}
        return rAmount;
    }

    @Override
    public final int fill_default(ForgeDirection aSide, FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid().getID() <= 0) return 0;

        int index = -1;
        for (int i = 0; i < mPipeAmount; i++) {
        	if (mFluids[i] != null && mFluids[i].isFluidEqual(aFluid)) {
        		index = i; break;
        	}
        	else if ((mFluids[i] == null || mFluids[i].getFluid().getID() <= 0) && index < 0) {
        		index = i;
        	}
        }
        
        return fill_default_intoIndex(aSide, aFluid, doFill, index);
    }

    private final int fill_default_intoIndex(ForgeDirection aSide, FluidStack aFluid, boolean doFill, int index) {
    	if (index < 0 || index >= mPipeAmount) return 0;
    	if (aFluid == null || aFluid.getFluid().getID() <= 0) return 0;

        if (mFluids[index] == null || mFluids[index].getFluid().getID() <= 0) {
            if (aFluid.amount * mPipeAmount <= getCapacity()) {
                if (doFill) {
                    mFluids[index] = aFluid.copy();
                    mLastReceivedFrom |= (1 << aSide.ordinal());
                }
                return aFluid.amount;
            }
            if (doFill) {
                mFluids[index] = aFluid.copy();
                mLastReceivedFrom |= (1 << aSide.ordinal());
                mFluids[index].amount = getCapacity() / mPipeAmount;
            }
            return getCapacity() / mPipeAmount;
        }

        if (!mFluids[index].isFluidEqual(aFluid)) return 0;

        int space = getCapacity() / mPipeAmount - mFluids[index].amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                mFluids[index].amount += aFluid.amount;
                mLastReceivedFrom |= (1 << aSide.ordinal());
            }
            return aFluid.amount;
        }
        if (doFill) {
            mFluids[index].amount = getCapacity() / mPipeAmount;
            mLastReceivedFrom |= (1 << aSide.ordinal());
        }
        return space;
    }

    @Override
    public final FluidStack drain(int maxDrain, boolean doDrain) {
    	FluidStack drained = null;
    	for (int i = 0; i < mPipeAmount; i++) {
    		if ((drained = drainFromIndex(maxDrain, doDrain, i)) != null)
    			return drained;
    	}
    	return null;
    }

    private final FluidStack drainFromIndex(int maxDrain, boolean doDrain, int index) {
    	if (index < 0 || index >= mPipeAmount) return null;
    	if (mFluids[index] == null) return null;
        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
            return null;
        }

        int used = maxDrain;
        if (mFluids[index].amount < used)
            used = mFluids[index].amount;

        if (doDrain) {
            mFluids[index].amount -= used;
        }

        FluidStack drained = mFluids[index].copy();
        drained.amount = used;

        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
        }

        return drained;
    }

    @Override
    public int getTankPressure() {
        return getFluidAmount() - (getCapacity() / 2);
    }

    @Override
    public String[] getDescription() {
        if (mPipeAmount == 1) {
        	return new String[]{
                    EnumChatFormatting.BLUE + "Fluid Capacity: %%%" + (mCapacity * 20) + "%%% L/sec" + EnumChatFormatting.GRAY,
                    EnumChatFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + EnumChatFormatting.GRAY
            };
        } else {
        	return new String[]{
                    EnumChatFormatting.BLUE + "Fluid Capacity: %%%" + (mCapacity * 20) + "%%% L/sec" + EnumChatFormatting.GRAY,
                    EnumChatFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + EnumChatFormatting.GRAY,
                    EnumChatFormatting.AQUA + "Pipe Amount: %%%" + mPipeAmount + EnumChatFormatting.GRAY
            };
        }
    }

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && (GT_Client.hideValue & 0x1) != 0) return 0.0625F;
        return mThickNess;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
    	return !isInputDisabledAtSide(aSide);
    }

    @Override
    public boolean isLiquidOutput(byte aSide) {
    	return true;
    }

    public boolean isInputDisabledAtSide(int aSide) {
    	return (mDisableInput & (1 << aSide)) != 0;
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
