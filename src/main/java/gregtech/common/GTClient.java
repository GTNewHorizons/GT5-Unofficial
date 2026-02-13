// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: GT_Client.java

package gregtech.common;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GregTech;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.input.Keyboard;

import com.glodblock.github.nei.recipes.FluidRecipe;
import com.glodblock.github.nei.recipes.extractor.GregTech5RecipeExtractor;
import com.gtnewhorizons.navigator.api.NavigatorApi;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.hazards.HazardProtectionTooltip;
import gregtech.api.interfaces.IBlockOnWalkOver;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.net.GTPacketClientPreference;
import gregtech.api.net.cape.GTPacketSetCape;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.render.RenderOverlay;
import gregtech.api.util.ColorsMetadataSection;
import gregtech.api.util.ColorsMetadataSectionSerializer;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTMusicSystem;
import gregtech.api.util.GTPlayedSound;
import gregtech.api.util.GTUtility;
import gregtech.client.BlockOverlayRenderer;
import gregtech.client.GTMouseEventHandler;
import gregtech.client.GTPowerfailRenderer;
import gregtech.client.SeekingOggCodec;
import gregtech.client.renderer.entity.RenderPowderBarrel;
import gregtech.client.renderer.waila.TTRenderGTProgressBar;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.config.Client;
import gregtech.common.entity.EntityPowderBarrelPrimed;
import gregtech.common.misc.GTCapeCommand;
import gregtech.common.misc.GTPowerfailCommandClient;
import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionRenderer;
import gregtech.common.powergoggles.PowerGogglesCommand;
import gregtech.common.render.BaseMetaTileEntityRenderer;
import gregtech.common.render.BlackholeRenderer;
import gregtech.common.render.DroneRender;
import gregtech.common.render.FlaskRenderer;
import gregtech.common.render.FluidDisplayStackRenderer;
import gregtech.common.render.GTRendererBlock;
import gregtech.common.render.GTRendererCasing;
import gregtech.common.render.LaserRenderer;
import gregtech.common.render.MetaGeneratedToolRenderer;
import gregtech.common.render.NanoForgeRenderer;
import gregtech.common.render.WormholeRenderer;
import gregtech.common.render.items.DataStickRenderer;
import gregtech.common.render.items.InfiniteSprayCanRenderer;
import gregtech.common.render.items.MetaGeneratedItemRenderer;
import gregtech.common.tileentities.debug.MTEAdvDebugStructureWriter;
import gregtech.common.tileentities.render.RenderingTileEntityBlackhole;
import gregtech.common.tileentities.render.RenderingTileEntityDrone;
import gregtech.common.tileentities.render.RenderingTileEntityLaser;
import gregtech.common.tileentities.render.RenderingTileEntityNanoForge;
import gregtech.common.tileentities.render.RenderingTileEntityWormhole;
import gregtech.crossmod.navigator.PowerfailLayerManager;
import gregtech.loaders.ExtraIcons;
import gregtech.loaders.misc.GTBees;
import gregtech.loaders.preload.GTPreLoad;
import gregtech.nei.NEIGTConfig;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;

public class GTClient extends GTProxy {

    public final PollutionRenderer mPollutionRenderer = new PollutionRenderer();
    public GTPowerfailRenderer powerfailRenderer;
    public KeyBinding shakeLockKey;
    public final boolean fixedBottomFaceUV;
    private final List<Materials> mPosR;
    private final List<Materials> mPosG;
    private final List<Materials> mPosB;
    private final List<Materials> mPosA = Collections.emptyList();
    private final List<Materials> mNegR;
    private final List<Materials> mNegG;
    private final List<Materials> mNegB;
    private final List<Materials> mNegA = Collections.emptyList();
    private final List<Materials> mMoltenPosR;
    private final List<Materials> mMoltenPosG;
    private final List<Materials> mMoltenPosB;
    private final List<Materials> mMoltenPosA = Collections.emptyList();
    private final List<Materials> mMoltenNegR;
    private final List<Materials> mMoltenNegG;
    private final List<Materials> mMoltenNegB;
    private final List<Materials> mMoltenNegA = Collections.emptyList();
    private static long mAnimationTick;
    /**
     * This is the place to def the value used below
     **/
    private long afterSomeTime;

