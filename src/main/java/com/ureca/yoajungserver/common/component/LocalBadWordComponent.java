package com.ureca.yoajungserver.common.component;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.BaseCode;
import com.vane.badwordfiltering.BadWordFiltering;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalBadWordComponent {

    public boolean validate(String question) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();

        boolean badWordCheck = badWordFiltering.check(question);

        if (badWordCheck) {
            return false;
        }

        return true;
    }
}
