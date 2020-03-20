package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

//    Optional<Program> findByProgramCd(String programCd);
}
