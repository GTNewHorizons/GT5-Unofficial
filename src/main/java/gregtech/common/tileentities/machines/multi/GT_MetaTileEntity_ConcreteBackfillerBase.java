package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GT_Values.VN;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class GT_MetaTileEntity_ConcreteBackfillerBase extends GT_MetaTileEntity_DrillerBase {
	
	private int mLastXOff = 0, mLastZOff = 0;
	
	public GT_MetaTileEntity_ConcreteBackfillerBase(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}
	
	public GT_MetaTileEntity_ConcreteBackfillerBase(String aName) {
        super(aName);
    }

	protected String[] getDescriptionInternal(String tierSuffix) {
		String casings = getCasingBlockItem().get(0).getDisplayName();
		return new String[]{
                "Controller Block for the Concrete Backfiller " + (tierSuffix != null ? tierSuffix : ""),
                "Size(WxHxD): 3x7x3", "Controller (Front middle at bottom)",
                "3x1x3 Base of " + casings,
                "1x3x1 " + casings + " pillar (Center of base)",
                "1x3x1 " + getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)",
                "1x Input Hatch (One of base casings)",
                "1x Maintenance Hatch (One of base casings)",
                "1x " + VN[getMinTier()] + "+ Energy Hatch (Any bottom layer casing)",
                "Radius is " + getRadius() + " blocks"};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "DrillingRig.png");
	}
	
	protected abstract int getRadius();

	@Override
	protected boolean checkHatches() {
		return !mMaintenanceHatches.isEmpty() && !mInputHatches.isEmpty() && !mEnergyHatches.isEmpty();
	}

	@Override
	protected void setElectricityStats() {
		this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -6 * (1 << (tier << 1));
        this.mMaxProgresstime = (workState == STATE_UPWARD ? 240 : 80) / (1 << tier);
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);		
	}
	
	@Override
	protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
		if (isRefillableBlock(xPipe, yHead - 1, zPipe))
			return tryRefillBlock(xPipe, yHead - 1, zPipe);
		int radius = getRadius();
		if (mLastXOff == 0 && mLastZOff == 0) {
			mLastXOff = - radius;
			mLastZOff = - radius;
		}
		if (yHead != yDrill) {
			for (int i = mLastXOff; i <= radius; i++) {
				for (int j = (i == mLastXOff ? mLastZOff : - radius); j <= radius; j++) {
					if (isRefillableBlock(xPipe + i, yHead, zPipe + j)){
						mLastXOff = i;
						mLastZOff = j;
						return tryRefillBlock(xPipe + i, yHead, zPipe + j);
					}
				}
			}
		}
		
		if (tryPickPipe()) {
			mLastXOff = 0;
			mLastZOff = 0;
            return true;
        } else {
            workState = STATE_DOWNWARD;
            stopMachine();
            return false;
        }
	}
    
	private boolean isRefillableBlock(int aX, int aY, int aZ){
		IGregTechTileEntity aBaseTile = getBaseMetaTileEntity();
		if (!aBaseTile.getBlock(aX, aY, aZ).isAir(aBaseTile.getWorld(), aX, aY, aZ) || aBaseTile.getBlock(aX, aY, aZ).getMaterial().isSolid()) return false;
		if (!GT_Utility.setBlockByFakePlayer(getFakePlayer(aBaseTile), aX, aY, aZ, GregTech_API.sBlockConcretes, 8, true)) return false;
		return true;
	}
	
	private boolean tryRefillBlock(int aX, int aY, int aZ) {
		if (!tryConsumeFluid()) return false;
		getBaseMetaTileEntity().getWorld().setBlock(aX, aY, aZ, GregTech_API.sBlockConcretes, 8, 3);
		return true;
	}
	
	private boolean tryConsumeFluid() {
        if (!depleteInput(Materials.Concrete.getMolten(144L))){
        	mMaxProgresstime = 0;
        	return false;
        }
        return true;
    }

}
