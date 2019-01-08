package edu.zju.gis.vectorTile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.sql.Connection;

@SpringBootApplication
@MapperScan({"edu.zju.gis.vectorTile.dao", "edu.zju.gis.vectorTile.mapper"})
@EnableCaching
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

