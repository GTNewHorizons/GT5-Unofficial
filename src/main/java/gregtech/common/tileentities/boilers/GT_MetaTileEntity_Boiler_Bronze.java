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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SteamVariant;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ValidationResult;
import gregtech.api.util.ValidationType;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.GT_Pollution;

public class GT_MetaTileEntity_Boiler_Bronze extends GT_MetaTileEntity_Boiler {

    public GT_MetaTileEntity_Boiler_Bronze(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            new String[] { "An early way to get Steam Power", "Produces 120L of Steam per second",
                "Causes " + GT_Mod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond + " Pollution per second" });
    }

    public GT_MetaTileEntity_Boiler_Bronze(int aID, String aName, String aNameRegional, String[] aDescription) {
        super(aID, aName, aNameRegional, aDescription);
    }

    public GT_MetaTileEntity_Boiler_Bronze(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Boiler_Bronze(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
    public int maxProgresstime() {
        return 500;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Boiler_Bronze(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
                && aBaseMetaTileEntity.getCoverIDAtSide(frontFacing) == 0
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
            GT_Pollution.addPollution(getBaseMetaTileEntity(), getPollution());
        }
    }

    @Override
    protected int getPollution() {
        return GT_Mod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond;
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
        int fuelItemCombustionEnergy = TileEntityFurnace.getItemBurnTime(mInventory[2]) / 10;
        if (fuelItemCombustionEnergy <= 0) return;
        ValidationResult<ItemStack> combustionResult = tryCombustFuel(mInventory[2]);
        if (combustionResult.getType() == ValidationType.VALID) {
            this.mProcessingEnergy += fuelItemCombustionEnergy;
            if (combustionResult.getResult() != null)
                aBaseMetaTileEntity.addStackToSlot(3, combustionResult.getResult());
            aBaseMetaTileEntity.decrStackSize(2, 1);
        }
    }

    @NotNull
    private static ValidationResult<ItemStack> tryCombustFuel(@NotNull ItemStack stack) {
        ValidationType valid = ValidationType.VALID;
        ItemStack ashes = null;
        if (isItemSolidCarbonFuelItem(stack)) {
            ashes = combustSolidCarbonFuelItem(stack);
        } else if (isItemSolidCarbonFuelBlock(stack)) {
            ashes = combustSolidCarbonFuelBlock(stack);
        } else if (isDenseSolidFuel(stack)) {
            ashes = combustDenseSolidFuel(stack);
        } else {
            valid = ValidationType.INVALID;
        }
        return ValidationResult.of(valid, ashes);
    }

    private static boolean isItemSolidCarbonFuelItem(@NotNull ItemStack fuelStack) {
        return (GT_Utility.isPartOfMaterials(fuelStack, Materials.Coal)
            && !GT_Utility.isPartOfOrePrefix(fuelStack, OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(fuelStack, Materials.Charcoal)
                && !GT_Utility.isPartOfOrePrefix(fuelStack, OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(fuelStack, Materials.Lignite)
                && !GT_Utility.isPartOfOrePrefix(fuelStack, OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(fuelStack, Materials.Diamond)
                && !GT_Utility.isPartOfOrePrefix(fuelStack, OrePrefixes.block))
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, "fuelCoke")
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, "fuelCactusCharcoal")
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, "fuelCactusCoke")
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, "fuelSugarCharcoal")
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, "fuelSugarCoke");
    }

    private static ItemStack combustSolidCarbonFuelItem(@NotNull ItemStack stack) {
        return XSTR.XSTR_INSTANCE.nextInt(
            GT_Utility.isPartOfMaterials(stack, Materials.Coal)
                || GT_Utility.isPartOfMaterials(stack, Materials.Charcoal) ? 3
                    : GT_Utility.isPartOfMaterials(stack, Materials.Lignite) ? 8 : 2)
            == 0 ? GT_OreDictUnificator.get(OrePrefixes.dustTiny, (GT_Utility.isPartOfMaterials(stack, Materials.Lignite) || GT_Utility.isPartOfMaterials(stack, Materials.Coal)) ? Materials.DarkAsh : Materials.Ash, 1L) : null;
    }

    private static boolean isItemSolidCarbonFuelBlock(@NotNull ItemStack fuelStack) {
        // If its a block of the following materials
        return GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, OrePrefixes.block.get(Materials.Coal))
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, OrePrefixes.block.get(Materials.Lignite))
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, OrePrefixes.block.get(Materials.Charcoal))
            || GT_OreDictUnificator.isItemStackInstanceOf(fuelStack, OrePrefixes.block.get(Materials.Diamond))
            ||

            // if its either a Railcraft Coke Block or a custom GTNH compressed Coal/charcoal/lignite/coke block
            (Block.getBlockFromItem(fuelStack.getItem()) != null && // check if the block exists
                (Block.getBlockFromItem(fuelStack.getItem())
                    .getUnlocalizedName()
                    .toLowerCase()
                    .contains("tile") && // check if the block is a tile -> block
                    (
                    // If the name of the block contains these names
                    Block.getBlockFromItem(fuelStack.getItem())
                        .getUnlocalizedName()
                        .toLowerCase()
                        .contains("charcoal")
                        || Block.getBlockFromItem(fuelStack.getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("coal")
                        || Block.getBlockFromItem(fuelStack.getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("diamond")
                        || Block.getBlockFromItem(fuelStack.getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("coke")
                        || Block.getBlockFromItem(fuelStack.getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("railcraft.cube")
                        || Block.getBlockFromItem(fuelStack.getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("lignite"))));
    }

    private static ItemStack combustSolidCarbonFuelBlock(@NotNull ItemStack stack) {
        return GT_OreDictUnificator.get(
            OrePrefixes.dust,
            (GT_Utility.isPartOfMaterials(stack, Materials.Lignite)
                || GT_Utility.isPartOfMaterials(stack, Materials.Coal)
                || Block.getBlockFromItem(stack.getItem())
                    .getUnlocalizedName()
                    .toLowerCase()
                    .contains("coal")
                || Block.getBlockFromItem(stack.getItem())
                    .getUnlocalizedName()
                    .toLowerCase()
                    .contains("lignite")) ? Materials.DarkAsh : Materials.Ash,
            1L);
    }

    private static boolean isDenseSolidFuel(@NotNull ItemStack fuelStack) {
        // enables every other fuel with at least 2000 burntime as a fuel,
        // i.e. peat, Magic/Solid Super Fuel, Coal
        // Singularities, Nitor, while bucket of creosite should be blocked
        // same goes for lava
        return (TileEntityFurnace.getItemBurnTime(fuelStack)) >= 2000 && !(fuelStack.getUnlocalizedName()
            .toLowerCase()
            .contains("bucket")
            || fuelStack.getUnlocalizedName()
                .toLowerCase()
                .contains("cell"));
    }

    private static ItemStack combustDenseSolidFuel(@NotNull ItemStack stack) {
        // adds tiny pile of ash for burntime under 10k, small pile for
        // under 100k and pile for
        // bigger values
        return (XSTR.XSTR_INSTANCE.nextInt(2) == 0) ? GT_OreDictUnificator.get(
            (TileEntityFurnace.getItemBurnTime(stack) >= 10000
                ? TileEntityFurnace.getItemBurnTime(stack) >= 100000 ? OrePrefixes.dust : OrePrefixes.dustSmall
                : OrePrefixes.dustTiny),
            Materials.Ash,
            1L) : null;
    }

    @Override
    protected boolean isItemValidFuel(@NotNull ItemStack stack) {
        return (TileEntityFurnace.getItemBurnTime(stack) / 10) > 0
            && (isItemSolidCarbonFuelItem(stack) || isItemSolidCarbonFuelBlock(stack) || isDenseSolidFuel(stack));
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }
}
