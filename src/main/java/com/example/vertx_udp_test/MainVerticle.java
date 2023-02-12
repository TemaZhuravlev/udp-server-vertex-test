package com.example.vertx_udp_test;

import com.example.vertx_udp_test.dto.GeoDataDto;
import com.example.vertx_udp_test.mapper.DataToDtoMapper;
import com.example.vertx_udp_test.repository.GeoDataRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramPacket;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {
  private final GeoDataRepository geoDataRepository = new GeoDataRepository();
  private static final int PORT = 7000;
  private static final String HOST = "localhost";

  @Override
  public void start() {
    DatagramSocket socket = vertx.createDatagramSocket(new DatagramSocketOptions());

    socket.listen(PORT, HOST, asyncResult -> {
      if (asyncResult.succeeded()) {
        log.info("Server is listening on port: {} host: {}", PORT, HOST);
        socket.handler(packet -> datagramHandler(socket, packet));
      } else {
        log.error("Listen failed ", asyncResult.cause());
      }
    });
  }

  private void datagramHandler(DatagramSocket socket, DatagramPacket packet) {
    Buffer data = packet.data();
    if (data.length() > 6 && data.getByte(6) == 1) {
      GeoDataDto geoDataDto = DataToDtoMapper.map(data);
      geoDataRepository.save(vertx, geoDataDto)
        .andThen(asyncResult -> sendReply(socket, packet, asyncResult));
      log.debug("Send geoData to DB");
    }
  }

  private void sendReply(DatagramSocket socket, DatagramPacket packet, AsyncResult<RowSet<Row>> asyncResult) {
    if (asyncResult.succeeded()) {
      log.debug("Save geoData in DB complete");
      Buffer buffer = Buffer.buffer();
      Buffer data = packet.data();
      buffer.setBytes(0, data.getBytes(0, 6));
      buffer.setByte(6, (byte) 2);
      buffer.setInt(7, data.getInt(7));

      socket.send(buffer, packet.sender().port(), packet.sender().host(), ar -> {
        log.debug("Send succeeded = {}", ar.succeeded());
      });
    } else {
      log.error("Failure: " + asyncResult.cause().getMessage());
    }
  }
}
