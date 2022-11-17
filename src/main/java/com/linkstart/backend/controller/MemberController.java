package com.linkstart.backend.controller;

import com.linkstart.backend.exception.NoUserException;
import com.linkstart.backend.exception.UserNotFoundException;
import com.linkstart.backend.model.dto.MemberDto;
import com.linkstart.backend.service.MemberService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MemberDto>> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<MemberDto>> seahMembers(@RequestParam String filter) {
        return memberService.searchMembers(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable("id") long id) {
        return memberService.getMemberById(id);
    }

    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        return memberService.createMember(memberDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable("id") long id, @RequestBody MemberDto memberDto) {
        return memberService.updateMember(id, memberDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMember(@PathVariable("id") long id) {
        return memberService.deleteMember(id);
    }
}
