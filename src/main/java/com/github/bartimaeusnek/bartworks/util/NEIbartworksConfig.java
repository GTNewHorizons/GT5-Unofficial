package com.github.bartimaeusnek.bartworks.util;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIbartworksConfig implements IConfigureNEI {

    @Optional.Method(modid="NotEnoughItems")
    @Override
    public String getName() {
        return MainMod.name;
    }

    @Optional.Method(modid="NotEnoughItems")
    @Override
    public String getVersion() {
        return MainMod.version;
    }

    @Optional.Method(modid="NotEnoughItems")
    @Override
    public void loadConfig() {
        API.hideItem(new ItemStack(ItemRegistry.tab));
    }
}