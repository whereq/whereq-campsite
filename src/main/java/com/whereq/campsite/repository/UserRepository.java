package com.whereq.campsite.repository;

import com.whereq.campsite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByEmailIgnoreCase(String email);

}
