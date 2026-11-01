package gregtech.api.items.armor.renderer;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BedrockArmorModel {

    @SerializedName("format_version")
    public String formatVersion;

    @SerializedName("minecraft:geometry")
    public List<Geometry> geometryList;

    public static class Geometry {

        public Description description;
        public List<Bone> bones;
    }

    public static class Description {

        public String identifier;
        public int texture_width;
        public int texture_height;
    }

    public static class Bone {

        public String name;
        public String parent;
        public float[] pivot;
        public float[] rotation;
        public List<Cube> cubes;
    }

    public static class Cube {

        public float[] origin;
        public float[] size;
        public float[] pivot;
        public float[] rotation;
        public float inflate;
        public UVs uv;
    }

    public static class UVs {

        public Face north, south, east, west, up, down;
    }

    public static class Face {

        public float[] uv;
        public float[] uv_size;
    }
}
