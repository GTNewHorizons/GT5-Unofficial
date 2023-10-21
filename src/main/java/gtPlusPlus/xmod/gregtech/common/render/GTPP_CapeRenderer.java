package gtPlusPlus.xmod.gregtech.common.render;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.proxy.ClientProxy;
import gtPlusPlus.core.util.data.AES;
import gtPlusPlus.core.util.data.FileUtils;
import gtPlusPlus.core.util.math.MathUtils;

public class GTPP_CapeRenderer extends RenderPlayer {

    private static final ResourceLocation[] mCapes = { new ResourceLocation("miscutils:textures/OrangeHD.png"),
            new ResourceLocation("miscutils:textures/FancyCapeHD.png"),
            new ResourceLocation("miscutils:textures/TesterCapeHD.png"),
            new ResourceLocation("miscutils:textures/PatreonCapeHD.png"),
            new ResourceLocation("miscutils:textures/DevCapeHD.png"), };

    private final boolean mInit;

    public GTPP_CapeRenderer() {
        mInit = init();
    }

    private boolean init() {
        if (mInit) {
            return false;
        }
        return CapeUtils.init();
    }

    private static boolean hasResourceChecked = false;
    private static boolean hasSetRenderer = false;
    private boolean hasCape = false;
    private ResourceLocation tResource = null;

    public synchronized void receiveRenderSpecialsEvent(RenderPlayerEvent.Specials.Pre aEvent) {

        // Check we have set Render Manager
        if (this.renderManager == null) {
            hasSetRenderer = false;
        }

        // Set Render Manager
        if (!hasSetRenderer) {
            if (RenderManager.instance != null) {
                setRenderManager(RenderManager.instance);
                hasSetRenderer = true;
            }
        }

        // Actually Render
        if (hasSetRenderer) {

            // We have capes turned off, so let's not render.
            if (!ConfigSwitches.enableCustomCapes) {
                return;
            }

            if (!CapeUtils.mapsPopulated) {
                if (!CapeUtils.cacheReady) {
                    return;
                }
                CapeUtils.writeCacheToMaps();
                CapeUtils.mapsPopulated = true;
            }

            // We have already checked if this player has a cape, but since they do not, we best not render.
            if (hasResourceChecked) {
                if (!hasCape && !CORE.DEVENV) {
                    return;
                }
            }

            // Allocate client player object
            AbstractClientPlayer aPlayer = (AbstractClientPlayer) aEvent.entityPlayer;

            // Make sure we don't keep checking on clients who dont have capes.
            if (!hasResourceChecked) {

                // Get players UUID
                String aPlayerUUID = aPlayer != null ? aPlayer.getGameProfile().getId().toString() : "BAD";

                // If for whatever reason this fails, we just exit early.
                if (aPlayerUUID.equals("BAD")) {
                    return;
                }

                // Automatically allocate a Dev cape while in Dev mode.
                if (tResource == null && CORE.DEVENV) {
                    tResource = mCapes[4];
                    hasCape = true;
                }

                String aPlayerName = ClientProxy.playerName;

                // Check cape lists for the cape this player owns.
                if (!hasCape) {
                    for (Pair<String, String> aData : CapeUtils.mOrangeCapes) {
                        if (aData.getKey().equals(aPlayerUUID) || aPlayerName.equals(aData.getValue())) {
                            tResource = mCapes[0];
                            hasCape = true;
                            break;
                        }
                    }
                }
                if (!hasCape) {
                    for (Pair<String, String> aData : CapeUtils.mMiscCapes) {
                        if (aData.getKey().equals(aPlayerUUID) || aPlayerName.equals(aData.getValue())) {
                            tResource = mCapes[1];
                            hasCape = true;
                            break;
                        }
                    }
                }
                if (!hasCape) {
                    for (Pair<String, String> aData : CapeUtils.mBetaTestCapes) {
                        if (aData.getKey().equals(aPlayerUUID) || aPlayerName.equals(aData.getValue())) {
                            tResource = mCapes[2];
                            hasCape = true;
                            break;
                        }
                    }
                }
                if (!hasCape) {
                    for (Pair<String, String> aData : CapeUtils.mPatreonCapes) {
                        if (aData.getKey().equals(aPlayerUUID) || aPlayerName.equals(aData.getValue())) {
                            tResource = mCapes[3];
                            hasCape = true;
                            break;
                        }
                    }
                }
                if (!hasCape) {
                    for (Pair<String, String> aData : CapeUtils.mDevCapes) {
                        if (aData.getKey().equals(aPlayerUUID) || aPlayerName.equals(aData.getValue())) {
                            tResource = mCapes[4];
                            hasCape = true;
                            break;
                        }
                    }
                }
                hasResourceChecked = true;
            }

            if (hasResourceChecked) {
                // We have met all the conditions, let's render that cape.
                renderCapeOnPlayer(aEvent, aPlayer);
            }
        }
    }

