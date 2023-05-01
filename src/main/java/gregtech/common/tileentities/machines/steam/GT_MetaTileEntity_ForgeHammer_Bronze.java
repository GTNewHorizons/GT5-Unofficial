package gregtech.common.tileentities.machines.steam;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Bronze;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;

public class GT_MetaTileEntity_ForgeHammer_Bronze extends GT_MetaTileEntity_BasicMachine_Bronze {

    public GT_MetaTileEntity_ForgeHammer_Bronze(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, "Forge Hammer", 1, 1, false);
    }

    public GT_MetaTileEntity_ForgeHammer_Bronze(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, false);
    }

    public GT_MetaTileEntity_ForgeHammer_Bronze(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures, 1, 1, false);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ForgeHammer_Bronze(this.mName, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sHammerRecipes;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(SoundResource.RANDOM_ANVIL_USE, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { super.getSideFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_SIDE_STEAM_HAMMER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_HAMMER_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { super.getSideFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_SIDE_STEAM_HAMMER),
            TextureFactory.builder()
                .addIcon(OVERLAY_SIDE_STEAM_HAMMER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { super.getFrontFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_FRONT_STEAM_HAMMER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_HAMMER_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { super.getFrontFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_FRONT_STEAM_HAMMER),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_STEAM_HAMMER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { super.getTopFacingActive(aColor)[0], TextureFactory.of(OVERLAY_TOP_STEAM_HAMMER_ACTIVE),
            TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_HAMMER_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { super.getTopFacingInactive(aColor)[0], TextureFactory.of(OVERLAY_TOP_STEAM_HAMMER),
            TextureFactory.builder()
                .addIcon(OVERLAY_TOP_STEAM_HAMMER_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { super.getBottomFacingActive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_HAMMER_ACTIVE), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_HAMMER_ACTIVE_GLOW)
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { super.getBottomFacingInactive(aColor)[0],
            TextureFactory.of(OVERLAY_BOTTOM_STEAM_HAMMER), TextureFactory.builder()
                .addIcon(OVERLAY_BOTTOM_STEAM_HAMMER_GLOW)
                .glow()
                .build() };
    }

    /**
     * Handles {@link Block#randomDisplayTick}. Draws Random Sparkles at main face.
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@see Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {

        // Random Sparkles at main face
        if (aBaseMetaTileEntity.isActive() && XSTR_INSTANCE.nextInt(3) == 0) {

            final ForgeDirection mainFacing = this.mMainFacing;

            if ((mainFacing.offsetX != 0 || mainFacing.offsetZ != 0)
                && aBaseMetaTileEntity.getCoverIDAtSide(mainFacing) == 0
                && !aBaseMetaTileEntity.getOpacityAtSide(mainFacing)) {

                final double oX = aBaseMetaTileEntity.getXCoord();
                final double oY = aBaseMetaTileEntity.getYCoord();
                final double oZ = aBaseMetaTileEntity.getZCoord();
                final double offset = 0.02D;
                final double horizontal = 0.5D + XSTR_INSTANCE.nextFloat() * 8D / 16D - 4D / 16D;

                final double x, y, z, mX, mZ;

                y = oY + XSTR_INSTANCE.nextFloat() * 10D / 16D + 5D / 16D;

                if (mainFacing == ForgeDirection.WEST) {
                    x = oX - offset;
                    mX = -.05D;
                    z = oZ + horizontal;
                    mZ = 0D;
                } else if (mainFacing == ForgeDirection.EAST) {
                    x = oX + offset;
                    mX = .05D;
                    z = oZ + horizontal;
                    mZ = 0D;
                } else if (mainFacing == ForgeDirection.NORTH) {
                    x = oX + horizontal;
                    mX = 0D;
                    z = oZ - offset;
                    mZ = -.05D;
                } else // if (frontFacing == ForgeDirection.SOUTH.ordinal())
                {
                    x = oX + horizontal;
                    mX = 0D;
                    z = oZ + offset;
                    mZ = .05D;
                }

                ParticleEventBuilder particleEventBuilder = (new ParticleEventBuilder()).setMotion(mX, 0, mZ)
                    .setPosition(x, y, z)
                    .setWorld(getBaseMetaTileEntity().getWorld());
                particleEventBuilder.setIdentifier(ParticleFX.LAVA)
                    .run();
            }
        }
    }
}
