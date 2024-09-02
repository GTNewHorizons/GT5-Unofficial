package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.LOCKERS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_LOCKER;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.objects.GTItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTELocker extends MTETieredMachineBlock {

    private static final String CHARGE_SLOT_WAILA_TAG = "charge_slot_";
    public byte mType = 0;

    public MTELocker(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 4, "Stores and recharges Armor");
    }

    public MTELocker(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public MTELocker(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        desc[mDescriptionArray.length] = "Click with Screwdriver to change Style";
        return desc;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = { MACHINE_CASINGS[this.mTier][(i + 1)] };
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = { MACHINE_CASINGS[this.mTier][(i + 1)], OVERLAYS_ENERGY_IN[this.mTier] };
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = { MACHINE_CASINGS[this.mTier][(i + 1)], TextureFactory.of(OVERLAY_LOCKER) };
            rTextures[2][(i + 1)] = tmp2;
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            return new ITexture[] { this.mTextures[2][(colorIndex + 1)][0], this.mTextures[2][(colorIndex + 1)][1],
                LOCKERS[Math.abs(this.mType % LOCKERS.length)] };
        }
        return this.mTextures[0][(colorIndex + 1)];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELocker(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return (facing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return GTValues.V[this.mTier] * maxAmperesIn();
    }

    @Override
    public long maxEUInput() {
        return GTValues.V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return this.mInventory.length * 2L;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return getBaseMetaTileEntity().isAllowedToWork() ? this.mInventory.length : 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mType", this.mType);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mType = aNBT.getByte("mType");
    }

    @Override
    public void onValueUpdate(byte aValue) {
        this.mType = aValue;
    }

    @Override
    public byte getUpdateData() {
        return this.mType;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == 16) {
            GTUtility.doSoundAtClient(SoundResource.RANDOM_CLICK, 1, 1.0F);
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            this.mType = ((byte) (this.mType + 1));
        }
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if ((aBaseMetaTileEntity.isServerSide()) && (side == aBaseMetaTileEntity.getFrontFacing())) {
            for (int i = 0; i < 4; i++) {
                ItemStack tSwapStack = this.mInventory[i];
                this.mInventory[i] = aPlayer.inventory.armorInventory[i];
                aPlayer.inventory.armorInventory[i] = tSwapStack;
            }
            aPlayer.inventoryContainer.detectAndSendChanges();
            sendSound((byte) 16);
        }
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        for (int i = 0; i < 4; i++) {
            final ItemStack itemStack = this.mInventory[3 - i];

            if (itemStack != null) {
                tag.setTag(CHARGE_SLOT_WAILA_TAG + i, itemStack.writeToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);

        final NBTTagCompound tag = accessor.getNBTData();

        for (int i = 0; i < 4; i++) {
            final String index = GTUtility.formatNumbers(i + 1);

            if (tag.hasKey(CHARGE_SLOT_WAILA_TAG + i)) {
                final ItemStack slotItem = ItemStack
                    .loadItemStackFromNBT(tag.getCompoundTag(CHARGE_SLOT_WAILA_TAG + i));
                assert slotItem != null;

                currentTip.add(
                    GTModHandler.getElectricItemCharge(slotItem)
                        .map(chargeInfo -> {
                            final float ratio = (float) chargeInfo[0] / (float) chargeInfo[1];
                            final EnumChatFormatting chargeFormat;

                            if (ratio == 0L) {
                                chargeFormat = EnumChatFormatting.GRAY;
                            } else if (ratio < 0.25) {
                                chargeFormat = EnumChatFormatting.RED;
                            } else if (ratio < 0.5) {
                                chargeFormat = EnumChatFormatting.GOLD;
                            } else if (ratio < 0.75) {
                                chargeFormat = EnumChatFormatting.YELLOW;
                            } else if (ratio < 1L) {
                                chargeFormat = EnumChatFormatting.GREEN;
                            } else {
                                chargeFormat = EnumChatFormatting.AQUA;
                            }

                            return StatCollector.translateToLocalFormatted(
                                "gt.locker.waila_armor_slot_charged",
                                index,
                                slotItem.getDisplayName(),
                                chargeFormat,
                                GTUtility.formatNumbers(ratio * 100));
                        })
                        .orElseGet(
                            // Lazy initialization
                            () -> StatCollector.translateToLocalFormatted(
                                "gt.locker.waila_armor_slot_generic",
                                index,
                                slotItem.getDisplayName())));
            } else {
                currentTip.add(StatCollector.translateToLocalFormatted("gt.locker.waila_armor_slot_none", index));
            }
        }
    }
}
