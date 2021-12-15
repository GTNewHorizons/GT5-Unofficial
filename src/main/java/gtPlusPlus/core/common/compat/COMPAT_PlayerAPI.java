package gtPlusPlus.core.common.compat;

import api.player.client.ClientPlayerAPI;
import gtPlusPlus.core.common.BasePlayer;
import gtPlusPlus.core.handler.events.SneakManager;
import gtPlusPlus.core.util.Utils;

public class COMPAT_PlayerAPI {

	public static class commonProxy{
		public static void initPre(){

		}

		public static void Init(){

		}

		public static void initPost(){

		}
	}

	public static class clientProxy{
		public static void initPre(){
			//Utils.registerEvent(SneakManager.instance);
		}

		public static void Init(){
			ClientPlayerAPI.register("SneakManager", BasePlayer.class);
		}

		public static void initPost(){

		}
	}

	public static class serverProxy{
		public static void initPre(){

		}

		public static void Init(){

		}

		public static void initPost(){

		}
	}

}
