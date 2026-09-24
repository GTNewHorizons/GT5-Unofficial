package gregtech.common.tileentities.machines.basic;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEMonsterRepellent extends MTETieredMachineBlock {

    private int mRange = -1;
    private boolean mPowered = false;

    public MTEMonsterRepellent(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, new String[] { "gt.blockmachines.basicmachine.mobrep.tooltip" });
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocalFormatted(
            "gt.blockmachines.basicmachine.mobrep.tooltip",
            (getRepellentRange(this.mTier, false)),
            (getRepellentRange(this.mTier, true)),
            (this.getEUToConsume())) };
    }

    public MTEMonsterRepellent(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMonsterRepellent(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != ForgeDirection.UP) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        if (active) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_TELEPORTER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_TELEPORTER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TELEPORTER_GLOW)
                .glow()
                .build() };
    }

    public long getEUToConsume() {
        return 1L << (this.mTier * 2);
    }

    @Override
    public void onPostTick(IGregTechTileEntity mte, long aTimer) {
        if (!mte.isServerSide()) return;
        if (mte.isAllowedToWork()) {
            final int prevRange = mRange;
            if (mte.isUniversalEnergyStored(getMinimumStoredEU())
                && mte.decreaseStoredEnergyUnits(this.getEUToConsume(), false)) {
                mRange = getRepellentRange(mTier, true);
                this.mPowered = true;
            } else {
                mRange = getRepellentRange(mTier, false);
                this.mPowered = false;
            }
            if (prevRange != mRange) {
                GTMod.proxy.spawnEventHandler.putRepellent(mte, mRange);
            }
        } else {
            if (mRange != -1) {
                GTMod.proxy.spawnEventHandler.removeRepellent(mte);
                mRange = -1;
                this.mPowered = false;
            }
        }
    }

    @Override
    public void onRemoval() {
        final IGregTechTileEntity mte = this.getBaseMetaTileEntity();
        if (mte.isServerSide()) {
            GTMod.proxy.spawnEventHandler.removeRepellent(mte);
        }
    }

    @Override
    public void onUnload() {
        final IGregTechTileEntity mte = this.getBaseMetaTileEntity();
        if (mte.isServerSide()) {
            GTMod.proxy.spawnEventHandler.removeRepellent(mte);
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512L;
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 50;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxAmperesIn() {
        return 2;
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
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        boolean status = tag.getBoolean("isAllowedToWork");
        boolean powered = tag.getBoolean("powered");
        long range = tag.getInteger("repellentRange");
        long eut = tag.getLong("eut");
        // if this machine is not allowed to work, show "Disabled"
        // else show the range and if this machine is powered or not, also the eut.
        if (!status) {
            currenttip.add(StatCollector.translateToLocal("GT5U.waila.machine.working_disabled"));
        } else {
            if (range > 0) {
                currenttip
                    .add(StatCollector.translateToLocalFormatted("GT5U.waila.monster_repellent.working_range", range));
            }
            if (powered) {
                if (eut > 0) {
                    double exactAmps = GTUtility.getExactAmperageForTier(eut, (byte) getInputTier());

                    currenttip.add(
                        translateToLocalFormatted(
                            "GT5U.waila.energy.use_with_amperage",
                            formatNumber(eut),
                            String.format("%.2f", exactAmps),
                            GTUtility.getColoredTierNameFromTier((byte) getInputTier())));
                }
            } else {
                currenttip
                    .add(StatCollector.translateToLocalFormatted("GT5U.waila.monster_repellent.unpowered", range));
            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        final IGregTechTileEntity gte = this.getBaseMetaTileEntity();
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("isAllowedToWork", gte.isAllowedToWork());
        tag.setBoolean("powered", this.mPowered);
        tag.setInteger("repellentRange", mRange);
        tag.setLong("eut", this.mPowered ? this.getEUToConsume() : 0L);
    }

    private static int getRepellentRange(int aTier, boolean powered) {
        return powered ? 16 + (48 * aTier) : 4 + (12 * aTier);
    }
}
