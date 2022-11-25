package org.polytech.covidapi.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

public class MyCustomEtagFilter extends ShallowEtagHeaderFilter {

    private static final String DIRECTIVE_NO_STORE = "no-store";
    private static Set<String> cache = new HashSet<>();
    private static String requestToFilter = "/public";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (preconditionFailed(request)) {
            response.sendError(HttpStatus.PRECONDITION_FAILED.value());
            return;
        }

        super.doFilterInternal(request, response, filterChain);

        updateCache(request, response);
    }

    private boolean preconditionFailed(HttpServletRequest request) {
        String ifMatchEtag = request.getHeader(HttpHeaders.IF_MATCH);
        if (Strings.isNotBlank(ifMatchEtag)) {
            return !cache.contains(ifMatchEtag);
        }
        String ifNoneMatchEtag = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (Strings.isNotBlank(ifNoneMatchEtag)) {
            return !cache.contains(ifNoneMatchEtag);
        }
        return false;
    }

    private void updateCache(HttpServletRequest request, HttpServletResponse response) {
        String ifMatchEtag = request.getHeader(HttpHeaders.IF_MATCH);
        if (Strings.isNotBlank(ifMatchEtag)) {
            cache.remove(ifMatchEtag);
        }

        String ifNoneMatchEtag = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (Strings.isNotBlank(ifNoneMatchEtag)) {
            cache.remove(ifNoneMatchEtag);
        }

        String etag = response.getHeader(HttpHeaders.ETAG);
        if (Strings.isNotBlank(etag)) {
            cache.add(etag);
        }
    }

    @Override
    protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response,
            int responseStatusCode, InputStream inputStream) {

        if (!response.isCommitted() &&
                responseStatusCode >= 200 && responseStatusCode < 300
                && request.getRequestURI().toString().contains(requestToFilter)) {

            String cacheControl = response.getHeader(HttpHeaders.CACHE_CONTROL);
            return (cacheControl == null || !cacheControl.contains(DIRECTIVE_NO_STORE));
        }

        return false;
    }

}
