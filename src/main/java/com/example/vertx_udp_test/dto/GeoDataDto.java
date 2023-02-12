package com.example.vertx_udp_test.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GeoDataDto {

  private String mac;
  private LocalDateTime dateTime;
  private double latitude;
  private double longitude;
}
