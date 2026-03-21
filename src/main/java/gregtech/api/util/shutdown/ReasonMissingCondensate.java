package gregtech.api.util.shutdown;

import static gregtech.api.util.GTUtility.translate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import appeng.api.storage.data.IAEFluidStack;
import appeng.util.item.AEFluidStack;
import cpw.mods.fml.common.network.ByteBufUtils;

public class ReasonMissingCondensate implements ShutDownReason {

    private IAEFluidStack stack;

    public ReasonMissingCondensate(IAEFluidStack stack) {
        this.stack = stack;
    }

    @NotNull
    @Override
    public String getID() {
        return "bec";
    }

    @Override
    public String getKey() {
        return getID();
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return translate("GT5U.gui.text.no_condensate", stack.getDisplayName(), stack.getStackSize());
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        if (stack != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            tag.setTag("stack", stackTag);
        }

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        stack = tag.hasKey("stack") ? AEFluidStack.loadFluidStackFromNBT(tag.getCompoundTag("stack")) : null;
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new ReasonMissingCondensate(null);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeBoolean(stack != null);
        if (stack != null) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.writeToNBT(tag);
            ByteBufUtils.writeTag(buffer, tag);
        }
    }

    @Override
    public void decode(PacketBuffer buffer) {
        if (buffer.readBoolean()) {
            stack = AEFluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(buffer));
        }
    }

    @Override
    public boolean wasCritical() {
        return true;
    }
}
