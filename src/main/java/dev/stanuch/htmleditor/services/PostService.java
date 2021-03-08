package dev.stanuch.htmleditor.services;

import dev.stanuch.htmleditor.entities.Post;
import dev.stanuch.htmleditor.repositories.PostRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PostService {
    private final PostRepository postRepository;

    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
        return post;
    }

    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        Post post = this.getPost(id);
        postRepository.delete(post);
    }

    public SseEmitter getPostLivePreviewSseStream(Long id) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    Post post = this.getPost(id);

                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(post.getContent(), MediaType.APPLICATION_JSON)
                            .id(String.valueOf(i))
                            .name("Post update");
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
