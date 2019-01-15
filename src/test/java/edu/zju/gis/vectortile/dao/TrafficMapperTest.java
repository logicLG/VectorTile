package edu.zju.gis.vectortile.dao;

import edu.zju.gis.vectortile.entity.Traffic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TrafficMapperTest {
    @Autowired
    private TrafficMapper trafficMapper;

    @Test
    public void selectByPrimaryKey() {
        Traffic traffic = trafficMapper.selectByPrimaryKey(219);
        System.out.println(traffic.getLos());
    }
}