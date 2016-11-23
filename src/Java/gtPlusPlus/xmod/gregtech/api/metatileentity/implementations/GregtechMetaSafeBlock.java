package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SafeBlock;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SafeBlock;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaSafeBlockBase;
import net.minecraft.entity.player.InventoryPlayer;

public class GregtechMetaSafeBlock
        extends GregtechMetaSafeBlockBase {
	
	@Override
	public String[] getDescription() {
		return new String[] {mDescription, CORE.GT_Tooltip};
	}
	
    public GregtechMetaSafeBlock(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 28, "Protecting your items from sticky fingers.");
    }

    public GregtechMetaSafeBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GregtechMetaSafeBlock(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaSafeBlock(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
    }

    @Override
	public ITexture getOverlayIcon() {
        return new GT_RenderedTexture(Textures.BlockIcons.VOID);
    }

    @Override
	public boolean isValidSlot(int aIndex) {
        return aIndex < this.mInventory.length - 1;
    }

    @Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new CONTAINER_SafeBlock(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GUI_SafeBlock(aPlayerInventory, aBaseMetaTileEntity);
    }
}
