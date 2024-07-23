package gtPlusPlus.core.item.general.matterManipulator;

enum DirectionUtil {

    Up(0, 1, 0, 1),
    Down(0, -1, 0, 0),
    North(0, 0, -1, 2),
    South(0, 0, 1, 3),
    West(-1, 0, 0, 4),
    East(1, 0, 0, 3);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;
    public final int side;

    private DirectionUtil(int x, int y, int z, int side) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        this.side = side;
    }

    public static DirectionUtil fromSide(int side) {
        if (side == Up.side) {
            return Up;
        } else if (side == Down.side) {
            return Down;
        } else if (side == North.side) {
            return North;
        } else if (side == South.side) {
            return South;
        } else if (side == West.side) {
            return West;
        } else if (side == East.side) {
            return East;
        } else {
            return null;
        }
    }

    public DirectionUtil opposite() {
        switch (this) {
            case Up: {
                return Down;
            }
            case Down: {
                return Up;
            }
            case North: {
                return South;
            }
            case South: {
                return North;
            }
            case West: {
                return East;
            }
            case East: {
                return West;
            }
            default: {
                return null;
            }
        }
    }
}