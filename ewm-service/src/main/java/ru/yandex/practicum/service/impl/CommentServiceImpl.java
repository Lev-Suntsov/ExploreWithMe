package ru.yandex.practicum.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exeptions.NotFoundException;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Event;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.dto.CommentDto;
import ru.yandex.practicum.model.mapper.Mapper;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository repository;

    @Override
    public CommentDto postComment(Long eventId, Long userid, CommentDto dto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("События с id = " +
                eventId + " не существует"));

        User user = userRepository.findById(userid).orElseThrow(() -> new  NotFoundException("пользователь с " +
                "id = " + userid + " не найден"));

        dto.setCommentator(Mapper.toUserDto(user));
        dto.setEvent(Mapper.toEventDto(event));

        return Mapper.commentEntityToDto(repository.save(Mapper.commentDtoToEntity(dto)));
    }

    @Override
    public CommentDto getById(Long id) {
       return Mapper.commentEntityToDto(repository.findById(id).orElseThrow(() -> new NotFoundException("комментарий с " +
               "id = " + id + " не найден")));
    }

    @Override
    public List<CommentDto> finCommentByEvent(Long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("События с id = " +
                eventId + " не существует"));

        List<Comment> comments = repository.findAllByEvent_Id(eventId);

        return comments.stream().map(Mapper::commentEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findCommentByCommentator(Long commentatorID) {
        userRepository.findById(commentatorID).orElseThrow(() -> new  NotFoundException("пользователь с " +
                "id = " + commentatorID + " не найден"));

        List<Comment> comments = repository.findAllByCommentator_Id(commentatorID);

        return comments.stream().map(Mapper::commentEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentID) {
        repository.findById(commentID).orElseThrow(() -> new NotFoundException("комментарий с " +
                "id = " + commentID + " не найден"));

        repository.deleteById(commentID);
    }

    @Override
    public CommentDto updateComment(CommentDto dto, Long commentID) {

       CommentDto newDto = Mapper.commentEntityToDto(repository.findById(commentID).orElseThrow(() -> new NotFoundException("комментарий с " +
                "id = " + commentID + " не найден")));

        if(dto.getCommentator() != null) {
            userRepository.findById(dto.getCommentator().getId()).orElseThrow(() -> new  NotFoundException("пользователь с " +
                    "id = " + dto.getCommentator().getId() + " не найден"));
            newDto.setCommentator(dto.getCommentator());
        }

        if(dto.getText() != null || !dto.getText().isBlank()) {
            newDto.setText(dto.getText());
        }

        if(dto.getEvent() != null) {
            eventRepository.findById(dto.getEvent().getId()).orElseThrow(() -> new NotFoundException("События с id = " +
                    dto.getEvent().getId() + " не существует"));

            newDto.setEvent(dto.getEvent());
        }

        return newDto;
    }
}
