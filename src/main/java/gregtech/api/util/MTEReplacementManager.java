package gregtech.api.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.gtnewhorizons.postea.utility.PosteaUtilities;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.MetaTileEntityIDs;
import vexatos.tgregworks.reference.Mods;

/**
 * Hybrid TileEntity + ItemStack replacement manager for replacing
 * TileEntities from other mods with GT MetaTileEntities.
 */
public class MTEReplacementManager {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String tileId;
        private String itemId;

        private MetaTileEntityIDs mteId;
        private Function<Integer, MetaTileEntityIDs> mteIdStackTransformer;

        private BiConsumer<NBTTagCompound, NBTTagCompound> tileNbtTransformer;
        private Consumer<NBTTagCompound> itemNbtTransformer;

        private Builder() {}

        public Builder fromTile(String tileId) {
            this.tileId = tileId;
            return this;
        }

        public Builder fromItem(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder toMTE(MetaTileEntityIDs mteId) {
            this.mteId = mteId;
            return this;
        }

        /**
         * Map an ItemStack's metadata value to an MTE ID. Useful for converting meta items to MTEs.
         *
         * @param mteIdStackTransformer A mapping of ItemStack metadata to MTE ID.
         */
        public Builder toMTE(Function<Integer, MetaTileEntityIDs> mteIdStackTransformer) {
            this.mteIdStackTransformer = mteIdStackTransformer;
            return this;
        }

        /**
         * Add custom TileEntity NBT conversion logic from the old tag to the new tag.
         *
         * @param tileNbtTransformer A BiConsumer of [Old TileEntity NBT, new MTE NBT]
         */
        public Builder withCustomTileNBT(BiConsumer<NBTTagCompound, NBTTagCompound> tileNbtTransformer) {
            this.tileNbtTransformer = tileNbtTransformer;
            return this;
        }

        /**
         * Add custom ItemStack NBT conversion logic from the old tag to the new tag.
         * The provided tag will be the ItemStack's NBT tag, NOT the save data NBT tag!
         *
         * @param itemNbtTransformer A Consumer of [ItemStack NBT tag]
         */
        public Builder withCustomItemStackNBT(Consumer<NBTTagCompound> itemNbtTransformer) {
            this.itemNbtTransformer = itemNbtTransformer;
            return this;
        }

        public void register() {
            if (this.tileId != null) registerTile();
            if (this.itemId != null) registerItem();
        }

        private void registerTile() {
            if (this.mteId == null) {
                GTLog.err.println(
                    "Could not apply Postea transformer for " + this.tileId
                        + " as no MTE mapping was specified, skipping");
                return;
            }

            BlockInfo blockInfo = new BlockInfo(GregTechAPI.sBlockMachines, this.mteId.ID, tileTag -> {
                NBTTagCompound newTag = PosteaUtilities.cleanseNBT(this.tileId, tileTag);

                newTag.setInteger("mID", this.mteId.ID);
                newTag.setInteger("nbtVersion", GTMod.NBT_VERSION);

                if (this.tileNbtTransformer != null) {
                    this.tileNbtTransformer.accept(tileTag, newTag);
                }

                // Set required tags if the custom NBT transformer did not apply them
                if (!newTag.hasKey("mOwnerName")) {
                    newTag.setString("mOwnerName", "Postea");
                }
                if (!newTag.hasKey("mOwnerUuid")) {
                    newTag.setString("mOwnerUuid", "");
                }
                if (!newTag.hasKey("mFacing")) {
                    newTag.setShort("mFacing", (short) ForgeDirection.NORTH.ordinal());
                }

                return newTag;
            });

            TileEntityReplacementManager.tileEntityTransformer(this.tileId, (tag, world) -> blockInfo);
        }

        private void registerItem() {
            if (this.mteIdStackTransformer == null && this.mteId == null) {
                GTLog.err.println(
                    "Could not apply Postea transformer for " + this.itemId
                        + ", as no MTE mapping specified, skipping");
                return;
            }

            Function<NBTTagCompound, NBTTagCompound> transformer = tag -> {
                int mteItemDamage;
                if (this.mteIdStackTransformer != null) {
                    // First try to get from the stack mapper
                    mteItemDamage = this.mteIdStackTransformer.apply(tag.getInteger("Damage")).ID;
                } else {
                    // Then try to get from the MTE ID
                    mteItemDamage = this.mteId.ID;
                }

                Item mteItem = GameRegistry.findItem(Mods.GregTech, "gt.blockmachines");
                int mteItemId = Item.getIdFromItem(mteItem);
                tag.setInteger("id", mteItemId);
                tag.setInteger("Damage", mteItemDamage);

                if (this.itemNbtTransformer != null) {
                    NBTTagCompound itemStackTag = tag.getCompoundTag("tag");
                    if (itemStackTag == null) {
                        itemStackTag = new NBTTagCompound();
                        tag.setTag("tag", itemStackTag);
                        this.itemNbtTransformer.accept(itemStackTag);
                    }
                }

                return tag;
            };

            ItemStackReplacementManager.addItemReplacement(this.itemId, transformer);
        }
    }
}
