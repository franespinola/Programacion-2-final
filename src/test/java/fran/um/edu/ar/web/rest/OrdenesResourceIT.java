package fran.um.edu.ar.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fran.um.edu.ar.IntegrationTest;
import fran.um.edu.ar.domain.Ordenes;
import fran.um.edu.ar.repository.OrdenesRepository;
import fran.um.edu.ar.service.criteria.OrdenesCriteria;
import fran.um.edu.ar.service.dto.OrdenesDTO;
import fran.um.edu.ar.service.mapper.OrdenesMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrdenesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdenesResourceIT {

    private static final Integer DEFAULT_CLIENTE = 1;
    private static final Integer UPDATED_CLIENTE = 2;
    private static final Integer SMALLER_CLIENTE = 1 - 1;

    private static final Integer DEFAULT_ACCION_ID = 1;
    private static final Integer UPDATED_ACCION_ID = 2;
    private static final Integer SMALLER_ACCION_ID = 1 - 1;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final String DEFAULT_OPERACION = "AAAAAAAAAA";
    private static final String UPDATED_OPERACION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;
    private static final Float SMALLER_PRECIO = 1F - 1F;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final Instant DEFAULT_FECHA_OPERACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_OPERACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODO = "AAAAAAAAAA";
    private static final String UPDATED_MODO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ordenes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdenesRepository ordenesRepository;

    @Autowired
    private OrdenesMapper ordenesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdenesMockMvc;

    private Ordenes ordenes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordenes createEntity(EntityManager em) {
        Ordenes ordenes = new Ordenes()
            .cliente(DEFAULT_CLIENTE)
            .accionId(DEFAULT_ACCION_ID)
            .accion(DEFAULT_ACCION)
            .operacion(DEFAULT_OPERACION)
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaOperacion(DEFAULT_FECHA_OPERACION)
            .modo(DEFAULT_MODO);
        return ordenes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordenes createUpdatedEntity(EntityManager em) {
        Ordenes ordenes = new Ordenes()
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO);
        return ordenes;
    }

    @BeforeEach
    public void initTest() {
        ordenes = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdenes() throws Exception {
        int databaseSizeBeforeCreate = ordenesRepository.findAll().size();
        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);
        restOrdenesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenesDTO)))
            .andExpect(status().isCreated());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeCreate + 1);
        Ordenes testOrdenes = ordenesList.get(ordenesList.size() - 1);
        assertThat(testOrdenes.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrdenes.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOrdenes.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOrdenes.getOperacion()).isEqualTo(DEFAULT_OPERACION);
        assertThat(testOrdenes.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOrdenes.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOrdenes.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOrdenes.getModo()).isEqualTo(DEFAULT_MODO);
    }

    @Test
    @Transactional
    void createOrdenesWithExistingId() throws Exception {
        // Create the Ordenes with an existing ID
        ordenes.setId(1L);
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        int databaseSizeBeforeCreate = ordenesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdenesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdenes() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordenes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].operacion").value(hasItem(DEFAULT_OPERACION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO)));
    }

    @Test
    @Transactional
    void getOrdenes() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get the ordenes
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL_ID, ordenes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordenes.getId().intValue()))
            .andExpect(jsonPath("$.cliente").value(DEFAULT_CLIENTE))
            .andExpect(jsonPath("$.accionId").value(DEFAULT_ACCION_ID))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.operacion").value(DEFAULT_OPERACION))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaOperacion").value(DEFAULT_FECHA_OPERACION.toString()))
            .andExpect(jsonPath("$.modo").value(DEFAULT_MODO));
    }

    @Test
    @Transactional
    void getOrdenesByIdFiltering() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        Long id = ordenes.getId();

        defaultOrdenesShouldBeFound("id.equals=" + id);
        defaultOrdenesShouldNotBeFound("id.notEquals=" + id);

        defaultOrdenesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdenesShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdenesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdenesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente equals to DEFAULT_CLIENTE
        defaultOrdenesShouldBeFound("cliente.equals=" + DEFAULT_CLIENTE);

        // Get all the ordenesList where cliente equals to UPDATED_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.equals=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente in DEFAULT_CLIENTE or UPDATED_CLIENTE
        defaultOrdenesShouldBeFound("cliente.in=" + DEFAULT_CLIENTE + "," + UPDATED_CLIENTE);

        // Get all the ordenesList where cliente equals to UPDATED_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.in=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente is not null
        defaultOrdenesShouldBeFound("cliente.specified=true");

        // Get all the ordenesList where cliente is null
        defaultOrdenesShouldNotBeFound("cliente.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente is greater than or equal to DEFAULT_CLIENTE
        defaultOrdenesShouldBeFound("cliente.greaterThanOrEqual=" + DEFAULT_CLIENTE);

        // Get all the ordenesList where cliente is greater than or equal to UPDATED_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.greaterThanOrEqual=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente is less than or equal to DEFAULT_CLIENTE
        defaultOrdenesShouldBeFound("cliente.lessThanOrEqual=" + DEFAULT_CLIENTE);

        // Get all the ordenesList where cliente is less than or equal to SMALLER_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.lessThanOrEqual=" + SMALLER_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente is less than DEFAULT_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.lessThan=" + DEFAULT_CLIENTE);

        // Get all the ordenesList where cliente is less than UPDATED_CLIENTE
        defaultOrdenesShouldBeFound("cliente.lessThan=" + UPDATED_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByClienteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cliente is greater than DEFAULT_CLIENTE
        defaultOrdenesShouldNotBeFound("cliente.greaterThan=" + DEFAULT_CLIENTE);

        // Get all the ordenesList where cliente is greater than SMALLER_CLIENTE
        defaultOrdenesShouldBeFound("cliente.greaterThan=" + SMALLER_CLIENTE);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId equals to DEFAULT_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.equals=" + DEFAULT_ACCION_ID);

        // Get all the ordenesList where accionId equals to UPDATED_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.equals=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId in DEFAULT_ACCION_ID or UPDATED_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.in=" + DEFAULT_ACCION_ID + "," + UPDATED_ACCION_ID);

        // Get all the ordenesList where accionId equals to UPDATED_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.in=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId is not null
        defaultOrdenesShouldBeFound("accionId.specified=true");

        // Get all the ordenesList where accionId is null
        defaultOrdenesShouldNotBeFound("accionId.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId is greater than or equal to DEFAULT_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.greaterThanOrEqual=" + DEFAULT_ACCION_ID);

        // Get all the ordenesList where accionId is greater than or equal to UPDATED_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.greaterThanOrEqual=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId is less than or equal to DEFAULT_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.lessThanOrEqual=" + DEFAULT_ACCION_ID);

        // Get all the ordenesList where accionId is less than or equal to SMALLER_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.lessThanOrEqual=" + SMALLER_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId is less than DEFAULT_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.lessThan=" + DEFAULT_ACCION_ID);

        // Get all the ordenesList where accionId is less than UPDATED_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.lessThan=" + UPDATED_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accionId is greater than DEFAULT_ACCION_ID
        defaultOrdenesShouldNotBeFound("accionId.greaterThan=" + DEFAULT_ACCION_ID);

        // Get all the ordenesList where accionId is greater than SMALLER_ACCION_ID
        defaultOrdenesShouldBeFound("accionId.greaterThan=" + SMALLER_ACCION_ID);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accion equals to DEFAULT_ACCION
        defaultOrdenesShouldBeFound("accion.equals=" + DEFAULT_ACCION);

        // Get all the ordenesList where accion equals to UPDATED_ACCION
        defaultOrdenesShouldNotBeFound("accion.equals=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accion in DEFAULT_ACCION or UPDATED_ACCION
        defaultOrdenesShouldBeFound("accion.in=" + DEFAULT_ACCION + "," + UPDATED_ACCION);

        // Get all the ordenesList where accion equals to UPDATED_ACCION
        defaultOrdenesShouldNotBeFound("accion.in=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accion is not null
        defaultOrdenesShouldBeFound("accion.specified=true");

        // Get all the ordenesList where accion is null
        defaultOrdenesShouldNotBeFound("accion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accion contains DEFAULT_ACCION
        defaultOrdenesShouldBeFound("accion.contains=" + DEFAULT_ACCION);

        // Get all the ordenesList where accion contains UPDATED_ACCION
        defaultOrdenesShouldNotBeFound("accion.contains=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdenesByAccionNotContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where accion does not contain DEFAULT_ACCION
        defaultOrdenesShouldNotBeFound("accion.doesNotContain=" + DEFAULT_ACCION);

        // Get all the ordenesList where accion does not contain UPDATED_ACCION
        defaultOrdenesShouldBeFound("accion.doesNotContain=" + UPDATED_ACCION);
    }

    @Test
    @Transactional
    void getAllOrdenesByOperacionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where operacion equals to DEFAULT_OPERACION
        defaultOrdenesShouldBeFound("operacion.equals=" + DEFAULT_OPERACION);

        // Get all the ordenesList where operacion equals to UPDATED_OPERACION
        defaultOrdenesShouldNotBeFound("operacion.equals=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByOperacionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where operacion in DEFAULT_OPERACION or UPDATED_OPERACION
        defaultOrdenesShouldBeFound("operacion.in=" + DEFAULT_OPERACION + "," + UPDATED_OPERACION);

        // Get all the ordenesList where operacion equals to UPDATED_OPERACION
        defaultOrdenesShouldNotBeFound("operacion.in=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByOperacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where operacion is not null
        defaultOrdenesShouldBeFound("operacion.specified=true");

        // Get all the ordenesList where operacion is null
        defaultOrdenesShouldNotBeFound("operacion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByOperacionContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where operacion contains DEFAULT_OPERACION
        defaultOrdenesShouldBeFound("operacion.contains=" + DEFAULT_OPERACION);

        // Get all the ordenesList where operacion contains UPDATED_OPERACION
        defaultOrdenesShouldNotBeFound("operacion.contains=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByOperacionNotContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where operacion does not contain DEFAULT_OPERACION
        defaultOrdenesShouldNotBeFound("operacion.doesNotContain=" + DEFAULT_OPERACION);

        // Get all the ordenesList where operacion does not contain UPDATED_OPERACION
        defaultOrdenesShouldBeFound("operacion.doesNotContain=" + UPDATED_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio equals to DEFAULT_PRECIO
        defaultOrdenesShouldBeFound("precio.equals=" + DEFAULT_PRECIO);

        // Get all the ordenesList where precio equals to UPDATED_PRECIO
        defaultOrdenesShouldNotBeFound("precio.equals=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio in DEFAULT_PRECIO or UPDATED_PRECIO
        defaultOrdenesShouldBeFound("precio.in=" + DEFAULT_PRECIO + "," + UPDATED_PRECIO);

        // Get all the ordenesList where precio equals to UPDATED_PRECIO
        defaultOrdenesShouldNotBeFound("precio.in=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio is not null
        defaultOrdenesShouldBeFound("precio.specified=true");

        // Get all the ordenesList where precio is null
        defaultOrdenesShouldNotBeFound("precio.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio is greater than or equal to DEFAULT_PRECIO
        defaultOrdenesShouldBeFound("precio.greaterThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the ordenesList where precio is greater than or equal to UPDATED_PRECIO
        defaultOrdenesShouldNotBeFound("precio.greaterThanOrEqual=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio is less than or equal to DEFAULT_PRECIO
        defaultOrdenesShouldBeFound("precio.lessThanOrEqual=" + DEFAULT_PRECIO);

        // Get all the ordenesList where precio is less than or equal to SMALLER_PRECIO
        defaultOrdenesShouldNotBeFound("precio.lessThanOrEqual=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio is less than DEFAULT_PRECIO
        defaultOrdenesShouldNotBeFound("precio.lessThan=" + DEFAULT_PRECIO);

        // Get all the ordenesList where precio is less than UPDATED_PRECIO
        defaultOrdenesShouldBeFound("precio.lessThan=" + UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByPrecioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where precio is greater than DEFAULT_PRECIO
        defaultOrdenesShouldNotBeFound("precio.greaterThan=" + DEFAULT_PRECIO);

        // Get all the ordenesList where precio is greater than SMALLER_PRECIO
        defaultOrdenesShouldBeFound("precio.greaterThan=" + SMALLER_PRECIO);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad equals to DEFAULT_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the ordenesList where cantidad equals to UPDATED_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the ordenesList where cantidad equals to UPDATED_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad is not null
        defaultOrdenesShouldBeFound("cantidad.specified=true");

        // Get all the ordenesList where cantidad is null
        defaultOrdenesShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ordenesList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the ordenesList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad is less than DEFAULT_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the ordenesList where cantidad is less than UPDATED_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where cantidad is greater than DEFAULT_CANTIDAD
        defaultOrdenesShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the ordenesList where cantidad is greater than SMALLER_CANTIDAD
        defaultOrdenesShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllOrdenesByFechaOperacionIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where fechaOperacion equals to DEFAULT_FECHA_OPERACION
        defaultOrdenesShouldBeFound("fechaOperacion.equals=" + DEFAULT_FECHA_OPERACION);

        // Get all the ordenesList where fechaOperacion equals to UPDATED_FECHA_OPERACION
        defaultOrdenesShouldNotBeFound("fechaOperacion.equals=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByFechaOperacionIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where fechaOperacion in DEFAULT_FECHA_OPERACION or UPDATED_FECHA_OPERACION
        defaultOrdenesShouldBeFound("fechaOperacion.in=" + DEFAULT_FECHA_OPERACION + "," + UPDATED_FECHA_OPERACION);

        // Get all the ordenesList where fechaOperacion equals to UPDATED_FECHA_OPERACION
        defaultOrdenesShouldNotBeFound("fechaOperacion.in=" + UPDATED_FECHA_OPERACION);
    }

    @Test
    @Transactional
    void getAllOrdenesByFechaOperacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where fechaOperacion is not null
        defaultOrdenesShouldBeFound("fechaOperacion.specified=true");

        // Get all the ordenesList where fechaOperacion is null
        defaultOrdenesShouldNotBeFound("fechaOperacion.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByModoIsEqualToSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where modo equals to DEFAULT_MODO
        defaultOrdenesShouldBeFound("modo.equals=" + DEFAULT_MODO);

        // Get all the ordenesList where modo equals to UPDATED_MODO
        defaultOrdenesShouldNotBeFound("modo.equals=" + UPDATED_MODO);
    }

    @Test
    @Transactional
    void getAllOrdenesByModoIsInShouldWork() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where modo in DEFAULT_MODO or UPDATED_MODO
        defaultOrdenesShouldBeFound("modo.in=" + DEFAULT_MODO + "," + UPDATED_MODO);

        // Get all the ordenesList where modo equals to UPDATED_MODO
        defaultOrdenesShouldNotBeFound("modo.in=" + UPDATED_MODO);
    }

    @Test
    @Transactional
    void getAllOrdenesByModoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where modo is not null
        defaultOrdenesShouldBeFound("modo.specified=true");

        // Get all the ordenesList where modo is null
        defaultOrdenesShouldNotBeFound("modo.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdenesByModoContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where modo contains DEFAULT_MODO
        defaultOrdenesShouldBeFound("modo.contains=" + DEFAULT_MODO);

        // Get all the ordenesList where modo contains UPDATED_MODO
        defaultOrdenesShouldNotBeFound("modo.contains=" + UPDATED_MODO);
    }

    @Test
    @Transactional
    void getAllOrdenesByModoNotContainsSomething() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        // Get all the ordenesList where modo does not contain DEFAULT_MODO
        defaultOrdenesShouldNotBeFound("modo.doesNotContain=" + DEFAULT_MODO);

        // Get all the ordenesList where modo does not contain UPDATED_MODO
        defaultOrdenesShouldBeFound("modo.doesNotContain=" + UPDATED_MODO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdenesShouldBeFound(String filter) throws Exception {
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordenes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].operacion").value(hasItem(DEFAULT_OPERACION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO)));

        // Check, that the count call also returns 1
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdenesShouldNotBeFound(String filter) throws Exception {
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdenesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrdenes() throws Exception {
        // Get the ordenes
        restOrdenesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrdenes() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();

        // Update the ordenes
        Ordenes updatedOrdenes = ordenesRepository.findById(ordenes.getId()).get();
        // Disconnect from session so that the updates on updatedOrdenes are not directly saved in db
        em.detach(updatedOrdenes);
        updatedOrdenes
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO);
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(updatedOrdenes);

        restOrdenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
        Ordenes testOrdenes = ordenesList.get(ordenesList.size() - 1);
        assertThat(testOrdenes.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrdenes.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrdenes.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrdenes.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrdenes.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrdenes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrdenes.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrdenes.getModo()).isEqualTo(UPDATED_MODO);
    }

    @Test
    @Transactional
    void putNonExistingOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdenesWithPatch() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();

        // Update the ordenes using partial update
        Ordenes partialUpdatedOrdenes = new Ordenes();
        partialUpdatedOrdenes.setId(ordenes.getId());

        partialUpdatedOrdenes
            .accionId(UPDATED_ACCION_ID)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO);

        restOrdenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdenes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdenes))
            )
            .andExpect(status().isOk());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
        Ordenes testOrdenes = ordenesList.get(ordenesList.size() - 1);
        assertThat(testOrdenes.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOrdenes.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrdenes.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOrdenes.getOperacion()).isEqualTo(DEFAULT_OPERACION);
        assertThat(testOrdenes.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrdenes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrdenes.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrdenes.getModo()).isEqualTo(UPDATED_MODO);
    }

    @Test
    @Transactional
    void fullUpdateOrdenesWithPatch() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();

        // Update the ordenes using partial update
        Ordenes partialUpdatedOrdenes = new Ordenes();
        partialUpdatedOrdenes.setId(ordenes.getId());

        partialUpdatedOrdenes
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .operacion(UPDATED_OPERACION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO);

        restOrdenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdenes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdenes))
            )
            .andExpect(status().isOk());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
        Ordenes testOrdenes = ordenesList.get(ordenesList.size() - 1);
        assertThat(testOrdenes.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOrdenes.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOrdenes.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOrdenes.getOperacion()).isEqualTo(UPDATED_OPERACION);
        assertThat(testOrdenes.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOrdenes.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOrdenes.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOrdenes.getModo()).isEqualTo(UPDATED_MODO);
    }

    @Test
    @Transactional
    void patchNonExistingOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordenesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdenes() throws Exception {
        int databaseSizeBeforeUpdate = ordenesRepository.findAll().size();
        ordenes.setId(count.incrementAndGet());

        // Create the Ordenes
        OrdenesDTO ordenesDTO = ordenesMapper.toDto(ordenes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordenesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordenes in the database
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdenes() throws Exception {
        // Initialize the database
        ordenesRepository.saveAndFlush(ordenes);

        int databaseSizeBeforeDelete = ordenesRepository.findAll().size();

        // Delete the ordenes
        restOrdenesMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordenes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordenes> ordenesList = ordenesRepository.findAll();
        assertThat(ordenesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
