package com.siemens.nextwork.tag;

import com.siemens.nextwork.tag.model.NextWorkUser;
import com.siemens.nextwork.tag.model.Workstream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class RestTestEnabler {

	protected MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	Optional<Workstream> pws;
	Optional<NextWorkUser> user;
	public abstract void setup();

	@BeforeEach
	public final void data() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		this.setup();
	}
	
	public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
