package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasingsEmitter extends BlockCasingsAbstract {

    public BlockCasingsEmitter() {
        super(ItemCasings.class, "gt.blockcasingsemitter", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.EmitterCasingLV);
        register(1, ItemList.EmitterCasingMV);
        register(2, ItemList.EmitterCasingHV);
        register(3, ItemList.EmitterCasingEV);
        register(4, ItemList.EmitterCasingIV);
        register(5, ItemList.EmitterCasingLuV);
        register(6, ItemList.EmitterCasingZPM);
        register(7, ItemList.EmitterCasingUV);
        register(8, ItemList.EmitterCasingUHV);
        register(9, ItemList.EmitterCasingUEV);
        register(10, ItemList.EmitterCasingUIV);
        register(11, ItemList.EmitterCasingUMV);
        register(12, ItemList.EmitterCasingUXV);
        register(13, ItemList.EmitterCasingMAX);

        for (int i = 0; i < 14; i++) {
            GTStructureChannels.TIER_EMITTER_CASING.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);

        return switch (side) {
            case UP, DOWN -> getTopIcon(aMeta);
            case NORTH, SOUTH, EAST, WEST -> getSideIcon(aMeta);
            case UNKNOWN -> throw new IllegalStateException("cannot determine texture for emitter casing");
        };
    }

    private IIcon getSideIcon(int meta) {
        return switch (meta) {
            case 1 -> Textures.BlockIcons.EMITTER_CASING_SIDE_MV.getIcon();
            case 2 -> Textures.BlockIcons.EMITTER_CASING_SIDE_HV.getIcon();
            case 3 -> Textures.BlockIcons.EMITTER_CASING_SIDE_EV.getIcon();
            case 4 -> Textures.BlockIcons.EMITTER_CASING_SIDE_IV.getIcon();
            case 5 -> Textures.BlockIcons.EMITTER_CASING_SIDE_LuV.getIcon();
            case 6 -> Textures.BlockIcons.EMITTER_CASING_SIDE_ZPM.getIcon();
            case 7 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UV.getIcon();
            case 8 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UHV.getIcon();
            case 9 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UEV.getIcon();
            case 10 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UIV.getIcon();
            case 11 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UMV.getIcon();
            case 12 -> Textures.BlockIcons.EMITTER_CASING_SIDE_UXV.getIcon();
            case 13 -> Textures.BlockIcons.EMITTER_CASING_SIDE_MAX.getIcon();
            default -> Textures.BlockIcons.EMITTER_CASING_SIDE_LV.getIcon();
        };
    }

    private IIcon getTopIcon(int meta) {
        return switch (meta) {
            case 1 -> Textures.BlockIcons.EMITTER_CASING_TOP_MV.getIcon();
            case 2 -> Textures.BlockIcons.EMITTER_CASING_TOP_HV.getIcon();
            case 3 -> Textures.BlockIcons.EMITTER_CASING_TOP_EV.getIcon();
            case 4 -> Textures.BlockIcons.EMITTER_CASING_TOP_IV.getIcon();
            case 5 -> Textures.BlockIcons.EMITTER_CASING_TOP_LuV.getIcon();
            case 6 -> Textures.BlockIcons.EMITTER_CASING_TOP_ZPM.getIcon();
            case 7 -> Textures.BlockIcons.EMITTER_CASING_TOP_UV.getIcon();
            case 8 -> Textures.BlockIcons.EMITTER_CASING_TOP_UHV.getIcon();
            case 9 -> Textures.BlockIcons.EMITTER_CASING_TOP_UEV.getIcon();
            case 10 -> Textures.BlockIcons.EMITTER_CASING_TOP_UIV.getIcon();
            case 11 -> Textures.BlockIcons.EMITTER_CASING_TOP_UMV.getIcon();
            case 12 -> Textures.BlockIcons.EMITTER_CASING_TOP_UXV.getIcon();
            case 13 -> Textures.BlockIcons.EMITTER_CASING_TOP_MAX.getIcon();
            default -> Textures.BlockIcons.EMITTER_CASING_TOP_LV.getIcon();
        };
    }
}
