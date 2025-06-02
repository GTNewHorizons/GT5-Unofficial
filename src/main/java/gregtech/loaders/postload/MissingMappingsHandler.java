package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import kubatech.loaders.BlockLoader;

public class MissingMappingsHandler {

    // spotless:off

    private static final Remapper REMAPPER = new Remapper()
        // Block remappings
        .remapBlock("dreamcraft:gt.blockcasingsNH", GregTechAPI.sBlockCasingsNH)
        .remapBlock("miscutils:oreCryolite", GameRegistry.findBlock(GTPlusPlus.ID, "oreCryoliteF"))
        .remapBlock("miscutils:oreFluorite", GameRegistry.findBlock(GTPlusPlus.ID, "oreFluoriteF"))
        .remapBlock("EMT:EMT_GTBLOCK_CASEING", BlockLoader.defcCasingBlock)
        .remapBlock("GalaxySpace:spaceelevatorparts", GregTechAPI.sBlockCasingsSE)
        .remapBlock("GalaxySpace:spaceelevatormotors", GregTechAPI.sBlockCasingsSEMotor)
        .remapBlock("GalaxySpace:spaceelevatorcable", GregTechAPI.sSpaceElevatorCable)
        .remapBlock("GalaxySpace:dysonswarmparts", GregTechAPI.sBlockCasingsDyson)
        .remapBlock("GalaxySpace:machineframes", GregTechAPI.sBlockCasingsSiphon)

        // Item remappings
        .remapItem("miscutils:Ammonium", GameRegistry.findItem(GTPlusPlus.ID, "itemCellAmmonium"))
        .remapItem("miscutils:Hydroxide", GameRegistry.findItem(GTPlusPlus.ID, "itemCellHydroxide"))
        .remapItem("miscutils:BerylliumHydroxide", GameRegistry.findItem(GTPlusPlus.ID, "itemCellBerylliumHydroxide"))
        .remapItem("miscutils:Bromine", GameRegistry.findItem(GTPlusPlus.ID, "itemCellBromine"))
        .remapItem("miscutils:Krypton", GameRegistry.findItem(GTPlusPlus.ID, "itemCellKrypton"))
        .remapItem("miscutils:itemCellZirconiumTetrafluoride", GameRegistry.findItem(GTPlusPlus.ID, "ZirconiumTetrafluoride"))
        .remapItem("miscutils:Li2BeF4", GameRegistry.findItem(GTPlusPlus.ID, "itemCellLithiumTetrafluoroberyllate"))

        .remapItem("miscutils:itemDustTinyCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustTinyCryoliteF"))
        .remapItem("miscutils:itemDustSmallCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustSmallCryoliteF"))
        .remapItem("miscutils:itemDustCryolite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustCryoliteF"))
        .remapItem("miscutils:dustPureCryolite", GameRegistry.findItem(GTPlusPlus.ID, "dustPureCryoliteF"))
        .remapItem("miscutils:dustImpureCryolite", GameRegistry.findItem(GTPlusPlus.ID, "dustImpureCryoliteF"))
        .remapItem("miscutils:crushedCryolite", GameRegistry.findItem(GTPlusPlus.ID, "crushedCryoliteF"))
        .remapItem("miscutils:crushedPurifiedCryolite", GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedCryoliteF"))
        .remapItem("miscutils:crushedCentrifugedCryolite", GameRegistry.findItem(GTPlusPlus.ID, "crushedCentrifugedCryoliteF"))

        .remapItem("miscutils:itemDustTinyFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustTinyFluoriteF"))
        .remapItem("miscutils:itemDustSmallFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustSmallFluoriteF"))
        .remapItem("miscutils:itemDustFluorite", GameRegistry.findItem(GTPlusPlus.ID, "itemDustFluoriteF"))
        .remapItem("miscutils:dustPureFluorite", GameRegistry.findItem(GTPlusPlus.ID, "dustPureFluoriteF"))
        .remapItem("miscutils:dustImpureFluorite", GameRegistry.findItem(GTPlusPlus.ID, "dustImpureFluoriteF"))
        .remapItem("miscutils:crushedFluorite", GameRegistry.findItem(GTPlusPlus.ID, "crushedFluoriteF"))
        .remapItem("miscutils:crushedPurifiedFluorite", GameRegistry.findItem(GTPlusPlus.ID, "crushedPurifiedFluoriteF"))
        .remapItem("miscutils:crushedCentrifugedFluorite", GameRegistry.findItem(GTPlusPlus.ID, "crushedCentrifugedFluoriteF"))

        .remapItem("GalaxySpace:item.SpaceElevatorParts", ItemList.NanotubeSpool.getItem())
        .remapItem("GalaxySpace:item.MiningDrone", ItemList.MiningDroneLV.getItem())
        .remapItem("GalaxySpace:item.DysonSwarmParts", ItemList.DysonSwarmModule.getItem())

        // Ignores
        .ignore("kekztech:kekztech_tfftcasingblock_block")
        .ignore("kekztech:kekztech_tfftmultihatch_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock1_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock2_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock3_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock4_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock5_block")

        .ignore("miscutils:itemPlateMeatRaw")
        .ignore("miscutils:AAA_Broken")
        .ignore("miscutils:item.empty");

    // spotless:on

    public static void handleMappings(List<FMLMissingMappingsEvent.MissingMapping> mappings) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : mappings) {
            if (REMAPPER.ignoreMappings.contains(mapping.name) || mapping.name.startsWith("Australia:")) {
                mapping.ignore();
                continue;
            }

            if (mapping.type == GameRegistry.Type.BLOCK) {
                Block block = REMAPPER.blockRemappings.get(mapping.name);
                if (block != null) {
                    mapping.remap(block);
                }
            } else if (mapping.type == GameRegistry.Type.ITEM) {
                Item item = REMAPPER.itemRemappings.get(mapping.name);
                if (item != null) {
                    mapping.remap(item);
                }
            }
        }
    }

    private static class Remapper {

        private final Map<String, Item> itemRemappings = new HashMap<>();
        private final Map<String, Block> blockRemappings = new HashMap<>();
        private final List<String> ignoreMappings = new ArrayList<>();

        public Remapper remapBlock(String oldName, Block newBlock) {
            blockRemappings.put(oldName, newBlock);
            return remapItem(oldName, Item.getItemFromBlock(newBlock));
        }

        public Remapper remapItem(String oldName, Item newItem) {
            itemRemappings.put(oldName, newItem);
            return this;
        }

        public Remapper ignore(String oldName) {
            ignoreMappings.add(oldName);
            return this;
        }
    }
}
