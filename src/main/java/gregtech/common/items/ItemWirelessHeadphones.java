package gregtech.common.items;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gregtech.api.enums.ItemList;
import gregtech.api.items.GTGenericItem;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.MTEBetterJukebox;

public class ItemWirelessHeadphones extends GTGenericItem implements IBauble {

    public static final String NBTKEY_JUKEBOX_COORDINATES = "jukeboxCoords";

    public ItemWirelessHeadphones() {
        super("WirelessHeadphones", "Wireless Headphones", null);
        setMaxStackSize(1);

        ItemList.WirelessHeadphones.set(this);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        if (aStack == null) {
            return;
        }
        final NBTTagCompound tag = aStack.getTagCompound();
        if (tag == null || !tag.hasKey(NBTKEY_JUKEBOX_COORDINATES, Constants.NBT.TAG_STRING)) {
            aList.add(StatCollector.translateToLocal("GT5U.machines.betterjukebox.headphonesunbound"));
        } else {
            aList.add(StatCollector.translateToLocal("GT5U.machines.betterjukebox.headphonesbound"));
            aList.add(tag.getString(NBTKEY_JUKEBOX_COORDINATES));
        }
    }

    @Override
    public boolean doesSneakBypassUse(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        final TileEntity pointedTe = world.getTileEntity(x, y, z);
        if (!(pointedTe instanceof BaseMetaTileEntity mte)) {
            return false;
        }
        if (!(mte.getMetaTileEntity() instanceof MTEBetterJukebox jukebox)) {
            return false;
        }
        final UUID uuid = jukebox.jukeboxUuid;
        if (uuid == MTEBetterJukebox.UNSET_UUID) {
            return false;
        }
        if (!world.isRemote) {
            final NBTTagCompound tag = (stack.getTagCompound() == null) ? new NBTTagCompound() : stack.getTagCompound();
            tag.setLong(MTEBetterJukebox.NBTKEY_UUID_LOW, uuid.getLeastSignificantBits());
            tag.setLong(MTEBetterJukebox.NBTKEY_UUID_HIGH, uuid.getMostSignificantBits());
            tag.setString(
                NBTKEY_JUKEBOX_COORDINATES,
                String.format("(%d, %d, %d) @ %d", x, y, z, world.provider.dimensionId));
            stack.setTagCompound(tag);

            player.addChatMessage(new ChatComponentTranslation("GT5U.machines.betterjukebox.headphonesbound"));
        }
        return true;
    }

    public static UUID getBoundJukeboxUUID(ItemStack stack) {
        if (stack == null || stack.getTagCompound() == null) {
            return null;
        }
        final NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey(MTEBetterJukebox.NBTKEY_UUID_LOW, Constants.NBT.TAG_ANY_NUMERIC)
            || !tag.hasKey(MTEBetterJukebox.NBTKEY_UUID_HIGH, Constants.NBT.TAG_ANY_NUMERIC)) {
            return null;
        }
        final long idLow = tag.getLong(MTEBetterJukebox.NBTKEY_UUID_LOW);
        final long idHigh = tag.getLong(MTEBetterJukebox.NBTKEY_UUID_HIGH);
        return new UUID(idHigh, idLow);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {}

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {}

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {}

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }
}
