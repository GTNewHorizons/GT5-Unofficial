package kubatech.loaders.tea.components;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import kubatech.api.utils.ModUtils;
import kubatech.client.renderer.TeaItemsRenderer;

public class Tea {

    private static int META_COUNTER = 1;
    protected static final HashMap<Integer, String> metaToName = new HashMap<>();
    protected static final HashMap<String, Tea> teas = new HashMap<>();
    public static final TeaItemCup cupItem = new TeaItemCup();
    public static final TeaItemBucket bucketItem = new TeaItemBucket();
    private static final ItemStack EMPTY_CUP = new ItemStack(cupItem, 1, 0);
    private static final ItemStack EMPTY_BUCKET = new ItemStack(bucketItem, 1, 0);

    final String name;
    final TeaFluid fluid;
    final TeaFluidBlock block;
    ItemStack cup;
    ItemStack bucket;

    public static void init() {
        GameRegistry.registerItem(cupItem, "tea_cup");
        GameRegistry.registerItem(bucketItem, "tea_bucket");
        if (ModUtils.isClientSided) {
            MinecraftForgeClient.registerItemRenderer(cupItem, new TeaItemsRenderer());
            MinecraftForgeClient.registerItemRenderer(bucketItem, new TeaItemsRenderer());
        }
    }

    public static void finish() {
        for (Tea tea : teas.values()) {
            FluidContainerRegistry.registerFluidContainer(new FluidStack(tea.fluid, 220), tea.cup, EMPTY_CUP);
            FluidContainerRegistry.registerFluidContainer(new FluidStack(tea.fluid, 1000), tea.bucket, EMPTY_BUCKET);
        }
    }

    private Tea(String name) {
        this.name = name;
        metaToName.put(META_COUNTER, name);
        this.fluid = new TeaFluid(name);
        FluidRegistry.registerFluid(this.fluid);
        this.block = new TeaFluidBlock(this.fluid);
        GameRegistry.registerBlock(this.block, "tea_" + name);
        this.cup = cupItem.getItem(META_COUNTER);
        this.bucket = bucketItem.getItem(META_COUNTER);
        META_COUNTER++;
        teas.put(name, this);
    }

    public static Tea createTea(String name) {
        if (teas.containsKey(name)) {
            return teas.get(name);
        }
        return new Tea(name);
    }

    public Tea setCustomCup(ItemStack cup) {
        API.hideItem(this.cup);
        this.cup = cup;
        return this;
    }

}
