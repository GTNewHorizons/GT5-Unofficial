package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.items.behaviors.Behaviour_None;
import gregtech.common.items.behaviors.Behaviour_Prospecting;
import gregtech.common.items.behaviors.Behaviour_Wrench;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Behaviour_Choocher
        extends Behaviour_None {
    private final Behaviour_Wrench wrench = new Behaviour_Wrench(150);
    private final Behaviour_Prospecting prospecting = new Behaviour_Prospecting(1, 1250);
    private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.choochering", "Wrench by default, Hold shift & Right click to prospect.");

    public Behaviour_Choocher() {
    	
    }

    @Override
	public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        if (!aPlayer.isSneaking()){
        	//Utils.LOG_INFO("Using Choocher as a wrench.");
        	wrench.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aSide, aSide, aSide, aSide, hitZ, hitZ, hitZ);
        	return false;
        }
    	//Utils.LOG_INFO("Using Choocher as a hard hammer.");
		prospecting.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
        
        return false;
    }

	public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
