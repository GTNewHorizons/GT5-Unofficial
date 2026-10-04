package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

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
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings1 extends BlockCasingsAbstract
    implements IBlockWithActiveOffset, IBlockWithClientMeta, IBlockWithTextures {

    /**
     * Texture Index Information Textures.BlockIcons.casingTexturePages[0][0-63] - Gregtech
     * Textures.BlockIcons.casingTexturePages[0][64-127] - GT++ Textures.BlockIcons.casingTexturePages[1][0-127] -
     * Gregtech Textures.BlockIcons.casingTexturePages[2][0-127] - Free Textures.BlockIcons.casingTexturePages[3][0-127]
     * - Free Textures.BlockIcons.casingTexturePages[4][0-127] - Free Textures.BlockIcons.casingTexturePages[5][0-127] -
     * Free Textures.BlockIcons.casingTexturePages[6][0-127] - Free Textures.BlockIcons.casingTexturePages[7][0-127] -
     * TecTech Textures.BlockIcons.casingTexturePages[8][0-127] - TecTech
     */
    public BlockCasings1() {
        super(ItemCasings.class, "gt.blockcasings", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_ULV);
        register(1, ItemList.Casing_LV);
        register(2, ItemList.Casing_MV);
        register(3, ItemList.Casing_HV);
        register(4, ItemList.Casing_EV);
        register(5, ItemList.Casing_IV);
        register(6, ItemList.Casing_LuV);
        register(7, ItemList.Casing_ZPM);
        register(8, ItemList.Casing_UV);
        register(9, ItemList.Casing_MAX);
        register(10, ItemList.Casing_BronzePlatedBricks);
        register(11, ItemList.Casing_HeatProof);
        register(12, ItemList.Casing_Dim_Trans);
        register(13, ItemList.Casing_Dim_Injector);
        register(14, ItemList.Casing_Dim_Bridge);
        register(15, ItemList.Casing_Coil_Superconductor);

        for (int i = 0; i < 10; i++) {
            GTStructureChannels.TIER_MACHINE_CASING.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta % ACTIVE_OFFSET;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        int meta = aMeta % ACTIVE_OFFSET;
        if (meta >= 0) {
            switch (meta % ACTIVE_OFFSET) {
                case 10 -> {
                    return Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
                }
                case 11 -> {
                    return Textures.BlockIcons.MACHINE_HEATPROOFCASING.getIcon();
                }
                case 12 -> {
                    return Textures.BlockIcons.MACHINE_DIM_TRANS_CASING.getIcon();
                }
                case 13 -> {
                    return Textures.BlockIcons.MACHINE_DIM_INJECTOR.getIcon();
                }
                case 14 -> {
                    return Textures.BlockIcons.MACHINE_DIM_BRIDGE.getIcon();
                }
                case 15 -> {
                    return Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR.getIcon();
                }
            }
            if (ordinalSide == 0) {
                return Textures.BlockIcons.MACHINECASINGS_BOTTOM[meta].getIcon();
            }
            if (ordinalSide == 1) {
                return Textures.BlockIcons.MACHINECASINGS_TOP[meta].getIcon();
            }
            return Textures.BlockIcons.MACHINECASINGS_SIDE[meta].getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    @Override
    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        if (aWorld.getBlockMetadata(aX, aY, aZ) > 9) {
            return 0xFFFFFF;
        }

        return super.colorMultiplier(aWorld, aX, aY, aZ);
    }

    private final AtomicReferenceArray<ITexture[][]> textureCache = new AtomicReferenceArray<>(ACTIVE_OFFSET * 2);

    @Override
    public @Nullable ITexture[][] getTextures(int metadata) {
        final int cacheIndex = Math.floorMod(metadata, ACTIVE_OFFSET) + (metadata >= ACTIVE_OFFSET ? ACTIVE_OFFSET : 0);
        ITexture[][] cached = textureCache.get(cacheIndex);
        if (cached != null) return cached;

        List<ITexture> textures = new ArrayList<>();
        List<ITexture> topTextures = new ArrayList<>();
        List<ITexture> botTextures = new ArrayList<>();
        int meta = metadata % ACTIVE_OFFSET;
        IIconContainer texture = switch (meta) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> Textures.BlockIcons.MACHINECASINGS_SIDE[meta];
            case 10 -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS;
            case 11 -> Textures.BlockIcons.MACHINE_HEATPROOFCASING;
            case 12 -> Textures.BlockIcons.MACHINE_DIM_TRANS_CASING;
            case 13 -> Textures.BlockIcons.MACHINE_DIM_INJECTOR;
            case 14 -> Textures.BlockIcons.MACHINE_DIM_BRIDGE;
            case 15 -> Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR;
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL;
        };
        textures.add(TextureFactory.of(texture));

        IIconContainer topTexture = switch (meta) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> Textures.BlockIcons.MACHINECASINGS_TOP[meta];
            case 10 -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS;
            case 11 -> Textures.BlockIcons.MACHINE_HEATPROOFCASING;
            case 12 -> Textures.BlockIcons.MACHINE_DIM_TRANS_CASING;
            case 13 -> Textures.BlockIcons.MACHINE_DIM_INJECTOR;
            case 14 -> Textures.BlockIcons.MACHINE_DIM_BRIDGE;
            case 15 -> Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR;
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL;
        };
        topTextures.add(TextureFactory.of(topTexture));

        IIconContainer botTexture = switch (meta) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> Textures.BlockIcons.MACHINECASINGS_BOTTOM[meta];
            case 10 -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS;
            case 11 -> Textures.BlockIcons.MACHINE_HEATPROOFCASING;
            case 12 -> Textures.BlockIcons.MACHINE_DIM_TRANS_CASING;
            case 13 -> Textures.BlockIcons.MACHINE_DIM_INJECTOR;
            case 14 -> Textures.BlockIcons.MACHINE_DIM_BRIDGE;
            case 15 -> Textures.BlockIcons.MACHINE_COIL_SUPERCONDUCTOR;
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL;
        };
        botTextures.add(TextureFactory.of(botTexture));

        if (metadata >= ACTIVE_OFFSET) {
            if (metadata % ACTIVE_OFFSET == 14) {

                botTextures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.MACHINE_DIM_BRIDGE_CONVERGENCE)
                        .glow()
                        .build());

                topTextures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.MACHINE_DIM_BRIDGE_CONVERGENCE)
                        .glow()
                        .build());

                textures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.MACHINE_DIM_BRIDGE_CONVERGENCE)
                        .glow()
                        .build());

            }
        }

        ITexture[] standardLayers = textures.toArray(new ITexture[0]);
        ITexture[] topLayers = topTextures.toArray(new ITexture[0]);
        ITexture[] botLayers = botTextures.toArray(new ITexture[0]);
        cached = new ITexture[][] { botLayers, topLayers, standardLayers, standardLayers, standardLayers,
            standardLayers };
        if (textureCache.compareAndSet(cacheIndex, null, cached)) return cached;
        return textureCache.get(cacheIndex);
    }

    @Override
    public int getClientMeta(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if ((meta == 14) && GTCoilTracker.isCoilActive(world, x, y, z)) meta += ACTIVE_OFFSET;
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
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

}
