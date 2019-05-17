package gtPlusPlus.core.util.minecraft;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.item.ItemStack;

public class EnergyUtils {

	public static class EU {
		
		public static boolean isElectricItem(ItemStack aStack) {			
			if (aStack.getItem() instanceof ISpecialElectricItem) {
				return true;
			}				
			else if (aStack.getItem() instanceof IElectricItem) {
				return true;
			}				
			else if (aStack.getItem() instanceof IElectricItemManager) {
				return true;
			}
			else {
				return GT_ModHandler.isElectricItem(aStack);				
			}			
		}
		
		public static boolean isChargerItem(ItemStack aStack) {
			return GT_ModHandler.isChargerItem(aStack);
		}
		
		public static boolean charge(ItemStack aStack, int aEnergyToInsert, int aTier) {
			return 0 != GT_ModHandler.chargeElectricItem(aStack, aEnergyToInsert, aTier, true, false);
		}
		
		public static boolean discharge(ItemStack aStack, int aEnergyToDrain, int aTier) {
			if (isElectricItem(aStack)) {
				int tTier = ((IElectricItem) aStack.getItem()).getTier(aStack);
				int aDischargeValue = GT_ModHandler.dischargeElectricItem(aStack, aEnergyToDrain, tTier, true, false, false);	
				//Logger.INFO("Trying to drain "+aDischargeValue);
				return aDischargeValue > 0;
			}
			else {
				return false;
			}
		}
		
		public static long getMaxStorage(ItemStack aStack) {			
			if (isElectricItem(aStack)) {				
				if (aStack.getItem() instanceof ISpecialElectricItem) {
					ISpecialElectricItem bStack = (ISpecialElectricItem) aStack.getItem();
					return (long) bStack.getMaxCharge(aStack);
				}				
				if (aStack.getItem() instanceof IElectricItem) {
					IElectricItem bStack = (IElectricItem) aStack.getItem();
					return (long) bStack.getMaxCharge(aStack);
				}				
				if (aStack.getItem() instanceof IElectricItemManager) {
					IElectricItemManager bStack = (IElectricItemManager) aStack.getItem();
					return (long) bStack.getCharge(aStack);
				}
			}
			else {
				return 0;
			}
			return 0;
		}
		
		public static long getCharge(ItemStack aStack) {
			if (isElectricItem(aStack)) {				
				if (aStack.getItem() instanceof ISpecialElectricItem) {
					ISpecialElectricItem bStack = (ISpecialElectricItem) aStack.getItem();
					return (long) bStack.getManager(aStack).getCharge(aStack);
				}			
				if (aStack.getItem() instanceof IElectricItemManager) {
					IElectricItemManager bStack = (IElectricItemManager) aStack.getItem();
					return (long) bStack.getCharge(aStack);
				}
			}
			else {
				return 0;
			}
			return 0;
		}
		
		public static boolean hasCharge(ItemStack aStack) {
			if (isElectricItem(aStack)) {				
				if (aStack.getItem() instanceof ISpecialElectricItem) {
					ISpecialElectricItem bStack = (ISpecialElectricItem) aStack.getItem();
					return bStack.canProvideEnergy(aStack);
				}				
				if (aStack.getItem() instanceof IElectricItem) {
					IElectricItem bStack = (IElectricItem) aStack.getItem();
					return bStack.canProvideEnergy(aStack);
				}				
				if (aStack.getItem() instanceof IElectricItemManager) {
					IElectricItemManager bStack = (IElectricItemManager) aStack.getItem();
					return bStack.getCharge(aStack) > 0;
				}
			}
			else {
				return false;
			}
			return false;
		}
		
		public static int getTier(ItemStack aStack) {
			if (isElectricItem(aStack)) {				
				if (aStack.getItem() instanceof ISpecialElectricItem) {
					ISpecialElectricItem bStack = (ISpecialElectricItem) aStack.getItem();
					return bStack.getTier(aStack);
				}				
				if (aStack.getItem() instanceof IElectricItem) {
					IElectricItem bStack = (IElectricItem) aStack.getItem();
					return bStack.getTier(aStack);
				}
			}
			else {
				return 0;
			}
			return 0;
		}
		
	}
	
	public static class RF {
		
	}
	
}
