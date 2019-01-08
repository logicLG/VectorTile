package edu.zju.gis.vectorTile.controller;


import edu.zju.gis.vectorTile.service.VectorTileService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@CrossOrigin
@Controller
@RequestMapping("/vectortile")
public class VectorTileController {

    @Autowired
    VectorTileService vectorTileService;

    @RequestMapping(value = "/getFeatureLos", method = RequestMethod.GET)
    @ResponseBody
    public String getLosList(String pbfId) {
        JSONObject jsonObject = new JSONObject();
        Map<Integer, Integer> tmpMap = vectorTileService.getFeatureLOSBypbfId(pbfId);
        if (tmpMap.isEmpty()) {
            jsonObject.put("status", "false");
        } else {
            jsonObject.put("status", "success");
            jsonObject.put("losMap", new JSONObject(tmpMap));
        }
        return jsonObject.toString();
    }
}
