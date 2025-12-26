package com.linebk.assignment.services;

import com.linebk.assignment.models.dto.BannerDto;

import java.util.List;

public interface BannerService {

    List<BannerDto> getBannerByUserId(String userId);
}

