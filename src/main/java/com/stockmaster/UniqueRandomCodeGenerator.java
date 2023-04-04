package com.stockmaster;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class UniqueRandomCodeGenerator {
  private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int CODE_LENGTH = 10;
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final Set<String> GENERATED_CODES = new HashSet<>();

  public static String generateUniqueRandomCode() {
    String code;
    do {
      code = generateRandomCode();
    } while (GENERATED_CODES.contains(code));
    GENERATED_CODES.add(code);
    return code;
  }

  private static String generateRandomCode() {
    StringBuilder sb = new StringBuilder(CODE_LENGTH);
    for (int i = 0; i < CODE_LENGTH; i++) {
      sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
    }
    return sb.toString();
  }

  public static void setGeneratedCodes(Set<String> generatedCodes) {
    GENERATED_CODES.addAll(generatedCodes);
  }
}
