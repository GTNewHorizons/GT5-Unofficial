package gtPlusPlus.api.objects.minecraft;

import java.util.Iterator;
import java.util.Stack;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftDataStack extends Stack<ThaumcraftItemStackData> {

	private final int mStackSize;

	public ThaumcraftDataStack() {
		this(Integer.MAX_VALUE);
	}

	public ThaumcraftDataStack(int aMaxSize) {
		mStackSize = aMaxSize;
	}

	public boolean containsItemStack(ItemStack aStack, boolean aAddItemStackIfMissingFromStack) {
		return getItemStackIndex(aStack) != -1;
	}

	private int getItemStackIndex(ItemStack aStack) {
		if (this.empty() || aStack == null) {
			return -1;
		}
		Iterator<ThaumcraftItemStackData> iterator = this.iterator();
		int aIndex = 0;
		while(iterator.hasNext()){
			ThaumcraftItemStackData value = iterator.next();
			if (value.doesItemStackDataMatch(aStack)) {
				//int index = this.search(value);
				return aIndex;
			}
			aIndex++;
		}		
		return -1;
	}

	public AspectList getAspectsForStack(ItemStack aStack) {
		if (aStack != null) {
			int aIndex = getItemStackIndex(aStack);
			if (!this.empty()) {
				if (aIndex != -1) {
					ThaumcraftItemStackData aValue = this.elementAt(aIndex);
					if (aValue != null) {
						return aValue.getAspectList();
					}
				}	
			}
			if (this.empty() || aIndex == -1) {
				ThaumcraftItemStackData aTemp = new ThaumcraftItemStackData(aStack);
				this.push(aTemp);
				return aTemp.getAspectList();
			}
		}
		return new AspectList();
	}

	@Override
	public ThaumcraftItemStackData push(ThaumcraftItemStackData item) {
		if (this.size() >= this.mStackSize) {
			this.pop();
		}
		return super.push(item);
	}


}
