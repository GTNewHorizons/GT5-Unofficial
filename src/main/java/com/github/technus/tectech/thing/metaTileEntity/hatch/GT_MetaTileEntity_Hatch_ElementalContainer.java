package com.github.technus.tectech.thing.metaTileEntity.hatch;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.util.CommonValues.DECAY_AT;
import static com.github.technus.tectech.util.CommonValues.MOVE_AT;
import static com.github.technus.tectech.util.CommonValues.OVERFLOW_AT;
import static com.github.technus.tectech.util.CommonValues.TEC_MARK_EM;
import static com.github.technus.tectech.util.CommonValues.V;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.IEMContainer;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.pipe.IConnectsToElementalPipe;
import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class GT_MetaTileEntity_Hatch_ElementalContainer extends GT_MetaTileEntity_Hatch
        implements IEMContainer, IConnectsToElementalPipe {

    private static Textures.BlockIcons.CustomIcon EM_T_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_T_ACTIVE;
    private static Textures.BlockIcons.CustomIcon EM_T_CONN;

    private String clientLocale = "en_US";

    protected EMInstanceStackMap content = new EMInstanceStackMap();
    // float lifeTimeMult=1f;
    public int postEnergize = 0;
    public double overflowMatter = 0f;
    public short id = -1;
    private byte deathDelay = 2;

    protected GT_MetaTileEntity_Hatch_ElementalContainer(int aID, String aName, String aNameRegional, int aTier,
            String descr) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                0,
                new String[] { TEC_MARK_EM, descr,
                        translateToLocal("tt.base.emhatch.desc.0") + " "
                                + EnumChatFormatting.AQUA
                                + GT_Utility.formatNumbers(aTier * aTier >> 4),
                        translateToLocal("tt.base.emhatch.desc.1") + " "
                                + EnumChatFormatting.AQUA
                                + TT_Utility.formatNumberShortExp(aTier * aTier >> 4),
                        translateToLocal("tt.base.emhatch.desc.2"), translateToLocal("tt.base.emhatch.desc.3"),
                        translateToLocal("tt.base.emhatch.desc.4"), translateToLocal("tt.base.emhatch.desc.5"),
                        translateToLocal("tt.base.emhatch.desc.6"),
                        EnumChatFormatting.AQUA + translateToLocal("tt.base.emhatch.desc.7") });
        TT_Utility.setTier(aTier, this);
    }

    protected GT_MetaTileEntity_Hatch_ElementalContainer(String aName, int aTier, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_T_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_ACTIVE");
        EM_T_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_T_SIDES");
        EM_T_CONN = new Textures.BlockIcons.CustomIcon("iconsets/EM_PIPE_CONN");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture(
                        EM_T_ACTIVE,
                        Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
                new GT_RenderedTexture(EM_T_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
                new GT_RenderedTexture(
                        EM_T_SIDES,
                        Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
                new GT_RenderedTexture(EM_T_CONN) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("postEnergize", postEnergize);
        // aNBT.setFloat("lifeTimeMult",lifeTimeMult);
        aNBT.setDouble("OverflowMatter", overflowMatter);
        content.cleanUp();
        aNBT.setTag("eM_Stacks", content.toNBT(TecTech.definitionsRegistry));
        aNBT.setShort("eID", id);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        postEnergize = aNBT.getInteger("postEnergize");
        // lifeTimeMult=aNBT.getFloat("lifeTimeMult");
        overflowMatter = aNBT.getFloat("overflowMatter") + aNBT.getDouble("OverflowMatter");
        id = aNBT.getShort("eID");
        try {
            content = EMInstanceStackMap.fromNBT(TecTech.definitionsRegistry, aNBT.getCompoundTag("eM_Stacks"));
        } catch (EMException e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            if (content == null) {
                content = new EMInstanceStackMap();
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            byte Tick = (byte) (aTick % 20);
            if (DECAY_AT == Tick) {
                purgeOverflow();
                content.tickContent(1, postEnergize, 1); // Hatches don't life time mult things
                purgeOverflow();
            } else if (OVERFLOW_AT == Tick) {
                if (overflowMatter <= 0) {
                    deathDelay = 3;
                } else {
                    if (deathDelay == 1) {
                        IGregTechTileEntity tGTTileEntity = aBaseMetaTileEntity
                                .getIGregTechTileEntityAtSide(aBaseMetaTileEntity.getBackFacing());
                        if (tGTTileEntity == null || !(tGTTileEntity
                                .getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental)) {
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(ForgeDirection.DOWN);
                        }
                        if (tGTTileEntity == null || !(tGTTileEntity
                                .getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental)) {
                            tGTTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(ForgeDirection.UP);
                        }
                        if (tGTTileEntity != null && tGTTileEntity
                                .getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
                            GT_MetaTileEntity_Hatch_OverflowElemental aMetaTileEntity = (GT_MetaTileEntity_Hatch_OverflowElemental) tGTTileEntity
                                    .getMetaTileEntity();
                            if (aMetaTileEntity.addOverflowMatter(overflowMatter)) {
                                if (TecTech.configTecTech.BOOM_ENABLE) {
                                    tGTTileEntity.doExplosion(V[14]);
                                } else {
                                    TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowMatter * 32D);
                                    TecTech.proxy.broadcast(
                                            "Container1 " + translateToLocal("tt.keyword.BOOM")
                                                    + " "
                                                    + aBaseMetaTileEntity.getXCoord()
                                                    + ' '
                                                    + aBaseMetaTileEntity.getYCoord()
                                                    + ' '
                                                    + aBaseMetaTileEntity.getZCoord());
                                }
                            }
                            deathDelay = 3; // needed in some cases like repetitive failures. Should be 4 since there is
                                            // --
                            // at end but meh...
                            overflowMatter = 0F;
                        }
                    } else if (deathDelay < 1) {
                        if (TecTech.configTecTech.BOOM_ENABLE) {
                            aBaseMetaTileEntity.doExplosion(V[14]);
                        } else {
                            TecTech.anomalyHandler.addAnomaly(aBaseMetaTileEntity, overflowMatter * 32D);
                            deathDelay = 3;
                            overflowMatter = 0;
                            TecTech.proxy.broadcast(
                                    "Container0 " + translateToLocal("tt.keyword.BOOM")
                                            + " "
                                            + aBaseMetaTileEntity.getXCoord()
                                            + ' '
                                            + aBaseMetaTileEntity.getYCoord()
                                            + ' '
                                            + aBaseMetaTileEntity.getZCoord());
                        }
                    }
                    deathDelay--;
                }
            } else if (MOVE_AT == Tick) {
                if (content.hasStacks()) {
                    content.cleanUp();
                    moveAround(aBaseMetaTileEntity);
                }
                getBaseMetaTileEntity().setActive(content.hasStacks());
            }
        }
    }

    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {}

    @Override
    public EMInstanceStackMap getContentHandler() {
        return content;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    public int getMaxStacksCount() {
        return mTier * mTier >> 4;
    }

    public double getMaxStackSize() {
        return mTier * (mTier - 7) * 64D * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
    }

    @Override
    public void purgeOverflow() {
        overflowMatter += content.removeOverflow(getMaxStacksCount(), getMaxStackSize());
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
        if (TecTech.configTecTech.EASY_SCAN || DEBUG_MODE) {
            if (id > 0) {
                if (content == null || content.size() == 0) {
                    return new String[] { translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": "
                            + EnumChatFormatting.AQUA
                            + id, translateToLocalFormatted("tt.keyphrase.No_Stacks", clientLocale) };
                } else {
                    String[] lines = content.getElementalInfo();
                    String[] output = new String[lines.length + 1];
                    output[0] = translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": "
                            + EnumChatFormatting.AQUA
                            + id;
                    System.arraycopy(lines, 0, output, 1, lines.length);
                    return output;
                }
            }
            if (content == null || content.size() == 0) {
                return new String[] { translateToLocalFormatted("tt.keyphrase.No_Stacks", clientLocale) };
            }
            return content.getElementalInfo();
        } else {
            if (id > 0) {
                if (content == null || content.size() == 0) {
                    return new String[] { translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": "
                            + EnumChatFormatting.AQUA
                            + id, translateToLocalFormatted("tt.keyphrase.No_Stacks", clientLocale) };
                }
                return new String[] {
                        translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": " + EnumChatFormatting.AQUA + id,
                        translateToLocalFormatted("tt.keyphrase.Contains_EM", clientLocale) };
            }
            if (content == null || content.size() == 0) {
                return new String[] { translateToLocalFormatted("tt.keyphrase.No_Stacks", clientLocale) };
            }
            return new String[] { translateToLocalFormatted("tt.keyphrase.Contains_EM", clientLocale) };
        }
    }

    public void updateSlots() {
        purgeOverflow();
    }

    @Override
    public void onRemoval() {
        if (isValidMetaTileEntity(this) && getBaseMetaTileEntity().isActive()) {
            TecTech.anomalyHandler.addAnomaly(getBaseMetaTileEntity(), (overflowMatter + content.getMass()) * 16D);
            IGregTechTileEntity base = getBaseMetaTileEntity();
            if (TecTech.configTecTech.BOOM_ENABLE) {
                base.doExplosion(V[15]);
            } else {
                TecTech.proxy.broadcast(
                        translateToLocal("tt.keyword.BOOM") + " "
                                + base.getXCoord()
                                + ' '
                                + base.getYCoord()
                                + ' '
                                + base.getZCoord());
            }
        }
    }
}
