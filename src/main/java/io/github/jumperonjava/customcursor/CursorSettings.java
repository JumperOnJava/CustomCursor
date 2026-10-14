package io.github.jumperonjava.customcursor;


import com.google.gson.Gson;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CursorSettings implements Cloneable {
    public static CursorType globalCursor;

    public boolean enabled = false;
    public boolean dynamicEnabled = true;

    public final CursorSprite arrow = new CursorSprite(0.0625f, 0.0625f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/arrow.png"));
    public final CursorSprite ibeam = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/ibeam.png"));
    public final CursorSprite crosshair = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/crosshair.png"));
    public final CursorSprite pointing_hand = new CursorSprite(0.375f, 32, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/pointing_hand.png"));
    public final CursorSprite resize_ns = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/resize_ns.png"));
    public final CursorSprite resize_ew = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/resize_ew.png"));
    public final CursorSprite resize_all = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("customcursor", "cursors/resize_all.png"));
    public final CursorSprite not_allowed = new CursorSprite(0.5f, 0.5f, 0, false, false, Identifier.fromNamespaceAndPath("minecraft", "textures/item/barrier.png"));
    public int size;

    public Map<String, CursorSprite> allCursors() {
        return Map.of(
                "arrow", arrow,
                "ibeam", ibeam,
                "crosshair", crosshair,
                "pointing_hand", pointing_hand,
                "resize_ns", resize_ns,
                "resize_ew", resize_ew,
                "resize_all", resize_all,
                "not_allowed", not_allowed
        );
    }

    public static Map<String, CursorType> cursors() {
        return Map.of(
                "arrow", CursorTypes.ARROW,
                "ibeam", CursorTypes.IBEAM,
                "crosshair", CursorTypes.CROSSHAIR,
                "pointing_hand", CursorTypes.POINTING_HAND,
                "resize_ns", CursorTypes.RESIZE_NS,
                "resize_ew", CursorTypes.RESIZE_EW,
                "resize_all", CursorTypes.RESIZE_ALL,
                "not_allowed", CursorTypes.NOT_ALLOWED
        );
    }

    public static final List<String> cursorOrder = List.of(
            "arrow",
            "ibeam",
            "crosshair",
            "pointing_hand",
            "resize_ns",
            "resize_ew",
            "resize_all",
            "not_allowed"
    );

    public CursorSprite cursorToSprite(CursorType cursor) {
        if (!this.dynamicEnabled) {
            return this.arrow;
        }
        if (cursor == CursorTypes.ARROW) return arrow;
        if (cursor == CursorTypes.IBEAM) return ibeam;
        if (cursor == CursorTypes.CROSSHAIR) return crosshair;
        if (cursor == CursorTypes.POINTING_HAND) return pointing_hand;
        if (cursor == CursorTypes.RESIZE_NS) return resize_ns;
        if (cursor == CursorTypes.RESIZE_EW) return resize_ew;
        if (cursor == CursorTypes.RESIZE_ALL) return resize_all;
        if (cursor == CursorTypes.NOT_ALLOWED) return not_allowed;
        return arrow;
    }

    public CursorSettings clone() {
        return new Gson().fromJson(new Gson().toJson(this), CursorSettings.class);
    }


    public static class CursorSprite {
        public float x;
        public float y;
        public float rotation;
        public boolean mirroredX;
        public boolean mirroredY;
        public Identifier identifier;

        public CursorSprite(float x, float y, float rotation, boolean mirroredX, boolean mirroredY, Identifier identifier) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.mirroredX = mirroredX;
            this.mirroredY = mirroredY;
            this.identifier = identifier;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (CursorSprite) obj;
            return Float.floatToIntBits(this.x) == Float.floatToIntBits(that.x) &&
                    Float.floatToIntBits(this.y) == Float.floatToIntBits(that.y) &&
                    Float.floatToIntBits(this.rotation) == Float.floatToIntBits(that.rotation) &&
                    this.mirroredX == that.mirroredX &&
                    Objects.equals(this.identifier, that.identifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, rotation, mirroredX, identifier);
        }

        @Override
        public String toString() {
            return "CursorSprite[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "rotation=" + rotation + ", " +
                    "mirror=" + mirroredX + ", " +
                    "identifier=" + identifier + ']';
        }


    }



}
