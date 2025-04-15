package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.GregTechAPI.mEUtoRF;
import static gregtech.api.enums.Mods.COFHCore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyContainerItem;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessCharger;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.misc.WirelessChargerManager;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEWirelessCharger extends MTETieredMachineBlock implements IWirelessCharger {

    private static final int MODE_LONG_RANGE = 0;
    private static final int MODE_LOCAL = 1;
    private static final int MODE_MIXED = 2;

    public int mode = MODE_LONG_RANGE;
    public boolean locked = true;

    private final Map<String, UUID> longRangeMap = new HashMap<>();
    private final Map<String, UUID> localRangeMap = new HashMap<>();

    public MTEWirelessCharger(final int aID, final String aName, final String aNameRegional, final int aTier,
        final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, new String[] {});
    }

    public MTEWirelessCharger(final String name, final int tier, final String[] description,
        final ITexture[][][] textures, final int slotCount) {
        super(name, tier, slotCount, description, textures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("gtpp.tooltip.wireless_charger.0"),
            StatCollector.translateToLocal("gtpp.tooltip.wireless_charger.1"),
            StatCollector.translateToLocalFormatted("gtpp.tooltip.wireless_charger.2", this.getLongRange(false)),
            StatCollector.translateToLocalFormatted("gtpp.tooltip.wireless_charger.3", this.getLocalRange(false)),
            StatCollector.translateToLocalFormatted(
                "gtpp.tooltip.wireless_charger.4",
                this.getLongRange(true),
                this.getLocalRange(true)),
            GTPPCore.GT_Tooltip.get() };
    }

    private static String translateChat(String key) {
        return StatCollector.translateToLocal("gtpp.chat.wireless_charger." + key);
    }

    private static String translateChat(String key, Object... args) {
        return StatCollector.translateToLocalFormatted("gtpp.chat.wireless_charger." + key, args);
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity baseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int colorIndex, final boolean active, final boolean redstone) {
        return this.mTextures[(baseMetaTileEntity.isAllowedToWork() ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][colorIndex + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_Inactive) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Screen_2) };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
            TextureFactory.of(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer player, float x, float y, float z) {
        if (player.isSneaking()) {
            locked = !locked;
            GTUtility.sendChatToPlayer(player, translateChat(locked ? "lock" : "unlock"));
            return;
        }

        longRangeMap.clear();
        localRangeMap.clear();

        if (this.mode >= MODE_MIXED) {
            this.mode = MODE_LONG_RANGE;
        } else {
            this.mode++;
        }
        if (this.mode == MODE_LONG_RANGE) {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_change", translateChat("mode.long"), translateChat("mode")));
        } else if (this.mode == MODE_LOCAL) {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_change", translateChat("mode.local"), translateChat("mode")));
        } else {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_change", translateChat("mode.mixed"), translateChat("mode")));
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEWirelessCharger(this.mName, this.mTier, null, this.mTextures, this.mInventory.length);
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return true;
    }

    @Override
    public boolean isFacingValid(final ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public boolean isInputFacing(final ForgeDirection side) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(final ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxEUStore() {
        return GTValues.V[this.mTier] * 128;
    }

    @Override
    public int getCapacity() {
        return (int) (GTValues.V[this.mTier] * 32);
    }

    @Override
    public long maxEUInput() {
        return GTValues.V[this.mTier];
    }

    @Override
    public long maxEUOutput() {
        return 0;
    }

    @Override
    public long maxAmperesIn() {
        if (this.mode == MODE_LONG_RANGE) {
            return this.longRangeMap.size() + 1L;
        } else if (this.mode == MODE_LOCAL) {
            return this.localRangeMap.size() * 2L + 1L;
        } else {
            return this.localRangeMap.size() + this.longRangeMap.size() + 1L;
        }
    }

    @Override
    public long maxAmperesOut() {
        return 0;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

    @Override
    public int getProgresstime() {
        return (int) this.getBaseMetaTileEntity()
            .getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) this.getBaseMetaTileEntity()
            .getUniversalEnergyCapacity();
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
        final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { this.getLocalName() };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(final int p_70301_1_) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {}

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setBoolean("mLocked", this.locked);
        aNBT.setInteger("mMode", this.mode);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.locked = aNBT.getBoolean("mLocked");
        this.mode = aNBT.getInteger("mMode");
    }

    @Override
    public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    private boolean isValidPlayer(EntityPlayer aPlayer) {
        BaseMetaTileEntity aTile = (BaseMetaTileEntity) this.getBaseMetaTileEntity();
        if (locked || (aTile != null && aTile.privateAccess())) {
            return aPlayer.getUniqueID()
                .equals(getBaseMetaTileEntity().getOwnerUuid());
        }
        return true;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity baseMetaTileEntity, final long tick) {
        super.onPostTick(baseMetaTileEntity, tick);
        if (!this.getBaseMetaTileEntity()
            .isServerSide()) {
            return;
        }

        if (tick % 20 == 0) {
            final ChunkCoordinates coord = baseMetaTileEntity.getCoords();
            boolean mapped = this.equals(WirelessChargerManager.getCharger(coord.posX, coord.posY, coord.posZ));
            if (!mapped) {
                WirelessChargerManager.addCharger(this);
            }

            for (EntityPlayer player : baseMetaTileEntity.getWorld().playerEntities) {
                if (this.mode == MODE_LOCAL || this.mode == MODE_MIXED) {
                    if (WirelessChargerManager.calcDistance(player, baseMetaTileEntity)
                        < this.getLocalRange(this.mode == MODE_MIXED)) {
                        if (this.isValidPlayer(player) && !localRangeMap.containsKey(player.getDisplayName())) {
                            localRangeMap.put(player.getDisplayName(), player.getPersistentID());
                        }
                    } else {
                        localRangeMap.remove(player.getDisplayName());
                    }
                }
                if (this.mode == MODE_LONG_RANGE || this.mode == MODE_MIXED) {
                    int range = getLongRange(this.mode == MODE_MIXED);
                    if (WirelessChargerManager.calcDistance(player, baseMetaTileEntity) <= range) {
                        if (!longRangeMap.containsKey(player.getDisplayName())) {
                            if (this.isValidPlayer(player)) {
                                longRangeMap.put(player.getDisplayName(), player.getPersistentID());
                                GTUtility.sendChatToPlayer(
                                    player,
                                    translateChat("enter", range, translateChat("mode.long")));
                            }
                        }
                    } else {
                        if (longRangeMap.containsKey(player.getDisplayName())) {
                            if (longRangeMap.remove(player.getDisplayName()) != null) {
                                GTUtility.sendChatToPlayer(
                                    player,
                                    translateChat("leave", range, translateChat("mode.long")));
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onRemoval() {
        WirelessChargerManager.removeCharger(this);
        longRangeMap.clear();
        localRangeMap.clear();

        super.onRemoval();
    }

    private int getLongRange(boolean mixed) {
        return (int) GTValues.V[this.mTier] * (mixed ? 2 : 4);
    }

    private int getLocalRange(boolean mixed) {
        return this.mTier * (mixed ? 10 : 20);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player, ForgeDirection side,
        float x, float y, float z) {

        if (this.mode == MODE_LONG_RANGE) {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_info", translateChat("mode.long"), translateChat("mode")));
            GTUtility.sendChatToPlayer(
                player,
                translateChat("range") + String.format(
                    ": %sm",
                    NumberFormat.getInstance()
                        .format(this.getLongRange(false))));
            GTUtility.sendChatToPlayer(player, translateChat("mode_info_player"));
            for (String name : this.longRangeMap.keySet()) {
                GTUtility.sendChatToPlayer(player, name);
            }
        } else if (this.mode == MODE_LOCAL) {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_info", translateChat("mode.local"), translateChat("mode")));
            GTUtility.sendChatToPlayer(
                player,
                translateChat("range") + String.format(
                    ": %sm",
                    NumberFormat.getInstance()
                        .format(this.getLocalRange(false))));
            GTUtility.sendChatToPlayer(player, translateChat("mode_info_player"));
            for (String name : this.localRangeMap.keySet()) {
                GTUtility.sendChatToPlayer(player, name);
            }
        } else {
            GTUtility.sendChatToPlayer(
                player,
                translateChat("mode_info", translateChat("mode.mixed"), translateChat("mode")));
            NumberFormat numberFormat = NumberFormat.getInstance();
            GTUtility.sendChatToPlayer(
                player,
                String.format(
                    "%s: %sm (%s: %sm)",
                    translateChat("range"),
                    numberFormat.format(this.getLongRange(true)),
                    translateChat("mode.local"),
                    numberFormat.format(this.getLocalRange(true))));
            GTUtility.sendChatToPlayer(player, translateChat("mode_info_player"));
            for (String name : this.localRangeMap.keySet()) {
                GTUtility.sendChatToPlayer(player, translateChat("mode.local") + ": " + name);
            }
            for (String name : this.longRangeMap.keySet()) {
                GTUtility.sendChatToPlayer(player, translateChat("mode.long") + ": " + name);
            }
        }

        return super.onRightclick(baseMetaTileEntity, player, side, x, y, z);
    }

    @Override
    public void onServerStart() {
        longRangeMap.clear();
        localRangeMap.clear();
        super.onServerStart();
    }

    @Override
    public void onExplosion() {
        WirelessChargerManager.removeCharger(this);
        super.onExplosion();
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        WirelessChargerManager.removeCharger(this);
        super.doExplosion(aExplosionPower);
    }

    @Override
    public IGregTechTileEntity getChargerTE() {
        return this.getBaseMetaTileEntity();
    }

    @Override
    public boolean canChargeItems(EntityPlayer player) {
        if (!this.getBaseMetaTileEntity()
            .isAllowedToWork() || player.getEntityWorld().provider.dimensionId
                != this.getBaseMetaTileEntity()
                    .getWorld().provider.dimensionId)
            return false;
        if (this.mode == 0) {
            return longRangeMap.containsKey(player.getDisplayName());
        } else if (this.mode == 1) {
            return localRangeMap.containsKey(player.getDisplayName());
        } else {
            if (longRangeMap.containsKey(player.getDisplayName())) {
                return true;
            }
            return localRangeMap.containsKey(player.getDisplayName());
        }
    }

    @Override
    public void chargeItems(ItemStack[] stacks, EntityPlayer player) {
        final int amp;
        if (localRangeMap.containsKey(player.getDisplayName())) {
            amp = 2;
        } else if (longRangeMap.containsKey(player.getDisplayName())) {
            amp = 1;
        } else {
            return;
        }

        final long storedEU = this.getEUVar();
        final long maxChargeableEU = Math.min(storedEU, this.maxEUInput() * amp * WirelessChargerManager.CHARGE_TICK);

        long chargedEU = 0;
        for (ItemStack stack : stacks) {
            if (chargedEU >= maxChargeableEU) break;
            if (stack == null) continue;

            final int chargeableEU = (int) Math.min(
                Integer.MAX_VALUE,
                Math.min(maxChargeableEU - chargedEU, this.maxEUInput() * WirelessChargerManager.CHARGE_TICK));
            if (stack.getItem() instanceof ic2.api.item.IElectricItem) {
                final int charged = Math.max(
                    0,
                    (int) ic2.api.item.ElectricItem.manager
                        .charge(stack, chargeableEU, Integer.MAX_VALUE, true, false));
                chargedEU += charged;
            } else if (COFHCore.isModLoaded() && stack.getItem() instanceof IEnergyContainerItem rfItem) {
                int chargeableRF = Math.min(
                    rfItem.getMaxEnergyStored(stack) - rfItem.getEnergyStored(stack),
                    chargeableEU * mEUtoRF / 100);
                int chargedRF = rfItem.receiveEnergy(stack, chargeableRF, false);
                chargedEU += chargedRF * 100L / mEUtoRF;
            }
        }

        this.setEUVar(storedEU - chargedEU);
    }
}
