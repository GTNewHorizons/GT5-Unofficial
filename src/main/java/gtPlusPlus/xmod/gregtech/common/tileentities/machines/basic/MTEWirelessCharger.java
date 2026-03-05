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
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyContainerItem;
import gregtech.GTMod;
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

    private enum ChargeMode {

        LONG_RANGE,
        LOCAL,
        MIXED;

        public ChargeMode next() {
            return switch (this) {
                case LONG_RANGE -> LOCAL;
                case LOCAL -> MIXED;
                case MIXED -> LONG_RANGE;
            };
        }

        public static ChargeMode fromOrdinal(int ordinal) {
            return switch (ordinal) {
                case 1 -> LOCAL;
                case 2 -> MIXED;
                default -> LONG_RANGE;
            };
        }
    }

    private ChargeMode mode = ChargeMode.LONG_RANGE;
    private boolean locked = true;
    // this is only used to register/unregister the charger
    private int maxCurrentRange = -1;

    private final Map<String, UUID> longRangeMap = new HashMap<>();
    private final Map<String, UUID> localRangeMap = new HashMap<>();

    public MTEWirelessCharger(final int aID, final String aName, final String aNameRegional, final int aTier,
        final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aSlotCount, GTValues.emptyStringArray);
    }

    public MTEWirelessCharger(final String name, final int tier, final String[] description,
        final ITexture[][][] textures, final int slotCount) {
        super(name, tier, slotCount, description, textures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { GTUtility.translate("gtpp.tooltip.wireless_charger.0"),
            GTUtility.translate("gtpp.tooltip.wireless_charger.1"),
            GTUtility.translate("gtpp.tooltip.wireless_charger.2", this.getLongRange(false)),
            GTUtility.translate("gtpp.tooltip.wireless_charger.3", this.getLocalRange(false)),
            GTUtility.translate("gtpp.tooltip.wireless_charger.4", this.getLongRange(true), this.getLocalRange(true)),
            GTPPCore.GT_Tooltip.get() };
    }

    private static String translateChat(String key) {
        return GTUtility.translate("gtpp.chat.wireless_charger." + key);
    }

    private static String translateChat(String key, Object... args) {
        return GTUtility.translate("gtpp.chat.wireless_charger." + key, args);
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer player, float x, float y, float z,
        ItemStack tool) {

        if (player.isSneaking()) {
            locked = !locked;
            GTUtility.sendChatTrans(player, translateChat(locked ? "lock" : "unlock"));
            return;
        }

        longRangeMap.clear();
        localRangeMap.clear();

        this.mode = this.mode.next();

        if (this.mode == ChargeMode.LONG_RANGE) {
            GTUtility
                .sendChatTrans(player, translateChat("mode_change", translateChat("mode.long"), translateChat("mode")));
        } else if (this.mode == ChargeMode.LOCAL) {
            GTUtility.sendChatTrans(
                player,
                translateChat("mode_change", translateChat("mode.local"), translateChat("mode")));
        } else {
            GTUtility.sendChatTrans(
                player,
                translateChat("mode_change", translateChat("mode.mixed"), translateChat("mode")));
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEWirelessCharger(this.mName, this.mTier, null, this.mTextures, this.mInventory.length);
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
    public long maxAmperesIn() {
        if (this.mode == ChargeMode.LONG_RANGE) {
            return this.longRangeMap.size() + 1L;
        } else if (this.mode == ChargeMode.LOCAL) {
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
        return GTValues.emptyIntArray;
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
    public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {}

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void saveNBTData(final NBTTagCompound aNBT) {
        aNBT.setBoolean("mLocked", this.locked);
        aNBT.setInteger("mMode", this.mode.ordinal());
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        this.locked = aNBT.getBoolean("mLocked");
        this.mode = ChargeMode.fromOrdinal(aNBT.getInteger("mMode"));
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
    public void onPostTick(final IGregTechTileEntity mte, final long tick) {
        super.onPostTick(mte, tick);
        if (!mte.isServerSide()) {
            return;
        }

        if (mte.isAllowedToWork()) {
            int prevRange = this.maxCurrentRange;
            maxCurrentRange = getMaxRange();
            if (prevRange != maxCurrentRange) {
                GTMod.proxy.wirelessChargerManager.addCharger(this, maxCurrentRange);
            }
        } else {
            if (maxCurrentRange != -1) {
                GTMod.proxy.wirelessChargerManager.removeCharger(this);
                maxCurrentRange = -1;
            }
        }

        if (tick % 20 == 0) {
            for (EntityPlayer player : mte.getWorld().playerEntities) {
                final double distSq = player.getDistanceSq(mte.getXCoord(), mte.getYCoord(), mte.getZCoord());
                if (this.mode == ChargeMode.LOCAL || this.mode == ChargeMode.MIXED) {
                    final double range = this.getLocalRange(this.mode == ChargeMode.MIXED);
                    if (distSq < range * range) {
                        if (this.isValidPlayer(player) && !localRangeMap.containsKey(
                            player.getGameProfile()
                                .getName())) {
                            localRangeMap.put(
                                player.getGameProfile()
                                    .getName(),
                                player.getPersistentID());
                        }
                    } else {
                        localRangeMap.remove(
                            player.getGameProfile()
                                .getName());
                    }
                }
                if (this.mode == ChargeMode.LONG_RANGE || this.mode == ChargeMode.MIXED) {
                    final double range = getLongRange(this.mode == ChargeMode.MIXED);
                    if (distSq <= range * range) {
                        if (!longRangeMap.containsKey(
                            player.getGameProfile()
                                .getName())) {
                            if (this.isValidPlayer(player)) {
                                longRangeMap.put(
                                    player.getGameProfile()
                                        .getName(),
                                    player.getPersistentID());
                                GTUtility.sendChatTrans(
                                    player,
                                    translateChat("enter", (int) range, translateChat("mode.long")));
                            }
                        }
                    } else {
                        if (longRangeMap.containsKey(
                            player.getGameProfile()
                                .getName())) {
                            if (longRangeMap.remove(
                                player.getGameProfile()
                                    .getName())
                                != null) {
                                GTUtility.sendChatTrans(
                                    player,
                                    translateChat("leave", (int) range, translateChat("mode.long")));
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onRemoval() {
        final IGregTechTileEntity mte = this.getBaseMetaTileEntity();
        if (mte.isServerSide()) {
            GTMod.proxy.wirelessChargerManager.removeCharger(this);
        }
        super.onRemoval();
    }

    @Override
    public void onUnload() {
        final IGregTechTileEntity mte = this.getBaseMetaTileEntity();
        if (mte.isServerSide()) {
            GTMod.proxy.wirelessChargerManager.removeCharger(this);
        }
        super.onUnload();
    }

    private int getLongRange(boolean mixed) {
        return (int) GTValues.V[this.mTier] * (mixed ? 2 : 4);
    }

    private int getLocalRange(boolean mixed) {
        return this.mTier * (mixed ? 10 : 20);
    }

    private int getMaxRange() {
        return switch (mode) {
            case LONG_RANGE -> getLongRange(false);
            case LOCAL -> getLocalRange(false);
            case MIXED -> getLongRange(true);
        };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player, ForgeDirection side,
        float x, float y, float z) {

        if (this.mode == ChargeMode.LONG_RANGE) {
            GTUtility
                .sendChatTrans(player, translateChat("mode_info", translateChat("mode.long"), translateChat("mode")));
            GTUtility.sendChatTrans(
                player,
                translateChat("range") + String.format(
                    ": %sm",
                    NumberFormat.getInstance()
                        .format(this.getLongRange(false))));
            GTUtility.sendChatTrans(player, translateChat("mode_info_player"));
            for (String name : this.longRangeMap.keySet()) {
                GTUtility.sendChatTrans(player, name);
            }
        } else if (this.mode == ChargeMode.LOCAL) {
            GTUtility
                .sendChatTrans(player, translateChat("mode_info", translateChat("mode.local"), translateChat("mode")));
            GTUtility.sendChatTrans(
                player,
                translateChat("range") + String.format(
                    ": %sm",
                    NumberFormat.getInstance()
                        .format(this.getLocalRange(false))));
            GTUtility.sendChatTrans(player, translateChat("mode_info_player"));
            for (String name : this.localRangeMap.keySet()) {
                GTUtility.sendChatTrans(player, name);
            }
        } else {
            GTUtility
                .sendChatTrans(player, translateChat("mode_info", translateChat("mode.mixed"), translateChat("mode")));
            NumberFormat numberFormat = NumberFormat.getInstance();
            GTUtility.sendChatTrans(
                player,
                String.format(
                    "%s: %sm (%s: %sm)",
                    translateChat("range"),
                    numberFormat.format(this.getLongRange(true)),
                    translateChat("mode.local"),
                    numberFormat.format(this.getLocalRange(true))));
            GTUtility.sendChatTrans(player, translateChat("mode_info_player"));
            for (String name : this.localRangeMap.keySet()) {
                GTUtility.sendChatTrans(player, translateChat("mode.local") + ": " + name);
            }
            for (String name : this.longRangeMap.keySet()) {
                GTUtility.sendChatTrans(player, translateChat("mode.long") + ": " + name);
            }
        }

        return super.onRightclick(baseMetaTileEntity, player, side, x, y, z);
    }

    @Override
    public IGregTechTileEntity getChargerTE() {
        return this.getBaseMetaTileEntity();
    }

    @Override
    public boolean canChargePlayerItems(EntityPlayer player) {
        if (this.mode == ChargeMode.LONG_RANGE) {
            return longRangeMap.containsKey(
                player.getGameProfile()
                    .getName());
        } else if (this.mode == ChargeMode.LOCAL) {
            return localRangeMap.containsKey(
                player.getGameProfile()
                    .getName());
        } else {
            if (longRangeMap.containsKey(
                player.getGameProfile()
                    .getName())) {
                return true;
            }
            return localRangeMap.containsKey(
                player.getGameProfile()
                    .getName());
        }
    }

    @Override
    public void chargePlayerItems(EntityPlayer player, ItemStack[]... inventories) {
        final int amp;
        if (localRangeMap.containsKey(
            player.getGameProfile()
                .getName())) {
            amp = 2;
        } else if (longRangeMap.containsKey(
            player.getGameProfile()
                .getName())) {
                    amp = 1;
                } else {
                    return;
                }

        final long storedEU = this.getEUVar();
        final long maxChargeableEU = Math.min(storedEU, this.maxEUInput() * amp * WirelessChargerManager.CHARGE_TICK);

        long chargedEU = 0;
        for (ItemStack[] stacks : inventories) {
            if (chargedEU >= maxChargeableEU) break;
            if (stacks == null) continue;
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
                        (int) Math.min(Integer.MAX_VALUE, (long) chargeableEU * mEUtoRF / 100));
                    int chargedRF = rfItem.receiveEnergy(stack, chargeableRF, false);
                    chargedEU += (long) chargedRF * 100L / mEUtoRF;
                }
            }
        }

        this.setEUVar(storedEU - chargedEU);
    }
}
