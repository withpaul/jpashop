package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// jpa는 데이터 변경이 있으면 무조건 넣어야 한다. 그래야 lazy등등을 쓸수있으
// readonly를 넣으면 읽기에 대해 최적화해주고, 이 서비스는 읽기가 많으니까 이렇게 넣은거임
@Transactional(readOnly = true)
@RequiredArgsConstructor // final에 가진것만 필드만 롬복이 보고 생성자를 만들어주고 주입해줌
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional // 데이터 수정 및 변경등 readonly true넣으면 안됨. 그래서 따로넣었음
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하느 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    @Transactional // jpa는 데이터 변경이 있으면 무조건 넣어야 한다. 그래야 lazy등등을 쓸수있으
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 1건 조회
     */
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
