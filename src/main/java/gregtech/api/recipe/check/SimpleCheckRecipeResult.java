package gregtech.api.recipe.check;

import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

public class SimpleCheckRecipeResult implements CheckRecipeResult {

    private final boolean success;
    private final String id;

    private SimpleCheckRecipeResult(boolean success, String id) {
        this.success = success;
        this.id = id;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public boolean wasSuccessful() {
        return success;
    }

    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocal("GT5U.gui.text." + id);
    }

    @Override
    public CheckRecipeResult newInstance() {
        return new SimpleCheckRecipeResult(success, id);
    }

    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void decode(PacketBuffer buffer) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCheckRecipeResult that = (SimpleCheckRecipeResult) o;
        return success == that.success && Objects.equals(id, that.id);
    }

    /**
     * Creates new result object with successful state. Don't forget to register it to
     * {@link CheckRecipeResultRegistry}!
     */
    public static CheckRecipeResult ofSuccessFactory(String id) {
        return new SimpleCheckRecipeResult(true, id);
    }

    /**
     * Creates new result object with failed state. Don't forget to register it to
     * {@link CheckRecipeResultRegistry}!
     */
    public static CheckRecipeResult ofFailureFactory(String id) {
        return new SimpleCheckRecipeResult(false, id);
    }
}
