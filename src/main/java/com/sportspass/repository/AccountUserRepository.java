package com.sportspass.repository;

import com.sportspass.dms.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

    @Query(value = "SELECT * FROM account_users au WHERE au.user_id = :userId", nativeQuery = true)
    AccountUser findByUserId(Long userId);

    @Query(value = "SELECT * FROM account_users as au " +
            "WHERE au.user_id = :userId AND au.package_a = :packageId AND au.active = :active",nativeQuery = true)
    AccountUser findByUserIdByPackageAAndActive(@Param("userId") Long userId, @Param("packageId") Long packageId,
                                                @Param("active") boolean active);
    @Query(value = "SELECT * FROM account_users as au WHERE au.user_id = :userId AND au.active = :active",nativeQuery = true)
    List<AccountUser> findAllByUserIdAndActive(@Param("userId") Long id, @Param("active") boolean active);
}
