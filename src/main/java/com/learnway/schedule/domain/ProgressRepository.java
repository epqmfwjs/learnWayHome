package com.learnway.schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long>{

	void deleteByScheduleId(Schedule schedule);
}
