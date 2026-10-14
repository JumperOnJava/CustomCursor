package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class TextureWidget{
    public static void render(GuiGraphicsExtractor context, Identifier texture, int x, int y, int width, int height) {
        VF.blit(context,texture,x,y,0,0,width,height,width,height);
    }
}
