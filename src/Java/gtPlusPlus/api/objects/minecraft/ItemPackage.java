package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.handler.COMPAT_HANDLER;

public abstract class ItemPackage implements RunnableWithInfo<String> {

	public ItemPackage() {
		// Register for late run
		COMPAT_HANDLER.mObjectsToRunInPostInit.put(this);		
		init();
	}	
	
	@Override
	public final void run() {
		generateRecipes();
	}

	@Override
	public final String getInfoData() {
		return errorMessage();
	}

	public abstract String errorMessage();
	
	public abstract boolean generateRecipes();
	
	private final void init() {
		items();
		blocks();
		fluids();
	}
	
	public abstract void items();
	
	public abstract void blocks();
	
	public abstract void fluids();
	
	
}
