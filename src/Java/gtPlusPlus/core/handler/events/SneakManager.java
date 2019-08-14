package gtPlusPlus.core.handler.events;

import java.util.concurrent.ConcurrentHashMap;

import gtPlusPlus.api.objects.Logger;
import net.minecraft.entity.player.EntityPlayer;

public class SneakManager {

	//We make this a singleton for clientside data storage.

	public static ConcurrentHashMap<String, SneakManager> mPlayerCache = new ConcurrentHashMap<String, SneakManager>();
	
	private static void addPlayer(EntityPlayer aPlayer) {
		String aKey = getKey(aPlayer);
		if (!mPlayerCache.containsKey(aKey)) {
			mPlayerCache.put(aKey, new SneakManager(aPlayer));
		}		
	}
	
	public static SneakManager get(EntityPlayer aPlayer) {
		
		String aKey = getKey(aPlayer);
		if (!mPlayerCache.containsKey(aKey)) {
			addPlayer(aPlayer);
		}		
		return mPlayerCache.get(aKey);
	}
	
	private static String getKey(EntityPlayer aPlayer) {
		return ""+aPlayer.getGameProfile().getId().toString();
	}
	
	
	public SneakManager instance;
	public EntityPlayer owner;
	public boolean		canSprint		= true;
	public boolean		isSneaking		= true;
	public boolean		optionDoubleTap			= true;
	public boolean		wasSprintDisabled		= false;

	private static State Sprinting = State.ON;
	private static State Crouching = State.OFF;
	
	public SneakManager(EntityPlayer aPlayer) {
		owner = aPlayer;
	}

	public boolean Sneaking(){
		return Crouching.getState();
	}

	public boolean Sprinting(){
		return Sprinting.getState();
	}

	public State getSneakingState(){
		return Crouching;
	}

	public State getSprintingDisabledState(){
		return Sprinting;
	}

	public void toggleSneaking(){
		toggleState(Crouching);
	}

	public void toggleSprinting(){
		toggleState(Sprinting);
	}

	private State toggleState(final State state){
		Logger.INFO("State Toggle");
		if (state == State.ON) {
			return State.OFF;
		}
		return State.ON;
	}

	public State setCrouchingStateON(){
		return Crouching = State.ON;
	}

	public State setCrouchingStateOFF(){
		return Crouching = State.OFF;
	}

	public State setSprintingStateON(){
		return Sprinting = State.ON;
	}

	public State setSprintingStateOFF(){
		return Sprinting = State.OFF;
	}

	public static enum State {
		ON(true),
		OFF(false);

		private final boolean STATE;
		private State (final boolean State)
		{
			this.STATE = State;
		}

		public boolean getState() {
			return this.STATE;
		}

	}

}

