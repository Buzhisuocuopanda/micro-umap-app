package com.mkst.umap.app.admin.domain;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ArraignRoomPlus {

    private Long id;

    private LocalTime amStartTime;
    private LocalTime amEndTime;
    private LocalTime pmStartTime;
    private LocalTime pmEndTime;
    private Long timeSpan;
}
