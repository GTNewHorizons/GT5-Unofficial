package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.item.ItemStack;

public abstract class CustomMetaTileBase extends MetaTileEntity {
	private IGregTechTileEntity mBaseMetaTileEntity;

	public CustomMetaTileBase(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
		super(aID, aBasicName, aRegionalName, aInvSlotCount);
				this.setBaseMetaTileEntity(Meta_GT_Proxy.constructBaseMetaTileEntity());
				this.getBaseMetaTileEntity().setMetaTileID((short) aID);				
	}

	public CustomMetaTileBase(String aName, int aInvSlotCount) {
		super(aName, aInvSlotCount);
	}

	public ItemStack getStackForm(long aAmount) {
		return new ItemStack(Meta_GT_Proxy.sBlockMachines, (int) aAmount, this.getBaseMetaTileEntity().getMetaTileID());
	}

	public String getLocalName() {
		return GT_LanguageManager.getTranslation("gt.blockmachines." + this.mName + ".name");
	}
	
}