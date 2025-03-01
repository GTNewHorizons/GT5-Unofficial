package gregtech.api.covers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.Cover;

public class CoverRegistration {

    private final int coverId;
    private final CoverFactory factory;
    private final CoverPlacer coverPlacer;

    public CoverRegistration(int coverId, CoverFactory factory, CoverPlacer coverPlacer) {
        this.coverId = coverId;
        this.factory = factory;
        this.coverPlacer = coverPlacer;
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable) {
        return buildCoverFromContext(side, coverable, null);
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable, @NotNull ISerializableObject data) {
        return buildCoverFromContext(side, coverable, data);
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable, @NotNull NBTTagCompound nbt) {
        return buildCoverFromContext(side, coverable, nbt);
    }

    public Cover buildCover(ICoverable coverable, @NotNull ByteArrayDataInput data) {
        return buildCoverFromContext(ForgeDirection.getOrientation(data.readByte()), coverable, data);
    }

    public Cover buildCover(@NotNull ForgeDirection side, ICoverable coverable, @NotNull ItemStack coverItem) {
        return buildCoverFromContext(side, coverable, coverItem);
    }

    private Cover buildCoverFromContext(ForgeDirection side, ICoverable coverable, Object initializer) {
        return factory.buildCover(new CoverContext(coverId, side, coverable, initializer));
    }

    public CoverPlacer getCoverPlacer() {
        return coverPlacer;
    }
}