    private boolean renderCapeOnPlayer(RenderPlayerEvent.Specials.Pre aEvent, AbstractClientPlayer aPlayer) {
        float aPartialTicks = aEvent.partialRenderTick;
        try {
            if (tResource == null && CORE.DEVENV) {
                tResource = mCapes[3];
            }

            // If player is invisible, don't render.
            if (GT_Utility.getFullInvisibility(aPlayer) || aPlayer.isInvisible()
                    || GT_Utility.getPotion(aPlayer, Integer.valueOf(Potion.invisibility.id))) {
                aEvent.setCanceled(true);
                return false;
            }

            if ((tResource != null) && (!aPlayer.getHideCape())) {
                bindTexture(tResource);
                GL11.glPushMatrix();
                GL11.glTranslatef(0.0F, 0.0F, 0.125F);
                double d0 = aPlayer.field_71091_bM + (aPlayer.field_71094_bP - aPlayer.field_71091_bM) * aPartialTicks
                        - (aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * aPartialTicks);
                double d1 = aPlayer.field_71096_bN + (aPlayer.field_71095_bQ - aPlayer.field_71096_bN) * aPartialTicks
                        - (aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * aPartialTicks);
                double d2 = aPlayer.field_71097_bO + (aPlayer.field_71085_bR - aPlayer.field_71097_bO) * aPartialTicks
                        - (aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * aPartialTicks);
                float f6 = aPlayer.prevRenderYawOffset
                        + (aPlayer.renderYawOffset - aPlayer.prevRenderYawOffset) * aPartialTicks;
                double d3 = MathHelper.sin(f6 * CORE.PI / 180.0F);
                double d4 = -MathHelper.cos(f6 * CORE.PI / 180.0F);
                float f7 = (float) d1 * 10.0F;
                float f8 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                float f9 = (float) (d0 * d4 - d2 * d3) * 100.0F;
                if (f7 < -6.0F) {
                    f7 = -6.0F;
                }
                if (f7 > 32.0F) {
                    f7 = 32.0F;
                }
                if (f8 < 0.0F) {
                    f8 = 0.0F;
                }
                float f10 = aPlayer.prevCameraYaw + (aPlayer.cameraYaw - aPlayer.prevCameraYaw) * aPartialTicks;
                f7 += MathHelper.sin(
                        (aPlayer.prevDistanceWalkedModified
                                + (aPlayer.distanceWalkedModified - aPlayer.prevDistanceWalkedModified) * aPartialTicks)
                                * 6.0F)
                        * 32.0F
                        * f10;
                if (aPlayer.isSneaking()) {
                    f7 += 25.0F;
                }
                GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                ((ModelBiped) this.mainModel).renderCloak(0.0625F);
                GL11.glPopMatrix();
                return true;
            }
        } catch (Throwable e) {

        }
        return false;
    }

    private static class CapeUtils {

        private static char SPLIT_CHARACTER = '§';
        private static AES sAES;
        private static volatile boolean cacheReady = false;
        private static boolean mapsPopulated = false;

        // UUID - Username
        private static final AutoMap<Pair<String, String>> mOrangeCapes = new AutoMap<>();
        private static final AutoMap<Pair<String, String>> mMiscCapes = new AutoMap<>();
        private static final AutoMap<Pair<String, String>> mBetaTestCapes = new AutoMap<>();
        private static final AutoMap<Pair<String, String>> mPatreonCapes = new AutoMap<>();
        private static final AutoMap<Pair<String, String>> mDevCapes = new AutoMap<>();

