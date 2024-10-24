package com.chatapp.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;

@Service
public class TranslationService {

    private static final Logger LOG = LogManager.getLogger(TranslationService.class);

    @Value("${google.api.key}")
    private String apiKey;

    private Translate translate;

    @PostConstruct
    public void init() {
        try {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                LOG.error("Google API key is not configured. Please set google.api.key in application.properties");
                return;
            }

            LOG.info("Initializing TranslationService with API key");
            translate = TranslateOptions.newBuilder()
                    .setApiKey(apiKey)
                    .build()
                    .getService();
            LOG.info("TranslationService initialized successfully");
        } catch (Exception e) {
            LOG.error("Failed to initialize TranslationService", e);
        }
    }

    public String translateText(String text, String targetLanguage) {
        if (text == null || text.trim().isEmpty()) {
            LOG.warn("Empty text provided for translation");
            return text;
        }

        if (targetLanguage == null || targetLanguage.trim().isEmpty()) {
            LOG.warn("No target language specified");
            return text;
        }

        if (translate == null) {
            LOG.error("Translation service is not initialized. Check if Google API key is properly configured.");
            return text;
        }

        try {
            Translation translation = translate.translate(
                    text,
                    Translate.TranslateOption.targetLanguage(targetLanguage));

            if (translation == null || translation.getTranslatedText() == null) {
                LOG.error("Translation returned null");
                return text;
            }

            String translatedText = translation.getTranslatedText();
            LOG.info("Successfully translated text to {}", targetLanguage);
            LOG.debug("Original: '{}', Translated: '{}'", text, translatedText);

            return translatedText;
        } catch (Exception e) {
            LOG.error("Translation failed", e);
            return text; // Return original text in case of failure
        }
    }
}