package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEHatchReservoir extends MTEHatchFluidGenerator {

    public MTEHatchReservoir(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchReservoir(final String aName, final int aTier, final String[] aDescription,
        final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEHatchReservoir(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getCustomTooltip() {
        String[] aTooltip = new String[2];
        aTooltip[0] = "Infinite water supply hatch";
        aTooltip[1] = "Fills to max capacity every 5 seconds";
        return aTooltip;
    }

    @Override
    public Fluid getFluidToGenerate() {
        return FluidRegistry.WATER;
    }

    @Override
    public int getAmountOfFluidToGenerate() {
        return 2_000_000_000;
    }

    @Override
    public int getMaxTickTime() {
        return 100;
    }

    @Override
    public int getCapacity() {
        return 2_000_000_000;
    }

    @Override
    public boolean doesHatchMeetConditionsToGenerate() {
        return true;
    }

    @Override
    public void generateParticles(World aWorld, String name) {}

    @Override
    public ITexture[] getTexturesActive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(TexturesGtBlock.Overlay_Water) };
    }

    @Override
    public ITexture[] getTexturesInactive(final ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(TexturesGtBlock.Overlay_Water) };
    }
}
