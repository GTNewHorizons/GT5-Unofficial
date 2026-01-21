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
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import gregtech.common.powergoggles.gui.PowerGogglesGuiOverlay;
import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;
import gregtech.common.powergoggles.handlers.PowerGogglesKeybindHandler;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.client.renderer.CustomItemBlockRenderer;
import gtPlusPlus.core.client.renderer.RenderDecayChest;
import gtPlusPlus.core.client.renderer.RenderMiningExplosivesPrimed;
import gtPlusPlus.core.client.renderer.RenderSickBlaze;
import gtPlusPlus.core.client.renderer.RenderStaballoyConstruct;
import gtPlusPlus.core.client.renderer.RenderToxinball;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.Configuration;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.xmod.gregtech.common.render.FlaskRenderer;
import gtPlusPlus.xmod.gregtech.common.render.MachineBlockRenderer;
import ic2.core.item.ItemFluidCell;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(final FMLPreInitializationEvent e) {
        super.preInit(e);
        // Do this weird things for textures.
        GTplusplus.loadTextures();
        PowerGogglesGuiOverlay.init();
    }

    @Override
    public void init(final FMLInitializationEvent e) {
        new CustomItemBlockRenderer();
        RenderingRegistry.registerBlockHandler(new MachineBlockRenderer());
        new FlaskRenderer();
        MinecraftForge.EVENT_BUS.register(PowerGogglesHudHandler.getInstance());
        PowerGogglesKeybindHandler.init();
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecayablesChest.class, new RenderDecayChest());
    }

    @Override
    public int addArmor(final String armor) {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (Configuration.features.hideUniversalCells) {
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
