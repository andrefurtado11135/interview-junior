package br.com.brainweb.interview.model.enums;

/**
 * Aqui temos algumas raças de super heróis. A API https://superheroapi.com/ pode ou não conter
 * as raças aqui listadas.
 * Em caso de não existirem será necessário adicionar a raça que deseja ao enum.
 */
public enum Race {
    HUMAN("HUMAN"),
    ALIEN("ALIEN"),
    DIVINE("DIVINE"),
    CYBORG("CYBORG");

    private String value;

    Race(String value) {
        this.value = value;
    }

    public static Race getByValue(String value){
        Race race = null;
        for (Race r : values()) {
            if (value.equalsIgnoreCase(r.value)){
                race = r;
            }
        }
        return race;
    }
}
