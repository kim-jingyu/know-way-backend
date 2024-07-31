package com.knowway.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.knowway.record.entity.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

}
