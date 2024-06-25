package pe.takya.chatpdf.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.takya.chatpdf.config.Assistant;
import java.util.Scanner;

public class Console {

    final static String welcomeMessageEng = """
            Welcome to the chat with the assistant. You can start the conversation by typing a message. 
            Type 'exit' or 'bye' to finish the conversation.
            """;
    final static String welcomeMessageEsp = """
            Bienvenido al chat con el asistente. Puedes iniciar la conversación escribiendo un mensaje. 
            Escribe 'exit' o 'bye' para finalizar la conversación.
            """;

    private Console() {
        throw new IllegalStateException("Instantiation not allowed");
    }

    public static void startConversationWith(Assistant assistant) {
        Logger log = LoggerFactory.getLogger(Assistant.class);
        log.info(welcomeMessageEng);
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                log.info("==================================================");
                log.info("User: ");
                String userQuery = scanner.nextLine();
                log.info("==================================================");

                if ("exit".equalsIgnoreCase(userQuery) || "bye".equalsIgnoreCase(userQuery)) {
                    break;
                }
                String agentAnswer = assistant.answer(userQuery);
                log.info("==================================================");
                log.info("Assistant: " + agentAnswer);
            }
        }
    }
}
