package gregtech.api.render;

import net.minecraftforge.common.util.ForgeDirection;

public interface ISBRInventoryContext extends ISBRContext {

    ISBRInventoryContext reset();

    ISBRInventoryContext setupColor(ForgeDirection side, int hexColor);

    int getMeta();
}
