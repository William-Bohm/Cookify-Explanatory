package RecipeService.models.recipeMetadata;

public enum Cuisine {
    AMERICAN("American"),
    ITALIAN("Italian"),
    MEXICAN("Mexican"),
    CHINESE("Chinese"),
    JAPANESE("Japanese"),
    INDIAN("Indian"),
    FRENCH("French"),
    THAI("Thai"),
    GREEK("Greek"),
    SPANISH("Spanish"),
    MEDITERRANEAN("Mediterranean"),
    CARIBBEAN("Caribbean"),
    KOREAN("Korean"),
    VIETNAMESE("Vietnamese"),
    GERMAN("German"),
    BRAZILIAN("Brazilian"),
    MOROCCAN("Moroccan"),
    RUSSIAN("Russian"),
    ETHIOPIAN("Ethiopian"),
    LEVANTINE("Levantine"),
    AUSTRALIAN("Australian");

    private final String label;

    Cuisine(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
