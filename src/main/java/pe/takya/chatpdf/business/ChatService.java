package pe.takya.chatpdf.business;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.stereotype.Service;
import pe.takya.chatpdf.config.Assistant;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;
import static pe.takya.chatpdf.console.Console.startConversationWith;

@Service
public class ChatService {

    private final ChatLanguageModel chatLanguageModel;

    private final String systemMessage = """
            You are a chat rag for a peruvian restaurant with a menu as a database knowdledge. 
            You can chat with customers and provide them answer to their questions if there is a query that you dont know just answer you dont have that information. 
            Questions that you would receive are: give all seafood options in the menu. Most affordable or expensive option in the menu. Prices, all related to the menu of the restaurant. 
            Please do not forget to answer complete sentences and also review all menu as If i request options with chicken, you must guarantee that you are give me all possible options with chicken.
            """;


    public ChatService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public void startConsoleChat() {
        try{
            List<Document> document = loadDocuments(toPath("documents/"), glob("*.txt"));
            Assistant assistant = AiServices.builder(Assistant.class)
                    .systemMessageProvider(chatMemoryId -> systemMessage)
                    .chatLanguageModel(chatLanguageModel)
                    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                    .contentRetriever(createContentRetriever(document))
                    .build();
            startConversationWith(assistant);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    ContentRetriever createContentRetriever(List<Document> documents) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents,embeddingStore);
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }


    public static Path toPath(String relativePath) throws URISyntaxException {
        URL fileUrl = ChatService.class.getClassLoader().getResource(relativePath);
        assert fileUrl != null;
        return Paths.get(fileUrl.toURI());
    }

    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }
}
