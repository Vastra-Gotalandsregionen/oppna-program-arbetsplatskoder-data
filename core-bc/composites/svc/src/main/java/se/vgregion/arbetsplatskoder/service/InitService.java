package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @author Patrik Björk
 */
@Service
@Transactional
public class InitService {

    @Autowired
    private BatchService batchService;

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @PostConstruct
    public void init() {
        batchService.init();
    }

}
