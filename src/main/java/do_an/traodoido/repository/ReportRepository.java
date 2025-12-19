package do_an.traodoido.repository;

import do_an.traodoido.entity.Report;
import do_an.traodoido.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("""
        SELECT COUNT(r)
        FROM Report r
        WHERE r.postId = :postId
          AND r.status = :status
    """)
    int  getReportsByPostId(
            @Param("postId") Long postId,
            @Param("status") ReportStatus status
    );
    @Query("""
        SELECT r.reason
        FROM Report r
        WHERE r.postId = :postId
          AND r.status = :status
    """)
    List<String> getReportReasonsByPostId(
            @Param("postId") Long postId,
            @Param("status") ReportStatus status
    );
}
