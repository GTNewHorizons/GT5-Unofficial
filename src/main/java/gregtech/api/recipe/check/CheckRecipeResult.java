package gregtech.api.recipe.check;

import javax.annotation.Nonnull;

import net.minecraft.network.PacketBuffer;

/**
 * Class to indicate the result of recipe check in the machine. It doesn't need to be actual result of recipemap check,
 * but can also be status of whether to start the machine. Examples can be found at {@link CheckRecipeResultRegistry}.
 * <p>
 * Sample instance must be registered to {@link CheckRecipeResultRegistry}.
 */
public interface CheckRecipeResult {

    /**
     * @return Unique registry ID
     */
    @Nonnull
    String getID();

    /**
     * @return If recipe check is successful
     */
    boolean wasSuccessful();

    /**
     * @return Actual text to show on client GUI
     */
    @Nonnull
    String getDisplayString();

    /**
     * Create new instance to receive packet.
     */
    @Nonnull
    CheckRecipeResult newInstance();

    /**
     * Encode value to sync.
     */
    void encode(@Nonnull PacketBuffer buffer);

    /**
     * Decode synced value.
     */
    void decode(@Nonnull PacketBuffer buffer);
}
