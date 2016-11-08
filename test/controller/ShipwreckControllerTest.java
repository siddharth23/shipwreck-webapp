package controller;

import com.boot.Application;
import com.boot.model.Shipwreck;
import com.boot.repository.ShipwreckRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ShipwreckControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Shipwreck> shipwreckList = new ArrayList<Shipwreck>();

    private Shipwreck shipwreck;

    @Autowired
    private ShipwreckRepository shipwreckRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                //.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.shipwreckRepository.deleteAllInBatch();
        this.shipwreck = shipwreckRepository.save(new Shipwreck(1L,"Ship","Ship Description","not working", 1, -1.0,-33.0,2007));
    }

    @Test
    public void readSingleShip() throws Exception {
        mockMvc.perform(get("/api/v1/shipwrecks/"
                + this.shipwreck.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.shipwreck.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Ship")))
                .andExpect(jsonPath("$.description", is("Ship Description")))
                .andExpect(jsonPath("$.condition", is("not working")))
                .andExpect(jsonPath("$.depth", is(1)))
                .andExpect(jsonPath("$.latitude", is(-1.0)))
                .andExpect(jsonPath("$.longitude", is(-33.0)))
                .andExpect(jsonPath("$.yearDiscovered", is(2007)));
        ;
    }


}
