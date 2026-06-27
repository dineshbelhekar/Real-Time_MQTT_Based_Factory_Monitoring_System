package com.example.IM.PT.request;

import com.example.IM.PT.DataCache.ConnectedUsersStore;
import com.example.IM.PT.Entity.User;
import com.example.IM.PT.Responce.UserSessionInfo;
import com.example.IM.PT.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;


@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ConnectedUsersStore store;
    private final UserRepository userRepository;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();

        if (principal == null) {
            return ;
        }

        String username = principal.getName();

        User user = userRepository.findByUsername(username);

        String[] parts = user.getEmployeeId().split("-");

        UserSessionInfo info =  new UserSessionInfo(
                user.getEmployeeId(),
                user.getUsername(),
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                accessor.getSessionId()
        );

        store.add(accessor.getSessionId(), info);

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        String sessionId = event.getSessionId();

        store.getAllUsers().removeIf(
                user -> sessionId.equals(user.getSessionId())
        );
    }
}