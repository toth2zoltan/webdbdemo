package com.example.webdbdemo.security;

import com.example.webdbdemo.view.LoginView;
import com.vaadin.flow.server.ServletHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@EnableWebSecurity
@Configuration
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    /**
     * Require login to access internal pages and configure login form.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable() //

                // Register our custom RequestCache that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new HttpSessionRequestCache() {
            /**
             * {@inheritDoc}
             *
             * If the method is considered an internal request from the framework, we skip
             * saving it.
             *
             * @see ::isFrameworkInternalRequest(HttpServletRequest)
             */
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                if (!isFrameworkInternalRequest(request)) {
                    super.saveRequest(request, response);
                }
            }
        })

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all flow internal requests.
                .requestMatchers(SecurityConfiguration::isFrameworkInternalRequest).permitAll() //

                // Allow all requests by logged in users.
                .anyRequest().authenticated() //

                // Configure the login page.
                .and().formLogin().loginPage(LOGIN_URL).permitAll() //
                .loginProcessingUrl(LOGIN_PROCESSING_URL) //
                .failureUrl(LOGIN_FAILURE_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources //
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest //
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // (development mode) static resources //
                "/frontend/**",

                // (development mode) webjars //
                "/webjars/**",

                // (production mode) static resources //
                "/frontend-es5/**", "/frontend-es6/**");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // typical logged in user with some privileges
        UserDetails normalUser =
                User.withUsername("user")
                        .password("{noop}alma")
                        .roles("User")
                        .build();

        // admin user with all privileges
        UserDetails adminUser =
                User.withUsername("admin")
                        .password("{noop}korte")
                        .roles("User", "Admin")
                        .build();

        UserDetails anybody=
                User.withUsername("anybody")
                        .password("{noop}banan")
                        .roles()
                        .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser, anybody);
    }

    /**
     * Tests if the request is an internal framework request. The test consists of
     * checking if the request parameter is present and if its value is consistent
     * with any of the request types know.
     *
     * @param request {@link HttpServletRequest}
     * @return true if is an internal framework request. False otherwise.
     */
    static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(ServletHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    /**
     * Tests if some user is authenticated. As Spring Security always will create an {@link AnonymousAuthenticationToken}
     * we have to ignore those tokens explicitly.
     */
    static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static boolean isAccessGranted(Class<?> securedClass) {
        // Enable the login view
        if (LoginView.class.equals(securedClass)) { //
            return true;
        }
        // If comment this section it will enable anonymous users
        /*
        if(!isUserLoggedIn()) {
            return false;
        }
        */
        // Allow if no roles are required.
        Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
        if (secured == null) {
            return true;
        }

        // lookup needed role in user roles
        List<String> allowedRoles = Arrays.asList(secured.value());
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        return userAuthentication.getAuthorities().stream() //
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);
    }
}