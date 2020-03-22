package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByOutlineContaining(String keyword);

    List<Program> findByDetailContaining(String keyword);

}
