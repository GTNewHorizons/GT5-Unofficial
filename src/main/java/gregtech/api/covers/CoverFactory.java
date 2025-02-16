package gregtech.api.covers;

import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.DataSupplierCoverFactory;

public interface CoverFactory<T extends ISerializableObject> {

    static <T extends ISerializableObject> CoverFactory<T> ofData(Supplier<T> dataSupplier) {
        return new DataSupplierCoverFactory<>(dataSupplier);
    }

    T createDataObject();

    T initializeDataFromCover(ItemStack cover);

    T createDataObject(NBTBase aNBT);

    boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity);

    boolean isSimpleCover();

    void placeCover(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side);
}
