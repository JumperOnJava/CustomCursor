package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.util.VersionFunctions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class TextureWidget{
    public static void render(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        VersionFunctions.drawTexture(context,texture,x,y,0,0,width,height,width,height);
    }
}
