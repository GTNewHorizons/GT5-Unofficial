package gregtech.api.interfaces.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public interface Coverable {

    boolean canPlaceCover(@Nonnull final ForgeDirection side, final int id);

    boolean canPlaceCover(@Nonnull final ForgeDirection side, @Nonnull final ItemStack itemCover);

    boolean dropCover(@Nonnull final ForgeDirection side, @Nonnull final ForgeDirection droppedSide, final boolean forced);

    void setCover(@Nonnull final ForgeDirection side, final int id, @Nonnull final ISerializableObject data);

    void setCover(@Nonnull final ForgeDirection side, @Nonnull final ItemStack itemCover);

    @Nullable
    CoverInfo getCoverInfo(@Nonnull final ForgeDirection side);

    void receiveCoverData(@Nonnull final ForgeDirection coverSide, final int aCoverID, @Nonnull final ISerializableObject aCoverData,
        @Nonnull final EntityPlayerMP aPlayer);
}
