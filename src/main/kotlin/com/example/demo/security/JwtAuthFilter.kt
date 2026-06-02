package com.example.demo.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader =
            request.getHeader("Authorization")

        if (
            authHeader == null ||
            !authHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        try {

            val token =
                authHeader.substring(7)

            if (
                jwtTokenProvider.validateToken(token)
            ) {

                val userId =
                    jwtTokenProvider.extractUserId(token)

                val userDetails =
                    userDetailsService.loadUserByUsername(
                        userId.toString()
                    )

                val authentication =
                    UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )

                authentication.details =
                    WebAuthenticationDetailsSource()
                        .buildDetails(request)

                SecurityContextHolder
                    .getContext()
                    .authentication = authentication
            }

        } catch (_: Exception) {
        }

        filterChain.doFilter(request, response)
    }
}