package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.behaviors.Behaviour_None;
import gregtech.common.items.behaviors.Behaviour_Wrench;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Behaviour_Choocher
extends Behaviour_None {
	private boolean isWrench = true;
	private final Behaviour_Wrench wrench = new Behaviour_Wrench(150);
	private final Behaviour_Prospecting_Ex prospecting = new Behaviour_Prospecting_Ex(10, 1250);
	private String mTooltip1 = GT_LanguageManager.addStringLocalization("gt.behaviour.choochering1", "Current tool mode: ");
	private String mTooltip2 = GT_LanguageManager.addStringLocalization("gt.behaviour.choochering2", "Change tool mode using Shift+Rightclick.");
	private final String mTooltipH = GT_LanguageManager.addStringLocalization("gt.behaviour.prospectingEx", "Usable for Prospecting large areas.");
	private final String mTooltipW = GT_LanguageManager.addStringLocalization("gt.behaviour.wrench", "Rotates Blocks on Rightclick.");

	public Behaviour_Choocher() {

	}

	@Override
	public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		if (aWorld.isRemote) {
			return false;
		}
		if (aPlayer.isSneaking()){
			if (isWrench){
				isWrench = false;
				return false;
			}
			isWrench = true; 
			return false;
		}
		else if (!aPlayer.isSneaking()){
			if (isWrench){
				wrench.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aSide, aSide, aSide, aSide, hitZ, hitZ, hitZ);
				return false;
			}
			prospecting.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
			return false;
		}
		return false;
	}

	@Override
	public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {

		  
		if (isWrench){
			aList.add(this.mTooltip1+"Wrench");   
			aList.add(this.mTooltipW);
		}
		else {
			aList.add(this.mTooltip1+"Prospecting");   
			aList.add(this.mTooltipH);
		}
		aList.add(this.mTooltip2);

		return aList;
	}
}
