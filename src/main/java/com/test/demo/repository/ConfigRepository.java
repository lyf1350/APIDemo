package com.test.demo.repository;

import com.test.demo.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config,Long> {
    Config findByConfigName(String configName);
    List<Config> findAllByType(String type);
}
