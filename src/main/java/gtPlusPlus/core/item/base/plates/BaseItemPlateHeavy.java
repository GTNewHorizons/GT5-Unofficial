package gtPlusPlus.core.item.base.plates;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;

public class BaseItemPlateHeavy extends BaseItemComponent {

    static final ComponentTypes HEAVY = ComponentTypes.PLATEHEAVY;

    public BaseItemPlateHeavy(final Material material) {
        super(material, HEAVY);
    }

    @Override
    public String getCorrectTextures() {
        return GTPlusPlus.ID + ":" + "itemHeavyPlate";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.base = i.registerIcon(GTPlusPlus.ID + ":" + "itemHeavyPlate");
        this.overlay = i.registerIcon(GTPlusPlus.ID + ":" + "itemHeavyPlate_Overlay");
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {

        if (pass == 0) {
            return this.base;
        } else {
            return this.overlay;
        }
    }
}
