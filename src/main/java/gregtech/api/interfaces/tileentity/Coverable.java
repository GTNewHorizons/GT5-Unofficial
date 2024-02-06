package gregtech.api.interfaces.tileentity;

import javax.annotation.Nonnull;

import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface Coverable {

    boolean canPlaceCover(@Nonnull ForgeDirection side, int id);

    boolean canPlaceCover(@Nonnull ForgeDirection side, @Nonnull ItemStack item);

    boolean dropCover(@Nonnull ForgeDirection side, @Nonnull ForgeDirection droppedSide, boolean forced);

    void setCover(@Nonnull ForgeDirection side, int id, @Nonnull ISerializableObject data);

    void setCover(@Nonnull ForgeDirection side, @Nonnull ItemStack item);

    @Nonnull
    CoverInfo getCoverInfo(@Nonnull ForgeDirection side);

    @Nonnull
    ISerializableObject getComplexCoverData(@Nonnull ForgeDirection side);


}


