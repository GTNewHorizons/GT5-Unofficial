package gregtech.common.tileentities.machines.basic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawersCustom;
import com.jaquadro.minecraft.storagedrawers.item.ItemCustomDrawers;

import gregtech.api.enums.MachineType;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;

public class MTEDrawerFramer extends MTEBasicMachine {

    private static final int RECIPE_DURATION = 20;

    public MTEDrawerFramer(int aID, String aName, String aNameRegional, int aTier) {

        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.DRAWER_FRAMER.description(),
            4,
            1,
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_SIDE_DRAWER_FRAMER)
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_SIDE_DRAWER_FRAMER)
                .build(),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_DRAWER_FRAMER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_DRAWER_FRAMER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_DRAWER_FRAMER),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_DRAWER_FRAMER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_TOP_DRAWER_FRAMER)
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_TOP_DRAWER_FRAMER)
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_DRAWER_FRAMER)
                .build(),
            TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_BOTTOM_DRAWER_FRAMER)
                .build());
    }

    public MTEDrawerFramer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 4, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDrawerFramer(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
        SlotWidget slot = super.createItemInputSlot(index, backgrounds, pos);
        if (index == 0) return slot;
        if (index == 1) return (SlotWidget) slot
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_DRAWER_SIDE);
        if (index == 2) return (SlotWidget) slot
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_DRAWER_TRIM);
        return (SlotWidget) slot
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_DRAWER_FRONT);
    }

    // we don't need a special slot here
    @Override
    protected SlotWidget createSpecialSlot(IDrawable[] backgrounds, Pos2d pos, BasicUIProperties uiProperties) {
        return null;
    }

    private boolean isCustomDrawer(ItemStack itemStack) {
        return Block.getBlockFromItem(itemStack.getItem()) instanceof BlockDrawersCustom;
    }

    private boolean isValidMaterial(ItemStack itemStack) {
        Block itemBlock = Block.getBlockFromItem(itemStack.getItem());
        if (itemBlock == null) return false;
        return itemBlock.isOpaqueCube();
    }

    private ItemStack createNewDrawer(ItemStack drawer, ItemStack sideMat, ItemStack trimMat, ItemStack frontMat,
        int count) {
        Block drawerBlock = Block.getBlockFromItem(drawer.getItem());
        return ItemCustomDrawers.makeItemStack(drawerBlock, count, sideMat, trimMat, frontMat);
    }

    @Override
    public int checkRecipe() {

        ItemStack drawerSlot = getInputAt(0);
        ItemStack sideSlot = getInputAt(1);
        ItemStack trimSlot = getInputAt(2);
        ItemStack frontSlot = getInputAt(3);

        // there must be a drawer in the drawerSlot
        if (drawerSlot == null || !isCustomDrawer(drawerSlot)) return DID_NOT_FIND_RECIPE;

        // there must be an item in the sideSlot that is a valid material
        if (sideSlot == null || !isValidMaterial(sideSlot)) return DID_NOT_FIND_RECIPE;

        // there must be a valid recipe now
        getInputAt(0).stackSize -= 1;
        getInputAt(1).stackSize -= 1;
        if (trimSlot != null) getInputAt(2).stackSize -= 1;
        if (frontSlot != null) getInputAt(3).stackSize -= 1;

        ItemStack output = createNewDrawer(drawerSlot, sideSlot, trimSlot, frontSlot, 1);

        // check if the output is blocked
        if (!canOutput(output)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }

        calculateOverclockedNess((int) TierEU.RECIPE_LV, RECIPE_DURATION);

        // In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        this.mOutputItems[0] = output.copy();

        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;

    }

}
