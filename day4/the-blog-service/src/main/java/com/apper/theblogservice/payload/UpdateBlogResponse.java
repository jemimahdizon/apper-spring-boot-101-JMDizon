package com.apper.theblogservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateBlogResponse {

    private String blogId;

    private String bloggerId;

    @JsonProperty("created_at")
    private LocalDateTime dateCreated;

    @JsonProperty("last_updated")
    private LocalDateTime dateUpdated;
}
