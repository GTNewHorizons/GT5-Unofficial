package gregtech;

public class GT_Test {
    public static final boolean isTestEnv;

    static {
        boolean test;
        try {
            Class.forName(
                    "org.junit.jupiter.api.Assertions",
                    false,
                    Thread.currentThread().getContextClassLoader());
            test = true;
        } catch (Exception ignored) {
            test = false;
        }
        isTestEnv = test;
    }
}
