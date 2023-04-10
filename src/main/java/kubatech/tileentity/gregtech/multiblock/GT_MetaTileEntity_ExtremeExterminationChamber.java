/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.*;
import static kubatech.api.Variables.Author;
import static kubatech.api.Variables.StructureHologram;

import java.nio.charset.StandardCharsets;
import java.util.*;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.helpers.ReflectionHelper;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.api.network.CustomTileEntityPacket;
import kubatech.api.tileentity.CustomTileEntityPacketHandler;
import kubatech.api.utils.FastRandom;
import kubatech.api.utils.ItemID;
import kubatech.client.effect.EntityRenderer;
import kubatech.loaders.MobRecipeLoader;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.event.RitualRunEvent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.rituals.RitualEffectWellOfSuffering;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.google.common.collect.Multimap;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.EnderIO;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_ExtremeExterminationChamber
    extends KubaTechGTMultiBlockBase<GT_MetaTileEntity_ExtremeExterminationChamber>
    implements CustomTileEntityPacketHandler, ISurvivalConstructable {

    public static final HashMap<String, MobRecipeLoader.MobRecipe> MobNameToRecipeMap = new HashMap<>();
    public static final double DIAMOND_SPIKES_DAMAGE = 9d;
    // Powered spawner with octadic capacitor spawns ~22/min ~= 0.366/sec ~= 2.72s/spawn ~= 54.54t/spawn
    public static final int MOB_SPAWN_INTERVAL = 55;
    public final Random rand = new FastRandom();

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_ExtremeExterminationChamber(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ExtremeExterminationChamber(String aName) {
        super(aName);
        if (LoaderReference.BloodMagic) MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onRemoval() {
        if (LoaderReference.BloodMagic) MinecraftForge.EVENT_BUS.unregister(this);
        if (getBaseMetaTileEntity().isClientSide() && entityRenderer != null) {
            entityRenderer.setDead();
        }
    }

    private static final String WellOfSufferingRitualName = "AW013Suffering";

    private static final Item poweredSpawnerItem = Item.getItemFromBlock(EnderIO.blockPoweredSpawner);
    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_ExtremeExterminationChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_ExtremeExterminationChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                    { "fgggf", "g---g", "g---g", "g---g", "fgggf" }, { "fgggf", "g---g", "g---g", "g---g", "fgggf" },
                    { "fgggf", "g---g", "g---g", "g---g", "fgggf" }, { "fgggf", "g---g", "g---g", "g---g", "fgggf" },
                    { "fgggf", "gsssg", "gsssg", "gsssg", "fgggf" },
                    { "CC~CC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" }, }))
        .addElement('c', onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings2, 0)))
        .addElement(
            'C',
            buildHatchAdder(GT_MetaTileEntity_ExtremeExterminationChamber.class)
                .atLeast(InputBus, OutputBus, OutputHatch, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings2, 0))))
        .addElement(
            'g',
            LoaderReference.Bartworks
                ? BorosilicateGlass.ofBoroGlass((byte) 0, (t, v) -> t.mGlassTier = v, t -> t.mGlassTier)
                : onElementPass(t -> t.mGlassTier = 100, ofBlock(Blocks.glass, 0)))
        .addElement('f', ofFrame(Materials.Steel))
        .addElement(
            's',
            LoaderReference.ExtraUtilities ? ofBlock(Block.getBlockFromName("ExtraUtilities:spike_base_diamond"), 0)
                : isAir())
        .build();

    private TileEntity masterStoneRitual = null;
    private TileEntity tileAltar = null;
    private boolean isInRitualMode = false;
    private int mCasing = 0;
    private byte mGlassTier = 0;
    private boolean mAnimationEnabled = true;
    private boolean mIsProducingInfernalDrops = true;

    private EntityRenderer entityRenderer = null;
    private boolean renderEntity = false;
    private EECFakePlayer EECPlayer = null;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("isInRitualMode", isInRitualMode);
        aNBT.setBoolean("mAnimationEnabled", mAnimationEnabled);
        aNBT.setByte("mGlassTier", mGlassTier);
        aNBT.setBoolean("mIsProducingInfernalDrops", mIsProducingInfernalDrops);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        isInRitualMode = aNBT.getBoolean("isInRitualMode");
        mAnimationEnabled = !aNBT.hasKey("mAnimationEnabled") || aNBT.getBoolean("mAnimationEnabled");
        mGlassTier = aNBT.getByte("mGlassTier");
        mIsProducingInfernalDrops = !aNBT.hasKey("mIsProducingInfernalDrops")
            || aNBT.getBoolean("mIsProducingInfernalDrops");
    }

    @Override
    public boolean isOverclockingInfinite() {
        return true;
    }

    @Override
    protected int getOverclockTimeLimit() {
        return 20;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_ExtremeExterminationChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Powered Spawner")
            .addInfo("Controller block for Extreme Extermination Chamber")
            .addInfo(Author)
            .addInfo("Spawns and Exterminates monsters for you")
            .addInfo("You have to insert the powered spawner in controller")
            .addInfo("Base energy usage: 2,000 EU/t")
            .addInfo("Supports perfect OC, minimum time: 20 ticks, after that multiplies the outputs")
            .addInfo("Recipe time is based on mob health")
            .addInfo("You can additionally put a weapon to the ULV input bus")
            .addInfo("It will speed up the process and apply looting level from the weapon (maximum 4 levels)")
            .addInfo(EnumChatFormatting.RED + "Enchanting the spikes inside does nothing!")
            .addInfo("Also produces 120 Liquid XP per operation")
            .addInfo("If the mob spawns infernal")
            .addInfo("it will drain 8 times more power")
            .addInfo("You can prevent infernal spawns by shift clicking with a screwdriver")
            .addInfo("Note: If the mob has forced infernal spawn, it will do it anyway")
            .addInfo("You can enable ritual mode with a screwdriver")
            .addInfo("When in ritual mode and Well Of Suffering ritual is built directly on the machine in center")
            .addInfo("The mobs will start to buffer and die very slowly by a ritual")
            .addInfo("You can disable mob animation with a soldering iron")
            .addInfo(StructureHologram)
            .addSeparator()
            .beginStructureBlock(5, 7, 5, true)
            .addController("Front Bottom Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 10, false)
            .addOtherStructurePart("Borosilicate Glass", "All walls without corners")
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addOtherStructurePart("Steel Frame Box", "All vertical corners (except top and bottom)")
            .addOtherStructurePart("Diamond spikes", "Inside second layer")
            .addOutputBus("Any bottom casing", 1)
            .addOtherStructurePart(
                "1x ULV " + StatCollector.translateToLocal("GT5U.MBTT.InputBus"),
                "Any bottom casing",
                1)
            .addOutputHatch("Any bottom casing", 1)
            .addEnergyHatch("Any bottom casing", 1)
            .addMaintenanceHatch("Any bottom casing", 1)
            .toolTipFinisher(Tags.MODNAME);
        return tt;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 6, 0, elementBudget, env, true, true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ExtremeExterminationChamber(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
        boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
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
        if (message.getDataBoolean()) {
            renderEntity = true;
            String mobType = message.getDataString();
            MobRecipeLoader.MobRecipe r = MobNameToRecipeMap.get(mobType);
            if (r != null) {
                if (entityRenderer == null) setupEntityRenderer(getBaseMetaTileEntity(), 40);
                entityRenderer.setEntity(r.entity);
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
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.mMaxProgresstime > 0) {
            GT_Utility.sendChatToPlayer(aPlayer, "Can't change mode when running !");
            return;
        }
        if (aPlayer.isSneaking()) {
            if (!LoaderReference.InfernalMobs) return;
            mIsProducingInfernalDrops = !mIsProducingInfernalDrops;
            if (!mIsProducingInfernalDrops)
                GT_Utility.sendChatToPlayer(aPlayer, "Mobs will now be prevented from spawning infernal");
            else GT_Utility.sendChatToPlayer(aPlayer, "Mobs can spawn infernal now");
        } else {
            if (!LoaderReference.BloodMagic) return;
            isInRitualMode = !isInRitualMode;
            if (!isInRitualMode) {
                GT_Utility.sendChatToPlayer(aPlayer, "Ritual mode disabled");
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "Ritual mode enabled");
                if (connectToRitual()) GT_Utility.sendChatToPlayer(aPlayer, "Successfully connected to the ritual");
                else GT_Utility.sendChatToPlayer(aPlayer, "Can't connect to the ritual");
            }
        }
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        if (super.onSolderingToolRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ)) return true;
        mAnimationEnabled = !mAnimationEnabled;
        GT_Utility.sendChatToPlayer(aPlayer, "Animations are " + (mAnimationEnabled ? "enabled" : "disabled"));
        return true;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRitualPerform(RitualRunEvent event) {
        if (!isInRitualMode) return;
        if (masterStoneRitual == null) return;
        if (this.mMaxProgresstime == 0) return;
        if (event.mrs.equals(masterStoneRitual) && event.ritualKey.equals(WellOfSufferingRitualName)) {
            Rituals ritual = Rituals.ritualMap.get(WellOfSufferingRitualName);
            if (ritual != null && ritual.effect instanceof RitualEffectWellOfSuffering) {
                RitualEffectWellOfSuffering effect = (RitualEffectWellOfSuffering) ritual.effect;
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
                            ReflectionHelper.getField(effect, "offensaDrain", 3),
                            true) ? 2 : 1)
                        * (effect.canDrainReagent(
                            event.mrs,
                            ReagentRegistry.tenebraeReagent,
                            ReflectionHelper.getField(effect, "tennebraeDrain", 5),
                            true) ? 2 : 1),
                    true);

                SoulNetworkHandler.syphonFromNetwork(owner, effect.getCostPerRefresh() * 100);
            }
        }
    }

    private CustomTileEntityPacket mobPacket = null;

    private static class WeaponCache {

        boolean isValid = false;
        ItemID id = null;
        int looting = 0;
        double attackDamage = 0;
    }

    private final WeaponCache weaponCache = new WeaponCache();

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if (getBaseMetaTileEntity().isClientSide()) return false;
        if (aStack == null) return false;

        if (aStack.getItem() != poweredSpawnerItem) return false;

        if (aStack.getTagCompound() == null) return false;
        String mobType = aStack.getTagCompound()
            .getString("mobType");
        if (mobType.isEmpty()) return false;

        if (mobType.equals("Skeleton") && getBaseMetaTileEntity().getWorld().provider instanceof WorldProviderHell
            && rand.nextInt(5) > 0) mobType = "witherSkeleton";

        MobRecipeLoader.MobRecipe recipe = MobNameToRecipeMap.get(mobType);

        if (recipe == null) return false;
        if (!recipe.isPeacefulAllowed && this.getBaseMetaTileEntity()
            .getWorld().difficultySetting == EnumDifficulty.PEACEFUL) return false;

        if (isInRitualMode && isRitualValid()) {
            if (getMaxInputEu() < recipe.mEUt / 4) return false;
            this.mOutputFluids = new FluidStack[] { FluidRegistry.getFluidStack("xpjuice", 5000) };
            this.mOutputItems = recipe.generateOutputs(rand, this, 3, 0, mIsProducingInfernalDrops);
            this.lEUt /= 4L;
            this.mMaxProgresstime = 400;
        } else {
            if (getMaxInputEu() < recipe.mEUt) return false;

            double attackDamage = DIAMOND_SPIKES_DAMAGE; // damage from spikes
            GT_MetaTileEntity_Hatch_InputBus inputbus = this.mInputBusses.size() == 0 ? null : this.mInputBusses.get(0);
            if (inputbus != null && !isValidMetaTileEntity(inputbus)) inputbus = null;
            ItemStack lootingHolder = inputbus == null ? null : inputbus.getStackInSlot(0);
            weaponCheck: {
                // noinspection EqualsBetweenInconvertibleTypes
                if (weaponCache.isValid && weaponCache.id.equals(lootingHolder)) break weaponCheck;
                if (lootingHolder == null || !Enchantment.looting.canApply(lootingHolder)) {
                    weaponCache.isValid = false;
                    break weaponCheck;
                }
                try {
                    // noinspection unchecked
                    weaponCache.attackDamage = ((Multimap<String, AttributeModifier>) lootingHolder
                        .getAttributeModifiers())
                            .get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())
                            .stream()
                            .mapToDouble(
                                attr -> attr.getAmount() + (double) EnchantmentHelper
                                    .func_152377_a(lootingHolder, EnumCreatureAttribute.UNDEFINED))
                            .sum();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                weaponCache.isValid = true;
                weaponCache.looting = Math
                    .min(4, EnchantmentHelper.getEnchantmentLevel(Enchantment.looting.effectId, lootingHolder));
                weaponCache.id = ItemID.create_NoCopy(lootingHolder, true, true);
            }
            if (weaponCache.isValid) attackDamage += weaponCache.attackDamage;

            this.mOutputItems = recipe.generateOutputs(
                rand,
                this,
                attackDamage,
                weaponCache.isValid ? weaponCache.looting : 0,
                mIsProducingInfernalDrops);
            this.mOutputFluids = new FluidStack[] { FluidRegistry.getFluidStack("xpjuice", 120) };
            int times = this.calculatePerfectOverclock(this.lEUt, this.mMaxProgresstime);
            // noinspection ConstantConditions
            if (weaponCache.isValid && lootingHolder.isItemStackDamageable()) {
                if (EECPlayer == null) EECPlayer = new EECFakePlayer(this);
                EECPlayer.currentWeapon = lootingHolder;
                Item lootingHolderItem = lootingHolder.getItem();
                for (int i = 0; i < times + 1; i++) {
                    // noinspection ConstantConditions
                    if (!lootingHolderItem.hitEntity(lootingHolder, recipe.entity, EECPlayer)) break;
                    if (lootingHolder.stackSize == 0) {
                        // noinspection ConstantConditions
                        inputbus.setInventorySlotContents(0, null);
                        break;
                    }
                }
                EECPlayer.currentWeapon = null;
            }
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
        return true;
    }

    private boolean isRitualValid() {
        if (!isInRitualMode) return false;
        if (masterStoneRitual == null) return false;
        if (masterStoneRitual.isInvalid() || !(((TEMasterStone) masterStoneRitual).getCurrentRitual()
            .equals(WellOfSufferingRitualName))) {
            masterStoneRitual = null;
            return false;
        }
        return true;
    }

    private boolean connectToRitual() {
        if (!LoaderReference.BloodMagic) return false;
        ChunkCoordinates coords = this.getBaseMetaTileEntity()
            .getCoords();
        int[] abc = new int[] { 0, -8, 2 };
        int[] xyz = new int[] { 0, 0, 0 };
        this.getExtendedFacing()
            .getWorldOffset(abc, xyz);
        xyz[0] += coords.posX;
        xyz[1] += coords.posY;
        xyz[2] += coords.posZ;
        TileEntity te = this.getBaseMetaTileEntity()
            .getTileEntity(xyz[0], xyz[1], xyz[2]);
        if (te instanceof TEMasterStone) {
            if (((TEMasterStone) te).getCurrentRitual()
                .equals(WellOfSufferingRitualName)) {
                masterStoneRitual = te;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mGlassTier = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 6, 0)) return false;
        if (mCasing < 10 || mMaintenanceHatches.size() != 1
            || mEnergyHatches.size() == 0
            || !(mInputBusses.size() == 0 || (mInputBusses.size() == 1 && mInputBusses.get(0).mTier == 0)))
            return false;
        if (mGlassTier < 8)
            for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches) if (hatch.mTier > mGlassTier) return false;
        if (isInRitualMode) connectToRitual();
        return true;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Animations: " + EnumChatFormatting.YELLOW + (mAnimationEnabled ? "Enabled" : "Disabled"));
        info.add(
            "Is allowed to produce infernal drops: " + EnumChatFormatting.YELLOW
                + (mIsProducingInfernalDrops ? "Yes" : "No"));
        info.add("Is in ritual mode: " + EnumChatFormatting.YELLOW + (isInRitualMode ? "Yes" : "No"));
        if (isInRitualMode) info.add(
            "Is connected to ritual: "
                + (isRitualValid() ? EnumChatFormatting.GREEN + "Yes" : EnumChatFormatting.RED + "No"));
        else {
            info.add("Inserted weapon: " + EnumChatFormatting.YELLOW + (weaponCache.isValid ? "Yes" : "No"));
            if (weaponCache.isValid) {
                info.add("Weapon attack damage: " + EnumChatFormatting.YELLOW + weaponCache.attackDamage);
                info.add("Weapon looting level: " + EnumChatFormatting.YELLOW + weaponCache.looting);
                info.add(
                    "Total attack damage: " + EnumChatFormatting.YELLOW
                        + (DIAMOND_SPIKES_DAMAGE + weaponCache.attackDamage));
            } else info.add("Total attack damage: " + EnumChatFormatting.YELLOW + DIAMOND_SPIKES_DAMAGE);
        }
        return info.toArray(new String[0]);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.bindPlayerInventory(
            buildContext.getPlayer(),
            new Pos2d(7, 83),
            this.getGUITextureSet()
                .getItemSlot());
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(143, 75)
                .setEnabled(widget -> !isFixed.apply(widget)));
        final SlotWidget inventorySlot = new SlotWidget(inventoryHandler, 1)
            .setFilter(stack -> stack.getItem() == poweredSpawnerItem);

        DynamicPositionedColumn configurationElements = new DynamicPositionedColumn();
        addConfigurationWidgets(configurationElements, buildContext, inventorySlot);

        builder.widget(
            new DynamicPositionedColumn().setSynced(false)
                .widget(inventorySlot)
                .widget(new CycleButtonWidget().setToggle(() -> getBaseMetaTileEntity().isAllowedToWork(), works -> {
                    if (works) getBaseMetaTileEntity().enableWorking();
                    else getBaseMetaTileEntity().disableWorking();

                    if (!(buildContext.getPlayer() instanceof EntityPlayerMP)) return;
                    String tChat = GT_Utility.trans("090", "Machine Processing: ")
                        + (works ? GT_Utility.trans("088", "Enabled") : GT_Utility.trans("087", "Disabled"));
                    if (hasAlternativeModeText()) tChat = getAlternativeModeText();
                    GT_Utility.sendChatToPlayer(buildContext.getPlayer(), tChat);
                })
                    .addTooltip(0, new Text("Disabled").color(Color.RED.dark(3)))
                    .addTooltip(1, new Text("Enabled").color(Color.GREEN.dark(3)))
                    .setVariableBackgroundGetter(toggleButtonBackgroundGetter)
                    .setSize(18, 18)
                    .addTooltip("Working status"))
                .widget(configurationElements.setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                .widget(
                    new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CROSS)
                        .setSize(18, 18)
                        .addTooltip(new Text("Please stop the machine to configure it").color(Color.RED.dark(3)))
                        .setEnabled(widget -> getBaseMetaTileEntity().isActive()))
                .setPos(151, 4));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(screenElements);
    }

    private void addConfigurationWidgets(DynamicPositionedColumn configurationElements, UIBuildContext buildContext,
        SlotWidget inventorySlot) {
        configurationElements.setSynced(false);
        configurationElements.widget(new CycleButtonWidget().setToggle(() -> isInRitualMode, v -> {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Can't change mode when running !");
                return;
            }

            isInRitualMode = v;

            if (!(buildContext.getPlayer() instanceof EntityPlayerMP)) return;
            if (!isInRitualMode) {
                GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Ritual mode disabled");
            } else {
                GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Ritual mode enabled");
                if (connectToRitual())
                    GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Successfully connected to the ritual");
                else GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Can't connect to the ritual");
            }
        })
            .setVariableBackgroundGetter(toggleButtonBackgroundGetter)
            .setSize(18, 18)
            .addTooltip("Ritual mode"));
        configurationElements.widget(new CycleButtonWidget().setToggle(() -> mIsProducingInfernalDrops, v -> {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Can't change mode when running !");
                return;
            }

            mIsProducingInfernalDrops = v;

            if (!(buildContext.getPlayer() instanceof EntityPlayerMP)) return;
            if (!mIsProducingInfernalDrops) GT_Utility
                .sendChatToPlayer(buildContext.getPlayer(), "Mobs will now be prevented from spawning infernal");
            else GT_Utility.sendChatToPlayer(buildContext.getPlayer(), "Mobs can spawn infernal now");
        })
            .setVariableBackgroundGetter(toggleButtonBackgroundGetter)
            .setSize(18, 18)
            .addTooltip("Is allowed to spawn infernal mobs")
            .addTooltip(new Text("Does not affect mobs that are always infernal !").color(Color.GRAY.normal)));
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(10, 7);

        screenElements.widget(
            new DynamicPositionedRow().setSynced(false)
                .widget(new TextWidget("Status: ").setDefaultColor(COLOR_TEXT_GRAY.get()))
                .widget(new DynamicTextWidget(() -> {
                    if (getBaseMetaTileEntity().isActive()) return new Text("Working !").color(Color.GREEN.dark(3));
                    else if (getBaseMetaTileEntity().isAllowedToWork())
                        return new Text("Enabled").color(Color.GREEN.dark(3));
                    else if (getBaseMetaTileEntity().wasShutdown())
                        return new Text("Shutdown (CRITICAL)").color(Color.RED.dark(3));
                    else return new Text("Disabled").color(Color.RED.dark(3));
                }))
                .setEnabled(isFixed));
        screenElements.widget(new DynamicTextWidget(() -> {
            ItemStack aStack = mInventory[1];
            if (aStack == null) return new Text("Insert Powered Spawner").color(Color.RED.dark(3));
            else {
                Text invalid = new Text("Invalid Spawner").color(Color.RED.dark(3));
                if (aStack.getItem() != poweredSpawnerItem) return invalid;

                if (aStack.getTagCompound() == null) return invalid;
                String mobType = aStack.getTagCompound()
                    .getString("mobType");
                if (mobType.isEmpty()) return invalid;

                if (!MobNameToRecipeMap.containsKey(mobType)) return invalid;

                return new Text(StatCollector.translateToLocal("entity." + mobType + ".name"))
                    .color(Color.GREEN.dark(3));
            }
        }).setEnabled(isFixed));

        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("132", "Pipe is loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mWrench))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("133", "Screws are loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mScrewdriver))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("134", "Something is stuck.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSoftHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftHammer, val -> mSoftHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("135", "Platings are dented.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mHardHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("136", "Circuitry burned out.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSolderingTool))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("137", "That doesn't belong there."))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mCrowbar))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));
    }

    private static class EECFakePlayer extends FakePlayer {

        GT_MetaTileEntity_ExtremeExterminationChamber mte;
        ItemStack currentWeapon;

        public EECFakePlayer(GT_MetaTileEntity_ExtremeExterminationChamber mte) {
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
    }
}
