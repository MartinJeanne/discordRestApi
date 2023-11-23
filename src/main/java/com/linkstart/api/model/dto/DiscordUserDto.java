package com.linkstart.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiscordUserDto {
    private Long id;
    private String discordId;
    private String avatarURL;
    private String tag;
    private LocalDate birthday;
}
