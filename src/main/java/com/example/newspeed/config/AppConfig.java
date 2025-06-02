package com.example.newspeed.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>애플리케이션 공통 설정</p>
 */
@Configuration
public class AppConfig {

    private final EntityManager entityManager;

    public AppConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * QueryDSL을 사용하기 위한 JPAQueryFactory 빈을 생성
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
