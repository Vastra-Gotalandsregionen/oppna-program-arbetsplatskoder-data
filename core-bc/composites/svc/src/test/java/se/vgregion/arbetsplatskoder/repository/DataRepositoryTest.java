package se.vgregion.arbetsplatskoder.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class DataRepositoryTest {

    @Autowired
    private DataRepository dataRepository;

    @Before
    public void setup() {
        Data d1 = new Data();
        d1.setId(1);
        d1.setArbetsplatskodlan("14111");
        
        Data d2 = new Data();
        d2.setId(2);
        d2.setArbetsplatskodlan("14222");
        
        Data d3 = new Data();
        d3.setId(3);
        d3.setArbetsplatskodlan("14333");
        
        Data d4 = new Data();
        d4.setId(4);
        d4.setArbetsplatskodlan("14000");
        
        dataRepository.save(d1);
        dataRepository.save(d2);
        dataRepository.save(d3);
        dataRepository.save(d4);
    }

    @Test
    public void testFindAll() {
        List<Data> all = dataRepository.findAll();

        assertEquals(1, all.size());
    }

    @Test
    public void findHighestBeginningWith() throws Exception {
        Data data = dataRepository.findHighestBeginningWith("14");
        assertEquals(3, data.getId().intValue());

        data = dataRepository.findHighestBeginningWith("142");
        assertEquals(2, data.getId().intValue());
    }

}