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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

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

            final byte frontFacing = aBaseMetaTileEntity.getFrontFacing();

            if (frontFacing > 1 && aBaseMetaTileEntity.getCoverIDAtSide(frontFacing) == 0
                && !aBaseMetaTileEntity.getOpacityAtSide(frontFacing)) {

                final double oX = aBaseMetaTileEntity.getOffsetX(frontFacing, 1) + 8D / 16D;
                final double oY = aBaseMetaTileEntity.getOffsetY(frontFacing, 1);
                final double oZ = aBaseMetaTileEntity.getOffsetZ(frontFacing, 1) + 8D / 16D;
                final double offset = -0.48D;
                final double horizontal = XSTR_INSTANCE.nextFloat() * 10D / 16D - 5D / 16D;

                final double x, y, z;

                y = oY + XSTR_INSTANCE.nextFloat() * 6D / 16D;

                if (frontFacing == ForgeDirection.WEST.ordinal()) {
                    x = oX - offset;
                    z = oZ + horizontal;
                } else if (frontFacing == ForgeDirection.EAST.ordinal()) {
                    x = oX + offset;
                    z = oZ + horizontal;
                } else if (frontFacing == ForgeDirection.NORTH.ordinal()) {
                    x = oX + horizontal;
                    z = oZ - offset;
                } else // if (frontFacing == ForgeDirection.SOUTH.ordinal())
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
        if (this.mInventory[2] == null) return;
        if ((GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Coal)
            && !GT_Utility.isPartOfOrePrefix(this.mInventory[2], OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Charcoal)
                && !GT_Utility.isPartOfOrePrefix(this.mInventory[2], OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Lignite)
                && !GT_Utility.isPartOfOrePrefix(this.mInventory[2], OrePrefixes.block))
            || (GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Diamond)
                && !GT_Utility.isPartOfOrePrefix(this.mInventory[2], OrePrefixes.block))
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelCoke")
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelCactusCharcoal")
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelCactusCoke")
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelSugarCharcoal")
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], "fuelSugarCoke")) {
            if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10) > 0) {
                this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10);
                aBaseMetaTileEntity.decrStackSize(2, 1);
                if (XSTR.XSTR_INSTANCE.nextInt(
                    GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Coal)
                        || GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Charcoal) ? 3
                            : GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Lignite) ? 8 : 2)
                    == 0) {
                    aBaseMetaTileEntity.addStackToSlot(
                        3,
                        GT_OreDictUnificator.get(
                            OrePrefixes.dustTiny,
                            (GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Lignite)
                                || GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Coal)) ? Materials.DarkAsh
                                    : Materials.Ash,
                            1L));
                }
            }
        } else if (
        // If its a block of the following materials
        GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Coal))
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Lignite))
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Charcoal))
            || GT_OreDictUnificator.isItemStackInstanceOf(this.mInventory[2], OrePrefixes.block.get(Materials.Diamond))
            ||

            // if its either a Railcraft Coke Block or a custom GTNH compressed Coal/charcoal/lignite/coke block
            (Block.getBlockFromItem(this.mInventory[2].getItem()) != null && // check if the block exists
                (Block.getBlockFromItem(this.mInventory[2].getItem())
                    .getUnlocalizedName()
                    .toLowerCase()
                    .contains("tile") && // check if the block is a tile -> block
                    (
                    // If the name of the block contains these names
                    Block.getBlockFromItem(this.mInventory[2].getItem())
                        .getUnlocalizedName()
                        .toLowerCase()
                        .contains("charcoal")
                        || Block.getBlockFromItem(this.mInventory[2].getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("coal")
                        || Block.getBlockFromItem(this.mInventory[2].getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("diamond")
                        || Block.getBlockFromItem(this.mInventory[2].getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("coke")
                        || Block.getBlockFromItem(this.mInventory[2].getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("railcraft.cube")
                        || Block.getBlockFromItem(this.mInventory[2].getItem())
                            .getUnlocalizedName()
                            .toLowerCase()
                            .contains("lignite"))))) {
                                // try to add 10% of the burnvalue as Processing energy, no boost
                                // for coal coke here
                                if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10) > 0) {
                                    this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2])
                                        / 10);
                                    aBaseMetaTileEntity.decrStackSize(2, 1);
                                    aBaseMetaTileEntity.addStackToSlot(
                                        3,
                                        GT_OreDictUnificator.get(
                                            OrePrefixes.dust,
                                            (GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Lignite)
                                                || GT_Utility.isPartOfMaterials(this.mInventory[2], Materials.Coal)
                                                || Block.getBlockFromItem(this.mInventory[2].getItem())
                                                    .getUnlocalizedName()
                                                    .toLowerCase()
                                                    .contains("coal")
                                                || Block.getBlockFromItem(this.mInventory[2].getItem())
                                                    .getUnlocalizedName()
                                                    .toLowerCase()
                                                    .contains("lignite")) ? Materials.DarkAsh : Materials.Ash,
                                            1L));
                                }
                                // enables every other fuel with at least 2000 burntime as a fuel,
                                // i.e. peat, Magic/Solid Super Fuel, Coal
                                // Singularities, Nitor, while bucket of creosite should be blocked
                                // same goes for lava
                            } else
            if ((TileEntityFurnace.getItemBurnTime(this.mInventory[2])) >= 2000
                && !(this.mInventory[2].getUnlocalizedName()
                    .toLowerCase()
                    .contains("bucket")
                    || this.mInventory[2].getUnlocalizedName()
                        .toLowerCase()
                        .contains("cell"))) {
                            this.mProcessingEnergy += (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) / 10);
                            aBaseMetaTileEntity.decrStackSize(2, 1);
                            // adds tiny pile of ash for burntime under 10k, small pile for
                            // under 100k and pile for
                            // bigger values
                            if (XSTR.XSTR_INSTANCE.nextInt(2) == 0)
                                aBaseMetaTileEntity.addStackToSlot(
                                    3,
                                    GT_OreDictUnificator.get(
                                        (TileEntityFurnace.getItemBurnTime(this.mInventory[2]) >= 10000
                                            ? TileEntityFurnace.getItemBurnTime(this.mInventory[2]) >= 100000
                                                ? OrePrefixes.dust
                                                : OrePrefixes.dustSmall
                                            : OrePrefixes.dustTiny),
                                        Materials.Ash,
                                        1L));
                        }
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }
}
