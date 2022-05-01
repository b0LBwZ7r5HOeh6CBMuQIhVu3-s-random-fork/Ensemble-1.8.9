package it.fktcod.ktykshrk.module.mods;

import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import it.fktcod.ktykshrk.module.HackCategory;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.utils.TimerUtils;
import it.fktcod.ktykshrk.utils.system.Connection;
import it.fktcod.ktykshrk.utils.visual.ChatUtils;
import it.fktcod.ktykshrk.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class PingSpoof extends Module
{
    public NumberValue pingval;
    private static final ArrayList<Packet> packets;
    private Packet sendPacket;
    private final TimerUtils timer;
    
    public PingSpoof() {
        super("PingSpoof", HackCategory.ANOTHER);
        this.pingval = new NumberValue("Packets Delay", 350.0d, 100.0d, 5000.0d);
        this.sendPacket = null;
        this.timer = new TimerUtils();
        this.addValue(this.pingval);
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        //System.out.println(packet);

        if (this.sendPacket != null && packet instanceof C00PacketKeepAlive && packet == this.sendPacket) {
            this.sendPacket = null;
        }
        else if (packet instanceof C00PacketKeepAlive) {
            PingSpoof.packets.add((Packet)packet);
            EventPacket C00 = new EventPacket(EventType.SEND,packet);
            C00.setCancelled(true);
            return true;
        }
        return false;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        ChatUtils.message("Ping Spoof " + String.valueOf(this.pingval.getValue()));
        if (this.timer.hasReached(this.pingval.getValue().floatValue()) && PingSpoof.packets.size() >= 1) {
            this.sendPacket = PingSpoof.packets.get(0);
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(PingSpoof.packets.get(0));
            PingSpoof.packets.remove(0);
            timer.reset();
        }
    }
    
    static {
        packets = new ArrayList<Packet>();
    }
}