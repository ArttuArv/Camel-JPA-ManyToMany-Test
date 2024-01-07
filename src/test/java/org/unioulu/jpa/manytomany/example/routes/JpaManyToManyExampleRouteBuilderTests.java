package org.unioulu.jpa.manytomany.example.routes;

import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unioulu.jpa.manytomany.example.models.Hobby;
import org.unioulu.jpa.manytomany.example.models.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Dictionary;
import java.util.List;

@SuppressWarnings("unchecked")
public class JpaManyToManyExampleRouteBuilderTests extends CamelBlueprintTestSupport {

    final static Logger LOGGER = LoggerFactory.getLogger(JpaManyToManyExampleRouteBuilderTests.class);

    @Test
    void personsJsonFileIsReadSuccesfully() throws Exception {

        MockEndpoint resultMockEndpoint = addResultMockEndpointAsLast("readPersonsFromFile", "result");
        resultMockEndpoint.expectedMessageCount(1);
        resultMockEndpoint.message(0).body().isInstanceOf(List.class);

        startCamelContext();
        template.sendBody("direct:readPersonsFromFile", null);

        resultMockEndpoint.assertIsSatisfied();

        final List<Person> EXPECTED_PERSONS 
            = resultMockEndpoint.getExchanges().get(0).getMessage().getBody(List.class);
        assertEquals(3, EXPECTED_PERSONS.size());
        
    }

    @Test
    void hobbiesJsonFileIsReadSuccesfully() throws Exception {

        MockEndpoint resultMockEndpoint = addResultMockEndpointAsLast("readHobbiesFromFile", "result");
        resultMockEndpoint.expectedMessageCount(1);
        resultMockEndpoint.message(0).body().isInstanceOf(List.class);

        startCamelContext();
        template.sendBody("direct:readHobbiesFromFile", null);

        resultMockEndpoint.assertIsSatisfied();

        final List<Hobby> EXPECTED_HOBBIES 
            = resultMockEndpoint.getExchanges().get(0).getMessage().getBody(List.class);
        final List<Hobby> EXPECTED_PROPERTY 
            = resultMockEndpoint.getExchanges().get(0).getProperty("hobbies", List.class);
        assertEquals(2, EXPECTED_HOBBIES.size());
        assertEquals(2, EXPECTED_PROPERTY.size());
        assertEquals(EXPECTED_HOBBIES, EXPECTED_PROPERTY);
    }

    /**
     * Adds mock with given mockName as last as last endpoint in the route.
     * 
     * @param routeId route id for the route.
     * @param mockName name for the mock e.g result
     * @return MockEndpoint for uri mock:mockName
     * @throws Exception when advice fails.
     */
    MockEndpoint addResultMockEndpointAsLast(String routeId, String mockName) throws Exception {

        String mockUri = "mock:" + mockName;
        AdviceWith.adviceWith(context, routeId, builder -> {

            builder.weaveAddLast()
                .to(mockUri);
        });
        return getMockEndpoint(mockUri);
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/Blueprint.xml";
    }

    @Override
    protected void startCamelContext() throws Exception {

        // Remove helloTimer to prevent it from running in background for tests.
        context.removeRouteDefinition(context.getRouteDefinition("exampleTimer"));
        super.startCamelContext();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }
    
    @Override
    protected String useOverridePropertiesWithConfigAdmin(Dictionary<String, String> props) throws Exception {
        
        // props.put("json.file.path", "src/main/resources/json");
        return "org.unioulu.jpa.manytomany.example.JpaManyToManyExampleCtx";
    }
}
