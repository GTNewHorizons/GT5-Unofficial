package gtPlusPlus.xmod.gregtech.common.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.proxy.ClientProxy;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.FileUtils;
import gtPlusPlus.core.util.sys.NetworkUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.UsernameCache;

public class GTPP_CapeRenderer extends RenderPlayer {

	private final ResourceLocation[] mCapes = { 
			new ResourceLocation("miscutils:textures/OrangeHD.png"),
			new ResourceLocation("miscutils:textures/FancyCapeHD.png"),
			new ResourceLocation("miscutils:textures/TesterCapeHD.png"),
			new ResourceLocation("miscutils:textures/DevCapeHD.png"),
			new ResourceLocation("miscutils:textures/PatreonCapeHD.png") };

	private final ArrayList<String> mData;

	public GTPP_CapeRenderer() {
		setRenderManager(RenderManager.instance);
		downloadCapeList();		
		ArrayList<String> aTemp = new ArrayList<String>();
		try {
		aTemp = returnDatData();
		}
		catch (Throwable t) {
			aTemp = new ArrayList<String>();
		}		
		mData = aTemp;
	}

	private static boolean hasResourceChecked = false;
	private boolean hasCape = false;
	private ResourceLocation tResource = null;

	public synchronized void receiveRenderSpecialsEvent(RenderPlayerEvent.Specials.Pre aEvent) {
		AbstractClientPlayer aPlayer = (AbstractClientPlayer) aEvent.entityPlayer;
		if (!ConfigSwitches.enableCustomCapes) {
			aEvent.setCanceled(true);
			Logger.WARNING("A1");
			return;
		}
		
		if (hasResourceChecked) {
			if (!hasCape && !CORE.DEVENV) {
				aEvent.setCanceled(true);
				Logger.WARNING("A2");
				return;
			}
		}

		// Make sure we don't keep checking on clients who dont have capes.
		if (!hasResourceChecked) {

			// Time to Spliterate some data
			Map<String, ResourceLocation> aPlayerData = new HashMap<String, ResourceLocation>();
			Map<String, String> aNameMap = new HashMap<String, String>();
			int i = 0;		
			if (!CORE.DEVENV) {
				for (String s : mData) {
					String[] aSplit = s.split("@");
					int a[] = new int[] { 0, mCapes.length - 1 };
					int aID = Integer.parseInt(aSplit[1]);
					// Quick Check to prevent lag
					Logger.WARNING("Trying to create UUID from - " + aSplit[0]);
					UUID aPlayerID = UUID.fromString(aSplit[0]);
					Logger.WARNING("Result: " + aPlayerID.toString());
					if (aPlayerID != null) {
						if (UsernameCache.containsUUID(aPlayerID)) {
							Logger.WARNING("UsernameCache contains a last known username for current players UUID.");
							if (!UsernameCache.getLastKnownUsername(aPlayerID).toLowerCase()
									.equals(ClientProxy.playerName)) {
								Logger.WARNING("Last known name does not match current name. Checking next UUID.");
								continue;
							} else {
								Logger.WARNING("Last known name does match current name.");
							}
						} else {
							Logger.WARNING("UsernameCache did not hold results for current player, oops.");
							continue;
						}
					}
					String aPlayerName = this.getPlayerName("iteration-" + (i++), aSplit[0]);
					aNameMap.put(aSplit[0], aPlayerName);
					aPlayerData.put(aPlayerName, this.mCapes[Math.max(a[0], Math.min(aID, a[1]))]);
				}
			}

			// Set flag to only render this event if player has a cape.
			for (String s : aNameMap.values()) {
				if (s.toLowerCase().equals(ClientProxy.playerName)) {
					hasCape = true;
					tResource = aPlayerData.get(ClientProxy.playerName);
					break;
				}
			}

			// Dev capes for dev mode.
			if (CORE.DEVENV) {
				hasCape = true;
				tResource = mCapes[3];
			}

			// Safety Check
			if (tResource == null) {
				if (aPlayerData.get(aPlayer.getDisplayName()) != null) {
					tResource = aPlayerData.get(aPlayer.getDisplayName());
				} else {
					hasCape = false;
				}
			}

			// Only run once.
			hasResourceChecked = true;
		}

		if (GT_Utility.getFullInvisibility(aPlayer) || aPlayer.isInvisible()
				|| GT_Utility.getPotion(aPlayer, Integer.valueOf(Potion.invisibility.id).intValue())) {
			aEvent.setCanceled(true);
			Logger.WARNING("A3");
			return;
		}

		float aPartialTicks = aEvent.partialRenderTick;
		try {

			if (tResource == null && CORE.DEVENV) {
				tResource = mCapes[3];
			}

			if ((tResource != null) && (!aPlayer.getHideCape())) {
				bindTexture(tResource);
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0F, 0.0F, 0.125F);
				double d0 = aPlayer.field_71091_bM + (aPlayer.field_71094_bP - aPlayer.field_71091_bM) * aPartialTicks
						- (aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * aPartialTicks);
				double d1 = aPlayer.field_71096_bN + (aPlayer.field_71095_bQ - aPlayer.field_71096_bN) * aPartialTicks
						- (aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * aPartialTicks);
				double d2 = aPlayer.field_71097_bO + (aPlayer.field_71085_bR - aPlayer.field_71097_bO) * aPartialTicks
						- (aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * aPartialTicks);
				float f6 = aPlayer.prevRenderYawOffset
						+ (aPlayer.renderYawOffset - aPlayer.prevRenderYawOffset) * aPartialTicks;
				double d3 = MathHelper.sin(f6 * CORE.PI / 180.0F);
				double d4 = -MathHelper.cos(f6 * CORE.PI / 180.0F);
				float f7 = (float) d1 * 10.0F;
				float f8 = (float) (d0 * d3 + d2 * d4) * 100.0F;
				float f9 = (float) (d0 * d4 - d2 * d3) * 100.0F;
				if (f7 < -6.0F) {
					f7 = -6.0F;
				}
				if (f7 > 32.0F) {
					f7 = 32.0F;
				}
				if (f8 < 0.0F) {
					f8 = 0.0F;
				}
				float f10 = aPlayer.prevCameraYaw + (aPlayer.cameraYaw - aPlayer.prevCameraYaw) * aPartialTicks;
				f7 += MathHelper.sin((aPlayer.prevDistanceWalkedModified
						+ (aPlayer.distanceWalkedModified - aPlayer.prevDistanceWalkedModified) * aPartialTicks) * 6.0F)
						* 32.0F * f10;
				if (aPlayer.isSneaking()) {
					f7 += 25.0F;
				}
				GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				((ModelBiped) this.mainModel).renderCloak(0.0625F);
				GL11.glPopMatrix();
			}
		} catch (Throwable e) {
			if (GT_Values.D1) {
				e.printStackTrace(GT_Log.err);
			}
		}
	}

	private String getPlayerName(String name, String uuid) {
		try {
			Logger.WORLD("[Capes++] Trying to UUID check " + name + ".");
			if (uuid != null) {
				if (uuid.length() > 0) {
					UUID g = UUID.fromString(uuid);
					if (g != null) {
						Logger.WORLD("[Capes++] Mojang/Cache checking for " + name + ".");
						GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(g);
						if (profile != null) {
							Logger.WARNING("[Capes++] Found for UUID check: " + profile.getName() + ".");
							return profile.getName();
						}
					} else {
						g = UUID.fromString(uuid.replace("-", ""));
						if (g != null) {
							Logger.WORLD("[Capes++] Mojang/Cache checking for " + name + ".");
							GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(g);
							if (profile != null) {
								Logger.WARNING("[Capes++] Found for UUID check 2: " + profile.getName() + ".");
								return profile.getName();
							}
						}
					}
				}
			}
			if (name != null) {
				if (name.length() > 0) {
					Logger.WORLD("[Capes++] Mojang/Cache checking for " + name + ".");
					GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(name);
					if (profile != null) {
						Logger.WARNING("[Capes++] Found for name check: " + profile.getName() + ".");
						return profile.getName();
					}
				}
			}
			Logger.WORLD("[Capes++] Failed UUID check for " + name + ".");
		} catch (Throwable t) {
		}
		return name;
	}

	private void downloadCapeList() {
		if (!NetworkUtils.checkNetworkIsAvailableWithValidInterface()) {
			return;
		}		
		try {
			File dat = new File(Utils.getMcDir(), "GTPP.dat");
			if (FileUtils.doesFileExist(dat)) {
				Date dateLastMod = new Date(dat.lastModified());
				Date dateNow = new Date(System.currentTimeMillis() - (7l * 24 * 60 * 60 * 1000));
				if (!dateLastMod.before(dateNow)) {
					return;
				}
			}
			InputStream inputStream = new URL("https://alkcorp.overminddl1.com/GTPP.dat").openStream();
			FileOutputStream fileOS = new FileOutputStream(dat);
			IOUtils.copy(inputStream, fileOS);

		} catch (Throwable t) {
			Logger.INFO("Unable to download GT++ cape list.");
		}
	}
	
	/* TODO: fix this stuff \u002a\u002f\u0009\u0070\u0072\u0069\u0076\u0061\u0074\u0065\u0020\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0020\u0072\u0065\u0074\u0075\u0072\u006e\u0044\u0061\u0074\u0044\u0061\u0074\u0061\u0028\u0029\u007b\u000a\u0009\u0009\u0046\u0069\u006c\u0065\u0020\u0064\u0061\u0074\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0046\u0069\u006c\u0065\u0028\u0055\u0074\u0069\u006c\u0073\u002e\u0067\u0065\u0074\u004d\u0063\u0044\u0069\u0072\u0028\u0029\u002c\u0020\u0022\u0047\u0054\u0050\u0050\u002e\u0064\u0061\u0074\u0022\u0029\u003b\u000a\u0009\u0009\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0020\u0048\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0028\u0029\u003b\u000a\u0009\u0009\u0074\u0072\u0079\u0020\u0028\u0042\u0075\u0066\u0066\u0065\u0072\u0065\u0064\u0052\u0065\u0061\u0064\u0065\u0072\u0020\u0062\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0042\u0075\u0066\u0066\u0065\u0072\u0065\u0064\u0052\u0065\u0061\u0064\u0065\u0072\u0028\u006e\u0065\u0077\u0020\u0046\u0069\u006c\u0065\u0052\u0065\u0061\u0064\u0065\u0072\u0028\u0064\u0061\u0074\u0029\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u006c\u0069\u006e\u0065\u003b\u000a\u0009\u0009\u0009\u0077\u0068\u0069\u006c\u0065\u0020\u0028\u0028\u006c\u0069\u006e\u0065\u0020\u003d\u0020\u0062\u0072\u002e\u0072\u0065\u0061\u0064\u004c\u0069\u006e\u0065\u0028\u0029\u0029\u0020\u0021\u003d\u0020\u006e\u0075\u006c\u006c\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0048\u002e\u0061\u0064\u0064\u0028\u006c\u0069\u006e\u0065\u0029\u003b\u000a\u0009\u0009\u0009\u007d\u000a\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0049\u004f\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u0065\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0065\u002e\u0070\u0072\u0069\u006e\u0074\u0053\u0074\u0061\u0063\u006b\u0054\u0072\u0061\u0063\u0065\u0028\u0029\u003b\u000a\u0009\u0009\u007d\u000a\u0009\u0009\u000a\u0009\u0009\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0061\u0053\u0070\u006c\u0069\u0074\u0044\u0061\u0074\u0061\u0043\u0068\u0065\u0063\u006b\u005b\u005d\u0020\u003d\u0020\u0048\u002e\u0067\u0065\u0074\u0028\u0030\u0029\u002e\u0073\u0070\u006c\u0069\u0074\u0028\u0022\u0025\u0022\u0029\u003b\u000a\u0009\u0009\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0020\u0061\u0043\u006c\u0065\u0061\u006e\u0044\u0061\u0074\u0061\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0028\u0029\u003b\u000a\u0009\u0009\u0066\u006f\u0072\u0020\u0028\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0073\u0020\u003a\u0020\u0061\u0053\u0070\u006c\u0069\u0074\u0044\u0061\u0074\u0061\u0043\u0068\u0065\u0063\u006b\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0073\u0020\u0021\u003d\u0020\u006e\u0075\u006c\u006c\u0020\u0026\u0026\u0020\u0073\u002e\u006c\u0065\u006e\u0067\u0074\u0068\u0028\u0029\u0020\u003e\u0020\u0030\u0020\u0026\u0026\u0020\u0021\u0073\u002e\u0065\u0071\u0075\u0061\u006c\u0073\u0028\u0022\u0020\u0022\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0061\u0043\u006c\u0065\u0061\u006e\u0044\u0061\u0074\u0061\u002e\u0061\u0064\u0064\u0028\u0073\u0029\u003b\u000a\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u007d\u000a\u0009\u0009\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u004d\u0041\u0049\u004e\u005f\u0053\u0054\u0041\u0054\u0049\u0043\u005f\u0049\u0056\u0020\u003d\u0020\u0022\u0030\u0031\u0030\u0030\u0030\u0030\u0030\u0031\u0020\u0030\u0031\u0030\u0031\u0030\u0030\u0031\u0022\u003b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u004d\u0041\u0049\u004e\u005f\u0053\u0054\u0041\u0054\u0049\u0043\u005f\u004b\u0045\u0059\u0020\u003d\u0020\u0022\u0030\u0031\u0030\u0030\u0030\u0031\u0030\u0030\u0020\u0030\u0031\u0031\u0030\u0031\u0031\u0031\u0022\u003b\u000a\u0009\u0009\u000a\u0009\u0009\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0020\u0061\u0044\u0061\u0074\u0061\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0041\u0072\u0072\u0061\u0079\u004c\u0069\u0073\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0028\u0029\u003b\u000a\u0009\u0009\u0066\u006f\u0072\u0020\u0028\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0068\u0020\u003a\u0020\u0061\u0043\u006c\u0065\u0061\u006e\u0044\u0061\u0074\u0061\u0029\u0020\u007b\u0009\u0009\u0009\u000a\u0009\u0009\u0009\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u003d\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0028\u0068\u002c\u0020\u004d\u0041\u0049\u004e\u005f\u0053\u0054\u0041\u0054\u0049\u0043\u005f\u0049\u0056\u002c\u0020\u004d\u0041\u0049\u004e\u005f\u0053\u0054\u0041\u0054\u0049\u0043\u005f\u004b\u0045\u0059\u0029\u003b\u000a\u0009\u0009\u0009\u0061\u0044\u0061\u0074\u0061\u002e\u0061\u0064\u0064\u0028\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0053\u0074\u0072\u0069\u006e\u0067\u0029\u003b\u000a\u0009\u0009\u007d\u000a\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0061\u0044\u0061\u0074\u0061\u003b\u0009\u0009\u000a\u0009\u007d\u000a\u0009\u000a\u0009\u0070\u0072\u0069\u0076\u0061\u0074\u0065\u0020\u0073\u0074\u0061\u0074\u0069\u0063\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u006d\u0064\u0035\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0069\u006e\u0070\u0075\u0074\u0029\u0020\u0074\u0068\u0072\u006f\u0077\u0073\u0020\u004e\u006f\u0053\u0075\u0063\u0068\u0041\u006c\u0067\u006f\u0072\u0069\u0074\u0068\u006d\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u007b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u004d\u0065\u0073\u0073\u0061\u0067\u0065\u0044\u0069\u0067\u0065\u0073\u0074\u0020\u006d\u0064\u0020\u003d\u0020\u004d\u0065\u0073\u0073\u0061\u0067\u0065\u0044\u0069\u0067\u0065\u0073\u0074\u002e\u0067\u0065\u0074\u0049\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0028\u0022\u004d\u0044\u0035\u0022\u0029\u003b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0062\u0079\u0074\u0065\u005b\u005d\u0020\u006d\u0065\u0073\u0073\u0061\u0067\u0065\u0044\u0069\u0067\u0065\u0073\u0074\u0020\u003d\u0020\u006d\u0064\u002e\u0064\u0069\u0067\u0065\u0073\u0074\u0028\u0069\u006e\u0070\u0075\u0074\u002e\u0067\u0065\u0074\u0042\u0079\u0074\u0065\u0073\u0028\u0029\u0029\u003b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0042\u0069\u0067\u0049\u006e\u0074\u0065\u0067\u0065\u0072\u0020\u006e\u0075\u006d\u0062\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0042\u0069\u0067\u0049\u006e\u0074\u0065\u0067\u0065\u0072\u0028\u0031\u002c\u0020\u006d\u0065\u0073\u0073\u0061\u0067\u0065\u0044\u0069\u0067\u0065\u0073\u0074\u0029\u003b\u000a\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u002e\u0066\u006f\u0072\u006d\u0061\u0074\u0028\u0022\u0025\u0030\u0033\u0032\u0078\u0022\u002c\u0020\u006e\u0075\u006d\u0062\u0065\u0072\u0029\u003b\u000a\u0009\u007d\u000a\u000a\u0009\u0070\u0072\u0069\u0076\u0061\u0074\u0065\u0020\u0073\u0074\u0061\u0074\u0069\u0063\u0020\u0043\u0069\u0070\u0068\u0065\u0072\u0020\u0069\u006e\u0069\u0074\u0043\u0069\u0070\u0068\u0065\u0072\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0069\u006e\u0074\u0020\u006d\u006f\u0064\u0065\u002c\u0020\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u0053\u0074\u0072\u0069\u006e\u0067\u002c\u0020\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0073\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0029\u000a\u0009\u0009\u0009\u0074\u0068\u0072\u006f\u0077\u0073\u0020\u004e\u006f\u0053\u0075\u0063\u0068\u0041\u006c\u0067\u006f\u0072\u0069\u0074\u0068\u006d\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u002c\u0020\u004e\u006f\u0053\u0075\u0063\u0068\u0050\u0061\u0064\u0064\u0069\u006e\u0067\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u002c\u0020\u0049\u006e\u0076\u0061\u006c\u0069\u0064\u004b\u0065\u0079\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u002c\u000a\u0009\u0009\u0009\u0049\u006e\u0076\u0061\u006c\u0069\u0064\u0041\u006c\u0067\u006f\u0072\u0069\u0074\u0068\u006d\u0050\u0061\u0072\u0061\u006d\u0065\u0074\u0065\u0072\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u007b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0053\u0070\u0065\u0063\u0020\u0073\u006b\u0065\u0079\u0053\u0070\u0065\u0063\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0053\u0070\u0065\u0063\u0028\u0074\u006f\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u0028\u006d\u0064\u0035\u0028\u0073\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0029\u0029\u002c\u0020\u0022\u0041\u0045\u0053\u0022\u0029\u003b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0049\u0076\u0050\u0061\u0072\u0061\u006d\u0065\u0074\u0065\u0072\u0053\u0070\u0065\u0063\u0020\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0049\u0076\u0050\u0061\u0072\u0061\u006d\u0065\u0074\u0065\u0072\u0053\u0070\u0065\u0063\u0028\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u0053\u0074\u0072\u0069\u006e\u0067\u002e\u0067\u0065\u0074\u0042\u0079\u0074\u0065\u0073\u0028\u0029\u0029\u003b\u000a\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0043\u0069\u0070\u0068\u0065\u0072\u0020\u0063\u0069\u0070\u0068\u0065\u0072\u0020\u003d\u0020\u0043\u0069\u0070\u0068\u0065\u0072\u002e\u0067\u0065\u0074\u0049\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0028\u0022\u0041\u0045\u0053\u002f\u0043\u0046\u0042\u0038\u002f\u004e\u006f\u0050\u0061\u0064\u0064\u0069\u006e\u0067\u0022\u0029\u003b\u000a\u0009\u0009\u0063\u0069\u0070\u0068\u0065\u0072\u002e\u0069\u006e\u0069\u0074\u0028\u006d\u006f\u0064\u0065\u002c\u0020\u0073\u006b\u0065\u0079\u0053\u0070\u0065\u0063\u002c\u0020\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u0029\u003b\u000a\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0063\u0069\u0070\u0068\u0065\u0072\u003b\u000a\u0009\u007d\u000a\u000a\u0009\u0070\u0075\u0062\u006c\u0069\u0063\u0020\u0073\u0074\u0061\u0074\u0069\u0063\u0020\u0062\u0079\u0074\u0065\u005b\u005d\u0020\u0074\u006f\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u0028\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0073\u0029\u0020\u007b\u000a\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0044\u0061\u0074\u0061\u0074\u0079\u0070\u0065\u0043\u006f\u006e\u0076\u0065\u0072\u0074\u0065\u0072\u002e\u0070\u0061\u0072\u0073\u0065\u0048\u0065\u0078\u0042\u0069\u006e\u0061\u0072\u0079\u0028\u0073\u0029\u003b\u000a\u0009\u007d\u000a\u0009\u000a\u0009\u0070\u0072\u0069\u0076\u0061\u0074\u0065\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0065\u006e\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0044\u0061\u0074\u0061\u002c\u0020\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u002c\u0020\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0073\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0029\u0020\u007b\u000a\u0009\u0009\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0044\u0061\u0074\u0061\u0020\u003d\u0020\u006e\u0075\u006c\u006c\u003b\u000a\u0009\u0009\u0074\u0072\u0079\u0020\u007b\u000a\u0009\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0043\u0069\u0070\u0068\u0065\u0072\u0020\u0063\u0069\u0070\u0068\u0065\u0072\u0020\u003d\u0020\u0069\u006e\u0069\u0074\u0043\u0069\u0070\u0068\u0065\u0072\u0028\u0043\u0069\u0070\u0068\u0065\u0072\u002e\u0044\u0045\u0043\u0052\u0059\u0050\u0054\u005f\u004d\u004f\u0044\u0045\u002c\u0020\u0069\u006e\u0069\u0074\u0069\u0061\u006c\u0056\u0065\u0063\u0074\u006f\u0072\u002c\u0020\u0073\u0065\u0063\u0072\u0065\u0074\u004b\u0065\u0079\u0029\u003b\u000a\u0009\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0062\u0079\u0074\u0065\u005b\u005d\u0020\u0065\u006e\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u0020\u003d\u0020\u0042\u0061\u0073\u0065\u0036\u0034\u002e\u0067\u0065\u0074\u0044\u0065\u0063\u006f\u0064\u0065\u0072\u0028\u0029\u002e\u0064\u0065\u0063\u006f\u0064\u0065\u0028\u0065\u006e\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0044\u0061\u0074\u0061\u0029\u003b\u000a\u0009\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0062\u0079\u0074\u0065\u005b\u005d\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u0020\u003d\u0020\u0063\u0069\u0070\u0068\u0065\u0072\u002e\u0064\u006f\u0046\u0069\u006e\u0061\u006c\u0028\u0065\u006e\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u0029\u003b\u000a\u0009\u0009\u0009\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0044\u0061\u0074\u0061\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0028\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0042\u0079\u0074\u0065\u0041\u0072\u0072\u0061\u0079\u002c\u0020\u0022\u0055\u0054\u0046\u0038\u0022\u0029\u003b\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u0065\u0029\u0020\u007b\u000a\u0009\u0009\u007d\u000a\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0064\u0065\u0063\u0072\u0079\u0070\u0074\u0065\u0064\u0044\u0061\u0074\u0061\u003b\u000a\u0009\u007d\u002f\u002a */
	

}