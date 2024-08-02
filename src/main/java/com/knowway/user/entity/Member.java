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
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "member",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_member_chat_id", columnNames = "member_chat_id")
    },
    indexes = {
        @Index(name = "idx_member_chat_id", columnList = "member_chat_id")
    }
)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", length = 10, nullable = false)
    private Role role;

    @SequenceGenerator(name = "member_chat_id_seq", sequenceName = "member_chat_id_seq", allocationSize = 1)
    @Column(name = "member_chat_id", nullable = false)
    private Long chatId;

    @OneToMany(mappedBy = "member")
    private List<Point> pointList;

    @OneToMany(mappedBy = "member")
    private List<Record> recordList;
}
