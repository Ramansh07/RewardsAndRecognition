package com.inorg.rewardAndRecognition.config;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private  EmployeeService employeeService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;

    }

    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("token-empty:" );
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {

            String email = jwtTokenProvider.getUsername(token);

            EmployeeDTO employeeDTO = null;

            System.out.println("token-token:"+ email + "\n");

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter((ServletRequest) request, (ServletResponse) response);
    }


}
