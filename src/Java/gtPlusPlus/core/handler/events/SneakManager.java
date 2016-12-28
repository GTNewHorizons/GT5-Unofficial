package gtPlusPlus.core.handler.events;

import net.minecraft.client.Minecraft;

public class SneakManager {
	
	//We make this a singleton for clientside data storage.
	public static SneakManager instance = new SneakManager();
	protected static final Minecraft mc = Minecraft.getMinecraft();
	public static boolean		canSprint		= true;
	public static boolean		isSneaking		= true;
	public static boolean		optionDoubleTap			= false;
	public static boolean		wasSprintDisabled		= false;
	
	private static State Sprinting = State.OFF;
	private static State Crouching = State.OFF;
	
	public static boolean Sneaking(){
		return Crouching.getState();
	}
	
	public static boolean SprintingDisabled(){
		return Sprinting.getState();
	}
	
	public static State getSneakingState(){
		return Crouching;
	}
	
	public static State getSprintingDisabledState(){
		return Sprinting;
	}
	
	public static void toggleSneaking(){
		toggleState(Crouching);
	}
	
	public static void toggleSprinting(){
		toggleState(Sprinting);
	}
	
	private static State toggleState(State state){
		if (state == State.ON)
		return state = State.OFF;
		return state = State.ON;
	}
	
	public static State setStateON(State state1){
		return state1 = State.ON;
	}
	
	public static State setStateOFF(State state1){
		return state1 = State.OFF;
	}
	
	 public static enum State {
	    	ON(true),
	    	OFF(false);
	    
	    	private boolean STATE;
	    	private State (final boolean State)
	    	{
	    		this.STATE = State;
	    	}

	    	public boolean getState() {
	    		return STATE;
	    	}
	    
	    }
	
}

