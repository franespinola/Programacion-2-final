package fran.procesador.orden.service.impl;

import fran.procesador.orden.service.OperacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperacionServiceImpl implements OperacionService {

    private final Logger log = LoggerFactory.getLogger(OperacionServiceImpl.class);
}
