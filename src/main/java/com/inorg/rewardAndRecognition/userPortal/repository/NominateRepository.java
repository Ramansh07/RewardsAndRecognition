package com.inorg.rewardAndRecognition.userPortal.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Repository
@RequiredArgsConstructor
public class NominateRepository {
    private final DataSource dataSource;

    public Boolean giveReward(String nomineeId, String nominatorId, int rewardId, String justification) {
        String sql = "INSERT INTO nominations (nomineeid, nominatorid, rewardid, justification) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomineeId);
            pstmt.setString(2, nominatorId);
            pstmt.setInt(3, rewardId);
            pstmt.setString(4, justification);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
//            throw new TransactionException("Failed to give reward", e);
        }
        return false;
    }

}
