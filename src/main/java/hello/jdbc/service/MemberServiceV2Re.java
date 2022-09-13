package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV2Re;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2Re {

    private final DataSource dataSource;
    private final MemberRepositoryV2Re memberRepository;

    public void accountTransfer(String fromMemberId, String toMemberId, int money) throws SQLException {

        Connection conn = dataSource.getConnection();

        try {
            conn.setAutoCommit(false);

            Member fromMember = memberRepository.findById(conn, fromMemberId);
            Member toMember = memberRepository.findById(conn, toMemberId);
            validation(toMember);
            memberRepository.update(conn, fromMemberId, fromMember.getMoney() - money);
            memberRepository.update(conn, toMemberId, toMember.getMoney() + money);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(conn);
        }
    }

    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체중 예외 발생");
        }
    }

    private void release(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

}
