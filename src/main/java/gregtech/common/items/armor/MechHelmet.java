package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.IIconRegister;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MechHelmet extends MechArmorBase {

    public MechHelmet() {
        super(SLOT_HELMET);
        type = "helmet";
    }
}
