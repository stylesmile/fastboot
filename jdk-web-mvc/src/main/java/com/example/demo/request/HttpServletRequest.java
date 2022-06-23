//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.demo.request;

import java.security.Principal;
import java.util.Enumeration;

public interface HttpServletRequest extends ServletRequest {
    String BASIC_AUTH = "BASIC";
    String FORM_AUTH = "FORM";
    String CLIENT_CERT_AUTH = "CLIENT_CERT";
    String DIGEST_AUTH = "DIGEST";

    String getAuthType();

//    Cookie[] getCookies();

    long getDateHeader(String var1);

    String getHeader(String var1);

    Enumeration<String> getHeaders(String var1);

    Enumeration<String> getHeaderNames();

    int getIntHeader(String var1);

    String getMethod();

    String getPathInfo();

    String getPathTranslated();

    String getContextPath();

    String getQueryString();

    String getRemoteUser();

    boolean isUserInRole(String var1);

    Principal getUserPrincipal();

    String getRequestedSessionId();

    String getRequestURI();

    StringBuffer getRequestURL();

    String getServletPath();

//    HttpSession getSession(boolean var1);
//    HttpSession getSession();

    String changeSessionId();

    boolean isRequestedSessionIdValid();

    boolean isRequestedSessionIdFromCookie();

    boolean isRequestedSessionIdFromURL();

    /** @deprecated */
    boolean isRequestedSessionIdFromUrl();

//    boolean authenticate(HttpServletResponse var1) throws IOException, ServletException;
//
//    void login(String var1, String var2) throws ServletException;
//
//    void logout() throws ServletException;

//    Collection<Part> getParts() throws IOException, ServletException;
//
//    Part getPart(String var1) throws IOException, ServletException;
//
//    <T extends HttpUpgradeHandler> T upgrade(Class<T> var1) throws IOException, ServletException;
}
