package com.luise.whatsappclone.message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageServices messageServices;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage (@RequestBody MessageRequest messageRequest) {
        messageServices.saveMessage(messageRequest);
    }
    @PostMapping(value = "/upload-media", consumes = "multipart/form-date")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadMedia(@RequestParam("chat-id") String chatId,
                            @RequestParam("file") MultipartFile file,
                            Authentication authentication) {
        messageServices.uploadMediaMessage(chatId, file, authentication);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessagesToSeen (@RequestParam("chat-id") String chatId, Authentication authentication) {
        messageServices.setMessageToSeen(chatId, authentication);
    }
    @GetMapping("/chat/{chat-id}")
    public ResponseEntity<List<MessageResponse>> getMessages (@PathVariable("chat-id") String chatId) {
        return ResponseEntity.ok(messageServices.findChatMessage(chatId));
    }
}
