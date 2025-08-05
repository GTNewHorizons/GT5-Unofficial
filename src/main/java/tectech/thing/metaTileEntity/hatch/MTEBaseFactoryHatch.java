package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public abstract class MTEBaseFactoryHatch extends MTEHatch {

    public static final CustomIcon EM_D_ACTIVE = new CustomIcon("iconsets/OVERLAY_EM_D_ACTIVE");
    public static final CustomIcon EM_D_SIDES = new CustomIcon("iconsets/OVERLAY_EM_D_SIDES");
    public static final CustomIcon EM_D_CONN = new CustomIcon("iconsets/EM_DATA_CONN");

    protected MTEBaseFactoryHatch(MTEBaseFactoryHatch prototype) {
        super(prototype.mName, prototype.mTier, 0, prototype.mDescriptionArray, prototype.mTextures);
    }

    public MTEBaseFactoryHatch(int id, String name, String nameRegional, int tier, String[] description) {
        super(id, name, nameRegional, tier, 0, description);
    }

    @Override
    public abstract MetaTileEntity newMetaEntity(IGregTechTileEntity igte);

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.builder()
            .addIcon(EM_D_ACTIVE)
            .setRGBA(Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA()))
            .build(), TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.builder()
            .addIcon(EM_D_SIDES)
            .setRGBA(Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA()))
            .build(), TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack fluid) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidSlot(int index) {
        return false;
    }
}
