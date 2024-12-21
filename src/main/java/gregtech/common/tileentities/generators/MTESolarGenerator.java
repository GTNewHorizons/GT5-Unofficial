package gregtech.common.tileentities.generators;

import static gregtech.api.enums.GTValues.V;

import gregtech.api.metatileentity.BaseMetaTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;

public class MTESolarGenerator extends MTETieredMachineBlock {

    public MTESolarGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Generates EU From Solar Power");
    }

    public MTESolarGenerator(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTESolarGenerator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != ForgeDirection.UP) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_POWER[mTier] };
        }
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(Textures.BlockIcons.SOLARPANEL_LV) };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESolarGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    private boolean valid = true;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        World world = aBaseMetaTileEntity.getWorld();
        // Check every 5 seconds for world conditions
        if (aTick % 100 == 0) {
            doWorldChecks(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity);
        }
        if (aTick % 20 == 0 && valid) {
            aBaseMetaTileEntity.increaseStoredEnergyUnits(maxEUOutput() * 20, false);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        doWorldChecks(aBaseMetaTileEntity.getWorld(), aBaseMetaTileEntity);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    public void doWorldChecks(World world, IGregTechTileEntity aBaseMetaTileEntity) {
        valid = ((world.isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F) || !world.isDaytime()
            || !aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP));
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[mTier] * 80L + getMinimumStoredEU());
    }

    @Override
    public long maxEUOutput() {
        return GTValues.V[mTier];
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return facing == ForgeDirection.UP;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
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
}
