package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, member.getMemberId());
            stmt.setInt(2, member.getMoney());
            stmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(conn, stmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException {

        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("No found Member : " + memberId);
            }

        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(conn, stmt, rs);
        }
    }

    public int update(String memberId, int money) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        String sql = "update member set money = ? where member_id = ?";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, money);
            stmt.setString(2, memberId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally{
            close(conn, stmt, null);
        }
    }

    public int delete(String memberId) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;

        String sql = "delete from member where member_id = ?";
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally{
            close(conn, stmt, null);
        }
    }

    private void close(Connection conn, PreparedStatement stmt, ResultSet rs) throws SQLException {


        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error : ", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("error : ", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("error : ", e);
            }
        }

    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
