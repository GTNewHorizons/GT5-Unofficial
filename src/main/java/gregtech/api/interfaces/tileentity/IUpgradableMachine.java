package gregtech.api.interfaces.tileentity;

import org.jetbrains.annotations.ApiStatus;

/**
 * To access my Machines a bit easier
 */
public interface IUpgradableMachine extends IMachineProgress {

    /**
     * Accepts Upgrades. Some Machines have an Upgrade Limit.
     */
    boolean isUpgradable();

    /**
     * Accepts Muffler Upgrades
     */
    boolean isMufflerUpgradable();

    /**
     * Accepts Steam-Converter Upgrades
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default boolean isSteamEngineUpgradable() {
        return false;
    }

    /**
     * Adds Muffler Upgrade
     */
    boolean addMufflerUpgrade();

    /**
     * Adds MJ-Converter Upgrade
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default boolean addSteamEngineUpgrade() {
        return false;
    }

    /**
     * Does this Machine have an Muffler
     */
    boolean hasMufflerUpgrade();

    /**
     * Does this Machine have a Steam-Converter
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default boolean hasSteamEngineUpgrade() {
        return false;
    }
}
