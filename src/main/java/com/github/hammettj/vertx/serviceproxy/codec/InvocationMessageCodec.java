package com.github.hammettj.vertx.serviceproxy.codec;

import com.github.hammettj.vertx.serviceproxy.Invocation;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class InvocationMessageCodec implements MessageCodec<Invocation, Invocation> {
    public static final InvocationMessageCodec CODEC = new InvocationMessageCodec();

    private InvocationMessageCodec() {
    }

    @Override
    public void encodeToWire(Buffer buffer, Invocation invocation) {
        throw new RuntimeException("not supported");
    }

    @Override
    public Invocation decodeFromWire(int pos, Buffer buffer) {
        throw new RuntimeException("not supported");
    }

    @Override
    public Invocation transform(Invocation invocation) {
        return invocation;
    }

    @Override
    public String name() {
        return "invocation";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
