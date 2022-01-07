package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.behaviors.Behaviour_None;
import gregtech.common.items.behaviors.Behaviour_Wrench;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class Behaviour_Choocher
extends Behaviour_None {
	private boolean isWrench = true;
	private final Behaviour_Wrench wrench = new Behaviour_Wrench(150);
	private final Behaviour_Prospecting_Ex prospecting = new Behaviour_Prospecting_Ex(10, 1250);
	private final String mTooltip1 = GT_LanguageManager.addStringLocalization("gt.behaviour.choochering1", "Current tool mode: ");
	private final String mTooltip2 = GT_LanguageManager.addStringLocalization("gt.behaviour.choochering2", "Change tool mode using Shift+Rightclick.");
	private final String mTooltipH = GT_LanguageManager.addStringLocalization("gt.behaviour.prospectingEx", "Usable for Prospecting large areas.");
	private final String mTooltipW = GT_LanguageManager.addStringLocalization("gt.behaviour.wrench", "Rotates Blocks on Rightclick.");

	public Behaviour_Choocher() {

	}

	@Override
	public boolean onItemUseFirst(final GT_MetaBase_Item aItem, final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX, final int aY, final int aZ, final int aSide, final float hitX, final float hitY, final float hitZ) {
		if (aWorld.isRemote) {
			return false;
		}
		
		boolean inWrenchMode;
		if (NBTUtils.hasKey(aStack, "aMode")) {
			inWrenchMode = NBTUtils.getBoolean(aStack, "aMode");
		}
		else {
			aStack.getTagCompound().setBoolean("aMode", true);
			inWrenchMode = true;
		}		
		
		if (aPlayer.isSneaking()){
			boolean aModeNew = Utils.invertBoolean(inWrenchMode);
			aStack.getTagCompound().setBoolean("aMode", aModeNew);
			PlayerUtils.messagePlayer(aPlayer, "Mode: "+(aModeNew ? "Wrench" : "Hammer"));	
			return true;
		}
		else {
			if (inWrenchMode){
				return this.wrench.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aSide, aSide, aSide, aSide, hitZ, hitZ, hitZ);
			}
			else {
				return this.prospecting.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);			
			}
		}
	}

	@Override
	public List<String> getAdditionalToolTips(final GT_MetaBase_Item aItem, final List<String> aList, final ItemStack aStack) {

		boolean inWrenchMode;
		if (NBTUtils.hasKey(aStack, "aMode")) {
			inWrenchMode = NBTUtils.getBoolean(aStack, "aMode");
		}
		else {
			NBTUtils.setBoolean(aStack, "aMode", true);
			aStack.getTagCompound().setBoolean("aMode", true);
			inWrenchMode = true;
		}	
		

		if (inWrenchMode){
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
