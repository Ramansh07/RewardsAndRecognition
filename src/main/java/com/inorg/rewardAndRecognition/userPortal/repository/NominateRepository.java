package com.inorg.rewardAndRecognition.userPortal.repository;
import com.inorg.rewardAndRecognition.userPortal.Utility.NominationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class NominateRepository {
    private final DataSource dataSource;

    public Boolean giveReward(String nominatorId, List<NominationHelper>nominations) throws Exception{
        String nominationSql = "INSERT INTO nominations (nomineeid, nominatorid, rewardid, justification) VALUES (?, ?, ?, ?)";
        String approvalSql = "INSERT INTO approvals (nominationid, approvalid, approvalLevel, approvalStatus) VALUES (?, ?, 1, 0)";

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement nominationPstmt = conn.prepareStatement(nominationSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement approvalPstmt = conn.prepareStatement(approvalSql)) {

                for (NominationHelper nomination : nominations) {
                    // Insert into nominations table
                    nominationPstmt.setString(1, nomination.getId());
                    nominationPstmt.setString(2, nominatorId);
                    nominationPstmt.setInt(3, nomination.getRewardId());
                    nominationPstmt.setString(4, nomination.getJustification());
                    nominationPstmt.addBatch();
                }
                nominationPstmt.executeBatch();

                try (ResultSet generatedKeys = nominationPstmt.getGeneratedKeys()) {
                    int i = 0;
                    while (generatedKeys.next()) {
                        int nominationId = generatedKeys.getInt(1);
                        NominationHelper nomination = nominations.get(i);

                        // Insert into approvals table
                        approvalPstmt.setInt(1, nominationId);
                        approvalPstmt.setString(2, nominatorId);
                        approvalPstmt.addBatch();

                        i++;
                    }
                }

                approvalPstmt.executeBatch();
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
