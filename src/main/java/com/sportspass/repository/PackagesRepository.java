package com.sportspass.repository;

import com.sportspass.dms.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PackagesRepository extends JpaRepository<Packages,Long> {

    @Query(value = "SELECT * FROM packages WHERE active = 1",nativeQuery = true)
    List<Packages> findAllByActiveTrue();

}
