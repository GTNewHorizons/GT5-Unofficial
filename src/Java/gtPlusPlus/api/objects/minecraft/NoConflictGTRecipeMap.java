package gtPlusPlus.api.objects.minecraft;

import java.util.Collection;
import java.util.Iterator;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.objects.data.AutoMap;

public class NoConflictGTRecipeMap implements Collection<GT_Recipe> {

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

	@Override
	public boolean addAll(Collection<? extends GT_Recipe> arg0) {
		int a = 0;
		for (Object v : arg0) {
			if (!this.mRecipeCache.containsValue((GT_Recipe) v)) {
				this.mRecipeCache.put((GT_Recipe) v);
				a++;
			}
		}
		return a > 0;
	}

	@Override
	public void clear() {
		mRecipeCache.clear();		
	}

	@Override
	public boolean contains(Object arg0) {
		return mRecipeCache.containsValue((GT_Recipe) arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		int a = 0;
		for (Object v : arg0) {
			if (this.mRecipeCache.containsValue((GT_Recipe) v)) {
				a++;
			}
		}
		return a == arg0.size();
	}

	@Override
	public boolean isEmpty() {
		return mRecipeCache.isEmpty();
	}

	@Override
	public Iterator<GT_Recipe> iterator() {
		return mRecipeCache.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return mRecipeCache.remove((GT_Recipe) arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		int a = 0;
		for (Object v : arg0) {
			if (this.mRecipeCache.containsValue((GT_Recipe) v)) {
				this.mRecipeCache.remove((GT_Recipe) v);
				a++;
			}
		}
		return a > 0;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		int mStartSize = this.mRecipeCache.size();
		this.mRecipeCache = (AutoMap<GT_Recipe>) arg0;
		int mEndsize = this.mRecipeCache.size();
		return mStartSize != mEndsize;
	}

	@Override
	public int size() {
		return this.mRecipeCache.size();
	}

	@Override
	public Object[] toArray() {
		return this.mRecipeCache.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return (T[]) this.mRecipeCache.toArray();
	}


}
