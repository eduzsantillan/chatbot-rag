package pe.takya.chatpdf;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pe.takya.chatpdf.business.ChatService;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

@SpringBootApplication
public class ChatpdfApplication {

    private final ChatService chatService;

    public ChatpdfApplication(ChatService chatService) {
        this.chatService = chatService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatpdfApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            chatService.startConsoleChat();
        };
    }

}
