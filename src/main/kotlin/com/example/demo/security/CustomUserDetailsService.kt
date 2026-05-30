package com.example.demo.security


import com.example.demo.auth.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val user = userRepository.findById(java.util.UUID.fromString(userId))
            .orElseThrow { UsernameNotFoundException("User not found") }

        return org.springframework.security.core.userdetails.User
            .withUsername(user.id.toString())
            .password(user.password)
            .authorities("ROLE_USER")
            .build()
    }
}