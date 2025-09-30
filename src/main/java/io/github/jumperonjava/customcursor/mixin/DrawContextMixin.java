package io.github.jumperonjava.customcursor.mixin;

//? if > 1.21.8 {
/*import io.github.jumperonjava.customcursor.CursorSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.Cursor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "setCursor", at = @At("HEAD"))
    void setCursor(Cursor cursor, CallbackInfo ci) {
        CursorSettings.globalCursor = cursor;
    }
}
*///?} else {

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrawContext.class)
public class DrawContextMixin {

}
//?}