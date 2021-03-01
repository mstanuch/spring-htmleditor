package dev.stanuch.htmleditor.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.stanuch.htmleditor.entities.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {}
