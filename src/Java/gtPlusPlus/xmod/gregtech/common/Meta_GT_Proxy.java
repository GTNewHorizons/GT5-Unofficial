package gtPlusPlus.xmod.gregtech.common;

import static gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_Overflow.mOverflowCache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.common.GT_Proxy;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.ObjMap;
import gtPlusPlus.core.util.reflect.ProxyFinder;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class Meta_GT_Proxy {

	public static List<Runnable> GT_BlockIconload = new ArrayList<>();
	public static List<Runnable> GT_ItemIconload = new ArrayList<>();

	@SideOnly(Side.CLIENT)
	public static IIconRegister sBlockIcons, sItemIcons;

	public Meta_GT_Proxy() {
		Logger.INFO("GT_PROXY - initialized.");
		scheduleCoverMapCleaner();
	}

	public static boolean areWeUsingGregtech5uExperimental(){
		final int version = GregTech_API.VERSION;
		if ((version == 508) || (version == 507)){
			return false;
		}
		else if (version == 509){
			return true;
		}
		else {
			return false;
		}
	}
	

	public void scheduleCoverMapCleaner(){
	    TimerTask repeatedTask = new TimerTask() {
	        public void run() {
	            cleanupOverFlowCoverCache();
	        }
	    };
	    Timer timer = new Timer("CoverCleanupManager");	     
	    long delay  = 120000L;
	    long period = 300000L;
	    timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}
	
	public static int cleanupOverFlowCoverCache() {
		ObjMap cache = mOverflowCache;
		int aRemoved = 0;
		long aCurrentTime = System.currentTimeMillis()/1000;
		for (Object o : cache.values()) {
			if (o != null && o instanceof HashMap) {
				HashMap<String, Object> m = (HashMap<String, Object>) o;
				if (m != null) {
					String s = (String) m.get("aCoverKey");
					if (m.containsKey("aLastUpdatedTime")) {
						long mapTime = (long) m.get("mLastUpdatedTime");
						if ((aCurrentTime-(mapTime/1000) > 30)){
							mOverflowCache.remove(s);
							aRemoved++;							
						}
					}
					else {
						mOverflowCache.remove(s);
						aRemoved++;
					}
				}
			}
		}
		return aRemoved;
	}
	
	
	private static GT_Proxy[] mProxies = new GT_Proxy[2];

	public static Object getFieldFromGregtechProxy(boolean client, String fieldName) {
		Object proxyGT;

		if (mProxies[0] != null && client) {
			proxyGT = mProxies[0];
		} else if (mProxies[1] != null && !client) {
			proxyGT = mProxies[1];
		} else {
			try {
				proxyGT = (client ? ProxyFinder.getClientProxy(GT_Mod.instance)
						: ProxyFinder.getServerProxy(GT_Mod.instance));
			} catch (final ReflectiveOperationException e1) {
				proxyGT = null;
				Logger.INFO("Failed to obtain instance of GT " + (client ? "Client" : "Server") + " proxy.");
			}
			if (mProxies[0] == null && client) {
				mProxies[0] = (GT_Proxy) proxyGT;
			} else if (mProxies[1] == null && !client) {
				mProxies[1] = (GT_Proxy) proxyGT;
			}
		}

		if (proxyGT != null && proxyGT instanceof GT_Proxy) {
			try {
				return ReflectionUtils.getField(proxyGT.getClass(), fieldName).get(proxyGT);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			}
		}
		return null;
	}

}
