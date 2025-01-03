package io.github.jumperonjava.customcursor.mixin;

import io.github.jumperonjava.customcursor.CursorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Nullable protected MinecraftClient client;

    @Inject(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 1),locals = LocalCapture.CAPTURE_FAILSOFT)
    //? if < 1.21 {
    void renderCursor(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,tickDelta);
    }
     //?} else {
    /*void renderCursor(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext drawContext){
        CursorRenderer.render(drawContext,i,j,tickCounter.getLastDuration());
    }
    *///?}
}
