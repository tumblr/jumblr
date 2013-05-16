package com.tumblr.jumblr.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tumblr.jumblr.responses.PostDeserializer;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Answer Post tests
 * @author jc
 */
public class AnswerPostTest extends TypeTest {

    private AnswerPost post;

    private String askingUrl = "asking_url";
    private String askingName = "asking_name";
    private String question = "question";
    private String answer = "answer";

    @Before
    public void setup() {
        Map<String, String> flat = new HashMap<String, String>();
        flat.put("type", "answer");
        flat.put("asking_url", askingUrl);
        flat.put("asking_name", askingName);
        flat.put("question", question);
        flat.put("answer", answer);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class, new PostDeserializer()).create();
        post = (AnswerPost) gson.fromJson(flatSerialize(flat), Post.class);
    }

    @Test
    public void getAskingName() {
        assertEquals(askingName, post.getAskingName());
        assertEquals(askingUrl, post.getAskingUrl());
        assertEquals(question, post.getQuestion());
        assertEquals(answer, post.getAnswer());
    }

    @Test(expected=IllegalArgumentException.class)
    public void save() {
        post.save();
    }

}
