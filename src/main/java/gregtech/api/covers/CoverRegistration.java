package gregtech.api.covers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;

public final class CoverRegistration {

    // Keeping an ItemStack reference so it remains valid through world load item remaps.
    private final ItemStack coverIdStack;
    private final CoverFactory factory;
    private final CoverPlacer coverPlacer;

    public CoverRegistration(ItemStack coverIdStack, CoverFactory factory, CoverPlacer coverPlacer) {
        this.coverIdStack = coverIdStack;
        this.factory = factory;
        this.coverPlacer = coverPlacer;
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable) {
        return buildCoverFromContext(side, coverable, null);
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable, @NotNull NBTTagCompound nbt) {
        return buildCoverFromContext(side, coverable, nbt);
    }

    public Cover buildCover(ForgeDirection side, ICoverable coverable, @NotNull ByteArrayDataInput data) {
        return buildCoverFromContext(side, coverable, data);
    }

    public Cover buildCover(@NotNull ForgeDirection side, ICoverable coverable, @NotNull ItemStack coverItem) {
        return buildCoverFromContext(side, coverable, coverItem);
    }

    private Cover buildCoverFromContext(ForgeDirection side, ICoverable coverable, Object initializer) {
        return factory.buildCover(new CoverContext(GTUtility.stackToInt(coverIdStack), side, coverable, initializer));
    }

    public CoverPlacer getCoverPlacer() {
        return coverPlacer;
    }
}
