package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Pollution;
import gregtech.common.gui.GT_Container_Boiler;
import gregtech.common.gui.GT_GUIContainer_Boiler;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_Boiler_Base
        extends GT_MetaTileEntity_Boiler {
			
			private int mSteamPerSecond = 0;
			private int mPollutionPerSecond = 20;
			private int mBoilerTier = 0;
			
    public GT_MetaTileEntity_Boiler_Base(int aID, String aNameRegional, int aBoilerTier) {
        super(aID, "electricboiler."+aBoilerTier+".tier.single", aNameRegional, new String[]{
                "Steam Power! Hi-Ho!",
                "Produces "+(750+(250*aBoilerTier))+"L of Steam per second",
                "Causes "+(20*aBoilerTier)+" Pollution per second"});
				mSteamPerSecond = (750+(250*aBoilerTier));
				mPollutionPerSecond = 20+(15*aBoilerTier);
				mBoilerTier = aBoilerTier;
    }

    public GT_MetaTileEntity_Boiler_Base(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Base(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(i + 1)]};
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(i + 1)], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE)};
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(i + 1)], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE)};
            rTextures[2][(i + 1)] = tmp2;
            ITexture[] tmp4 = {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(i + 1)], new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT)};
            rTextures[3][(i + 1)] = tmp4;
            ITexture[] tmp5 = {Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(i + 1)], new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT_ACTIVE)};
            rTextures[4][(i + 1)] = tmp5;
        }
        return rTextures;
    }

	//Please find out what I do.
	//I do stuff within the GUI. 
	//this.mTemperature = Math.min(54, Math.max(0, this.mTemperature * 54 / (((GT_MetaTileEntity_Boiler) this.mTileEntity.getMetaTileEntity()).maxProgresstime() - 10)));
    @Override
	public int maxProgresstime() {
        return 1000+(250*mBoilerTier);
    }
	
	//Electric boiler? Okay.
	@Override
	public boolean isElectric(){
		return true;
	}
	
	//Hold more Steam
	@Override
	public int getCapacity() {
        return (16000+(16000*mBoilerTier));
    }
	
	//We want automation.
	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return true;
    }

	//We want automation.
    @Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return true;
    }

    @Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Boiler(aPlayerInventory, aBaseMetaTileEntity, getCapacity());
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Boiler(aPlayerInventory, aBaseMetaTileEntity, "AdvancedBoiler.png", getCapacity());
    }

    @Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Base(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
            if (this.mTemperature <= 20) {
                this.mTemperature = 20;
                this.mLossTimer = 0;
            }
            if (++this.mLossTimer > 40) {
                this.mTemperature -= 1;
                this.mLossTimer = 0;
            }
            for (byte i = 1; (this.mSteam != null) && (i < 6); i = (byte) (i + 1)) {
                if (i != aBaseMetaTileEntity.getFrontFacing()) {
                    IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
                    if (tTileEntity != null) {
                        FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), Math.max(1, this.mSteam.amount / 2), false);
                        if (tDrained != null) {
                            int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), tDrained, false);
                            if (tFilledAmount > 0) {
                                tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
                            }
                        }
                    }
                }
            }
            if (aTick % 10L == 0L) {
                if (this.mTemperature > 100) {
                    if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
                        this.mHadNoWater = true;
                    } else {
                        if (this.mHadNoWater) {
                            aBaseMetaTileEntity.doExplosion(4096L);
                            return;
                        }
                        this.mFluid.amount -= 1;
                        if (this.mSteam == null) {
                            this.mSteam = GT_ModHandler.getSteam((this.mSteamPerSecond/2));
                        } else if (GT_ModHandler.isSteam(this.mSteam)) {
                            this.mSteam.amount += (this.mSteamPerSecond/2);
                        } else {
                            this.mSteam = GT_ModHandler.getSteam((this.mSteamPerSecond/2));
                        }
                    }
                } else {
                    this.mHadNoWater = false;
                }
            }
            if ((this.mSteam != null) &&
                    (this.mSteam.amount > (getCapacity()*2))) {
                sendSound((byte) 1);
                this.mSteam.amount = (getCapacity()+(getCapacity()/2));
            }
			ItemStack fuelSlot = this.mInventory[2];
            if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) &&
                    (fuelSlot != null)) {
               
			   if (isInputFuelItem(fuelSlot)){
					useInputFuelItem(fuelSlot);
			   }
			   
			   
            if ((this.mTemperature < maxProgresstime()) && (this.mProcessingEnergy > 0) && (aTick % 12L == 0L)) {
                this.mProcessingEnergy -= 2;
                this.mTemperature += 1;
            }
            if (this.mProcessingEnergy > 0 && (aTick % 20L == 0L)) {
                GT_Pollution.addPollution(getBaseMetaTileEntity(), this.mPollutionPerSecond);
            }
            aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
        }
    }
	
	public boolean isInputFuelItem(ItemStack inputItem){
		private int vCurrentBurnTime = 0;
		vCurrentBurnTime = net.minecraftforge.event.ForgeEventFactory.getFuelBurnTime(inputItem);
		if (vCurrentBurnTime > 0){
			return true;
		}
		return false;
	}
	
	public boolean useInputFuelItem(ItemStack inputItem){
		private int vCurrentBurnTime = 0;
		vCurrentBurnTime = net.minecraftforge.event.ForgeEventFactory.getFuelBurnTime(inputItem);
		if (vCurrentBurnTime > 0){
			this.mProcessingEnergy += (vCurrentBurnTime/10);
			aBaseMetaTileEntity.decrStackSize(2, 1);
			if (aBaseMetaTileEntity.getRandomNumber(3) == 0) {
				if (inputItem.getDisplayName().toLowercase().contains("charcoal") || inputItem.getDisplayName().toLowercase().contains("coke")){
					aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1L));
				}
				else {
					aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L));
				}
			}
				return true;
		}
		else {
			return false;
		}
	}
}