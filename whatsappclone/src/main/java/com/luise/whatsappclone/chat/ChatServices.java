package com.luise.whatsappclone.chat;

import com.luise.whatsappclone.user.User;
import com.luise.whatsappclone.user.UserReposity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServices {


    private final ChatRepository chatRepository;
    private final UserReposity userReposity;
    private final ChatMapper mapper;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId (Authentication authentication) {
        final String userId = authentication.getName();
        return chatRepository.findChatsBySenderId(userId).stream()
                .map(c -> mapper.toChatResponse(c, userId))
                .toList();
    }

    public String createChat (String senderId, String receiverId) {
        Optional<Chat> exitingChat = chatRepository.findByReceiverAndSender(senderId, receiverId);
        if (exitingChat.isPresent()) {
            return exitingChat.get().getId();
        }
        User sender = userReposity.findByPublicId(senderId).orElseThrow(() -> new EntityNotFoundException("User with id " + senderId + " not found"));
        User receiver = userReposity.findByPublicId(receiverId).orElseThrow(() -> new EntityNotFoundException("User with id " + receiverId + " not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();
    }
}