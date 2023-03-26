package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // 기본적으로 rollback true임
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false) false로 하면 db 내역 확인 가능
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long joinId = memberService.join(member);
        em.flush(); // flush해주면 실제 db에 commit이 되는거임(그래서 인서트 쿼리 볼수있어)

        //then
        Assertions.assertThat(member).isEqualTo(memberService.findOne(joinId));
    }

    @Test
    public void 중복회원예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);

        //then(junit5에서는 아래처럼 써야데)
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }
}