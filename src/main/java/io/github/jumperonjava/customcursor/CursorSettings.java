package io.github.jumperonjava.customcursor;


import com.google.gson.Gson;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class CursorSettings implements Cloneable{
    public int size=32;
    public boolean enabled=false;

    CursorSprite pointer = new CursorSprite(1,9, 0, false,Identifier.of("minecraft", "textures/item/diamond_sword.png"));
    CursorSprite arrow = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/arrow.png"));
    CursorSprite ibeam = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/ink_sac.png"));
    CursorSprite crosshair = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/lead.png"));
    CursorSprite pointing_hand = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/rabbit_foot.png"));
    CursorSprite resize_ns = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/golden_hoe.png"));
    CursorSprite resize_ew = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/copper_hoe.png"));
    CursorSprite resize_all = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/nether_star.png"));
    CursorSprite not_allowed = new CursorSprite(0,0, 0, false,Identifier.of("minecraft", "textures/item/barrier.png"));

    public CursorSettings clone() {
        return new Gson().fromJson(new Gson().toJson(this), CursorSettings.class);
    }

    public CursorSprite currentCursor() {
        return pointer;
    }

    public static final class CursorSprite {
        public float x;
        public float y;
        public float rotation;
        public boolean mirror;
        public Identifier identifier;

        public CursorSprite(float x, float y, float rotation, boolean mirror, Identifier identifier) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
            this.mirror = mirror;
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
                    this.mirror == that.mirror &&
                    Objects.equals(this.identifier, that.identifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, rotation, mirror, identifier);
        }

        @Override
        public String toString() {
            return "CursorSprite[" +
                    "x=" + x + ", " +
                    "y=" + y + ", " +
                    "rotation=" + rotation + ", " +
                    "mirror=" + mirror + ", " +
                    "identifier=" + identifier + ']';
        }

        }

}
