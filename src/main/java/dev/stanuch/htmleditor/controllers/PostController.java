package dev.stanuch.htmleditor.controllers;

import dev.stanuch.htmleditor.entities.Post;
import dev.stanuch.htmleditor.repositories.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

@Controller
public class PostController {
    private final PostRepository postRepository;

    PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    @GetMapping("/post")
    public String showPost(Post post) {
        return "add-post";
    }

    /*
     @TODO:
       investigate it,
       because @DeleteMapping does not work as expected
       and @GetMapping instead of delete method is required to make it work as expected!
     */
    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
        postRepository.delete(post);

        return "redirect:/index";
    }

    @PostMapping("/addpost")
    public String addPost(@Valid Post post, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-post";
        }

        postRepository.save(post);
        return "redirect:/index";
    }

    @GetMapping("/post/edit/{id}")
    public String updatePost(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user post Id: " + id));

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

        postRepository.save(post);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showPostList(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }
}
