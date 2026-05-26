package com.example.demo.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import java.util.UUID

@Component
class JwtTokenProvider(

    @Value("\${jwt.secret}")
    secret: String,

    @Value("\${jwt.expiration}")
    private val expiration: Long
) {

    private val key: Key = Keys.hmacShaKeyFor(
        secret.toByteArray()
    )

    fun generateToken(userId: UUID): String {

        val now = Date()

        val expiryDate = Date(
            now.time + expiration
        )

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractUserId(token: String): UUID {

        val claims = extractClaims(token)

        return UUID.fromString(claims.subject)
    }

    fun validateToken(token: String): Boolean {

        return try {

            extractClaims(token)

            true

        } catch (e: Exception) {

            false
        }
    }

    private fun extractClaims(token: String): Claims {

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}