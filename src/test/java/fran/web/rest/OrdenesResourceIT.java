package fran.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fran.IntegrationTest;
import fran.domain.Ordenes;
import fran.repository.OrdenesRepository;
import fran.service.dto.OrdenesDTO;
import fran.service.mapper.OrdenesMapper;
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

    private static final Integer DEFAULT_ACCION_ID = 1;
    private static final Integer UPDATED_ACCION_ID = 2;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final String DEFAULT_OPERACION = "AAAAAAAAAA";
    private static final String UPDATED_OPERACION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

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
