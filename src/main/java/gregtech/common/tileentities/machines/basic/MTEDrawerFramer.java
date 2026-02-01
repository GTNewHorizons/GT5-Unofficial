package gregtech.common.tileentities.machines.basic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawersCustom;
import com.jaquadro.minecraft.storagedrawers.item.ItemCustomDrawers;

import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;

public class MTEDrawerFramer extends MTEBasicMachine {

    public MTEDrawerFramer(int aID, String aName, String aNameRegional, int aTier) {

        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Automatically frames Drawers",
            4,
            1,
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
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
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_GLOW)
                    .glow()
                    .build()));
    }

    public MTEDrawerFramer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 4, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDrawerFramer(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Add Framed Drawers to Slot 1",
            "The following slots are Side, Trim, and Front materials");
    }

    private static final FallbackableUITexture progressBarTexture = GTUITextures
        .fallbackableProgressbar("drawer_framer", GTUITextures.PROGRESSBAR_ARROW_MULTIPLE);

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(progressBarTexture)
            .build();
    }

    @Override
    protected SlotWidget createItemInputSlot(int index, IDrawable[] backgrounds, Pos2d pos) {
        return (SlotWidget) super.createItemInputSlot(index, backgrounds, pos).setBackground(
            getGUITextureSet().getItemSlot(),
            (index == 0) ? GTUITextures.OVERLAY_SLOT_BOXED : GTUITextures.OVERLAY_SLOT_EXTRUDER_SHAPE);
    }

    // we don't need a special slot here
    @Override
    protected SlotWidget createSpecialSlot(IDrawable[] backgrounds, Pos2d pos, BasicUIProperties uiProperties) {
        return null;
    }

    private boolean isCustomDrawer(ItemStack aStack) {
        return Block.getBlockFromItem(aStack.getItem()) instanceof BlockDrawersCustom;
    }

    private boolean isValidMaterial(ItemStack aStack) {
        return Block.getBlockFromItem(aStack.getItem())
            .isOpaqueCube();
    }

    private ItemStack createNewDrawer(ItemStack aDrawer, ItemStack aSide, ItemStack aTrim, ItemStack aFront,
        int count) {
        Block drawer_block = Block.getBlockFromItem(aDrawer.getItem());
        return ItemCustomDrawers.makeItemStack(drawer_block, count, aSide, aTrim, aFront);
    }

    @Override
    public int checkRecipe() {

        // if storage drawers is not loaded, a recipe could never be made
        if (!Mods.StorageDrawers.isModLoaded()) return DID_NOT_FIND_RECIPE;

        ItemStack drawer_slot = getInputAt(0);
        ItemStack side_slot = getInputAt(1);
        ItemStack trim_slot = getInputAt(2);
        ItemStack front_slot = getInputAt(3);

        // there must be a drawer in the drawer_slot
        if (drawer_slot == null || !isCustomDrawer(drawer_slot)) return DID_NOT_FIND_RECIPE;

        // there must be an item in the side_slot that is a valid material
        if (side_slot == null || !isValidMaterial(side_slot)) return DID_NOT_FIND_RECIPE;

        // there must be a valid recipe now
        getInputAt(0).stackSize -= 1;
        getInputAt(1).stackSize -= 1;
        if (trim_slot != null) getInputAt(2).stackSize -= 1;
        if (front_slot != null) getInputAt(3).stackSize -= 1;

        ItemStack output = createNewDrawer(drawer_slot, side_slot, trim_slot, front_slot, 1);

        // check if the output is blocked
        if (!canOutput(output)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }

        // calculate overclock
        calculateOverclockedNess(16, 20);
        // In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        this.mOutputItems[0] = output.copy();

        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;

    }

}
