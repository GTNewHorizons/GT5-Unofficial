package gtPlusPlus.core.handler.events;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;

import gtPlusPlus.api.objects.Logger;

public class SneakManager {

    // We make this a singleton for clientside data storage.

    public static ConcurrentHashMap<String, SneakManager> mPlayerCache = new ConcurrentHashMap<>();

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
        return aPlayer.getGameProfile()
            .getId()
            .toString();
    }

    public SneakManager instance;
    public EntityPlayer owner;
    public boolean canSprint = true;
    public boolean isSneaking = true;
    public boolean optionDoubleTap = true;
    public boolean wasSprintDisabled = false;
    public boolean mIsWearingRing = false;

    private State Sprinting = State.ON;
    private State Crouching = State.OFF;

    public SneakManager(EntityPlayer aPlayer) {
        owner = aPlayer;
    }

    public boolean Sneaking() {
        return Crouching.getState();
    }

    public boolean Sprinting() {
        return Sprinting.getState();
    }

    public State getSneakingState() {
        return Crouching;
    }

    public State getSprintingDisabledState() {
        return Sprinting;
    }

    public void toggleSneaking() {
        toggleState(Crouching);
    }

    public void toggleSprinting() {
        toggleState(Sprinting);
    }

    private State toggleState(final State state) {
        Logger.INFO("State Toggle");
        if (state == State.ON) {
            return State.OFF;
        }
        return State.ON;
    }

    private State setCrouchingStateON() {
        return Crouching = State.ON;
    }

    private State setCrouchingStateOFF() {
        return Crouching = State.OFF;
    }

    private State setSprintingStateON() {
        return Sprinting = State.ON;
    }

    private State setSprintingStateOFF() {
        return Sprinting = State.OFF;
    }

    public void putRingOn() {
        mIsWearingRing = true;
        setSprintingStateOFF();
        setCrouchingStateON();
    }

    public void takeRingOff() {
        mIsWearingRing = false;
        setSprintingStateON();
        setCrouchingStateOFF();
    }

    public boolean isWearingRing() {
        return mIsWearingRing;
    }

    public enum State {

        ON(true),
        OFF(false);

        private final boolean STATE;

        State(final boolean State) {
            this.STATE = State;
        }

        public boolean getState() {
            return this.STATE;
        }
    }
}
