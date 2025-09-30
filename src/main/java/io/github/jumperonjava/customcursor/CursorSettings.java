package io.github.jumperonjava.customcursor;


import com.google.gson.Gson;
import net.minecraft.client.gui.cursor.Cursor;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CursorSettings implements Cloneable {
    public static Cursor globalCursor;

    public int size = 32;
    public boolean enabled = false;
    public boolean dynamicEnabled = true;


    public final CursorSprite arrow = new CursorSprite(0.0625f, 0.0625f, 0, false, Identifier.of("customcursor", "cursors/arrow.png"));
    public final CursorSprite ibeam = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("customcursor", "cursors/ibeam.png"));
    public final CursorSprite crosshair = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("customcursor", "cursors/crosshair.png"));
    public final CursorSprite pointing_hand = new CursorSprite(0.375f, 0, 0, false, Identifier.of("customcursor", "cursors/pointing_hand.png"));
    public final CursorSprite resize_ns = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("customcursor", "cursors/resize_ns.png"));
    public final CursorSprite resize_ew = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("customcursor", "cursors/resize_ew.png"));
    public final CursorSprite resize_all = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("customcursor", "cursors/resize_all.png"));
    public final CursorSprite not_allowed = new CursorSprite(0.5f, 0.5f, 0, false, Identifier.of("minecraft", "textures/item/barrier.png"));

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

    public static Map<String, Cursor> cursors() {
        return Map.of(
                "arrow", StandardCursors.ARROW,
                "ibeam", StandardCursors.IBEAM,
                "crosshair", StandardCursors.CROSSHAIR,
                "pointing_hand", StandardCursors.POINTING_HAND,
                "resize_ns", StandardCursors.RESIZE_NS,
                "resize_ew", StandardCursors.RESIZE_EW,
                "resize_all", StandardCursors.RESIZE_ALL,
                "not_allowed", StandardCursors.NOT_ALLOWED
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

    public CursorSprite cursorToSprite(Cursor cursor) {
        if(!this.dynamicEnabled){
            return this.arrow;
        }
        var map = Map.of(
                Cursor.DEFAULT, arrow,
                StandardCursors.ARROW, arrow,
                StandardCursors.IBEAM, ibeam,
                StandardCursors.CROSSHAIR, crosshair,
                StandardCursors.POINTING_HAND, pointing_hand,
                StandardCursors.RESIZE_NS, resize_ns,
                StandardCursors.RESIZE_EW, resize_ew,
                StandardCursors.RESIZE_ALL, resize_all,
                StandardCursors.NOT_ALLOWED, not_allowed
        );
        return map.get(cursor);
    }

    public CursorSettings clone() {
        return new Gson().fromJson(new Gson().toJson(this), CursorSettings.class);
    }

    public static final class CursorSprite {
        public float x;
        public float y;
        public float rotation;
        public boolean mirroredX;
        public boolean mirroredY;
        public Identifier identifier;

        public CursorSprite(float x, float y, float rotation, boolean mirror, Identifier identifier) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.mirroredX = mirror;
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
