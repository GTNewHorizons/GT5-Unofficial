package gregtech.nei.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.network.NetworkUtils;

import gregtech.api.util.item.GhostCircuitItemStackHandler;

/**
 * What the client asks the machine for when the player uses the NEI "+" button. The server checks everything again,
 * so this only describes the recipe and how many crafts the player wants.
 *
 * @param ingredients   item inputs, without the programmed circuit and fluids
 * @param fluid         fluid used per craft, null if the recipe uses none
 * @param circuitConfig circuit configuration of the recipe, {@link GhostCircuitItemStackHandler#NO_CONFIG} if it has
 *                      none
 * @param crafts        crafts for {@link Mode#ADD}
 */
record RecipeTransferRequest(List<Ingredient> ingredients, FluidStack fluid, int circuitConfig, Mode mode, int crafts) {

    enum Mode {
        /** As many crafts as fit, counting the ones already in the machine. */
        FILL,
        /** This many crafts more than the complete crafts already in the machine, or as many as possible up to it. */
        ADD
    }

    private static final int MAX_INGREDIENTS = 64;
    private static final int MAX_ALTERNATIVES = 256;

    /**
     * One item input of the recipe.
     *
     * @param alternatives the stacks NEI accepts for this input, with stack size 1
     * @param amount       items used per craft, 0 if the item is not consumed (molds, shapes, lenses)
     */
    record Ingredient(List<ItemStack> alternatives, int amount) {

        boolean isConsumed() {
            return amount > 0;
        }
    }

    RecipeTransferRequest {
        ingredients = Collections.unmodifiableList(ingredients);
    }

    void write(PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(ingredients.size());
        for (Ingredient ingredient : ingredients) {
            buffer.writeVarIntToBuffer(ingredient.amount());
            buffer.writeVarIntToBuffer(
                ingredient.alternatives()
                    .size());
            for (ItemStack alternative : ingredient.alternatives()) {
                NetworkUtils.writeItemStack(buffer, alternative);
            }
        }
        NetworkUtils.writeFluidStack(buffer, fluid);
        // Shifted by one, since NO_CONFIG is -1 and a negative var int takes five bytes
        buffer.writeVarIntToBuffer(circuitConfig + 1);
        buffer.writeVarIntToBuffer(mode.ordinal());
        buffer.writeVarIntToBuffer(crafts);
    }

    /** Reads a request, or returns null if it is malformed or too large. */
    static RecipeTransferRequest read(PacketBuffer buffer) {
        int ingredientCount = buffer.readVarIntFromBuffer();
        if (ingredientCount < 0 || ingredientCount > MAX_INGREDIENTS) return null;

        List<Ingredient> ingredients = new ArrayList<>(ingredientCount);
        for (int i = 0; i < ingredientCount; i++) {
            int amount = buffer.readVarIntFromBuffer();
            int alternativeCount = buffer.readVarIntFromBuffer();
            if (amount < 0 || alternativeCount <= 0 || alternativeCount > MAX_ALTERNATIVES) return null;

            List<ItemStack> alternatives = new ArrayList<>(alternativeCount);
            for (int j = 0; j < alternativeCount; j++) {
                ItemStack alternative = NetworkUtils.readItemStack(buffer);
                if (alternative == null) return null;
                alternatives.add(alternative);
            }
            ingredients.add(new Ingredient(alternatives, amount));
        }

        FluidStack fluid = NetworkUtils.readFluidStack(buffer);
        if (fluid != null && fluid.amount <= 0) fluid = null;
        int circuitConfig = buffer.readVarIntFromBuffer() - 1;
        int mode = buffer.readVarIntFromBuffer();
        int crafts = buffer.readVarIntFromBuffer();
        if (mode < 0 || mode >= Mode.values().length || crafts < 0) return null;
        return new RecipeTransferRequest(ingredients, fluid, circuitConfig, Mode.values()[mode], crafts);
    }
}
