package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.vgregion.arbetsplatskoder.db.migration.util.BeanMap;
import se.vgregion.arbetsplatskoder.db.service.Crud;
import se.vgregion.arbetsplatskoder.domain.AbstractEntity;

import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/find")
public class FindController {

    @Autowired
    Crud crud;

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> main(@PathVariable String name, @RequestParam Map<String, String> params) {
        try {
            Object bean = Class.forName(AbstractEntity.class.getPackage().getName() + "." + name).newInstance();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() instanceof String) {
                    entry.setValue(ControllerUtil.formatBeforeJpaUsage(entry.getValue()));
                }
            }
            BeanMap bm = new BeanMap(bean);
            System.out.println("Params is " + params);
            bm.putAll(params);
            return crud.find(bean);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //return shop;
    }

}
