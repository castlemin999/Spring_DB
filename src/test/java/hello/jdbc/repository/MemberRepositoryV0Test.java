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

    @Test
    public void 수정() throws Exception{
        // given
        Member member = new Member("test4", 1000);
        Member saveMember = memberRepository.save(member);

        // when
        int updateCount = memberRepository.update("test4", 200000);
        Member findMember = memberRepository.findById("test4");

        // then
        assertThat(updateCount).isEqualTo(1);
        assertThat(findMember.getMoney()).isEqualTo(200000);
    }

    @Test
    public void 삭제() throws Exception{
        // given
        Member member = new Member("test5", 1000);
        Member saveMember = memberRepository.save(member);

        // when
        int deleteCount = memberRepository.delete("test5");

        // then
        assertThat(deleteCount).isEqualTo(1);

    }

}