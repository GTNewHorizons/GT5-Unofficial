package gregtech.api.util;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTRenderUtil;

public class GTUtilityClient {

    public static List<String> getTooltip(ItemStack aStack, boolean aGuiStyle) {
        try {
            List<String> tooltip = aStack.getTooltip(
                Minecraft.getMinecraft().thePlayer,
                Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
            if (aGuiStyle) {
                tooltip.set(
                    0,
                    (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor + tooltip.get(0));
                for (int i = 1; i < tooltip.size(); i++) {
                    tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
                }
            }
            return tooltip;
        } catch (RuntimeException e) {
            // Collections.singletonList() can not be added to. we don't want that
            if (aGuiStyle) return Lists.newArrayList(
                (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor
                    + aStack.getDisplayName());
            return Lists.newArrayList(aStack.getDisplayName());
        }
    }

    public static void clearTurbineOverlay(List<RenderOverlay.OverlayTicket> tickets) {
        tickets.forEach(RenderOverlay.OverlayTicket::remove);
        tickets.clear();
    }

    public static void setTurbineOverlay(World aWorld, int aX, int aY, int aZ, ExtendedFacing tExtendedFacing,
        IIconContainer[] tTextures, List<RenderOverlay.OverlayTicket> ticketContainer) {
        clearTurbineOverlay(ticketContainer);

        int[] tABCCoord = new int[] { -1, -1, 0 };
        int[] tXYZOffset = new int[3];
        final ForgeDirection tDirection = tExtendedFacing.getDirection();
        tExtendedFacing = ExtendedFacing.of(tDirection);

        RenderOverlay overlay = RenderOverlay.getOrCreate(aWorld);

        for (int i = 0; i < 9; i++) {
            tExtendedFacing.getWorldOffset(tABCCoord, tXYZOffset);
            int tX = tXYZOffset[0] + aX;
            int tY = tXYZOffset[1] + aY;
            int tZ = tXYZOffset[2] + aZ;
            ticketContainer.add(overlay.set(aX, aY, aZ, tX, tY, tZ, tDirection, TextureFactory.of(tTextures[i]), 0));
            if (++tABCCoord[0] == 2) {
                tABCCoord[0] = -1;
                tABCCoord[1]++;
            }
        }

    }

    public static void renderTurbineOverlay(IBlockAccess aWorld, int aX, int aY, int aZ, RenderBlocks aRenderer,
        ExtendedFacing tExtendedFacing, Block tBlockOverride, IIconContainer[] tTextures) {
        int[] tABCCoord = new int[] { -1, -1, 0 };
        int[] tXYZOffset = new int[3];
        final ForgeDirection tDirection = tExtendedFacing.getDirection();
        tExtendedFacing = ExtendedFacing.of(tDirection);
        final LightingHelper tLighting = new LightingHelper(aRenderer);

        // for some reason +x and -z need this field set to true, but not any other sides
        if (tDirection == ForgeDirection.NORTH || tDirection == ForgeDirection.EAST) aRenderer.field_152631_f = true;

        for (int i = 0; i < 9; i++) {
            tExtendedFacing.getWorldOffset(tABCCoord, tXYZOffset);
            // since structure check passed, we can assume it is turbine casing
            int tX = tXYZOffset[0] + aX;
            int tY = tXYZOffset[1] + aY;
            int tZ = tXYZOffset[2] + aZ;
            Block tBlock;
            if (tBlockOverride == null) {
                tBlock = aWorld.getBlock(aX + tDirection.offsetX, tY + tDirection.offsetY, aZ + tDirection.offsetZ);
            } else {
                tBlock = tBlockOverride;
            }
            // we skip the occlusion test, as we always require a working turbine to have a block of air before it
            // so the front face cannot be occluded whatsoever in the most cases.
            Tessellator.instance.setBrightness(
                tBlock.getMixedBrightnessForBlock(
                    aWorld,
                    aX + tDirection.offsetX,
                    tY + tDirection.offsetY,
                    aZ + tDirection.offsetZ));
            tLighting.setupLighting(tBlock, tX, tY, tZ, tDirection)
                .setupColor(tDirection, Dyes._NULL.getRGBA());
            GTRenderUtil.renderBlockIcon(
                aRenderer,
                tBlock,
                tX + tDirection.offsetX * 0.001,
                tY + tDirection.offsetY * 0.001,
                tZ + tDirection.offsetZ * 0.001,
                tTextures[i].getIcon(),
                tDirection);
            if (++tABCCoord[0] == 2) {
                tABCCoord[0] = -1;
                tABCCoord[1]++;
            }
        }

        aRenderer.field_152631_f = false;
    }
}
