package com.inorg.rewardAndRecognition.userPortal.repository;
import com.inorg.rewardAndRecognition.userPortal.Utility.NominationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class NominateRepository {
    private final DataSource dataSource;

    public Boolean giveReward(String nominatorId, List<NominationHelper>nominations) throws Exception{
        String sql = "INSERT INTO nominations (nomineeid, nominatorid, rewardid, justification) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (NominationHelper nomination : nominations) {
                    pstmt.setString(1, nomination.getId());
                    pstmt.setString(2, nominatorId);
                    pstmt.setInt(3, nomination.getRewardId());
                    pstmt.setString(4, nomination.getJustification());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new SQLException("Failed to give reward", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return false;
        }
    }

}
