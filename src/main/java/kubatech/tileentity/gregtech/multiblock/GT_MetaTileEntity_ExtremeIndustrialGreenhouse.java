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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static kubatech.api.Variables.Author;
import static kubatech.api.Variables.StructureHologram;
import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.shapes.Rectangle;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_Item_Ores;
import gregtech.common.blocks.GT_TileEntity_Ores;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import kubatech.Tags;
import kubatech.api.DynamicInventory;
import kubatech.api.LoaderReference;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.client.effect.CropRenderer;

@SuppressWarnings("unused")
public class GT_MetaTileEntity_ExtremeIndustrialGreenhouse
    extends KubaTechGTMultiBlockBase<GT_MetaTileEntity_ExtremeIndustrialGreenhouse> {

    private static final boolean debug = false;
    private static final int EIG_MATH_VERSION = 0;
    private static final int CONFIGURATION_WINDOW_ID = 999;

    public final List<GreenHouseSlot> mStorage = new ArrayList<>();
    private int oldVersion = 0;
    private int mCasing = 0;
    public int mMaxSlots = 0;
    private int setupphase = 1;
    private boolean isIC2Mode = false;
    private byte glasTier = 0;
    private int waterusage = 0;
    private int weedexusage = 0;
    private boolean isNoHumidity = false;
    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final Item forestryfertilizer = GameRegistry.findItem("Forestry", "fertilizerCompound");
    private static final Fluid weedex = Materials.WeedEX9000.mFluid;
    private static final IStructureDefinition<GT_MetaTileEntity_ExtremeIndustrialGreenhouse> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_ExtremeIndustrialGreenhouse>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                    { "ccccc", "clllc", "clllc", "clllc", "ccccc" },
                    { "ggggg", "g---g", "g---g", "g---g", "ggggg" },
                    { "ggggg", "g---g", "g---g", "g---g", "ggggg" },
                    { "ccccc", "cdddc", "cdwdc", "cdddc", "ccccc" },
                    { "cc~cc", "cCCCc", "cCCCc", "cCCCc", "ccccc" },
                })) // spotless:on
        .addElement(
            'c',
            ofChain(
                onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)),
                ofHatchAdder(
                    GT_MetaTileEntity_ExtremeIndustrialGreenhouse::addEnergyInputToMachineList,
                    CASING_INDEX,
                    1),
                ofHatchAdder(
                    GT_MetaTileEntity_ExtremeIndustrialGreenhouse::addMaintenanceToMachineList,
                    CASING_INDEX,
                    1),
                ofHatchAdder(GT_MetaTileEntity_ExtremeIndustrialGreenhouse::addInputToMachineList, CASING_INDEX, 1),
                ofHatchAdder(GT_MetaTileEntity_ExtremeIndustrialGreenhouse::addOutputToMachineList, CASING_INDEX, 1)))
        .addElement('C', onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)))
        .addElement(
            'l',
            LoaderReference.ProjRedIllumination
                ? ofBlock(Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"), 10)
                : ofChain(ofBlock(Blocks.redstone_lamp, 0), ofBlock(Blocks.lit_redstone_lamp, 0)))
        .addElement(
            'g',
            LoaderReference.Bartworks
                ? BorosilicateGlass
                    .ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glasTier = t, te -> te.glasTier)
                : onElementPass(t -> t.glasTier = 100, ofBlock(Blocks.glass, 0)))
        .addElement(
            'd',
            ofBlock(
                LoaderReference.RandomThings ? Block.getBlockFromName("RandomThings:fertilizedDirt_tilled")
                    : Blocks.farmland,
                0))
        .addElement(
            'w',
            ofChain(ofBlock(Blocks.water, 0), ofBlock(BlocksItems.getFluidBlock(InternalName.fluidDistilledWater), 0)))
        .build();

    public GT_MetaTileEntity_ExtremeIndustrialGreenhouse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ExtremeIndustrialGreenhouse(String aName) {
        super(aName);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (getBaseMetaTileEntity().isServerSide()) tryOutputAll(mStorage, s -> {
            ArrayList<ItemStack> l = new ArrayList<>(2);
            l.add(((GreenHouseSlot) s).input.copy());
            if (((GreenHouseSlot) s).undercrop != null) l.add(((GreenHouseSlot) s).undercrop.copy());
            return l;
        });
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "You can't change IC2 mode if the machine is working!");
                return;
            }
            if (!mStorage.isEmpty()) {
                GT_Utility.sendChatToPlayer(aPlayer, "You can't change IC2 mode if there are seeds inside!");
                return;
            }
            this.isIC2Mode = !this.isIC2Mode;
            GT_Utility.sendChatToPlayer(aPlayer, "IC2 mode is now " + (this.isIC2Mode ? "enabled" : "disabled."));
        } else {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "You can't enable/disable setup if the machine is working!");
                return;
            }
            this.setupphase++;
            if (this.setupphase == 3) this.setupphase = 0;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                "EIG is now running in " + (this.setupphase == 1 ? "setup mode (input)."
                    : (this.setupphase == 2 ? "setup mode (output)." : "normal operation.")));
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        isNoHumidity = !isNoHumidity;
        GT_Utility.sendChatToPlayer(aPlayer, "Give incoming crops no humidity " + isNoHumidity);
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_ExtremeIndustrialGreenhouse(this.mName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_ExtremeIndustrialGreenhouse> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Crop Farm")
            .addInfo("Controller block for the Extreme Industrial Greenhouse")
            .addInfo(Author)
            .addInfo("Grow your crops like a chad !")
            .addInfo("Use screwdriver to enable/change/disable setup mode")
            .addInfo("Use screwdriver while sneaking to enable/disable IC2 mode")
            .addInfo("Use wire cutters to give incoming IC2 crops 0 humidity")
            .addInfo("Uses 1000L of water per crop per operation")
            .addInfo("If there are >= 1000 crops -> Uses 1L of Weed-EX 9000 per crop per second")
            .addInfo("Otherwise, around 1% of crops will die each operation")
            .addInfo("You can insert fertilizer each operation to get more drops (max +400%)")
            .addInfo("-------------------- SETUP   MODE --------------------")
            .addInfo("Does not take power")
            .addInfo("There are two modes: input / output")
            .addInfo("Input mode: machine will take seeds from input bus and plant them")
            .addInfo("[IC2] You need to also input block that is required under the crop")
            .addInfo("Output mode: machine will take planted seeds and output them")
            .addInfo("-------------------- NORMAL CROPS --------------------")
            .addInfo("Minimal tier: " + voltageTooltipFormatted(4))
            .addInfo("Starting with 1 slot")
            .addInfo("Every slot gives 64 crops")
            .addInfo("Every tier past " + voltageTooltipFormatted(4) + ", slots are multiplied by 2")
            .addInfo("Base process time: 5 sec")
            .addInfo(
                "Process time is divided by number of tiers past " + voltageTooltipFormatted(3) + " (Minimum 1 sec)")
            .addInfo("All crops are grown at the end of the operation")
            .addInfo("Will automatically craft seeds if they are not dropped")
            .addInfo("1 Fertilizer per 1 crop +200%")
            .addInfo("-------------------- IC2    CROPS --------------------")
            .addInfo("Minimal tier: " + voltageTooltipFormatted(6))
            .addInfo("Need " + voltageTooltipFormatted(6) + " glass tier")
            .addInfo("Starting with 4 slots")
            .addInfo("Every slot gives 1 crop")
            .addInfo("Every tier past " + voltageTooltipFormatted(6) + ", slots are multiplied by 4")
            .addInfo("Process time: 5 sec")
            .addInfo("All crops are accelerated by x32 times")
            .addInfo("1 Fertilizer per 1 crop +10%")
            .addInfo(StructureHologram)
            .addSeparator()
            .beginStructureBlock(5, 6, 5, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Clean Stainless Steel Casings", 70, false)
            .addOtherStructurePart("Borosilicate Glass", "Hollow two middle layers")
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addStructureInfo("The dirt is from RandomThings, must be tilled")
            .addStructureInfo("Regular water and IC2 Distilled Water are accepted")
            .addStructureInfo("Purple lamps are from ProjectRedIllumination. They can be powered and/or inverted")
            .addMaintenanceHatch("Any casing (Except inner bottom ones)", 1)
            .addInputBus("Any casing (Except inner bottom ones)", 1)
            .addOutputBus("Any casing (Except inner bottom ones)", 1)
            .addInputHatch("Any casing (Except inner bottom ones)", 1)
            .addEnergyHatch("Any casing (Except inner bottom ones)", 1)
            .toolTipFinisher(Tags.MODNAME);
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        List<String> info = new ArrayList<>(Arrays.asList(super.getStructureDescription(stackSize)));
        info.add("The dirt is from RandomThings, must be tilled");
        info.add("Purple lamps are from ProjectRedIllumination. They can be powered and/or inverted");
        return info.toArray(new String[] {});
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("EIG_MATH_VERSION", EIG_MATH_VERSION);
        aNBT.setByte("glasTier", glasTier);
        aNBT.setInteger("setupphase", setupphase);
        aNBT.setBoolean("isIC2Mode", isIC2Mode);
        aNBT.setBoolean("isNoHumidity", isNoHumidity);
        aNBT.setInteger("mStorageSize", mStorage.size());
        for (int i = 0; i < mStorage.size(); i++) aNBT.setTag(
            "mStorage." + i,
            mStorage.get(i)
                .toNBTTagCompound());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        oldVersion = aNBT.hasKey("EIG_MATH_VERSION") ? aNBT.getInteger("EIG_MATH_VERSION") : -1;
        glasTier = aNBT.getByte("glasTier");
        setupphase = aNBT.getInteger("setupphase");
        isIC2Mode = aNBT.getBoolean("isIC2Mode");
        isNoHumidity = aNBT.getBoolean("isNoHumidity");
        for (int i = 0; i < aNBT.getInteger("mStorageSize"); i++)
            mStorage.add(new GreenHouseSlot(aNBT.getCompoundTag("mStorage." + i)));
    }

    @SideOnly(Side.CLIENT)
    public void spawnVisualCrops(World world, int x, int y, int z, int age) {
        CropRenderer crop = new CropRenderer(world, x, y, z, age);
        Minecraft.getMinecraft().effectRenderer.addEffect(crop);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
            if (aBaseMetaTileEntity.isActive() && aTick % 40 == 0) {
                int[] abc = new int[] { 0, -2, 2 };
                int[] xyz = new int[] { 0, 0, 0 };
                this.getExtendedFacing()
                    .getWorldOffset(abc, xyz);
                xyz[0] += aBaseMetaTileEntity.getXCoord();
                xyz[1] += aBaseMetaTileEntity.getYCoord();
                xyz[2] += aBaseMetaTileEntity.getZCoord();
                spawnVisualCrops(aBaseMetaTileEntity.getWorld(), xyz[0], xyz[1], xyz[2], 40);
            }
        }
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 5, 0);
    }

    private void updateMaxSlots() {
        int tier = getVoltageTier();
        if (tier < (isIC2Mode ? 6 : 4)) mMaxSlots = 0;
        else if (isIC2Mode) mMaxSlots = 4 << (2 * (tier - 6));
        else mMaxSlots = 1 << (tier - 4);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        int tier = getVoltageTier();
        updateMaxSlots();

        if (oldVersion != EIG_MATH_VERSION) {
            for (GreenHouseSlot slot : mStorage) slot.recalculate(this, getBaseMetaTileEntity().getWorld());
            oldVersion = EIG_MATH_VERSION;
        }

        if (setupphase > 0) {
            if ((mStorage.size() >= mMaxSlots && setupphase == 1) || (mStorage.size() == 0 && setupphase == 2))
                return CheckRecipeResultRegistry.NO_RECIPE;

            if (setupphase == 1) {
                List<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) {
                    addCrop(input);
                    if (mStorage.size() >= mMaxSlots) break;
                }
            } else if (setupphase == 2) {
                tryOutputAll(mStorage, s -> {
                    ArrayList<ItemStack> l = new ArrayList<>(2);
                    l.add(((GreenHouseSlot) s).input.copy());
                    if (((GreenHouseSlot) s).undercrop != null) l.add(((GreenHouseSlot) s).undercrop.copy());
                    return l;
                });
            }

            this.updateSlots();
            this.mMaxProgresstime = 5;
            this.lEUt = 0;
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        if (mStorage.size() > mMaxSlots) return SimpleCheckRecipeResult.ofFailure("EIG_slotoverflow");
        if (mStorage.isEmpty()) return CheckRecipeResultRegistry.NO_RECIPE;

        waterusage = 0;
        weedexusage = 0;
        for (GreenHouseSlot s : mStorage) waterusage += s.input.stackSize;
        if (waterusage >= 1000) weedexusage = waterusage;
        waterusage *= 1000;

        List<GT_MetaTileEntity_Hatch_Input> fluids = mInputHatches;
        List<GT_MetaTileEntity_Hatch_Input> fluidsToUse = new ArrayList<>(fluids.size());
        int watercheck = waterusage;
        FluidStack waterStack = new FluidStack(FluidRegistry.WATER, 1);
        for (GT_MetaTileEntity_Hatch_Input i : fluids) {
            if (!i.isValid()) continue;
            if (i instanceof GT_MetaTileEntity_Hatch_MultiInput) {
                int amount = ((GT_MetaTileEntity_Hatch_MultiInput) i).getFluidAmount(waterStack);
                if (amount == 0) continue;
                watercheck -= amount;
            } else {
                FluidStack stack = i.getDrainableStack();
                if (stack == null) continue;
                if (!stack.isFluidEqual(waterStack)) continue;
                if (stack.amount <= 0) continue;
                watercheck -= stack.amount;
            }
            fluidsToUse.add(i);
            if (watercheck <= 0) break;
        }
        if (watercheck > 0 && !debug) return SimpleCheckRecipeResult.ofFailure("EIG_missingwater");
        watercheck = waterusage;
        for (GT_MetaTileEntity_Hatch_Input i : fluidsToUse) {
            int used = i.drain(watercheck, true).amount;
            watercheck -= used;
        }

        // weedex
        if (weedexusage > 0 && !this.depleteInput(new FluidStack(weedex, isIC2Mode ? weedexusage * 5 : weedexusage))) {
            IGregTechTileEntity baseMTE = this.getBaseMetaTileEntity();
            int toKill = baseMTE.getRandomNumber((int) ((double) weedexusage * 0.02d) + 1);
            while (toKill > 0) {
                GreenHouseSlot removed = mStorage.remove(baseMTE.getRandomNumber(mStorage.size()));
                toKill -= removed.input.stackSize;
            }
        }

        // OVERCLOCK
        // FERTILIZER IDEA:
        // IC2 +10% per fertilizer per crop per operation
        // NORMAL +200% per fertilizer per crop per operation

        int boost = 0;
        int maxboost = 0;
        for (GreenHouseSlot s : mStorage) maxboost += s.input.stackSize * (isIC2Mode ? 40 : 2);

        ArrayList<ItemStack> inputs = getStoredInputs();
        for (ItemStack i : inputs) {
            if ((i.getItem() == Items.dye && i.getItemDamage() == 15)
                || (forestryfertilizer != null && (i.getItem() == forestryfertilizer))
                || (GT_Utility.areStacksEqual(i, Ic2Items.fertilizer))) {
                int used = Math.min(i.stackSize, maxboost - boost);
                i.stackSize -= used;
                boost += used;
            }
            if (boost == maxboost) break;
        }

        double multiplier = 1.d + (((double) boost / (double) maxboost) * 4d);

        if (isIC2Mode) {
            if (glasTier < 6) return SimpleCheckRecipeResult.ofFailure("EIG_ic2glass");
            this.mMaxProgresstime = 100;
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++) outputs.addAll(
                mStorage.get(i)
                    .getIC2Drops(this, ((double) this.mMaxProgresstime * 32d) * multiplier));
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        } else {
            this.mMaxProgresstime = Math.max(20, 100 / (tier - 3)); // Min 1 s
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++) {
                for (ItemStack drop : mStorage.get(i)
                    .getDrops()) {
                    ItemStack s = drop.copy();
                    s.stackSize = (int) ((double) s.stackSize * multiplier);
                    outputs.add(s);
                }
            }
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        }
        this.lEUt = -(int) ((double) GT_Values.V[tier] * 0.99d);
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        mCasing = 0;
        glasTier = 0;
        if (debug) glasTier = 8;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0)) return false;

        if (this.glasTier < 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.glasTier < hatchEnergy.mTier) return false;

        boolean valid = this.mMaintenanceHatches.size() == 1 && this.mEnergyHatches.size() >= 1 && this.mCasing >= 70;

        if (valid) updateMaxSlots();

        return valid;
    }

    private static final UIInfo<?, ?> GreenhouseUI = createKTMetaTileEntityUI(
        KT_ModulaUIContainer_ExtremeIndustrialGreenhouse::new);

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        GreenhouseUI.open(
            aPlayer,
            aBaseMetaTileEntity.getWorld(),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord());
        return true;
    }

    private static class KT_ModulaUIContainer_ExtremeIndustrialGreenhouse extends ModularUIContainer {

        final WeakReference<GT_MetaTileEntity_ExtremeIndustrialGreenhouse> parent;

        public KT_ModulaUIContainer_ExtremeIndustrialGreenhouse(ModularUIContext context, ModularWindow mainWindow,
            GT_MetaTileEntity_ExtremeIndustrialGreenhouse mte) {
            super(context, mainWindow);
            parent = new WeakReference<>(mte);
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
            if (!(aPlayer instanceof EntityPlayerMP)) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final Slot s = getSlot(aSlotIndex);
            if (s == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (aSlotIndex >= 36) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final ItemStack aStack = s.getStack();
            if (aStack == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            GT_MetaTileEntity_ExtremeIndustrialGreenhouse mte = parent.get();
            if (mte == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (mte.mStorage.size() >= mte.mMaxSlots) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (mte.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(aPlayer, EnumChatFormatting.RED + "Can't insert while running !");
                return super.transferStackInSlot(aPlayer, aSlotIndex);
            }
            if (mte.addCrop(aStack) != null) {
                if (aStack.stackSize == 0) s.putStack(null);
                else s.putStack(aStack);
                detectAndSendChanges();
                return null;
            }
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        }
    }

    @Override
    protected void addConfigurationWidgets(DynamicPositionedColumn configurationElements, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(CONFIGURATION_WINDOW_ID, this::createConfigurationWindow);
        configurationElements.setSynced(false);
        configurationElements.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(CONFIGURATION_WINDOW_ID);
                })
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .addTooltip("Configuration")
                .setSize(16, 16));
    }

    DynamicInventory<GreenHouseSlot> dynamicInventory = new DynamicInventory<>(
        128,
        60,
        () -> mMaxSlots,
        mStorage,
        s -> s.input).allowInventoryInjection(this::addCrop)
            .allowInventoryExtraction(mStorage::remove)
            .allowInventoryReplace((i, stack) -> {
                if (!isIC2Mode) {
                    GreenHouseSlot slot = mStorage.get(i);
                    if (GT_Utility.areStacksEqual(stack, slot.input)) {
                        if (slot.input.stackSize < 64) {
                            slot.addAll(
                                this.getBaseMetaTileEntity()
                                    .getWorld(),
                                stack);
                            return stack;
                        }
                        return null;
                    }
                    if (!addCrop(stack, i, true)) return null;
                    slot = mStorage.remove(i);
                    addCrop(stack, i, false);
                    return slot.input;
                } else {
                    if (stack.stackSize != 1) return null;
                    if (!addCrop(stack, i, true)) return null;
                    GreenHouseSlot slot = mStorage.remove(i);
                    addCrop(stack, i, false);
                    return slot.input;
                }
            })
            .setEnabled(() -> this.mMaxProgresstime == 0);

    @Override
    public void createInventorySlots() {

    }

    private boolean isInInventory = true;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        isInInventory = !getBaseMetaTileEntity().isActive();
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(4, 4)
                .setSize(190, 85)
                .setEnabled(w -> !isInInventory));
        builder.widget(
            dynamicInventory.asWidget(builder, buildContext)
                .setPos(10, 16)
                .setBackground(new Rectangle().setColor(Color.rgb(163, 163, 198)))
                .setEnabled(w -> isInInventory));
        builder.widget(
            new CycleButtonWidget().setToggle(() -> isInInventory, i -> isInInventory = i)
                .setTextureGetter(i -> i == 0 ? new Text("Inventory") : new Text("Status"))
                .setBackground(GT_UITextures.BUTTON_STANDARD)
                .setPos(140, 4)
                .setSize(55, 16));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, null);
        builder.widget(screenElements.setEnabled(w -> !isInInventory));

        builder.widget(createPowerSwitchButton(builder))
            .widget(createVoidExcessButton(builder))
            .widget(createInputSeparationButton(builder))
            .widget(createBatchModeButton(builder))
            .widget(createLockToSingleRecipeButton(builder));

        DynamicPositionedColumn configurationElements = new DynamicPositionedColumn();
        addConfigurationWidgets(configurationElements, buildContext);

        builder.widget(
            configurationElements.setAlignment(MainAxisAlignment.END)
                .setPos(getPowerSwitchButtonPos().subtract(0, 18)));
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 100);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget("Configuration").setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(185, 3))
            .widget(
                new Column().widget(
                    new CycleButtonWidget().setLength(3)
                        .setGetter(() -> setupphase)
                        .setSetter(val -> {
                            if (!(player instanceof EntityPlayerMP)) return;
                            if (this.mMaxProgresstime > 0) {
                                GT_Utility.sendChatToPlayer(
                                    player,
                                    "You can't enable/disable setup if the machine is working!");
                                return;
                            }
                            this.setupphase = val;
                            GT_Utility.sendChatToPlayer(
                                player,
                                "EIG is now running in " + (this.setupphase == 1 ? "setup mode (input)."
                                    : (this.setupphase == 2 ? "setup mode (output)." : "normal operation.")));
                        })
                        .addTooltip(0, new Text("Operating").color(Color.GREEN.dark(3)))
                        .addTooltip(1, new Text("Input").color(Color.YELLOW.dark(3)))
                        .addTooltip(2, new Text("Output").color(Color.YELLOW.dark(3)))
                        .setTextureGetter(
                            i -> i == 0 ? new Text("Operating").color(Color.GREEN.dark(3))
                                .withFixedSize(70 - 18, 18, 15, 0)
                                : i == 1 ? new Text("Input").color(Color.YELLOW.dark(3))
                                    .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text("Output").color(Color.YELLOW.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                        .setBackground(
                            ModularUITextures.VANILLA_BACKGROUND,
                            GT_UITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                        .setSize(70, 18)
                        .addTooltip("Setup mode"))
                    .widget(
                        new CycleButtonWidget().setLength(2)
                            .setGetter(() -> isIC2Mode ? 1 : 0)
                            .setSetter(val -> {
                                if (!(player instanceof EntityPlayerMP)) return;
                                if (this.mMaxProgresstime > 0) {
                                    GT_Utility.sendChatToPlayer(
                                        player,
                                        "You can't change IC2 mode if the machine is working!");
                                    return;
                                }
                                if (!mStorage.isEmpty()) {
                                    GT_Utility.sendChatToPlayer(
                                        player,
                                        "You can't change IC2 mode if there are seeds inside!");
                                    return;
                                }
                                this.isIC2Mode = val == 1;
                                GT_Utility.sendChatToPlayer(
                                    player,
                                    "IC2 mode is now " + (this.isIC2Mode ? "enabled" : "disabled."));
                            })
                            .addTooltip(0, new Text("Disabled").color(Color.RED.dark(3)))
                            .addTooltip(1, new Text("Enabled").color(Color.GREEN.dark(3)))
                            .setTextureGetter(
                                i -> i == 0 ? new Text("Disabled").color(Color.RED.dark(3))
                                    .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text("Enabled").color(Color.GREEN.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                            .setBackground(
                                ModularUITextures.VANILLA_BACKGROUND,
                                GT_UITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                            .setSize(70, 18)
                            .addTooltip("IC2 mode"))
                    .widget(
                        new CycleButtonWidget().setLength(2)
                            .setGetter(() -> isNoHumidity ? 1 : 0)
                            .setSetter(val -> {
                                if (!(player instanceof EntityPlayerMP)) return;
                                isNoHumidity = val == 1;
                                GT_Utility.sendChatToPlayer(player, "Give incoming crops no humidity " + isNoHumidity);
                            })
                            .addTooltip(0, new Text("Disabled").color(Color.RED.dark(3)))
                            .addTooltip(1, new Text("Enabled").color(Color.GREEN.dark(3)))
                            .setTextureGetter(
                                i -> i == 0 ? new Text("Disabled").color(Color.RED.dark(3))
                                    .withFixedSize(70 - 18, 18, 15, 0)
                                    : new Text("Enabled").color(Color.GREEN.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0))
                            .setBackground(
                                ModularUITextures.VANILLA_BACKGROUND,
                                GT_UITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18))
                            .setSize(70, 18)
                            .addTooltip("No Humidity mode"))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(10, 30))
            .widget(
                new Column().widget(new TextWidget("Setup mode").setSize(100, 18))
                    .widget(new TextWidget("IC2 mode").setSize(100, 18))
                    .widget(new TextWidget("No Humidity mode").setSize(100, 18))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(80, 30))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CROSS)
                    .setSize(18, 18)
                    .setPos(10, 30)
                    .addTooltip(new Text("Can't change configuration when running !").color(Color.RED.dark(3)))
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive()));
        return builder.build();
    }

    private HashMap<ItemStack, Double> GUIDropProgress = new HashMap<>();

    @Override
    protected String generateCurrentRecipeInfoString() {
        if (!isIC2Mode) return super.generateCurrentRecipeInfoString();
        StringBuilder ret = new StringBuilder(EnumChatFormatting.WHITE + "Progress: ")
            .append(String.format("%,.2f", (double) mProgresstime / 20))
            .append("s / ")
            .append(String.format("%,.2f", (double) mMaxProgresstime / 20))
            .append("s (")
            .append(String.format("%,.1f", (double) mProgresstime / mMaxProgresstime * 100))
            .append("%)\n");

        for (Map.Entry<ItemStack, Double> drop : GUIDropProgress.entrySet()) {
            ret.append(
                drop.getKey()
                    .getDisplayName())
                .append(": ")
                .append(
                    String.format(
                        "%.2f (+%d)\n",
                        drop.getValue(),
                        Arrays.stream(mOutputItems)
                            .filter(s -> s.isItemEqual(drop.getKey()))
                            .mapToInt(i -> i.stackSize)
                            .sum()));
        }

        return ret.toString();
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.widget(new FakeSyncWidget.BooleanSyncer(() -> isIC2Mode, b -> isIC2Mode = b));
        screenElements.widget(new FakeSyncWidget<>(() -> {
            HashMap<ItemStack, Double> ret = new HashMap<>();
            HashMap<String, Double> dropProgress = new HashMap<>();

            for (Map.Entry<String, Double> drop : dropprogress.entrySet()) {
                dropProgress.merge(drop.getKey(), drop.getValue(), Double::sum);
            }

            for (Map.Entry<String, Double> drop : dropProgress.entrySet()) {
                ret.put(GreenHouseSlot.dropstacks.get(drop.getKey()), drop.getValue());
            }
            return ret;
        }, h -> GUIDropProgress = h, (buffer, h) -> {
            buffer.writeVarIntToBuffer(h.size());
            for (Map.Entry<ItemStack, Double> itemStackDoubleEntry : h.entrySet()) {
                try {
                    buffer.writeItemStackToBuffer(itemStackDoubleEntry.getKey());
                    buffer.writeDouble(itemStackDoubleEntry.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, buffer -> {
            int len = buffer.readVarIntFromBuffer();
            HashMap<ItemStack, Double> ret = new HashMap<>(len);
            for (int i = 0; i < len; i++) {
                try {
                    ret.put(buffer.readItemStackFromBuffer(), buffer.readDouble());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return ret;
        }));
        super.drawTexts(screenElements, inventorySlot);
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(
            Arrays.asList(
                "Running in mode: " + EnumChatFormatting.GREEN
                    + (setupphase == 0 ? (isIC2Mode ? "IC2 crops" : "Normal crops")
                        : ("Setup mode " + (setupphase == 1 ? "(input)" : "(output)")))
                    + EnumChatFormatting.RESET,
                "Uses " + waterusage + "L/operation of water",
                "Uses " + weedexusage + "L/second of Weed-EX 9000",
                "Max slots: " + EnumChatFormatting.GREEN + this.mMaxSlots + EnumChatFormatting.RESET,
                "Used slots: " + ((mStorage.size() > mMaxSlots) ? EnumChatFormatting.RED : EnumChatFormatting.GREEN)
                    + this.mStorage.size()
                    + EnumChatFormatting.RESET));
        HashMap<String, Integer> storageList = new HashMap<>();
        for (GreenHouseSlot greenHouseSlot : mStorage) {
            if (!greenHouseSlot.isValid) continue;
            StringBuilder a = new StringBuilder(
                EnumChatFormatting.GREEN + "x"
                    + greenHouseSlot.input.stackSize
                    + " "
                    + greenHouseSlot.input.getDisplayName());
            if (this.isIC2Mode) {
                a.append(" | Humidity: ")
                    .append(greenHouseSlot.noHumidity ? 0 : 12);
            }
            a.append(EnumChatFormatting.RESET);
            storageList.merge(a.toString(), 1, Integer::sum);
        }
        storageList.forEach((k, v) -> info.add("x" + v + " " + k));
        if (mStorage.size() > mMaxSlots) info
            .add(EnumChatFormatting.DARK_RED + "There are too many crops inside to run !" + EnumChatFormatting.RESET);
        info.addAll(Arrays.asList(super.getInfoData()));
        return info.toArray(new String[0]);
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

    private boolean addCrop(ItemStack input, int slot, boolean simulate) {
        if (!isIC2Mode && !simulate)
            for (GreenHouseSlot g : mStorage) if (g.input.stackSize < 64 && GT_Utility.areStacksEqual(g.input, input)) {
                g.addAll(
                    this.getBaseMetaTileEntity()
                        .getWorld(),
                    input);
                if (input.stackSize == 0) return true;
            }
        GreenHouseSlot h = new GreenHouseSlot(this, simulate ? input.copy() : input, isIC2Mode, isNoHumidity);
        if (h.isValid) {
            if (!simulate) {
                if (slot == -1) mStorage.add(h);
                else mStorage.add(slot, h);
            }
            return true;
        }
        return false;
    }

    private ItemStack addCrop(ItemStack input) {
        if (addCrop(input, -1, false)) return input;
        return null;
    }

    final Map<String, Double> dropprogress = new HashMap<>();

    private static class GreenHouseSlot extends InventoryCrafting {

        final ItemStack input;
        Block crop;
        ArrayList<ItemStack> customDrops = null;
        ItemStack undercrop = null;
        List<ItemStack> drops;
        boolean isValid;
        boolean isIC2Crop;
        boolean noHumidity;
        int growthticks;
        List<List<ItemStack>> generations;

        Random rn;
        IRecipe recipe;
        ItemStack recipeInput;

        int optimalgrowth = 7;

        boolean needsreplanting = true;

        static final GreenHouseWorld fakeworld = new GreenHouseWorld(5, 5, 5);

        public NBTTagCompound toNBTTagCompound() {
            NBTTagCompound aNBT = new NBTTagCompound();
            aNBT.setTag("input", writeItemStackToNBT(input));
            aNBT.setBoolean("isValid", isValid);
            aNBT.setBoolean("isIC2Crop", isIC2Crop);
            if (!isIC2Crop) {
                aNBT.setInteger("crop", Block.getIdFromBlock(crop));
                if (customDrops != null && customDrops.size() > 0) {
                    aNBT.setInteger("customDropsCount", customDrops.size());
                    for (int i = 0; i < customDrops.size(); i++)
                        aNBT.setTag("customDrop." + i, writeItemStackToNBT(customDrops.get(i)));
                }
                aNBT.setInteger("dropscount", drops.size());
                for (int i = 0; i < drops.size(); i++) aNBT.setTag("drop." + i, writeItemStackToNBT(drops.get(i)));
                aNBT.setInteger("optimalgrowth", optimalgrowth);
                aNBT.setBoolean("needsreplanting", needsreplanting);
            } else {
                if (undercrop != null) aNBT.setTag("undercrop", writeItemStackToNBT(undercrop));
                aNBT.setInteger("generationscount", generations.size());
                for (int i = 0; i < generations.size(); i++) {
                    aNBT.setInteger(
                        "generation." + i + ".count",
                        generations.get(i)
                            .size());
                    for (int j = 0; j < generations.get(i)
                        .size(); j++)
                        aNBT.setTag(
                            "generation." + i + "." + j,
                            writeItemStackToNBT(
                                generations.get(i)
                                    .get(j)));
                }
                aNBT.setInteger("growthticks", growthticks);
                aNBT.setBoolean("noHumidity", noHumidity);
            }
            return aNBT;
        }

        public GreenHouseSlot(NBTTagCompound aNBT) {
            super(null, 3, 3);
            isIC2Crop = aNBT.getBoolean("isIC2Crop");
            isValid = aNBT.getBoolean("isValid");
            input = readItemStackFromNBT(aNBT.getCompoundTag("input"));
            if (!isIC2Crop) {
                crop = Block.getBlockById(aNBT.getInteger("crop"));
                if (aNBT.hasKey("customDropsCount")) {
                    int imax = aNBT.getInteger("customDropsCount");
                    customDrops = new ArrayList<>(imax);
                    for (int i = 0; i < imax; i++)
                        customDrops.add(readItemStackFromNBT(aNBT.getCompoundTag("customDrop." + i)));
                }
                drops = new ArrayList<>();
                for (int i = 0; i < aNBT.getInteger("dropscount"); i++)
                    drops.add(readItemStackFromNBT(aNBT.getCompoundTag("drop." + i)));
                optimalgrowth = aNBT.getInteger("optimalgrowth");
                if (optimalgrowth == 0) optimalgrowth = 7;
                if (aNBT.hasKey("needsreplanting")) needsreplanting = aNBT.getBoolean("needsreplanting");
            } else {
                if (aNBT.hasKey("undercrop")) undercrop = readItemStackFromNBT(aNBT.getCompoundTag("undercrop"));
                generations = new ArrayList<>();
                for (int i = 0; i < aNBT.getInteger("generationscount"); i++) {
                    generations.add(new ArrayList<>());
                    for (int j = 0; j < aNBT.getInteger("generation." + i + ".count"); j++) generations.get(i)
                        .add(readItemStackFromNBT(aNBT.getCompoundTag("generation." + i + "." + j)));
                }
                growthticks = aNBT.getInteger("growthticks");
                noHumidity = aNBT.getBoolean("noHumidity");
                rn = new Random();
            }
        }

        public boolean addAll(World world, ItemStack input) {
            if (!GT_Utility.areStacksEqual(this.input, input)) return false;
            if (this.input.stackSize == 64) return false;
            int toconsume = Math.min(64 - this.input.stackSize, input.stackSize);
            int left = addDrops(world, toconsume);
            input.stackSize -= toconsume - left;
            this.input.stackSize += toconsume - left;
            return left == 0;
        }

        public boolean findCropRecipe(World world) {
            if (recipe != null) return true;
            out: for (ItemStack drop : drops) {
                recipeInput = drop;
                for (int j = 0; j < CraftingManager.getInstance()
                    .getRecipeList()
                    .size(); j++) {
                    recipe = (IRecipe) CraftingManager.getInstance()
                        .getRecipeList()
                        .get(j);
                    if (recipe.matches(this, world)
                        && GT_Utility.areStacksEqual(recipe.getCraftingResult(this), input)) {
                        break out;
                    } else recipe = null;
                }
            }
            return recipe != null;
        }

        @Override
        public ItemStack getStackInSlot(int p_70301_1_) {
            if (p_70301_1_ == 0) return recipeInput.copy();
            return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int par1) {
            return null;
        }

        @Override
        public ItemStack decrStackSize(int par1, int par2) {
            return null;
        }

        @SuppressWarnings("EmptyMethod")
        @Override
        public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {}

        public GreenHouseSlot(GT_MetaTileEntity_ExtremeIndustrialGreenhouse tileEntity, ItemStack input, boolean IC2,
            boolean noHumidity) {
            super(null, 3, 3);
            World world = tileEntity.getBaseMetaTileEntity()
                .getWorld();
            this.input = input.copy();
            this.isValid = false;
            if (IC2) {
                GreenHouseSlotIC2(tileEntity, world, input, noHumidity);
                return;
            }
            Item i = input.getItem();
            Block b = null;
            boolean detectedCustomHandler = false;
            // Custom handlers
            // FLOWERS //
            Block bb = Block.getBlockFromItem(i);
            if (bb == Blocks.air) bb = null;
            if (bb instanceof BlockFlower) {
                detectedCustomHandler = true;
                needsreplanting = false;
                customDrops = new ArrayList<>(Collections.singletonList(input.copy()));
                customDrops.get(0).stackSize = 1;
            }
            if (!detectedCustomHandler) {
                if (i instanceof IPlantable) {
                    if (i instanceof ItemSeeds) b = ((ItemSeeds) i).getPlant(world, 0, 0, 0);
                    else if (i instanceof ItemSeedFood) b = ((ItemSeedFood) i).getPlant(world, 0, 0, 0);
                } else {
                    if (i == Items.reeds) b = Blocks.reeds;
                    else {
                        b = Block.getBlockFromItem(i);
                        if (b != Blocks.cactus) return;
                    }
                    needsreplanting = false;
                }
                if (!(b instanceof IPlantable)) return;
                GameRegistry.UniqueIdentifier u = GameRegistry.findUniqueIdentifierFor(i);
                if (u != null && Objects.equals(u.modId, "Natura")) optimalgrowth = 8;

                if (b instanceof BlockStem) {
                    fakeworld.block = null;
                    try {
                        b.updateTick(fakeworld, 5, 5, 5, fakeworld.rand);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                    if (fakeworld.block == null) return;
                    b = fakeworld.block;
                    needsreplanting = false;
                }
            }
            crop = b;
            isIC2Crop = false;
            int toUse = Math.min(64, input.stackSize);
            if (addDrops(world, toUse) == 0 && !drops.isEmpty()) {
                input.stackSize -= toUse;
                this.input.stackSize = toUse;
                this.isValid = true;
            }
        }

        public void GreenHouseSlotIC2(GT_MetaTileEntity_ExtremeIndustrialGreenhouse tileEntity, World world,
            ItemStack input, boolean noHumidity) {
            if (!ItemList.IC2_Crop_Seeds.isStackEqual(input, true, true)) return;
            this.isIC2Crop = true;
            this.noHumidity = noHumidity;
            recalculate(tileEntity, world);
            if (this.isValid) input.stackSize--;
        }

        private boolean setBlock(ItemStack a, int x, int y, int z, World world) {
            Item item = a.getItem();
            Block b = Block.getBlockFromItem(item);
            if (b == Blocks.air || !(item instanceof ItemBlock)) return false;
            short tDamage = (short) item.getDamage(a);
            if (item instanceof GT_Item_Ores && tDamage > 0) {
                if (!world.setBlock(
                    x,
                    y,
                    z,
                    b,
                    GT_TileEntity_Ores.getHarvestData(
                        tDamage,
                        ((GT_Block_Ores_Abstract) b).getBaseBlockHarvestLevel(tDamage % 16000 / 1000)),
                    0)) {
                    return false;
                }
                GT_TileEntity_Ores tTileEntity = (GT_TileEntity_Ores) world.getTileEntity(x, y, z);
                tTileEntity.mMetaData = tDamage;
                tTileEntity.mNatural = false;
            } else world.setBlock(x, y, z, b, tDamage, 0);
            return true;
        }

        public void recalculate(GT_MetaTileEntity_ExtremeIndustrialGreenhouse tileEntity, World world) {
            if (isIC2Crop) {
                CropCard cc = Crops.instance.getCropCard(input);
                this.input.stackSize = 1;
                NBTTagCompound nbt = input.getTagCompound();
                byte gr = nbt.getByte("growth");
                byte ga = nbt.getByte("gain");
                byte re = nbt.getByte("resistance");
                int[] abc = new int[] { 0, -2, 3 };
                int[] xyz = new int[] { 0, 0, 0 };
                tileEntity.getExtendedFacing()
                    .getWorldOffset(abc, xyz);
                xyz[0] += tileEntity.getBaseMetaTileEntity()
                    .getXCoord();
                xyz[1] += tileEntity.getBaseMetaTileEntity()
                    .getYCoord();
                xyz[2] += tileEntity.getBaseMetaTileEntity()
                    .getZCoord();
                boolean cheating = false;
                try {
                    if (world.getBlock(xyz[0], xyz[1] - 2, xyz[2]) != GregTech_API.sBlockCasings4
                        || world.getBlockMetadata(xyz[0], xyz[1] - 2, xyz[2]) != 1) {
                        // no
                        cheating = true;
                        return;
                    }

                    world.setBlock(xyz[0], xyz[1], xyz[2], Block.getBlockFromItem(Ic2Items.crop.getItem()), 0, 0);
                    TileEntity wte = world.getTileEntity(xyz[0], xyz[1], xyz[2]);
                    if (!(wte instanceof TileEntityCrop)) {
                        // should not be even possible
                        return;
                    }
                    TileEntityCrop te = (TileEntityCrop) wte;
                    te.ticker = 1; // don't even think about ticking once
                    te.setCrop(cc);

                    te.setGrowth(gr);
                    te.setGain(ga);
                    te.setResistance(re);

                    ItemStack tobeused = null;

                    if (undercrop != null) setBlock(undercrop, xyz[0], xyz[1] - 2, xyz[2], world);
                    else {
                        te.setSize((byte) (cc.maxSize() - 1));
                        if (!cc.canGrow(te)) {
                            // needs special block

                            boolean cangrow = false;
                            ArrayList<ItemStack> inputs = tileEntity.getStoredInputs();
                            for (ItemStack a : inputs) {
                                if (a.stackSize <= 0) continue;
                                if (!setBlock(a, xyz[0], xyz[1] - 2, xyz[2], world)) continue;
                                if (!cc.canGrow(te)) continue;
                                cangrow = true;
                                undercrop = a.copy();
                                undercrop.stackSize = 1;
                                tobeused = a;
                                break;
                            }

                            if (!cangrow) return;
                        }
                    }

                    te.setSize((byte) cc.maxSize());

                    if (!cc.canBeHarvested(te)) return;

                    // GENERATE DROPS
                    generations = new ArrayList<>();
                    out: for (int i = 0; i < 10; i++) // get 10 generations
                    {
                        ItemStack[] st = te.harvest_automated(false);
                        te.setSize((byte) cc.maxSize());
                        if (st == null) continue;
                        if (st.length == 0) continue;
                        for (ItemStack s : st) if (s == null) continue out;
                        generations.add(new ArrayList<>(Arrays.asList(st)));
                    }
                    if (generations.isEmpty()) return;
                    rn = new Random();

                    // CHECK GROWTH SPEED
                    te.humidity = (byte) (noHumidity ? 0 : 12); // humidity with full water storage or 0 humidity
                    te.airQuality = 6; // air quality when sky is seen
                    te.nutrients = 8; // nutrients with full nutrient storage

                    int dur = cc.growthDuration(te);
                    int rate = te.calcGrowthRate();
                    if (rate == 0) return; // should not be possible with those stats
                    growthticks = (int) Math.ceil(
                        ((double) dur / (double) rate) * (double) cc.maxSize() * (double) TileEntityCrop.tickRate);
                    if (growthticks < 1) growthticks = 1;

                    if (tobeused != null) tobeused.stackSize--;

                    this.isValid = true;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                } finally {
                    if (!cheating) world.setBlock(xyz[0], xyz[1] - 2, xyz[2], GregTech_API.sBlockCasings4, 1, 0);
                    world.setBlockToAir(xyz[0], xyz[1], xyz[2]);
                }
            } else {
                drops = new ArrayList<>();
                addDrops(world, input.stackSize);
            }
        }

        public List<ItemStack> getDrops() {
            return drops;
        }

        static final Map<String, ItemStack> dropstacks = new HashMap<>();

        public List<ItemStack> getIC2Drops(GT_MetaTileEntity_ExtremeIndustrialGreenhouse tileEntity,
            double timeelapsed) {
            int r = rn.nextInt(10);
            if (generations.size() <= r) return new ArrayList<>();
            double growthPercent = (timeelapsed / (double) growthticks);
            List<ItemStack> generation = generations.get(r);
            List<ItemStack> copied = new ArrayList<>();
            for (ItemStack g : generation) copied.add(g.copy());
            for (ItemStack s : copied) {
                double pro = ((double) s.stackSize * growthPercent);
                s.stackSize = 1;
                tileEntity.dropprogress.merge(s.toString(), pro, Double::sum);
                if (!dropstacks.containsKey(s.toString())) dropstacks.put(s.toString(), s.copy());
            }
            copied.clear();
            for (Map.Entry<String, Double> entry : tileEntity.dropprogress.entrySet()) if (entry.getValue() >= 1d) {
                copied.add(
                    dropstacks.get(entry.getKey())
                        .copy());
                copied.get(copied.size() - 1).stackSize = entry.getValue()
                    .intValue();
                entry.setValue(
                    entry.getValue() - (double) entry.getValue()
                        .intValue());
            }
            return copied;
        }

        public int addDrops(World world, int count) {
            if (drops == null) drops = new ArrayList<>();
            if (customDrops != null && customDrops.size() > 0) {
                @SuppressWarnings("unchecked")
                ArrayList<ItemStack> d = (ArrayList<ItemStack>) customDrops.clone();
                for (ItemStack x : drops) {
                    for (Iterator<ItemStack> iterator = d.iterator(); iterator.hasNext();) {
                        ItemStack y = iterator.next();
                        if (GT_Utility.areStacksEqual(x, y)) {
                            x.stackSize += y.stackSize * count;
                            iterator.remove();
                        }
                    }
                }
                final int finalCount = count;
                d.forEach(stack -> {
                    ItemStack i = stack.copy();
                    i.stackSize *= finalCount;
                    drops.add(i);
                });
                return 0;
            } else {
                if (crop == null) return count;
                for (int i = 0; i < count; i++) {
                    List<ItemStack> d = crop.getDrops(world, 0, 0, 0, optimalgrowth, 0);
                    for (ItemStack x : drops) for (ItemStack y : d) if (GT_Utility.areStacksEqual(x, y)) {
                        x.stackSize += y.stackSize;
                        y.stackSize = 0;
                    }
                    for (ItemStack x : d) if (x.stackSize > 0) drops.add(x.copy());
                }
            }
            if (!needsreplanting) return 0;
            for (int i = 0; i < drops.size(); i++) {
                if (GT_Utility.areStacksEqual(drops.get(i), input)) {
                    int took = Math.min(drops.get(i).stackSize, count);
                    drops.get(i).stackSize -= took;
                    count -= took;
                    if (drops.get(i).stackSize == 0) {
                        drops.remove(i);
                        i--;
                    }
                    if (count == 0) {
                        return 0;
                    }
                }
            }
            if (!findCropRecipe(world)) return count;
            int totake = count / recipe.getCraftingResult(this).stackSize + 1;
            for (int i = 0; i < drops.size(); i++) {
                if (GT_Utility.areStacksEqual(drops.get(i), recipeInput)) {
                    int took = Math.min(drops.get(i).stackSize, totake);
                    drops.get(i).stackSize -= took;
                    totake -= took;
                    if (drops.get(i).stackSize == 0) {
                        drops.remove(i);
                        i--;
                    }
                    if (totake == 0) {
                        return 0;
                    }
                }
            }
            return count;
        }
    }

    private static class GreenHouseWorld extends GT_DummyWorld {

        public int x, y, z, meta = 0;
        public Block block;

        GreenHouseWorld(int x, int y, int z) {
            super();
            this.x = x;
            this.y = y;
            this.z = z;
            this.rand = new GreenHouseRandom();
        }

        @Override
        public int getBlockMetadata(int aX, int aY, int aZ) {
            if (aX == x && aY == y && aZ == z) return 7;
            return 0;
        }

        @Override
        public Block getBlock(int aX, int aY, int aZ) {
            if (aY == y - 1) return Blocks.farmland;
            return Blocks.air;
        }

        @Override
        public int getBlockLightValue(int p_72957_1_, int p_72957_2_, int p_72957_3_) {
            return 10;
        }

        @Override
        public boolean setBlock(int aX, int aY, int aZ, Block aBlock, int aMeta, int aFlags) {
            if (aBlock == Blocks.air) return false;
            if (aX == x && aY == y && aZ == z) return false;
            block = aBlock;
            meta = aMeta;
            return true;
        }
    }

    private static class GreenHouseRandom extends Random {

        private static final long serialVersionUID = -387271808935248890L;

        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }
}
