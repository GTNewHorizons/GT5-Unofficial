package gtPlusPlus.core.proxy;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
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
import gtPlusPlus.core.lib.GTPPCore.ConfigSwitches;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.nei.GTPPNEIConfig;
import gtPlusPlus.xmod.gregtech.common.render.CapeRenderer;
import gtPlusPlus.xmod.gregtech.common.render.FlaskRenderer;
import gtPlusPlus.xmod.gregtech.common.render.MachineBlockRenderer;
import ic2.core.item.ItemFluidCell;

public class ClientProxy extends CommonProxy implements Runnable {

    private final CapeRenderer mCapeRenderer;

    @SideOnly(Side.CLIENT)
    public static boolean mFancyGraphics = false;

    public ClientProxy() {
        mCapeRenderer = new CapeRenderer();
        // Get Graphics Mode.
        mFancyGraphics = Minecraft.isFancyGraphicsEnabled();
    }

    @SubscribeEvent
    public void receiveRenderSpecialsEvent(net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre aEvent) {
        if (ConfigSwitches.enableCustomCapes) {
            mCapeRenderer.receiveRenderSpecialsEvent(aEvent);
        }
    }

    @SideOnly(Side.CLIENT)
    public static String playerName = "";

    @Override
    public void preInit(final FMLPreInitializationEvent e) {
        super.preInit(e);
        if (ConfigSwitches.enableCustomCapes) {
            onPreLoad();
        }
        // Do this weird things for textures.
        GTplusplus.loadTextures();
    }

    @Override
    public void init(final FMLInitializationEvent e) {

        /**
         * Custom Block Renderers
         */
        new CustomOreBlockRenderer();
        new CustomItemBlockRenderer();
        new MachineBlockRenderer();

        new FlaskRenderer();

        MinecraftForge.EVENT_BUS.register(new GTPPNEIConfig());

        super.init(e);
    }

    @Override
    public void postInit(final FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void registerRenderThings() {

        // Standard GT++

        /**
         * Entities
         */
        RenderingRegistry
            .registerEntityRenderingHandler(EntityPrimedMiningExplosive.class, new RenderMiningExplosivesPrimed());
        RenderingRegistry.registerEntityRenderingHandler(EntitySickBlaze.class, new RenderSickBlaze());
        RenderingRegistry
            .registerEntityRenderingHandler(EntityStaballoyConstruct.class, new RenderStaballoyConstruct());
        RenderingRegistry.registerEntityRenderingHandler(EntityToxinballSmall.class, new RenderToxinball(1F));
        RenderingRegistry.registerEntityRenderingHandler(EntityLightningAttack.class, new RenderFireball(1F));

        /**
         * Tiles
         */
        Logger.INFO("Registering Custom Renderer for the Lead Lined Chest.");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecayablesChest.class, new RenderDecayChest());
        Logger.INFO("Registering Custom Renderer for the Egg Box.");

        /**
         * Items
         */
        for (Pair<Item, IItemRenderer> sItemRenderMappings : mItemRenderMappings) {
            MinecraftForgeClient.registerItemRenderer(sItemRenderMappings.getKey(), sItemRenderMappings.getValue());
        }
    }

    @Override
    public int addArmor(final String armor) {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    @Override
    public void serverStarting(final FMLServerStartingEvent e) {}

    public void onPreLoad() {

    }

    @Override
    public void run() {

    }

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (GTPPCore.ConfigSwitches.hideUniversalCells) {
            hideUniversalCells();
        }
        super.onLoadComplete(event);
    }

    public void hideUniversalCells() {
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
