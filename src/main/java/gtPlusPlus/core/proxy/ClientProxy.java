package gtPlusPlus.core.proxy;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.client.renderer.CustomItemBlockRenderer;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.client.renderer.RenderDecayChest;
import gtPlusPlus.core.client.renderer.RenderMiningExplosivesPrimed;
import gtPlusPlus.core.client.renderer.RenderSickBlaze;
import gtPlusPlus.core.client.renderer.RenderStaballoyConstruct;
import gtPlusPlus.core.client.renderer.RenderToxinball;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.nei.NEIGTPPConfig;
import gtPlusPlus.xmod.gregtech.common.render.FlaskRenderer;
import gtPlusPlus.xmod.gregtech.common.render.MachineBlockRenderer;
import ic2.core.item.ItemFluidCell;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(final FMLPreInitializationEvent e) {
        super.preInit(e);
        // Do this weird things for textures.
        GTplusplus.loadTextures();
    }

    @Override
    public void init(final FMLInitializationEvent e) {
        new CustomOreBlockRenderer();
        new CustomItemBlockRenderer();
        new MachineBlockRenderer();
        new FlaskRenderer();
        MinecraftForge.EVENT_BUS.register(new NEIGTPPConfig());
        super.init(e);
    }

    @Override
    public void postInit(final FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void registerRenderThings() {
        // Entities
        RenderingRegistry
            .registerEntityRenderingHandler(EntityPrimedMiningExplosive.class, new RenderMiningExplosivesPrimed());
        RenderingRegistry.registerEntityRenderingHandler(EntitySickBlaze.class, new RenderSickBlaze());
        RenderingRegistry
            .registerEntityRenderingHandler(EntityStaballoyConstruct.class, new RenderStaballoyConstruct());
        RenderingRegistry.registerEntityRenderingHandler(EntityToxinballSmall.class, new RenderToxinball(1F));
        RenderingRegistry.registerEntityRenderingHandler(EntityLightningAttack.class, new RenderFireball(1F));
        // Tiles
        Logger.INFO("Registering Custom Renderer for the Lead Lined Chest.");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecayablesChest.class, new RenderDecayChest());
    }

    @Override
    public int addArmor(final String armor) {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    @Override
    public void serverStarting(final FMLServerStartingEvent e) {}

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (GTPPCore.ConfigSwitches.hideUniversalCells) {
            hideUniversalCells();
        }
        super.onLoadComplete(event);
    }

    private void hideUniversalCells() {
        ArrayList<ItemStack> itemList = new ArrayList<>();
        for (Fluid fluid : FluidRegistry.getRegisteredFluids()
            .values()) {
            if (fluid == null) {
                continue;
            }
            itemList.add(ItemFluidCell.getUniversalFluidCell(new FluidStack(fluid, 2147483647)));
        }
        for (ItemStack aCell : itemList) {
            codechicken.nei.api.API.hideItem(aCell);
        }
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance()
            .getClient().theWorld;
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }
}
