package com.knowway.user.entity;


import com.knowway.common.entity.BaseEntity;
import com.knowway.point.entity.Point;
import com.knowway.record.entity.Record;
import com.knowway.user.vo.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 10, nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Point> pointList;

    @OneToMany(mappedBy = "member")
    private List<Record> recordList;






}
