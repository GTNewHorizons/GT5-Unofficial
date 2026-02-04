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

        .remapItem("IC2:reactorLithiumCell", ItemList.RodLithium.getItem())
        .remapItem("gregtech:gt.glowstoneCell", ItemList.RodGlowstone.getItem())
        .remapItem("gregtech:gt.Thoriumcell", ItemList.RodThorium.getItem())
        .remapItem("gregtech:gt.Double_Thoriumcell", ItemList.RodThorium2.getItem())
        .remapItem("gregtech:gt.Quad_Thoriumcell", ItemList.RodThorium4.getItem())
        .remapItem("gregtech:gt.reactorUraniumSimple", ItemList.RodUranium.getItem())
        .remapItem("gregtech:gt.reactorUraniumDual", ItemList.RodUranium2.getItem())
        .remapItem("gregtech:gt.reactorUraniumQuad", ItemList.RodUranium4.getItem())
        .remapItem("gregtech:gt.reactorMOXSimple", ItemList.RodMOX.getItem())
        .remapItem("gregtech:gt.reactorMOXDual", ItemList.RodMOX2.getItem())
        .remapItem("gregtech:gt.reactorMOXQuad", ItemList.RodMOX4.getItem())
        .remapItem("GoodGenerator:rodCompressedUranium", ItemList.RodHighDensityUranium.getItem())
        .remapItem("GoodGenerator:rodCompressedUranium2", ItemList.RodHighDensityUranium2.getItem())
        .remapItem("GoodGenerator:rodCompressedUranium4", ItemList.RodHighDensityUranium4.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutonium", ItemList.RodHighDensityPlutonium.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutonium2", ItemList.RodHighDensityPlutonium2.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutonium4", ItemList.RodHighDensityPlutonium4.getItem())
        .remapItem("GoodGenerator:rodLiquidUranium", ItemList.RodExcitedUranium.getItem())
        .remapItem("GoodGenerator:rodLiquidUranium2", ItemList.RodExcitedUranium2.getItem())
        .remapItem("GoodGenerator:rodLiquidUranium4", ItemList.RodExcitedUranium4.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutonium", ItemList.RodExcitedPlutonium.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutonium2", ItemList.RodExcitedPlutonium2.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutonium4", ItemList.RodExcitedPlutonium4.getItem())
        .remapItem("gregtech:gt.Naquadahcell", ItemList.RodNaquadah.getItem())
        .remapItem("gregtech:gt.Double_Naquadahcell", ItemList.RodNaquadah2.getItem())
        .remapItem("gregtech:gt.Quad_Naquadahcell", ItemList.RodNaquadah4.getItem())
        .remapItem("bartworks:gt.Core_Reactor_Cell", ItemList.RodNaquadah32.getItem())
        .remapItem("gregtech:gt.MNqCell", ItemList.RodNaquadria.getItem())
        .remapItem("gregtech:gt.Double_MNqCell", ItemList.RodNaquadria2.getItem())
        .remapItem("gregtech:gt.Quad_MNqCell", ItemList.RodNaquadria4.getItem())
        .remapItem("bartworks:gt.Tiberiumcell", ItemList.RodTiberium.getItem())
        .remapItem("bartworks:gt.Double_Tiberiumcell", ItemList.RodTiberium2.getItem())
        .remapItem("bartworks:gt.Quad_Tiberiumcell", ItemList.RodTiberium4.getItem())

        .remapItem("IC2:itemTritiumCell", ItemList.DepletedRodLithium.getItem())
        .remapItem("gregtech:gt.sunnariumCell", ItemList.DepletedRodGlowstone.getItem())
        .remapItem("gregtech:gt.ThoriumcellDep", ItemList.DepletedRodThorium.getItem())
        .remapItem("gregtech:gt.Double_ThoriumcellDep", ItemList.DepletedRodThorium2.getItem())
        .remapItem("gregtech:gt.Quad_ThoriumcellDep", ItemList.DepletedRodThorium4.getItem())
        .remapItem("IC2:reactorUraniumSimpledepleted", ItemList.DepletedRodUranium.getItem())
        .remapItem("IC2:reactorUraniumDualdepleted", ItemList.DepletedRodUranium2.getItem())
        .remapItem("IC2:reactorUraniumQuaddepleted", ItemList.DepletedRodUranium4.getItem())
        .remapItem("IC2:reactorMOXSimpledepleted", ItemList.DepletedRodMOX.getItem())
        .remapItem("IC2:reactorMOXDualdepleted", ItemList.DepletedRodMOX2.getItem())
        .remapItem("IC2:reactorMOXQuaddepleted", ItemList.DepletedRodMOX4.getItem())
        .remapItem("GoodGenerator:rodCompressedUraniumDepleted", ItemList.DepletedRodHighDensityUranium.getItem())
        .remapItem("GoodGenerator:rodCompressedUraniumDepleted2", ItemList.DepletedRodHighDensityUranium2.getItem())
        .remapItem("GoodGenerator:rodCompressedUraniumDepleted4", ItemList.DepletedRodHighDensityUranium4.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutoniumDepleted", ItemList.DepletedRodHighDensityPlutonium.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutoniumDepleted2", ItemList.DepletedRodHighDensityPlutonium2.getItem())
        .remapItem("GoodGenerator:rodCompressedPlutoniumDepleted4", ItemList.DepletedRodHighDensityPlutonium4.getItem())
        .remapItem("GoodGenerator:rodLiquidUraniumDepleted", ItemList.DepletedRodExcitedUranium.getItem())
        .remapItem("GoodGenerator:rodLiquidUraniumDepleted2", ItemList.DepletedRodExcitedUranium2.getItem())
        .remapItem("GoodGenerator:rodLiquidUraniumDepleted4", ItemList.DepletedRodExcitedUranium4.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutoniumDepleted", ItemList.DepletedRodExcitedPlutonium.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutoniumDepleted2", ItemList.DepletedRodExcitedPlutonium2.getItem())
        .remapItem("GoodGenerator:rodLiquidPlutoniumDepleted4", ItemList.DepletedRodExcitedPlutonium4.getItem())
        .remapItem("gregtech:gt.NaquadahcellDep", ItemList.DepletedRodNaquadah.getItem())
        .remapItem("gregtech:gt.Double_NaquadahcellDep", ItemList.DepletedRodNaquadah2.getItem())
        .remapItem("gregtech:gt.Quad_NaquadahcellDep", ItemList.DepletedRodNaquadah4.getItem())
        .remapItem("bartworks:gt.Core_Reactor_CellDep", ItemList.DepletedRodNaquadah32.getItem())
        .remapItem("gregtech:gt.MNqCellDep", ItemList.DepletedRodNaquadria.getItem())
        .remapItem("gregtech:gt.Double_MNqCellDep", ItemList.DepletedRodNaquadria2.getItem())
        .remapItem("gregtech:gt.Quad_MNqCellDep", ItemList.DepletedRodNaquadria4.getItem())
        .remapItem("bartworks:gt.TiberiumcellDep", ItemList.DepletedRodTiberium.getItem())
        .remapItem("bartworks:gt.Double_TiberiumcellDep", ItemList.DepletedRodTiberium2.getItem())
        .remapItem("bartworks:gt.Quad_TiberiumcellDep", ItemList.DepletedRodTiberium4.getItem())

        // Ignores
        .ignore("kekztech:kekztech_tfftcasingblock_block")
        .ignore("kekztech:kekztech_tfftmultihatch_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock1_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock2_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock3_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock4_block")
        .ignore("kekztech:kekztech_tfftstoragefieldblock5_block")

        .ignore("miscutils:itemPlateMeatRaw")
        .ignore("miscutils:blockBlockMeatRaw")
        .ignore("miscutils:itemTerra")
        .ignore("miscutils:itemIgnis")
        .ignore("miscutils:itemAer")
        .ignore("miscutils:itemAqua")
        .ignore("miscutils:item.BioRecipeSelector")
        .ignore("miscutils:item.T3RecipeSelector")
        .ignore("miscutils:Hydroxide")
        .ignore("miscutils:itemCellHydroxide")
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
