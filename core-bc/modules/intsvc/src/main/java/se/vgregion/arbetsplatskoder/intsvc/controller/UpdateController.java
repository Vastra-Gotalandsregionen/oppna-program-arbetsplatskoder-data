package se.vgregion.arbetsplatskoder.intsvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.vgregion.arbetsplatskoder.db.migration.util.BeanMap;
import se.vgregion.arbetsplatskoder.db.service.Crud;
import se.vgregion.arbetsplatskoder.domain.AbstractEntity;

import java.util.Map;

@Controller
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    Crud crud;

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Object main(@PathVariable String name, @RequestParam Map<String, String> params) {
        try {
            Object bean = Class.forName(AbstractEntity.class.getPackage().getName() + "." + name).newInstance();
            BeanMap bm = new BeanMap(bean);
            ControllerUtil.formatBeforeJpaUsage(params);
            System.out.println("Params is " + params);
            bm.putAll(params);
            return crud.update(bean);
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
