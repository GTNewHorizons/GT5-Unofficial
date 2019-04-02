package gtPlusPlus.api.objects.minecraft;

import java.util.LinkedHashMap;
import java.util.Set;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraft.util.IIcon;

public class TexturePackage {

	private AutoMap<IIcon> mAnimationArray = new AutoMap<IIcon>();
	
	public IIcon getFrame(int aFrame) {
		if (aFrame < 0 || aFrame >= mAnimationArray.size()) {
			return mAnimationArray.get(0);
		}		
		return mAnimationArray.get(aFrame);
	}	
	
	public boolean addFrame(IIcon aFrame) {		
		if (aFrame != null) {
			return mAnimationArray.add(aFrame);
		}		
		return false;
	}
	
	public boolean addFrames(AutoMap<IIcon> aFrames) {
		for (IIcon h : aFrames) {
			if (!addFrame(h)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addFrames(LinkedHashMap<?, IIcon> aFrames) {
		for (IIcon h : aFrames.values()) {
			if (!addFrame(h)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addFrames(Set<IIcon> aFrames) {
		for (IIcon h : aFrames) {
			if (!addFrame(h)) {
				return false;
			}
		}
		return true;
	}
	
	
}
