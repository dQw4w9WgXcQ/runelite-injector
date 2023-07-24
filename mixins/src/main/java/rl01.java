import com.allatori.annotations.DoNotRename;
import net.runelite.api.widgets.Widget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;

@DoNotRename
public class rl01 {
    //tag
    @DoNotRename
    public static long fet = -1;
    //widget
    @DoNotRename
    public static Widget fww = null;
    //disableInput
    @DoNotRename
    public static volatile boolean dii = false;

    static File file = Paths.get(System.getProperty("user.home"), "NovaLite", "mouserecording" + System.currentTimeMillis() + ".txt").toFile();
    static BufferedWriter writer;

    static Class<?> my;
    static Class<?> ix;
    static Class<?> ck;
    static Class<?> gr;
    static Class<?> widgetClass;
    static Class<?> mouseRecorderClass;
    static Field entityCount;
    static Field entityTags;

    static Method addSceneMenuOptions;
    static Method addCancelMenuEntry;//should be named resetMenu
    static Method addWidgetMenuOptions;

    @DoNotRename
    static int lbb = -1;

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

        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
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
    static void mmm() {
        try {
            long tag = rl01.fet;

            if (tag == -1) {
                return;
            }

            entityCount.set(null, -429048361);
            long[] entityTags = (long[]) rl01.entityTags.get(null);
            entityTags[0] = tag;
            addCancelMenuEntry.invoke(null, -742935767);
            addSceneMenuOptions.invoke(null, 11, 0, 0, 0, -1708655552);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    @DoNotRename
    static void www() {
        try {
            Widget widget = rl01.fww;

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

    //mouseClickPacket
    @DoNotRename
    static void mcp(int dt, int x, int y) {
        try {
            System.out.println("click," + lbb + "," + dt + "," + x + "," + y);
            writer.write("click," + lbb + "," + dt + "," + x + "," + y);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lbb = -1;
    }

    //mouseRecorderWrite
    @DoNotRename
    static void mrw(int dt, int dx, int dy, int dtRemAccumualtion) {
        try {
            System.out.println("move," + dt + "," + dx + "," + dy + "," + dtRemAccumualtion);
            writer.write("move," + dt + "," + dx + "," + dy + "," + dtRemAccumualtion);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //mouseRecorderSend
    @DoNotRename
    static void mrs() {
        try {
            System.out.println("packet");
            writer.write("packet");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //button1
    @DoNotRename
    static void bb1() {
        lbb = 1;
    }

    //button0
    @DoNotRename
    static void bb0() {
        lbb = 0;
    }

    //fakeMouseRecorder
    @DoNotRename
    static boolean fmr() {
        return false;
    }

    //fakeMouseClick
    //need to set MouseHandler_lastPressedTimeMillis
    @DoNotRename
    static boolean fmc() {
        return false;
    }

    //getFakeX
    @DoNotRename
    static int gfx(int x) {
        System.out.println("getFakeX " + x);
        return x;
    }

    //getFakeY
    @DoNotRename
    static int gfy(int y) {
        System.out.println("getFakeY " + y);
        return y;
    }

    //getFakeButton
    @DoNotRename
    static int gfb(int button) {
        System.out.println("getFakeButton " + button);
        return button;
    }
}
