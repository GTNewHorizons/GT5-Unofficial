package gregtech.api.modernmaterials;

import static gregtech.api.modernmaterials.blocks.registration.SimpleBlockRegistration.registerSimpleBlock;
import static gregtech.api.modernmaterials.blocks.registration.SpecialBlockRegistration.registerTESRBlock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialItemBlock;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.fluids.ModernMaterialFluid;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;
import gregtech.api.modernmaterials.items.partclasses.MaterialPart;
import gregtech.api.modernmaterials.items.partproperties.ModernMaterialItemRenderer;

public class ModernMaterialUtilities {

    public static void registerAllMaterialsItems() {

        for (ItemsEnum part : ItemsEnum.values()) {

            if (part.getAssociatedMaterials()
                .isEmpty()) return;

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

    public static ArrayList<String> tooltipGenerator(@NotNull ItemStack itemStack, EntityPlayer player,
        List<String> tooltipList, boolean F3_H) {
        // Todo, this is just temporary as a proof of concept/debug info.
        // Probably will put radioactive warning here. Not sure what else yet, if any.

        ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);
        ArrayList<String> tooltip = new ArrayList<>();

        if (material.getCustomTooltipGenerator() != null) {
            return material.getCustomTooltipGenerator()
                .apply(itemStack, player, tooltipList, F3_H);
        }

        // Default handling, no custom tooltip.
        Item part = itemStack.getItem();

        tooltip.add("Generic Tooltip");
        tooltip.add("Material Name: " + material.getMaterialName());
        tooltip.add("Texture Type: " + material.getTextureType());

        // Todo interface? There was some reason this was impractical before...
        if (part instanceof BaseMaterialItemBlock blockPart) {
            tooltip.add("Material Part Type: " + blockPart.getPart());
        } else if (part instanceof MaterialPart itemPart) {
            tooltip.add("Material Part Type: " + itemPart.getPart());
        }

        return tooltip;
    }

}
