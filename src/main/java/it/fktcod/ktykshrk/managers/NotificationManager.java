package it.fktcod.ktykshrk.managers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.module.Notification;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.utils.visual.Colors;
import it.fktcod.ktykshrk.utils.visual.RenderUtils;
import it.fktcod.ktykshrk.utils.visual.font.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import scala.reflect.internal.Trees.New;

public class NotificationManager {
    public ArrayList<Notification> notifications = new ArrayList<Notification>();

    public void add(Notification noti) {
        noti.y = notifications.size() * 25;
        notifications.add(noti);

    }

    public void draw() {
        int i = 0;
        Notification remove = null;
        for (Notification notification : notifications) {

            if (notification.x == 0) {
                notification.in = !notification.in;
            }
            if (Math.abs(notification.x - notification.width) < 0.1 && !notification.in) {
                remove = notification;
            }
            if (notification.in) {
                notification.x = (float) notification.animationUtils.animate(0, notification.x, 0.1f);
            } else {
                notification.x = (float) notification.animationUtils.animate(notification.width, notification.x, 0.1f);
            }
//            }
            notification.onRender();
            i++;
        }
        if (remove != null) {
            notifications.remove(remove);
        }
    }
}