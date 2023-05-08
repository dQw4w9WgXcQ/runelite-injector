import com.allatori.annotations.DoNotRename;
import net.runelite.api.widgets.Widget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@DoNotRename
public class rl10 {
    @DoNotRename
    public static long tag = -1;
    @DoNotRename
    public static Widget widget = null;
    @DoNotRename
    public static boolean fakeClick = false;

    static Class<?> my;
    static Class<?> ix;
    static Class<?> ck;
    static Class<?> gr;
    static Class<?> widgetClass;
    static Class<?> mouseRecorderClass;
    static Field entityCount;
    static Field entityTags;

    static Method addSceneMenuOptions;
    static Method addCancelMenuEntry;
    static Method addWidgetMenuOptions;

    static {
        try {
            my = Class.forName("my");
            ix = Class.forName("ix");
            ck = Class.forName("ck");
            gr = Class.forName("gr");
            mouseRecorderClass = Class.forName("dl");
            widgetClass = Class.forName("mq");

            entityCount = ix.getDeclaredField("ai");
            entityTags = ix.getDeclaredField("ag");

            addSceneMenuOptions = ck.getDeclaredMethod("ls", int.class, int.class, int.class, int.class, int.class);
            addCancelMenuEntry = gr.getDeclaredMethod("kw", int.class);
            addWidgetMenuOptions = my.getDeclaredMethod("lo", widgetClass, int.class);
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @DoNotRename
    static void zz() {
        try {
            long tag = rl10.tag;

            if (tag == -1) {
                return;
            }

            entityCount.set(null, -429048361);
            long[] entityTags = (long[]) rl10.entityTags.get(null);
            entityTags[0] = tag;
            addCancelMenuEntry.invoke(null, -742935767);
            addSceneMenuOptions.invoke(null, 11, 0, 0, 0, -1708655552);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    @DoNotRename
    static void yy() {
        try {
            Widget widget = rl10.widget;

            if (widget == null) {
                return;
            }

            System.out.println("widget " + widget.getId());

            addWidgetMenuOptions.invoke(null, widget, -478627331);
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    static boolean weed = false;
    static int x = 42069;
    static int y = 42069;

    @DoNotRename
    static void print(int i) {
        if (weed) {
            y = i;
        } else {
            x = i;
        }

        weed = !weed;
    }

    @DoNotRename
    static void print(long l) {
        System.out.println("x: " + x + " y: " + y + " t: " + l);
    }

    static File file = new File(System.getProperty("user.home"), "mouse.txt");
    static BufferedWriter writer;

    static {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DoNotRename
    static void printMouseRecorderSend(int var6, int var13, int var11, int var12) {
        try {
            writer.write("move," + var13 + "," + var11 + "," + var12 + "," + var6);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int button = -1;

    @DoNotRename
    static void printMouseClickSend(int dt, int x, int y) {
        try {
            writer.write("click," + button + "," + dt + "," + x + "," + y);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        button = -1;
    }

    @DoNotRename
    static void button1() {
        button = 1;
    }

    @DoNotRename
    static void button0() {
        button = 0;
    }
}
