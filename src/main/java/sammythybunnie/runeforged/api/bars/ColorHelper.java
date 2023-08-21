package sammythybunnie.runeforged.api.bars;

import org.jetbrains.annotations.NotNull;

public class ColorHelper {
    private int[] rgb = new int[3];

    public ColorHelper(String hexValue) {
        hexToRgb(hexValue);
    }

    private void hexToRgb(@NotNull String hexValue) {
        rgb[0] = Integer.parseInt(hexValue.substring(1, 3), 16);
        rgb[1] = Integer.parseInt(hexValue.substring(3, 5), 16);
        rgb[2] = Integer.parseInt(hexValue.substring(5, 7), 16);
    }

    public int[] getRgb() {
        return rgb;
    }

    public static void main(String[] args) {
        ColorHelper color = new ColorHelper("#FFA500"); // Hex value for orange
        int[] rgb = color.getRgb();
        System.out.println("R: " + rgb[0] + ", G: " + rgb[1] + ", B: " + rgb[2]);
    }
}