        private static boolean init() {
            CapeUtils.handleOldCapeCache();
            if (CORE.DEVENV) {
                return true;
            }
            ForkJoinPool.commonPool().execute(() -> {
                try {
                    if (shouldDownloadCapeList()) {
                        downloadCapeList();
                    }
                } catch (Exception ignored) {}
                cacheReady = true;
            });
            return true;
        }

        private static boolean shouldDownloadCapeList() {
            if (!doesCapeCacheExistLocally()) {
                return true;
            }
            if (isCapeCacheWeekOld()) {
                return true;
            }
            return false;
        }

        private static boolean isCapeCacheWeekOld() {
            if (!doesCapeCacheExistLocally()) {
                return true;
            } else {
                File dat = CapeUtils.getCapeCache();
                Date dateLastMod = new Date(dat.lastModified());
                Date dateNow = new Date(System.currentTimeMillis() - (7l * 24 * 60 * 60 * 1000));
                if (dateLastMod.before(dateNow)) {
                    return true;
                }
            }
            return false;
        }

        private static void downloadCapeList() {
            try {
                File dat = getCapeCache();
                File temp = allocateTempFile();
                InputStream inputStream = new URL("https://alkcorp.overminddl1.com/CapeCache.dat").openStream();
                FileOutputStream fileOS = new FileOutputStream(temp);
                IOUtils.copy(inputStream, fileOS);
                if (isDownloadedCapeListBigger(temp)) {
                    fileOS = new FileOutputStream(dat);
                    IOUtils.copy(inputStream, fileOS);
                }
            } catch (Throwable t) {
                Logger.INFO("Unable to download GT++ cape list.");
            }
        }

        private static boolean isDownloadedCapeListBigger(File aFile) {
            double aExistingFileSize = (doesCapeCacheExistLocally() ? getCapeCache().length() : 0);
            double aNewFileSize = aFile.length();
            if (aNewFileSize > aExistingFileSize) {
                return true;
            }
            return false;
        }

        private static void handleOldCapeCache() {
            File aCacheFile = FileUtils.getFile("GTPP", "dat");
            if (FileUtils.doesFileExist(aCacheFile)) {
                aCacheFile.delete();
            }
        }

        private static boolean doesCapeCacheExistLocally() {
            File aCacheFile = FileUtils.getFile("CapeCache", "dat");
            if (FileUtils.doesFileExist(aCacheFile)) {
                return true;
            }
            return false;
        }

        private static File getCapeCache() {
            File aCacheFile = FileUtils.getFile("CapeCache", "dat");
            if (FileUtils.doesFileExist(aCacheFile)) {
                FileUtils.createFile(aCacheFile);
            }
            return aCacheFile;
        }

        public static final List<String> getDataFromCache() {
            File aCacheFile = getCapeCache();
            List<String> aCache = FileUtils.readLines(aCacheFile);
            if (aCache != null && !aCache.isEmpty()) {
                return aCache;
            }
            return new AutoMap<>();
        }

        private static File allocateTempFile() {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("gtpp-", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tempFile == null) {
                tempFile = FileUtils
                        .createFile("", "gtpp-" + MathUtils.randInt(Short.MAX_VALUE, (Integer.MAX_VALUE / 2)), "tmp");
            }
            tempFile.deleteOnExit();
            return tempFile;
        }

