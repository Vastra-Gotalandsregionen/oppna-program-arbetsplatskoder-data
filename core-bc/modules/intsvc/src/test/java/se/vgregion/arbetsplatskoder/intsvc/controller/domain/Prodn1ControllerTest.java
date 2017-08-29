package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.intsvc.test.AppConfigTest;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.service.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfigTest.class)
@Rollback
public class Prodn1ControllerTest {

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private Prodn1Controller prodn1Controller;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {

        User user = new User();
        user.setId("userId1");
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        Prodn1 p1 = new Prodn1();
        p1.setForetagsnamn("AA");
        p1.setId(1);
        p1.setRaderad(false);

        Prodn1 p2 = new Prodn1();
        p2.setForetagsnamn("DD");
        p2.setId(2);
        p2.setRaderad(false);

        Prodn1 p3 = new Prodn1();
        p3.setForetagsnamn("CC");
        p3.setId(3);
        p3.setRaderad(false);

        Prodn1 p4 = new Prodn1();
        p4.setForetagsnamn("bb");
        p4.setId(4);
        p4.setRaderad(false);

        prodn1Repository.save(p1);
        prodn1Repository.save(p2);
        prodn1Repository.save(p3);
        prodn1Repository.save(p4);

        // Inject HttpServletRequest with JWT token
        String token = JwtUtil.createToken("userId1", "My Name", "ADMIN", new HashSet<>());

        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Field field = prodn1Controller.getClass().getDeclaredField("request");
        field.setAccessible(true);
        field.set(prodn1Controller, httpServletRequest);
    }

    @Test
    public void getProdn1s() throws Exception {
        ResponseEntity<List<Prodn1>> response = prodn1Controller.getProdn1s(false);

        assertEquals(200, response.getStatusCode().value());

        List<Prodn1> prodn1s = response.getBody();

        // Assert order
        assertEquals(1, prodn1s.get(0).getId().intValue());
        assertEquals(4, prodn1s.get(1).getId().intValue());
        assertEquals(3, prodn1s.get(2).getId().intValue());
        assertEquals(2, prodn1s.get(3).getId().intValue());
    }

    @Test
    public void getProdn1() throws Exception {
        Prodn1 prodn1 = prodn1Controller.getProdn1(3);

        assertEquals("CC", prodn1.getForetagsnamn());
    }

    @Test
    @Rollback
    public void saveProdn1() throws Exception {
        Prodn1 prodn1 = new Prodn1();
        ResponseEntity<Prodn1> prodn1ResponseEntity = prodn1Controller.saveProdn1(prodn1);

        assertNotNull(prodn1Repository.findOne(prodn1ResponseEntity.getBody().getId()));
    }

}