package com.apper.theblogservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBlogRequest {

    @NotBlank(message = "`Title` is required")
    private String title;

    @NotBlank(message = "`Body` is required")
    private  String body;

    @JsonProperty("blogger_id")
    @NotBlank(message = "`bloggerId` is required")
    private String bloggerId;
}
