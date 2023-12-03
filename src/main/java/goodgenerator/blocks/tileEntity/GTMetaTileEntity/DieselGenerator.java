package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.DIESEL_GENERATOR_SIDE_ACTIVE_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class DieselGenerator extends GT_MetaTileEntity_BasicGenerator {

    public int mEfficiency;

    public DieselGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                new String[] { "Requires liquid Fuel", "Causes "
                        + (int) (GT_Mod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond * (1.1 - aTier * 0.1))
                        + " Pollution per second" });
        mEfficiency = 100 - aTier * 10;
    }

    public DieselGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        mEfficiency = 100 - aTier * 10;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new DieselGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.dieselFuels;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency;
    }

    @Override
    public int getFuelValue(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack) || getRecipeMap() == null) return 0;
        long rValue = Math.max(GT_ModHandler.getFuelCanValue(aStack) * 6 / 5, super.getFuelValue(aStack));
        if (ItemList.Fuel_Can_Plastic_Filled.isStackEqual(aStack, false, true)) {
            rValue = Math.max(rValue, GameRegistry.getFuelValue(aStack) * 3L);
        }
        if (rValue > Integer.MAX_VALUE) {
            throw new ArithmeticException("Integer LOOPBACK!");
        }
        return (int) rValue;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 100 == 0 && mFluid != null && mFluid.amount > this.getCapacity()) {
            GT_Log.err.println(
                    "Dupe Abuse: " + aBaseMetaTileEntity.getOwnerName()
                            + " Coords: "
                            + aBaseMetaTileEntity.getXCoord()
                            + " "
                            + aBaseMetaTileEntity.getYCoord()
                            + " "
                            + aBaseMetaTileEntity.getZCoord());
            aBaseMetaTileEntity.setToFire();
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_FRONT),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_FRONT_GLOW).glow().build()),
                OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_BACK),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_BACK_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_BOTTOM),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_BOTTOM_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_TOP),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_TOP_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_SIDE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_SIDE_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_FRONT_ACTIVE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_FRONT_ACTIVE_GLOW).glow().build()),
                OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_BACK_ACTIVE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_BACK_ACTIVE_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_BOTTOM_ACTIVE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_BOTTOM_ACTIVE_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_TOP_ACTIVE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_TOP_ACTIVE_GLOW).glow().build()) };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
                TextureFactory.of(
                        TextureFactory.of(DIESEL_GENERATOR_SIDE_ACTIVE),
                        TextureFactory.builder().addIcon(DIESEL_GENERATOR_SIDE_ACTIVE_GLOW).glow().build()) };
    }

    @Override
    public int getPollution() {
        return (int) (GT_Mod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond * (1.1 - mTier * 0.1));
    }
}
