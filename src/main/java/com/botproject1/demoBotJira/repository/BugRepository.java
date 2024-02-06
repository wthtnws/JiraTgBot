package com.botproject1.demoBotJira.repository;

import com.botproject1.demoBotJira.entity.BugEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface BugRepository extends JpaRepository<BugEntity, UUID> {
}
