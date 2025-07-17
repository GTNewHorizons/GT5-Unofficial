package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_MUFFLER;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.ParticleFX;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.common.pollution.Pollution;

@SuppressWarnings("unused") // Unused API is expected within scope
public class MTEHatchMuffler extends MTEHatch {

    private static final String localizedDescFormat = GTLanguageManager.addStringLocalization(
        "gt.blockmachines.hatch.muffler.desc.format",
        "Outputs the Pollution (Might cause ... things)%n" + "DO NOT OBSTRUCT THE OUTPUT!%n"
            + "Reduces Pollution to %d%%%n");
    private final int pollutionReduction = calculatePollutionReduction(100);
    private final int pollutionRecover = 100 - pollutionReduction;
    private final String[] description = String.format(localizedDescFormat, pollutionReduction, pollutionRecover)
        .split("\\R");

    public MTEHatchMuffler(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
    }

    public MTEHatchMuffler(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatchMuffler(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, 0, aDescription, aTextures);
    }

    public MTEHatchMuffler(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return description;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_MUFFLER) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_MUFFLER) };
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchMuffler(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() && this.getBaseMetaTileEntity()
            .isActive()) {
            pollutionParticles(
                this.getBaseMetaTileEntity()
                    .getWorld(),
                ParticleFX.LARGE_SMOKE.toString());
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void pollutionParticles(World aWorld, String name) {
        boolean chk1, chk2, chk3;
        float ran1 = XSTR_INSTANCE.nextFloat(), ran2, ran3;
        chk1 = ran1 * 100 < calculatePollutionReduction(100);
        if (Pollution.getPollution(getBaseMetaTileEntity()) >= GTMod.proxy.mPollutionSmogLimit) {
            ran2 = XSTR_INSTANCE.nextFloat();
            ran3 = XSTR_INSTANCE.nextFloat();
            chk2 = ran2 * 100 < calculatePollutionReduction(100);
            chk3 = ran3 * 100 < calculatePollutionReduction(100);
            if (!(chk1 || chk2 || chk3)) return;
        } else {
            if (!chk1) return;
            ran2 = ran3 = 0.0F;
            chk2 = chk3 = false;
        }

        final IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
        final ForgeDirection aDir = aMuffler.getFrontFacing();
        final float xPos = aDir.offsetX * 0.76F + aMuffler.getXCoord() + 0.25F;
        final float yPos = aDir.offsetY * 0.76F + aMuffler.getYCoord() + 0.25F;
        final float zPos = aDir.offsetZ * 0.76F + aMuffler.getZCoord() + 0.25F;

        final float ySpd = aDir.offsetY * 0.1F + 0.2F + 0.1F * XSTR_INSTANCE.nextFloat();
        final float xSpd;
        final float zSpd;

        if (aDir.offsetY == -1) {
            final float temp = XSTR_INSTANCE.nextFloat() * 2 * (float) Math.PI;
            xSpd = (float) Math.sin(temp) * 0.1F;
            zSpd = (float) Math.cos(temp) * 0.1F;
        } else {
            xSpd = aDir.offsetX * (0.1F + 0.2F * XSTR_INSTANCE.nextFloat());
            zSpd = aDir.offsetZ * (0.1F + 0.2F * XSTR_INSTANCE.nextFloat());
        }

        final WorldSpawnedEventBuilder.ParticleEventBuilder events = new WorldSpawnedEventBuilder.ParticleEventBuilder()
            .setIdentifier(name)
            .setWorld(aWorld)
            .setMotion(xSpd, ySpd, zSpd);

        if (chk1) {
            events
                .setPosition(
                    xPos + ran1 * 0.5F,
                    yPos + XSTR_INSTANCE.nextFloat() * 0.5F,
                    zPos + XSTR_INSTANCE.nextFloat() * 0.5F)
                .run();
        }
        if (chk2) {
            events
                .setPosition(
                    xPos + ran2 * 0.5F,
                    yPos + XSTR_INSTANCE.nextFloat() * 0.5F,
                    zPos + XSTR_INSTANCE.nextFloat() * 0.5F)
                .run();
        }
        if (chk3) {
            events
                .setPosition(
                    xPos + ran3 * 0.5F,
                    yPos + XSTR_INSTANCE.nextFloat() * 0.5F,
                    zPos + XSTR_INSTANCE.nextFloat() * 0.5F)
                .run();
        }
    }

    public int calculatePollutionReduction(int aPollution) {
        if (mTier < 2) {
            return aPollution;
        }
        return (int) ((float) aPollution * ((100F - 12.5F * ((float) mTier - 1F)) / 100F));
    }

    /**
     * @param mte             The multi-block controller's {@link MetaTileEntity} MetaTileEntity is passed so newer
     *                        muffler hatches can do wacky things with the multis
     * @param pollutionAmount How much pollution to output. Reduced by muffler efficiency.
     * @return pollution success
     */
    public boolean polluteEnvironment(MetaTileEntity mte, int pollutionAmount) {
        if (getBaseMetaTileEntity().getAirAtSide(getBaseMetaTileEntity().getFrontFacing())) {
            Pollution.addPollution(getBaseMetaTileEntity(), calculatePollutionReduction(pollutionAmount));
            return true;
        }
        return false;
    }
}
