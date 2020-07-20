package io.github.manusant.ss.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackupRequest {
    private String name;
    private String network;
}
