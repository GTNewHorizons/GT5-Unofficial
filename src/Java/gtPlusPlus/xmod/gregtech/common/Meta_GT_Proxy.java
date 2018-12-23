package gtPlusPlus.xmod.gregtech.common;

import static gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_Overflow.mOverflowCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.ObjMap;
import gtPlusPlus.api.objects.minecraft.FormattedTooltipString;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ProxyFinder;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class Meta_GT_Proxy {

	public static List<Runnable> GT_BlockIconload = new ArrayList<>();
	public static List<Runnable> GT_ItemIconload = new ArrayList<>();
	
	public static AutoMap<Integer> GT_ValidHeatingCoilMetas = new AutoMap<Integer>();
	
	public static final Map<String, FormattedTooltipString> mCustomGregtechMetaTooltips = new LinkedHashMap<String, FormattedTooltipString>();

	@SideOnly(Side.CLIENT)
	public static IIconRegister sBlockIcons, sItemIcons;

	public Meta_GT_Proxy() {
		Logger.INFO("GT_PROXY - initialized.");
		scheduleCoverMapCleaner();
		setValidHeatingCoilMetas();
	}
	
	public void setValidHeatingCoilMetas() {
		for (int i = 0; i <= 6; i++ ) {
			GT_ValidHeatingCoilMetas.put(i);			
		}
		if (CORE.GTNH) {
			for (int i = 7; i <= 8; i++ ) {
				GT_ValidHeatingCoilMetas.put(i);			
			}		
		}
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
		ObjMap<String, ?> cache = mOverflowCache;
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
	
	public void setCustomGregtechTooltip(String aNbtTagName, FormattedTooltipString aData) {
		mCustomGregtechMetaTooltips.put(aNbtTagName, aData);
	}
	
    public static void conStructGtTileBlockTooltip(ItemStack aStack, EntityPlayer aPlayer, List<Object> aList, boolean par4) {
        try {
            int tDamage = aStack.getItemDamage();
            if ((tDamage <= 0) || (tDamage >= GregTech_API.METATILEENTITIES.length)) {
                return;
            }

            if (GregTech_API.METATILEENTITIES[tDamage] != null) {
                IGregTechTileEntity tTileEntity = GregTech_API.METATILEENTITIES[tDamage].getBaseMetaTileEntity();
                if (tTileEntity.getDescription() != null) {
                    int i = 0;
                    for (String tDescription : tTileEntity.getDescription()) {
                        if (GT_Utility.isStringValid(tDescription)) {
                        	if(tDescription.contains("%%%")){
                        		String[] tString = tDescription.split("%%%");
                        		if(tString.length>=2){
                                                StringBuffer tBuffer = new StringBuffer();
                        			Object tRep[] = new String[tString.length / 2];
                        			for (int j = 0; j < tString.length; j++)
                        				if (j % 2 == 0) tBuffer.append(tString[j]);
                        				else {tBuffer.append(" %s"); tRep[j / 2] = tString[j];}
                                                aList.add(String.format(GT_LanguageManager.addStringLocalization("TileEntity_DESCRIPTION_" + tDamage + "_Index_" + i++, tBuffer.toString(), !GregTech_API.sPostloadFinished), tRep));
                        		}
                        	}else{String tTranslated = GT_LanguageManager.addStringLocalization("TileEntity_DESCRIPTION_" + tDamage + "_Index_" + i++, tDescription, !GregTech_API.sPostloadFinished );
                            aList.add(tTranslated.equals("") ? tDescription : tTranslated);}
                        }else i++;
                    }
                }
                if (tTileEntity.getEUCapacity() > 0L) {
                    if (tTileEntity.getInputVoltage() > 0L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.GREEN + tTileEntity.getInputVoltage() + " (" + GT_Values.VN[GT_Utility.getTier(tTileEntity.getInputVoltage())] + ")" + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputVoltage() > 0L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.GREEN + tTileEntity.getOutputVoltage() + " (" + GT_Values.VN[GT_Utility.getTier(tTileEntity.getOutputVoltage())] + ")" + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputAmperage() > 1L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.YELLOW + tTileEntity.getOutputAmperage() + EnumChatFormatting.GRAY);
                    }
                    aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.BLUE + tTileEntity.getEUCapacity() + EnumChatFormatting.GRAY);
                }
            }
            NBTTagCompound aNBT = aStack.getTagCompound();
            if (aNBT != null) {
                if (aNBT.getBoolean("mMuffler")) {
                    aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "has Muffler Upgrade", !GregTech_API.sPostloadFinished ));
                }
                if (aNBT.getBoolean("mSteamConverter")) {
                    aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER", "has Steam Upgrade", !GregTech_API.sPostloadFinished ));
                }
                int tAmount = 0;
                if ((tAmount = aNBT.getByte("mSteamTanks")) > 0) {
                    aList.add(tAmount + " " + GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS", "Steam Tank Upgrades", !GregTech_API.sPostloadFinished ));
                }
                
                FluidStack afluid = net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag("mFluid"));
                if (afluid != null) {
                    int tFluidAmount = afluid.amount;
                    if (tFluidAmount > 0) {
                        aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_FLUIDTANK", "Tank Fluid: "+tFluidAmount+"L "+afluid.getLocalizedName()+"", !GregTech_API.sPostloadFinished ));
                    }                	
                }
                
                //Add Custom Tooltips
                for (String s : mCustomGregtechMetaTooltips.keySet()) {
                	if (aNBT.hasKey(s)) {                		
                		String aTip = mCustomGregtechMetaTooltips.get(s).getTooltip(aNBT.getString(s));
                		aList.add(aTip);
                	}                	
                }
                
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

}
