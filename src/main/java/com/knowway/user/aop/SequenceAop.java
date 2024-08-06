package com.knowway.user.aop;

import com.knowway.user.annotation.InjectSequenceValue;
import com.knowway.user.entity.Member;
import com.knowway.user.repository.MemberRepository;
import jakarta.persistence.Entity;
import java.lang.reflect.Field;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@RequiredArgsConstructor
@Aspect
@Configuration
public class SequenceAop {

    private final JdbcTemplate jdbcTemplate;

    @Before("execution(* org.springframework.data.repository.CrudRepository.save(..))")
    public void generateSequence(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (isEntityWithSequence(arg)) {
                setSequenceValue(arg);
            }
        }
    }

    private boolean isEntityWithSequence(Object arg) {
        return arg.getClass().isAnnotationPresent(Entity.class) &&
               Arrays.stream(arg.getClass().getDeclaredFields())
                     .anyMatch(field -> field.isAnnotationPresent(InjectSequenceValue.class));
    }

    private void setSequenceValue(Object entity) {
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectSequenceValue.class)) {
                field.setAccessible(true);
                try {
                    if (field.get(entity) == null) {
                        String sequenceName = field.getAnnotation(InjectSequenceValue.class).sequencename();
                        long nextVal = getNextValue(sequenceName);
                        field.set(entity, nextVal);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private long getNextValue(String sequence) {
        String sql = "SELECT " + sequence + ".NEXTVAL as value FROM DUAL";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
