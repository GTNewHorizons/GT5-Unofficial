package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasingsEmitter extends BlockCasingsAbstract {

    /**
     * Texture Index Information Textures.BlockIcons.casingTexturePages[0][0-63] - Gregtech
     * Textures.BlockIcons.casingTexturePages[0][64-127] - GT++ Textures.BlockIcons.casingTexturePages[1][0-127] -
     * Gregtech Textures.BlockIcons.casingTexturePages[2][0-127] - Free Textures.BlockIcons.casingTexturePages[3][0-127]
     * - Free Textures.BlockIcons.casingTexturePages[4][0-127] - Free Textures.BlockIcons.casingTexturePages[5][0-127] -
     * Free Textures.BlockIcons.casingTexturePages[6][0-127] - Free Textures.BlockIcons.casingTexturePages[7][0-127] -
     * TecTech Textures.BlockIcons.casingTexturePages[8][0-127] - TecTech
     */
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

        for (int i = 0; i < 13; i++) {
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
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 2 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 3 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 4 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 5 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 6 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 7 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 8 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 9 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 10 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 11 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            case 12 -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
            default -> Textures.BlockIcons.COLLIDER_CASING.getIcon();
        };
    }

}
