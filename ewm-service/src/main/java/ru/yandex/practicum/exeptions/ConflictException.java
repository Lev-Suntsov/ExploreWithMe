package ru.yandex.practicum.exeptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String massage) {
        super(massage);
    }
}
