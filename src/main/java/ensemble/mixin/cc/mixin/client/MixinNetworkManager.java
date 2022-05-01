package ensemble.mixin.cc.mixin.client;

import ensemble.mixin.cc.mixin.interfaces.INetworkManager;
import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import it.fktcod.ktykshrk.event.EventPacket;
import it.fktcod.ktykshrk.eventapi.EventManager;
import it.fktcod.ktykshrk.eventapi.types.EventType;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager implements INetworkManager {

    @Shadow
    private Channel channel;
    @Final
    @Shadow
    private Queue outboundPacketsQueue;



    @Inject(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet, CallbackInfo ci) {
        EventPacket event = new EventPacket(EventType.RECIEVE, packet);
        EventManager.call(event);

        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet packetIn, CallbackInfo ci) {
        EventPacket event = new EventPacket(EventType.SEND, packetIn);
        EventManager.call(event);

        if (event.isCancelled()) ci.cancel();
    }




    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Shadow
    public abstract boolean isChannelOpen();


    @Override
    public void sendPacketNoEvent(Packet packet) {
        if (channel != null && channel.isOpen()) {
            flushOutboundQueue();
            dispatchPacket(packet, null);
        } else {
            outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packet, (GenericFutureListener[]) null));
        }
    }

    @Override
    public void sendPacketSilent(Packet packetIn) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[]) null));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }





    @Shadow
    protected abstract void dispatchPacket(Packet a, GenericFutureListener[] a2);

    @Shadow
    protected abstract void flushOutboundQueue();


}


class InboundHandlerTuplePacketListener {
    private final Packet packet;
    private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

    @SafeVarargs
    public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
        this.packet = inPacket;
        this.futureListeners = inFutureListeners;
    }
}

