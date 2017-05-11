package data;

public class GameKeys {

    private static boolean[] keys;
    private static boolean[] pkeys;

    private static final int NUM_KEYS = 8;
    public static final int NUM_1 = 0;
    public static final int NUM_2 = 1;
    public static final int NUM_3 = 2;
    public static final int NUM_4 = 3;
    public static final int Q = 4;
    public static final int RIGHT_MOUSE = 5;
    public static final int LEFT_MOUSE = 6;
    public static final int ESCAPE = 7;

    public GameKeys() {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];

    }

    public void update() {
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public void setKey(int k, boolean b) {
        keys[k] = b;
    }


    public boolean isDown(int k) {
        return keys[k];
    }

    public boolean isPressed(int k) {
        return keys[k] && !pkeys[k];
    }

}
