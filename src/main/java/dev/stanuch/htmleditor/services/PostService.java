package dev.stanuch.htmleditor.services;

import dev.stanuch.htmleditor.entities.Post;
import dev.stanuch.htmleditor.repositories.PostRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.util.Date;
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
                Date lastPostLastModifiedDateSent = Date.from(Instant.ofEpochSecond(0));
                while (true) {
                    Post post = this.getPost(id);
                    Date postLastModifiedDate = post.getLastModifiedDate();

                    if (postLastModifiedDate.after(lastPostLastModifiedDateSent)) {
                        lastPostLastModifiedDateSent = post.getLastModifiedDate();

                        SseEmitter.SseEventBuilder event = SseEmitter.event()
                                .data(post.getContent(), MediaType.APPLICATION_JSON)
                                .name("post-update");
                        emitter.send(event);
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
