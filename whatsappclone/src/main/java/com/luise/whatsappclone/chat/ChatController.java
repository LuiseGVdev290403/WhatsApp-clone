package com.luise.whatsappclone.chat;

import com.luise.whatsappclone.commom.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatServices chatServices;

    @PostMapping
    public ResponseEntity<StringResponse> createChat (@RequestParam(name = "sender-id") String senderId,
                                                      @RequestParam(name = "receiver-id") String receiverId) {
        final String chatId = chatServices.createChat(senderId, receiverId);
        StringResponse stringResponse = StringResponse.builder()
                .response(chatId)
                .build();
        return ResponseEntity.ok(stringResponse);
    }
    @GetMapping
    public  ResponseEntity<List<ChatResponse>> getChatsByReceiver (Authentication authentication) {
        return ResponseEntity.ok(chatServices.getChatsByReceiverId(authentication));
    }
}
