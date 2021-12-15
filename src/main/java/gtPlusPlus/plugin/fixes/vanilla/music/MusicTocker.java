package gtPlusPlus.plugin.fixes.vanilla.music;

import java.lang.reflect.Field;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class MusicTocker extends MusicTicker implements Runnable {

	private final Random mRandom = new XSTR();
	private final Minecraft mMinecraft;
	private final IPlugin mPlugin;
	private ISound mSound;
	private int mTimeUntilNextTrack = 100;
	
	public boolean mVanillaManager = false;

	public MusicTocker(IPlugin aPlugin) {
		super(Minecraft.getMinecraft());
		mPlugin = aPlugin;
		mMinecraft = Minecraft.getMinecraft();	
		mPlugin.log("[BGM] Created BGM Watchdog with a delay of "+getDelay()+" ticks.");	
		inject();
	}

	private static int getDelay() {
		return CORE_Preloader.enableWatchdogBGM;
	}	

	private boolean inject() {
		mPlugin.log("[BGM] Inject new Watchdog into Minecraft instance.");	
		ReflectionUtils.setField(Minecraft.getMinecraft(), "mcMusicTicker", this);
		mPlugin.log("[BGM] Verifying...");
		Field f = ReflectionUtils.getField(Minecraft.class, "mcMusicTicker");
		try {
			Object m = f.get(mMinecraft);
			if (m != null) {
				if (m instanceof MusicTocker || m.getClass().isAssignableFrom(getClass())) {
					mPlugin.log("[BGM] Success.");	
					return true;
				}
				else if (m instanceof MusicTicker || m.getClass().isAssignableFrom(MusicTicker.class)) {
					mPlugin.log("[BGM] Found Vanilla MusicTicker, but may be instance of MusicTocker.");		
					return true;
				}			
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
		
		mPlugin.log("[BGM] Failed.");	
		return false;
	}
	
	private final void updateInternalNumber() {
		if (ReflectionUtils.doesFieldExist(getClass(), "field_147676_d")) {
			ReflectionUtils.setField(this, "field_147676_d", mTimeUntilNextTrack);
		}
	}
	
	private final void updateInternalSound(ISound aSound) {
		if (ReflectionUtils.doesFieldExist(getClass(), "field_147678_c")) {
			ReflectionUtils.setField(this, "field_147678_c", aSound);
		}		
	}

	/**
	 * Updates the JList with a new model.
	 */
	@Override
	public void update() {	
		run();
		mVanillaManager = true;
	}

	@Override
	public void run() {
		MusicType musictype = this.mMinecraft.func_147109_W();
		
			if (this.mSound != null) {
				if (!musictype.getMusicTickerLocation().equals(this.mSound.getPositionedSoundLocation())) {
					this.mMinecraft.getSoundHandler().stopSound(this.mSound);
					this.mTimeUntilNextTrack = MathHelper.getRandomIntegerInRange(this.mRandom, 0, getDelay() / 2);
					updateInternalNumber();
					Logger.INFO("[BGM] Adjusted BGM delay 1");
				}
				if (!this.mMinecraft.getSoundHandler().isSoundPlaying(this.mSound)) {
					this.mSound = null;
					updateInternalSound(null);
					this.mTimeUntilNextTrack = Math.min(MathHelper.getRandomIntegerInRange(this.mRandom, getDelay(), getDelay() * 2), this.mTimeUntilNextTrack);
					updateInternalNumber();
					Logger.INFO("[BGM] Adjusted BGM delay 2");
				}
			}
			else if (this.mSound == null && this.mTimeUntilNextTrack-- <= 0) {			
				this.mSound = PositionedSoundRecord.func_147673_a(musictype.getMusicTickerLocation());
				updateInternalSound(mSound);
				this.mMinecraft.getSoundHandler().playSound(this.mSound);
				this.mTimeUntilNextTrack = getDelay();
				updateInternalNumber();
				Logger.INFO("[BGM] Adjusted BGM 3");
			}
				
		/*
		 * try { // Get Value stored in underlying object. Integer aRealDelay =
		 * (Integer) ReflectionUtils.getField(getClass(), "field_147676_d").get(this);
		 * 
		 * if (aRealDelay == null) { return; } else { if (aRealDelay > getDelay() ||
		 * aRealDelay <= 0) { this.mTimeUntilNextTrack = getDelay();
		 * updateInternalNumber(); } else { this.mTimeUntilNextTrack -= 5 * 20;
		 * updateInternalNumber(); } aRealDelay = (Integer)
		 * ReflectionUtils.getField(getClass(), "field_147676_d").get(this);
		 * Logger.INFO("[BGM] Adjusted BGM - "+aRealDelay); }
		 * 
		 * } catch (IllegalArgumentException | IllegalAccessException e) { }
		 */					
	}

}