package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;

public interface CoverFactory<T extends ISerializableObject> {

    T createDataObject();

    T initializeDataFromCover(ItemStack cover);

    T createDataObject(NBTBase aNBT);

    boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity);

    boolean isSimpleCover();

    void placeCover(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side);
}
