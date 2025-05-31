package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;

@EventBusSubscriber
public class MTEMagLevPylon extends MTETieredMachineBlock {

    public int mRange = 16;
    public Tether machineTether;

    // TODO Power/Range balancing
    public MTEMagLevPylon(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            "Grants flight with the power of magnets. Range: " + (4 + (12 * aTier))
                + " unpowered / "
                + (16 + (48 * aTier))
                + " powered. Costs "
                + (GTValues.VP[aTier])
                + " EU/t");
    }

    public MTEMagLevPylon(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity baseMetaTileEntity) {
        if (!baseMetaTileEntity.isServerSide()) return;

        machineTether = new Tether(
            baseMetaTileEntity.getXCoord(),
            baseMetaTileEntity.getYCoord(),
            baseMetaTileEntity.getZCoord(),
            baseMetaTileEntity.getWorld().provider.dimensionId,
            8 * mTier,
            mTier);
        TetherManager.ACTIVE_PYLONS.get(baseMetaTileEntity.getWorld().provider.dimensionId)
            .insert(this);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (!baseMetaTileEntity.isServerSide()) return;

        if (baseMetaTileEntity.isAllowedToWork()) {
            if (baseMetaTileEntity.isUniversalEnergyStored(32)) {
                machineTether.range(16 * mTier);
                // only drain power if a player is connected
                if (TetherManager.PLAYER_TETHERS.containsValue(this.machineTether)) {
                    baseMetaTileEntity.setActive(true);
                    baseMetaTileEntity.decreaseStoredEnergyUnits(32, false);
                } else {
                    baseMetaTileEntity.setActive(false);
                }
            } else {
                baseMetaTileEntity.setActive(false);
                machineTether.range(8 * mTier);
            }
        } else {
            baseMetaTileEntity.setActive(false);
        }
    }

    @Override
    public void onRemoval() {
        TetherManager.ACTIVE_PYLONS.get(getBaseMetaTileEntity().getWorld().provider.dimensionId)
            .remove(this);
        TetherManager.PLAYER_TETHERS.entrySet()
            .forEach(entry -> {
                Tether value = entry.getValue();
                if (getBaseMetaTileEntity().getWorld().provider.dimensionId == value.dimID()
                    && getBaseMetaTileEntity().getXCoord() == value.sourceX()
                    && getBaseMetaTileEntity().getYCoord() == value.sourceY()
                    && getBaseMetaTileEntity().getZCoord() == value.sourceZ()) {
                    entry.setValue(null);
                }
            });
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMagLevPylon(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
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
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == ForgeDirection.UP || side == ForgeDirection.DOWN)
            return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1] };
        if (active) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_MAGLEV_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_MAGLEV_ACTIVE_GLOW)
                .glow()
                .build() };
        return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_MAGLEV) };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}
}
