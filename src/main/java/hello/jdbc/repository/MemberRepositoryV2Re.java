package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV2Re {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;


        String sql = "insert into member (member_id, money) values(?, ?)";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, member.getMemberId());
            stmt.setInt(2, member.getMoney());
        } catch (SQLException e) {
            log.info("error", e);
            throw e;
        } finally {
            close(conn, stmt, null);
        }
        return member;

    }

    public Member findById(String memberId) throws SQLException{

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select * from member where member_id = ?";

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            rs = stmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new IllegalStateException("No Found Member : " + memberId);
            }
        } catch (SQLException e) {
            log.info("error", e);
            throw e;
        } finally {
            close(conn, stmt, rs);
        }
    }

    public Member findById(Connection conn, String memberId) throws SQLException{

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "select * from member where member_id = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            rs = stmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new IllegalStateException("No Found Member : " + memberId);
            }
        } catch (SQLException e) {
            log.info("error", e);
            throw e;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
        }
    }

    public void update(Connection conn, String memberId, int money) throws SQLException {

        PreparedStatement stmt = null;
        String sql = "update member set money = ? where member_id = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, money);
            stmt.setString(2, memberId);
            int count = stmt.executeUpdate();
        } catch (SQLException e) {
            log.info("error", e);
            throw e;
        } finally {
            JdbcUtils.closeStatement(stmt);
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


    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        log.info("get connection={}, class={}", conn, conn.getClass());
        return conn;
    }

    private void close(Connection conn, PreparedStatement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(conn);
    }

}
