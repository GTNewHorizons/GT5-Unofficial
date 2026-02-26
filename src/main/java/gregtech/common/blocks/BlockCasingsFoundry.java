package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IBlockWithActiveOffset;
import gregtech.api.interfaces.IBlockWithClientMeta;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.common.data.GTCoilTracker;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.render.GTRendererBlock;

/**
 * The base class for casings. Casings are the blocks that are mainly used to build multiblocks.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasingsFoundry extends BlockCasingsAbstract
    implements IBlockWithActiveOffset, IBlockWithClientMeta, IBlockWithTextures {

    public BlockCasingsFoundry() {
        super(ItemCasingsFoundry.class, "gt.foundrycasings", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.Primary_Casing_ExoFoundry);
        register(1, ItemList.Magnetic_Chassis_T1_ExoFoundry);
        register(2, ItemList.Magnetic_Chassis_T2_ExoFoundry);
        register(3, ItemList.Magnetic_Chassis_T3_ExoFoundry);
        register(4, ItemList.Universal_Collapser_ExoFoundry);
        register(5, ItemList.Efficient_Overclocking_ExoFoundry);
        register(6, ItemList.Power_Efficient_Subsystems_ExoFoundry);
        register(7, ItemList.Heliocast_Reinforcement_ExoFoundry);
        register(8, ItemList.Extra_Casting_Basins_ExoFoundry);
        register(9, ItemList.Hypercooler_ExoFoundry);
        register(10, ItemList.Streamlined_Casters_ExoFoundry);
        register(11, ItemList.Secondary_Casing_ExoFoundry);
        register(12, ItemList.Central_Casing_ExoFoundry);
        for (int i = 1; i <= 3; i++) {
            GTStructureChannels.MAGNETIC_CHASSIS.registerAsIndicator(new ItemStack(this, 1, i), i);
        }

    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (8 << 7) | ((aMeta % ACTIVE_OFFSET) + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        switch (aMeta % ACTIVE_OFFSET) {
            case 0 -> {
                return Textures.BlockIcons.EXOFOUNDRY_CASING.getIcon();
            }
            case 1 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS.getIcon();
            }
            case 2 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS.getIcon();
            }
            case 3 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS.getIcon();
            }
            case 4 -> {
                return Textures.BlockIcons.EXOFOUNDRY_UNIVERSAL_COLLAPSER_ACTIVE.getIcon();
            }
            case 5 -> {
                return Textures.BlockIcons.EXOFOUNDRY_EFFICIENT_OVERCLOCKING.getIcon();
            }
            case 6 -> {
                return Textures.BlockIcons.EXOFOUNDRY_POWER_EFFICIENT_SUBSYSTEMS.getIcon();
            }
            case 7 -> {
                return Textures.BlockIcons.EXOFOUNDRY_HELIOCAST_REINFORCEMENT.getIcon();
            }
            case 8 -> {
                return Textures.BlockIcons.EXOFOUNDRY_EXTRA_CASTING_BASINS.getIcon();
            }
            case 9 -> {
                return Textures.BlockIcons.EXOFOUNDRY_HYPERCOOLER.getIcon();
            }
            case 10 -> {
                return Textures.BlockIcons.EXOFOUNDRY_STREAMLINED_CASTERS.getIcon();
            }
            case 11 -> {
                return Textures.BlockIcons.EXOFOUNDRY_SECONDARY_CASING.getIcon();
            }
            case 12 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    return Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING_TOP.getIcon();
                return Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING.getIcon();
            }
            default -> {
                return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            }

        }
    }

    @Override
    public int getClientMeta(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if ((meta == 12 || meta == 4) && GTCoilTracker.isCoilActive(world, x, y, z)) meta += ACTIVE_OFFSET;
        return meta;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return super.getDamageValue(aWorld, aX, aY, aZ) % ACTIVE_OFFSET;
    }

    @Override
    public int damageDropped(int metadata) {
        return super.damageDropped(metadata) % ACTIVE_OFFSET;
    }

    @Override
    public @Nullable ITexture[][] getTextures(int metadata) {
        List<ITexture> textures = new ArrayList<>();
        List<ITexture> topTextures = new ArrayList<>();
        IIconContainer texture = switch (metadata % ACTIVE_OFFSET) {
            case 0 -> Textures.BlockIcons.EXOFOUNDRY_CASING;
            case 1 -> Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS;
            case 2 -> Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS;
            case 3 -> Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS;
            case 4 -> Textures.BlockIcons.EXOFOUNDRY_UNIVERSAL_COLLAPSER;
            case 5 -> Textures.BlockIcons.EXOFOUNDRY_EFFICIENT_OVERCLOCKING;
            case 6 -> Textures.BlockIcons.EXOFOUNDRY_POWER_EFFICIENT_SUBSYSTEMS;
            case 7 -> Textures.BlockIcons.EXOFOUNDRY_HELIOCAST_REINFORCEMENT;
            case 8 -> Textures.BlockIcons.EXOFOUNDRY_EXTRA_CASTING_BASINS;
            case 9 -> Textures.BlockIcons.EXOFOUNDRY_HYPERCOOLER;
            case 10 -> Textures.BlockIcons.EXOFOUNDRY_STREAMLINED_CASTERS;
            case 11 -> Textures.BlockIcons.EXOFOUNDRY_SECONDARY_CASING;
            case 12 -> Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING;
            default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_BACKGROUND;
        };
        textures.add(TextureFactory.of(texture));
        IIconContainer topTexture = switch (metadata % ACTIVE_OFFSET) {
            case 1 -> Textures.BlockIcons.EXOFOUNDRY_INFINITE_CHASSIS_TOP;
            case 2 -> Textures.BlockIcons.EXOFOUNDRY_ETERNAL_CHASSIS_TOP;
            case 3 -> Textures.BlockIcons.EXOFOUNDRY_CELESTIAL_CHASSIS_TOP;
            case 12 -> Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING_TOP;
            default -> texture;
        };

        if (metadata >= ACTIVE_OFFSET) {
            if (metadata % ACTIVE_OFFSET == 12) {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING_ACTIVE)
                        .glow()
                        .build());
            }
            if (metadata % ACTIVE_OFFSET == 4) {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.EXOFOUNDRY_UNIVERSAL_COLLAPSER_ACTIVE)
                        .build());
            }

        }

        topTextures.add(TextureFactory.of(topTexture));
        ITexture[] standardLayers = textures.toArray(new ITexture[0]);
        ITexture[] topLayers = topTextures.toArray(new ITexture[0]);
        return new ITexture[][] { topLayers, topLayers, standardLayers, standardLayers, standardLayers,
            standardLayers };
    }

    @Override
    public @Nullable ITexture[][] getInventoryTextures(int meta) {
        if (meta % ACTIVE_OFFSET == 4) {
            ITexture[] standard = new ITexture[] { TextureFactory.builder()
                .addIcon(Textures.BlockIcons.EXOFOUNDRY_UNIVERSAL_COLLAPSER_ACTIVE)
                .build() };
            return new ITexture[][] { standard, standard, standard, standard, standard, standard };
        } else {
            return getTextures(meta);
        }
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
