package com.aline.underwritermicroservice.service;

import com.aline.core.dto.response.CardResponse;
import com.aline.core.model.card.Card;
import com.aline.core.util.CardUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardUtility cardUtility;

    public String generateCardNumber(String iin, int cardNumberLength) {
        return cardUtility.generateCardNumber(iin, cardNumberLength);
    }

    public CardResponse mapToResponse(Card card) {
        return cardUtility.mapToResponse(card);
    }

    public boolean validateCardNumber(String cardNumber) {
        return cardUtility.validateCardNumber(cardNumber);
    }
}
