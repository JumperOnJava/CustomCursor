package io.github.jumperonjava.customcursor.util;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
//? if > 1.21.8 {
import net.minecraft.client.gui.Click;
//?}
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Scroll list widget for general use.
 */
public class ScrollListWidget extends AlwaysSelectedEntryListWidget<ScrollListWidget.ScrollListEntry> {
    public ScrollListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        //? if < 1.20.4 {
        /*super(client,width,height,y,height,itemHeight);
        *///?} else {
        super(client,width,height,y,itemHeight);
        //?}

        //? if < 1.20.4
        /*setLeftPos(x);*/
    }
    @Override
    public int getRowWidth() {
        return this.width;
    }
    public int addEntry(ScrollListEntry entry){
        return super.addEntry(entry);
    }
    //? if < 1.20.4 {
    /*@Override
    protected int getScrollbarPositionX() {
        return width-6;
    }
    *///?}


    //? if < 1.20.4 {
    /*@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.enableScissor(left,top,left+width,top+height-1);
        super.render(context, mouseX, mouseY, delta);
        context.disableScissor();
    }
    *///?} else {
    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.enableScissor(getX(),getY(),getX()+width,getBottom());
        super.renderWidget(context, mouseX, mouseY, delta);
        context.disableScissor();
    }
    //?}


    public static class ScrollListEntry extends Entry<ScrollListEntry> {
        private final Identifier key;
        private final Runnable event;

        public ScrollListEntry(Identifier key, Runnable onClick) {
            this.key = key;
            this.event = onClick;
        }
        //? if <= 1.21.8 {
        /*@Override
        public void render(DrawContext context,
                           int index,
                           int y, int x,
                           int entryWidth,
                           int entryHeight,
                           int mouseX, int mouseY,
                           boolean hovered,
                           float delta) {
            *///?} else {
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            var x = this.getX();
            var y = this.getY();
            var entryHeight = this.getContentHeight();

            //?}
            TextureWidget.render(context,key,x,y,entryHeight,entryHeight);
            context.drawText(MinecraftClient.getInstance().textRenderer,key.toString(),x+entryHeight+5,y+entryHeight/2-5,0xFFFFFFFF,true);
        }

        @Override
        public Text getNarration() {
            return Text.literal(key.toString());
        }

        //? if <= 1.21.8 {
        /*@Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            var b = super.mouseClicked(mouseX, mouseY, button);
            if(isMouseOver(mouseX, mouseY)){
                event.run();
                return true;
            }
            else return b;
        }
        *///?} else {
            @Override
            public boolean mouseClicked(Click click, boolean doubled) {
                var b = super.mouseClicked(click,doubled);
                if(isMouseOver(click.x(),click.y())){
                    event.run();
                    return true;
                }
                else return b;
            }
        //?}
    }
}