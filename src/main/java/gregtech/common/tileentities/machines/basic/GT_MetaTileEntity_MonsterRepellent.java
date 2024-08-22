package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TELEPORTER_GLOW;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.TieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_SpawnEventHandler;

public class GT_MetaTileEntity_MonsterRepellent extends TieredMachineBlock {

    public int mRange = 16;

    public GT_MetaTileEntity_MonsterRepellent(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            "Repels nasty Creatures. Range: " + (4 + (12 * aTier))
                + " unpowered / "
                + (16 + (48 * aTier))
                + " powered. Costs "
                + (1L << (aTier * 2))
                + " EU/t");
    }

    public GT_MetaTileEntity_MonsterRepellent(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_MonsterRepellent(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MonsterRepellent(
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

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide()) {
            int[] tCoords = { aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(),
                aBaseMetaTileEntity.getZCoord(), aBaseMetaTileEntity.getWorld().provider.dimensionId };
            if ((aTimer % 600 == 0) && !GT_SpawnEventHandler.mobReps.contains(tCoords)) {
                GT_SpawnEventHandler.mobReps.add(tCoords);
            }
            if (aBaseMetaTileEntity.isUniversalEnergyStored(getMinimumStoredEU())
                && aBaseMetaTileEntity.decreaseStoredEnergyUnits(1L << (this.mTier * 2), false)) {
                mRange = GT_SpawnEventHandler.getPoweredRepellentRange(mTier);
            } else {
                mRange = GT_SpawnEventHandler.getUnpoweredRepellentRange(mTier);
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        int[] tCoords = { aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord(), aBaseMetaTileEntity.getWorld().provider.dimensionId };
        GT_SpawnEventHandler.mobReps.add(tCoords);
    }

    @Override
    public void onRemoval() {
        int[] tCoords = { this.getBaseMetaTileEntity()
            .getXCoord(),
            this.getBaseMetaTileEntity()
                .getYCoord(),
            this.getBaseMetaTileEntity()
                .getZCoord(),
            this.getBaseMetaTileEntity()
                .getWorld().provider.dimensionId };
        GT_SpawnEventHandler.mobReps.removeIf(coords -> Arrays.equals(coords, tCoords));
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
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
}
