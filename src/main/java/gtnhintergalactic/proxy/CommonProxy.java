package gtnhintergalactic.proxy;

import net.minecraft.item.Item;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gtnhintergalactic.block.BlockCasingDysonSwarm;
import gtnhintergalactic.block.BlockCasingGasSiphon;
import gtnhintergalactic.block.BlockCasingSpaceElevator;
import gtnhintergalactic.block.BlockCasingSpaceElevatorMotor;
import gtnhintergalactic.block.BlockSpaceElevatorCable;
import gtnhintergalactic.item.ItemBlockSpaceElevatorCable;
import gtnhintergalactic.item.ItemCasingDysonSwarm;
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
        GregTechAPI.sSpaceElevatorCable = new BlockSpaceElevatorCable();
        GameRegistry
            .registerBlock(GregTechAPI.sSpaceElevatorCable, ItemBlockSpaceElevatorCable.class, "spaceelevatorcable");

        GregTechAPI.sBlockCasingsSE = new BlockCasingSpaceElevator();
        GregTechAPI.sBlockCasingsSEMotor = new BlockCasingSpaceElevatorMotor();

        GregTechAPI.sBlockCasingsDyson = new BlockCasingDysonSwarm();
        GameRegistry.registerBlock(GregTechAPI.sBlockCasingsDyson, ItemCasingDysonSwarm.class, "dysonswarmparts");

        GregTechAPI.sBlockCasingsSiphon = new BlockCasingGasSiphon();
        GameRegistry.registerBlock(GregTechAPI.sBlockCasingsSiphon, "gassiphoncasing");

        new MachineLoader().run();
        IG_RecipeAdder.init();
        GameRegistry.registerTileEntity(TileEntitySpaceElevatorCable.class, "Space Elevator Cable");
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
}
