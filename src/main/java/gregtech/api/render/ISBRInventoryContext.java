package gregtech.api.render;

import net.minecraftforge.common.util.ForgeDirection;

public interface ISBRInventoryContext extends ISBRContext {

    @Override
    ISBRInventoryContext reset();

    @Override
    ISBRInventoryContext setupColor(ForgeDirection side, int hexColor);

    int getMeta();
}
