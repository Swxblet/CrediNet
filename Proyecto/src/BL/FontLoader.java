package BL;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class FontLoader {

    public static Font loadFont(String path, float size) {
        try {
            File fontFile = new File(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar la fuente: " + e.getMessage());
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
}
