package pe.takya.chatpdf.config;

import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangchaingConfig {
    @Value("${ollama.model.name}")
    private String modelName;

    @Value("${ollama.host.url}")
    private String baseUrl;

    @Bean
    ChatLanguageModel chatLanguageModel(){
        return OllamaChatModel.builder()
                .modelName(modelName)
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    EmbeddingModel embeddingModel(){
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }

    @Bean
    EmbeddingStore embeddingStore(){
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    EmbeddingStoreIngestor embeddingStoreIngestor(){
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300,0))
                .embeddingStore(embeddingStore())
                .embeddingModel(embeddingModel())
                .build();
    }
}
