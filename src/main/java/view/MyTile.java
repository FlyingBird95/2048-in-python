package view;

import java.awt.*;

public class MyTile {
    private int value;

    MyTile() {
        this(0);
    }

    MyTile(int num) {
        value = num;
    }

    boolean isEmpty() {
        return value == 0;
    }

    public static Color getForeground(int value) {
        return value < 16 ? new Color(0x776e65) :  new Color(0xf9f6f2);
    }

    public static Color getBackground(int value) {
        switch (value) {
            case 2:    return new Color(0xeee4da);
            case 4:    return new Color(0xede0c8);
            case 8:    return new Color(0xf2b179);
            case 16:   return new Color(0xf59563);
            case 32:   return new Color(0xf67c5f);
            case 64:   return new Color(0xf65e3b);
            case 128:  return new Color(0xedcf72);
            case 256:  return new Color(0xedcc61);
            case 512:  return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
    }

    int getValue(){
        return value;
    }

    void setValue(int value){
        this.value = value;
    }

    static boolean compare(MyTile[] line1, MyTile[] line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.length != line2.length) {
            return false;
        }

        for (int i = 0; i < line1.length; i++) {
            if (line1[i].value != line2[i].value) {
                return false;
            }
        }
        return true;
    }
}
