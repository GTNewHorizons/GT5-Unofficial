/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.InfernalMobs;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.kuba6000.mobsinfo.api.utils.FastRandom;
import com.mojang.authlib.GameProfile;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.event.RitualRunEvent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectWellOfSuffering;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.EnderIO;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.api.tileentity.CustomTileEntityPacketHandler;
import kubatech.api.utils.ModUtils;
import kubatech.client.effect.EntityRenderer;
import kubatech.config.Config;
import kubatech.loaders.MobHandlerLoader;
import kubatech.network.CustomTileEntityPacket;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEExtremeEntityCrusher extends KubaTechGTMultiBlockBase<MTEExtremeEntityCrusher>
    implements CustomTileEntityPacketHandler, ISurvivalConstructable {

    // Powered spawner with octadic capacitor spawns ~22/min ~= 0.366/sec ~= 2.72s/spawn ~= 54.54t/spawn
    public static final int MOB_SPAWN_INTERVAL = 55;
    public static final int MAX_LOOTING_LEVEL = 4;
    public static final double DIAMOND_SPIKES_DAMAGE = 9d;
    public final Random rand = new FastRandom();
    protected final WeaponCache weaponCache;
    private EECEventHandler eventHandler;

    @SuppressWarnings("unused")
    public MTEExtremeEntityCrusher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        weaponCache = new WeaponCache(mInventory);
    }

    public MTEExtremeEntityCrusher(String aName) {
        super(aName);
        weaponCache = new WeaponCache(mInventory);
        if (BloodMagic.isModLoaded() && FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) {
            eventHandler = new EECEventHandler();
            MinecraftForge.EVENT_BUS.register(eventHandler);
        }
    }

    @Override
    public void onRemoval() {
        if (eventHandler != null) MinecraftForge.EVENT_BUS.unregister(eventHandler);
        if (getBaseMetaTileEntity().isClientSide() && entityRenderer != null) {
            entityRenderer.setDead();
        }
    }

    @Override
    public void onUnload() {
        if (eventHandler != null) MinecraftForge.EVENT_BUS.unregister(eventHandler);
    }

    private static final String WellOfSufferingRitualName = "AW013Suffering";

    private static final Item poweredSpawnerItem = Item.getItemFromBlock(EnderIO.blockPoweredSpawner);
    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEExtremeEntityCrusher> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEExtremeEntityCrusher>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    {"ccccc", "ccccc", "ccccc", "ccccc", "ccccc"},
                    {"fgggf", "g---g", "g---g", "g---g", "fgggf"},
                    {"fgggf", "g---g", "g---g", "g---g", "fgggf"},
                    {"fgggf", "g---g", "g---g", "g---g", "fgggf"},
                    {"fgggf", "g---g", "g---g", "g---g", "fgggf"},
                    {"fgggf", "gsssg", "gsssg", "gsssg", "fgggf"},
                    {"CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"},
                })) // spotless:on
        .addElement('c', onElementPass(t -> t.mCasing++, ofBlock(GregTechAPI.sBlockCasings2, 0)))
        .addElement(
            'C',
            buildHatchAdder(MTEExtremeEntityCrusher.class)
                .atLeast(InputBus, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, ofBlock(GregTechAPI.sBlockCasings2, 0))))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('f', ofFrame(Materials.Steel))
        .addElement(
            's',
            ExtraUtilities.isModLoaded() ? ofBlock(Block.getBlockFromName("ExtraUtilities:spike_base_diamond"), 0)
                : isAir())
        .build();

    private static final int[][] VALID_RITUAL_POSITIONS = { { 0, -8, 2 }, { 0, -7, 2 } };

    private TileEntity masterStoneRitual = null;
    private TileEntity tileAltar = null;
    private boolean isInRitualMode = false;
    private int mCasing = 0;
    private int glassTier = -1;
    private boolean mAnimationEnabled = true;
    private boolean mIsProducingInfernalDrops = true;
    private boolean voidAllDamagedAndEnchantedItems = false;
    private boolean mPreserveWeapon;
    private boolean mCycleWeapons;

    private boolean mIsRitualValid;
    private boolean mIsPreventingGUIWeaponUse;

    private EntityRenderer entityRenderer = null;
    private boolean renderEntity = false;
    public EECFakePlayer EECPlayer = null;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isInRitualMode", isInRitualMode);
        aNBT.setBoolean("mAnimationEnabled", mAnimationEnabled);
        aNBT.setBoolean("mIsProducingInfernalDrops", mIsProducingInfernalDrops);
        aNBT.setBoolean("voidAllDamagedAndEnchantedItems", voidAllDamagedAndEnchantedItems);
        aNBT.setBoolean("mPreserveWeapon", mPreserveWeapon);
        aNBT.setBoolean("mCycleWeapons", mCycleWeapons);
        if (weaponCache.getStackInSlot(0) != null) aNBT.setTag(
            "weaponCache",
            weaponCache.getStackInSlot(0)
                .writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isInRitualMode = aNBT.getBoolean("isInRitualMode");
        mAnimationEnabled = !aNBT.hasKey("mAnimationEnabled") || aNBT.getBoolean("mAnimationEnabled");
        mIsProducingInfernalDrops = !aNBT.hasKey("mIsProducingInfernalDrops")
            || aNBT.getBoolean("mIsProducingInfernalDrops");
        voidAllDamagedAndEnchantedItems = aNBT.getBoolean("voidAllDamagedAndEnchantedItems");
        mPreserveWeapon = aNBT.getBoolean("mPreserveWeapon");
        mCycleWeapons = !aNBT.hasKey("mCycleWeapons") || aNBT.getBoolean("mCycleWeapons");
        if (aNBT.hasKey("weaponCache"))
            weaponCache.setStackInSlot(0, ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("weaponCache")));
    }

    @Override
    public boolean isOverclockingInfinite() {
        return true;
    }

    @Override
    protected int getOverclockTimeLimit() {
        return (batchMode ? 16 : 1) * 20;
    }

    @Override
    public IStructureDefinition<MTEExtremeEntityCrusher> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Powered Spawner, EEC")
            .addInfo("Spawns and kills monsters for you!")
            .addInfo(
                "Produces " + EnumChatFormatting.GREEN + "120 Liquid XP" + EnumChatFormatting.GRAY + " per operation")
            .addInfo("Powered Spawner goes in Controller Slot")
            .addInfo("Base energy usage: " + EnumChatFormatting.AQUA + "1920" + EnumChatFormatting.GRAY + " EU/t")
            .addInfo("Supports " + EnumChatFormatting.LIGHT_PURPLE + "perfect OC!")
            .addSeparator()
            .addInfo("Has a minimum recipe time of 20 ticks, further overclocks multiply outputs by 4x")
            .addInfo("Recipe time is based on mob health")
            .addInfo("You can additionally put a weapon inside the GUI")
            .addInfo(
                "It will speed up the process and apply the looting level from the weapon (maximum " + MAX_LOOTING_LEVEL
                    + " levels)")
            .addInfo("Enable Weapon Preservation to prevent the weapon from breaking on it's last hit")
            .addInfo(
                "Enable Weapon Cycling to pull a weapon from input when the current one breaks or is moved to an output")
            .addInfo(EnumChatFormatting.RED + "Enchanting the spikes inside the structure does nothing!")
            .addSeparator()
            .addInfo(
                "If the mob spawns " + EnumChatFormatting.RED
                    + "infernal"
                    + EnumChatFormatting.GRAY
                    + ", it will drain 8 times more power!")
            .addInfo(
                "You can prevent " + EnumChatFormatting.RED
                    + "infernal"
                    + EnumChatFormatting.GRAY
                    + " spawns by shift clicking with a screwdriver")
            .addInfo(
                "Mobs who are always " + EnumChatFormatting.RED
                    + "infernal"
                    + EnumChatFormatting.GRAY
                    + " will ignore this factor")
            .addSeparator()
            .addInfo("You can enable ritual mode with a screwdriver")
            .addInfo("When in ritual mode, can link to above Well of Suffering rituals")
            .addInfo("The Ritual must be built directly centered above the machine")
            .addInfo("When linked, mobs will start to buffer and die very slowly, providing blood to the linked altar")
            .addSeparator()
            .addInfo("You can disable mob animation with a soldering iron")
            .addInfo(
                "You can enable batch mode with wire cutters. Providing " + EnumChatFormatting.BLUE
                    + " 16x Time, Output, Weapon Damage")
            .addGlassEnergyLimitInfo(VoltageIndex.UV)
            .beginStructureBlock(5, 7, 5, true)
            .addController("Front Bottom Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 35, false)
            .addCasingInfoExactly("Any Tiered Glass", 60, false)
            .addCasingInfoExactly("Steel Frame Box", 20, false)
            .addCasingInfoExactly("Diamond Spike", 9, false)
            .addOutputBus("Any bottom casing", 1)
            .addOutputHatch("Any bottom casing", 1)
            .addEnergyHatch("Any bottom casing", 1)
            .addMaintenanceHatch("Any bottom casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTValues.AuthorKuba);
        return tt;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 6, 0, elementBudget, env, true, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExtremeEntityCrusher(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @SideOnly(Side.CLIENT)
    private void setupEntityRenderer(IGregTechTileEntity aBaseMetaTileEntity, int time) {
        if (entityRenderer == null) {
            ChunkCoordinates coords = this.getBaseMetaTileEntity()
                .getCoords();
            int[] abc = new int[] { 0, -2, 2 };
            int[] xyz = new int[] { 0, 0, 0 };
            this.getExtendedFacing()
                .getWorldOffset(abc, xyz);
            xyz[0] += coords.posX;
            xyz[1] += coords.posY;
            xyz[2] += coords.posZ;
            entityRenderer = new EntityRenderer(aBaseMetaTileEntity.getWorld(), xyz[0], xyz[1], xyz[2], time);
        } else {
            entityRenderer.setDead();
            entityRenderer = new EntityRenderer(entityRenderer, time);
        }
        Minecraft.getMinecraft().effectRenderer.addEffect(entityRenderer);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
            if (renderEntity && aBaseMetaTileEntity.isActive() && aTick % 40 == 0) {
                setupEntityRenderer(aBaseMetaTileEntity, 40);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void HandleCustomPacket(CustomTileEntityPacket message) {
        if (message.getDataBoolean() && Mods.MobsInfo.isModLoaded()) {
            renderEntity = true;
            String mobType = message.getDataString();
            MobHandlerLoader.MobEECRecipe r = MobHandlerLoader.recipeMap.get(mobType);
            if (r != null) {
                if (entityRenderer == null) setupEntityRenderer(getBaseMetaTileEntity(), 40);
                entityRenderer.setEntity(r.entityCopy);
            } else entityRenderer.setEntity(null);
        } else {
            renderEntity = false;
            if (entityRenderer != null) {
                entityRenderer.setDead();
                entityRenderer = null;
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatTrans(aPlayer, "kubatech.chat.forbidden_while_running");
            return;
        }
        if (aPlayer.isSneaking()) {
            if (!InfernalMobs.isModLoaded()) return;
            mIsProducingInfernalDrops = !mIsProducingInfernalDrops;
            GTUtility.sendChatTrans(
                aPlayer,
                "kubatech.chat.eec.infernal_drops_" + (mIsProducingInfernalDrops ? "enabled" : "disabled"));
        } else {
            if (!BloodMagic.isModLoaded()) return;
            isInRitualMode = !isInRitualMode;
            checkRitualConnection();

            if (!isInRitualMode) {
                GTUtility.sendChatTrans(aPlayer, "kubatech.chat.eec.ritual_mode_disabled");
            } else {
                GTUtility.sendChatTrans(aPlayer, "kubatech.chat.eec.ritual_mode_enabled");
                GTUtility.sendChatTrans(
                    aPlayer,
                    "kubatech.chat.eec.ritual_mode_" + (mIsRitualValid ? "connected" : "error"));
            }
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (wrenchingSide == getBaseMetaTileEntity().getFrontFacing()) {
            mAnimationEnabled = !mAnimationEnabled;
            GTUtility.sendChatToPlayer(aPlayer, "Animations are " + (mAnimationEnabled ? "enabled" : "disabled"));
            return true;
        } else return super.onSolderingToolRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        this.batchMode = !this.batchMode;
        GTUtility.sendChatTrans(
            aPlayer,
            this.batchMode ? "GT5U.chat.machine.batch_mode.enable" : "GT5U.chat.machine.batch_mode.disable");
        return true;
    }

    // We place the event handler in an inner
    // class to prevent high costs of registering
    // the event because forge event bus reflects
    // through all the methods and super of the class
    // in order to find the @SubscribeEvent annotations
    public class EECEventHandler {

        @SuppressWarnings("unused")
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onRitualPerform(RitualRunEvent event) {
            if (!isInRitualMode) return;
            if (masterStoneRitual == null) return;
            if (mMaxProgresstime == 0) return;
            if (event.mrs.equals(masterStoneRitual)) {
                Rituals ritual = Rituals.ritualMap.get(WellOfSufferingRitualName);
                if (ritual != null && ritual.effect instanceof RitualEffectWellOfSuffering effect) {
                    event.setCanceled(true); // we will handle that
                    String owner = event.mrs.getOwner();
                    int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
                    World world = event.mrs.getWorld();
                    int x = event.mrs.getXCoord();
                    int y = event.mrs.getYCoord();
                    int z = event.mrs.getZCoord();

                    if (world.getWorldTime() % RitualEffectWellOfSuffering.timeDelay != 0) return;

                    if (tileAltar == null || tileAltar.isInvalid()) {
                        tileAltar = null;
                        for (int i = -5; i <= 5; i++) for (int j = -5; j <= 5; j++) for (int k = -10; k <= 10; k++)
                            if (world.getTileEntity(x + i, y + k, z + j) instanceof IBloodAltar)
                                tileAltar = world.getTileEntity(x + i, y + k, z + j);
                    }
                    if (tileAltar == null) return;

                    if (currentEssence < effect.getCostPerRefresh() * 100) {
                        SoulNetworkHandler.causeNauseaToPlayer(owner);
                        return;
                    }

                    ((IBloodAltar) tileAltar).sacrificialDaggerCall(
                        100 * RitualEffectWellOfSuffering.amount
                            * (effect.canDrainReagent(
                                event.mrs,
                                ReagentRegistry.offensaReagent,
                                RitualEffectWellOfSuffering.offensaDrain,
                                true) ? 2 : 1)
                            * (effect.canDrainReagent(
                                event.mrs,
                                ReagentRegistry.tenebraeReagent,
                                RitualEffectWellOfSuffering.tennebraeDrain,
                                true) ? 2 : 1),
                        true);

                    SoulNetworkHandler.syphonFromNetwork(owner, effect.getCostPerRefresh() * 100);
                }
            }
        }
    }

    private CustomTileEntityPacket mobPacket = null;

    private static class WeaponCache extends ItemStackHandler {

        boolean isValid = false;
        int looting = 0;
        double attackDamage = 0;

        public WeaponCache(ItemStack[] inventory) {
            super(inventory);
        }

        @Override
        protected final void onContentsChanged(final int slot) {
            if (slot != 0) return;
            if (ModUtils.isClientThreaded()) return;
            ItemStack stack = getStackInSlot(0);
            if (stack == null) {
                isValid = false;
                return;
            }
            attackDamage = MTEExtremeEntityCrusher.getWeaponAttackDamage(stack);
            looting = MTEExtremeEntityCrusher.getWeaponLooting(stack);
            isValid = true;
        }

        @Override
        public final boolean isItemValid(final int aSlot, final ItemStack aStack) {
            return aSlot == 0 && MTEExtremeEntityCrusher.isUsableWeapon(aStack);
        }
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex >= 0;
    }

    @Override
    public void onSetActive(boolean active) {
        super.onSetActive(active);
        if (active) return;
        checkRitualConnection();
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (getBaseMetaTileEntity().isClientSide()) return CheckRecipeResultRegistry.NO_RECIPE;
        mIsPreventingGUIWeaponUse = false;
        ItemStack aStack = mInventory[1];
        if (aStack == null) return SimpleCheckRecipeResult.ofFailure("EEC_nospawner");

        if (aStack.getItem() != poweredSpawnerItem) return SimpleCheckRecipeResult.ofFailure("EEC_nospawner");

        if (aStack.getTagCompound() == null) return SimpleCheckRecipeResult.ofFailure("EEC_invalidspawner");
        String mobType = aStack.getTagCompound()
            .getString("mobType");
        if (mobType.isEmpty()) return SimpleCheckRecipeResult.ofFailure("EEC_invalidspawner");

        if (mobType.equals("Skeleton") && getBaseMetaTileEntity().getWorld().provider instanceof WorldProviderHell
            && rand.nextInt(5) > 0) mobType = "witherSkeleton";

        MobHandlerLoader.MobEECRecipe recipe = MobHandlerLoader.recipeMap.get(mobType);

        if (recipe == null) return CheckRecipeResultRegistry.NO_RECIPE;
        if (!recipe.recipe.isPeacefulAllowed && this.getBaseMetaTileEntity()
            .getWorld().difficultySetting == EnumDifficulty.PEACEFUL && !Config.MobHandler.ignorePeacefulCheck)
            return SimpleCheckRecipeResult.ofFailure("EEC_peaceful");

        if (checkRitualConnection()) {
            if (getMaxInputEu() < recipe.mEUt / 4) return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt / 4);
            this.mOutputFluids = new FluidStack[] { FluidRegistry.getFluidStack("xpjuice", 5000) };
            this.mOutputItems = recipe
                .generateOutputs(rand, this, 3, 0, mIsProducingInfernalDrops, voidAllDamagedAndEnchantedItems);
            this.lEUt /= 4L;
            this.mMaxProgresstime = 400;
        } else {
            long tRecipeEUt = recipe.mEUt;
            if (recipe.recipe.alwaysinfernal) tRecipeEUt *= 8;

            if (getMaxInputEu() < tRecipeEUt) return CheckRecipeResultRegistry.insufficientPower(tRecipeEUt);

            double tAttackDamage = DIAMOND_SPIKES_DAMAGE;
            int tLootingLevel = 0;

            final int tBatchMultiplier = batchMode ? 16 : 1;

            final int tMaxTries = 2; // 2 => Weapon already in the slot + one extra
            ItemStack tWeaponToUse = cycleWeaponsUntilNoBreakage(recipe, tBatchMultiplier, tMaxTries);

            if (this.mCycleWeapons && weaponCache.getStackInSlot(0) == null) {
                moveWeaponFromInputToCache();
            }

            ItemStack tWeaponInCache = weaponCache.getStackInSlot(0);
            if (tWeaponToUse != null) {
                if (tWeaponToUse == tWeaponInCache) {
                    tAttackDamage += weaponCache.attackDamage;
                    tLootingLevel += weaponCache.looting;
                } else {
                    tAttackDamage += getWeaponAttackDamage(tWeaponToUse);
                    tLootingLevel += getWeaponLooting(tWeaponToUse);
                }
            } else if (tWeaponInCache != null) {
                mIsPreventingGUIWeaponUse = true;
            }

            if (EECPlayer == null) EECPlayer = new EECFakePlayer(this);

            EECPlayer.currentWeapon = tWeaponToUse;
            this.mOutputItems = recipe.generateOutputs(
                rand,
                this,
                tAttackDamage,
                Math.min(tLootingLevel, MAX_LOOTING_LEVEL),
                mIsProducingInfernalDrops,
                voidAllDamagedAndEnchantedItems);
            EECPlayer.currentWeapon = null;

            if (batchMode) {
                for (ItemStack item : mOutputItems) {
                    item.stackSize *= tBatchMultiplier;
                }
            }

            this.mOutputFluids = new FluidStack[] { FluidRegistry.getFluidStack("xpjuice", 120 * tBatchMultiplier) };
            this.mMaxProgresstime *= tBatchMultiplier;

            this.calculatePerfectOverclock(this.lEUt, this.mMaxProgresstime);
        }
        if (this.lEUt > 0) this.lEUt = -this.lEUt;
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (mobPacket == null) mobPacket = new CustomTileEntityPacket((TileEntity) this.getBaseMetaTileEntity(), null);
        mobPacket.resetHelperData();
        mobPacket.addData(mAnimationEnabled);
        if (mAnimationEnabled) mobPacket.addData(mobType);
        mobPacket.sendToAllAround(16);

        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private ItemStack cycleWeaponsUntilNoBreakage(final MobHandlerLoader.MobEECRecipe aRecipe,
        final int aBatchModeMultiplier, int aMaxTries) {
        final int tBusCount = mInputBusses.size();
        int tCurrentInputBus = 0;
        int tCurrentInputBusSlot = 0;

        ItemStack tWeapon = this.weaponCache.getStackInSlot(0);
        if (!this.weaponCache.isValid) {
            if (!mCycleWeapons) return null;
            aMaxTries--;
        }

        for (int i = 0; i < aMaxTries; i++) {
            if (!this.weaponCache.isValid) {
                // Try to find weapon from inputs
                if (tBusCount == 0) return null;

                for (int tBusIndex = tCurrentInputBus; tBusIndex < tBusCount; tBusIndex++) {
                    MTEHatchInputBus tBus = mInputBusses.get(tBusIndex);
                    for (int tSlotIndex = tCurrentInputBusSlot; tSlotIndex < tBus.mInventory.length; tSlotIndex++) {
                        tCurrentInputBusSlot++;

                        if (!tBus.isValidSlot(tSlotIndex)) continue;

                        ItemStack tItem = tBus.mInventory[tSlotIndex];
                        if (tItem == null || tItem.stackSize == 0 || !isUsableWeapon(tItem)) continue;

                        tWeapon = tItem;
                        weaponCache.setStackInSlot(0, tItem);
                        tBus.mInventory[tSlotIndex] = null;
                        break;
                    }
                    if (weaponCache.isValid) break;

                    tCurrentInputBusSlot = 0;
                    tCurrentInputBus++;
                }

                // Looped through all buses and found no usable weapon
                if (!weaponCache.isValid) {
                    weaponCache.setStackInSlot(0, null);
                    return null;
                }
            }

            ItemStack tWeaponResult = runWeaponHitSimulation(
                tWeapon,
                aRecipe.recipe.entity,
                aBatchModeMultiplier,
                mPreserveWeapon);

            // Weapon didn't break, use it
            if (tWeaponResult != null) {
                weaponCache.setStackInSlot(0, tWeaponResult);
                return tWeaponResult;
            }

            // Weapon copy broke during simulation, do we care?

            // We don't, use it for the next run and destroy it.
            if (!mPreserveWeapon) {
                weaponCache.setStackInSlot(0, null);
                playWeaponBreakSound();
                return tWeapon;
            }

            // We do care. Preserve the weapon
            if (!mCycleWeapons || !addOutputAtomic(tWeapon)) return null;
            weaponCache.setStackInSlot(0, null);
            tWeapon = null;
        }

        return null;
    }

    private ItemStack runWeaponHitSimulation(ItemStack aWeapon, final EntityLiving aTarget,
        final int aBatchModeMultiplier, final boolean aPreventPerfectUnbreaking) {
        if (aWeapon == null || !aWeapon.isItemStackDamageable()) return aWeapon;
        if (EECPlayer == null) EECPlayer = new EECFakePlayer(this);

        ItemStack tWeaponCopy = aWeapon.copy();
        Item tItem = tWeaponCopy.getItem();

        EECPlayer.currentWeapon = tWeaponCopy;
        for (int i = 0; i < aBatchModeMultiplier; i++) {
            // Force weapons at max damage to be considered broken,
            // even if they would survive a hit by having the Unbreaking enchantment.
            // This prevents weapons from being effectively unbreakable due to being
            // able to perfectly predict when a hit would or would not damage it.
            if (aPreventPerfectUnbreaking && tWeaponCopy.getItemDamage() == tWeaponCopy.getMaxDamage()) {
                EECPlayer.currentWeapon = null;
                return null;
            }

            // Simulate hit on entity with the weapon's copy
            if (!tItem.hitEntity(tWeaponCopy, aTarget, EECPlayer)) break;
            if (tWeaponCopy.stackSize == 0) {
                EECPlayer.currentWeapon = null;
                return null;
            }
        }
        EECPlayer.currentWeapon = null;
        return tWeaponCopy;
    }

    private void moveWeaponFromInputToCache() {
        if (this.weaponCache.isValid) return;
        for (MTEHatchInputBus tBus : mInputBusses) {
            for (int tSlotIndex = 0; tSlotIndex < tBus.mInventory.length; tSlotIndex++) {
                if (!tBus.isValidSlot(tSlotIndex)) continue;

                ItemStack tItem = tBus.mInventory[tSlotIndex];
                if (tItem == null || tItem.stackSize == 0 || !isUsableWeapon(tItem)) continue;

                weaponCache.setStackInSlot(0, tItem);
                tBus.mInventory[tSlotIndex] = null;
                return;
            }
        }
    }

    private static boolean isUsableWeapon(final ItemStack aWeapon) {
        return Enchantment.looting.canApply(aWeapon);
    }

    private static double getWeaponAttackDamage(final ItemStack aWeapon) {
        return aWeapon.getAttributeModifiers()
            .get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())
            .stream()
            .mapToDouble(
                attr -> attr.getAmount()
                    + (double) EnchantmentHelper.func_152377_a(aWeapon, EnumCreatureAttribute.UNDEFINED))
            .sum();
    }

    private static int getWeaponLooting(final ItemStack aWeapon) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, aWeapon);
    }

    private void playWeaponBreakSound() {
        final IGregTechTileEntity tMTE = this.getBaseMetaTileEntity();

        if (tMTE == null || tMTE.isMuffled()) return;

        // A little muffled and modulated, helps simulate a Low-Pass filter
        GTUtility.sendSoundToPlayers(
            tMTE.getWorld(),
            SoundResource.RANDOM_BREAK,
            0.5F,
            0.9F,
            tMTE.getXCoord() + .5,
            tMTE.getYCoord() + .5,
            tMTE.getZCoord() + .5);
    }

    private boolean checkRitualConnection() {
        mIsRitualValid = isInRitualMode && connectToRitual();
        return mIsRitualValid;
    }

    private boolean connectToRitual() {
        if (masterStoneRitual == null) {
            if (!BloodMagic.isModLoaded()) return false;

            for (int[] ritualRelativePos : VALID_RITUAL_POSITIONS) {
                masterStoneRitual = getTileEntityAtRelativePosition(ritualRelativePos);
                if (isWellOfSufferingRitual(masterStoneRitual)) return true;
            }
        } else if (BloodMagic.isModLoaded() && isWellOfSufferingRitual(masterStoneRitual)) return true;

        masterStoneRitual = null;
        return false;
    }

    @Nullable
    private TileEntity getTileEntityAtRelativePosition(int @NotNull [] relativePosition) {

        if (relativePosition.length < 3) return null;

        int[] relativeCoords = new int[] { 0, 0, 0 };
        this.getExtendedFacing()
            .getWorldOffset(relativePosition, relativeCoords);
        ChunkCoordinates worldCoords = this.getBaseMetaTileEntity()
            .getCoords();

        return this.getBaseMetaTileEntity()
            .getTileEntity(
                worldCoords.posX + relativeCoords[0],
                worldCoords.posY + relativeCoords[1],
                worldCoords.posZ + relativeCoords[2]);
    }

    private static boolean isWellOfSufferingRitual(@Nullable TileEntity tileEntity) {
        return tileEntity != null && !tileEntity.isInvalid()
            && tileEntity instanceof TEMasterStone ritualTE
            && ritualTE.getCurrentRitual()
                .equals(WellOfSufferingRitualName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glassTier = -1;
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 6, 0)) return false;
        if (mCasing < 35 || mEnergyHatches.isEmpty()) return false;
        if (glassTier < VoltageIndex.UV)
            for (MTEHatchEnergy hatch : mEnergyHatches) if (hatch.mTier > glassTier) return false;
        checkRitualConnection();
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_EXTREME_ENTITY_CRUSHER_LOOP;
    }

    private String getCurrentMob() {
        ItemStack spawner = mInventory[1];
        if (spawner != null && spawner.getTagCompound() != null) {
            return spawner.getTagCompound()
                .getString("mobType");
        }
        return null;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        String mob = getCurrentMob();
        if (mob != null) {
            tag.setString("eecMobType", mob);
        }
        if (isInRitualMode) {
            tag.setBoolean("isInRitualMode", true);
            tag.setBoolean("isRitualValid", mIsRitualValid);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.hasKey("eecMobType", Constants.NBT.TAG_STRING)) {
            String mob = tag.getString("eecMobType");
            String mobKey = "entity." + mob + ".name";
            if (StatCollector.canTranslate(mobKey)) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "kubatech.waila.eec.mob_type",
                        StatCollector.translateToLocal(mobKey)));
            } else {
                currentTip.add(StatCollector.translateToLocalFormatted("kubatech.waila.eec.mob_type", mob));
            }
        } else {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "kubatech.waila.eec.mob_type",
                    StatCollector.translateToLocal("kubatech.waila.eec.no_mob")));
        }

        if (tag.hasKey("isInRitualMode") && tag.getBoolean("isInRitualMode")) {
            if (tag.hasKey("isRitualValid") && tag.getBoolean("isRitualValid")) {
                currentTip.add(
                    EnumChatFormatting.GREEN
                        + StatCollector.translateToLocal("kubatech.waila.eec.ritual_mode_connected"));
            } else {
                currentTip.add(
                    EnumChatFormatting.RED + StatCollector.translateToLocal("kubatech.waila.eec.ritual_mode_error"));
            }
        }
    }

    @Override
    public final boolean supportsBatchMode() {
        return true;
    }

    private static class EECFakePlayer extends FakePlayer {

        MTEExtremeEntityCrusher mte;
        ItemStack currentWeapon;

        public EECFakePlayer(MTEExtremeEntityCrusher mte) {
            super(
                (WorldServer) mte.getBaseMetaTileEntity()
                    .getWorld(),
                new GameProfile(
                    UUID.nameUUIDFromBytes("[EEC Fake Player]".getBytes(StandardCharsets.UTF_8)),
                    "[EEC Fake Player]"));
            this.mte = mte;
        }

        @Override
        public void renderBrokenItemStack(ItemStack p_70669_1_) {}

        @Override
        public Random getRNG() {
            return mte.rand;
        }

        @Override
        public void destroyCurrentEquippedItem() {}

        @Override
        public ItemStack getCurrentEquippedItem() {
            return currentWeapon;
        }

        @Override
        public ItemStack getHeldItem() {
            return currentWeapon;
        }
    }

    // mui2 start //
    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEExtremeEntityCrusherGui getGui() {
        return new MTEExtremeEntityCrusherGui(this);
    }

    protected boolean isAnimationEnabled() {
        return mAnimationEnabled;
    }

    protected void setAnimationEnabled(boolean mAnimationEnabled) {
        this.mAnimationEnabled = mAnimationEnabled;
    }

    protected boolean isProducingInfernalDrops() {
        return mIsProducingInfernalDrops;
    }

    protected void setIsProducingInfernalDrops(boolean mIsProducingInfernalDrops) {
        this.mIsProducingInfernalDrops = mIsProducingInfernalDrops;
    }

    protected boolean isVoidAllDamagedAndEnchantedItems() {
        return voidAllDamagedAndEnchantedItems;
    }

    protected void setVoidAllDamagedAndEnchantedItems(boolean voidAllDamagedAndEnchantedItems) {
        this.voidAllDamagedAndEnchantedItems = voidAllDamagedAndEnchantedItems;
    }

    protected boolean isPreserveWeapon() {
        return mPreserveWeapon;
    }

    protected void setPreserveWeapon(boolean mPreserveWeapon) {
        this.mPreserveWeapon = mPreserveWeapon;
    }

    protected boolean isCycleWeapons() {
        return mCycleWeapons;
    }

    protected void setCycleWeapons(boolean mCycleWeapons) {
        this.mCycleWeapons = mCycleWeapons;
    }

    protected boolean isInRitualMode() {
        return isInRitualMode;
    }

    protected void setIsInRitualMode(boolean isInRitualMode) {
        this.isInRitualMode = isInRitualMode;
        checkRitualConnection();
    }
}
