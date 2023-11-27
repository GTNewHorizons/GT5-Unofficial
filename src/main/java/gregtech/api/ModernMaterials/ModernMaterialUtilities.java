package gregtech.api.ModernMaterials;

import static gregtech.api.ModernMaterials.Blocks.Registration.SimpleBlockRegistration.registerSimpleBlock;
import static gregtech.api.ModernMaterials.Blocks.Registration.SpecialBlockRegistration.registerTESRBlock;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.Fluids.ModernMaterialFluid;
import gregtech.api.ModernMaterials.Items.PartProperties.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.Items.PartsClasses.MaterialPart;

public class ModernMaterialUtilities {

    public static void registerAllMaterialsItems() {

        for (ItemsEnum part : ItemsEnum.values()) {

            if (part.getAssociatedMaterials().isEmpty()) return;

            MaterialPart materialPart = new MaterialPart(part);
            materialPart.setUnlocalizedName(part.partName);

            // Registers the item with the game, only available in preInit.
            GameRegistry.registerItem(materialPart, part.partName);

            // Registers the renderer which allows for part colouring.
            MinecraftForgeClient.registerItemRenderer(materialPart, new ModernMaterialItemRenderer());

            part.setItem(materialPart);
        }

    }

    public static void registerAllMaterialsBlocks() {
        for (BlocksEnum blockType : BlocksEnum.values()) {
            registerSimpleBlock(blockType);
            registerTESRBlock(blockType);
        }
    }

    public static void registerAllMaterialsFluids() {

        // Register the fluids with forge.
        for (ModernMaterial material : ModernMaterial.getAllMaterials()) {
            for (ModernMaterialFluid fluid : material.getAssociatedFluids()) {
                FluidRegistry.registerFluid(fluid);
            }
        }
    }

    public static ArrayList<String> tooltipGenerator(Item part, ModernMaterial material) {
        // Todo, this is just temporary as a proof of concept/debug info.
        // Probably will put radioactive warning here. Not sure what else yet, if any.

        ArrayList<String> tooltip = new ArrayList<>();

        tooltip.add("Generic Tooltip");
        tooltip.add("Material Name: " + material.getMaterialName());

        if (part instanceof BaseMaterialItemBlock blockPart) {
            tooltip.add("Material Part Type: " + blockPart.getPart());
        } else if (part instanceof MaterialPart itemPart) {
            tooltip.add("Material Part Type: " + itemPart.getPart());
        }

        return tooltip;
    }

}
