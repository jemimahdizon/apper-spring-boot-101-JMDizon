package com.apper.theblogservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetAllBlogsByBloggerResponse {

    private String title;
    private  String body;

    @JsonProperty("created_at")
    private LocalDateTime dateCreated;

    @JsonProperty("last_updated")
    private LocalDateTime dateUpdated;
}
