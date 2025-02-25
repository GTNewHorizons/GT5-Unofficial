package gregtech.api.covers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.Cover;

public class CoverRegistration<T extends ISerializableObject> {

    private final int coverId;
    private final CoverFactory factory;
    private final CoverPlacer coverPlacer;
    private static final String NBT_SIDE = "s";

    public CoverRegistration(int coverId, CoverFactory factory, CoverPlacer coverPlacer) {
        this.coverId = coverId;
        this.factory = factory;
        this.coverPlacer = coverPlacer;
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable) {
        return buildCoverFromContext(side, coverable, null);
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable, ISerializableObject data) {
        return buildCoverFromContext(side, coverable, data);
    }

    public Cover buildCover(ICoverable coverable, @NotNull NBTTagCompound nbt) {
        return buildCoverFromContext(ForgeDirection.getOrientation(nbt.getByte(NBT_SIDE)), coverable, nbt);
    }

    public Cover buildCover(ICoverable coverable, @NotNull ByteArrayDataInput data) {
        return buildCoverFromContext(ForgeDirection.getOrientation(data.readByte()), coverable, data);
    }

    public Cover buildCover(@NotNull ForgeDirection side, ICoverable coverable, @NotNull ItemStack cover) {
        return buildCoverFromContext(side, coverable, cover);
    }

    private Cover buildCoverFromContext(ForgeDirection side, ICoverable coverable, Object initializer) {
        return factory.buildCover(new CoverContext(coverId, side, coverable, initializer));
    }

    public CoverPlacer getCoverPlacer() {
        return coverPlacer;
    }
}
