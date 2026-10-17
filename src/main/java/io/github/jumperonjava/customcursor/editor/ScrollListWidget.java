package io.github.jumperonjava.customcursor.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Scroll list widget for general use.
 */
public class ScrollListWidget extends ObjectSelectionList<ScrollListWidget.ScrollListEntry> {
    public ScrollListWidget(Minecraft client, int width, int height, int x, int y, int itemHeight) {
        super(client,width,height,y,itemHeight);
    }
    @Override
    public int getRowWidth() {
        return this.width;
    }
    public int addEntry(ScrollListEntry entry){
        return super.addEntry(entry);
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        context.enableScissor(getX(),getY(),getX()+width,getBottom());
        super.extractWidgetRenderState(context, mouseX, mouseY, delta);
        context.disableScissor();
    }


    public static class ScrollListEntry extends Entry<ScrollListEntry> {
        private final Identifier key;
        private final Runnable event;

        public ScrollListEntry(Identifier key, Runnable onClick) {
            this.key = key;
            this.event = onClick;
        }
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            var x = this.getX();
            var y = this.getY();
            var entryHeight = this.getContentHeight();

            TextureWidget.render(context,key,x,y,entryHeight,entryHeight);
            context.text(Minecraft.getInstance().font,key.toString(),x+entryHeight+5,y+entryHeight/2-5,0xFFFFFFFF,true);
        }

        @Override
        public Component getNarration() {
            return Component.literal(key.toString());
        }
            @Override
            public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
                var b = super.mouseClicked(click,doubled);
                if(isMouseOver(click.x(),click.y())){
                    event.run();
                    return true;
                }
                else return b;
            }
    }
}