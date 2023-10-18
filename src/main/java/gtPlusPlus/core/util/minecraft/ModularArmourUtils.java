package gtPlusPlus.core.util.minecraft;

import baubles.api.BaubleType;

public class ModularArmourUtils {

    public enum BT {

        TYPE_AMULET(BaubleType.AMULET, 0),
        TYPE_RING(BaubleType.RING, 1),
        TYPE_BELT(BaubleType.BELT, 2);

        private final BaubleType mType;
        private final int mID;

        BT(final BaubleType tType, int tID) {
            this.mType = tType;
            this.mID = tID;
        }

        public BaubleType getType() {
            return this.mType;
        }

        public BT getThis() {
            return this;
        }

        public int getID() {
            return this.mID;
        }

    }

}
