package gtnhintergalactic.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gtnhintergalactic.GTNHIntergalactic;
import gtnhintergalactic.block.BlockCasingDysonSwarm;
import gtnhintergalactic.block.BlockCasingGasSiphon;
import gtnhintergalactic.block.BlockCasingSpaceElevator;
import gtnhintergalactic.block.BlockCasingSpaceElevatorMotor;
import gtnhintergalactic.block.BlockSpaceElevatorCable;
import gtnhintergalactic.item.ItemBlockSpaceElevatorCable;
import gtnhintergalactic.item.ItemDysonSwarmParts;
import gtnhintergalactic.item.ItemMiningDrones;
import gtnhintergalactic.item.ItemSpaceElevatorParts;
import gtnhintergalactic.loader.MachineLoader;
import gtnhintergalactic.loader.RecipeLoader;
import gtnhintergalactic.recipe.IG_RecipeAdder;
import gtnhintergalactic.recipe.MachineRecipes;
import gtnhintergalactic.recipe.ResultNoSpaceProject;
import gtnhintergalactic.recipe.SpaceProjectRegistration;
import gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

/**
 * Proxy used by both, the server and the client to load stuff
 *
 * @author minecraft7771
 */
public class CommonProxy {

    public void init(FMLInitializationEvent event) {
        if (Textures.BlockIcons.casingTexturePages[32] == null) {
            Textures.BlockIcons.casingTexturePages[32] = new ITexture[128];
        }

        // Items
        registerItem(new ItemSpaceElevatorParts());
        registerItem(new ItemMiningDrones());
        registerItem(new ItemDysonSwarmParts());

        // Blocks
        if (Mods.GalacticraftCore.isModLoaded()) {
            GregTechAPI.sSpaceElevatorCable = new BlockSpaceElevatorCable();
        } else {
            GregTechAPI.sSpaceElevatorCable = new BlockFakeSECable();
        }
        GregTechAPI.sBlockCasingsSE = new BlockCasingSpaceElevator();
        GregTechAPI.sBlockCasingsSEMotor = new BlockCasingSpaceElevatorMotor();
        GregTechAPI.sBlockCasingsDyson = new BlockCasingDysonSwarm();
        GregTechAPI.sBlockCasingsSiphon = new BlockCasingGasSiphon();

        new MachineLoader().run();
        IG_RecipeAdder.init();
        if (Mods.GalacticraftCore.isModLoaded()) {
            GameRegistry.registerTileEntity(TileEntitySpaceElevatorCable.class, "Space Elevator Cable");
        }
        CheckRecipeResultRegistry.register(new ResultNoSpaceProject("", ""));
    }

    public void postInit(FMLPostInitializationEvent event) {
        new RecipeLoader().run();
        new SpaceProjectRegistration().run();
        new MachineRecipes().run();
        IG_RecipeAdder.postInit();
    }

    private static void registerItem(final Item item) {
        GameRegistry.registerItem(item, item.getUnlocalizedName());
    }

    // This is only used when GC isn't present.
    // GTNHIntergalactic effectively has a hard dep on GC, but it's easier to just no-op its integration than disable
    // the mod in the dev env somehow.
    private static class BlockFakeSECable extends Block {

        public BlockFakeSECable() {
            super(Material.iron);
            setBlockName("SpaceElevatorCable");
            setCreativeTab(GTNHIntergalactic.tab);
            setHarvestLevel("pickaxe", 2);

            GameRegistry.registerBlock(this, ItemBlockSpaceElevatorCable.class, "spaceelevatorcable");

            ItemList.SpaceElevatorCable.set(new ItemStack(this));
        }
    }
}
