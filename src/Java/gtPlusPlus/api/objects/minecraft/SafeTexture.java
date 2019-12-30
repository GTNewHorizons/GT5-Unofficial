package gtPlusPlus.api.objects.minecraft;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gtPlusPlus.core.util.Utils;
import net.minecraft.util.IIcon;

/**
 * A Server Side safe object that can hold {@link IIcon}s.
 * @author Alkalus
 *
 */
public class SafeTexture implements Runnable {	

	@SideOnly(Side.CLIENT)
	private static final HashMap<Integer, IIcon> mHashToIconCache = new HashMap<Integer, IIcon>();
	
	@SideOnly(Side.CLIENT)
	private static final HashMap<String, Integer> mPathToHashCash = new HashMap<String, Integer>();
	
	private static final HashMap<String, SafeTexture> mTextureObjectCache = new HashMap<String, SafeTexture>();	
	
	private final int mHash;
	
	private final String mTextureName;
	
	private final static String getKey(String aTexPath) {
		String aNameKey = Utils.sanitizeString(aTexPath);
		aNameKey = aNameKey.replace('/', ' ');
		aNameKey = aNameKey.toLowerCase();
		return aNameKey;
	}
	
	public static SafeTexture register(String aTexturePath) {
		String aNameKey = getKey(aTexturePath);
		SafeTexture g = mTextureObjectCache.get(aNameKey);
		if (g == null) {
			g = new SafeTexture(aTexturePath);			
			mTextureObjectCache.put(aNameKey, g);			
			mPathToHashCash.put(aTexturePath, aTexturePath.hashCode());			
		}	
		return g;
	}
	
	private SafeTexture(String aTexturePath) {
		mTextureName = aTexturePath;
		mHash = getKey(aTexturePath).hashCode();
		GregTech_API.sGTBlockIconload.add(this);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon() {
		return mHashToIconCache.get(mHash);
	}

	@Override
	public void run() {
		mHashToIconCache.put(getKey(mTextureName).hashCode(), GregTech_API.sBlockIcons.registerIcon(mTextureName));	
	}
	
}
