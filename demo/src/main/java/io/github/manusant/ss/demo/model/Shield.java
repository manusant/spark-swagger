package io.github.manusant.ss.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shield {
    private String id;
    private String name;
    private String owner;
    private int defense;
}
