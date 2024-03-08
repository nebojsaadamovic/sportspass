package com.sportspass.repository;


import com.sportspass.dms.UnionAccountUserPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UnionAccountUserPackageRepository  extends JpaRepository<UnionAccountUserPackages, Long> {

    @Query(value = "SELECT uap FROM UnionAccountUserPackages uap " +
            "LEFT JOIN FETCH uap.packages p " +
            "LEFT JOIN FETCH uap.accountUser au " +
            "LEFT JOIN FETCH au.user users " +
            "WHERE uap.id = :id")
    UnionAccountUserPackages findPackageAndAccountUserByIdUnion(@Param("id") Long id);





}
