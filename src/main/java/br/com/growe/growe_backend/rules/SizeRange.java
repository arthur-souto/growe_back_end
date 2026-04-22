package br.com.growe.growe_backend.rules;

public enum SizeRange {
  ONE_TO_10("1-10"),
  ELEVEN_TO_50("11-50"),
  FIFTY_ONE_TO_200("51-200"),
  TWO_HUNDRED_PLUS("201+");

  private final String label;

  SizeRange(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static SizeRange fromLabel(String label) {
    for (SizeRange range : values()) {
      if (range.label.equalsIgnoreCase(label)) {
        return range;
      }
    }
    throw new IllegalArgumentException("Unknown size range: " + label);
  }
}
