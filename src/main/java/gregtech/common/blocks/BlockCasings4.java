package gregtech.common.blocks;

import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IBlockWithActiveOffset;
import gregtech.api.interfaces.IBlockWithClientMeta;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ActiveSideOverlayTextures;
import gregtech.common.data.GTActiveBlockTracker;
import gregtech.common.render.GTRendererBlock;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings4 extends BlockCasingsAbstract
    implements IBlockWithActiveOffset, IBlockWithClientMeta, IBlockWithTextures {

    private final ActiveSideOverlayTextures textures = new ActiveSideOverlayTextures(
        this,
        metadata -> metadata == 3,
        Textures.BlockIcons.MACHINE_CASING_FIREBOX_BLOOM);

    public BlockCasings4() {
        super(ItemCasings.class, "gt.blockcasings4", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.Casing_RobustTungstenSteel);
        register(1, ItemList.Casing_CleanStainlessSteel);
        register(2, ItemList.Casing_StableTitanium);
        register(3, ItemList.Casing_Firebox_Titanium);
        register(6, ItemList.Casing_Fusion);
        register(7, ItemList.Casing_Fusion_Coil);
        register(8, ItemList.Casing_Fusion2);
        register(9, ItemList.Casing_Turbine);
        register(10, ItemList.Casing_Turbine1);
        register(11, ItemList.Casing_Turbine2);
        register(12, ItemList.Casing_Turbine3);
        register(13, ItemList.Casing_EngineIntake);
        register(14, ItemList.Casing_MiningOsmiridium);
        register(15, ItemList.Casing_Firebricks);
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
        return Math.floorMod(aMeta, ACTIVE_OFFSET) + 48;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (Math.floorMod(aMeta, ACTIVE_OFFSET)) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 3 -> {
                if (ordinalSide == 0 || ordinalSide == 1)
                    yield Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM_TOP.getIcon();
                yield Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM.getIcon();
            }
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
    public int getClientMeta(World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (textures.hasOverlay(metadata) && GTActiveBlockTracker.isBlockActive(world, x, y, z))
            metadata += ACTIVE_OFFSET;
        return metadata;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return super.getDamageValue(world, x, y, z) % ACTIVE_OFFSET;
    }

    @Override
    public int damageDropped(int metadata) {
        return super.damageDropped(metadata) % ACTIVE_OFFSET;
    }

    @Override
    public ITexture[][] getTextures(int metadata) {
        return textures.getTextures(metadata);
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
