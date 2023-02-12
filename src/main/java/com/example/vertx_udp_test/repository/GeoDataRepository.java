package com.example.vertx_udp_test.repository;

import com.example.vertx_udp_test.dto.GeoDataDto;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;

public class GeoDataRepository {
  PgConnectOptions connectOptions = new PgConnectOptions()
    .setPort(5432)
    .setHost("localhost")
    .setDatabase("postgres")
    .setUser("postgres")
    .setPassword("postgres");

  PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

  public Future<RowSet<Row>> save(Vertx vertx, GeoDataDto geoDataDto) {
    SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);

    return client
      .preparedQuery("INSERT INTO geodata (mac, date_time, latitude, longitude) VALUES ($1, $2, $3, $4)")
      .execute(Tuple.of(
        geoDataDto.getMac(),
        geoDataDto.getDateTime(),
        geoDataDto.getLatitude(),
        geoDataDto.getLongitude()));
  }
}

