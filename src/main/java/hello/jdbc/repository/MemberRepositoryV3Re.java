package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV3Re {

    private final DataSource dataSource;

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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    private Connection getConnection() throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", conn, conn.getClass());
        return conn;
    }

}
