package gregtech.api.enums;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.ICopy;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.github.bsideup.jabel.Desugar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Used as a bit set for {@link gregtech.api.metatileentity.implementations.MTEMultiBlockBase#structureErrors}. You can
 * reorder these as needed.
 */
public enum StructureError {

    WRONG_BLOCK,
    BLOCK_NOT_LOADED,
    MISSING_MAINTENANCE,
    MISSING_MUFFLER,
    UNNEEDED_MUFFLER,
    TOO_FEW_CASINGS,
    MISSING_CRYO_HATCH,
    TOO_MANY_CRYO_HATCHES,
    MISSING_STEAM_HATCH,
    MISSING_STRUCTURE_WRAPPER_CASINGS;

    public String getLocalizedString(NBTTagCompound context) {
        switch (this) {
            case WRONG_BLOCK:
                return StatCollector.translateToLocalFormatted(
                    "GT5U.gui.wrong_block",
                    context.getInteger("x"),
                    context.getInteger("y"),
                    context.getInteger("z"));
            case BLOCK_NOT_LOADED:
                return StatCollector.translateToLocal("GT5U.gui.not_loaded");
        }
        return this.name();
    }

}

//class StructureErrorManager {
//    private static int id = 0;
//    static int getId() {
//        return id++;
//    }
//}
//
//abstract class NewStructureError implements ICopy<NewStructureError>, IByteBufAdapter<NewStructureError> {
//    private static final List<Function<NBTTagCompound, NewStructureError>> deserializers = new ArrayList<>(StructureError.values().length);
//
//    protected static void registerDeserializerNBT(StructureError error, Function<NBTTagCompound, NewStructureError> deserializer) {
//        deserializers.set(error.ordinal(), deserializer);
//    }
//
//    abstract public StructureError getError();
//    abstract public IWidget createWidget();
//    abstract protected NBTTagCompound serializeNBT();
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public NewStructureError deserialize(PacketBuffer buffer) throws IOException {
//        NBTTagCompound tag = buffer.readNBTTagCompoundFromBuffer();
//        return deserializers.get(tag.getInteger("type")).apply(tag.getCompoundTag("data"));
//    }
//
//    @Override
//    public void serialize(PacketBuffer buffer, NewStructureError u) throws IOException {
//        NBTTagCompound tag = new NBTTagCompound();
//        tag.setInteger("type", getError().ordinal());
//        tag.setTag("data", u.serializeNBT());
//        buffer.writeNBTTagCompoundToBuffer(tag);
//    }
//
//}
//
//class BlockNotLoaded extends NewStructureError {
//
//    @Override
//    public StructureError getError() {
//        return StructureError.BLOCK_NOT_LOADED;
//    }
//
//    @Override
//    public IWidget createWidget() {
//        return IKey.str(StatCollector.translateToLocal("GT5U.gui.block_not_loaded")).asWidget();
//    }
//
//    @Override
//    protected NBTTagCompound serializeNBT() {
//        return null;
//    }
//
//    static {
//        registerDeserializerNBT(StructureError.BLOCK_NOT_LOADED, (tag) -> new BlockNotLoaded());
//    }
//
//    @Override
//    public NewStructureError createDeepCopy(NewStructureError newStructureError) {
//        return newStructureError;
//    }
//
//    @Override
//    public boolean areEqual(@NotNull NewStructureError t1, @NotNull NewStructureError t2) {
//        return true;
//    }
//}
