package tectech.thing.metaTileEntity.hatch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

/// A hatch that emits or receives a redstone signal. This does not perform any redstone logic.
public abstract class MTEHatchConfigurableBase extends MTEBaseFactoryHatch implements IDataCopyable {

    public MTEHatchConfigurableBase(int id, String name, int tier, String[] description) {
        super(id, name, tier, description);
    }

    public MTEHatchConfigurableBase(MTEHatchConfigurableBase prototype) {
        super(prototype);
    }

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity igte);

    protected abstract void saveConfig(NBTTagCompound tag);

    protected abstract void loadConfig(@Nullable NBTTagCompound tag);

    @Override
    public abstract String getCopiedDataIdentifier(EntityPlayer player);

    /// When true, this hatch's config will be synced to the client via description packets. []
    public boolean needsConfigSync() {
        return false;
    }

    /// Syncs this hatch's config at some point in the near future.
    public void requestConfigSync() {
        if (getBaseMetaTileEntity() != null) {
            getBaseMetaTileEntity().issueTileUpdate();
        }
    }

    public void setOutput(boolean active) {
        IGregTechTileEntity igte = getBaseMetaTileEntity();

        if (igte == null || igte.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            igte.setStrongOutputRedstoneSignal(dir, (byte) 0);
        }

        igte.setStrongOutputRedstoneSignal(igte.getFrontFacing(), (byte) (active ? 15 : 0));
        igte.setActive(active);
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        if (needsConfigSync()) {
            NBTTagCompound config = new NBTTagCompound();
            saveConfig(config);
            tag.setTag("config", config);
        }

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);

        if (needsConfigSync()) {
            loadConfig(data.getCompoundTag("config"));
        }
    }

    @Override
    public void onLeftclick(IGregTechTileEntity igte, EntityPlayer player) {
        if (!(player instanceof EntityPlayerMP)) return;

        ItemStack heldItem = player.getHeldItem();
        if (!ItemList.Tool_DataStick.isStackEqual(heldItem, false, true)) return;

        heldItem.setTagCompound(getCopiedData(player));
        heldItem.setStackDisplayName(EnumChatFormatting.RESET + getStackForm(1).getDisplayName() + " Config");

        GTUtility.sendChatTrans(player, "GT5U.machines.controller_hatch.saved");
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity igte, EntityPlayer player, ForgeDirection side, float aX, float aY,
        float aZ) {
        ItemStack heldItem = player.getHeldItem();
        if (!ItemList.Tool_DataStick.isStackEqual(heldItem, false, true)) {
            openGui(player);
            return true;
        }

        // intentionally run on the client so that the player's arm swings
        if (pasteCopiedData(player, heldItem.getTagCompound())) {
            if (GTUtility.isServer()) {
                GTUtility.sendChatTrans(player, "GT5U.machines.controller_hatch.loaded");
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", getCopiedDataIdentifier(player));
        saveConfig(tag);
        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (!nbt.getString("type")
            .equals(getCopiedDataIdentifier(player))) return false;

        loadConfig(nbt);

        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagCompound config = new NBTTagCompound();
        saveConfig(config);
        aNBT.setTag("config", config);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        loadConfig(aNBT.getCompoundTag("config"));
    }
}
