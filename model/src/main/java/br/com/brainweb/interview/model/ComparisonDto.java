package br.com.brainweb.interview.model;

import java.util.UUID;

public class ComparisonDto {

    private UUID id;

    private String strengthDiff;

    private String agilityDiff;

    private String dexterityDiff;

    private String intelligenceDiff;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStrengthDiff() {
        return strengthDiff;
    }

    public void setStrengthDiff(String strengthDiff) {
        this.strengthDiff = strengthDiff;
    }

    public String getAgilityDiff() {
        return agilityDiff;
    }

    public void setAgilityDiff(String agilityDiff) {
        this.agilityDiff = agilityDiff;
    }

    public String getDexterityDiff() {
        return dexterityDiff;
    }

    public void setDexterityDiff(String dexterityDiff) {
        this.dexterityDiff = dexterityDiff;
    }

    public String getIntelligenceDiff() {
        return intelligenceDiff;
    }

    public void setIntelligenceDiff(String intelligenceDiff) {
        this.intelligenceDiff = intelligenceDiff;
    }
}
