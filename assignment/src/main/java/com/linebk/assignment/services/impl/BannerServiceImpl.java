package com.linebk.assignment.services.impl;

import com.linebk.assignment.models.dto.BannerDto;
import com.linebk.assignment.repositories.BannerRepository;
import com.linebk.assignment.services.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public List<BannerDto> getBannerByUserId(String userId) {
        log.info("BannerServiceImpl.getBannerByUserId userId={}", userId);
        List<BannerDto> bannerList = bannerRepository.getBannerByUserId(userId);
        log.info("BannerServiceImpl.getBannerByUserId banner size={}, userId={}", bannerList.size(), userId);
        return bannerList;
    }
}

