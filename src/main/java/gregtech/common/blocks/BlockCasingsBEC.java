package gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockCasingsBEC extends BlockCasingsAbstract {

    public BlockCasingsBEC() {
        super(ItemCasings.class, "gt.blockcasings.bec", MaterialCasings.INSTANCE, 16);
        register(0, ItemList.SuperconductivePlasmaEnergyConduit, translatedText("gt.blockcasings.bec.0.tooltip"));
        register(1, ItemList.ElectromagneticallyIsolatedCasing, translatedText("gt.blockcasings.bec.1.tooltip"));
        register(2, ItemList.FineStructureConstantManipulator, translatedText("gt.blockcasings.bec.2.tooltip"));
        register(3, ItemList.ConflictInducementCasing, translatedText("gt.blockcasings.bec.3.tooltip"));
        register(4, ItemList.PeaceEnforcementCasing, translatedText("gt.blockcasings.bec.4.tooltip"));
        register(5, ItemList.CondensateTransformativeCoil, translatedText("gt.blockcasings.bec.5.tooltip"));
        register(6, ItemList.CondensateGuidanceCoil, translatedText("gt.blockcasings.bec.6.tooltip"));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (17 << 7) | aMeta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.BEC_CONDUIT.getIcon();
            case 1 -> Textures.BlockIcons.BEC_CASING.getIcon();
            case 2 -> Textures.BlockIcons.BEC_MANIPULATOR.getIcon();
            case 3 -> Textures.BlockIcons.BEC_CONFLICTCASING.getIcon();
            case 4 -> Textures.BlockIcons.BEC_PEACECASING.getIcon();
            case 5 -> Textures.BlockIcons.BEC_PRIMARYCOIL.getIcon();
            case 6 -> Textures.BlockIcons.BEC_SECONDARYCOIL.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
