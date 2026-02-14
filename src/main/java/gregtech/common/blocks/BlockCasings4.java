package gregtech.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IBlockWithActiveOffset;
import gregtech.api.interfaces.IBlockWithClientMeta;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRenderingWorld;
import gregtech.common.data.GTCoilTracker;
import gregtech.common.render.GTRendererBlock;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings4 extends BlockCasingsAbstract
    implements IBlockWithActiveOffset, IBlockWithClientMeta, IBlockWithTextures {

    /**
     * This mapping is used to look up which texture should be used to render the connected texture for fusion casings.
     * <p>
     * This mapping is computed from that giant if ladder from #getIcon in commit
     * da3421547afadc49938b5b6a7f9a9679afa1d570 The exact meaning of these numbers are like black magic. Read the
     * original getIcon implementation to understand why it is 0, 1, etc, if that if ladder is even intelligible.
     */
    private static final int[][] mapping = new int[][] {
        { 7, 7, 7, 7, 0, 7, 0, 7, 1, 7, 1, 7, 8, 7, 8, 7, 0, 7, 0, 7, 0, 7, 0, 7, 9, 7, 9, 7, 3, 7, 3, 7, 1, 7, 1, 7,
            11, 7, 11, 7, 1, 7, 1, 7, 2, 7, 2, 7, 10, 7, 10, 7, 5, 7, 5, 7, 4, 7, 4, 7, 6, 7, 6, 7 },
        { 7, 7, 7, 7, 0, 0, 7, 7, 1, 1, 7, 7, 8, 8, 7, 7, 0, 0, 7, 7, 0, 0, 7, 7, 9, 9, 7, 7, 3, 3, 7, 7, 1, 1, 7, 7,
            11, 11, 7, 7, 1, 1, 7, 7, 2, 2, 7, 7, 10, 10, 7, 7, 5, 5, 7, 7, 4, 4, 7, 7, 6, 6, 7, 7 },
        { 7, 1, 1, 1, 0, 9, 10, 4, 7, 1, 1, 1, 0, 9, 10, 4, 0, 8, 11, 2, 0, 3, 5, 6, 0, 8, 11, 2, 0, 3, 5, 6, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
        { 7, 1, 1, 1, 0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9, 10, 4, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 1, 1, 1,
            0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9, 10, 4, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7 },
        { 7, 1, 1, 1, 7, 1, 1, 1, 0, 8, 11, 2, 0, 8, 11, 2, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0, 9, 10, 4,
            0, 9, 10, 4, 0, 3, 5, 6, 0, 3, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 },
        { 7, 1, 1, 1, 7, 7, 7, 7, 0, 9, 10, 4, 7, 7, 7, 7, 7, 1, 1, 1, 7, 7, 7, 7, 0, 9, 10, 4, 7, 7, 7, 7, 0, 8, 11, 2,
            7, 7, 7, 7, 0, 3, 5, 6, 7, 7, 7, 7, 0, 8, 11, 2, 7, 7, 7, 7, 0, 3, 5, 6, 7, 7, 7, 7 }, };

    public static boolean mConnectedMachineTextures = true;

    public BlockCasings4() {
        super(ItemCasings.class, "gt.blockcasings4", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_RobustTungstenSteel, "Robust Tungstensteel Machine Casing");
        register(1, ItemList.Casing_CleanStainlessSteel, "Clean Stainless Steel Machine Casing");
        register(2, ItemList.Casing_StableTitanium, "Stable Titanium Machine Casing");
        register(3, ItemList.Casing_Firebox_Titanium, "Titanium Firebox Casing");
        register(6, ItemList.Casing_Fusion, "Fusion Machine Casing");
        register(7, ItemList.Casing_Fusion_Coil, "Fusion Coil Block");
        register(8, ItemList.Casing_Fusion2, "Fusion Machine Casing MK II");
        register(9, ItemList.Casing_Turbine, "Turbine Casing");
        register(10, ItemList.Casing_Turbine1, "Stainless Steel Turbine Casing");
        register(11, ItemList.Casing_Turbine2, "Titanium Turbine Casing");
        register(12, ItemList.Casing_Turbine3, "Tungstensteel Turbine Casing");
        register(13, ItemList.Casing_EngineIntake, "Engine Intake Casing");
        register(14, ItemList.Casing_MiningOsmiridium, "Mining Osmiridium Casing");
        register(15, ItemList.Casing_Firebricks, "Firebricks");

        GTMod.proxy.mCTMBlockCache.put(this, (byte) 6, true);
        GTMod.proxy.mCTMBlockCache.put(this, (byte) 8, true);
    }

    @Override
    public String getHarvestTool(int meta) {
        if (meta == 15) return HarvestTool.PickaxeLevel2.getHarvestTool();
        return super.getHarvestTool(meta);
    }

    @Override
    public int getHarvestLevel(int meta) {
        if (meta == 15) return HarvestTool.PickaxeLevel2.getHarvestLevel();
        return super.getHarvestLevel(meta);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta + 48;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 3 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 4 ->
                // Do not overwrite!
                Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW.getIcon();
            case 5 ->
                // Do not overwrite!
                Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_FUSION_COIL.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STEEL.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STAINLESSSTEEL.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TITANIUM.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TUNGSTENSTEEL.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_ENGINE_INTAKE.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_MINING_OSMIRIDIUM.getIcon();
            case 15 -> Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        aWorld = GTRenderingWorld.getInstance(aWorld);
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord) % ACTIVE_OFFSET;
        if (tMeta != 6 && tMeta != 8 || !mConnectedMachineTextures) {
            return getIcon(ordinalSide, tMeta);
        }
        int tStartIndex = tMeta == 6 ? 1 : 13;
        int tIndexIntoMapping = 0;
        if (isSameBlock(aWorld, xCoord, yCoord - 1, zCoord, tMeta)) tIndexIntoMapping |= 1;
        if (isSameBlock(aWorld, xCoord, yCoord + 1, zCoord, tMeta)) tIndexIntoMapping |= 1 << 1;
        if (isSameBlock(aWorld, xCoord + 1, yCoord, zCoord, tMeta)) tIndexIntoMapping |= 1 << 2;
        if (isSameBlock(aWorld, xCoord, yCoord, zCoord + 1, tMeta)) tIndexIntoMapping |= 1 << 3;
        if (isSameBlock(aWorld, xCoord - 1, yCoord, zCoord, tMeta)) tIndexIntoMapping |= 1 << 4;
        if (isSameBlock(aWorld, xCoord, yCoord, zCoord - 1, tMeta)) tIndexIntoMapping |= 1 << 5;
        return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + mapping[ordinalSide][tIndexIntoMapping]].getIcon();
    }

    private boolean isSameBlock(IBlockAccess aWorld, int aX, int aY, int aZ, int aMeta) {
        return aWorld.getBlock(aX, aY, aZ) == this && aWorld.getBlockMetadata(aX, aY, aZ) == aMeta;
    }

    @Override
    public int getClientMeta(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 7 && GTCoilTracker.isCoilActive(world, x, y, z)) meta += ACTIVE_OFFSET;
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
    public @Nullable ITexture[][] getTextures(int metadata) {
        List<ITexture> textures = new ArrayList<>();
        List<ITexture> topFaceTextures = new ArrayList<>();
        IIconContainer texture = switch (metadata % ACTIVE_OFFSET) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL;
            case 1 -> Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL;
            case 2, 3 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM;
            case 4 -> Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW;
            case 5 -> Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;
            case 6 -> Textures.BlockIcons.MACHINE_CASING_FUSION;
            case 7 -> Textures.BlockIcons.MACHINE_CASING_FUSION_COIL;
            case 8 -> Textures.BlockIcons.MACHINE_CASING_FUSION_2;
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STEEL;
            case 10 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STAINLESSSTEEL;
            case 11 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TITANIUM;
            case 12 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TUNGSTENSTEEL;
            case 13 -> Textures.BlockIcons.MACHINE_CASING_ENGINE_INTAKE;
            case 14 -> Textures.BlockIcons.MACHINE_CASING_MINING_OSMIRIDIUM;
            case 15 -> Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS;
            default -> Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL_BACKGROUND;
        };
        textures.add(TextureFactory.of(texture));
        IIconContainer topTexture = (metadata % ACTIVE_OFFSET == 3)
            ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM
            : texture;

        if (metadata >= ACTIVE_OFFSET) {
            if (metadata % ACTIVE_OFFSET == 7) {
                textures.add(
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.EXOFOUNDRY_CENTRAL_CASING_ACTIVE)
                        .glow()
                        .build());
            }
        }
        topFaceTextures.add(TextureFactory.of(topTexture));
        ITexture[] standardLayers = textures.toArray(new ITexture[0]);
        ITexture[] topLayers = topFaceTextures.toArray(new ITexture[0]);
        return new ITexture[][] { topLayers, topLayers, standardLayers, standardLayers, standardLayers,
            standardLayers };
    }
}
