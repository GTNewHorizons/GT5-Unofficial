package gregtech.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IDataCopyable {

    /**
     * Implementation will set the type field. caller can assume the returned tag (if not null) always contains a "type"
     * field
     * with a value of {@link #getCopiedDataIdentifier(EntityPlayer)}
     * Implementation should not try to alert player of situations that copy has failed.
     *
     * @return null if cannot copy (e.g. not properly configured yet) or the data to copy.
     */
    NBTTagCompound getCopiedData(EntityPlayer player);

    /**
     * Callee should check if the given tag is valid.
     * Implementation should not try to alert player of situations that paste has failed.
     *
     * @return true if pasted. false otherwise.
     */
    boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt);

    /**
     * @return the type identifier. this should be a constant for the given set of arguments.
     */
    String getCopiedDataIdentifier(EntityPlayer player);
}
