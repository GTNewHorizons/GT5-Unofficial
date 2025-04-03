package gregtech.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTRenderingWorld;
import gregtech.common.tileentities.machines.multi.MTELargeTurbine;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings8 extends BlockCasingsAbstract {

    public static boolean mConnectedMachineTextures = true;

    // WATCH OUT FOR TEXTURE ID's
    public BlockCasings8() {
        super(ItemCasings.class, "gt.blockcasings8", MaterialCasings.INSTANCE, 15);
        /*
         * DO NOT USE INDEX 15 ! USED HERE: https://github.com/GTNewHorizons/KubaTech/pull/101
         */

        register(0, ItemList.Casing_Chemically_Inert, "Chemically Inert Machine Casing");
        register(1, ItemList.Casing_Pipe_Polytetrafluoroethylene, "PTFE Pipe Casing");
        register(2, ItemList.Casing_MiningNeutronium, "Mining Neutronium Casing");
        register(3, ItemList.Casing_MiningBlackPlutonium, "Mining Black Plutonium Casing");
        register(4, ItemList.Casing_ExtremeEngineIntake, "Extreme Engine Intake Casing");
        register(5, ItemList.Casing_AdvancedRadiationProof, "Europium Reinforced Radiation Proof Machine Casing");
        register(6, ItemList.Casing_Advanced_Rhodium_Palladium, "Advanced Rhodium Plated Palladium Machine Casing");
        register(7, ItemList.Casing_Advanced_Iridium, "Advanced Iridium Plated Machine Casing");
        register(8, ItemList.Casing_Magical, "Magical Machine Casing");
        register(9, ItemList.Casing_TurbineGasAdvanced, "HSS-S Turbine Casing");
        register(10, ItemList.RadiantNaquadahAlloyCasing, "Radiant Naquadah Alloy Casing");
        register(11, ItemList.BasicPhotolithographicFrameworkCasing, "Basic Photolithographic Framework Casing");
        register(
            12,
            ItemList.ReinforcedPhotolithographicFrameworkCasing,
            "Reinforced Photolithographic Framework Casing");
        register(
            13,
            ItemList.RadiationProofPhotolithographicFrameworkCasing,
            "Radiation Proof Photolithographic Framework Casing");
        register(14, ItemList.InfinityCooledCasing, "Infinity Cooled Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta + 48);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_MINING_NEUTRONIUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_MINING_BLACKPLUTONIUM.getIcon();
            // changed color in a terrible way
            case 4 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_ENGINE_INTAKE.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ADVANCEDRADIATIONPROOF.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_RHODIUM_PALLADIUM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_IRIDIUM.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_MAGICAL.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_HSSS.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_RADIANT_NAQUADAH_ALLOY.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_1.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_2.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_3.getIcon();
            case 14 -> Textures.BlockIcons.INFINITY_COOLED_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }

    public IIcon getTurbineCasing(int meta, int iconIndex, boolean active, boolean hasTurbine) {
        // noinspection SwitchStatementWithTooFewBranches // "if" is harder to edit
        return switch (meta) {
            case 9 -> active ? Textures.BlockIcons.TURBINE_ADVGASACTIVE[iconIndex].getIcon()
                : hasTurbine ? Textures.BlockIcons.TURBINEADVGAS[iconIndex].getIcon()
                    : Textures.BlockIcons.TURBINE_ADVGASEMPTY[iconIndex].getIcon();
            default -> active ? Textures.BlockIcons.TURBINE_ACTIVE[iconIndex].getIcon()
                : hasTurbine ? Textures.BlockIcons.TURBINE[iconIndex].getIcon()
                    : Textures.BlockIcons.TURBINE_EMPTY[iconIndex].getIcon();
        };
    }

    private static int isTurbineControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, int ordinalSide) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof MTELargeTurbine turbine && tTile.getFrontFacing()
            .ordinal() == ordinalSide) {
            if (turbine.isNewStyleRendering()) return 0;
            if (tTile.isActive()) return 1;
            return turbine.hasTurbine() ? 2 : 3;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        aWorld = GTRenderingWorld.getInstance(aWorld);
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);

        if (tMeta != 9 || !mConnectedMachineTextures) {
            return getIcon(ordinalSide, tMeta);
        }

        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;

        switch (ordinalSide / 2) {
            case 0 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, ordinalSide))
                            != 0) {
                            return getTurbineCasing(tMeta, 4 - i * 3 - j, tState == 1, tState == 2);
                        }
                    }
                }
            }
            case 1 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, ordinalSide))
                            != 0) {
                            return getTurbineCasing(
                                tMeta,
                                4 + i * 3 - j * tInvertLeftRightMod,
                                tState == 1,
                                tState == 2);
                        }
                    }
                }
            }
            case 2 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, ordinalSide))
                            != 0) {
                            return getTurbineCasing(
                                tMeta,
                                4 + i * 3 + j * tInvertLeftRightMod,
                                tState == 1,
                                tState == 2);
                        }
                    }
                }
            }
        }

        return Textures.BlockIcons.MACHINE_CASING_TURBINE_HSSS.getIcon();
    }
}
