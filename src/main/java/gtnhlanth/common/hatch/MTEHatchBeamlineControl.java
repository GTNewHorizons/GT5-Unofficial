package gtnhlanth.common.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IRedstoneReceiver;
import gregtech.api.interfaces.tileentity.IRedstoneTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import net.minecraftforge.common.util.ForgeDirection;

public class MTEHatchBeamlineControl extends MTEHatch {
	
	private static final String activeIconPath = "iconsets/OVERLAY_BC_ACTIVE";
    private static final String sideIconPath = "iconsets/OVERLAY_BC_SIDES";

    private static final Textures.BlockIcons.CustomIcon activeIcon = new Textures.BlockIcons.CustomIcon(activeIconPath);
    private static final Textures.BlockIcons.CustomIcon sideIcon = new Textures.BlockIcons.CustomIcon(sideIconPath);
    
    public MTEHatchBeamlineControl(int id, String name, String nameRegional) {
    	super(id, name, nameRegional, 6, 0, 
    			"Redstone Input for Beamline Multis");
    }
    
    public MTEHatchBeamlineControl(String name, String[] desc, ITexture[][][] textures) {
    	super(name, 0, 0, desc, textures);
    }

	@Override
	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[] { aBaseTexture,
	            TextureFactory
	                .of(activeIcon, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA()))};
	}

	@Override
	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		
		return new ITexture[] { aBaseTexture,
	            TextureFactory
	                .of(sideIcon, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA()))};
		
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new MTEHatchBeamlineControl(this.mName, this.mDescriptionArray, this.mTextures);
	}
	
	public byte getRedstoneInput() {
		
		byte redstoneInput = 0;
		
		ForgeDirection front = this.getBaseMetaTileEntity().getFrontFacing();
		
		redstoneInput = (byte) Math.max(redstoneInput, this.getBaseMetaTileEntity().getInternalInputRedstoneSignal(front));

		return redstoneInput;
	}
	
	private boolean isActive() {
		
		return this.getRedstoneInput() > 0;
		
	}
	
	@Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTimer % 20 == 0) {
                this.getBaseMetaTileEntity().setActive(isActive());
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }
	
	@Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }
	
	@Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }
}
