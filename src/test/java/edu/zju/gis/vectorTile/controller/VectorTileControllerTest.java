package edu.zju.gis.vectorTile.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VectorTileControllerTest {
    @Autowired
    VectorTileController vectorTileController;

    @Test
    public void getLosList() {
        long currentTimeStamp = System.currentTimeMillis();
        String s = vectorTileController.getLosList("10", "1677", "712");
        long nowTimeStamp = System.currentTimeMillis();
        System.out.println(nowTimeStamp - currentTimeStamp);
        System.out.println(s);
    }
}