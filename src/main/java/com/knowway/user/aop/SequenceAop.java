package com.knowway.user.aop;

import com.knowway.user.annotation.InjectSequenceValue;
import com.knowway.user.entity.Member;
import com.knowway.user.repository.MemberRepository;
import jakarta.persistence.Entity;
import java.lang.reflect.Field;
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
    public void generateSequence(JoinPoint joinPoint){

        Object [] aragumentList=joinPoint.getArgs();
        for (Object arg :aragumentList ) {
            if (arg.getClass().isAnnotationPresent(Entity.class)){

                Field[] fields = arg.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(InjectSequenceValue.class)) {

                        field.setAccessible(true); 
                        try {
                            if (field.get(arg) == null){
                                String sequenceName=field.getAnnotation(InjectSequenceValue.class).sequencename();
                                long nextval=getNextValue(sequenceName);
                                field.set(arg, nextval);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    /**
     * This method fetches the next value from sequence
     * @param sequence
     * @return
     */

    public long getNextValue(String sequence){
        long sequenceNextVal=0L;

        SqlRowSet sqlRowSet= jdbcTemplate.queryForRowSet("SELECT "+sequence+".NEXTVAL as value FROM DUAL");
        while (sqlRowSet.next()){
            sequenceNextVal=sqlRowSet.getLong("value");
        }
        return  sequenceNextVal;
    }
}