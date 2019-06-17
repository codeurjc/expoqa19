package es.codeurjc.test.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public Optional<List<Message>> findByTitle(String title);
}