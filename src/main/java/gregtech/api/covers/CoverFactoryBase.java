package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;

public abstract class CoverFactoryBase<T extends ISerializableObject> implements CoverFactory<T> {

    @Override
    public T initializeDataFromCover(ItemStack cover) {
        return createDataObject();
    }

    @Override
    public T createDataObject(NBTBase nbt) {
        return createDataObject();
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack cover, ICoverable tileEntity) {
        return true;
    }

    @Override
    public boolean isSimpleCover() {
        return false;
    }

    /**
     * sets the Cover upon placement.
     */
    @Override
    public final void placeCover(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side) {
        tileEntity.setCoverIdAndDataAtSide(side, GTUtility.stackToInt(cover), initializeDataFromCover(cover));
        tileEntity.setCoverIdAndDataAtSide(side, GTUtility.stackToInt(cover), initializeDataFromCover(cover));
        onPlayerAttach(player, cover, tileEntity, side);
    }

    /**
     * Called when the cover is initially attached to a machine.
     *
     * @param player     The attaching player
     * @param cover      An {@link ItemStack} containing the cover
     * @param tileEntity The machine receiving the cover
     * @param side       Which side the cover is attached to
     */
    protected void onPlayerAttach(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side) {
        // Do nothing by default.
    }
}
