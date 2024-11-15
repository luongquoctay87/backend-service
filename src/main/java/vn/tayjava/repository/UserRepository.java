package vn.tayjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tayjava.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
