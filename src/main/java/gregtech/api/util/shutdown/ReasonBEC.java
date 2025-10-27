package gregtech.api.util.shutdown;

import static gregtech.api.util.GTUtility.translate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.network.ByteBufUtils;
import tectech.mechanics.boseEinsteinCondensate.CondensateStack;

public class ReasonBEC implements ShutDownReason {

    private BECError error;
    private CondensateStack stack;

    private enum BECError {
        NO_CONDENSATE,
        CLOGGED
    }

    public static ReasonBEC noCondensate(CondensateStack stack) {
        ReasonBEC reason = new ReasonBEC();

        reason.error = BECError.NO_CONDENSATE;
        reason.stack = stack;

        return reason;
    }

    public static ReasonBEC clogged() {
        ReasonBEC reason = new ReasonBEC();

        reason.error = BECError.CLOGGED;

        return reason;
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
        return switch (error) {
            case NO_CONDENSATE -> translate("GT5U.gui.text.no_condensate", stack.getDisplayName(), stack.amount);
            case CLOGGED -> translate("GT5U.gui.text.condensate_clogged");
        };
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setInteger("error", error.ordinal());
        if (stack != null) stack.writeToTag(tag);

        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        error = BECError.values()[tag.getInteger("error")];
        stack = tag.hasKey("n") ? CondensateStack.readFromTag(tag) : null;
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new ReasonBEC();
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(error.ordinal());

        buffer.writeBoolean(stack != null);
        if (stack != null) {
            ByteBufUtils.writeTag(buffer, stack.writeToTag(new NBTTagCompound()));
        }
    }

    @Override
    public void decode(PacketBuffer buffer) {
        error = BECError.values()[buffer.readInt()];

        if (buffer.readBoolean()) {
            stack = CondensateStack.readFromTag(ByteBufUtils.readTag(buffer));
        }
    }

    @Override
    public boolean wasCritical() {
        return true;
    }
}
