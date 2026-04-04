package com.coffeeshopsystem.domain.userpoint.entity;

import com.coffeeshopsystem.common.entity.BaseEntity;
import com.coffeeshopsystem.domain.userpoint.enums.PointHistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "point_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PointHistoryType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPoint userPoint;

    public static PointHistory charge(BigDecimal amount, UserPoint userPoint) {
        PointHistory pointHistory = new PointHistory();

        pointHistory.amount = amount;
        pointHistory.userPoint = userPoint;
        pointHistory.type = PointHistoryType.CHARGE;
        return pointHistory;
    }

    public static PointHistory use(BigDecimal amount, UserPoint userPoint) {
        PointHistory pointHistory = new PointHistory();

        pointHistory.amount = amount.negate();
        pointHistory.userPoint = userPoint;
        pointHistory.type = PointHistoryType.USE;
        return pointHistory;
    }
}
