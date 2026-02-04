package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;

public class MTEMonsterRepellent extends MTETieredMachineBlock {

    private int mRange = -1;

    public MTEMonsterRepellent(Args args) {
        super(
            args.toBuilder()
                .descriptionArray(
                    new String[] { "Repels nasty Creatures. Radius: " + (4 + (12 * args.getTier()))
                        + " unpowered / "
                        + (16 + (48 * args.getTier()))
                        + " powered. Costs "
                        + (1L << (args.getTier() * 2))
                        + " EU/t" })
                .build());
    }

    @Deprecated
    public MTEMonsterRepellent(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            "Repels nasty Creatures. Radius: " + (4 + (12 * aTier))
                + " unpowered / "
                + (16 + (48 * aTier))
                + " powered. Costs "
                + (1L << (aTier * 2))
                + " EU/t");
    }

    @Deprecated
    public MTEMonsterRepellent(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMonsterRepellent(getPrototype());
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

    @Override
    public void onPostTick(IGregTechTileEntity mte, long aTimer) {
        if (!mte.isServerSide()) return;
        if (mte.isAllowedToWork()) {
            final int prevRange = mRange;
            if (mte.isUniversalEnergyStored(getMinimumStoredEU())
                && mte.decreaseStoredEnergyUnits(1L << (this.mTier * 2), false)) {
                mRange = getRepellentRange(mTier, true);
            } else {
                mRange = getRepellentRange(mTier, false);
            }
            if (prevRange != mRange) {
                GTMod.proxy.spawnEventHandler.putRepellent(mte, mRange);
            }
        } else {
            if (mRange != -1) {
                GTMod.proxy.spawnEventHandler.removeRepellent(mte);
                mRange = -1;
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

    private static int getRepellentRange(int aTier, boolean powered) {
        return powered ? 16 + (48 * aTier) : 4 + (12 * aTier);
    }
}
