package tectech.thing.metaTileEntity.multi.godforge.color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.utils.Color;

import gregtech.api.modularui2.GTGuiTextures;

@SuppressWarnings("unused") // for the preset color fields
public class ForgeOfGodsStarColor {

    private static final int LATEST_VERSION = 1;

    // Preset colors
    private static final Map<String, ForgeOfGodsStarColor> PRESETS = new LinkedHashMap<>(4);

    public static final int DEFAULT_RED = 179;
    public static final int DEFAULT_GREEN = 204;
    public static final int DEFAULT_BLUE = 255;
    public static final int DEFAULT_HUE = 220;
    public static final float DEFAULT_SATURATION = 0.298f;
    public static final float DEFAULT_VALUE = 1.0f;
    public static final float DEFAULT_GAMMA = 3.0f;
    public static final int DEFAULT_CYCLE_SPEED = 1;
    public static final int MAX_COLORS = 9;

    public static final ForgeOfGodsStarColor DEFAULT = new ForgeOfGodsStarColor("Default")
        .setNameKey("tt.godforge.star_color.preset.default")
        .addColor(DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE, DEFAULT_GAMMA)
        .registerPreset();

    public static final ForgeOfGodsStarColor RAINBOW = new ForgeOfGodsStarColor("Rainbow")
        .setNameKey("tt.godforge.star_color.preset.rainbow")
        .addColor(255, 0, 0, 3.0f)
        .addColor(255, 255, 0, 3.0f)
        .addColor(0, 255, 0, 3.0f)
        .addColor(0, 255, 255, 3.0f)
        .addColor(255, 0, 255, 3.0f)
        .setCycleSpeed(1)
        .setCustomDrawable(GTGuiTextures.PICTURE_RAINBOW_SQUARE)
        .registerPreset();

    public static final ForgeOfGodsStarColor CLOUDS_PICK = new ForgeOfGodsStarColor("Cloud's Pick")
        .setNameKey("tt.godforge.star_color.preset.clouds_pick")
        .addColor(255, 255, 0, 0.8f)
        .addColor(0, 0, 0, 0)
        .addColor(0, 255, 255, 0.4f)
        .addColor(0, 0, 0, 0)
        .setCycleSpeed(1)
        .setCustomDrawable(
            new Rectangle()
                .setColor(Color.rgb(255, 255, 0), Color.rgb(0, 0, 0), Color.rgb(0, 0, 0), Color.rgb(0, 255, 255)))
        .registerPreset();

    public static final ForgeOfGodsStarColor MAYAS_PICK = new ForgeOfGodsStarColor("Maya's Pick")
        .setNameKey("tt.godforge.star_color.preset.mayas_pick")
        .addColor(0, 0, 0, 0.0f)
        .addColor(109, 201, 225, 1.0f)
        .addColor(255, 255, 255, 3.0f)
        .addColor(255, 172, 210, 1.0f)
        .setCycleSpeed(1)
        .setCustomDrawable(
            new Rectangle().setColor(
                Color.rgb(255, 172, 210),
                Color.rgb(255, 255, 255),
                Color.rgb(0, 0, 0),
                Color.rgb(109, 201, 225)))
        .registerPreset();

    public static List<ForgeOfGodsStarColor> getDefaultColors() {
        return new ArrayList<>(PRESETS.values());
    }

    // "Metadata" about this star color, not related to star rendering
    private String name;
    // version currently unused, but can be used to retain compatibility with old serialized star colors
    // if the structure of the data changes significantly.
    private final int version;
    private boolean isPreset;
    private IDrawable drawable;
    private String nameKey = "";

    // Star render settings
    private final List<StarColorSetting> settings = new ArrayList<>();
    private int cycleSpeed = DEFAULT_CYCLE_SPEED;

    protected ForgeOfGodsStarColor(String name) {
        this(name, LATEST_VERSION);
    }

    private ForgeOfGodsStarColor(String name, int version) {
        this.name = name;
        this.version = version;
    }

    private ForgeOfGodsStarColor registerPreset() {
        this.isPreset = true;
        PRESETS.put(getName(), this);
        return this;
    }

    public boolean isPresetColor() {
        return isPreset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        // Only presets need unlocalized name
        this.nameKey = "";
    }

    public ForgeOfGodsStarColor setNameKey(String key) {
        this.nameKey = key;
        return this;
    }

    public String getLocalizedName() {
        if (this.nameKey.isEmpty()) {
            return this.getName();
        }
        return StatCollector.translateToLocal(this.nameKey);
    }

    public ForgeOfGodsStarColor setCycleSpeed(int speed) {
        this.cycleSpeed = speed;
        return this;
    }

    public int getCycleSpeed() {
        return cycleSpeed;
    }

    public ForgeOfGodsStarColor addColor(int r, int g, int b, float gamma) {
        settings.add(new StarColorSetting(r, g, b, gamma));
        return this;
    }

    public ForgeOfGodsStarColor addColor(StarColorSetting color) {
        settings.add(color);
        return this;
    }

    private ForgeOfGodsStarColor addColors(List<StarColorSetting> colors) {
        settings.addAll(colors);
        return this;
    }

    public int numColors() {
        return settings.size();
    }

    public StarColorSetting getColor(int index) {
        return settings.get(index);
    }

    public void setColor(int index, StarColorSetting color) {
        settings.set(index, color);
    }

    public void removeColor(int index) {
        settings.remove(index);
    }

    public ForgeOfGodsStarColor setCustomDrawable(IDrawable drawable) {
        this.drawable = drawable;
        return this;
    }

