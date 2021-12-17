package kea.sem3.jwtdemo.repositories;

import kea.sem3.jwtdemo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    @Query("select m from Member m where m.user.username = :userName")
    Optional<Member> findByUserName(String userName);

    @Query("select (count(m) > 0) from Member m where m.user.username = :userName")
    boolean userExist(String userName);









}
