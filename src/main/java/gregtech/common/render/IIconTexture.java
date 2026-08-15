package gregtech.common.render;

import javax.annotation.Nullable;

import net.minecraft.util.IIcon;

import gregtech.api.render.ISBRContext;

public interface IIconTexture {

    IIcon getIcon(int ordinalSide, @Nullable ISBRContext ctx);

}
