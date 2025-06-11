package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.common.data.maglev.Tether;
import gregtech.common.data.maglev.TetherManager;

@EventBusSubscriber
public class MTEMagLevPylon extends MTETieredMachineBlock {

    public Tether machineTether;
    private final int poweredRange = TetherManager.getRange(mTier, true);
    private final int unpoweredRange = TetherManager.getRange(mTier, false);
    private final long powerCost = TetherManager.getPowerCost(mTier);

    public MTEMagLevPylon(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { "Grants creative flight to those wearing a MagLev Harness.",
                "Range is a cube centered on the pylon.",
                String.format(
                    "Unpowered Range: %s%d blocks",
                    EnumChatFormatting.WHITE,
                    TetherManager.getRange(aTier, false)),
                String.format(
                    "Powered Range: %s%d blocks",
                    EnumChatFormatting.WHITE,
                    TetherManager.getRange(aTier, true)),
                String.format(
                    "Cost: %s%d EU/t if tethered",
                    EnumChatFormatting.WHITE,
                    TetherManager.getPowerCost(aTier)), });
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
            poweredRange);
        TetherManager.ACTIVE_PYLONS.get(baseMetaTileEntity.getWorld().provider.dimensionId)
            .insert(this.machineTether);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (!baseMetaTileEntity.isServerSide()) return;

        /*
         * Active state of machine is used to control the animated texture
         * Should only be active when a player is connected
         * If machine is fully disabled, also disable the tether and active state
         * EU should only drain when a player is connected
         * Tether range is determined if the machine has enough EU
         */
        if (baseMetaTileEntity.isAllowedToWork()) {
            machineTether.active(true);
            boolean playerConnected = TetherManager.PLAYER_TETHERS.containsValue(this.machineTether);
            baseMetaTileEntity.setActive(playerConnected);
            if (baseMetaTileEntity.isUniversalEnergyStored(powerCost)) {
                machineTether.range(poweredRange);
                if (playerConnected) {
                    baseMetaTileEntity.decreaseStoredEnergyUnits(powerCost, false);
                }
            } else {
                machineTether.range(unpoweredRange);
            }
        } else {
            machineTether.active(false);
            baseMetaTileEntity.setActive(false);
        }
    }

    @Override
    public void onRemoval() {
        if (this.getBaseMetaTileEntity()
            .isServerSide()
            && TetherManager.ACTIVE_PYLONS.get(getBaseMetaTileEntity().getWorld().provider.dimensionId)
                .contains(this.machineTether)) {
            TetherManager.ACTIVE_PYLONS.get(getBaseMetaTileEntity().getWorld().provider.dimensionId)
                .remove(this.machineTether);
        }
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
