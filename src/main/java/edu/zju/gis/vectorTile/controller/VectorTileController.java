package edu.zju.gis.vectorTile.controller;


import edu.zju.gis.vectorTile.service.VectorTileService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
    public String getLosList(@PathVariable("z") String z, @PathVariable("x") String x, @PathVariable("y") String y) {
        JSONObject jsonObject = new JSONObject();
        Map<Integer, Integer> tmpMap = vectorTileService.getFeatureLOSBypbfId(z + "_" + x + "_" + y);
        if (tmpMap.isEmpty()) {
            jsonObject.put("status", "false");
        } else {
            jsonObject.put("status", "success");
            jsonObject.put("losMap", new JSONObject(tmpMap));
        }
        return jsonObject.toString();
    }
}
