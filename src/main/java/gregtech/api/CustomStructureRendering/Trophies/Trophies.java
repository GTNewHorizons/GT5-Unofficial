package gregtech.api.CustomStructureRendering.Trophies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.CustomStructureRendering.Structures.BaseModelStructure;
import gregtech.api.CustomStructureRendering.Structures.Model_DTPF;
import gregtech.api.CustomStructureRendering.Structures.Model_NanoForge;
import gregtech.api.CustomStructureRendering.Structures.Model_Default;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.HashMap;
import java.util.Set;


public abstract class Trophies {

    public static final Block TrophyBlock = new BaseTrophyBlock("% Trophy");
    public static Item TrophyItem;


    // These two registrations happen at different stages of the minecraft loading process,
    // hence why we have different methods for them.
    public static void registerBlock() {
        GameRegistry.registerBlock(TrophyBlock, BaseTrophyItemBlock.class, "% Trophy");
    }

    public static void registerRenderer() {
        GameRegistry.registerTileEntity(
                BaseTrophyTileEntity.class,
                "GregTech:ModelTrophy");

        TrophyItem = Item.getItemFromBlock(TrophyBlock);

        MinecraftForgeClient.registerItemRenderer(TrophyItem, new BaseTrophyItemRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(BaseTrophyTileEntity.class, new BaseTrophyTESR());

        registerAll();
    }


    private static final HashMap<String, BaseModelStructure> modelMap = new HashMap<>();

    public static void registerModel(final String label, final BaseModelStructure model) {
        modelMap.put(label, model);
    }

    public static Set<String> getModelList() {
        return modelMap.keySet();
    }

    public static BaseModelStructure getModel(final String modelName) {
        BaseModelStructure model = modelMap.getOrDefault(modelName, null);

        if (model == null) return modelMap.get("Default");

        return model;
    }

    private static void registerAll() {
        registerModel("Default", new Model_Default());
        registerModel("DTPF", new Model_DTPF());
        registerModel("Nano Forge", new Model_NanoForge());
    }
}
