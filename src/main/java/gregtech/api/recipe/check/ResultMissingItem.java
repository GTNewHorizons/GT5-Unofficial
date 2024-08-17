package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import cpw.mods.fml.common.network.ByteBufUtils;

public class ResultMissingItem implements CheckRecipeResult {

    @Nullable
    public ItemStack itemStack;

    public ResultMissingItem() {

    }

    public ResultMissingItem(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    @Nonnull
    public String getID() {
        return "missing_item";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return Objects.requireNonNull(
            I18n.format("GT5U.gui.text.missing_item", itemStack != null ? itemStack.getDisplayName() : "null"));
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new ResultMissingItem(itemStack != null ? itemStack.copy() : null);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        ByteBufUtils.writeItemStack(buffer, itemStack);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.itemStack = ByteBufUtils.readItemStack(buffer);
    }
}
