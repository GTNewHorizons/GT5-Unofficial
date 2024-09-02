package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.FluidUtils;

public class METHatchAirIntake extends MTEHatchFluidGenerator {

    public METHatchAirIntake(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public METHatchAirIntake(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new METHatchAirIntake(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getCustomTooltip() {
        String[] aTooltip = new String[3];
        aTooltip[0] = "DO NOT OBSTRUCT THE INPUT!";
        aTooltip[1] = "Draws in Air from the surrounding environment";
        aTooltip[2] = "Creates " + getAmountOfFluidToGenerate() + "L of Air every " + getMaxTickTime() + " ticks";
        return aTooltip;
    }

    @Override
    public Fluid getFluidToGenerate() {
        return FluidUtils.getAir(1)
            .getFluid();
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 1000;
    }

    @Override
    public int getMaxTickTime() {
        return 4;
    }

    @Override
    public int getCapacity() {
        return 128000;
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        return this.getBaseMetaTileEntity()
            .getAirAtSide(
                this.getBaseMetaTileEntity()
                    .getFrontFacing());
    }

    @Override
    public void generateParticles(World aWorld, String name) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            return;
        }
        final float ran1 = METHatchAirIntake.floatGen.nextFloat();
        float ran2 = 0.0f;
        float ran3 = 0.0f;
        ran2 = METHatchAirIntake.floatGen.nextFloat();
        ran3 = METHatchAirIntake.floatGen.nextFloat();

        final IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
        final ForgeDirection aDir = aMuffler.getFrontFacing();
        final float xPos = aDir.offsetX * 0.76f + aMuffler.getXCoord() + 0.25f;
        float yPos = aDir.offsetY * 0.76f + aMuffler.getYCoord() + 0.65f;
        final float zPos = aDir.offsetZ * 0.76f + aMuffler.getZCoord() + 0.25f;
        float ySpd = aDir.offsetY * 0.1f + 0.2f + 0.1f * METHatchAirIntake.floatGen.nextFloat();
        float xSpd;
        float zSpd;
        if (aDir.offsetY == -1) {
            // Logger.INFO("Y = -1");
            final float temp = METHatchAirIntake.floatGen.nextFloat() * 2.0f * GTPPCore.PI;
            xSpd = (float) Math.sin(temp) * 0.1f;
            zSpd = (float) Math.cos(temp) * 0.1f;
            ySpd = -ySpd;
            yPos = yPos - 0.8f;
        } else {
            xSpd = aDir.offsetX * (0.1f + 0.2f * METHatchAirIntake.floatGen.nextFloat());
            zSpd = aDir.offsetZ * (0.1f + 0.2f * METHatchAirIntake.floatGen.nextFloat());

            xSpd = -xSpd;
            zSpd = -zSpd;
        }

        aWorld.spawnParticle(
            name,
            (double) (xPos + ran1 * 0.5f),
            (double) (yPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) (zPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) xSpd,
            (double) -ySpd,
            (double) zSpd);
        aWorld.spawnParticle(
            name,
            (double) (xPos + ran2 * 0.5f),
            (double) (yPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) (zPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) xSpd,
            (double) -ySpd,
            (double) zSpd);
        aWorld.spawnParticle(
            name,
            (double) (xPos + ran3 * 0.5f),
            (double) (yPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) (zPos + METHatchAirIntake.floatGen.nextFloat() * 0.5f),
            (double) xSpd,
            (double) -ySpd,
            (double) zSpd);
    }
}
