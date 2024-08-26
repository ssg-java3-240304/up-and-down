package com.up.and.down.user.member.repository;

import com.up.and.down.user.admin.dto.BrowserCountDto;
import com.up.and.down.user.admin.dto.VisitCountDto;
import com.up.and.down.user.member.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    @Query("""
        select new com.up.and.down.user.admin.dto.VisitCountDto(
            concat(YEAR(min(i.loginTime)), '-', lpad(cast(MONTH(min(i.loginTime)) as string), 2, '0')),
            count(i.userId))
        from LoginInfo i
        where i.loginTime >= :sixMonthsAgo
        group by YEAR(i.loginTime), MONTH(i.loginTime)
        order by YEAR(i.loginTime), MONTH(i.loginTime)
        """)
    List<VisitCountDto> findVisitCountsByMonth(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);

    @Query("""
            select new com.up.and.down.user.admin.dto.BrowserCountDto(
                i.browserInfo,
                COUNT(i.userId))
            from LoginInfo i
            where i.loginTime >= :sixMonthsAgo
            group by i.browserInfo
            order by i.browserInfo
        """)
    List<BrowserCountDto> findBrowserCounts(@Param("sixMonthsAgo") LocalDateTime sixMonthsAgo);

    @Query("""
            select new com.up.and.down.user.admin.dto.VisitCountDto(
                concat(YEAR(min(i.loginTime)), '-', lpad(cast(MONTH(min(i.loginTime)) as string), 2, '0')),
                count(i.userId))
            from
                LoginInfo i
            where
                i.loginTime between :startDate and :endDate
            group by YEAR(i.loginTime), MONTH(i.loginTime)
            order by YEAR(i.loginTime), MONTH(i.loginTime)
        """)
    List<VisitCountDto> getVisitCounts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            select new com.up.and.down.user.admin.dto.BrowserCountDto(
                i.browserInfo,
                COUNT(i.userId))
            from
                LoginInfo i
            where
                i.loginTime between :startDate and :endDate
            group by i.browserInfo
            order by i.browserInfo
        """)
    List<BrowserCountDto> getBrowserCounts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
