package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * The WRITE command (programming framework, Phase B) — the data-OUT channel of the 256-slot USS variable space
 * (user spec: "Voidcraft can write … to a global variable list … communicating data out of the USS").
 *
 * <p>
 * Params: {@code value} — either a plain string or a nested {@link USSValue} compound (LITERAL / VAR / STAT —
 * the compound form is what lets a ship write a stat or another variable out); {@code slot} (0..255, default 0).
 * Immediate (DONE at begin; never in flight).
 */
public final class USSCommandWrite implements USSCommandHandler {

    /** Param key: the value (string, or a nested USSValue compound). */
    public static final String PARAM_VALUE = "value";
    /** Param key: the destination slot (0..255). */
    public static final String PARAM_SLOT = "slot";

    @Override
    public int commandId() {
        return USSCommand.WRITE;
    }

    @Override
    public USSCommandStatus begin(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        NBTTagCompound p = node.params();
        int slot = p.hasKey(PARAM_SLOT) ? p.getInteger(PARAM_SLOT) : 0;
        NBTBase raw = p.getTag(PARAM_VALUE);
        String value;
        if (raw instanceof NBTTagCompound) {
            value = ctx.resolve(USSValue.readFromNBT((NBTTagCompound) raw));
        } else {
            value = p.getString(PARAM_VALUE);
        }
        ctx.writeVar(slot, value == null ? "" : value);
        return USSCommandStatus.DONE;
    }

    @Override
    public USSCommandStatus tick(USSExecutionContext ctx, USSNode node, NBTTagCompound state) {
        return USSCommandStatus.DONE; // immediate command — never in flight
    }
}
