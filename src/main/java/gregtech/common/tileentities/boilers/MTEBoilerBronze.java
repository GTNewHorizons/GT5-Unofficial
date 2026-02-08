package gregtech.common.tileentities.boilers;

import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.BOILER_FRONT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.pollution.Pollution;

public class MTEBoilerBronze extends MTEBoiler {

    public MTEBoilerBronze(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            new String[] { "An early way to get Steam Power", "Produces 120L of Steam per second",
                "Causes " + GTMod.proxy.mPollutionSmallCoalBoilerPerSecond + " Pollution per second" });
    }

    public MTEBoilerBronze(int aID, String aName, String aNameRegional, String[] aDescription) {
        super(aID, aName, aNameRegional, aDescription);
    }

    public MTEBoilerBronze(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[5][17][];
        final ITexture[] texBottom = { TextureFactory.of(MACHINE_BRONZEBRICKS_BOTTOM) },
            texTop = { TextureFactory.of(MACHINE_BRONZEBRICKS_TOP), TextureFactory.of(OVERLAY_PIPE) },
            texSide = { TextureFactory.of(MACHINE_BRONZEBRICKS_SIDE), TextureFactory.of(OVERLAY_PIPE) },
            texFront = { TextureFactory.of(MACHINE_BRONZEBRICKS_SIDE), TextureFactory.of(BOILER_FRONT),
                TextureFactory.builder()
                    .addIcon(BOILER_FRONT_GLOW)
                    .glow()
                    .build() },
            texFrontActive = { TextureFactory.of(MACHINE_BRONZEBRICKS_SIDE), TextureFactory.of(BOILER_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(BOILER_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
        for (int i = 0; i < 17; i++) {
            rTextures[0][i] = texBottom;
            rTextures[1][i] = texTop;
            rTextures[2][i] = texSide;
            rTextures[3][i] = texFront;
            rTextures[4][i] = texFrontActive;
        }
        return rTextures;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBoilerBronze(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    /**
     * Draws random flames and smoke particles in front of active boiler
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@link Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isActive()) {

            final ForgeDirection frontFacing = aBaseMetaTileEntity.getFrontFacing();

            if ((frontFacing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0
                && !aBaseMetaTileEntity.hasCoverAtSide(frontFacing)
                && !aBaseMetaTileEntity.getOpacityAtSide(frontFacing)) {

                final double oX = aBaseMetaTileEntity.getOffsetX(frontFacing, 1) + 8D / 16D;
                final double oY = aBaseMetaTileEntity.getOffsetY(frontFacing, 1);
                final double oZ = aBaseMetaTileEntity.getOffsetZ(frontFacing, 1) + 8D / 16D;
                final double offset = -0.48D;
                final double horizontal = XSTR_INSTANCE.nextFloat() * 10D / 16D - 5D / 16D;

                final double x, y, z;

                y = oY + XSTR_INSTANCE.nextFloat() * 6D / 16D;

                if (frontFacing == ForgeDirection.WEST) {
                    x = oX - offset;
                    z = oZ + horizontal;
                } else if (frontFacing == ForgeDirection.EAST) {
                    x = oX + offset;
                    z = oZ + horizontal;
                } else if (frontFacing == ForgeDirection.NORTH) {
                    x = oX + horizontal;
                    z = oZ - offset;
                } else // if (frontFacing == ForgeDirection.SOUTH)
                {
                    x = oX + horizontal;
                    z = oZ + offset;
                }

                ParticleEventBuilder particleEventBuilder = (new ParticleEventBuilder()).setMotion(0D, 0D, 0D)
                    .setPosition(x, y, z)
                    .setWorld(getBaseMetaTileEntity().getWorld());
                particleEventBuilder.setIdentifier(ParticleFX.SMOKE)
                    .run();
                particleEventBuilder.setIdentifier(ParticleFX.FLAME)
                    .run();
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)
            && this.mProcessingEnergy > 0
            && (aTick % 20L == 0L)) {
            Pollution.addPollution(getBaseMetaTileEntity(), getPollution());
        }
    }

    @Override
    protected int getPollution() {
        return GTMod.proxy.mPollutionSmallCoalBoilerPerSecond;
    }

    @Override
    protected int getProductionPerSecond() {
        return 120;
    }

    @Override
    protected int getMaxTemperature() {
        return 500;
    }

    @Override
    protected int getEnergyConsumption() {
        return 1;
    }

    @Override
    protected int getCooldownInterval() {
        return 45;
    }

    @Override
    protected void updateFuel(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        ItemStack fuel = mInventory[2];
        int burnTime = TileEntityFurnace.getItemBurnTime(fuel);
        getCombustionPotential(fuel, burnTime).ifPresent(ashMaterial -> {
            aBaseMetaTileEntity.decrStackSize(2, 1);
            this.mProcessingEnergy += burnTime / 10;
            boolean isABlock = !Block.getBlockFromItem(fuel.getItem())
                .equals(Blocks.air);
            combustFuel(burnTime, isABlock).map(dustSize -> GTOreDictUnificator.get(dustSize, ashMaterial, 1L))
                .ifPresent(ashes -> aBaseMetaTileEntity.addStackToSlot(3, ashes));
        });
    }

    private static Optional<Materials> getCombustionPotential(ItemStack fuel, int burnTime) {
        if (burnTime / 10 <= 0 || FluidContainerRegistry.isFilledContainer(fuel)) {
            return Optional.empty();
        }
        String lowerCaseBlockName = Block.getBlockFromItem(fuel.getItem())
            .getUnlocalizedName()
            .toLowerCase();
        if (couldProduceDarkAshes(fuel, lowerCaseBlockName)) {
            return Optional.of(Materials.AshDark);
        }
        if (couldProduceRegularAshes(fuel, lowerCaseBlockName, burnTime)) {
            return Optional.of(Materials.Ash);
        }
        return Optional.empty();
    }

    private static boolean couldProduceDarkAshes(ItemStack fuel, String lowerCaseBlockName) {
        return GTUtility.isPartOfMaterials(fuel, Materials.Coal) || GTUtility.isPartOfMaterials(fuel, Materials.Lignite)
            || lowerCaseBlockName.matches("tile\\..+compressedcoal");
    }

    private static boolean couldProduceRegularAshes(ItemStack fuel, String lowerCaseBlockName, int burnTime) {
        return GTUtility.isPartOfMaterials(fuel, Materials.Charcoal)
            || GTUtility.isPartOfMaterials(fuel, Materials.Diamond)
            || (Stream.of("^tile\\..+charcoal", "^tile\\..+coke", "^tile\\..+railcraft.cube")
                .anyMatch(lowerCaseBlockName::matches))
            || Stream.of("fuelCoke", "fuelCactusCharcoal", "fuelCactusCoke", "fuelSugarCharcoal", "fuelSugarCoke")
                .anyMatch(name -> GTOreDictUnificator.isItemStackInstanceOf(fuel, name))
            || burnTime >= 2000;
    }

    private static Optional<OrePrefixes> combustFuel(int burnTime, boolean isABlock) {
        if (isABlock) {
            return Optional.of(OrePrefixes.dust);
        } else if (XSTR.XSTR_INSTANCE.nextInt(getAshChanceBound(burnTime)) == 0) {
            if (burnTime > 100000) {
                return Optional.of(OrePrefixes.dust);
            } else if (burnTime > 10000) {
                return Optional.of(OrePrefixes.dustSmall);
            } else {
                return Optional.of(OrePrefixes.dustTiny);
            }
        }
        return Optional.empty();
    }

    /**
     * The upper bound for the chance to get ash from combustion <br>
     * Ash chance scales based on burn time from 14% at 0 up to 50% at 2000
     *
     * @param burnTime number assumed to be positive
     * @return an upper bound between 7 and 2.
     */
    private static int getAshChanceBound(int burnTime) {
        return (5 - (Math.min(burnTime, 2000) / 400)) + 2;
    }

    @Override
    protected boolean isItemValidFuel(@NotNull ItemStack stack) {
        return getCombustionPotential(stack, TileEntityFurnace.getItemBurnTime(stack)).isPresent();
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.BRONZE;
    }
}
