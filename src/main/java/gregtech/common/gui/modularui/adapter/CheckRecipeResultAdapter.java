package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public class CheckRecipeResultAdapter implements IByteBufAdapter<CheckRecipeResult> {

    @Override
    public CheckRecipeResult deserialize(PacketBuffer buffer) throws IOException {
        String id = NetworkUtils.readStringSafe(buffer);
        CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(id)
            .newInstance();
        result.decode(buffer);
        return result;
    }

    @Override
    public void serialize(PacketBuffer buffer, CheckRecipeResult result) throws IOException {
        NetworkUtils.writeStringSafe(buffer, result.getID());
        result.encode(buffer);
    }

    @Override
    public boolean areEqual(@NotNull CheckRecipeResult t1, @NotNull CheckRecipeResult t2) {
        return t1.equals(t2);
    }
}
