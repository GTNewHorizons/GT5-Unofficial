package gtPlusPlus.api.objects.minecraft;

import java.util.Collection;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.objects.data.AutoMap;

public class NoConflictGTRecipeMap {

	private AutoMap<GT_Recipe> mRecipeCache = new AutoMap<GT_Recipe>();
	private final IGregTechTileEntity mMachineType;

	public NoConflictGTRecipeMap () {
		this(null);
	}
	
	public NoConflictGTRecipeMap (IGregTechTileEntity tile0) {
		this.mMachineType = tile0;
	}
	public boolean put(GT_Recipe recipe) {
		return add(recipe);
	}
	
	public boolean add(GT_Recipe recipe) {
		return mRecipeCache.setValue(recipe);
	}
	
	public Collection<GT_Recipe> getRecipeMap() {
		return mRecipeCache.values();
	}
	
	public boolean isMapValidForMachine(IGregTechTileEntity tile) {
		return tile == mMachineType;
	}


}
