package com.ssafy.gallery.redis.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "token")
@AllArgsConstructor
@Builder
@ToString
public class LoginTokenDto {

    @Id
    private String id;
    private int userId;
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

}
