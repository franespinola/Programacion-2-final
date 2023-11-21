package fran.procesador.orden.service.impl;

import fran.procesador.orden.service.OrdenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);
}
