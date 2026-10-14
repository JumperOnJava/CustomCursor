package io.github.jumperonjava.customcursor.editor;

import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;

public class CursorWidget implements Renderable, GuiEventListener, NarratableEntry {
    private final String key;
    private final CursorSettings.CursorSprite sprite;
    private final int x;
    private final int y;
    private final int size;
    private final Runnable callback;

    public CursorWidget(String key, CursorSettings.CursorSprite sprite, int x, int y, int size, Runnable callback) {
        this.key = key;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.size = size;
        this.callback = callback;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        render(context, mouseX, mouseY, deltaTicks);
    }

    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        context.pose().pushMatrix();
        context.pose().rotateAbout((float) Math.toRadians(sprite.rotation), x + (float) size / 2, y + (float) size / 2);
        if (isMouseOver(mouseX, mouseY)) {
            context.requestCursor(CursorSettings.cursors().get(key));
            context.fill(x,y,x+size,y+size, 0x7f000000);
        }
        VF.blit(context, sprite.identifier, x, y, 0, 0, size, size, size * (sprite.mirroredX ? -1 : 1), size * (sprite.mirroredY ? -1 : 1));
        context.pose().popMatrix();

    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (isMouseOver(click.x(), click.y())) {
            callback.run();
            return true;
        }
        return false;
    }


    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        var minx = x;
        var maxx = x + size;
        var miny = y;
        var maxy = y + size;

        return mouseX >= minx && mouseX < maxx && mouseY >= miny && mouseY < maxy;
    }

    boolean focused = false;

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {

    }
}
