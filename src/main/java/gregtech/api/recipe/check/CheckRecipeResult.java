package gregtech.api.recipe.check;

import net.minecraft.network.PacketBuffer;

/**
 * Class to indicate the result of recipe check in the machine. Sample instance must be registered to
 * {@link CheckRecipeResultRegistry}.
 */
public interface CheckRecipeResult {

    /**
     * @return Unique registry ID
     */
    String getID();

    /**
     * @return If recipe check is successful
     */
    boolean wasSuccessful();

    /**
     * @return Actual text to show on client GUI
     */
    String getDisplayString();

    /**
     * Create new instance to receive packet.
     */
    CheckRecipeResult newInstance();

    /**
     * Encode value to sync.
     */
    void encode(PacketBuffer buffer);

    /**
     * Decode synced value.
     */
    void decode(PacketBuffer buffer);
}
