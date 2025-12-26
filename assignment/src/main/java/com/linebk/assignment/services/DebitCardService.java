package com.linebk.assignment.services;


import com.linebk.assignment.models.dto.DebitCardDto;

import java.util.List;

public interface DebitCardService {

    List<DebitCardDto> getDebitCardByUserId(String userId);
}

