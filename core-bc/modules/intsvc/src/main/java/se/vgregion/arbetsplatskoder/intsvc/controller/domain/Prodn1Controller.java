package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.repository.DataRepository;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.service.AuthService;
import se.vgregion.arbetsplatskoder.service.MailMan;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static se.vgregion.arbetsplatskoder.intsvc.controller.util.HttpUtil.getUserIdFromRequest;

@Controller
@RequestMapping("/prodn1")
public class Prodn1Controller {

    @Autowired
    private MailMan mailMan;

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private HttpServletRequest request;

    static final String creationMailMessageDefaultTemplate = "En Concise SumNivå 1 skapades.\n" +
            "Hej\n" +
            "\n" +
            "Användaren ${changer} har precis skapat en ny Concise SumNivå 1.\n" +
            "\n" +
            "Den nya posten ser ut så här\n" +
            "Id: ${new.id}\n" +
            "Concise SumNivå: ${new.kortnamn}\n" +
            "Raderad: ${new.raderad}\n" +
            "\n" +
            "\n" +
            "Med vänliga hälsningar\n" +
            "\n" +
            "Arbetsplatskodsregistret (https://arbetsplatskoder.vgregion.se/)\n" +
            "\n" +
            "PS: Du får det här brevet eftersom din användare i det här systemet är konfigurerad att få notiser varje gång en Cocise SumNivå 1 skapas.\n";

    static final String changeMailMessageDefaultTemplate = "En Concise SumNivå 1 uppdaterades.\n" +
            "Hej\n" +
            "\n" +
            "Användaren ${changer} har precis uppdaterat Concise SumNivå 1.\n" +
            "\n" +
            "Den ursprunglia posten var\n" +
            "Id: ${old.id}\n" +
            "Concise SumNivå: ${old.kortnamn}\n" +
            "Raderad: ${old.raderad}\n" +
            "\n" +
            "Den nya ser ut så här\n" +
            "Id: ${new.id}\n" +
            "Concise SumNivå: ${new.kortnamn}\n" +
            "Raderad: ${new.raderad}\n" +
            "\n" +
            "\n" +
            "Med vänliga hälsningar\n" +
            "\n" +
            "Arbetsplatskodsregistret (https://arbetsplatskoder.vgregion.se/)\n" +
            "\n" +
            "PS: Du får det här brevet eftersom din användare i det här systemet är konfigurerad att få notiser varje gång en Cocise SumNivå 1 ändras.\n";

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Prodn1>> getProdn1s(
            @RequestParam(value = "orphan", defaultValue = "false") boolean orphan) {

        String userId = getUserIdFromRequest(this.request);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findOne(userId);

        List<Prodn1> result;
        if (Role.ADMIN.equals(user.getRole())) {

            Sort.Order sortOrder = new Sort.Order(Sort.Direction.ASC, "kortnamn").ignoreCase();

            PageRequest pageable = new PageRequest(0, Integer.MAX_VALUE, new Sort(sortOrder));
            Page<Prodn1> all = prodn1Repository.findAll(pageable);

            result = new ArrayList<>(all.getContent()); // So we don't get an unmodifiable collection.

        } else {
            result = new ArrayList<>(user.getProdn1s());
        }

        if (orphan) {
            Iterator<Prodn1> iterator = result.iterator();

            while (iterator.hasNext()) {
                HashSet<Prodn1> prodn1s = new HashSet<>(Collections.singletonList(iterator.next()));
                List<Data> allByProdn1In = dataRepository.findAllByProdn1In(prodn1s);
                if (allByProdn1In.size() > 0) {
                    iterator.remove();
                }
            }
        }

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn1 getProdn1(@PathVariable(value = "id", required = true) Integer id) {
        return prodn1Repository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("@authService.hasProdn1Access(authentication, #prodn1)")
    @Transactional()
    public ResponseEntity<Prodn1> saveProdn1(@RequestBody Prodn1 prodn1, Authentication authentication) {
        System.out.println("Authentication " + AuthService.getCurrentUser());
        Prodn1 previousVersion = null;
        if (prodn1.getId() == null) {
            // New entity.
            prodn1.setId(Math.abs(new Random().nextInt()));
        } else {
            previousVersion = prodn1Repository.findOne(prodn1.getId());
        }

        if (prodn1.getRaderad() == null) {
            prodn1.setRaderad(false);
        }

        ResponseEntity<Prodn1> result = ResponseEntity.ok(prodn1Repository.save(prodn1));

        List<String> emails = new ArrayList<>();
        for (User user : userRepository.findAllByOrderById()) {
            if (user.getMail() != null && !"".equals(user.getMail()) && Boolean.TRUE.equals(user.getProdnChangeAware())) {
                emails.add(user.getMail());
            }
        }

        if (!emails.isEmpty()) {
            MessageTemplate mt = toUpdateMailText(AuthService.getCurrentUser(), previousVersion, prodn1);
            mailMan.send("arbetsplatskoder-dontreply@vgregion.se", mt.subject, mt.body, emails, null);
        }

        return result;
    }

    @Transactional
    MessageTemplate toUpdateMailText(User changer, Prodn1 beforeChange, Prodn1 change) {

        String userName = changer.getFirstName() + " " + changer.getLastName();
        if (changer.getMail() != null) {
            userName += " (" + changer.getMail() + ")";
        }

        MessageTemplate mt = null;

        if (beforeChange == null) {
            mt = getCreationTemplate();
        } else {
            mt = getChangeTemplate();
        }

        String message = mt.body;

        message = message.replace("${changer}", userName);

        if (beforeChange != null) {
            message = message.replace("${old.id}", String.valueOf(beforeChange.getId()));
            message = message.replace("${old.kortnamn}", String.valueOf(beforeChange.getKortnamn()));
            message = message.replace("${old.raderad}", beforeChange.getRaderad() != null && beforeChange.getRaderad() ? "Ja" : "Nej");
        }

        message = message.replace("${new.id}", String.valueOf(change.getId()));
        message = message.replace("${new.kortnamn}", String.valueOf(change.getKortnamn()));
        message = message.replace("${new.raderad}", change.getRaderad() != null && change.getRaderad() ? "Ja" : "Nej");

        mt.body = message;

        return mt;
    }


    static MessageTemplate getCreationTemplate() {
        try {
            Path path = Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "prodn1.mail.template.creation.txt");
            if (!Files.exists(path)) {
                Files.write(path, creationMailMessageDefaultTemplate.getBytes());
            }
            List<String> lines = Files.readAllLines(path);
            MessageTemplate mt = new MessageTemplate();
            mt.subject = lines.get(0);
            mt.body = String.join("\n", lines.subList(1, lines.size()));
            return mt;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static MessageTemplate getChangeTemplate() {
        try {
            Path path = Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "prodn1.mail.template.change.txt");
            if (!Files.exists(path)) {
                Files.write(path, changeMailMessageDefaultTemplate.getBytes());
            }
            List<String> lines = Files.readAllLines(path);
            MessageTemplate mt = new MessageTemplate();
            mt.subject = lines.get(0);
            mt.body = String.join("\n", lines.subList(1, lines.size()));
            return mt;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class MessageTemplate {

        public String subject;

        public String body;

    }


}
