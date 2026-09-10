package ru.yandex.practicum.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.dto.CommentDto;
import ru.yandex.practicum.service.impl.CommentServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/events/comments")

public class CommentController {

    private final CommentServiceImpl service;

    @PostMapping
    public CommentDto postComment(@RequestBody @Valid CommentDto dto, @RequestHeader(name = "eventId") @NotNull Long eventId,
                                  @RequestHeader(name = "X-Sharer-User-Id") @NotNull Long userID) {
        return service.postComment(eventId, userID, dto);
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable @NotNull Long commentId) {
        return service.getById(commentId);
    }

    @GetMapping("/byEvent/{eventId}")
    public List<CommentDto> getCommentByEvent(@PathVariable @NotNull Long eventId) {
        return service.finCommentByEvent(eventId);
    }

    @GetMapping("/byUser/{userId}")
    public List<CommentDto> getCommentByUSer(@PathVariable @NotNull Long userId) {
        return service.findCommentByCommentator(userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable @NotNull Long commentId, @RequestBody CommentDto dto) {
        return service.updateComment(dto, commentId);
    }

    @DeleteMapping
    public void delete(@RequestParam @NotNull Long id) {
        service.deleteComment(id);
    }
}
