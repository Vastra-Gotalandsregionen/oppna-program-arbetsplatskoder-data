package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Vardform;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Verksamhet;
import se.vgregion.arbetsplatskoder.repository.VerksamhetRepository;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/verksamhet")
public class VerksamhetController {

    @Autowired
    private VerksamhetRepository verksamhetRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Verksamhet> getVerksamhets() {
        return verksamhetRepository.findAllByOrderByVerksamhettext();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Verksamhet getVardform(@PathVariable("id") Integer id) {
        return verksamhetRepository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    // todo secure
    public ResponseEntity<Verksamhet> saveVerksamhet(@RequestBody Verksamhet verksamhet) {

        if (verksamhet.getId() == null) {
            // New entity.
            verksamhet.setId(Math.abs(new Random().nextInt()));
        }

        verksamhet.setSsmaTimestamp(new Byte[]{0x00}); // todo What to do with these?

        return ResponseEntity.ok(verksamhetRepository.save(verksamhet));
    }

}
