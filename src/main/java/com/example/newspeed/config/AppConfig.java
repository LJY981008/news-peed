package com.example.newspeed.config;

import com.example.newspeed.dto.comment.CommentBaseResponse;
import com.example.newspeed.entity.Comment;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addMappings(new PropertyMap<Comment, CommentBaseResponse>() {
            @Override
            protected void configure() {
                map(source.getUser().getUserName(), destination.getUserName());
                map(source.getPost().getPostId(), destination.getPostId());
            }
        });
        return modelMapper;
    }
}
