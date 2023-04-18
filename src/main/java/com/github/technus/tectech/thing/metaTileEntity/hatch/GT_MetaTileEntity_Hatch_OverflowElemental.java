package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static com.github.technus.tectech.loader.MainLoader.elementalPollution;
import static com.github.technus.tectech.util.CommonValues.DISPERSE_AT;
import static com.github.technus.tectech.util.CommonValues.V;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Created by danie_000 on 12.12.2016.
 */
public class GT_MetaTileEntity_Hatch_OverflowElemental extends GT_MetaTileEntity_Hatch {

    private static Textures.BlockIcons.CustomIcon EM_T_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_T_ACTIVE;
    public static Textures.BlockIcons.CustomIcon MufflerEM;
    public static Textures.BlockIcons.CustomIcon MufflerEMidle;
    private double overflowMatter;
    public final double overflowMax;
    private final double overflowDisperse;

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_OverflowElemental(int aID, String aName, String aNameRegional, int aTier,
            double max) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                0,
                new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.emmuffler.desc.0"),
                        translateToLocal("gt.blockmachines.hatch.emmuffler.desc.1") + ": "
                                + EnumChatFormatting.AQUA
                                + TT_Utility.formatNumberShortExp(max)
                                + " "
                                + translateToLocal("tt.keyword.unit.mass"),
                        translateToLocal("gt.blockmachines.hatch.emmuffler.desc.2") + ": "
                                + EnumChatFormatting.AQUA
                                + TT_Utility.formatNumberShortExp(max / (float) (30 - aTier))
                                + " "
                                + translateToLocal("tt.keyword.unit.massFlux"),
                        translateToLocal("gt.blockmachines.hatch.emmuffler.desc.3") });
        overflowMatter = max / 2;
        overflowMax = max;
        overflowDisperse = overflowMax / (float) (30 - aTier);
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_OverflowElemental(String aName, int aTier, double max, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        overflowMatter = max / 2;
        overflowMax = max;
        overflowDisperse = overflowMax / (float) (30 - aTier);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_T_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_ACTIVE");
        EM_T_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_SIDES");
        MufflerEM = new Textures.BlockIcons.CustomIcon("iconsets/MUFFLER_EM");
        MufflerEMidle = new Textures.BlockIcons.CustomIcon("iconsets/MUFFLER_EM_IDLE");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture(
                        EM_T_ACTIVE,
                        Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
                new GT_RenderedTexture(MufflerEM) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture(
                        EM_T_SIDES,
                        Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
                new GT_RenderedTexture(MufflerEMidle) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_OverflowElemental(mName, mTier, overflowMax, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setDouble("OverflowMatter", overflowMatter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        overflowMatter = aNBT.getFloat("overflowMatter") + aNBT.getDouble("OverflowMatter");
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == DISPERSE_AT) {
            if (aBaseMetaTileEntity.isActive()) {
                if (overflowMatter > overflowDisperse) {
                    TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowDisperse / (Math.pow(2, mTier)));
                    overflowMatter -= overflowDisperse;
                } else {
                    TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowMatter / (Math.pow(2, mTier)));
                    overflowMatter = 0;
                    aBaseMetaTileEntity.setActive(false);
                    aBaseMetaTileEntity.setLightValue((byte) 0);
                    aBaseMetaTileEntity.getWorld().updateLightByType(
                            EnumSkyBlock.Block,
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getZCoord());
                }
                vapePollution(aBaseMetaTileEntity);
            } else {
                if (overflowMatter > 0) {
                    aBaseMetaTileEntity.setActive(true);
                    aBaseMetaTileEntity.setLightValue((byte) 15);
                    aBaseMetaTileEntity.getWorld().updateLightByType(
                            EnumSkyBlock.Block,
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getZCoord());
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {
            TecTech.proxy.em_particle(aBaseMetaTileEntity, aBaseMetaTileEntity.getFrontFacing());
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // DOES NOT CHECK FOR TOO MUCH, it is done only while putting stuff in (OPTIMIZATION!!!)
    }

    private void vapePollution(IGregTechTileEntity mte) {
        float xPos = mte.getXCoord() + 0.5f;
        float yPos = mte.getYCoord() + 0.5f;
        float zPos = mte.getZCoord() + 0.5f;

        int xDirShift = ForgeDirection.getOrientation(mte.getFrontFacing()).offsetX;
        int yDirShift = ForgeDirection.getOrientation(mte.getFrontFacing()).offsetY;
        int zDirShift = ForgeDirection.getOrientation(mte.getFrontFacing()).offsetZ;

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
                xPos - .5 + xDirShift,
                yPos - .5 + yDirShift,
                zPos - .5 + zDirShift,
                xPos + .5 + xDirShift,
                yPos + 1.5 + yDirShift,
                zPos + .5 + zDirShift);
        for (Object entity : mte.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, aabb)) {
            float damagingFactor = (float) Math.log(overflowDisperse);
            ((EntityLivingBase) entity)
                    .addPotionEffect(new PotionEffect(Potion.confusion.id, 1, (int) (damagingFactor * 20)));
            ((EntityLivingBase) entity)
                    .addPotionEffect(new PotionEffect(Potion.wither.id, 2, (int) (damagingFactor * 15)));
            ((EntityLivingBase) entity).attackEntityFrom(elementalPollution, damagingFactor);
            if (entity instanceof EntityPlayer) {
                TecTech.anomalyHandler.addMass((EntityPlayer) entity, overflowDisperse);
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocalFormatted("tt.keyphrase.Contained_mass", clientLocale) + ":",
                EnumChatFormatting.RED + TT_Utility.formatNumberExp(overflowMatter)
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocal("tt.keyword.unit.mass")
                        + " / ",
                EnumChatFormatting.GREEN + TT_Utility.formatNumberShortExp(overflowMax)
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocal("tt.keyword.unit.mass"),
                translateToLocalFormatted("tt.keyphrase.Mass_Disposal_speed", clientLocale) + ": "
                        + EnumChatFormatting.BLUE
                        + TT_Utility.formatNumberShortExp(overflowDisperse)
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocal("tt.keyword.unit.massFlux") };
    }

    @Override
    public void onRemoval() {
        if (isValidMetaTileEntity(this) && getBaseMetaTileEntity().isActive()) {
            TecTech.anomalyHandler.addAnomaly(getBaseMetaTileEntity(), overflowMatter);
            if (TecTech.configTecTech.BOOM_ENABLE) {
                getBaseMetaTileEntity().doExplosion(V[15]);
            } else {
                TecTech.proxy.broadcast(
                        translateToLocalFormatted("tt.keyphrase.Muffler_BOOM", clientLocale) + " "
                                + getBaseMetaTileEntity().getXCoord()
                                + ' '
                                + getBaseMetaTileEntity().getYCoord()
                                + ' '
                                + getBaseMetaTileEntity().getZCoord());
            }
        }
    }

    // Return - Should Explode
    public boolean addOverflowMatter(double matter) {
        overflowMatter += matter;
        return overflowMatter > overflowMax;
    }

    public double getOverflowMatter() {
        return overflowMatter;
    }

    // Return - Should Explode
    public boolean setOverflowMatter(double overflowMatter) {
        this.overflowMatter = overflowMatter;
        return overflowMatter > overflowMax;
    }
}
