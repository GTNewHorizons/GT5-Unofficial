/*
 *  Copyright (C) 2022 kuba6000
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.bartimaeusnek.bartworks.common.tileentities.multis;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.MULTIBLOCK_ADDED_VIA_BARTWORKS;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.client.renderer.BW_CropVisualizer;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_TileEntity_ExtremeIndustrialGreenhouse
        extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_TileEntity_ExtremeIndustrialGreenhouse> {

    private static final boolean debug = false;

    private int mCasing = 0;
    private int mMaxSlots = 0;
    private int setupphase = 1;
    private boolean isIC2Mode = false;
    private byte glasTier = 0;
    private int waterusage = 0;
    private static final int CASING_INDEX = 49;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final Item forestryfertilizer = GameRegistry.findItem("Forestry", "fertilizerCompound");
    private static final IStructureDefinition<GT_TileEntity_ExtremeIndustrialGreenhouse> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_TileEntity_ExtremeIndustrialGreenhouse>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] {
                        {"ccccc", "ccccc", "ccccc", "ccccc", "ccccc"},
                        {"ccccc", "clllc", "clllc", "clllc", "ccccc"},
                        {"ggggg", "g---g", "g---g", "g---g", "ggggg"},
                        {"ggggg", "g---g", "g---g", "g---g", "ggggg"},
                        {"ccccc", "cdddc", "cdwdc", "cdddc", "ccccc"},
                        {"cc~cc", "cCCCc", "cCCCc", "cCCCc", "ccccc"},
                    }))
                    .addElement(
                            'c',
                            ofChain(
                                    onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)),
                                    ofHatchAdder(
                                            GT_TileEntity_ExtremeIndustrialGreenhouse::addEnergyInputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_TileEntity_ExtremeIndustrialGreenhouse::addMaintenanceToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_TileEntity_ExtremeIndustrialGreenhouse::addInputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_TileEntity_ExtremeIndustrialGreenhouse::addOutputToMachineList,
                                            CASING_INDEX,
                                            1)))
                    .addElement('C', onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings4, 1)))
                    .addElement(
                            'l',
                            LoaderReference.ProjRedIllumination
                                    ? ofBlock(
                                            Block.getBlockFromName("ProjRed|Illumination:projectred.illumination.lamp"),
                                            10)
                                    : ofBlock(Blocks.redstone_lamp, 0))
                    .addElement(
                            'g',
                            debug
                                    ? ofBlock(Blocks.glass, 0)
                                    : BorosilicateGlass.ofBoroGlass(
                                            (byte) 0,
                                            (byte) 1,
                                            Byte.MAX_VALUE,
                                            (te, t) -> te.glasTier = t,
                                            te -> te.glasTier))
                    .addElement(
                            'd',
                            ofBlock(
                                    LoaderReference.RandomThings
                                            ? Block.getBlockFromName("RandomThings:fertilizedDirt_tilled")
                                            : Blocks.farmland,
                                    0))
                    .addElement('w', ofBlock(Blocks.water, 0))
                    .build();

    public GT_TileEntity_ExtremeIndustrialGreenhouse(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_ExtremeIndustrialGreenhouse(String aName) {
        super(aName);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant change IC2 mode if the machine is working!");
                return;
            }
            if (!mStorage.isEmpty()) {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant change IC2 mode if there are seeds inside!");
                return;
            }
            this.isIC2Mode = !this.isIC2Mode;
            GT_Utility.sendChatToPlayer(aPlayer, "IC2 mode is now " + (this.isIC2Mode ? "enabled" : "disabled."));
        } else {
            if (this.mMaxProgresstime > 0) {
                GT_Utility.sendChatToPlayer(aPlayer, "You cant enable/disable setup if the machine is working!");
                return;
            }
            this.setupphase++;
            if (this.setupphase == 3) this.setupphase = 0;
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    "EIG is now running in "
                            + (this.setupphase == 1
                                    ? "setup mode (input)."
                                    : (this.setupphase == 2 ? "setup mode (output)." : "normal operation.")));
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_ExtremeIndustrialGreenhouse(this.mName);
    }

    @Override
    public IStructureDefinition<GT_TileEntity_ExtremeIndustrialGreenhouse> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    private static String tierString(int tier) {
        return GT_Values.TIER_COLORS[tier] + GT_Values.VN[tier] + ChatColorHelper.RESET + ChatColorHelper.GRAY;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Crop Farm")
                .addInfo("Controller block for the Extreme Industrial Greenhouse")
                .addInfo("Grow your crops like a chad !")
                .addInfo("Use screwdriver to enable/change/disable setup mode")
                .addInfo("Use screwdriver while sneaking to enable/disable IC2 mode")
                .addInfo("Uses 1000L of water per crop per operation")
                .addInfo("You can insert fertilizer each operation to get more drops (max +400%)")
                .addInfo("-------------------- SETUP   MODE --------------------")
                .addInfo("Does not take power")
                .addInfo("There are two modes: input / output")
                .addInfo("Input mode: machine will take seeds from input bus and plant them")
                .addInfo("[IC2] You need to also input block that is required under the crop")
                .addInfo("Output mode: machine will take planted seeds and output them")
                .addInfo("-------------------- NORMAL CROPS --------------------")
                .addInfo("Minimal tier: " + tierString(4))
                .addInfo("Starting with 1 slot")
                .addInfo("Every slot gives 64 crops")
                .addInfo("Every tier past " + tierString(4) + ", slots are multiplied by 2")
                .addInfo("Base process time: 5 sec")
                .addInfo("Process time is divided by number of tiers past " + tierString(3) + " (Minimum 1 sec)")
                .addInfo("All crops are grown at the end of the operation")
                .addInfo("Will automatically craft seeds if they are not dropped")
                .addInfo("1 Fertilizer per 1 crop +200%")
                .addInfo("-------------------- IC2    CROPS --------------------")
                .addInfo("Minimal tier: " + tierString(6))
                .addInfo("Need " + tierString(6) + " glass tier")
                .addInfo("Starting with 4 slots")
                .addInfo("Every slot gives 1 crop")
                .addInfo("Every tier past " + tierString(6) + ", slots are multiplied by 4")
                .addInfo("Process time: 5 sec")
                .addInfo("All crops are accelerated by x32 times")
                .addInfo("1 Fertilizer per 1 crop +10%")
                .addInfo(BW_Tooltip_Reference.TT_BLUEPRINT)
                .addSeparator()
                .beginStructureBlock(5, 4, 5, false)
                .addController("Front bottom center")
                .addCasingInfo("Clean Stainless Steel Casings", 70)
                .addOtherStructurePart("Borosilicate Glass", "Hollow two middle layers", 2)
                .addStructureInfo("The glass tier limits the Energy Input tier")
                .addStructureInfo("The dirt is from RandomThings, must be tilled")
                .addStructureInfo("Purple lamps are from ProjectRedIllumination. They can be lit")
                .addMaintenanceHatch("Any casing (Except inner bottom ones)", 1)
                .addInputBus("Any casing (Except inner bottom ones)", 1)
                .addOutputBus("Any casing (Except inner bottom ones)", 1)
                .addInputHatch("Any casing (Except inner bottom ones)", 1)
                .addEnergyHatch("Any casing (Except inner bottom ones)", 1)
                .toolTipFinisher(MULTIBLOCK_ADDED_VIA_BARTWORKS.apply(ChatColorHelper.GOLD + "kuba6000"));
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        List<String> info = new ArrayList<>(Arrays.asList(super.getStructureDescription(stackSize)));
        info.add("The dirt is from RandomThings, must be tilled");
        info.add("Purple lamps are from ProjectRedIllumination. They can be lit");
        return info.toArray(new String[] {});
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("glasTier", glasTier);
        aNBT.setInteger("setupphase", setupphase);
        aNBT.setBoolean("isIC2Mode", isIC2Mode);
        aNBT.setInteger("mStorageSize", mStorage.size());
        for (int i = 0; i < mStorage.size(); i++)
            aNBT.setTag("mStorage." + i, mStorage.get(i).toNBTTagCompound());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        glasTier = aNBT.getByte("glasTier");
        setupphase = aNBT.getInteger("setupphase");
        isIC2Mode = aNBT.getBoolean("isIC2Mode");
        for (int i = 0; i < aNBT.getInteger("mStorageSize"); i++)
            mStorage.add(new GreenHouseSlot(aNBT.getCompoundTag("mStorage." + i)));
    }

    @SideOnly(Side.CLIENT)
    public void spawnVisualCrop(World world, int x, int y, int z, int meta, int age) {
        BW_CropVisualizer crop = new BW_CropVisualizer(world, x, y, z, meta, age);
        Minecraft.getMinecraft().effectRenderer.addEffect(crop);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide()) {
            if (aBaseMetaTileEntity.isActive() && aTick % 40 == 0) {
                for (int x = -1; x <= 1; x++)
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && z == 0) continue;
                        int[] abc = new int[] {x, -2, z + 2};
                        int[] xyz = new int[] {0, 0, 0};
                        this.getExtendedFacing().getWorldOffset(abc, xyz);
                        xyz[0] += aBaseMetaTileEntity.getXCoord();
                        xyz[1] += aBaseMetaTileEntity.getYCoord();
                        xyz[2] += aBaseMetaTileEntity.getZCoord();
                        spawnVisualCrop(
                                aBaseMetaTileEntity.getWorld(),
                                xyz[0],
                                xyz[1],
                                xyz[2],
                                aBaseMetaTileEntity.getRandomNumber(8),
                                40);
                    }
            }
        }
        if (aBaseMetaTileEntity.isServerSide() && this.mMaxProgresstime > 0 && setupphase > 0 && aTick % 5 == 0) {
            startRecipeProcessing();
            if (setupphase == 1 && mStorage.size() < mMaxSlots) {
                List<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) addCrop(input);
                this.updateSlots();
            } else if (setupphase == 2 && mStorage.size() > 0) {
                int emptySlots = 0;
                boolean ignoreEmptiness = false;
                for (GT_MetaTileEntity_Hatch_OutputBus i : mOutputBusses) {
                    if (i instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
                        ignoreEmptiness = true;
                        break;
                    }
                    for (int j = 0; j < i.getSizeInventory(); j++)
                        if (i.isValidSlot(j)) if (i.getStackInSlot(j) == null) emptySlots++;
                }
                while (mStorage.size() > 0) {
                    if (!ignoreEmptiness && (emptySlots -= 2) < 0) break;
                    this.addOutput(this.mStorage.get(0).input.copy());
                    if (this.mStorage.get(0).undercrop != null)
                        this.addOutput(this.mStorage.get(0).undercrop.copy());
                    this.mStorage.remove(0);
                }
                this.updateSlots();
            }
            endRecipeProcessing();
        }
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 5, 0);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    private void updateMaxSlots() {
        long v = this.getMaxInputVoltage();
        int tier = GT_Utility.getTier(v);
        if (tier < (isIC2Mode ? 6 : 4)) mMaxSlots = 0;
        else if (isIC2Mode) mMaxSlots = 4 << (2 * (tier - 6));
        else mMaxSlots = 1 << (tier - 4);
    }

    @Override
    public boolean checkRecipe(ItemStack itemStack) {
        long v = this.getMaxInputVoltage();
        int tier = GT_Utility.getTier(v);
        updateMaxSlots();
        if (setupphase > 0) {
            if ((mStorage.size() >= mMaxSlots && setupphase == 1) || (mStorage.size() == 0 && setupphase == 2))
                return false;
            this.mMaxProgresstime = 20;
            this.mEUt = 0;
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            return true;
        }
        if (mStorage.size() > mMaxSlots) return false;
        if (mStorage.isEmpty()) return false;

        waterusage = 0;
        for (GreenHouseSlot s : mStorage) waterusage += s.input.stackSize;
        waterusage *= 1000;

        List<GT_MetaTileEntity_Hatch_Input> fluids = mInputHatches;
        List<GT_MetaTileEntity_Hatch_Input> fluidsToUse = new ArrayList<>(fluids.size());
        int watercheck = waterusage;
        FluidStack waterStack = new FluidStack(FluidRegistry.WATER, 1);
        for (GT_MetaTileEntity_Hatch_Input i : fluids) {
            if (!isValidMetaTileEntity(i)) continue;
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
        if (watercheck > 0 && !debug) return false;
        watercheck = waterusage;
        for (GT_MetaTileEntity_Hatch_Input i : fluidsToUse) {
            int used = i.drain(watercheck, true).amount;
            watercheck -= used;
        }

        // OVERCLOCK
        // FERTILIZER IDEA - IC2 +10% per fertilizer per crop per operation, NORMAL +200% per fertilizer per crop per
        // operation

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
            if (glasTier < 6) return false;
            this.mMaxProgresstime = 100;
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++)
                outputs.addAll(mStorage.get(i).getIC2Drops(((double) this.mMaxProgresstime / 8d) * multiplier));
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        } else {
            this.mMaxProgresstime = Math.max(20, 100 / (tier - 3)); // Min 1 s
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < Math.min(mMaxSlots, mStorage.size()); i++) {
                for (ItemStack drop : mStorage.get(i).getDrops()) {
                    ItemStack s = drop.copy();
                    s.stackSize = (int) ((double) s.stackSize * multiplier);
                    outputs.add(s);
                }
            }
            this.mOutputItems = outputs.toArray(new ItemStack[0]);
        }
        this.mEUt = -(int) ((double) GT_Values.V[tier] * 0.99d);
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.updateSlots();
        return true;
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

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(Arrays.asList(
                "Running in mode: " + EnumChatFormatting.GREEN
                        + (setupphase == 0
                                ? (isIC2Mode ? "IC2 crops" : "Normal crops")
                                : ("Setup mode " + (setupphase == 1 ? "(input)" : "(output)")))
                        + EnumChatFormatting.RESET,
                "Uses " + waterusage + "L/operation of water",
                "Max slots: " + EnumChatFormatting.GREEN + this.mMaxSlots + EnumChatFormatting.RESET,
                "Used slots: " + ((mStorage.size() > mMaxSlots) ? EnumChatFormatting.RED : EnumChatFormatting.GREEN)
                        + this.mStorage.size() + EnumChatFormatting.RESET));
        for (int i = 0; i < mStorage.size(); i++) {
            if (!mStorage.get(i).isValid) continue;
            StringBuilder a = new StringBuilder(
                    "Slot " + i + ": " + EnumChatFormatting.GREEN + "x" + this.mStorage.get(i).input.stackSize + " "
                            + this.mStorage.get(i).input.getDisplayName());
            if (this.isIC2Mode) {
                a.append(" : ");
                for (Map.Entry<String, Double> entry :
                        mStorage.get(i).dropprogress.entrySet())
                    a.append((int) (entry.getValue() * 100d)).append("% ");
            }
            a.append(EnumChatFormatting.RESET);
            info.add(a.toString());
        }
        if (mStorage.size() > mMaxSlots)
            info.add(EnumChatFormatting.DARK_RED + "There are too many crops inside to run !"
                    + EnumChatFormatting.RESET);
        info.addAll(Arrays.asList(super.getInfoData()));
        return info.toArray(new String[0]);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    public List<GreenHouseSlot> mStorage = new ArrayList<>();

    public boolean addCrop(ItemStack input) {
        if (!isIC2Mode)
            for (GreenHouseSlot g : mStorage)
                if (GT_Utility.areStacksEqual(g.input, input)) {
                    g.addAll(this.getBaseMetaTileEntity().getWorld(), input);
                    if (input.stackSize == 0) return true;
                }
        GreenHouseSlot h = new GreenHouseSlot(this, input.copy(), true, isIC2Mode);
        if (h.isValid) {
            if (isIC2Mode) input.stackSize--;
            else input.stackSize = 0;
            mStorage.add(h);
            return true;
        }
        return false;
    }

    private static class GreenHouseSlot extends InventoryCrafting {

        ItemStack input;
        Block crop;
        ItemStack undercrop = null;
        List<ItemStack> drops;
        boolean isValid;
        boolean isIC2Crop;
        int growthticks;
        List<List<ItemStack>> generations;

        Random rn;
        IRecipe recipe;
        ItemStack recipeInput;

        int optimalgrowth = 7;

        boolean needsreplanting = true;

        static GreenHouseWorld fakeworld = new GreenHouseWorld(5, 5, 5);

        public NBTTagCompound toNBTTagCompound() {
            NBTTagCompound aNBT = new NBTTagCompound();
            aNBT.setTag("input", input.writeToNBT(new NBTTagCompound()));
            aNBT.setBoolean("isValid", isValid);
            aNBT.setBoolean("isIC2Crop", isIC2Crop);
            if (!isIC2Crop) {
                aNBT.setInteger("crop", Block.getIdFromBlock(crop));
                aNBT.setInteger("dropscount", drops.size());
                for (int i = 0; i < drops.size(); i++)
                    aNBT.setTag("drop." + i, drops.get(i).writeToNBT(new NBTTagCompound()));
                aNBT.setInteger("optimalgrowth", optimalgrowth);
                aNBT.setBoolean("needsreplanting", needsreplanting);
            } else {
                if (undercrop != null) aNBT.setTag("undercrop", undercrop.writeToNBT(new NBTTagCompound()));
                aNBT.setInteger("generationscount", generations.size());
                for (int i = 0; i < generations.size(); i++) {
                    aNBT.setInteger(
                            "generation." + i + ".count", generations.get(i).size());
                    for (int j = 0; j < generations.get(i).size(); j++)
                        aNBT.setTag(
                                "generation." + i + "." + j,
                                generations.get(i).get(j).writeToNBT(new NBTTagCompound()));
                }
                aNBT.setInteger("growthticks", growthticks);
            }
            return aNBT;
        }

        public GreenHouseSlot(NBTTagCompound aNBT) {
            super(null, 3, 3);
            isIC2Crop = aNBT.getBoolean("isIC2Crop");
            isValid = aNBT.getBoolean("isValid");
            input = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("input"));
            if (!isIC2Crop) {
                crop = Block.getBlockById(aNBT.getInteger("crop"));
                drops = new ArrayList<>();
                for (int i = 0; i < aNBT.getInteger("dropscount"); i++)
                    drops.add(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("drop." + i)));
                optimalgrowth = aNBT.getInteger("optimalgrowth");
                if (optimalgrowth == 0) optimalgrowth = 7;
                if (aNBT.hasKey("needsreplanting")) needsreplanting = aNBT.getBoolean("needsreplanting");
            } else {
                if (aNBT.hasKey("undercrop"))
                    undercrop = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("undercrop"));
                generations = new ArrayList<>();
                for (int i = 0; i < aNBT.getInteger("generationscount"); i++) {
                    generations.add(new ArrayList<>());
                    for (int j = 0; j < aNBT.getInteger("generation." + i + ".count"); j++)
                        generations
                                .get(i)
                                .add(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("generation." + i + "." + j)));
                }
                growthticks = aNBT.getInteger("growthticks");
                rn = new Random();
            }
        }

        public boolean addAll(World world, ItemStack input) {
            if (!GT_Utility.areStacksEqual(this.input, input)) return false;
            if (this.input.stackSize == 64) return false;
            int toconsume = Math.min(64 - this.input.stackSize, input.stackSize);
            int left = addDrops(world, toconsume, true);
            input.stackSize -= toconsume - left;
            this.input.stackSize += toconsume - left;
            return left == 0;
        }

        public boolean findCropRecipe(World world) {
            if (recipe != null) return true;
            out:
            for (ItemStack drop : drops) {
                recipeInput = drop;
                for (int j = 0;
                        j < CraftingManager.getInstance().getRecipeList().size();
                        j++) {
                    recipe = (IRecipe)
                            CraftingManager.getInstance().getRecipeList().get(j);
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

        @Override
        public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
            return;
        }

        public GreenHouseSlot(
                GT_TileEntity_ExtremeIndustrialGreenhouse tileEntity, ItemStack input, boolean autocraft, boolean IC2) {
            super(null, 3, 3);
            World world = tileEntity.getBaseMetaTileEntity().getWorld();
            this.input = input.copy();
            this.isValid = false;
            if (IC2) {
                GreenHouseSlotIC2(tileEntity, world, input);
                return;
            }
            Item i = input.getItem();
            Block b = null;
            if (i instanceof IPlantable) {
                if (i instanceof ItemSeeds) b = ((ItemSeeds) i).getPlant(world, 0, 0, 0);
                else if (i instanceof ItemSeedFood) b = ((ItemSeedFood) i).getPlant(world, 0, 0, 0);
            } else {
                if (i == Items.reeds) b = Blocks.reeds;
                else {
                    b = Block.getBlockFromItem(i);
                    if (!(b == Blocks.cactus)) return;
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

            crop = b;
            isIC2Crop = false;
            if (addDrops(world, input.stackSize, autocraft) == 0 && !drops.isEmpty()) {
                this.isValid = true;
            }
        }

        public void GreenHouseSlotIC2(
                GT_TileEntity_ExtremeIndustrialGreenhouse tileEntity, World world, ItemStack input) {
            if (!ItemList.IC2_Crop_Seeds.isStackEqual(input, true, true)) return;
            CropCard cc = Crops.instance.getCropCard(input);
            this.input.stackSize = 1;
            NBTTagCompound nbt = input.getTagCompound();
            byte gr = nbt.getByte("growth");
            byte ga = nbt.getByte("gain");
            byte re = nbt.getByte("resistance");
            this.isIC2Crop = true;
            int[] abc = new int[] {0, -2, 3};
            int[] xyz = new int[] {0, 0, 0};
            tileEntity.getExtendedFacing().getWorldOffset(abc, xyz);
            xyz[0] += tileEntity.getBaseMetaTileEntity().getXCoord();
            xyz[1] += tileEntity.getBaseMetaTileEntity().getYCoord();
            xyz[2] += tileEntity.getBaseMetaTileEntity().getZCoord();
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
                te.ticker = 1; // dont even think about ticking once
                te.setCrop(cc);

                te.setGrowth(gr);
                te.setGain(ga);
                te.setResistance(re);

                ItemStack tobeused = null;

                te.setSize((byte) (cc.maxSize() - 1));
                if (!cc.canGrow(te)) {
                    // needs special block

                    boolean cangrow = false;
                    ArrayList<ItemStack> inputs = tileEntity.getStoredInputs();
                    for (ItemStack a : inputs) {
                        Block b = Block.getBlockFromItem(a.getItem());
                        if (b == Blocks.air) continue;
                        world.setBlock(xyz[0], xyz[1] - 2, xyz[2], b, a.getItemDamage(), 0);
                        if (!cc.canGrow(te)) continue;
                        cangrow = true;
                        undercrop = a.copy();
                        undercrop.stackSize = 1;
                        tobeused = a;
                        break;
                    }

                    if (!cangrow) return;
                }

                te.setSize((byte) cc.maxSize());

                if (!cc.canBeHarvested(te)) return;

                // GENERATE DROPS
                generations = new ArrayList<>();
                out:
                for (int i = 0; i < 10; i++) // get 10 generations
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
                te.humidity = 12; // humidity with full water storage
                te.airQuality = 6; // air quality when sky is seen
                te.nutrients = 8; // netrients with full nutrient storage

                int dur = cc.growthDuration(te);
                int rate = te.calcGrowthRate();
                if (rate == 0) // should not be possible with those stats
                return;
                growthticks = dur / rate;
                if (growthticks < 1) growthticks = 1;

                input.stackSize--;
                if (tobeused != null) tobeused.stackSize--;

                this.isValid = true;
            } catch (Exception e) {
                e.printStackTrace(System.err);
            } finally {
                if (!cheating) world.setBlock(xyz[0], xyz[1] - 2, xyz[2], GregTech_API.sBlockCasings4, 1, 0);
                world.setBlockToAir(xyz[0], xyz[1], xyz[2]);
            }
        }

        public List<ItemStack> getDrops() {
            return drops;
        }

        Map<String, Double> dropprogress = new HashMap<>();
        static Map<String, ItemStack> dropstacks = new HashMap<>();

        public List<ItemStack> getIC2Drops(double timeelapsed) {
            int r = rn.nextInt(10);
            if (generations.size() <= r) return new ArrayList<>();
            double growthPercent = (timeelapsed / (double) growthticks);
            List<ItemStack> generation = generations.get(r);
            List<ItemStack> copied = new ArrayList<>();
            for (ItemStack g : generation) copied.add(g.copy());
            for (ItemStack s : copied) {
                double pro = ((double) s.stackSize * growthPercent);
                s.stackSize = 1;
                if (dropprogress.containsKey(s.toString()))
                    dropprogress.put(s.toString(), dropprogress.get(s.toString()) + pro);
                else dropprogress.put(s.toString(), pro);
                if (!dropstacks.containsKey(s.toString())) dropstacks.put(s.toString(), s.copy());
            }
            copied.clear();
            for (Map.Entry<String, Double> entry : dropprogress.entrySet())
                if (entry.getValue() >= 1d) {
                    copied.add(dropstacks.get(entry.getKey()).copy());
                    copied.get(copied.size() - 1).stackSize = entry.getValue().intValue();
                    entry.setValue(entry.getValue() - (double) entry.getValue().intValue());
                }
            return copied;
        }

        public int addDrops(World world, int count, boolean autocraft) {
            drops = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                List<ItemStack> d = crop.getDrops(world, 0, 0, 0, optimalgrowth, 0);
                for (ItemStack x : drops)
                    for (ItemStack y : d)
                        if (GT_Utility.areStacksEqual(x, y)) {
                            x.stackSize += y.stackSize;
                            y.stackSize = 0;
                        }
                for (ItemStack x : d) if (x.stackSize > 0) drops.add(x.copy());
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
            if (autocraft) {
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
            }
            return count;
        }
    }

    private static class GreenHouseWorld extends GT_DummyWorld {

        public int x = 0, y = 0, z = 0, meta = 0;
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
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    }
}
