package gtPlusPlus.plugin.fixes.vanilla;

import java.util.Timer;
import java.util.TimerTask;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.plugin.fixes.interfaces.IBugFix;
import gtPlusPlus.plugin.fixes.vanilla.music.MusicTocker;
import gtPlusPlus.preloader.PreloaderCore;

public class VanillaBackgroundMusicFix implements IBugFix {

    private final IPlugin mParent;
    private final boolean enabled;
    private MusicTocker mFixInstance;

    public VanillaBackgroundMusicFix(IPlugin minstance) {
        mParent = minstance;
        if (PreloaderCore.enableWatchdogBGM > 0 && Utils.isClient()) {
            mParent.log("[BGM] Registering BGM delay Fix.");
            enabled = true;
            mFixInstance = new MusicTocker(mParent);
        } else if (PreloaderCore.enableWatchdogBGM > 0 && Utils.isServer()) {
            mParent.log("[BGM] Tried registering BGM delay Fix on Server, disabling.");
            enabled = false;
        } else {
            mParent.log("[BGM] Not registering BGM delay Fix.");
            enabled = false;
        }
    }

    @Override
    public boolean isFixValid() {
        return enabled;
    }

    public void manage() {
        if (PreloaderCore.enableWatchdogBGM > 0 && Utils.isClient()) {
            TimerTask task = new ManageTask(this.mFixInstance);
            Timer timer = new Timer("BGM-WatchDog");
            long delay = 1000 * 60;
            timer.scheduleAtFixedRate(task, delay, 5000);
        }
    }

    private static class ManageTask extends TimerTask {

        private final MusicTocker A;

        public ManageTask(MusicTocker a) {
            A = a;
        }

        @Override
        public void run() {
            if (PreloaderCore.enableWatchdogBGM > 0 && Utils.isClient()) {
                if (!A.mVanillaManager) {
                    A.run();
                }
            }
        }
    }
}
