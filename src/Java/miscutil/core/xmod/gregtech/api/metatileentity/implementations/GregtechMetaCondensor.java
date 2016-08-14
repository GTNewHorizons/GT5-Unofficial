package miscutil.core.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.math.MathUtils;
import miscutil.core.xmod.gregtech.api.gui.CONTAINER_SteamCondenser;
import miscutil.core.xmod.gregtech.api.gui.GUI_SteamCondenser;
import miscutil.core.xmod.gregtech.api.metatileentity.implementations.base.generators.GregtechMetaBoilerBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GregtechMetaCondensor extends GregtechMetaBoilerBase{

	 public GregtechMetaCondensor(int aID, String aName, String aNameRegional)
	  {
	    super(aID, aName, aNameRegional, "A Steam condenser - [IC2->Steam]", new ITexture[0]);
	  }
	  
	  public GregtechMetaCondensor(String aName, int aTier, String aDescription, ITexture[][][] aTextures)
	  {
	    super(aName, aTier, aDescription, aTextures);
	  }
	  
	  @Override
		public String[] getDescription() {
			return new String[] {mDescription, CORE.GT_Tooltip};
		}
	  
	  public ITexture[][][] getTextureSet(ITexture[] aTextures)
	  {
	    ITexture[][][] rTextures = new ITexture[5][17][];
	    for (byte i = -1; i < 16; i++){
	      rTextures[0][(i + 1)] = new ITexture [] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT, Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa))};
	      rTextures[1][(i + 1)] = new ITexture [] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT, Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) };
	      rTextures[2][(i + 1)] = new ITexture [] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT, Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) };
	      rTextures[3][(i + 1)] = new ITexture [] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT, Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER) };
	      rTextures[4][(i + 1)] = new ITexture [] { new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT, Dyes.getModulation(i, Dyes.MACHINE_METAL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_POTIONBREWER_ACTIVE) };
	    }
	    return rTextures;
	  }
	  
	  public int maxProgresstime()
	  {
	    return 1000;
	  }
	  
	  public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	  {
	    return new CONTAINER_SteamCondenser(aPlayerInventory, aBaseMetaTileEntity, 32000);
	  }
	  
	  public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity)
	  {
	    return new GUI_SteamCondenser(aPlayerInventory, aBaseMetaTileEntity, "SteelBoiler.png", 32000);
	  }
	  
	  public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity)
	  {
	    return new GregtechMetaCondensor(this.mName, this.mTier, this.mDescription, this.mTextures);
	  }
	  
	  public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick)
	  {
		  this.RI = MathUtils.randLong(5L, 30L);
	    if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L))
	    {
	      if (this.mTemperature <= 5)
	      {
	        this.mTemperature = 5;
	        this.mLossTimer = 0;
	      }
	      if (++this.mLossTimer > 10)
	      {
	        this.mTemperature -= 1;
	        this.mLossTimer = 0;
	      }
	      for (byte i = 1; (this.mSteam != null) && (i < 6); i = (byte)(i + 1)) {
	        if (i != aBaseMetaTileEntity.getFrontFacing())
	        {
	          IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
	          if (tTileEntity != null)
	          {
	            FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), Math.max(1, this.mSteam.amount / 2), false);
	            if (tDrained != null)
	            {
	              int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), tDrained, false);
	              if (tFilledAmount > 0) {
	                tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
	              }
	            }
	          }
	        }
	      }
	      if (aTick % 10L == 0L) {
	        if (this.mTemperature > 5)
	        {
	          if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0))
	          {
	            this.mHadNoWater = true;
	          }
	          else
	          {
	            if (this.mHadNoWater)
	            {
	              aBaseMetaTileEntity.doExplosion(2048L);
	              return;
	            }
	            this.mFluid.amount -= 1;
	            if (this.mSteam == null) {
	              this.mSteam = GT_ModHandler.getSteam(30L);
	            } else if (GT_ModHandler.isSteam(this.mSteam)) {
	              this.mSteam.amount += 30;
	            } else {
	              this.mSteam = GT_ModHandler.getSteam(30L);
	            }
	          }
	        }
	        else {
	          this.mHadNoWater = false;
	        }
	      }
	      if ((this.mSteam != null) && 
	        (this.mSteam.amount > 32000))
	      {
	        sendSound((byte)1);
	        this.mSteam.amount = 24000;
	      }
	      /*if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && 
	        (GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.bucket.get(IC2.getItemFromBlock(p_150898_0_)))))
	      {
	        this.mProcessingEnergy += 1000;
	        aBaseMetaTileEntity.decrStackSize(2, 1);
	        aBaseMetaTileEntity.addStackToSlot(3, GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L));
	      }*/	      
	      if ((this.mTemperature < 1000) && (this.mProcessingEnergy > 0) && (aTick % RI == 0L))
	      {
	        this.mProcessingEnergy -= 40;
	        this.mTemperature += 2;
	      }
	      aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
	    }
	  }
	  
	  public final int fill(FluidStack aFluid, boolean doFill)
	  {
	    if ((Utils.isIC2Steam(aFluid)) && (this.mProcessingEnergy < 50))
	    {
	      int tFilledAmount = Math.min(50, aFluid.amount);
	      if (doFill) {
	        this.mProcessingEnergy += tFilledAmount;
	      }
	      return tFilledAmount;
	    }
	    return super.fill(aFluid, doFill);
	  }
}
