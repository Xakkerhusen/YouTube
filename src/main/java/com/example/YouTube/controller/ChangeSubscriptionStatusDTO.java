package com.example.YouTube.controller;

import com.example.YouTube.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeSubscriptionStatusDTO {
    private String channelId;
    private Status status;
}
