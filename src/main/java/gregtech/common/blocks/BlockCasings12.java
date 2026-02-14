package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

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
public class BlockCasings12 extends BlockCasingsAbstract {

    public BlockCasings12() {
        super(ItemCasings.class, "gt.blockcasings12", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.CokeOvenCasing, "Coke Oven Bricks");
        register(1, ItemList.Casing_Strengthened_Inanimate, "Strengthened Inanimate Machine Casing");
        register(2, ItemList.Casing_Precise_Stationary, "Precise Stationary Machine Casing");
        register(3, ItemList.Casing_Ultimately_Static, "Ultimately Static Machine Casing");
        register(9, ItemList.Spinmatron_Casing, "Vibration-Safe Casing");
        register(10, ItemList.CasingThaumium, "Alchemically Resistant Thaumium Casing");
        register(11, ItemList.CasingVoid, "Alchemically Inert Void Casing");
        register(12, ItemList.CasingIchorium, "Alchemically Immune Ichorium Casing");
        for (int i = 0; i < 3; i++) {
            GTStructureChannels.METAL_MACHINE_CASING.registerAsIndicator(new ItemStack(this, 1, i + 10), i + 1);
        }
    }

    @Override
    public String getHarvestTool(int aMeta) {
        // Coke Oven Bricks can be harvested with a pickaxe.
        if (aMeta == 0) return "pickaxe";
        return super.getHarvestTool(aMeta);
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        // Coke Oven Bricks have Harvest Level 0.
        if (aMeta == 0) return 0;
        return super.getHarvestLevel(aMeta);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.COKE_OVEN_CASING.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_STRENGTHENED_INANIMATE.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_PRECISE_STATIONARY.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_ULTIMATELY_STATIC.getIcon();
            case 9 -> Textures.BlockIcons.SPINMATRON_CASING.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_THAUMIUM.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VOID.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_ICHORIUM.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
