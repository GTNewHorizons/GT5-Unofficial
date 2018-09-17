package gtPlusPlus;

import java.util.LinkedHashMap;

import com.google.common.base.Objects;

import gtPlusPlus.api.objects.data.ObjMap;
import net.minecraft.item.ItemStack;

/**
 * This Class purely exists to note down ideas and or plans to (re)implement things.
 * 
 * @author Alkalus
 *
 */
public class RoadMap {

	//Reorganization of Item, Block and Common Class loading.
	/*
	 * So, due to the complex/silly way I've done things, I've ran into some circular loading problems around the mod.
	 * Issues occur where Classes like CI.java try access the GregtechItemList.java objects before they're actually set.
	 * A plan should be created to organize the best scheme to load things in the best order.
	 */

	//Recreation of GUIs for all Multiblocks
	/*
	 * Most Multi's use generic or straight out wrong GUI's on the controller.
	 * I'd like to go back and recreate all of these.
	 * 
	 * Some could even benefit from a totally new type of UI (Instead of Text issues, just change a 2x2px area between red and green for status lights)
	 * These advanced GUIs are probably out of my capability, but if anyone thinks they're a good idea, I'll give them a go.
	 */

	//Better Integration with GTNH
	/*
	 * Refactor things to be more common, refactor things to automatically switch between GTNH and standard variants 
	 * without having to over-abuse CORE.GTNH switches everywhere.
	 * Most of this can be done via expanding CI.java, so that we have automated handlers for everything 
	 * (IE. getX(5) will get 5x of the correct version of X)
	 */


	/*private static final LinkedHashMap<String, ObjMap<Integer, Boolean>>mCachedResults = new LinkedHashMap<String, ObjMap<Integer, Boolean>>();
	public boolean contains(ItemStack aStack) {
		if (aStack == null){
			return false;
		}
		ObjMap<Integer, Boolean> aCurrentSet;
		if (mCachedResults.get(this.toString()) != null){
			aCurrentSet = mCachedResults.get(this.toString());
		}

		else {
			aCurrentSet = new ObjMap<Integer, Boolean>(mPrefixedItems.size(), 0.5f);
			mCachedResults.put(this.toString(), aCurrentSet);
		}

		if (aCurrentSet.get(aStack.hashCode()) != null){
			return aCurrentSet.get(aStack.hashCode());
		}
		else {
			for (ItemStack tStack : mPrefixedItems){
				if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())){
					aCurrentSet.put(aStack.hashCode(), true);
					return true;
				}
			}
		}
		aCurrentSet.put(aStack.hashCode(), false);
		return false;
	}*/

	/*private static final LinkedHashMap<String, ObjMap<Integer, Boolean>>mCachedResults = new LinkedHashMap<String, ObjMap<Integer, Boolean>>();
    
    public boolean contains(ItemStack aStack) {
        if (aStack == null){
            return false;
        }        
        ObjMap<Integer, Boolean> aCurrentSet = mCachedResults.get(this.toString().toUpperCase());        
        if (aCurrentSet == null){
            aCurrentSet = new ObjMap<Integer, Boolean>((mPrefixedItems != null && mPrefixedItems.size() > 0 ? mPrefixedItems.size() : 1000), 0.5f);
            mCachedResults.put(this.toString().toUpperCase(), aCurrentSet);
        }        
        int mainHash = Objects.hashCode(aStack.getItem(), aStack.getItemDamage());
        Boolean result = aCurrentSet.get(mainHash);
        if (result != null){
            return result;
        }
        else {
            for (ItemStack tStack : mPrefixedItems){            	
                if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())){
                    aCurrentSet.put(Objects.hashCode(tStack.getItem(), tStack.getItemDamage()), true);
                    return true;
                }
            }
        }
        aCurrentSet.put(mainHash, false);
        return false;
    }*/

}
