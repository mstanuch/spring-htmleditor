package dev.stanuch.htmleditor.controllers;

import dev.stanuch.htmleditor.entities.Post;
import dev.stanuch.htmleditor.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;

@Controller
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @GetMapping("/post")
    public String showAddPostForm(Post post) {
        return "add-post";
    }

    @GetMapping("post/preview/{id}")
    public String showPostPreview(@PathVariable("id") long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "preview-post";
    }

    @GetMapping("post/preview/{id}/live")
    public String showLivePostPreview() {
        return "live-preview-post";
    }

    @GetMapping("post/preview/{id}/sse")
    public SseEmitter stream(@PathVariable("id") long id) {
        SseEmitter postPreviewSseEmitter = postService.getPostLivePreviewSseStream(id);
        return  postPreviewSseEmitter;
    }

    @GetMapping(value = "post/download/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> downloadPost(@PathVariable("id") long id) {
        Post post = postService.getPost(id);

        String contentDispositionValue = "attachment; filename=\"" + post.getName() + ".html\"";

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, contentDispositionValue);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(post.getContent());
    }

    /*
     @TODO:
       investigate it,
       because @DeleteMapping does not work as expected
       and @GetMapping instead of delete method is required to make it work as expected!
     */
    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") long id) {
        postService.deletePost(id);

        return "redirect:/index";
    }

    @PostMapping("/addpost")
    public String addPost(@Valid Post post, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-post";
        }

        postService.savePost(post);
        return "redirect:/index";
    }

    @GetMapping("/post/edit/{id}")
    public String updatePost(@PathVariable("id") long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "update-post";
    }

    /*
        @TODO:
          another thing to investigate,
          why POST instead of PUT?!
     */
    @PostMapping("/post/update/{id}")
    public String updatePost(@PathVariable("id") long id, @Valid Post post, BindingResult result, Model model) {
        if (result.hasErrors()) {
            post.setId(id);
            return "update-post";
        }

        postService.savePost(post);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showPostList(Model model) {
        model.addAttribute(
                "posts",
                postService.getAllPosts()
        );
        return "index";
    }
}
