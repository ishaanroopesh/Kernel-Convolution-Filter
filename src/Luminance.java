import java.awt.Color;

public class Luminance {
    
    public static double lum(Color color){
        return intensity(color);
    }

    public static double intensity(Color color){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if (r == g && r == b)
            return r;
        return (0.299*r + 0.587*g + 0.114*b);
    }

    public static Color toGray(Color color){
        int y = (int)(Math.round(intensity(color)));
        Color gray = new Color(y, y, y);
        return gray;
    }

    
}
