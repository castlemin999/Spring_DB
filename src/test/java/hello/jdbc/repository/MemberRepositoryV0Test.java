package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member("test1", 1000000);

        // when
        Member savedMember = memberRepository.save(member);

        log.info("Saved Member = {}", member);

        //then
        assertThat(member).isEqualTo(savedMember);
    }

    @Test
    public void 조회() throws Exception {
        // given
        Member member = new Member("test3", 1000000);
        Member saveMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById("test3");
        log.info("Find Member = {}", findMember);

        //then
        assertThat(saveMember).isEqualTo(findMember);

    }

}