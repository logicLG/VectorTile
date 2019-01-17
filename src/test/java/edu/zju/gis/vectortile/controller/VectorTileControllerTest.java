package edu.zju.gis.vectortile.controller;

import edu.zju.gis.vectortile.model.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VectorTileControllerTest {
    @Autowired
    VectorTileController vectorTileController;

    @Test
    public void getLosList() {
        long currentTimeStamp = System.currentTimeMillis();
        Result<Map<String, Integer>> s = vectorTileController.getLosList("12", "3385", "1440");
        long nowTimeStamp = System.currentTimeMillis();
        System.out.println(nowTimeStamp - currentTimeStamp);
        System.out.println(s);
    }
}