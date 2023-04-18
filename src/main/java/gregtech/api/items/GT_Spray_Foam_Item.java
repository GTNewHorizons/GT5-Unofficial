package gregtech.api.items;

import static gregtech.api.enums.GT_Values.D1;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class GT_Spray_Foam_Item extends GT_Tool_Item {

    public GT_Spray_Foam_Item(String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
        super(
            aUnlocalized,
            aEnglish,
            "Precision Spray",
            aMaxDamage,
            aEntityDamage,
            true); /*
                    * setCraftingSound(Sounds.IC2_TOOLS_PAINTER); setBreakingSound(Sounds.IC2_TOOLS_PAINTER);
                    * setEntityHitSound(Sounds.IC2_TOOLS_PAINTER); setUsageAmounts(25, 3, 1);
                    */
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int aSide, float hitX, float hitY, float hitZ) {
        super.onItemUseFirst(aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
        if (aPlayer.isSneaking()) return false;
        if (aWorld.isRemote) {
            return false;
        }
        Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) return false;
        // byte aMeta = (byte)aWorld.getBlockMetadata(aX, aY, aZ);
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);

        try {
            if (GT_Utility.getClassName(aTileEntity)
                .startsWith("TileEntityCable")) {
                if (GT_Utility.getPublicField(aTileEntity, "foamed")
                    .getByte(aTileEntity) == 0) {
                    if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
                        GT_Utility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, -1, aX, aY, aZ);
                        GT_Utility.callPublicMethod(aTileEntity, "changeFoam", (byte) 1);
                        return true;
                    }
                }
                return false;
            }
        } catch (Throwable e) {
            if (D1) e.printStackTrace(GT_Log.err);
        }

        if (aTileEntity instanceof BaseMetaPipeEntity && (((BaseMetaPipeEntity) aTileEntity).mConnections & -64) == 0) {
            if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
                GT_Utility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, -1, aX, aY, aZ);
                ((BaseMetaPipeEntity) aTileEntity).mConnections |= 64;
            }
            return true;
        }

        aX += ForgeDirection.getOrientation(aSide).offsetX;
        aY += ForgeDirection.getOrientation(aSide).offsetY;
        aZ += ForgeDirection.getOrientation(aSide).offsetZ;

        ItemStack tStack = GT_ModHandler.getIC2Item("constructionFoam", 1);
        if (tStack != null && tStack.getItem() instanceof ItemBlock) {
            int tRotationPitch = Math.round(aPlayer.rotationPitch);
            byte tSide = 0;
            if (tRotationPitch >= 65) {
                tSide = 1;
            } else if (tRotationPitch <= -65) {
                tSide = 0;
            } else {
                tSide = switch (MathHelper.floor_double((aPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
                    case 0 -> 2;
                    case 1 -> 5;
                    case 2 -> 3;
                    case 3 -> 4;
                    default -> tSide;
                };
            }
            switch (0) {
                case 0 -> {
                    if (GT_Utility.isBlockAir(aWorld, aX, aY, aZ)
                        && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
                        GT_Utility.sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, -1, aX, aY, aZ);
                        aWorld.setBlock(aX, aY, aZ, GT_Utility.getBlockFromStack(tStack), tStack.getItemDamage(), 3);
                        return true;
                    }
                }
                case 1 -> {
                    for (byte i = 0; i < 4; i++) {
                        if (GT_Utility.isBlockAir(aWorld, aX, aY, aZ)
                            && GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
                            GT_Utility
                                .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, -1, aX, aY, aZ);
                            aWorld
                                .setBlock(aX, aY, aZ, GT_Utility.getBlockFromStack(tStack), tStack.getItemDamage(), 3);
                        } else {
                            if (i == 0) return false;
                            break;
                        }
                        aX -= ForgeDirection.getOrientation(tSide).offsetX;
                        aY -= ForgeDirection.getOrientation(tSide).offsetY;
                        aZ -= ForgeDirection.getOrientation(tSide).offsetZ;
                    }
                    return true;
                }
                case 2 -> {
                    boolean temp = false, tXFactor = (ForgeDirection.getOrientation(tSide).offsetX == 0),
                        tYFactor = (ForgeDirection.getOrientation(tSide).offsetY == 0),
                        tZFactor = (ForgeDirection.getOrientation(tSide).offsetZ == 0);
                    aX -= (tXFactor ? 1 : 0);
                    aY -= (tYFactor ? 1 : 0);
                    aZ -= (tZFactor ? 1 : 0);
                    for (byte i = 0; i < 3; i++) for (byte j = 0; j < 3; j++) {
                        if (GT_Utility.isBlockAir(
                            aWorld,
                            aX + (tXFactor ? i : 0),
                            aY + (!tXFactor && tYFactor ? i : 0) + (!tZFactor && tYFactor ? j : 0),
                            aZ + (tZFactor ? j : 0))) {
                            if (GT_ModHandler.damageOrDechargeItem(aStack, 1, 1000, aPlayer)) {
                                GT_Utility
                                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_PAINTER, 1.0F, -1, aX, aY, aZ);
                                aWorld.setBlock(
                                    aX + (tXFactor ? i : 0),
                                    aY + (!tXFactor && tYFactor ? i : 0) + (!tZFactor && tYFactor ? j : 0),
                                    aZ + (tZFactor ? j : 0),
                                    GT_Utility.getBlockFromStack(tStack),
                                    tStack.getItemDamage(),
                                    3);
                                temp = true;
                            } else {
                                break;
                            }
                        }
                    }
                    return temp;
                }
            }
        }
        return false;
    }
}
