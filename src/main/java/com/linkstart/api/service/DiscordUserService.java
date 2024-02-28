package com.linkstart.api.service;

import com.linkstart.api.exception.NoColumnsException;
import com.linkstart.api.exception.NoContentException;
import com.linkstart.api.exception.NotFoundException;
import com.linkstart.api.model.dto.PlaylistDto;
import com.linkstart.api.model.entity.DiscordServer;
import com.linkstart.api.model.entity.DiscordUser;
import com.linkstart.api.model.dto.DiscordUserDto;
import com.linkstart.api.repo.DiscordServerRepo;
import com.linkstart.api.repo.DiscordUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DiscordUserService {
    private final DiscordUserRepo discordUserRepo;
    private final PlaylistService playlistService;
    private final DiscordServerRepo discordServerRepo;
    private final ModelMapper modelMapper;

    public DiscordUserService(
            DiscordUserRepo discordUserRepo,
            PlaylistService playlistService,
            ModelMapper modelMapper,
            DiscordServerRepo discordServerRepo) {
        this.discordUserRepo = discordUserRepo;
        this.playlistService = playlistService;
        this.modelMapper = modelMapper;
        this.discordServerRepo = discordServerRepo;
    }

    public List<DiscordUserDto> getDiscordUsers() {
        List<DiscordUser> discordUsers = discordUserRepo.findAll();
        return discordUsers
                .stream()
                .map(user -> modelMapper.map(user, DiscordUserDto.class))
                .toList();
    }

    public List<DiscordUserDto> getDiscordUsersBirthdayIsToday() {
        List<DiscordUserDto> discordUsers = getDiscordUsers();
        LocalDate today = LocalDate.now();

        List<DiscordUserDto> discordUsersBirthdayIsNow = new ArrayList<>();
        for (DiscordUserDto user : discordUsers) {
            if (user.getBirthday() == null) continue;

            LocalDate userBirthday = user.getBirthday();
            // To compare dates, we set them to the same year
            userBirthday = userBirthday.withYear(today.getYear());

            if (userBirthday.equals(today)) {
                discordUsersBirthdayIsNow.add(user);
            }
        }
        return discordUsersBirthdayIsNow;
    }

    public DiscordUserDto getDiscordUserByDiscordId(String id) {
        DiscordUser discordUser = discordUserRepo.findByDiscordId(id).orElseThrow(NoContentException::new);
        return modelMapper.map(discordUser, DiscordUserDto.class);
    }

    public DiscordUserDto createDiscordUser(DiscordUserDto discordUserDto) {
        Optional<DiscordServer> discordServer = discordServerRepo.findByDiscordId(discordUserDto.getDiscordServerId());
        if (discordServer.isEmpty())
            throw new NotFoundException(discordUserDto.getDiscordServerId()+ " DiscordServerId");

        DiscordUser discordUser = modelMapper.map(discordUserDto, DiscordUser.class);

        discordUserRepo.save(discordUser);
        return modelMapper.map(discordUser, DiscordUserDto.class);
    }

    public DiscordUserDto updateDiscordUser(Long id, DiscordUserDto discordUserDto) {
        discordUserRepo.findById(id).orElseThrow(NoContentException::new);
        DiscordUser updatedDiscordUser = discordUserRepo.save(modelMapper.map(discordUserDto, DiscordUser.class));
        //updatedDiscordUser.setBirthday(LocalDate.of(2023, 11, 23));
        return modelMapper.map(updatedDiscordUser, DiscordUserDto.class);
    }

    public ResponseEntity<HttpStatus> deleteDiscordUser(Long id) {
        DiscordUser discordUser = discordUserRepo.findById(id).orElseThrow(NoContentException::new);
        discordUserRepo.delete(discordUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public List<PlaylistDto> getPlaylistsByDiscordUser(String id) {
        DiscordUserDto discordUserDto = this.getDiscordUserByDiscordId(id);
        DiscordUser discordUser = modelMapper.map(discordUserDto, DiscordUser.class);
        return playlistService.getPlaylistsByDiscordUser(discordUser);
    }
}
