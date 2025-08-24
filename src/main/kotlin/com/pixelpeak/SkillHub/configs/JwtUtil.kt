package com.pixelpeak.SkillHub.configs


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JwtUtil {

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    private fun generateToken(claims: Map<String, Any>, expirationTime: Long, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), TimeUnit.MINUTES.toMillis(15), userDetails.username)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), TimeUnit.DAYS.toMillis(7), userDetails.username)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }
}