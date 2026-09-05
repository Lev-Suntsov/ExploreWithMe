package ru.yandex.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.dto.CommentDto;

import java.util.List;

public interface CommentService {

    @Transactional
    CommentDto postComment(Long eventId, Long userId, CommentDto dto);

    CommentDto getById(Long id);

    List<CommentDto> finCommentByEvent(Long eventId);

    List<CommentDto> findCommentByCommentator(Long userid);

    @Transactional
    CommentDto updateComment(CommentDto dto, Long commentID);

    @Transactional
    void deleteComment(Long commentId);
}