    private boolean mAnimationDirection;
    private GTClientPreference mPreference;
    private boolean mFirstTick = false;
    private int mReloadCount;
    private static float renderTickTime;

    public GTClient() {
        mAnimationTick = 0L;
        mAnimationDirection = false;
        mPosR = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.Thaumium,
            Materials.InfusedVis,
            Materials.InfusedAir,
            Materials.InfusedFire,
            Materials.FierySteel,
            Materials.Firestone);
        mPosG = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.InfusedAir,
            Materials.InfusedEarth);
        mPosB = Arrays.asList(
            Materials.Enderium,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.InfusedVis,
            Materials.InfusedWater,
            Materials.Thaumium);
        mNegR = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mNegG = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mNegB = Arrays.asList(Materials.InfusedEntropy, Materials.NetherStar);
        mMoltenPosR = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.Thaumium,
            Materials.InfusedVis,
            Materials.InfusedAir,
            Materials.InfusedFire,
            Materials.FierySteel,
            Materials.Firestone);
        mMoltenPosG = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.Force,
            Materials.Pyrotheum,
            Materials.Sunnarium,
            Materials.Glowstone,
            Materials.InfusedAir,
            Materials.InfusedEarth);
        mMoltenPosB = Arrays.asList(
            Materials.Enderium,
            Materials.NetherStar,
            Materials.Vinteum,
            Materials.Uranium235,
            Materials.InfusedGold,
            Materials.Plutonium241,
            Materials.NaquadahEnriched,
            Materials.Naquadria,
            Materials.InfusedOrder,
            Materials.InfusedVis,
            Materials.InfusedWater,
            Materials.Thaumium);
        mMoltenNegR = Collections.singletonList(Materials.InfusedEntropy);
        mMoltenNegG = Collections.singletonList(Materials.InfusedEntropy);
        mMoltenNegB = Collections.singletonList(Materials.InfusedEntropy);
        fixedBottomFaceUV = (boolean) Launch.blackboard
            .getOrDefault("hodgepodge.FixesConfig.fixBottomFaceUV", Boolean.FALSE);
    }

    @Override
    public boolean isClientSide() {
        return true;
    }

    @Override
    public EntityPlayer getThePlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void onPreInitialization(FMLPreInitializationEvent event) {
        super.onPreInitialization(event);
        SoundSystemConfig.setNumberNormalChannels(Client.preference.maxNumSounds);
        MinecraftForge.EVENT_BUS.register(new ExtraIcons());
        Minecraft.getMinecraft()
            .getResourcePackRepository().rprMetadataSerializer
                .registerMetadataSectionType(new ColorsMetadataSectionSerializer(), ColorsMetadataSection.class);
        mPreference = new GTClientPreference();
        Materials.initClient();

        ClientCommandHandler.instance.registerCommand(new GTPowerfailCommandClient());
        ClientCommandHandler.instance.registerCommand(new PowerGogglesCommand());
        ClientCommandHandler.instance.registerCommand(new GTCapeCommand());

        if (Mods.Navigator.isModLoaded()) {
            registerMapLayers();
        }
    }

    @Optional.Method(modid = Mods.ModIDs.NAVIGATOR)
    private void registerMapLayers() {
        NavigatorApi.registerLayerManager(PowerfailLayerManager.INSTANCE);
    }

    @Override
    public void onInitialization(FMLInitializationEvent event) {
        // spotless:off
        super.onInitialization(event);
        RenderingRegistry.registerBlockHandler(new GTRendererBlock());
        RenderingRegistry.registerBlockHandler(new GTRendererCasing());

        ClientRegistry.bindTileEntitySpecialRenderer(RenderingTileEntityDrone.class, new DroneRender());
        ClientRegistry.bindTileEntitySpecialRenderer(RenderingTileEntityLaser.class, new LaserRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(RenderingTileEntityWormhole.class, new WormholeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(RenderingTileEntityBlackhole.class, new BlackholeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(RenderingTileEntityNanoForge.class, new NanoForgeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, new BaseMetaTileEntityRenderer());

        MetaGeneratedItemRenderer metaItemRenderer = new MetaGeneratedItemRenderer();
        for (MetaGeneratedItem item : MetaGeneratedItem.sInstances.values()) {
            metaItemRenderer.registerItem(item);
        }
        if (Forestry.isModLoaded()) {
            metaItemRenderer.registerItem(GTBees.combs);
        }

        final MetaGeneratedToolRenderer metaToolRenderer = new MetaGeneratedToolRenderer();
        for (MetaGeneratedTool tItem : MetaGeneratedTool.sInstances.values()) {
            if (tItem != null) {
                MinecraftForgeClient.registerItemRenderer(tItem, metaToolRenderer);
            }
        }

        final FlaskRenderer flaskRenderer = new FlaskRenderer();
        MinecraftForgeClient.registerItemRenderer(ItemList.VOLUMETRIC_FLASK.getItem(), flaskRenderer);
        MinecraftForgeClient.registerItemRenderer(GregtechItemList.VOLUMETRIC_FLASK_8k.getItem(), flaskRenderer);
        MinecraftForgeClient.registerItemRenderer(GregtechItemList.VOLUMETRIC_FLASK_32k.getItem(), flaskRenderer);
        MinecraftForgeClient.registerItemRenderer(GregtechItemList.KLEIN_BOTTLE.getItem(), flaskRenderer);

        MinecraftForgeClient.registerItemRenderer(ItemList.Display_Fluid.getItem(), new FluidDisplayStackRenderer());
        MetaGeneratedItemRenderer.registerSpecialRenderer(ItemList.Tool_DataStick, new DataStickRenderer());
        MetaGeneratedItemRenderer.registerSpecialRenderer(ItemList.Spray_Color_Infinite, new InfiniteSprayCanRenderer());
        MinecraftForge.EVENT_BUS.register(new NEIGTConfig());
        MinecraftForge.EVENT_BUS.register(mPollutionRenderer);
        FMLCommonHandler.instance().bus().register(mPollutionRenderer);
        MinecraftForge.EVENT_BUS.register(new GTMouseEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockOverlayRenderer());
        MinecraftForge.EVENT_BUS.register(new MTEAdvDebugStructureWriter.EventHandler());
        powerfailRenderer = new GTPowerfailRenderer();
        MinecraftForge.EVENT_BUS.register(powerfailRenderer);
        shakeLockKey = new KeyBinding("GTPacketInfiniteSpraycan.Action.TOGGLE_SHAKE_LOCK", Keyboard.KEY_NONE, "Gregtech");
        ClientRegistry.registerKeyBinding(shakeLockKey);

        RenderManager.instance.entityRenderMap.put(EntityPowderBarrelPrimed.class, new RenderPowderBarrel());
        // spotless:on
    }

    @Override
    public void onPostInitialization(FMLPostInitializationEvent event) {
        super.onPostInitialization(event);

        // reobf doesn't work with lambda, so this must be a class
        // noinspection Convert2Lambda
        ((IReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {

                @Override
                public void onResourceManagerReload(IResourceManager l) {
                    GUIColorOverride.onResourceManagerReload();
                    FallbackableSteamTexture.reload();
                    CoverRegistry.reloadCoverColorOverrides();
                }
            });
        Pollution.onPostInitClient();

        ModuleRegistrar.instance()
            .registerTooltipRenderer("waila.gt.progress", new TTRenderGTProgressBar());
    }

    @Override
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        super.onLoadComplete(event);
        for (RecipeCategory category : RecipeCategory.ALL_RECIPE_CATEGORIES.values()) {
            if (category.recipeMap.getFrontend()
                .getNEIProperties().registerNEI) {
                FluidRecipe.addRecipeMap(
                    category.unlocalizedName,
                    new GregTech5RecipeExtractor(
                        category.unlocalizedName.equals("gt.recipe.scanner")
                            || category.unlocalizedName.equals("gt.recipe.fakeAssemblylineProcess")));
            }
        }
    }

    @Override
    @SubscribeEvent
    public void applyBlockWalkOverEffects(LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        // the player should handle its own movement, rest is handled by the server
        if (entity instanceof EntityClientPlayerMP && entity.onGround) {
            int tX = MathHelper.floor_double(entity.posX),
                tY = MathHelper.floor_double(entity.boundingBox.minY - 0.001F),
                tZ = MathHelper.floor_double(entity.posZ);
            Block tBlock = entity.worldObj.getBlock(tX, tY, tZ);
            if (tBlock instanceof IBlockOnWalkOver)
                ((IBlockOnWalkOver) tBlock).onWalkOver(entity, entity.worldObj, tX, tY, tZ);
        } else {
            super.applyBlockWalkOverEffects(event);
        }
    }

    @SubscribeEvent
    public void onSoundSetup(SoundSetupEvent event) {
        try {
            SoundSystemConfig.setCodec(SeekingOggCodec.EXTENSION, SeekingOggCodec.class);
        } catch (SoundSystemException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent aEvent) {
        mFirstTick = true;
        mReloadCount++;
        GTMusicSystem.ClientSystem.reset();
        RenderOverlay.reset();
    }

    @Override
    public void reloadNEICache() {
        mReloadCount++;
    }

    @Override
    public int getNEIReloadCount() {
        return mReloadCount;
    }

    @SubscribeEvent
    public void onPlayerTickEventClient(TickEvent.PlayerTickEvent aEvent) {
        if ((aEvent.side.isClient()) && (aEvent.phase == TickEvent.Phase.END) && (!aEvent.player.isDead)) {
            if (mFirstTick) {
                mFirstTick = false;
                GTValues.NW.sendToServer(new GTPacketClientPreference(mPreference));
                GTValues.NW.sendToServer(new GTPacketSetCape(Client.preference.selectedCape));

                if (!Minecraft.getMinecraft()
                    .isSingleplayer()) {
                    GTModHandler.removeAllIC2Recipes();
                }
            }
            afterSomeTime++;
            if (afterSomeTime >= 100L) {
                afterSomeTime = 0;
            }
            for (Iterator<Map.Entry<GTPlayedSound, Integer>> iterator = GTUtility.sPlayedSoundMap.entrySet()
                .iterator(); iterator.hasNext();) {
                Map.Entry<GTPlayedSound, Integer> tEntry = iterator.next();
                if (tEntry.getValue() < 0) {
                    iterator.remove();
                } else {
                    tEntry.setValue(tEntry.getValue() - 1);
                }
            }
            if (!GregTechAPI.mServerStarted) GregTechAPI.mServerStarted = true;
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent e) {
        if (GregTech.ID.equals(e.modID)) {
            // refresh client preference and send to server, since it's the only config we allow changing at runtime.
            mPreference = new GTClientPreference();
            GTPreLoad.loadClientConfig();
            if (e.isWorldRunning) {
                GTValues.NW.sendToServer(new GTPacketClientPreference(mPreference));
                GTValues.NW.sendToServer(new GTPacketSetCape(Client.preference.selectedCape));
            }
        }
    }

    @SubscribeEvent
    public void receiveRenderEvent(net.minecraftforge.client.event.RenderPlayerEvent.Pre aEvent) {
        if (GTUtility.getFullInvisibility(aEvent.entityPlayer)) {
            aEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(ClientTickEvent aEvent) {
        if (aEvent.phase == TickEvent.Phase.END) {
            mTicksUntilNextCraftSound--;
            GTMusicSystem.ClientSystem.tick();

            if (changeDetected > 0) changeDetected--;
            final boolean newHideValue = shouldHeldItemHideThings();
            if (newHideValue != hideThings) {
                hideThings = newHideValue;
                changeDetected = 5;
            }
            heldItemForcesFullBlockBB = shouldHeldItemForceFullBlockBB();

            // Animation related bits need to cease when game is paused in singleplayer.
            if (!Minecraft.getMinecraft()
                .isGamePaused()) {
                mAnimationTick++;

                if (mAnimationTick % 50L == 0L) {
                    mAnimationDirection = !mAnimationDirection;
                }

                final int tDirection = mAnimationDirection ? 1 : -1;
                for (Materials tMaterial : mPosR) {
                    tMaterial.mRGBa[0] = getSafeRGBValue(tMaterial.mRGBa[0], tDirection);
                }

                for (Materials tMaterial : mPosG) {
                    tMaterial.mRGBa[1] = getSafeRGBValue(tMaterial.mRGBa[1], tDirection);
                }

                for (Materials tMaterial : mPosB) {
                    tMaterial.mRGBa[2] = getSafeRGBValue(tMaterial.mRGBa[2], tDirection);
                }

                for (Materials tMaterial : mPosA) {
                    tMaterial.mRGBa[3] = getSafeRGBValue(tMaterial.mRGBa[3], tDirection);
                }

                for (Materials tMaterial : mNegR) {
                    tMaterial.mRGBa[0] = getSafeRGBValue(tMaterial.mRGBa[0], -tDirection);
                }

                for (Materials tMaterial : mNegG) {
                    tMaterial.mRGBa[1] = getSafeRGBValue(tMaterial.mRGBa[1], -tDirection);
                }

                for (Materials tMaterial : mNegB) {
                    tMaterial.mRGBa[2] = getSafeRGBValue(tMaterial.mRGBa[2], -tDirection);
                }

                for (Materials tMaterial : mNegA) {
                    tMaterial.mRGBa[3] = getSafeRGBValue(tMaterial.mRGBa[3], -tDirection);
                }

                for (Materials tMaterial : mMoltenPosR) {
                    tMaterial.mMoltenRGBa[0] = getSafeRGBValue(tMaterial.mMoltenRGBa[0], tDirection);
                }

                for (Materials tMaterial : mMoltenPosG) {
                    tMaterial.mMoltenRGBa[1] = getSafeRGBValue(tMaterial.mMoltenRGBa[1], tDirection);
                }

                for (Materials tMaterial : mMoltenPosB) {
                    tMaterial.mMoltenRGBa[2] = getSafeRGBValue(tMaterial.mMoltenRGBa[2], tDirection);
                }

                for (Materials tMaterial : mMoltenPosA) {
                    tMaterial.mMoltenRGBa[3] = getSafeRGBValue(tMaterial.mMoltenRGBa[3], tDirection);
                }

                for (Materials tMaterial : mMoltenNegR) {
                    tMaterial.mMoltenRGBa[0] = getSafeRGBValue(tMaterial.mMoltenRGBa[0], -tDirection);
                }

                for (Materials tMaterial : mMoltenNegG) {
                    tMaterial.mMoltenRGBa[1] = getSafeRGBValue(tMaterial.mMoltenRGBa[1], -tDirection);
                }

                for (Materials tMaterial : mMoltenNegB) {
                    tMaterial.mMoltenRGBa[2] = getSafeRGBValue(tMaterial.mMoltenRGBa[2], -tDirection);
                }

                for (Materials tMaterial : mMoltenNegA) {
                    tMaterial.mMoltenRGBa[3] = getSafeRGBValue(tMaterial.mMoltenRGBa[3], -tDirection);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (HazardProtection.providesFullHazmatProtection(event.itemStack)) {
            addHazmatTooltip(event, HazardProtectionTooltip.FULL_PROTECTION_TRANSLATION_KEY);
            return;
        }

        // TreeSet so it's always the same order
        TreeSet<Hazard> protections = new TreeSet<>();
        for (Hazard hazard : Hazard.VALUES) {
            if (HazardProtection.protectsAgainstHazard(event.itemStack, hazard)) {
                protections.add(hazard);
            }
        }
        if (protections.containsAll(HazardProtectionTooltip.CBRN_HAZARDS)) {
            protections.removeAll(HazardProtectionTooltip.CBRN_HAZARDS);
            addHazmatTooltip(event, HazardProtectionTooltip.CBRN_TRANSLATION_KEY);
        }

        if (protections.containsAll(HazardProtectionTooltip.TEMPERATURE_HAZARDS)) {
            protections.removeAll(HazardProtectionTooltip.TEMPERATURE_HAZARDS);
            addHazmatTooltip(event, HazardProtectionTooltip.EXTREME_TEMP_TRANSLATION_KEY);
        }
        for (Hazard hazard : protections) {
            // Handled by GalaxySpace
            if (hazard == Hazard.SPACE) continue;
            addHazmatTooltip(event, HazardProtectionTooltip.singleHazardTranslationKey(hazard));
        }
    }

    private void addHazmatTooltip(ItemTooltipEvent event, String translationKey) {
        event.toolTip.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal(translationKey));
    }

    private int mTicksUntilNextCraftSound = 0;

    // Used for tool sounds in the crafting grid
    @SubscribeEvent
    public void onPlayerCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (!MetaGeneratedTool.playSound) return;
        if (this.mTicksUntilNextCraftSound > 0) return;
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = event.craftMatrix.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof MetaGeneratedTool mgt) {
                IToolStats tStats = mgt.getToolStats(stack);
                boolean playBreak = (MetaGeneratedTool.getToolDamage(stack) + tStats.getToolDamagePerContainerCraft())
                    >= MetaGeneratedTool.getToolMaxDamage(stack);
                String sound = playBreak ? tStats.getBreakingSound() : tStats.getCraftingSound();
                GTUtility.doSoundAtClient(sound, 1, 1.0F);
                this.mTicksUntilNextCraftSound = 10;
                return;
            }
        }
    }

    public short getSafeRGBValue(short aRBG, int aDelta) {
        int tmp = aRBG + aDelta;
        if (tmp > 255) tmp = 255;
        if (tmp < 0) tmp = 0;
        return (short) tmp;
    }

    public static float getAnimationRenderTicks() {
        return mAnimationTick + renderTickTime;
    }

    private boolean hideThings = false;

    public boolean shouldHideThings() {
        return hideThings;
    }

    /**
     * <p>
     * Client tick counter that is set to 5 on hiding pipes and covers.
     * </p>
     * <p>
     * It triggers a texture update next client tick when reaching 4, with provision for 3 more update tasks, spreading
     * client change detection related work and network traffic on different ticks, until it reaches 0.
     * </p>
     */
    private int changeDetected = 0;

    public int changeDetected() {
        return changeDetected;
    }

    private static boolean shouldHeldItemHideThings() {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return false;
        final ItemStack tCurrentItem = player.getCurrentEquippedItem();
        if (tCurrentItem == null) return false;
        final int[] ids = OreDictionary.getOreIDs(tCurrentItem);
        for (int i : ids) {
            String oreName = OreDictionary.getOreName(i);
            if (oreName != null && oreName.equals("craftingToolSolderingIron")) {
                return true;
            }
        }
        return false;
    }

    private boolean isRenderingWorld;
    private boolean isComputingPickBlock;
    private boolean heldItemForcesFullBlockBB;

    public void setComputingPickBlock(boolean b) {
        isComputingPickBlock = b;
    }

    @SubscribeEvent
    public void onRenderStart(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            renderTickTime = event.renderTickTime;
            isRenderingWorld = true;
        } else if (event.phase == TickEvent.Phase.END) {
            isRenderingWorld = false;
        }
    }

    public boolean forceFullBlockBB() {
        return heldItemForcesFullBlockBB && (isRenderingWorld || isComputingPickBlock);
    }

    private static boolean shouldHeldItemForceFullBlockBB() {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return false;
        final ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null) return false;
        return GTUtility.isStackInList(stack, GregTechAPI.sWrenchList)
            || GTUtility.isStackInList(stack, GregTechAPI.sHardHammerList)
            || GTUtility.isStackInList(stack, GregTechAPI.sSoftMalletList)
            || GTUtility.isStackInList(stack, GregTechAPI.sWireCutterList)
            || GTUtility.isStackInList(stack, GregTechAPI.sSolderingToolList)
            || GTUtility.isStackInList(stack, GregTechAPI.sCrowbarList)
            || CoverRegistry.isCover(stack)
            || (stack.getItem() instanceof ItemMachines
                && GregTechAPI.METATILEENTITIES[stack.getItemDamage()] instanceof MetaPipeEntity
                && player.isSneaking());
    }

    public void processChunkPollutionPacket(ChunkCoordIntPair chunk, int pollution) {
        mPollutionRenderer.processPacket(chunk, pollution);
    }

    @Override
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        super.onWorldUnload(event);
        RenderOverlay.onWorldUnload(event.world);
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        RenderOverlay.onChunkUnload(
            event.world,
            event.getChunk()
                .getChunkCoordIntPair());
    }
}
