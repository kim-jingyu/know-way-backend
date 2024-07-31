package com.knowway.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is the Util class for returning the HttpServletResponse with json type
 */
public class JsonBinderUtil {

  private JsonBinderUtil(){


  }

  public static void setResponseWithJson(HttpServletResponse response, int status,
      Object type) throws IOException {
    response.setContentType("application/json");
    response.setStatus(status);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(getJsonFromType(type));
  }



  private static String getJsonFromType(Object type) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(type);
  }

}
