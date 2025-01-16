package gregtech.common.tileentities.generators;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;

public class MTESolarGenerator extends MTETieredMachineBlock {

    public MTESolarGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { "Generates EU From Solar Power", "Does not generate power when raining",
                "Cleans itself automatically", "Does not explode in rain!" });
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
        if (sideDirection == ForgeDirection.UP) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SOLAR_PANEL) };
        }
        if (sideDirection == facingDirection) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                OVERLAYS_ENERGY_OUT[mTier] };
        }
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1] };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESolarGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    private boolean valid = true;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
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

    private void doWorldChecks(World world, IGregTechTileEntity aBaseMetaTileEntity) {
        valid = (!(world.isRaining() && aBaseMetaTileEntity.getBiome().rainfall > 0.0F) && world.isDaytime()
            && aBaseMetaTileEntity.getSkyAtSide(ForgeDirection.UP));
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 10000;
    }

    @Override
    public long maxEUOutput() {
        return GTValues.V[mTier];
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
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
