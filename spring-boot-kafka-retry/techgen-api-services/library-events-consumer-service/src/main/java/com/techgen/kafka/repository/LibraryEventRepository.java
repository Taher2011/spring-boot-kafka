package com.techgen.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techgen.kafka.entity.LibraryEvent;

public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Integer> {

}
