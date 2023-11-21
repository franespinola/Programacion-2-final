package fran.procesador.orden.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fran.procesador.orden.IntegrationTest;
import fran.procesador.orden.domain.Operacion;
import fran.procesador.orden.repository.OperacionRepository;
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
 * Integration tests for the {@link OperacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperacionResourceIT {

    private static final Integer DEFAULT_CLIENTE = 1;
    private static final Integer UPDATED_CLIENTE = 2;

    private static final Integer DEFAULT_ACCION_ID = 1;
    private static final Integer UPDATED_ACCION_ID = 2;

    private static final String DEFAULT_ACCION = "AAAAAAAAAA";
    private static final String UPDATED_ACCION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Instant DEFAULT_FECHA_OPERACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_OPERACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODO = "AAAAAAAAAA";
    private static final String UPDATED_MODO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPERACION_EXITOSA = false;
    private static final Boolean UPDATED_OPERACION_EXITOSA = true;

    private static final String DEFAULT_OPERACION_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OPERACION_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperacionRepository operacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperacionMockMvc;

    private Operacion operacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacion createEntity(EntityManager em) {
        Operacion operacion = new Operacion()
            .cliente(DEFAULT_CLIENTE)
            .accionId(DEFAULT_ACCION_ID)
            .accion(DEFAULT_ACCION)
            .precio(DEFAULT_PRECIO)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaOperacion(DEFAULT_FECHA_OPERACION)
            .modo(DEFAULT_MODO)
            .operacionExitosa(DEFAULT_OPERACION_EXITOSA)
            .operacionObservaciones(DEFAULT_OPERACION_OBSERVACIONES);
        return operacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operacion createUpdatedEntity(EntityManager em) {
        Operacion operacion = new Operacion()
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .operacionExitosa(UPDATED_OPERACION_EXITOSA)
            .operacionObservaciones(UPDATED_OPERACION_OBSERVACIONES);
        return operacion;
    }

    @BeforeEach
    public void initTest() {
        operacion = createEntity(em);
    }

    @Test
    @Transactional
    void createOperacion() throws Exception {
        int databaseSizeBeforeCreate = operacionRepository.findAll().size();
        // Create the Operacion
        restOperacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isCreated());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeCreate + 1);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOperacion.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOperacion.getAccion()).isEqualTo(DEFAULT_ACCION);
        assertThat(testOperacion.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOperacion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOperacion.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOperacion.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testOperacion.getOperacionExitosa()).isEqualTo(DEFAULT_OPERACION_EXITOSA);
        assertThat(testOperacion.getOperacionObservaciones()).isEqualTo(DEFAULT_OPERACION_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createOperacionWithExistingId() throws Exception {
        // Create the Operacion with an existing ID
        operacion.setId(1L);

        int databaseSizeBeforeCreate = operacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOperacions() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        // Get all the operacionList
        restOperacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].cliente").value(hasItem(DEFAULT_CLIENTE)))
            .andExpect(jsonPath("$.[*].accionId").value(hasItem(DEFAULT_ACCION_ID)))
            .andExpect(jsonPath("$.[*].accion").value(hasItem(DEFAULT_ACCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaOperacion").value(hasItem(DEFAULT_FECHA_OPERACION.toString())))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO)))
            .andExpect(jsonPath("$.[*].operacionExitosa").value(hasItem(DEFAULT_OPERACION_EXITOSA.booleanValue())))
            .andExpect(jsonPath("$.[*].operacionObservaciones").value(hasItem(DEFAULT_OPERACION_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getOperacion() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        // Get the operacion
        restOperacionMockMvc
            .perform(get(ENTITY_API_URL_ID, operacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operacion.getId().intValue()))
            .andExpect(jsonPath("$.cliente").value(DEFAULT_CLIENTE))
            .andExpect(jsonPath("$.accionId").value(DEFAULT_ACCION_ID))
            .andExpect(jsonPath("$.accion").value(DEFAULT_ACCION))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaOperacion").value(DEFAULT_FECHA_OPERACION.toString()))
            .andExpect(jsonPath("$.modo").value(DEFAULT_MODO))
            .andExpect(jsonPath("$.operacionExitosa").value(DEFAULT_OPERACION_EXITOSA.booleanValue()))
            .andExpect(jsonPath("$.operacionObservaciones").value(DEFAULT_OPERACION_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingOperacion() throws Exception {
        // Get the operacion
        restOperacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperacion() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

        // Update the operacion
        Operacion updatedOperacion = operacionRepository.findById(operacion.getId()).get();
        // Disconnect from session so that the updates on updatedOperacion are not directly saved in db
        em.detach(updatedOperacion);
        updatedOperacion
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .operacionExitosa(UPDATED_OPERACION_EXITOSA)
            .operacionObservaciones(UPDATED_OPERACION_OBSERVACIONES);

        restOperacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperacion))
            )
            .andExpect(status().isOk());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOperacion.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOperacion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOperacion.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOperacion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOperacion.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOperacion.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOperacion.getOperacionExitosa()).isEqualTo(UPDATED_OPERACION_EXITOSA);
        assertThat(testOperacion.getOperacionObservaciones()).isEqualTo(UPDATED_OPERACION_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperacionWithPatch() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

        // Update the operacion using partial update
        Operacion partialUpdatedOperacion = new Operacion();
        partialUpdatedOperacion.setId(operacion.getId());

        partialUpdatedOperacion.accion(UPDATED_ACCION).operacionObservaciones(UPDATED_OPERACION_OBSERVACIONES);

        restOperacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperacion))
            )
            .andExpect(status().isOk());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getCliente()).isEqualTo(DEFAULT_CLIENTE);
        assertThat(testOperacion.getAccionId()).isEqualTo(DEFAULT_ACCION_ID);
        assertThat(testOperacion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOperacion.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testOperacion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testOperacion.getFechaOperacion()).isEqualTo(DEFAULT_FECHA_OPERACION);
        assertThat(testOperacion.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testOperacion.getOperacionExitosa()).isEqualTo(DEFAULT_OPERACION_EXITOSA);
        assertThat(testOperacion.getOperacionObservaciones()).isEqualTo(UPDATED_OPERACION_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateOperacionWithPatch() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();

        // Update the operacion using partial update
        Operacion partialUpdatedOperacion = new Operacion();
        partialUpdatedOperacion.setId(operacion.getId());

        partialUpdatedOperacion
            .cliente(UPDATED_CLIENTE)
            .accionId(UPDATED_ACCION_ID)
            .accion(UPDATED_ACCION)
            .precio(UPDATED_PRECIO)
            .cantidad(UPDATED_CANTIDAD)
            .fechaOperacion(UPDATED_FECHA_OPERACION)
            .modo(UPDATED_MODO)
            .operacionExitosa(UPDATED_OPERACION_EXITOSA)
            .operacionObservaciones(UPDATED_OPERACION_OBSERVACIONES);

        restOperacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperacion))
            )
            .andExpect(status().isOk());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
        Operacion testOperacion = operacionList.get(operacionList.size() - 1);
        assertThat(testOperacion.getCliente()).isEqualTo(UPDATED_CLIENTE);
        assertThat(testOperacion.getAccionId()).isEqualTo(UPDATED_ACCION_ID);
        assertThat(testOperacion.getAccion()).isEqualTo(UPDATED_ACCION);
        assertThat(testOperacion.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testOperacion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testOperacion.getFechaOperacion()).isEqualTo(UPDATED_FECHA_OPERACION);
        assertThat(testOperacion.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testOperacion.getOperacionExitosa()).isEqualTo(UPDATED_OPERACION_EXITOSA);
        assertThat(testOperacion.getOperacionObservaciones()).isEqualTo(UPDATED_OPERACION_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperacion() throws Exception {
        int databaseSizeBeforeUpdate = operacionRepository.findAll().size();
        operacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operacion in the database
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperacion() throws Exception {
        // Initialize the database
        operacionRepository.saveAndFlush(operacion);

        int databaseSizeBeforeDelete = operacionRepository.findAll().size();

        // Delete the operacion
        restOperacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, operacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operacion> operacionList = operacionRepository.findAll();
        assertThat(operacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
