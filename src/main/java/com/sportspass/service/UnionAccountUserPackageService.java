package com.sportspass.service;

import com.sportspass.dms.UnionAccountUserPackages;
import com.sportspass.repository.UnionAccountUserPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnionAccountUserPackageService {

    @Autowired
    UnionAccountUserPackageRepository unionAccountUserPackageRepository;

    public UnionAccountUserPackages save(UnionAccountUserPackages unionAccountUserPackages) {
        return unionAccountUserPackageRepository.save(unionAccountUserPackages);
    }

    public UnionAccountUserPackages findPackageAndAccountUserByIdUnion(Long id) {

        return unionAccountUserPackageRepository.findPackageAndAccountUserByIdUnion(id);
    }
}
