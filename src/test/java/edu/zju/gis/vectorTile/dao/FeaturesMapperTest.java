package edu.zju.gis.vectorTile.dao;

import edu.zju.gis.vectorTile.entity.Features;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class FeaturesMapperTest {
    @Autowired
    private FeaturesMapper featuresMapper;

    @Test
    public void selectByPrimaryKey() {
        Features features=featuresMapper.selectByPrimaryKey(219);
        System.out.println(features.getLos());
    }
}