package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public interface EssentiaInputHatch extends IMetaTileEntity, IAspectContainer, IEssentiaTransport {

    void updateTexture(int id);

    void updateCraftingIcon(ItemStack icon);

    int takeEssentia(Aspect aspect, int amount);
}
