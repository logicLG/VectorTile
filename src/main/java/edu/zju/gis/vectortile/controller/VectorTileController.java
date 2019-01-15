package edu.zju.gis.vectortile.controller;


import edu.zju.gis.vectortile.model.Result;
import edu.zju.gis.vectortile.service.VectorTileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
public class VectorTileController {

    @Autowired
    VectorTileService vectorTileService;

    @RequestMapping(value = "/roadLos/{z}/{x}/{y}", method = RequestMethod.GET)
    public Result<Map<String, Integer>> getLosList(@PathVariable String z, @PathVariable String x, @PathVariable String y) {
        Map<String, Integer> tmpMap = vectorTileService.getLOSByTile(z + "_" + x + "_" + y);
        Result<Map<String, Integer>> result = new Result<>();
        if (tmpMap.isEmpty()) {
            result.setStatus("false");
        } else {
            result.setStatus("success");
            result.setBody(tmpMap);
        }
        return result;
    }


    @RequestMapping(value = "/history/{z}/{x}/{y}/{time}", method = RequestMethod.GET)
    public Result<Map<String, Integer>> history(@PathVariable("z") String z, @PathVariable("x") String x
            , @PathVariable("y") String y, @PathVariable("time") long time) {
        Map<String, Integer> tmpMap = vectorTileService.getLOSByTile(z + "_" + x + "_" + y, time);
        Result<Map<String, Integer>> result = new Result<>();
        if (tmpMap.isEmpty()) {
            result.setStatus("false");
        } else {
            result.setStatus("success");
            result.setBody(tmpMap);
        }
        return result;
    }
}
