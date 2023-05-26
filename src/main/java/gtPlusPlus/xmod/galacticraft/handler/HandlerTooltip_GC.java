package gtPlusPlus.xmod.galacticraft.handler;

import static gregtech.api.enums.Mods.GalacticraftCore;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.asm.AsmConfig;

public class HandlerTooltip_GC {

    private static Item mItemBlock;
    private static Block mBlock;
    private static Class<?> oMainClass;
    private static Class<?> oFuelLoaderClass;
    private static HashMap<Integer, String> mFuelNames;

    static {
        mFuelNames = new LinkedHashMap<Integer, String>();
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (GalacticraftCore.isModLoaded() && AsmConfig.enableGcFuelChanges) {

            if (mBlock == null) {
                try {
                    Class<?> GCBlocks = ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.blocks.GCBlocks");
                    if (GCBlocks != null) {
                        oMainClass = GCBlocks;

                        Class<?> GCFuelLoader = ReflectionUtils
                                .getClass("micdoodle8.mods.galacticraft.core.blocks.BlockFuelLoader");

                        if (GCFuelLoader != null) {
                            oFuelLoaderClass = GCFuelLoader;
                        }

                        Field aField = ReflectionUtils.getField(oMainClass, "fuelLoader");
                        if (aField != null) {
                            Block aBlock = (Block) aField.get(null);
                            if (aBlock != null) {
                                mBlock = aBlock;
                                mItemBlock = Item.getItemFromBlock(mBlock);
                            }
                        }
                    }
                } catch (Throwable t) {}
            }
            if (mFuelNames == null) {
                mFuelNames = new LinkedHashMap<Integer, String>();
            }

            if (mFuelNames.isEmpty()) {
                for (int aMapKey : RocketFuels.mValidRocketFuels.keySet()) {
                    Fluid aFuel = RocketFuels.mValidRocketFuels.get(aMapKey);
                    if (aFuel != null) {
                        // noinspection deprecation
                        mFuelNames.put(aMapKey, aFuel.getLocalizedName());
                    }
                }
            }
            if (mItemBlock != null && !mFuelNames.isEmpty()) {
                Item aTempItem = event.itemStack.getItem();
                Block aTempBlock = Block.getBlockFromItem(aTempItem);
                if (aTempItem == mItemBlock || oFuelLoaderClass.isInstance(aTempBlock)
                        || event.itemStack.getUnlocalizedName().toLowerCase().contains("fuelloader")) {
                    for (int aMapKey : mFuelNames.keySet()) {
                        String aFuel = mFuelNames.get(aMapKey);
                        if (aFuel != null) {
                            event.toolTip.add("Tier " + (aMapKey + 1) + ": " + aFuel);
                        }
                    }
                }
            }
        }
    }
}
