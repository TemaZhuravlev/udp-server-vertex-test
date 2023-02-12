package com.example.vertx_udp_test.mapper;

import com.example.vertx_udp_test.dto.GeoDataDto;
import io.vertx.core.buffer.Buffer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DataToDtoMapper {

  public static GeoDataDto map(Buffer data) {
    GeoDataDto geoDataDto = new GeoDataDto();

    byte[] mac = new byte[6];
    data.getBytes(0, 6, mac);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
      sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
    }
    LocalDateTime localDateTime = Instant.ofEpochMilli(data.getLong(11)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    geoDataDto.setMac(sb.toString());
    geoDataDto.setDateTime(localDateTime);
    geoDataDto.setLatitude(data.getDouble(19));
    geoDataDto.setLongitude(data.getDouble(27));
    return geoDataDto;
  }
}
