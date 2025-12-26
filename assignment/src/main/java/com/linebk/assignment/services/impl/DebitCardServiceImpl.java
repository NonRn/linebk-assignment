package com.linebk.assignment.services.impl;

import com.linebk.assignment.models.dto.DebitCardDto;
import com.linebk.assignment.repositories.DebitCardRepository;
import com.linebk.assignment.services.DebitCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Override
    public List<DebitCardDto> getDebitCardByUserId(String userId) {
        log.info("DebitCardServiceImpl.getDebitCardByUserId userId={}", userId);
        return debitCardRepository.getDebitCardByUserId(userId);
    }
}