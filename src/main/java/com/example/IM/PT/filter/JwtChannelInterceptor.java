package com.example.IM.PT.filter;

import com.example.IM.PT.util.JWTutil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTutil jwtService;           // your existing JWT utility
    private final UserDetailsService userDetailsService;

    public JwtChannelInterceptor(JWTutil jwtService,
                                 UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        //  Only process CONNECT frames (authentication happens once at connection)
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Client sends: headers: { Authorization: "Bearer <token>" }
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    String username = jwtService.extractUsername(token);

                    if (username != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtService.validateToken(token)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            //  Set auth on the STOMP session (persists for all subsequent frames)
                            accessor.setUser(authentication);

                            // Also set in SecurityContext for this thread
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                } catch (Exception e) {
                    // Token invalid — throw to reject the connection
                    throw new IllegalArgumentException("Invalid JWT token in STOMP CONNECT", e);
                }
            } else {
                // Reject connections without a token
                throw new IllegalArgumentException("Missing Authorization header in STOMP CONNECT");
            }
        }

        return message;
    }
}