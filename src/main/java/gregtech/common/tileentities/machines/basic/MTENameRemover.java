package gregtech.common.tileentities.machines.basic;

import static net.minecraft.util.EnumChatFormatting.BOLD;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.UNDERLINE;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;
import gregtech.common.items.ItemIntegratedCircuit;

public class MTENameRemover extends MTEBasicMachine {

    public MTENameRemover(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Can fix GT items with broken NBT data, will erase everything!",
            2,
            1,
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER_GLOW)
                    .glow()
                    .build()));
    }

    public MTENameRemover(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENameRemover(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int checkRecipe() {
        if (getInputAt(0) == null) return 0;
        ItemStack output = getInputAt(0).copy();
        NBTTagCompound nbt = output.getTagCompound();
        if (nbt != null) {
            int circuitSetting = 0;
            ItemStack circuit = getInputAt(1);
            if (circuit != null && circuit.getItem() instanceof ItemIntegratedCircuit) {
                circuitSetting = circuit.getItemDamage();
            }
            boolean removeName = false;
            boolean removeDisassembly = false;
            boolean removeColor = false;
            boolean removeRepair = false;
            boolean removeDye = false;
            boolean removeSpray = false;
            boolean removeMuffler = false;
            boolean removeCovers = false;
            switch (circuitSetting) {
                case 1:
                    removeName = true;
                    break;
                case 2:
                    removeDisassembly = true;
                    break;
                case 3:
                    removeColor = true;
                    break;
                case 4:
                    removeRepair = true;
                    break;
                case 5:
                    removeDye = true;
                    break;
                case 6:
                    removeSpray = true;
                    break;
                case 7:
                    removeMuffler = true;
                    break;
                case 24:
                    removeCovers = true;
                default:
                    removeName = true;
                    removeDisassembly = true;
                    removeColor = true;
                    removeRepair = true;
                    removeDye = true;
                    removeSpray = true;
                    removeMuffler = true;
            }
            if (removeName && nbt.hasKey("display")) {
                nbt.getCompoundTag("display")
                    .removeTag("Name");
                if (nbt.getCompoundTag("display")
                    .hasNoTags()) {
                    nbt.removeTag("display");
                }
            }
            if (removeDisassembly) removeTag(nbt, "GT.CraftingComponents");
            if (removeColor) removeTag(nbt, "color");
            if (removeRepair) removeTag(nbt, "RepairCost");
            if (removeDye && nbt.hasKey("display")) {
                nbt.getCompoundTag("display")
                    .removeTag("color");
                if (nbt.getCompoundTag("display")
                    .hasNoTags()) {
                    nbt.removeTag("display");
                }
            }
            if (removeSpray) removeTag(nbt, "mColor");
            if (removeMuffler) removeTag(nbt, "mMuffler");
            removeTag(nbt, "mTargetStackSize"); // MTEBuffer
            removeTag(nbt, "mOutputFluid"); // MTEDigitalTankBase
            removeTag(nbt, "mVoidOverflow"); // MTEDigitalTankBase & MTEQuantumChest
            removeTag(nbt, "mVoidFluidFull"); // MTEDigitalTankBase
            removeTag(nbt, "mLockFluid"); // MTEDigitalTankBase
            removeTag(nbt, "lockedFluidName"); // MTEDigitalTankBase
            removeTag(nbt, "mAllowInputFromOutputSide"); // MTEDigitalTankBase
            removeTag(nbt, "mItemsPerSide"); // MTEItemDistributor
            removeTag(nbt, "radiusConfig"); // MTEMiner & MTEPump
            removeTag(nbt, "mDisallowRetract"); // MTEPump
            removeTag(nbt, "mStrongRedstone"); // BaseMetaTileEntity
            removeTag(nbt, "mCoverSides"); // CoverableTileEntity, no longer read or written
            if (removeCovers) { // BaseMetaTileEntity
                removeTag(nbt, "mMuffler");
                removeTag(nbt, "mLockUpgrade");
                removeTag(nbt, "gt.covers");
                for (String key : CoverableTileEntity.COVER_DATA_NBT_KEYS) {
                    removeTag(nbt, key);
                }
            }
            if (nbt.hasNoTags()) {
                output.setTagCompound(null);
            }
        }
        if (canOutput(output)) {
            getInputAt(0).stackSize = 0;
            mEUt = 0;
            mMaxProgresstime = 20;
            mOutputItems[0] = output;
            return 2;
        }
        return 0;
    }

    private static void removeTag(NBTTagCompound nbt, String key) {
        if (nbt.hasKey(key)) {
            nbt.removeTag(key);
        }
    }

    @Override
    public String[] getDescription() {
        List<String> description = new ArrayList<>();
        description.add("Removes various NBT tags as well as covers.");
        description.add(" ");
        description.add(UNDERLINE + "First Slot" + RESET);
        description.add("The item you want to strip of NBT");
        description.add(" ");
        description.add(UNDERLINE + "Second Slot" + RESET);
        description.add("One of the following circuits:");
        description.add(BOLD + "Circuit 1:" + RESET + "  Attempt to fix broken ores by removing the Display Name tag");
        description.add(BOLD + "Circuit 3:" + RESET + "  Remove Railcraft stacking tag");
        description.add(BOLD + "Circuit 4:" + RESET + "  Remove Anvil repair tag");
        description.add(BOLD + "Circuit 5:" + RESET + "  Remove Dye from Leather armor");
        description.add(BOLD + "Circuit 6:" + RESET + "  Remove Spray color from GT items");
        description.add(BOLD + "Circuit 7:" + RESET + "  Remove Muffler Upgrade from GT machines");
        description.add(
            BOLD + "Circuit 24:"
                + RESET
                + "  Remove everything including covers. Be careful you won't recover the covers!");
        description.add(" ");
        description.add(BOLD + "No Circuit:" + RESET + " Remove everything except covers");
        return description.toArray(new String[0]);
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public long maxAmperesOut() {
        return 0;
    }

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_NAME_REMOVER))
            .build();
    }
}
