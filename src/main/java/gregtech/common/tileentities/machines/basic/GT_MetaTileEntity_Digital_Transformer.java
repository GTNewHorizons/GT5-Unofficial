package gregtech.common.tileentities.machines.basic;

import gregtech.common.gui.GT_Container_DigitalTransformer;
import gregtech.common.gui.GT_GUIContainer_DigitalTransformer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;

	
public class GT_MetaTileEntity_Digital_Transformer extends GT_MetaTileEntity_Transformer {
	    public int EUT=0,AMP=0;
	    public boolean producing=true;

	    public GT_MetaTileEntity_Digital_Transformer(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
			super(aID, aName, aNameRegional, aTier, aDescription);
		}

		public GT_MetaTileEntity_Digital_Transformer(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
			super(aName, aTier, aDescription, aTextures);
		}

		@Override
	    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
	        return new GT_MetaTileEntity_Digital_Transformer(mName, mTier, mDescription, mTextures);
	    }

		public ITexture[][][] getTextureSet(ITexture[] aTextures) {
	        ITexture[][][] rTextures = new ITexture[12][17][];
	        for (byte i = -1; i < 16; i++) {
	            rTextures[0][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	            rTextures[1][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	            rTextures[2][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	            rTextures[3][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier]};
	            rTextures[4][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier]};
	            rTextures[5][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier]};
	            rTextures[6][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier]};
	            rTextures[7][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier]};
	            rTextures[8][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier]};
	            rTextures[9][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	            rTextures[10][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	            rTextures[11][i + 1] = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.IDSU), new GT_RenderedTexture(Textures.BlockIcons.IDSU_Out)};
	        }
	        return rTextures;
	    }
	    
		@Override
	    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
	        return new GT_Container_DigitalTransformer(aPlayerInventory, aBaseMetaTileEntity);
	    }

	    @Override
	    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
	        return new GT_GUIContainer_DigitalTransformer(aPlayerInventory, aBaseMetaTileEntity);
	    }

	    @Override
	    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
	        return false;
	    }

	    @Override
	    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
	        return false;
	    }

	    @Override
	    public void saveNBTData(NBTTagCompound aNBT) {
	        aNBT.setInteger("eEUT",EUT);
	        aNBT.setInteger("eAMP",AMP);
	    }

	    @Override
	    public void loadNBTData(NBTTagCompound aNBT) {
	        EUT=aNBT.getInteger("eEUT");
	        AMP=aNBT.getInteger("eAMP");
	        producing=(long)AMP*EUT>=0;
	        getBaseMetaTileEntity().setActive(producing);
	    }
	    
	    @Override
	    public long maxEUInput() {
	    	return ((getBaseMetaTileEntity().isAllowedToWork()) ?V[mTier]:Math.abs(EUT));
	    }

	    @Override
	    public long maxEUOutput() {
	    	return ((getBaseMetaTileEntity().isAllowedToWork()) ? Math.abs(EUT):V[mTier]);
	    }
	    
	    /*@Override
	    public long maxAmperesIn() {
	        return producing?0:Math.abs(AMP);
	    }
	    
	    @Override
	    public long maxAmperesOut() {
	        return producing?Math.abs(AMP):0;
	    }*/
	    
	    @Override
		public long maxAmperesOut() {
			
			return ((getBaseMetaTileEntity().isAllowedToWork()) ? Math.abs(AMP) : 1L);
		}

		@Override
		public long maxAmperesIn() {
			
			return ((getBaseMetaTileEntity().isAllowedToWork()) ? 1L : Math.abs(AMP));
		}
	   
	    @Override
	    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
	        if (aBaseMetaTileEntity.isClientSide()) {
	            return true;
	        }
	        aBaseMetaTileEntity.openGUI(aPlayer);
	        return true;
	    }

	    @Override
		public String[] getDescription() {
			return new String[] {"Accepts " + V[mTier] + "V and outputs 0 - MAXV"};
		}

	    @Override
	    public boolean isElectric() {
	        return true;
	    }

	    @Override
		public long maxEUStore() {
			return 8192L + V[mTier + 1] * 64L;
		}
	    @Override
	    public int getProgresstime() {
	        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
	    }

	    @Override
	    public int maxProgresstime() {
	        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
	    }
	    
	}
