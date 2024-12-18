package com.consoleolog.anyaskapiserver.v1.util;

import com.consoleolog.anyaskapiserver.v1.model.dto.UserPrincipal;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommonUtils {

    public static UserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("");
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    public static <T> void printJsonResponse(HttpServletResponse httpResponse, ApiResponse<T> apiResponse) throws IOException {
        Gson gson = new Gson();
        httpResponse.setStatus(apiResponse.getStatus());
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().println(gson.toJson(apiResponse));
    }

}