    /** @return a picture representation of this star color. */
    public IDrawable getDrawable() {
        if (drawable == null) {
            StarColorSetting setting = settings.get(0);
            drawable = new Rectangle()
                .setColor(Color.rgb(setting.getColorR(), setting.getColorG(), setting.getColorB()));
        }
        return drawable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForgeOfGodsStarColor that = (ForgeOfGodsStarColor) o;

        if (cycleSpeed != that.cycleSpeed) return false;
        if (!name.equals(that.name)) return false;
        if (settings.size() != that.settings.size()) return false;
        for (int i = 0; i < settings.size(); i++) {
            StarColorSetting thisSetting = settings.get(i);
            StarColorSetting thatSetting = that.settings.get(i);
            if (!thisSetting.equals(thatSetting)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serializeToString();
    }

    public NBTTagCompound serializeToNBT() {
        NBTTagCompound NBT = new NBTTagCompound();

        if (isPresetColor()) {
            // Known color, we can just do this with a simple key
            NBT.setString("Preset", getName());
            return NBT;
        }

        NBT.setString("Name", getName());
        NBT.setInteger("CycleSpeed", cycleSpeed);
        NBT.setInteger("Version", version);

        if (!settings.isEmpty()) {
            NBTTagCompound settingsNBT = new NBTTagCompound();
            settingsNBT.setInteger("Size", settings.size());

            for (int i = 0; i < settings.size(); i++) {
                StarColorSetting setting = settings.get(i);
                settingsNBT.setTag(Integer.toString(i), setting.serialize());
            }

            NBT.setTag("Settings", settingsNBT);
        }

        return NBT;
    }

    public static ForgeOfGodsStarColor deserialize(NBTTagCompound NBT) {
        if (NBT.hasKey("Preset")) {
            return PRESETS.get(NBT.getString("Preset"));
        }

        ForgeOfGodsStarColor color = new ForgeOfGodsStarColor(NBT.getString("Name"));
        color.setCycleSpeed(NBT.getInteger("CycleSpeed"));

        if (NBT.hasKey("Settings")) {
            NBTTagCompound settingsNBT = NBT.getCompoundTag("Settings");
            int size = settingsNBT.getInteger("Size");
            for (int i = 0; i < size; i++) {
                NBTTagCompound colorNBT = settingsNBT.getCompoundTag(Integer.toString(i));
                StarColorSetting setting = StarColorSetting.deserialize(colorNBT);
                color.settings.add(setting);
            }
        }

        return color;
    }

    public String serializeToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StarColorV1:{");
        sb.append("Name:");
        sb.append(getName());
        sb.append(",Cycle:");
        sb.append(getCycleSpeed());

        for (StarColorSetting setting : settings) {
            sb.append("|");
            sb.append("r:");
            sb.append(setting.getColorR());
            sb.append(",g:");
            sb.append(setting.getColorG());
            sb.append(",b:");
            sb.append(setting.getColorB());
            sb.append(",m:");
            sb.append(setting.getGamma());
        }

        sb.append("}");
        return sb.toString();
    }

    @Nullable
    public static ForgeOfGodsStarColor deserialize(String raw) {
        if (raw == null) return null;
        if (!raw.startsWith("StarColorV1:{") || !raw.endsWith("}")) return null;

        // Wrap in try-catch for easy format "checking"
        try {
            String[] data = raw.substring(13, raw.length() - 1)
                .split("\\|");

            // Parse the header (name and cycle rate)
            String header = data[0];
            String[] headerData = header.split(",");

            // Name
            String name = null;
            if (headerData[0].startsWith("Name:")) {
                name = headerData[0].substring(5);
            }

            // Cycle Rate
            Integer cycleRate = null;
            if (headerData[1].startsWith("Cycle:")) {
                cycleRate = Integer.valueOf(headerData[1].substring(6));
            }

            List<StarColorSetting> colorSettings = new ArrayList<>();
            for (int i = 1; i < data.length; i++) {
                String[] colorData = data[i].split(",");
                int r = -1, g = -1, b = -1;
                float m = -1;
                for (String color : colorData) {
                    String[] singleData = color.split(":");
                    switch (singleData[0]) {
                        case "r" -> r = Integer.parseInt(singleData[1]);
                        case "g" -> g = Integer.parseInt(singleData[1]);
                        case "b" -> b = Integer.parseInt(singleData[1]);
                        case "m" -> m = Float.parseFloat(singleData[1]);
                    }
                }
                if (r != -1 && g != -1 && b != -1 && m != -1) {
                    colorSettings.add(new StarColorSetting(r, g, b, m));
                }
            }

            if (name != null && cycleRate != null && !colorSettings.isEmpty()) {
                return new ForgeOfGodsStarColor(name).setCycleSpeed(cycleRate)
                    .addColors(colorSettings);
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static void writeToBuffer(PacketBuffer buf, ForgeOfGodsStarColor color) {
        buf.writeBoolean(color == null);
        if (color != null) {
            buf.writeBoolean(color.isPresetColor());
            try {
                buf.writeStringToBuffer(color.name);
            } catch (IOException ignored) {}

            if (!color.isPresetColor()) {
                buf.writeInt(color.cycleSpeed);
                buf.writeInt(color.settings.size());
                for (StarColorSetting setting : color.settings) {
                    StarColorSetting.writeToBuffer(buf, setting);
                }
            }
        }
    }

    public static ForgeOfGodsStarColor readFromBuffer(PacketBuffer buf) {
        if (buf.readBoolean()) return null;
        boolean isPresetColor = buf.readBoolean();
        String name;
        try {
            name = buf.readStringFromBuffer(32767);
        } catch (IOException ignored) {
            return null;
        }

        if (isPresetColor) {
            return PRESETS.get(name);
        }

        ForgeOfGodsStarColor color = new ForgeOfGodsStarColor(name);
        color.setCycleSpeed(buf.readInt());
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            color.addColor(StarColorSetting.readFromBuffer(buf));
        }
        return color;
    }
}