        public static final void writeCacheToMaps() {
            List<String> aCacheData = getDataFromCache();
            if (aCacheData != null && !aCacheData.isEmpty()) {
                if (sAES == null) {
                    sAES = new AES();
                }
                AutoMap<String> aDecodedData = new AutoMap<>();
                for (String aToDecode : aCacheData) {
                    aDecodedData.put(sAES.decode(aToDecode));
                }
                if (!aDecodedData.isEmpty()) {
                    AutoMap<Pair<String, String>> aCapeType1 = new AutoMap<>();
                    AutoMap<Pair<String, String>> aCapeType2 = new AutoMap<>();
                    AutoMap<Pair<String, String>> aCapeType3 = new AutoMap<>();
                    AutoMap<Pair<String, String>> aCapeType4 = new AutoMap<>();
                    AutoMap<Pair<String, String>> aCapeType5 = new AutoMap<>();
                    boolean didProcessStringData = false;
                    Logger.INFO("Decoded String Count: " + aDecodedData.size());
                    for (String aToSplit : aDecodedData) {
                        String[] aSplitData = aToSplit.split("" + SPLIT_CHARACTER);
                        if (aSplitData != null && aSplitData.length >= 2) {
                            if (aSplitData[0] != null) {
                                Integer aCapeTypeID2 = Integer.parseInt(aSplitData[0]);
                                if (aCapeTypeID2 != null) {
                                    int aCapeTypeID = aCapeTypeID2;
                                    Pair<String, String> aFinalString = new Pair<>(
                                            "UUID: " + aSplitData[1],
                                            "Username: " + (aSplitData[2] != null && aSplitData[0].length() > 0
                                                    ? aSplitData[2]
                                                    : "Not Specified"));
                                    Logger.INFO("Cape Type: " + aCapeTypeID);
                                    switch (aCapeTypeID) {
                                        case 0 -> {
                                            aCapeType1.add(aFinalString);
                                            Logger.INFO(
                                                    "Added user to map " + aCapeTypeID
                                                            + ", map now holds "
                                                            + aCapeType1.size()
                                                            + " users.");
                                        }
                                        case 1 -> {
                                            aCapeType2.add(aFinalString);
                                            Logger.INFO(
                                                    "Added user to map " + aCapeTypeID
                                                            + ", map now holds "
                                                            + aCapeType2.size()
                                                            + " users.");
                                        }
                                        case 2 -> {
                                            aCapeType3.add(aFinalString);
                                            Logger.INFO(
                                                    "Added user to map " + aCapeTypeID
                                                            + ", map now holds "
                                                            + aCapeType3.size()
                                                            + " users.");
                                        }
                                        case 3 -> {
                                            aCapeType4.add(aFinalString);
                                            Logger.INFO(
                                                    "Added user to map " + aCapeTypeID
                                                            + ", map now holds "
                                                            + aCapeType4.size()
                                                            + " users.");
                                        }
                                        case 4 -> {
                                            aCapeType5.add(aFinalString);
                                            Logger.INFO(
                                                    "Added user to map " + aCapeTypeID
                                                            + ", map now holds "
                                                            + aCapeType5.size()
                                                            + " users.");
                                        }
                                        default -> {}
                                    }
                                }
                            }
                        }
                    }
                    if (!aCapeType1.isEmpty() || !aCapeType2.isEmpty()
                            || !aCapeType3.isEmpty()
                            || !aCapeType4.isEmpty()
                            || !aCapeType5.isEmpty()) {
                        didProcessStringData = true;
                    } else {
                        // did not process any data
                    }
                    if (didProcessStringData) {
                        if (!aCapeType1.isEmpty()) {
                            for (Pair<String, String> aUser : aCapeType1) {
                                Logger.INFO("Adding Generic cape for " + aUser.getKey());
                                mOrangeCapes.add(aUser);
                            }
                        }
                        if (!aCapeType2.isEmpty()) {
                            for (Pair<String, String> aUser : aCapeType2) {
                                Logger.INFO("Adding Blue cape for " + aUser.getKey());
                                mMiscCapes.add(aUser);
                            }
                        }
                        if (!aCapeType3.isEmpty()) {
                            for (Pair<String, String> aUser : aCapeType3) {
                                Logger.INFO("Adding Beta cape for " + aUser.getKey());
                                mBetaTestCapes.add(aUser);
                            }
                        }
                        if (!aCapeType4.isEmpty()) {
                            for (Pair<String, String> aUser : aCapeType4) {
                                Logger.INFO("Adding Patreon cape for " + aUser.getKey());
                                mPatreonCapes.add(aUser);
                            }
                        }
                        if (!aCapeType5.isEmpty()) {
                            for (Pair<String, String> aUser : aCapeType5) {
                                Logger.INFO("Adding Dev cape for " + aUser.getKey());
                                mDevCapes.add(aUser);
                            }
                        }
                    }
                } else {
                    // No data decoded
                }
            } else {
                // Nothing was cached?
            }
        }
    }
}
