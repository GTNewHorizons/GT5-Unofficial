package gtPlusPlus.xmod.gregtech.common.render;

import static gtPlusPlus.GTplusplus.*;

import java.util.Collection;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class GTPP_CapeRenderer
extends RenderPlayer {
	private final ResourceLocation[] mCapes = {new ResourceLocation("miscutils:textures/OrangeHD.png"), new ResourceLocation("miscutils:textures/TesterCapeHD.png"), new ResourceLocation("miscutils:textures/FancyCapeHD.png"), new ResourceLocation("miscutils:textures/DevCapeHD.png"), new ResourceLocation("miscutils:textures/PatreonCapeHD.png")};
	private final Collection<String> mCapeList;

	public GTPP_CapeRenderer(Collection<String> aCapeList) {
		this.mCapeList = aCapeList;
		setRenderManager(RenderManager.instance);
		BuildCapeList();
	}

	private static volatile ResourceLocation cachedResource = null;
	private static boolean hasResourceChecked = false;

	public synchronized void receiveRenderSpecialsEvent(RenderPlayerEvent.Specials.Pre aEvent) {
		AbstractClientPlayer aPlayer = (AbstractClientPlayer) aEvent.entityPlayer;
		ResourceLocation tResource = null;	
		if (cachedResource != null) {
			tResource = cachedResource;
		}
		else {
			String mTemp = "";
			//Make sure we don't keep checking on clients who dont have capes.
			if (!hasResourceChecked) {
				//Only run once.
				hasResourceChecked = true;
				//If list's have not been built yet for some reason, we best do it now.
				if (mDevCapes.size() <= 1) {
					BuildCapeList();
				}

				//Iterates all players in all lists, caches result.
				for (Pair<String, String> mName : mOrangeCapes){				
					mTemp = getPlayerName(mName.getKey(), mName.getValue());
					if (mTemp.toLowerCase().contains(aPlayer.getDisplayName().toLowerCase())) {
						tResource = this.mCapes[0];
					}
				}
				for (Pair<String, String> mName : mMiscCapes){
					mTemp = getPlayerName(mName.getKey(), mName.getValue());
					if (mTemp.toLowerCase().contains(aPlayer.getDisplayName().toLowerCase())) {
						tResource = this.mCapes[1];
					}
				}
				for (Pair<String, String> mName : mBetaTestCapes){
					mTemp = getPlayerName(mName.getKey(), mName.getValue());
					if (mTemp.toLowerCase().contains(aPlayer.getDisplayName().toLowerCase())) {
						tResource = this.mCapes[2];
					}
				}
				for (Pair<String, String> mName : mDevCapes){
					mTemp = getPlayerName(mName.getKey(), mName.getValue());
					if (mTemp.toLowerCase().contains(aPlayer.getDisplayName().toLowerCase())) {
						tResource = this.mCapes[3];
					}
				}
				for (Pair<String, String> mName : mPatreonCapes){
					mTemp = getPlayerName(mName.getKey(), mName.getValue());
					if (mTemp.toLowerCase().contains(aPlayer.getDisplayName().toLowerCase())) {
						tResource = this.mCapes[4];
					}
				}
				if (tResource != null) {
					cachedResource = tResource;
				}
			}
		}


		if (GT_Utility.getFullInvisibility(aPlayer) || aPlayer.isInvisible() || GT_Utility.getPotion(aPlayer, Integer.valueOf(Potion.invisibility.id).intValue())) {
			aEvent.setCanceled(true);
			return;
		}
		float aPartialTicks = aEvent.partialRenderTick;
		try {		


			/*if (CORE.DEVENV) {
				tResource = this.mCapes[3];
			}*/

			/*if (this.mCapeList.contains(aPlayer.getDisplayName().toLowerCase())) {
                tResource = this.mCapes[0];
            }*/
			if ((tResource != null) && (!aPlayer.getHideCape())) {
				bindTexture(tResource);
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0F, 0.0F, 0.125F);
				double d0 = aPlayer.field_71091_bM + (aPlayer.field_71094_bP - aPlayer.field_71091_bM) * aPartialTicks - (aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * aPartialTicks);
				double d1 = aPlayer.field_71096_bN + (aPlayer.field_71095_bQ - aPlayer.field_71096_bN) * aPartialTicks - (aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * aPartialTicks);
				double d2 = aPlayer.field_71097_bO + (aPlayer.field_71085_bR - aPlayer.field_71097_bO) * aPartialTicks - (aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * aPartialTicks);
				float f6 = aPlayer.prevRenderYawOffset + (aPlayer.renderYawOffset - aPlayer.prevRenderYawOffset) * aPartialTicks;
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
				f7 += MathHelper.sin((aPlayer.prevDistanceWalkedModified + (aPlayer.distanceWalkedModified - aPlayer.prevDistanceWalkedModified) * aPartialTicks) * 6.0F) * 32.0F * f10;
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
		Logger.WORLD("[Capes++] Trying to UUID check "+name+".");
		if (uuid != null) {    		
			if (uuid.length() > 0) {
				UUID g = UUID.fromString(uuid);
				if (g != null) {
					Logger.WORLD("[Capes++] Mojang/Cache checking for "+name+".");
					GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(g);
					if (profile != null) {
						Logger.INFO("[Capes++] Found for UUID check: "+profile.getName()+".");
						return profile.getName();
					}  
				}
			}    		
		}  
		if (name != null) {    		
			if (name.length() > 0) {
				Logger.WORLD("[Capes++] Mojang/Cache checking for "+name+".");
				GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(name);
				if (profile != null) {
					Logger.INFO("[Capes++] Found for name check: "+profile.getName()+".");
					return profile.getName();
				} 			
			}    		
		}  
		Logger.WORLD("[Capes++] Failed UUID check for "+name+".");
		return name;
	}

}