package com.knowway.auth.util;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class ClaimsWrapper {

  private final Map<String, Object> claims;

  public Object get(String key){
    return claims.get(key);
  }
}
