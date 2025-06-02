package com.example.newspeed.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 공통 설정을 관리하는 설정 클래스
 * QueryDSL, 공통 빈 등 애플리케이션 전반에 걸쳐 사용되는 설정을 정의합니다.
 *
 * @author 이준영
 */
@Configuration
public class AppConfig {

    private final EntityManager entityManager;

    /**
     * AppConfig 생성자
     *
     * @param entityManager JPA 엔티티 매니저
     */
    public AppConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * QueryDSL을 사용하기 위한 JPAQueryFactory 빈을 생성합니다.
     * 동적 쿼리 생성을 위한 QueryDSL 설정을 제공합니다.
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
