package io.github.jumperonjava.customcursor.mixin;

import io.github.jumperonjava.customcursor.CursorSettings;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HoveredTooltipPositioner.class)
public class TooltipPositionMixin
{
    @ModifyArgs(method = "getPosition", at = @At(value = "INVOKE",target = "Lorg/joml/Vector2i;add(II)Lorg/joml/Vector2i;"))
    void tooltipPosChange(Args args){
        var scale = MinecraftClient.getInstance().getWindow().getScaleFactor();
        var cursor = CustomCursorInit.getConfig().settings.clone();
        cursor.size/=scale;

        var config = CustomCursorInit.getConfig().settings;
        var sprite = config.arrow;
        //? if > 1.21.8 {
        var contextCursor = CursorSettings.globalCursor;
        sprite = config.cursorToSprite(contextCursor);
        //?}


        args.set(0,(int)args.get(0) - 6 + (int)(cursor.size * (1-sprite.x)));
        args.set(1,(int)args.get(1) - (int)(cursor.size * (sprite.y)));
    }
}
