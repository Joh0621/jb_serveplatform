package com.bonc.jibei.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Measurement(name = "iot_tmp")
@JsonIgnoreProperties("time")
public class Influx {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    @Column(name = "time")
    private Instant time;
    @Column(name = "value")
    private Double value;
    @Column(name = "code")
    private String code;

    public LocalDateTime getDatetime() {
        return LocalDateTime.ofInstant(time, ZoneId.systemDefault());
    }

}
