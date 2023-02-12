package com.example.vertex_test;

import com.example.vertx_udp_test.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  private final Buffer buffer;

  {
    buffer = Buffer.buffer();
    buffer.setBytes(0, new byte[6]);
    buffer.setByte(6, (byte) 1);
    buffer.setInt(7, 45);
    buffer.setLong(11, System.currentTimeMillis());
    buffer.setDouble(19, 44.123456);
    buffer.setDouble(27, 24.123456);
  }


  @BeforeEach
  void deployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticleDeployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  void sendReply(Vertx vertx, VertxTestContext testContext) {
    DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());
    socket.listen(7001, "localhost", result -> {
      assertTrue(result.succeeded());
        socket.handler(packet -> {
          Buffer data = packet.data();
          assertEquals(2, data.getByte(6));
          testContext.checkpoint().flag();
        });
    });
    socket.send(buffer, 7000, "localhost", ar -> {
      assertTrue(ar.succeeded());
    });
  }
}
