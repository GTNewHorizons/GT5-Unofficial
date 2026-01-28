package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IBiodomeCompatible;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.MTEBiodome;
import gtPlusPlus.core.lib.GTPPCore;

public class MTEHatchAirIntake extends MTEHatchFluidGenerator implements IBiodomeCompatible {

    MTEBiodome connectedBiodome;

    public MTEHatchAirIntake(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchAirIntake(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchAirIntake(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getCustomTooltip() {
        String[] aTooltip = new String[4];
        aTooltip[0] = "§cDO NOT OBSTRUCT THE INPUT!";
        aTooltip[1] = "Draws in Air from the surrounding environment";
        aTooltip[2] = "Creates " + getAmountOfFluidToGenerate() + "L of Air every " + getMaxTickTime() + " ticks";
        aTooltip[3] = "§7Added by: §2Alkalus §7- §c[GT++]";
        return aTooltip;
    }

    @Override
    public Fluid getFluidToGenerate() {
        String id;
        if (connectedBiodome != null) {
            id = connectedBiodome.getDimensionOverride();
        } else {
            id = getBaseMetaTileEntity().getWorld().provider.getDimensionName();
        }

        if (id.equals(DimensionDef.Nether.name())) {
            return Materials.NetherAir.mFluid;
        } else {
            return Materials.Air.getGas(1)
                .getFluid();
        }
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 5000;
    }

    @Override
    public int getMaxTickTime() {
        return 20;
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
        final float ran1 = MTEHatchAirIntake.floatGen.nextFloat();
        float ran2 = 0.0f;
        float ran3 = 0.0f;
        ran2 = MTEHatchAirIntake.floatGen.nextFloat();
        ran3 = MTEHatchAirIntake.floatGen.nextFloat();

        final IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
        final ForgeDirection aDir = aMuffler.getFrontFacing();
        final float xPos = aDir.offsetX * 0.76f + aMuffler.getXCoord() + 0.25f;
        float yPos = aDir.offsetY * 0.76f + aMuffler.getYCoord() + 0.65f;
        final float zPos = aDir.offsetZ * 0.76f + aMuffler.getZCoord() + 0.25f;
        float ySpd = aDir.offsetY * 0.1f + 0.2f + 0.1f * MTEHatchAirIntake.floatGen.nextFloat();
        float xSpd;
        float zSpd;
        if (aDir.offsetY == -1) {
            // Logger.INFO("Y = -1");
            final float temp = MTEHatchAirIntake.floatGen.nextFloat() * 2.0f * GTPPCore.PI;
            xSpd = (float) Math.sin(temp) * 0.1f;
            zSpd = (float) Math.cos(temp) * 0.1f;
            ySpd = -ySpd;
            yPos = yPos - 0.8f;
        } else {
            xSpd = aDir.offsetX * (0.1f + 0.2f * MTEHatchAirIntake.floatGen.nextFloat());
            zSpd = aDir.offsetZ * (0.1f + 0.2f * MTEHatchAirIntake.floatGen.nextFloat());

            xSpd = -xSpd;
            zSpd = -zSpd;
        }

        aWorld.spawnParticle(
            name,
            xPos + ran1 * 0.5f,
            yPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            zPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            xSpd,
            -ySpd,
            zSpd);
        aWorld.spawnParticle(
            name,
            xPos + ran2 * 0.5f,
            yPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            zPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            xSpd,
            -ySpd,
            zSpd);
        aWorld.spawnParticle(
            name,
            xPos + ran3 * 0.5f,
            yPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            zPos + MTEHatchAirIntake.floatGen.nextFloat() * 0.5f,
            xSpd,
            -ySpd,
            zSpd);
    }

    @Override
    protected FluidSlotWidget createFluidSlot() {
        return super.createFluidSlot().setFilter(f -> f == Materials.Air.mGas);
    }

    @Override
    public void updateBiodome(MTEBiodome biodome) {
        connectedBiodome = biodome;
    }
}
