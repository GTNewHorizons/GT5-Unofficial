package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import java.util.Locale;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.item.ItemStack;

public abstract class CustomMetaTileBase extends MetaTileEntity {

	public CustomMetaTileBase(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
		super(aID, aBasicName, aRegionalName, aInvSlotCount);
        GT_LanguageManager.addStringLocalization("gtpp.blockmachines." + aBasicName.replaceAll(" ", "_").toLowerCase(Locale.ENGLISH) + ".name", aRegionalName);
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
		return GT_LanguageManager.getTranslation("gtpp.blockmachines." + this.mName + ".name");
	}
	
	/**
     * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
     * <p/>
     * 0 = BaseMetaTileEntity, Wrench lvl 0 to dismantlee
     * 1 = BaseMetaTileEntity, Wrench lvl 1 to dismantle
     * 2 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
     * 3 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
     * 4 = BaseMetaPipeEntity, Wrench lvl 0 to dismantle
     * 5 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle
     * 6 = BaseMetaPipeEntity, Wrench lvl 2 to dismantle
     * 7 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle
     * 8 = BaseMetaPipeEntity, Cutter lvl 0 to dismantle
     * 9 = BaseMetaPipeEntity, Cutter lvl 1 to dismantle
     * 10 = BaseMetaPipeEntity, Cutter lvl 2 to dismantle
     * 11 = BaseMetaPipeEntity, Cutter lvl 3 to dismantle
     * 
     * == Reserved for Alk now
     * 
     * 12 = BaseMetaPipeEntity, Wrench lvl 0 to dismantle
     * 13 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle
     * 14 = BaseMetaPipeEntity, Wrench lvl 2 to dismantle
     * 15 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle
     */
	@Override
    public byte getTileEntityBaseType() {
		return 12;
	}
	
}