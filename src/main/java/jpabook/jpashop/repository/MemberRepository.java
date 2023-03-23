package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // final로 되어있는것들 주입
public class MemberRepository {

//    @PersistenceContext // jpa entity manager를 스프링이 주입해줌
//    스프링데이터 jpa를 쓰면 @PersistenceContext를 @Autowired로 쓸수있게해줘서 2번처럼 쓸수있음
//    private EntityManager em;

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // 나중에 커밋될떄 그때 insert 가 날아가
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // entity member를 조회해 이런거임 테이블이 아님(m은 alias라고 생각하면됨)
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
